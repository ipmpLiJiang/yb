package cc.mrbird.febs.drg.entity;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import com.baomidou.mybatisplus.annotation.TableField;

import java.io.Serializable;


import lombok.Data;
import lombok.experimental.Accessors;

import java.util.Date;

import com.wuwenze.poi.annotation.Excel;
import com.wuwenze.poi.annotation.ExcelField;

/**
 * <p>
 *
 * </p>
 *
 * @author viki
 * @since 2021-11-23
 */

@Excel("yb_drg_jk")
@Data
@Accessors(chain = true)
public class YbDrgJk implements Serializable, Comparable<YbDrgJk> {

    private static final long serialVersionUID = 1L;

    /**
     * drg接口
     */
    @ExcelField(value = "drg接口")
    private String id;

    /**
     * 复议年月Str
     */
    @TableField("applyDateStr")
    @ExcelField(value = "复议年月Str")
    private String applyDateStr;

    /**
     * 序号
     */
    @TableField("orderNumber")
    @ExcelField(value = "序号")
    private String orderNumber;

    /**
     * 排序
     */
    @TableField("orderNum")
    @ExcelField(value = "排序")
    private Integer orderNum;

    /**
     * drg申请明细
     */
    @TableField("applyDataId")
    @ExcelField(value = "drg申请明细")
    private String applyDataId;

    /**
     * 院区
     */
    @TableField("areaType")
    @ExcelField(value = "院区")
    private Integer areaType;

    /**
     * 入院日期
     */
    @TableField("ryDate")
    @ExcelField(value = "入院日期")
    private Date ryDate;
    private transient String ryDateFrom;
    private transient String ryDateTo;

    /**
     * 出院日期
     */
    @TableField("cyDate")
    @ExcelField(value = "出院日期")
    private Date cyDate;
    private transient String cyDateFrom;
    private transient String cyDateTo;

    /**
     * 统筹支付
     */
    @TableField("tczf")
    @ExcelField(value = "统筹支付")
    private BigDecimal tczf;

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
     * 院区
     */
    @TableField("yq")
    @ExcelField(value = "院区")
    private String yq;

    /**
     * 科室编码
     */
    @TableField("deptId")
    @ExcelField(value = "科室编码")
    private String deptId;

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
     * 医疗组医师科室Id
     */
    @TableField("ylzDeptId")
    @ExcelField(value = "医疗组医师科室Id")
    private String ylzDeptId;

    /**
     * 医疗组医师科室名称
     */
    @TableField("ylzDeptName")
    @ExcelField(value = "医疗组医师科室名称")
    private String ylzDeptName;


    public static final String ID = "id";

    public static final String APPLYDATESTR = "applyDateStr";

    public static final String ORDERNUMBER = "orderNumber";

    public static final String ORDERNUM = "orderNum";

    public static final String APPLYDATAID = "applyDataId";

    public static final String AREATYPE = "areaType";

    public static final String TCZF = "tczf";

    public static final String RYDATE = "ryDate";

    public static final String CYDATE = "cyDate";

    public static final String FZCODE = "fzCode";

    public static final String FZNAME = "fzName";

    public static final String ZYZDCODE = "zyzdCode";

    public static final String ZYZDNAME = "zyzdName";

    public static final String ZSSCODE = "zssCode";

    public static final String ZSSNAME = "zssName";

    public static final String QTZDCODE = "qtzdCode";

    public static final String QTZDNAME = "qtzdName";

    public static final String QTSSCODE = "qtssCode";

    public static final String QTSSNAME = "qtssName";

    public static final String DEPTID = "deptId";

    public static final String DEPTNAME = "deptName";

    public static final String AREAID = "areaId";

    public static final String AREANAME = "areaName";

    public static final String QZ = "qz";

    public static final String KZRDOCID = "kzrDocId";

    public static final String KZRDOCNAME = "kzrDocName";

    public static final String ZRYSDOCID = "zrysDocId";

    public static final String ZRYSDOCNAME = "zrysDocName";

    public static final String ZZYSDOCID = "zzysDocId";

    public static final String ZZYSDOCNAME = "zzysDocName";

    public static final String ZYYSDOCID = "zyysDocId";

    public static final String ZYYSDOCNAME = "zyysDocName";

    public static final String YLZDEPTID = "ylzDeptId";

    public static final String YLZDEPTNAME = "ylzDeptName";

    public static final String YLZDOCID = "ylzDocId";

    public static final String YLZDOCNAME = "ylzDocName";


    @Override
    public int compareTo(YbDrgJk o) {
        if (this.getId() != null && o.getId() != null) {
            return this.getId().compareTo(o.getId());
        } else if (this.getId() != null) {
            return 1;
        } else {
            return 0;
        }
    }
}