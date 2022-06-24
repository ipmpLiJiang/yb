package cc.mrbird.febs.chs.entity;

import java.time.LocalDate;
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
 * @since 2022-06-20
 */

@Excel("yb_chs_apply")
@Data
@Accessors(chain = true)
public class YbChsApply implements Serializable , Comparable<YbChsApply>{

private static final long serialVersionUID=1L;

    /**
     * chsID
     */
                                @ExcelField(value ="chsID")
    private String id;

    /**
     * chs年月
     */
    @TableField("applyDate")
            @ExcelField(value ="chs年月")
    private Date applyDate;
    private transient String applyDateFrom;
    private transient String applyDateTo;

    /**
     * chs年月Str
     */
    @TableField("applyDateStr")
            @ExcelField(value ="chs年月Str")
    private String applyDateStr;

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
     * 上传名称
     */
    @TableField("uploadFileName")
            @ExcelField(value ="上传名称")
    private String uploadFileName;

    /**
     * 截止时间
     */
    @TableField("endDate")
            @ExcelField(value ="截止时间")
    private Date endDate;
    private transient String endDateFrom;
    private transient String endDateTo;

    /**
     * 任务状态
     */
    @TableField("taskState")
            @ExcelField(value ="任务状态")
    private Integer taskState;

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

    /**
     * 院区
     */
    @TableField("areaType")
            @ExcelField(value ="院区")
    private Integer areaType;

    /**
     * 确认时间
     */
    @TableField("enableDate")
            @ExcelField(value ="确认时间")
    private Date enableDate;
    private transient String enableDateFrom;
    private transient String enableDateTo;



    public static final String ID ="id" ;

    public static final String APPLYDATE ="applyDate" ;

    public static final String APPLYDATESTR ="applyDateStr" ;

    public static final String OPERATORID ="operatorId" ;

    public static final String OPERATORNAME ="operatorName" ;

    public static final String UPLOADFILENAME ="uploadFileName" ;

    public static final String ENDDATE ="endDate" ;

    public static final String TASKSTATE ="taskState" ;

    public static final String COMMENTS ="COMMENTS" ;

    public static final String STATE ="STATE" ;

    public static final String IS_DELETEMARK ="IS_DELETEMARK" ;

    public static final String MODIFY_TIME ="MODIFY_TIME" ;

    public static final String CREATE_TIME ="CREATE_TIME" ;

    public static final String CREATE_USER_ID ="CREATE_USER_ID" ;

    public static final String MODIFY_USER_ID ="MODIFY_USER_ID" ;

    public static final String AREATYPE ="areaType" ;

    public static final String ENABLEDATE ="enableDate" ;

@Override
public int compareTo(YbChsApply o) {
        if (this.getId() != null && o.getId() != null) {
        return this.getId().compareTo(o.getId());
        } else if (this.getId() != null) {
        return 1;
        } else {
        return 0;
        }
        }
}