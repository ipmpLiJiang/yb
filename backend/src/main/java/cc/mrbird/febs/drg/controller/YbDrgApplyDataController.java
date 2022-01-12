package cc.mrbird.febs.drg.controller;

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
import cc.mrbird.febs.drg.entity.YbDrgApply;
import cc.mrbird.febs.drg.entity.YbDrgApplyDataExport;
import cc.mrbird.febs.drg.service.IYbDrgApplyDataService;
import cc.mrbird.febs.drg.entity.YbDrgApplyData;

import cc.mrbird.febs.common.utils.FebsUtil;
import cc.mrbird.febs.drg.service.IYbDrgApplyService;
import cc.mrbird.febs.export.excel.ExportExcelUtils;
import cc.mrbird.febs.system.domain.User;
import cc.mrbird.febs.yb.domain.ResponseImportResultData;
import cc.mrbird.febs.yb.domain.ResponseResult;
import cc.mrbird.febs.yb.entity.YbDefaultValue;
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

/**
 * @author viki
 * @since 2021-11-23
 */
@Slf4j
@Validated
@RestController
@RequestMapping("ybDrgApplyData")

public class YbDrgApplyDataController extends BaseController {

    private String message;
    @Autowired
    public IYbDrgApplyDataService iYbDrgApplyDataService;

    @Autowired
    public IYbDrgApplyService iYbDrgApplyService;

    @Autowired
    FebsProperties febsProperties;


    /**
     * 分页查询数据
     *
     * @param request        分页信息
     * @param ybDrgApplyData 查询条件
     * @return
     */
    @GetMapping
    @RequiresPermissions("ybDrgApplyData:view")
    public Map<String, Object> List(QueryRequest request, YbDrgApplyData ybDrgApplyData) {
        return getDataTable(this.iYbDrgApplyDataService.findYbDrgApplyDatas(request, ybDrgApplyData));
    }

    @GetMapping("findDrgApplyDataList")
    public Map<String, Object> findDrgApplyDatas(QueryRequest request, YbDrgApplyData ybDrgApplyData) {
        return getDataTable(this.iYbDrgApplyDataService.findYbDrgApplyDataList(request, ybDrgApplyData));
    }

    /**
     * 添加
     *
     * @param ybDrgApplyData
     * @return
     */
    @Log("新增/按钮")
    @PostMapping
    @RequiresPermissions("ybDrgApplyData:add")
    public void addYbDrgApplyData(@Valid YbDrgApplyData ybDrgApplyData) throws FebsException {
        try {
            User currentUser = FebsUtil.getCurrentUser();
//        ybDrgApplyData.setCreateUserId(currentUser.getUserId());
            this.iYbDrgApplyDataService.createYbDrgApplyData(ybDrgApplyData);
        } catch (Exception e) {
            message = "新增/按钮失败";
            log.error(message, e);
            throw new FebsException(message);
        }
    }

    /**
     * 修改
     *
     * @param ybDrgApplyData
     * @return
     */
    @Log("修改")
    @PutMapping
    @RequiresPermissions("ybDrgApplyData:update")
    public void updateYbDrgApplyData(@Valid YbDrgApplyData ybDrgApplyData) throws FebsException {
        try {
            User currentUser = FebsUtil.getCurrentUser();
//            ybDrgApplyData.setModifyUserId(currentUser.getUserId());
            this.iYbDrgApplyDataService.updateYbDrgApplyData(ybDrgApplyData);
        } catch (Exception e) {
            message = "修改失败";
            log.error(message, e);
            throw new FebsException(message);
        }
    }


    @Log("删除")
    @DeleteMapping("/{ids}")
    @RequiresPermissions("ybDrgApplyData:delete")
    public void deleteYbDrgApplyDatas(@NotBlank(message = "{required}") @PathVariable String ids) throws FebsException {
        try {
            String[] arr_ids = ids.split(StringPool.COMMA);
            this.iYbDrgApplyDataService.deleteYbDrgApplyDatas(arr_ids);
        } catch (Exception e) {
            message = "删除失败";
            log.error(message, e);
            throw new FebsException(message);
        }
    }

