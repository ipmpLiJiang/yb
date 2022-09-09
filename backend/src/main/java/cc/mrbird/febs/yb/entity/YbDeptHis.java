package cc.mrbird.febs.yb.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.wuwenze.poi.annotation.ExcelField;
import lombok.Data;

import java.io.Serializable;


@Data
public class YbDeptHis implements Serializable {


    /**
     * 病区编码
     */
    @TableField("deptId")
    @ExcelField(value = "病区编码")
    private String deptId;

    /**
     * 病区名称
     */
    @TableField("deptName")
    @ExcelField(value = "病区名称")
    private String deptName;

    /**
     * 拼写代码
     */
    @TableField("spellCode")
    @ExcelField(value = "拼写代码")
    private String spellCode;

    /**
     * 大科室编码
     */
    @TableField("parentCode")
    @ExcelField(value ="大科室编码")
    private String parentCode;

    /**
     * 大科室名称
     */
    @TableField("bm_mc")
    @ExcelField(value ="大科室名称")
    private String bm_mc;

    /**
     * 分院
     */
    @TableField("fy_id")
    @ExcelField(value ="分院")
    private String fy_id;

    /**
     * 肿瘤
     */
    @TableField("fb_id")
    @ExcelField(value ="肿瘤")
    private String fb_id;

}
