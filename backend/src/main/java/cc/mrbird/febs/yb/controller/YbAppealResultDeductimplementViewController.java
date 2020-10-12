package cc.mrbird.febs.yb.controller;

import cc.mrbird.febs.common.annotation.Log;
import cc.mrbird.febs.common.controller.BaseController;
import cc.mrbird.febs.common.domain.router.VueRouter;
import cc.mrbird.febs.common.exception.FebsException;
import cc.mrbird.febs.common.domain.QueryRequest;

import cc.mrbird.febs.yb.service.IYbAppealResultDeductimplementViewService;
import cc.mrbird.febs.yb.entity.YbAppealResultDeductimplementView;

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
 * @since 2020-09-24
 */
@Slf4j
@Validated
@RestController
@RequestMapping("ybAppealResultDeductimplementView")

public class YbAppealResultDeductimplementViewController extends BaseController {

    private String message;
    @Autowired
    public IYbAppealResultDeductimplementViewService iYbAppealResultDeductimplementViewService;


    /**
     * 分页查询数据
     *
     * @param request                           分页信息
     * @param ybAppealResultDeductimplementView 查询条件
     * @return
     */
    @GetMapping
    @RequiresPermissions("ybAppealResultDeductimplementView:view")
    public Map<String, Object> List(QueryRequest request, YbAppealResultDeductimplementView ybAppealResultDeductimplementView) {
        return getDataTable(this.iYbAppealResultDeductimplementViewService.findYbAppealResultDeductimplementViews(request, ybAppealResultDeductimplementView));
    }

    @GetMapping("findAppealResultDeductimplementViewList")
    @RequiresPermissions("ybAppealResultDeductimplementView:view")
    public Map<String, Object> List1(QueryRequest request, YbAppealResultDeductimplementView ybAppealResultDeductimplementView) {
        return getDataTable(this.iYbAppealResultDeductimplementViewService.findYbAppealResultDeductimplementViewList(request, ybAppealResultDeductimplementView));
    }

    @GetMapping("findAppealResultDeductimplementUserView")
    @RequiresPermissions("ybAppealResultDeductimplementView:userView")
    public Map<String, Object> userList(QueryRequest request, YbAppealResultDeductimplementView ybAppealResultDeductimplementView) {
        User currentUser = FebsUtil.getCurrentUser();
        ybAppealResultDeductimplementView.setArDoctorCode(currentUser.getUsername());
        return getDataTable(this.iYbAppealResultDeductimplementViewService.findYbAppealResultDeductimplementViews(request, ybAppealResultDeductimplementView));
    }

    /**
     * 添加
     *
     * @param ybAppealResultDeductimplementView
     * @return
     */
    @Log("新增/按钮")
    @PostMapping
    @RequiresPermissions("ybAppealResultDeductimplementView:add")
    public void addYbAppealResultDeductimplementView(@Valid YbAppealResultDeductimplementView ybAppealResultDeductimplementView) throws FebsException {
        try {
            User currentUser = FebsUtil.getCurrentUser();
            ybAppealResultDeductimplementView.setCreateUserId(currentUser.getUserId());
            this.iYbAppealResultDeductimplementViewService.createYbAppealResultDeductimplementView(ybAppealResultDeductimplementView);
        } catch (Exception e) {
            message = "新增/按钮失败";
            log.error(message, e);
            throw new FebsException(message);
        }
    }

    /**
     * 修改
     *
     * @param ybAppealResultDeductimplementView
     * @return
     */
    @Log("修改")
    @PutMapping
    @RequiresPermissions("ybAppealResultDeductimplementView:update")
    public void updateYbAppealResultDeductimplementView(@Valid YbAppealResultDeductimplementView ybAppealResultDeductimplementView) throws FebsException {
        try {
            User currentUser = FebsUtil.getCurrentUser();
            ybAppealResultDeductimplementView.setModifyUserId(currentUser.getUserId());
            this.iYbAppealResultDeductimplementViewService.updateYbAppealResultDeductimplementView(ybAppealResultDeductimplementView);
        } catch (Exception e) {
            message = "修改失败";
            log.error(message, e);
            throw new FebsException(message);
        }
    }


    @Log("删除")
    @DeleteMapping("/{ids}")
    @RequiresPermissions("ybAppealResultDeductimplementView:delete")
    public void deleteYbAppealResultDeductimplementViews(@NotBlank(message = "{required}") @PathVariable String ids) throws FebsException {
        try {
            String[] arr_ids = ids.split(StringPool.COMMA);
            this.iYbAppealResultDeductimplementViewService.deleteYbAppealResultDeductimplementViews(arr_ids);
        } catch (Exception e) {
            message = "删除失败";
            log.error(message, e);
            throw new FebsException(message);
        }
    }

    @PostMapping("excel")
    @RequiresPermissions("ybAppealResultDeductimplementView:export")
    public void export(QueryRequest request, YbAppealResultDeductimplementView ybAppealResultDeductimplementView, HttpServletResponse response) throws FebsException {
        try {
            List<YbAppealResultDeductimplementView> ybAppealResultDeductimplementViews = this.iYbAppealResultDeductimplementViewService.findYbAppealResultDeductimplementViews(request, ybAppealResultDeductimplementView).getRecords();
            ExcelKit.$Export(YbAppealResultDeductimplementView.class, response).downXlsx(ybAppealResultDeductimplementViews, false);
        } catch (Exception e) {
            message = "导出Excel失败";
            log.error(message, e);
            throw new FebsException(message);
        }
    }

    @GetMapping("/{id}")
    public YbAppealResultDeductimplementView detail(@NotBlank(message = "{required}") @PathVariable String id) {
        YbAppealResultDeductimplementView ybAppealResultDeductimplementView = this.iYbAppealResultDeductimplementViewService.getById(id);
        return ybAppealResultDeductimplementView;
    }
}