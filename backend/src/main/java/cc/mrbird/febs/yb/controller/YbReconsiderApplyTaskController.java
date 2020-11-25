package cc.mrbird.febs.yb.controller;

import cc.mrbird.febs.common.annotation.Log;
import cc.mrbird.febs.common.controller.BaseController;
import cc.mrbird.febs.common.domain.router.VueRouter;
import cc.mrbird.febs.common.exception.FebsException;
import cc.mrbird.febs.common.domain.QueryRequest;

import cc.mrbird.febs.yb.service.IYbReconsiderApplyTaskService;
import cc.mrbird.febs.yb.entity.YbReconsiderApplyTask;

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
 * @since 2020-11-23
 */
@Slf4j
@Validated
@RestController
@RequestMapping("ybReconsiderApplyTask")

public class YbReconsiderApplyTaskController extends BaseController{

private String message;
@Autowired
public IYbReconsiderApplyTaskService iYbReconsiderApplyTaskService;


/**
 * 分页查询数据
 *
 * @param  request 分页信息
 * @param ybReconsiderApplyTask 查询条件
 * @return
 */
@GetMapping
@RequiresPermissions("ybReconsiderApplyTask:view")
public Map<String, Object> List(QueryRequest request, YbReconsiderApplyTask ybReconsiderApplyTask){
        return getDataTable(this.iYbReconsiderApplyTaskService.findYbReconsiderApplyTasks(request, ybReconsiderApplyTask));
        }

/**
 * 添加
 * @param  ybReconsiderApplyTask
 * @return
 */
@Log("新增/按钮")
@PostMapping
@RequiresPermissions("ybReconsiderApplyTask:add")
public void addYbReconsiderApplyTask(@Valid YbReconsiderApplyTask ybReconsiderApplyTask)throws FebsException{
        try{
        User currentUser= FebsUtil.getCurrentUser();
        ybReconsiderApplyTask.setCreateUserId(currentUser.getUserId());
        this.iYbReconsiderApplyTaskService.createYbReconsiderApplyTask(ybReconsiderApplyTask);
        }catch(Exception e){
        message="新增/按钮失败" ;
        log.error(message,e);
        throw new FebsException(message);
        }
        }

/**
 * 修改
 * @param ybReconsiderApplyTask
 * @return
 */
@Log("修改")
@PutMapping
@RequiresPermissions("ybReconsiderApplyTask:update")
public void updateYbReconsiderApplyTask(@Valid YbReconsiderApplyTask ybReconsiderApplyTask)throws FebsException{
        try{
        User currentUser= FebsUtil.getCurrentUser();
      ybReconsiderApplyTask.setModifyUserId(currentUser.getUserId());
        this.iYbReconsiderApplyTaskService.updateYbReconsiderApplyTask(ybReconsiderApplyTask);
        }catch(Exception e){
        message="修改失败" ;
        log.error(message,e);
        throw new FebsException(message);
        }
        }


@Log("删除")
@DeleteMapping("/{ids}")
@RequiresPermissions("ybReconsiderApplyTask:delete")
public void deleteYbReconsiderApplyTasks(@NotBlank(message = "{required}") @PathVariable String ids)throws FebsException{
        try{
        String[]arr_ids=ids.split(StringPool.COMMA);
        this.iYbReconsiderApplyTaskService.deleteYbReconsiderApplyTasks(arr_ids);
        }catch(Exception e){
        message="删除失败" ;
        log.error(message,e);
        throw new FebsException(message);
        }
        }
@PostMapping("excel")
@RequiresPermissions("ybReconsiderApplyTask:export")
public void export(QueryRequest request, YbReconsiderApplyTask ybReconsiderApplyTask, HttpServletResponse response) throws FebsException {
        try {
        List<YbReconsiderApplyTask> ybReconsiderApplyTasks = this.iYbReconsiderApplyTaskService.findYbReconsiderApplyTasks(request, ybReconsiderApplyTask).getRecords();
        ExcelKit.$Export(YbReconsiderApplyTask.class, response).downXlsx(ybReconsiderApplyTasks, false);
        } catch (Exception e) {
        message = "导出Excel失败";
        log.error(message, e);
        throw new FebsException(message);
        }
        }

@GetMapping("/{id}")
public YbReconsiderApplyTask detail(@NotBlank(message = "{required}") @PathVariable String id) {
    YbReconsiderApplyTask ybReconsiderApplyTask=this.iYbReconsiderApplyTaskService.getById(id);
        return ybReconsiderApplyTask;
        }
        }