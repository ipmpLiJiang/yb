package cc.mrbird.febs.yb.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
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
 * @since 2021-03-08
 */

@Excel("yb_appeal_sumdept")
@Data
@Accessors(chain = true)
public class YbAppealSumdept implements Serializable , Comparable<YbAppealSumdept>{

private static final long serialVersionUID=1L;

    /**
     * 申诉汇总科室
     */
                    @TableId(value = "id" )
                    @ExcelField(value ="申诉汇总科室")
    private String id;

    /**
     * 编码
     */
    @TableField("asCode")
            @ExcelField(value ="编码")
    private String asCode;

    /**
     * 名称
     */
    @TableField("asName")
            @ExcelField(value ="名称")
    private String asName;

    /**
     * 通用字段
     */
    @TableField("currencyField")
            @ExcelField(value ="通用字段")
    private String currencyField;

    /**
     * 类型
     */
    @TableField("asType")
            @ExcelField(value ="类型")
    private Integer asType;

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

    /**
     * 是否删除
     */
    @TableField("IS_DELETEMARK")
            @ExcelField(value ="是否删除")
    private Integer isDeletemark;

    /**
     * 修改时间
     */
    @TableField("MODIFY_TIME")
            @ExcelField(value ="修改时间")
    private Date modifyTime;
    private transient String modifyTimeFrom;
    private transient String modifyTimeTo;

    /**
     * 创建时间
     */
    @TableField("CREATE_TIME")
            @ExcelField(value ="创建时间")
    private Date createTime;
    private transient String createTimeFrom;
    private transient String createTimeTo;

    /**
     * 创建人
     */
    @TableField("CREATE_USER_ID")
            @ExcelField(value ="创建人")
    private Long createUserId;

    /**
     * 修改人
     */
    @TableField("MODIFY_USER_ID")
            @ExcelField(value ="修改人")
    private Long modifyUserId;



    public static final String ID ="id" ;

    public static final String ASCODE ="asCode" ;

    public static final String ASNAME ="asName" ;

    public static final String CURRENCYFIELD ="currencyField" ;

    public static final String ASTYPE ="asType" ;

    public static final String COMMENTS ="COMMENTS" ;

    public static final String STATE ="STATE" ;

    public static final String IS_DELETEMARK ="IS_DELETEMARK" ;

    public static final String MODIFY_TIME ="MODIFY_TIME" ;

    public static final String CREATE_TIME ="CREATE_TIME" ;

    public static final String CREATE_USER_ID ="CREATE_USER_ID" ;

    public static final String MODIFY_USER_ID ="MODIFY_USER_ID" ;

@Override
public int compareTo(YbAppealSumdept o) {
        if (this.getId() != null && o.getId() != null) {
        return this.getId().compareTo(o.getId());
        } else if (this.getId() != null) {
        return 1;
        } else {
        return 0;
        }
        }
}