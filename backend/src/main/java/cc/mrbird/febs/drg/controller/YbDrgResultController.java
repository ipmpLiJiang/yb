package cc.mrbird.febs.drg.controller;

import cc.mrbird.febs.common.annotation.Log;
import cc.mrbird.febs.common.controller.BaseController;
import cc.mrbird.febs.common.domain.router.VueRouter;
import cc.mrbird.febs.common.exception.FebsException;
import cc.mrbird.febs.common.domain.QueryRequest;

import cc.mrbird.febs.drg.service.IYbDrgResultService;
import cc.mrbird.febs.drg.entity.YbDrgResult;

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
@RequestMapping("ybDrgResult")

public class YbDrgResultController extends BaseController {

    private String message;
    @Autowired
    public IYbDrgResultService iYbDrgResultService;


    /**
     * 分页查询数据
     *
     * @param request     分页信息
     * @param ybDrgResult 查询条件
     * @return
     */
    @GetMapping
    @RequiresPermissions("ybDrgResult:view")
    public Map<String, Object> List(QueryRequest request, YbDrgResult ybDrgResult) {
        return getDataTable(this.iYbDrgResultService.findYbDrgResults(request, ybDrgResult));
    }

    /**
     * 添加
     *
     * @param ybDrgResult
     * @return
     */
    @Log("新增/按钮")
    @PostMapping
    @RequiresPermissions("ybDrgResult:add")
    public void addYbDrgResult(@Valid YbDrgResult ybDrgResult) throws FebsException {
        try {
            User currentUser = FebsUtil.getCurrentUser();
//            ybDrgResult.setCreateUserId(currentUser.getUserId());
            this.iYbDrgResultService.createYbDrgResult(ybDrgResult);
        } catch (Exception e) {
            message = "新增/按钮失败";
            log.error(message, e);
            throw new FebsException(message);
        }
    }

    /**
     * 修改
     *
     * @param ybDrgResult
     * @return
     */
    @Log("修改")
    @PutMapping
    @RequiresPermissions("ybDrgResult:update")
    public void updateYbDrgResult(@Valid YbDrgResult ybDrgResult) throws FebsException {
        try {
            User currentUser = FebsUtil.getCurrentUser();
//            ybDrgResult.setModifyUserId(currentUser.getUserId());
            this.iYbDrgResultService.updateYbDrgResult(ybDrgResult);
        } catch (Exception e) {
            message = "修改失败";
            log.error(message, e);
            throw new FebsException(message);
        }
    }


    @Log("删除")
    @DeleteMapping("/{ids}")
    @RequiresPermissions("ybDrgResult:delete")
    public void deleteYbDrgResults(@NotBlank(message = "{required}") @PathVariable String ids) throws FebsException {
        try {
            String[] arr_ids = ids.split(StringPool.COMMA);
            this.iYbDrgResultService.deleteYbDrgResults(arr_ids);
        } catch (Exception e) {
            message = "删除失败";
            log.error(message, e);
            throw new FebsException(message);
        }
    }

    @PostMapping("excel")
    @RequiresPermissions("ybDrgResult:export")
    public void export(QueryRequest request, YbDrgResult ybDrgResult, HttpServletResponse response) throws FebsException {
        try {
            List<YbDrgResult> ybDrgResults = this.iYbDrgResultService.findYbDrgResults(request, ybDrgResult).getRecords();
            ExcelKit.$Export(YbDrgResult.class, response).downXlsx(ybDrgResults, false);
        } catch (Exception e) {
            message = "导出Excel失败";
            log.error(message, e);
            throw new FebsException(message);
        }
    }

    @GetMapping("/{id}")
    public YbDrgResult detail(@NotBlank(message = "{required}") @PathVariable String id) {
        YbDrgResult ybDrgResult = this.iYbDrgResultService.getById(id);
        return ybDrgResult;
    }
}