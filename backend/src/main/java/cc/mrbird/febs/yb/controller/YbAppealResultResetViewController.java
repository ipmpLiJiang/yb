package cc.mrbird.febs.yb.controller;

import cc.mrbird.febs.common.annotation.Log;
import cc.mrbird.febs.common.controller.BaseController;
import cc.mrbird.febs.common.domain.FebsResponse;
import cc.mrbird.febs.common.domain.QueryRequest;
import cc.mrbird.febs.common.exception.FebsException;
import cc.mrbird.febs.common.properties.FebsProperties;
import cc.mrbird.febs.common.utils.FebsUtil;
import cc.mrbird.febs.system.domain.User;
import cc.mrbird.febs.yb.entity.*;
import cc.mrbird.febs.yb.service.IYbAppealResultResetViewService;
import cc.mrbird.febs.yb.service.IYbDeptService;
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
 * @since 2020-08-12
 */
@Slf4j
@Validated
@RestController
@RequestMapping("ybAppealResultResetView")

public class YbAppealResultResetViewController extends BaseController {

    private String message;
    @Autowired
    public IYbAppealResultResetViewService iYbAppealResultResetViewService;
    @Autowired
    private FebsProperties febsProperties;

    /**
     * 分页查询数据
     *
     * @param request                 分页信息
     * @param ybAppealResultResetView 查询条件
     * @return
     */
    @GetMapping
//    @RequiresPermissions("ybAppealResultResetView:view")
    public Map<String, Object> List(QueryRequest request, YbAppealResultResetView ybAppealResultResetView, String keyField) {
//        if (ybAppealResultResetView.getCurrencyField() != null && ybAppealResultResetView.getCurrencyField() != "") {
//            System.out.println("View-New");
//            return getDataTable(this.iYbAppealResultResetViewService.findAppealResultResetViewNew(request, ybAppealResultResetView, keyField));
//        } else {
//            System.out.println("View-Old");
//            return getDataTable(this.iYbAppealResultResetViewService.findAppealResultResetViews(request, ybAppealResultResetView, keyField));
//        }
        return getDataTable(this.iYbAppealResultResetViewService.findAppealResultResetViews(request, ybAppealResultResetView, keyField));
    }

