package cc.mrbird.febs.yb.controller;

import cc.mrbird.febs.com.controller.DataTypeHelpers;
import cc.mrbird.febs.common.annotation.Log;
import cc.mrbird.febs.common.controller.BaseController;
import cc.mrbird.febs.common.domain.router.VueRouter;
import cc.mrbird.febs.common.exception.FebsException;
import cc.mrbird.febs.common.domain.QueryRequest;

import cc.mrbird.febs.common.properties.FebsProperties;
import cc.mrbird.febs.yb.entity.*;
import cc.mrbird.febs.yb.service.IYbAppealResultReportViewService;

import cc.mrbird.febs.common.utils.FebsUtil;
import cc.mrbird.febs.system.domain.User;
import cn.hutool.core.io.IoUtil;
import cn.hutool.poi.excel.ExcelUtil;
import cn.hutool.poi.excel.ExcelWriter;
import cn.hutool.poi.excel.StyleSet;
import com.baomidou.mybatisplus.core.toolkit.StringPool;
import com.wuwenze.poi.ExcelKit;
import com.wuwenze.poi.factory.ExcelMappingFactory;
import com.wuwenze.poi.pojo.ExcelMapping;
import com.wuwenze.poi.pojo.ExcelProperty;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Font;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @author viki
 * @since 2020-09-24
 */
@Slf4j
@Validated
@RestController
@RequestMapping("ybAppealResultReportView")

public class YbAppealResultReportViewController extends BaseController {

    private String message;
    @Autowired
    IYbAppealResultReportViewService iYbAppealResultReportViewService;

    @Autowired
    FebsProperties febsProperties;


    /**
     * 分页查询数据
     *
     * @param request                  分页信息
     * @param ybAppealResultReportView 查询条件
     * @return
     */
    @GetMapping
    @RequiresPermissions("ybAppealResultReportView:view")
    public Map<String, Object> List(QueryRequest request, YbAppealResultReportView ybAppealResultReportView, String keyField) {
        String val = ybAppealResultReportView.getCurrencyField();
        if (val!= null && !val.equals("")) {
            System.out.println("View-New");
            return getDataTable(this.iYbAppealResultReportViewService.findAppealResultReportViewNew(request, ybAppealResultReportView, keyField,null));
        } else {
            System.out.println("View-Old");
            return getDataTable(this.iYbAppealResultReportViewService.findAppealResultReportViews(request, ybAppealResultReportView, keyField, false));
        }
    }

    @GetMapping("findAppealResultReportConfView")
    @RequiresPermissions("ybAppealResultReportView:userView")
    public Map<String, Object> ListConf(QueryRequest request, YbAppealResultReportView ybAppealResultReportView, String keyField) {
        User currentUser = FebsUtil.getCurrentUser();
        return getDataTable(this.iYbAppealResultReportViewService.findAppealResultReportViewNew(request, ybAppealResultReportView, keyField,currentUser.getUsername()));
    }

    @GetMapping("findAppealResultReportUserView")
    @RequiresPermissions("ybAppealResultReportView:userView")
    public Map<String, Object> userList(QueryRequest request, YbAppealResultReportView ybAppealResultReportView, String keyField) {
        User currentUser = FebsUtil.getCurrentUser();
        ybAppealResultReportView.setArDoctorCode(currentUser.getUsername());
//        return getDataTable(this.iYbAppealResultReportViewService.findAppealResultReportViews(request, ybAppealResultReportView,keyField,true));
        if (ybAppealResultReportView.getApplyDateFrom().equals(ybAppealResultReportView.getApplyDateTo())) {
            ybAppealResultReportView.setApplyDateStr(ybAppealResultReportView.getApplyDateFrom());
            return getDataTable(this.iYbAppealResultReportViewService.findAppealResultReportViewNew(request, ybAppealResultReportView, keyField,null));
        } else {
            return getDataTable(this.iYbAppealResultReportViewService.findAppealResultReportViewUserNew(request, ybAppealResultReportView, keyField));
        }
    }

