package cc.mrbird.febs.drg.entity;

import com.wuwenze.poi.annotation.Excel;
import com.wuwenze.poi.annotation.ExcelField;
import lombok.Data;

import java.math.BigDecimal;

@Excel("YbReconsiderApplyDataResult")
@Data
public class YbDrgApplyDataResultYj {

    /**
     * 就诊记录号
     */
    @ExcelField(value = "就诊记录号")
    private String jzjlh;

    /**
     * 病案号
     */
    @ExcelField(value = "病案号")
    private String bah;


    /**
     * 审核意见
     */
    @ExcelField(value = "审核意见")
    private String operateReason;



}