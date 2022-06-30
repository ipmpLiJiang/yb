package cc.mrbird.febs.chs.controller;

import cc.mrbird.febs.common.controller.BaseController;
import cc.mrbird.febs.common.domain.QueryRequest;
import cc.mrbird.febs.common.utils.FebsUtil;
import cc.mrbird.febs.chs.entity.YbChsManageView;
import cc.mrbird.febs.chs.service.IYbChsManageViewService;
import cc.mrbird.febs.system.domain.User;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

/**
 * @author viki
 * @since 2020-07-30
 */
@Slf4j
@Validated
@RestController
@RequestMapping("ybChsManageView")

public class YbChsManageViewController extends BaseController {

    private String message;
    @Autowired
    public IYbChsManageViewService iYbChsManageViewService;

    @GetMapping()
    @RequiresPermissions("ybChsManageView:view")
    public Map<String, Object> List(QueryRequest request, YbChsManageView ybChsManageView, String keyField) {
        if (StringUtils.isNotBlank(ybChsManageView.getCurrencyField())) {
            return getDataTable(this.iYbChsManageViewService.findYbChsManageLikeViews(request, ybChsManageView, keyField, false));
        } else {
            return getDataTable(this.iYbChsManageViewService.findYbChsManageViews(request, ybChsManageView));
        }
    }

    @GetMapping("chsManageUserView")
    @RequiresPermissions("ybChsManageView:userView")
    public Map<String, Object> userList(QueryRequest request, YbChsManageView ybChsManageView, String keyField) {
        User currentUser = FebsUtil.getCurrentUser();
        ybChsManageView.setReadyDoctorCode(currentUser.getUsername());
        return getDataTable(this.iYbChsManageViewService.findYbChsManageLikeViews(request, ybChsManageView, keyField,false));
    }

    @GetMapping("chsManageDeptView")
    @RequiresPermissions("ybChsManageView:deptView")
    public Map<String, Object> deptList(QueryRequest request, YbChsManageView ybChsManageView, String keyField) {
        User currentUser = FebsUtil.getCurrentUser();
        ybChsManageView.setReadyDoctorCode(currentUser.getUsername());
        return getDataTable(this.iYbChsManageViewService.findYbChsManageLikeViews(request, ybChsManageView, keyField,true));
    }
}