package cc.mrbird.febs.yb.controller;

import cc.mrbird.febs.common.annotation.Log;
import cc.mrbird.febs.common.controller.BaseController;
import cc.mrbird.febs.common.domain.router.VueRouter;
import cc.mrbird.febs.common.exception.FebsException;
import cc.mrbird.febs.common.domain.QueryRequest;

import cc.mrbird.febs.yb.entity.YbAppealConfireData;
import cc.mrbird.febs.yb.service.IYbAppealSumdeptDataService;
import cc.mrbird.febs.yb.entity.YbAppealSumdeptData;

import cc.mrbird.febs.common.utils.FebsUtil;
import cc.mrbird.febs.system.domain.User;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
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
@RequestMapping("ybAppealSumdeptData")

public class YbAppealSumdeptDataController extends BaseController {

    private String message;
    @Autowired
    public IYbAppealSumdeptDataService iYbAppealSumdeptDataService;


    /**
     * 分页查询数据
     *
     * @param request             分页信息
     * @param ybAppealSumdeptData 查询条件
     * @return
     */
    @GetMapping
    @RequiresPermissions("ybAppealSumdeptData:view")
    public Map<String, Object> List(QueryRequest request, YbAppealSumdeptData ybAppealSumdeptData) {
        return getDataTable(this.iYbAppealSumdeptDataService.findYbAppealSumdeptDatas(request, ybAppealSumdeptData));
    }

    /**
     * 添加
     *
     * @param ybAppealSumdeptData
     * @return
     */
    @Log("新增/按钮")
    @PostMapping
    @RequiresPermissions("ybAppealSumdeptData:add")
    public void addYbAppealSumdeptData(@Valid YbAppealSumdeptData ybAppealSumdeptData) throws FebsException {
        try {
            User currentUser = FebsUtil.getCurrentUser();
//            ybAppealSumdeptData.setCreateUserId(currentUser.getUserId());
            this.iYbAppealSumdeptDataService.createYbAppealSumdeptData(ybAppealSumdeptData);
        } catch (Exception e) {
            message = "新增/按钮失败";
            log.error(message, e);
            throw new FebsException(message);
        }
    }

    /**
     * 修改
     *
     * @param ybAppealSumdeptData
     * @return
     */
    @Log("修改")
    @PutMapping
    @RequiresPermissions("ybAppealSumdeptData:update")
    public void updateYbAppealSumdeptData(@Valid YbAppealSumdeptData ybAppealSumdeptData) throws FebsException {
        try {
            User currentUser = FebsUtil.getCurrentUser();
//            ybAppealSumdeptData.setModifyUserId(currentUser.getUserId());
            this.iYbAppealSumdeptDataService.updateYbAppealSumdeptData(ybAppealSumdeptData);
        } catch (Exception e) {
            message = "修改失败";
            log.error(message, e);
            throw new FebsException(message);
        }
    }


    @Log("删除")
    @DeleteMapping("/{ids}")
    @RequiresPermissions("ybAppealSumdeptData:delete")
    public void deleteYbAppealSumdeptDatas(@NotBlank(message = "{required}") @PathVariable String ids) throws FebsException {
        try {
            String[] arr_ids = ids.split(StringPool.COMMA);
            this.iYbAppealSumdeptDataService.deleteYbAppealSumdeptDatas(arr_ids);
        } catch (Exception e) {
            message = "删除失败";
            log.error(message, e);
            throw new FebsException(message);
        }
    }

    @PostMapping("excel")
    @RequiresPermissions("ybAppealSumdeptData:export")
    public void export(QueryRequest request, YbAppealSumdeptData ybAppealSumdeptData, HttpServletResponse response) throws FebsException {
        try {
            List<YbAppealSumdeptData> ybAppealSumdeptDatas = this.iYbAppealSumdeptDataService.findYbAppealSumdeptDatas(request, ybAppealSumdeptData).getRecords();
            ExcelKit.$Export(YbAppealSumdeptData.class, response).downXlsx(ybAppealSumdeptDatas, false);
        } catch (Exception e) {
            message = "导出Excel失败";
            log.error(message, e);
            throw new FebsException(message);
        }
    }

    @GetMapping("/{id}")
    public YbAppealSumdeptData detail(@NotBlank(message = "{required}") @PathVariable String id) {
        YbAppealSumdeptData ybAppealSumdeptData = this.iYbAppealSumdeptDataService.getById(id);
        return ybAppealSumdeptData;
    }


}