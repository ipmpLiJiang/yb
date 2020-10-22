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
import cc.mrbird.febs.system.domain.Dept;
import cc.mrbird.febs.yb.domain.ResponseImportResultData;
import cc.mrbird.febs.yb.domain.ResponseResult;
import cc.mrbird.febs.yb.domain.ResponseResultData;
import cc.mrbird.febs.yb.entity.*;
import cc.mrbird.febs.yb.service.IYbReconsiderApplyDataService;

//import cc.mrbird.febs.yb.service.IYbReconsiderApplyService;

import cc.mrbird.febs.common.utils.FebsUtil;
import cc.mrbird.febs.system.domain.User;
import cc.mrbird.febs.yb.service.IYbReconsiderApplyService;
import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.map.MapUtil;
import com.baomidou.mybatisplus.core.toolkit.StringPool;
import com.google.common.collect.Lists;
import com.wuwenze.poi.ExcelKit;
import com.wuwenze.poi.handler.ExcelReadHandler;
import com.wuwenze.poi.pojo.ExcelErrorField;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import java.io.*;
import java.math.BigDecimal;
import java.net.URLEncoder;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

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
    public IYbReconsiderApplyDataService iYbReconsiderApplyDataService;
    @Autowired
    public IYbReconsiderApplyService iYbReconsiderApplyService;
    @Autowired
    private FebsProperties febsProperties;

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


    @GetMapping("ListReconsiderApplyData")
    public Map<String, Object> findReconsiderApplyDatas(QueryRequest request, YbReconsiderApplyData ybReconsiderApplyData) {
        return getDataTable(this.iYbReconsiderApplyDataService.findYbReconsiderApplyDataList(request, ybReconsiderApplyData));
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
            User currentUser = FebsUtil.getCurrentUser();
            ybReconsiderApplyData.setCreateUserId(currentUser.getUserId());
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
            User currentUser = FebsUtil.getCurrentUser();
            ybReconsiderApplyData.setModifyUserId(currentUser.getUserId());
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
//                response.sendRedirect("../error.jsp");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @PostMapping("importReconsiderApplyData")
    @RequiresPermissions("ybReconsiderApplyData:add")
    public FebsResponse importReconsiderApplyData(@RequestParam MultipartFile file, @RequestParam String pid, @RequestParam Integer typeno) {
        int success = 0;
        String uploadFileName = "";
        if (file.isEmpty()) {
            message = "空文件";
        } else {
            uploadFileName = file.getOriginalFilename();
            boolean blError = false;
            try {
                String filePath = febsProperties.getUploadPath(); // 上传后的路径
                File getFile = FileHelpers.fileUpLoad(file, filePath, pid, "ReconsiderApplyTemp");
                Map<Integer, String> sheetMap = ImportExcelUtils.getSheelNames(getFile);
                if (sheetMap.size() == 2) {
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
                    if (nZd == 1 && nMx == 1) {
                        List<Object[]> objMx = new ArrayList<Object[]>();
                        List<Object[]> objZd = new ArrayList<Object[]>();
                        for (Integer key : sheetMap.keySet()) {
                            String value = sheetMap.get(key);
                            int sheetIndex = key;
                            if (value.equals("明细扣款")) {
                                objMx = ImportExcelUtils.importExcelBySheetIndex(getFile, key, 0, 0);
                            }
                            if (value.equals("主单扣款")) {
                                objZd = ImportExcelUtils.importExcelBySheetIndex(getFile, key, 0, 0);
                            }
                        }
                        if (objMx.size() > 1 || objZd.size() > 1) {
                            List<YbReconsiderApplyData> ListData = new ArrayList<YbReconsiderApplyData>();
                            List<YbReconsiderApplyData> ListMain = new ArrayList<YbReconsiderApplyData>();
                            String guid = pid;
                            if (objMx.size() > 1) {
                                if (objMx.get(0).length == 27) {
                                    for (int i = 1; i < objMx.size(); i++) {
                                        String strVersionNumber = DataTypeHelpers.importTernaryOperate(objMx.get(i), 25);//'版本号',
                                        if (typeno == 1 || (typeno == 2 && strVersionNumber.equals("2"))) {
                                            message = "明细扣款数据读取失败，请确保Excel列表数据正确无误.";
                                            YbReconsiderApplyData rrData = new YbReconsiderApplyData();
                                            rrData.setId(UUID.randomUUID().toString());
                                            rrData.setPid(guid);
                                            rrData.setIsDeletemark(1);
                                            String strOrderNumber = DataTypeHelpers.importTernaryOperate(objMx.get(i), 0);//序号',
                                            rrData.setOrderNumber(strOrderNumber);
                                            String strSerialNo = DataTypeHelpers.importTernaryOperate(objMx.get(i), 1);//交易流水号',
                                            rrData.setSerialNo(strSerialNo);
                                            String strBillNo = DataTypeHelpers.importTernaryOperate(objMx.get(i), 2);//'单据号',
                                            rrData.setBillNo(strBillNo);
                                            String strProposalCode = DataTypeHelpers.importTernaryOperate(objMx.get(i), 3);//交易流水号',
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
                                            String strHospitalizedNo = DataTypeHelpers.importTernaryOperate(objMx.get(i), 18);//'住院号',
                                            rrData.setHospitalizedNo(strHospitalizedNo);
                                            String strTreatmentMode = DataTypeHelpers.importTernaryOperate(objMx.get(i), 19);//'就医方式',
                                            rrData.setTreatmentMode(strTreatmentMode);
                                            String strSettlementDateStr = DataTypeHelpers.importTernaryOperate(objMx.get(i), 20);//'结算日期Str',
                                            rrData.setSettlementDateStr(strSettlementDateStr);
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
                                            rrData.setDataType(0);
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
                                    if (objZd.get(0).length == 18) {
                                        for (int i = 1; i < objZd.size(); i++) {
                                            String strVersionNumber = DataTypeHelpers.importTernaryOperate(objZd.get(i), 16);//'版本号',
                                            if (typeno == 1 || (typeno == 2 && strVersionNumber.equals("2"))) {
                                                message = "主单扣款数据读取失败，请确保Excel列表数据正确无误.";
                                                YbReconsiderApplyData rrMain = new YbReconsiderApplyData();
                                                rrMain.setIsDeletemark(1);
                                                rrMain.setId(UUID.randomUUID().toString());
                                                rrMain.setPid(guid);
                                                String strOrderNumber = DataTypeHelpers.importTernaryOperate(objZd.get(i), 0);//序号',
                                                rrMain.setOrderNumber(strOrderNumber);
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
                                                rrMain.setDataType(1);
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
                                //1待复议 2上传一 3申述一 4上传二 5申述二 6已剔除 7已还款
                                YbReconsiderApply ybReconsiderApply = new YbReconsiderApply();
                                if (typeno == 1) {
                                    ybReconsiderApply.setState(2);//State=2 审核一
                                    ybReconsiderApply.setUploadFileNameOne(uploadFileName);
                                }
                                if (typeno == 2) {
                                    ybReconsiderApply.setState(4);
                                    ybReconsiderApply.setUploadFileNameTwo(uploadFileName);
                                }
                                ybReconsiderApply.setId(pid);
                                this.iYbReconsiderApplyDataService.importReconsiderApply(ybReconsiderApply, ListData, ListMain);
                                success = 1;
                                message = "Excel导入成功.";
                            }
                        } else {
                            message = "Excel导入失败,需确保存在两个Sheet 明细扣款、主单扣款,并确认列表数据是否正确.";
                        }
                    } else if (nZd == 0 && nMx == 0) {
                        message = "Excel导入失败，需确保存在两个Sheet 明细扣款、主单扣款.";
                    } else if (nZd == 0 && nMx == 1) {
                        message = "Excel导入失败，Sheet 主单扣款不存在.";
                    } else if (nZd == 1 && nMx == 0) {
                        message = "Excel导入失败，Sheet 明细扣款不存在.";
                    }
                } else {
                    message = "Excel导入失败，Sheet个数不正确,需确保存在两个Sheet 明细扣款、主单扣款.";
                }
            } catch (Exception ex) {
                //message = ex.getMessage();
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