package cc.mrbird.febs.yb.controller;

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
import cc.mrbird.febs.export.excel.ExportExcelUtils;
import cc.mrbird.febs.yb.domain.ResponseImportResultData;
import cc.mrbird.febs.yb.entity.*;
import cc.mrbird.febs.yb.service.IYbReconsiderApplyService;
import cc.mrbird.febs.yb.service.IYbReconsiderVerifyService;

import cc.mrbird.febs.common.utils.FebsUtil;
import cc.mrbird.febs.system.domain.User;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
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
@RequestMapping("ybReconsiderVerify")

public class YbReconsiderVerifyController extends BaseController {

    private String message;
    @Autowired
    public IYbReconsiderVerifyService iYbReconsiderVerifyService;

    @Autowired
    public IYbReconsiderApplyService iYbReconsiderApplyService;

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
            ybReconsiderVerify.setCreateUserId(currentUser.getUserId());
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
            ybReconsiderVerify.setModifyUserId(currentUser.getUserId());
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

    @PostMapping("importReconsiderVerify")
    @RequiresPermissions("ybReconsiderVerify:addImport")
    public void importReconsiderVerifys(String applyDate) throws FebsException {
        try {
            User currentUser = FebsUtil.getCurrentUser();
            this.iYbReconsiderVerifyService.insertReconsiderVerifyImports(applyDate, currentUser.getUserId(), currentUser.getUsername());
        } catch (Exception e) {
            message = "匹配失败";
            log.error(message, e);
            throw new FebsException(message);
        }
    }

    @PostMapping("importMainReconsiderVerify")
    @RequiresPermissions("ybReconsiderVerify:addImport")
    public void importMainReconsiderVerifys(String applyDate) throws FebsException {
        try {
            User currentUser = FebsUtil.getCurrentUser();
            this.iYbReconsiderVerifyService.insertMainReconsiderVerifyImports(applyDate, currentUser.getUserId(), currentUser.getUsername());
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
            Long uid = currentUser.getUserId();
            String uname = currentUser.getUsername();

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
    public void updateSendState(String dataJson, Integer dataType) throws FebsException {
        try {
            User currentUser = FebsUtil.getCurrentUser();
            Long uid = currentUser.getUserId();
            String uname = currentUser.getUsername();

            List<YbReconsiderVerify> list = JSON.parseObject(dataJson, new TypeReference<List<YbReconsiderVerify>>() {
            });

            this.iYbReconsiderVerifyService.updateSendStates(list, dataType, uid, uname);
        } catch (Exception e) {
            message = "发送失败";
            log.error(message, e);
            throw new FebsException(message);
        }
    }

    @Log("修改")
    @PutMapping("updateASendState")
    @RequiresPermissions("ybReconsiderVerify:stateUpdate")
    public void updateASendState(String applyDateStr, Integer state, Integer dataType) throws FebsException {
        try {
            User currentUser = FebsUtil.getCurrentUser();
            Long uid = currentUser.getUserId();
            String uname = currentUser.getUsername();

            this.iYbReconsiderVerifyService.updateAllSendStates(applyDateStr, state, dataType, uid, uname);
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


    @PostMapping("importReconsiderDataVerify")
    @RequiresPermissions("ybReconsiderVerify:add")
    public FebsResponse importReconsiderApplyData(@RequestParam MultipartFile file, @RequestParam String applyDateStr, @RequestParam Integer dataType) {
        int success = 0;
        String uploadFileName = "";
        String ssm = "";
        if (file.isEmpty()) {
            message = "空文件";
        } else {
            YbReconsiderApply queryReconsiderApply = new YbReconsiderApply();
            queryReconsiderApply.setApplyDateStr(applyDateStr);
            List<YbReconsiderApply> list = iYbReconsiderApplyService.findReconsiderApplyList(queryReconsiderApply);
            queryReconsiderApply = list.size() > 0 ? list.get(0) : null;

            if (queryReconsiderApply != null) {
                int state = queryReconsiderApply.getState();
                int typeno = state == YbDefaultValue.APPLYSTATE_2 || state == YbDefaultValue.APPLYSTATE_3 ? 1 :
                        state == YbDefaultValue.APPLYSTATE_4 || state == YbDefaultValue.APPLYSTATE_5 ? 2 : 0;

                if (typeno == YbDefaultValue.DATATYPE_0 || typeno == YbDefaultValue.DATATYPE_1) {
                    uploadFileName = file.getOriginalFilename();
                    boolean blError = false;
                    try {
                        String filePath = febsProperties.getUploadPath(); // 上传后的路径
                        File getFile = FileHelpers.fileUpLoad(file, filePath, queryReconsiderApply.getId(), "ReconsiderVerifyTemp");
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
                                    List<YbReconsiderApplyDataVerify> ListData = new ArrayList<>();
                                    List<YbReconsiderApplyDataVerify> ListMain = new ArrayList<>();

                                    if (objMx.size() > 1) {
                                        if (objMx.get(0).length >= 31) {
                                            for (int i = 1; i < objMx.size(); i++) {
                                                YbReconsiderApplyDataVerify rrData = new YbReconsiderApplyDataVerify();
                                                ListData.add(rrData);
                                            }
                                        } else {
                                            blError = true;
                                            message = "Excel导入失败，Sheet明细扣款 列表列数不正确";
                                        }
                                    }
                                    if (objZd.size() > 1) {
                                        if (objZd.get(0).length >= 22) {
                                            for (int i = 1; i < objZd.size(); i++) {
                                                YbReconsiderApplyDataVerify rrMain = new YbReconsiderApplyDataVerify();

                                                ListMain.add(rrMain);
                                            }
                                        } else {
                                            blError = true;
                                            message = "Excel导入失败，Sheet主单扣款 列表列数不正确";
                                        }
                                    }

                                    if (!blError) {
//                                        if (ListData.size() > 0 || ListMain.size() > 0) {
//                                            //1待复议 2上传一 3申述一 4上传二 5申述二 6已剔除 7已还款
//                                            YbReconsiderApply ybReconsiderApply = new YbReconsiderApply();
//                                            if (typeno == 1) {
//                                                ybReconsiderApply.setState(YbDefaultValue.APPLYSTATE_2);//State=2 审核一
//                                                ybReconsiderApply.setUploadFileNameOne(uploadFileName);
//                                            }
//                                            if (typeno == 2) {
//                                                ybReconsiderApply.setState(YbDefaultValue.APPLYSTATE_4);
//                                                ybReconsiderApply.setUploadFileNameTwo(uploadFileName);
//                                            }
//                                            ybReconsiderApply.setId(pid);
//
//                                            this.iYbReconsiderApplyDataService.importReconsiderApply(ybReconsiderApply, ListData, ListMain);
//                                            success = 1;
//                                            message = "Excel导入成功.";
//                                        } else {
//                                            message = "Excel导入失败，导入数据为空.";
//                                        }
                                    }
                                } else {
                                    message = "Excel导入失败，请确认 "+ssm+" 列表数据是否正确.";
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

}