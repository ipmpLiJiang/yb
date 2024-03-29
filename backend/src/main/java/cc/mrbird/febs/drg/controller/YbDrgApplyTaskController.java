package cc.mrbird.febs.drg.controller;

import cc.mrbird.febs.common.annotation.Log;
import cc.mrbird.febs.common.controller.BaseController;
import cc.mrbird.febs.common.domain.router.VueRouter;
import cc.mrbird.febs.common.exception.FebsException;
import cc.mrbird.febs.common.domain.QueryRequest;

import cc.mrbird.febs.drg.service.IYbDrgApplyTaskService;
import cc.mrbird.febs.drg.entity.YbDrgApplyTask;

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
 * @author viki
 * @since 2021-11-23
 */
@Slf4j
@Validated
@RestController
@RequestMapping("ybDrgApplyTask")

public class YbDrgApplyTaskController extends BaseController {

    private String message;
    @Autowired
    public IYbDrgApplyTaskService iYbDrgApplyTaskService;


    /**
     * 分页查询数据
     *
     * @param request        分页信息
     * @param ybDrgApplyTask 查询条件
     * @return
     */
    @GetMapping
    @RequiresPermissions("ybDrgApplyTask:view")
    public Map<String, Object> List(QueryRequest request, YbDrgApplyTask ybDrgApplyTask) {
        return getDataTable(this.iYbDrgApplyTaskService.findYbDrgApplyTasks(request, ybDrgApplyTask));
    }

    /**
     * 添加
     *
     * @param ybDrgApplyTask
     * @return
     */
    @Log("新增/按钮")
    @PostMapping
    @RequiresPermissions("ybDrgApplyTask:add")
    public void addYbDrgApplyTask(@Valid YbDrgApplyTask ybDrgApplyTask) throws FebsException {
        try {
            User currentUser = FebsUtil.getCurrentUser();
//            ybDrgApplyTask.setCreateUserId(currentUser.getUserId());
            this.iYbDrgApplyTaskService.createYbDrgApplyTask(ybDrgApplyTask);
        } catch (Exception e) {
            message = "新增/按钮失败";
            log.error(message, e);
            throw new FebsException(message);
        }
    }

    /**
     * 修改
     *
     * @param ybDrgApplyTask
     * @return
     */
    @Log("修改")
    @PutMapping
    @RequiresPermissions("ybDrgApplyTask:update")
    public void updateYbDrgApplyTask(@Valid YbDrgApplyTask ybDrgApplyTask) throws FebsException {
        try {
            User currentUser = FebsUtil.getCurrentUser();
//            ybDrgApplyTask.setModifyUserId(currentUser.getUserId());
            this.iYbDrgApplyTaskService.updateYbDrgApplyTask(ybDrgApplyTask);
        } catch (Exception e) {
            message = "修改失败";
            log.error(message, e);
            throw new FebsException(message);
        }
    }


    @Log("删除")
    @DeleteMapping("/{ids}")
    @RequiresPermissions("ybDrgApplyTask:delete")
    public void deleteYbDrgApplyTasks(@NotBlank(message = "{required}") @PathVariable String ids) throws FebsException {
        try {
            String[] arr_ids = ids.split(StringPool.COMMA);
            this.iYbDrgApplyTaskService.deleteYbDrgApplyTasks(arr_ids);
        } catch (Exception e) {
            message = "删除失败";
            log.error(message, e);
            throw new FebsException(message);
        }
    }

    @PostMapping("excel")
    @RequiresPermissions("ybDrgApplyTask:export")
    public void export(QueryRequest request, YbDrgApplyTask ybDrgApplyTask, HttpServletResponse response) throws FebsException {
        try {
            List<YbDrgApplyTask> ybDrgApplyTasks = this.iYbDrgApplyTaskService.findYbDrgApplyTasks(request, ybDrgApplyTask).getRecords();
            ExcelKit.$Export(YbDrgApplyTask.class, response).downXlsx(ybDrgApplyTasks, false);
        } catch (Exception e) {
            message = "导出Excel失败";
            log.error(message, e);
            throw new FebsException(message);
        }
    }

    @GetMapping("/{id}")
    public YbDrgApplyTask detail(@NotBlank(message = "{required}") @PathVariable String id) {
        YbDrgApplyTask ybDrgApplyTask = this.iYbDrgApplyTaskService.getById(id);
        return ybDrgApplyTask;
    }
}