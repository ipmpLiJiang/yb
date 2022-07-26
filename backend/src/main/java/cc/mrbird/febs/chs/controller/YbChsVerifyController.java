package cc.mrbird.febs.chs.controller;

import cc.mrbird.febs.chs.entity.*;
import cc.mrbird.febs.chs.service.IYbChsApplyDataService;
import cc.mrbird.febs.chs.service.IYbChsApplyService;
import cc.mrbird.febs.com.controller.DataTypeHelpers;
import cc.mrbird.febs.com.controller.FileHelpers;
import cc.mrbird.febs.com.controller.ImportExcelUtils;
import cc.mrbird.febs.common.annotation.Log;
import cc.mrbird.febs.common.controller.BaseController;
import cc.mrbird.febs.common.domain.FebsResponse;
import cc.mrbird.febs.common.domain.router.VueRouter;
import cc.mrbird.febs.common.exception.FebsException;
import cc.mrbird.febs.common.domain.QueryRequest;

import cc.mrbird.febs.chs.service.IYbChsVerifyService;

import cc.mrbird.febs.common.properties.FebsProperties;
import cc.mrbird.febs.common.utils.FebsUtil;
import cc.mrbird.febs.system.domain.User;
import cc.mrbird.febs.yb.entity.YbDefaultValue;
import com.alibaba.fastjson.TypeReference;
import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.StringPool;
import com.wuwenze.poi.ExcelKit;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.ModelMap;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import java.io.File;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @author viki
 * @since 2022-06-20
 */
@Slf4j
@Validated
@RestController
@RequestMapping("ybChsVerify")

public class YbChsVerifyController extends BaseController {

    private String message;
    @Autowired
    public IYbChsVerifyService iYbChsVerifyService;
    @Autowired
    IYbChsApplyService iYbChsApplyService;
    @Autowired
    IYbChsApplyDataService iYbChsApplyDataService;
    @Autowired
    FebsProperties febsProperties;

    /**
     * 分页查询数据
     *
     * @param request     分页信息
     * @param ybChsVerify 查询条件
     * @return
     */
    @GetMapping
    @RequiresPermissions("ybChsVerify:view")
    public Map<String, Object> List(QueryRequest request, YbChsVerify ybChsVerify) {
        return getDataTable(this.iYbChsVerifyService.findYbChsVerifys(request, ybChsVerify));
    }

    /**
     * 添加
     *
     * @param ybChsVerify
     * @return
     */
    @Log("新增/按钮")
    @PostMapping
    @RequiresPermissions("ybChsVerify:add")
    public void addYbChsVerify(@Valid YbChsVerify ybChsVerify) throws FebsException {
        try {
            User currentUser = FebsUtil.getCurrentUser();
//            ybChsVerify.setCreateUserId(currentUser.getUserId());
            this.iYbChsVerifyService.createYbChsVerify(ybChsVerify);
        } catch (Exception e) {
            message = "新增/按钮失败";
            log.error(message, e);
            throw new FebsException(message);
        }
    }

    /**
     * 修改
     *
     * @param ybChsVerify
     * @return
     */
    @Log("修改")
    @PutMapping
    @RequiresPermissions("ybChsVerify:update")
    public void updateYbChsVerify(@Valid YbChsVerify ybChsVerify) throws FebsException {
        try {
            User currentUser = FebsUtil.getCurrentUser();
//            ybChsVerify.setModifyUserId(currentUser.getUserId());
            this.iYbChsVerifyService.updateYbChsVerify(ybChsVerify);
        } catch (Exception e) {
            message = "修改失败";
            log.error(message, e);
            throw new FebsException(message);
        }
    }


