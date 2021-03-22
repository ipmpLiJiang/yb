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
import cc.mrbird.febs.export.excel.ExportExcelUtils;
import cc.mrbird.febs.system.domain.User;
import cc.mrbird.febs.yb.domain.ResponseImportResultData;
import cc.mrbird.febs.yb.entity.*;
import cc.mrbird.febs.yb.service.IYbReconsiderApplyService;
import cc.mrbird.febs.yb.service.IYbReconsiderInpatientfeesService;
import cc.mrbird.febs.yb.service.IYbReconsiderVerifyViewService;
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
import java.util.stream.Collectors;

/**
 * @author viki
 * @since 2020-07-30
 */
@Slf4j
@Validated
@RestController
@RequestMapping("ybReconsiderVerifyView")

public class YbReconsiderVerifyViewController extends BaseController {

    private String message;
    @Autowired
    public IYbReconsiderVerifyViewService iYbReconsiderVerifyViewService;

    @Autowired
    private FebsProperties febsProperties;

    @Autowired
    public IYbReconsiderApplyService iYbReconsiderApplyService;

    @Autowired
    private IYbReconsiderInpatientfeesService iYbReconsiderInpatientfeesService;


    /**
     * 分页查询数据
     *
     * @param request                分页信息
     * @param ybReconsiderVerifyView 查询条件
     * @return
     */
    @GetMapping
    @RequiresPermissions("ybReconsiderVerifyView:view")
    public Map<String, Object> List(QueryRequest request, YbReconsiderVerifyView ybReconsiderVerifyView, String[] searchType) {
        return getDataTable(this.iYbReconsiderVerifyViewService.findYbReconsiderVerifyViews(request, ybReconsiderVerifyView, searchType));
    }

    @GetMapping("findVerifyViewNull")
    @RequiresPermissions("ybReconsiderVerifyView:nullview")
    public Map<String, Object> findVerifyViewNull(QueryRequest request, YbReconsiderVerifyView ybReconsiderVerifyView, String[] searchType) {
        return getDataTable(this.iYbReconsiderVerifyViewService.findYbReconsiderVerifyViewNulls(request, ybReconsiderVerifyView, searchType));
    }

    /**
     * 添加
     *
     * @param ybReconsiderVerifyView
     * @return
     */
    @Log("新增/按钮")
    @PostMapping
    @RequiresPermissions("ybReconsiderVerifyView:add")
    public void addYbReconsiderVerifyView(@Valid YbReconsiderVerifyView ybReconsiderVerifyView) throws FebsException {
        try {
            User currentUser = FebsUtil.getCurrentUser();
//            ybReconsiderVerifyView.setCreateUserId(currentUser.getUserId());
            this.iYbReconsiderVerifyViewService.createYbReconsiderVerifyView(ybReconsiderVerifyView);
        } catch (Exception e) {
            message = "新增/按钮失败";
            log.error(message, e);
            throw new FebsException(message);
        }
    }

    /**
     * 修改
     *
     * @param ybReconsiderVerifyView
     * @return
     */
    @Log("修改")
    @PutMapping
    @RequiresPermissions("ybReconsiderVerifyView:update")
    public void updateYbReconsiderVerifyView(@Valid YbReconsiderVerifyView ybReconsiderVerifyView) throws FebsException {
        try {
            User currentUser = FebsUtil.getCurrentUser();
//            ybReconsiderVerifyView.setModifyUserId(currentUser.getUserId());
            this.iYbReconsiderVerifyViewService.updateYbReconsiderVerifyView(ybReconsiderVerifyView);
        } catch (Exception e) {
            message = "修改失败";
            log.error(message, e);
            throw new FebsException(message);
        }
    }


    @Log("删除")
    @DeleteMapping("/{ids}")
    @RequiresPermissions("ybReconsiderVerifyView:delete")
    public void deleteYbReconsiderVerifyViews(@NotBlank(message = "{required}") @PathVariable String ids) throws FebsException {
        try {
            String[] arr_ids = ids.split(StringPool.COMMA);
            this.iYbReconsiderVerifyViewService.deleteYbReconsiderVerifyViews(arr_ids);
        } catch (Exception e) {
            message = "删除失败";
            log.error(message, e);
            throw new FebsException(message);
        }
    }

