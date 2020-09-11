package cc.mrbird.febs.yb.controller;

import cc.mrbird.febs.common.annotation.Log;
import cc.mrbird.febs.common.controller.BaseController;
import cc.mrbird.febs.common.domain.router.VueRouter;
import cc.mrbird.febs.common.exception.FebsException;
import cc.mrbird.febs.common.domain.QueryRequest;

import cc.mrbird.febs.yb.service.IYbReconsiderResetDataViewService;
import cc.mrbird.febs.yb.entity.YbReconsiderResetDataView;

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
 * @since 2020-08-18
 */
@Slf4j
@Validated
@RestController
@RequestMapping("ybReconsiderResetDataView")

public class YbReconsiderResetDataViewController extends BaseController {

    private String message;
    @Autowired
    public IYbReconsiderResetDataViewService iYbReconsiderResetDataViewService;


    /**
     * 分页查询数据
     *
     * @param request                   分页信息
     * @param ybReconsiderResetDataView 查询条件
     * @return
     */
    @GetMapping
    @RequiresPermissions("ybReconsiderResetDataView:view")
    public Map<String, Object> List(QueryRequest request, YbReconsiderResetDataView ybReconsiderResetDataView) {
        return getDataTable(this.iYbReconsiderResetDataViewService.findYbReconsiderResetDataViews(request, ybReconsiderResetDataView));
    }

    /**
     * 添加
     *
     * @param ybReconsiderResetDataView
     * @return
     */
    @Log("新增/按钮")
    @PostMapping
    @RequiresPermissions("ybReconsiderResetDataView:add")
    public void addYbReconsiderResetDataView(@Valid YbReconsiderResetDataView ybReconsiderResetDataView) throws FebsException {
        try {
            User currentUser = FebsUtil.getCurrentUser();
//        ybReconsiderResetDataView.setCreateUserId(currentUser.getUserId());
            this.iYbReconsiderResetDataViewService.createYbReconsiderResetDataView(ybReconsiderResetDataView);
        } catch (Exception e) {
            message = "新增/按钮失败";
            log.error(message, e);
            throw new FebsException(message);
        }
    }

    /**
     * 修改
     *
     * @param ybReconsiderResetDataView
     * @return
     */
    @Log("修改")
    @PutMapping
    @RequiresPermissions("ybReconsiderResetDataView:update")
    public void updateYbReconsiderResetDataView(@Valid YbReconsiderResetDataView ybReconsiderResetDataView) throws FebsException {
        try {
            User currentUser = FebsUtil.getCurrentUser();
//      ybReconsiderResetDataView.setModifyUserId(currentUser.getUserId());
            this.iYbReconsiderResetDataViewService.updateYbReconsiderResetDataView(ybReconsiderResetDataView);
        } catch (Exception e) {
            message = "修改失败";
            log.error(message, e);
            throw new FebsException(message);
        }
    }


    @Log("删除")
    @DeleteMapping("/{ids}")
    @RequiresPermissions("ybReconsiderResetDataView:delete")
    public void deleteYbReconsiderResetDataViews(@NotBlank(message = "{required}") @PathVariable String ids) throws FebsException {
        try {
            String[] arr_ids = ids.split(StringPool.COMMA);
            this.iYbReconsiderResetDataViewService.deleteYbReconsiderResetDataViews(arr_ids);
        } catch (Exception e) {
            message = "删除失败";
            log.error(message, e);
            throw new FebsException(message);
        }
    }

    @PostMapping("excel")
    @RequiresPermissions("ybReconsiderResetDataView:export")
    public void export(QueryRequest request, YbReconsiderResetDataView ybReconsiderResetDataView, HttpServletResponse response) throws FebsException {
        try {
            List<YbReconsiderResetDataView> ybReconsiderResetDataViews = this.iYbReconsiderResetDataViewService.findYbReconsiderResetDataViews(request, ybReconsiderResetDataView).getRecords();
            ExcelKit.$Export(YbReconsiderResetDataView.class, response).downXlsx(ybReconsiderResetDataViews, false);
        } catch (Exception e) {
            message = "导出Excel失败";
            log.error(message, e);
            throw new FebsException(message);
        }
    }

    @GetMapping("/{id}")
    public YbReconsiderResetDataView detail(@NotBlank(message = "{required}") @PathVariable String id) {
        YbReconsiderResetDataView ybReconsiderResetDataView = this.iYbReconsiderResetDataViewService.getById(id);
        return ybReconsiderResetDataView;
    }
}