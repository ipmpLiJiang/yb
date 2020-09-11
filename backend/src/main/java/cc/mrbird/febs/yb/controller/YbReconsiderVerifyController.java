package cc.mrbird.febs.yb.controller;

import cc.mrbird.febs.common.annotation.Log;
import cc.mrbird.febs.common.controller.BaseController;
import cc.mrbird.febs.common.domain.router.VueRouter;
import cc.mrbird.febs.common.exception.FebsException;
import cc.mrbird.febs.common.domain.QueryRequest;

import cc.mrbird.febs.yb.service.IYbReconsiderVerifyService;
import cc.mrbird.febs.yb.entity.YbReconsiderVerify;

import cc.mrbird.febs.common.utils.FebsUtil;
import cc.mrbird.febs.system.domain.User;
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
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @author viki
 * @since 2020-07-30
 */
@Slf4j
@Validated
@RestController
@RequestMapping("ybReconsiderVerify")

public class YbReconsiderVerifyController extends BaseController {

    private String message;
    @Autowired
    public IYbReconsiderVerifyService iYbReconsiderVerifyService;


    /**
     * 分页查询数据
     *
     * @param request            分页信息
     * @param ybReconsiderVerify 查询条件
     * @return
     */
    @GetMapping
    @RequiresPermissions("ybReconsiderVerify:view")
    public Map<String, Object> List(QueryRequest request, YbReconsiderVerify ybReconsiderVerify) {
        return getDataTable(this.iYbReconsiderVerifyService.findYbReconsiderVerifys(request, ybReconsiderVerify));
    }


    /**
     * 添加
     *
     * @param ybReconsiderVerify
     * @return
     */
    @Log("新增/按钮")
    @PostMapping
    @RequiresPermissions("ybReconsiderVerify:add")
    public void addYbReconsiderVerify(@Valid YbReconsiderVerify ybReconsiderVerify) throws FebsException {
        try {
            User currentUser = FebsUtil.getCurrentUser();
            ybReconsiderVerify.setCreateUserId(currentUser.getUserId());
            this.iYbReconsiderVerifyService.createYbReconsiderVerify(ybReconsiderVerify);
        } catch (Exception e) {
            message = "新增/按钮失败";
            log.error(message, e);
            throw new FebsException(message);
        }
    }

    /**
     * 修改
     *
     * @param ybReconsiderVerify
     * @return
     */
    @Log("修改")
    @PutMapping
    @RequiresPermissions("ybReconsiderVerify:update")
    public void updateYbReconsiderVerify(@Valid YbReconsiderVerify ybReconsiderVerify) throws FebsException {
        try {
            User currentUser = FebsUtil.getCurrentUser();
            ybReconsiderVerify.setModifyUserId(currentUser.getUserId());
            this.iYbReconsiderVerifyService.updateYbReconsiderVerify(ybReconsiderVerify);
        } catch (Exception e) {
            message = "修改失败";
            log.error(message, e);
            throw new FebsException(message);
        }
    }


    @Log("删除")
    @DeleteMapping("/{ids}")
    @RequiresPermissions("ybReconsiderVerify:delete")
    public void deleteYbReconsiderVerifys(@NotBlank(message = "{required}") @PathVariable String ids) throws FebsException {
        try {
            String[] arr_ids = ids.split(StringPool.COMMA);
            this.iYbReconsiderVerifyService.deleteYbReconsiderVerifys(arr_ids);
        } catch (Exception e) {
            message = "删除失败";
            log.error(message, e);
            throw new FebsException(message);
        }
    }

    @PostMapping("excel")
    @RequiresPermissions("ybReconsiderVerify:export")
    public void export(QueryRequest request, YbReconsiderVerify ybReconsiderVerify, HttpServletResponse response) throws FebsException {
        try {
            List<YbReconsiderVerify> ybReconsiderVerifys = this.iYbReconsiderVerifyService.findYbReconsiderVerifys(request, ybReconsiderVerify).getRecords();
            ExcelKit.$Export(YbReconsiderVerify.class, response).downXlsx(ybReconsiderVerifys, false);
        } catch (Exception e) {
            message = "导出Excel失败";
            log.error(message, e);
            throw new FebsException(message);
        }
    }

