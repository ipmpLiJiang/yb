package cc.mrbird.febs.drg.entity;

import com.wuwenze.poi.annotation.Excel;
import com.wuwenze.poi.annotation.ExcelField;
import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;

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
     * 病区编码
     */
//    @ExcelField(value = "病区编码")
//    private String verifyDeptCode;

    /**
     * 病区名称
     */
//    @ExcelField(value = "病区名称")
//    private String verifyDeptName;

    /**
     * 复议科室编码
     */
    @ExcelField(value = "复议科室编码")
    private String verifyDksId;

    /**
     * 科室
     */
    @ExcelField(value = "复议科室名称")
    private String verifyDksName;

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
     * 入院日期
     */
    @ExcelField(value = "入院日期")
    private String ryDateStr;

    /**
     * 出院日期
     */
    @ExcelField(value = "出院日期")
    private String cyDateStr;

    /**
     * 统筹支付
     */
    @ExcelField(value = "统筹支付")
    private BigDecimal tczf;

    /**
     * DRG管理系统-DRG分组编码
     */
    @ExcelField(value = "DRG管理系统-DRG分组编码")
    private String fzCode;

    /**
     * DRG管理系统-DRG分组名称
     */
    @ExcelField(value = "DRG管理系统-DRG分组名称")
    private String fzName;

    /**
     * DRG管理系统-医保主要诊断编码
     */
    @ExcelField(value = "DRG管理系统-医保主要诊断编码")
    private String zyzdCode;

    /**
     * DRG管理系统-医保主要诊断名称
     */
    @ExcelField(value = "DRG管理系统-医保主要诊断名称")
    private String zyzdName;

    /**
     * DRG管理系统-医保主手术编码
     */
    @ExcelField(value = "DRG管理系统-医保主手术编码")
    private String zssCode;

    /**
     * DRG管理系统-医保主手术名称
     */
    @ExcelField(value = "DRG管理系统-医保主手术名称")
    private String zssName;

    /**
     * DRG管理系统-其他诊断编码
     */
    @ExcelField(value = "DRG管理系统-其他诊断编码")
    private String qtzdCode;

    /**
     * DRG管理系统-其他诊断名称
     */
    @ExcelField(value = "DRG管理系统-其他诊断名称")
    private String qtzdName;

    /**
     * DRG管理系统-其他手术编码
     */
    @ExcelField(value = "DRG管理系统-其他手术编码")
    private String qtssCode;

    /**
     * DRG管理系统-其他手术名称
     */
    @ExcelField(value = "DRG管理系统-其他手术名称")
    private String qtssName;

    /**
     * 院区
     */
    @ExcelField(value = "院区")
    private String yq;

    /**
     * 科室
     */
    @ExcelField(value = "大专业")
    private String deptName;

    /**
     * 病区名称
     */
    @ExcelField(value = "病区名称")
    private String areaName;

    /**
     * 权重
     */
    @ExcelField(value = "权重")
    private BigDecimal qz;


    /**
     * 科主任名称
     */
    @ExcelField(value = "科主任姓名")
    private String kzrDocName;

    /**
     * 主任医师名称
     */
    @ExcelField(value = "主任医师姓名")
    private String zrysDocName;


    /**
     * 主治医师名称
     */
    @ExcelField(value = "主治医师姓名")
    private String zzysDocName;

    /**
     * 住院医师名称
     */
    @ExcelField(value = "住院医师姓名")
    private String zyysDocName;

    /**
     * 医疗组医师科室名称
     */
    @ExcelField(value = "医疗组医师科室名称")
    private String ylzDeptName;

    /**
     * 医疗组医师名称
     */
    @ExcelField(value = "医疗组医师姓名")
    private String ylzDocName;


}