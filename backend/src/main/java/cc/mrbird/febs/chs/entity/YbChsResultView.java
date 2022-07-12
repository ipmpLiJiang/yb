package cc.mrbird.febs.chs.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.wuwenze.poi.annotation.ExcelField;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

/**
 * <p>
 * VIEW
 * </p>
 *
 * @author viki
 * @since 2020-07-30
 */

@Data
public class YbChsResultView {

    /**
     * pid
     */
    @TableField("pid")
    @ExcelField(value = "pid")
    private String pid;

    /**
     * 复议年月Str
     */
    @TableField("applyDateStr")
    @ExcelField(value = "复议年月Str")
    private String applyDateStr;

    /**
     * 结果Id
     */
    @ExcelField(value = "结果Id")
    private String id;

    /**
     * 申请数据明细
     */
    @TableField("applyDataId")
    @ExcelField(value = "申请数据明细")
    private String applyDataId;


    /**
     * 复议医生编码
     */
    @TableField("resultDoctorCode")
    @ExcelField(value = "复议医生编码")
    private String resultDoctorCode;

    /**
     * 复议医生
     */
    @TableField("resultDoctorName")
    @ExcelField(value = "复议医生")
    private String resultDoctorName;

    /**
     * 科室编码
     */
    @TableField("resultDksId")
    @ExcelField(value = "科室编码")
    private String resultDksId;

    /**
     * 科室
     */
    @ExcelField(value = "科室")
    private String resultDksName;


    /**
     * 复议截止日期
     */
    @TableField("applyEndDate")
    @ExcelField(value = "复议截止日期")
    private Date applyEndDate;

    /**
     * 操作理由
     */
    @TableField("operateReason")
    @ExcelField(value = "操作理由")
    private String operateReason;

    /**
     * 操作日期
     */
    @TableField("operateDate")
    @ExcelField(value = "操作日期")
    private Date operateDate;
    private transient String operateDateFrom;
    private transient String operateDateTo;


    /**
     * 序号
     */
    @TableField("orderNum")
    @ExcelField(value = "序号")
    private Integer orderNum;


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
     * 院区
     */
    @TableField("areaType")
    @ExcelField(value = "院区")
    private Integer areaType;

    /**
     * 申诉截止日期
     */
    @TableField("appealEndDate")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @JsonFormat(timezone = "Asia/Shanghai", pattern = "yyyy-MM-dd")
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
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @JsonFormat(timezone = "Asia/Shanghai", pattern = "yyyy-MM-dd")
    @ExcelField(value = "入院日期")
    private Date enterHospitalDate;

    /**
     * 出院日期
     */
    @TableField("outHospitalDate")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @JsonFormat(timezone = "Asia/Shanghai", pattern = "yyyy-MM-dd")
    @ExcelField(value = "出院日期")
    private Date outHospitalDate;

    /**
     * 出院日期Str
     */
    @ExcelField(value = "出院日期Str")
    private String outHospitalDateStr;

    /**
     * 结算日期
     */
    @TableField("settlementDate")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @JsonFormat(timezone = "Asia/Shanghai", pattern = "yyyy-MM-dd")
    @ExcelField(value = "结算日期")
    private Date settlementDate;

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
     * 初审违规金额Str
     */
    @TableField("violateCsPriceStr")
    @ExcelField(value = "初审违规金额Str")
    private String violateCsPriceStr;

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
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @JsonFormat(timezone = "Asia/Shanghai", pattern = "yyyy-MM-dd")
    private Date costDate;

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


    private transient String fileSizeWork;

    private transient String fileNumber;



}