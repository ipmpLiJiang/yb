package cc.mrbird.febs.chs.controller;

import cc.mrbird.febs.chs.entity.YbChsApply;
import cc.mrbird.febs.chs.service.IYbChsApplyService;
import cc.mrbird.febs.com.controller.DataTypeHelpers;
import cc.mrbird.febs.com.controller.FileHelpers;
import cc.mrbird.febs.com.controller.ImportExcelUtils;
import cc.mrbird.febs.common.annotation.Log;
import cc.mrbird.febs.common.controller.BaseController;
import cc.mrbird.febs.common.domain.FebsResponse;
import cc.mrbird.febs.common.domain.router.VueRouter;
import cc.mrbird.febs.common.exception.FebsException;
import cc.mrbird.febs.common.domain.QueryRequest;

import cc.mrbird.febs.chs.service.IYbChsApplyDataService;
import cc.mrbird.febs.chs.entity.YbChsApplyData;

import cc.mrbird.febs.common.properties.FebsProperties;
import cc.mrbird.febs.common.utils.FebsUtil;
import cc.mrbird.febs.system.domain.User;
import cc.mrbird.febs.yb.entity.YbDefaultValue;
import com.baomidou.mybatisplus.core.toolkit.StringPool;
import com.wuwenze.poi.ExcelKit;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.ModelMap;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import java.io.File;
import java.math.BigDecimal;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;

/**
 * @author viki
 * @since 2022-06-21
 */
@Slf4j
@Validated
@RestController
@RequestMapping("ybChsApplyData")

public class YbChsApplyDataController extends BaseController {

    private String message;
    @Autowired
    public IYbChsApplyDataService iYbChsApplyDataService;

    @Autowired
    IYbChsApplyService iYbChsApplyService;

    @Autowired
    FebsProperties febsProperties;

    /**
     * 分页查询数据
     *
     * @param request        分页信息
     * @param ybChsApplyData 查询条件
     * @return
     */
    @GetMapping
    @RequiresPermissions("ybChsApplyData:view")
    public Map<String, Object> List(QueryRequest request, YbChsApplyData ybChsApplyData) {
        return getDataTable(this.iYbChsApplyDataService.findYbChsApplyDatas(request, ybChsApplyData));
    }

    @GetMapping("findChsApplyDataList")
    public Map<String, Object> findChsApplyDatas(QueryRequest request, YbChsApplyData ybChsApplyData) {
        return getDataTable(this.iYbChsApplyDataService.findYbChsApplyDataList(request, ybChsApplyData));
    }

    /**
     * 添加
     *
     * @param ybChsApplyData
     * @return
     */
    @Log("新增/按钮")
    @PostMapping
    @RequiresPermissions("ybChsApplyData:add")
    public void addYbChsApplyData(@Valid YbChsApplyData ybChsApplyData) throws FebsException {
        try {
            User currentUser = FebsUtil.getCurrentUser();
//            ybChsApplyData.setCreateUserId(currentUser.getUserId());
            this.iYbChsApplyDataService.createYbChsApplyData(ybChsApplyData);
        } catch (Exception e) {
            message = "新增/按钮失败";
            log.error(message, e);
            throw new FebsException(message);
        }
    }

    /**
     * 修改
     *
     * @param ybChsApplyData
     * @return
     */
    @Log("修改")
    @PutMapping
    @RequiresPermissions("ybChsApplyData:update")
    public void updateYbChsApplyData(@Valid YbChsApplyData ybChsApplyData) throws FebsException {
        try {
            User currentUser = FebsUtil.getCurrentUser();
//            ybChsApplyData.setModifyUserId(currentUser.getUserId());
            this.iYbChsApplyDataService.updateYbChsApplyData(ybChsApplyData);
        } catch (Exception e) {
            message = "修改失败";
            log.error(message, e);
            throw new FebsException(message);
        }
    }


    @Log("删除")
    @DeleteMapping("/{ids}")
    @RequiresPermissions("ybChsApplyData:delete")
    public void deleteYbChsApplyDatas(@NotBlank(message = "{required}") @PathVariable String ids) throws FebsException {
        try {
            String[] arr_ids = ids.split(StringPool.COMMA);
            this.iYbChsApplyDataService.deleteYbChsApplyDatas(arr_ids);
        } catch (Exception e) {
            message = "删除失败";
            log.error(message, e);
            throw new FebsException(message);
        }
    }

