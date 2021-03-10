package cc.mrbird.febs.yb.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import java.time.LocalDate;
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

@Excel("yb_notice")
@Data
@Accessors(chain = true)
public class YbNotice implements Serializable , Comparable<YbNotice>{

private static final long serialVersionUID=1L;

    /**
     * 培训通知
     */
                    @TableId(value = "id" )
                    @ExcelField(value ="培训通知")
    private String id;

    /**
     * 标题
     */
    @TableField("ntTitle")
            @ExcelField(value ="标题")
    private String ntTitle;

    /**
     * 内容简介
     */
    @TableField("ntExplain")
            @ExcelField(value ="内容简介")
    private String ntExplain;

    /**
     * 内容详情
     */
    @TableField("ntDetail")
            @ExcelField(value ="内容详情")
    private String ntDetail;

    /**
     * 发送类型
     */
    @TableField("sendType")
            @ExcelField(value ="发送类型")
    private Integer sendType;

    /**
     * 发布时间
     */
    @TableField("releaseDate")
            @ExcelField(value ="发布时间")
    private Date releaseDate;
    private transient String releaseDateFrom;
    private transient String releaseDateTo;

    /**
     * 操作员代码
     */
    @TableField("operatorId")
            @ExcelField(value ="操作员代码")
    private Long operatorId;

    /**
     * 操作员名称
     */
    @TableField("operatorName")
            @ExcelField(value ="操作员名称")
    private String operatorName;

    /**
     * 点击量
     */
    @TableField("clickNum")
            @ExcelField(value ="点击量")
    private Integer clickNum;

    /**
     * 短信状态
     */
    @TableField("smsState")
            @ExcelField(value ="短信状态")
    private Integer smsState;

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

    public static final String NTTITLE ="ntTitle" ;

    public static final String NTEXPLAIN ="ntExplain" ;

    public static final String NTDETAIL ="ntDetail" ;

    public static final String SENDTYPE ="sendType" ;

    public static final String RELEASEDATE ="releaseDate" ;

    public static final String OPERATORID ="operatorId" ;

    public static final String OPERATORNAME ="operatorName" ;

    public static final String CLICKNUM ="clickNum" ;

    public static final String SMSSTATE ="smsState" ;

    public static final String CURRENCYFIELD ="currencyField" ;

    public static final String COMMENTS ="COMMENTS" ;

    public static final String STATE ="STATE" ;

    public static final String IS_DELETEMARK ="IS_DELETEMARK" ;

    public static final String MODIFY_TIME ="MODIFY_TIME" ;

    public static final String CREATE_TIME ="CREATE_TIME" ;

    public static final String CREATE_USER_ID ="CREATE_USER_ID" ;

    public static final String MODIFY_USER_ID ="MODIFY_USER_ID" ;

@Override
public int compareTo(YbNotice o) {
        if (this.getId() != null && o.getId() != null) {
        return this.getId().compareTo(o.getId());
        } else if (this.getId() != null) {
        return 1;
        } else {
        return 0;
        }
        }
}