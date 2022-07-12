package cc.mrbird.febs.chs.entity;

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
 * @since 2022-06-27
 */
@Excel("YbChsApplyDataResult")
@Data
public class YbChsApplyDataResult {

    /**
     * 住院门诊号
     */
    @ExcelField(value = "住院门诊号")
    private String zymzNumber;

    /**
     * 医保项目名称
     */
    @ExcelField(value = "医保项目名称")
    private String projectName;

    /**
     * 初审违规金额
     */
    @ExcelField(value = "初审违规金额（元）")
    private String violateCsPriceStr;

    /**
     * 出院日期
     */
    @ExcelField(value = "出院日期")
    private String outHospitalDateStr;


    /**
     * 复议理由
     */
    @ExcelField(value = "复议理由")
    private String fyly;


    /**
     * 文件名称
     */
    @ExcelField(value = "文件名称")
    private String wjmc;


    /**
     * 是否上传成功
     */
    @ExcelField(value = "是否上传成功")
    private String iscg;

    /**
     * 上传未成功原因
     */
    @ExcelField(value = "上传未成功原因")
    private String wcgyy;
}