package cc.mrbird.febs.yb.controller;

import cc.mrbird.febs.common.annotation.Log;
import cc.mrbird.febs.common.controller.BaseController;
import cc.mrbird.febs.common.domain.router.VueRouter;
import cc.mrbird.febs.common.exception.FebsException;
import cc.mrbird.febs.common.domain.QueryRequest;

import cc.mrbird.febs.yb.service.IYbReconsiderInpatientfeesService;
import cc.mrbird.febs.yb.entity.YbReconsiderInpatientfees;

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
 * @since 2020-07-22
 */
@Slf4j
@Validated
@RestController
@RequestMapping("ybReconsiderInpatientfees")

public class YbReconsiderInpatientfeesController extends BaseController {

    private String message;
    @Autowired
    public IYbReconsiderInpatientfeesService iYbReconsiderInpatientfeesService;


    /**
     * 分页查询数据
     *
     * @param request                   分页信息
     * @param ybReconsiderInpatientfees 查询条件
     * @return
     */
    @GetMapping
    @RequiresPermissions("ybReconsiderInpatientfees:view")
    public Map<String, Object> List(QueryRequest request, YbReconsiderInpatientfees ybReconsiderInpatientfees) {
        return getDataTable(this.iYbReconsiderInpatientfeesService.findYbReconsiderInpatientfeess(request, ybReconsiderInpatientfees));
    }

    @GetMapping("ListEq")
    @RequiresPermissions("ybReconsiderInpatientfees:viewEq")
    public Map<String, Object> List1(QueryRequest request, YbReconsiderInpatientfees ybReconsiderInpatientfees) {
        return getDataTable(this.iYbReconsiderInpatientfeesService.findYbReconsiderInpatientfeesEqs(request, ybReconsiderInpatientfees));
    }

    /**
     * 添加
     *
     * @param ybReconsiderInpatientfees
     * @return
     */
    @Log("新增/按钮")
    @PostMapping
    @RequiresPermissions("ybReconsiderInpatientfees:add")
    public void addYbReconsiderInpatientfees(@Valid YbReconsiderInpatientfees ybReconsiderInpatientfees) throws FebsException {
        try {
            User currentUser = FebsUtil.getCurrentUser();
            ybReconsiderInpatientfees.setCreateUserId(currentUser.getUserId());
            this.iYbReconsiderInpatientfeesService.createYbReconsiderInpatientfees(ybReconsiderInpatientfees);
        } catch (Exception e) {
            message = "新增/按钮失败";
            log.error(message, e);
            throw new FebsException(message);
        }
    }

    /**
     * 修改
     *
     * @param ybReconsiderInpatientfees
     * @return
     */
    @Log("修改")
    @PutMapping
    @RequiresPermissions("ybReconsiderInpatientfees:update")
    public void updateYbReconsiderInpatientfees(@Valid YbReconsiderInpatientfees ybReconsiderInpatientfees) throws FebsException {
        try {
            User currentUser = FebsUtil.getCurrentUser();
            ybReconsiderInpatientfees.setModifyUserId(currentUser.getUserId());
            this.iYbReconsiderInpatientfeesService.updateYbReconsiderInpatientfees(ybReconsiderInpatientfees);
        } catch (Exception e) {
            message = "修改失败";
            log.error(message, e);
            throw new FebsException(message);
        }
    }


    @Log("删除")
    @DeleteMapping("/{ids}")
    @RequiresPermissions("ybReconsiderInpatientfees:delete")
    public void deleteYbReconsiderInpatientfeess(@NotBlank(message = "{required}") @PathVariable String ids) throws FebsException {
        try {
            String[] arr_ids = ids.split(StringPool.COMMA);
            this.iYbReconsiderInpatientfeesService.deleteYbReconsiderInpatientfeess(arr_ids);
        } catch (Exception e) {
            message = "删除失败";
            log.error(message, e);
            throw new FebsException(message);
        }
    }

    @PostMapping("excel")
    @RequiresPermissions("ybReconsiderInpatientfees:export")
    public void export(QueryRequest request, YbReconsiderInpatientfees ybReconsiderInpatientfees, HttpServletResponse response) throws FebsException {
        try {
            List<YbReconsiderInpatientfees> ybReconsiderInpatientfeess = this.iYbReconsiderInpatientfeesService.findYbReconsiderInpatientfeess(request, ybReconsiderInpatientfees).getRecords();
            ExcelKit.$Export(YbReconsiderInpatientfees.class, response).downXlsx(ybReconsiderInpatientfeess, false);
        } catch (Exception e) {
            message = "导出Excel失败";
            log.error(message, e);
            throw new FebsException(message);
        }
    }

    @GetMapping("/{id}")
    public YbReconsiderInpatientfees detail(@NotBlank(message = "{required}") @PathVariable String id) {
        YbReconsiderInpatientfees ybReconsiderInpatientfees = this.iYbReconsiderInpatientfeesService.getById(id);
        return ybReconsiderInpatientfees;
    }
}