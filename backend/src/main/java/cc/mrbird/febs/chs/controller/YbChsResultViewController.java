package cc.mrbird.febs.chs.controller;

import cc.mrbird.febs.chs.entity.*;
import cc.mrbird.febs.chs.service.*;
import cc.mrbird.febs.com.controller.DataTypeHelpers;
import cc.mrbird.febs.com.entity.ComFile;
import cc.mrbird.febs.com.service.IComFileService;
import cc.mrbird.febs.common.controller.BaseController;
import cc.mrbird.febs.common.domain.FebsResponse;
import cc.mrbird.febs.common.domain.QueryRequest;
import cc.mrbird.febs.common.exception.FebsException;
import cc.mrbird.febs.common.utils.FebsUtil;
import cc.mrbird.febs.export.excel.ExportExcelUtils;
import cc.mrbird.febs.system.domain.User;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletResponse;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @author viki
 * @since 2020-07-30
 */
@Slf4j
@Validated
@RestController
@RequestMapping("ybChsResultView")

public class YbChsResultViewController extends BaseController {

    private String message;
    @Autowired
    public IYbChsResultViewService iYbChsResultViewService;

    @Autowired
    IYbChsApplyService iYbChsApplyService;

    @Autowired
    IYbChsManageService iYbChsManageService;

    @Autowired
    IYbChsJkService iYbChsJkService;

    @Autowired
    IComFileService iComFileService;


    @GetMapping()
    @RequiresPermissions("ybChsResultView:view")
    public Map<String, Object> List(QueryRequest request, YbChsResultView ybChsResultView, String keyField) {
        if (StringUtils.isNotBlank(ybChsResultView.getCurrencyField())) {
            return getDataTable(this.iYbChsResultViewService.findYbChsResultLikeViews(request, ybChsResultView, keyField));
        } else {
            return getDataTable(this.iYbChsResultViewService.findYbChsResultViews(request, ybChsResultView));
        }
    }

