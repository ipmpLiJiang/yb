package cc.mrbird.febs.drg.controller;

import cc.mrbird.febs.com.controller.DataTypeHelpers;
import cc.mrbird.febs.com.entity.ComType;
import cc.mrbird.febs.com.service.IComConfiguremanageService;
import cc.mrbird.febs.com.service.IComTypeService;
import cc.mrbird.febs.common.annotation.Log;
import cc.mrbird.febs.common.controller.BaseController;
import cc.mrbird.febs.common.domain.FebsResponse;
import cc.mrbird.febs.common.domain.QueryRequest;
import cc.mrbird.febs.common.exception.FebsException;
import cc.mrbird.febs.common.utils.FebsUtil;
import cc.mrbird.febs.drg.entity.*;
import cc.mrbird.febs.export.excel.ExportExcelUtils;
import cc.mrbird.febs.system.domain.User;
import cc.mrbird.febs.yb.domain.ResponseResultData;
import cc.mrbird.febs.drg.service.IYbDrgConfireDataService;
import cc.mrbird.febs.drg.service.IYbDrgConfireService;
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
 * @since 2021-01-11
 */
@Slf4j
@Validated
@RestController
@RequestMapping("ybDrgConfire")

public class YbDrgConfireController extends BaseController {

    private String message;
    @Autowired
    public IYbDrgConfireService iYbDrgConfireService;

    @Autowired
    public IYbDrgConfireDataService iYbDrgConfireDataService;

    @Autowired
    public IComConfiguremanageService iComConfiguremanageService;

    @Autowired
    IComTypeService iComTypeService;

    /**
     * 分页查询数据
     *
     * @param request 分页信息
     * @return
     */
    @GetMapping
    @RequiresPermissions("ybDrgConfire:view")
    public Map<String, Object> List(QueryRequest request, String doctorContent, Integer adminType, Integer areaType, String deptContent, String operatorName) {
        return getDataTable(this.iYbDrgConfireService.findDrgConfireView(request, doctorContent, adminType, areaType, deptContent, operatorName, null));
    }

    @GetMapping("findDrgConfireView")
    @RequiresPermissions("ybDrgConfire:userView")
    public Map<String, Object> findUserList(QueryRequest request, String doctorContent, Integer adminType, Integer areaType, String deptContent, String operatorName) {
        User currentUser = FebsUtil.getCurrentUser();
        return getDataTable(this.iYbDrgConfireService.findDrgConfireUserView(request, doctorContent, adminType, areaType, deptContent, currentUser, operatorName, null));
    }

    /**
     * 添加
     *
     * @param ybDrgConfire
     * @return
     */
    @Log("新增/按钮")
    @PostMapping
    @RequiresPermissions("ybDrgConfire:add")
    public void addYbDrgConfire(@Valid YbDrgConfire ybDrgConfire) throws FebsException {
        try {
            User currentUser = FebsUtil.getCurrentUser();
            ybDrgConfire.setCreateUserId(currentUser.getUserId());
            ybDrgConfire.setOperatorId(currentUser.getUserId());
            ybDrgConfire.setOperatorName(currentUser.getUsername() + "-" + currentUser.getXmname());
            this.iYbDrgConfireService.createYbDrgConfire(ybDrgConfire);
        } catch (Exception e) {
            message = "新增/按钮失败";
            log.error(message, e);
            throw new FebsException(message);
        }
    }

