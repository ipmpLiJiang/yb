package cc.mrbird.febs.yb.controller;

import cc.mrbird.febs.com.controller.DataTypeHelpers;
import cc.mrbird.febs.com.controller.FileHelpers;
import cc.mrbird.febs.com.controller.ImportExcelUtils;
import cc.mrbird.febs.common.annotation.Log;
import cc.mrbird.febs.common.controller.BaseController;
import cc.mrbird.febs.common.domain.FebsResponse;
import cc.mrbird.febs.common.exception.FebsException;
import cc.mrbird.febs.common.domain.QueryRequest;

import cc.mrbird.febs.common.properties.FebsProperties;
import cc.mrbird.febs.yb.domain.ResponseResult;
import cc.mrbird.febs.yb.domain.ResponseResultData;
import cc.mrbird.febs.yb.entity.YbDefaultValue;
import cc.mrbird.febs.yb.entity.YbReconsiderResetData;
import cc.mrbird.febs.yb.service.IYbReconsiderResetService;
import cc.mrbird.febs.yb.entity.YbReconsiderReset;

import cc.mrbird.febs.common.utils.FebsUtil;
import cc.mrbird.febs.system.domain.User;
import com.baomidou.mybatisplus.core.toolkit.StringPool;
import com.wuwenze.poi.ExcelKit;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.*;

/**
 * @author viki
 * @since 2020-08-17
 */
@Slf4j
@Validated
@RestController
@RequestMapping("ybReconsiderReset")

public class YbReconsiderResetController extends BaseController {

    private String message;
    @Autowired
    public IYbReconsiderResetService iYbReconsiderResetService;

    @Autowired
    private FebsProperties febsProperties;


    /**
     * 分页查询数据
     *
     * @param request           分页信息
     * @param ybReconsiderReset 查询条件
     * @return
     */
    @GetMapping
    @RequiresPermissions("ybReconsiderReset:view")
    public Map<String, Object> List(QueryRequest request, YbReconsiderReset ybReconsiderReset) {
        return getDataTable(this.iYbReconsiderResetService.findYbReconsiderResets(request, ybReconsiderReset));
    }

    /**
     * 添加
     *
     * @param ybReconsiderReset
     * @return
     */
    @Log("新增/按钮")
    @PostMapping
    @RequiresPermissions("ybReconsiderReset:add")
    public void addYbReconsiderReset(@Valid YbReconsiderReset ybReconsiderReset) throws FebsException {
        try {
            User currentUser = FebsUtil.getCurrentUser();
            ybReconsiderReset.setCreateUserId(currentUser.getUserId());
            this.iYbReconsiderResetService.createYbReconsiderReset(ybReconsiderReset);
        } catch (Exception e) {
            message = "新增/按钮失败";
            log.error(message, e);
            throw new FebsException(message);
        }
    }

    /**
     * 修改
     *
     * @param ybReconsiderReset
     * @return
     */
    @Log("修改")
    @PutMapping
    @RequiresPermissions("ybReconsiderReset:update")
    public void updateYbReconsiderReset(@Valid YbReconsiderReset ybReconsiderReset) throws FebsException {
        try {
            User currentUser = FebsUtil.getCurrentUser();
            ybReconsiderReset.setModifyUserId(currentUser.getUserId());
            this.iYbReconsiderResetService.updateYbReconsiderReset(ybReconsiderReset);
        } catch (Exception e) {
            message = "修改失败";
            log.error(message, e);
            throw new FebsException(message);
        }
    }

    @Log("删除")
    @DeleteMapping("/{ids}")
    @RequiresPermissions("ybReconsiderReset:delete")
    public void deleteYbReconsiderResets(@NotBlank(message = "{required}") @PathVariable String ids) throws FebsException {
        try {
            String[] arr_ids = ids.split(StringPool.COMMA);
            this.iYbReconsiderResetService.deleteYbReconsiderResets(arr_ids);
        } catch (Exception e) {
            message = "删除失败";
            log.error(message, e);
            throw new FebsException(message);
        }
    }

    @PostMapping("excel")
    @RequiresPermissions("ybReconsiderReset:export")
    public void export(QueryRequest request, YbReconsiderReset ybReconsiderReset, HttpServletResponse response) throws FebsException {
        try {
            List<YbReconsiderReset> ybReconsiderResets = this.iYbReconsiderResetService.findYbReconsiderResets(request, ybReconsiderReset).getRecords();
            ExcelKit.$Export(YbReconsiderReset.class, response).downXlsx(ybReconsiderResets, false);
        } catch (Exception e) {
            message = "导出Excel失败";
            log.error(message, e);
            throw new FebsException(message);
        }
    }

