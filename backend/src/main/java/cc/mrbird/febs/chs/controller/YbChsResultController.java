package cc.mrbird.febs.chs.controller;

import cc.mrbird.febs.common.annotation.Log;
import cc.mrbird.febs.common.controller.BaseController;
import cc.mrbird.febs.common.domain.router.VueRouter;
import cc.mrbird.febs.common.exception.FebsException;
import cc.mrbird.febs.common.domain.QueryRequest;

import cc.mrbird.febs.chs.service.IYbChsResultService;
import cc.mrbird.febs.chs.entity.YbChsResult;

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
 * @since 2022-06-20
 */
@Slf4j
@Validated
@RestController
@RequestMapping("ybChsResult")

public class YbChsResultController extends BaseController {

    private String message;
    @Autowired
    public IYbChsResultService iYbChsResultService;


    /**
     * 分页查询数据
     *
     * @param request     分页信息
     * @param ybChsResult 查询条件
     * @return
     */
    @GetMapping
    @RequiresPermissions("ybChsResult:view")
    public Map<String, Object> List(QueryRequest request, YbChsResult ybChsResult) {
        return getDataTable(this.iYbChsResultService.findYbChsResults(request, ybChsResult));
    }

    /**
     * 添加
     *
     * @param ybChsResult
     * @return
     */
    @Log("新增/按钮")
    @PostMapping
    @RequiresPermissions("ybChsResult:add")
    public void addYbChsResult(@Valid YbChsResult ybChsResult) throws FebsException {
        try {
            User currentUser = FebsUtil.getCurrentUser();
//            ybChsResult.setCreateUserId(currentUser.getUserId());
            this.iYbChsResultService.createYbChsResult(ybChsResult);
        } catch (Exception e) {
            message = "新增/按钮失败";
            log.error(message, e);
            throw new FebsException(message);
        }
    }

    /**
     * 修改
     *
     * @param ybChsResult
     * @return
     */
    @Log("修改")
    @PutMapping
    @RequiresPermissions("ybChsResult:update")
    public void updateYbChsResult(@Valid YbChsResult ybChsResult) throws FebsException {
        try {
            User currentUser = FebsUtil.getCurrentUser();
//            ybChsResult.setModifyUserId(currentUser.getUserId());
            this.iYbChsResultService.updateYbChsResult(ybChsResult);
        } catch (Exception e) {
            message = "修改失败";
            log.error(message, e);
            throw new FebsException(message);
        }
    }


    @Log("删除")
    @DeleteMapping("/{ids}")
    @RequiresPermissions("ybChsResult:delete")
    public void deleteYbChsResults(@NotBlank(message = "{required}") @PathVariable String ids) throws FebsException {
        try {
            String[] arr_ids = ids.split(StringPool.COMMA);
            this.iYbChsResultService.deleteYbChsResults(arr_ids);
        } catch (Exception e) {
            message = "删除失败";
            log.error(message, e);
            throw new FebsException(message);
        }
    }

    @PostMapping("excel")
    @RequiresPermissions("ybChsResult:export")
    public void export(QueryRequest request, YbChsResult ybChsResult, HttpServletResponse response) throws FebsException {
        try {
            List<YbChsResult> ybChsResults = this.iYbChsResultService.findYbChsResults(request, ybChsResult).getRecords();
            ExcelKit.$Export(YbChsResult.class, response).downXlsx(ybChsResults, false);
        } catch (Exception e) {
            message = "导出Excel失败";
            log.error(message, e);
            throw new FebsException(message);
        }
    }

    @GetMapping("/{id}")
    public YbChsResult detail(@NotBlank(message = "{required}") @PathVariable String id) {
        YbChsResult ybChsResult = this.iYbChsResultService.getById(id);
        return ybChsResult;
    }
}