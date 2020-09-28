package cc.mrbird.febs.yb.controller;

import cc.mrbird.febs.com.controller.DataTypeHelpers;
import cc.mrbird.febs.common.annotation.Log;
import cc.mrbird.febs.common.controller.BaseController;
import cc.mrbird.febs.common.domain.FebsResponse;
import cc.mrbird.febs.common.domain.router.VueRouter;
import cc.mrbird.febs.common.exception.FebsException;
import cc.mrbird.febs.common.domain.QueryRequest;

import cc.mrbird.febs.yb.domain.ResponseResult;
import cc.mrbird.febs.yb.service.IYbAppealResultDeductimplementService;
import cc.mrbird.febs.yb.entity.YbAppealResultDeductimplement;

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
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * @author viki
 * @since 2020-09-24
 */
@Slf4j
@Validated
@RestController
@RequestMapping("ybAppealResultDeductimplement")

public class YbAppealResultDeductimplementController extends BaseController {

    private String message;
    @Autowired
    public IYbAppealResultDeductimplementService iYbAppealResultDeductimplementService;


    /**
     * 分页查询数据
     *
     * @param request                       分页信息
     * @param ybAppealResultDeductimplement 查询条件
     * @return
     */
    @GetMapping
    @RequiresPermissions("ybAppealResultDeductimplement:view")
    public Map<String, Object> List(QueryRequest request, YbAppealResultDeductimplement ybAppealResultDeductimplement) {
        return getDataTable(this.iYbAppealResultDeductimplementService.findYbAppealResultDeductimplements(request, ybAppealResultDeductimplement));
    }

    /**
     * 添加
     *
     * @param ybAppealResultDeductimplement
     * @return
     */
    @Log("新增/按钮")
    @PostMapping
    @RequiresPermissions("ybAppealResultDeductimplement:add")
    public FebsResponse addYbAppealResultDeductimplement(@Valid YbAppealResultDeductimplement ybAppealResultDeductimplement) {
        int success = 0;
        try {
            User currentUser = FebsUtil.getCurrentUser();
            ybAppealResultDeductimplement.setCreateUserId(currentUser.getUserId());
            if(ybAppealResultDeductimplement.getImplementDateStr()!=null) {
                Date implementDate = DataTypeHelpers.stringDateFormat(ybAppealResultDeductimplement.getImplementDateStr() + "-15", "yyyy-MM-dd", true);
                ybAppealResultDeductimplement.setImplementDate(implementDate);
            }
            String msg = this.iYbAppealResultDeductimplementService.createAppealResultDeductimplement(ybAppealResultDeductimplement);
            if(msg.equals("ok")){
                success = 1;
                message = "提交数据成功.";
            }else{
                if(msg.equals("n1")) {
                    message = "提交数据失败.";
                }else{
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
     * @param ybAppealResultDeductimplement
     * @return
     */
    @Log("修改")
    @PutMapping
    @RequiresPermissions("ybAppealResultDeductimplement:update")
    public void updateYbAppealResultDeductimplement(@Valid YbAppealResultDeductimplement ybAppealResultDeductimplement) throws FebsException {
        try {
            User currentUser = FebsUtil.getCurrentUser();
            ybAppealResultDeductimplement.setModifyUserId(currentUser.getUserId());
            this.iYbAppealResultDeductimplementService.updateYbAppealResultDeductimplement(ybAppealResultDeductimplement);
        } catch (Exception e) {
            message = "修改失败";
            log.error(message, e);
            throw new FebsException(message);
        }
    }


    @Log("删除")
    @DeleteMapping("/{ids}")
    @RequiresPermissions("ybAppealResultDeductimplement:delete")
    public void deleteYbAppealResultDeductimplements(@NotBlank(message = "{required}") @PathVariable String ids) throws FebsException {
        try {
            String[] arr_ids = ids.split(StringPool.COMMA);
            this.iYbAppealResultDeductimplementService.deleteYbAppealResultDeductimplements(arr_ids);
        } catch (Exception e) {
            message = "删除失败";
            log.error(message, e);
            throw new FebsException(message);
        }
    }

    @PostMapping("excel")
    @RequiresPermissions("ybAppealResultDeductimplement:export")
    public void export(QueryRequest request, YbAppealResultDeductimplement ybAppealResultDeductimplement, HttpServletResponse response) throws FebsException {
        try {
            List<YbAppealResultDeductimplement> ybAppealResultDeductimplements = this.iYbAppealResultDeductimplementService.findYbAppealResultDeductimplements(request, ybAppealResultDeductimplement).getRecords();
            ExcelKit.$Export(YbAppealResultDeductimplement.class, response).downXlsx(ybAppealResultDeductimplements, false);
        } catch (Exception e) {
            message = "导出Excel失败";
            log.error(message, e);
            throw new FebsException(message);
        }
    }

    @GetMapping("/{id}")
    public YbAppealResultDeductimplement detail(@NotBlank(message = "{required}") @PathVariable String id) {
        YbAppealResultDeductimplement ybAppealResultDeductimplement = this.iYbAppealResultDeductimplementService.getById(id);
        return ybAppealResultDeductimplement;
    }
}