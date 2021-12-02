package cc.mrbird.febs.drg.controller;

import cc.mrbird.febs.common.annotation.Log;
import cc.mrbird.febs.common.controller.BaseController;
import cc.mrbird.febs.common.domain.router.VueRouter;
import cc.mrbird.febs.common.exception.FebsException;
import cc.mrbird.febs.common.domain.QueryRequest;

import cc.mrbird.febs.drg.service.IYbDrgJkService;
import cc.mrbird.febs.drg.entity.YbDrgJk;

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
 * @since 2021-11-23
 */
@Slf4j
@Validated
@RestController
@RequestMapping("ybDrgJk")

public class YbDrgJkController extends BaseController {

    private String message;
    @Autowired
    public IYbDrgJkService iYbDrgJkService;


    /**
     * 分页查询数据
     *
     * @param request 分页信息
     * @param ybDrgJk 查询条件
     * @return
     */
    @GetMapping
    @RequiresPermissions("ybDrgJk:view")
    public Map<String, Object> List(QueryRequest request, YbDrgJk ybDrgJk) {
        return getDataTable(this.iYbDrgJkService.findYbDrgJks(request, ybDrgJk));
    }

    /**
     * 添加
     *
     * @param ybDrgJk
     * @return
     */
    @Log("新增/按钮")
    @PostMapping
    @RequiresPermissions("ybDrgJk:add")
    public void addYbDrgJk(@Valid YbDrgJk ybDrgJk) throws FebsException {
        try {
            User currentUser = FebsUtil.getCurrentUser();
//            ybDrgJk.setCreateUserId(currentUser.getUserId());
            this.iYbDrgJkService.createYbDrgJk(ybDrgJk);
        } catch (Exception e) {
            message = "新增/按钮失败";
            log.error(message, e);
            throw new FebsException(message);
        }
    }

    /**
     * 修改
     *
     * @param ybDrgJk
     * @return
     */
    @Log("修改")
    @PutMapping
    @RequiresPermissions("ybDrgJk:update")
    public void updateYbDrgJk(@Valid YbDrgJk ybDrgJk) throws FebsException {
        try {
            User currentUser = FebsUtil.getCurrentUser();
//            ybDrgJk.setModifyUserId(currentUser.getUserId());
            this.iYbDrgJkService.updateYbDrgJk(ybDrgJk);
        } catch (Exception e) {
            message = "修改失败";
            log.error(message, e);
            throw new FebsException(message);
        }
    }


    @Log("删除")
    @DeleteMapping("/{ids}")
    @RequiresPermissions("ybDrgJk:delete")
    public void deleteYbDrgJks(@NotBlank(message = "{required}") @PathVariable String ids) throws FebsException {
        try {
            String[] arr_ids = ids.split(StringPool.COMMA);
            this.iYbDrgJkService.deleteYbDrgJks(arr_ids);
        } catch (Exception e) {
            message = "删除失败";
            log.error(message, e);
            throw new FebsException(message);
        }
    }

    @PostMapping("excel")
    @RequiresPermissions("ybDrgJk:export")
    public void export(QueryRequest request, YbDrgJk ybDrgJk, HttpServletResponse response) throws FebsException {
        try {
            List<YbDrgJk> ybDrgJks = this.iYbDrgJkService.findYbDrgJks(request, ybDrgJk).getRecords();
            ExcelKit.$Export(YbDrgJk.class, response).downXlsx(ybDrgJks, false);
        } catch (Exception e) {
            message = "导出Excel失败";
            log.error(message, e);
            throw new FebsException(message);
        }
    }

    @GetMapping("/{id}")
    public YbDrgJk detail(@NotBlank(message = "{required}") @PathVariable String id) {
        YbDrgJk ybDrgJk = this.iYbDrgJkService.getById(id);
        return ybDrgJk;
    }

    @GetMapping("getJkData")
    @RequiresPermissions("ybDrgJk:look")
    public YbDrgJk detailByApplyDataId(String applyDataId) {
        YbDrgJk ybDrgJk = this.iYbDrgJkService.findYbDrgJkByApplyDataId(applyDataId);
        return ybDrgJk;
    }
}