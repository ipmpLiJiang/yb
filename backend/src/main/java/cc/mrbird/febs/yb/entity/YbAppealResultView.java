package cc.mrbird.febs.yb.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.wuwenze.poi.annotation.Excel;
import com.wuwenze.poi.annotation.ExcelField;
import lombok.Data;
import lombok.experimental.Accessors;

import java.math.BigDecimal;
import java.util.Date;

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
     * 复议年月Str
     */
    @TableField("applyDateStr")
    @ExcelField(value = "复议年月Str")
    private String applyDateStr;

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
    @TableField("arDoctorCode")
    @ExcelField(value = "医生编码")
    private String arDoctorCode;

    /**
     * 医生
     */
    @TableField("arDoctorName")
    @ExcelField(value = "医生")
    private String arDoctorName;

    /**
     * 科室编码
     */
    @TableField("arDeptCode")
    @ExcelField(value = "科室编码")
    private String arDeptCode;

    /**
     * 科室
     */
    @TableField("arDeptName")
    @ExcelField(value = "科室")
    private String arDeptName;

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
     * 住院科室代码
     */
    @TableField("orderDeptId")
    @ExcelField(value = "住院科室代码")
    private String orderDeptId;

    /**
     * 住院科室名称
     */
    @TableField("orderDeptName")
    @ExcelField(value = "住院科室名称")
    private String orderDeptName;

    /**
     * 开方医生代码
     */
    @TableField("orderDocId")
    @ExcelField(value = "开方医生代码")
    private String orderDocId;


    /**
     * orderDocName
     */
    @TableField("orderDocName")
    @ExcelField(value = "开方医生名称")
    private String orderDocName;


    /**
     * 执行科室代码
     */
    @TableField("excuteDeptId")
    @ExcelField(value = "执行科室代码")
    private String excuteDeptId;


    /**
     * 执行科室名称
     */
    @TableField("excuteDeptName")
    @ExcelField(value = "执行科室名称")
    private String excuteDeptName;


    /**
     * 执行医生代码
     */
    @TableField("excuteDocId")
    @ExcelField(value = "执行医生代码")
    private String excuteDocId;


    /**
     * 执行医生名称
     */
    @TableField("excuteDocName")
    @ExcelField(value = "执行医生名称")
    private String excuteDocName;


    /**
     * 主治医生编码
     */
    @TableField("attendDocId")
    @ExcelField(value = "主治医生编码")
    private String attendDocId;


    /**
     * 主治医生名称
     */
    @TableField("attendDocName")
    @ExcelField(value = "主治医生名称")
    private String attendDocName;


    /**
     * 项目类型编码
     */
    @TableField("itemTypeCode")
    @ExcelField(value = "项目类型编码")
    private String itemTypeCode;


    /**
     * 项目类型名称
     */
    @TableField("itemTypeName")
    @ExcelField(value = "项目类型名称")
    private String itemTypeName;


    /**
     * 计费人编码
     */
    @TableField("feeOperatorId")
    @ExcelField(value = "计费人编码")
    private String feeOperatorId;


    /**
     * 计费人名称
     */
    @TableField("feeOperatorName")
    @ExcelField(value = "计费人名称")
    private String feeOperatorName;


    /**
     * 计费科室编码
     */
    @TableField("feeDeptId")
    @ExcelField(value = "计费科室编码")
    private String feeDeptId;


    /**
     * 计费科室名称
     */
    @TableField("feeDeptName")
    @ExcelField(value = "计费科室名称")
    private String feeDeptName;


    /**
     * 开方医生编码
     */
    @TableField("arOrderDoctorCode")
    @ExcelField(value = "开方医生编码")
    private String arOrderDoctorCode;

    /**
     * 开方医生名称
     */
    @TableField("arOrderDoctorName")
    @ExcelField(value = "开方医生名称")
    private String arOrderDoctorName;

    /**
     * 开方科室编码
     */
    @TableField("arOrderDeptCode")
    @ExcelField(value = "开方科室编码")
    private String arOrderDeptCode;

    /**
     * 开方科室名称
     */
    @TableField("arOrderDeptName")
    @ExcelField(value = "开方科室名称")
    private String arOrderDeptName;


    /**
     * 状态
     */
    @TableField("STATE")
    @ExcelField(value = "状态")
    private Integer state;

    /**
     * 还款状态
     */
    @TableField("repayState")
    @ExcelField(value = "还款状态")
    private Integer repayState;

    /**
     * 通用
     */
    @TableField("currencyField")
    @ExcelField(value = "通用")
    private String currencyField;


    /**
     * 关联Id
     */
    @TableField("relatelDataId")
    @ExcelField(value = "关联Id")
    private String relatelDataId;

    /**
     * pid
     */
    @TableField("pid")
    @ExcelField(value = "pid")
    private String pid;

    /**
     * 院区
     */
    @TableField("areaType")
    @ExcelField(value = "院区")
    private Integer areaType;

    /**
     * 大专业
     */
    @TableField("dksName")
    @ExcelField(value = "大专业")
    private String dksName;

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
    public static final String ENTERHOSPITALDATESTR = "enterHospitalDateStr";
    public static final String OUTHOSPITALDATESTR = "outHospitalDateStr";
    public static final String COSTDATESTR = "costDateStr";
    public static final String HOSPITALIZEDNO = "hospitalizedNo";
    public static final String TREATMENTMODE = "treatmentMode";
    public static final String SETTLEMENTDATESTR = "settlementDateStr";
    public static final String PERSONALNO = "personalNo";
    public static final String INSUREDNAME = "insuredName";
    public static final String INSUREDTYPE = "insuredType";
    public static final String CARDNUMBER = "cardNumber";
    public static final String AREANAME = "areaName";
    public static final String VERSIONNUMBER = "versionNumber";
    public static final String BACKAPPEAL = "backAppeal";
    public static final String TYPENO = "typeno";
    public static final String APPLYDATESTR = "applyDateStr";
    public static final String ID = "id";
    public static final String APPLYDATAID = "applyDataId";
    public static final String VERIFYID = "verifyId";
    public static final String ARDOCTORCODE = "arDoctorCode";
    public static final String ARDOCTORNAME = "arDoctorName";
    public static final String ARDEPTCODE = "arDeptCode";
    public static final String ARDEPTNAME = "arDeptName";
    public static final String OPERATEREASON = "operateReason";
    public static final String OPERATEDATE = "operateDate";
    public static final String SOURCETYPE = "sourceType";
    public static final String DATATYPE = "dataType";
    public static final String ORDERNUMBER = "orderNumber";
    public static final String ORDERNUM = "orderNum";
    public static final String STATE = "STATE";
    public static final String REPAYSTATE = "repayState";
    public static final String CURRENCYFIELD = "currencyField";
    public static final String RELATELDATAID = "relatelDataId";

    public static final String AREATYPE = "areaType";

    public static final String DKSNAME = "dksName";

    @Override
    public int compareTo(YbAppealResultView o) {
        if (this.getOrderNum() != null && o.getOrderNum() != null) {
            return this.getOrderNum().compareTo(o.getOrderNum());
        } else if (this.getOrderNum() != null) {
            return 1;
        } else {
            return 0;
        }
    }

}