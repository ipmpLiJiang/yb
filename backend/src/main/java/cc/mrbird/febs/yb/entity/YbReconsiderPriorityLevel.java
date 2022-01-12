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
 * @since 2020-10-13
 */

@Excel("yb_reconsider_priority_level")
@Data
@Accessors(chain = true)
public class YbReconsiderPriorityLevel implements Serializable, Comparable<YbReconsiderPriorityLevel> {

    private static final long serialVersionUID = 1L;

    /**
     * 1规则
     */
    public static final Integer PL_STATE_1 = 1;

    /**
     * 2项目
     */
    public static final Integer PL_STATE_2 = 2;

    /**
     * 3科室
     */
    public static final Integer PL_STATE_3 = 3;

    /**
     * 4 Drg
     */
    public static final Integer PL_STATE_4 = 4;

    /**
     * 1执行科室
     */
    public static final Integer DEPT_STATE_1 = 1;

    /**
     * 2计费科室
     */
    public static final Integer DEPT_STATE_2 = 2;

    /**
     * 1开单人员
     */
    public static final Integer PERSON_TYPE_1 = 1;

    /**
     * 2执行人员
     */
    public static final Integer PERSON_TYPE_2 = 2;

    /**
     * 3计费人员
     */
    public static final Integer PERSON_TYPE_3 = 3;

    /**
     * 4固定人员
     */
    public static final Integer PERSON_TYPE_4 = 4;


    /**
     * 1开单科室
     */
    public static final Integer DEPT_TYPE_1 = 1;

    /**
     * 2执行科室
     */
    public static final Integer DEPT_TYPE_2 = 2;

    /**
     * 3计费科室
     */
    public static final Integer DEPT_TYPE_3 = 3;

    /**
     * 4固定科室
     */
    public static final Integer DEPT_TYPE_4 = 4;

    /**
     * 复议规则id
     */
    @ExcelField(value = "复议规则id")
    private String id;

    /**
     * 规则名称
     */
    @TableField("rplName")
    @ExcelField(value = "规则名称")
    private String rplName;

    /**
     * 医生编码
     */
    @TableField("doctorCode")
    @ExcelField(value = "医生编码")
    private String doctorCode;

    /**
     * 医生名称
     */
    @TableField("doctorName")
    @ExcelField(value = "医生名称")
    private String doctorName;

    /**
     * 科室编码
     */
    @TableField("deptCode")
    @ExcelField(value = "科室编码")
    private String deptCode;

    /**
     * 科室名称
     */
    @TableField("deptName")
    @ExcelField(value = "科室名称")
    private String deptName;

    /**
     * 科室类型
     */
    @TableField("deptState")
    @ExcelField(value = "科室类型")
    private Integer deptState;

    /**
     * 复议科室类型
     */
    @TableField("deptType")
    @ExcelField(value = "复议科室类型")
    private Integer deptType;


    /**
     * 复议医生类型
     */
    @TableField("personType")
    @ExcelField(value = "复议医生类型")
    private Integer personType;

    /**
     * 是否固定科室
     */
    @TableField("isFixDept")
    @ExcelField(value = "是否固定科室")
    private Boolean isFixDept;

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
     * 通用字段
     */
    @TableField("currencyField")
    @ExcelField(value = "通用字段")
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

    /**
     * 科室To
     */
    @TableField("dksName")
    @ExcelField(value ="科室")
    private String dksName;

    /**
     * 科室To
     */
    @TableField("dksNameTo")
    @ExcelField(value ="科室To")
    private String dksNameTo;

    /**
     * 医生To
     */
    @TableField("doctorCodeTo")
    @ExcelField(value ="科室")
    private String doctorCodeTo;

    /**
     * 医生To
     */
    @TableField("doctorNameTo")
    @ExcelField(value ="科室To")
    private String doctorNameTo;

    public static final String ID = "id";

    public static final String RPLNAME = "rplName";

    public static final String DOCTORCODE = "doctorCode";

    public static final String DOCTORNAME = "doctorName";

    public static final String DEPTCODE = "deptCode";

    public static final String DEPTNAME = "deptName";

    public static final String DEPTSTATE = "deptState";

    public static final String DEPTTYPE = "deptType";

    public static final String PERSONTYPE = "personType";

    public static final String OPERATORID = "operatorId";

    public static final String OPERATORNAME = "operatorName";

    public static final String CURRENCYFIELD = "currencyField";

    public static final String COMMENTS = "COMMENTS";

    public static final String STATE = "STATE";

    public static final String IS_DELETEMARK = "IS_DELETEMARK";

    public static final String MODIFY_TIME = "MODIFY_TIME";

    public static final String CREATE_TIME = "CREATE_TIME";

    public static final String CREATE_USER_ID = "CREATE_USER_ID";

    public static final String MODIFY_USER_ID = "MODIFY_USER_ID";

    public static final String AREATYPE ="areaType" ;

    public static final String DKSNAME ="dksName" ;

    public static final String DKSNAMETO ="dksNameTo" ;

    public static final String DOCTORCODETO ="doctorCodeTo" ;

    public static final String DOCTORNAMETO ="doctorNameTo" ;


    @Override
    public int compareTo(YbReconsiderPriorityLevel o) {
        if (this.getId() != null && o.getId() != null) {
            return this.getId().compareTo(o.getId());
        } else if (this.getId() != null) {
            return 1;
        } else {
            return 0;
        }
    }
}