    @PostMapping("excel")
    @RequiresPermissions("ybChsApplyData:export")
    public void export(QueryRequest request, YbChsApplyData ybChsApplyData, HttpServletResponse response) throws FebsException {
        try {
            List<YbChsApplyData> ybChsApplyDatas = this.iYbChsApplyDataService.findYbChsApplyDatas(request, ybChsApplyData).getRecords();
            ExcelKit.$Export(YbChsApplyData.class, response).downXlsx(ybChsApplyDatas, false);
        } catch (Exception e) {
            message = "导出Excel失败";
            log.error(message, e);
            throw new FebsException(message);
        }
    }

    @GetMapping("/{id}")
    public YbChsApplyData detail(@NotBlank(message = "{required}") @PathVariable String id) {
        YbChsApplyData ybChsApplyData = this.iYbChsApplyDataService.getById(id);
        return ybChsApplyData;
    }

    @PostMapping("importChsApplyData")
    @RequiresPermissions("ybChsApplyData:add")
    public FebsResponse importChsApplyData(@RequestParam MultipartFile file, @RequestParam String pid) {
        int success = 0;
        ModelMap map = new ModelMap();
        String uploadFileName = "";
        if (file.isEmpty()) {
            message = "空文件";
        } else {
            YbChsApply chsApply = new YbChsApply();
            chsApply.setId(pid);
            List<YbChsApply> list = iYbChsApplyService.findChsApplyList(chsApply);
            chsApply = list.size() > 0 ? list.get(0) : null;

            if (chsApply != null) {
                int state = chsApply.getState();
                if (state == YbDefaultValue.DRGAPPLYSTATE_1 || state == YbDefaultValue.DRGAPPLYSTATE_2) {
                    uploadFileName = file.getOriginalFilename();
                    boolean blError = false;
                    try {
                        String filePath = febsProperties.getUploadPath(); // 上传后的路径
                        File getFile = FileHelpers.fileUpLoad(file, filePath, pid, "ChsApplyTemp");
                        Map<Integer, String> sheetMap = ImportExcelUtils.getSheelNames(getFile);
                        if (sheetMap.size() > 0) {
                            List<Object[]> objMx = new ArrayList<Object[]>();
                            for (Integer key : sheetMap.keySet()) {
                                String value = sheetMap.get(key);
                                objMx = ImportExcelUtils.importExcelBySheetIndex(getFile, key, 0, 0);
                                break;
                            }
                            if (objMx.size() > 1) {
                                List<YbChsApplyData> ListData = new ArrayList<>();
                                DateFormat formater = new SimpleDateFormat("yyyy-MM-dd");
                                if (objMx.size() > 1) {
                                    int orderZy = 1;
                                    int orderMz = 1;
                                    if (objMx.get(0).length >= 21) {
                                        for (int i = 1; i < objMx.size(); i++) {
                                            message = "上传数据读取失败，请确保Excel列表数据正确无误.";
                                            YbChsApplyData rrData = new YbChsApplyData();
                                            rrData.setId(UUID.randomUUID().toString());
                                            rrData.setPid(pid);
                                            rrData.setOrderNum(i);

                                            String appealEndDateStr = DataTypeHelpers.importTernaryOperate(objMx.get(i), 0);//申诉截止日期
                                            if(StringUtils.isNotBlank(appealEndDateStr)) {
                                                rrData.setAppealEndDate(formater.parse(appealEndDateStr));
                                            }
                                            String payPlaceType = DataTypeHelpers.importTernaryOperate(objMx.get(i), 1);//支付地点类别
                                            rrData.setPayPlaceType(payPlaceType);
                                            String ydState = DataTypeHelpers.importTernaryOperate(objMx.get(i), 2);//疑点状态
                                            rrData.setYdState(ydState);
                                            String areaName = DataTypeHelpers.importTernaryOperate(objMx.get(i), 3);//医保区划
                                            rrData.setAreaName(areaName);
                                            String yyjgCode = DataTypeHelpers.importTernaryOperate(objMx.get(i), 4);//医药机构编码
                                            rrData.setYyjgCode(yyjgCode);
                                            String yyjgName = DataTypeHelpers.importTernaryOperate(objMx.get(i), 5);//医药机构名称
                                            rrData.setYyjgName(yyjgName);
                                            String deptName = DataTypeHelpers.importTernaryOperate(objMx.get(i), 6);//科室名称
                                            rrData.setDeptName(deptName);
                                            String doctorName = DataTypeHelpers.importTernaryOperate(objMx.get(i), 7);//	医生姓名
                                            rrData.setDoctorName(doctorName);
                                            String medicalType = DataTypeHelpers.importTernaryOperate(objMx.get(i), 8);//医疗类别
                                            if(StringUtils.isNotBlank(medicalType)) {
                                                rrData.setMedicalType(medicalType);
                                                if(medicalType.contains("门诊")) {
                                                    rrData.setIsOutpfees(1);
                                                    rrData.setOrderSettlementNum(orderMz);
                                                    orderMz ++;
                                                } else {
                                                    rrData.setIsOutpfees(2);
                                                    rrData.setOrderSettlementNum(i);
                                                    rrData.setOrderSettlementNum(orderZy);
                                                    orderZy ++;
                                                }
                                            }
                                            String zymzNumber = DataTypeHelpers.importTernaryOperate(objMx.get(i), 9);//	住院门诊号
                                            rrData.setZymzNumber(zymzNumber);
                                            String insuredName = DataTypeHelpers.importTernaryOperate(objMx.get(i), 10);//参保人
                                            rrData.setInsuredName(insuredName);
                                            String enterHospitalDateStr = DataTypeHelpers.importTernaryOperate(objMx.get(i), 11);//入院日期
                                            if(StringUtils.isNotBlank(enterHospitalDateStr)) {
                                                rrData.setEnterHospitalDate(formater.parse(enterHospitalDateStr));
                                            }
                                            String outHospitalDateStr = DataTypeHelpers.importTernaryOperate(objMx.get(i), 12);//出院日期
                                            rrData.setOutHospitalDateStr(outHospitalDateStr);
                                            if(StringUtils.isNotBlank(outHospitalDateStr)) {
                                                rrData.setOutHospitalDate(formater.parse(outHospitalDateStr));
                                            }
                                            String settlementDateStr = DataTypeHelpers.importTernaryOperate(objMx.get(i), 13);//	结算日期
                                            if(StringUtils.isNotBlank(settlementDateStr)) {
                                                rrData.setSettlementDate(formater.parse(settlementDateStr));
                                            }
                                            String cardNumber = DataTypeHelpers.importTernaryOperate(objMx.get(i), 14);//身份证号
                                            rrData.setCardNumber(cardNumber);
                                            String projectCode = DataTypeHelpers.importTernaryOperate(objMx.get(i), 15);//医保项目编码
                                            if(StringUtils.isNotBlank(projectCode)) {
                                                rrData.setProjectCode(projectCode);

                                                projectCode = projectCode.replace("，", ",");
                                                String[] pcArr = projectCode.split(",");
                                                projectCode = pcArr[0];
                                                rrData.setProjectCodeOne(projectCode);
                                            }
                                            String projectName = DataTypeHelpers.importTernaryOperate(objMx.get(i), 16);//医保项目名称
                                            if(StringUtils.isNotBlank(projectName)) {
                                                rrData.setProjectName(projectName);

                                                projectName = projectName.replace("，", ",");
                                                String[] pnArr = projectName.split(",");
                                                projectName = pnArr[0];
                                                rrData.setProjectNameOne(projectName);
                                            }
                                            String projectYyName = DataTypeHelpers.importTernaryOperate(objMx.get(i), 17);//医院项目名称
                                            rrData.setProjectYyName(projectYyName);
                                            String ruleName = DataTypeHelpers.importTernaryOperate(objMx.get(i), 18);//规则名称
                                            rrData.setRuleName(ruleName);
                                            BigDecimal bd = new BigDecimal(0);
                                            String violateCsPrice = DataTypeHelpers.importTernaryOperate(objMx.get(i), 19);//初审违规金额
                                            rrData.setViolateCsPriceStr(violateCsPrice);
                                            if (DataTypeHelpers.isNumeric(violateCsPrice)) {
                                                bd = new BigDecimal(violateCsPrice);
                                                rrData.setViolateCsPrice(bd);
                                            }

                                            String violatePrice = DataTypeHelpers.importTernaryOperate(objMx.get(i), 20);//违规金额
                                            bd = new BigDecimal(0);
                                            if (DataTypeHelpers.isNumeric(violatePrice)) {
                                                bd = new BigDecimal(violatePrice);
                                                rrData.setViolatePrice(bd);
                                            }
                                            String violateReason = DataTypeHelpers.importTernaryOperate(objMx.get(i), 21);//违规内容
                                            rrData.setViolateReason(violateReason);
                                            String zdNote = DataTypeHelpers.importTernaryOperate(objMx.get(i), 22);//诊断信息
                                            rrData.setZdNote(zdNote);
                                            String costDateStr = DataTypeHelpers.importTernaryOperate(objMx.get(i), 23);//费用日期
                                            if(StringUtils.isNotBlank(costDateStr)){
                                                rrData.setCostDate(formater.parse(costDateStr));
                                            }
                                            String insuredType = DataTypeHelpers.importTernaryOperate(objMx.get(i), 24);//险种类型
                                            rrData.setInsuredType(insuredType);
                                            String num = DataTypeHelpers.importTernaryOperate(objMx.get(i), 25);//数量
                                            bd = new BigDecimal(0);
                                            if (DataTypeHelpers.isNumeric(num)) {
                                                bd = new BigDecimal(num);
                                                rrData.setNum(bd);
                                            }
                                            String price = DataTypeHelpers.importTernaryOperate(objMx.get(i), 26);//	单价
                                            bd = new BigDecimal(0);
                                            if (DataTypeHelpers.isNumeric(price)) {
                                                bd = new BigDecimal(price);
                                                rrData.setPrice(bd);
                                            }
                                            String medicalPrice = DataTypeHelpers.importTernaryOperate(objMx.get(i), 27);//金额
                                            bd = new BigDecimal(0);
                                            if (DataTypeHelpers.isNumeric(medicalPrice)) {
                                                bd = new BigDecimal(medicalPrice);
                                                rrData.setMedicalPrice(bd);
                                            }
                                            String tcPayPrice = DataTypeHelpers.importTernaryOperate(objMx.get(i), 28);//统筹支付
                                            bd = new BigDecimal(0);
                                            if (DataTypeHelpers.isNumeric(tcPayPrice)) {
                                                bd = new BigDecimal(tcPayPrice);
                                                rrData.setTcPayPrice(bd);
                                            }
                                            String specs = DataTypeHelpers.importTernaryOperate(objMx.get(i), 29);//	规格
                                            rrData.setSpecs(specs);
                                            String jx = DataTypeHelpers.importTernaryOperate(objMx.get(i), 30);//剂型
                                            rrData.setJx(jx);
                                            String jgLevel = DataTypeHelpers.importTernaryOperate(objMx.get(i), 31);//机构等级
                                            rrData.setJgLevel(jgLevel);
                                            rrData.setState(0);
                                            rrData.setIsDeletemark(1);
                                            ListData.add(rrData);
                                        }
                                    } else {
                                        blError = true;
                                        message = "Excel导入失败，Sheet明细扣款 列表列数不正确";
                                    }
                                }
                                if (!blError) {
                                    if (ListData.size() > 0) {
                                        YbChsApply ybChsApply = new YbChsApply();
                                        ybChsApply.setState(YbDefaultValue.APPLYSTATE_2);
                                        ybChsApply.setId(pid);
                                        ybChsApply.setUploadFileName(uploadFileName);
                                        message = "Excel导入失败，插入数据异常.";
                                        this.iYbChsApplyDataService.importChsApply(ybChsApply, ListData);
                                        success = 1;
                                        message = "Excel导入成功.";
                                    } else {
                                        message = "Excel导入失败，导入数据为空.";
                                    }
                                }
                            } else {
                                message = "Excel导入失败，需确保 明细扣款,并确认列表数据是否正确.";
                            }
                        } else {
                            message = "Excel导入失败，需确保存在1个Sheet.";
                        }
                    } catch (Exception ex) {
                        //message = ex.getMessage();
                        if ("".equals(message)) {
                            message = "Excel导入失败.";
                        }
                        log.error(message, ex);
                    }
                } else {
                    message = "Excel导入失败.";
                }
            } else {
                message = "Excel导入失败，当前状态无法上传，待流程完成后再进行上传.";
            }
        }

        map.put("message",message);
        map.put("success",success);
        map.put("fileName",uploadFileName);
        return new FebsResponse().data(map);
    }