    @PostMapping("exportResult")
    @RequiresPermissions("ybChsResultView:view")
    public void exportResult(QueryRequest request, YbChsResultView ybChsResultView, HttpServletResponse response) throws FebsException {
        boolean isError = false;
        try {
            YbChsApply drgApply = this.iYbChsApplyService.findChsApplyByApplyDateStrs(ybChsResultView.getApplyDateStr(), ybChsResultView.getAreaType());
            if (drgApply != null) {
                ybChsResultView.setPid(drgApply.getId());
                List<YbChsResultView> list = this.iYbChsResultViewService.findChsResultLeftDetailViewLists(ybChsResultView);
                list = list.stream().sorted(Comparator.comparing(YbChsResultView::getOrderNum)).collect(Collectors.toList());

                List<YbChsManage> manageList = new ArrayList<>();
                List<YbChsJk> jkList = new ArrayList<>();
                List<YbChsManage> queryManageList = new ArrayList<>();
                List<YbChsJk> queryJkList = new ArrayList<>();
                if (list.size() > 0) {
                    YbChsManage query = new YbChsManage();
                    query.setApplyDateStr(drgApply.getApplyDateStr());
                    query.setAreaType(drgApply.getAreaType());
                    manageList = iYbChsManageService.findChsResultByStateList(query,true);

                    LambdaQueryWrapper<YbChsJk> wrapper = new LambdaQueryWrapper<>();
                    wrapper.eq(YbChsJk::getApplyDateStr, drgApply.getApplyDateStr());
                    wrapper.eq(YbChsJk::getAreaType, drgApply.getAreaType());
                    jkList = iYbChsJkService.list(wrapper);
                }

                List<YbChsApplyDataResultDetaile> exportList = new ArrayList<>();
                for (YbChsResultView item : list) {
                    YbChsApplyDataResultDetaile dataExport = new YbChsApplyDataResultDetaile();
                    dataExport.setOrderNum(item.getOrderNum());//序号
                    dataExport.setAppealEndDateStr(DataTypeHelpers.dateToString(item.getAppealEndDate()));//申诉截止日期
                    dataExport.setPayPlaceType(item.getPayPlaceType());//支付地点类别
                    dataExport.setYdState(item.getYdState());//疑点状态
                    dataExport.setAreaName(item.getAreaName());//医保区划
                    dataExport.setYyjgCode(item.getYyjgCode());//医药机构编码
                    dataExport.setYyjgName(item.getYyjgName());//医药机构名称
                    dataExport.setDeptName(item.getDeptName());//科室
                    dataExport.setDoctorName(item.getDoctorName());//医生
                    dataExport.setMedicalType(item.getMedicalType());//医疗类别
                    dataExport.setZymzNumber(item.getZymzNumber());//住院门诊号
                    dataExport.setInsuredName(item.getInsuredName());//参保人
                    dataExport.setEnterHospitalDateStr(DataTypeHelpers.dateToString(item.getEnterHospitalDate()));//入院日期
                    dataExport.setOutHospitalDateStr(DataTypeHelpers.dateToString(item.getOutHospitalDate()));//出院日期
                    dataExport.setSettlementDateStr(DataTypeHelpers.dateToString(item.getSettlementDate()));//结算日期
                    dataExport.setCardNumber(item.getCardNumber());//身份证号
                    dataExport.setProjectCode(item.getProjectCode());//医保项目编码
                    dataExport.setProjectName(item.getProjectName());//医院项目名称
                    dataExport.setRuleName(item.getRuleName());//规则名称
                    dataExport.setViolateCsPrice(item.getViolateCsPrice());//初审违规金额（元）
                    dataExport.setViolatePrice(item.getViolatePrice());//违规金额（元）
                    dataExport.setViolateReason(item.getViolateReason());//违规内容
                    dataExport.setZdNote(item.getZdNote());//诊断信息
                    dataExport.setCostDateStr(DataTypeHelpers.dateToString(item.getCostDate()));//费用日期
                    dataExport.setInsuredType(item.getInsuredType());//险种类型
                    dataExport.setPrice(item.getPrice());//单价（元）
                    dataExport.setNum(item.getNum());//数量
                    dataExport.setMedicalPrice(item.getMedicalPrice());//金额（元）
                    dataExport.setTcPayPrice(item.getTcPayPrice());//统筹支付（元）
                    dataExport.setSpecs(item.getSpecs());//规格
                    dataExport.setJx(item.getJx());//剂型
                    dataExport.setJgLevel(item.getJgLevel());//机构等级

                    dataExport.setFyly(item.getOperateReason());
                    dataExport.setResultDoctorCode(item.getResultDoctorCode());//复议医生编码
                    dataExport.setResultDoctorName(item.getResultDoctorName());//
                    dataExport.setResultDksId(item.getResultDksId());//复议科室名称
                    dataExport.setResultDksName(item.getResultDksName());//复议科室名称

                    if (item.getResultDoctorCode() == null) {
                        queryManageList = manageList.stream().filter(s -> s.getApplyDataId().equals(item.getApplyDataId())).collect(Collectors.toList());
                        if (queryManageList.size() > 0) {
                            dataExport.setResultDoctorCode(queryManageList.get(0).getReadyDoctorCode());//复议医生编码
                            dataExport.setResultDoctorName(queryManageList.get(0).getReadyDoctorName());//复议医生姓名
                            dataExport.setResultDksId(queryManageList.get(0).getReadyDksId());//复议科室名称
                            dataExport.setResultDksName(queryManageList.get(0).getReadyDksName());//复议科室名称
                        }
                    }

                    queryJkList = jkList.stream().filter(s -> s.getApplyDataId().equals(item.getApplyDataId())).collect(Collectors.toList());
                    if (queryJkList.size() > 0) {
                        YbChsJk jk = queryJkList.get(0);
                        if (jk.getItemTypeCode() != null) {
                            dataExport.setItemTypeCode(jk.getItemTypeCode());
                            dataExport.setItemTypeName(jk.getItemTypeName());
                        }
                        if (jk.getAttendDocId() != null) {
                            dataExport.setAttendDocId(jk.getAttendDocId());
                            dataExport.setAttendDocName(jk.getAttendDocName());
                        }
                        if (jk.getDeptId() != null) {
                            dataExport.setOrderDeptId(jk.getDeptId());
                            dataExport.setOrderDeptName(jk.getDeptName());
                        }
                        if (jk.getOrderDocId() != null) {
                            dataExport.setOrderDocId(jk.getOrderDocId());
                            dataExport.setOrderDocName(jk.getOrderDocName());
                        }
                        if (jk.getExcuteDeptId() != null) {
                            dataExport.setExcuteDeptId(jk.getExcuteDeptId());
                            dataExport.setExcuteDeptName(jk.getExcuteDeptName());
                        }
                        if (jk.getExcuteDeptId() != null) {
                            dataExport.setExcuteDocId(jk.getExcuteDocId());
                            dataExport.setExcuteDocName(jk.getExcuteDocName());
                        }
                        dataExport.setFeeOperatorId(jk.getFeeOperatorId());
                        dataExport.setFeeOperatorName(jk.getFeeOperatorName());
                        dataExport.setFeeDeptId(jk.getFeeDeptId());
                        dataExport.setFeeDeptName(jk.getFeeDeptName());
                    }
                    exportList.add(dataExport);
                }

                ExportExcelUtils.exportExcel(response, YbChsApplyDataResultDetaile.class, exportList, "结果明细数据");

            } else {
                isError = true;
                message = "未找到 " + ybChsResultView.getApplyDateStr() + " 上传数据.";
            }
            if (isError) {
                throw new FebsException(message);
            }
        } catch (Exception e) {
            if (!isError)
                message = "导出Excel失败";

            log.error(message, e);
            throw new FebsException(message);
        }

    }

