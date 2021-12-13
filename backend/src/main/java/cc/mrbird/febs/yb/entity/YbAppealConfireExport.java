package cc.mrbird.febs.yb.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.wuwenze.poi.annotation.Excel;
import com.wuwenze.poi.annotation.ExcelField;
import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.util.Date;

/**
 * <p>
 *
 * </p>
 *
 * @author viki
 * @since 2021-01-11
 */

@Excel("YbAppealConfireExport")
@Data
@Accessors(chain = true)
public class YbAppealConfireExport{

    /**
     * 医生编码
     */
    @ExcelField(value = "医生编码")
    private String doctorCode;

    /**
     * 医生名称
     */
    @ExcelField(value = "医生名称")
    private String doctorName;

    /**
     * 管理员类型
     */
    @ExcelField(value = "管理员类型")
    private String adminTypeName;

    /**
     * 管理科室
     */
    @ExcelField(value = "管理科室")
    private String deptNames;

    /**
     * 操作员
     */
    @ExcelField(value = "操作员")
    private String operatorName;

}