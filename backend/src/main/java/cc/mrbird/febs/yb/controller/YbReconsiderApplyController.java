package cc.mrbird.febs.yb.controller;

import cc.mrbird.febs.common.annotation.Log;
import cc.mrbird.febs.common.controller.BaseController;
import cc.mrbird.febs.common.domain.FebsResponse;
import cc.mrbird.febs.common.domain.router.VueRouter;
import cc.mrbird.febs.common.exception.FebsException;
import cc.mrbird.febs.common.domain.QueryRequest;

import cc.mrbird.febs.yb.domain.ResponseResult;
import cc.mrbird.febs.yb.service.IYbReconsiderApplyService;
import cc.mrbird.febs.yb.entity.YbReconsiderApply;

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
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @author viki
 * @since 2020-07-23
 */
@Slf4j
@Validated
@RestController
@RequestMapping("ybReconsiderApply")

public class YbReconsiderApplyController extends BaseController {

    private String message;
    @Autowired
    public IYbReconsiderApplyService iYbReconsiderApplyService;


    /**
     * 分页查询数据
     *
     * @param request           分页信息
     * @param ybReconsiderApply 查询条件
     * @return
     */
    @GetMapping
    @RequiresPermissions("ybReconsiderApply:view")
    public Map<String, Object> List(QueryRequest request, YbReconsiderApply ybReconsiderApply) {
        return getDataTable(this.iYbReconsiderApplyService.findYbReconsiderApplys(request, ybReconsiderApply));
    }

    /**
     * 添加
     *
     * @param ybReconsiderApply
     * @return
     */
    @Log("新增/按钮")
    @PostMapping
    @RequiresPermissions("ybReconsiderApply:add")
    public void addYbReconsiderApply(@Valid YbReconsiderApply ybReconsiderApply) throws FebsException {
        try {
            User currentUser = FebsUtil.getCurrentUser();
            ybReconsiderApply.setCreateUserId(currentUser.getUserId());
            ybReconsiderApply.setOperatorId(currentUser.getUserId());
            ybReconsiderApply.setOperatorName(currentUser.getUsername());
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM");
            String appDateStr = sdf.format(ybReconsiderApply.getApplyDate());
            ybReconsiderApply.setApplyDateStr(appDateStr);
            this.iYbReconsiderApplyService.createYbReconsiderApply(ybReconsiderApply);
        } catch (Exception e) {
            message = "新增/按钮失败";
            log.error(message, e);
            throw new FebsException(message);
        }
    }


    @Log("创建Job")
    @PutMapping("startJob")
    @RequiresPermissions("ybReconsiderApply:add")
    public FebsResponse cStartJob(String applyDateStr) {
        int success = 0;
        try {
            String msg = this.iYbReconsiderApplyService.createJobState(applyDateStr);
            if(msg.equals("")){
                success = 1;
            }else{
                message = msg;
            }
        } catch (Exception e) {
            message = "创建Job失败";
            log.error(message, e);
        }

        ResponseResult rr = new ResponseResult();
        rr.setSuccess(success);
        rr.setMessage(message);
        return new FebsResponse().data(rr);
    }

    @Log("新增/按钮")
    @PostMapping("addYbReconsiderApplyCheck")
    @RequiresPermissions("ybReconsiderApply:add")
    public FebsResponse addYbReconsiderApplyCheck(@Valid YbReconsiderApply ybReconsiderApply) {
        int success = 0;
        try {
            User currentUser = FebsUtil.getCurrentUser();
            ybReconsiderApply.setCreateUserId(currentUser.getUserId());
            ybReconsiderApply.setOperatorId(currentUser.getUserId());
            ybReconsiderApply.setOperatorName(currentUser.getUsername());
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM");
            String appDateStr = sdf.format(ybReconsiderApply.getApplyDate());
            ybReconsiderApply.setApplyDateStr(appDateStr);
            message = this.iYbReconsiderApplyService.createReconsiderApplyCheck(ybReconsiderApply);
            if(message.equals("ok")){
                success = 1;
                message = "新增成功.";
            }
        } catch (Exception e) {
            message = "新增/按钮失败";
            log.error(message, e);
        }
        ResponseResult rr =new ResponseResult();
        rr.setSuccess(success);
        rr.setMessage(message);
        return new FebsResponse().data(rr);
    }

