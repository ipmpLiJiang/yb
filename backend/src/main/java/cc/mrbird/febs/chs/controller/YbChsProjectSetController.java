package cc.mrbird.febs.chs.controller;

import cc.mrbird.febs.common.annotation.Log;
import cc.mrbird.febs.common.controller.BaseController;
import cc.mrbird.febs.common.domain.router.VueRouter;
import cc.mrbird.febs.common.exception.FebsException;
import cc.mrbird.febs.common.domain.QueryRequest;

import cc.mrbird.febs.chs.service.IYbChsProjectSetService;
import cc.mrbird.febs.chs.entity.YbChsProjectSet;

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
 * @since 2022-10-12
 */
@Slf4j
@Validated
@RestController
@RequestMapping("ybChsProjectSet")

public class YbChsProjectSetController extends BaseController {

    private String message;
    @Autowired
    public IYbChsProjectSetService iYbChsProjectSetService;


    /**
     * 分页查询数据
     *
     * @param request         分页信息
     * @param ybChsProjectSet 查询条件
     * @return
     */
    @GetMapping
    @RequiresPermissions("ybChsProjectSet:view")
    public Map<String, Object> List(QueryRequest request, YbChsProjectSet ybChsProjectSet) {
        return getDataTable(this.iYbChsProjectSetService.findYbChsProjectSets(request, ybChsProjectSet));
    }

    /**
     * 添加
     *
     * @param ybChsProjectSet
     * @return
     */
    @Log("新增/按钮")
    @PostMapping
    @RequiresPermissions("ybChsProjectSet:add")
    public void addYbChsProjectSet(@Valid YbChsProjectSet ybChsProjectSet) throws FebsException {
        try {
            User currentUser = FebsUtil.getCurrentUser();
            ybChsProjectSet.setCreateUserId(currentUser.getUserId());
            ybChsProjectSet.setOperatorId(currentUser.getUserId());
            ybChsProjectSet.setOperatorName(currentUser.getUsername() + "-" + currentUser.getXmname());
            this.iYbChsProjectSetService.createYbChsProjectSet(ybChsProjectSet);
        } catch (Exception e) {
            message = "新增/按钮失败";
            log.error(message, e);
            throw new FebsException(message);
        }
    }

    /**
     * 修改
     *
     * @param ybChsProjectSet
     * @return
     */
    @Log("修改")
    @PutMapping
    @RequiresPermissions("ybChsProjectSet:update")
    public void updateYbChsProjectSet(@Valid YbChsProjectSet ybChsProjectSet) throws FebsException {
        try {
            User currentUser = FebsUtil.getCurrentUser();
            ybChsProjectSet.setModifyUserId(currentUser.getUserId());
            this.iYbChsProjectSetService.updateYbChsProjectSet(ybChsProjectSet);
        } catch (Exception e) {
            message = "修改失败";
            log.error(message, e);
            throw new FebsException(message);
        }
    }


    @Log("删除")
    @DeleteMapping("/{ids}")
    @RequiresPermissions("ybChsProjectSet:delete")
    public void deleteYbChsProjectSets(@NotBlank(message = "{required}") @PathVariable String ids) throws FebsException {
        try {
            String[] arr_ids = ids.split(StringPool.COMMA);
            this.iYbChsProjectSetService.deleteYbChsProjectSets(arr_ids);
        } catch (Exception e) {
            message = "删除失败";
            log.error(message, e);
            throw new FebsException(message);
        }
    }

    @PostMapping("excel")
    @RequiresPermissions("ybChsProjectSet:export")
    public void export(QueryRequest request, YbChsProjectSet ybChsProjectSet, HttpServletResponse response) throws FebsException {
        try {
            List<YbChsProjectSet> ybChsProjectSets = this.iYbChsProjectSetService.findYbChsProjectSets(request, ybChsProjectSet).getRecords();
            ExcelKit.$Export(YbChsProjectSet.class, response).downXlsx(ybChsProjectSets, false);
        } catch (Exception e) {
            message = "导出Excel失败";
            log.error(message, e);
            throw new FebsException(message);
        }
    }

    @GetMapping("/{id}")
    public YbChsProjectSet detail(@NotBlank(message = "{required}") @PathVariable String id) {
        YbChsProjectSet ybChsProjectSet = this.iYbChsProjectSetService.getById(id);
        return ybChsProjectSet;
    }
}
