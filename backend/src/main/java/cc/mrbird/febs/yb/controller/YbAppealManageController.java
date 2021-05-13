package cc.mrbird.febs.yb.controller;

import cc.mrbird.febs.common.annotation.Log;
import cc.mrbird.febs.common.controller.BaseController;
import cc.mrbird.febs.common.domain.FebsResponse;
import cc.mrbird.febs.common.domain.QueryRequest;
import cc.mrbird.febs.common.exception.FebsException;
import cc.mrbird.febs.common.utils.FebsUtil;
import cc.mrbird.febs.system.domain.User;
import cc.mrbird.febs.yb.domain.ResponseResult;
import cc.mrbird.febs.yb.entity.YbAppealManage;
import cc.mrbird.febs.yb.service.IYbAppealManageService;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
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
import java.util.List;
import java.util.Map;

/**
 * @author viki
 * @since 2020-07-30
 */
@Slf4j
@Validated
@RestController
@RequestMapping("ybAppealManage")

public class YbAppealManageController extends BaseController {

    private String message;
    @Autowired
    public IYbAppealManageService iYbAppealManageService;


    /**
     * 分页查询数据
     *
     * @param request        分页信息
     * @param ybAppealManage 查询条件
     * @return
     */
    @GetMapping
    @RequiresPermissions("ybAppealManage:view")
    public Map<String, Object> List(QueryRequest request, YbAppealManage ybAppealManage) {
        return getDataTable(this.iYbAppealManageService.findYbAppealManages(request, ybAppealManage));
    }

    /**
     * 添加
     *
     * @param ybAppealManage
     * @return
     */
    @Log("新增/按钮")
    @PostMapping
    @RequiresPermissions("ybAppealManage:add")
    public void addYbAppealManage(@Valid YbAppealManage ybAppealManage) throws FebsException {
        try {
            User currentUser = FebsUtil.getCurrentUser();
//            ybAppealManage.setCreateUserId(currentUser.getUserId());
            this.iYbAppealManageService.createYbAppealManage(ybAppealManage);
        } catch (Exception e) {
            message = "新增/按钮失败";
            log.error(message, e);
            throw new FebsException(message);
        }
    }

    /**
     * 修改
     *
     * @param ybAppealManage
     * @return
     */
    @Log("修改")
    @PutMapping
    @RequiresPermissions("ybAppealManage:update")
    public void updateYbAppealManage(@Valid YbAppealManage ybAppealManage) throws FebsException {
        try {
            User currentUser = FebsUtil.getCurrentUser();
//            ybAppealManage.setModifyUserId(currentUser.getUserId());
            this.iYbAppealManageService.updateYbAppealManage(ybAppealManage);
        } catch (Exception e) {
            message = "修改失败";
            log.error(message, e);
            throw new FebsException(message);
        }
    }

    @Log("修改")
    @PutMapping("updateCreateAppealManage")
    @RequiresPermissions("ybAppealManage:amCreateUpdate")
    public void updateCreateAppealManage(@Valid YbAppealManage ybAppealManage, Integer type) throws FebsException {
        try {
            User currentUser = FebsUtil.getCurrentUser();
            Long uid = currentUser.getUserId();
            String uname = currentUser.getUsername();

            this.iYbAppealManageService.updateCreateAppealManage(ybAppealManage, uid, uname, type);
        } catch (Exception e) {
            message = "修改失败";
            log.error(message, e);
            throw new FebsException(message);
        }
    }

    @Log("修改")
    @PutMapping("updateCreateAdminAppealManage")
    @RequiresPermissions("ybAppealManage:adminCreateUpdate")
    public void updateCreateAdminAppealManage(@Valid YbAppealManage ybAppealManage) throws FebsException {
        try {
            User currentUser = FebsUtil.getCurrentUser();
            Long uid = currentUser.getUserId();
            String uname = currentUser.getUsername();

            this.iYbAppealManageService.updateCreateAdminAppealManage(ybAppealManage, uid, uname);
        } catch (Exception e) {
            message = "更改失败";
            log.error(message, e);
            throw new FebsException(message);
        }
    }

