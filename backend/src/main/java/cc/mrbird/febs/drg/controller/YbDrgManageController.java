package cc.mrbird.febs.drg.controller;

import cc.mrbird.febs.common.annotation.Log;
import cc.mrbird.febs.common.controller.BaseController;
import cc.mrbird.febs.common.domain.router.VueRouter;
import cc.mrbird.febs.common.exception.FebsException;
import cc.mrbird.febs.common.domain.QueryRequest;

import cc.mrbird.febs.drg.service.IYbDrgManageService;
import cc.mrbird.febs.drg.entity.YbDrgManage;

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
@RequestMapping("ybDrgManage")

public class YbDrgManageController extends BaseController {

    private String message;
    @Autowired
    public IYbDrgManageService iYbDrgManageService;


    /**
     * 分页查询数据
     *
     * @param request     分页信息
     * @param ybDrgManage 查询条件
     * @return
     */
    @GetMapping
    @RequiresPermissions("ybDrgManage:view")
    public Map<String, Object> List(QueryRequest request, YbDrgManage ybDrgManage) {
        return getDataTable(this.iYbDrgManageService.findYbDrgManages(request, ybDrgManage));
    }

    /**
     * 添加
     *
     * @param ybDrgManage
     * @return
     */
    @Log("新增/按钮")
    @PostMapping
    @RequiresPermissions("ybDrgManage:add")
    public void addYbDrgManage(@Valid YbDrgManage ybDrgManage) throws FebsException {
        try {
            User currentUser = FebsUtil.getCurrentUser();
//            ybDrgManage.setCreateUserId(currentUser.getUserId());
            this.iYbDrgManageService.createYbDrgManage(ybDrgManage);
        } catch (Exception e) {
            message = "新增/按钮失败";
            log.error(message, e);
            throw new FebsException(message);
        }
    }

    /**
     * 修改
     *
     * @param ybDrgManage
     * @return
     */
    @Log("修改")
    @PutMapping
    @RequiresPermissions("ybDrgManage:update")
    public void updateYbDrgManage(@Valid YbDrgManage ybDrgManage) throws FebsException {
        try {
            User currentUser = FebsUtil.getCurrentUser();
//            ybDrgManage.setModifyUserId(currentUser.getUserId());
            this.iYbDrgManageService.updateYbDrgManage(ybDrgManage);
        } catch (Exception e) {
            message = "修改失败";
            log.error(message, e);
            throw new FebsException(message);
        }
    }


    @Log("删除")
    @DeleteMapping("/{ids}")
    @RequiresPermissions("ybDrgManage:delete")
    public void deleteYbDrgManages(@NotBlank(message = "{required}") @PathVariable String ids) throws FebsException {
        try {
            String[] arr_ids = ids.split(StringPool.COMMA);
            this.iYbDrgManageService.deleteYbDrgManages(arr_ids);
        } catch (Exception e) {
            message = "删除失败";
            log.error(message, e);
            throw new FebsException(message);
        }
    }

    @PostMapping("excel")
    @RequiresPermissions("ybDrgManage:export")
    public void export(QueryRequest request, YbDrgManage ybDrgManage, HttpServletResponse response) throws FebsException {
        try {
            List<YbDrgManage> ybDrgManages = this.iYbDrgManageService.findYbDrgManages(request, ybDrgManage).getRecords();
            ExcelKit.$Export(YbDrgManage.class, response).downXlsx(ybDrgManages, false);
        } catch (Exception e) {
            message = "导出Excel失败";
            log.error(message, e);
            throw new FebsException(message);
        }
    }

    @GetMapping("/{id}")
    public YbDrgManage detail(@NotBlank(message = "{required}") @PathVariable String id) {
        YbDrgManage ybDrgManage = this.iYbDrgManageService.getById(id);
        return ybDrgManage;
    }
}