    @PostMapping("excel")
    @RequiresPermissions("ybReconsiderVerifyView:export")
    public void export(QueryRequest request, YbReconsiderVerifyView ybReconsiderVerifyView, HttpServletResponse response) throws FebsException {
        try {
            List<YbReconsiderVerifyView> ybReconsiderVerifyViews = this.iYbReconsiderVerifyViewService.findYbReconsiderVerifyViews(request, ybReconsiderVerifyView).getRecords();
            ExcelKit.$Export(YbReconsiderVerifyView.class, response).downXlsx(ybReconsiderVerifyViews, false);
        } catch (Exception e) {
            message = "导出Excel失败";
            log.error(message, e);
            throw new FebsException(message);
        }
    }

    @PostMapping("exportVerify")
    @RequiresPermissions("ybReconsiderVerifyView:nullview")
    public void exportVerify(QueryRequest request, YbReconsiderVerifyView ybReconsiderVerifyView, HttpServletResponse response) throws FebsException {
        boolean isError = false;
        try {
            YbReconsiderApply reconsiderApply = this.iYbReconsiderApplyService.findReconsiderApplyByApplyDateStrs(ybReconsiderVerifyView.getApplyDateStr(),ybReconsiderVerifyView.getAreaType());
            if (reconsiderApply != null) {
                int state = reconsiderApply.getState();
                int typeno = state == YbDefaultValue.APPLYSTATE_2 || state == YbDefaultValue.APPLYSTATE_3 ? 1 :
                        state == YbDefaultValue.APPLYSTATE_4 || state == YbDefaultValue.APPLYSTATE_5 ? 2 : 0;
                if (typeno != 0) {
                    ybReconsiderVerifyView.setTypeno(typeno);
                    ybReconsiderVerifyView.setPid(reconsiderApply.getId());
                    ybReconsiderVerifyView.setAreaType(ybReconsiderVerifyView.getAreaType());
                    List<YbReconsiderVerifyView> list = this.iYbReconsiderVerifyViewService.findReconsiderVerifyViewLists(ybReconsiderVerifyView);
                    list = list.stream().sorted(Comparator.comparing(YbReconsiderVerifyView::getOrderNum)).collect(Collectors.toList());
                    if (ybReconsiderVerifyView.getDataType().equals(YbDefaultValue.DATATYPE_0)) {
                        YbReconsiderInpatientfees queryRif = new YbReconsiderInpatientfees();
                        queryRif.setApplyDateStr(ybReconsiderVerifyView.getApplyDateStr());
                        queryRif.setDataType(YbDefaultValue.DATATYPE_0);
                        queryRif.setAreaType(ybReconsiderVerifyView.getAreaType());
                        queryRif.setTypeno(typeno);
                        List<YbReconsiderInpatientfees> rifList = this.iYbReconsiderInpatientfeesService.findReconsiderInpatientfeesList(queryRif);
                        List<YbReconsiderInpatientfees> queryRifList = new ArrayList<>();

                        List<YbReconsiderApplyDataVerify> exportList = new ArrayList<>();
                        for (YbReconsiderVerifyView item : list) {
                            YbReconsiderApplyDataVerify dataExport = new YbReconsiderApplyDataVerify();
                            dataExport.setOrderNumber(item.getOrderNumber());//序号
                            dataExport.setSerialNo(item.getSerialNo());//交易流水号
                            dataExport.setBillNo(item.getBillNo());//单据号
                            dataExport.setProposalCode(item.getProposalCode());//意见书编码
                            dataExport.setProjectCode(item.getProjectCode());//项目编码
                            dataExport.setProjectName(item.getProjectName());//项目名称
                            dataExport.setNum(item.getNum());//数量
                            dataExport.setMedicalPrice(item.getMedicalPrice());//医保内金额
                            dataExport.setRuleName(item.getRuleName());//规则名称
                            dataExport.setDeductPrice(item.getDeductPrice());//扣除金额
                            dataExport.setDeductReason(item.getDeductReason());// 扣除原因
                            dataExport.setRepaymentReason(item.getRepaymentReason());//还款原因
                            dataExport.setDoctorName(item.getDoctorName());//医生姓名
                            dataExport.setDeptCode(item.getDeptCode());//科室编码
                            dataExport.setDeptName(item.getDeptName());//科室名称
                            dataExport.setEnterHospitalDateStr(item.getEnterHospitalDateStr());//入院日期
                            dataExport.setOutHospitalDateStr(item.getOutHospitalDateStr());//出院日期
                            dataExport.setCostDateStr(item.getCostDateStr());//费用日期
                            dataExport.setHospitalizedNo(item.getHospitalizedNo());//住院号
                            dataExport.setTreatmentMode(item.getTreatmentMode());//就医方式
                            dataExport.setSettlementDateStr(item.getSettlementDateStr());//结算日期
                            dataExport.setPersonalNo(item.getPersonalNo());//个人编号
                            dataExport.setInsuredName(item.getInsuredName());//参保人姓名
                            dataExport.setCardNumber(item.getCardNumber());//医保卡号
                            dataExport.setAreaName(item.getAreaName());//统筹区名称
                            dataExport.setVersionNumber(item.getVersionNumber());//版本号
                            dataExport.setBackAppeal(item.getBackAppeal());//反馈申诉

                            String strVerifyDeptName = DataTypeHelpers.stringReplaceSetString(item.getVerifyDeptName(), item.getVerifyDeptCode() + "-");
                            String strVerifyDoctorName = DataTypeHelpers.stringReplaceSetString(item.getVerifyDoctorName(), item.getVerifyDoctorCode() + "-");

                            dataExport.setVerifyDoctorCode(item.getVerifyDoctorCode());//复议医生编码
                            dataExport.setVerifyDoctorName(strVerifyDoctorName);//复议医生姓名
                            dataExport.setVerifyDeptCode(item.getVerifyDeptCode());//复议科室编码
                            dataExport.setVerifyDeptName(strVerifyDeptName);//复议科室名称

                            queryRifList = rifList.stream().filter(s -> s.getApplyDataId().equals(item.getApplyDataId())).collect(Collectors.toList());
                            if (queryRifList.size() > 0) {
                                dataExport.setItemTypeCode(queryRifList.get(0).getItemTypeCode());
                                dataExport.setItemTypeName(queryRifList.get(0).getItemTypeName());
                                dataExport.setAttendDocId(queryRifList.get(0).getAttendDocId());
                                dataExport.setAttendDocName(queryRifList.get(0).getAttendDocName());
                                dataExport.setOrderDeptId(queryRifList.get(0).getDeptId());
                                dataExport.setOrderDeptName(queryRifList.get(0).getDeptName());
                                dataExport.setOrderDocId(queryRifList.get(0).getOrderDocId());
                                dataExport.setOrderDocName(queryRifList.get(0).getOrderDocName());
                                dataExport.setExcuteDeptId(queryRifList.get(0).getExcuteDeptId());
                                dataExport.setExcuteDeptName(queryRifList.get(0).getExcuteDeptName());
                                dataExport.setExcuteDocId(queryRifList.get(0).getExcuteDocId());
                                dataExport.setExcuteDocName(queryRifList.get(0).getExcuteDocName());
                                dataExport.setFeeOperatorId(queryRifList.get(0).getFeeOperatorId());
                                dataExport.setFeeOperatorName(queryRifList.get(0).getFeeOperatorName());
                                dataExport.setFeeDeptId(queryRifList.get(0).getFeeDeptId());
                                dataExport.setFeeDeptName(queryRifList.get(0).getFeeDeptName());
                            }
                            if (item.getOrderDeptCode() != null && !item.getOrderDeptCode().equals(""))
                                dataExport.setOrderDeptId(item.getOrderDeptCode());

                            if (item.getOrderDeptName() != null && !item.getOrderDeptName().equals(""))
                                dataExport.setOrderDeptName(item.getOrderDeptName());

                            if (item.getOrderDoctorCode() != null && !item.getOrderDoctorCode().equals(""))
                                dataExport.setOrderDocId(item.getOrderDoctorCode());

                            if (item.getOrderDoctorName() != null && !item.getOrderDoctorName().equals(""))
                                dataExport.setOrderDocName(item.getOrderDoctorName());

                            exportList.add(dataExport);
                        }
                        if (exportList.size() == 0) {
                            isError = true;
                            message = "未找到明细扣款数据";
                        } else {
                            ExportExcelUtils.exportExcel(response, YbReconsiderApplyDataVerify.class, exportList, "明细扣款");
                        }
                    } else {
                        List<YbReconsiderApplyMainVerify> exportList = new ArrayList<>();
                        for (YbReconsiderVerifyView item : list) {
                            YbReconsiderApplyMainVerify dataExport = new YbReconsiderApplyMainVerify();
                            dataExport.setOrderNumber(item.getOrderNumber());//序号
                            dataExport.setSerialNo(item.getSerialNo());//交易流水号
                            dataExport.setBillNo(item.getBillNo());//单据号
                            dataExport.setProposalCode(item.getProposalCode());//意见书编码
                            dataExport.setMedicalPrice(item.getMedicalPrice());//医保内金额
                            dataExport.setRuleName(item.getRuleName());//规则名称
                            dataExport.setDeductPrice(item.getDeductPrice());//扣除金额
                            dataExport.setEnterHospitalDateStr(item.getEnterHospitalDateStr());//入院日期
                            dataExport.setOutHospitalDateStr(item.getOutHospitalDateStr());//出院日期
                            dataExport.setHospitalizedNo(item.getHospitalizedNo());//住院号
                            dataExport.setTreatmentMode(item.getTreatmentMode());//就医方式
                            dataExport.setSettlementDateStr(item.getSettlementDateStr());//结算日期
                            dataExport.setPersonalNo(item.getPersonalNo());//个人编号
                            dataExport.setInsuredName(item.getInsuredName());//参保人姓名
                            dataExport.setInsuredType(item.getInsuredType());//参保类型
                            dataExport.setAreaName(item.getAreaName());//统筹区名称
                            dataExport.setVersionNumber(item.getVersionNumber());//版本号
                            dataExport.setBackAppeal(item.getBackAppeal());//反馈申诉
                            dataExport.setVerifyDoctorCode(item.getVerifyDoctorCode());//复议医生编码
                            dataExport.setVerifyDoctorName(item.getVerifyDoctorName());//复议医生姓名
                            dataExport.setVerifyDeptCode(item.getVerifyDeptCode());//复议科室编码
                            dataExport.setVerifyDeptName(item.getVerifyDeptName());//复议科室名称
                            exportList.add(dataExport);
                        }
                        if (exportList.size() == 0) {
                            isError = true;
                            message = "未找到主单扣款数据";
                        } else {
                            ExportExcelUtils.exportExcel(response, YbReconsiderApplyMainVerify.class, exportList, "主单扣款");
                        }
                    }

                } else {
                    isError = true;
                    message = ybReconsiderVerifyView.getApplyDateStr() + " 当前状态无法进行下载";
                }
            } else {
                isError = true;
                message = "未找到 " + ybReconsiderVerifyView.getApplyDateStr() + " 上传数据.";
            }
            if(isError){
                throw new FebsException(message);
            }
        } catch (Exception e) {
            if(!isError)
                message = "导出Excel失败";

            log.error(message, e);
            throw new FebsException(message);
        }

    }


    @GetMapping("/{id}")
    public YbReconsiderVerifyView detail(@NotBlank(message = "{required}") @PathVariable String id) {
        YbReconsiderVerifyView ybReconsiderVerifyView = this.iYbReconsiderVerifyViewService.getById(id);
        return ybReconsiderVerifyView;
    }
}