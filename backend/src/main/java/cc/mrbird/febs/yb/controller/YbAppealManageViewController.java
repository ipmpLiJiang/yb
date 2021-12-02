package cc.mrbird.febs.yb.controller;

import cc.mrbird.febs.common.annotation.Log;
import cc.mrbird.febs.common.controller.BaseController;
import cc.mrbird.febs.common.domain.router.VueRouter;
import cc.mrbird.febs.common.exception.FebsException;
import cc.mrbird.febs.common.domain.QueryRequest;

import cc.mrbird.febs.yb.service.IYbAppealManageViewService;
import cc.mrbird.febs.yb.entity.YbAppealManageView;

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
 * @since 2020-07-30
 */
@Slf4j
@Validated
@RestController
@RequestMapping("ybAppealManageView")

public class YbAppealManageViewController extends BaseController {

    private String message;
    @Autowired
    public IYbAppealManageViewService iYbAppealManageViewService;


    /**
     * 分页查询数据
     *
     * @param request            分页信息
     * @param ybAppealManageView 查询条件
     * @return
     */
    @GetMapping
    @RequiresPermissions("ybAppealManageView:view")
    public Map<String, Object> List(QueryRequest request, YbAppealManageView ybAppealManageView, String keyField) {
        if (ybAppealManageView.getCurrencyField() != null && !ybAppealManageView.getCurrencyField().equals("")) {
            System.out.println("View-New");
            return getDataTable(this.iYbAppealManageViewService.findAppealManageViewNew(request, ybAppealManageView, keyField, false));
        } else {
            System.out.println("View-Old");
            return getDataTable(this.iYbAppealManageViewService.findYbAppealManageViews(request, ybAppealManageView, keyField));
        }
    }

    @GetMapping("appealManageCount")
    @RequiresPermissions("ybAppealManageView:view")
    public int viewCount(YbAppealManageView ybAppealManageView, String keyField) {
        try {
            return this.iYbAppealManageViewService.findYbAppealManageCounts(ybAppealManageView, keyField);
        } catch (Exception e) {
            return 0;
        }
    }

    @GetMapping("appealManageUserView")
    @RequiresPermissions("ybAppealManageView:userView")
    public Map<String, Object> userList(QueryRequest request, YbAppealManageView ybAppealManageView, String keyField) {
        User currentUser = FebsUtil.getCurrentUser();
        ybAppealManageView.setReadyDoctorCode(currentUser.getUsername());
        return getDataTable(this.iYbAppealManageViewService.findAppealManageViewNew(request, ybAppealManageView, keyField, false));
//        return getDataTable(this.iYbAppealManageViewService.findAppealManageUserViews(request, ybAppealManageView,keyField));
    }

    @GetMapping("appealManageOperateRoomView")
    @RequiresPermissions("ybAppealManageView:operateRoomView")
    public Map<String, Object> operateRoomList(QueryRequest request, YbAppealManageView ybAppealManageView, String keyField) {
        User currentUser = FebsUtil.getCurrentUser();
        ybAppealManageView.setOrderDoctorCode(currentUser.getUsername());
        return getDataTable(this.iYbAppealManageViewService.findAppealManageViewNew(request, ybAppealManageView, keyField, false));
//        return getDataTable(this.iYbAppealManageViewService.findAppealManageOperateRoomViews(request, ybAppealManageView,keyField));
    }

    @GetMapping("appealManageConfireView")
    @RequiresPermissions("ybAppealManageView:confireView")
    public Map<String, Object> confireList(QueryRequest request, YbAppealManageView ybAppealManageView, String keyField) {
        User currentUser = FebsUtil.getCurrentUser();
        ybAppealManageView.setReadyDoctorCode(currentUser.getUsername());
        return getDataTable(this.iYbAppealManageViewService.findAppealManageViewNew(request, ybAppealManageView, keyField, true));
//        return getDataTable(this.iYbAppealManageViewService.findAppealManageConfireViews(request, ybAppealManageView,currentUser,keyField));
    }

    /**
     * 添加
     *
     * @param ybAppealManageView
     * @return
     */
    @Log("新增/按钮")
    @PostMapping
    @RequiresPermissions("ybAppealManageView:add")
    public void addYbAppealManageView(@Valid YbAppealManageView ybAppealManageView) throws FebsException {
        try {
            User currentUser = FebsUtil.getCurrentUser();
//            ybAppealManageView.setCreateUserId(currentUser.getUserId());
            this.iYbAppealManageViewService.createYbAppealManageView(ybAppealManageView);
        } catch (Exception e) {
            message = "新增/按钮失败";
            log.error(message, e);
            throw new FebsException(message);
        }
    }

    /**
     * 修改
     *
     * @param ybAppealManageView
     * @return
     */
    @Log("修改")
    @PutMapping
    @RequiresPermissions("ybAppealManageView:update")
    public void updateYbAppealManageView(@Valid YbAppealManageView ybAppealManageView) throws FebsException {
        try {
            User currentUser = FebsUtil.getCurrentUser();
//            ybAppealManageView.setModifyUserId(currentUser.getUserId());
            this.iYbAppealManageViewService.updateYbAppealManageView(ybAppealManageView);
        } catch (Exception e) {
            message = "修改失败";
            log.error(message, e);
            throw new FebsException(message);
        }
    }


    @Log("删除")
    @DeleteMapping("/{ids}")
    @RequiresPermissions("ybAppealManageView:delete")
    public void deleteYbAppealManageViews(@NotBlank(message = "{required}") @PathVariable String ids) throws FebsException {
        try {
            String[] arr_ids = ids.split(StringPool.COMMA);
            this.iYbAppealManageViewService.deleteYbAppealManageViews(arr_ids);
        } catch (Exception e) {
            message = "删除失败";
            log.error(message, e);
            throw new FebsException(message);
        }
    }

    @PostMapping("excel")
    @RequiresPermissions("ybAppealManageView:export")
    public void export(QueryRequest request, YbAppealManageView ybAppealManageView, HttpServletResponse response) throws FebsException {
        try {
            List<YbAppealManageView> ybAppealManageViews = this.iYbAppealManageViewService.findYbAppealManageViews(request, ybAppealManageView, null).getRecords();
            ExcelKit.$Export(YbAppealManageView.class, response).downXlsx(ybAppealManageViews, false);
        } catch (Exception e) {
            message = "导出Excel失败";
            log.error(message, e);
            throw new FebsException(message);
        }
    }

    @GetMapping("/{id}")
    public YbAppealManageView detail(@NotBlank(message = "{required}") @PathVariable String id) {
        YbAppealManageView ybAppealManageView = this.iYbAppealManageViewService.getById(id);
        return ybAppealManageView;
    }
}