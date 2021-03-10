package cc.mrbird.febs.yb.controller;

import cc.mrbird.febs.com.controller.DataTypeHelpers;
import cc.mrbird.febs.common.annotation.Log;
import cc.mrbird.febs.common.controller.BaseController;
import cc.mrbird.febs.common.domain.FebsResponse;
import cc.mrbird.febs.common.domain.router.VueRouter;
import cc.mrbird.febs.common.exception.FebsException;
import cc.mrbird.febs.common.domain.QueryRequest;

import cc.mrbird.febs.yb.domain.ResponseImportResultData;
import cc.mrbird.febs.yb.domain.ResponseResultData;
import cc.mrbird.febs.yb.entity.*;
import cc.mrbird.febs.yb.service.IYbAppealConfireDataService;
import cc.mrbird.febs.yb.service.IYbAppealConfireService;

import cc.mrbird.febs.common.utils.FebsUtil;
import cc.mrbird.febs.system.domain.User;
import cn.hutool.json.JSONUtil;
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

/**
 * @author viki
 * @since 2021-01-11
 */
@Slf4j
@Validated
@RestController
@RequestMapping("ybAppealConfire")

public class YbAppealConfireController extends BaseController {

    private String message;
    @Autowired
    public IYbAppealConfireService iYbAppealConfireService;

    @Autowired
    public IYbAppealConfireDataService iYbAppealConfireDataService;


    /**
     * 分页查询数据
     *
     * @param request 分页信息
     * @return
     */
    @GetMapping
    @RequiresPermissions("ybAppealConfire:view")
    public Map<String, Object> List(QueryRequest request, String doctorContent, Integer adminType, String deptContent) {
        return getDataTable(this.iYbAppealConfireService.findAppealConfireView(request, doctorContent, adminType, deptContent));
    }

    @GetMapping("findAppealConfireView")
    @RequiresPermissions("ybAppealConfire:userView")
    public Map<String, Object> findUserList(QueryRequest request, String doctorContent, Integer adminType, String deptContent) {
        User currentUser = FebsUtil.getCurrentUser();
        return getDataTable(this.iYbAppealConfireService.findAppealConfireUserView(request, doctorContent, adminType, deptContent,currentUser.getUserId()));
    }

    /**
     * 添加
     *
     * @param ybAppealConfire
     * @return
     */
    @Log("新增/按钮")
    @PostMapping
    @RequiresPermissions("ybAppealConfire:add")
    public void addYbAppealConfire(@Valid YbAppealConfire ybAppealConfire) throws FebsException {
        try {
            User currentUser = FebsUtil.getCurrentUser();
            ybAppealConfire.setCreateUserId(currentUser.getUserId());
            this.iYbAppealConfireService.createYbAppealConfire(ybAppealConfire);
        } catch (Exception e) {
            message = "新增/按钮失败";
            log.error(message, e);
            throw new FebsException(message);
        }
    }

