package cc.mrbird.febs.yb.controller;

import cc.mrbird.febs.common.annotation.Log;
import cc.mrbird.febs.common.controller.BaseController;
import cc.mrbird.febs.common.domain.FebsResponse;
import cc.mrbird.febs.common.domain.router.VueRouter;
import cc.mrbird.febs.common.exception.FebsException;
import cc.mrbird.febs.common.domain.QueryRequest;

import cc.mrbird.febs.yb.domain.ResponseResultData;
import cc.mrbird.febs.yb.service.IYbReconsiderRepayDataService;
import cc.mrbird.febs.yb.entity.YbReconsiderRepayData;

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
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author viki
 * @since 2020-09-07
 */
@Slf4j
@Validated
@RestController
@RequestMapping("ybReconsiderRepayData")

public class YbReconsiderRepayDataController extends BaseController {

    private String message;
    @Autowired
    public IYbReconsiderRepayDataService iYbReconsiderRepayDataService;


    /**
     * 分页查询数据
     *
     * @param request               分页信息
     * @param ybReconsiderRepayData 查询条件
     * @return
     */
    @GetMapping
    @RequiresPermissions("ybReconsiderRepayData:view")
    public Map<String, Object> List(QueryRequest request, YbReconsiderRepayData ybReconsiderRepayData) {
        return getDataTable(this.iYbReconsiderRepayDataService.findYbReconsiderRepayDatas(request, ybReconsiderRepayData));
    }

    /**
     * 添加
     *
     * @param ybReconsiderRepayData
     * @return
     */
    @Log("新增/按钮")
    @PostMapping
    @RequiresPermissions("ybReconsiderRepayData:add")
    public void addYbReconsiderRepayData(@Valid YbReconsiderRepayData ybReconsiderRepayData) throws FebsException {
        try {
            User currentUser = FebsUtil.getCurrentUser();
            ybReconsiderRepayData.setCreateUserId(currentUser.getUserId());
            this.iYbReconsiderRepayDataService.createYbReconsiderRepayData(ybReconsiderRepayData);
        } catch (Exception e) {
            message = "新增/按钮失败";
            log.error(message, e);
            throw new FebsException(message);
        }
    }

    /**
     * 修改
     *
     * @param ybReconsiderRepayData
     * @return
     */
    @Log("修改")
    @PutMapping
    @RequiresPermissions("ybReconsiderRepayData:update")
    public void updateYbReconsiderRepayData(@Valid YbReconsiderRepayData ybReconsiderRepayData) throws FebsException {
        try {
            User currentUser = FebsUtil.getCurrentUser();
            ybReconsiderRepayData.setModifyUserId(currentUser.getUserId());
            this.iYbReconsiderRepayDataService.updateYbReconsiderRepayData(ybReconsiderRepayData);
        } catch (Exception e) {
            message = "修改失败";
            log.error(message, e);
            throw new FebsException(message);
        }
    }


    @Log("删除")
    @DeleteMapping("/{ids}")
    @RequiresPermissions("ybReconsiderRepayData:delete")
    public void deleteYbReconsiderRepayDatas(@NotBlank(message = "{required}") @PathVariable String ids) throws FebsException {
        try {
            String[] arr_ids = ids.split(StringPool.COMMA);
            this.iYbReconsiderRepayDataService.deleteYbReconsiderRepayDatas(arr_ids);
        } catch (Exception e) {
            message = "删除失败";
            log.error(message, e);
            throw new FebsException(message);
        }
    }

    @PostMapping("excel")
    @RequiresPermissions("ybReconsiderRepayData:export")
    public void export(QueryRequest request, YbReconsiderRepayData ybReconsiderRepayData, HttpServletResponse response) throws FebsException {
        try {
            List<YbReconsiderRepayData> ybReconsiderRepayDatas = this.iYbReconsiderRepayDataService.findYbReconsiderRepayDatas(request, ybReconsiderRepayData).getRecords();
            ExcelKit.$Export(YbReconsiderRepayData.class, response).downXlsx(ybReconsiderRepayDatas, false);
        } catch (Exception e) {
            message = "导出Excel失败";
            log.error(message, e);
            throw new FebsException(message);
        }
    }

    @GetMapping("/{id}")
    public YbReconsiderRepayData detail(@NotBlank(message = "{required}") @PathVariable String id) {
        YbReconsiderRepayData ybReconsiderRepayData = this.iYbReconsiderRepayDataService.getById(id);
        return ybReconsiderRepayData;
    }

    @GetMapping("findGroupBelongDateStr")
    public FebsResponse findGroupBelongDateStrs(String pid){
        List<String> list =  this.iYbReconsiderRepayDataService.findGroupBelongDateStrs(pid);
        Map<String, Object> result = new HashMap<>();
        if (list.size() > 0) {
            result.put("data", list);
            result.put("success", 1);
        } else {
            result.put("data", null);
            result.put("error", "无数据");
            result.put("success", 1);
        }
        return new FebsResponse().data(result);
    }

    @Log("修改")
    @PutMapping("updateRepayData")
    @RequiresPermissions("ybReconsiderRepayData:updateRepayData")
    public FebsResponse updateRepayDatas(@Valid YbReconsiderRepayData ybReconsiderRepayData) {
        ResponseResultData responseResultData = new ResponseResultData();
        responseResultData.setSuccess(0);
        try {
            User currentUser = FebsUtil.getCurrentUser();
            Long uid = currentUser.getUserId();
            String uname = currentUser.getUsername();

            message = this.iYbReconsiderRepayDataService.updateOrderNumberRepayDatas(ybReconsiderRepayData,uid,uname);
            if("ok".equals(message)){
                message = this.iYbReconsiderRepayDataService.updateFieldRepayDatas(ybReconsiderRepayData,uid,uname);
                if("ok".equals(message) || "repay0".equals(message)) {
                    responseResultData.setSuccess(1);
                    message = "还款数据成功.";
                }else {
                    if(message.equals("result0")){
                        message = "模糊匹配，未找到有效的申诉上传数据.";
                    }
                }
            }
            if(message.equals("update0")){
                message = "未找到可更新的数据.";
            }
            if(message.equals("result0")){
                message = "未找到有效的申诉上传数据.";
            }
            if(message.equals("")){
                message = "未找到可变更的数据.";
            }
            responseResultData.setMessage(message);
        } catch (Exception e) {
            message = "还款数据失败.";
            log.error(message, e);
            responseResultData.setMessage(message);
        }
        return new FebsResponse().data(responseResultData);
    }

    @Log("修改")
    @PutMapping("updateHandleRepayData")
    @RequiresPermissions("ybReconsiderRepayData:updateRepayData")
    public FebsResponse updateHandleRepayData(String resultId,String repayId) {
        ResponseResultData responseResultData = new ResponseResultData();
        responseResultData.setSuccess(0);
        try {
            User currentUser = FebsUtil.getCurrentUser();
            Long uid = currentUser.getUserId();
            String uname = currentUser.getUsername();
            message = this.iYbReconsiderRepayDataService.updateHandleRepayDatas(resultId,repayId,uid,uname);
            if("ok".equals(message)){
                responseResultData.setSuccess(1);
                message = "还款数据成功.";
            }
            responseResultData.setMessage(message);
        } catch (Exception e) {
            message = "还款数据失败.";
            log.error(message, e);
            responseResultData.setMessage(message);
        }
        return new FebsResponse().data(responseResultData);
    }
}