package cc.mrbird.febs.drg.controller;

import cc.mrbird.febs.com.controller.DataTypeHelpers;
import cc.mrbird.febs.com.entity.ComFile;
import cc.mrbird.febs.com.entity.ComType;
import cc.mrbird.febs.com.service.IComFileService;
import cc.mrbird.febs.com.service.IComTypeService;
import cc.mrbird.febs.common.controller.BaseController;
import cc.mrbird.febs.common.domain.FebsResponse;
import cc.mrbird.febs.common.domain.QueryRequest;
import cc.mrbird.febs.common.exception.FebsException;
import cc.mrbird.febs.common.utils.FebsUtil;
import cc.mrbird.febs.drg.entity.*;
import cc.mrbird.febs.drg.service.IYbDrgApplyService;
import cc.mrbird.febs.drg.service.IYbDrgJkService;
import cc.mrbird.febs.drg.service.IYbDrgManageService;
import cc.mrbird.febs.drg.service.IYbDrgResultViewService;
import cc.mrbird.febs.export.excel.ExportExcelUtils;
import cc.mrbird.febs.system.domain.User;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletResponse;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @author viki
 * @since 2020-07-30
 */
@Slf4j
@Validated
@RestController
@RequestMapping("ybDrgResultView")

public class YbDrgResultViewController extends BaseController {

    @Autowired
    IYbDrgApplyService iYbDrgApplyService;

    @Autowired
    IComFileService iComFileService;

    @Autowired
    IComTypeService iComTypeService;

    private String message;
    @Autowired
    public IYbDrgResultViewService iYbDrgResultViewService;

    @Autowired
    public IYbDrgManageService iYbDrgManageService;

    @Autowired
    public IYbDrgJkService iYbDrgJkService;

    @GetMapping()
    @RequiresPermissions("ybDrgResultView:view")
    public Map<String, Object> List(QueryRequest request, YbDrgResultView ybDrgResultView, String keyField) {
        return getDataTable(this.iYbDrgResultViewService.findDrgResultView(request, ybDrgResultView, keyField));
    }