    @Log("删除")
    @DeleteMapping("/{ids}")
    @RequiresPermissions("ybChsVerify:delete")
    public void deleteYbChsVerifys(@NotBlank(message = "{required}") @PathVariable String ids) throws FebsException {
        try {
            String[] arr_ids = ids.split(StringPool.COMMA);
            this.iYbChsVerifyService.deleteYbChsVerifys(arr_ids);
        } catch (Exception e) {
            message = "删除失败";
            log.error(message, e);
            throw new FebsException(message);
        }
    }

    @Log("删除")
    @DeleteMapping("deleteVerifyState")
    @RequiresPermissions("ybChsVerify:addImport")
    public FebsResponse deleteChsVerifys(YbChsVerify delVerify) {
        ModelMap map = new ModelMap();
        int success = 0;
        try {
            YbChsApply ybChsApply = this.iYbChsApplyService.findChsApplyByApplyDateStrs(delVerify.getApplyDateStr(), delVerify.getAreaType());
            if (ybChsApply != null) {
                this.iYbChsVerifyService.deleteChsVerifyState(delVerify, ybChsApply);
                message = "删除成功";
                success = 1;
            } else {
                message = "未找到" + delVerify.getApplyDateStr() + "年月数据.";
            }
        } catch (Exception e) {
            message = "删除失败.";
            log.error(message, e);
        }
        map.put("message", message);
        map.put("success", success);
        return new FebsResponse().data(map);
    }


    @PostMapping("excel")
    @RequiresPermissions("ybChsVerify:export")
    public void export(QueryRequest request, YbChsVerify ybChsVerify, HttpServletResponse response) throws FebsException {
        try {
            List<YbChsVerify> ybChsVerifys = this.iYbChsVerifyService.findYbChsVerifys(request, ybChsVerify).getRecords();
            ExcelKit.$Export(YbChsVerify.class, response).downXlsx(ybChsVerifys, false);
        } catch (Exception e) {
            message = "导出Excel失败";
            log.error(message, e);
            throw new FebsException(message);
        }
    }

    @GetMapping("/{id}")
    public YbChsVerify detail(@NotBlank(message = "{required}") @PathVariable String id) {
        YbChsVerify ybChsVerify = this.iYbChsVerifyService.getById(id);
        return ybChsVerify;
    }

    @PostMapping("importChsVerify")
    @RequiresPermissions("ybChsVerify:addImport")
    public FebsResponse importChsVerifys(String applyDate, Integer areaType) throws FebsException {
        ModelMap map = new ModelMap();
        int success = 0;
        List<YbChsVerifyMsg> backList = new ArrayList<>();
        try {
            User currentUser = FebsUtil.getCurrentUser();
            this.iYbChsVerifyService.insertChsVerifyImports(applyDate, areaType, currentUser.getUserId(), currentUser.getUsername(), backList);
            if (backList.size() > 0) {
                backList.sort(Comparator.comparing(YbChsVerifyMsg::getZymzName).thenComparing(YbChsVerifyMsg::getRuleName));
            }
            success = 1;
        } catch (Exception e) {
            message = "匹配失败";
            log.error(message, e);
            throw new FebsException(message);
        }
        map.put("message", message);
        map.put("success", success);
        map.put("data", backList);
        return new FebsResponse().data(map);
    }

    @Log("修改")
    @PutMapping("updateSendState")
    @RequiresPermissions("ybChsVerify:stateUpdate")
    public void updateSendState(String dataJson, Integer areaType) throws FebsException {
        try {
            User currentUser = FebsUtil.getCurrentUser();
            Long uid = currentUser.getUserId();
            String uname = currentUser.getUsername();

            List<YbChsVerify> list = JSON.parseObject(dataJson, new TypeReference<List<YbChsVerify>>() {
            });

            this.iYbChsVerifyService.updateSendStates(list, areaType, uid, uname);
        } catch (Exception e) {
            message = "发送失败";
            log.error(message, e);
            throw new FebsException(message);
        }
    }

