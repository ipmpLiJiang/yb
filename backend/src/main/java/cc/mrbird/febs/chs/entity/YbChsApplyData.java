package cc.mrbird.febs.chs.entity;

import java.math.BigDecimal;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
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
 *
 * </p>
 *
 * @author viki
 * @since 2022-06-21
 */

@Excel("yb_chs_apply_data")
@Data
@Accessors(chain = true)
public class YbChsApplyData implements Serializable, Comparable<YbChsApplyData> {

    private static final long serialVersionUID = 1L;

    /**
     * id
     */
    @ExcelField(value = "id")
    private String id;

    /**
     * 申请pid
     */
    @ExcelField(value = "申请pid")
    private String pid;

    /**
     * 申诉截止日期
     */
    @TableField("appealEndDate")
    @ExcelField(value = "申诉截止日期")
    private Date appealEndDate;
    private transient String appealEndDateFrom;
    private transient String appealEndDateTo;

    /**
     * 支付地点类别
     */
    @TableField("payPlaceType")
    @ExcelField(value = "支付地点类别")
    private String payPlaceType;

    /**
     * 疑点状态
     */
    @TableField("ydState")
    @ExcelField(value = "疑点状态")
    private String ydState;

    /**
     * 医保区划
     */
    @TableField("areaName")
    @ExcelField(value = "医保区划")
    private String areaName;

    /**
     * 医药机构编码
     */
    @TableField("yyjgCode")
    @ExcelField(value = "医药机构编码")
    private String yyjgCode;

    /**
     * 医药机构名称
     */
    @TableField("yyjgName")
    @ExcelField(value = "医药机构名称")
    private String yyjgName;

    /**
     * 科室名称
     */
    @TableField("deptName")
    @ExcelField(value = "科室名称")
    private String deptName;

    /**
     * 医生姓名
     */
    @TableField("doctorName")
    @ExcelField(value = "医生姓名")
    private String doctorName;

    /**
     * 医疗类别
     */
    @TableField("medicalType")
    @ExcelField(value = "医疗类别")
    private String medicalType;

    /**
     * 住院门诊号
     */
    @TableField("zymzNumber")
    @ExcelField(value = "住院门诊号")
    private String zymzNumber;

    /**
     * 参保人
     */
    @TableField("insuredName")
    @ExcelField(value = "参保人")
    private String insuredName;

    /**
     * 入院日期
     */
    @TableField("enterHospitalDate")
    @ExcelField(value = "入院日期")
    private Date enterHospitalDate;
    private transient String enterHospitalDateFrom;
    private transient String enterHospitalDateTo;

    /**
     * 出院日期
     */
    @TableField("outHospitalDate")
    @ExcelField(value = "出院日期")
    private Date outHospitalDate;
    private transient String outHospitalDateFrom;
    private transient String outHospitalDateTo;

    /**
     * 结算日期
     */
    @TableField("settlementDate")
    @ExcelField(value = "结算日期")
    private Date settlementDate;
    private transient String settlementDateFrom;
    private transient String settlementDateTo;

    /**
     * 身份证号
     */
    @TableField("cardNumber")
    @ExcelField(value = "身份证号")
    private String cardNumber;

    /**
     * 医保项目编码
     */
    @TableField("projectCode")
    @ExcelField(value = "医保项目编码")
    private String projectCode;

    /**
     * 医保项目名称
     */
    @TableField("projectName")
    @ExcelField(value = "医保项目名称")
    private String projectName;

    /**
     * 医院项目名称
     */
    @TableField("projectYyName")
    @ExcelField(value = "医院项目名称")
    private String projectYyName;

    /**
     * 规则名称
     */
    @TableField("ruleName")
    @ExcelField(value = "规则名称")
    private String ruleName;

    /**
     * 初审违规金额
     */
    @TableField("violateCsPrice")
    @ExcelField(value = "初审违规金额")
    private BigDecimal violateCsPrice;

    /**
     * 违规金额
     */
    @TableField("violatePrice")
    @ExcelField(value = "违规金额")
    private BigDecimal violatePrice;

    /**
     * 违规内容
     */
    @TableField("violateReason")
    @ExcelField(value = "违规内容")
    private String violateReason;

    /**
     * 诊断信息
     */
    @TableField("zdNote")
    @ExcelField(value = "诊断信息")
    private String zdNote;

    /**
     * 费用日期
     */
    @TableField("costDate")
    @ExcelField(value = "费用日期")
    private Date costDate;
    private transient String costDateFrom;
    private transient String costDateTo;

    /**
     * 险种类型
     */
    @TableField("insuredType")
    @ExcelField(value = "险种类型")
    private String insuredType;

    /**
     * 数量
     */
    @ExcelField(value = "数量")
    private BigDecimal num;

    /**
     * 单价
     */
    @ExcelField(value = "单价")
    private BigDecimal price;

