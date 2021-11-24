package cc.mrbird.febs.yb.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.wuwenze.poi.annotation.Excel;
import com.wuwenze.poi.annotation.ExcelField;
import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.util.Date;

/**
 * <p>
 *
 * </p>
 *
 * @author viki
 * @since 2020-07-23
 */

@Excel("yb_drg_apply")
@Data
@Accessors(chain = true)
public class YbDrgApply implements Serializable {

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
     * id
     */
    @TableId(value = "id")
    @ExcelField(value = "id")
    private String id;

    /**
     * drg年月
     */
    @TableField("applyDate")
    @ExcelField(value = "drg年月")
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
     * 上传名称
     */
    @TableField("uploadFileName")
    @ExcelField(value = "上传名称")
    private String uploadFileName;

    /**
     * 结束时间
     */
    @TableField("endDate")
    @ExcelField(value = "结束时间")
    private Date endDate;
    private transient String endDateFrom;
    private transient String endDateTo;

    /**
     * 确认时间
     */
    @TableField("enableDate")
    @ExcelField(value = "确认时间")
    private Date enableDate;
    private transient String enableDateFrom;
    private transient String enableDateTo;


    /**
     * 院区
     */
    @TableField("areaType")
    @ExcelField(value = "院区")
    private Integer areaType;


    public static final String ID = "id";

    public static final String APPLYDATE = "applyDate";


    public static final String OPERATORID = "operatorId";

    public static final String OPERATORNAME = "operatorName";

    public static final String COMMENTS = "COMMENTS";

    public static final String STATE = "STATE";

    public static final String TASKSTATE = "taskState";

    public static final String IS_DELETEMARK = "IS_DELETEMARK";

    public static final String MODIFY_TIME = "MODIFY_TIME";

    public static final String CREATE_TIME = "CREATE_TIME";

    public static final String CREATE_USER_ID = "CREATE_USER_ID";

    public static final String MODIFY_USER_ID = "MODIFY_USER_ID";

    public static final String APPLYDATESTR = "applyDateStr";

    public static final String UPLOADFILENAMEONE = "uploadFileName";

    public static final String ENDDATE = "endDate";

    public static final String ENABLEDATE = "enableDate";

    public static final String AREATYPE = "areaType";

}