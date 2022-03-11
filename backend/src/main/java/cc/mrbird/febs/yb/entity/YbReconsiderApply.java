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
 * @since 2020-07-23
 */

@Excel("yb_reconsider_apply")
@Data
@Accessors(chain = true)
public class YbReconsiderApply implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 0 没任务
     */
    public static final int TASK_STATE_0 = 0;

    /**
     * 1 审核一任务
     */
    public static final int TASK_STATE_1 = 1;

    /**
     * 2 审核二任务
     */
    public static final int TASK_STATE_2 = 2;

    /**
     * id
     */
    @TableId(value = "id")
    @ExcelField(value = "id")
    private String id;

    /**
     * 复议年月
     */
    @TableField("applyDate")
    @ExcelField(value = "复议年月")
    private Date applyDate;
    private transient String applyDateFrom;
    private transient String applyDateTo;


    /**
     * 操作员代码
     */
    @TableField("operatorId")
    @ExcelField(value = "操作员代码")
    private Long operatorId;

    /**
     * 操作员名称
     */
    @TableField("operatorName")
    @ExcelField(value = "操作员名称")
    private String operatorName;

    /**
     * 备注
     */
    @TableField("COMMENTS")
    @ExcelField(value = "备注")
    private String comments;

    /**
     * 状态
     */
    @TableField("STATE")
    @ExcelField(value = "状态")
    private Integer state;

    /**
     * 剔除状态
     */
    @TableField("resetState")
    @ExcelField(value = "剔除状态")
    private Integer resetState;

    /**
     * 申诉状态
     */
    @TableField("resultState")
    @ExcelField(value = "申诉状态")
    private Integer resultState;

    /**
     * 还款状态
     */
    @TableField("repayState")
    @ExcelField(value = "还款状态")
    private Integer repayState;

    /**
     * 任务状态
     */
    @TableField("taskState")
    @ExcelField(value = "任务状态")
    private Integer taskState;


    /**
     * 是否删除
     */
    @TableField("IS_DELETEMARK")
    @ExcelField(value = "是否删除")
    private Integer isDeletemark;

    /**
     * 修改时间
     */
    @TableField("MODIFY_TIME")
    @ExcelField(value = "修改时间")
    private Date modifyTime;
    private transient String modifyTimeFrom;
    private transient String modifyTimeTo;

    /**
     * 创建时间
     */
    @TableField("CREATE_TIME")
    @ExcelField(value = "创建时间")
    private Date createTime;
    private transient String createTimeFrom;
    private transient String createTimeTo;

    /**
     * 创建人
     */
    @TableField("CREATE_USER_ID")
    @ExcelField(value = "创建人")
    private Long createUserId;

    /**
     * 修改人
     */
    @TableField("MODIFY_USER_ID")
    @ExcelField(value = "修改人")
    private Long modifyUserId;

    /**
     * 复议年月Str
     */
    @TableField("applyDateStr")
    @ExcelField(value = "复议年月Str")
    private String applyDateStr;

    /**
     * 第一版上传名称
     */
    @TableField("uploadFileNameOne")
    @ExcelField(value = "第一版上传名称")
    private String uploadFileNameOne;

    /**
     * 第二版上传名称
     */
    @TableField("uploadFileNameTwo")
    @ExcelField(value = "第二版上传名称")
    private String uploadFileNameTwo;


    /**
     * 第一版结束时间
     */
    @TableField("endDateOne")
    @ExcelField(value = "第一版结束时间")
    private Date endDateOne;
    private transient String endDateOneFrom;
    private transient String endDateOneTo;

    /**
     * 第二版结束时间
     */
    @TableField("endDateTwo")
    @ExcelField(value = "第二版结束时间")
    private Date endDateTwo;
    private transient String endDateTwoFrom;
    private transient String endDateTwoTo;


    /**
     * 第一版确认时间
     */
    @TableField("enableDateOne")
    @ExcelField(value = "第一版确认时间")
    private Date enableDateOne;
    private transient String enableDateOneFrom;
    private transient String enableDateOneTo;

    /**
     * 第二版确认时间
     */
    @TableField("enableDateTwo")
    @ExcelField(value = "第二版确认时间")
    private Date enableDateTwo;
    private transient String enableDateTwoFrom;
    private transient String enableDateTwoTo;

    /**
     * 院区
     */
    @TableField("areaType")
    @ExcelField(value = "院区")
    private Integer areaType;

    /**
     * 非常规结束时间
     */
    @TableField("endDateReset")
    @ExcelField(value = "非常规结束时间")
    private Date endDateReset;
//    private transient String endDateResetFrom;
//    private transient String endDateResetTo;


    public static final String ID = "id";

    public static final String APPLYDATE = "applyDate";


    public static final String OPERATORID = "operatorId";

    public static final String OPERATORNAME = "operatorName";

    public static final String COMMENTS = "COMMENTS";

    public static final String STATE = "STATE";

    public static final String RESETSTATE = "resetState";

    public static final String RESULTSTATE = "resultState";

    public static final String REPAYSTATE = "repayState";

    public static final String TASKSTATE = "taskState";

    public static final String IS_DELETEMARK = "IS_DELETEMARK";

    public static final String MODIFY_TIME = "MODIFY_TIME";

    public static final String CREATE_TIME = "CREATE_TIME";

    public static final String CREATE_USER_ID = "CREATE_USER_ID";

    public static final String MODIFY_USER_ID = "MODIFY_USER_ID";

    public static final String APPLYDATESTR = "applyDateStr";

    public static final String UPLOADFILENAMEONE = "uploadFileNameOne";

    public static final String UPLOADFILENAMETWO = "uploadFileNameTwo";

    public static final String ENDDATEONE = "endDateOne";

    public static final String ENDDATETWO = "endDateTwo";

    public static final String ENABLEDATEONE = "enableDateOne";

    public static final String ENABLEDATETWO = "enableDateTwo";

    public static final String AREATYPE = "areaType";

    public static final String ENDDATERESET = "endDateReset";

}