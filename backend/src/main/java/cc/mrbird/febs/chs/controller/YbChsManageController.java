package cc.mrbird.febs.chs.controller;

import cc.mrbird.febs.common.annotation.Log;
import cc.mrbird.febs.common.controller.BaseController;
import cc.mrbird.febs.common.domain.FebsResponse;
import cc.mrbird.febs.common.domain.router.VueRouter;
import cc.mrbird.febs.common.exception.FebsException;
import cc.mrbird.febs.common.domain.QueryRequest;

import cc.mrbird.febs.chs.service.IYbChsManageService;
import cc.mrbird.febs.chs.entity.YbChsManage;

import com.alibaba.fastjson.TypeReference;
import com.alibaba.fastjson.JSON;
import cc.mrbird.febs.common.utils.FebsUtil;
import cc.mrbird.febs.system.domain.User;
import com.baomidou.mybatisplus.core.toolkit.StringPool;
import com.wuwenze.poi.ExcelKit;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.ModelMap;
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
@RequestMapping("ybChsManage")

public class YbChsManageController extends BaseController {

    private String message;
    @Autowired
    public IYbChsManageService iYbChsManageService;


    /**
     * 分页查询数据
     *
     * @param request     分页信息
     * @param ybChsManage 查询条件
     * @return
     */
    @GetMapping
    @RequiresPermissions("ybChsManage:view")
    public Map<String, Object> List(QueryRequest request, YbChsManage ybChsManage) {
        return getDataTable(this.iYbChsManageService.findYbChsManages(request, ybChsManage));
    }

    /**
     * 添加
     *
     * @param ybChsManage
     * @return
     */
    @Log("新增/按钮")
    @PostMapping
    @RequiresPermissions("ybChsManage:add")
    public void addYbChsManage(@Valid YbChsManage ybChsManage) throws FebsException {
        try {
            User currentUser = FebsUtil.getCurrentUser();
//            ybChsManage.setCreateUserId(currentUser.getUserId());
            this.iYbChsManageService.createYbChsManage(ybChsManage);
        } catch (Exception e) {
            message = "新增/按钮失败";
            log.error(message, e);
            throw new FebsException(message);
        }
    }

    /**
     * 修改
     *
     * @param ybChsManage
     * @return
     */
    @Log("修改")
    @PutMapping
    @RequiresPermissions("ybChsManage:update")
    public void updateYbChsManage(@Valid YbChsManage ybChsManage) throws FebsException {
        try {
            User currentUser = FebsUtil.getCurrentUser();
//            ybChsManage.setModifyUserId(currentUser.getUserId());
            this.iYbChsManageService.updateYbChsManage(ybChsManage);
        } catch (Exception e) {
            message = "修改失败";
            log.error(message, e);
            throw new FebsException(message);
        }
    }


    @Log("删除")
    @DeleteMapping("/{ids}")
    @RequiresPermissions("ybChsManage:delete")
    public void deleteYbChsManages(@NotBlank(message = "{required}") @PathVariable String ids) throws FebsException {
        try {
            String[] arr_ids = ids.split(StringPool.COMMA);
            this.iYbChsManageService.deleteYbChsManages(arr_ids);
        } catch (Exception e) {
            message = "删除失败";
            log.error(message, e);
            throw new FebsException(message);
        }
    }

    @PostMapping("excel")
    @RequiresPermissions("ybChsManage:export")
    public void export(QueryRequest request, YbChsManage ybChsManage, HttpServletResponse response) throws FebsException {
        try {
            List<YbChsManage> ybChsManages = this.iYbChsManageService.findYbChsManages(request, ybChsManage).getRecords();
            ExcelKit.$Export(YbChsManage.class, response).downXlsx(ybChsManages, false);
        } catch (Exception e) {
            message = "导出Excel失败";
            log.error(message, e);
            throw new FebsException(message);
        }
    }

    @GetMapping("/{id}")
    public YbChsManage detail(@NotBlank(message = "{required}") @PathVariable String id) {
        YbChsManage ybChsManage = this.iYbChsManageService.getById(id);
        return ybChsManage;
    }

    @Log("修改")
    @PutMapping("updateAcceptRejectState")
    @RequiresPermissions("ybChsManage:acceptRejectStateUpdate")
    public void updateAcceptRejectState(String dataJson) throws FebsException {
        try {
            List<YbChsManage> list = JSON.parseObject(dataJson, new TypeReference<List<YbChsManage>>() {
            });

            this.iYbChsManageService.updateAcceptRejectStates(list);
        } catch (Exception e) {
            message = "操作失败";
            log.error(message, e);
            throw new FebsException(message);
        }
    }


    @Log("修改")
    @PutMapping("updateUploadState")
    @RequiresPermissions("ybChsManage:uploadState")
    public FebsResponse updateUploadState(String dataJson) {
        ModelMap map = new ModelMap();
        int success = 0;
        try {
            YbChsManage ybChsManage = JSON.parseObject(dataJson, new TypeReference<YbChsManage>() {
            });

            message = this.iYbChsManageService.updateUploadStates(ybChsManage);
            if (message.equals("ok")) {
                success = 1;
                message = "操作成功.";
            }
        } catch (Exception e) {
            message = "操作失败.";
            log.error(message, e);
        }

        map.put("message",message);
        map.put("success",success);
        return  new FebsResponse().data(map);
    }


    @Log("修改")
    @PutMapping("updateUploadStateCompleted")
    @RequiresPermissions("ybChsManage:uploadState")
    public FebsResponse updateUploadStateCompleted(String dataJson) {
        ModelMap map = new ModelMap();
        int success = 0;
        try {
            YbChsManage ybChsManage = JSON.parseObject(dataJson, new TypeReference<YbChsManage>() {
            });

            message = this.iYbChsManageService.updateUploadStateCompleteds(ybChsManage);
            if (message.equals("ok")) {
                success = 1;
                message = "操作成功.";
            }
        } catch (Exception e) {
            message = "操作失败.";
            log.error(message, e);
        }

        map.put("message",message);
        map.put("success",success);
        return  new FebsResponse().data(map);
    }

    @Log("修改")
    @PutMapping("updateCreateChsManage")
    @RequiresPermissions("ybChsManage:amCreateUpdate")
    public void updateCreateChsManage(@Valid YbChsManage ybChsManage, Integer type) throws FebsException {
        try {
            User currentUser = FebsUtil.getCurrentUser();
            Long uid = currentUser.getUserId();
            String uname = currentUser.getUsername();

            this.iYbChsManageService.updateCreateChsManage(ybChsManage, uid, uname, type);
        } catch (Exception e) {
            message = "修改失败";
            log.error(message, e);
            throw new FebsException(message);
        }
    }

    @Log("修改")
    @PutMapping("updateCreateAdminChsManage")
    @RequiresPermissions("ybChsManage:amCreateUpdate")
    public void updateCreateAdminChsManage(@Valid YbChsManage ybChsManage) throws FebsException {
        try {
            User currentUser = FebsUtil.getCurrentUser();
            Long uid = currentUser.getUserId();
            String uname = currentUser.getUsername();

            this.iYbChsManageService.updateCreateAdminChsManage(ybChsManage, uid, uname);
        } catch (Exception e) {
            message = "更改失败";
            log.error(message, e);
            throw new FebsException(message);
        }
    }
}