    @Log("新增/按钮")
    @PostMapping("addAppealConfire")
    @RequiresPermissions("ybAppealConfire:add")
    public FebsResponse addAppealConfire(String dataJson) {
        int success = 0;
        String id = "";
        try {
            User currentUser = FebsUtil.getCurrentUser();
            YbAppealConfireJson appealConfireJson = JSON.parseObject(dataJson, new TypeReference<YbAppealConfireJson>() {
            });
            YbAppealConfire appealConfire = new YbAppealConfire();
            appealConfire.setDoctorCode(appealConfireJson.getDoctorCode());
            appealConfire = this.iYbAppealConfireService.findAppealConfire(appealConfire);
            if (appealConfire == null) {
                id = UUID.randomUUID().toString();
                List<YbAppealConfireData> createDataList = new ArrayList<>();
                YbAppealConfire create = new YbAppealConfire();
                create.setId(id);
                create.setDoctorCode(appealConfireJson.getDoctorCode());
                String strDoctorName = DataTypeHelpers.stringReplaceSetString(appealConfireJson.getDoctorName(), appealConfireJson.getDoctorCode() + "-");
                create.setDoctorName(strDoctorName);
                create.setAdminType(appealConfireJson.getAdminType());
                create.setIsDeletemark(1);
                create.setCreateTime(new Date());
                create.setCreateUserId(currentUser.getUserId());
                for (YbAppealConfireDataJson item : appealConfireJson.getChild()) {
                    YbAppealConfireData createData = new YbAppealConfireData();
                    createData.setId(UUID.randomUUID().toString());
                    createData.setPid(id);
                    createData.setDeptId(item.getDeptId());
                    String strDeptName = DataTypeHelpers.stringReplaceSetString(item.getDeptName(), item.getDeptId() + "-");
                    createData.setDeptName(strDeptName);
                    createData.setIsDeletemark(1);
                    createDataList.add(createData);
                }
                this.iYbAppealConfireService.createAppealConfire(create, createDataList);
                success = 1;
            } else {
                message = "当前职工 " + appealConfireJson.getDoctorName() + " 已经维护过数据了";
            }
            //dataJson = JSONUtil.toJsonStr(appealConfireJson);
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
    @PutMapping("updateAppealConfire")
    @RequiresPermissions("ybAppealConfire:update")
    public FebsResponse updateAppealConfire(String dataJson) {
        int success = 0;
        try {
            User currentUser = FebsUtil.getCurrentUser();
            YbAppealConfireJson appealConfireJson = JSON.parseObject(dataJson, new TypeReference<YbAppealConfireJson>() {
            });
            List<YbAppealConfireData> updateDataList = new ArrayList<>();
            List<YbAppealConfireData> createDataList = new ArrayList<>();
            YbAppealConfire update = new YbAppealConfire();
            update.setId(appealConfireJson.getId());
            update.setAdminType(appealConfireJson.getAdminType());
            update.setModifyTime(new Date());
            update.setModifyUserId(currentUser.getUserId());
            for (YbAppealConfireDataJson item : appealConfireJson.getChild()) {
                YbAppealConfireData updateData = new YbAppealConfireData();
                updateData.setId(item.getId());
                updateData.setPid(appealConfireJson.getId());
                updateData.setDeptId(item.getDeptId());
                String strDeptName = DataTypeHelpers.stringReplaceSetString(item.getDeptName(), item.getDeptId() + "-");
                updateData.setDeptName(strDeptName);
                if (updateData.getId() == null) {
                    updateData.setIsDeletemark(1);
                    updateData.setId(UUID.randomUUID().toString());
                    createDataList.add(updateData);
                } else {
                    updateDataList.add(updateData);
                }
            }
            if (appealConfireJson.getChild().size() == 0) {
                this.iYbAppealConfireService.updateAppealConfire(update, createDataList, updateDataList);
                success = 1;
            } else {
                YbAppealConfireData quertAcd = new YbAppealConfireData();
                quertAcd.setPid(appealConfireJson.getId());
                quertAcd.setDeptId(createDataList.get(0).getDeptId());
                List<YbAppealConfireData> queryAcdList = iYbAppealConfireDataService.findAppealConfireDataList(quertAcd);
                if (queryAcdList.size() == 0) {
                    this.iYbAppealConfireService.updateAppealConfire(update, createDataList, updateDataList);
                    success = 1;
                } else {
                    message = createDataList.get(0).getDeptId() + "-" + createDataList.get(0).getDeptName() + " 科室已存在!";
                }
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
     * 修改
     *
     * @param ybAppealConfire
     * @return
     */
    @Log("修改")
    @PutMapping
    @RequiresPermissions("ybAppealConfire:update")
    public void updateYbAppealConfire(@Valid YbAppealConfire ybAppealConfire) throws FebsException {
        try {
            User currentUser = FebsUtil.getCurrentUser();
            ybAppealConfire.setModifyUserId(currentUser.getUserId());
            this.iYbAppealConfireService.updateYbAppealConfire(ybAppealConfire);
        } catch (Exception e) {
            message = "修改失败";
            log.error(message, e);
            throw new FebsException(message);
        }
    }


    @Log("删除")
    @DeleteMapping("/{ids}")
    @RequiresPermissions("ybAppealConfire:delete")
    public void deleteYbAppealConfires(@NotBlank(message = "{required}") @PathVariable String ids) throws FebsException {
        try {
            String[] arr_ids = ids.split(StringPool.COMMA);
            this.iYbAppealConfireService.deleteYbAppealConfires(arr_ids);
        } catch (Exception e) {
            message = "删除失败";
            log.error(message, e);
            throw new FebsException(message);
        }
    }

    @PostMapping("excel")
    @RequiresPermissions("ybAppealConfire:export")
    public void export(QueryRequest request, YbAppealConfire ybAppealConfire, HttpServletResponse response) throws FebsException {
        try {
            List<YbAppealConfire> ybAppealConfires = this.iYbAppealConfireService.findYbAppealConfires(request, ybAppealConfire).getRecords();
            ExcelKit.$Export(YbAppealConfire.class, response).downXlsx(ybAppealConfires, false);
        } catch (Exception e) {
            message = "导出Excel失败";
            log.error(message, e);
            throw new FebsException(message);
        }
    }

    @GetMapping("/{id}")
    public YbAppealConfire detail(@NotBlank(message = "{required}") @PathVariable String id) {
        YbAppealConfire ybAppealConfire = this.iYbAppealConfireService.getById(id);
        return ybAppealConfire;
    }
}