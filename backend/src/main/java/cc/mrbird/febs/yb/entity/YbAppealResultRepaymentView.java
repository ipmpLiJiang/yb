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
 * @since 2020-10-09
 */

@Excel("yb_appeal_result_repayment_view")
@Data
@Accessors(chain = true)
public class YbAppealResultRepaymentView implements Serializable , Comparable<YbAppealResultRepaymentView>{

private static final long serialVersionUID=1L;

    /**
     * 还款核对明细
     */
            @ExcelField(value ="还款核对明细")
    private String id;

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
     * 数据类型
     */
    @TableField("dataType")
            @ExcelField(value ="数据类型")
    private Integer dataType;

    /**
     * 保险类型
     */
    @TableField("repayType")
            @ExcelField(value ="保险类型")
    private Integer repayType;

    /**
     * 所属期
     */
    @TableField("belongDate")
            @ExcelField(value ="所属期")
    private Date belongDate;
    private transient String belongDateFrom;
    private transient String belongDateTo;

    /**
     * 所属期Str
     */
    @TableField("belongDateStr")
            @ExcelField(value ="所属期Str")
    private String belongDateStr;

    /**
     * 医院编号
     */
    @TableField("hospitalCode")
            @ExcelField(value ="医院编号")
    private String hospitalCode;

    /**
     * 医院名称
     */
    @TableField("hospitalName")
            @ExcelField(value ="医院名称")
    private String hospitalName;

    /**
     * 序号
     */
    @TableField("orderNumber")
            @ExcelField(value ="序号")
    private String orderNumber;

    /**
     * 序号New
     */
    @TableField("orderNumberNew")
            @ExcelField(value ="序号New")
    private String orderNumberNew;

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
     * 项目编码
     */
    @TableField("projectCode")
            @ExcelField(value ="项目编码")
    private String projectCode;

    /**
     * 项目名称
     */
    @TableField("projectName")
            @ExcelField(value ="项目名称")
    private String projectName;

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
     * 扣除原因
     */
    @TableField("deductReason")
            @ExcelField(value ="扣除原因")
    private String deductReason;

    /**
     * 还款金额
     */
    @TableField("repaymentPrice")
            @ExcelField(value ="还款金额")
    private BigDecimal repaymentPrice;

    /**
     * 还款原因
     */
    @TableField("repaymentReason")
            @ExcelField(value ="还款原因")
    private String repaymentReason;

    /**
     * 医生姓名
     */
    @TableField("doctorName")
            @ExcelField(value ="医生姓名")
    private String doctorName;

    /**
     * 科室编码
     */
    @TableField("deptCode")
            @ExcelField(value ="科室编码")
    private String deptCode;

    /**
     * 科室名称
     */
    @TableField("deptName")
            @ExcelField(value ="科室名称")
    private String deptName;

    /**
     * 费用日期
     */
    @TableField("costDate")
            @ExcelField(value ="费用日期")
    private Date costDate;
    private transient String costDateFrom;
    private transient String costDateTo;

    /**
     * 费用日期str
     */
    @TableField("costDateStr")
            @ExcelField(value ="费用日期str")
    private String costDateStr;

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
     * 更新类型
     */
    @TableField("updateType")
            @ExcelField(value ="更新类型")
    private Integer updateType;

    /**
     * 提醒状态
     */
    @TableField("warnType")
            @ExcelField(value ="提醒状态")
    private Integer warnType;

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
     * 还款落实Id
     */
    @TableField("repaymentId")
            @ExcelField(value ="还款落实Id")
    private String repaymentId;

    /**
     * 复议上传Id
     */
    @TableField("resultId")
            @ExcelField(value ="复议上传Id")
    private String resultId;

    /**
     * 剔除明细Id
     */
    @TableField("resetDataId")
            @ExcelField(value ="剔除明细Id")
    private String resetDataId;

    /**
     * 扣除落实Id
     */
    @TableField("deductImplementId")
            @ExcelField(value ="扣除落实Id")
    private String deductImplementId;

    /**
     * 还款方案
     */
    @TableField("repaymentProgramme")
            @ExcelField(value ="还款方案")
    private String repaymentProgramme;

    /**
     * 分摊方式
     */
    @TableField("shareState")
    @ExcelField(value ="分摊方式")
    private Integer shareState;

    /**
     * 分摊方案
     */
    @TableField("shareProgramme")
    @ExcelField(value ="分摊方案")
    private String shareProgramme;

    /**
     * 通用
     */
    @TableField("currencyField")
    @ExcelField(value ="通用")
    private String currencyField;


    public static final String ID ="id" ;

    public static final String APPLYDATE ="applyDate" ;

    public static final String APPLYDATESTR ="applyDateStr" ;

    public static final String DATATYPE ="dataType" ;

    public static final String REPAYTYPE ="repayType" ;

    public static final String BELONGDATE ="belongDate" ;

    public static final String BELONGDATESTR ="belongDateStr" ;

    public static final String HOSPITALCODE ="hospitalCode" ;

    public static final String HOSPITALNAME ="hospitalName" ;

    public static final String ORDERNUMBER ="orderNumber" ;

    public static final String ORDERNUMBERNEW ="orderNumberNew" ;

    public static final String SERIALNO ="serialNo" ;

    public static final String BILLNO ="billNo" ;

    public static final String PROJECTCODE ="projectCode" ;

    public static final String PROJECTNAME ="projectName" ;

    public static final String MEDICALPRICE ="medicalPrice" ;

    public static final String RULENAME ="ruleName" ;

    public static final String DEDUCTPRICE ="deductPrice" ;

    public static final String DEDUCTREASON ="deductReason" ;

    public static final String REPAYMENTPRICE ="repaymentPrice" ;

    public static final String REPAYMENTREASON ="repaymentReason" ;

    public static final String DOCTORNAME ="doctorName" ;

    public static final String DEPTCODE ="deptCode" ;

    public static final String DEPTNAME ="deptName" ;

    public static final String COSTDATE ="costDate" ;

    public static final String COSTDATESTR ="costDateStr" ;

    public static final String HOSPITALIZEDNO ="hospitalizedNo" ;

    public static final String TREATMENTMODE ="treatmentMode" ;

    public static final String SETTLEMENTDATE ="settlementDate" ;

    public static final String SETTLEMENTDATESTR ="settlementDateStr" ;

    public static final String PERSONALNO ="personalNo" ;

    public static final String INSUREDNAME ="insuredName" ;

    public static final String UPDATETYPE ="updateType" ;

    public static final String WARNTYPE ="warnType" ;

    public static final String ARDOCTORCODE ="arDoctorCode" ;

    public static final String ARDOCTORNAME ="arDoctorName" ;

    public static final String ARDEPTCODE ="arDeptCode" ;

    public static final String ARDEPTNAME ="arDeptName" ;

    public static final String REPAYMENTID ="repaymentId" ;

    public static final String RESULTID ="resultId" ;

    public static final String RESETDATAID ="resetDataId" ;

    public static final String DEDUCTIMPLEMENTID ="deductImplementId" ;

    public static final String REPAYMENTPROGRAMME ="repaymentProgramme" ;

    public static final String SHARESTATE ="shareState" ;

    public static final String SHAREPROGRAMME ="shareProgramme" ;

    public static final String CURRENCYFIELD ="currencyField" ;

@Override
public int compareTo(YbAppealResultRepaymentView o) {
        if (this.getId() != null && o.getId() != null) {
        return this.getId().compareTo(o.getId());
        } else if (this.getId() != null) {
        return 1;
        } else {
        return 0;
        }
        }
}