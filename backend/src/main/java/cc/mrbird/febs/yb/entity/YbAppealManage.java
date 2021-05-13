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

@Excel("yb_appeal_manage")
@Data
@Accessors(chain = true)
public class YbAppealManage implements Serializable{

private static final long serialVersionUID=1L;

    /**
     * 核对申请
     */
                    @TableId(value = "id" )
                    @ExcelField(value ="核对申请")
    private String id;

    /**
     * 复议申请明细
     */
    @TableField("applyDataId")
            @ExcelField(value ="复议申请明细")
    private String applyDataId;

    /**
     * 核对Id
     */
    @TableField("verifyId")
            @ExcelField(value ="核对Id")
    private String verifyId;

    /**
     * 复议医生编码
     */
    @TableField("readyDoctorCode")
            @ExcelField(value ="复议医生编码")
    private String readyDoctorCode;

    /**
     * 复议医生
     */
    @TableField("readyDoctorName")
            @ExcelField(value ="复议医生")
    private String readyDoctorName;

    /**
     * 复议科室编码
     */
    @TableField("readyDeptCode")
            @ExcelField(value ="复议科室编码")
    private String readyDeptCode;

    /**
     * 复议科室
     */
    @TableField("readyDeptName")
            @ExcelField(value ="复议科室")
    private String readyDeptName;

    /**
     * 变更医生编码
     */
    @TableField("changeDoctorCode")
            @ExcelField(value ="变更医生编码")
    private String changeDoctorCode;

    /**
     * 变更医生
     */
    @TableField("changeDoctorName")
            @ExcelField(value ="变更医生")
    private String changeDoctorName;

    /**
     * 变更复议科室编码
     */
    @TableField("changeDeptCode")
            @ExcelField(value ="变更复议科室编码")
    private String changeDeptCode;

    /**
     * 变更复议科室
     */
    @TableField("changeDeptName")
            @ExcelField(value ="变更复议科室")
    private String changeDeptName;

    /**
     * 接受状态
     */
    @TableField("acceptState")
            @ExcelField(value ="接受状态")
    private Integer acceptState;

    /**
     * 操作理由
     */
    @TableField("operateReason")
            @ExcelField(value ="操作理由")
    private String operateReason;

    /**
     * 操作日期
     */
    @TableField("operateDate")
            @ExcelField(value ="操作日期")
    private Date operateDate;
    private transient String operateDateFrom;
    private transient String operateDateTo;

    /**
     * 医保人代码
     */
    @TableField("refuseId")
            @ExcelField(value ="医保人代码")
    private Long refuseId;

    /**
     * 医保人
     */
    @TableField("refuseName")
            @ExcelField(value ="医保人")
    private String refuseName;

    /**
     * 拒绝理由
     */
    @TableField("refuseReason")
            @ExcelField(value ="拒绝理由")
    private String refuseReason;

    /**
     * 拒绝日期
     */
    @TableField("refuseDate")
            @ExcelField(value ="拒绝日期")
    private Date refuseDate;
    private transient String refuseDateFrom;
    private transient String refuseDateTo;

    /**
     * 管理员代码
     */
    @TableField("adminPersonId")
            @ExcelField(value ="管理员代码")
    private Long adminPersonId;

    /**
     * 管理员
     */
    @TableField("adminPersonName")
            @ExcelField(value ="管理员")
    private String adminPersonName;

    /**
     * 更改日期
     */
    @TableField("adminChangeDate")
            @ExcelField(value ="更改日期")
    private Date adminChangeDate;
    private transient String adminChangeDateFrom;
    private transient String adminChangeDateTo;

    /**
     * 管理员理由
     */
    @TableField("adminReason")
            @ExcelField(value ="管理员理由")
    private String adminReason;

    /**
     * 可操作日期
     */
    @TableField("enableDate")
    @ExcelField(value ="可操作日期")
    private Date enableDate;
    private transient String enableDateFrom;
    private transient String enableDateTo;

    /**
     * 来源类型
     */
    @TableField("sourceType")
    @ExcelField(value ="来源类型")
    private Integer sourceType;

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
    @ExcelField(value ="开方医生编码")
    private String orderDoctorCode;

    /**
     * 开方医生名称
     */
    @TableField("orderDoctorName")
    @ExcelField(value ="开方医生名称")
    private String orderDoctorName;

    /**
     * 开方科室编码
     */
    @TableField("orderDeptCode")
    @ExcelField(value ="开方科室编码")
    private String orderDeptCode;

    /**
     * 开方科室名称
     */
    @TableField("orderDeptName")
    @ExcelField(value ="开方科室名称")
    private String orderDeptName;

    /**
     * 操作过程
     */
    @TableField("operateProcess")
    @ExcelField(value ="操作过程")
    private String operateProcess;

    /**
     * 发送关联Id
     */
    @TableField("verifySendId")
    @ExcelField(value ="发送关联Id")
    private String verifySendId;

    /**
     * 审批状态
     */
    @TableField("approvalState")
    //@ExcelField(value ="审批状态")
    private Integer approvalState;

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
     * 院区
     */
    @TableField("areaType")
    @ExcelField(value = "院区")
    private Integer areaType;

    public static final String ID ="id" ;

    public static final String APPLYDATAID ="applyDataId" ;

    public static final String VERIFYID ="verifyId" ;

    public static final String READYDOCTORCODE ="readyDoctorCode" ;

    public static final String READYDOCTORNAME ="readyDoctorName" ;

    public static final String READYDEPTCODE ="readyDeptCode" ;

    public static final String READYDEPTNAME ="readyDeptName" ;

    public static final String CHANGEDOCTORCODE ="changeDoctorCode" ;

    public static final String CHANGEDOCTORNAME ="changeDoctorName" ;

    public static final String CHANGEDEPTCODE ="changeDeptCode" ;

    public static final String CHANGEDEPTNAME ="changeDeptName" ;

    public static final String ACCEPTSTATE ="acceptState" ;

    public static final String OPERATEREASON ="operateReason" ;

    public static final String OPERATEDATE ="operateDate" ;

    public static final String REFUSEID ="refuseId" ;

    public static final String REFUSENAME ="refuseName" ;

    public static final String REFUSEREASON ="refuseReason" ;

    public static final String REFUSEDATE ="refuseDate" ;

    public static final String ADMINPERSONID ="adminPersonId" ;

    public static final String ADMINPERSONNAME ="adminPersonName" ;

    public static final String ADMINCHANGEDATE ="adminChangeDate" ;

    public static final String ADMINREASON ="adminReason" ;

    public static final String ENABLEDATE ="enableDate" ;

    public static final String SOURCETYPE ="sourceType";

    public static final String DATATYPE ="dataType" ;

    public static final String ORDERDOCTORCODE = "orderDoctorCode";

    public static final String ORDERDOCTORNAME = "orderDoctorName";

    public static final String ORDERDEPTCODE = "orderDeptCode";

    public static final String ORDERDEPTNAME = "orderDeptName";

    public static final String OPERATEPROCESS = "operateProcess";

    public static final String VERIFYSENDID = "verifySendId";

    public static final String APPROVALSTATE = "approvalState";

    public static final String COMMENTS ="COMMENTS" ;

    public static final String STATE ="STATE" ;

    public static final String IS_DELETEMARK ="IS_DELETEMARK" ;

    public static final String APPLYDATESTR = "applyDateStr";
    public static final String ORDERNUMBER = "orderNumber";
    public static final String ORDERNUM = "orderNum";
    public static final String TYPENO = "typeno";

    public static final String AREATYPE = "areaType";

        }