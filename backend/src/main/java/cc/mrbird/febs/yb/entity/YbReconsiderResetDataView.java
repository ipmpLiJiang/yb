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
 * @since 2020-08-18
 */

@Excel("yb_reconsider_reset_data_view")
@Data
@Accessors(chain = true)
public class YbReconsiderResetDataView implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 剔除表明细扣款
     */
    @ExcelField(value = "剔除表明细扣款")
    private String id;

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
     * 费用日期
     */
    @TableField("costDate")
    @ExcelField(value = "费用日期")
    private Date costDate;
    private transient String costDateFrom;
    private transient String costDateTo;

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
     * 结算日期
     */
    @TableField("settlementDate")
    @ExcelField(value = "结算日期")
    private Date settlementDate;
    private transient String settlementDateFrom;
    private transient String settlementDateTo;

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
     * 状态
     */
    @TableField("STATE")
    @ExcelField(value ="状态")
    private Integer state;

    /**
     * 剔除表
     */
    @TableField("resetId")
    @ExcelField(value = "剔除表")
    private String resetId;

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
     * 通用
     */
    @TableField("currencyField")
    @ExcelField(value = "通用")
    private String currencyField;

    /**
     * 查找状态
     */
    @TableField("seekState")
    //@ExcelField(value ="查找状态")
    private Integer seekState;


    public static final String ID = "id";

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

    public static final String COSTDATE = "costDate";

    public static final String COSTDATESTR = "costDateStr";

    public static final String HOSPITALIZEDNO = "hospitalizedNo";

    public static final String TREATMENTMODE = "treatmentMode";

    public static final String SETTLEMENTDATE = "settlementDate";

    public static final String SETTLEMENTDATESTR = "settlementDateStr";

    public static final String PERSONALNO = "personalNo";

    public static final String INSUREDNAME = "insuredName";

    public static final String CARDNUMBER = "cardNumber";

    public static final String AREANAME = "areaName";

    public static final String INSUREDTYPE = "insuredType";

    public static final String DATATYPE = "dataType";

    public static final String ORDERNUMBER = "orderNumber";

    public static final String STATE = "STATE";

    public static final String RESETID = "resetId";

    public static final String APPLYDATE = "applyDate";

    public static final String APPLYDATESTR = "applyDateStr";

    public static final String CURRENCYFIELD = "currencyField";

    public static final String SEEKSTATE = "seekState";

}