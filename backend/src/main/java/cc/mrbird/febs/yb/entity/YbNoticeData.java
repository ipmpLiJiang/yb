package cc.mrbird.febs.yb.entity;

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
 * @since 2021-03-08
 */

@Excel("yb_notice_data")
@Data
@Accessors(chain = true)
public class YbNoticeData implements Serializable , Comparable<YbNoticeData>{

private static final long serialVersionUID=1L;

    /**
     * 人员类型 1 医管人员类型
     */
    public static final Integer NDTYPE_1 = 1;

    /**
     * 人员类型 2 人员
     */
    public static final Integer NDTYPE_2 = 2;

    /**
     * 培训通知明细
     */
                    @TableId(value = "id" )
                    @ExcelField(value ="培训通知明细")
    private String id;

    /**
     * pid
     */
            @ExcelField(value ="pid")
    private String pid;

    /**
     * 人员编码
     */
    @TableField("personCode")
            @ExcelField(value ="人员编码")
    private String personCode;

    /**
     * 人员名称
     */
    @TableField("personName")
            @ExcelField(value ="人员名称")
    private String personName;

    /**
     * 医管id
     */
    @TableField("cmId")
            @ExcelField(value ="医管id")
    private Integer cmId;

    /**
     * 医管名称
     */
    @TableField("cmName")
            @ExcelField(value ="医管名称")
    private String cmName;

    /**
     * 类型
     */
    @TableField("ndType")
            @ExcelField(value ="类型")
    private Integer ndType;



    public static final String ID ="id" ;

    public static final String PID ="pid" ;

    public static final String PERSONCODE ="personCode" ;

    public static final String PERSONNAME ="personName" ;

    public static final String CMID ="cmId" ;

    public static final String CMNAME ="cmName" ;

    public static final String NDTYPE ="ndType" ;

@Override
public int compareTo(YbNoticeData o) {
        if (this.getId() != null && o.getId() != null) {
        return this.getId().compareTo(o.getId());
        } else if (this.getId() != null) {
        return 1;
        } else {
        return 0;
        }
        }
}