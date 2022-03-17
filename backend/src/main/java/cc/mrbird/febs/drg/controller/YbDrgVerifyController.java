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
import cc.mrbird.febs.drg.entity.YbDrgJk;
import cc.mrbird.febs.drg.service.IYbDrgApplyDataService;
import cc.mrbird.febs.drg.service.IYbDrgVerifyService;
import cc.mrbird.febs.drg.service.IYbDrgApplyService;
import cc.mrbird.febs.drg.entity.YbDrgVerify;
import cc.mrbird.febs.drg.entity.YbDrgApplyData;

import cc.mrbird.febs.common.utils.FebsUtil;
import cc.mrbird.febs.system.domain.User;
import cc.mrbird.febs.yb.domain.ResponseImportResultData;
import cc.mrbird.febs.yb.domain.ResponseResult;
import cc.mrbird.febs.yb.entity.YbDefaultValue;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
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
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

/**
 * @author viki
 * @since 2021-11-23
 */
@Slf4j
@Validated
@RestController
@RequestMapping("ybDrgVerify")

public class YbDrgVerifyController extends BaseController {

    private String message;
    @Autowired
    IYbDrgVerifyService iYbDrgVerifyService;

    @Autowired
    IYbDrgApplyService iYbDrgApplyService;

    @Autowired
    FebsProperties febsProperties;

    @Autowired
    IYbDrgApplyDataService iYbDrgApplyDataService;


    /**
     * 分页查询数据
     *
     * @param request     分页信息
     * @param ybDrgVerify 查询条件
     * @return
     */
    @GetMapping
    @RequiresPermissions("ybDrgVerify:view")
    public Map<String, Object> List(QueryRequest request, YbDrgVerify ybDrgVerify) {
        return getDataTable(this.iYbDrgVerifyService.findYbDrgVerifys(request, ybDrgVerify));
    }

    @Log("删除")
    @DeleteMapping("deleteVerifyState")
    @RequiresPermissions("ybDrgVerify:addImport")
    public FebsResponse deleteDrgVerifys(YbDrgVerify delVerify) {
        int success = 0;
        try {
            YbDrgApply ybDrgApply = this.iYbDrgApplyService.findDrgApplyByApplyDateStrs(delVerify.getApplyDateStr(), delVerify.getAreaType());
            if (ybDrgApply != null) {
                delVerify.setState(1);
                this.iYbDrgVerifyService.deleteDrgVerifyState(delVerify);
                message = "删除成功";
                success = 1;
            } else {
                message = "未找到" + delVerify.getApplyDateStr() + "年月数据.";
            }
        } catch (Exception e) {
            message = "删除失败.";
            log.error(message, e);
        }
        ResponseResult rr = new ResponseResult();
        rr.setSuccess(success);
        rr.setMessage(message);

        return new FebsResponse().data(rr);
    }


    /**
     * 添加
     *
     * @param ybDrgVerify
     * @return
     */
    @Log("新增/按钮")
    @PostMapping
    @RequiresPermissions("ybDrgVerify:add")
    public void addYbDrgVerify(@Valid YbDrgVerify ybDrgVerify) throws FebsException {
        try {
            User currentUser = FebsUtil.getCurrentUser();
//            ybDrgVerify.setCreateUserId(currentUser.getUserId());
            this.iYbDrgVerifyService.createYbDrgVerify(ybDrgVerify);
        } catch (Exception e) {
            message = "新增/按钮失败";
            log.error(message, e);
            throw new FebsException(message);
        }
    }

    /**
     * 修改
     *
     * @param ybDrgVerify
     * @return
     */
    @Log("修改")
    @PutMapping
    @RequiresPermissions("ybDrgVerify:update")
    public void updateYbDrgVerify(@Valid YbDrgVerify ybDrgVerify) throws FebsException {
        try {
            User currentUser = FebsUtil.getCurrentUser();
//            ybDrgVerify.setModifyUserId(currentUser.getUserId());
            this.iYbDrgVerifyService.updateYbDrgVerify(ybDrgVerify);
        } catch (Exception e) {
            message = "修改失败";
            log.error(message, e);
            throw new FebsException(message);
        }
    }


