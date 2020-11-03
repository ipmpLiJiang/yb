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
import cc.mrbird.febs.yb.domain.ResponseResult;
import cc.mrbird.febs.yb.domain.ResponseResultData;
import cc.mrbird.febs.yb.entity.*;
import cc.mrbird.febs.yb.service.IYbAppealResultDeductimplementService;

import cc.mrbird.febs.common.utils.FebsUtil;
import cc.mrbird.febs.system.domain.User;
import cc.mrbird.febs.yb.service.IYbReconsiderApplyService;
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
 * @since 2020-09-24
 */
@Slf4j
@Validated
@RestController
@RequestMapping("ybAppealResultDeductimplement")

public class YbAppealResultDeductimplementController extends BaseController {

    private String message;
    @Autowired
    private IYbAppealResultDeductimplementService iYbAppealResultDeductimplementService;

    @Autowired
    private IYbReconsiderApplyService iYbReconsiderApplyService;

    @Autowired
    private FebsProperties febsProperties;


    /**
     * 分页查询数据
     *
     * @param request                       分页信息
     * @param ybAppealResultDeductimplement 查询条件
     * @return
     */
    @GetMapping
    @RequiresPermissions("ybAppealResultDeductimplement:view")
    public Map<String, Object> List(QueryRequest request, YbAppealResultDeductimplement ybAppealResultDeductimplement) {
        return getDataTable(this.iYbAppealResultDeductimplementService.findYbAppealResultDeductimplements(request, ybAppealResultDeductimplement));
    }

    /**
     * 添加
     *
     * @param ybAppealResultDeductimplement
     * @return
     */
    @Log("新增/按钮")
    @PostMapping
    @RequiresPermissions("ybAppealResultDeductimplement:add")
    public FebsResponse addYbAppealResultDeductimplement(@Valid YbAppealResultDeductimplement ybAppealResultDeductimplement) {
        int success = 0;
        try {
            User currentUser = FebsUtil.getCurrentUser();
            ybAppealResultDeductimplement.setCreateUserId(currentUser.getUserId());
            if (ybAppealResultDeductimplement.getApplyDateStr() != null) {
                Date applyDate = DataTypeHelpers.stringDateFormat(ybAppealResultDeductimplement.getApplyDateStr() + "-15", "yyyy-MM-dd", true);
                ybAppealResultDeductimplement.setApplyDate(applyDate);
            }
            if (ybAppealResultDeductimplement.getImplementDateStr() != null) {
                Date implementDate = DataTypeHelpers.stringDateFormat(ybAppealResultDeductimplement.getImplementDateStr() + "-15", "yyyy-MM-dd", true);
                ybAppealResultDeductimplement.setImplementDate(implementDate);
            }
            String msg = this.iYbAppealResultDeductimplementService.createAppealResultDeductimplement(ybAppealResultDeductimplement);
            if (msg.equals("ok")) {
                success = 1;
                message = "提交数据成功.";
            } else {
                if (msg.equals("n1")) {
                    message = "提交数据失败.";
                } else {
                    message = "提交失败,该条数据已提交过扣款落实.";
                }
            }
        } catch (Exception e) {
            message = "提交数据失败.";
            log.error(message, e);
        }
        ResponseResult rr = new ResponseResult();
        rr.setSuccess(success);
        rr.setMessage(message);

        return new FebsResponse().data(rr);
    }

    /**
     * 修改
     *
     * @param ybAppealResultDeductimplement
     * @return
     */
    @Log("修改")
    @PutMapping
    @RequiresPermissions("ybAppealResultDeductimplement:update")
    public void updateYbAppealResultDeductimplement(@Valid YbAppealResultDeductimplement ybAppealResultDeductimplement) throws FebsException {
        try {
            User currentUser = FebsUtil.getCurrentUser();
            ybAppealResultDeductimplement.setModifyUserId(currentUser.getUserId());
            this.iYbAppealResultDeductimplementService.updateYbAppealResultDeductimplement(ybAppealResultDeductimplement);
        } catch (Exception e) {
            message = "修改失败";
            log.error(message, e);
            throw new FebsException(message);
        }
    }


    @Log("删除")
    @DeleteMapping("/{ids}")
    @RequiresPermissions("ybAppealResultDeductimplement:delete")
    public void deleteYbAppealResultDeductimplements(@NotBlank(message = "{required}") @PathVariable String ids) throws FebsException {
        try {
            String[] arr_ids = ids.split(StringPool.COMMA);
            this.iYbAppealResultDeductimplementService.deleteYbAppealResultDeductimplements(arr_ids);
        } catch (Exception e) {
            message = "删除失败";
            log.error(message, e);
            throw new FebsException(message);
        }
    }

