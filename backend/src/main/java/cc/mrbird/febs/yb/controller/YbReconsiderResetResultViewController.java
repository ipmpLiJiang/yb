package cc.mrbird.febs.yb.controller;

import cc.mrbird.febs.common.annotation.Log;
import cc.mrbird.febs.common.controller.BaseController;
import cc.mrbird.febs.common.domain.router.VueRouter;
import cc.mrbird.febs.common.exception.FebsException;
import cc.mrbird.febs.common.domain.QueryRequest;

import cc.mrbird.febs.yb.service.IYbReconsiderResetResultViewService;
import cc.mrbird.febs.yb.entity.YbReconsiderResetResultView;

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
 * @since 2020-09-10
 */
@Slf4j
@Validated
@RestController
@RequestMapping("ybReconsiderResetResultView")

public class YbReconsiderResetResultViewController extends BaseController {

    private String message;
    @Autowired
    public IYbReconsiderResetResultViewService iYbReconsiderResetResultViewService;


    /**
     * 分页查询数据
     *
     * @param request                     分页信息
     * @param ybReconsiderResetResultView 查询条件
     * @return
     */
    @GetMapping
    @RequiresPermissions("ybReconsiderResetResultView:view")
    public Map<String, Object> List(QueryRequest request, YbReconsiderResetResultView ybReconsiderResetResultView) {
        return getDataTable(this.iYbReconsiderResetResultViewService.findYbReconsiderResetResultViews(request, ybReconsiderResetResultView));
    }

    /**
     * 添加
     *
     * @param ybReconsiderResetResultView
     * @return
     */
    @Log("新增/按钮")
    @PostMapping
    @RequiresPermissions("ybReconsiderResetResultView:add")
    public void addYbReconsiderResetResultView(@Valid YbReconsiderResetResultView ybReconsiderResetResultView) throws FebsException {
        try {
            User currentUser = FebsUtil.getCurrentUser();
            // ybReconsiderResetResultView.setCreateUserId(currentUser.getUserId());
            this.iYbReconsiderResetResultViewService.createYbReconsiderResetResultView(ybReconsiderResetResultView);
        } catch (Exception e) {
            message = "新增/按钮失败";
            log.error(message, e);
            throw new FebsException(message);
        }
    }

    /**
     * 修改
     *
     * @param ybReconsiderResetResultView
     * @return
     */
    @Log("修改")
    @PutMapping
    @RequiresPermissions("ybReconsiderResetResultView:update")
    public void updateYbReconsiderResetResultView(@Valid YbReconsiderResetResultView ybReconsiderResetResultView) throws FebsException {
        try {
            User currentUser = FebsUtil.getCurrentUser();
            //ybReconsiderResetResultView.setModifyUserId(currentUser.getUserId());
            this.iYbReconsiderResetResultViewService.updateYbReconsiderResetResultView(ybReconsiderResetResultView);
        } catch (Exception e) {
            message = "修改失败";
            log.error(message, e);
            throw new FebsException(message);
        }
    }


    @Log("删除")
    @DeleteMapping("/{ids}")
    @RequiresPermissions("ybReconsiderResetResultView:delete")
    public void deleteYbReconsiderResetResultViews(@NotBlank(message = "{required}") @PathVariable String ids) throws FebsException {
        try {
            String[] arr_ids = ids.split(StringPool.COMMA);
            this.iYbReconsiderResetResultViewService.deleteYbReconsiderResetResultViews(arr_ids);
        } catch (Exception e) {
            message = "删除失败";
            log.error(message, e);
            throw new FebsException(message);
        }
    }

    @PostMapping("excel")
    @RequiresPermissions("ybReconsiderResetResultView:export")
    public void export(QueryRequest request, YbReconsiderResetResultView ybReconsiderResetResultView, HttpServletResponse response) throws FebsException {
        try {
            List<YbReconsiderResetResultView> ybReconsiderResetResultViews = this.iYbReconsiderResetResultViewService.findYbReconsiderResetResultViews(request, ybReconsiderResetResultView).getRecords();
            ExcelKit.$Export(YbReconsiderResetResultView.class, response).downXlsx(ybReconsiderResetResultViews, false);
        } catch (Exception e) {
            message = "导出Excel失败";
            log.error(message, e);
            throw new FebsException(message);
        }
    }

    @GetMapping("/{id}")
    public YbReconsiderResetResultView detail(@NotBlank(message = "{required}") @PathVariable String id) {
        YbReconsiderResetResultView ybReconsiderResetResultView = this.iYbReconsiderResetResultViewService.getById(id);
        return ybReconsiderResetResultView;
    }
}