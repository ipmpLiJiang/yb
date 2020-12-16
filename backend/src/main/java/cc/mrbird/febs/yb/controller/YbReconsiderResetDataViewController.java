package cc.mrbird.febs.yb.controller;

import cc.mrbird.febs.common.annotation.Log;
import cc.mrbird.febs.common.controller.BaseController;
import cc.mrbird.febs.common.domain.router.VueRouter;
import cc.mrbird.febs.common.exception.FebsException;
import cc.mrbird.febs.common.domain.QueryRequest;

import cc.mrbird.febs.common.properties.FebsProperties;
import cc.mrbird.febs.yb.entity.*;
import cc.mrbird.febs.yb.service.IYbReconsiderResetDataViewService;

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
 * @since 2020-08-18
 */
@Slf4j
@Validated
@RestController
@RequestMapping("ybReconsiderResetDataView")

public class YbReconsiderResetDataViewController extends BaseController {

    private String message;
    @Autowired
    public IYbReconsiderResetDataViewService iYbReconsiderResetDataViewService;

    @Autowired
    public FebsProperties febsProperties;

    /**
     * 分页查询数据
     *
     * @param request                   分页信息
     * @param ybReconsiderResetDataView 查询条件
     * @return
     */
    @GetMapping
    @RequiresPermissions("ybReconsiderResetDataView:view")
    public Map<String, Object> List(QueryRequest request, YbReconsiderResetDataView ybReconsiderResetDataView) {
        return getDataTable(this.iYbReconsiderResetDataViewService.findYbReconsiderResetDataViews(request, ybReconsiderResetDataView));
    }

    /**
     * 添加
     *
     * @param ybReconsiderResetDataView
     * @return
     */
    @Log("新增/按钮")
    @PostMapping
    @RequiresPermissions("ybReconsiderResetDataView:add")
    public void addYbReconsiderResetDataView(@Valid YbReconsiderResetDataView ybReconsiderResetDataView) throws FebsException {
        try {
            User currentUser = FebsUtil.getCurrentUser();
//        ybReconsiderResetDataView.setCreateUserId(currentUser.getUserId());
            this.iYbReconsiderResetDataViewService.createYbReconsiderResetDataView(ybReconsiderResetDataView);
        } catch (Exception e) {
            message = "新增/按钮失败";
            log.error(message, e);
            throw new FebsException(message);
        }
    }

    /**
     * 修改
     *
     * @param ybReconsiderResetDataView
     * @return
     */
    @Log("修改")
    @PutMapping
    @RequiresPermissions("ybReconsiderResetDataView:update")
    public void updateYbReconsiderResetDataView(@Valid YbReconsiderResetDataView ybReconsiderResetDataView) throws FebsException {
        try {
            User currentUser = FebsUtil.getCurrentUser();
//      ybReconsiderResetDataView.setModifyUserId(currentUser.getUserId());
            this.iYbReconsiderResetDataViewService.updateYbReconsiderResetDataView(ybReconsiderResetDataView);
        } catch (Exception e) {
            message = "修改失败";
            log.error(message, e);
            throw new FebsException(message);
        }
    }


    @Log("删除")
    @DeleteMapping("/{ids}")
    @RequiresPermissions("ybReconsiderResetDataView:delete")
    public void deleteYbReconsiderResetDataViews(@NotBlank(message = "{required}") @PathVariable String ids) throws FebsException {
        try {
            String[] arr_ids = ids.split(StringPool.COMMA);
            this.iYbReconsiderResetDataViewService.deleteYbReconsiderResetDataViews(arr_ids);
        } catch (Exception e) {
            message = "删除失败";
            log.error(message, e);
            throw new FebsException(message);
        }
    }

    @PostMapping("excel")
    @RequiresPermissions("ybReconsiderResetDataView:export")
    public void export(QueryRequest request, YbReconsiderResetDataView ybReconsiderResetDataView, HttpServletResponse response) throws FebsException {
        try {
            List<YbReconsiderResetDataView> ybReconsiderResetDataViews = this.iYbReconsiderResetDataViewService.findYbReconsiderResetDataViews(request, ybReconsiderResetDataView).getRecords();
            ExcelKit.$Export(YbReconsiderResetDataView.class, response).downXlsx(ybReconsiderResetDataViews, false);
        } catch (Exception e) {
            message = "导出Excel失败";
            log.error(message, e);
            throw new FebsException(message);
        }
    }

    @GetMapping("/{id}")
    public YbReconsiderResetDataView detail(@NotBlank(message = "{required}") @PathVariable String id) {
        YbReconsiderResetDataView ybReconsiderResetDataView = this.iYbReconsiderResetDataViewService.getById(id);
        return ybReconsiderResetDataView;
    }

