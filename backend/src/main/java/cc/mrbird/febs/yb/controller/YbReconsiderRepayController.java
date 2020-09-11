package cc.mrbird.febs.yb.controller;

import cc.mrbird.febs.com.controller.DataTypeHelpers;
import cc.mrbird.febs.com.controller.FileHelpers;
import cc.mrbird.febs.com.controller.ImportExcelUtils;
import cc.mrbird.febs.common.annotation.Log;
import cc.mrbird.febs.common.controller.BaseController;
import cc.mrbird.febs.common.domain.FebsResponse;
import cc.mrbird.febs.common.domain.router.VueRouter;
import cc.mrbird.febs.common.exception.FebsException;
import cc.mrbird.febs.common.domain.QueryRequest;

import cc.mrbird.febs.common.properties.FebsProperties;
import cc.mrbird.febs.yb.domain.ResponseImportResultData;
import cc.mrbird.febs.yb.entity.*;
import cc.mrbird.febs.yb.service.IYbReconsiderRepayService;

import cc.mrbird.febs.common.utils.FebsUtil;
import cc.mrbird.febs.system.domain.User;
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
import java.io.File;
import java.math.BigDecimal;
import java.util.*;

/**
 * @author viki
 * @since 2020-09-07
 */
@Slf4j
@Validated
@RestController
@RequestMapping("ybReconsiderRepay")

public class YbReconsiderRepayController extends BaseController {

    private String message;
    @Autowired
    public IYbReconsiderRepayService iYbReconsiderRepayService;

    @Autowired
    private FebsProperties febsProperties;


    /**
     * 分页查询数据
     *
     * @param request           分页信息
     * @param ybReconsiderRepay 查询条件
     * @return
     */
    @GetMapping
    @RequiresPermissions("ybReconsiderRepay:view")
    public Map<String, Object> List(QueryRequest request, YbReconsiderRepay ybReconsiderRepay) {
        return getDataTable(this.iYbReconsiderRepayService.findYbReconsiderRepays(request, ybReconsiderRepay));
    }

    /**
     * 添加
     *
     * @param ybReconsiderRepay
     * @return
     */
    @Log("新增/按钮")
    @PostMapping
    @RequiresPermissions("ybReconsiderRepay:add")
    public void addYbReconsiderRepay(@Valid YbReconsiderRepay ybReconsiderRepay) throws FebsException {
        try {
            User currentUser = FebsUtil.getCurrentUser();
            ybReconsiderRepay.setCreateUserId(currentUser.getUserId());
            this.iYbReconsiderRepayService.createYbReconsiderRepay(ybReconsiderRepay);
        } catch (Exception e) {
            message = "新增/按钮失败";
            log.error(message, e);
            throw new FebsException(message);
        }
    }

    /**
     * 修改
     *
     * @param ybReconsiderRepay
     * @return
     */
    @Log("修改")
    @PutMapping
    @RequiresPermissions("ybReconsiderRepay:update")
    public void updateYbReconsiderRepay(@Valid YbReconsiderRepay ybReconsiderRepay) throws FebsException {
        try {
            User currentUser = FebsUtil.getCurrentUser();
            ybReconsiderRepay.setModifyUserId(currentUser.getUserId());
            this.iYbReconsiderRepayService.updateYbReconsiderRepay(ybReconsiderRepay);
        } catch (Exception e) {
            message = "修改失败";
            log.error(message, e);
            throw new FebsException(message);
        }
    }


    @Log("删除")
    @DeleteMapping("/{ids}")
    @RequiresPermissions("ybReconsiderRepay:delete")
    public void deleteYbReconsiderRepays(@NotBlank(message = "{required}") @PathVariable String ids) throws FebsException {
        try {
            String[] arr_ids = ids.split(StringPool.COMMA);
            this.iYbReconsiderRepayService.deleteYbReconsiderRepays(arr_ids);
        } catch (Exception e) {
            message = "删除失败";
            log.error(message, e);
            throw new FebsException(message);
        }
    }

    @PostMapping("excel")
    @RequiresPermissions("ybReconsiderRepay:export")
    public void export(QueryRequest request, YbReconsiderRepay ybReconsiderRepay, HttpServletResponse response) throws FebsException {
        try {
            List<YbReconsiderRepay> ybReconsiderRepays = this.iYbReconsiderRepayService.findYbReconsiderRepays(request, ybReconsiderRepay).getRecords();
            ExcelKit.$Export(YbReconsiderRepay.class, response).downXlsx(ybReconsiderRepays, false);
        } catch (Exception e) {
            message = "导出Excel失败";
            log.error(message, e);
            throw new FebsException(message);
        }
    }