    /**
     * 金额
     */
    @TableField("medicalPrice")
    @ExcelField(value = "金额")
    private BigDecimal medicalPrice;

    /**
     * 统筹支付
     */
    @TableField("tcPayPrice")
    @ExcelField(value = "统筹支付")
    private BigDecimal tcPayPrice;

    /**
     * 规格
     */
    @ExcelField(value = "规格")
    private String specs;

    /**
     * 剂型
     */
    @ExcelField(value = "剂型")
    private String jx;

    /**
     * 机构等级
     */
    @TableField("jgLevel")
    @ExcelField(value = "机构等级")
    private String jgLevel;

    /**
     * 备注
     */
    @TableField("COMMENTS")
    @ExcelField(value = "备注")
    private String comments;

    /**
     * 状态
     */
    @TableField("STATE")
    @ExcelField(value = "状态")
    private Integer state;

    /**
     * 是否删除
     */
    @TableField("IS_DELETEMARK")
    @ExcelField(value = "是否删除")
    private Integer isDeletemark;

    /**
     * 序号
     */
    @TableField("orderNum")
    @ExcelField(value = "序号")
    private Integer orderNum;

    /**
     * 是否门诊
     */
    @TableField("isOutpfees")
    @ExcelField(value = "是否门诊")
    private Integer isOutpfees;

    /**
     * 结算日期排序
     */
    @TableField("orderSettlementNum")
    @ExcelField(value = "结算日期排序")
    private Integer orderSettlementNum;

    DateFormat formater = new SimpleDateFormat("yyyy-MM-dd");

    public String getAppealEndDateStr() {
        return this.getAppealEndDate() != null ? formater.format(this.getAppealEndDate()) : null;
    }

    public String getEnterHospitalDateStr() {
        return this.getEnterHospitalDate() != null ? formater.format(this.getEnterHospitalDate()) : null;
    }

    public String getOutHospitalDateStr() {
        return this.getOutHospitalDate() != null ? formater.format(this.getOutHospitalDate()) : null;
    }

    public String getSettlementDateStr() {
        return this.getSettlementDate() != null ? formater.format(this.getSettlementDate()) : null;
    }

    public String getCostDateStr() {
        return this.getCostDate() != null ? formater.format(this.getCostDate()) : null;
    }

    private transient String appealEndDateStr;
    private transient String enterHospitalDateStr;
    private transient String outHospitalDateStr;
    private transient String settlementDateStr;
    private transient String costDateStr;


    public static final String ID = "id";

    public static final String PID = "pid";

    public static final String APPEALENDDATE = "appealEndDate";

    public static final String PAYPLACETYPE = "payPlaceType";

    public static final String YDSTATE = "ydState";

    public static final String AREANAME = "areaName";

    public static final String YYJGCODE = "yyjgCode";

    public static final String YYJGNAME = "yyjgName";

    public static final String DEPTNAME = "deptName";

    public static final String DOCTORNAME = "doctorName";

    public static final String MEDICALTYPE = "medicalType";

    public static final String ZYMZNUMBER = "zymzNumber";

    public static final String INSUREDNAME = "insuredName";

    public static final String ENTERHOSPITALDATE = "enterHospitalDate";

    public static final String OUTHOSPITALDATE = "outHospitalDate";

    public static final String SETTLEMENTDATE = "settlementDate";

    public static final String CARDNUMBER = "cardNumber";

    public static final String PROJECTCODE = "projectCode";

    public static final String PROJECTNAME = "projectName";

    public static final String PROJECTYYNAME = "projectYyName";

    public static final String RULENAME = "ruleName";

    public static final String VIOLATECSPRICE = "violateCsPrice";

    public static final String VIOLATEPRICE = "violatePrice";

    public static final String VIOLATEREASON = "violateReason";

    public static final String ZDNOTE = "zdNote";

    public static final String COSTDATE = "costDate";

    public static final String INSUREDTYPE = "insuredType";

    public static final String NUM = "num";

    public static final String PRICE = "price";

    public static final String MEDICALPRICE = "medicalPrice";

    public static final String TCPAYPRICE = "tcPayPrice";

    public static final String SPECS = "specs";

    public static final String JX = "jx";

    public static final String JGLEVEL = "jgLevel";

    public static final String COMMENTS = "COMMENTS";

    public static final String STATE = "STATE";

    public static final String IS_DELETEMARK = "IS_DELETEMARK";

    public static final String ORDERNUM = "orderNum";

    public static final String ISOUTPFEES = "isOutpfees";

    public static final String ORDERSETTLEMENTNUM = "orderSettlementNum";

    @Override
    public int compareTo(YbChsApplyData o) {
        if (this.getId() != null && o.getId() != null) {
            return this.getId().compareTo(o.getId());
        } else if (this.getId() != null) {
            return 1;
        } else {
            return 0;
        }
    }
}