    @PostMapping("excelReset")
    @RequiresPermissions("ybReconsiderResetDataView:exportData")
    public void export1(QueryRequest request, YbReconsiderResetDataView resetDataView, HttpServletResponse response) throws FebsException {
        try {
            List<YbReconsiderResetDataView> resetDataList = this.iYbReconsiderResetDataViewService.findYbReconsiderResetDataList(resetDataView);

            if (resetDataList.size() > 0) {
                List<YbReconsiderResetDataView> resetDataDataList = new ArrayList<>();
                List<YbReconsiderResetDataView> resetDataMainList = new ArrayList<>();

                resetDataDataList = resetDataList.stream().filter(s -> s.getDataType().equals(0)).collect(Collectors.toList());
                if (resetDataDataList.size() > 0) {
                    Collections.sort(resetDataDataList);
                }
                resetDataMainList = resetDataList.stream().filter(s -> s.getDataType().equals(1)).collect(Collectors.toList());
                if (resetDataMainList.size() > 0) {
                    Collections.sort(resetDataMainList);
                }

                List<YbReconsiderResetDataExport> dataList = new ArrayList<>();
                List<YbReconsiderResetMainExport> mainList = new ArrayList<>();

                SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
                String dateString = "";

                //明细扣款
                for (YbReconsiderResetDataView item : resetDataDataList) {
                    YbReconsiderResetDataExport are = new YbReconsiderResetDataExport();
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
                    //统筹区名称
                    are.setAreaName(item.getAreaName());
                    dataList.add(are);
                }
                //主单扣款
                for (YbReconsiderResetDataView item : resetDataMainList) {
                    YbReconsiderResetMainExport are = new YbReconsiderResetMainExport();
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
                    mainList.add(are);
                }

                String sheetName1 = "明细扣款";
                String sheetName2 = "主单扣款";

                ExcelWriter writer = ExcelUtil.getWriterWithSheet(sheetName1);

                ExcelMapping excelMappingData = ExcelMappingFactory.get(YbReconsiderResetDataExport.class);
                Map<String,Integer> sheetColumnCountMap =  new LinkedHashMap<>();
                sheetColumnCountMap.put(sheetName1,excelMappingData.getPropertyList().size());

                List<String> rowHead = new ArrayList<>();
                Map<String, String> headerAliasData = new LinkedHashMap<>();
                for(ExcelProperty item : excelMappingData.getPropertyList()){
                    rowHead.add(item.getColumn());
                    headerAliasData.put(item.getName(), item.getColumn());
                }

                if(dataList.size()==0) {
                    writer.writeHeadRow(rowHead);
                }else {
                    writer.setHeaderAlias(headerAliasData);
                    writer.write(dataList, true);
                }

                //设置所有列为自动宽度，不考虑合并单元格
                writer.autoSizeColumnAll();
                //标题Row高度
                writer.setRowHeight(0,25);
                //内容Row高度
                for (int i = 1; i <= dataList.size(); i++) {
                    writer.setRowHeight(i,20);
                }
                //设置遍历单个列为自动宽度
//                for (int i = 0; i < YbReconsiderResetUnknownDataExport.class.getDeclaredFields().length; i++) {
//                    writer.autoSizeColumn(i);
//                }

                ExcelMapping excelMappingMain = ExcelMappingFactory.get(YbReconsiderResetMainExport.class);
                sheetColumnCountMap.put(sheetName2,excelMappingMain.getPropertyList().size());
                rowHead.clear();

                writer.setSheet(sheetName2);
                Map<String, String> headerAliasMain = new LinkedHashMap<>();
                for(ExcelProperty item : excelMappingMain.getPropertyList()){
                    rowHead.add(item.getColumn());
                    headerAliasMain.put(item.getName(), item.getColumn());
                }
                if(mainList.size()==0) {
                    writer.writeHeadRow(rowHead);
                }else {
                    writer.setHeaderAlias(headerAliasMain);
                    writer.write(mainList, true);
                }

                //设置所有列为自动宽度，不考虑合并单元格
                writer.autoSizeColumnAll();
                //标题Row高度
                writer.setRowHeight(0,25);
                //内容Row高度
                for (int i = 1; i <= mainList.size(); i++) {
                    writer.setRowHeight(i,20);
                }
                //设置遍历单个列为自动宽度
//                for (int i = 0; i < YbReconsiderResetMainExport.class.getDeclaredFields().length; i++) {
//                    writer.autoSizeColumn(i);
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
                for(org.apache.poi.ss.usermodel.Sheet sheet : sheetList){
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

    @PostMapping("excelResetDeductimplement")
    @RequiresPermissions("ybReconsiderResetDataView:exportData")
    public void export2(QueryRequest request, YbReconsiderResetDataView resetDataView, HttpServletResponse response) throws FebsException {
        try {
            List<YbReconsiderResetDataView> resetDataList = this.iYbReconsiderResetDataViewService.findYbReconsiderResetDataList(resetDataView);

            if (resetDataList.size() > 0) {
                List<YbReconsiderResetDataView> resetDataDataList = new ArrayList<>();
                List<YbReconsiderResetDataView> resetDataMainList = new ArrayList<>();

                resetDataDataList = resetDataList.stream().filter(s -> s.getDataType().equals(0)).collect(Collectors.toList());
                if (resetDataDataList.size() > 0) {
                    Collections.sort(resetDataDataList);
                }
                resetDataMainList = resetDataList.stream().filter(s -> s.getDataType().equals(1)).collect(Collectors.toList());
                if (resetDataMainList.size() > 0) {
                    Collections.sort(resetDataMainList);
                }

                List<YbReconsiderResetDeductimplementDataExport> dataList = new ArrayList<>();
                List<YbReconsiderResetDeductimplementMainExport> mainList = new ArrayList<>();

                SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
                String dateString = "";
                boolean isOne = true;
                //明细扣款
                for (YbReconsiderResetDataView item : resetDataDataList) {
                    YbReconsiderResetDeductimplementDataExport are = new YbReconsiderResetDeductimplementDataExport();
                    if(isOne){
                        are.setImplementDateStr("格式:202010");
                        are.setShareStateStr("个人分摊/科室分摊");
                    }
                    isOne = false;
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
                    //统筹区名称
                    are.setAreaName(item.getAreaName());
                    dataList.add(are);
                }

                isOne = true;
                //主单扣款
                for (YbReconsiderResetDataView item : resetDataMainList) {
                    YbReconsiderResetDeductimplementMainExport are = new YbReconsiderResetDeductimplementMainExport();
                    if(isOne){
                        are.setImplementDateStr("格式:202010");
                        are.setShareStateStr("个人分摊/科室分摊");
                    }
                    isOne = false;
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
                    mainList.add(are);
                }

                String sheetName1 = "明细扣款";
                String sheetName2 = "主单扣款";

                ExcelWriter writer = ExcelUtil.getWriterWithSheet(sheetName1);

                ExcelMapping excelMappingData = ExcelMappingFactory.get(YbReconsiderResetDeductimplementDataExport.class);
                Map<String,Integer> sheetColumnCountMap =  new LinkedHashMap<>();
                sheetColumnCountMap.put(sheetName1,excelMappingData.getPropertyList().size());

                List<String> rowHead = new ArrayList<>();
                Map<String, String> headerAliasData = new LinkedHashMap<>();
                for(ExcelProperty item : excelMappingData.getPropertyList()){
                    rowHead.add(item.getColumn());
                    headerAliasData.put(item.getName(), item.getColumn());
                }

                if(dataList.size()==0) {
                    writer.writeHeadRow(rowHead);
                }else {
                    writer.setHeaderAlias(headerAliasData);
                    writer.write(dataList, true);
                }

                //设置所有列为自动宽度，不考虑合并单元格
                writer.autoSizeColumnAll();
                //标题Row高度
                writer.setRowHeight(0,25);
                //内容Row高度
                for (int i = 1; i <= dataList.size(); i++) {
                    writer.setRowHeight(i,20);
                }
                //设置遍历单个列为自动宽度
//                for (int i = 0; i < YbReconsiderResetDeductimplementDataExport.class.getDeclaredFields().length; i++) {
//                    writer.autoSizeColumn(i);
//                }

                ExcelMapping excelMappingMain = ExcelMappingFactory.get(YbReconsiderResetDeductimplementMainExport.class);
                sheetColumnCountMap.put(sheetName2,excelMappingMain.getPropertyList().size());
                rowHead.clear();

                writer.setSheet(sheetName2);
                Map<String, String> headerAliasMain = new LinkedHashMap<>();
                for(ExcelProperty item : excelMappingMain.getPropertyList()){
                    rowHead.add(item.getColumn());
                    headerAliasMain.put(item.getName(), item.getColumn());
                }
                if(mainList.size()==0) {
                    writer.writeHeadRow(rowHead);
                }else {
                    writer.setHeaderAlias(headerAliasMain);
                    writer.write(mainList, true);
                }

                //设置所有列为自动宽度，不考虑合并单元格
                writer.autoSizeColumnAll();
                //标题Row高度
                writer.setRowHeight(0,25);
                //内容Row高度
                for (int i = 1; i <= mainList.size(); i++) {
                    writer.setRowHeight(i,20);
                }
                //设置遍历单个列为自动宽度
//                for (int i = 0; i < YbReconsiderResetDeductimplementMainExport.class.getDeclaredFields().length; i++) {
//                    writer.autoSizeColumn(i);
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
                for(org.apache.poi.ss.usermodel.Sheet sheet : sheetList){
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