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
 * @since 2020-09-24
 */

@Excel("yb_appeal_result_deductimplement_view")
@Data
@Accessors(chain = true)
public class YbAppealResultDeductimplementView implements Serializable, Comparable<YbAppealResultDeductimplementView> {

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
     * 复议年月
     */
    @TableField("applyDate")
    @ExcelField(value = "复议年月")
    private Date applyDate;
    private transient String applyDateFrom;
    private transient String applyDateTo;

    /**
     * 复议年月Str
     */
    @TableField("applyDateStr")
    @ExcelField(value = "复议年月Str")
    private String applyDateStr;

    /**
     * 复议结果
     */
    @ExcelField(value = "复议结果")
    private String id;

    /**
     * 复议申请明细
     */
    @TableField("applyDataId")
    @ExcelField(value = "复议申请明细")
    private String applyDataId;

    /**
     * 医生编码
     */
    @TableField("arDoctorCode")
    @ExcelField(value = "医生编码")
    private String arDoctorCode;

    /**
     * 医生
     */
    @TableField("arDoctorName")
    @ExcelField(value = "医生")
    private String arDoctorName;

    /**
     * 科室编码
     */
    @TableField("arDeptCode")
    @ExcelField(value = "科室编码")
    private String arDeptCode;

    /**
     * 科室
     */
    @TableField("arDeptName")
    @ExcelField(value = "科室")
    private String arDeptName;


    /**
     * 剔除明细扣款Id
     */
    @TableField("resetDataId")
    @ExcelField(value = "剔除明细扣款Id")
    private String resetDataId;

    /**
     * 关联Id
     */
    @TableField("relatelDataId")
    @ExcelField(value = "关联Id")
    private String relatelDataId;


    /**
     * 通用
     */
    @TableField("currencyField")
    @ExcelField(value = "通用")
    private String currencyField;

    /**
     * 绩效年月
     */
    @TableField("implementDate")
    @ExcelField(value = "绩效年月")
    private Date implementDate;
    private transient String implementDateFrom;
    private transient String implementDateTo;

    /**
     * 绩效年月Str
     */
    @TableField("implementDateStr")
    @ExcelField(value = "绩效年月Str")
    private String implementDateStr;

    /**
     * 分摊方式
     */
    @TableField("shareState")
    @ExcelField(value = "分摊方式")
    private Integer shareState;

    /**
     * 分摊方案
     */
    @TableField("shareProgramme")
    @ExcelField(value = "分摊方案")
    private String shareProgramme;

    /**
     * 扣款落实ID
     */
    @TableField("deductImplementId")
    @ExcelField(value = "扣款落实ID")
    private String deductImplementId;

    /**
     * 院区
     */
    @TableField("areaType")
    @ExcelField(value = "院区")
    private Integer areaType;

    public static final String PID = "pid";
    public static final String SERIALNO = "serialNo";
    public static final String BILLNO = "billNo";
    public static final String PROJECTCODE = "projectCode";
    public static final String PROJECTNAME = "projectName";
    public static final String MEDICALPRICE = "medicalPrice";
    public static final String RULENAME = "ruleName";
    public static final String DEDUCTPRICE = "deductPrice";
    public static final String DEDUCTREASON = "deductReason";
    public static final String REPAYMENTREASON = "repaymentReason";
    public static final String DOCTORNAME = "doctorName";
    public static final String DEPTCODE = "deptCode";
    public static final String DEPTNAME = "deptName";
    public static final String COSTDATESTR = "costDateStr";
    public static final String HOSPITALIZEDNO = "hospitalizedNo";
    public static final String TREATMENTMODE = "treatmentMode";
    public static final String SETTLEMENTDATESTR = "settlementDateStr";
    public static final String PERSONALNO = "personalNo";
    public static final String INSUREDNAME = "insuredName";
    public static final String CARDNUMBER = "cardNumber";
    public static final String AREANAME = "areaName";
    public static final String TYPENO = "typeno";
    public static final String INSUREDTYPE = "insuredType";
    public static final String DATATYPE = "dataType";
    public static final String ORDERNUMBER = "orderNumber";
    public static final String ORDERNUM = "orderNum";
    public static final String APPLYDATE = "applyDate";
    public static final String APPLYDATESTR = "applyDateStr";
    public static final String ID = "id";
    public static final String APPLYDATAID = "applyDataId";
    public static final String ARDOCTORCODE = "arDoctorCode";
    public static final String ARDOCTORNAME = "arDoctorName";
    public static final String ARDEPTCODE = "arDeptCode";
    public static final String ARDEPTNAME = "arDeptName";
    public static final String RESETDATAID = "resetDataId";
    public static final String RELATELDATAID = "relatelDataId";
    public static final String CURRENCYFIELD = "currencyField";
    public static final String IMPLEMENTDATE = "implementDate";
    public static final String IMPLEMENTDATESTR = "implementDateStr";
    public static final String SHARESTATE = "shareState";
    public static final String SHAREPROGRAMME = "shareProgramme";
    public static final String DEDUCTIMPLEMENTID = "deductImplementId";
    public static final String AREATYPE = "areaType";

    @Override
    public int compareTo(YbAppealResultDeductimplementView o) {
        if (this.getOrderNum() != null && o.getOrderNum() != null) {
            return this.getOrderNum().compareTo(o.getOrderNum());
        } else if (this.getOrderNum() != null) {
            return 1;
        } else {
            return 0;
        }
    }
}