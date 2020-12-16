package cc.mrbird.febs.yb.entity;

import com.wuwenze.poi.annotation.Excel;
import com.wuwenze.poi.annotation.ExcelField;
import lombok.Data;

import java.math.BigDecimal;

@Excel("YbReconsiderRepayDataExport")
@Data
public class YbReconsiderRepayDataExport {

    //序号
    @ExcelField(value = "序号")
    private String orderNumber;

    /**
     * 所属期Str
     */
    @ExcelField(value = "所属期")
    private String belongDateStr;

    /**
     * 单据号
     */
    @ExcelField(value = "单据号")
    private String billNo;
    /**
     * 项目名称
     */
    @ExcelField(value = "项目名称")
    private String projectName;
    /**
     * 扣除金额
     */
    @ExcelField(value = "扣除金额")
    private BigDecimal deductPrice;
    /**
     * 费用日期
     */
    //private Date costDate;
    @ExcelField(value = "费用日期")
    private String costDateStr;

    /**
     * 还款金额
     */
    @ExcelField(value = "还款金额")
    private BigDecimal repaymentPrice;

}