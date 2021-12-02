package cc.mrbird.febs.drg.controller;

import cc.mrbird.febs.common.annotation.Log;
import cc.mrbird.febs.common.controller.BaseController;
import cc.mrbird.febs.common.domain.QueryRequest;
import cc.mrbird.febs.common.exception.FebsException;
import cc.mrbird.febs.common.utils.FebsUtil;
import cc.mrbird.febs.system.domain.User;
import cc.mrbird.febs.drg.entity.YbDrgManageView;
import cc.mrbird.febs.drg.service.IYbDrgManageViewService;
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
import java.util.List;
import java.util.Map;

/**
 * @author viki
 * @since 2020-07-30
 */
@Slf4j
@Validated
@RestController
@RequestMapping("ybDrgManageView")

public class YbDrgManageViewController extends BaseController {

    private String message;
    @Autowired
    public IYbDrgManageViewService iYbDrgManageViewService;

    @GetMapping("drgManageUserView")
    @RequiresPermissions("ybDrgManageView:userView")
    public Map<String, Object> userList(QueryRequest request, YbDrgManageView ybDrgManageView, String keyField) {
        User currentUser = FebsUtil.getCurrentUser();
        ybDrgManageView.setReadyDoctorCode(currentUser.getUsername());
        return getDataTable(this.iYbDrgManageViewService.findDrgManageView(request, ybDrgManageView, keyField));
    }
}