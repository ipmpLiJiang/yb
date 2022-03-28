package cc.mrbird.febs.yb.controller;

import cc.mrbird.febs.com.controller.DataTypeHelpers;
import cc.mrbird.febs.com.controller.FileHelpers;
import cc.mrbird.febs.com.controller.ImportExcelUtils;
import cc.mrbird.febs.common.annotation.Log;
import cc.mrbird.febs.common.controller.BaseController;
import cc.mrbird.febs.common.domain.FebsResponse;
import cc.mrbird.febs.common.domain.QueryRequest;
import cc.mrbird.febs.common.exception.FebsException;
import cc.mrbird.febs.common.properties.FebsProperties;
import cc.mrbird.febs.common.utils.FebsUtil;
import cc.mrbird.febs.job.domain.Job;
import cc.mrbird.febs.job.service.JobService;
import cc.mrbird.febs.system.domain.User;
import cc.mrbird.febs.yb.domain.ResponseImportResultData;
import cc.mrbird.febs.yb.domain.ResponseResult;
import cc.mrbird.febs.yb.domain.ResponseResultData;
import cc.mrbird.febs.yb.entity.*;
import cc.mrbird.febs.yb.service.IYbReconsiderApplyDataService;
import cc.mrbird.febs.yb.service.IYbReconsiderApplyService;
import cc.mrbird.febs.yb.service.IYbReconsiderInpatientfeesService;
import cc.mrbird.febs.yb.service.IYbReconsiderVerifyService;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.StringPool;
import com.wuwenze.poi.ExcelKit;
import lombok.Data;
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
import java.util.*;
import java.util.stream.Collectors;

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

    @Autowired
    public IYbReconsiderApplyService iYbReconsiderApplyService;

    @Autowired
    public IYbReconsiderApplyDataService iYbReconsiderApplyDataService;

    @Autowired
    private IYbReconsiderInpatientfeesService iYbReconsiderInpatientfeesService;


    @Autowired
    private FebsProperties febsProperties;

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
//            ybReconsiderVerify.setCreateUserId(currentUser.getUserId());
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
//            ybReconsiderVerify.setModifyUserId(currentUser.getUserId());
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

    @Log("删除")
    @DeleteMapping("deleteVerifyState")
    @RequiresPermissions("ybReconsiderVerify:addImport")
    public FebsResponse deleteReconsiderVerifys(YbReconsiderVerify delVerify) {
        int success = 0;
        try {
            YbReconsiderApply ybReconsiderApply = this.iYbReconsiderApplyService.findReconsiderApplyByApplyDateStrs(delVerify.getApplyDateStr(), delVerify.getAreaType());
            if (ybReconsiderApply != null) {
                int state = ybReconsiderApply.getState();
                int typeno = state == YbDefaultValue.APPLYSTATE_2 || state == YbDefaultValue.APPLYSTATE_3 ? YbDefaultValue.TYPENO_1 :
                        state == YbDefaultValue.APPLYSTATE_4 || state == YbDefaultValue.APPLYSTATE_5 ? YbDefaultValue.TYPENO_2 : 0;
                delVerify.setTypeno(typeno);
                delVerify.setState(1);
                this.iYbReconsiderVerifyService.deleteReconsiderVerifyState(delVerify);
                message = "删除成功";
                success = 1;
            } else {
                message = "未找到" + delVerify.getApplyDateStr() + "年月数据.";
            }
        } catch (Exception e) {
            message = "删除失败.";
            log.error(message, e);
        }
        ResponseResult rr = new ResponseResult();
        rr.setSuccess(success);
        rr.setMessage(message);

        return new FebsResponse().data(rr);
    }

    @PostMapping("importReconsiderVerify")
    @RequiresPermissions("ybReconsiderVerify:addImport")
    public void importReconsiderVerifys(String applyDate, Integer areaType, int[] sumxu) throws FebsException {
        try {
            User currentUser = FebsUtil.getCurrentUser();
            List<Integer> sunxuList = new ArrayList<>();
            if(sumxu!=null && sumxu.length == 3){
                for (int item : sumxu){
                    sunxuList.add(item);
                }
            }
            this.iYbReconsiderVerifyService.insertReconsiderVerifyImports(applyDate, areaType, currentUser.getUserId(), currentUser.getUsername(),sunxuList);
        } catch (Exception e) {
            message = "匹配失败";
            log.error(message, e);
            throw new FebsException(message);
        }
    }

    @PostMapping("importMainReconsiderVerify")
    @RequiresPermissions("ybReconsiderVerify:addImport")
    public void importMainReconsiderVerifys(String applyDate, Integer areaType) throws FebsException {
        try {
            User currentUser = FebsUtil.getCurrentUser();
            this.iYbReconsiderVerifyService.insertMainReconsiderVerifyImports(applyDate, areaType, currentUser.getUserId(), currentUser.getUsername());
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
    @RequiresPermissions("ybReconsiderVerify:stateUpdate")
    public void updateSendState(String dataJson, Integer areaType, Integer dataType) throws FebsException {
        try {
            User currentUser = FebsUtil.getCurrentUser();
            Long uid = currentUser.getUserId();
            String uname = currentUser.getUsername();

            List<YbReconsiderVerify> list = JSON.parseObject(dataJson, new TypeReference<List<YbReconsiderVerify>>() {
            });

            this.iYbReconsiderVerifyService.updateSendStates(list, areaType, dataType, uid, uname);
        } catch (Exception e) {
            message = "发送失败";
            log.error(message, e);
            throw new FebsException(message);
        }
    }

    @Log("修改")
    @PutMapping("updateBackState")
    @RequiresPermissions("ybReconsiderVerify:stateUpdate")
    public void updateBackState(String applyDateStr, Integer areaType) throws FebsException {
        try {
            this.iYbReconsiderVerifyService.updateBackStates(applyDateStr, areaType, YbDefaultValue.VERIFYSTATE_2, 0);
        } catch (Exception e) {
            message = "退回失败";
            log.error(message, e);
            throw new FebsException(message);
        }
    }

    @Log("修改")
    @PutMapping("updateASendState")
    @RequiresPermissions("ybReconsiderVerify:stateUpdate")
    public void updateASendState(String applyDateStr, Integer areaType, Integer state, Integer dataType) throws FebsException {
        try {
            User currentUser = FebsUtil.getCurrentUser();
            Long uid = currentUser.getUserId();
            String uname = currentUser.getUsername();

            this.iYbReconsiderVerifyService.updateAllSendStates(applyDateStr, areaType, state, dataType, uid, uname);
        } catch (Exception e) {
            message = "发送失败";
            log.error(message, e);
            throw new FebsException(message);
        }
    }

    //单个，多个核对
    @Log("修改")
    @PutMapping("updateReviewerState")
    @RequiresPermissions("ybReconsiderVerify:stateUpdate")
    public void updateReviewerState(String dataJson) throws FebsException {
        try {
            User currentUser = FebsUtil.getCurrentUser();
            Long uid = currentUser.getUserId();
            String uname = currentUser.getUsername();

            List<YbReconsiderVerify> list = JSON.parseObject(dataJson, new TypeReference<List<YbReconsiderVerify>>() {
            });
            this.iYbReconsiderVerifyService.updateReviewerStates(list, uid, uname);
        } catch (Exception e) {
            message = "核对失败";
            log.error(message, e);
            throw new FebsException(message);
        }
    }

    @Log("修改")
    @PutMapping("updateAReviewerState")
    @RequiresPermissions("ybReconsiderVerify:stateUpdate")
    public void updateAReviewerState(String applyDateStr, Integer areaType, Integer state, Integer dataType) throws FebsException {
        try {
            User currentUser = FebsUtil.getCurrentUser();
            Long uid = currentUser.getUserId();
            String uname = currentUser.getUsername();

            this.iYbReconsiderVerifyService.updateAllReviewerStates(applyDateStr, areaType, state, dataType, uid, uname);
        } catch (Exception e) {
            message = "核对审核失败";
            log.error(message, e);
            throw new FebsException(message);
        }
    }

    @PostMapping("importReconsiderDataVerify")
    @RequiresPermissions("ybReconsiderVerify:addImport")
    public FebsResponse importReconsiderApplyData(@RequestParam MultipartFile file, @RequestParam String applyDateStr, @RequestParam Integer areaType, @RequestParam Integer dataType) {
        int success = 0;
        String uploadFileName = "";
        String ssm = "";
        if (file.isEmpty()) {
            message = "空文件";
        } else {
            YbReconsiderApply reconsiderApply = iYbReconsiderApplyService.findReconsiderApplyByApplyDateStrs(applyDateStr, areaType);

            if (reconsiderApply != null) {
                int state = reconsiderApply.getState();
                int typeno = state == YbDefaultValue.APPLYSTATE_2 || state == YbDefaultValue.APPLYSTATE_3 ? YbDefaultValue.TYPENO_1 :
                        state == YbDefaultValue.APPLYSTATE_4 || state == YbDefaultValue.APPLYSTATE_5 ? YbDefaultValue.TYPENO_2 : 0;

                if (typeno == YbDefaultValue.TYPENO_1 || typeno == YbDefaultValue.TYPENO_2) {
                    LambdaQueryWrapper<YbReconsiderApplyData> wrapper = new LambdaQueryWrapper<>();
                    wrapper.eq(YbReconsiderApplyData::getPid, reconsiderApply.getId());
                    wrapper.eq(YbReconsiderApplyData::getTypeno, typeno);
                    wrapper.eq(YbReconsiderApplyData::getDataType, dataType);
                    List<YbReconsiderApplyData> applyDataList = this.iYbReconsiderApplyDataService.list(wrapper);

                    if (applyDataList.size() > 0) {
                        uploadFileName = file.getOriginalFilename();
                        boolean blError = false;
                        User currentUser = FebsUtil.getCurrentUser();
                        try {
                            List<YbReconsiderApplyData> queryApplyDataList = new ArrayList<>();
                            String filePath = febsProperties.getUploadPath(); // 上传后的路径
                            File getFile = FileHelpers.fileUpLoad(file, filePath, reconsiderApply.getId(), "ReconsiderVerifyTemp");
                            Map<Integer, String> sheetMap = ImportExcelUtils.getSheelNames(getFile);
                            ssm = dataType == 0 ? "明细扣款" : "主单扣款";
                            if (sheetMap.size() > 0) {
                                int nZd = 0;
                                int nMx = 0;
                                String value = sheetMap.get(0);
                                if (value.equals("明细扣款")) {
                                    nMx = 1;
                                }
                                if (value.equals("主单扣款")) {
                                    nZd = 1;
                                }

                                if (nZd == 1 || nMx == 1) {
                                    List<Object[]> objMx = new ArrayList<Object[]>();
                                    List<Object[]> objZd = new ArrayList<Object[]>();
                                    if (value.equals("明细扣款")) {
                                        objMx = ImportExcelUtils.importExcelBySheetIndex(getFile, 0, 0, 0);
                                    }
                                    if (value.equals("主单扣款")) {
                                        objZd = ImportExcelUtils.importExcelBySheetIndex(getFile, 0, 0, 0);
                                    }
                                    if (objMx.size() > 1 || objZd.size() > 1) {
                                        List<YbReconsiderVerify> verifyList = new ArrayList<>();
                                        if (objMx.size() > 1) {
                                            if (objMx.get(0).length >= 48) {
                                                YbReconsiderInpatientfees queryRif = new YbReconsiderInpatientfees();
                                                queryRif.setApplyDateStr(applyDateStr);
                                                queryRif.setAreaType(areaType);
                                                queryRif.setDataType(dataType);
                                                queryRif.setTypeno(typeno);
                                                List<YbReconsiderInpatientfees> rifList = this.iYbReconsiderInpatientfeesService.findReconsiderInpatientfeesList(queryRif);
                                                for (int i = 1; i < objMx.size(); i++) {
                                                    YbReconsiderVerify rv = this.getReconsiderVerify(objMx, i, applyDateStr, areaType, currentUser, dataType, applyDataList, rifList);
                                                    if (rv != null) {
                                                        verifyList.add(rv);
                                                    }
                                                }
                                            } else {
                                                blError = true;
                                                message = "Excel导入失败，Sheet明细扣款 列表列数不正确";
                                            }
                                        }
                                        if (objZd.size() > 1) {
                                            if (objZd.get(0).length >= 23) {
                                                for (int i = 1; i < objZd.size(); i++) {
                                                    YbReconsiderVerify rv = this.getReconsiderVerify(objZd, i, applyDateStr, areaType, currentUser, dataType, applyDataList, null);
                                                    if (rv != null) {
                                                        verifyList.add(rv);
                                                    }
                                                }
                                            } else {
                                                blError = true;
                                                message = "Excel导入失败，Sheet主单扣款 列表列数不正确";
                                            }
                                        }

                                        if (!blError) {
                                            if (verifyList.size() > 0) {
                                                this.iYbReconsiderVerifyService.importReconsiderDataVerifys(reconsiderApply, dataType, typeno, verifyList);
                                                success = 1;
                                                message = "Excel导入成功.";
                                            } else {
                                                message = "Excel导入失败，导入数据为空.";
                                            }
                                        }
                                    } else {
                                        message = "Excel导入失败，请确认 " + ssm + " 列表数据是否正确.";
                                    }
                                } else {
                                    message = "Excel导入失败，确保Sheet页名为 " + ssm + " .";
                                }
                            } else {
                                message = "Excel导入失败，确保Sheet页名为 " + ssm + " .";
                            }
                        } catch (Exception ex) {
                            //message = ex.getMessage();
                            if ("".equals(message)) {
                                message = "Excel导入失败.";
                            }
                            log.error(message, ex);
                        }
                    } else {
                        message = typeno == 1 ? "第一版" : "第二版";
                        message += dataType == 0 ? " 明细扣款数据" : " 主单扣款数据";
                        message = "Excel导入失败，" + applyDateStr + " 未上传" + message + ".";
                    }
                } else {
                    ssm = state == YbDefaultValue.APPLYSTATE_1 ? "为：待复议" :
                            state == YbDefaultValue.APPLYSTATE_6 ? "为：已剔除" :
                                    state == YbDefaultValue.APPLYSTATE_7 ? "为：还款中" : "";
                    message = "Excel导入失败，当前状态" + ssm + " 无法上传.";
                }
            } else {
                message = "Excel导入失败.";
            }
        }

        ResponseImportResultData rrd = new ResponseImportResultData();
        rrd.setSuccess(success);
        rrd.setMessage(message);

        rrd.setFileName(uploadFileName);
        return new FebsResponse().data(rrd);
    }

    private YbReconsiderVerify getReconsiderVerify(List<Object[]> obj, int i, String applyDateStr, int areaType,
                                                   User currentUser, int dataType, List<YbReconsiderApplyData> applyDataList,
                                                   List<YbReconsiderInpatientfees> rifList) {
        YbReconsiderVerify ybReconsiderVerify = null;
        List<YbReconsiderApplyData> queryApplyDataList = new ArrayList<>();
        String strOrderNumber = DataTypeHelpers.importTernaryOperate(obj.get(i), 0);//序号
        String strDeptCode = "";
        String strDeptName = "";
        String strDocCode = "";
        String strDocName = "";
        String strOrderDeptCode = "";
        String strOrderDeptName = "";
        String strOrderDoctorCode = "";
        String strOrderDoctorName = "";
        if (dataType == 0) {
            strDeptCode = DataTypeHelpers.importTernaryOperate(obj.get(i), 28);//科室编码
            strDeptName = DataTypeHelpers.importTernaryOperate(obj.get(i), 29);//科室名称
            strDocCode = DataTypeHelpers.importTernaryOperate(obj.get(i), 30);//医生编码
            strDocName = DataTypeHelpers.importTernaryOperate(obj.get(i), 31);//医生名称

            strOrderDeptCode = DataTypeHelpers.importTernaryOperate(obj.get(i), 34);//住院科室编码
            strOrderDeptName = DataTypeHelpers.importTernaryOperate(obj.get(i), 35);//住院科室名称
            strOrderDoctorCode = DataTypeHelpers.importTernaryOperate(obj.get(i), 36);//开方医生编码
            strOrderDoctorName = DataTypeHelpers.importTernaryOperate(obj.get(i), 37);//开方医生名称
        } else {
            strDeptCode = DataTypeHelpers.importTernaryOperate(obj.get(i), 19);//科室编码
            strDeptName = DataTypeHelpers.importTernaryOperate(obj.get(i), 20);//科室名称
            strDocCode = DataTypeHelpers.importTernaryOperate(obj.get(i), 21);//医生编码
            strDocName = DataTypeHelpers.importTernaryOperate(obj.get(i), 22);//医生名称
        }
//        Date thisDate = new Date();
        if (!strOrderNumber.equals("")) {
            queryApplyDataList = applyDataList.stream().filter(
                    s -> s.getOrderNumber().equals(strOrderNumber)
            ).collect(Collectors.toList());
            if (queryApplyDataList.size() > 0) {
                if (!strDeptCode.equals("") && !strDeptName.equals("") &&
                        !strDocCode.equals("") && !strDocName.equals("")) {
                    YbReconsiderApplyData entity = queryApplyDataList.get(0);

                    ybReconsiderVerify = new YbReconsiderVerify();
                    ybReconsiderVerify.setId(UUID.randomUUID().toString());
                    ybReconsiderVerify.setApplyDataId(entity.getId());
                    ybReconsiderVerify.setVerifyDeptCode(strDeptCode);
                    ybReconsiderVerify.setVerifyDeptName(strDeptName);
                    ybReconsiderVerify.setVerifyDoctorCode(strDocCode);
                    ybReconsiderVerify.setVerifyDoctorName(strDocName);

//                    ybReconsiderVerify.setOperateDate(thisDate);//'操作日期'
//                    ybReconsiderVerify.setMatchDate(thisDate);//'匹配日期'
//                    ybReconsiderVerify.setMatchPersonId(currentUser.getUserId());//'匹配人代码'
//                    ybReconsiderVerify.setMatchPersonName(currentUser.getUsername());//'匹配人'

                    ybReconsiderVerify.setApplyDateStr(applyDateStr);
                    ybReconsiderVerify.setTypeno(entity.getTypeno());
                    ybReconsiderVerify.setOrderNumber(entity.getOrderNumber());
                    ybReconsiderVerify.setOrderNum(entity.getOrderNum());
                    ybReconsiderVerify.setDataType(entity.getDataType());//扣款类型  0 明细扣款 1 主单扣款

                    if (entity.getDataType() == YbDefaultValue.DATATYPE_0) {
                        List<YbReconsiderInpatientfees> queryRifList = new ArrayList<>();
                        queryRifList = rifList.stream().filter(s -> s.getApplyDataId().equals(entity.getId())).collect(Collectors.toList());
                        if (queryRifList.size() > 0) {
                            strOrderDeptCode = strOrderDeptCode == null || strOrderDeptCode.equals("") ? queryRifList.get(0).getDeptId() : strOrderDeptCode;
                            strOrderDeptName = strOrderDeptName == null || strOrderDeptName.equals("") ? queryRifList.get(0).getDeptName() : strOrderDeptName;
                            strOrderDoctorCode = strOrderDoctorCode == null || strOrderDoctorCode.equals("") ? queryRifList.get(0).getOrderDocId() : strOrderDoctorCode;
                            strOrderDoctorName = strOrderDoctorName == null || strOrderDoctorName.equals("") ? queryRifList.get(0).getOrderDocName() : strOrderDoctorName;
                        }
                    }

                    //住院科室、开单医生
//                    if (!strOrderDeptCode.equals("") && strOrderDeptCode != null)
                    ybReconsiderVerify.setOrderDeptCode(strOrderDeptCode);

//                    if (!strOrderDeptName.equals("") && strOrderDeptName != null)
                    ybReconsiderVerify.setOrderDeptName(strOrderDeptName);

//                    if (!strOrderDoctorCode.equals("") && strOrderDoctorCode != null)
                    ybReconsiderVerify.setOrderDoctorCode(strOrderDoctorCode);

//                    if (!strOrderDoctorName.equals("") && strOrderDoctorName != null)
                    ybReconsiderVerify.setOrderDoctorName(strOrderDoctorName);
                    ybReconsiderVerify.setAreaType(areaType);

                    ybReconsiderVerify.setState(YbDefaultValue.VERIFYSTATE_1);//1 待审核、2已审核、3已发送
                    ybReconsiderVerify.setIsDeletemark(1);
//                    ybReconsiderVerify.setCreateUserId(currentUser.getUserId());
//                    ybReconsiderVerify.setCreateTime(thisDate);
                }
            }
        }
        return ybReconsiderVerify;
    }

    @Log("创建Job")
    @PutMapping("startJob")
    @RequiresPermissions("ybReconsiderVerify:stateUpdate")
    public FebsResponse cStartJob(String applyDateStr, Integer areaType, int[] jobTypeList) {
        int success = 0;
        try {
            String msg = this.iYbReconsiderVerifyService.createEndJobState(applyDateStr, areaType, jobTypeList);
//            ok,"",noApply,noType
            if (msg.equals("ok")) {
                success = 1;
            } else if (msg.equals("")) {
                message = "该" + applyDateStr + "年月已完成复议.";
            } else {
                if (msg.equals("noApply")) {
                    message = "未找到" + applyDateStr + "年月数据.";
                } else if (msg.equals("noType")) {
                    message = "未找到传入的类型.";
                } else if (msg.equals("noReset")) {
                    message = applyDateStr + "年月数据,未设置非常规截止日期.";
                } else if (msg.equals("noResetState")) {
                    message = applyDateStr + "年月数据,未完成数据剔除.";
                }  else {
                    message = msg;
                }
            }
        } catch (Exception e) {
            message = "创建Job失败";
            log.error(message, e);
        }

        ResponseResult rr = new ResponseResult();
        rr.setSuccess(success);
        rr.setMessage(message);
        return new FebsResponse().data(rr);
    }

}