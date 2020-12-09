package cc.mrbird.febs.yb.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.wuwenze.poi.annotation.ExcelField;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;


@Data
public class YbReconsiderInpatientfeesMain implements Serializable {

    /**
     * 住院号
     */
    @TableField("inpatientId")
    @ExcelField(value = "住院号")
    private String inpatientId;

    /**
     * 患者姓名
     */
    @TableField("patientName")
    @ExcelField(value = "患者姓名")
    private String patientName;

    /**
     * HIS结算序号
     */
    @TableField("settlementId")
    @ExcelField(value = "HIS结算序号")
    private String settlementId;

    /**
     * 单据号
     */
    @TableField("billNo")
    @ExcelField(value = "单据号")
    private String billNo;

    /**
     * 交易流水号
     */
    @TableField("transNo")
    @ExcelField(value = "交易流水号")
    private String transNo;

    /**
     * 主单结算时间
     */
    @TableField("settleDate")
    @ExcelField(value = "主单结算时间")
    private Date settleDate;

    /**
     * 入院责任医生代码
     */
    @TableField("inHospDocId")
    @ExcelField(value = "入院责任医生代码")
    private String inHospDocId;

    /**
     * 入院责任医生名称
     */
    @TableField("inHospDocName")
    @ExcelField(value = "入院责任医生名称")
    private String inHospDocName;

    /**
     * 办入院操作员代码
     */
    @TableField("inHospOpterId")
    @ExcelField(value = "办入院操作员代码")
    private String inHospOpterId;

    /**
     * 办入院操作员名称
     */
    @TableField("inHospOpterName")
    @ExcelField(value = "办入院操作员名称")
    private String inHospOpterName;

    /**
     * 入院科室代码
     */
    @TableField("inHospDeptId")
    @ExcelField(value = "入院科室代码")
    private String inHospDeptId;

    /**
     * 入院科室名称
     */
    @TableField("inHospDeptName")
    @ExcelField(value = "入院科室名称")
    private String inHospDeptName;

    /**
     * 办入院操作员科室代码
     */
    @TableField("opterDeptId")
    @ExcelField(value = "办入院操作员科室代码")
    private String opterDeptId;

    /**
     * 办入院操作员科室名称
     */
    @TableField("opterDeptName")
    @ExcelField(value = "办入院操作员科室名称")
    private String opterDeptName;

}