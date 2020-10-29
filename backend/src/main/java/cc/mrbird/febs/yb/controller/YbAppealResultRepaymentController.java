package cc.mrbird.febs.yb.controller;

import cc.mrbird.febs.com.controller.DataTypeHelpers;
import cc.mrbird.febs.common.annotation.Log;
import cc.mrbird.febs.common.controller.BaseController;
import cc.mrbird.febs.common.domain.FebsResponse;
import cc.mrbird.febs.common.domain.router.VueRouter;
import cc.mrbird.febs.common.exception.FebsException;
import cc.mrbird.febs.common.domain.QueryRequest;

import cc.mrbird.febs.yb.domain.ResponseResult;
import cc.mrbird.febs.yb.entity.YbAppealResultDeductimplement;
import cc.mrbird.febs.yb.entity.YbReconsiderVerify;
import cc.mrbird.febs.yb.service.IYbAppealResultRepaymentService;
import cc.mrbird.febs.yb.entity.YbAppealResultRepayment;

import cc.mrbird.febs.common.utils.FebsUtil;
import cc.mrbird.febs.system.domain.User;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
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
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * @author viki
 * @since 2020-10-09
 */
@Slf4j
@Validated
@RestController
@RequestMapping("ybAppealResultRepayment")

public class YbAppealResultRepaymentController extends BaseController {

    private String message;
    @Autowired
    public IYbAppealResultRepaymentService iYbAppealResultRepaymentService;


    /**
     * 分页查询数据
     *
     * @param request                 分页信息
     * @param ybAppealResultRepayment 查询条件
     * @return
     */
    @GetMapping
    @RequiresPermissions("ybAppealResultRepayment:view")
    public Map<String, Object> List(QueryRequest request, YbAppealResultRepayment ybAppealResultRepayment) {
        return getDataTable(this.iYbAppealResultRepaymentService.findYbAppealResultRepayments(request, ybAppealResultRepayment));
    }

    /**
     * 添加
     *
     * @param ybAppealResultRepayment
     * @return
     */
    @Log("新增/按钮")
    @PostMapping
    @RequiresPermissions("ybAppealResultRepayment:add")
    public FebsResponse addYbAppealResultRepayment(@Valid YbAppealResultRepayment ybAppealResultRepayment) {
        int success = 0;
        try {
            User currentUser = FebsUtil.getCurrentUser();
            ybAppealResultRepayment.setCreateUserId(currentUser.getUserId());
            if (ybAppealResultRepayment.getApplyDateStr() != null) {
                Date applyDate = DataTypeHelpers.stringDateFormat(ybAppealResultRepayment.getApplyDateStr() + "-15", "yyyy-MM-dd", true);
                ybAppealResultRepayment.setApplyDate(applyDate);
            }
            String msg = this.iYbAppealResultRepaymentService.createAppealResultRepayment(ybAppealResultRepayment);
            if (msg.equals("ok")) {
                success = 1;
                message = "提交数据成功.";
            } else {
                if (msg.equals("n1")) {
                    message = "提交数据失败.";
                } else {
                    message = "提交失败,该条数据已提交过扣款落实.";
                }
            }
        } catch (Exception e) {
            message = "提交数据失败.";
            log.error(message, e);
        }
        ResponseResult rr = new ResponseResult();
        rr.setSuccess(success);
        rr.setMessage(message);

        return new FebsResponse().data(rr);
    }

    /**
     * 修改
     *
     * @param ybAppealResultRepayment
     * @return
     */
    @Log("修改")
    @PutMapping
    @RequiresPermissions("ybAppealResultRepayment:update")
    public void updateYbAppealResultRepayment(@Valid YbAppealResultRepayment ybAppealResultRepayment) throws FebsException {
        try {
            User currentUser = FebsUtil.getCurrentUser();
            ybAppealResultRepayment.setModifyUserId(currentUser.getUserId());
            this.iYbAppealResultRepaymentService.updateYbAppealResultRepayment(ybAppealResultRepayment);
        } catch (Exception e) {
            message = "修改失败";
            log.error(message, e);
            throw new FebsException(message);
        }
    }

    @Log("修改")
    @PutMapping("createRepayment")
    @RequiresPermissions("ybAppealResultRepayment:add")
    public void createAppealResultRepayment(String dataJson) throws FebsException {
        try {
            User currentUser = FebsUtil.getCurrentUser();
            Long uid = currentUser.getUserId();

            List<YbAppealResultRepayment> list = JSON.parseObject(dataJson, new TypeReference<List<YbAppealResultRepayment>>() {
            });

            this.iYbAppealResultRepaymentService.batchCreateAppealResultRepaymentNull(list,uid);
        } catch (Exception e) {
            message = "还款失败";
            log.error(message, e);
            throw new FebsException(message);
        }
    }

    @Log("修改")
    @PutMapping("createRepaymentA")
    @RequiresPermissions("ybAppealResultRepayment:add")
    public void createAppealResultRepaymentA(String applyDateStr) throws FebsException {
        try {
            User currentUser = FebsUtil.getCurrentUser();
            Long uid = currentUser.getUserId();

            this.iYbAppealResultRepaymentService.batchCreateAppealResultRepaymentNull(applyDateStr,uid);
        } catch (Exception e) {
            message = "还款失败";
            log.error(message, e);
            throw new FebsException(message);
        }
    }


    @Log("删除")
    @DeleteMapping("/{ids}")
    @RequiresPermissions("ybAppealResultRepayment:delete")
    public void deleteYbAppealResultRepayments(@NotBlank(message = "{required}") @PathVariable String ids) throws FebsException {
        try {
            String[] arr_ids = ids.split(StringPool.COMMA);
            this.iYbAppealResultRepaymentService.deleteYbAppealResultRepayments(arr_ids);
        } catch (Exception e) {
            message = "删除失败";
            log.error(message, e);
            throw new FebsException(message);
        }
    }

    @PostMapping("excel")
    @RequiresPermissions("ybAppealResultRepayment:export")
    public void export(QueryRequest request, YbAppealResultRepayment ybAppealResultRepayment, HttpServletResponse response) throws FebsException {
        try {
            List<YbAppealResultRepayment> ybAppealResultRepayments = this.iYbAppealResultRepaymentService.findYbAppealResultRepayments(request, ybAppealResultRepayment).getRecords();
            ExcelKit.$Export(YbAppealResultRepayment.class, response).downXlsx(ybAppealResultRepayments, false);
        } catch (Exception e) {
            message = "导出Excel失败";
            log.error(message, e);
            throw new FebsException(message);
        }
    }

    @GetMapping("/{id}")
    public YbAppealResultRepayment detail(@NotBlank(message = "{required}") @PathVariable String id) {
        YbAppealResultRepayment ybAppealResultRepayment = this.iYbAppealResultRepaymentService.getById(id);
        return ybAppealResultRepayment;
    }
}