    @Log("修改")
    @PutMapping("updateBackState")
    @RequiresPermissions("ybChsVerify:stateUpdate")
    public void updateBackState(String applyDateStr, Integer areaType) throws FebsException {
        try {
            this.iYbChsVerifyService.updateBackStates(applyDateStr, areaType, YbDefaultValue.VERIFYSTATE_2);
        } catch (Exception e) {
            message = "退回失败";
            log.error(message, e);
            throw new FebsException(message);
        }
    }

    @Log("修改")
    @PutMapping("updateASendState")
    @RequiresPermissions("ybChsVerify:stateUpdate")
    public void updateASendState(String applyDateStr, Integer areaType, Integer state) throws FebsException {
        try {
            User currentUser = FebsUtil.getCurrentUser();
            Long uid = currentUser.getUserId();
            String uname = currentUser.getUsername();

            this.iYbChsVerifyService.updateAllSendStates(applyDateStr, areaType, state, uid, uname);
        } catch (Exception e) {
            message = "发送失败";
            log.error(message, e);
            throw new FebsException(message);
        }
    }


    //单个，多个核对
    @Log("修改")
    @PutMapping("updateReviewerState")
    @RequiresPermissions("ybChsVerify:stateUpdate")
    public void updateReviewerState(String dataJson) throws FebsException {
        try {
            List<YbChsVerify> list = JSON.parseObject(dataJson, new TypeReference<List<YbChsVerify>>() {
            });
            this.iYbChsVerifyService.updateReviewerStates(list);
        } catch (Exception e) {
            message = "核对失败";
            log.error(message, e);
            throw new FebsException(message);
        }
    }

    @Log("修改")
    @PutMapping("updateAReviewerState")
    @RequiresPermissions("ybChsVerify:stateUpdate")
    public void updateAReviewerState(String applyDateStr, Integer areaType, Integer state) throws FebsException {
        try {
            this.iYbChsVerifyService.updateAllReviewerStates(applyDateStr, areaType, state);
        } catch (Exception e) {
            message = "核对审核失败";
            log.error(message, e);
            throw new FebsException(message);
        }
    }

    @PutMapping("updateChsVerifyImport")
    @RequiresPermissions("ybChsVerify:stateUpdate")
    public void updateChsVerifyImports(String dataJson) throws FebsException {
        try {
            List<YbChsVerify> list = JSON.parseObject(dataJson, new TypeReference<List<YbChsVerify>>() {
            });
            this.iYbChsVerifyService.updateChsVerifyImports(list);
        } catch (Exception e) {
            message = "更新失败";
            log.error(message, e);
            throw new FebsException(message);
        }
    }

