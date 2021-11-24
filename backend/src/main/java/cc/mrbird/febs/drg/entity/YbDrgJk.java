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
public class YbDrgJk implements Serializable , Comparable<YbDrgJk>{

private static final long serialVersionUID=1L;

    /**
     * drg接口
     */
                                @ExcelField(value ="drg接口")
    private String id;

    /**
     * 复议年月Str
     */
    @TableField("applyDateStr")
            @ExcelField(value ="复议年月Str")
    private String applyDateStr;

    /**
     * 序号
     */
    @TableField("orderNumber")
            @ExcelField(value ="序号")
    private String orderNumber;

    /**
     * drg申请明细
     */
    @TableField("applyDataId")
            @ExcelField(value ="drg申请明细")
    private String applyDataId;

    /**
     * 院区
     */
    @TableField("areaType")
            @ExcelField(value ="院区")
    private Integer areaType;

    /**
     * 入院日期
     */
            @ExcelField(value ="入院日期")
    private Date ryrq;
    private transient String ryrqFrom;
    private transient String ryrqTo;

    /**
     * 出院日期
     */
            @ExcelField(value ="出院日期")
    private Date cyrq;
    private transient String cyrqFrom;
    private transient String cyrqTo;

    /**
     * DRG管理系统-DRG分组编码
     */
            @ExcelField(value ="DRG管理系统-DRG分组编码")
    private String fzbm;

    /**
     * DRG管理系统-DRG分组名称
     */
            @ExcelField(value ="DRG管理系统-DRG分组名称")
    private String fzmc;

    /**
     * DRG管理系统-医保主要诊断编码
     */
            @ExcelField(value ="DRG管理系统-医保主要诊断编码")
    private String zyzdbm;

    /**
     * DRG管理系统-医保主要诊断名称
     */
            @ExcelField(value ="DRG管理系统-医保主要诊断名称")
    private String zyzdmc;

    /**
     * DRG管理系统-医保主手术编码
     */
            @ExcelField(value ="DRG管理系统-医保主手术编码")
    private String zssbm;

    /**
     * DRG管理系统-医保主手术名称
     */
            @ExcelField(value ="DRG管理系统-医保主手术名称")
    private String zssmc;

    /**
     * DRG管理系统-其他诊断编码
     */
            @ExcelField(value ="DRG管理系统-其他诊断编码")
    private String qtzdbm;

    /**
     * DRG管理系统-其他诊断名称
     */
            @ExcelField(value ="DRG管理系统-其他诊断名称")
    private String qtzdmc;

    /**
     * DRG管理系统-其他手术编码
     */
            @ExcelField(value ="DRG管理系统-其他手术编码")
    private String qtssbm;

    /**
     * DRG管理系统-其他手术名称
     */
            @ExcelField(value ="DRG管理系统-其他手术名称")
    private String qtssmc;

    /**
     * 科室编码
     */
            @ExcelField(value ="科室编码")
    private String ksbm;

    /**
     * 病区编码
     */
            @ExcelField(value ="病区编码")
    private String bqbm;

    /**
     * 权重
     */
            @ExcelField(value ="权重")
    private BigDecimal qz;

    /**
     * 科主任编码
     */
            @ExcelField(value ="科主任编码")
    private String kzrbm;

    /**
     * 主任医师编码
     */
            @ExcelField(value ="主任医师编码")
    private String zrysbm;

    /**
     * 主治医师编码
     */
            @ExcelField(value ="主治医师编码")
    private String zzysbm;

    /**
     * 住院医师编码
     */
            @ExcelField(value ="住院医师编码")
    private String zyysbm;

    /**
     * 科室名称
     */
            @ExcelField(value ="科室名称")
    private String ksmc;

    /**
     * 科主任名称
     */
            @ExcelField(value ="科主任名称")
    private String kzrmc;

    /**
     * 主任医师名称
     */
            @ExcelField(value ="主任医师名称")
    private String zrysmc;

    /**
     * 主治医师名称
     */
            @ExcelField(value ="主治医师名称")
    private String zzysmc;

    /**
     * 住院医师名称
     */
            @ExcelField(value ="住院医师名称")
    private String zyysmc;

    /**
     * 病区名称
     */
            @ExcelField(value ="病区名称")
    private String bqmc;



    public static final String ID ="id" ;

    public static final String APPLYDATESTR ="applyDateStr" ;

    public static final String ORDERNUMBER ="orderNumber" ;

    public static final String APPLYDATAID ="applyDataId" ;

    public static final String AREATYPE ="areaType" ;

    public static final String RYRQ ="ryrq" ;

    public static final String CYRQ ="cyrq" ;

    public static final String FZBM ="fzbm" ;

    public static final String FZMC ="fzmc" ;

    public static final String ZYZDBM ="zyzdbm" ;

    public static final String ZYZDMC ="zyzdmc" ;

    public static final String ZSSBM ="zssbm" ;

    public static final String ZSSMC ="zssmc" ;

    public static final String QTZDBM ="qtzdbm" ;

    public static final String QTZDMC ="qtzdmc" ;

    public static final String QTSSBM ="qtssbm" ;

    public static final String QTSSMC ="qtssmc" ;

    public static final String KSBM ="ksbm" ;

    public static final String BQBM ="bqbm" ;

    public static final String QZ ="qz" ;

    public static final String KZRBM ="kzrbm" ;

    public static final String ZRYSBM ="zrysbm" ;

    public static final String ZZYSBM ="zzysbm" ;

    public static final String ZYYSBM ="zyysbm" ;

    public static final String KSMC ="ksmc" ;

    public static final String KZRMC ="kzrmc" ;

    public static final String ZRYSMC ="zrysmc" ;

    public static final String ZZYSMC ="zzysmc" ;

    public static final String ZYYSMC ="zyysmc" ;

    public static final String BQMC ="bqmc" ;

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