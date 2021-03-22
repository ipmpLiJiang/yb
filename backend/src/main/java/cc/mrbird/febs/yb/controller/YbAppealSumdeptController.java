package cc.mrbird.febs.yb.controller;

import cc.mrbird.febs.com.controller.DataTypeHelpers;
import cc.mrbird.febs.com.entity.ComConfiguremanage;
import cc.mrbird.febs.com.service.IComConfiguremanageService;
import cc.mrbird.febs.common.annotation.Log;
import cc.mrbird.febs.common.controller.BaseController;
import cc.mrbird.febs.common.domain.FebsResponse;
import cc.mrbird.febs.common.domain.router.VueRouter;
import cc.mrbird.febs.common.exception.FebsException;
import cc.mrbird.febs.common.domain.QueryRequest;

import cc.mrbird.febs.yb.domain.ResponseResultData;
import cc.mrbird.febs.yb.entity.*;
import cc.mrbird.febs.yb.service.IYbAppealSumdeptDataService;
import cc.mrbird.febs.yb.service.IYbAppealSumdeptService;

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
import java.util.*;
import java.util.stream.Collectors;

/**
 * @author viki
 * @since 2021-03-08
 */
@Slf4j
@Validated
@RestController
@RequestMapping("ybAppealSumdept")

public class YbAppealSumdeptController extends BaseController {

    private String message;
    @Autowired
    public IYbAppealSumdeptService iYbAppealSumdeptService;

    @Autowired
    public IYbAppealSumdeptDataService iYbAppealSumdeptDataService;

    @Autowired
    IComConfiguremanageService iComConfiguremanageService;

    /**
     * 分页查询数据
     *
     * @param request         分页信息
     * @param ybAppealSumdept 查询条件
     * @return
     */
    @GetMapping
    @RequiresPermissions("ybAppealSumdept:view")
    public Map<String, Object> List(QueryRequest request, YbAppealSumdept ybAppealSumdept) {
        return getDataTable(this.iYbAppealSumdeptService.findAppealSumdeptView(request, ybAppealSumdept));
    }

    @Log("新增/按钮")
    @PostMapping("addAppealSumdept")
    @RequiresPermissions("ybAppealSumdept:add")
    public FebsResponse addAppealSumdept(String dataJson) {
        int success = 0;
        String id = "";
        try {
            User currentUser = FebsUtil.getCurrentUser();
            YbAppealSumdeptJson appealSumdeptJson = JSON.parseObject(dataJson, new TypeReference<YbAppealSumdeptJson>() {
            });
            YbAppealSumdept appealSumdept = new YbAppealSumdept();
            appealSumdept.setAreaType(appealSumdeptJson.getAreaType());
            appealSumdept.setAsName(appealSumdeptJson.getAsName());
            appealSumdept = this.iYbAppealSumdeptService.findAppealSumdept(appealSumdept);
            if (appealSumdept == null) {
                id = UUID.randomUUID().toString();
                List<YbAppealSumdeptData> createDataList = new ArrayList<>();
                YbAppealSumdept create = new YbAppealSumdept();
                create.setId(id);
                create.setAsName(appealSumdeptJson.getAsName());
                create.setAreaType(appealSumdeptJson.getAreaType());
                create.setIsDeletemark(1);
                create.setCreateTime(new Date());
                create.setCreateUserId(currentUser.getUserId());
                for (YbAppealSumdeptDataJson item : appealSumdeptJson.getChild()) {
                    YbAppealSumdeptData createData = new YbAppealSumdeptData();
                    createData.setId(UUID.randomUUID().toString());
                    createData.setPid(id);
                    createData.setDeptId(item.getDeptId());
                    String strDeptName = DataTypeHelpers.stringReplaceSetString(item.getDeptName(), item.getDeptId() + "-");
                    createData.setDeptName(strDeptName);
                    createDataList.add(createData);
                }
                this.iYbAppealSumdeptService.createAppealSumdept(create, createDataList);
                success = 1;
            } else {
                message = iComConfiguremanageService.getConfigAreaName(appealSumdeptJson.getAreaType());
                message = message + "当前汇总科室 " + appealSumdeptJson.getAsName() + " 已经维护过数据了";
            }
            //dataJson = JSONUtil.toJsonStr(AppealSumdeptJson);
        } catch (Exception e) {
            message = "新增/按钮失败";
            log.error(message, e);
        }

        ResponseResultData rrd = new ResponseResultData();
        rrd.setSuccess(success);
        rrd.setMessage(message);
        rrd.setData(id);
        return new FebsResponse().data(rrd);


    }

