package cc.mrbird.febs.yb.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.wuwenze.poi.annotation.Excel;
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
 * @since 2020-07-17
 */

@Excel("yb_drg_apply_data")
@Data
//@Accessors(chain = true)
public class YbDrgApplyData implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * drg数据明细
     */
    @TableId(value = "id")
    //@ExcelField(value ="drg数据明细")
    private String id;

    /**
     * drg编号
     */
    //@ExcelField(value ="drg编号")
    private String pid;

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
     * 科室
     */
    @TableField("ks")
    @ExcelField(value = "科室")
    private String ks;


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
     * 违规类型
     */
    @TableField("wglx")
    @ExcelField(value = "违规类型")
    private String wglx;

    /**
     * 问题描述
     */
    @TableField("wtms")
    @ExcelField(value = "问题描述")
    private String wtms;

    /**
     * 医疗总费用
     */
    @TableField("ylzfy")
    @ExcelField(value = "医疗总费用")
    private Double ylzfy;


    /**
     * 违规金额
     */
    @TableField("wgje")
    @ExcelField(value = "违规金额")
    private Double wgje;

    /**
     * 是否编码造成直接错误
     */
    @TableField("sfbmzczjcw")
    @ExcelField(value = "是否编码造成直接错误")
    private String sfbmzczjcw;

    /**
     * 理由
     */
    @TableField("ly")
    @ExcelField(value = "理由")
    private String ly;


    public static final String ID = "id";

    public static final String PID = "pid";

    public static final String ORDERNUMBER = "orderNumber";

    public static final String ORDERNUM = "orderNum";

    public static final String KS = "ks";

    public static final String JZJLH = "jzjlh";

    public static final String BAH = "bah";

    public static final String WGLX = "wglx";

    public static final String WTMS = "wtms";

    public static final String YLZFY = "ylzfy";

    public static final String WGJE = "wgje";

    public static final String SFBMZCZJCW = "sfbmzczjcw";

    public static final String LY = "ly";

}