    /**
     * 修改
     *
     * @param ybReconsiderApply
     * @return
     */
    @Log("修改")
    @PutMapping
    @RequiresPermissions("ybReconsiderApply:update")
    public FebsResponse updateYbReconsiderApply(@Valid YbReconsiderApply ybReconsiderApply,boolean isUpOverdue) {
        int success = 0;
        try {
            User currentUser = FebsUtil.getCurrentUser();
            ybReconsiderApply.setModifyUserId(currentUser.getUserId());
            ybReconsiderApply.setOperatorId(currentUser.getUserId());
            ybReconsiderApply.setOperatorName(currentUser.getUsername());
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM");
            String appDateStr = sdf.format(ybReconsiderApply.getApplyDate());
            ybReconsiderApply.setApplyDateStr(appDateStr);
            message = this.iYbReconsiderApplyService.updateYbReconsiderApply(ybReconsiderApply,isUpOverdue);
            if(message.equals("") || message.equals("ok")){
                message = "ok";
            }else if(message.equals("date")){
                message = "当前结束日期应大于之前结束日期";
            }else if(message.equals("nostate")){
                message = "当前状态无法进行未申诉更新";
            }
            success = 1;
        } catch (Exception e) {
            message = "修改失败";
            log.error(message, e);
            //throw new FebsException(message);
        }
        ResponseResult rr= new ResponseResult();
        rr.setSuccess(success);
        rr.setMessage(message);
        return new FebsResponse().data(rr);
    }


    @Log("删除")
    @DeleteMapping("/{ids}")
    @RequiresPermissions("ybReconsiderApply:delete")
    public void deleteYbReconsiderApplys(@NotBlank(message = "{required}") @PathVariable String ids) throws FebsException {
        try {
            String[] arr_ids = ids.split(StringPool.COMMA);
            this.iYbReconsiderApplyService.deleteBatchStateIdsYbReconsiderApplys(arr_ids, 1);
        } catch (Exception e) {
            message = "删除失败";
            log.error(message, e);
            throw new FebsException(message);
        }
    }

    @PostMapping("excel")
    @RequiresPermissions("ybReconsiderApply:export")
    public void export(QueryRequest request, YbReconsiderApply ybReconsiderApply, HttpServletResponse response) throws FebsException {
        try {
            List<YbReconsiderApply> ybReconsiderApplys = this.iYbReconsiderApplyService.findYbReconsiderApplys(request, ybReconsiderApply).getRecords();
            ExcelKit.$Export(YbReconsiderApply.class, response).downXlsx(ybReconsiderApplys, false);
        } catch (Exception e) {
            message = "导出Excel失败";
            log.error(message, e);
            throw new FebsException(message);
        }
    }

    @GetMapping("/{id}")
    public YbReconsiderApply detail(@NotBlank(message = "{required}") @PathVariable String id) {
        YbReconsiderApply ybReconsiderApply = this.iYbReconsiderApplyService.getById(id);
        return ybReconsiderApply;
    }

    @Log("修改")
    @PutMapping("updateReconsiderApplyState")
    @RequiresPermissions("ybReconsiderApply:update")
    public FebsResponse updateReconsiderApplyStates(@Valid YbReconsiderApply ybReconsiderApply){
        int success = 0;
        try {
            String applyDateStr = ybReconsiderApply.getApplyDateStr();
            Integer state = ybReconsiderApply.getState();
            boolean bl = this.iYbReconsiderApplyService.updateYbReconsiderApplyState(applyDateStr,state);
            if(bl){
                message = "状态修改成功.";
                success = 1;
            }else{
                message = "状态修改失败.";
            }
        } catch (Exception e) {
            message = "状态修改失败";
            log.error(message, e);
        }
        ResponseResult rr = new ResponseResult();
        rr.setSuccess(success);
        rr.setMessage(message);
        return  new FebsResponse().data(rr);
    }
}