package cc.mrbird.febs.drg.entity;

import com.wuwenze.poi.annotation.Excel;
import com.wuwenze.poi.annotation.ExcelField;
import lombok.Data;

@Excel("YbReconsiderApplyDataResult")
@Data
public class YbDrgApplyDataResultWj {


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
     * 编辑上传类型
     */
    @ExcelField(value = "编辑上传类型")
    private String typeName;

    /**
     * 文件名称
     */
    @ExcelField(value = "文件名称")
    private String fileName;

}