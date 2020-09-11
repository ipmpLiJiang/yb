package cc.mrbird.febs.yb.controller;

import cc.mrbird.febs.common.annotation.Log;
import cc.mrbird.febs.common.controller.BaseController;
import cc.mrbird.febs.common.domain.FebsResponse;
import cc.mrbird.febs.common.domain.router.VueRouter;
import cc.mrbird.febs.common.exception.FebsException;
import cc.mrbird.febs.common.domain.QueryRequest;

import cc.mrbird.febs.yb.service.IYbAppealResultService;
import cc.mrbird.febs.yb.entity.YbAppealResult;

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
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author viki
 * @since 2020-07-30
 */
@Slf4j
@Validated
@RestController
@RequestMapping("ybAppealResult")

public class YbAppealResultController extends BaseController {

    private String message;
    @Autowired
    public IYbAppealResultService iYbAppealResultService;


    /**
     * 分页查询数据
     *
     * @param request        分页信息
     * @param ybAppealResult 查询条件
     * @return
     */
    @GetMapping
    @RequiresPermissions("ybAppealResult:view")
    public Map<String, Object> List(QueryRequest request, YbAppealResult ybAppealResult) {
        return getDataTable(this.iYbAppealResultService.findYbAppealResults(request, ybAppealResult));
    }

    /**
     * 添加
     *
     * @param ybAppealResult
     * @return
     */
    @Log("新增/按钮")
    @PostMapping
    @RequiresPermissions("ybAppealResult:add")
    public void addYbAppealResult(@Valid YbAppealResult ybAppealResult) throws FebsException {
        try {
            User currentUser = FebsUtil.getCurrentUser();
            ybAppealResult.setCreateUserId(currentUser.getUserId());
            this.iYbAppealResultService.createYbAppealResult(ybAppealResult);
        } catch (Exception e) {
            message = "新增/按钮失败";
            log.error(message, e);
            throw new FebsException(message);
        }
    }

    /**
     * 修改
     *
     * @param ybAppealResult
     * @return
     */
    @Log("修改")
    @PutMapping
    @RequiresPermissions("ybAppealResult:update")
    public void updateYbAppealResult(@Valid YbAppealResult ybAppealResult) throws FebsException {
        try {
            User currentUser = FebsUtil.getCurrentUser();
            ybAppealResult.setModifyUserId(currentUser.getUserId());
            this.iYbAppealResultService.updateYbAppealResult(ybAppealResult);
        } catch (Exception e) {
            message = "修改失败";
            log.error(message, e);
            throw new FebsException(message);
        }
    }


    @Log("删除")
    @DeleteMapping("/{ids}")
    @RequiresPermissions("ybAppealResult:delete")
    public void deleteYbAppealResults(@NotBlank(message = "{required}") @PathVariable String ids) throws FebsException {
        try {
            String[] arr_ids = ids.split(StringPool.COMMA);
            this.iYbAppealResultService.deleteYbAppealResults(arr_ids);
        } catch (Exception e) {
            message = "删除失败";
            log.error(message, e);
            throw new FebsException(message);
        }
    }

    @PostMapping("excel")
    @RequiresPermissions("ybAppealResult:export")
    public void export(QueryRequest request, YbAppealResult ybAppealResult, HttpServletResponse response) throws FebsException {
        try {
            List<YbAppealResult> ybAppealResults = this.iYbAppealResultService.findYbAppealResults(request, ybAppealResult).getRecords();
            ExcelKit.$Export(YbAppealResult.class, response).downXlsx(ybAppealResults, false);
        } catch (Exception e) {
            message = "导出Excel失败";
            log.error(message, e);
            throw new FebsException(message);
        }
    }

    @GetMapping("/{id}")
    public YbAppealResult detail(@NotBlank(message = "{required}") @PathVariable String id) {
        YbAppealResult ybAppealResult = this.iYbAppealResultService.getById(id);
        return ybAppealResult;
    }

    @Log("查询创建")
    @PostMapping("findCreateAppealResult")
    @RequiresPermissions("ybAppealResult:findCreate")
    public FebsResponse findCreateAppealResults(YbAppealResult ybAppealResult){
        int success = 1;
        try {
            User currentUser = FebsUtil.getCurrentUser();
            Long uid = currentUser.getUserId();
            String uname = currentUser.getUsername();
            ybAppealResult = this.iYbAppealResultService.findCreateAppealResult(ybAppealResult,uid,uname);
        } catch (Exception e) {
            message = "创建失败";
            success = 0;
            log.error(message, e);
        }
        Map<String, Object> result = new HashMap<>();
        result.put("success",success);
        result.put("message", message);
        result.put("data", ybAppealResult);
        return new FebsResponse().put("data", result);
    }


}