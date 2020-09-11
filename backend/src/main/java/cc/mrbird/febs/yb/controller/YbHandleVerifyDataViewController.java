package cc.mrbird.febs.yb.controller;

import cc.mrbird.febs.common.annotation.Log;
import cc.mrbird.febs.common.controller.BaseController;
import cc.mrbird.febs.common.domain.router.VueRouter;
import cc.mrbird.febs.common.exception.FebsException;
import cc.mrbird.febs.common.domain.QueryRequest;

import cc.mrbird.febs.yb.service.IYbHandleVerifyDataViewService;
import cc.mrbird.febs.yb.entity.YbHandleVerifyDataView;

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
 * @since 2020-08-28
 */
@Slf4j
@Validated
@RestController
@RequestMapping("ybHandleVerifyDataView")

public class YbHandleVerifyDataViewController extends BaseController {

    private String message;
    @Autowired
    public IYbHandleVerifyDataViewService iYbHandleVerifyDataViewService;


    /**
     * 分页查询数据
     *
     * @param request                分页信息
     * @param ybHandleVerifyDataView 查询条件
     * @return
     */
    @GetMapping
    @RequiresPermissions("ybHandleVerifyDataView:view")
    public Map<String, Object> List(QueryRequest request, YbHandleVerifyDataView ybHandleVerifyDataView) {
        return getDataTable(this.iYbHandleVerifyDataViewService.findYbHandleVerifyDataViews(request, ybHandleVerifyDataView));
    }

    /**
     * 添加
     *
     * @param ybHandleVerifyDataView
     * @return
     */
    @Log("新增/按钮")
    @PostMapping
    @RequiresPermissions("ybHandleVerifyDataView:add")
    public void addYbHandleVerifyDataView(@Valid YbHandleVerifyDataView ybHandleVerifyDataView) throws FebsException {
        try {
            User currentUser = FebsUtil.getCurrentUser();
            //ybHandleVerifyDataView.setCreateUserId(currentUser.getUserId());
            this.iYbHandleVerifyDataViewService.createYbHandleVerifyDataView(ybHandleVerifyDataView);
        } catch (Exception e) {
            message = "新增/按钮失败";
            log.error(message, e);
            throw new FebsException(message);
        }
    }

    /**
     * 修改
     *
     * @param ybHandleVerifyDataView
     * @return
     */
    @Log("修改")
    @PutMapping
    @RequiresPermissions("ybHandleVerifyDataView:update")
    public void updateYbHandleVerifyDataView(@Valid YbHandleVerifyDataView ybHandleVerifyDataView) throws FebsException {
        try {
            User currentUser = FebsUtil.getCurrentUser();
            //ybHandleVerifyDataView.setModifyUserId(currentUser.getUserId());
            this.iYbHandleVerifyDataViewService.updateYbHandleVerifyDataView(ybHandleVerifyDataView);
        } catch (Exception e) {
            message = "修改失败";
            log.error(message, e);
            throw new FebsException(message);
        }
    }


    @Log("删除")
    @DeleteMapping("/{ids}")
    @RequiresPermissions("ybHandleVerifyDataView:delete")
    public void deleteYbHandleVerifyDataViews(@NotBlank(message = "{required}") @PathVariable String ids) throws FebsException {
        try {
            String[] arr_ids = ids.split(StringPool.COMMA);
            this.iYbHandleVerifyDataViewService.deleteYbHandleVerifyDataViews(arr_ids);
        } catch (Exception e) {
            message = "删除失败";
            log.error(message, e);
            throw new FebsException(message);
        }
    }

    @PostMapping("excel")
    @RequiresPermissions("ybHandleVerifyDataView:export")
    public void export(QueryRequest request, YbHandleVerifyDataView ybHandleVerifyDataView, HttpServletResponse response) throws FebsException {
        try {
            List<YbHandleVerifyDataView> ybHandleVerifyDataViews = this.iYbHandleVerifyDataViewService.findYbHandleVerifyDataViews(request, ybHandleVerifyDataView).getRecords();
            ExcelKit.$Export(YbHandleVerifyDataView.class, response).downXlsx(ybHandleVerifyDataViews, false);
        } catch (Exception e) {
            message = "导出Excel失败";
            log.error(message, e);
            throw new FebsException(message);
        }
    }

    @GetMapping("/{id}")
    public YbHandleVerifyDataView detail(@NotBlank(message = "{required}") @PathVariable String id) {
        YbHandleVerifyDataView ybHandleVerifyDataView = this.iYbHandleVerifyDataViewService.getById(id);
        return ybHandleVerifyDataView;
    }
}