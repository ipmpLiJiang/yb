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

@Excel("yb_reconsider_reset_main_view")
@Data
@Accessors(chain = true)
public class YbReconsiderResetMainView implements Serializable{

private static final long serialVersionUID=1L;

    /**
     * 剔除表主单扣款
     */
            @ExcelField(value ="剔除表主单扣款")
    private String id;

    /**
     * 交易流水号
     */
    @TableField("serialNo")
            @ExcelField(value ="交易流水号")
    private String serialNo;

    /**
     * 单据号
     */
    @TableField("billNo")
            @ExcelField(value ="单据号")
    private String billNo;

    /**
     * 医保内金额
     */
    @TableField("medicalPrice")
            @ExcelField(value ="医保内金额")
    private BigDecimal medicalPrice;

    /**
     * 规则名称
     */
    @TableField("ruleName")
            @ExcelField(value ="规则名称")
    private String ruleName;

    /**
     * 扣除金额
     */
    @TableField("deductPrice")
            @ExcelField(value ="扣除金额")
    private BigDecimal deductPrice;

    /**
     * 住院号
     */
    @TableField("hospitalizedNo")
            @ExcelField(value ="住院号")
    private String hospitalizedNo;

    /**
     * 就医方式
     */
    @TableField("treatmentMode")
            @ExcelField(value ="就医方式")
    private String treatmentMode;

    /**
     * 结算日期
     */
    @TableField("settlementDate")
            @ExcelField(value ="结算日期")
    private Date settlementDate;
    private transient String settlementDateFrom;
    private transient String settlementDateTo;

    /**
     * 结算日期Str
     */
    @TableField("settlementDateStr")
            @ExcelField(value ="结算日期Str")
    private String settlementDateStr;

    /**
     * 个人编号
     */
    @TableField("personalNo")
            @ExcelField(value ="个人编号")
    private String personalNo;

    /**
     * 参保人姓名
     */
    @TableField("insuredName")
            @ExcelField(value ="参保人姓名")
    private String insuredName;

    /**
     * 参保类型
     */
    @TableField("insuredType")
            @ExcelField(value ="参保类型")
    private String insuredType;

    /**
     * 统筹区名称
     */
    @TableField("areaName")
            @ExcelField(value ="统筹区名称")
    private String areaName;

    /**
     * 剔除表
     */
    @TableField("resetId")
            @ExcelField(value ="剔除表")
    private String resetId;

    /**
     * 复议年月
     */
    @TableField("applyDate")
            @ExcelField(value ="复议年月")
    private Date applyDate;
    private transient String applyDateFrom;
    private transient String applyDateTo;

    /**
     * 复议年月Str
     */
    @TableField("applyDateStr")
            @ExcelField(value ="复议年月Str")
    private String applyDateStr;

    /**
     * 复议申请明细
     */
    @TableField("applyDataId")
            @ExcelField(value ="复议申请明细")
    private String applyDataId;

    /**
     * 规则编号
     */
    @TableField("reconsiderApplyId")
            @ExcelField(value ="规则编号")
    private String reconsiderApplyId;

    /**
     * 复议结果
     */
    @TableField("appealResultId")
            @ExcelField(value ="复议结果")
    private String appealResultId;

    /**
     * 核对Id
     */
    @TableField("verifyId")
            @ExcelField(value ="核对Id")
    private String verifyId;

    /**
     * 管理Id
     */
    @TableField("manageId")
            @ExcelField(value ="管理Id")
    private String manageId;

    /**
     * 医生编码
     */
    @TableField("arDoctorCode")
            @ExcelField(value ="医生编码")
    private String arDoctorCode;

    /**
     * 医生
     */
    @TableField("arDoctorName")
            @ExcelField(value ="医生")
    private String arDoctorName;

    /**
     * 科室编码
     */
    @TableField("arDeptCode")
            @ExcelField(value ="科室编码")
    private String arDeptCode;

    /**
     * 科室
     */
    @TableField("arDeptName")
            @ExcelField(value ="科室")
    private String arDeptName;

    /**
     * 版本类型
     */
            @ExcelField(value ="版本类型")
    private Integer typeno;

    @TableField("operateType")
            private Integer operateType;

    /**
     * 通用
     */
    @TableField("currencyField")
    @ExcelField(value = "通用")
    private String currencyField;

    public static final String ID ="id" ;

    public static final String SERIALNO ="serialNo" ;

    public static final String BILLNO ="billNo" ;

    public static final String MEDICALPRICE ="medicalPrice" ;

    public static final String RULENAME ="ruleName" ;

    public static final String DEDUCTPRICE ="deductPrice" ;

    public static final String HOSPITALIZEDNO ="hospitalizedNo" ;

    public static final String TREATMENTMODE ="treatmentMode" ;

    public static final String SETTLEMENTDATE ="settlementDate" ;

    public static final String SETTLEMENTDATESTR ="settlementDateStr" ;

    public static final String PERSONALNO ="personalNo" ;

    public static final String INSUREDNAME ="insuredName" ;

    public static final String INSUREDTYPE ="insuredType" ;

    public static final String AREANAME ="areaName" ;

    public static final String RESETID ="resetId" ;

    public static final String APPLYDATE ="applyDate" ;

    public static final String APPLYDATESTR ="applyDateStr" ;

    public static final String APPLYDATAID ="applyDataId" ;

    public static final String RECONSIDERAPPLYID ="reconsiderApplyId" ;

    public static final String APPEALRESULTID ="appealResultId" ;

    public static final String VERIFYID ="verifyId" ;

    public static final String MANAGEID ="manageId" ;

    public static final String ARDOCTORCODE ="arDoctorCode" ;

    public static final String ARDOCTORNAME ="arDoctorName" ;

    public static final String ARDEPTCODE ="arDeptCode" ;

    public static final String ARDEPTNAME ="arDeptName" ;

    public static final String TYPENO ="typeno" ;

    public static final String OPERATETYPE ="operateType" ;

    public static final String CURRENCYFIELD ="currencyField" ;

        }