    @Log("删除")
    @DeleteMapping("/{ids}")
    @RequiresPermissions("ybDrgVerify:delete")
    public void deleteYbDrgVerifys(@NotBlank(message = "{required}") @PathVariable String ids) throws FebsException {
        try {
            String[] arr_ids = ids.split(StringPool.COMMA);
            this.iYbDrgVerifyService.deleteYbDrgVerifys(arr_ids);
        } catch (Exception e) {
            message = "删除失败";
            log.error(message, e);
            throw new FebsException(message);
        }
    }

    @PostMapping("excel")
    @RequiresPermissions("ybDrgVerify:export")
    public void export(QueryRequest request, YbDrgVerify ybDrgVerify, HttpServletResponse response) throws FebsException {
        try {
            List<YbDrgVerify> ybDrgVerifys = this.iYbDrgVerifyService.findYbDrgVerifys(request, ybDrgVerify).getRecords();
            ExcelKit.$Export(YbDrgVerify.class, response).downXlsx(ybDrgVerifys, false);
        } catch (Exception e) {
            message = "导出Excel失败";
            log.error(message, e);
            throw new FebsException(message);
        }
    }

    @GetMapping("/{id}")
    public YbDrgVerify detail(@NotBlank(message = "{required}") @PathVariable String id) {
        YbDrgVerify ybDrgVerify = this.iYbDrgVerifyService.getById(id);
        return ybDrgVerify;
    }

    @PostMapping("importDrgVerify")
    @RequiresPermissions("ybDrgVerify:addImport")
    public void importDrgVerifys(String applyDate, Integer areaType) throws FebsException {
        try {
            User currentUser = FebsUtil.getCurrentUser();
            this.iYbDrgVerifyService.insertDrgVerifyImports(applyDate, areaType, currentUser.getUserId(), currentUser.getUsername());
        } catch (Exception e) {
            message = "匹配失败";
            log.error(message, e);
            throw new FebsException(message);
        }
    }

