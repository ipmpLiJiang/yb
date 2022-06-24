package cc.mrbird.febs.chs.controller;

import cc.mrbird.febs.common.annotation.Log;
import cc.mrbird.febs.common.controller.BaseController;
import cc.mrbird.febs.common.domain.router.VueRouter;
import cc.mrbird.febs.common.exception.FebsException;
import cc.mrbird.febs.common.domain.QueryRequest;

import cc.mrbird.febs.chs.service.IYbChsVerifyService;
import cc.mrbird.febs.chs.entity.YbChsVerify;

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
 * @since 2022-06-20
 */
@Slf4j
@Validated
@RestController
@RequestMapping("ybChsVerify")

public class YbChsVerifyController extends BaseController {

    private String message;
    @Autowired
    public IYbChsVerifyService iYbChsVerifyService;


    /**
     * 分页查询数据
     *
     * @param request     分页信息
     * @param ybChsVerify 查询条件
     * @return
     */
    @GetMapping
    @RequiresPermissions("ybChsVerify:view")
    public Map<String, Object> List(QueryRequest request, YbChsVerify ybChsVerify) {
        return getDataTable(this.iYbChsVerifyService.findYbChsVerifys(request, ybChsVerify));
    }

    /**
     * 添加
     *
     * @param ybChsVerify
     * @return
     */
    @Log("新增/按钮")
    @PostMapping
    @RequiresPermissions("ybChsVerify:add")
    public void addYbChsVerify(@Valid YbChsVerify ybChsVerify) throws FebsException {
        try {
            User currentUser = FebsUtil.getCurrentUser();
//            ybChsVerify.setCreateUserId(currentUser.getUserId());
            this.iYbChsVerifyService.createYbChsVerify(ybChsVerify);
        } catch (Exception e) {
            message = "新增/按钮失败";
            log.error(message, e);
            throw new FebsException(message);
        }
    }

    /**
     * 修改
     *
     * @param ybChsVerify
     * @return
     */
    @Log("修改")
    @PutMapping
    @RequiresPermissions("ybChsVerify:update")
    public void updateYbChsVerify(@Valid YbChsVerify ybChsVerify) throws FebsException {
        try {
            User currentUser = FebsUtil.getCurrentUser();
//            ybChsVerify.setModifyUserId(currentUser.getUserId());
            this.iYbChsVerifyService.updateYbChsVerify(ybChsVerify);
        } catch (Exception e) {
            message = "修改失败";
            log.error(message, e);
            throw new FebsException(message);
        }
    }


    @Log("删除")
    @DeleteMapping("/{ids}")
    @RequiresPermissions("ybChsVerify:delete")
    public void deleteYbChsVerifys(@NotBlank(message = "{required}") @PathVariable String ids) throws FebsException {
        try {
            String[] arr_ids = ids.split(StringPool.COMMA);
            this.iYbChsVerifyService.deleteYbChsVerifys(arr_ids);
        } catch (Exception e) {
            message = "删除失败";
            log.error(message, e);
            throw new FebsException(message);
        }
    }

    @PostMapping("excel")
    @RequiresPermissions("ybChsVerify:export")
    public void export(QueryRequest request, YbChsVerify ybChsVerify, HttpServletResponse response) throws FebsException {
        try {
            List<YbChsVerify> ybChsVerifys = this.iYbChsVerifyService.findYbChsVerifys(request, ybChsVerify).getRecords();
            ExcelKit.$Export(YbChsVerify.class, response).downXlsx(ybChsVerifys, false);
        } catch (Exception e) {
            message = "导出Excel失败";
            log.error(message, e);
            throw new FebsException(message);
        }
    }

    @GetMapping("/{id}")
    public YbChsVerify detail(@NotBlank(message = "{required}") @PathVariable String id) {
        YbChsVerify ybChsVerify = this.iYbChsVerifyService.getById(id);
        return ybChsVerify;
    }
}