    /**
     * 添加
     *
     * @param ybAppealResultReportView
     * @return
     */
    @Log("新增/按钮")
    @PostMapping
    @RequiresPermissions("ybAppealResultReportView:add")
    public void addYbAppealResultReportView(@Valid YbAppealResultReportView ybAppealResultReportView) throws FebsException {
        try {
            User currentUser = FebsUtil.getCurrentUser();
            this.iYbAppealResultReportViewService.createYbAppealResultReportView(ybAppealResultReportView);
        } catch (Exception e) {
            message = "新增/按钮失败";
            log.error(message, e);
            throw new FebsException(message);
        }
    }

    /**
     * 修改
     *
     * @param ybAppealResultReportView
     * @return
     */
    @Log("修改")
    @PutMapping
    @RequiresPermissions("ybAppealResultReportView:update")
    public void updateYbAppealResultReportView(@Valid YbAppealResultReportView ybAppealResultReportView) throws FebsException {
        try {
            User currentUser = FebsUtil.getCurrentUser();
            this.iYbAppealResultReportViewService.updateYbAppealResultReportView(ybAppealResultReportView);
        } catch (Exception e) {
            message = "修改失败";
            log.error(message, e);
            throw new FebsException(message);
        }
    }


    @Log("删除")
    @DeleteMapping("/{ids}")
    @RequiresPermissions("ybAppealResultReportView:delete")
    public void deleteYbAppealResultReportViews(@NotBlank(message = "{required}") @PathVariable String ids) throws FebsException {
        try {
            String[] arr_ids = ids.split(StringPool.COMMA);
            this.iYbAppealResultReportViewService.deleteYbAppealResultReportViews(arr_ids);
        } catch (Exception e) {
            message = "删除失败";
            log.error(message, e);
            throw new FebsException(message);
        }
    }

    @PostMapping("excel")
    @RequiresPermissions("ybAppealResultReportView:export")
    public void export(QueryRequest request, YbAppealResultReportView ybAppealResultReportView, HttpServletResponse response) throws FebsException {
        try {
            List<YbAppealResultReportView> ybAppealResultReportViews = this.iYbAppealResultReportViewService.findYbAppealResultReportViews(request, ybAppealResultReportView).getRecords();
            ExcelKit.$Export(YbAppealResultReportView.class, response).downXlsx(ybAppealResultReportViews, false);
        } catch (Exception e) {
            message = "导出Excel失败";
            log.error(message, e);
            throw new FebsException(message);
        }
    }

    @GetMapping("/{id}")
    public YbAppealResultReportView detail(@NotBlank(message = "{required}") @PathVariable String id) {
        YbAppealResultReportView ybAppealResultReportView = this.iYbAppealResultReportViewService.getById(id);
        return ybAppealResultReportView;
    }

