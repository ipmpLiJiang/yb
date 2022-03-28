package cc.mrbird.febs.drg.controller;

import cc.mrbird.febs.com.controller.DataTypeHelpers;
import cc.mrbird.febs.common.annotation.Log;
import cc.mrbird.febs.common.controller.BaseController;
import cc.mrbird.febs.common.domain.QueryRequest;
import cc.mrbird.febs.common.exception.FebsException;
import cc.mrbird.febs.common.properties.FebsProperties;
import cc.mrbird.febs.common.utils.FebsUtil;
import cc.mrbird.febs.drg.entity.YbDrgApply;
import cc.mrbird.febs.drg.entity.YbDrgApplyDataVerify;
import cc.mrbird.febs.drg.entity.YbDrgJk;
import cc.mrbird.febs.drg.entity.YbDrgVerifyView;
import cc.mrbird.febs.drg.service.IYbDrgApplyService;
import cc.mrbird.febs.drg.service.IYbDrgJkService;
import cc.mrbird.febs.drg.service.IYbDrgVerifyViewService;
import cc.mrbird.febs.export.excel.ExportExcelUtils;
import cc.mrbird.febs.system.domain.User;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.StringPool;
import com.wuwenze.poi.ExcelKit;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
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
@RequestMapping("ybDrgVerifyView")

public class YbDrgVerifyViewController extends BaseController {

    private String message;
    @Autowired
    public IYbDrgVerifyViewService iYbDrgVerifyViewService;

    @Autowired
    private FebsProperties febsProperties;

    @Autowired
    public IYbDrgApplyService iYbDrgApplyService;

    @Autowired
    public IYbDrgJkService iYbDrgJkService;

    /**
     * 分页查询数据
     *
     * @param request         分页信息
     * @param ybDrgVerifyView 查询条件
     * @return
     */
    @GetMapping
    @RequiresPermissions("ybDrgVerifyView:view")
    public Map<String, Object> List(QueryRequest request, YbDrgVerifyView ybDrgVerifyView, String[] searchType) {
        return getDataTable(this.iYbDrgVerifyViewService.findYbDrgVerifyViews(request, ybDrgVerifyView));

    }

    @PostMapping("excel")
    @RequiresPermissions("ybDrgVerifyView:export")
    public void export(QueryRequest request, YbDrgVerifyView ybDrgVerifyView, HttpServletResponse response) throws FebsException {
        try {
            List<YbDrgVerifyView> ybDrgVerifyViews = this.iYbDrgVerifyViewService.findYbDrgVerifyViews(request, ybDrgVerifyView).getRecords();
            ExcelKit.$Export(YbDrgVerifyView.class, response).downXlsx(ybDrgVerifyViews, false);
        } catch (Exception e) {
            message = "导出Excel失败";
            log.error(message, e);
            throw new FebsException(message);
        }
    }

