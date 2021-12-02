package cc.mrbird.febs.drg.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.wuwenze.poi.annotation.Excel;
import com.wuwenze.poi.annotation.ExcelField;
import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

/**
 * <p>
 * VIEW
 * </p>
 *
 * @author viki
 * @since 2020-07-30
 */

@Excel("yb_drg_manage_view")
@Data
@Accessors(chain = true)
public class YbDrgManageView implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * pid
     */
    @TableField("pid")
    @ExcelField(value = "pid")
    private String pid;

    /**
     * 复议年月Str
     */
    @TableField("applyDateStr")
    @ExcelField(value = "复议年月Str")
    private String applyDateStr;

    /**
     * 核对申请
     */
    @ExcelField(value = "核对申请")
    private String id;

    /**
     * 申请数据明细
     */
    @TableField("applyDataId")
    @ExcelField(value = "申请数据明细")
    private String applyDataId;

    /**
     * 核对申请
     */
    @TableField("verifyId")
    @ExcelField(value = "核对申请")
    private String verifyId;

    /**
     * 复议医生编码
     */
    @TableField("readyDoctorCode")
    @ExcelField(value = "复议医生编码")
    private String readyDoctorCode;

    /**
     * 复议医生
     */
    @TableField("readyDoctorName")
    @ExcelField(value = "复议医生")
    private String readyDoctorName;

    /**
     * 复议科室编码
     */
    @TableField("readyDeptCode")
    @ExcelField(value = "复议科室编码")
    private String readyDeptCode;

    /**
     * 复议科室
     */
    @TableField("readyDeptName")
    @ExcelField(value = "复议科室")
    private String readyDeptName;

    /**
     * 变更医生编码
     */
    @TableField("changeDoctorCode")
    @ExcelField(value = "变更医生编码")
    private String changeDoctorCode;

    /**
     * 变更医生
     */
    @TableField("changeDoctorName")
    @ExcelField(value = "变更医生")
    private String changeDoctorName;

    /**
     * 变更复议科室编码
     */
    @TableField("changeDeptCode")
    @ExcelField(value = "变更复议科室编码")
    private String changeDeptCode;

    /**
     * 变更复议科室
     */
    @TableField("changeDeptName")
    @ExcelField(value = "变更复议科室")
    private String changeDeptName;

    /**
     * 复议截止日期
     */
    @TableField("applyEndDate")
    @ExcelField(value = "复议截止日期")
    private Date applyEndDate;

    /**
     * 操作理由
     */
    @TableField("operateReason")
    @ExcelField(value = "操作理由")
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
     * 接受状态
     */
    @TableField("acceptState")
    @ExcelField(value = "接受状态")
    private Integer acceptState;

    /**
     * 医保人代码
     */
    @TableField("refuseId")
    @ExcelField(value = "医保人代码")
    private Long refuseId;

    /**
     * 医保人
     */
    @TableField("refuseName")
    @ExcelField(value = "医保人")
    private String refuseName;

    /**
     * 拒绝理由
     */
    @TableField("refuseReason")
    @ExcelField(value = "拒绝理由")
    private String refuseReason;

    /**
     * 拒绝日期
     */
    @TableField("refuseDate")
    @ExcelField(value = "拒绝日期")
    private Date refuseDate;
    private transient String refuseDateFrom;
    private transient String refuseDateTo;

    /**
     * 管理员代码
     */
    @TableField("adminPersonId")
    @ExcelField(value = "管理员代码")
    private Long adminPersonId;

    /**
     * 管理员
     */
    @TableField("adminPersonName")
    @ExcelField(value = "管理员")
    private String adminPersonName;

    /**
     * 更改日期
     */
    @TableField("adminChangeDate")
    @ExcelField(value = "更改日期")
    private Date adminChangeDate;
    private transient String adminChangeDateFrom;
    private transient String adminChangeDateTo;

    /**
     * 管理员理由
     */
    @TableField("adminReason")
    @ExcelField(value = "管理员理由")
    private String adminReason;

    /**
     * 可操作日期
     */
    @TableField("enableDate")
    @ExcelField(value = "可操作日期")
    private Date enableDate;
    private transient String enableDateFrom;
    private transient String enableDateTo;

    /**
     * 是否可操作日期
     */
    @TableField("isEnableDate")
    @ExcelField(value = "是否可操作日期")
    private Integer isEnableDate;

    /**
     * 可操作状态
     */
    @TableField("enableType")
    @ExcelField(value = "可操作状态")
    private Integer enableType;

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
     * 操作过程
     */
    @TableField("operateProcess")
    @ExcelField(value ="操作过程")
    private String operateProcess;

    /**
     * 审批状态
     */
    @TableField("approvalState")
    @ExcelField(value ="审批状态")
    private Integer approvalState;

    /**
     * 是否截止
     */
    @TableField("isEnd")
    @ExcelField(value = "是否截止")
    private Integer isEnd;


    /**
     * 状态
     */
    @TableField("STATE")
    @ExcelField(value = "状态")
    private Integer state;

    /**
     * 通用
     */
    @TableField("currencyField")
    @ExcelField(value = "通用")
    private String currencyField;

    /**
     * 院区
     */
    @TableField("areaType")
    @ExcelField(value = "院区")
    private Integer areaType;

    /**
     * 科室
     */
    @ExcelField(value = "科室")
    private String ks;

    /**
     * 就诊记录号
     */
    @ExcelField(value = "就诊记录号")
    private String jzjlh;

    /**
     * 病案号
     */
    @ExcelField(value = "病案号")
    private String bah;

    /**
     * 违规类型
     */
    @ExcelField(value = "违规类型")
    private String wglx;

    /**
     * 问题描述
     */
    @ExcelField(value = "问题描述")
    private String wtms;

    /**
     * 医疗总费用
     */
    @ExcelField(value = "医疗总费用")
    private BigDecimal ylzfy;

    /**
     * 违规金额
     */
    @ExcelField(value = "违规金额")
    private BigDecimal wgje;

    /**
     * 是否编码造成直接错误
     */
    @ExcelField(value = "是否编码造成直接错误")
    private String sfbmzczjcw;

    /**
     * 理由
     */
    @ExcelField(value = "理由")
    private String ly;

    private transient List<String> listPid;

    public static final String ID = "id";
    public static final String PID = "pid";
    public static final String APPLYDATAID = "applyDataId";
    public static final String VERIFYID = "verifyId";
    public static final String APPLYDATESTR = "applyDateStr";
    public static final String ISEND = "isEnd";
    public static final String OPERATEPROCESS = "operateProcess";
    public static final String APPROVALSTATE ="approvalState" ;
    public static final String READYDOCTORCODE = "readyDoctorCode";
    public static final String READYDOCTORNAME = "readyDoctorName";
    public static final String READYDEPTCODE = "readyDeptCode";
    public static final String READYDEPTNAME = "readyDeptName";
    public static final String CHANGEDOCTORCODE = "changeDoctorCode";
    public static final String CHANGEDOCTORNAME = "changeDoctorName";
    public static final String CHANGEDEPTCODE = "changeDeptCode";
    public static final String APPLYENDDATE = "applyEndDate";
    public static final String CHANGEDEPTNAME = "changeDeptName";
    public static final String OPERATEREASON = "operateReason";
    public static final String OPERATEDATE = "operateDate";
    public static final String REFUSEID = "refuseId";
    public static final String REFUSENAME = "refuseName";
    public static final String REFUSEREASON = "refuseReason";
    public static final String REFUSEDATE = "refuseDate";
    public static final String ADMINPERSONID = "adminPersonId";
    public static final String ADMINPERSONNAME = "adminPersonName";
    public static final String ADMINCHANGEDATE = "adminChangeDate";
    public static final String ADMINREASON = "adminReason";
    public static final String ENABLEDATE = "enableDate";
    public static final String ENABLETYPE = "enableType";
    public static final String ORDERNUMBER ="orderNumber";
    public static final String ORDERNUM ="orderNum";
    public static final String STATE = "STATE";
    public static final String CURRENCYFIELD = "currencyField";
    public static final String AREATYPE = "areaType";

    public static final String KS = "ks";

    public static final String JZJLH = "jzjlh";

    public static final String BAH = "bah";

    public static final String WGLX = "wglx";

    public static final String WTMS = "wtms";

    public static final String YLZFY = "ylzfy";

    public static final String WGJE = "wgje";

    public static final String SFBMZCZJCW = "sfbmzczjcw";

    public static final String LY = "ly";
}