    @PostMapping("excelAppealResultReset")
//    @RequiresPermissions("ybAppealResultResetView:exportData")
    public void export2(QueryRequest request, YbAppealResultResetView ybAppealResultResetView, HttpServletResponse response) throws FebsException {
        try {
            List<YbAppealResultResetView> resultResetViewList = this.iYbAppealResultResetViewService.findAppealResultResetLists(ybAppealResultResetView);
            if (resultResetViewList.size() > 0) {
                List<YbAppealResultResetView> resultResetDataList = new ArrayList<>();
                List<YbAppealResultResetView> resultResetMainList = new ArrayList<>();

                resultResetDataList = resultResetViewList.stream().filter(s -> s.getDataType().equals(0)).collect(Collectors.toList());
                if (resultResetDataList.size() > 0) {
                    Collections.sort(resultResetDataList);
                }
                resultResetMainList = resultResetViewList.stream().filter(s -> s.getDataType().equals(1)).collect(Collectors.toList());
                if (resultResetMainList.size() > 0) {
                    Collections.sort(resultResetMainList);
                }

                List<YbAppealResultResetDataExport> dataList = new ArrayList<>();
                List<YbAppealResultResetMainExport> mainList = new ArrayList<>();

                //明细扣款
                for (YbAppealResultResetView item : resultResetDataList) {
                    YbAppealResultResetDataExport are = new YbAppealResultResetDataExport();
                    //序号
                    are.setOrderNumber(item.getOrderNumber());
                    //交易流水号
                    are.setSerialNo(item.getSerialNo());
                    //单据号
                    are.setBillNo(item.getBillNo());
                    //项目编码
                    are.setProjectCode(item.getProjectCode());
                    //项目名称
                    are.setProjectName(item.getProjectName());
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

                    //费用日期
//                    if (item.getCostDate() != null) {
//                        dateString = formatter.format(item.getCostDate());
//                    }
                    are.setCostDateStr(item.getCostDateStr());
                    //住院号
                    are.setHospitalizedNo(item.getHospitalizedNo());
                    //就医方式
                    are.setTreatmentMode(item.getTreatmentMode());
                    //结算日期
//                    if (item.getSettlementDate() != null) {
//                        dateString = formatter.format(item.getSettlementDate());
//                    }
                    are.setSettlementDateStr(item.getSettlementDateStr());
                    //个人编号
                    are.setPersonalNo(item.getPersonalNo());
                    //参保人姓名
                    are.setInsuredName(item.getInsuredName());
                    //统筹区名称
                    are.setAreaName(item.getAreaName());

                    are.setBackAppeal(item.getOperateReason());
                    if (item.getArDoctorCode() != null && item.getArDoctorName() != null) {
                        are.setResultDoctorName(item.getArDoctorCode() + "-" + item.getArDoctorName());
                    }
                    if (item.getArDeptCode() != null && item.getArDeptName() != null) {
                        are.setResultDeptName(item.getArDeptCode() + "-" + item.getArDeptName());
                    }

//                    are.setProposalCode(item.getProposalCode());
                    are.setDksName(item.getDksName());

                    dataList.add(are);
                }

                //主单扣款
                for (YbAppealResultResetView item : resultResetMainList) {
                    YbAppealResultResetMainExport are = new YbAppealResultResetMainExport();
                    //序号
                    are.setOrderNumber(item.getOrderNumber());
                    //交易流水号
                    are.setSerialNo(item.getSerialNo());
                    //单据号
                    are.setBillNo(item.getBillNo());

                    //医保内金额
                    are.setMedicalPrice(item.getMedicalPrice());
                    //规则名称
                    are.setRuleName(item.getRuleName());
                    //扣除金额
                    are.setDeductPrice(item.getDeductPrice());


                    //住院号
                    are.setHospitalizedNo(item.getHospitalizedNo());
                    //就医方式
                    are.setTreatmentMode(item.getTreatmentMode());
                    //结算日期
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

                    are.setBackAppeal(item.getOperateReason());
                    if (item.getArDoctorCode() != null && item.getArDoctorName() != null) {
                        are.setResultDoctorName(item.getArDoctorCode() + "-" + item.getArDoctorName());
                    }
                    if (item.getArDeptCode() != null && item.getArDeptName() != null) {
                        are.setResultDeptName(item.getArDeptCode() + "-" + item.getArDeptName());
                    }

//                    are.setProposalCode(item.getProposalCode());
                    are.setDksName(item.getDksName());
                    mainList.add(are);
                }

                String sheetName1 = "明细扣款";
                String sheetName2 = "主单扣款";

                ExcelWriter writer = ExcelUtil.getWriterWithSheet(sheetName1);

                ExcelMapping excelMappingData = ExcelMappingFactory.get(YbAppealResultResetDataExport.class);
                Map<String, Integer> sheetColumnCountMap = new LinkedHashMap<>();
                sheetColumnCountMap.put(sheetName1, excelMappingData.getPropertyList().size());

                List<String> rowHead = new ArrayList<>();
                Map<String, String> headerAliasData = new LinkedHashMap<>();
                for (ExcelProperty item : excelMappingData.getPropertyList()) {
                    rowHead.add(item.getColumn());
                    headerAliasData.put(item.getName(), item.getColumn());
                }

                if (dataList.size() == 0) {
                    writer.writeHeadRow(rowHead);
                } else {
                    writer.setHeaderAlias(headerAliasData);
                    writer.write(dataList, true);
                }

                //设置所有列为自动宽度，不考虑合并单元格
                writer.autoSizeColumnAll();
                //标题Row高度
                writer.setRowHeight(0, 25);
                //内容Row高度 有效 慢
                /*
                for (int i = 1; i <= dataList.size(); i++) {
                    writer.setRowHeight(i,20);
                }*/

                ExcelMapping excelMappingMain = ExcelMappingFactory.get(YbAppealResultResetMainExport.class);
                sheetColumnCountMap.put(sheetName2, excelMappingMain.getPropertyList().size());
                rowHead.clear();

                writer.setSheet(sheetName2);
                Map<String, String> headerAliasMain = new LinkedHashMap<>();
                for (ExcelProperty item : excelMappingMain.getPropertyList()) {
                    rowHead.add(item.getColumn());
                    headerAliasMain.put(item.getName(), item.getColumn());
                }
                if (mainList.size() == 0) {
                    writer.writeHeadRow(rowHead);
                } else {
                    writer.setHeaderAlias(headerAliasMain);
                    writer.write(mainList, true);
                }

                //设置所有列为自动宽度，不考虑合并单元格
                writer.autoSizeColumnAll();
                //标题Row高度
                writer.setRowHeight(0, 25);
                //内容Row高度 有效 慢
                /*
                for (int i = 1; i <= mainList.size(); i++) {
                    writer.setRowHeight(i,20);
                }*/

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