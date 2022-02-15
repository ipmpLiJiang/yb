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
 * @since 2020-07-30
 */

@Excel("yb_reconsider_verify_view")
@Data
@Accessors(chain = true)
public class YbReconsiderVerifyView implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * pid
     */
    @TableField("pid")
    @ExcelField(value = "pid")
    private String pid;

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
     * 入院日期
     */
    @TableField("enterHospitalDateStr")
    @ExcelField(value = "入院日期Str")
    private String enterHospitalDateStr;

    /**
     * 出院日期
     */
    @TableField("outHospitalDateStr")
    @ExcelField(value = "出院日期Str")
    private String outHospitalDateStr;

    /**
     * 费用日期
     */
    @TableField("costDateStr")
    @ExcelField(value = "费用日期Str")
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
     * 结算日期
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
     * 发送人代码
     */
    @TableField("sendPersonId")
    @ExcelField(value ="发送人代码")
    private Long sendPersonId;

    /**
     * 发送人
     */
    @TableField("sendPersonName")
    @ExcelField(value ="发送人")
    private String sendPersonName;

    /**
     * 发送日期
     */
    @TableField("sendDate")
    @ExcelField(value ="发送日期")
    private Date sendDate;
    private transient String sendDateFrom;
    private transient String sendDateTo;

    /**
     * 复议年月Str
     */
    @TableField("applyDateStr")
    @ExcelField(value = "复议年月Str")
    private String applyDateStr;


    private String id;

    /**
     * 申请数据明细
     */
    @TableField("applyDataId")
    @ExcelField(value = "申请数据明细")
    private String applyDataId;

    /**
     * 参考复议医生编码
     */
    @TableField("verifyDoctorCode")
    @ExcelField(value = "参考复议医生编码")
    private String verifyDoctorCode;

    /**
     * 参考复议医生
     */
    @TableField("verifyDoctorName")
    @ExcelField(value = "参考复议医生")
    private String verifyDoctorName;

    /**
     * 参考复议科室编码
     */
    @TableField("verifyDeptCode")
    @ExcelField(value = "参考复议科室编码")
    private String verifyDeptCode;

    /**
     * 参考复议科室
     */
    @TableField("verifyDeptName")
    @ExcelField(value = "参考复议科室")
    private String verifyDeptName;

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
    //@ExcelField(value ="数据类型")
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
     * 是否核对
     */
    @TableField("isVerify")
    @ExcelField(value = "是否核对")
    private Integer isVerify;

    /**
     * 是否医生
     */
    @TableField("isPerson")
    @ExcelField(value = "是否医生")
    private Integer isPerson;

    /**
     * 开方医生编码
     */
    @TableField("orderDoctorCode")
    @ExcelField(value ="开方医生编码")
    private String orderDoctorCode;

    /**
     * 开方医生名称
     */
    @TableField("orderDoctorName")
    @ExcelField(value ="开方医生名称")
    private String orderDoctorName;

    /**
     * 开方科室编码
     */
    @TableField("orderDeptCode")
    @ExcelField(value ="开方科室编码")
    private String orderDeptCode;

    /**
     * 开方科室名称
     */
    @TableField("orderDeptName")
    @ExcelField(value ="开方科室名称")
    private String orderDeptName;


    /**
     * 状态
     */
    @TableField("STATE")
    @ExcelField(value = "状态")
    private Integer state;

    /**
     * 院区
     */
    @TableField("areaType")
    @ExcelField(value = "院区")
    private Integer areaType;

    /**
     * 汇总科室
     */
    @TableField("dksName")
    @ExcelField(value = "汇总科室")
    private String dksName;

    public static final String PID = "pid";

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

    public static final String SENDPERSONID ="sendPersonId" ;

    public static final String SENDPERSONNAME ="sendPersonName" ;

    public static final String SENDDATE ="sendDate" ;

    public static final String APPLYDATESTR = "applyDateStr";

    public static final String ID = "id";

    public static final String APPLYDATAID = "applyDataId";

    public static final String VERIFYDOCTORCODE = "verifyDoctorCode";

    public static final String VERIFYDOCTORNAME = "verifyDoctorName";

    public static final String VERIFYDEPTCODE = "verifyDeptCode";

    public static final String VERIFYDEPTNAME = "verifyDeptName";

    public static final String INSUREDTYPE = "insuredType";

    public static final String DATATYPE = "dataType";

    public static final String ORDERNUMBER = "orderNumber";

    public static final String ORDERNUM = "orderNum";

    public static final String ISVERIFY = "isVerify";

    public static final String ISPERSON = "isPerson";

    public static final String ORDERDOCTORCODE = "orderDoctorCode";

    public static final String ORDERDOCTORNAME = "orderDoctorName";

    public static final String ORDERDEPTCODE = "orderDeptCode";

    public static final String ORDERDEPTNAME = "orderDeptName";

    public static final String STATE = "STATE";

    public static final String AREATYPE = "areaType";

    public static final String DKSNAME = "dksName";

}