    @GetMapping("/{id}")
    public YbReconsiderRepay detail(@NotBlank(message = "{required}") @PathVariable String id) {
        YbReconsiderRepay ybReconsiderRepay = this.iYbReconsiderRepayService.getById(id);
        return ybReconsiderRepay;
    }

    private String getNianYue(String nianYue) {
        String resultNianYue = "";
        if (nianYue.length() == 6) {
            String nian = nianYue.substring(0, 4);
            boolean isInteger = DataTypeHelpers.isInteger(nian);
            if (isInteger) {
                String yue = nianYue.substring(4, nianYue.length());
                isInteger = DataTypeHelpers.isInteger(yue);
                if (isInteger) {
                    resultNianYue = nian + "-" + yue;
                }
            }
        }

        return resultNianYue;
    }

    @PostMapping("importReconsiderRepayData")
    @RequiresPermissions("ybReconsiderRepay:add")
    public FebsResponse importReconsiderRepayData(@RequestParam MultipartFile file, @RequestParam Integer repayType, @RequestParam Integer dataType) {
        int success = 0;
        String uploadFileName = "";
        if (file.isEmpty()) {
            message = "空文件";
        } else {
            uploadFileName = file.getOriginalFilename();
            boolean blError = false;
            String mmError = "上传的Excel文件,Sheet命名错误,请按规则更改.";
            try {
                String guid = UUID.randomUUID().toString();
                String filePath = febsProperties.getUploadPath(); // 上传后的路径
                File getFile = FileHelpers.fileUpLoad(file, filePath, guid, "ReconsiderRepayTemp");
                Map<Integer, String> sheetMap = ImportExcelUtils.getSheelNames(getFile);
                Map<Integer, String> newSheetMap = new HashMap<>();
                Integer addMap = 0;
                if (repayType == 0) {
                    for (Integer key : sheetMap.keySet()) {
                        String sheetName = sheetMap.get(key);
                        sheetName = this.getNianYue(sheetName);
                        if ("".equals(sheetName)) {
                            message = mmError;
                            blError = true;
                            break;
                        } else {
                            newSheetMap.put(addMap, sheetName);
                            addMap++;
                        }
                    }
                } else {
                    if (sheetMap.size() == 1) {
                        if (!sheetMap.get(0).equals("职保明细")) {
                            blError = true;
                            message = mmError;
                        }
                    } else {
                        message = mmError;
                        blError = true;
                    }
                }
                List<YbReconsiderRepayData> listRrd = new ArrayList<YbReconsiderRepayData>();
                if (!blError) {
                    if (repayType == 0) {
                        for (Integer key : newSheetMap.keySet()) {
                            String value = newSheetMap.get(key);
                            String valueSheet = sheetMap.get(key);
                            List<Object[]> objJb = ImportExcelUtils.importExcelBySheetIndex(getFile, key, 0, 0);
                            if (objJb.size() > 1) {
                                if (objJb.get(0).length == 19) {
                                    for (int i = 1; i < objJb.size(); i++) {
                                        message = "Excel导入 Sheet " + value + "数据读取失败，请确保Excel列表数据正确无误.";
                                        YbReconsiderRepayData rrd = new YbReconsiderRepayData();
                                        rrd.setId(UUID.randomUUID().toString());
                                        rrd.setPid(guid);
                                        rrd.setBelongDateStr(value);
                                        rrd.setBelongDateUpload(valueSheet);
                                        Date belongDate = DataTypeHelpers.stringDateFormat(value + "-15", "yyyy-MM-dd",true);
                                        rrd.setBelongDate(belongDate);
                                        rrd.setIsDeletemark(1);
                                        String strOrderNumber = DataTypeHelpers.importTernaryOperate(objJb.get(i), 0);//序号',
                                        rrd.setOrderNumber(strOrderNumber);
                                        String strSerialNo = DataTypeHelpers.importTernaryOperate(objJb.get(i), 1);//交易流水号',
                                        rrd.setSerialNo(strSerialNo);
                                        String strBillNo = DataTypeHelpers.importTernaryOperate(objJb.get(i), 2);//'单据号',
                                        rrd.setBillNo(strBillNo);
                                        String strProjectCode = DataTypeHelpers.importTernaryOperate(objJb.get(i), 3);//'项目编码',
                                        rrd.setProjectCode(strProjectCode);
                                        String strProjectName = DataTypeHelpers.importTernaryOperate(objJb.get(i), 4);//'项目名称',
                                        rrd.setProjectName(strProjectName);
                                        BigDecimal bd = new BigDecimal(0);
                                        String strMedicalPrice = DataTypeHelpers.importTernaryOperate(objJb.get(i), 5);//'医保内金额',
                                        if (DataTypeHelpers.isNumeric(strMedicalPrice)) {
                                            bd = new BigDecimal(strMedicalPrice);
                                            rrd.setMedicalPrice(bd);
                                        }
                                        String strRuleName = DataTypeHelpers.importTernaryOperate(objJb.get(i), 6);//'规则名称',
                                        rrd.setRuleName(strRuleName);

                                        String strDeductPrice = DataTypeHelpers.importTernaryOperate(objJb.get(i), 7);//'扣除金额',
                                        if (DataTypeHelpers.isNumeric(strDeductPrice)) {
                                            bd = new BigDecimal(strDeductPrice);
                                            rrd.setDeductPrice(bd);
                                        }
                                        String strDeductReason = DataTypeHelpers.importTernaryOperate(objJb.get(i), 8);//'扣除原因',
                                        rrd.setDeductReason(strDeductReason);
                                        String strRepaymentReason = DataTypeHelpers.importTernaryOperate(objJb.get(i), 9);//'还款原因',
                                        rrd.setRepaymentReason(strRepaymentReason);
                                        String strDoctorName = DataTypeHelpers.importTernaryOperate(objJb.get(i), 10);//'医生姓名',
                                        rrd.setDoctorName(strDoctorName);
                                        String strDeptCode = DataTypeHelpers.importTernaryOperate(objJb.get(i), 11);// '科室编码',
                                        rrd.setDeptCode(strDeptCode);
                                        String strDeptName = DataTypeHelpers.importTernaryOperate(objJb.get(i), 12);//'科室名称',
                                        rrd.setDeptName(strDeptName);
                                        String strCostDateStr = DataTypeHelpers.importTernaryOperate(objJb.get(i), 13);//'费用日期str',
                                        rrd.setCostDateStr(strCostDateStr);
                                        String strHospitalizedNo = DataTypeHelpers.importTernaryOperate(objJb.get(i), 14);//'住院号',
                                        rrd.setHospitalizedNo(strHospitalizedNo);
                                        String strTreatmentMode = DataTypeHelpers.importTernaryOperate(objJb.get(i), 15);//'就医方式',
                                        rrd.setTreatmentMode(strTreatmentMode);
                                        String strSettlementDateStr = DataTypeHelpers.importTernaryOperate(objJb.get(i), 16);//'结算日期Str',
                                        rrd.setSettlementDateStr(strSettlementDateStr);
                                        String strPersonalNo = DataTypeHelpers.importTernaryOperate(objJb.get(i), 17);//'个人编号',
                                        rrd.setPersonalNo(strPersonalNo);
                                        String strInsuredName = DataTypeHelpers.importTernaryOperate(objJb.get(i), 18);//'参保人姓名',
                                        rrd.setInsuredName(strInsuredName);
                                        rrd.setUpdateType(0);
                                        rrd.setWarnType(0);
                                        rrd.setRepayType(repayType);
                                        rrd.setDataType(dataType);
                                        rrd.setSeekState(0);
                                        rrd.setState(0);
                                        listRrd.add(rrd);
                                    }
                                } else {
                                    blError = true;
                                    message = "Excel导入失败，Sheet " + value + " 列表列数不正确";
                                    break;
                                }
                            }
                        }
                    } else {
                        String value = sheetMap.get(0);
                        List<Object[]> objJb = ImportExcelUtils.importExcelBySheetIndex(getFile, 0, 0, 0);
                        if (objJb.size() > 1) {
                            if (objJb.get(0).length == 12) {
                                for (int i = 1; i < objJb.size(); i++) {
                                    message = "Excel导入 Sheet " + value + "数据读取失败，请确保Excel列表数据正确无误.";
                                    YbReconsiderRepayData rrd = new YbReconsiderRepayData();
                                    rrd.setId(UUID.randomUUID().toString());
                                    rrd.setPid(guid);
                                    rrd.setIsDeletemark(1);
                                    BigDecimal bd = new BigDecimal(0);

                                    String strHospitalCode = DataTypeHelpers.importTernaryOperate(objJb.get(i), 0);//医院编号',
                                    rrd.setHospitalCode(strHospitalCode);
                                    String strHospitalName = DataTypeHelpers.importTernaryOperate(objJb.get(i), 1);//医院名称',
                                    rrd.setOrderNumber(strHospitalName);
                                    String strBelongDateStr = DataTypeHelpers.importTernaryOperate(objJb.get(i), 2);//所属期',
                                    rrd.setBelongDateUpload(strBelongDateStr);
                                    strBelongDateStr = this.getNianYue(strBelongDateStr);
                                    if ("".equals(strBelongDateStr)) {
                                        blError = true;
                                        message = "Excel导入 Sheet " + value + "数据读取失败，第" + i + "行所属区,格式错误,请修改后再继续.";
                                        break;
                                    } else {
                                        rrd.setBelongDateStr(strBelongDateStr);
                                        Date belongDate = DataTypeHelpers.stringDateFormat(strBelongDateStr + "-15", "yyyy-MM-dd",true);
                                        rrd.setBelongDate(belongDate);
                                    }

                                    String strOrderNumber = DataTypeHelpers.importTernaryOperate(objJb.get(i), 3);//序号',
                                    rrd.setOrderNumber(strOrderNumber);
                                    String strBillNo = DataTypeHelpers.importTernaryOperate(objJb.get(i), 4);//'单据号',
                                    rrd.setBillNo(strBillNo);
                                    String strCostDateStr = DataTypeHelpers.importTernaryOperate(objJb.get(i), 5);//'费用日期str',
                                    rrd.setCostDateStr(strCostDateStr);
                                    String strTreatmentMode = DataTypeHelpers.importTernaryOperate(objJb.get(i), 6);//'就医方式',
                                    rrd.setTreatmentMode(strTreatmentMode);
                                    String strPersonalNo = DataTypeHelpers.importTernaryOperate(objJb.get(i), 7);//'个人编号',
                                    rrd.setPersonalNo(strPersonalNo);
                                    String strInsuredName = DataTypeHelpers.importTernaryOperate(objJb.get(i), 8);//'参保人姓名',
                                    rrd.setInsuredName(strInsuredName);
                                    String strProjectName = DataTypeHelpers.importTernaryOperate(objJb.get(i), 9);//'项目名称',
                                    rrd.setProjectName(strProjectName);
                                    String strDeductPrice = DataTypeHelpers.importTernaryOperate(objJb.get(i), 10);//'扣除金额',
                                    if (DataTypeHelpers.isNumeric(strDeductPrice)) {
                                        bd = new BigDecimal(strDeductPrice);
                                        rrd.setDeductPrice(bd);
                                    }
                                    String strRepaymentPrice = DataTypeHelpers.importTernaryOperate(objJb.get(i), 11);//'还款金额',
                                    if (DataTypeHelpers.isNumeric(strRepaymentPrice)) {
                                        bd = new BigDecimal(strRepaymentPrice);
                                        rrd.setRepaymentPrice(bd);
                                    }
                                    rrd.setUpdateType(0);
                                    rrd.setWarnType(0);
                                    rrd.setRepayType(repayType);
                                    rrd.setDataType(dataType);
                                    rrd.setSeekState(0);
                                    rrd.setState(0);
                                    listRrd.add(rrd);
                                }
                            } else {
                                blError = true;
                                message = "Excel导入失败，Sheet " + value + " 列表列数不正确";
                            }
                        } else{
                            blError = true;
                            message = "Excel导入失败，Sheet " + value + " 无数据";
                        }
                    }

                    if (!blError) {
                        if (listRrd.size() > 0) {
                            User currentUser = FebsUtil.getCurrentUser();
                            Long uid = currentUser.getUserId();
                            String uname = currentUser.getUsername();
                            blError = this.iYbReconsiderRepayService.importReconsiderRepay(listRrd, uid, uname, uploadFileName);
                            if (blError) {
                                success = 1;
                                message = "Excel导入成功.";
                            }
                        }else{
                            message = "Excel导入失败，Sheet页 无数据";
                        }
                    }
                }
                if (success == 0) {
                    if ("".equals(message)) {
                        message = "Excel导入失败.";
                    }
                }
            } catch (Exception ex) {
                if ("".equals(message)) {
                    message = "Excel导入失败.";
                }
                log.error(message, ex);
            }
        }

        ResponseImportResultData rrd = new ResponseImportResultData();
        rrd.setSuccess(success);
        rrd.setMessage(message);

        rrd.setFileName(uploadFileName);
        return new FebsResponse().data(rrd);
    }
}