    @PostMapping("exportVerify")
    @RequiresPermissions("ybDrgVerifyView:nullview")
    public void exportVerify(QueryRequest request, YbDrgVerifyView ybDrgVerifyView, HttpServletResponse response) throws FebsException {
        boolean isError = false;
        try {
            YbDrgApply drgApply = this.iYbDrgApplyService.findDrgApplyByApplyDateStrs(ybDrgVerifyView.getApplyDateStr(), ybDrgVerifyView.getAreaType());
            if (drgApply != null) {
                ybDrgVerifyView.setPid(drgApply.getId());
                ybDrgVerifyView.setAreaType(ybDrgVerifyView.getAreaType());
                List<YbDrgVerifyView> list = this.iYbDrgVerifyViewService.findDrgVerifyViewLists(ybDrgVerifyView);
                list = list.stream().sorted(Comparator.comparing(YbDrgVerifyView::getOrderNum)).collect(Collectors.toList());

                List<YbDrgJk> jkList = new ArrayList<>();

                if (list.size() > 0) {
                    LambdaQueryWrapper<YbDrgJk> wrapper = new LambdaQueryWrapper<>();
                    wrapper.eq(YbDrgJk::getApplyDateStr, drgApply.getApplyDateStr());
                    wrapper.eq(YbDrgJk::getAreaType, drgApply.getAreaType());
                    jkList = iYbDrgJkService.list(wrapper);
                }
                List<YbDrgApplyDataVerify> exportList = new ArrayList<>();
                List<YbDrgJk> queryJkList = new ArrayList<>();
                SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
                for (YbDrgVerifyView item : list) {
                    YbDrgApplyDataVerify dataExport = new YbDrgApplyDataVerify();
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

                    String strVerifyDksName = DataTypeHelpers.stringReplaceSetString(item.getVerifyDksName(), item.getVerifyDksId() + "-");
                    String strVerifyDoctorName = DataTypeHelpers.stringReplaceSetString(item.getVerifyDoctorName(), item.getVerifyDoctorCode() + "-");

                    dataExport.setVerifyDoctorCode(item.getVerifyDoctorCode());//复议医生编码
                    dataExport.setVerifyDoctorName(strVerifyDoctorName);//复议医生姓名
//                    dataExport.setVerifyDeptCode(item.getVerifyDeptCode());//病区编码
//                    dataExport.setVerifyDeptName(strVerifyDeptName);//病区名称
                    dataExport.setVerifyDksId(item.getVerifyDksId());//科室名称
                    dataExport.setVerifyDksName(strVerifyDksName);//科室名称

                    queryJkList = jkList.stream().filter(s -> s.getApplyDataId().equals(item.getApplyDataId())).collect(Collectors.toList());
                    if (queryJkList.size() > 0) {
                        YbDrgJk jk = queryJkList.get(0);
                        dataExport.setRyDateStr(formatter.format(jk.getRyDate()));
                        dataExport.setCyDateStr(formatter.format(jk.getCyDate()));
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
                        dataExport.setYq(jk.getYq());
                        dataExport.setDeptName(jk.getDeptId()+ "-" +jk.getDeptName());
                        dataExport.setAreaName(jk.getAreaId()+ "-" +jk.getAreaName());
                        dataExport.setQz(jk.getQz());
                        if (StringUtils.isNotBlank(jk.getKzrDocId())) {
                            dataExport.setKzrDocName(jk.getKzrDocId() + "-" + jk.getKzrDocName());
                        } else if (StringUtils.isNotBlank(jk.getKzrDocName())) {
                            dataExport.setKzrDocName(jk.getKzrDocName());
                        }
                        if (StringUtils.isNotBlank(jk.getZrysDocId())) {
                            dataExport.setZrysDocName(jk.getZrysDocId() + "-" + jk.getZrysDocName());
                        } else if(StringUtils.isNotBlank(jk.getZrysDocName())){
                            dataExport.setZrysDocName(jk.getZrysDocName());
                        }
                        if (StringUtils.isNotBlank(jk.getZzysDocId())) {
                            dataExport.setZzysDocName(jk.getZzysDocId() + "-" + jk.getZzysDocName());
                        } else if(StringUtils.isNotBlank(jk.getZzysDocName())){
                            dataExport.setZzysDocName(jk.getZzysDocName());
                        }
                        if (StringUtils.isNotBlank(jk.getZyysDocId())) {
                            if(StringUtils.isNotBlank(jk.getZyysDocName()) && jk.getZyysDocId().equals(jk.getZyysDocName())){
                                dataExport.setZyysDocName(jk.getZyysDocName());
                            } else {
                                dataExport.setZyysDocName(jk.getZyysDocId() + "-" + jk.getZyysDocName());
                            }
                        } else if(StringUtils.isNotBlank(jk.getZyysDocName())){
                            dataExport.setZyysDocName(jk.getZyysDocName());
                        }
                        if (StringUtils.isNotBlank(jk.getYlzDeptId())) {
                            dataExport.setYlzDeptName(jk.getYlzDeptId()+ "-" +jk.getYlzDeptName());
                        } else if(StringUtils.isNotBlank(jk.getYlzDeptName())){
                            dataExport.setYlzDeptName(jk.getYlzDeptName());
                        }
                        if (StringUtils.isNotBlank(jk.getYlzDocId())) {
                            dataExport.setYlzDocName(jk.getYlzDocId() + "-" + jk.getYlzDocName());
                        } else if(StringUtils.isNotBlank(jk.getYlzDocName())){
                            dataExport.setYlzDocName(jk.getYlzDocName());
                        }
                    }

                    exportList.add(dataExport);
                }
                ExportExcelUtils.exportExcel(response, YbDrgApplyDataVerify.class, exportList, "DRG核对明细数据");

            } else {
                isError = true;
                message = "未找到 " + ybDrgVerifyView.getApplyDateStr() + " 上传数据.";
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
    public YbDrgVerifyView detail(@NotBlank(message = "{required}") @PathVariable String id) {
        YbDrgVerifyView ybDrgVerifyView = this.iYbDrgVerifyViewService.getById(id);
        return ybDrgVerifyView;
    }
}