    @PostMapping("delJk")
    @RequiresPermissions("ybChsApplyData:add")
    public FebsResponse delJk1(String applyDateStr,Integer areaType) {
        ModelMap map = new ModelMap();
        int success = 0;
        try {
            message= this.iYbChsApplyDataService.delChsJk(applyDateStr,areaType);
            if(message.equals("ok")){
                success = 1;
            } else {
                if(message.equals("no")) {
                    message = applyDateStr + "未获申请数据.";
                }
                if(message.equals("no1")) {
                    message = applyDateStr + "数据，当前状态无法删除.";
                }
            }
        } catch (Exception e) {
            message = "获取HIS数据失败.";
            log.error(message, e);
        }

        map.put("message",message);
        map.put("success",success);
        return new FebsResponse().data(map);
    }

    @Log("删除")
    @DeleteMapping("deleteData")
    @RequiresPermissions("ybChsApplyData:delete")
    public FebsResponse deleteChsApplyByPids(@Valid YbChsApplyData ybChsApplyData) {
        ModelMap map = new ModelMap();
        int success = 0;
        try {
            int count = this.iYbChsApplyDataService.deleteChsApplyDataByPid(ybChsApplyData);
            success = count == 1 ? 1 : 0;
            if (success == 1) {
                message = "删除明细成功.";
            } else {
                message = "删除明细失败.";
            }
        } catch (Exception e) {
            message = "删除明细失败.";
            log.error(message, e);
        }

        map.put("message",message);
        map.put("success",success);
        return new FebsResponse().data(map);
    }

