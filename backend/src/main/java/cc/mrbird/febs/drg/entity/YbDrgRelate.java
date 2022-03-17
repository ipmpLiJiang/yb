package cc.mrbird.febs.drg.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
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
 * @since 2022-03-16
 */

@Excel("yb_drg_relate")
@Data
@Accessors(chain = true)
public class YbDrgRelate implements Serializable , Comparable<YbDrgRelate>{

private static final long serialVersionUID=1L;

    /**
     * Id
     */
                    @TableId(value = "id" , type = IdType.AUTO)
                    @ExcelField(value ="Id")
    private Integer id;

    /**
     * bq编码
     */
    @TableField("bqCode")
            @ExcelField(value ="bq编码")
    private String bqCode;

    /**
     * bq名称
     */
    @TableField("bqName")
            @ExcelField(value ="bq名称")
    private String bqName;

    /**
     * ks编码
     */
    @TableField("ksCode")
            @ExcelField(value ="ks编码")
    private String ksCode;

    /**
     * ks名称
     */
    @TableField("ksName")
            @ExcelField(value ="ks名称")
    private String ksName;

    /**
     * dzy编码
     */
    @TableField("dzyCode")
            @ExcelField(value ="dzy编码")
    private String dzyCode;

    /**
     * dzy名称
     */
    @TableField("dzyName")
            @ExcelField(value ="dzy名称")
    private String dzyName;

    /**
     * 院区
     */
            @ExcelField(value ="院区")
    private String yq;

    /**
     * 通用字段
     */
    @TableField("currencyField")
            @ExcelField(value ="通用字段")
    private String currencyField;

    /**
     * 备注
     */
    @TableField("COMMENTS")
            @ExcelField(value ="备注")
    private String comments;

    /**
     * 状态
     */
    @TableField("STATE")
            @ExcelField(value ="状态")
    private Integer state;



    public static final String ID ="id" ;

    public static final String BQCODE ="bqCode" ;

    public static final String BQNAME ="bqName" ;

    public static final String KSCODE ="ksCode" ;

    public static final String KSNAME ="ksName" ;

    public static final String DZYCODE ="dzyCode" ;

    public static final String DZYNAME ="dzyName" ;

    public static final String YQ ="yq" ;

    public static final String CURRENCYFIELD ="currencyField" ;

    public static final String COMMENTS ="COMMENTS" ;

    public static final String STATE ="STATE" ;

@Override
public int compareTo(YbDrgRelate o) {
        if (this.getId() != null && o.getId() != null) {
        return this.getId().compareTo(o.getId());
        } else if (this.getId() != null) {
        return 1;
        } else {
        return 0;
        }
        }
}