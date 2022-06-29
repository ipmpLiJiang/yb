package cc.mrbird.febs.chs.controller;

import cc.mrbird.febs.common.annotation.Log;
import cc.mrbird.febs.common.controller.BaseController;
import cc.mrbird.febs.common.domain.router.VueRouter;
import cc.mrbird.febs.common.exception.FebsException;
import cc.mrbird.febs.common.domain.QueryRequest;

import cc.mrbird.febs.chs.service.IYbChsJkService;
import cc.mrbird.febs.chs.entity.YbChsJk;

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
 * @since 2022-06-21
 */
@Slf4j
@Validated
@RestController
@RequestMapping("ybChsJk")

public class YbChsJkController extends BaseController {

    private String message;
    @Autowired
    public IYbChsJkService iYbChsJkService;


    /**
     * 分页查询数据
     *
     * @param request 分页信息
     * @param ybChsJk 查询条件
     * @return
     */
    @GetMapping
    @RequiresPermissions("ybChsJk:view")
    public Map<String, Object> List(QueryRequest request, YbChsJk ybChsJk) {
        return getDataTable(this.iYbChsJkService.findYbChsJks(request, ybChsJk));
    }

    @GetMapping("findChsJkList")
    public Map<String, Object> findChsJks(QueryRequest request, YbChsJk ybChsJk) {
        return getDataTable(this.iYbChsJkService.findYbChsJkList(request, ybChsJk));
    }

    @GetMapping("getJkData")
    @RequiresPermissions("ybChsJk:look")
    public Map<String, Object> getJkDataByApplyDataId(QueryRequest request,String applyDataId) {
        return getDataTable(this.iYbChsJkService.findYbChsJkByApplyDataId(request,applyDataId));
    }

    /**
     * 添加
     *
     * @param ybChsJk
     * @return
     */
    @Log("新增/按钮")
    @PostMapping
    @RequiresPermissions("ybChsJk:add")
    public void addYbChsJk(@Valid YbChsJk ybChsJk) throws FebsException {
        try {
            User currentUser = FebsUtil.getCurrentUser();
//            ybChsJk.setCreateUserId(currentUser.getUserId());
            this.iYbChsJkService.createYbChsJk(ybChsJk);
        } catch (Exception e) {
            message = "新增/按钮失败";
            log.error(message, e);
            throw new FebsException(message);
        }
    }

    /**
     * 修改
     *
     * @param ybChsJk
     * @return
     */
    @Log("修改")
    @PutMapping
    @RequiresPermissions("ybChsJk:update")
    public void updateYbChsJk(@Valid YbChsJk ybChsJk) throws FebsException {
        try {
            User currentUser = FebsUtil.getCurrentUser();
//            ybChsJk.setModifyUserId(currentUser.getUserId());
            this.iYbChsJkService.updateYbChsJk(ybChsJk);
        } catch (Exception e) {
            message = "修改失败";
            log.error(message, e);
            throw new FebsException(message);
        }
    }


    @Log("删除")
    @DeleteMapping("/{ids}")
    @RequiresPermissions("ybChsJk:delete")
    public void deleteYbChsJks(@NotBlank(message = "{required}") @PathVariable String ids) throws FebsException {
        try {
            String[] arr_ids = ids.split(StringPool.COMMA);
            this.iYbChsJkService.deleteYbChsJks(arr_ids);
        } catch (Exception e) {
            message = "删除失败";
            log.error(message, e);
            throw new FebsException(message);
        }
    }

    @PostMapping("excel")
    @RequiresPermissions("ybChsJk:export")
    public void export(QueryRequest request, YbChsJk ybChsJk, HttpServletResponse response) throws FebsException {
        try {
            List<YbChsJk> ybChsJks = this.iYbChsJkService.findYbChsJks(request, ybChsJk).getRecords();
            ExcelKit.$Export(YbChsJk.class, response).downXlsx(ybChsJks, false);
        } catch (Exception e) {
            message = "导出Excel失败";
            log.error(message, e);
            throw new FebsException(message);
        }
    }

    @GetMapping("/{id}")
    public YbChsJk detail(@NotBlank(message = "{required}") @PathVariable String id) {
        YbChsJk ybChsJk = this.iYbChsJkService.getById(id);
        return ybChsJk;
    }


}