package cc.mrbird.febs.yb.controller;

import cc.mrbird.febs.common.annotation.Log;
import cc.mrbird.febs.common.controller.BaseController;
import cc.mrbird.febs.common.domain.router.VueRouter;
import cc.mrbird.febs.common.exception.FebsException;
import cc.mrbird.febs.common.domain.QueryRequest;

import cc.mrbird.febs.yb.service.IYbDeptService;
import cc.mrbird.febs.yb.entity.YbDept;

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
 *
 * @author viki
 * @since 2020-07-21
 */
@Slf4j
@Validated
@RestController
@RequestMapping("ybDept")

public class YbDeptController extends BaseController{

private String message;
@Autowired
public IYbDeptService iYbDeptService;


/**
 * 分页查询数据
 *
 * @param  request 分页信息
 * @param ybDept 查询条件
 * @return
 */
@GetMapping
@RequiresPermissions("ybDept:view")
public Map<String, Object> List(QueryRequest request, YbDept ybDept){
        return getDataTable(this.iYbDeptService.findYbDepts(request, ybDept));
        }

/**
 * 添加
 * @param  ybDept
 * @return
 */
@Log("新增/按钮")
@PostMapping
@RequiresPermissions("ybDept:add")
public void addYbDept(@Valid YbDept ybDept)throws FebsException{
        try{
        User currentUser= FebsUtil.getCurrentUser();
        ybDept.setCreateUserId(currentUser.getUserId());
        this.iYbDeptService.createYbDept(ybDept);
        }catch(Exception e){
        message="新增/按钮失败" ;
        log.error(message,e);
        throw new FebsException(message);
        }
        }

/**
 * 修改
 * @param ybDept
 * @return
 */
@Log("修改")
@PutMapping
@RequiresPermissions("ybDept:update")
public void updateYbDept(@Valid YbDept ybDept)throws FebsException{
        try{
        User currentUser= FebsUtil.getCurrentUser();
      ybDept.setModifyUserId(currentUser.getUserId());
        this.iYbDeptService.updateYbDept(ybDept);
        }catch(Exception e){
        message="修改失败" ;
        log.error(message,e);
        throw new FebsException(message);
        }
        }


@Log("删除")
@DeleteMapping("/{ids}")
@RequiresPermissions("ybDept:delete")
public void deleteYbDepts(@NotBlank(message = "{required}") @PathVariable String ids)throws FebsException{
        try{
        String[]arr_ids=ids.split(StringPool.COMMA);
        this.iYbDeptService.deleteYbDepts(arr_ids);
        }catch(Exception e){
        message="删除失败" ;
        log.error(message,e);
        throw new FebsException(message);
        }
        }
@PostMapping("excel")
@RequiresPermissions("ybDept:export")
public void export(QueryRequest request, YbDept ybDept, HttpServletResponse response) throws FebsException {
        try {
        List<YbDept> ybDepts = this.iYbDeptService.findYbDepts(request, ybDept).getRecords();
        ExcelKit.$Export(YbDept.class, response).downXlsx(ybDepts, false);
        } catch (Exception e) {
        message = "导出Excel失败";
        log.error(message, e);
        throw new FebsException(message);
        }
        }

@GetMapping("/{id}")
public YbDept detail(@NotBlank(message = "{required}") @PathVariable String id) {
    YbDept ybDept=this.iYbDeptService.getById(id);
        return ybDept;
        }
        }