    @PostMapping("reportExcel")
    @RequiresPermissions("ybAppealResultReportView:exportData")
    public void reportExcel(QueryRequest request, YbAppealResultReportView ybAppealResultReportView, HttpServletResponse response, String keyField) throws FebsException {
        try {
            List<YbAppealResultReportView> appealResultReportList = this.iYbAppealResultReportViewService.findYbAppealResultReportLists(ybAppealResultReportView, keyField, false);

            if (appealResultReportList.size() > 0) {
                List<YbAppealResultReportView> appealResultReportDataList = new ArrayList<>();
                List<YbAppealResultReportView> appealResultReportMainList = new ArrayList<>();
                appealResultReportDataList = appealResultReportList.stream().filter(
                        s -> s.getDataType().equals(YbDefaultValue.DATATYPE_0)).sorted(Comparator.comparing(YbAppealResultReportView::getTypeno).thenComparing(YbAppealResultReportView::getOrderNum)
                ).collect(Collectors.toList());
//                if (appealResultReportDataList.size() > 0) {
//                    Collections.sort(appealResultReportDataList);
//                }
                appealResultReportMainList = appealResultReportList.stream().filter(
                        s -> s.getDataType().equals(YbDefaultValue.DATATYPE_1)).sorted(Comparator.comparing(YbAppealResultReportView::getTypeno).thenComparing(YbAppealResultReportView::getOrderNum)
                ).collect(Collectors.toList());
//                if (appealResultReportMainList.size() > 0) {
//                    Collections.sort(appealResultReportMainList);
//                }
                List<YbAppealResultDataReportExport> dataList = new ArrayList<>();
                List<YbAppealResultMainReportExport> mainList = new ArrayList<>();

                List<YbAppealResultDataReportExport> dataSearchList = new ArrayList<>();
                List<YbAppealResultMainReportExport> mainSearchList = new ArrayList<>();

                SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
                String dateString = "";

                List<String> mxApplyDateStrList = new ArrayList<>();
                List<String> zdApplyDateStrList = new ArrayList<>();
                //明细扣款
                for (YbAppealResultReportView item : appealResultReportDataList) {
                    YbAppealResultDataReportExport are = new YbAppealResultDataReportExport();

                    String appDateStr = item.getApplyDateStr();
                    appDateStr = DataTypeHelpers.stringDate7Chang6(appDateStr);
                    item.setApplyDateStr(appDateStr);

                    if (!mxApplyDateStrList.stream().anyMatch(task -> task.equals(item.getApplyDateStr()))) {
                        mxApplyDateStrList.add(item.getApplyDateStr());
                    }

                    are.setApplyDateStr(item.getApplyDateStr());
                    //序号
                    are.setOrderNumber(item.getOrderNumber());
                    //交易流水号
                    are.setSerialNo(item.getSerialNo());
                    //单据号
                    are.setBillNo(item.getBillNo());
                    //意见书编码
                    are.setProposalCode(item.getProposalCode());
                    //项目编码
                    are.setProjectCode(item.getProjectCode());
                    //项目名称
                    are.setProjectName(item.getProjectName());
                    //数量
                    are.setNum(item.getNum());
                    //医保内金额
                    are.setMedicalPrice(item.getMedicalPrice());
                    //规则名称
                    are.setRuleName(item.getRuleName());
                    //扣除金额
                    are.setDeductPrice(item.getDeductPrice());
                    //扣除原因
                    are.setDeductReason(item.getDeductReason());
                    //还款原因
                    are.setRepaymentReason(item.getRepaymentReason());
                    //医生姓名
                    are.setDoctorName(item.getDoctorName());
                    //科室编码
                    are.setDeptCode(item.getDeptCode());
                    //科室名称
                    are.setDeptName(item.getDeptName());
                    //入院日期
//                    if (item.getEnterHospitalDate() != null) {
//                        dateString = formatter.format(item.getEnterHospitalDate());
//                    }
                    are.setEnterHospitalDateStr(item.getEnterHospitalDateStr());

                    //出院日期
                    dateString = "";
//                    if (item.getOutHospitalDate() != null) {
//                        dateString = formatter.format(item.getOutHospitalDate());
//                    }
                    are.setOutHospitalDateStr(item.getOutHospitalDateStr());
                    //费用日期
                    dateString = "";
//                    if (item.getCostDate() != null) {
//                        dateString = formatter.format(item.getCostDate());
//                    }
                    are.setCostDateStr(item.getCostDateStr());
                    //住院号
                    are.setHospitalizedNo(item.getHospitalizedNo());
                    //就医方式
                    are.setTreatmentMode(item.getTreatmentMode());
                    //结算日期
                    dateString = "";
//                    if (item.getSettlementDate() != null) {
//                        dateString = formatter.format(item.getSettlementDate());
//                    }
                    are.setSettlementDateStr(item.getSettlementDateStr());
                    //个人编号
                    are.setPersonalNo(item.getPersonalNo());
                    //参保人姓名
                    are.setInsuredName(item.getInsuredName());
                    //医保卡号
                    are.setCardNumber(item.getCardNumber());
                    //统筹区名称
                    are.setAreaName(item.getAreaName());
                    //版本号
                    are.setVersionNumber(item.getVersionNumber());
                    //反馈申诉
                    are.setBackAppeal(item.getOperateReason());

                    are.setArDoctorCode(item.getArDoctorCode());
                    are.setArDoctorName(item.getArDoctorName());
                    are.setArDeptCode(item.getArDeptCode());
                    are.setArDeptName(item.getArDeptName());
                    dataList.add(are);

                }
                //主单扣款
                for (YbAppealResultReportView item : appealResultReportMainList) {
                    YbAppealResultMainReportExport are = new YbAppealResultMainReportExport();

                    String appDateStr = item.getApplyDateStr();
                    appDateStr = DataTypeHelpers.stringDate7Chang6(appDateStr);
                    item.setApplyDateStr(appDateStr);

                    if (!zdApplyDateStrList.stream().anyMatch(task -> task.equals(item.getApplyDateStr()))) {
                        zdApplyDateStrList.add(item.getApplyDateStr());
                    }

                    are.setApplyDateStr(item.getApplyDateStr());
                    //序号
                    are.setOrderNumber(item.getOrderNumber());
                    //交易流水号
                    are.setSerialNo(item.getSerialNo());
                    //单据号
                    are.setBillNo(item.getBillNo());
                    //意见书编码
                    are.setProposalCode(item.getProposalCode());

                    //医保内金额
                    are.setMedicalPrice(item.getMedicalPrice());
                    //规则名称
                    are.setRuleName(item.getRuleName());
                    //扣除金额
                    are.setDeductPrice(item.getDeductPrice());

                    //入院日期
//                    if (item.getEnterHospitalDate() != null) {
//                        dateString = formatter.format(item.getEnterHospitalDate());
//                    }
                    are.setEnterHospitalDateStr(item.getEnterHospitalDateStr());

                    //出院日期
                    dateString = "";
//                    if (item.getOutHospitalDate() != null) {
//                        dateString = formatter.format(item.getOutHospitalDate());
//                    }
                    are.setOutHospitalDateStr(item.getOutHospitalDateStr());

                    //住院号
                    are.setHospitalizedNo(item.getHospitalizedNo());
                    //就医方式
                    are.setTreatmentMode(item.getTreatmentMode());
                    //结算日期
                    dateString = "";
//                    if (item.getSettlementDate() != null) {
//                        dateString = formatter.format(item.getSettlementDate());
//                    }
                    are.setSettlementDateStr(item.getSettlementDateStr());

                    //个人编号
                    are.setPersonalNo(item.getPersonalNo());
                    //参保人姓名
                    are.setInsuredName(item.getInsuredName());
                    //参保类型
                    are.setInsuredType(item.getInsuredType());
                    //统筹区名称
                    are.setAreaName(item.getAreaName());
                    //版本号
                    are.setVersionNumber(item.getVersionNumber());
                    //反馈申诉
                    are.setBackAppeal(item.getOperateReason());

                    are.setArDoctorCode(item.getArDoctorCode());
                    are.setArDoctorName(item.getArDoctorName());
                    are.setArDeptCode(item.getArDeptCode());
                    are.setArDeptName(item.getArDeptName());
                    mainList.add(are);

                }

                if (mxApplyDateStrList.size() > 0) {
                    mxApplyDateStrList = mxApplyDateStrList.stream().sorted(Comparator.naturalOrder()).collect(Collectors.toList());
                }
                if (zdApplyDateStrList.size() > 0) {
                    zdApplyDateStrList = zdApplyDateStrList.stream().sorted(Comparator.naturalOrder()).collect(Collectors.toList());
                }

                String sheetName1 = "明细扣款";
                String sheetName2 = "主单扣款";

                ExcelMapping excelMappingData = ExcelMappingFactory.get(YbAppealResultDataReportExport.class);
                Map<String, Integer> sheetColumnCountMap = new LinkedHashMap<>();
                sheetColumnCountMap.put(sheetName1, excelMappingData.getPropertyList().size());

                ExcelWriter writer = ExcelUtil.getWriterWithSheet(sheetName1);

                //合并单元格后的标题行，使用默认标题样式
                //writer.merge(row1.size() - 1, "测试标题1");
                //writer.passCurrentRow();

                List<String> rowHead = new ArrayList<>();
                Map<String, String> headerAliasData = new LinkedHashMap<>();
                for (ExcelProperty item : excelMappingData.getPropertyList()) {
                    rowHead.add(item.getColumn());
                    headerAliasData.put(item.getName(), item.getColumn());
                }
                boolean isHead = true;

                if (mxApplyDateStrList.size() == 0) {
                    writer.writeHeadRow(rowHead);
                } else {
                    writer.setHeaderAlias(headerAliasData);
                    for (String applyDateStr : mxApplyDateStrList) {
                        dataSearchList = dataList.stream().filter(s -> s.getApplyDateStr().equals(applyDateStr)).collect(Collectors.toList());
                        writer.write(dataSearchList, isHead);
                        //writer.passCurrentRow();
                        writer.merge(headerAliasData.size() - 1, "", false);
                        isHead = false;
                    }
                }

                //设置所有列为自动宽度，不考虑合并单元格
                writer.autoSizeColumnAll();
                //标题Row高度
                writer.setRowHeight(0, 25);
                int rowHeightCount = dataList.size() + mxApplyDateStrList.size();
                //内容Row高度 有效 慢
//                for (int i = 1; i <= rowHeightCount; i++) {
//                    writer.setRowHeight(i,20);
//                }

                rowHead.clear();
                ExcelMapping excelMappingMain = ExcelMappingFactory.get(YbAppealResultMainReportExport.class);
                sheetColumnCountMap.put(sheetName2, excelMappingMain.getPropertyList().size());

                writer.setSheet(sheetName2);
                Map<String, String> headerAliasMain = new LinkedHashMap<>();
                for (ExcelProperty item : excelMappingMain.getPropertyList()) {
                    rowHead.add(item.getColumn());
                    headerAliasMain.put(item.getName(), item.getColumn());
                }

                if (zdApplyDateStrList.size() == 0) {
                    writer.writeHeadRow(rowHead);
                } else {
                    isHead = true;
                    writer.setHeaderAlias(headerAliasMain);
                    for (String applyDateStr : zdApplyDateStrList) {
                        mainSearchList = mainList.stream().filter(s -> s.getApplyDateStr().equals(applyDateStr)).collect(Collectors.toList());
                        writer.write(mainSearchList, isHead);
                        writer.merge(headerAliasMain.size() - 1, "", false);
                        //writer.passCurrentRow();
                        isHead = false;
                    }
                }

                //设置所有列为自动宽度，不考虑合并单元格
                writer.autoSizeColumnAll();
                //标题Row高度
                writer.setRowHeight(0, 25);
                rowHeightCount = mainList.size() + zdApplyDateStrList.size();
                //内容Row高度 有效 慢
//                for (int i = 1; i <= rowHeightCount; i++) {
//                    writer.setRowHeight(i,20);
//                }

                StyleSet style = writer.getStyleSet();
                CellStyle cellStyle = style.getHeadCellStyle();
                Font f1 = writer.createFont();
                f1.setBold(true);
                f1.setFontName("宋体");
                short fontHeight = 280;
                f1.setFontHeight(fontHeight);
                cellStyle.setFont(f1);

                List<org.apache.poi.ss.usermodel.Sheet> sheetList = writer.getSheets();
                for (org.apache.poi.ss.usermodel.Sheet sheet : sheetList) {
                    int count = sheetColumnCountMap.get(sheet.getSheetName());
                    for (int i = 0; i <= count; i++) {
                        sheet.autoSizeColumn(i);
                    }
                }
                //response为HttpServletResponse对象
                response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet;charset=utf-8");
                //response.setContentType("application/vnd.ms-excel;charset=utf-8");
                //test.xls是弹出下载对话框的文件名，不能为中文，中文请自行编码
                response.setHeader("Content-Disposition", "attachment;filename=test.xls");
                ServletOutputStream out = response.getOutputStream();

                writer.flush(out, true);
                // 关闭writer，释放内存
                writer.close();
                //此处记得关闭输出Servlet流
                IoUtil.close(out);
            } else {
            }
        } catch (Exception e) {
            message = "导出Excel失败";
            log.error(message, e);
            throw new FebsException(message);
        }
    }

