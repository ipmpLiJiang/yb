package cc.mrbird.febs.yb.controller;

import cc.mrbird.febs.common.annotation.Log;
import cc.mrbird.febs.common.controller.BaseController;
import cc.mrbird.febs.common.domain.router.VueRouter;
import cc.mrbird.febs.common.exception.FebsException;
import cc.mrbird.febs.common.domain.QueryRequest;

import cc.mrbird.febs.yb.service.IYbReconsiderRuleService;
import cc.mrbird.febs.yb.entity.YbReconsiderRule;

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
 * @since 2020-07-15
 */
@Slf4j
@Validated
@RestController
@RequestMapping("ybReconsiderRule")

public class YbReconsiderRuleController extends BaseController {

    private String message;
    @Autowired
    public IYbReconsiderRuleService iYbReconsiderRuleService;


    /**
     * 分页查询数据
     *
     * @param request          分页信息
     * @param ybReconsiderRule 查询条件
     * @return
     */
    @GetMapping
    @RequiresPermissions("ybReconsiderRule:view")
    public Map<String, Object> List(QueryRequest request, YbReconsiderRule ybReconsiderRule) {
        return getDataTable(this.iYbReconsiderRuleService.findYbReconsiderRules(request, ybReconsiderRule));
    }

    /**
     * 添加
     *
     * @param ybReconsiderRule
     * @return
     */
    @Log("新增/按钮")
    @PostMapping
    @RequiresPermissions("ybReconsiderRule:add")
    public void addYbReconsiderRule(@Valid YbReconsiderRule ybReconsiderRule) throws FebsException {
        try {
            User currentUser = FebsUtil.getCurrentUser();
            ybReconsiderRule.setCreateUserId(currentUser.getUserId());
            ybReconsiderRule.setOperatorid(currentUser.getUserId());
            ybReconsiderRule.setOperatorname(currentUser.getUsername());
            ybReconsiderRule.setRno(1);
            this.iYbReconsiderRuleService.createYbReconsiderRule(ybReconsiderRule);
        } catch (Exception e) {
            message = "新增/按钮失败";
            log.error(message, e);
            throw new FebsException(message);
        }
    }

    /**
     * 修改
     *
     * @param ybReconsiderRule
     * @return
     */
    @Log("修改")
    @PutMapping
    @RequiresPermissions("ybReconsiderRule:update")
    public void updateYbReconsiderRule(@Valid YbReconsiderRule ybReconsiderRule) throws FebsException {
        try {
            User currentUser = FebsUtil.getCurrentUser();
            ybReconsiderRule.setModifyUserId(currentUser.getUserId());
            ybReconsiderRule.setOperatorid(currentUser.getUserId());
            ybReconsiderRule.setOperatorname(currentUser.getUsername());
            ybReconsiderRule.setRno(1);
            this.iYbReconsiderRuleService.updateYbReconsiderRule(ybReconsiderRule);
        } catch (Exception e) {
            message = "修改失败";
            log.error(message, e);
            throw new FebsException(message);
        }
    }


    @Log("删除")
    @DeleteMapping("/{ids}")
    @RequiresPermissions("ybReconsiderRule:delete")
    public void deleteYbReconsiderRules(@NotBlank(message = "{required}") @PathVariable String ids) throws FebsException {
        try {
            String[] arr_ids = ids.split(StringPool.COMMA);
            this.iYbReconsiderRuleService.deleteYbReconsiderRules(arr_ids);
        } catch (Exception e) {
            message = "删除失败";
            log.error(message, e);
            throw new FebsException(message);
        }
    }

    @PostMapping("excel")
    @RequiresPermissions("ybReconsiderRule:export")
    public void export(QueryRequest request, YbReconsiderRule ybReconsiderRule, HttpServletResponse response) throws FebsException {
        try {
            List<YbReconsiderRule> ybReconsiderRules = this.iYbReconsiderRuleService.findYbReconsiderRules(request, ybReconsiderRule).getRecords();
            ExcelKit.$Export(YbReconsiderRule.class, response).downXlsx(ybReconsiderRules, false);
        } catch (Exception e) {
            message = "导出Excel失败";
            log.error(message, e);
            throw new FebsException(message);
        }
    }

    @GetMapping("/{id}")
    public YbReconsiderRule detail(@NotBlank(message = "{required}") @PathVariable String id) {
        YbReconsiderRule ybReconsiderRule = this.iYbReconsiderRuleService.getById(id);
        return ybReconsiderRule;
    }
}