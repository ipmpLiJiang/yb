package cc.mrbird.febs.scm.controller;

import cc.mrbird.febs.common.annotation.Log;
import cc.mrbird.febs.common.controller.BaseController;
import cc.mrbird.febs.common.domain.router.VueRouter;
import cc.mrbird.febs.common.exception.FebsException;
import cc.mrbird.febs.common.domain.QueryRequest;

import cc.mrbird.febs.scm.service.IScmDPlanService;
import cc.mrbird.febs.scm.entity.ScmDPlan;

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
 *
 * @author viki
 * @since 2019-10-23
 */
@Slf4j
@Validated
@RestController
@RequestMapping("scmDPlan")

public class ScmDPlanController extends BaseController{

private String message;
@Autowired
public IScmDPlanService iScmDPlanService;


/**
 * 分页查询数据
 *
 * @param bootStrapTable  分页信息
 * @param scmDPlan 查询条件
 * @return
 */
@GetMapping
@RequiresPermissions("scmDPlan:view")
public Map<String, Object> List(QueryRequest request, ScmDPlan scmDPlan){
        return getDataTable(this.iScmDPlanService.findScmDPlans(request, scmDPlan));
        }

/**
 * 跳转添加页面
 * @param request
 * @param response
 * @param model
 * @return
 */
@Log("新增/按钮")
@PostMapping
@RequiresPermissions("scmDPlan:add")
public void addScmDPlan(@Valid ScmDPlan scmDPlan)throws FebsException{
        try{
        this.iScmDPlanService.createScmDPlan(scmDPlan);
        }catch(Exception e){
        message="新增/按钮失败" ;
        log.error(message,e);
        throw new FebsException(message);
        }
        }

/**
 * 跳转修改页面
 * @param request
 * @param id  实体ID
 * @return
 */
@Log("修改")
@PutMapping
@RequiresPermissions("scmDPlan:update")
public void updateScmDPlan(@Valid ScmDPlan scmDPlan)throws FebsException{
        try{
        this.iScmDPlanService.updateScmDPlan(scmDPlan);
        }catch(Exception e){
        message="修改成功" ;
        log.error(message,e);
        throw new FebsException(message);
        }
        }


@Log("删除")
@DeleteMapping("/{ids}")
@RequiresPermissions("scmDPlan:delete")
public void deleteScmDPlans(@NotBlank(message = "{required}") @PathVariable String ids)throws FebsException{
        try{
        String[]arr_ids=ids.split(StringPool.COMMA);
        this.iScmDPlanService.deleteScmDPlans(arr_ids);
        }catch(Exception e){
        message="删除成功" ;
        log.error(message,e);
        throw new FebsException(message);
        }
        }
@PostMapping("excel")
@RequiresPermissions("scmDPlan:export")
public void export(QueryRequest request, ScmDPlan scmDPlan, HttpServletResponse response) throws FebsException {
        try {
        List<ScmDPlan> scmDPlans = this.iScmDPlanService.findScmDPlans(request, scmDPlan).getRecords();
        ExcelKit.$Export(ScmDPlan.class, response).downXlsx(scmDPlans, false);
        } catch (Exception e) {
        message = "导出Excel失败";
        log.error(message, e);
        throw new FebsException(message);
        }
        }

@GetMapping("/{id}")
public ScmDPlan detail(@NotBlank(message = "{required}") @PathVariable String id) {
    ScmDPlan scmDPlan=this.iScmDPlanService.getById(id);
        return scmDPlan;
        }
        }