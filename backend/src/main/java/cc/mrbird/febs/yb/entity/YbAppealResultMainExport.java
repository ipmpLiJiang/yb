package cc.mrbird.febs.yb.entity;

import com.wuwenze.poi.annotation.Excel;
import com.wuwenze.poi.annotation.ExcelField;
import lombok.Data;

import java.math.BigDecimal;

@Excel("YbAppealResultMainExport")
@Data
public class YbAppealResultMainExport {

    //序号
    @ExcelField(value ="序号")
    private String orderNumber;
    /**
     * 交易流水号
     */
    @ExcelField(value ="交易流水号")
    private String serialNo;
    /**
     * 意见书编码
     */
    @ExcelField(value ="意见书编码")
    private String proposalCode;
    /**
     * 单据号
     */
    @ExcelField(value ="单据号")
    private String billNo;

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
     * 结算日期
     */
    //private Date settlementDate;
    @ExcelField(value ="结算日期")
    private String settlementDateStr;
    /**
     * 住院号
     */
    @ExcelField(value ="住院号")
    private String hospitalizedNo;
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
     * 就医方式
     */
    @ExcelField(value ="就医方式")
    private String treatmentMode;

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
     * 参保类型
     */
    @ExcelField(value ="参保类型")
    private String insuredType;
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
     * 汇总科室
     */
    @ExcelField(value ="汇总科室")
    private String dksName;

    /**
     * 科室编码
     */
    @ExcelField(value ="复议科室代码")
    private String resultDeptCode;

    /**
     * 科室
     */
    @ExcelField(value ="复议科室名称")
    private String resultDeptName;

    /**
     * 医生编码
     */
    @ExcelField(value ="复议医生编码")
    private String resultDoctorCode;

    /**
     * 医生
     */
    @ExcelField(value ="复议医生姓名")
    private String resultDoctorName;

    /**
     * 入院责任科室代码
     */
    @ExcelField(value = "入院责任科室代码")
    private String orderDeptId;

    /**
     * 入院责任科室名称
     */
    @ExcelField(value = "入院责任科室名称")
    private String orderDeptName;

    /**
     * 入院责任人代码
     */
    @ExcelField(value = "入院责任人代码")
    private String orderDocId;

    /**
     * 入院责任人名称
     */
    @ExcelField(value = "入院责任人名称")
    private String orderDocName;

    /**
     * 办入院操作员科室代码
     */
    @ExcelField(value = "办入院操作员科室代码")
    private String excuteDeptId;


    /**
     * 办入院操作员科室名称
     */
    @ExcelField(value = "办入院操作员科室名称")
    private String excuteDeptName;


    /**
     * 办入院操作员代码
     */
    @ExcelField(value = "办入院操作员代码")
    private String excuteDocId;


    /**
     * 办入院操作员名称
     */
    @ExcelField(value = "办入院操作员名称")
    private String excuteDocName;
}