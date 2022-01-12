package cc.mrbird.febs.drg.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.wuwenze.poi.annotation.ExcelField;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * <p>
 *
 * </p>
 *
 * @author viki
 * @since 2021-11-23
 */

@Data
public class YbDrgJkData implements Serializable {

    /**
     * drg接口
     */
    @TableField("drgNo")
    @ExcelField(value = "drg接口")
    private String drgNo;

    /**
     * 就诊记录号
     */
    @TableField("jzjlh")
    @ExcelField(value = "就诊记录号")
    private String jzjlh;

    /**
     * 病案号
     */
    @TableField("bah")
    @ExcelField(value = "病案号")
    private String bah;

    /**
     * 统筹支付
     */
    @TableField("tczf")
    @ExcelField(value = "统筹支付")
    private BigDecimal tczf;

    /**
     * 入院日期
     */
    @TableField("ryDate")
    @ExcelField(value = "入院日期")
    private Date ryDate;

    /**
     * 出院日期
     */
    @TableField("cyDate")
    @ExcelField(value = "出院日期")
    private Date cyDate;


    /**
     * DRG管理系统-DRG分组编码
     */
    @TableField("fzCode")
    @ExcelField(value = "DRG管理系统-DRG分组编码")
    private String fzCode;

    /**
     * DRG管理系统-DRG分组名称
     */
    @TableField("fzName")
    @ExcelField(value = "DRG管理系统-DRG分组名称")
    private String fzName;

    /**
     * DRG管理系统-医保主要诊断编码
     */
    @TableField("zyzdCode")
    @ExcelField(value = "DRG管理系统-医保主要诊断编码")
    private String zyzdCode;

    /**
     * DRG管理系统-医保主要诊断名称
     */
    @TableField("zyzdName")
    @ExcelField(value = "DRG管理系统-医保主要诊断名称")
    private String zyzdName;

    /**
     * DRG管理系统-医保主手术编码
     */
    @TableField("zssCode")
    @ExcelField(value = "DRG管理系统-医保主手术编码")
    private String zssCode;

    /**
     * DRG管理系统-医保主手术名称
     */
    @TableField("zssName")
    @ExcelField(value = "DRG管理系统-医保主手术名称")
    private String zssName;

    /**
     * DRG管理系统-其他诊断编码
     */
    @TableField("qtzdCode")
    @ExcelField(value = "DRG管理系统-其他诊断编码")
    private String qtzdCode;

    /**
     * DRG管理系统-其他诊断名称
     */
    @TableField("qtzdName")
    @ExcelField(value = "DRG管理系统-其他诊断名称")
    private String qtzdName;

    /**
     * DRG管理系统-其他手术编码
     */
    @TableField("qtssCode")
    @ExcelField(value = "DRG管理系统-其他手术编码")
    private String qtssCode;

    /**
     * DRG管理系统-其他手术名称
     */
    @TableField("qtssName")
    @ExcelField(value = "DRG管理系统-其他手术名称")
    private String qtssName;

    /**
     * 科室名称
     */
    @TableField("deptName")
    @ExcelField(value = "科室名称")
    private String deptName;

    /**
     * 病区编码
     */
    @TableField("areaId")
    @ExcelField(value = "病区编码")
    private String areaId;

    /**
     * 病区名称
     */
    @TableField("areaName")
    @ExcelField(value = "病区名称")
    private String areaName;

    /**
     * 权重
     */
    @TableField("qz")
    @ExcelField(value = "权重")
    private BigDecimal qz;

    /**
     * 科主任编码
     */
    @TableField("kzrDocId")
    @ExcelField(value = "科主任编码")
    private String kzrDocId;

    /**
     * 科主任名称
     */
    @TableField("kzrDocName")
    @ExcelField(value = "科主任姓名")
    private String kzrDocName;

    /**
     * 主任医师编码
     */
    @TableField("zrysDocId")
    @ExcelField(value = "主任医师编码")
    private String zrysDocId;

    /**
     * 主任医师名称
     */
    @TableField("zrysDocName")
    @ExcelField(value = "主任医师姓名")
    private String zrysDocName;

    /**
     * 主治医师编码
     */
    @TableField("zzysDocId")
    @ExcelField(value = "主治医师编码")
    private String zzysDocId;

    /**
     * 主治医师名称
     */
    @TableField("zzysDocName")
    @ExcelField(value = "主治医师姓名")
    private String zzysDocName;

    /**
     * 住院医师编码
     */
    @TableField("zyysDocId")
    @ExcelField(value = "住院医师编码")
    private String zyysDocId;

    /**
     * 住院医师名称
     */
    @TableField("zyysDocName")
    @ExcelField(value = "住院医师姓名")
    private String zyysDocName;

    /**
     * 医疗组住院医师编码
     */
    @TableField("ylzDocId")
    @ExcelField(value = "医疗组医师编码")
    private String ylzDocId;

    /**
     * 医疗组医师名称
     */
    @TableField("ylzDocName")
    @ExcelField(value = "医疗组医师姓名")
    private String ylzDocName;

    /**
     * 医疗组医师科室名称
     */
    @TableField("ylzDeptName")
    @ExcelField(value = "医疗组医师科室名称")
    private String ylzDeptName;


}