package cc.mrbird.febs.drg.entity;

import java.math.BigDecimal;

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

@Excel("yb_drg_apply_data")
@Data
@Accessors(chain = true)
public class YbDrgApplyData implements Serializable, Comparable<YbDrgApplyData> {

    private static final long serialVersionUID = 1L;

    /**
     * drg数据明细
     */
    @ExcelField(value = "drg数据明细")
    private String id;

    /**
     * drg编号
     */
    @ExcelField(value = "drg编号")
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
    @ExcelField(value = "科室")
    private String ks;

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
     * 违规类型
     */
    @ExcelField(value = "违规类型")
    private String wglx;

    /**
     * 问题描述
     */
    @ExcelField(value = "问题描述")
    private String wtms;

    /**
     * 医疗总费用
     */
    @ExcelField(value = "医疗总费用")
    private BigDecimal ylzfy;

    /**
     * 违规金额
     */
    @ExcelField(value = "违规金额")
    private BigDecimal wgje;

    /**
     * 是否编码造成直接错误
     */
    @ExcelField(value = "是否编码造成直接错误")
    private String sfbmzczjcw;

    /**
     * 理由
     */
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

    @Override
    public int compareTo(YbDrgApplyData o) {
        if (this.getId() != null && o.getId() != null) {
            return this.getId().compareTo(o.getId());
        } else if (this.getId() != null) {
            return 1;
        } else {
            return 0;
        }
    }
}