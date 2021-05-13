package cc.mrbird.febs.yb.entity;

import com.wuwenze.poi.annotation.Excel;
import com.wuwenze.poi.annotation.ExcelField;
import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;

/**
 * <p>
 *
 * </p>
 *
 * @author viki
 * @since 2020-08-17
 */

@Excel("YbReconsiderResetDeductimplement")
@Data
//@Accessors(chain = true)
public class YbReconsiderResetDeductimplement {

    /**
     * 序号
     */
    @ExcelField(value = "序号")
    private String orderNumber;

    /**
     * 绩效年月
     */
    @ExcelField(value = "绩效年月")
    private String implementDateStr;

    /**
     * 绩效年月
     */
    @ExcelField(value = "绩效年月")
    private Date implementDate;

    /**
     * 分摊方式
     */
    @ExcelField(value = "分摊方式")
    private String shareStateStr;

    /**
     * 分摊方式
     */
    @ExcelField(value = "分摊方式")
    private Integer shareState;

    /**
     * 分摊方案
     */
    @ExcelField(value = "分摊方案")
    private String shareProgramme;

    /**
     * 数据类型
     */
    @ExcelField(value = "数据类型")
    private Integer dataType;



}