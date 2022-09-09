package cc.mrbird.febs.yb.controller;

import cc.mrbird.febs.com.controller.DataTypeHelpers;
import cc.mrbird.febs.com.controller.FileHelpers;
import cc.mrbird.febs.com.controller.ImportExcelUtils;
import cc.mrbird.febs.com.entity.ComSms;
import cc.mrbird.febs.common.annotation.Log;
import cc.mrbird.febs.common.controller.BaseController;
import cc.mrbird.febs.common.domain.FebsResponse;
import cc.mrbird.febs.common.domain.router.VueRouter;
import cc.mrbird.febs.common.exception.FebsException;
import cc.mrbird.febs.common.domain.QueryRequest;

import cc.mrbird.febs.common.properties.FebsProperties;
import cc.mrbird.febs.export.excel.ExportExcelUtils;
import cc.mrbird.febs.system.domain.Dept;
import cc.mrbird.febs.yb.domain.ResponseImportResultData;
import cc.mrbird.febs.yb.domain.ResponseResult;
import cc.mrbird.febs.yb.domain.ResponseResultData;
import cc.mrbird.febs.yb.entity.*;
import cc.mrbird.febs.yb.manager.YbApplyDataManager;
import cc.mrbird.febs.yb.service.IYbReconsiderApplyDataService;

//import cc.mrbird.febs.yb.service.IYbReconsiderApplyService;

import cc.mrbird.febs.common.utils.FebsUtil;
import cc.mrbird.febs.system.domain.User;
import cc.mrbird.febs.yb.service.IYbReconsiderApplyService;
import cc.mrbird.febs.yb.service.IYbReconsiderVerifyService;
import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.io.IoUtil;
import cn.hutool.core.map.MapUtil;
import cn.hutool.poi.excel.ExcelUtil;
import cn.hutool.poi.excel.ExcelWriter;
import cn.hutool.poi.excel.StyleSet;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import com.baomidou.mybatisplus.core.toolkit.StringPool;
import com.google.common.collect.Lists;
import com.wuwenze.poi.ExcelKit;
import com.wuwenze.poi.factory.ExcelMappingFactory;
import com.wuwenze.poi.handler.ExcelReadHandler;
import com.wuwenze.poi.pojo.ExcelErrorField;
import com.wuwenze.poi.pojo.ExcelMapping;
import com.wuwenze.poi.pojo.ExcelProperty;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.annotations.Case;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import java.io.*;
import java.math.BigDecimal;
import java.net.URLEncoder;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @author viki
 * @since 2020-07-17
 */
@Slf4j
@Validated
@RestController
@RequestMapping("ybReconsiderApplyData")

public class YbReconsiderApplyDataController extends BaseController {

    private String message;
    @Autowired
    IYbReconsiderApplyDataService iYbReconsiderApplyDataService;
    @Autowired
    IYbReconsiderApplyService iYbReconsiderApplyService;
    @Autowired
    FebsProperties febsProperties;
    @Autowired
    IYbReconsiderVerifyService iYbReconsiderVerifyService;
    @Autowired
    YbApplyDataManager ybApplyDataManager;

    /**
     * 分页查询数据
     *
     * @param request               分页信息
     * @param ybReconsiderApplyData 查询条件
     * @return
     */
    @GetMapping
    @RequiresPermissions("ybReconsiderApplyData:view")
    public Map<String, Object> List(QueryRequest request, YbReconsiderApplyData ybReconsiderApplyData) {
        return getDataTable(this.iYbReconsiderApplyDataService.findYbReconsiderApplyDatas(request, ybReconsiderApplyData));
    }


    @GetMapping("findReconsiderApplyDataList")
    public Map<String, Object> findReconsiderApplyDatas(QueryRequest request, YbReconsiderApplyData ybReconsiderApplyData) {
        return getDataTable(this.iYbReconsiderApplyDataService.findYbReconsiderApplyDataList(request, ybReconsiderApplyData));
    }