    @GetMapping("/{id}")
    public YbReconsiderReset detail(@NotBlank(message = "{required}") @PathVariable String id) {
        YbReconsiderReset ybReconsiderReset = this.iYbReconsiderResetService.getById(id);
        return ybReconsiderReset;
    }

    //复议申请更新resetState状态 按钮完成剔除
    @Log("修改")
    @PutMapping("updateApplyState")
    @RequiresPermissions("ybReconsiderReset:updateResetState")
    public FebsResponse updateReconsiderResetApplyState(@Valid YbReconsiderReset ybReconsiderReset) {
        int success = 0;
        try {
            message = this.iYbReconsiderResetService.updateReconsiderApplyState(ybReconsiderReset);
            if (message.equals("ok")) {
                success = 1;
                message = "完成剔除成功";
            }
        } catch (Exception e) {
            message = "完成剔除失败.";
            log.error(message, e);
        }
        ResponseResult rr = new ResponseResult();
        rr.setMessage(message);
        rr.setSuccess(success);
        return new FebsResponse().data(rr);
    }

    @PostMapping("importReconsiderReset")
    @RequiresPermissions("ybReconsiderReset:import")
    public FebsResponse importReconsiderReset(@RequestParam MultipartFile file, @RequestParam String applyDateStr) {
        int success = 0;
        if (file.isEmpty()) {
            message = "空文件";
        } else {
            YbReconsiderReset yRr = this.iYbReconsiderResetService.findReconsiderResetByApplyDateStr(applyDateStr);
            if (yRr == null) {
                boolean blError = false;
                try {
                    User currentUser = FebsUtil.getCurrentUser();
                    Date thisDate= new Date();
                    String guid = UUID.randomUUID().toString();
                    String filePath = febsProperties.getUploadPath(); // 上传后的路径
                    File getFile = FileHelpers.fileUpLoad(file, filePath, guid, "ReconsiderResetTemp");
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
                                List<YbReconsiderResetData> ListData = new ArrayList<>();
                                List<YbReconsiderResetData> ListMain = new ArrayList<>();

                                YbReconsiderReset reconsiderReset = new YbReconsiderReset();
                                reconsiderReset.setId(guid);
                                reconsiderReset.setIsDeletemark(1);
                                reconsiderReset.setApplyDateStr(applyDateStr);
                                reconsiderReset.setState(0);
                                String newApplyDateStr = applyDateStr + "15";
                                Date applyDate = DataTypeHelpers.stringDateFormat(newApplyDateStr, null, false);
                                reconsiderReset.setApplyDate(applyDate);
                                List<String> mxOrderNumberList = new ArrayList<>();
                                String mxCongfu = "";
                                boolean mxIsNull = false;
                                List<String> zdOrderNumberList = new ArrayList<>();
                                String zdCongfu = "";
                                boolean zdIsNull = false;
                                if (objMx.size() > 1) {
                                    if (objMx.get(0).length >= 20) {
                                        for (int i = 1; i < objMx.size(); i++) {
                                            message = "明细扣款数据读取失败，请确保Excel列表数据正确无误.";
                                            YbReconsiderResetData rrData = new YbReconsiderResetData();
                                            rrData.setId(UUID.randomUUID().toString());
                                            rrData.setPid(guid);
                                            rrData.setIsDeletemark(1);
                                            String strOrderNumber = DataTypeHelpers.importTernaryOperate(objMx.get(i), 0);//序号',
                                            if (!DataTypeHelpers.isNullOrEmpty(strOrderNumber)) {
                                                if (!mxOrderNumberList.stream().anyMatch(task -> task.equals(strOrderNumber))) {
                                                    mxOrderNumberList.add(strOrderNumber);
                                                } else {
                                                    mxCongfu = DataTypeHelpers.stringSeparate(mxCongfu, strOrderNumber, "、");
                                                }
                                            } else {
                                                mxIsNull = true;
                                            }
                                            rrData.setOrderNumber(strOrderNumber);
                                            rrData.setOrderNum(i);
                                            String strSerialNo = DataTypeHelpers.importTernaryOperate(objMx.get(i), 1);//交易流水号',
                                            rrData.setSerialNo(strSerialNo);
                                            String strBillNo = DataTypeHelpers.importTernaryOperate(objMx.get(i), 2);//'单据号',
                                            rrData.setBillNo(strBillNo);
                                            String strProjectCode = DataTypeHelpers.importTernaryOperate(objMx.get(i), 3);//'项目编码',
                                            rrData.setProjectCode(strProjectCode);
                                            String strProjectName = DataTypeHelpers.importTernaryOperate(objMx.get(i), 4);//'项目名称',
                                            rrData.setProjectName(strProjectName);
                                            String strMedicalPrice = DataTypeHelpers.importTernaryOperate(objMx.get(i), 5);//'医保内金额',
                                            BigDecimal bd = new BigDecimal(0);
                                            if (DataTypeHelpers.isNumeric(strMedicalPrice)) {
                                                bd = new BigDecimal(strMedicalPrice);
                                                rrData.setMedicalPrice(bd);
                                            }
                                            String strRuleName = DataTypeHelpers.importTernaryOperate(objMx.get(i), 6);//'规则名称',
                                            rrData.setRuleName(strRuleName);
                                            String strDeductPrice = DataTypeHelpers.importTernaryOperate(objMx.get(i), 7);//'扣除金额',
                                            if (DataTypeHelpers.isNumeric(strDeductPrice)) {
                                                bd = new BigDecimal(strDeductPrice);
                                                rrData.setDeductPrice(bd);
                                            }
                                            String strDeductReason = DataTypeHelpers.importTernaryOperate(objMx.get(i), 8);//'扣除原因',
                                            rrData.setDeductReason(strDeductReason);
                                            String strRepaymentReason = DataTypeHelpers.importTernaryOperate(objMx.get(i), 9);//'还款原因',
                                            rrData.setRepaymentReason(strRepaymentReason);
                                            String strDoctorName = DataTypeHelpers.importTernaryOperate(objMx.get(i), 10);//'医生姓名',
                                            rrData.setDoctorName(strDoctorName);
                                            String strDeptCode = DataTypeHelpers.importTernaryOperate(objMx.get(i), 11);// '科室编码',
                                            rrData.setDeptCode(strDeptCode);
                                            String strDeptName = DataTypeHelpers.importTernaryOperate(objMx.get(i), 12);//'科室名称',
                                            rrData.setDeptName(strDeptName);
                                            //String strCostDate = objMx.get(i)[0].toString();//'费用日期',
                                            String strCostDateStr = DataTypeHelpers.importTernaryOperate(objMx.get(i), 13);//'费用日期str',
                                            rrData.setCostDateStr(strCostDateStr);
                                            String strHospitalizedNo = DataTypeHelpers.importTernaryOperate(objMx.get(i), 14);//'住院号',
                                            rrData.setHospitalizedNo(strHospitalizedNo);
                                            String strTreatmentMode = DataTypeHelpers.importTernaryOperate(objMx.get(i), 15);//'就医方式',
                                            rrData.setTreatmentMode(strTreatmentMode);
                                            //String strSettlementDate = objMx.get(i)[0].toString();//'结算日期',
                                            String strSettlementDateStr = DataTypeHelpers.importTernaryOperate(objMx.get(i), 16);//'结算日期Str',
                                            rrData.setSettlementDateStr(strSettlementDateStr);
                                            String strPersonalNo = DataTypeHelpers.importTernaryOperate(objMx.get(i), 17);//'个人编号',
                                            rrData.setPersonalNo(strPersonalNo);
                                            String strInsuredName = DataTypeHelpers.importTernaryOperate(objMx.get(i), 18);//'参保人姓名',
                                            rrData.setInsuredName(strInsuredName);
//                    String strCardNumber = DataTypeHelpers.importTernaryOperate(objMx.get(i),18);//'医保卡号',
//                    rrData.setCardNumber(strCardNumber);
                                            String strAreaName = DataTypeHelpers.importTernaryOperate(objMx.get(i), 19);//'统筹区名称',
                                            rrData.setAreaName(strAreaName);
                                            rrData.setDataType(YbDefaultValue.DATATYPE_0);
                                            rrData.setCreateUserId(currentUser.getUserId());
                                            rrData.setCreateTime(thisDate);
                                            ListData.add(rrData);
                                        }
                                    } else {
                                        blError = true;
                                        message = "Excel导入失败，Sheet明细扣款 列表列数不正确";
                                    }
                                }
                                if (!blError) {
                                    if (objZd.size() > 1) {
                                        if (objZd.get(0).length >= 13) {
                                            for (int i = 1; i < objZd.size(); i++) {
                                                message = "主单扣款数据读取失败，请确保Excel列表数据正确无误.";
                                                YbReconsiderResetData rrMain = new YbReconsiderResetData();
                                                rrMain.setIsDeletemark(1);
                                                rrMain.setId(UUID.randomUUID().toString());
                                                rrMain.setPid(guid);
                                                rrMain.setOrderNum(i);
                                                String strOrderNumber = DataTypeHelpers.importTernaryOperate(objZd.get(i), 0);//序号',
                                                if (!DataTypeHelpers.isNullOrEmpty(strOrderNumber)) {
                                                    if (!zdOrderNumberList.stream().anyMatch(task -> task.equals(strOrderNumber))) {
                                                        zdOrderNumberList.add(strOrderNumber);
                                                    } else {
                                                        zdCongfu = DataTypeHelpers.stringSeparate(zdCongfu, strOrderNumber, "、");
                                                    }
                                                } else {
                                                    zdIsNull = true;
                                                }
                                                rrMain.setOrderNumber(strOrderNumber);
                                                String strSerialNo = DataTypeHelpers.importTernaryOperate(objZd.get(i), 1);//交易流水号',
                                                rrMain.setSerialNo(strSerialNo);
                                                String strBillNo = DataTypeHelpers.importTernaryOperate(objZd.get(i), 2);//'单据号',
                                                rrMain.setBillNo(strBillNo);
                                                String strRuleName = DataTypeHelpers.importTernaryOperate(objZd.get(i), 3);//'规则名称',
                                                rrMain.setRuleName(strRuleName);
                                                String strMedicalPrice = DataTypeHelpers.importTernaryOperate(objZd.get(i), 4);//'医保内金额',
                                                BigDecimal bd = new BigDecimal(0);
                                                if (DataTypeHelpers.isNumeric(strMedicalPrice)) {
                                                    bd = new BigDecimal(strMedicalPrice);
                                                    rrMain.setMedicalPrice(bd);
                                                }
                                                String strDeductPrice = DataTypeHelpers.importTernaryOperate(objZd.get(i), 5);//'扣除金额',
                                                if (DataTypeHelpers.isNumeric(strDeductPrice)) {
                                                    bd = new BigDecimal(strDeductPrice);
                                                    rrMain.setDeductPrice(bd);
                                                }
                                                String strSettlementDateStr = DataTypeHelpers.importTernaryOperate(objZd.get(i), 6);//'结算日期Str',
                                                rrMain.setSettlementDateStr(strSettlementDateStr);
                                                String strHospitalizedNo = DataTypeHelpers.importTernaryOperate(objZd.get(i), 7);//'住院号',
                                                rrMain.setHospitalizedNo(strHospitalizedNo);
                                                String strTreatmentMode = DataTypeHelpers.importTernaryOperate(objZd.get(i), 8);//'就医方式',
                                                rrMain.setTreatmentMode(strTreatmentMode);
                                                String strPersonalNo = DataTypeHelpers.importTernaryOperate(objZd.get(i), 9);//'个人编号',
                                                rrMain.setPersonalNo(strPersonalNo);
                                                String strInsuredName = DataTypeHelpers.importTernaryOperate(objZd.get(i), 10);//'参保人姓名',
                                                rrMain.setInsuredName(strInsuredName);
                                                String strInsuredType = DataTypeHelpers.importTernaryOperate(objZd.get(i), 11);//'参保类型',
                                                rrMain.setInsuredType(strInsuredType);
                                                String strAreaName = DataTypeHelpers.importTernaryOperate(objZd.get(i), 12);//'统筹区名称',
                                                rrMain.setAreaName(strAreaName);
                                                rrMain.setDataType(YbDefaultValue.DATATYPE_1);
                                                rrMain.setCreateUserId(currentUser.getUserId());
                                                rrMain.setCreateTime(thisDate);
                                                ListMain.add(rrMain);
                                            }
                                        } else {
                                            blError = true;
                                            message = "Excel导入失败，Sheet主单扣款 列表列数不正确";
                                        }
                                    }
                                }
                                if (!blError) {
                                    message = "";
                                    if (ListData.size() > 0) {
                                        if (mxIsNull) {
                                            message = "明细扣款存在序号为空数据";
                                        }
                                        if (!DataTypeHelpers.isNullOrEmpty(mxCongfu)) {
                                            if (!DataTypeHelpers.isNullOrEmpty(message)) {
                                                message += " , 序号： " + mxCongfu + " 重复";
                                            } else {
                                                message = "明细扣款 序号： " + mxCongfu + " 重复";
                                            }
                                        }
                                    }
                                    if (ListMain.size() > 0) {
                                        if (!DataTypeHelpers.isNullOrEmpty(message)) {
                                            message += " 。 ";
                                        }
                                        if (zdIsNull) {
                                            message += "主单扣款存在序号为空数据";
                                        }
                                        if (!DataTypeHelpers.isNullOrEmpty(zdCongfu)) {
                                            if (!DataTypeHelpers.isNullOrEmpty(message)) {
                                                message += " , 序号： " + zdCongfu + " 重复";
                                            } else {
                                                message += "主单扣款 序号： " + zdCongfu + " 重复";
                                            }
                                        }
                                    }
                                    if (!DataTypeHelpers.isNullOrEmpty(message)) {
                                        message = "上传的剔除Excel  " + message + "。 请检查后重新上传.";
                                    }
                                    if (DataTypeHelpers.isNullOrEmpty(message)) {
                                        if (ListData.size() > 0 || ListMain.size() > 0) {
                                            this.iYbReconsiderResetService.importReconsiderResets(reconsiderReset, ListData, ListMain);
                                            success = 1;
                                            message = "Excel导入成功.";
                                        } else {
                                            message = "Excel导入失败，导入数据为空.";
                                        }
                                    }
                                }
                            } else {
                                message = "Excel导入失败,需确保存在1个Sheet 明细扣款、主单扣款,并确认列表数据是否正确.";
                            }
                        } else {
                            message = "Excel导入失败，需确保存在1个Sheet 明细扣款、主单扣款.";
                        }
                    } else {
                        message = "Excel导入失败，Sheet个数不正确,需确保存1个Sheet 明细扣款、主单扣款.";
                    }
                } catch (Exception ex) {
                    //message = ex.getMessage();
                    if ("".equals(message)) {
                        message = "Excel导入失败.";
                    }
                    log.error(message, ex);
                }
            } else {
                message = "已导入过剔除数据.";
            }
        }

        ResponseResultData rrd = new ResponseResultData();
        rrd.setSuccess(success);
        rrd.setMessage(message);
        return new FebsResponse().data(rrd);
    }

    @PostMapping("importReconsiderReset1")
    public FebsResponse importReconsiderReset1(@RequestParam MultipartFile file, @RequestParam String applyDateStr) throws FebsException {
        Workbook wb = null;
        Map<String, Object> result = new HashMap<>();
        if (file.isEmpty()) {
            //result.put("error", "空文件");
            throw new FebsException("空文件");
        }
        // 根据文件后缀名不同(xls和xlsx)获得不同的Workbook实现类对象
        Iterator<Sheet> sheets = null;
        List<List> returnlist = new ArrayList<List>();
        try {

            String filePath = febsProperties.getUploadPath(); // 上传后的路径
            File getFile = FileHelpers.fileUpLoad(file, filePath, "", "ReconsiderResetTemp");

            String fileName = getFile.getName();// 读取上传文件(excel)的名字，含后缀后
            if (fileName.endsWith("xls")) {
                wb = new HSSFWorkbook(new FileInputStream(getFile));
                sheets = wb.iterator();
            } else if (fileName.endsWith("xlsx")) {
                wb = new XSSFWorkbook(new FileInputStream(getFile));
                sheets = wb.iterator();
            }
            if (sheets == null) {
                throw new FebsException("excel中不含有sheet工作表");
            }
            // 遍历excel里每个sheet的数据。
            while (sheets.hasNext()) {
                System.out.println("-----遍历sheet-----");
                Sheet sheet = sheets.next();
                for (Row r : sheet) {
                    //如果当前行的行号（从0开始）未达到2（第三行）则从新循环
                    if (r.getRowNum() < 4) {
                        continue;
                    }
                    //r.getCell(2).getNumericCellValue()
                    String str1 = r.getCell(0).getStringCellValue();
                    String str2 = r.getCell(1).getStringCellValue();
                    String str3 = r.getCell(2).getStringCellValue();
                    String str4 = r.getCell(3).getStringCellValue();
                    String str5 = r.getCell(4).getStringCellValue();
                    String str6 = r.getCell(5).getStringCellValue();
                    String str7 = r.getCell(6).getStringCellValue();
                    String str8 = r.getCell(7).getStringCellValue();
                    String str9 = r.getCell(8).getStringCellValue();
                }
//                List<Map> list = getCellValue(sheet);
//                System.out.println(list);
//                returnlist.add(list);
            }
        } catch (Exception ex) {
            throw new FebsException(ex.getMessage());
        } finally {
            try {
                if (wb != null) {
                    wb.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        //result.put("data", successList);
        result.put("haveError", "!CollectionUtil.isEmpty(errorList)");
        result.put("timeConsuming", " (System.currentTimeMillis() - beginMillis) / 1000L");
        result.put("filename", "file.getOriginalFilename()");
        return new FebsResponse().data(result);
    }


}