    @Log("His数据接口")
    @PostMapping("getJk")
    @RequiresPermissions("ybChsApplyData:his")
    public FebsResponse getHiss(String applyDateStr, Integer areaType, Integer isOutpfees) {
        ModelMap map = new ModelMap();
        int success = 0;
        try {

            String msg = this.iYbChsApplyDataService.findChsApplyDataTask(applyDateStr, areaType, isOutpfees);
            if (msg.equals("no")) {
                msg = iYbChsApplyDataService.findChsApplyDataNotTask(applyDateStr, areaType, isOutpfees);
                if (msg.equals("ok")) {
                    success = 1;
                } else if (msg.equals("")) {
                    message = "未找到相关" + applyDateStr + "数据或已完成复议.";
                } else if (msg.equals("no")) {
                    msg = iYbChsApplyDataService.findChsApplyProjCodeTask(applyDateStr, areaType, isOutpfees);
                    if (msg.equals("ok")) {
                        success = 1;
                    } else if (msg.equals("")) {
                        message = "未找到相关" + applyDateStr + "数据或已完成复议.";
                    } else if (msg.equals("no")) {
                        message = "His数据已获取完成.";
                    } else {
                        message = this.getHisMsg(msg);
                    }
                } else {
                    message = this.getHisMsg(msg);
                }
            } else {
                if (msg.equals("ok")) {
                    success = 1;
                } else if (msg.equals("")) {
                    message = "未找到相关" + applyDateStr + "数据或已完成复议.";
                } else {
                    message = this.getHisMsg(msg);
                }
            }

        } catch (Exception e) {
            message = "获取His数据失败.";
            log.error(message, e);
        }

        map.put("message",message);
        map.put("success",success);
        return new FebsResponse().data(map);
    }

    private String getHisMsg(String msg) {
        //"","no","deptNo","getMessage","hisSqlNo","dataNo",hisDataNo,hisMainNo,
        if (msg.equals("deptNo")) {
            return "获取科室数据异常.";
        } else if (msg.equals("hisSqlNo")) {
            return "获取查询语句异常.";
        } else if (msg.equals("dataNo")) {
            return "获取明细数据为空.";
        } else if (msg.equals("hisDataNo")) {
            return "获取His明细扣款数据为空.";
        } else if (msg.equals("hisMainNo")) {
            return "获取His主单扣款数据为空.";
        } else if (msg.equals("lockNo")) {
            return "已有其他用户正在获取His数据,请稍等...";
        } else {
            return msg;
        }

    }
}