    @Log("updateCache")
    @PutMapping("updateCache")
    public FebsResponse updateCaches(String applyDateStr, Integer areaType) {
        int success = 0;
        try {
            YbReconsiderApply apply = iYbReconsiderApplyService.findReconsiderApplyByApplyDateStrs(applyDateStr, areaType);
            if (apply != null) {
                ybApplyDataManager.loadgetApplyDataCache(apply.getId(), applyDateStr + "-" + areaType);
                success = 1;
            }
        } catch (Exception e) {
            message = "updateCache失败";
            log.error(message, e);
        }

        ResponseResult rr = new ResponseResult();
        rr.setSuccess(success);
        rr.setMessage(message);
        return new FebsResponse().data(rr);
    }

    @Log("His数据接口")
    @PutMapping("getHis")
    @RequiresPermissions("ybReconsiderApplyData:his")
    public FebsResponse getHiss(String applyDateStr, Integer areaType, Integer typeno, Integer isOutpfees) {
        int success = 0;
        try {
            String msg = this.iYbReconsiderApplyDataService.findReconsiderApplyDataTask(applyDateStr, areaType, typeno, isOutpfees);
            if (msg.equals("no")) {
                msg = iYbReconsiderApplyDataService.findReconsiderApplyDataNotTask(applyDateStr, areaType, typeno, isOutpfees);
                if (msg.equals("ok")) {
                    success = 1;
                } else if (msg.equals("")) {
                    message = "未找到相关" + applyDateStr + "数据或已完成复议.";
                } else if (msg.equals("no")) {
                    msg = iYbReconsiderApplyDataService.findReconsiderApplyProjCodeTask(applyDateStr, areaType, typeno, isOutpfees);
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

        ResponseResult rr = new ResponseResult();
        rr.setSuccess(success);
        rr.setMessage(message);
        return new FebsResponse().data(rr);
    }

    private String getHisMsg(String msg) {
        //"","no","deptNo","getMessage","hisSqlNo","dataNo",hisDataNo,hisMainNo,
        if (msg.equals("deptNo")) {
            return "获取科室无数据或异常，请重新获取.";
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

    /**
     * 添加
     *
     * @param ybReconsiderApplyData
     * @return
     */
    @Log("新增/按钮")
    @PostMapping
    @RequiresPermissions("ybReconsiderApplyData:add")
    public void addYbReconsiderApplyData(@Valid YbReconsiderApplyData ybReconsiderApplyData) throws FebsException {
        try {
            this.iYbReconsiderApplyDataService.createYbReconsiderApplyData(ybReconsiderApplyData);
        } catch (Exception e) {
            message = "新增/按钮失败";
            log.error(message, e);
            throw new FebsException(message);
        }
    }

    /**
     * 修改
     *
     * @param ybReconsiderApplyData
     * @return
     */
    @Log("修改")
    @PutMapping
    @RequiresPermissions("ybReconsiderApplyData:update")
    public void updateYbReconsiderApplyData(@Valid YbReconsiderApplyData ybReconsiderApplyData) throws FebsException {
        try {
            this.iYbReconsiderApplyDataService.updateYbReconsiderApplyData(ybReconsiderApplyData);
        } catch (Exception e) {
            message = "修改失败";
            log.error(message, e);
            throw new FebsException(message);
        }
    }

    @Log("删除")
    @DeleteMapping("deleteData")
    @RequiresPermissions("ybReconsiderApplyData:delete")
    public FebsResponse deleteReconsiderApplyByPids(@Valid YbReconsiderApplyData ybReconsiderApplyData) {
        int success = 0;
        try {
            int count = this.iYbReconsiderApplyDataService.deleteReconsiderApplyDataByPid(ybReconsiderApplyData);
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

        ResponseResult rr = new ResponseResult();
        rr.setSuccess(success);
        rr.setMessage(message);
        return new FebsResponse().data(rr);
    }

    @Log("删除")
    @DeleteMapping("/{ids}")
    @RequiresPermissions("ybReconsiderApplyData:delete")
    public void deleteYbReconsiderApplyDatas(@NotBlank(message = "{required}") @PathVariable String ids) throws FebsException {
        try {
            String[] arr_ids = ids.split(StringPool.COMMA);
            this.iYbReconsiderApplyDataService.deleteYbReconsiderApplyDatas(arr_ids);
        } catch (Exception e) {
            message = "删除失败";
            log.error(message, e);
            throw new FebsException(message);
        }
    }

    @PostMapping("excel")
    @RequiresPermissions("ybReconsiderApplyData:export")
    public void export(QueryRequest request, YbReconsiderApplyData ybReconsiderApplyData, HttpServletResponse response) throws FebsException {
        try {
            List<YbReconsiderApplyData> ybReconsiderApplyDatas = this.iYbReconsiderApplyDataService.findYbReconsiderApplyDatas(request, ybReconsiderApplyData).getRecords();
            ExcelKit.$Export(YbReconsiderApplyData.class, response).downXlsx(ybReconsiderApplyDatas, false);
        } catch (Exception e) {
            message = "导出Excel失败";
            log.error(message, e);
            throw new FebsException(message);
        }
    }

    @GetMapping("/{id}")
    public YbReconsiderApplyData detail(@NotBlank(message = "{required}") @PathVariable String id) {
        YbReconsiderApplyData ybReconsiderApplyData = this.iYbReconsiderApplyDataService.getById(id);
        return ybReconsiderApplyData;
    }

    @PostMapping("downFile")
    public void export1(HttpServletResponse response) throws FebsException {
        try {
            String sheetName1 = "明细扣款";
            String sheetName2 = "主单扣款";
            ExportExcelUtils.exportTemplateFileT(response, YbAppealResultDataExport.class, sheetName1, YbAppealResultMainExport.class, sheetName2);
        } catch (Exception e) {
            message = "导出Excel模板失败";
            log.error(message, e);
            throw new FebsException(message);
        }
    }
    /*
    @PostMapping("downFile")
    public void downFile(HttpServletResponse response) {
        try {
            String path = febsProperties.getUploadPath();
            String fileName = "复议审核模板.xlsx";
            String filePath = path + fileName;
            File file = new File(filePath);
            if (file.exists()) {
                InputStream ins = new FileInputStream(filePath);
                BufferedInputStream bins = new BufferedInputStream(ins);// 放到缓冲流里面
                OutputStream outs = response.getOutputStream();// 获取文件输出IO流
                BufferedOutputStream bouts = new BufferedOutputStream(outs);
                response.setHeader("Content-Disposition",
                        "attachment;filename=" + URLEncoder.encode(fileName, "utf-8"));

                int bytesRead = 0;
                byte[] buffer = new byte[512];
                //开始向网络传输文件流
                while ((bytesRead = bins.read(buffer, 0, 512)) != -1) {
                    bouts.write(buffer, 0, bytesRead);
                }
                bouts.flush();// 这里一定要调用flush()方法
                ins.close();
                bins.close();
                outs.close();
                bouts.close();
            } else {
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }*/

    @PostMapping("importReconsiderApplyData")
    @RequiresPermissions("ybReconsiderApplyData:add")
    public FebsResponse importReconsiderApplyData(@RequestParam MultipartFile file, @RequestParam String pid, @RequestParam Integer areaType, @RequestParam Integer typeno) {
        int success = 0;
        String uploadFileName = "";
        if (file.isEmpty()) {
            message = "空文件";
        } else {
            YbReconsiderApply queryReconsiderApply = new YbReconsiderApply();
            queryReconsiderApply.setId(pid);
            List<YbReconsiderApply> list = iYbReconsiderApplyService.findReconsiderApplyList(queryReconsiderApply);
            queryReconsiderApply = list.size() > 0 ? list.get(0) : null;

            if (queryReconsiderApply != null) {
                areaType = queryReconsiderApply.getAreaType();
                int state = queryReconsiderApply.getState();
                if ((typeno == YbDefaultValue.TYPENO_1 && (state == YbDefaultValue.APPLYSTATE_1 || state == YbDefaultValue.APPLYSTATE_2)) ||
                        typeno == YbDefaultValue.TYPENO_2 && (state == YbDefaultValue.APPLYSTATE_3 || state == YbDefaultValue.APPLYSTATE_4)) {
                    int checkCount = 0;
                    if (typeno == YbDefaultValue.TYPENO_2) {
                        checkCount = iYbReconsiderVerifyService.findReconsiderVerifyApplyDataCheckCounts(queryReconsiderApply.getId(), queryReconsiderApply.getApplyDateStr(), areaType, YbDefaultValue.TYPENO_1);
                    }
                    if (checkCount == 0) {
                        uploadFileName = file.getOriginalFilename();
                        boolean blError = false;
                        try {
                            String filePath = febsProperties.getUploadPath(); // 上传后的路径
                            File getFile = FileHelpers.fileUpLoad(file, filePath, pid, "ReconsiderApplyTemp");
                            Map<Integer, String> sheetMap = ImportExcelUtils.getSheelNames(getFile);
                            if (sheetMap.size() > 0) {
                                int nZd = 0;
                                int nMx = 0;
                                for (Integer key : sheetMap.keySet()) {
                                    String value = sheetMap.get(key);
                                    if (value.equals("明细扣款")) {
                                        nMx = 1;
                                    }
                                    if (value.equals("主单扣款")) {
                                        nZd = 1;
                                    }
                                }
                                if (nZd == 1 || nMx == 1) {
                                    List<Object[]> objMx = new ArrayList<Object[]>();
                                    List<Object[]> objZd = new ArrayList<Object[]>();
                                    for (Integer key : sheetMap.keySet()) {
                                        String value = sheetMap.get(key);
                                        if (value.equals("明细扣款")) {
                                            objMx = ImportExcelUtils.importExcelBySheetIndex(getFile, key, 0, 0);
                                        }
                                        if (value.equals("主单扣款")) {
                                            objZd = ImportExcelUtils.importExcelBySheetIndex(getFile, key, 0, 0);
                                        }
                                    }
                                    if (objMx.size() > 1 || objZd.size() > 1) {
                                        List<YbReconsiderApplyData> ListData = new ArrayList<>();
                                        List<YbReconsiderApplyData> ListMain = new ArrayList<>();

                                        if (objMx.size() > 1) {
                                            if (objMx.get(0).length >= 27) {
                                                for (int i = 1; i < objMx.size(); i++) {
                                                    String strVersionNumber = DataTypeHelpers.importTernaryOperate(objMx.get(i), 25);//'版本号',
                                                    if ((typeno == YbDefaultValue.TYPENO_1 && strVersionNumber.equals("1")) || (typeno == YbDefaultValue.TYPENO_2 && strVersionNumber.equals("2"))) {
                                                        message = "明细扣款数据读取失败，请确保Excel列表数据正确无误.";
                                                        YbReconsiderApplyData rrData = new YbReconsiderApplyData();
                                                        rrData.setId(UUID.randomUUID().toString());
                                                        rrData.setPid(pid);
                                                        rrData.setIsDeletemark(1);
                                                        String strOrderNumber = DataTypeHelpers.importTernaryOperate(objMx.get(i), 0);//序号',
                                                        rrData.setOrderNumber(strOrderNumber);
                                                        rrData.setOrderNum(i);
                                                        String strSerialNo = DataTypeHelpers.importTernaryOperate(objMx.get(i), 1);//交易流水号',
                                                        rrData.setSerialNo(strSerialNo);
                                                        String strBillNo = DataTypeHelpers.importTernaryOperate(objMx.get(i), 2);//'单据号',
                                                        rrData.setBillNo(strBillNo);
                                                        String strProposalCode = DataTypeHelpers.importTernaryOperate(objMx.get(i), 3);//意见书编码',
                                                        rrData.setProposalCode(strProposalCode);
                                                        String strProjectCode = DataTypeHelpers.importTernaryOperate(objMx.get(i), 4);//'项目编码',
                                                        rrData.setProjectCode(strProjectCode);
                                                        String strProjectName = DataTypeHelpers.importTernaryOperate(objMx.get(i), 5);//'项目名称',
                                                        rrData.setProjectName(strProjectName);
                                                        String strNum = DataTypeHelpers.importTernaryOperate(objMx.get(i), 6);//'数量',
                                                        BigDecimal bd = new BigDecimal(0);
                                                        if (DataTypeHelpers.isNumeric(strNum)) {
                                                            bd = new BigDecimal(strNum);
                                                            rrData.setNum(bd);
                                                        }
                                                        String strMedicalPrice = DataTypeHelpers.importTernaryOperate(objMx.get(i), 7);//'医保内金额',
                                                        bd = new BigDecimal(0);
                                                        if (DataTypeHelpers.isNumeric(strMedicalPrice)) {
                                                            bd = new BigDecimal(strMedicalPrice);
                                                            rrData.setMedicalPrice(bd);
                                                        }
                                                        String strRuleName = DataTypeHelpers.importTernaryOperate(objMx.get(i), 8);//'规则名称',
                                                        rrData.setRuleName(strRuleName);
                                                        String strDeductPrice = DataTypeHelpers.importTernaryOperate(objMx.get(i), 9);//'扣除金额',
                                                        if (DataTypeHelpers.isNumeric(strDeductPrice)) {
                                                            bd = new BigDecimal(strDeductPrice);
                                                            rrData.setDeductPrice(bd);
                                                        }
                                                        String strDeductReason = DataTypeHelpers.importTernaryOperate(objMx.get(i), 10);//'扣除原因',
                                                        rrData.setDeductReason(strDeductReason);
                                                        String strRepaymentReason = DataTypeHelpers.importTernaryOperate(objMx.get(i), 11);//'还款原因',
                                                        rrData.setRepaymentReason(strRepaymentReason);
                                                        String strDoctorName = DataTypeHelpers.importTernaryOperate(objMx.get(i), 12);//'医生姓名',
                                                        rrData.setDoctorName(strDoctorName);
                                                        String strDeptCode = DataTypeHelpers.importTernaryOperate(objMx.get(i), 13);// '科室编码',
                                                        rrData.setDeptCode(strDeptCode);
                                                        String strDeptName = DataTypeHelpers.importTernaryOperate(objMx.get(i), 14);//'科室名称',
                                                        rrData.setDeptName(strDeptName);
                                                        String strEnterHospitalDate = DataTypeHelpers.importTernaryOperate(objMx.get(i), 15);//'入院日期str',
                                                        rrData.setEnterHospitalDateStr(strEnterHospitalDate);
                                                        String strOutHospitalDate = DataTypeHelpers.importTernaryOperate(objMx.get(i), 16);//'出院日期str',
                                                        rrData.setOutHospitalDateStr(strOutHospitalDate);
                                                        String strCostDateStr = DataTypeHelpers.importTernaryOperate(objMx.get(i), 17);//'费用日期str',
                                                        rrData.setCostDateStr(strCostDateStr);
                                                        Date CostDate = DataTypeHelpers.stringToDate(strCostDateStr);
                                                        if (CostDate != null) {
                                                            rrData.setCostDate(CostDate);
                                                        } else {
                                                            message = "Excel导入失败，Sheet明细扣款 费用日期存在错误格式或为空，序号：" + strOrderNumber + ".";
                                                            blError = true;
                                                            break;
                                                        }
                                                        String strHospitalizedNo = DataTypeHelpers.importTernaryOperate(objMx.get(i), 18);//'住院号',
                                                        rrData.setHospitalizedNo(strHospitalizedNo);
                                                        String strTreatmentMode = DataTypeHelpers.importTernaryOperate(objMx.get(i), 19);//'就医方式',
                                                        rrData.setTreatmentMode(strTreatmentMode);
                                                        String strSettlementDateStr = DataTypeHelpers.importTernaryOperate(objMx.get(i), 20);//'结算日期Str',
                                                        rrData.setSettlementDateStr(strSettlementDateStr);
                                                        Date SettlementDate = DataTypeHelpers.stringToDate(strSettlementDateStr);
                                                        if (SettlementDate != null) {
                                                            rrData.setSettlementDate(SettlementDate);
                                                        } else {
                                                            message = "Excel导入失败，Sheet明细扣款 结算日期存在错误格式或为空，序号：" + strOrderNumber + ".";
                                                            blError = true;
                                                            break;
                                                        }

                                                        String strPersonalNo = DataTypeHelpers.importTernaryOperate(objMx.get(i), 21);//'个人编号',
                                                        rrData.setPersonalNo(strPersonalNo);
                                                        String strInsuredName = DataTypeHelpers.importTernaryOperate(objMx.get(i), 22);//'参保人姓名',
                                                        rrData.setInsuredName(strInsuredName);
                                                        String strCardNumber = DataTypeHelpers.importTernaryOperate(objMx.get(i), 23);//'医保卡号',
                                                        rrData.setCardNumber(strCardNumber);
                                                        String strAreaName = DataTypeHelpers.importTernaryOperate(objMx.get(i), 24);//'统筹区名称',
                                                        rrData.setAreaName(strAreaName);

                                                        rrData.setVersionNumber(strVersionNumber);
                                                        String strBackAppeal = DataTypeHelpers.importTernaryOperate(objMx.get(i), 26);//'反馈申诉',
                                                        rrData.setBackAppeal(strBackAppeal);
                                                        rrData.setTypeno(typeno);
                                                        rrData.setDataType(YbDefaultValue.DATATYPE_0);
                                                        rrData.setState(0);
                                                        ListData.add(rrData);
                                                    }
                                                }
                                            } else {
                                                blError = true;
                                                message = "Excel导入失败，Sheet明细扣款 列表列数不正确";
                                            }
                                        }
                                        if (!blError) {
                                            if (objZd.size() > 1) {
                                                if (objZd.get(0).length >= 18) {
                                                    for (int i = 1; i < objZd.size(); i++) {
                                                        String strVersionNumber = DataTypeHelpers.importTernaryOperate(objZd.get(i), 16);//'版本号',
                                                        if ((typeno == YbDefaultValue.TYPENO_1 && strVersionNumber.equals("1")) || (typeno == YbDefaultValue.TYPENO_2 && strVersionNumber.equals("2"))) {
                                                            message = "主单扣款数据读取失败，请确保Excel列表数据正确无误.";
                                                            YbReconsiderApplyData rrMain = new YbReconsiderApplyData();
                                                            rrMain.setIsDeletemark(1);
                                                            rrMain.setId(UUID.randomUUID().toString());
                                                            rrMain.setPid(pid);
                                                            String strOrderNumber = DataTypeHelpers.importTernaryOperate(objZd.get(i), 0);//序号',
                                                            rrMain.setOrderNumber(strOrderNumber);
                                                            rrMain.setOrderNum(i);
                                                            String strSerialNo = DataTypeHelpers.importTernaryOperate(objZd.get(i), 1);//交易流水号',
                                                            rrMain.setSerialNo(strSerialNo);
                                                            String strProposalCode = DataTypeHelpers.importTernaryOperate(objZd.get(i), 2);//意见书编码',
                                                            rrMain.setProposalCode(strProposalCode);
                                                            String strBillNo = DataTypeHelpers.importTernaryOperate(objZd.get(i), 3);//'单据号',
                                                            rrMain.setBillNo(strBillNo);
                                                            String strMedicalPrice = DataTypeHelpers.importTernaryOperate(objZd.get(i), 4);//'医保内金额',
                                                            BigDecimal bd = new BigDecimal(0);
                                                            if (DataTypeHelpers.isNumeric(strMedicalPrice)) {
                                                                bd = new BigDecimal(strMedicalPrice);
                                                                rrMain.setMedicalPrice(bd);
                                                            }
                                                            String strRuleName = DataTypeHelpers.importTernaryOperate(objZd.get(i), 5);//'规则名称',
                                                            rrMain.setRuleName(strRuleName);
                                                            String strDeductPrice = DataTypeHelpers.importTernaryOperate(objZd.get(i), 6);//'扣除金额',
                                                            if (DataTypeHelpers.isNumeric(strDeductPrice)) {
                                                                bd = new BigDecimal(strDeductPrice);
                                                                rrMain.setDeductPrice(bd);
                                                            }
                                                            String strSettlementDateStr = DataTypeHelpers.importTernaryOperate(objZd.get(i), 7);//'结算日期Str',
                                                            rrMain.setSettlementDateStr(strSettlementDateStr);
                                                            Date SettlementDate = DataTypeHelpers.stringToDate(strSettlementDateStr);
                                                            if (SettlementDate != null) {
                                                                rrMain.setSettlementDate(SettlementDate);
                                                            } else {
                                                                message = "Excel导入失败，Sheet主单扣款 结算日期存在错误格式或为空，序号：" + strOrderNumber + ".";
                                                                blError = true;
                                                                break;
                                                            }

                                                            String strHospitalizedNo = DataTypeHelpers.importTernaryOperate(objZd.get(i), 8);//'住院号',
                                                            rrMain.setHospitalizedNo(strHospitalizedNo);
                                                            String strEnterHospitalDate = DataTypeHelpers.importTernaryOperate(objZd.get(i), 9);//'入院日期str',
                                                            rrMain.setEnterHospitalDateStr(strEnterHospitalDate);
                                                            String strOutHospitalDate = DataTypeHelpers.importTernaryOperate(objZd.get(i), 10);//'出院日期str',
                                                            rrMain.setOutHospitalDateStr(strOutHospitalDate);
                                                            String strTreatmentMode = DataTypeHelpers.importTernaryOperate(objZd.get(i), 11);//'就医方式',
                                                            rrMain.setTreatmentMode(strTreatmentMode);
                                                            String strPersonalNo = DataTypeHelpers.importTernaryOperate(objZd.get(i), 12);//'个人编号',
                                                            rrMain.setPersonalNo(strPersonalNo);
                                                            String strInsuredName = DataTypeHelpers.importTernaryOperate(objZd.get(i), 13);//'参保人姓名',
                                                            rrMain.setInsuredName(strInsuredName);
                                                            String strInsuredType = DataTypeHelpers.importTernaryOperate(objZd.get(i), 14);//'参保类型',
                                                            rrMain.setInsuredType(strInsuredType);
                                                            String strAreaName = DataTypeHelpers.importTernaryOperate(objZd.get(i), 15);//'统筹区名称',
                                                            rrMain.setAreaName(strAreaName);
                                                            rrMain.setVersionNumber(strVersionNumber);
                                                            String strBackAppeal = DataTypeHelpers.importTernaryOperate(objZd.get(i), 17);//'反馈申诉',
                                                            rrMain.setBackAppeal(strBackAppeal);
                                                            rrMain.setTypeno(typeno);
                                                            rrMain.setDataType(YbDefaultValue.DATATYPE_1);
                                                            rrMain.setState(0);
                                                            ListMain.add(rrMain);
                                                        }
                                                    }
                                                } else {
                                                    blError = true;
                                                    message = "Excel导入失败，Sheet主单扣款 列表列数不正确";
                                                }
                                            }
                                        }
                                        if (!blError) {
                                            if (ListData.size() > 0 || ListMain.size() > 0) {
                                                //1待复议 2上传一 3申述一 4上传二 5申述二 6已剔除 7已还款
                                                YbReconsiderApply ybReconsiderApply = new YbReconsiderApply();
                                                if (typeno == 1) {
                                                    ybReconsiderApply.setState(YbDefaultValue.APPLYSTATE_2);//State=2 审核一
                                                    ybReconsiderApply.setUploadFileNameOne(uploadFileName);
                                                }
                                                if (typeno == 2) {
                                                    ybReconsiderApply.setState(YbDefaultValue.APPLYSTATE_4);
                                                    ybReconsiderApply.setUploadFileNameTwo(uploadFileName);
                                                }
                                                ybReconsiderApply.setId(pid);

                                                this.iYbReconsiderApplyDataService.importReconsiderApply(ybReconsiderApply, ListData, ListMain);
                                                this.ybApplyDataManager.loadgetApplyDataCache(pid, queryReconsiderApply.getApplyDateStr() + "-" + areaType);
                                                success = 1;
                                                message = "Excel导入成功.";
                                            } else {
                                                message = "Excel导入失败，导入数据为空.";
                                            }
                                        }
                                    } else {
                                        message = "Excel导入失败，需确保存在两个Sheet 明细扣款、主单扣款,并确认列表数据是否正确.";
                                    }
                                } else {
                                    message = "Excel导入失败，需确保存在1个Sheet 明细扣款、主单扣款.";
                                }
                            } else {
                                message = "Excel导入失败，Sheet个数不正确,需确保存在1个Sheet 明细扣款、主单扣款.";
                            }
                        } catch (Exception ex) {
                            //message = ex.getMessage();
                            if ("".equals(message)) {
                                message = "Excel导入失败.";
                            }
                            log.error(message, ex);
                        }
                    } else {
                        message = "Excel导入失败,版本一还有未核对的数据.";
                    }
                } else {
                    message = "Excel导入失败.";
                }
            } else {
                message = "Excel导入失败，当前状态无法上传，待流程完成后再进行上传.";
            }
        }

        ResponseImportResultData rrd = new ResponseImportResultData();
        rrd.setSuccess(success);
        rrd.setMessage(message);

        rrd.setFileName(uploadFileName);
        return new FebsResponse().data(rrd);
    }

    @PostMapping("importReconsiderApplyData1")
    public FebsResponse importReconsiderApplyData1(@RequestParam MultipartFile file, @RequestParam String pid, @RequestParam int typeno)
            throws IOException, ParseException {
        long beginMillis = System.currentTimeMillis();
        List<YbReconsiderApplyData> successList = Lists.newArrayList();
        List<Map<String, Object>> errorList = Lists.newArrayList();
        ExcelKit.$Import(YbReconsiderApplyData.class)
                .readXlsx(file.getInputStream(), new ExcelReadHandler<YbReconsiderApplyData>() {
                    @Override
                    public void onSuccess(int sheetIndex, int rowIndex, YbReconsiderApplyData entity) {
                        successList.add(entity); // 单行读取成功，加入入库队列。
                    }

                    @Override
                    public void onError(int sheetIndex, int rowIndex,
                                        List<ExcelErrorField> errorFields) {
                        // 读取数据失败，记录了当前行所有失败的数据
                        errorList.add(MapUtil.of(//
                                "sheetIndex", sheetIndex
                        ));
                        errorList.add(MapUtil.of(//
                                "rowIndex", rowIndex));
                        errorList.add(MapUtil.of(//
                                "errorFields", errorFields));
                    }
                });

        // TODO: 执行successList的入库操作。
        Map<String, Object> result = new HashMap<>();
        //result.put("data", successList);
        result.put("haveError", !CollectionUtil.isEmpty(errorList));
        result.put("error", errorList);
        result.put("timeConsuming", (System.currentTimeMillis() - beginMillis) / 1000L);
        result.put("filename", file.getOriginalFilename());
        if (CollectionUtil.isEmpty(errorList)) {
//            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            for (YbReconsiderApplyData d : successList) {
                d.setId(UUID.randomUUID().toString());
                //日期
//                Date date = sdf.parse(d.getEnterHospitalDateStr().replace('/','-'));
//                d.setEnterHospitalDate(date);
//                date = sdf.parse(d.getOutHospitalDateStr().replace('/','-'));
//                d.setOutHospitalDate(date);
//                date = sdf.parse(d.getCostDateStr().replace('/','-'));
//                d.setCostDate(date);
//                date = sdf.parse(d.getSettlementDateStr().replace('/','-'));
//                d.setSettlementDate(date);
                //日期
                d.setPid(pid);
                d.setIsDeletemark(1);
                d.setTypeno(typeno);
                //this.iYbReconsiderApplyDataService.createYbReconsiderApplyData(d);//把部门表放入数据库
            }
            //1待复议 2上传一 3申述一 4上传二 5申述二 6已剔除 7已还款
            YbReconsiderApply ybReconsiderApply = new YbReconsiderApply();
            if (typeno == 1) {
                ybReconsiderApply.setState(2);//State=2 审核一
            }
            if (typeno == 2) {
                ybReconsiderApply.setState(4);
            }
            ybReconsiderApply.setId(pid);
            this.iYbReconsiderApplyDataService.createBatchYbReconsiderApplyData(successList, ybReconsiderApply);

//            this.iYbReconsiderApplyService.updateYbReconsiderApply(ybReconsiderApply);
            new FebsResponse().data("上传成功");
        }

        return new FebsResponse().data(result);
    }
}
