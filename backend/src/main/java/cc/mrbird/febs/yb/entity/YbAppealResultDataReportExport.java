package cc.mrbird.febs.yb.entity;

import com.wuwenze.poi.annotation.Excel;
import com.wuwenze.poi.annotation.ExcelField;
import lombok.Data;

import java.math.BigDecimal;

@Excel("YbAppealResultDataExport")
@Data
public class YbAppealResultDataReportExport {

    @ExcelField(value ="复议年月")
    private String applyDateStr;
    //序号
    @ExcelField(value ="序号")
    private String orderNumber;
    /**
     * 交易流水号
     */
    @ExcelField(value ="交易流水号")
    private String serialNo;
    /**
     * 单据号
     */
    @ExcelField(value ="单据号")
    private String billNo;
    /**
     * 意见书编码
     */
    @ExcelField(value ="意见书编码")
    private String proposalCode;
    /**
     * 项目编码
     */
    @ExcelField(value ="项目编码")
    private String projectCode;
    /**
     * 项目名称
     */
    @ExcelField(value ="项目名称")
    private String projectName;
    /**
     * 数量
     */
    @ExcelField(value ="数量")
    private BigDecimal num;
    /**
     * 医保内金额
     */
    @ExcelField(value ="医保内金额")
    private BigDecimal medicalPrice;
    /**
     * 规则名称
     */
    @ExcelField(value ="规则名称")
    private String ruleName;
    /**
     * 扣除金额
     */
    @ExcelField(value ="扣除金额")
    private BigDecimal deductPrice;
    /**
     * 扣除原因
     */
    @ExcelField(value ="扣除原因")
    private String deductReason;
    /**
     * 还款原因
     */
    @ExcelField(value ="还款原因")
    private String repaymentReason;
    /**
     * 医生姓名
     */
    @ExcelField(value ="医生姓名")
    private String doctorName;
    /**
     * 科室编码
     */
    @ExcelField(value ="科室编码")
    private String deptCode;
    /**
     * 科室名称
     */
    @ExcelField(value ="科室名称")
    private String deptName;

    /**
     * 入院日期
     */
    //private Date enterHospitalDate;
    @ExcelField(value ="入院日期")
    private String enterHospitalDateStr;
    /**
     * 出院日期
     */
    //private Date outHospitalDate;
    @ExcelField(value ="出院日期")
    private String outHospitalDateStr;
    /**
     * 费用日期
     */
    //private Date costDate;
    @ExcelField(value ="费用日期")
    private String costDateStr;
    /**
     * 住院号
     */
    @ExcelField(value ="住院号")
    private String hospitalizedNo;
    /**
     * 就医方式
     */
    @ExcelField(value ="就医方式")
    private String treatmentMode;
    /**
     * 结算日期
     */
    //private Date settlementDate;
    @ExcelField(value ="结算日期")
    private String settlementDateStr;
    /**
     * 个人编号
     */
    @ExcelField(value ="个人编号")
    private String personalNo;
    /**
     * 参保人姓名
     */
    @ExcelField(value ="参保人姓名")
    private String insuredName;
    /**
     * 医保卡号
     */
    @ExcelField(value ="医保卡号")
    private String cardNumber;
    /**
     * 统筹区名称
     */
    @ExcelField(value ="统筹区名称")
    private String areaName;
    /**
     * 版本号
     */
    @ExcelField(value ="版本号")
    private String versionNumber;
    /**
     * 反馈申诉
     */
    @ExcelField(value ="反馈申诉")
    private String backAppeal;


}