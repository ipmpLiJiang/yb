package cc.mrbird.febs.yb.entity;

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
 * @since 2020-09-07
 */

@Excel("yb_reconsider_repay")
@Data
@Accessors(chain = true)
public class YbReconsiderRepay implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 还款核对
     */
    @ExcelField(value = "还款核对")
    private String id;

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
     * 上传名称
     */
    @TableField("uploadFileName")
    @ExcelField(value = "上传名称")
    private String uploadFileName;

    /**
     * 保险类型
     */
    @TableField("repayType")
    @ExcelField(value = "保险类型")
    private Integer repayType;

    /**
     * 数据类型
     */
    @TableField("dataType")
    @ExcelField(value = "数据类型")
    private Integer dataType;

    /**
     * 复议年月
     */
    @TableField("applyDate")
    @ExcelField(value = "复议年月")
    private Date applyDate;
    private transient String applyDateFrom;
    private transient String applyDateTo;

    /**
     * 复议年月Str
     */
    @TableField("applyDateStr")
    @ExcelField(value = "复议年月Str")
    private String applyDateStr;

    /**
     * 通用
     */
    @TableField("currencyField")
    @ExcelField(value = "通用")
    private String currencyField;

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


    public static final String ID = "id";

    public static final String OPERATORID = "operatorId";

    public static final String OPERATORNAME = "operatorName";

    public static final String UPLOADFILENAME = "uploadFileName";

    public static final String REPAYTYPE = "repayType";

    public static final String DATATYPE = "dataType";

    public static final String CURRENCYFIELD = "currencyField";

    public static final String COMMENTS = "COMMENTS";

    public static final String STATE = "STATE";

    public static final String IS_DELETEMARK = "IS_DELETEMARK";

    public static final String MODIFY_TIME = "MODIFY_TIME";

    public static final String CREATE_TIME = "CREATE_TIME";

    public static final String CREATE_USER_ID = "CREATE_USER_ID";

    public static final String MODIFY_USER_ID = "MODIFY_USER_ID";

    public static final String AREATYPE = "areaType";

}