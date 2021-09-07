package cc.mrbird.febs.yb.entity;

import com.wuwenze.poi.annotation.Excel;
import com.wuwenze.poi.annotation.ExcelField;
import lombok.Data;

import java.math.BigDecimal;

/**
 * <p>
 *
 * </p>
 *
 * @author viki
 * @since 2020-08-17
 */

@Excel("YbAppealResultResetDataExport")
@Data
//@Accessors(chain = true)
public class YbAppealResultResetDataExport {

    /**
     * 序号
     */
    @ExcelField(value = "序号")
    private String orderNumber;

    /**
     * 交易流水号
     */
    @ExcelField(value = "交易流水号")
    private String serialNo;

    /**
     * 单据号
     */
    @ExcelField(value = "单据号")
    private String billNo;

    /**
     * 项目编码
     */
    @ExcelField(value = "项目编码")
    private String projectCode;

    /**
     * 项目名称
     */
    @ExcelField(value = "项目名称")
    private String projectName;

    /**
     * 医保内金额
     */
    @ExcelField(value = "医保内金额")
    private BigDecimal medicalPrice;

    /**
     * 规则名称
     */
    @ExcelField(value = "规则名称")
    private String ruleName;

    /**
     * 扣除金额
     */
    @ExcelField(value = "扣除金额")
    private BigDecimal deductPrice;

    /**
     * 扣除原因
     */
    @ExcelField(value = "扣除原因")
    private String deductReason;

    /**
     * 还款原因
     */
    @ExcelField(value = "还款原因")
    private String repaymentReason;

    /**
     * 医生姓名
     */
    @ExcelField(value = "医生姓名")
    private String doctorName;

    /**
     * 科室编码
     */
    @ExcelField(value = "科室编码")
    private String deptCode;

    /**
     * 科室名称
     */
    @ExcelField(value = "科室名称")
    private String deptName;

    /**
     * 费用日期str
     */
    @ExcelField(value = "费用日期")
    private String costDateStr;

    /**
     * 住院号
     */
    @ExcelField(value = "住院号")
    private String hospitalizedNo;

    /**
     * 就医方式
     */
    @ExcelField(value = "就医方式")
    private String treatmentMode;

    /**
     * 结算日期Str
     */
    @ExcelField(value = "结算日期")
    private String settlementDateStr;

    /**
     * 个人编号
     */
    @ExcelField(value = "个人编号")
    private String personalNo;

    /**
     * 参保人姓名
     */
    @ExcelField(value = "参保人姓名")
    private String insuredName;

    /**
     * 统筹区名称
     */
    @ExcelField(value = "统筹区名称")
    private String areaName;

    /**
     * 反馈申诉
     */
    @ExcelField(value = "反馈申诉")
    private String backAppeal;

    /**
     * 科室
     */
    @ExcelField(value ="复议科室名称")
    private String resultDeptName;

    /**
     * 医生
     */
    @ExcelField(value ="复议医生姓名")
    private String resultDoctorName;



}