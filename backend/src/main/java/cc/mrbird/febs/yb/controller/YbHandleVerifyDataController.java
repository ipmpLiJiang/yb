package cc.mrbird.febs.yb.controller;

import cc.mrbird.febs.common.annotation.Log;
import cc.mrbird.febs.common.controller.BaseController;
import cc.mrbird.febs.common.domain.FebsResponse;
import cc.mrbird.febs.common.domain.router.VueRouter;
import cc.mrbird.febs.common.exception.FebsException;
import cc.mrbird.febs.common.domain.QueryRequest;

import cc.mrbird.febs.yb.domain.ResponseResultData;
import cc.mrbird.febs.yb.entity.YbReconsiderVerify;
import cc.mrbird.febs.yb.service.IYbHandleVerifyDataService;
import cc.mrbird.febs.yb.entity.YbHandleVerifyData;

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
import org.springframework.web.multipart.MultipartFile;

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
@RequestMapping("ybHandleVerifyData")

public class YbHandleVerifyDataController extends BaseController {

    private String message;
    @Autowired
    public IYbHandleVerifyDataService iYbHandleVerifyDataService;


    /**
     * 分页查询数据
     *
     * @param request            分页信息
     * @param ybHandleVerifyData 查询条件
     * @return
     */
    @GetMapping
    @RequiresPermissions("ybHandleVerifyData:view")
    public Map<String, Object> List(QueryRequest request, YbHandleVerifyData ybHandleVerifyData) {
        return getDataTable(this.iYbHandleVerifyDataService.findYbHandleVerifyDatas(request, ybHandleVerifyData));
    }

    /**
     * 添加
     *
     * @param ybHandleVerifyData
     * @return
     */
    @Log("新增/按钮")
    @PostMapping
    @RequiresPermissions("ybHandleVerifyData:add")
    public void addYbHandleVerifyData(@Valid YbHandleVerifyData ybHandleVerifyData) throws FebsException {
        try {
            User currentUser = FebsUtil.getCurrentUser();
            ybHandleVerifyData.setCreateUserId(currentUser.getUserId());
            this.iYbHandleVerifyDataService.createYbHandleVerifyData(ybHandleVerifyData);
        } catch (Exception e) {
            message = "新增/按钮失败";
            log.error(message, e);
            throw new FebsException(message);
        }
    }

    /**
     * 修改
     *
     * @param ybHandleVerifyData
     * @return
     */
    @Log("修改")
    @PutMapping
    @RequiresPermissions("ybHandleVerifyData:update")
    public void updateYbHandleVerifyData(@Valid YbHandleVerifyData ybHandleVerifyData) throws FebsException {
        try {
            User currentUser = FebsUtil.getCurrentUser();
            ybHandleVerifyData.setModifyUserId(currentUser.getUserId());
            this.iYbHandleVerifyDataService.updateYbHandleVerifyData(ybHandleVerifyData);
        } catch (Exception e) {
            message = "修改失败";
            log.error(message, e);
            throw new FebsException(message);
        }
    }


    @Log("删除")
    @DeleteMapping("/{ids}")
    @RequiresPermissions("ybHandleVerifyData:delete")
    public void deleteYbHandleVerifyDatas(@NotBlank(message = "{required}") @PathVariable String ids) throws FebsException {
        try {
            String[] arr_ids = ids.split(StringPool.COMMA);
            this.iYbHandleVerifyDataService.deleteYbHandleVerifyDatas(arr_ids);
        } catch (Exception e) {
            message = "删除失败";
            log.error(message, e);
            throw new FebsException(message);
        }
    }

    @PostMapping("excel")
    @RequiresPermissions("ybHandleVerifyData:export")
    public void export(QueryRequest request, YbHandleVerifyData ybHandleVerifyData, HttpServletResponse response) throws FebsException {
        try {
            List<YbHandleVerifyData> ybHandleVerifyDatas = this.iYbHandleVerifyDataService.findYbHandleVerifyDatas(request, ybHandleVerifyData).getRecords();
            ExcelKit.$Export(YbHandleVerifyData.class, response).downXlsx(ybHandleVerifyDatas, false);
        } catch (Exception e) {
            message = "导出Excel失败";
            log.error(message, e);
            throw new FebsException(message);
        }
    }

    @GetMapping("/{id}")
    public YbHandleVerifyData detail(@NotBlank(message = "{required}") @PathVariable String id) {
        YbHandleVerifyData ybHandleVerifyData = this.iYbHandleVerifyDataService.getById(id);
        return ybHandleVerifyData;
    }

    @Log("修改")
    @PutMapping("updateSendState")
    @RequiresPermissions("ybHandleVerifyData:update")
    public void updateSendStates(String dataJson) throws FebsException {
        try {
            User currentUser = FebsUtil.getCurrentUser();
            Long uid = currentUser.getUserId();
            String uname = currentUser.getUsername();
            List<YbHandleVerifyData> list = JSON.parseObject(dataJson, new TypeReference<List<YbHandleVerifyData>>() {
            });
            this.iYbHandleVerifyDataService.updateSendStates(list,uid,uname);
        } catch (Exception e) {
            message = "发送失败";
            log.error(message, e);
            throw new FebsException(message);
        }
    }

    @Log("修改")
    @PutMapping("updateASendState")
    @RequiresPermissions("ybHandleVerifyData:update")
    public void updateASendState(String applyDateStr,Integer state,Integer dataType) throws FebsException {
        try {
            User currentUser = FebsUtil.getCurrentUser();
            Long uid = currentUser.getUserId();
            String uname = currentUser.getUsername();

            this.iYbHandleVerifyDataService.updateAllSendStates(applyDateStr,dataType,state,uid ,uname);
        } catch (Exception e) {
            message = "发送失败";
            log.error(message, e);
            throw new FebsException(message);
        }
    }

    @PostMapping("importCreateHandleVerifyData")
    public FebsResponse importCreateHandleVerifyDatas(String applyDateStr,Integer areaType) {
        int success = 0;
        try {
            User currentUser = FebsUtil.getCurrentUser();
            Long uid = currentUser.getUserId();
            String uname = currentUser.getUsername();
            this.iYbHandleVerifyDataService.importCreateHandleVerifyData(applyDateStr,areaType,uid,uname);
            message = "获得剔除操作成功,如未获取到数据，请检查是否剔除完成操作.";
            success = 1;
        } catch (Exception e) {
            message = "获取失败";
            log.error(message, e);
        }
        ResponseResultData rrd = new ResponseResultData();
        rrd.setSuccess(success);
        rrd.setMessage(message);
        return new FebsResponse().data(rrd);
    }
}