package cc.mrbird.febs.yb.controller;

import cc.mrbird.febs.common.annotation.Log;
import cc.mrbird.febs.common.controller.BaseController;
import cc.mrbird.febs.common.domain.FebsResponse;
import cc.mrbird.febs.common.domain.router.VueRouter;
import cc.mrbird.febs.common.exception.FebsException;
import cc.mrbird.febs.common.domain.QueryRequest;

import cc.mrbird.febs.yb.domain.ResponseResultData;
import cc.mrbird.febs.yb.service.IYbReconsiderResetDataService;
import cc.mrbird.febs.yb.entity.YbReconsiderResetData;

import cc.mrbird.febs.common.utils.FebsUtil;
import cc.mrbird.febs.system.domain.User;
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
 * @since 2020-08-17
 */
@Slf4j
@Validated
@RestController
@RequestMapping("ybReconsiderResetData")

public class YbReconsiderResetDataController extends BaseController {

    private String message;
    @Autowired
    public IYbReconsiderResetDataService iYbReconsiderResetDataService;


    /**
     * 分页查询数据
     *
     * @param request               分页信息
     * @param ybReconsiderResetData 查询条件
     * @return
     */
    @GetMapping
    @RequiresPermissions("ybReconsiderResetData:view")
    public Map<String, Object> List(QueryRequest request, YbReconsiderResetData ybReconsiderResetData) {
        return getDataTable(this.iYbReconsiderResetDataService.findYbReconsiderResetDatas(request, ybReconsiderResetData));
    }

    /**
     * 添加
     *
     * @param ybReconsiderResetData
     * @return
     */
    @Log("新增/按钮")
    @PostMapping
    @RequiresPermissions("ybReconsiderResetData:add")
    public void addYbReconsiderResetData(@Valid YbReconsiderResetData ybReconsiderResetData) throws FebsException {
        try {
            User currentUser = FebsUtil.getCurrentUser();
            ybReconsiderResetData.setCreateUserId(currentUser.getUserId());
            this.iYbReconsiderResetDataService.createYbReconsiderResetData(ybReconsiderResetData);
        } catch (Exception e) {
            message = "新增/按钮失败";
            log.error(message, e);
            throw new FebsException(message);
        }
    }

    /**
     * 修改
     *
     * @param ybReconsiderResetData
     * @return
     */
    @Log("修改")
    @PutMapping
    @RequiresPermissions("ybReconsiderResetData:update")
    public void updateYbReconsiderResetData(@Valid YbReconsiderResetData ybReconsiderResetData) throws FebsException {
        try {
            User currentUser = FebsUtil.getCurrentUser();
            ybReconsiderResetData.setModifyUserId(currentUser.getUserId());
            this.iYbReconsiderResetDataService.updateYbReconsiderResetData(ybReconsiderResetData);
        } catch (Exception e) {
            message = "修改失败";
            log.error(message, e);
            throw new FebsException(message);
        }
    }


    @Log("删除")
    @DeleteMapping("/{ids}")
    @RequiresPermissions("ybReconsiderResetData:delete")
    public void deleteYbReconsiderResetDatas(@NotBlank(message = "{required}") @PathVariable String ids) throws FebsException {
        try {
            String[] arr_ids = ids.split(StringPool.COMMA);
            this.iYbReconsiderResetDataService.deleteYbReconsiderResetDatas(arr_ids);
        } catch (Exception e) {
            message = "删除失败";
            log.error(message, e);
            throw new FebsException(message);
        }
    }

    @PostMapping("excel")
    @RequiresPermissions("ybReconsiderResetData:export")
    public void export(QueryRequest request, YbReconsiderResetData ybReconsiderResetData, HttpServletResponse response) throws FebsException {
        try {
            List<YbReconsiderResetData> ybReconsiderResetDatas = this.iYbReconsiderResetDataService.findYbReconsiderResetDatas(request, ybReconsiderResetData).getRecords();
            ExcelKit.$Export(YbReconsiderResetData.class, response).downXlsx(ybReconsiderResetDatas, false);
        } catch (Exception e) {
            message = "导出Excel失败";
            log.error(message, e);
            throw new FebsException(message);
        }
    }

    @GetMapping("/{id}")
    public YbReconsiderResetData detail(@NotBlank(message = "{required}") @PathVariable String id) {
        YbReconsiderResetData ybReconsiderResetData = this.iYbReconsiderResetDataService.getById(id);
        return ybReconsiderResetData;
    }

    @Log("修改")
    @PutMapping("updateResetData")
    @RequiresPermissions("ybReconsiderResetData:updateResetData")
    public FebsResponse updateAppealResulResetData(String applyDateStr, Integer dataType) {
        ResponseResultData responseResultData = new ResponseResultData();
        responseResultData.setSuccess(0);
        try {
            User currentUser = FebsUtil.getCurrentUser();
            Long uid = currentUser.getUserId();
            String uname = currentUser.getUsername();
            message = this.iYbReconsiderResetDataService.updateResetDatas(applyDateStr,uid,uname,dataType);
            responseResultData.setSuccess(1);
            if("".equals(message)){
                message = "剔除数据成功.";
            }
            if(message.equals("update0")){
                message = "未找到可更新的数据";
            }
            if(message.equals("result0")){
                message = "未找到有效的申诉上传数据";
            }
            responseResultData.setMessage(message);
        } catch (Exception e) {
            message = "剔除数据失败.";
            log.error(message, e);
            responseResultData.setMessage(message);
        }
        return new FebsResponse().data(responseResultData);
    }
    @Log("修改")
    @PutMapping("updateHandleResetData")
    @RequiresPermissions("ybReconsiderResetData:updateResetData")
    public FebsResponse updateHandResetData(String resultId,String resetId) {
        ResponseResultData responseResultData = new ResponseResultData();
        responseResultData.setSuccess(0);
        try {
            User currentUser = FebsUtil.getCurrentUser();
            Long uid = currentUser.getUserId();
            String uname = currentUser.getUsername();
            message = this.iYbReconsiderResetDataService.updateHandleResetDatas(resultId,resetId,uid,uname);
            if("ok".equals(message)){
                responseResultData.setSuccess(1);
                message = "剔除数据成功.";
            }
            responseResultData.setMessage(message);
        } catch (Exception e) {
            message = "剔除数据失败.";
            log.error(message, e);
            responseResultData.setMessage(message);
        }
        return new FebsResponse().data(responseResultData);
    }
}