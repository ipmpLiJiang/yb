package cc.mrbird.febs.chs.controller;

import cc.mrbird.febs.common.annotation.Log;
import cc.mrbird.febs.common.controller.BaseController;
import cc.mrbird.febs.common.domain.router.VueRouter;
import cc.mrbird.febs.common.exception.FebsException;
import cc.mrbird.febs.common.domain.QueryRequest;

import cc.mrbird.febs.chs.service.IYbChsApplyTaskService;
import cc.mrbird.febs.chs.entity.YbChsApplyTask;

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
 * @since 2022-06-24
 */
@Slf4j
@Validated
@RestController
@RequestMapping("ybChsApplyTask")

public class YbChsApplyTaskController extends BaseController{

private String message;
@Autowired
public IYbChsApplyTaskService iYbChsApplyTaskService;


/**
 * 分页查询数据
 *
 * @param  request 分页信息
 * @param ybChsApplyTask 查询条件
 * @return
 */
@GetMapping
@RequiresPermissions("ybChsApplyTask:view")
public Map<String, Object> List(QueryRequest request, YbChsApplyTask ybChsApplyTask){
        return getDataTable(this.iYbChsApplyTaskService.findYbChsApplyTasks(request, ybChsApplyTask));
        }

/**
 * 添加
 * @param  ybChsApplyTask
 * @return
 */
@Log("新增/按钮")
@PostMapping
@RequiresPermissions("ybChsApplyTask:add")
public void addYbChsApplyTask(@Valid YbChsApplyTask ybChsApplyTask)throws FebsException{
        try{
        User currentUser= FebsUtil.getCurrentUser();
        ybChsApplyTask.setCreateUserId(currentUser.getUserId());
        this.iYbChsApplyTaskService.createYbChsApplyTask(ybChsApplyTask);
        }catch(Exception e){
        message="新增/按钮失败" ;
        log.error(message,e);
        throw new FebsException(message);
        }
        }

/**
 * 修改
 * @param ybChsApplyTask
 * @return
 */
@Log("修改")
@PutMapping
@RequiresPermissions("ybChsApplyTask:update")
public void updateYbChsApplyTask(@Valid YbChsApplyTask ybChsApplyTask)throws FebsException{
        try{
        User currentUser= FebsUtil.getCurrentUser();
      ybChsApplyTask.setModifyUserId(currentUser.getUserId());
        this.iYbChsApplyTaskService.updateYbChsApplyTask(ybChsApplyTask);
        }catch(Exception e){
        message="修改失败" ;
        log.error(message,e);
        throw new FebsException(message);
        }
        }


@Log("删除")
@DeleteMapping("/{ids}")
@RequiresPermissions("ybChsApplyTask:delete")
public void deleteYbChsApplyTasks(@NotBlank(message = "{required}") @PathVariable String ids)throws FebsException{
        try{
        String[]arr_ids=ids.split(StringPool.COMMA);
        this.iYbChsApplyTaskService.deleteYbChsApplyTasks(arr_ids);
        }catch(Exception e){
        message="删除失败" ;
        log.error(message,e);
        throw new FebsException(message);
        }
        }
@PostMapping("excel")
@RequiresPermissions("ybChsApplyTask:export")
public void export(QueryRequest request, YbChsApplyTask ybChsApplyTask, HttpServletResponse response) throws FebsException {
        try {
        List<YbChsApplyTask> ybChsApplyTasks = this.iYbChsApplyTaskService.findYbChsApplyTasks(request, ybChsApplyTask).getRecords();
        ExcelKit.$Export(YbChsApplyTask.class, response).downXlsx(ybChsApplyTasks, false);
        } catch (Exception e) {
        message = "导出Excel失败";
        log.error(message, e);
        throw new FebsException(message);
        }
        }

@GetMapping("/{id}")
public YbChsApplyTask detail(@NotBlank(message = "{required}") @PathVariable String id) {
    YbChsApplyTask ybChsApplyTask=this.iYbChsApplyTaskService.getById(id);
        return ybChsApplyTask;
        }
        }