    @Log("删除")
    @DeleteMapping("deleteData")
    @RequiresPermissions("ybDrgApplyData:delete")
    public FebsResponse deleteDrgApplyByPids(@Valid YbDrgApplyData ybDrgApplyData) {
        int success = 0;
        try {
            int count = this.iYbDrgApplyDataService.deleteDrgApplyDataByPid(ybDrgApplyData);
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

    @PostMapping("getJk")
    @RequiresPermissions("ybDrgApplyData:add")
    public FebsResponse getJk(String applyDateStr,Integer areaType) {
        int success = 0;
        try {
            message= this.iYbDrgApplyDataService.getDrgJk(applyDateStr,areaType);
            if(message.equals("ok")){
                success = 1;
            } else {
                message = message.equals("") ? "未获取到DRG数据." : message;
            }
        } catch (Exception e) {
            message = "获取DRG数据失败.";
            log.error(message, e);
        }

        ResponseResult rr = new ResponseResult();
        rr.setSuccess(success);
        rr.setMessage(message);
        return new FebsResponse().data(rr);
    }

    @PostMapping("downFile")
    public void export1(HttpServletResponse response) throws FebsException {
        try {
            ExportExcelUtils.exportTemplateFile(response, YbDrgApplyDataExport.class, "Sheel1");
        } catch (Exception e) {
            message = "导出Excel模板失败";
            log.error(message, e);
            throw new FebsException(message);
        }
    }

    @PostMapping("importDrgApplyData")
    @RequiresPermissions("ybDrgApplyData:add")
    public FebsResponse importDrgApplyData(@RequestParam MultipartFile file, @RequestParam String pid) {
        int success = 0;
        String uploadFileName = "";
        if (file.isEmpty()) {
            message = "空文件";
        } else {
            YbDrgApply queryDrgApply = new YbDrgApply();
            queryDrgApply.setId(pid);
            List<YbDrgApply> list = iYbDrgApplyService.findDrgApplyList(queryDrgApply);
            queryDrgApply = list.size() > 0 ? list.get(0) : null;

            if (queryDrgApply != null) {
                int state = queryDrgApply.getState();
                if (state == YbDefaultValue.DRGAPPLYSTATE_1 || state == YbDefaultValue.DRGAPPLYSTATE_2) {
                    uploadFileName = file.getOriginalFilename();
                    boolean blError = false;
                    try {
                        String filePath = febsProperties.getUploadPath(); // 上传后的路径
                        File getFile = FileHelpers.fileUpLoad(file, filePath, pid, "DrgApplyTemp");
                        Map<Integer, String> sheetMap = ImportExcelUtils.getSheelNames(getFile);
                        if (sheetMap.size() > 0) {
                            List<Object[]> objMx = new ArrayList<Object[]>();
                            for (Integer key : sheetMap.keySet()) {
                                String value = sheetMap.get(key);
                                objMx = ImportExcelUtils.importExcelBySheetIndex(getFile, key, 0, 0);
                                break;
                            }
                            if (objMx.size() > 1) {
                                List<YbDrgApplyData> ListData = new ArrayList<>();
                                String guid = pid;

                                if (objMx.size() > 1) {
                                    if (objMx.get(0).length >= 10) {
                                        for (int i = 1; i < objMx.size(); i++) {
                                            message = "上传数据读取失败，请确保Excel列表数据正确无误.";
                                            YbDrgApplyData rrData = new YbDrgApplyData();
                                            rrData.setId(UUID.randomUUID().toString());
                                            rrData.setPid(guid);
                                            String strOrderNumber = DataTypeHelpers.importTernaryOperate(objMx.get(i), 0);//序号
                                            rrData.setOrderNumber(strOrderNumber);
                                            rrData.setOrderNum(i);
                                            String strKs = DataTypeHelpers.importTernaryOperate(objMx.get(i), 1);//科室
                                            rrData.setKs(strKs);
                                            String strJzjlh = DataTypeHelpers.importTernaryOperate(objMx.get(i), 2);//就诊记录号
                                            rrData.setJzjlh(strJzjlh);
                                            String strBah = DataTypeHelpers.importTernaryOperate(objMx.get(i), 3);//病案号
                                            rrData.setBah(strBah);
                                            String strWglx = DataTypeHelpers.importTernaryOperate(objMx.get(i), 4);//违规类型
                                            rrData.setWglx(strWglx);
                                            String strWtms = DataTypeHelpers.importTernaryOperate(objMx.get(i), 5);//问题描述
                                            rrData.setWtms(strWtms);

                                            String strYlzfy = DataTypeHelpers.importTernaryOperate(objMx.get(i), 6);//医疗总费用
                                            BigDecimal bd = new BigDecimal(0);
                                            if (DataTypeHelpers.isNumeric(strYlzfy)) {
                                                bd = new BigDecimal(strYlzfy);
                                                rrData.setYlzfy(bd);
                                            }

                                            String strWgje = DataTypeHelpers.importTernaryOperate(objMx.get(i), 7);//违规金额
                                            bd = new BigDecimal(0);
                                            if (DataTypeHelpers.isNumeric(strWgje)) {
                                                bd = new BigDecimal(strWgje);
                                                rrData.setWgje(bd);
                                            }
                                            String strSfbmzczjcw = DataTypeHelpers.importTernaryOperate(objMx.get(i), 8);//是否编码造成直接错误
                                            rrData.setSfbmzczjcw(strSfbmzczjcw);

                                            String strLy = DataTypeHelpers.importTernaryOperate(objMx.get(i), 9);//理由
                                            rrData.setLy(strLy);
                                            ListData.add(rrData);
                                        }
                                    } else {
                                        blError = true;
                                        message = "Excel导入失败，Sheet明细扣款 列表列数不正确";
                                    }
                                }
                                if (!blError) {
                                    if (ListData.size() > 0) {
                                        YbDrgApply ybDrgApply = new YbDrgApply();
                                        ybDrgApply.setState(YbDefaultValue.DRGAPPLYSTATE_2);
                                        ybDrgApply.setId(pid);
                                        ybDrgApply.setUploadFileName(uploadFileName);

                                        this.iYbDrgApplyDataService.importDrgApply(ybDrgApply, ListData);
                                        success = 1;
                                        message = "Excel导入成功.";
                                    } else {
                                        message = "Excel导入失败，导入数据为空.";
                                    }
                                }
                            } else {
                                message = "Excel导入失败，需确保存在两个Sheet 明细扣款、主单扣款,并确认列表数据是否正确.";
                            }
                        } else {
                            message = "Excel导入失败，Sheet个数不正确,需确保存在1个Sheet.";
                        }
                    } catch (Exception ex) {
                        //message = ex.getMessage();
                        if ("".equals(message)) {
                            message = "Excel导入失败.";
                        }
                        log.error(message, ex);
                    }
                } else {
                    message = "Excel导入失败.";
                }
            } else {
                message = "Excel导入失败，当前状态无法上传，待流程完成后再进行上传.";
            }
        }

        ResponseImportResultData rrd = new ResponseImportResultData();
        rrd.setSuccess(success);
        rrd.setMessage(message);

        rrd.setFileName(uploadFileName);
        return new FebsResponse().data(rrd);
    }


    @PostMapping("excel")
    @RequiresPermissions("ybDrgApplyData:export")
    public void export(QueryRequest request, YbDrgApplyData ybDrgApplyData, HttpServletResponse response) throws FebsException {
        try {
            List<YbDrgApplyData> ybDrgApplyDatas = this.iYbDrgApplyDataService.findYbDrgApplyDatas(request, ybDrgApplyData).getRecords();
            ExcelKit.$Export(YbDrgApplyData.class, response).downXlsx(ybDrgApplyDatas, false);
        } catch (Exception e) {
            message = "导出Excel失败";
            log.error(message, e);
            throw new FebsException(message);
        }
    }

    @GetMapping("/{id}")
    public YbDrgApplyData detail(@NotBlank(message = "{required}") @PathVariable String id) {
        YbDrgApplyData ybDrgApplyData = this.iYbDrgApplyDataService.getById(id);
        return ybDrgApplyData;
    }
}