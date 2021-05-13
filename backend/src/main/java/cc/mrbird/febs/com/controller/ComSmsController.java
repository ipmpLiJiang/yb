package cc.mrbird.febs.com.controller;

import cc.mrbird.febs.common.annotation.Log;
import cc.mrbird.febs.common.controller.BaseController;
import cc.mrbird.febs.common.domain.FebsResponse;
import cc.mrbird.febs.common.exception.FebsException;
import cc.mrbird.febs.common.domain.QueryRequest;

import cc.mrbird.febs.com.service.IComSmsService;
import cc.mrbird.febs.com.entity.ComSms;

import cc.mrbird.febs.common.utils.FebsUtil;
import cc.mrbird.febs.system.domain.User;
import cc.mrbird.febs.yb.domain.ResponseResult;
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
import java.util.List;
import java.util.Map;

/**
 * @author viki
 * @since 2020-11-13
 */
@Slf4j
@Validated
@RestController
@RequestMapping("comSms")

public class ComSmsController extends BaseController {

    private String message;
    @Autowired
    public IComSmsService iComSmsService;


    /**
     * 分页查询数据
     *
     * @param request 分页信息
     * @param comSms  查询条件
     * @return
     */
    @GetMapping
    @RequiresPermissions("comSms:view")
    public Map<String, Object> List(QueryRequest request, ComSms comSms) {
        return getDataTable(this.iComSmsService.findComSmss(request, comSms));
    }

    /**
     * 添加
     *
     * @param comSms
     * @return
     */
    @Log("新增/按钮")
    @PostMapping
    @RequiresPermissions("comSms:add")
    public void addComSms(@Valid ComSms comSms) throws FebsException {
        try {
            User currentUser = FebsUtil.getCurrentUser();
            comSms.setCreateUserId(currentUser.getUserId());
            this.iComSmsService.createComSms(comSms);
        } catch (Exception e) {
            message = "新增/按钮失败";
            log.error(message, e);
            throw new FebsException(message);
        }
    }

    /**
     * 修改
     *
     * @param comSms
     * @return
     */
    @Log("修改")
    @PutMapping
    @RequiresPermissions("comSms:update")
    public void updateComSms(@Valid ComSms comSms) throws FebsException {
        try {
            User currentUser = FebsUtil.getCurrentUser();
            comSms.setModifyUserId(currentUser.getUserId());
            this.iComSmsService.updateComSms(comSms);
        } catch (Exception e) {
            message = "修改失败";
            log.error(message, e);
            throw new FebsException(message);
        }
    }

    @Log("发送短信")
    @PutMapping("sendSms")
    @RequiresPermissions("comSms:update")
    public FebsResponse sendSmss(ComSms comsms) {
        int success = 0;
        try {
            String msg = this.iComSmsService.sendSms(comsms,null);
            if(msg.equals("ok")){
                success = 1;
            }else{
                message = msg;
            }
        } catch (Exception e) {
            message = "发送短信失败";
            log.error(message, e);
        }

        ResponseResult rr = new ResponseResult();
        rr.setSuccess(success);
        rr.setMessage(message);
        return new FebsResponse().data(rr);
    }

    @Log("删除")
    @DeleteMapping("/{ids}")
    @RequiresPermissions("comSms:delete")
    public void deleteComSmss(@NotBlank(message = "{required}") @PathVariable String ids) throws FebsException {
        try {
            String[] arr_ids = ids.split(StringPool.COMMA);
            this.iComSmsService.deleteComSmss(arr_ids);
        } catch (Exception e) {
            message = "删除失败";
            log.error(message, e);
            throw new FebsException(message);
        }
    }

    @PostMapping("excel")
    @RequiresPermissions("comSms:export")
    public void export(QueryRequest request, ComSms comSms, HttpServletResponse response) throws FebsException {
        try {
            List<ComSms> comSmss = this.iComSmsService.findComSmss(request, comSms).getRecords();
            ExcelKit.$Export(ComSms.class, response).downXlsx(comSmss, false);
        } catch (Exception e) {
            message = "导出Excel失败";
            log.error(message, e);
            throw new FebsException(message);
        }
    }

    @GetMapping("/{id}")
    public ComSms detail(@NotBlank(message = "{required}") @PathVariable String id) {
        ComSms comSms = this.iComSmsService.getById(id);
        return comSms;
    }
}