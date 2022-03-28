package cc.mrbird.febs.drg.controller;

import cc.mrbird.febs.common.annotation.Log;
import cc.mrbird.febs.common.controller.BaseController;
import cc.mrbird.febs.common.domain.router.VueRouter;
import cc.mrbird.febs.common.exception.FebsException;
import cc.mrbird.febs.common.domain.QueryRequest;

import cc.mrbird.febs.drg.service.IYbDrgRelateService;
import cc.mrbird.febs.drg.entity.YbDrgRelate;

import cc.mrbird.febs.common.utils.FebsUtil;
import cc.mrbird.febs.export.excel.ExportExcelUtils;
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
 * @since 2022-03-16
 */
@Slf4j
@Validated
@RestController
@RequestMapping("ybDrgRelate")

public class YbDrgRelateController extends BaseController {

    private String message;
    @Autowired
    public IYbDrgRelateService iYbDrgRelateService;


    /**
     * 分页查询数据
     *
     * @param request     分页信息
     * @param ybDrgRelate 查询条件
     * @return
     */
    @GetMapping
    @RequiresPermissions("ybDrgRelate:view")
    public Map<String, Object> List(QueryRequest request, YbDrgRelate ybDrgRelate) {
        return getDataTable(this.iYbDrgRelateService.findYbDrgRelates(request, ybDrgRelate));
    }

    /**
     * 添加
     *
     * @param ybDrgRelate
     * @return
     */
    @Log("新增/按钮")
    @PostMapping
    @RequiresPermissions("ybDrgRelate:add")
    public void addYbDrgRelate(@Valid YbDrgRelate ybDrgRelate) throws FebsException {
        try {
            User currentUser = FebsUtil.getCurrentUser();
//        ybDrgRelate.setCreateUserId(currentUser.getUserId());
            this.iYbDrgRelateService.createYbDrgRelate(ybDrgRelate);
        } catch (Exception e) {
            message = "新增/按钮失败";
            log.error(message, e);
            throw new FebsException(message);
        }
    }

    /**
     * 修改
     *
     * @param ybDrgRelate
     * @return
     */
    @Log("修改")
    @PutMapping
    @RequiresPermissions("ybDrgRelate:update")
    public void updateYbDrgRelate(@Valid YbDrgRelate ybDrgRelate) throws FebsException {
        try {
            User currentUser = FebsUtil.getCurrentUser();
//      ybDrgRelate.setModifyUserId(currentUser.getUserId());
            this.iYbDrgRelateService.updateYbDrgRelate(ybDrgRelate);
        } catch (Exception e) {
            message = "修改失败";
            log.error(message, e);
            throw new FebsException(message);
        }
    }


    @Log("删除")
    @DeleteMapping("/{ids}")
    @RequiresPermissions("ybDrgRelate:delete")
    public void deleteYbDrgRelates(@NotBlank(message = "{required}") @PathVariable String ids) throws FebsException {
        try {
            String[] arr_ids = ids.split(StringPool.COMMA);
            this.iYbDrgRelateService.deleteYbDrgRelates(arr_ids);
        } catch (Exception e) {
            message = "删除失败";
            log.error(message, e);
            throw new FebsException(message);
        }
    }

    @PostMapping("excel")
    @RequiresPermissions("ybDrgRelate:export")
    public void export(QueryRequest request, YbDrgRelate ybDrgRelate, String dataJson, HttpServletResponse response) throws FebsException {
        try {
//        List<YbDrgRelate> ybDrgRelates = this.iYbDrgRelateService.findYbDrgRelates(request, ybDrgRelate).getRecords();
//        ExcelKit.$Export(YbDrgRelate.class, response).downXlsx(ybDrgRelates, false);
            List<YbDrgRelate> ybDrgDkss = this.iYbDrgRelateService.list();
            ExportExcelUtils.exportCustomExcel(response, ybDrgDkss, dataJson, "Sheel1");
        } catch (Exception e) {
            message = "导出Excel失败";
            log.error(message, e);
            throw new FebsException(message);
        }
    }

    @GetMapping("/{id}")
    public YbDrgRelate detail(@NotBlank(message = "{required}") @PathVariable String id) {
        YbDrgRelate ybDrgRelate = this.iYbDrgRelateService.getById(id);
        return ybDrgRelate;
    }
}