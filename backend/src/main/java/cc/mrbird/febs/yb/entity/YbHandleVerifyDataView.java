package cc.mrbird.febs.yb.entity;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import com.baomidou.mybatisplus.annotation.TableField;

import java.io.Serializable;


import lombok.Data;
import lombok.experimental.Accessors;

import java.util.Date;

import com.wuwenze.poi.annotation.Excel;
import com.wuwenze.poi.annotation.ExcelField;

/**
 * <p>
 * VIEW
 * </p>
 *
 * @author viki
 * @since 2020-08-28
 */

@Excel("yb_handle_verify_data_view")
@Data
@Accessors(chain = true)
public class YbHandleVerifyDataView implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 交易流水号
     */
    @TableField("serialNo")
    @ExcelField(value = "交易流水号")
    private String serialNo;

    /**
     * 单据号
     */
    @TableField("billNo")
    @ExcelField(value = "单据号")
    private String billNo;

    /**
     * 意见书编码
     */
    @TableField("proposalCode")
    @ExcelField(value = "意见书编码")
    private String proposalCode;

    /**
     * 项目编码
     */
    @TableField("projectCode")
    @ExcelField(value = "项目编码")
    private String projectCode;

    /**
     * 项目名称
     */
    @TableField("projectName")
    @ExcelField(value = "项目名称")
    private String projectName;

    /**
     * 数量
     */
    @ExcelField(value = "数量")
    private BigDecimal num;

    /**
     * 医保内金额
     */
    @TableField("medicalPrice")
    @ExcelField(value = "医保内金额")
    private BigDecimal medicalPrice;

    /**
     * 规则名称
     */
    @TableField("ruleName")
    @ExcelField(value = "规则名称")
    private String ruleName;

    /**
     * 扣除金额
     */
    @TableField("deductPrice")
    @ExcelField(value = "扣除金额")
    private BigDecimal deductPrice;

    /**
     * 扣除原因
     */
    @TableField("deductReason")
    @ExcelField(value = "扣除原因")
    private String deductReason;

    /**
     * 还款原因
     */
    @TableField("repaymentReason")
    @ExcelField(value = "还款原因")
    private String repaymentReason;

    /**
     * 医生姓名
     */
    @TableField("doctorName")
    @ExcelField(value = "医生姓名")
    private String doctorName;

    /**
     * 科室编码
     */
    @TableField("deptCode")
    @ExcelField(value = "科室编码")
    private String deptCode;

    /**
     * 科室名称
     */
    @TableField("deptName")
    @ExcelField(value = "科室名称")
    private String deptName;


    /**
     * 入院日期str
     */
    @TableField("enterHospitalDateStr")
    @ExcelField(value = "入院日期str")
    private String enterHospitalDateStr;

    /**
     * 出院日期str
     */
    @TableField("outHospitalDateStr")
    @ExcelField(value = "出院日期str")
    private String outHospitalDateStr;

    /**
     * 费用日期str
     */
    @TableField("costDateStr")
    @ExcelField(value = "费用日期str")
    private String costDateStr;

    /**
     * 住院号
     */
    @TableField("hospitalizedNo")
    @ExcelField(value = "住院号")
    private String hospitalizedNo;

    /**
     * 就医方式
     */
    @TableField("treatmentMode")
    @ExcelField(value = "就医方式")
    private String treatmentMode;

    /**
     * 结算日期Str
     */
    @TableField("settlementDateStr")
    @ExcelField(value = "结算日期Str")
    private String settlementDateStr;

    /**
     * 个人编号
     */
    @TableField("personalNo")
    @ExcelField(value = "个人编号")
    private String personalNo;

    /**
     * 参保人姓名
     */
    @TableField("insuredName")
    @ExcelField(value = "参保人姓名")
    private String insuredName;

    /**
     * 医保卡号
     */
    @TableField("cardNumber")
    @ExcelField(value = "医保卡号")
    private String cardNumber;

    /**
     * 统筹区名称
     */
    @TableField("areaName")
    @ExcelField(value = "统筹区名称")
    private String areaName;

    /**
     * 版本号
     */
    @TableField("versionNumber")
    @ExcelField(value = "版本号")
    private String versionNumber;

    /**
     * 反馈申诉
     */
    @TableField("backAppeal")
    @ExcelField(value = "反馈申诉")
    private String backAppeal;

    /**
     * 版本类型
     */
    @ExcelField(value = "版本类型")
    private Integer typeno;

    /**
     * 参保类型
     */
    @TableField("insuredType")
    @ExcelField(value = "参保类型")
    private String insuredType;

    /**
     * 数据类型
     */
    @TableField("dataType")
    @ExcelField(value = "数据类型")
    private Integer dataType;

    /**
     * 序号
     */
    @TableField("orderNumber")
    @ExcelField(value = "序号")
    private String orderNumber;

    /**
     * 排序
     */
    @TableField("orderNum")
    @ExcelField(value = "排序")
    private Integer orderNum;

    /**
     * 复议年月Str
     */
    @TableField("applyDateStr")
    @ExcelField(value = "复议年月Str")
    private String applyDateStr;

    /**
     * 手动核对明细
     */
    @ExcelField(value = "手动核对明细")
    private String id;

    /**
     * 手动核对Id
     */
    @ExcelField(value = "手动核对Id")
    private String pid;

    /**
     * 复议申请明细
     */
    @TableField("applyDataId")
    @ExcelField(value = "复议申请明细")
    private String applyDataId;

    /**
     * 核对Id
     */
    @TableField("verifyId")
    @ExcelField(value = "核对Id")
    private String verifyId;

    /**
     * 管理Id
     */
    @TableField("manageId")
    @ExcelField(value = "管理Id")
    private String manageId;

