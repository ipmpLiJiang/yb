package cc.mrbird.febs.chs.entity;

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
 * @since 2022-06-24
 */

@Excel("yb_chs_apply_task")
@Data
@Accessors(chain = true)
public class YbChsApplyTask implements Serializable, Comparable<YbChsApplyTask> {

    private static final long serialVersionUID = 1L;

    /**
     * 复议申请任务记录
     */
    @ExcelField(value = "复议申请任务记录")
    private String id;

    /**
     * 复议年月Str
     */
    @TableField("applyDateStr")
    @ExcelField(value = "复议年月Str")
    private String applyDateStr;

    /**
     * 开始数
     */
    @TableField("startNum")
    @ExcelField(value = "开始数")
    private Integer startNum;

    /**
     * 结束数
     */
    @TableField("endNum")
    @ExcelField(value = "结束数")
    private Integer endNum;

    /**
     * 总数
     */
    @TableField("totalRow")
    @ExcelField(value = "总数")
    private Integer totalRow;

    /**
     * 当前页
     */
    @TableField("currentPage")
    @ExcelField(value = "当前页")
    private Integer currentPage;

    /**
     * 页数
     */
    @TableField("pageSize")
    @ExcelField(value = "页数")
    private Integer pageSize;

    /**
     * 总页数
     */
    @TableField("totalPage")
    @ExcelField(value = "总页数")
    private Integer totalPage;

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
     * 院区
     */
    @TableField("areaType")
    @ExcelField(value = "院区")
    private Integer areaType;

    /**
     * His更新数量
     */
    @TableField("hisCount")
    @ExcelField(value = "His更新数量")
    private Integer hisCount;

    /**
     * 是否门诊
     */
    @TableField("isOutpfees")
    @ExcelField(value = "是否门诊")
    private Integer isOutpfees;

    /**
     * 数据类型
     */
    @TableField("dataType")
    @ExcelField(value = "数据类型")
    private Integer dataType;

    public static final String ID = "id";

    public static final String APPLYDATESTR = "applyDateStr";

    public static final String STARTNUM = "startNum";

    public static final String ENDNUM = "endNum";

    public static final String TOTALROW = "totalRow";

    public static final String CURRENTPAGE = "currentPage";

    public static final String PAGESIZE = "pageSize";

    public static final String TOTALPAGE = "totalPage";

    public static final String COMMENTS = "COMMENTS";

    public static final String STATE = "STATE";

    public static final String IS_DELETEMARK = "IS_DELETEMARK";

    public static final String MODIFY_TIME = "MODIFY_TIME";

    public static final String CREATE_TIME = "CREATE_TIME";

    public static final String CREATE_USER_ID = "CREATE_USER_ID";

    public static final String MODIFY_USER_ID = "MODIFY_USER_ID";

    public static final String AREATYPE = "areaType";

    public static final String HISCOUNT = "hisCount";

    public static final String ISOUTPFEES = "isOutpfees";

    public static final String DATATYPE = "dataType";

    @Override
    public int compareTo(YbChsApplyTask o) {
        if (this.getId() != null && o.getId() != null) {
            return this.getId().compareTo(o.getId());
        } else if (this.getId() != null) {
            return 1;
        } else {
            return 0;
        }
    }
}