    @PostMapping("reportUserExcel")
    @RequiresPermissions("ybAppealResultReportView:exportUserData")
    public void reportUserExcel(QueryRequest request, YbAppealResultReportView ybAppealResultReportView, HttpServletResponse response, String keyField) throws FebsException {
        try {
            User currentUser = FebsUtil.getCurrentUser();
            ybAppealResultReportView.setArDoctorCode(currentUser.getUsername());
            List<YbAppealResultReportView> appealResultReportList = this.iYbAppealResultReportViewService.findYbAppealResultReportLists(ybAppealResultReportView, keyField, true);

            if (appealResultReportList.size() > 0) {
                List<YbAppealResultReportView> appealResultReportDataList = new ArrayList<>();
                List<YbAppealResultReportView> appealResultReportMainList = new ArrayList<>();
                appealResultReportDataList = appealResultReportList.stream().filter(
                        s -> s.getDataType().equals(YbDefaultValue.DATATYPE_0)).sorted(Comparator.comparing(YbAppealResultReportView::getApplyDateStr).thenComparing(YbAppealResultReportView::getTypeno).thenComparing(YbAppealResultReportView::getOrderNum)
                ).collect(Collectors.toList());
//                if (appealResultReportDataList.size() > 0) {
//                    Collections.sort(appealResultReportDataList);
//                }
                appealResultReportMainList = appealResultReportList.stream().filter(
                        s -> s.getDataType().equals(YbDefaultValue.DATATYPE_1)).sorted(Comparator.comparing(YbAppealResultReportView::getApplyDateStr).thenComparing(YbAppealResultReportView::getTypeno).thenComparing(YbAppealResultReportView::getOrderNum)
                ).collect(Collectors.toList());
//                if (appealResultReportMainList.size() > 0) {
//                    Collections.sort(appealResultReportMainList);
//                }
                List<YbAppealResultDataReportExport> dataList = new ArrayList<>();
                List<YbAppealResultMainReportExport> mainList = new ArrayList<>();

                List<YbAppealResultDataReportExport> dataSearchList = new ArrayList<>();
                List<YbAppealResultMainReportExport> mainSearchList = new ArrayList<>();

                SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
                String dateString = "";

                List<String> mxApplyDateStrList = new ArrayList<>();
                List<String> zdApplyDateStrList = new ArrayList<>();
                //明细扣款
                for (YbAppealResultReportView item : appealResultReportDataList) {
                    YbAppealResultDataReportExport are = new YbAppealResultDataReportExport();

                    String appDateStr = item.getApplyDateStr();
                    appDateStr = DataTypeHelpers.stringDate7Chang6(appDateStr);
                    item.setApplyDateStr(appDateStr);

                    if (!mxApplyDateStrList.stream().anyMatch(task -> task.equals(item.getApplyDateStr()))) {
                        mxApplyDateStrList.add(item.getApplyDateStr());
                    }

                    are.setApplyDateStr(item.getApplyDateStr());
                    //序号
                    are.setOrderNumber(item.getOrderNumber());
                    //交易流水号
                    are.setSerialNo(item.getSerialNo());
                    //单据号
                    are.setBillNo(item.getBillNo());
                    //意见书编码
                    are.setProposalCode(item.getProposalCode());
                    //项目编码
                    are.setProjectCode(item.getProjectCode());
                    //项目名称
                    are.setProjectName(item.getProjectName());
                    //数量
                    are.setNum(item.getNum());
                    //医保内金额
                    are.setMedicalPrice(item.getMedicalPrice());
                    //规则名称
                    are.setRuleName(item.getRuleName());
                    //扣除金额
                    are.setDeductPrice(item.getDeductPrice());
                    //扣除原因
                    are.setDeductReason(item.getDeductReason());
                    //还款原因
                    are.setRepaymentReason(item.getRepaymentReason());
                    //医生姓名
                    are.setDoctorName(item.getDoctorName());
                    //科室编码
                    are.setDeptCode(item.getDeptCode());
                    //科室名称
                    are.setDeptName(item.getDeptName());
                    //入院日期
//                    if (item.getEnterHospitalDate() != null) {
//                        dateString = formatter.format(item.getEnterHospitalDate());
//                    }
                    are.setEnterHospitalDateStr(item.getEnterHospitalDateStr());

                    //出院日期
                    dateString = "";
//                    if (item.getOutHospitalDate() != null) {
//                        dateString = formatter.format(item.getOutHospitalDate());
//                    }
                    are.setOutHospitalDateStr(item.getOutHospitalDateStr());
                    //费用日期
                    dateString = "";
//                    if (item.getCostDate() != null) {
//                        dateString = formatter.format(item.getCostDate());
//                    }
                    are.setCostDateStr(item.getCostDateStr());
                    //住院号
                    are.setHospitalizedNo(item.getHospitalizedNo());
                    //就医方式
                    are.setTreatmentMode(item.getTreatmentMode());
                    //结算日期
                    dateString = "";
//                    if (item.getSettlementDate() != null) {
//                        dateString = formatter.format(item.getSettlementDate());
//                    }
                    are.setSettlementDateStr(item.getSettlementDateStr());
                    //个人编号
                    are.setPersonalNo(item.getPersonalNo());
                    //参保人姓名
                    are.setInsuredName(item.getInsuredName());
                    //医保卡号
                    are.setCardNumber(item.getCardNumber());
                    //统筹区名称
                    are.setAreaName(item.getAreaName());
                    //版本号
                    are.setVersionNumber(item.getVersionNumber());
                    //反馈申诉
                    are.setBackAppeal(item.getOperateReason());
                    are.setArDoctorCode(item.getArDoctorCode());
                    are.setArDoctorName(item.getArDoctorName());
                    are.setArDeptCode(item.getArDeptCode());
                    are.setArDeptName(item.getArDeptName());
                    dataList.add(are);

                }
                //主单扣款
                for (YbAppealResultReportView item : appealResultReportMainList) {
                    YbAppealResultMainReportExport are = new YbAppealResultMainReportExport();

                    String appDateStr = item.getApplyDateStr();
                    appDateStr = DataTypeHelpers.stringDate7Chang6(appDateStr);
                    item.setApplyDateStr(appDateStr);

                    if (!zdApplyDateStrList.stream().anyMatch(task -> task.equals(item.getApplyDateStr()))) {
                        zdApplyDateStrList.add(item.getApplyDateStr());
                    }
                    are.setApplyDateStr(item.getApplyDateStr());
                    //序号
                    are.setOrderNumber(item.getOrderNumber());
                    //交易流水号
                    are.setSerialNo(item.getSerialNo());
                    //单据号
                    are.setBillNo(item.getBillNo());
                    //意见书编码
                    are.setProposalCode(item.getProposalCode());

                    //医保内金额
                    are.setMedicalPrice(item.getMedicalPrice());
                    //规则名称
                    are.setRuleName(item.getRuleName());
                    //扣除金额
                    are.setDeductPrice(item.getDeductPrice());

                    //入院日期
//                    if (item.getEnterHospitalDate() != null) {
//                        dateString = formatter.format(item.getEnterHospitalDate());
//                    }
                    are.setEnterHospitalDateStr(item.getEnterHospitalDateStr());

                    //出院日期
                    dateString = "";
//                    if (item.getOutHospitalDate() != null) {
//                        dateString = formatter.format(item.getOutHospitalDate());
//                    }
                    are.setOutHospitalDateStr(item.getOutHospitalDateStr());

                    //住院号
                    are.setHospitalizedNo(item.getHospitalizedNo());
                    //就医方式
                    are.setTreatmentMode(item.getTreatmentMode());
                    //结算日期
                    dateString = "";
//                    if (item.getSettlementDate() != null) {
//                        dateString = formatter.format(item.getSettlementDate());
//                    }
                    are.setSettlementDateStr(item.getSettlementDateStr());

                    //个人编号
                    are.setPersonalNo(item.getPersonalNo());
                    //参保人姓名
                    are.setInsuredName(item.getInsuredName());
                    //参保类型
                    are.setInsuredType(item.getInsuredType());
                    //统筹区名称
                    are.setAreaName(item.getAreaName());
                    //版本号
                    are.setVersionNumber(item.getVersionNumber());
                    //反馈申诉
                    are.setBackAppeal(item.getOperateReason());
                    are.setArDoctorCode(item.getArDoctorCode());
                    are.setArDoctorName(item.getArDoctorName());
                    are.setArDeptCode(item.getArDeptCode());
                    are.setArDeptName(item.getArDeptName());
                    mainList.add(are);

                }

                if (mxApplyDateStrList.size() > 0) {
                    mxApplyDateStrList = mxApplyDateStrList.stream().sorted(Comparator.naturalOrder()).collect(Collectors.toList());
                }
                if (zdApplyDateStrList.size() > 0) {
                    zdApplyDateStrList = zdApplyDateStrList.stream().sorted(Comparator.naturalOrder()).collect(Collectors.toList());
                }

                String sheetName1 = "明细扣款";
                String sheetName2 = "主单扣款";

                ExcelMapping excelMappingData = ExcelMappingFactory.get(YbAppealResultDataReportExport.class);
                Map<String, Integer> sheetColumnCountMap = new LinkedHashMap<>();
                sheetColumnCountMap.put(sheetName1, excelMappingData.getPropertyList().size());

                ExcelWriter writer = ExcelUtil.getWriterWithSheet(sheetName1);

                //合并单元格后的标题行，使用默认标题样式
                //writer.merge(row1.size() - 1, "测试标题1");
                //writer.passCurrentRow();

                List<String> rowHead = new ArrayList<>();
                Map<String, String> headerAliasData = new LinkedHashMap<>();
                for (ExcelProperty item : excelMappingData.getPropertyList()) {
                    rowHead.add(item.getColumn());
                    headerAliasData.put(item.getName(), item.getColumn());
                }
                boolean isHead = true;

                if (mxApplyDateStrList.size() == 0) {
                    writer.writeHeadRow(rowHead);
                } else {
                    writer.setHeaderAlias(headerAliasData);
                    for (String applyDateStr : mxApplyDateStrList) {
                        dataSearchList = dataList.stream().filter(s -> s.getApplyDateStr().equals(applyDateStr)).collect(Collectors.toList());
                        writer.write(dataSearchList, isHead);
                        //writer.passCurrentRow();
                        writer.merge(headerAliasData.size() - 1, "", false);
                        isHead = false;
                    }
                }

                //设置所有列为自动宽度，不考虑合并单元格
                writer.autoSizeColumnAll();
                //标题Row高度
                writer.setRowHeight(0, 25);
                int rowHeightCount = dataList.size() + mxApplyDateStrList.size();
                //内容Row高度 有效 慢
//                for (int i = 1; i <= rowHeightCount; i++) {
//                    writer.setRowHeight(i,20);
//                }


                rowHead.clear();
                ExcelMapping excelMappingMain = ExcelMappingFactory.get(YbAppealResultMainReportExport.class);
                sheetColumnCountMap.put(sheetName2, excelMappingMain.getPropertyList().size());

                writer.setSheet(sheetName2);
                Map<String, String> headerAliasMain = new LinkedHashMap<>();
                for (ExcelProperty item : excelMappingMain.getPropertyList()) {
                    rowHead.add(item.getColumn());
                    headerAliasMain.put(item.getName(), item.getColumn());
                }

                if (zdApplyDateStrList.size() == 0) {
                    writer.writeHeadRow(rowHead);
                } else {
                    isHead = true;
                    writer.setHeaderAlias(headerAliasMain);
                    for (String applyDateStr : zdApplyDateStrList) {
                        mainSearchList = mainList.stream().filter(s -> s.getApplyDateStr().equals(applyDateStr)).collect(Collectors.toList());
                        writer.write(mainSearchList, isHead);
                        writer.merge(headerAliasMain.size() - 1, "", false);
                        //writer.passCurrentRow();
                        isHead = false;
                    }
                }

                //设置所有列为自动宽度，不考虑合并单元格
                writer.autoSizeColumnAll();
                //标题Row高度
                writer.setRowHeight(0, 25);
                rowHeightCount = mainList.size() + zdApplyDateStrList.size();
                //内容Row高度 有效 慢
//                for (int i = 1; i <= rowHeightCount; i++) {
//                    writer.setRowHeight(i,20);
//                }

                StyleSet style = writer.getStyleSet();
                CellStyle cellStyle = style.getHeadCellStyle();
                Font f1 = writer.createFont();
                f1.setBold(true);
                f1.setFontName("宋体");
                short fontHeight = 280;
                f1.setFontHeight(fontHeight);
                cellStyle.setFont(f1);

                List<org.apache.poi.ss.usermodel.Sheet> sheetList = writer.getSheets();
                for (org.apache.poi.ss.usermodel.Sheet sheet : sheetList) {
                    int count = sheetColumnCountMap.get(sheet.getSheetName());
                    for (int i = 0; i <= count; i++) {
                        sheet.autoSizeColumn(i);
                    }
                }
                //response为HttpServletResponse对象
                response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet;charset=utf-8");
                //response.setContentType("application/vnd.ms-excel;charset=utf-8");
                //test.xls是弹出下载对话框的文件名，不能为中文，中文请自行编码
                response.setHeader("Content-Disposition", "attachment;filename=test.xls");
                ServletOutputStream out = response.getOutputStream();

                writer.flush(out, true);
                // 关闭writer，释放内存
                writer.close();
                //此处记得关闭输出Servlet流
                IoUtil.close(out);
            } else {
            }
        } catch (Exception e) {
            message = "导出Excel失败";
            log.error(message, e);
            throw new FebsException(message);
        }
    }

}