package cc.mrbird.febs.drg.entity;

import com.wuwenze.poi.annotation.Excel;
import com.wuwenze.poi.annotation.ExcelField;
import lombok.Data;

import java.math.BigDecimal;

@Excel("YbReconsiderApplyDataVerify")
@Data
public class YbDrgApplyDataVerify {

    //序号
    @ExcelField(value ="序号")
    private String orderNumber;

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


}