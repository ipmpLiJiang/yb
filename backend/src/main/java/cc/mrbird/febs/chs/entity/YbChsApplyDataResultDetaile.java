package cc.mrbird.febs.chs.entity;

import com.wuwenze.poi.annotation.Excel;
import com.wuwenze.poi.annotation.ExcelField;
import lombok.Data;

import java.math.BigDecimal;

/**
 * <p>
 *
 * </p>
 *
 * @author viki
 * @since 2022-06-27
 */
@Excel("YbChsApplyDataVerify")
@Data
public class YbChsApplyDataResultDetaile {

    /**
     * 序号
     */
    @ExcelField(value = "序号")
    private Integer orderNum;

    /**
     * 申诉截止日期
     */
    @ExcelField(value = "申诉截止日期")
    private String appealEndDateStr;

    /**
     * 支付地点类别
     */
    @ExcelField(value = "支付地点类别")
    private String payPlaceType;

    /**
     * 疑点状态
     */
    @ExcelField(value = "疑点状态")
    private String ydState;

    /**
     * 医保区划
     */
    @ExcelField(value = "医保区划")
    private String areaName;

    /**
     * 医药机构编码
     */
    @ExcelField(value = "医药机构编码")
    private String yyjgCode;

    /**
     * 医药机构名称
     */
    @ExcelField(value = "医药机构名称")
    private String yyjgName;

    /**
     * 科室名称
     */
    @ExcelField(value = "科室名称")
    private String deptName;

    /**
     * 医生姓名
     */
    @ExcelField(value = "医生姓名")
    private String doctorName;

    /**
     * 医疗类别
     */
    @ExcelField(value = "医疗类别")
    private String medicalType;

    /**
     * 住院门诊号
     */
    @ExcelField(value = "住院门诊号")
    private String zymzNumber;

    /**
     * 参保人
     */
    @ExcelField(value = "参保人")
    private String insuredName;

    /**
     * 入院日期
     */
    @ExcelField(value = "入院日期")
    private String enterHospitalDateStr;

    /**
     * 出院日期
     */
    @ExcelField(value = "出院日期")
    private String outHospitalDateStr;

    /**
     * 结算日期
     */
    @ExcelField(value = "结算日期")
    private String settlementDateStr;

    /**
     * 身份证号
     */
    @ExcelField(value = "身份证号")
    private String cardNumber;

    /**
     * 医保项目编码
     */
    @ExcelField(value = "医保项目编码")
    private String projectCode;

    /**
     * 医保项目名称
     */
    @ExcelField(value = "医保项目名称")
    private String projectName;

    /**
     * 医院项目名称
     */
    @ExcelField(value = "医院项目名称")
    private String projectYyName;

    /**
     * 规则名称
     */
    @ExcelField(value = "规则名称")
    private String ruleName;

    /**
     * 初审违规金额（元）
     */
    @ExcelField(value = "初审违规金额（元）")
    private BigDecimal violateCsPrice;

    /**
     * 违规金额（元）
     */
    @ExcelField(value = "违规金额（元）")
    private BigDecimal violatePrice;

    /**
     * 违规内容
     */
    @ExcelField(value = "违规内容")
    private String violateReason;

    /**
     * 诊断信息
     */
    @ExcelField(value = "诊断信息")
    private String zdNote;

    /**
     * 费用日期
     */

    @ExcelField(value = "费用日期")
    private String costDateStr;

    /**
     * 险种类型
     */
    @ExcelField(value = "险种类型")
    private String insuredType;

    /**
     * 数量
     */
    @ExcelField(value = "数量")
    private BigDecimal num;

    /**
     * 单价（元）
     */
    @ExcelField(value = "单价（元）")
    private BigDecimal price;

    /**
     * 金额（元）
     */
    @ExcelField(value = "金额（元）")
    private BigDecimal medicalPrice;

    /**
     * 统筹支付（元）
     */
    @ExcelField(value = "统筹支付（元）")
    private BigDecimal tcPayPrice;

    /**
     * 规格
     */
    @ExcelField(value = "规格")
    private String specs;

    /**
     * 剂型
     */
    @ExcelField(value = "剂型")
    private String jx;

    /**
     * 机构等级
     */
    @ExcelField(value = "机构等级")
    private String jgLevel;

    /**
     * 复议理由
     */
    @ExcelField(value = "复议理由")
    private String fyly;

    /**
     * chs编码
     */
    @ExcelField(value = "复议科室编码")
    private String resultDksId;

    /**
     * chs科室
     */
    @ExcelField(value = "复议科室名称")
    private String resultDksName;

    /**
     * chs医生编码
     */
    @ExcelField(value = "复议医生编码")
    private String resultDoctorCode;

    /**
     * chs医生
     */
    @ExcelField(value = "复议医生")
    private String resultDoctorName;

    /**
     * 项目类型编码
     */
//    @ExcelField(value = "项目类型编码")
//    private String itemTypeCode;

    /**
     * 项目类型名称
     */
//    @ExcelField(value = "项目类型名称")
//    private String itemTypeName;


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