    @PostMapping("excel")
    @RequiresPermissions("ybAppealResultDeductimplement:export")
    public void export(QueryRequest request, YbAppealResultDeductimplement ybAppealResultDeductimplement, HttpServletResponse response) throws FebsException {
        try {
            List<YbAppealResultDeductimplement> ybAppealResultDeductimplements = this.iYbAppealResultDeductimplementService.findYbAppealResultDeductimplements(request, ybAppealResultDeductimplement).getRecords();
            ExcelKit.$Export(YbAppealResultDeductimplement.class, response).downXlsx(ybAppealResultDeductimplements, false);
        } catch (Exception e) {
            message = "导出Excel失败";
            log.error(message, e);
            throw new FebsException(message);
        }
    }

    @GetMapping("/{id}")
    public YbAppealResultDeductimplement detail(@NotBlank(message = "{required}") @PathVariable String id) {
        YbAppealResultDeductimplement ybAppealResultDeductimplement = this.iYbAppealResultDeductimplementService.getById(id);
        return ybAppealResultDeductimplement;
    }

    @PostMapping("importDeductimplement")
    @RequiresPermissions("ybAppealResultDeductimplement:import")
    public FebsResponse importDeductimplementReset(@RequestParam MultipartFile file, @RequestParam String applyDateStr) {
        int success = 0;
        if (file.isEmpty()) {
            message = "空文件";
        } else {
            YbReconsiderApply ybReconsiderApply = this.iYbReconsiderApplyService.findReconsiderApplyByApplyDateStrs(applyDateStr);
            if (ybReconsiderApply != null) {
                if (ybReconsiderApply.getResetState() == 1) {
                    boolean blError = false;
                    try {
                        String guid = UUID.randomUUID().toString();
                        String filePath = febsProperties.getUploadPath(); // 上传后的路径
                        File getFile = FileHelpers.fileUpLoad(file, filePath, guid, "ReconsiderResetTemp");
                        Map<Integer, String> sheetMap = ImportExcelUtils.getSheelNames(getFile);
                        if (sheetMap.size() == 2) {
                            int nZd = 0;
                            int nMx = 0;
                            for (Integer key : sheetMap.keySet()) {
                                String value = sheetMap.get(key);
                                if (value.equals("明细扣款")) {
                                    nMx = 1;
                                }
                                if (value.equals("主单扣款")) {
                                    nZd = 1;
                                }
                            }
                            if (nZd == 1 && nMx == 1) {
                                List<Object[]> objMx = new ArrayList<Object[]>();
                                List<Object[]> objZd = new ArrayList<Object[]>();
                                for (Integer key : sheetMap.keySet()) {
                                    String value = sheetMap.get(key);
                                    int sheetIndex = key;
                                    if (value.equals("明细扣款")) {
                                        objMx = ImportExcelUtils.importExcelBySheetIndex(getFile, key, 0, 0);
                                    }
                                    if (value.equals("主单扣款")) {
                                        objZd = ImportExcelUtils.importExcelBySheetIndex(getFile, key, 0, 0);
                                    }
                                }
                                if (objMx.size() > 1 || objZd.size() > 1) {
                                    int listDataCount = 0;
                                    int listMainCount = 0;
                                    List<YbReconsiderResetDeductimplement> resetDeductimplementList = new ArrayList<>();

                                    List<String> mxOrderNumberList = new ArrayList<>();
                                    String mxCongfu = "";
                                    boolean mxIsNull = false;
                                    List<String> zdOrderNumberList = new ArrayList<>();
                                    String zdCongfu = "";
                                    boolean zdIsNull = false;

                                    if (objMx.size() > 1) {
                                        if (objMx.get(0).length == 23) {
                                            for (int i = 1; i < objMx.size(); i++) {
                                                message = "明细扣款数据读取失败，请确保Excel列表数据正确无误.";
                                                YbReconsiderResetDeductimplement rrd = new YbReconsiderResetDeductimplement();
                                                String strOrderNumber = DataTypeHelpers.importTernaryOperate(objMx.get(i), 0);//序号',
                                                if (!DataTypeHelpers.isNullOrEmpty(strOrderNumber)) {
                                                    if (!mxOrderNumberList.stream().anyMatch(task -> task.equals(strOrderNumber))) {
                                                        mxOrderNumberList.add(strOrderNumber);
                                                    } else {
                                                        mxCongfu = DataTypeHelpers.stringSeparate(mxCongfu, strOrderNumber, "、");
                                                    }
                                                } else {
                                                    mxIsNull = true;
                                                }
                                                rrd.setOrderNumber(strOrderNumber);

                                                String implementDateStr = DataTypeHelpers.importTernaryOperate(objMx.get(i), 20);
                                                if (implementDateStr.equals("")) {
                                                    blError = true;
                                                    message = "明细扣款数据存在绩效年月为空，请填写完整后再上传...";
                                                    break;
                                                } else {
                                                    if (implementDateStr.length() == 6) {
                                                        Boolean isDate = DataTypeHelpers.isValidDate(implementDateStr + "01", "", false);
                                                        if (!isDate) {
                                                            blError = true;
                                                            message = "明细扣款数据存在绩效年月格式不正确，格式应为 202010 ,请填写完整后再上传...";
                                                            break;
                                                        }

                                                    } else {
                                                        blError = true;
                                                        message = "明细扣款数据存在绩效年月格式不正确，格式应为 202010 ,请填写完整后再上传...";
                                                        break;
                                                    }
                                                }
                                                Date implementDate = DataTypeHelpers.stringDateFormat(implementDateStr + "-15", "yyyyMM-dd", true);
                                                rrd.setImplementDate(implementDate);
                                                implementDateStr = DataTypeHelpers.stringDate6Chang7(implementDateStr,"-");
                                                rrd.setImplementDateStr(implementDateStr);
                                                String shareStateStr = DataTypeHelpers.importTernaryOperate(objMx.get(i), 21);
                                                if (shareStateStr.equals("")) {
                                                    blError = true;
                                                    message = "明细扣款数据存在分摊方式为空，请填写完整后再上传...";
                                                    break;
                                                } else {
                                                    if (shareStateStr.equals("个人分摊") || shareStateStr.equals("科室分摊")) {
                                                        rrd.setShareState(shareStateStr.equals("个人分摊") ? YbDefaultValue.SHARESTATE_0 : YbDefaultValue.SHARESTATE_1);
                                                    } else {
                                                        blError = true;
                                                        message = "明细扣款数据存在分摊方式填写错误，应填写【个人分摊/科室分摊】 ,请填写完整后再上传...";
                                                        break;
                                                    }
                                                }
                                                rrd.setShareStateStr(shareStateStr);
                                                String shareProgramme = DataTypeHelpers.importTernaryOperate(objMx.get(i), 22);
                                                rrd.setShareProgramme(shareProgramme);

                                                rrd.setDataType(YbDefaultValue.DATATYPE_0);
                                                listDataCount =1;
                                                resetDeductimplementList.add(rrd);
                                            }
                                        } else {
                                            blError = true;
                                            message = "Excel数据获取失败，Sheet明细扣款 列表列数不正确";
                                        }
                                    }
                                    if (!blError) {
                                        if (objZd.size() > 1) {
                                            if (objZd.get(0).length == 16) {
                                                for (int i = 1; i < objZd.size(); i++) {
                                                    message = "主单扣款落实数据读取失败，请确保Excel列表数据正确无误.";
                                                    YbReconsiderResetDeductimplement rrd = new YbReconsiderResetDeductimplement();
                                                    String strOrderNumber = DataTypeHelpers.importTernaryOperate(objZd.get(i), 0);//序号',
                                                    if (!DataTypeHelpers.isNullOrEmpty(strOrderNumber)) {
                                                        if (!zdOrderNumberList.stream().anyMatch(task -> task.equals(strOrderNumber))) {
                                                            zdOrderNumberList.add(strOrderNumber);
                                                        } else {
                                                            zdCongfu = DataTypeHelpers.stringSeparate(zdCongfu, strOrderNumber, "、");
                                                        }
                                                    } else {
                                                        zdIsNull = true;
                                                    }
                                                    rrd.setOrderNumber(strOrderNumber);
                                                    String implementDateStr = DataTypeHelpers.importTernaryOperate(objZd.get(i), 13);
                                                    if (implementDateStr.equals("")) {
                                                        blError = true;
                                                        message = "主单扣款数据存在绩效年月为空，请填写完整后再上传...";
                                                        break;
                                                    } else {
                                                        if (implementDateStr.length() == 6) {
                                                            Boolean isDate = DataTypeHelpers.isValidDate(implementDateStr + "01", "", false);
                                                            if (!isDate) {
                                                                blError = true;
                                                                message = "主单扣款数据存在绩效年月格式不正确，格式应为 202010 ,请填写完整后再上传...";
                                                                break;
                                                            }

                                                        } else {
                                                            blError = true;
                                                            message = "主单扣款数据存在绩效年月格式不正确，格式应为 202010 ,请填写完整后再上传...";
                                                            break;
                                                        }
                                                    }
                                                    Date implementDate = DataTypeHelpers.stringDateFormat(implementDateStr + "-15", "yyyyMM-dd", true);
                                                    rrd.setImplementDate(implementDate);
                                                    implementDateStr = DataTypeHelpers.stringDate6Chang7(implementDateStr,"-");
                                                    rrd.setImplementDateStr(implementDateStr);
                                                    String shareStateStr = DataTypeHelpers.importTernaryOperate(objZd.get(i), 14);
                                                    if (shareStateStr.equals("")) {
                                                        blError = true;
                                                        message = "主单扣款数据存在分摊方式为空，请填写完整后再上传...";
                                                        break;
                                                    } else {
                                                        if (shareStateStr.equals("个人分摊") || shareStateStr.equals("科室分摊")) {
                                                            rrd.setShareState(shareStateStr.equals("个人分摊") ? YbDefaultValue.SHARESTATE_0 : YbDefaultValue.SHARESTATE_1);
                                                        } else {
                                                            blError = true;
                                                            message = "主单扣款数据存在分摊方式填写错误，应填写【个人分摊或科室分摊】 ,请填写完整后再上传...";
                                                            break;
                                                        }
                                                    }
                                                    rrd.setShareStateStr(shareStateStr);
                                                    String shareProgramme = DataTypeHelpers.importTernaryOperate(objZd.get(i), 15);
                                                    rrd.setShareProgramme(shareProgramme);

                                                    rrd.setDataType(YbDefaultValue.DATATYPE_1);
                                                    listMainCount = 1;
                                                    resetDeductimplementList.add(rrd);
                                                }
                                            } else {
                                                blError = true;
                                                message = "Excel数据获取失败，Sheet主单扣款 列表列数不正确";
                                            }
                                        }
                                    }
                                    if (!blError) {
                                        message = "";
                                        if (listDataCount == 1) {
                                            if (mxIsNull) {
                                                message = "明细扣款存在序号为空数据";
                                            }
                                            if (!DataTypeHelpers.isNullOrEmpty(mxCongfu)) {
                                                if (!DataTypeHelpers.isNullOrEmpty(message)) {
                                                    message += " , 序号： " + mxCongfu + " 重复";
                                                } else {
                                                    message = "明细扣款 序号： " + mxCongfu + " 重复";
                                                }
                                            }
                                        }
                                        if (listMainCount == 1) {
                                            if (!DataTypeHelpers.isNullOrEmpty(message)) {
                                                message += " 。 ";
                                            }
                                            if (zdIsNull) {
                                                message += "主单扣款存在序号为空数据";
                                            }
                                            if (!DataTypeHelpers.isNullOrEmpty(zdCongfu)) {
                                                if (!DataTypeHelpers.isNullOrEmpty(message)) {
                                                    message += " , 序号： " + zdCongfu + " 重复";
                                                } else {
                                                    message += "主单扣款 序号： " + zdCongfu + " 重复";
                                                }
                                            }
                                        }
                                        if (!DataTypeHelpers.isNullOrEmpty(message)) {
                                            message = "上传的Excel  " + message + "。 请检查后重新上传.";
                                        }
                                        if (DataTypeHelpers.isNullOrEmpty(message)) {
                                            User currentUser = FebsUtil.getCurrentUser();
                                            String newApplyDateStr = applyDateStr + "15";
                                            Date applyDate = DataTypeHelpers.stringDateFormat(newApplyDateStr, null, false);
                                            YbAppealResultDeductimplement ybAppealResultDeductimplement = new YbAppealResultDeductimplement();
                                            ybAppealResultDeductimplement.setApplyDateStr(applyDateStr);
                                            ybAppealResultDeductimplement.setApplyDate(applyDate);
                                            ybAppealResultDeductimplement.setCreateUserId(currentUser.getUserId());
                                            this.iYbAppealResultDeductimplementService.importCreateAppealResultDeductimplement(ybAppealResultDeductimplement, resetDeductimplementList);
                                            success = 1;
                                            message = "Excel数据获取成功.";
                                        }
                                    }
                                } else {
                                    message = "Excel数据获取失败,需确保存在两个Sheet 明细扣款、主单扣款,并确认列表数据是否正确.";
                                }
                            } else if (nZd == 0 && nMx == 0) {
                                message = "Excel数据获取失败，需确保存在两个Sheet 明细扣款、主单扣款.";
                            } else if (nZd == 0 && nMx == 1) {
                                message = "Excel数据获取失败，Sheet 主单扣款不存在.";
                            } else if (nZd == 1 && nMx == 0) {
                                message = "Excel数据获取失败，Sheet 明细扣款不存在.";
                            }
                        } else {
                            message = "Excel数据获取失败，Sheet个数不正确,需确保存在两个Sheet 明细扣款、主单扣款.";
                        }
                    } catch (Exception ex) {
                        //message = ex.getMessage();
                        if ("".equals(message)) {
                            message = "Excel数据获取失败.";
                        }
                        log.error(message, ex);
                    }
                } else {
                    message = "该" + applyDateStr + " 年月在【数据剔除】中未完成，完成剔操操作.";
                }
            } else {
                message = "该" + applyDateStr + " 年月未发现复议申请记录.";
            }
        }

        ResponseResultData rrd = new ResponseResultData();
        rrd.setSuccess(success);
        rrd.setMessage(message);
        return new FebsResponse().data(rrd);
    }
}