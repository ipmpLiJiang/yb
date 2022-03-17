package cc.mrbird.febs.drg.controller;

import cc.mrbird.febs.common.annotation.Log;
import cc.mrbird.febs.common.controller.BaseController;
import cc.mrbird.febs.common.domain.FebsResponse;
import cc.mrbird.febs.common.domain.router.VueRouter;
import cc.mrbird.febs.common.exception.FebsException;
import cc.mrbird.febs.common.domain.QueryRequest;

import cc.mrbird.febs.drg.service.IYbDrgApplyService;
import cc.mrbird.febs.drg.entity.YbDrgApply;

import cc.mrbird.febs.common.utils.FebsUtil;
import cc.mrbird.febs.system.domain.User;
import cc.mrbird.febs.yb.domain.ResponseResult;
import com.baomidou.mybatisplus.core.toolkit.StringPool;
import com.wuwenze.poi.ExcelKit;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.ModelMap;
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
 * @since 2021-11-23
 */
@Slf4j
@Validated
@RestController
@RequestMapping("ybDrgApply")

public class YbDrgApplyController extends BaseController {

    private String message;
    @Autowired
    public IYbDrgApplyService iYbDrgApplyService;


    /**
     * 分页查询数据
     *
     * @param request    分页信息
     * @param ybDrgApply 查询条件
     * @return
     */
    @GetMapping
    @RequiresPermissions("ybDrgApply:view")
    public Map<String, Object> List(QueryRequest request, YbDrgApply ybDrgApply) {
        return getDataTable(this.iYbDrgApplyService.findYbDrgApplys(request, ybDrgApply));
    }

    /**
     * 添加
     *
     * @param ybDrgApply
     * @return
     */
    @Log("新增/按钮")
    @PostMapping
    @RequiresPermissions("ybDrgApply:add")
    public void addYbDrgApply(@Valid YbDrgApply ybDrgApply) throws FebsException {
        try {
            User currentUser = FebsUtil.getCurrentUser();
            ybDrgApply.setCreateUserId(currentUser.getUserId());
            ybDrgApply.setOperatorId(currentUser.getUserId());
            ybDrgApply.setOperatorName(currentUser.getUsername() + "-" + currentUser.getXmname());
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM");
            String appDateStr = sdf.format(ybDrgApply.getApplyDate());
            ybDrgApply.setApplyDateStr(appDateStr);
            this.iYbDrgApplyService.createYbDrgApply(ybDrgApply);
        } catch (Exception e) {
            message = "新增/按钮失败";
            log.error(message, e);
            throw new FebsException(message);
        }
    }

    @Log("新增/按钮")
    @PostMapping("addYbDrgApplyCheck")
    @RequiresPermissions("ybDrgApply:add")
    public FebsResponse addYbDrgApplyCheck(@Valid YbDrgApply ybDrgApply) {
        int success = 0;
        try {
            User currentUser = FebsUtil.getCurrentUser();
            ybDrgApply.setCreateUserId(currentUser.getUserId());
            ybDrgApply.setOperatorId(currentUser.getUserId());
            ybDrgApply.setOperatorName(currentUser.getUsername() + "-" + currentUser.getXmname());
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM");
            String appDateStr = sdf.format(ybDrgApply.getApplyDate());
            ybDrgApply.setApplyDateStr(appDateStr);
            message = this.iYbDrgApplyService.createDrgApplyCheck(ybDrgApply);
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
     * @param ybDrgApply
     * @return
     */
    @Log("修改")
    @PutMapping
    @RequiresPermissions("ybDrgApply:update")
    public FebsResponse updateYbDrgApply(@Valid YbDrgApply ybDrgApply,boolean isUpOverdue) throws FebsException {
        int success = 0;
        try {
            User currentUser = FebsUtil.getCurrentUser();
            ybDrgApply.setModifyUserId(currentUser.getUserId());

            ybDrgApply.setOperatorId(currentUser.getUserId());
            ybDrgApply.setOperatorName(currentUser.getUsername() + "-" + currentUser.getXmname());
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM");
            String appDateStr = sdf.format(ybDrgApply.getApplyDate());
            ybDrgApply.setApplyDateStr(appDateStr);
            this.message = this.iYbDrgApplyService.updateYbDrgApply(ybDrgApply, isUpOverdue);
            if(this.message.equals("") || this.message.equals("ok")){
                this.message = "ok";
            }else if(this.message.equals("date")){
                this.message = "当前结束日期应大于之前结束日期";
            }else if(this.message.equals("nodata")){
                this.message = "当前没有未申诉数据";
            }else if(this.message.equals("nostate")){
                this.message = "当前状态无法进行未申诉更新";
            }
            success = 1;
        } catch (Exception e) {
            message = "修改失败";
            log.error(message, e);
//            throw new FebsException(message);
        }
        ResponseResult rr= new ResponseResult();
        rr.setSuccess(success);
        rr.setMessage(message);
        return new FebsResponse().data(rr);
    }


    @Log("删除")
    @DeleteMapping("/{ids}")
    @RequiresPermissions("ybDrgApply:delete")
    public void deleteYbDrgApplys(@NotBlank(message = "{required}") @PathVariable String ids) throws FebsException {
        try {
            String[] arr_ids = ids.split(StringPool.COMMA);
            this.iYbDrgApplyService.deleteYbDrgApplys(arr_ids,1);
        } catch (Exception e) {
            message = "删除失败";
            log.error(message, e);
            throw new FebsException(message);
        }
    }

    @PostMapping("excel")
    @RequiresPermissions("ybDrgApply:export")
    public void export(QueryRequest request, YbDrgApply ybDrgApply, HttpServletResponse response) throws FebsException {
        try {
            List<YbDrgApply> ybDrgApplys = this.iYbDrgApplyService.findYbDrgApplys(request, ybDrgApply).getRecords();
            ExcelKit.$Export(YbDrgApply.class, response).downXlsx(ybDrgApplys, false);
        } catch (Exception e) {
            message = "导出Excel失败";
            log.error(message, e);
            throw new FebsException(message);
        }
    }

    @GetMapping("/{id}")
    public YbDrgApply detail(@NotBlank(message = "{required}") @PathVariable String id) {
        YbDrgApply ybDrgApply = this.iYbDrgApplyService.getById(id);
        return ybDrgApply;
    }

    // 更改日期时间 和 更改其他业务数据
    @Log("修改")
    @PutMapping("updateDrgApply")
    @RequiresPermissions("ybDrgApply:update")
    public FebsResponse updateDrgApplys(@Valid YbDrgApply ybDrgApply, Integer isChangDate){
        int success = 0;
        try {
            this.iYbDrgApplyService.updateYbDrgApply(ybDrgApply,isChangDate);
            success = 1;
        } catch (Exception e) {
            message = "修改失败";
            log.error(message, e);
        }
        ResponseResult rr = new ResponseResult();
        rr.setSuccess(success);
        rr.setMessage(message);
        return  new FebsResponse().data(rr);
    }


    @GetMapping("getDrgApply")
    public FebsResponse getDrgApplys(String applyDateStr, Integer areaType) {
        ModelMap map = new ModelMap();
        int success = 0;
        int typeno = 1;
        YbDrgApply drgApply = null;
        try {
            drgApply = this.iYbDrgApplyService.findDrgApplyByApplyDateStrs(applyDateStr, areaType);
            success = 1;
            message = "";
        }catch (Exception e){
            message = "获取状态值失败";
            log.error(message, e);
        }
        map.put("apply",drgApply);
        map.put("message",message);
        map.put("success",success);
        return  new FebsResponse().data(map);
    }
}