//    /**
//     * 剔除明细Id
//     */
//    @TableField("resetId")
//            @ExcelField(value ="剔除明细Id")
//    private String resetId;

    /**
     * 复议上传Id
     */
    @TableField("resultId")
    @ExcelField(value = "复议上传Id")
    private String resultId;

    /**
     * 复议医生编码
     */
    @TableField("hvDoctorCode")
    @ExcelField(value = "复议医生编码")
    private String hvDoctorCode;

    /**
     * 复议医生
     */
    @TableField("hvDoctorName")
    @ExcelField(value = "复议医生")
    private String hvDoctorName;

    /**
     * 复议科室编码
     */
    @TableField("hvDeptCode")
    @ExcelField(value = "复议科室编码")
    private String hvDeptCode;

    /**
     * 复议科室
     */
    @TableField("hvDeptName")
    @ExcelField(value = "复议科室")
    private String hvDeptName;

    /**
     * 状态
     */
    @TableField("STATE")
    @ExcelField(value = "状态")
    private Integer state;


    /**
     * 通用
     */
    @TableField("currencyField")
    @ExcelField(value = "通用")
    private String currencyField;

    /**
     * 关联Id
     */
    @TableField("relatelDataId")
    @ExcelField(value = "关联Id")
    private String relatelDataId;

    /**
     * 院区
     */
    @TableField("areaType")
    @ExcelField(value = "院区")
    private Integer areaType;

    public static final String SERIALNO = "serialNo";

    public static final String BILLNO = "billNo";

    public static final String PROPOSALCODE = "proposalCode";

    public static final String PROJECTCODE = "projectCode";

    public static final String PROJECTNAME = "projectName";

    public static final String NUM = "num";

    public static final String MEDICALPRICE = "medicalPrice";

    public static final String RULENAME = "ruleName";

    public static final String DEDUCTPRICE = "deductPrice";

    public static final String DEDUCTREASON = "deductReason";

    public static final String REPAYMENTREASON = "repaymentReason";

    public static final String DOCTORNAME = "doctorName";

    public static final String DEPTCODE = "deptCode";

    public static final String DEPTNAME = "deptName";

    public static final String ENTERHOSPITALDATESTR = "enterHospitalDateStr";

    public static final String OUTHOSPITALDATESTR = "outHospitalDateStr";

    public static final String COSTDATESTR = "costDateStr";

    public static final String HOSPITALIZEDNO = "hospitalizedNo";

    public static final String TREATMENTMODE = "treatmentMode";

    public static final String SETTLEMENTDATESTR = "settlementDateStr";

    public static final String PERSONALNO = "personalNo";

    public static final String INSUREDNAME = "insuredName";

    public static final String CARDNUMBER = "cardNumber";

    public static final String AREANAME = "areaName";

    public static final String VERSIONNUMBER = "versionNumber";

    public static final String BACKAPPEAL = "backAppeal";

    public static final String TYPENO = "typeno";

    public static final String INSUREDTYPE = "insuredType";

    public static final String DATATYPE = "dataType";

    public static final String ORDERNUMBER = "orderNumber";

    public static final String ORDERNUM = "orderNum";

    public static final String APPLYDATESTR = "applyDateStr";

    public static final String ID = "id";

    public static final String PID = "pid";

    public static final String APPLYDATAID = "applyDataId";

    public static final String VERIFYID = "verifyId";

    public static final String MANAGEID = "manageId";

//    public static final String RESETID ="resetId" ;

    public static final String RELATELDATAID = "relatelDataId";

    public static final String RESULTID = "resultId";

    public static final String HVDOCTORCODE = "hvDoctorCode";

    public static final String HVDOCTORNAME = "hvDoctorName";

    public static final String HVDEPTCODE = "hvDeptCode";

    public static final String HVDEPTNAME = "hvDeptName";

    public static final String STATE = "STATE";

    public static final String CURRENCYFIELD = "currencyField";

    public static final String AREATYPE = "areaType";

}