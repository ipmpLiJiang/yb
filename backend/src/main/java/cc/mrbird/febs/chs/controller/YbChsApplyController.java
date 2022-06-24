package cc.mrbird.febs.chs.controller;

import cc.mrbird.febs.common.annotation.Log;
import cc.mrbird.febs.common.controller.BaseController;
import cc.mrbird.febs.common.domain.FebsResponse;
import cc.mrbird.febs.common.domain.router.VueRouter;
import cc.mrbird.febs.common.exception.FebsException;
import cc.mrbird.febs.common.domain.QueryRequest;

import cc.mrbird.febs.chs.service.IYbChsApplyService;
import cc.mrbird.febs.chs.entity.YbChsApply;

import cc.mrbird.febs.common.utils.FebsUtil;
import cc.mrbird.febs.system.domain.User;
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
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * @author viki
 * @since 2022-06-20
 */
@Slf4j
@Validated
@RestController
@RequestMapping("ybChsApply")

public class YbChsApplyController extends BaseController {

    private String message;
    @Autowired
    public IYbChsApplyService iYbChsApplyService;


    /**
     * 分页查询数据
     *
     * @param request    分页信息
     * @param ybChsApply 查询条件
     * @return
     */
    @GetMapping
    @RequiresPermissions("ybChsApply:view")
    public Map<String, Object> List(QueryRequest request, YbChsApply ybChsApply) {
        return getDataTable(this.iYbChsApplyService.findYbChsApplys(request, ybChsApply));
    }

    /**
     * 添加
     *
     * @param ybChsApply
     * @return
     */
    @Log("新增/按钮")
    @PostMapping
    @RequiresPermissions("ybChsApply:add")
    public void addYbChsApply(@Valid YbChsApply ybChsApply) throws FebsException {
        try {
            User currentUser = FebsUtil.getCurrentUser();
            ybChsApply.setCreateUserId(currentUser.getUserId());
            ybChsApply.setOperatorId(currentUser.getUserId());
            ybChsApply.setOperatorName(currentUser.getUsername() + "-" + currentUser.getXmname());
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM");
            String appDateStr = sdf.format(ybChsApply.getApplyDate());
            ybChsApply.setApplyDateStr(appDateStr);
            this.iYbChsApplyService.createYbChsApply(ybChsApply);
        } catch (Exception e) {
            message = "新增/按钮失败";
            log.error(message, e);
            throw new FebsException(message);
        }
    }

    @Log("新增/按钮")
    @PostMapping("addYbChsApplyCheck")
    @RequiresPermissions("ybChsApply:add")
    public FebsResponse addYbChsApplyCheck(@Valid YbChsApply ybChsApply) {
        ModelMap map = new ModelMap();
        int success = 0;
        try {
            User currentUser = FebsUtil.getCurrentUser();
            ybChsApply.setCreateUserId(currentUser.getUserId());
            ybChsApply.setOperatorId(currentUser.getUserId());
            ybChsApply.setOperatorName(currentUser.getUsername() + "-" + currentUser.getXmname());
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM");
            String appDateStr = sdf.format(ybChsApply.getApplyDate());
            ybChsApply.setApplyDateStr(appDateStr);
            message = this.iYbChsApplyService.createChsApplyCheck(ybChsApply);
            if(message.equals("ok")){
                success = 1;
                message = "新增成功.";
            }
        } catch (Exception e) {
            message = "新增/按钮失败";
            log.error(message, e);
        }
        map.put("message",message);
        map.put("success",success);
        return  new FebsResponse().data(map);
    }


    /**
     * 修改
     *
     * @param ybChsApply
     * @return
     */
    @Log("修改")
    @PutMapping
    @RequiresPermissions("ybChsApply:update")
    public FebsResponse updateYbChsApply(@Valid YbChsApply ybChsApply,boolean isUpOverdue) throws FebsException {
        int success = 0;
        ModelMap map = new ModelMap();
        try {
            User currentUser = FebsUtil.getCurrentUser();
            ybChsApply.setModifyUserId(currentUser.getUserId());

            ybChsApply.setOperatorId(currentUser.getUserId());
            ybChsApply.setOperatorName(currentUser.getUsername() + "-" + currentUser.getXmname());
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM");
            String appDateStr = sdf.format(ybChsApply.getApplyDate());
            ybChsApply.setApplyDateStr(appDateStr);
            this.message = this.iYbChsApplyService.updateYbChsApply(ybChsApply, isUpOverdue);
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
        map.put("message",message);
        map.put("success",success);
        return  new FebsResponse().data(map);
    }


    @Log("删除")
    @DeleteMapping("/{ids}")
    @RequiresPermissions("ybChsApply:delete")
    public void deleteYbChsApplys(@NotBlank(message = "{required}") @PathVariable String ids) throws FebsException {
        try {
            String[] arr_ids = ids.split(StringPool.COMMA);
            this.iYbChsApplyService.deleteYbChsApplys(arr_ids,1);
        } catch (Exception e) {
            message = "删除失败";
            log.error(message, e);
            throw new FebsException(message);
        }
    }

    @PostMapping("excel")
    @RequiresPermissions("ybChsApply:export")
    public void export(QueryRequest request, YbChsApply ybChsApply, HttpServletResponse response) throws FebsException {
        try {
            List<YbChsApply> ybChsApplys = this.iYbChsApplyService.findYbChsApplys(request, ybChsApply).getRecords();
            ExcelKit.$Export(YbChsApply.class, response).downXlsx(ybChsApplys, false);
        } catch (Exception e) {
            message = "导出Excel失败";
            log.error(message, e);
            throw new FebsException(message);
        }
    }

    @GetMapping("/{id}")
    public YbChsApply detail(@NotBlank(message = "{required}") @PathVariable String id) {
        YbChsApply ybChsApply = this.iYbChsApplyService.getById(id);
        return ybChsApply;
    }

    // 更改日期时间 和 更改其他业务数据
    @Log("修改")
    @PutMapping("updateChsApply")
    @RequiresPermissions("ybChsApply:update")
    public FebsResponse updateChsApplys(@Valid YbChsApply ybChsApply, Integer isChangDate){
        ModelMap map = new ModelMap();
        int success = 0;
        try {
            this.iYbChsApplyService.updateYbChsApply(ybChsApply,isChangDate);
            success = 1;
        } catch (Exception e) {
            message = "修改失败";
            log.error(message, e);
        }

        map.put("message",message);
        map.put("success",success);
        return  new FebsResponse().data(map);
    }


    @GetMapping("getChsApply")
    public FebsResponse getChsApplys(String applyDateStr, Integer areaType) {
        ModelMap map = new ModelMap();
        int success = 0;
        YbChsApply chsApply = null;
        try {
            chsApply = this.iYbChsApplyService.findChsApplyByApplyDateStrs(applyDateStr, areaType);
            success = 1;
            message = "";
        }catch (Exception e){
            message = "获取状态值失败";
            log.error(message, e);
        }
        map.put("apply",chsApply);
        map.put("message",message);
        map.put("success",success);
        return  new FebsResponse().data(map);
    }
}