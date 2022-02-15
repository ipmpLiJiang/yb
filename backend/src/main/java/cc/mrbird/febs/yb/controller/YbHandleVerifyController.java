package cc.mrbird.febs.yb.controller;

import cc.mrbird.febs.common.annotation.Log;
import cc.mrbird.febs.common.controller.BaseController;
import cc.mrbird.febs.common.domain.router.VueRouter;
import cc.mrbird.febs.common.exception.FebsException;
import cc.mrbird.febs.common.domain.QueryRequest;

import cc.mrbird.febs.yb.service.IYbHandleVerifyService;
import cc.mrbird.febs.yb.entity.YbHandleVerify;

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
@RequestMapping("ybHandleVerify")

public class YbHandleVerifyController extends BaseController {

    private String message;
    @Autowired
    public IYbHandleVerifyService iYbHandleVerifyService;


    /**
     * 分页查询数据
     *
     * @param request        分页信息
     * @param ybHandleVerify 查询条件
     * @return
     */
    @GetMapping
    @RequiresPermissions("ybHandleVerify:view")
    public Map<String, Object> List(QueryRequest request, YbHandleVerify ybHandleVerify) {
        return getDataTable(this.iYbHandleVerifyService.findYbHandleVerifys(request, ybHandleVerify));
    }

    /**
     * 添加
     *
     * @param ybHandleVerify
     * @return
     */
    @Log("新增/按钮")
    @PostMapping
    @RequiresPermissions("ybHandleVerify:add")
    public void addYbHandleVerify(@Valid YbHandleVerify ybHandleVerify) throws FebsException {
        try {
            User currentUser = FebsUtil.getCurrentUser();
            ybHandleVerify.setCreateUserId(currentUser.getUserId());
            this.iYbHandleVerifyService.createYbHandleVerify(ybHandleVerify);
        } catch (Exception e) {
            message = "新增/按钮失败";
            log.error(message, e);
            throw new FebsException(message);
        }
    }

    /**
     * 修改
     *
     * @param ybHandleVerify
     * @return
     */
    @Log("修改")
    @PutMapping
    @RequiresPermissions("ybHandleVerify:update")
    public void updateYbHandleVerify(@Valid YbHandleVerify ybHandleVerify) throws FebsException {
        try {
            User currentUser = FebsUtil.getCurrentUser();
            ybHandleVerify.setModifyUserId(currentUser.getUserId());
            this.iYbHandleVerifyService.updateYbHandleVerify(ybHandleVerify);
        } catch (Exception e) {
            message = "修改失败";
            log.error(message, e);
            throw new FebsException(message);
        }
    }

    @Log("删除")
    @DeleteMapping("/{ids}")
    @RequiresPermissions("ybHandleVerify:delete")
    public void deleteYbHandleVerifys(@NotBlank(message = "{required}") @PathVariable String ids) throws FebsException {
        try {
            String[] arr_ids = ids.split(StringPool.COMMA);
            this.iYbHandleVerifyService.deleteYbHandleVerifys(arr_ids);
        } catch (Exception e) {
            message = "删除失败";
            log.error(message, e);
            throw new FebsException(message);
        }
    }

    @PostMapping("excel")
    @RequiresPermissions("ybHandleVerify:export")
    public void export(QueryRequest request, YbHandleVerify ybHandleVerify, HttpServletResponse response) throws FebsException {
        try {
            List<YbHandleVerify> ybHandleVerifys = this.iYbHandleVerifyService.findYbHandleVerifys(request, ybHandleVerify).getRecords();
            ExcelKit.$Export(YbHandleVerify.class, response).downXlsx(ybHandleVerifys, false);
        } catch (Exception e) {
            message = "导出Excel失败";
            log.error(message, e);
            throw new FebsException(message);
        }
    }

    @GetMapping("/{id}")
    public YbHandleVerify detail(@NotBlank(message = "{required}") @PathVariable String id) {
        YbHandleVerify ybHandleVerify = this.iYbHandleVerifyService.getById(id);
        return ybHandleVerify;
    }
}