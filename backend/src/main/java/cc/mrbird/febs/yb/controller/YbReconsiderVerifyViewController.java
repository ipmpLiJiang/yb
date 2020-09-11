package cc.mrbird.febs.yb.controller;

import cc.mrbird.febs.common.annotation.Log;
import cc.mrbird.febs.common.controller.BaseController;
import cc.mrbird.febs.common.domain.router.VueRouter;
import cc.mrbird.febs.common.exception.FebsException;
import cc.mrbird.febs.common.domain.QueryRequest;

import cc.mrbird.febs.yb.service.IYbReconsiderVerifyViewService;
import cc.mrbird.febs.yb.entity.YbReconsiderVerifyView;

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
@RequestMapping("ybReconsiderVerifyView")

public class YbReconsiderVerifyViewController extends BaseController {

    private String message;
    @Autowired
    public IYbReconsiderVerifyViewService iYbReconsiderVerifyViewService;


    /**
     * 分页查询数据
     *
     * @param request                分页信息
     * @param ybReconsiderVerifyView 查询条件
     * @return
     */
    @GetMapping
    @RequiresPermissions("ybReconsiderVerifyView:view")
    public Map<String, Object> List(QueryRequest request, YbReconsiderVerifyView ybReconsiderVerifyView) {
        return getDataTable(this.iYbReconsiderVerifyViewService.findYbReconsiderVerifyViews(request, ybReconsiderVerifyView));
    }

    @GetMapping("findVerifyViewNull")
    @RequiresPermissions("ybReconsiderVerifyView:nullview")
    public Map<String, Object> findVerifyViewNull(QueryRequest request, YbReconsiderVerifyView ybReconsiderVerifyView,String[] searchType) {
        return getDataTable(this.iYbReconsiderVerifyViewService.findYbReconsiderVerifyViewNulls(request, ybReconsiderVerifyView,searchType));
    }

    /**
     * 添加
     *
     * @param ybReconsiderVerifyView
     * @return
     */
    @Log("新增/按钮")
    @PostMapping
    @RequiresPermissions("ybReconsiderVerifyView:add")
    public void addYbReconsiderVerifyView(@Valid YbReconsiderVerifyView ybReconsiderVerifyView) throws FebsException {
        try {
            User currentUser = FebsUtil.getCurrentUser();
            ybReconsiderVerifyView.setCreateUserId(currentUser.getUserId());
            this.iYbReconsiderVerifyViewService.createYbReconsiderVerifyView(ybReconsiderVerifyView);
        } catch (Exception e) {
            message = "新增/按钮失败";
            log.error(message, e);
            throw new FebsException(message);
        }
    }

    /**
     * 修改
     *
     * @param ybReconsiderVerifyView
     * @return
     */
    @Log("修改")
    @PutMapping
    @RequiresPermissions("ybReconsiderVerifyView:update")
    public void updateYbReconsiderVerifyView(@Valid YbReconsiderVerifyView ybReconsiderVerifyView) throws FebsException {
        try {
            User currentUser = FebsUtil.getCurrentUser();
            ybReconsiderVerifyView.setModifyUserId(currentUser.getUserId());
            this.iYbReconsiderVerifyViewService.updateYbReconsiderVerifyView(ybReconsiderVerifyView);
        } catch (Exception e) {
            message = "修改失败";
            log.error(message, e);
            throw new FebsException(message);
        }
    }


    @Log("删除")
    @DeleteMapping("/{ids}")
    @RequiresPermissions("ybReconsiderVerifyView:delete")
    public void deleteYbReconsiderVerifyViews(@NotBlank(message = "{required}") @PathVariable String ids) throws FebsException {
        try {
            String[] arr_ids = ids.split(StringPool.COMMA);
            this.iYbReconsiderVerifyViewService.deleteYbReconsiderVerifyViews(arr_ids);
        } catch (Exception e) {
            message = "删除失败";
            log.error(message, e);
            throw new FebsException(message);
        }
    }

    @PostMapping("excel")
    @RequiresPermissions("ybReconsiderVerifyView:export")
    public void export(QueryRequest request, YbReconsiderVerifyView ybReconsiderVerifyView, HttpServletResponse response) throws FebsException {
        try {
            List<YbReconsiderVerifyView> ybReconsiderVerifyViews = this.iYbReconsiderVerifyViewService.findYbReconsiderVerifyViews(request, ybReconsiderVerifyView).getRecords();
            ExcelKit.$Export(YbReconsiderVerifyView.class, response).downXlsx(ybReconsiderVerifyViews, false);
        } catch (Exception e) {
            message = "导出Excel失败";
            log.error(message, e);
            throw new FebsException(message);
        }
    }

    @GetMapping("/{id}")
    public YbReconsiderVerifyView detail(@NotBlank(message = "{required}") @PathVariable String id) {
        YbReconsiderVerifyView ybReconsiderVerifyView = this.iYbReconsiderVerifyViewService.getById(id);
        return ybReconsiderVerifyView;
    }
}