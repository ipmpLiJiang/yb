package cc.mrbird.febs.yb.controller;

import cc.mrbird.febs.common.annotation.Log;
import cc.mrbird.febs.common.controller.BaseController;
import cc.mrbird.febs.common.domain.FebsResponse;
import cc.mrbird.febs.common.domain.router.VueRouter;
import cc.mrbird.febs.common.exception.FebsException;
import cc.mrbird.febs.common.domain.QueryRequest;

import cc.mrbird.febs.yb.service.IYbAppealConfireDataService;
import cc.mrbird.febs.yb.entity.YbAppealConfireData;

import cc.mrbird.febs.common.utils.FebsUtil;
import cc.mrbird.febs.system.domain.User;
import com.baomidou.mybatisplus.core.toolkit.StringPool;
import com.wuwenze.poi.ExcelKit;
import io.swagger.models.auth.In;
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
 * @since 2021-01-11
 */
@Slf4j
@Validated
@RestController
@RequestMapping("ybAppealConfireData")

public class YbAppealConfireDataController extends BaseController {

    private String message;
    @Autowired
    public IYbAppealConfireDataService iYbAppealConfireDataService;


    /**
     * 分页查询数据
     *
     * @param request             分页信息
     * @param ybAppealConfireData 查询条件
     * @return
     */
    @GetMapping
    @RequiresPermissions("ybAppealConfireData:view")
    public Map<String, Object> List(QueryRequest request, YbAppealConfireData ybAppealConfireData) {
        return getDataTable(this.iYbAppealConfireDataService.findYbAppealConfireDatas(request, ybAppealConfireData));
    }

    /**
     * 添加
     *
     * @param ybAppealConfireData
     * @return
     */
    @Log("新增/按钮")
    @PostMapping
    @RequiresPermissions("ybAppealConfireData:add")
    public void addYbAppealConfireData(@Valid YbAppealConfireData ybAppealConfireData) throws FebsException {
        try {
//            User currentUser = FebsUtil.getCurrentUser();
//            ybAppealConfireData.setCreateUserId(currentUser.getUserId());
            this.iYbAppealConfireDataService.createYbAppealConfireData(ybAppealConfireData);
        } catch (Exception e) {
            message = "新增/按钮失败";
            log.error(message, e);
            throw new FebsException(message);
        }
    }

    /**
     * 修改
     *
     * @param ybAppealConfireData
     * @return
     */
    @Log("修改")
    @PutMapping
    @RequiresPermissions("ybAppealConfireData:update")
    public void updateYbAppealConfireData(@Valid YbAppealConfireData ybAppealConfireData) throws FebsException {
        try {
//            User currentUser = FebsUtil.getCurrentUser();
//            ybAppealConfireData.setModifyUserId(currentUser.getUserId());
            this.iYbAppealConfireDataService.updateYbAppealConfireData(ybAppealConfireData);
        } catch (Exception e) {
            message = "修改失败";
            log.error(message, e);
            throw new FebsException(message);
        }
    }


    @Log("删除")
    @DeleteMapping("/{ids}")
    @RequiresPermissions("ybAppealConfireData:delete")
    public void deleteYbAppealConfireDatas(@NotBlank(message = "{required}") @PathVariable String ids) throws FebsException {
        try {
            String[] arr_ids = ids.split(StringPool.COMMA);
            this.iYbAppealConfireDataService.deleteYbAppealConfireDatas(arr_ids);
        } catch (Exception e) {
            message = "删除失败";
            log.error(message, e);
            throw new FebsException(message);
        }
    }

    @PostMapping("excel")
    @RequiresPermissions("ybAppealConfireData:export")
    public void export(QueryRequest request, YbAppealConfireData ybAppealConfireData, HttpServletResponse response) throws FebsException {
        try {
            List<YbAppealConfireData> ybAppealConfireDatas = this.iYbAppealConfireDataService.findYbAppealConfireDatas(request, ybAppealConfireData).getRecords();
            ExcelKit.$Export(YbAppealConfireData.class, response).downXlsx(ybAppealConfireDatas, false);
        } catch (Exception e) {
            message = "导出Excel失败";
            log.error(message, e);
            throw new FebsException(message);
        }
    }

    @GetMapping("/{id}")
    public YbAppealConfireData detail(@NotBlank(message = "{required}") @PathVariable String id) {
        YbAppealConfireData ybAppealConfireData = this.iYbAppealConfireDataService.getById(id);
        return ybAppealConfireData;
    }

    @GetMapping("findAppealConfireDataList")
    public FebsResponse findAppealConfireDataLists(Integer areaType) {
        List<YbAppealConfireData> list = new ArrayList<>();
        try {
            User currentUser = FebsUtil.getCurrentUser();
            list = this.iYbAppealConfireDataService.findAppealConfireDataByInDoctorCodeList(currentUser.getUsername(),areaType);

        } catch (Exception e) {
            log.error("获取医生失败", e);
        }

        return new FebsResponse().data(list);
    }
}