    @PostMapping("exportResult")
    @RequiresPermissions("ybDrgResultView:view")
    public void exportResult(QueryRequest request, YbDrgResultView ybDrgResultView, HttpServletResponse response) throws FebsException {
        boolean isError = false;
        try {
            YbDrgApply drgApply = this.iYbDrgApplyService.findDrgApplyByApplyDateStrs(ybDrgResultView.getApplyDateStr(), ybDrgResultView.getAreaType());
            if (drgApply != null) {
                ybDrgResultView.setPid(drgApply.getId());
                ybDrgResultView.setAreaType(ybDrgResultView.getAreaType());
                List<YbDrgResultView> list = this.iYbDrgResultViewService.findDrgResultViewLists(ybDrgResultView);
                list = list.stream().sorted(Comparator.comparing(YbDrgResultView::getOrderNum)).collect(Collectors.toList());

                List<YbDrgManage> manageList = new ArrayList<>();
                List<YbDrgJk> jkList = new ArrayList<>();
                List<YbDrgManage> queryManageList = new ArrayList<>();
                List<YbDrgJk> queryJkList = new ArrayList<>();
                if (list.size() > 0) {
                    YbDrgManage query = new YbDrgManage();
                    query.setApplyDateStr(drgApply.getApplyDateStr());
                    query.setAreaType(drgApply.getAreaType());
                    manageList = iYbDrgManageService.findDrgManageList(query);
                    if (manageList.size() > 0) {
                        manageList = manageList.stream().filter(s -> s.getState() != 3 && s.getState() != 4).collect(Collectors.toList());
                    }

                    LambdaQueryWrapper<YbDrgJk> wrapper = new LambdaQueryWrapper<>();
                    wrapper.eq(YbDrgJk::getApplyDateStr, drgApply.getApplyDateStr());
                    wrapper.eq(YbDrgJk::getAreaType, drgApply.getAreaType());
                    jkList = iYbDrgJkService.list(wrapper);
                }

                List<YbDrgApplyDataResult> exportList = new ArrayList<>();
                for (YbDrgResultView item : list) {
                    YbDrgApplyDataResult dataExport = new YbDrgApplyDataResult();
                    dataExport.setOrderNumber(item.getOrderNumber());//序号
                    dataExport.setKs(item.getKs());//科室
                    dataExport.setJzjlh(item.getJzjlh());//就诊记录号
                    dataExport.setBah(item.getBah());//病案号
                    dataExport.setWglx(item.getWglx());//违规类型
                    dataExport.setWtms(item.getWtms());//问题描述
                    dataExport.setYlzfy(item.getYlzfy());//医疗总费用
                    dataExport.setWgje(item.getWgje());//违规金额
                    dataExport.setSfbmzczjcw(item.getSfbmzczjcw());//是否编码造成直接错误
                    dataExport.setLy(item.getLy());//理由

                    String strDoctorName = DataTypeHelpers.stringReplaceSetString(item.getDoctorName(), item.getDoctorCode() + "-");

                    dataExport.setOperateReason(item.getOperateReason());
                    dataExport.setDoctorCode(item.getDoctorCode());//复议医生编码
                    dataExport.setDoctorName(strDoctorName);//复议医生姓名
                    dataExport.setDksName(item.getDksName());//复议科室名称

                    if (item.getDoctorCode() == null) {
                        queryManageList = manageList.stream().filter(s -> s.getApplyDataId().equals(item.getApplyDataId())).collect(Collectors.toList());
                        if (queryManageList.size() > 0) {
                            dataExport.setDoctorCode(queryManageList.get(0).getReadyDoctorCode());//复议医生编码
                            dataExport.setDoctorName(queryManageList.get(0).getReadyDoctorName());//复议医生姓名
                            dataExport.setDksName(queryManageList.get(0).getReadyDksName());//复议科室名称
                        }
                    }

                    queryJkList = jkList.stream().filter(s -> s.getApplyDataId().equals(item.getApplyDataId())).collect(Collectors.toList());
                    if (queryJkList.size() > 0) {
                        YbDrgJk jk = queryJkList.get(0);
                        dataExport.setRyDate(jk.getRyDate());
                        dataExport.setCyDate(jk.getCyDate());
                        dataExport.setTczf(jk.getTczf());
                        dataExport.setFzCode(jk.getFzCode());
                        dataExport.setFzName(jk.getFzName());
                        dataExport.setZyzdCode(jk.getZyzdCode());
                        dataExport.setZyzdName(jk.getZyzdName());
                        dataExport.setZssCode(jk.getZssCode());
                        dataExport.setZssName(jk.getZssName());
                        dataExport.setQtzdCode(jk.getQtzdCode());
                        dataExport.setQtzdName(jk.getQtzdName());
                        dataExport.setQtssCode(jk.getQtssCode());
                        dataExport.setQtssName(jk.getQtssName());
                        dataExport.setDeptName(jk.getDeptName());
                        dataExport.setAreaName(jk.getAreaName());
                        dataExport.setQz(jk.getQz());
                        if (jk.getKzrDocId() != null && !jk.getKzrDocId().equals("")) {
                            dataExport.setKzrDocName(jk.getKzrDocId() + "-" + jk.getKzrDocName());
                        }
                        if (jk.getZrysDocId() != null && !jk.getZrysDocId().equals("")) {
                            dataExport.setZrysDocName(jk.getZrysDocId() + "-" + jk.getZrysDocName());
                        }
                        if (jk.getZzysDocId() != null && !jk.getZzysDocId().equals("")) {
                            dataExport.setZzysDocName(jk.getZzysDocId() + "-" + jk.getZzysDocName());
                        }
                        if (jk.getZyysDocId() != null && !jk.getZyysDocId().equals("")) {
                            dataExport.setZyysDocName(jk.getZyysDocId() + "-" + jk.getZyysDocName());
                        }
                        if (jk.getYlzDeptName() != null && !jk.getYlzDeptName().equals("")) {
                            dataExport.setYlzDeptName(jk.getYlzDeptName());
                        }
                        if (jk.getYlzDocId() != null && !jk.getYlzDocId().equals("")) {
                            dataExport.setYlzDocName(jk.getYlzDocId() + "-" + jk.getYlzDocName());
                        }
                    }
                    exportList.add(dataExport);
                }

                ExportExcelUtils.exportExcel(response, YbDrgApplyDataResult.class, exportList, "DRG结果明细数据");

            } else {
                isError = true;
                message = "未找到 " + ybDrgResultView.getApplyDateStr() + " 上传数据.";
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

    @PostMapping("exportResultYj")
    @RequiresPermissions("ybDrgResultView:view")
    public void exportResultYj(QueryRequest request, YbDrgResultView ybDrgResultView, HttpServletResponse response) throws FebsException {
        boolean isError = false;
        try {
            YbDrgApply drgApply = this.iYbDrgApplyService.findDrgApplyByApplyDateStrs(ybDrgResultView.getApplyDateStr(), ybDrgResultView.getAreaType());
            if (drgApply != null) {
                ybDrgResultView.setPid(drgApply.getId());
                ybDrgResultView.setAreaType(ybDrgResultView.getAreaType());
                List<YbDrgResultView> list = this.iYbDrgResultViewService.findDrgResultViewInnerLists(ybDrgResultView);
                list = list.stream().sorted(Comparator.comparing(YbDrgResultView::getOrderNum)).collect(Collectors.toList());

                List<YbDrgApplyDataResultYj> exportList = new ArrayList<>();
                for (YbDrgResultView item : list) {
                    YbDrgApplyDataResultYj dataExport = new YbDrgApplyDataResultYj();
                    dataExport.setJzjlh(item.getJzjlh());//就诊记录号
                    dataExport.setBah(item.getBah());//病案号
                    dataExport.setOperateReason(item.getOperateReason());

                    exportList.add(dataExport);
                }

                ExportExcelUtils.exportExcel(response, YbDrgApplyDataResultYj.class, exportList, "DRG结果意见明细数据");

            } else {
                isError = true;
                message = "未找到 " + ybDrgResultView.getApplyDateStr() + " 上传数据.";
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
    @RequiresPermissions("ybDrgResultView:view")
    public void exportResultWj(QueryRequest request, YbDrgResultView ybDrgResultView, HttpServletResponse response) throws FebsException {
        boolean isError = false;
        try {
            YbDrgApply drgApply = this.iYbDrgApplyService.findDrgApplyByApplyDateStrs(ybDrgResultView.getApplyDateStr(), ybDrgResultView.getAreaType());
            if (drgApply != null) {
                ybDrgResultView.setPid(drgApply.getId());
                ybDrgResultView.setAreaType(ybDrgResultView.getAreaType());
                List<YbDrgResultView> list = this.iYbDrgResultViewService.findDrgResultViewInnerLists(ybDrgResultView);
                list = list.stream().sorted(Comparator.comparing(YbDrgResultView::getOrderNum)).collect(Collectors.toList());
                List<YbDrgApplyDataResultWj> exportList = new ArrayList<>();
                if (list.size() > 0) {
                    List<ComFile> fileList = iComFileService.findDrgResultComFiles(drgApply.getApplyDateStr(), drgApply.getAreaType());
                    if (fileList.size() > 0) {
                        ComType typeQuery = new ComType();
                        typeQuery.setCtType(2);
                        List<ComType> typeList = iComTypeService.findComTypeList(typeQuery);
                        if (typeList.size() > 0) {
                            List<ComType> queryList = new ArrayList<>();
                            for (ComFile file : fileList) {
                                queryList = typeList.stream().filter(s -> s.getId().equals(Integer.parseInt(file.getRefType()))).collect(Collectors.toList());
                                if (queryList.size() > 0) {
                                    file.setOrderNum(queryList.get(0).getOrderNum());
                                    file.setRefTypeName(queryList.get(0).getCtName());
                                }
                            }
                        }
                    }

                    List<ComFile> queryFileList = new ArrayList<>();
                    for (YbDrgResultView item : list) {
                        queryFileList = fileList.stream().filter(s -> s.getRefTabId().equals(item.getId())).
                                sorted(Comparator.comparing(ComFile::getOrderNum)).collect(Collectors.toList());
                        for (ComFile file : queryFileList) {
                            YbDrgApplyDataResultWj dataExport = new YbDrgApplyDataResultWj();
                            dataExport.setJzjlh(item.getJzjlh());//就诊记录号
                            dataExport.setBah(item.getBah());//病案号
                            dataExport.setTypeName(file.getRefTypeName());
                            dataExport.setFileName(file.getServerName());
                            exportList.add(dataExport);
                        }
                    }
                }

                ExportExcelUtils.exportExcel(response, YbDrgApplyDataResultWj.class, exportList, "DRG结果文件明细数据");

            } else {
                isError = true;
                message = "未找到 " + ybDrgResultView.getApplyDateStr() + " 上传数据.";
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
    public FebsResponse fileDownLoadList(String applyDateStr, Integer areaType, Integer startOrderNumber, Integer endOrderNumber) {
        Map<String, Object> result = new HashMap<>();
        try {
            if (startOrderNumber > 0 && endOrderNumber >= startOrderNumber) {
                List<String> orderNumberList = new ArrayList<>();
                if (startOrderNumber == endOrderNumber) {
                    orderNumberList.add(startOrderNumber.toString());
                } else {
                    while (startOrderNumber <= endOrderNumber) {
                        orderNumberList.add(startOrderNumber.toString());
                        startOrderNumber++;
                    }
                }
                List<YbDrgResultView> list = this.iYbDrgResultViewService.findDrgResultFileSizeByOrderNumberList(applyDateStr, areaType, orderNumberList);

                if (list.size() > 0) {
                    list = list.stream().sorted(Comparator.comparing(YbDrgResultView::getOrderNum)).collect(Collectors.toList());
                }

                if (list.size() > 0) {
                    result.put("data", list);
                    result.put("success", 1);
                } else {
                    result.put("data", null);
                    result.put("error", "DRG打包下载，无数据");
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