    @PostMapping("importChsDataVerify")
    @RequiresPermissions("ybChsVerify:addImport")
    public FebsResponse importChsApplyData(@RequestParam MultipartFile file, @RequestParam String applyDateStr, @RequestParam Integer areaType) {
        ModelMap map = new ModelMap();
        int success = 0;
        String uploadFileName = "";
        String ssm = "";
        if (file.isEmpty()) {
            message = "空文件";
        } else {
            YbChsApply chsApply = iYbChsApplyService.findChsApplyByApplyDateStrs(applyDateStr, areaType);

            if (chsApply != null) {
                LambdaQueryWrapper<YbChsApplyData> wrapper = new LambdaQueryWrapper<>();
                wrapper.eq(YbChsApplyData::getPid, chsApply.getId());
                List<YbChsApplyData> applyDataList = this.iYbChsApplyDataService.list(wrapper);

                if (applyDataList.size() > 0) {
                    uploadFileName = file.getOriginalFilename();
                    boolean blError = false;
                    try {
                        String filePath = febsProperties.getUploadPath(); // 上传后的路径
                        File getFile = FileHelpers.fileUpLoad(file, filePath, chsApply.getId(), "chsVerifyTemp");
                        Map<Integer, String> sheetMap = ImportExcelUtils.getSheelNames(getFile);

                        if (sheetMap.size() > 0) {
                            List<Object[]> objMx = ImportExcelUtils.importExcelBySheetIndex(getFile, 0, 0, 0);
                            if (objMx.size() > 1) {
                                List<YbChsVerify> verifyList = new ArrayList<>();
                                if (objMx.get(0).length >= 36) {
                                    for (int i = 1; i < objMx.size(); i++) {
                                        YbChsVerify rv = this.getChsVerify(objMx, i, applyDateStr, applyDataList);
                                        if (rv != null) {
                                            verifyList.add(rv);
                                        }
                                    }
                                } else {
                                    blError = true;
                                    message = "Excel导入失败，Sheet明细扣款 列表列数不正确";
                                }

                                if (!blError) {
                                    if (verifyList.size() > 0) {
                                        this.iYbChsVerifyService.importChsDataVerifys(chsApply, verifyList);
                                        success = 1;
                                        message = "Excel导入成功.";
                                    } else {
                                        message = "Excel导入失败，导入数据为空.";
                                    }
                                }
                            } else {
                                message = "Excel导入失败，请确认列表数据是否正确.";
                            }
                        } else {
                            message = "Excel导入失败,需确保存在1个Sheet.";
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

        map.put("message", message);
        map.put("success", success);
        map.put("fileName", uploadFileName);
        return new FebsResponse().data(map);
    }

    private YbChsVerify getChsVerify(List<Object[]> obj, int i, String applyDateStr, List<YbChsApplyData> applyDataList) {
        YbChsVerify ybChsVerify = null;
        List<YbChsApplyData> queryApplyDataList = new ArrayList<>();
        String strOrderNumber = DataTypeHelpers.importTernaryOperate(obj.get(i), 0);//序号

        String strDksId = DataTypeHelpers.importTernaryOperate(obj.get(i), 33);//科室名称
        String strDksName = DataTypeHelpers.importTernaryOperate(obj.get(i), 34);//科室名称
        String strDocCode = DataTypeHelpers.importTernaryOperate(obj.get(i), 35);//医生编码
        String strDocName = DataTypeHelpers.importTernaryOperate(obj.get(i), 36);//医生名称

        if (StringUtils.isNotBlank(strOrderNumber)) {
            int orderNum = Integer.parseInt(strOrderNumber);
            queryApplyDataList = applyDataList.stream().filter(
                    s -> s.getOrderNum() == orderNum
            ).collect(Collectors.toList());

            if (queryApplyDataList.size() > 0) {
                if (StringUtils.isNotBlank(strDksId) && StringUtils.isNotBlank(strDksName) &&
                        StringUtils.isNotBlank(strDocCode) && StringUtils.isNotBlank(strDocName)) {
                    YbChsApplyData entity = queryApplyDataList.get(0);

                    ybChsVerify = new YbChsVerify();
                    ybChsVerify.setId(UUID.randomUUID().toString());
                    ybChsVerify.setApplyDataId(entity.getId());
                    ybChsVerify.setVerifyDksId(strDksId);
                    ybChsVerify.setVerifyDksName(strDksName);
                    ybChsVerify.setVerifyDoctorCode(strDocCode);
                    ybChsVerify.setVerifyDoctorName(strDocName);

                    ybChsVerify.setApplyDateStr(applyDateStr);
                    ybChsVerify.setOrderNum(entity.getOrderNum());
                }
            }
        }
        return ybChsVerify;
    }

    @Log("创建Job")
    @PutMapping("startJob")
    @RequiresPermissions("ybChsVerify:stateUpdate")
    public FebsResponse cStartJob(String applyDateStr, Integer areaType, int[] jobTypeList) {
        ModelMap map = new ModelMap();
        int success = 0;
        try {
            String msg = this.iYbChsVerifyService.createEndJobState(applyDateStr, areaType, jobTypeList);
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

        map.put("message", message);
        map.put("success", success);
        return new FebsResponse().data(map);
    }


}