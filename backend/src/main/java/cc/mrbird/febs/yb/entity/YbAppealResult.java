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
 * @since 2020-07-30
 */

@Excel("yb_appeal_result")
@Data
@Accessors(chain = true)
public class YbAppealResult implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 复议结果
     */
    @TableId(value = "id")
    @ExcelField(value = "复议结果")
    private String id;

    /**
     * 复议申请明细
     */
    @TableField("applyDataId")
    @ExcelField(value = "复议申请明细")
    private String applyDataId;

    /**
     * 核对Id
     */
    @TableField("verifyId")
    @ExcelField(value = "核对Id")
    private String verifyId;

    /**
     * 管理Id
     */
    @TableField("manageId")
    @ExcelField(value = "管理Id")
    private String manageId;

    /**
     * 医生编码
     */
    @TableField("doctorCode")
    @ExcelField(value = "医生编码")
    private String doctorCode;

    /**
     * 医生
     */
    @TableField("doctorName")
    @ExcelField(value = "医生")
    private String doctorName;

    /**
     * 科室编码
     */
    @TableField("deptCode")
    @ExcelField(value = "科室编码")
    private String deptCode;

    /**
     * 科室
     */
    @TableField("deptName")
    @ExcelField(value = "科室")
    private String deptName;

    /**
     * 理由
     */
    @TableField("operateReason")
    @ExcelField(value = "理由")
    private String operateReason;

    /**
     * 操作日期
     */
    @TableField("operateDate")
    @ExcelField(value = "操作日期")
    private Date operateDate;
    private transient String operateDateFrom;
    private transient String operateDateTo;

    /**
     * 通用
     */
    @TableField("currencyField")
    @ExcelField(value = "通用")
    private String currencyField;

    /**
     * 来源类型
     */
    @TableField("sourceType")
    @ExcelField(value = "来源类型")
    private Integer sourceType;

    //    /**
//     * 剔除明细扣款
//     */
//    @TableField("resetDataId")
//    @ExcelField(value ="剔除明细扣款")
//    private String resetDataId;
//

    /**
     * 关联Id
     */
    @TableField("relatelDataId")
    @ExcelField(value = "关联Id")
    private String relatelDataId;


//
//    /**
//     * 剔除人代码
//     */
//    @TableField("resetPersonId")
//    @ExcelField(value ="剔除人代码")
//    private Long resetPersonId;
//
//    /**
//     * 剔除人
//     */
//    @TableField("resetPersonName")
//    @ExcelField(value ="剔除人")
//    private String resetPersonName;

    /**
     * 剔除日期
     */
    @TableField("resetDate")
    @ExcelField(value = "剔除日期")
    private Date resetDate;
    private transient String resetDateFrom;
    private transient String resetDateTo;


    /**
     * 数据类型
     */
    @TableField("dataType")
    //@ExcelField(value ="数据类型")
    private Integer dataType;

    /**
     * 开方医生编码
     */
    @TableField("orderDoctorCode")
    @ExcelField(value = "开方医生编码")
    private String orderDoctorCode;

    /**
     * 开方医生名称
     */
    @TableField("orderDoctorName")
    @ExcelField(value = "开方医生名称")
    private String orderDoctorName;

    /**
     * 开方科室编码
     */
    @TableField("orderDeptCode")
    @ExcelField(value = "开方科室编码")
    private String orderDeptCode;

    /**
     * 开方科室名称
     */
    @TableField("orderDeptName")
    @ExcelField(value = "开方科室名称")
    private String orderDeptName;

    /**
     * 还款状态
     */
    @TableField("repayState")
    //@ExcelField(value ="还款状态")
    private Integer repayState;

    /**
     * 复议年月Str
     */
    @TableField("applyDateStr")
    @ExcelField(value = "复议年月Str")
    private String applyDateStr;

    /**
     * 序号
     */
    @TableField("orderNumber")
    @ExcelField(value = "序号")
    private String orderNumber;

    /**
     * 排序
     */
    @TableField("orderNum")
    @ExcelField(value = "排序")
    private Integer orderNum;

    /**
     * 版本类型
     */
    @TableField("typeno")
    //@ExcelField(value ="版本类型")
    private Integer typeno;

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

    public static final String APPLYDATAID = "applyDataId";

    public static final String VERIFYID = "verifyId";

    public static final String MANAGEID = "manageId";

    public static final String DOCTORCODE = "doctorCode";

    public static final String DOCTORNAME = "doctorName";

    public static final String DEPTCODE = "deptCode";

    public static final String DEPTNAME = "deptName";

    public static final String OPERATEREASON = "operateReason";

    public static final String OPERATEDATE = "operateDate";

    public static final String CURRENCYFIELD = "currencyField";

    public static final String SOURCETYPE = "sourceType";

    //    public static final String RESETDATAID ="resetDataId";
//
//    public static final String RESETPERSONID ="resetPersonId" ;
//
//    public static final String RESETPERSONNAME ="resetPersonName" ;
    public static final String RELATELDATAID = "relatelDataId";

    public static final String RESETDATE = "resetDate";

    public static final String DATATYPE = "dataType";

    public static final String ORDERDOCTORCODE = "orderDoctorCode";

    public static final String ORDERDOCTORNAME = "orderDoctorName";

    public static final String ORDERDEPTCODE = "orderDeptCode";

    public static final String ORDERDEPTNAME = "orderDeptName";

    public static final String REPAYSTATE = "repayState";

    public static final String APPLYDATESTR = "applyDateStr";
    public static final String ORDERNUMBER = "orderNumber";
    public static final String ORDERNUM = "orderNum";
    public static final String TYPENO = "typeno";

    public static final String COMMENTS = "COMMENTS";

    public static final String STATE = "STATE";

    public static final String IS_DELETEMARK = "IS_DELETEMARK";

    public static final String MODIFY_TIME = "MODIFY_TIME";

    public static final String CREATE_TIME = "CREATE_TIME";

    public static final String CREATE_USER_ID = "CREATE_USER_ID";

    public static final String MODIFY_USER_ID = "MODIFY_USER_ID";

    public static final String AREATYPE = "areaType";

}