    @Log("新增/按钮")
    @PostMapping("addDrgConfire")
    @RequiresPermissions("ybDrgConfire:add")
    public FebsResponse addDrgConfire(String dataJson) {
        int success = 0;
        String id = "";
        try {
            User currentUser = FebsUtil.getCurrentUser();
            YbDrgConfireJson drgConfireJson = JSON.parseObject(dataJson, new TypeReference<YbDrgConfireJson>() {
            });
            YbDrgConfire drgConfire = new YbDrgConfire();
            drgConfire.setDoctorCode(drgConfireJson.getDoctorCode());
            drgConfire.setAreaType(drgConfireJson.getAreaType());
            drgConfire = this.iYbDrgConfireService.findDrgConfire(drgConfire);
            if (drgConfire == null) {
                id = UUID.randomUUID().toString();
                List<YbDrgConfireData> createDataList = new ArrayList<>();
                YbDrgConfire create = new YbDrgConfire();
                create.setId(id);
                create.setDoctorCode(drgConfireJson.getDoctorCode());
                String strDoctorName = DataTypeHelpers.stringReplaceSetString(drgConfireJson.getDoctorName(), drgConfireJson.getDoctorCode() + "-");
                create.setDoctorName(strDoctorName);
                create.setAdminType(drgConfireJson.getAdminType());
                create.setAreaType(drgConfireJson.getAreaType());
                create.setIsDeletemark(1);
                create.setCreateTime(new Date());
                create.setCreateUserId(currentUser.getUserId());
                create.setOperatorId(currentUser.getUserId());
                create.setOperatorName(currentUser.getUsername() + "-" + currentUser.getXmname());
                for (YbDrgConfireDataJson item : drgConfireJson.getChild()) {
                    YbDrgConfireData createData = new YbDrgConfireData();
                    createData.setId(UUID.randomUUID().toString());
                    createData.setPid(id);
//                    createData.setDeptId(item.getDeptId());
//                    String strDeptName = DataTypeHelpers.stringReplaceSetString(item.getDeptName(), item.getDeptId() + "-");
//                    createData.setDeptName(strDeptName);
                    String strDksName = item.getDksName();
                    createData.setDksName(strDksName);
                    createDataList.add(createData);
                }
                this.iYbDrgConfireService.createDrgConfire(create, createDataList);
                success = 1;
            } else {
                message = iComConfiguremanageService.getConfigAreaName(drgConfireJson.getAreaType());
                message = message + " 当前职工 " + drgConfireJson.getDoctorName() + " 已经维护过数据了";
            }
            //dataJson = JSONUtil.toJsonStr(drgConfireJson);
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
    @PutMapping("updateDrgConfire")
    @RequiresPermissions("ybDrgConfire:update")
    public FebsResponse updateDrgConfire(String dataJson) {
        int success = 0;
        try {
            User currentUser = FebsUtil.getCurrentUser();
            YbDrgConfireJson drgConfireJson = JSON.parseObject(dataJson, new TypeReference<YbDrgConfireJson>() {
            });
            YbDrgConfire drgConfire = new YbDrgConfire();
            drgConfire.setDoctorCode(drgConfireJson.getDoctorCode());
            drgConfire.setAreaType(drgConfireJson.getAreaType());
            drgConfire = this.iYbDrgConfireService.findDrgConfire(drgConfire);

            if (drgConfire == null || (drgConfireJson.getId().equals(drgConfire.getId()) && drgConfireJson.getDoctorCode().equals(drgConfire.getDoctorCode()))) {
                List<YbDrgConfireData> updateDataList = new ArrayList<>();
                List<YbDrgConfireData> createDataList = new ArrayList<>();
                YbDrgConfire update = new YbDrgConfire();
                update.setId(drgConfireJson.getId());
                update.setDoctorCode(drgConfire.getDoctorCode());
                update.setAdminType(drgConfireJson.getAdminType());
                update.setAreaType(drgConfire.getAreaType());
                update.setModifyTime(new Date());
                update.setModifyUserId(currentUser.getUserId());
                for (YbDrgConfireDataJson item : drgConfireJson.getChild()) {
                    YbDrgConfireData updateData = new YbDrgConfireData();
                    updateData.setId(item.getId());
                    updateData.setPid(drgConfireJson.getId());
//                    updateData.setDeptId(item.getDeptId());
//                    String strDeptName = DataTypeHelpers.stringReplaceSetString(item.getDeptName(), item.getDeptId() + "-");
//                    updateData.setDeptName(strDeptName);
                    String strDksName = item.getDksName();
                    updateData.setDksName(strDksName);
                    if (updateData.getId() == null) {
                        updateData.setId(UUID.randomUUID().toString());
                        createDataList.add(updateData);
                    } else {
                        updateDataList.add(updateData);
                    }
                }
                if (drgConfireJson.getChild().size() == 0) {
                    this.iYbDrgConfireService.updateDrgConfire(update, createDataList, updateDataList);
                    success = 1;
                } else {
                    YbDrgConfireData quertAcd = new YbDrgConfireData();
                    quertAcd.setPid(drgConfireJson.getId());
//                    quertAcd.setDeptId(createDataList.get(0).getDeptId());
                    quertAcd.setDksName(createDataList.get(0).getDksName());
                    List<YbDrgConfireData> queryAcdList = iYbDrgConfireDataService.findDrgConfireDataList(quertAcd);
                    if (queryAcdList.size() == 0) {
                        this.iYbDrgConfireService.updateDrgConfire(update, createDataList, updateDataList);
                        success = 1;
                    } else {
                        message = iComConfiguremanageService.getConfigAreaName(drgConfireJson.getAreaType());
//                        message = message + " " + createDataList.get(0).getDeptId() + "-" + createDataList.get(0).getDeptName() + " 科室已存在!";
                        message = message + " " + createDataList.get(0).getDksName() + " 已存在!";
                    }
                }
            } else {
                message = iComConfiguremanageService.getConfigAreaName(drgConfireJson.getAreaType());
                message = message + " 当前职工 " + drgConfireJson.getDoctorName() + " 已经维护过数据了";
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
     * @param ybDrgConfire
     * @return
     */
    @Log("修改")
    @PutMapping
    @RequiresPermissions("ybDrgConfire:update")
    public void updateYbDrgConfire(@Valid YbDrgConfire ybDrgConfire) throws FebsException {
        try {
            User currentUser = FebsUtil.getCurrentUser();
            ybDrgConfire.setModifyUserId(currentUser.getUserId());
            this.iYbDrgConfireService.updateYbDrgConfire(ybDrgConfire);
        } catch (Exception e) {
            message = "修改失败";
            log.error(message, e);
            throw new FebsException(message);
        }
    }


    @Log("删除")
    @DeleteMapping("/{ids}")
    @RequiresPermissions("ybDrgConfire:delete")
    public void deleteYbDrgConfires(@NotBlank(message = "{required}") @PathVariable String ids) throws FebsException {
        try {
            String[] arr_ids = ids.split(StringPool.COMMA);
            this.iYbDrgConfireService.deleteYbDrgConfires(arr_ids);
        } catch (Exception e) {
            message = "删除失败";
            log.error(message, e);
            throw new FebsException(message);
        }
    }

    @PostMapping("excel")
    @RequiresPermissions("ybDrgConfire:export")
    public void export(QueryRequest request, YbDrgConfire ybDrgConfire, HttpServletResponse response) throws FebsException {
        try {
            List<YbDrgConfire> ybDrgConfires = this.iYbDrgConfireService.findYbDrgConfires(request, ybDrgConfire).getRecords();
            ExcelKit.$Export(YbDrgConfire.class, response).downXlsx(ybDrgConfires, false);
        } catch (Exception e) {
            message = "导出Excel失败";
            log.error(message, e);
            throw new FebsException(message);
        }
    }

    @GetMapping("/{id}")
    public YbDrgConfire detail(@NotBlank(message = "{required}") @PathVariable String id) {
        YbDrgConfire ybDrgConfire = this.iYbDrgConfireService.getById(id);
        return ybDrgConfire;
    }

    @PostMapping("exportDrgConfire")
    @RequiresPermissions("ybDrgConfire:add")
    public void exportDrgConfire(QueryRequest request, String doctorContent, Integer adminType, Integer areaType, String deptContent, String operatorName, HttpServletResponse response) throws FebsException {
        try {
            request.setPageSize(-1);
            List<YbDrgConfire> list = list = this.iYbDrgConfireService.findDrgConfireView(request, doctorContent, adminType, areaType, deptContent, operatorName, "excel").getRecords();

            List<YbDrgConfireExport> exportList = new ArrayList<>();
            if (list.size() > 0) {
                List<ComType> ctQuery = new ArrayList<>();
                ComType query = new ComType();
                query.setCtType(1);
                query.setIsDeletemark(1);
                List<ComType> ctList = iComTypeService.findComTypeList(query);
                for (YbDrgConfire item : list) {
                    YbDrgConfireExport dataExport = new YbDrgConfireExport();
                    dataExport.setDoctorCode(item.getDoctorCode());
                    dataExport.setDoctorName(item.getDoctorName());
                    ctQuery = ctList.stream().filter(s -> s.getId().equals(item.getAdminType())).collect(Collectors.toList());
                    if (ctQuery.size() > 0) {
                        dataExport.setAdminTypeName(ctQuery.get(0).getCtName());
                    }
                    dataExport.setDeptNames(item.getCurrencyField());
                    dataExport.setOperatorName(item.getOperatorName());

                    exportList.add(dataExport);
                }
            }
            ExportExcelUtils.exportExcel(response, YbDrgConfireExport.class, exportList, "医管人员明细数据");

        } catch (Exception e) {
            message = "导出Excel失败";
            log.error(message, e);
            throw new FebsException(message);
        }

    }

    @PostMapping("exportUserDrgConfire")
    @RequiresPermissions("ybDrgConfire:add")
    public void exportUserDrgConfire(QueryRequest request, String doctorContent, Integer adminType, Integer areaType, String deptContent, String operatorName, HttpServletResponse response) throws FebsException {
        try {
            request.setPageSize(-1);
            User currentUser = FebsUtil.getCurrentUser();
            List<YbDrgConfire> list = this.iYbDrgConfireService.findDrgConfireUserView(request, doctorContent, adminType, areaType, deptContent, currentUser, operatorName, "excel").getRecords();

            List<YbDrgConfireExport> exportList = new ArrayList<>();
            if (list.size() > 0) {
                List<ComType> ctQuery = new ArrayList<>();
                ComType query = new ComType();
                query.setCtType(1);
                query.setIsDeletemark(1);
                List<ComType> ctList = iComTypeService.findComTypeList(query);
                for (YbDrgConfire item : list) {
                    YbDrgConfireExport dataExport = new YbDrgConfireExport();
                    dataExport.setDoctorCode(item.getDoctorCode());
                    dataExport.setDoctorName(item.getDoctorName());
                    ctQuery = ctList.stream().filter(s -> s.getId().equals(item.getAdminType())).collect(Collectors.toList());
                    if (ctQuery.size() > 0) {
                        dataExport.setAdminTypeName(ctQuery.get(0).getCtName());
                    }
                    dataExport.setDeptNames(item.getCurrencyField());
                    dataExport.setOperatorName(item.getOperatorName());

                    exportList.add(dataExport);
                }
            }
            ExportExcelUtils.exportExcel(response, YbDrgConfireExport.class, exportList, "医管人员明细数据");

        } catch (Exception e) {
            message = "导出Excel失败";
            log.error(message, e);
            throw new FebsException(message);
        }

    }

}