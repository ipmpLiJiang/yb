package cc.mrbird.febs.yb.controller;

import cc.mrbird.febs.common.annotation.Log;
import cc.mrbird.febs.common.controller.BaseController;
import cc.mrbird.febs.common.domain.FebsResponse;
import cc.mrbird.febs.common.domain.router.VueRouter;
import cc.mrbird.febs.common.exception.FebsException;
import cc.mrbird.febs.common.domain.QueryRequest;

import cc.mrbird.febs.yb.service.IYbNoticeDataService;
import cc.mrbird.febs.yb.entity.YbNoticeData;

import cc.mrbird.febs.common.utils.FebsUtil;
import cc.mrbird.febs.system.domain.User;
import cc.mrbird.febs.yb.service.IYbReconsiderApplyService;
import com.baomidou.mybatisplus.core.toolkit.StringPool;
import com.wuwenze.poi.ExcelKit;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @author viki
 * @since 2021-03-08
 */
@Slf4j
@Validated
@RestController
@RequestMapping("ybNoticeData")

public class YbNoticeDataController extends BaseController {

    private String message;
    @Autowired
    public IYbNoticeDataService iYbNoticeDataService;



    /**
     * 分页查询数据
     *
     * @param request      分页信息
     * @param ybNoticeData 查询条件
     * @return
     */
    @GetMapping
    @RequiresPermissions("ybNoticeData:view")
    public Map<String, Object> List(QueryRequest request, YbNoticeData ybNoticeData) {
        return getDataTable(this.iYbNoticeDataService.findYbNoticeDatas(request, ybNoticeData));
    }

    @GetMapping("findList")
    public FebsResponse findLists(YbNoticeData ybNoticeData) {
        List<YbNoticeData> list = this.iYbNoticeDataService.findNoticeDataList(ybNoticeData);
        return new FebsResponse().data(list);
    }

    /**
     * 添加
     *
     * @param ybNoticeData
     * @return
     */
    @Log("新增/按钮")
    @PostMapping
    @RequiresPermissions("ybNoticeData:add")
    public void addYbNoticeData(@Valid YbNoticeData ybNoticeData) throws FebsException {
        try {
            User currentUser = FebsUtil.getCurrentUser();
//            ybNoticeData.setCreateUserId(currentUser.getUserId());
            this.iYbNoticeDataService.createYbNoticeData(ybNoticeData);
        } catch (Exception e) {
            message = "新增/按钮失败";
            log.error(message, e);
            throw new FebsException(message);
        }
    }

    /**
     * 修改
     *
     * @param ybNoticeData
     * @return
     */
    @Log("修改")
    @PutMapping
    @RequiresPermissions("ybNoticeData:update")
    public void updateYbNoticeData(@Valid YbNoticeData ybNoticeData) throws FebsException {
        try {
            User currentUser = FebsUtil.getCurrentUser();
//            ybNoticeData.setModifyUserId(currentUser.getUserId());
            this.iYbNoticeDataService.updateYbNoticeData(ybNoticeData);
        } catch (Exception e) {
            message = "修改失败";
            log.error(message, e);
            throw new FebsException(message);
        }
    }


    @Log("删除")
    @DeleteMapping("/{ids}")
    @RequiresPermissions("ybNoticeData:delete")
    public void deleteYbNoticeDatas(@NotBlank(message = "{required}") @PathVariable String ids) throws FebsException {
        try {
            String[] arr_ids = ids.split(StringPool.COMMA);
            this.iYbNoticeDataService.deleteYbNoticeDatas(arr_ids);
        } catch (Exception e) {
            message = "删除失败";
            log.error(message, e);
            throw new FebsException(message);
        }
    }

    @PostMapping("excel")
    @RequiresPermissions("ybNoticeData:export")
    public void export(QueryRequest request, YbNoticeData ybNoticeData, HttpServletResponse response) throws FebsException {
        try {
            List<YbNoticeData> ybNoticeDatas = this.iYbNoticeDataService.findYbNoticeDatas(request, ybNoticeData).getRecords();
            ExcelKit.$Export(YbNoticeData.class, response).downXlsx(ybNoticeDatas, false);
        } catch (Exception e) {
            message = "导出Excel失败";
            log.error(message, e);
            throw new FebsException(message);
        }
    }

    @GetMapping("/{id}")
    public YbNoticeData detail(@NotBlank(message = "{required}") @PathVariable String id) {
        YbNoticeData ybNoticeData = this.iYbNoticeDataService.getById(id);
        return ybNoticeData;
    }
}