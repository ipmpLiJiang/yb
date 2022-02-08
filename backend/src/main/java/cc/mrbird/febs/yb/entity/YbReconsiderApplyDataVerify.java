package cc.mrbird.febs.yb.entity;

import com.wuwenze.poi.annotation.Excel;
import com.wuwenze.poi.annotation.ExcelField;
import lombok.Data;

import java.math.BigDecimal;

@Excel("YbReconsiderApplyDataVerify")
@Data
public class YbReconsiderApplyDataVerify {

    //序号
    @ExcelField(value ="序号")
    private String orderNumber;
    /**
     * 交易流水号
     */
    @ExcelField(value ="交易流水号")
    private String serialNo;
    /**
     * 单据号
     */
    @ExcelField(value ="单据号")
    private String billNo;
    /**
     * 意见书编码
     */
    @ExcelField(value ="意见书编码")
    private String proposalCode;
    /**
     * 项目编码
     */
    @ExcelField(value ="项目编码")
    private String projectCode;
    /**
     * 项目名称
     */
    @ExcelField(value ="项目名称")
    private String projectName;
    /**
     * 数量
     */
    @ExcelField(value ="数量")
    private BigDecimal num;
    /**
     * 医保内金额
     */
    @ExcelField(value ="医保内金额")
    private BigDecimal medicalPrice;
    /**
     * 规则名称
     */
    @ExcelField(value ="规则名称")
    private String ruleName;
    /**
     * 扣除金额
     */
    @ExcelField(value ="扣除金额")
    private BigDecimal deductPrice;
    /**
     * 扣除原因
     */
    @ExcelField(value ="扣除原因")
    private String deductReason;
    /**
     * 还款原因
     */
    @ExcelField(value ="还款原因")
    private String repaymentReason;
    /**
     * 医生姓名
     */
    @ExcelField(value ="医生姓名")
    private String doctorName;
    /**
     * 科室编码
     */
    @ExcelField(value ="科室编码")
    private String deptCode;
    /**
     * 科室名称
     */
    @ExcelField(value ="科室名称")
    private String deptName;

    /**
     * 入院日期
     */
    //private Date enterHospitalDate;
    @ExcelField(value ="入院日期")
    private String enterHospitalDateStr;
    /**
     * 出院日期
     */
    //private Date outHospitalDate;
    @ExcelField(value ="出院日期")
    private String outHospitalDateStr;
    /**
     * 费用日期
     */
    //private Date costDate;
    @ExcelField(value ="费用日期")
    private String costDateStr;
    /**
     * 住院号
     */
    @ExcelField(value ="住院号")
    private String hospitalizedNo;
    /**
     * 就医方式
     */
    @ExcelField(value ="就医方式")
    private String treatmentMode;
    /**
     * 结算日期
     */
    //private Date settlementDate;
    @ExcelField(value ="结算日期")
    private String settlementDateStr;
    /**
     * 个人编号
     */
    @ExcelField(value ="个人编号")
    private String personalNo;
    /**
     * 参保人姓名
     */
    @ExcelField(value ="参保人姓名")
    private String insuredName;
    /**
     * 医保卡号
     */
    @ExcelField(value ="医保卡号")
    private String cardNumber;
    /**
     * 统筹区名称
     */
    @ExcelField(value ="统筹区名称")
    private String areaName;
    /**
     * 版本号
     */
    @ExcelField(value ="版本号")
    private String versionNumber;
    /**
     * 反馈申诉
     */
    @ExcelField(value ="反馈申诉")
    private String backAppeal;

    /**
     * 大专业
     */
    @ExcelField(value ="大专业")
    private String dksName;

    /**
     * 复议科室编码
     */
    @ExcelField(value = "复议科室编码")
    private String verifyDeptCode;

    /**
     * 复议科室名称
     */
    @ExcelField(value = "复议科室名称")
    private String verifyDeptName;

    /**
            * 复议医生编码
     */
    @ExcelField(value = "复议医生编码")
    private String verifyDoctorCode;

    /**
     * 参考复议医生
     */
    @ExcelField(value = "复议医生姓名")
    private String verifyDoctorName;

    /**
     * 项目类型编码
     */
    @ExcelField(value = "项目类型编码")
    private String itemTypeCode;

    /**
     * 项目类型名称
     */
    @ExcelField(value = "项目类型名称")
    private String itemTypeName;


    /**
     * 住院科室代码
     */
    @ExcelField(value = "住院科室代码")
    private String orderDeptId;

    /**
     * 住院科室名称
     */
    @ExcelField(value = "住院科室名称")
    private String orderDeptName;


    /**
     * 开方医生代码
     */
    @ExcelField(value = "开方医生代码")
    private String orderDocId;


    /**
     * orderDocName
     */
    @ExcelField(value = "开方医生名称")
    private String orderDocName;

    /**
     * 主治医生编码
     */
    @ExcelField(value = "主治医生编码")
    private String attendDocId;


    /**
     * 主治医生名称
     */
    @ExcelField(value = "主治医生名称")
    private String attendDocName;


    /**
     * 执行科室代码
     */
    @ExcelField(value = "执行科室代码")
    private String excuteDeptId;


    /**
     * 执行科室名称
     */
    @ExcelField(value = "执行科室名称")
    private String excuteDeptName;


    /**
     * 执行医生代码
     */
    @ExcelField(value = "执行医生代码")
    private String excuteDocId;


    /**
     * 执行医生名称
     */
    @ExcelField(value = "执行医生名称")
    private String excuteDocName;



    /**
     * 计费人编码
     */
    @ExcelField(value = "计费人编码")
    private String feeOperatorId;


    /**
     * 计费人名称
     */
    @ExcelField(value = "计费人名称")
    private String feeOperatorName;


    /**
     * 计费科室编码
     */
    @ExcelField(value = "计费科室编码")
    private String feeDeptId;


    /**
     * 计费科室名称
     */
    @ExcelField(value = "计费科室名称")
    private String feeDeptName;


}