package cc.mrbird.febs.yb.entity;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import cc.mrbird.febs.com.entity.ComFile;
import com.baomidou.mybatisplus.annotation.TableField;

import java.io.Serializable;


import lombok.Data;
import lombok.experimental.Accessors;

import java.util.Date;

import com.wuwenze.poi.annotation.Excel;
import com.wuwenze.poi.annotation.ExcelField;

/**
 * <p>
 * VIEW
 * </p>
 *
 * @author viki
 * @since 2020-08-12
 */

@Excel("yb_appeal_result_view")
@Data
@Accessors(chain = true)
public class YbAppealResultView implements Comparable<YbAppealResultView> {

    private static final long serialVersionUID = 1L;

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
    @TableField("enterHospitalDate")
    @ExcelField(value = "入院日期")
    private Date enterHospitalDate;
    private transient String enterHospitalDateFrom;
    private transient String enterHospitalDateTo;

    /**
     * 出院日期
     */
    @TableField("outHospitalDate")
    @ExcelField(value = "出院日期")
    private Date outHospitalDate;
    private transient String outHospitalDateFrom;
    private transient String outHospitalDateTo;

    /**
     * 费用日期
     */
    @TableField("costDate")
    @ExcelField(value = "费用日期")
    private Date costDate;
    private transient String costDateFrom;
    private transient String costDateTo;

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
    @TableField("settlementDate")
    @ExcelField(value = "结算日期")
    private Date settlementDate;
    private transient String settlementDateFrom;
    private transient String settlementDateTo;

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
     * 参保类型
     */
    @TableField("insuredType")
    @ExcelField(value = "参保类型")
    private String insuredType;


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
     * 复议结果
     */
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
     * 医生编码
     */
    @TableField("ar_doctorCode")
    @ExcelField(value = "医生编码")
    private String arDoctorCode;

    /**
     * 医生
     */
    @TableField("ar_doctorName")
    @ExcelField(value = "医生")
    private String arDoctorname;

    /**
     * 科室编码
     */
    @TableField("ar_deptCode")
    @ExcelField(value = "科室编码")
    private String arDeptcode;

    /**
     * 科室
     */
    @TableField("ar_deptName")
    @ExcelField(value = "科室")
    private String arDeptname;

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
     * 来源类型
     */
    @TableField("sourceType")
    @ExcelField(value = "来源类型")
    private Integer sourceType;

    /**
     * 序号
     */
    @TableField("orderNumber")
    @ExcelField(value = "序号")
    private String orderNumber;


    /**
     * 剔除明细扣款
     */
    @TableField("resetDataId")
    @ExcelField(value = "剔除明细扣款")
    private String resetDataId;

    /**
     * 还款明细
     */
    @TableField("repayDataId")
    @ExcelField(value = "还款明细")
    private String repayDataId;

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
     * 通用
     */
    @TableField("currencyField")
    @ExcelField(value = "通用")
    private String currencyField;

    /**
     * 数据类型
     */
    @TableField("dataType")
    //@ExcelField(value ="数据类型")
    private Integer dataType;

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

    public static final String ENTERHOSPITALDATE = "enterHospitalDate";

    public static final String OUTHOSPITALDATE = "outHospitalDate";

    public static final String COSTDATE = "costDate";

    public static final String ENTERHOSPITALDATESTR = "enterHospitalDateStr";

    public static final String OUTHOSPITALDATESTR = "outHospitalDateStr";

    public static final String COSTDATESTR = "costDateStr";

    public static final String HOSPITALIZEDNO = "hospitalizedNo";

    public static final String TREATMENTMODE = "treatmentMode";

    public static final String SETTLEMENTDATE = "settlementDate";

    public static final String SETTLEMENTDATESTR = "settlementDateStr";

    public static final String PERSONALNO = "personalNo";

    public static final String INSUREDNAME = "insuredName";

    public static final String INSUREDTYPE = "insuredType";

    public static final String CARDNUMBER = "cardNumber";

    public static final String AREANAME = "areaName";

    public static final String VERSIONNUMBER = "versionNumber";

    public static final String BACKAPPEAL = "backAppeal";

    public static final String TYPENO = "typeno";

    public static final String APPLYDATE = "applyDate";

    public static final String APPLYDATESTR = "applyDateStr";

    public static final String OPERATORID = "operatorId";

    public static final String OPERATORNAME = "operatorName";

    public static final String ID = "id";

    public static final String APPLYDATAID = "applyDataId";

    public static final String VERIFYID = "verifyId";

    public static final String ARDOCTORCODE = "arDoctorCode";

    public static final String AR_DOCTORNAME = "ar_doctorName";

    public static final String AR_DEPTCODE = "ar_deptCode";

    public static final String AR_DEPTNAME = "ar_deptName";

    public static final String OPERATEREASON = "operateReason";

    public static final String OPERATEDATE = "operateDate结算日期";

    public static final String SOURCETYPE = "sourceType";

    public static final String RESETDATAID = "resetDataId";

    public static final String REPAYDATAID ="repayDataId";

    public static final String DATATYPE = "dataType";

    public static final String ORDERNUMBER = "orderNumber";

    public static final String COMMENTS = "COMMENTS";

    public static final String STATE = "STATE";

    public static final String IS_DELETEMARK = "IS_DELETEMARK";

    public static final String MODIFY_TIME = "MODIFY_TIME";

    public static final String CREATE_TIME = "CREATE_TIME";

    public static final String CREATE_USER_ID = "CREATE_USER_ID";

    public static final String MODIFY_USER_ID = "MODIFY_USER_ID";

    public static final String CURRENCYFIELD = "currencyField";

    @Override
    public int compareTo(YbAppealResultView o) {
        if (this.getOrderNumber() != null && o.getOrderNumber() != null) {
            return this.getOrderNumber().compareTo(o.getOrderNumber());
        } else if (this.getOrderNumber() != null) {
            return 1;
        } else {
            return 0;
        }
    }

}