    @Log("修改")
    @PutMapping("updateAppealSumdept")
    @RequiresPermissions("ybAppealSumdept:update")
    public FebsResponse updateAppealSumdept(String dataJson) {
        int success = 0;
        try {
            User currentUser = FebsUtil.getCurrentUser();
            YbAppealSumdeptJson appealSumdeptJson = JSON.parseObject(dataJson, new TypeReference<YbAppealSumdeptJson>() {
            });
            YbAppealSumdept appealSumdept = new YbAppealSumdept();
            appealSumdept.setAsName(appealSumdeptJson.getAsName());
            appealSumdept.setAreaType(appealSumdeptJson.getAreaType());
            appealSumdept = this.iYbAppealSumdeptService.findAppealSumdept(appealSumdept);
            if (appealSumdept == null || (appealSumdeptJson.getId().equals(appealSumdept.getId()) && appealSumdeptJson.getAsName().equals(appealSumdept.getAsName()))) {
                List<YbAppealSumdeptData> updateDataList = new ArrayList<>();
                List<YbAppealSumdeptData> createDataList = new ArrayList<>();
                YbAppealSumdept update = new YbAppealSumdept();
                update.setId(appealSumdeptJson.getId());
                update.setAsName(appealSumdeptJson.getAsName());
                update.setModifyTime(new Date());
                update.setModifyUserId(currentUser.getUserId());
                for (YbAppealSumdeptDataJson item : appealSumdeptJson.getChild()) {
                    YbAppealSumdeptData updateData = new YbAppealSumdeptData();
                    updateData.setId(item.getId());
                    updateData.setPid(appealSumdeptJson.getId());
                    updateData.setDeptId(item.getDeptId());
                    String strDeptName = DataTypeHelpers.stringReplaceSetString(item.getDeptName(), item.getDeptId() + "-");
                    updateData.setDeptName(strDeptName);
                    if (updateData.getId() == null) {
                        updateData.setId(UUID.randomUUID().toString());
                        createDataList.add(updateData);
                    } else {
                        updateDataList.add(updateData);
                    }
                }
                if (appealSumdeptJson.getChild().size() == 0) {
                    this.iYbAppealSumdeptService.updateAppealSumdept(update, createDataList, updateDataList);
                    success = 1;
                } else {
                    YbAppealSumdeptData quertAcd = new YbAppealSumdeptData();
                    quertAcd.setPid(appealSumdeptJson.getId());
                    quertAcd.setDeptId(createDataList.get(0).getDeptId());
                    List<YbAppealSumdeptData> queryAcdList = iYbAppealSumdeptDataService.findAppealSumdeptDataList(quertAcd);
                    if (queryAcdList.size() == 0) {
                        this.iYbAppealSumdeptService.updateAppealSumdept(update, createDataList, updateDataList);
                        success = 1;
                    } else {
                        message = iComConfiguremanageService.getConfigAreaName(appealSumdeptJson.getAreaType());
                        message = message +createDataList.get(0).getDeptId() + "-" + createDataList.get(0).getDeptName() + " 科室已存在!";
                    }
                }
            }else {
                message = iComConfiguremanageService.getConfigAreaName(appealSumdeptJson.getAreaType());
                message = message + "当前汇总科室 " + appealSumdeptJson.getAsName() + " 已经维护过数据了";
            }
        } catch (Exception e) {
            message = "修改失败";
            log.error(message, e);
        }

        ResponseResultData rrd = new ResponseResultData();
        rrd.setSuccess(success);
        rrd.setMessage(message);
        rrd.setData("");
        return new FebsResponse().data(rrd);
    }



    /**
     * 添加
     *
     * @param ybAppealSumdept
     * @return
     */
    @Log("新增/按钮")
    @PostMapping
    @RequiresPermissions("ybAppealSumdept:add")
    public void addYbAppealSumdept(@Valid YbAppealSumdept ybAppealSumdept) throws FebsException {
        try {
            User currentUser = FebsUtil.getCurrentUser();
            ybAppealSumdept.setCreateUserId(currentUser.getUserId());
            this.iYbAppealSumdeptService.createYbAppealSumdept(ybAppealSumdept);
        } catch (Exception e) {
            message = "新增/按钮失败";
            log.error(message, e);
            throw new FebsException(message);
        }
    }

    /**
     * 修改
     *
     * @param ybAppealSumdept
     * @return
     */
    @Log("修改")
    @PutMapping
    @RequiresPermissions("ybAppealSumdept:update")
    public void updateYbAppealSumdept(@Valid YbAppealSumdept ybAppealSumdept) throws FebsException {
        try {
            User currentUser = FebsUtil.getCurrentUser();
            ybAppealSumdept.setModifyUserId(currentUser.getUserId());
            this.iYbAppealSumdeptService.updateYbAppealSumdept(ybAppealSumdept);
        } catch (Exception e) {
            message = "修改失败";
            log.error(message, e);
            throw new FebsException(message);
        }
    }


    @Log("删除")
    @DeleteMapping("/{ids}")
    @RequiresPermissions("ybAppealSumdept:delete")
    public void deleteYbAppealSumdepts(@NotBlank(message = "{required}") @PathVariable String ids) throws FebsException {
        try {
            String[] arr_ids = ids.split(StringPool.COMMA);
            this.iYbAppealSumdeptService.deleteYbAppealSumdepts(arr_ids);
        } catch (Exception e) {
            message = "删除失败";
            log.error(message, e);
            throw new FebsException(message);
        }
    }

    @PostMapping("excel")
    @RequiresPermissions("ybAppealSumdept:export")
    public void export(QueryRequest request, YbAppealSumdept ybAppealSumdept, HttpServletResponse response) throws FebsException {
        try {
            List<YbAppealSumdept> ybAppealSumdepts = this.iYbAppealSumdeptService.findYbAppealSumdepts(request, ybAppealSumdept).getRecords();
            ExcelKit.$Export(YbAppealSumdept.class, response).downXlsx(ybAppealSumdepts, false);
        } catch (Exception e) {
            message = "导出Excel失败";
            log.error(message, e);
            throw new FebsException(message);
        }
    }

    @GetMapping("/{id}")
    public YbAppealSumdept detail(@NotBlank(message = "{required}") @PathVariable String id) {
        YbAppealSumdept ybAppealSumdept = this.iYbAppealSumdeptService.getById(id);
        return ybAppealSumdept;
    }
}