    @Log("修改")
    @PutMapping("updateAcceptRejectState")
    @RequiresPermissions("ybAppealManage:acceptRejectStateUpdate")
    public void updateAcceptRejectState(String dataJson) throws FebsException {
        try {
            User currentUser = FebsUtil.getCurrentUser();
            Long uid = currentUser.getUserId();
            String uname = currentUser.getUsername();

            List<YbAppealManage> list = JSON.parseObject(dataJson, new TypeReference<List<YbAppealManage>>() {
            });

            this.iYbAppealManageService.updateAcceptRejectStates(list, uid, uname);
        } catch (Exception e) {
            message = "操作失败";
            log.error(message, e);
            throw new FebsException(message);
        }
    }

    @Log("修改")
    @PutMapping("updateUploadState")
    @RequiresPermissions("ybAppealManage:uploadState")
    public FebsResponse updateUploadState(String dataJson) {
        int success = 0;
        try {
            User currentUser = FebsUtil.getCurrentUser();
            Long uid = currentUser.getUserId();
            String uname = currentUser.getUsername();

            YbAppealManage ybAppealManage = JSON.parseObject(dataJson, new TypeReference<YbAppealManage>() {
            });

            message = this.iYbAppealManageService.updateUploadStates(ybAppealManage, uid, uname);
            if (message.equals("ok")) {
                success = 1;
                message = "操作成功.";
            }
        } catch (Exception e) {
            message = "操作失败.";
            log.error(message, e);
        }

        ResponseResult rr = new ResponseResult();
        rr.setMessage(message);
        rr.setSuccess(success);

        return new FebsResponse().data(rr);
    }

    @Log("修改")
    @PutMapping("updateUploadStateCompleted")
    @RequiresPermissions("ybAppealManage:uploadState")
    public FebsResponse updateUploadStateCompleted(String dataJson) {
        int success = 0;
        try {
            User currentUser = FebsUtil.getCurrentUser();
            Long uid = currentUser.getUserId();
            String uname = currentUser.getUsername();

            YbAppealManage ybAppealManage = JSON.parseObject(dataJson, new TypeReference<YbAppealManage>() {
            });

            message = this.iYbAppealManageService.updateUploadStateCompleteds(ybAppealManage, uid, uname);
            if (message.equals("ok")) {
                success = 1;
                message = "操作成功.";
            }
        } catch (Exception e) {
            message = "操作失败.";
            log.error(message, e);
        }

        ResponseResult rr = new ResponseResult();
        rr.setMessage(message);
        rr.setSuccess(success);

        return new FebsResponse().data(rr);
    }


    @Log("删除")
    @DeleteMapping("/{ids}")
    @RequiresPermissions("ybAppealManage:delete")
    public void deleteYbAppealManages(@NotBlank(message = "{required}") @PathVariable String ids) throws FebsException {
        try {
            String[] arr_ids = ids.split(StringPool.COMMA);
            this.iYbAppealManageService.deleteYbAppealManages(arr_ids);
        } catch (Exception e) {
            message = "删除失败";
            log.error(message, e);
            throw new FebsException(message);
        }
    }

    @PostMapping("excel")
    @RequiresPermissions("ybAppealManage:export")
    public void export(QueryRequest request, YbAppealManage ybAppealManage, HttpServletResponse response) throws FebsException {
        try {
            List<YbAppealManage> ybAppealManages = this.iYbAppealManageService.findYbAppealManages(request, ybAppealManage).getRecords();
            ExcelKit.$Export(YbAppealManage.class, response).downXlsx(ybAppealManages, false);
        } catch (Exception e) {
            message = "导出Excel失败";
            log.error(message, e);
            throw new FebsException(message);
        }
    }

    @GetMapping("/{id}")
    public YbAppealManage detail(@NotBlank(message = "{required}") @PathVariable String id) {
        YbAppealManage ybAppealManage = this.iYbAppealManageService.getById(id);
        return ybAppealManage;
    }
}