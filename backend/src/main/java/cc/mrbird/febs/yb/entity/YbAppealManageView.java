package cc.mrbird.febs.yb.entity;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import com.baomidou.mybatisplus.annotation.TableField;

import java.io.Serializable;


import lombok.Data;
import lombok.experimental.Accessors;

import java.util.Date;
import java.util.List;

import com.wuwenze.poi.annotation.Excel;
import com.wuwenze.poi.annotation.ExcelField;

/**
 * <p>
 * VIEW
 * </p>
 *
 * @author viki
 * @since 2020-07-30
 */

@Excel("yb_appeal_manage_view")
@Data
@Accessors(chain = true)
public class YbAppealManageView implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * pid
     */
    @TableField("pid")
    @ExcelField(value = "pid")
    private String pid;
    /**
     * 交易流水号
     */
    @TableField("serialNo")
    @ExcelField(value = "交易流水号")
    private String serialNo;

    /**
     * 单据号
     */
    @TableField("billNo")
    @ExcelField(value = "单据号")
    private String billNo;

    /**
     * 意见书编码
     */
    @TableField("proposalCode")
    @ExcelField(value = "意见书编码")
    private String proposalCode;

    /**
     * 项目编码
     */
    @TableField("projectCode")
    @ExcelField(value = "项目编码")
    private String projectCode;

    /**
     * 项目名称
     */
    @TableField("projectName")
    @ExcelField(value = "项目名称")
    private String projectName;

    /**
     * 数量
     */
    @ExcelField(value = "数量")
    private BigDecimal num;

    /**
     * 医保内金额
     */
    @TableField("medicalPrice")
    @ExcelField(value = "医保内金额")
    private BigDecimal medicalPrice;

    /**
     * 规则名称
     */
    @TableField("ruleName")
    @ExcelField(value = "规则名称")
    private String ruleName;

    /**
     * 扣除金额
     */
    @TableField("deductPrice")
    @ExcelField(value = "扣除金额")
    private BigDecimal deductPrice;

    /**
     * 扣除原因
     */
    @TableField("deductReason")
    @ExcelField(value = "扣除原因")
    private String deductReason;

    /**
     * 还款原因
     */
    @TableField("repaymentReason")
    @ExcelField(value = "还款原因")
    private String repaymentReason;

    /**
     * 医生姓名
     */
    @TableField("doctorName")
    @ExcelField(value = "医生姓名")
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
     * 入院日期
     */
    @TableField("enterHospitalDateStr")
    @ExcelField(value = "入院日期Str")
    private String enterHospitalDateStr;

    /**
     * 出院日期
     */
    @TableField("outHospitalDateStr")
    @ExcelField(value = "出院日期Str")
    private String outHospitalDateStr;

    /**
     * 费用日期
     */
    @TableField("costDateStr")
    @ExcelField(value = "费用日期Str")
    private String costDateStr;

    /**
     * 住院号
     */
    @TableField("hospitalizedNo")
    @ExcelField(value = "住院号")
    private String hospitalizedNo;

    /**
     * 就医方式
     */
    @TableField("treatmentMode")
    @ExcelField(value = "就医方式")
    private String treatmentMode;


    /**
     * 结算日期
     */
    @TableField("settlementDateStr")
    @ExcelField(value = "结算日期Str")
    private String settlementDateStr;

    /**
     * 个人编号
     */
    @TableField("personalNo")
    @ExcelField(value = "个人编号")
    private String personalNo;

    /**
     * 参保人姓名
     */
    @TableField("insuredName")
    @ExcelField(value = "参保人姓名")
    private String insuredName;

    /**
     * 医保卡号
     */
    @TableField("cardNumber")
    @ExcelField(value = "医保卡号")
    private String cardNumber;

    /**
     * 统筹区名称
     */
    @TableField("areaName")
    @ExcelField(value = "统筹区名称")
    private String areaName;

    /**
     * 版本号
     */
    @TableField("versionNumber")
    @ExcelField(value = "版本号")
    private String versionNumber;

    /**
     * 反馈申诉
     */
    @TableField("backAppeal")
    @ExcelField(value = "反馈申诉")
    private String backAppeal;

    /**
     * 版本类型
     */
    @ExcelField(value = "版本类型")
    private Integer typeno;


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
     * 院区
     */
    @TableField("areaType")
    @ExcelField(value = "院区")
    private Integer areaType;

    private transient List<String> listPid;

    public static final String PID = "pid";
    public static final String SERIALNO = "serialNo";
    public static final String BILLNO = "billNo";
    public static final String PROPOSALCODE = "proposalCode";
    public static final String PROJECTCODE = "projectCode";
    public static final String PROJECTNAME = "projectName";
    public static final String NUM = "num";
    public static final String MEDICALPRICE = "medicalPrice";
    public static final String RULENAME = "ruleName";
    public static final String DEDUCTPRICE = "deductPrice";
    public static final String DEDUCTREASON = "deductReason";
    public static final String REPAYMENTREASON = "repaymentReason";
    public static final String DOCTORNAME = "doctorName";
    public static final String DEPTCODE = "deptCode";
    public static final String DEPTNAME = "deptName";
    public static final String ENTERHOSPITALDATESTR ="enterHospitalDateStr" ;
    public static final String OUTHOSPITALDATESTR ="outHospitalDateStr" ;
    public static final String COSTDATESTR ="costDateStr" ;
    public static final String HOSPITALIZEDNO = "hospitalizedNo";
    public static final String TREATMENTMODE = "treatmentMode";
    public static final String SETTLEMENTDATESTR ="settlementDateStr" ;
    public static final String PERSONALNO = "personalNo";
    public static final String INSUREDNAME = "insuredName";
    public static final String CARDNUMBER = "cardNumber";
    public static final String AREANAME = "areaName";
    public static final String VERSIONNUMBER = "versionNumber";
    public static final String BACKAPPEAL = "backAppeal";
    public static final String TYPENO = "typeno";
    public static final String APPLYDATESTR = "applyDateStr";
    public static final String ORDERDOCTORCODE = "orderDoctorCode";
    public static final String ORDERDOCTORNAME = "orderDoctorName";
    public static final String ORDERDEPTCODE = "orderDeptCode";
    public static final String ORDERDEPTNAME = "orderDeptName";
    public static final String ISEND = "isEnd";
    public static final String OPERATEPROCESS = "operateProcess";
    public static final String ID = "id";
    public static final String APPLYDATAID = "applyDataId";
    public static final String VERIFYID = "verifyId";
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
    public static final String ACCEPTSTATE = "acceptState";
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
    public static final String SOURCETYPE ="sourceType";
    public static final String DATATYPE ="dataType" ;
    public static final String ORDERNUMBER ="orderNumber";
    public static final String ORDERNUM ="orderNum";
    public static final String STATE = "STATE";
    public static final String CURRENCYFIELD = "currencyField";
    public static final String VERIFYSENDID = "verifySendId";
    public static final String APPROVALSTATE = "approvalState";
    public static final String AREATYPE = "areaType";
}