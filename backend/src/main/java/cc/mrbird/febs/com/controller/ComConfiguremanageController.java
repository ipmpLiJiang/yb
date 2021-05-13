package cc.mrbird.febs.com.controller;

import cc.mrbird.febs.com.entity.OutComArea;
import cc.mrbird.febs.com.manager.ComConfigureManager;
import cc.mrbird.febs.common.annotation.Log;
import cc.mrbird.febs.common.controller.BaseController;
import cc.mrbird.febs.common.domain.FebsResponse;
import cc.mrbird.febs.common.domain.router.VueRouter;
import cc.mrbird.febs.common.exception.FebsException;
import cc.mrbird.febs.common.domain.QueryRequest;

import cc.mrbird.febs.com.service.IComConfiguremanageService;
import cc.mrbird.febs.com.entity.ComConfiguremanage;

import cc.mrbird.febs.common.utils.FebsUtil;
import cc.mrbird.febs.system.domain.User;
import cc.mrbird.febs.yb.domain.ResponseResult;
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
 * @since 2020-08-05
 */
@Slf4j
@Validated
@RestController
@RequestMapping("comConfiguremanage")

public class ComConfiguremanageController extends BaseController {

    private String message;
    @Autowired
    public IComConfiguremanageService iComConfiguremanageService;

    @Autowired
    ComConfigureManager configureManager;

    /**
     * 分页查询数据
     *
     * @param request            分页信息
     * @param comConfiguremanage 查询条件
     * @return
     */
    @GetMapping
    @RequiresPermissions("comConfiguremanage:view")
    public Map<String, Object> List(QueryRequest request, ComConfiguremanage comConfiguremanage) {
        return getDataTable(this.iComConfiguremanageService.findComConfiguremanages(request, comConfiguremanage));
    }

    @GetMapping("getAreaList")
    public List<OutComArea> getAreaLists() {
        List<ComConfiguremanage> list = configureManager.getConfigures(5,"area");
        List<OutComArea> outAreaList = new ArrayList<>();
        for (ComConfiguremanage item : list){
            OutComArea area = new OutComArea();
            area.setAreaType(item.getIntField());
            area.setAreaName(item.getStringField());
            outAreaList.add(area);
        }
        return outAreaList;
    }

    @GetMapping("saveAreaCache")
    @RequiresPermissions("comConfiguremanage:add")
    public FebsResponse addAreaCache()  {
        int success = 0;
        try {
            configureManager.saveConfigureCache(5,"area");
            success = 1;
        } catch (Exception e) {
            message = "更新Area缓存失败";
            log.error(message, e);
        }

        ResponseResult rr = new ResponseResult();
        rr.setSuccess(success);
        rr.setMessage(message);
        return new FebsResponse().data(rr);
    }
    /**
     * 添加
     *
     * @param comConfiguremanage
     * @return
     */
    @Log("新增/按钮")
    @PostMapping
    @RequiresPermissions("comConfiguremanage:add")
    public void addComConfiguremanage(@Valid ComConfiguremanage comConfiguremanage) throws FebsException {
        try {
            User currentUser = FebsUtil.getCurrentUser();
            comConfiguremanage.setCreateUserId(currentUser.getUserId());
            this.iComConfiguremanageService.createComConfiguremanage(comConfiguremanage);
        } catch (Exception e) {
            message = "新增/按钮失败";
            log.error(message, e);
            throw new FebsException(message);
        }
    }


    /**
     * 修改
     *
     * @param comConfiguremanage
     * @return
     */
    @Log("修改")
    @PutMapping
    @RequiresPermissions("comConfiguremanage:update")
    public void updateComConfiguremanage(@Valid ComConfiguremanage comConfiguremanage) throws FebsException {
        try {
            User currentUser = FebsUtil.getCurrentUser();
            comConfiguremanage.setModifyUserId(currentUser.getUserId());
            this.iComConfiguremanageService.updateComConfiguremanage(comConfiguremanage);
        } catch (Exception e) {
            message = "修改失败";
            log.error(message, e);
            throw new FebsException(message);
        }
    }


    @Log("删除")
    @DeleteMapping("/{ids}")
    @RequiresPermissions("comConfiguremanage:delete")
    public void deleteComConfiguremanages(@NotBlank(message = "{required}") @PathVariable String ids) throws FebsException {
        try {
            String[] arr_ids = ids.split(StringPool.COMMA);
            this.iComConfiguremanageService.deleteComConfiguremanages(arr_ids);
        } catch (Exception e) {
            message = "删除失败";
            log.error(message, e);
            throw new FebsException(message);
        }
    }

    @PostMapping("excel")
    @RequiresPermissions("comConfiguremanage:export")
    public void export(QueryRequest request, ComConfiguremanage comConfiguremanage, HttpServletResponse response) throws FebsException {
        try {
            List<ComConfiguremanage> comConfiguremanages = this.iComConfiguremanageService.findComConfiguremanages(request, comConfiguremanage).getRecords();
            ExcelKit.$Export(ComConfiguremanage.class, response).downXlsx(comConfiguremanages, false);
        } catch (Exception e) {
            message = "导出Excel失败";
            log.error(message, e);
            throw new FebsException(message);
        }
    }

    @GetMapping("/{id}")
    public ComConfiguremanage detail(@NotBlank(message = "{required}") @PathVariable String id) {
        ComConfiguremanage comConfiguremanage = this.iComConfiguremanageService.getById(id);
        return comConfiguremanage;
    }
}