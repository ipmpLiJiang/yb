package cc.mrbird.febs.chs.entity;

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
 * @since 2022-07-13
 */

@Excel("yb_chs_priority_level")
@Data
@Accessors(chain = true)
public class YbChsPriorityLevel implements Serializable, Comparable<YbChsPriorityLevel> {

    private static final long serialVersionUID = 1L;

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
     * id
     */
    @TableId(value = "id", type = IdType.AUTO)
    @ExcelField(value = "id")
    private Integer id;

    /**
     * 住院门诊
     */
    @TableField("zymzType")
    @ExcelField(value = "住院门诊")
    private Integer zymzType;

    /**
     * 规则名称
     */
    @TableField("ruleName")
    @ExcelField(value = "规则名称")
    private String ruleName;

    /**
     * 项目名称
     */
    @TableField("projectName")
    @ExcelField(value = "项目名称")
    private String projectName;

    /**
     * 科室编码
     */
    @TableField("dksId")
    @ExcelField(value = "科室编码")
    private String dksId;

    /**
     * 科室名称
     */
    @TableField("dksName")
    @ExcelField(value = "科室名称")
    private String dksName;

    /**
     * 人员类型
     */
    @TableField("personType")
    @ExcelField(value = "人员类型")
    private Integer personType;

    /**
     * 科室类型
     */
    @TableField("deptType")
    @ExcelField(value = "科室类型")
    private Integer deptType;

    /**
     * dks编码To
     */
    @TableField("dksIdTo")
    @ExcelField(value = "dks编码To")
    private String dksIdTo;

    /**
     * 汇总科室To
     */
    @TableField("dksNameTo")
    @ExcelField(value = "汇总科室To")
    private String dksNameTo;

    /**
     * 医生编码To
     */
    @TableField("doctorCodeTo")
    @ExcelField(value = "医生编码To")
    private String doctorCodeTo;

    /**
     * 医生名称To
     */
    @TableField("doctorNameTo")
    @ExcelField(value = "医生名称To")
    private String doctorNameTo;

    /**
     * 院区
     */
    @TableField("areaType")
    @ExcelField(value = "院区")
    private Integer areaType;

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
     * 是否规则
     */
    @TableField("isRule")
    @ExcelField(value = "是否规则")
    private Integer isRule;

    /**
     * 是否项目
     */
    @TableField("isProject")
    @ExcelField(value = "是否项目")
    private Integer isProject;

    /**
     * 数据类型
     */
    @TableField("plType")
    @ExcelField(value = "数据类型")
    private Integer plType;


    public static final String ID = "id";

    public static final String ZYMZTYPE = "zymzType";

    public static final String RULENAME = "ruleName";

    public static final String PROJECTNAME = "projectName";

    public static final String DKSID = "dksId";

    public static final String DKSNAME = "dksName";

    public static final String PERSONTYPE = "personType";

    public static final String DEPTTYPE = "deptType";

    public static final String DKSIDTO = "dksIdTo";

    public static final String DKSNAMETO = "dksNameTo";

    public static final String DOCTORCODETO = "doctorCodeTo";

    public static final String DOCTORNAMETO = "doctorNameTo";

    public static final String AREATYPE = "areaType";

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

    public static final String ISRULE = "isRule";

    public static final String ISPROJECT = "isProject";

    public static final String PLTYPE = "plType";

    @Override
    public int compareTo(YbChsPriorityLevel o) {
        if (this.getId() != null && o.getId() != null) {
            return this.getId().compareTo(o.getId());
        } else if (this.getId() != null) {
            return 1;
        } else {
            return 0;
        }
    }
}