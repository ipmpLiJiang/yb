package cc.mrbird.febs.yb.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.wuwenze.poi.annotation.ExcelField;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;


@Data
public class YbReconsiderInpatientfeesData implements Serializable {

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
     * 项目代码
     */
    @TableField("itemId")
    @ExcelField(value = "项目代码")
    private String itemId;

    /**
     * 项目医保编码
     */
    @TableField("itemCode")
    @ExcelField(value = "项目医保编码")
    private String itemCode;

    /**
     * 项目名称
     */
    @TableField("itemName")
    @ExcelField(value = "项目名称")
    private String itemName;

    /**
     * 项目数量
     */
    @TableField("itemCount")
    @ExcelField(value = "项目数量")
    private BigDecimal itemCount;

    /**
     * 项目单价
     */
    @TableField("itemPrice")
    @ExcelField(value = "项目单价")
    private BigDecimal itemPrice;

    /**
     * 项目金额
     */
    @TableField("itemAmount")
    @ExcelField(value = "项目金额")
    private BigDecimal itemAmount;

    /**
     * 费用日期
     */
    @TableField("feeDate")
    @ExcelField(value = "费用日期")
    private Date feeDate;

    /**
     * 住院科室代码
     */
    @TableField("deptId")
    @ExcelField(value = "住院科室代码")
    private String deptId;

    /**
     * 住院科室名称
     */
    @TableField("deptName")
    @ExcelField(value = "住院科室名称")
    private String deptName;

    /**
     * 开方医生代码
     */
    @TableField("orderDocId")
    @ExcelField(value = "开方医生代码")
    private String orderDocId;

    /**
     * 开方医生名称
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
     * 结算时间
     */
    @TableField("settlementDate")
    @ExcelField(value = "结算时间")
    private Date settlementDate;


    /**
     * itemYbKey
     */
    @TableField("itemYbKey")
    @ExcelField(value = "itemYbKey")
    private String itemYbKey;

    /**
     * itemHisKey
     */
    @TableField("itemHisKey")
    @ExcelField(value = "itemHisKey")
    private String itemHisKey;

    /**
     * itemYbCode
     */
    @TableField("itemYbCode")
    @ExcelField(value = "itemYbCode")
    private String itemYbCode;

}