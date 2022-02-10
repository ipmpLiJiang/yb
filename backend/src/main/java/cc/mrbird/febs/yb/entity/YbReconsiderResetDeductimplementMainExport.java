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

@Excel("YbReconsiderResetDeductimplementMainExport")
@Data
//@Accessors(chain = true)
public class YbReconsiderResetDeductimplementMainExport {

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
     * 规则名称
     */
    @ExcelField(value = "规则名称")
    private String ruleName;

    /**
     * 医保内金额
     */
    @ExcelField(value = "医保内金额")
    private BigDecimal medicalPrice;

    /**
     * 扣除金额
     */
    @ExcelField(value = "扣除金额")
    private BigDecimal deductPrice;

    /**
     * 结算日期Str
     */
    @ExcelField(value = "结算日期")
    private String settlementDateStr;

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
     * 参保类型
     */
    @ExcelField(value = "参保类型")
    private String insuredType;

    /**
     * 统筹区名称
     */
    @ExcelField(value = "统筹区名称")
    private String areaName;

    /**
     * 大专业
     */
    @ExcelField(value = "大专业")
    private String dksName;

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

    /**
     * 绩效年月
     */
    @ExcelField(value = "绩效年月")
    private String implementDateStr;

    /**
     * '分摊方式'
     */
    @ExcelField(value = "分摊方式")
    private String shareStateStr;

    /**
     * ''分摊方案''
     */
    @ExcelField(value = "分摊方案")
    private String shareProgramme;

}