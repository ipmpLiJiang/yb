package cc.mrbird.febs.chs.controller;

import cc.mrbird.febs.chs.entity.YbChsApply;
import cc.mrbird.febs.chs.entity.YbChsApplyDataVerify;
import cc.mrbird.febs.chs.entity.YbChsJk;
import cc.mrbird.febs.chs.entity.YbChsVerifyView;
import cc.mrbird.febs.chs.service.IYbChsApplyService;
import cc.mrbird.febs.chs.service.IYbChsJkService;
import cc.mrbird.febs.chs.service.IYbChsVerifyViewService;
import cc.mrbird.febs.com.controller.DataTypeHelpers;
import cc.mrbird.febs.common.controller.BaseController;
import cc.mrbird.febs.common.domain.QueryRequest;
import cc.mrbird.febs.common.exception.FebsException;
import cc.mrbird.febs.common.properties.FebsProperties;
import cc.mrbird.febs.export.excel.ExportExcelUtils;
import cc.mrbird.febs.yb.entity.YbDefaultValue;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.wuwenze.poi.ExcelKit;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import javax.validation.constraints.NotBlank;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @author viki
 * @since 2020-07-30
 */
@Slf4j
@Validated
@RestController
@RequestMapping("ybChsVerifyView")

public class YbChsVerifyViewController extends BaseController {

    private String message;
    @Autowired
    public IYbChsVerifyViewService iYbChsVerifyViewService;

    @Autowired
    private FebsProperties febsProperties;

    @Autowired
    public IYbChsApplyService iYbChsApplyService;

    @Autowired
    public IYbChsJkService iYbChsJkService;

    /**
     * 分页查询数据
     *
     * @param request         分页信息
     * @param ybChsVerifyView 查询条件
     * @return
     */
    @GetMapping
    @RequiresPermissions("ybChsVerifyView:view")
    public Map<String, Object> List(QueryRequest request, YbChsVerifyView ybChsVerifyView) {
        return getDataTable(this.iYbChsVerifyViewService.findYbChsVerifyViews(request, ybChsVerifyView));

    }

    @PostMapping("excel")
    @RequiresPermissions("ybChsVerifyView:export")
    public void export(QueryRequest request, YbChsVerifyView ybChsVerifyView, HttpServletResponse response) throws FebsException {
        try {
            List<YbChsVerifyView> ybChsVerifyViews = this.iYbChsVerifyViewService.findYbChsVerifyViews(request, ybChsVerifyView).getRecords();
            ExcelKit.$Export(YbChsVerifyView.class, response).downXlsx(ybChsVerifyViews, false);
        } catch (Exception e) {
            message = "导出Excel失败";
            log.error(message, e);
            throw new FebsException(message);
        }
    }

    @PostMapping("exportVerify")
    @RequiresPermissions("ybChsVerifyView:nullview")
    public void exportVerify(QueryRequest request, YbChsVerifyView ybChsVerifyView, HttpServletResponse response) throws FebsException {
        boolean isError = false;
        try {
            YbChsApply chsApply = this.iYbChsApplyService.findChsApplyByApplyDateStrs(ybChsVerifyView.getApplyDateStr(), ybChsVerifyView.getAreaType());
            if (chsApply != null) {
                ybChsVerifyView.setPid(chsApply.getId());
                ybChsVerifyView.setAreaType(ybChsVerifyView.getAreaType());
                List<YbChsVerifyView> list = this.iYbChsVerifyViewService.findChsVerifyViewLists(ybChsVerifyView);
                list = list.stream().sorted(Comparator.comparing(YbChsVerifyView::getOrderNum)).collect(Collectors.toList());

                List<YbChsJk> jkList = new ArrayList<>();

                if (list.size() > 0) {
                    LambdaQueryWrapper<YbChsJk> wrapper = new LambdaQueryWrapper<>();
                    wrapper.eq(YbChsJk::getApplyDateStr, chsApply.getApplyDateStr());
                    wrapper.eq(YbChsJk::getAreaType, chsApply.getAreaType());
                    jkList = iYbChsJkService.list(wrapper);
                }
                List<YbChsApplyDataVerify> exportList = new ArrayList<>();
                List<YbChsJk> queryJkList = new ArrayList<>();
                SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
                for (YbChsVerifyView item : list) {
                    YbChsApplyDataVerify dataExport = new YbChsApplyDataVerify();
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

                    String strVerifyDksName = DataTypeHelpers.stringReplaceSetString(item.getVerifyDksName(), item.getVerifyDksId() + "-");
                    String strVerifyDoctorName = DataTypeHelpers.stringReplaceSetString(item.getVerifyDoctorName(), item.getVerifyDoctorCode() + "-");

                    dataExport.setVerifyDoctorCode(item.getVerifyDoctorCode());//复议医生编码
                    dataExport.setVerifyDoctorName(strVerifyDoctorName);//复议医生姓名
                    dataExport.setVerifyDksId(item.getVerifyDksId());//科室名称
                    dataExport.setVerifyDksName(strVerifyDksName);//科室名称

                    queryJkList = jkList.stream().filter(s -> s.getApplyDataId().equals(item.getApplyDataId())).collect(Collectors.toList());
                    if (queryJkList.size() > 0) {
                        YbChsJk jk = queryJkList.get(0);
//                        if(jk.getItemTypeCode() !=null) {
//                            dataExport.setItemTypeCode(jk.getItemTypeCode());
//                            dataExport.setItemTypeName(jk.getItemTypeName());
//                        }
                        if(jk.getAttendDocId() !=null) {
                            dataExport.setAttendDocId(jk.getAttendDocId());
                            dataExport.setAttendDocName(jk.getAttendDocName());
                        }
                        if(jk.getDeptId() !=null) {
                            dataExport.setOrderDeptId(jk.getDeptId());
                            dataExport.setOrderDeptName(jk.getDeptName());
                        }
                        if(jk.getOrderDocId() !=null) {
                            dataExport.setOrderDocId(jk.getOrderDocId());
                            dataExport.setOrderDocName(jk.getOrderDocName());
                        }
                        if(jk.getExcuteDeptId() !=null) {
                            dataExport.setExcuteDeptId(jk.getExcuteDeptId());
                            dataExport.setExcuteDeptName(jk.getExcuteDeptName());
                        }
                        if(jk.getExcuteDeptId() !=null) {
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
                ExportExcelUtils.exportExcel(response, YbChsApplyDataVerify.class, exportList, "CHS核对明细数据");

            } else {
                isError = true;
                message = "未找到 " + ybChsVerifyView.getApplyDateStr() + " 上传数据.";
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


    @GetMapping("/{id}")
    public YbChsVerifyView detail(@NotBlank(message = "{required}") @PathVariable String id) {
        YbChsVerifyView ybChsVerifyView = this.iYbChsVerifyViewService.getById(id);
        return ybChsVerifyView;
    }
}