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
 * @since 2020-07-21
 */

@Excel("yb_dept")
@Data
@Accessors(chain = true)
public class YbDept implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 部门Id
     */
    @TableId(value = "id", type = IdType.AUTO)
    @ExcelField(value = "科室Id")
    private Integer id;

    /**
     * 部门编码
     */
    @TableField("deptId")
    @ExcelField(value = "科室编码")
    private String deptId;

    /**
     * 部门名称
     */
    @TableField("deptName")
    @ExcelField(value = "科室名称")
    private String deptName;

    /**
     * 科室
     */
    @TableField("dksId")
    @ExcelField(value = "科室Id")
    private String dksId;

    /**
     * 科室
     */
    @TableField("dksName")
    @ExcelField(value = "科室")
    private String dksName;

    /**
     * 拼写代码
     */
    @TableField("spellCode")
    @ExcelField(value = "拼写代码")
    private String spellCode;

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
     * 分院
     */
    @TableField("fyid")
    @ExcelField(value = "分院")
    private String fyid;

    /**
     * lod分院
     */
    @TableField("lodfyid")
    @ExcelField(value = "分院")
    private String lodfyid;

    /**
     * 肿瘤
     */
    @TableField("fbid")
    @ExcelField(value = "肿瘤")
    private String fbid;


    public static final String ID = "id";

    public static final String DEPTID = "deptId";

    public static final String DEPTNAME = "deptName";

    public static final String DKSID = "dksId";

    public static final String DKSNAME = "dksName";

    public static final String COMMENTS = "COMMENTS";

    public static final String STATE = "STATE";

    public static final String IS_DELETEMARK = "IS_DELETEMARK";

    public static final String MODIFY_TIME = "MODIFY_TIME";

    public static final String CREATE_TIME = "CREATE_TIME";

    public static final String CREATE_USER_ID = "CREATE_USER_ID";

    public static final String MODIFY_USER_ID = "MODIFY_USER_ID";

    public static final String FYID = "fyid";

}