    @GetMapping("/{id}")
    public YbReconsiderVerify detail(@NotBlank(message = "{required}") @PathVariable String id) {
        YbReconsiderVerify ybReconsiderVerify = this.iYbReconsiderVerifyService.getById(id);
        return ybReconsiderVerify;
    }

    @PostMapping("importReconsiderVerify")
    @RequiresPermissions("ybReconsiderVerify:addImport")
    public void importReconsiderVerifys(String applyDate) throws FebsException {
        try {
            User currentUser = FebsUtil.getCurrentUser();
            this.iYbReconsiderVerifyService.insertReconsiderVerifyImports(applyDate, currentUser.getUserId(), currentUser.getUsername());
        } catch (Exception e) {
            message = "匹配失败";
            log.error(message, e);
            throw new FebsException(message);
        }
    }

    @PostMapping("importMainReconsiderVerify")
    @RequiresPermissions("ybReconsiderVerify:addImport")
    public void importMainReconsiderVerifys(String applyDate) throws FebsException {
        try {
            User currentUser = FebsUtil.getCurrentUser();
            this.iYbReconsiderVerifyService.insertMainReconsiderVerifyImports(applyDate, currentUser.getUserId(), currentUser.getUsername());
        } catch (Exception e) {
            message = "匹配失败";
            log.error(message, e);
            throw new FebsException(message);
        }
    }

    @PutMapping("updateReconsiderVerifyImport")
    @RequiresPermissions("ybReconsiderVerify:updateImport")
    public void updateReconsiderVerifyImports(String dataJson) throws FebsException {
        try {
            User currentUser = FebsUtil.getCurrentUser();
            Long uid = currentUser.getUserId();
            String uname = currentUser.getUsername();

            List<YbReconsiderVerify> list = JSON.parseObject(dataJson, new TypeReference<List<YbReconsiderVerify>>() {
            });
            this.iYbReconsiderVerifyService.updateReconsiderVerifyImports(list, currentUser.getUserId(), currentUser.getUsername());
        } catch (Exception e) {
            message = "匹配失败";
            log.error(message, e);
            throw new FebsException(message);
        }
    }

    @Log("修改")
    @PutMapping("updateSendState")
    @RequiresPermissions("ybReconsiderVerify:sendStateUpdate")
    public void updateSendState(String dataJson) throws FebsException {
        try {
            User currentUser = FebsUtil.getCurrentUser();
            Long uid = currentUser.getUserId();
            String uname = currentUser.getUsername();

            List<YbReconsiderVerify> list = JSON.parseObject(dataJson, new TypeReference<List<YbReconsiderVerify>>() {
            });

            this.iYbReconsiderVerifyService.updateSendStates(list,uid ,uname);
        } catch (Exception e) {
            message = "发送失败";
            log.error(message, e);
            throw new FebsException(message);
        }
    }

    @Log("修改")
    @PutMapping("updateMainSendState")
    @RequiresPermissions("ybReconsiderVerify:sendStateUpdate")
    public void updateMainSendState(String dataJson) throws FebsException {
        try {
            User currentUser = FebsUtil.getCurrentUser();
            Long uid = currentUser.getUserId();
            String uname = currentUser.getUsername();

            List<YbReconsiderVerify> list = JSON.parseObject(dataJson, new TypeReference<List<YbReconsiderVerify>>() {
            });

            this.iYbReconsiderVerifyService.updateMainSendStates(list,uid ,uname);
        } catch (Exception e) {
            message = "发送失败";
            log.error(message, e);
            throw new FebsException(message);
        }
    }

    //单个，多个核对
    @Log("修改")
    @PutMapping("updateReviewerState")
    @RequiresPermissions("ybReconsiderVerify:reviewerStateUpdate")
    public void updateReviewerState(String dataJson) throws FebsException {
        try {
            User currentUser = FebsUtil.getCurrentUser();
            Long uid = currentUser.getUserId();
            String uname = currentUser.getUsername();

            List<YbReconsiderVerify> list = JSON.parseObject(dataJson, new TypeReference<List<YbReconsiderVerify>>() {
            });
            this.iYbReconsiderVerifyService.updateReviewerStates(list,uid ,uname);
        } catch (Exception e) {
            message = "核对失败";
            log.error(message, e);
            throw new FebsException(message);
        }
    }
}