    @PostMapping("exportResultWj")
    @RequiresPermissions("ybChsResultView:view")
    public void exportResultWj(QueryRequest request, YbChsResultView ybChsResultView, HttpServletResponse response) throws FebsException {
        boolean isError = false;
        try {
            YbChsApply chsApply = this.iYbChsApplyService.findChsApplyByApplyDateStrs(ybChsResultView.getApplyDateStr(), ybChsResultView.getAreaType());
            if (chsApply != null) {
                ybChsResultView.setPid(chsApply.getId());
                List<YbChsApplyDataResult> exportList = new ArrayList<>();
                List<YbChsResultView> list = this.iYbChsResultViewService.findChsResultViewLists(ybChsResultView);
                if(list.size()>0) {
                    list = list.stream().sorted(Comparator.comparing(YbChsResultView::getOrderNum)).collect(Collectors.toList());

                    List<ComFile> fileList = iComFileService.findChsResultComFiles(chsApply.getApplyDateStr(), chsApply.getAreaType());
                    List<ComFile> fileQueryList = new ArrayList<>();
                    for (YbChsResultView item : list) {
                        YbChsApplyDataResult dataExport = new YbChsApplyDataResult();
                        dataExport.setZymzNumber(item.getZymzNumber());//住院门诊号
                        dataExport.setProjectName(item.getProjectName());//医院项目名称
                        dataExport.setViolateCsPriceStr(item.getViolateCsPriceStr());//初审违规金额（元）
                        dataExport.setOutHospitalDateStr(item.getOutHospitalDateStr());//出院日期
                        dataExport.setFyly(item.getOperateReason());
                        fileQueryList = fileList.stream().filter(s->s.getRefTabId().equals(item.getId())).collect(Collectors.toList());
                        if(fileQueryList.size()>0) {
                            dataExport.setWjmc(fileQueryList.get(0).getServerName());
                        }

                        exportList.add(dataExport);
                    }
                }

                ExportExcelUtils.exportExcel(response, YbChsApplyDataResult.class, exportList, "结果文件数据");

            } else {
                isError = true;
                message = "未找到 " + ybChsResultView.getApplyDateStr() + " 上传数据.";
            }
            if (isError) {
                throw new FebsException(message);
            }
        } catch (Exception e) {
            if (!isError)
                message = "导出Excel失败";

            log.error(message, e);
            throw new FebsException(message);
        }

    }

    @GetMapping("fileDownLoadList")
    public FebsResponse fileDownLoadList(String applyDateStr, Integer areaType, Integer startOrderNum, Integer endOrderNum,Integer maxCount) {
        Map<String, Object> result = new HashMap<>();
        try {
            if (startOrderNum > 0 && endOrderNum >= startOrderNum) {
                List<Integer> orderNumList = new ArrayList<>();
                if (startOrderNum == endOrderNum) {
                    orderNumList.add(startOrderNum);
                } else {
                    while (startOrderNum <= endOrderNum) {
                        orderNumList.add(startOrderNum);
                        startOrderNum++;
                    }
                }
                maxCount = maxCount == null ? 10 : maxCount;
                List<YbChsResultView> list = this.iYbChsResultViewService.findChsResultFileSizeByOrderNumList(applyDateStr, areaType, orderNumList,maxCount);

                if (list.size() > 0) {
                    list = list.stream().sorted(Comparator.comparing(YbChsResultView::getOrderNum)).collect(Collectors.toList());
                }

                if (list.size() > 0) {
                    result.put("data", list);
                    result.put("success", 1);
                } else {
                    result.put("data", null);
                    result.put("error", "打包下载，无数据");
                    result.put("success", 0);
                }
            } else {
                result.put("data", null);
                result.put("error", "传入的序号异常.");
                result.put("success", 0);
            }
        } catch (Exception e) {
            result.put("data", null);
            result.put("error", "数据异常.");
            result.put("success", 0);
        }
        return new FebsResponse().data(result);
    }


}