    @PostMapping("importDrgDataVerify")
    @RequiresPermissions("ybDrgVerify:addImport")
    public FebsResponse importDrgApplyData(@RequestParam MultipartFile file, @RequestParam String applyDateStr, @RequestParam Integer areaType) {
        int success = 0;
        String uploadFileName = "";
        String ssm = "";
        if (file.isEmpty()) {
            message = "空文件";
        } else {
            YbDrgApply drgApply = iYbDrgApplyService.findDrgApplyByApplyDateStrs(applyDateStr, areaType);

            if (drgApply != null) {
                LambdaQueryWrapper<YbDrgApplyData> wrapper = new LambdaQueryWrapper<>();
                wrapper.eq(YbDrgApplyData::getPid, drgApply.getId());
                List<YbDrgApplyData> applyDataList = this.iYbDrgApplyDataService.list(wrapper);

                if (applyDataList.size() > 0) {
                    uploadFileName = file.getOriginalFilename();
                    boolean blError = false;
                    try {
                        String filePath = febsProperties.getUploadPath(); // 上传后的路径
                        File getFile = FileHelpers.fileUpLoad(file, filePath, drgApply.getId(), "DrgVerifyTemp");
                        Map<Integer, String> sheetMap = ImportExcelUtils.getSheelNames(getFile);

                        if (sheetMap.size() > 0) {
                            List<Object[]> objMx = ImportExcelUtils.importExcelBySheetIndex(getFile, 0, 0, 0);
                            if (objMx.size() > 1) {
                                List<YbDrgVerify> verifyList = new ArrayList<>();
                                if (objMx.size() > 1) {
                                    if (objMx.get(0).length >= 14) {
                                        YbDrgJk queryRif = new YbDrgJk();
                                        queryRif.setApplyDateStr(applyDateStr);
                                        queryRif.setAreaType(areaType);
                                        for (int i = 1; i < objMx.size(); i++) {
                                            YbDrgVerify rv = this.getDrgVerify(objMx, i, applyDateStr, applyDataList);
                                            if (rv != null) {
                                                verifyList.add(rv);
                                            }
                                        }
                                    } else {
                                        blError = true;
                                        message = "Excel导入失败，Sheet明细扣款 列表列数不正确";
                                    }
                                }

                                if (!blError) {
                                    if (verifyList.size() > 0) {
                                        this.iYbDrgVerifyService.importDrgDataVerifys(drgApply, verifyList);
                                        success = 1;
                                        message = "Excel导入成功.";
                                    } else {
                                        message = "Excel导入失败，导入数据为空.";
                                    }
                                }
                            } else {
                                message = "Excel导入失败，请确认 " + ssm + " 列表数据是否正确.";
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
                    message = "Excel导入失败，" + applyDateStr + " 未上传数据.";
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

    private YbDrgVerify getDrgVerify(List<Object[]> obj, int i, String applyDateStr, List<YbDrgApplyData> applyDataList) {
        YbDrgVerify ybDrgVerify = null;
        List<YbDrgApplyData> queryApplyDataList = new ArrayList<>();
        String strOrderNumber = DataTypeHelpers.importTernaryOperate(obj.get(i), 0);//序号
//        String strDeptCode = "";
//        String strDeptName = "";
        String strDocCode = "";
        String strDocName = "";
        String strDksId = "";
        String strDksName = "";

//        strDeptCode = DataTypeHelpers.importTernaryOperate(obj.get(i), 10);//病区编码
//        strDeptName = DataTypeHelpers.importTernaryOperate(obj.get(i), 11);//病区名称
        strDksId = DataTypeHelpers.importTernaryOperate(obj.get(i), 10);//科室名称
        strDksName = DataTypeHelpers.importTernaryOperate(obj.get(i), 11);//科室名称
        strDocCode = DataTypeHelpers.importTernaryOperate(obj.get(i), 12);//医生编码
        strDocName = DataTypeHelpers.importTernaryOperate(obj.get(i), 13);//医生名称

        if (!strOrderNumber.equals("")) {
            queryApplyDataList = applyDataList.stream().filter(
                    s -> s.getOrderNumber().equals(strOrderNumber)
            ).collect(Collectors.toList());
            if (queryApplyDataList.size() > 0) {
                if (!strDksId.equals("") && !strDksName.equals("") &&
                        !strDocCode.equals("") && !strDocName.equals("")) {
                    YbDrgApplyData entity = queryApplyDataList.get(0);

                    ybDrgVerify = new YbDrgVerify();
                    ybDrgVerify.setId(UUID.randomUUID().toString());
                    ybDrgVerify.setApplyDataId(entity.getId());
//                    ybDrgVerify.setVerifyDeptCode(strDeptCode);
//                    ybDrgVerify.setVerifyDeptName(strDeptName);
                    ybDrgVerify.setVerifyDksId(strDksId);
                    ybDrgVerify.setVerifyDksName(strDksName);
                    ybDrgVerify.setVerifyDoctorCode(strDocCode);
                    ybDrgVerify.setVerifyDoctorName(strDocName);

                    ybDrgVerify.setApplyDateStr(applyDateStr);
                    ybDrgVerify.setOrderNumber(entity.getOrderNumber());
                    ybDrgVerify.setOrderNum(entity.getOrderNum());
                }
            }
        }
        return ybDrgVerify;
    }


    @PutMapping("updateDrgVerifyImport")
    @RequiresPermissions("ybDrgVerify:stateUpdate")
    public void updateDrgVerifyImports(String dataJson) throws FebsException {
        try {
            List<YbDrgVerify> list = JSON.parseObject(dataJson, new TypeReference<List<YbDrgVerify>>() {
            });
            this.iYbDrgVerifyService.updateDrgVerifyImports(list);
        } catch (Exception e) {
            message = "匹配失败";
            log.error(message, e);
            throw new FebsException(message);
        }
    }


    @Log("修改")
    @PutMapping("updateSendState")
    @RequiresPermissions("ybDrgVerify:stateUpdate")
    public void updateSendState(String dataJson, Integer areaType) throws FebsException {
        try {
            User currentUser = FebsUtil.getCurrentUser();
            Long uid = currentUser.getUserId();
            String uname = currentUser.getUsername();

            List<YbDrgVerify> list = JSON.parseObject(dataJson, new TypeReference<List<YbDrgVerify>>() {
            });

            this.iYbDrgVerifyService.updateSendStates(list, areaType, uid, uname);
        } catch (Exception e) {
            message = "发送失败";
            log.error(message, e);
            throw new FebsException(message);
        }
    }

    @Log("修改")
    @PutMapping("updateBackState")
    @RequiresPermissions("ybDrgVerify:stateUpdate")
    public void updateBackState(String applyDateStr, Integer areaType) throws FebsException {
        try {
            this.iYbDrgVerifyService.updateBackStates(applyDateStr, areaType, YbDefaultValue.VERIFYSTATE_2);
        } catch (Exception e) {
            message = "退回失败";
            log.error(message, e);
            throw new FebsException(message);
        }
    }

    @Log("修改")
    @PutMapping("updateASendState")
    @RequiresPermissions("ybDrgVerify:stateUpdate")
    public void updateASendState(String applyDateStr, Integer areaType, Integer state) throws FebsException {
        try {
            User currentUser = FebsUtil.getCurrentUser();
            Long uid = currentUser.getUserId();
            String uname = currentUser.getUsername();

            this.iYbDrgVerifyService.updateAllSendStates(applyDateStr, areaType, state, uid, uname);
        } catch (Exception e) {
            message = "发送失败";
            log.error(message, e);
            throw new FebsException(message);
        }
    }

    //单个，多个核对
    @Log("修改")
    @PutMapping("updateReviewerState")
    @RequiresPermissions("ybDrgVerify:stateUpdate")
    public void updateReviewerState(String dataJson) throws FebsException {
        try {
            List<YbDrgVerify> list = JSON.parseObject(dataJson, new TypeReference<List<YbDrgVerify>>() {
            });
            this.iYbDrgVerifyService.updateReviewerStates(list);
        } catch (Exception e) {
            message = "核对失败";
            log.error(message, e);
            throw new FebsException(message);
        }
    }

    @Log("修改")
    @PutMapping("updateAReviewerState")
    @RequiresPermissions("ybDrgVerify:stateUpdate")
    public void updateAReviewerState(String applyDateStr, Integer areaType, Integer state) throws FebsException {
        try {
            this.iYbDrgVerifyService.updateAllReviewerStates(applyDateStr, areaType, state);
        } catch (Exception e) {
            message = "核对审核失败";
            log.error(message, e);
            throw new FebsException(message);
        }
    }

    @Log("创建Job")
    @PutMapping("startJob")
    @RequiresPermissions("ybDrgVerify:stateUpdate")
    public FebsResponse cStartJob(String applyDateStr, Integer areaType, int[] jobTypeList) {
        int success = 0;
        try {
            String msg = this.iYbDrgVerifyService.createEndJobState(applyDateStr, areaType, jobTypeList);
//            ok,"",noApply,noType
            if (msg.equals("ok")) {
                success = 1;
            } else if (msg.equals("")) {
                message = "该" + applyDateStr + "年月已完成复议.";
            } else {
                if (msg.equals("noApply")) {
                    message = "未找到" + applyDateStr + "年月数据.";
                } else if (msg.equals("noType")) {
                    message = "未找到传入的类型.";
                } else {
                    message = msg;
                }
            }
        } catch (Exception e) {
            message = "创建Job失败";
            log.error(message, e);
        }

        ResponseResult rr = new ResponseResult();
        rr.setSuccess(success);
        rr.setMessage(message);
        return new FebsResponse().data(rr);
    }



}