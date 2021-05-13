package cc.mrbird.febs.yb.entity;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import com.baomidou.mybatisplus.annotation.TableField;

import java.io.Serializable;


import lombok.Data;
import lombok.experimental.Accessors;

import java.util.Date;

import com.wuwenze.poi.annotation.Excel;
import com.wuwenze.poi.annotation.ExcelField;

/**
 * <p>
 *
 * </p>
 *
 * @author viki
 * @since 2020-08-17
 */

@Excel("明细扣款")
@Data
//@Accessors(chain = true)
public class YbReconsiderResetData implements Comparable<YbReconsiderResetData> {

    private static final long serialVersionUID = 1L;

    /**
     * 剔除表明细扣款
     */
    //@ExcelField(value = "剔除表明细扣款")
    private String id;

    /**
     * 关联
     */
    //@ExcelField(value = "关联")
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
     * 费用日期
     */
    @TableField("costDate")
    //@ExcelField(value = "费用日期")
    private Date costDate;
    private transient String costDateFrom;
    private transient String costDateTo;

    /**
     * 费用日期str
     */
    @TableField("costDateStr")
    @ExcelField(value = "费用日期str")
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
    //@ExcelField(value = "结算日期")
    private Date settlementDate;
    private transient String settlementDateFrom;
    private transient String settlementDateTo;

    /**
     * 结算日期Str
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
     * 参保类型
     */
    @TableField("insuredType")
    @ExcelField(value = "参保类型")
    private String insuredType;

    /**
     * 上传数据类型
     */
    @TableField("dataType")
    //@ExcelField(value ="上传数据类型")
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
     * 查找状态
     */
    @TableField("seekState")
    //@ExcelField(value ="查找状态")
    private Integer seekState;

    /**
     * 还款金额
     */
    @TableField("repaymentPrice")
    @ExcelField(value = "还款金额")
    private BigDecimal repaymentPrice;

//    /**
//     * 复议申请明细
//     */
//    @TableField("applyDataId")
//    @ExcelField(value = "复议申请明细")
//    private String applyDataId;

//    /**
//     * 申请序号
//     */
//    @TableField("applyOrderNumber")
//    @ExcelField(value = "申请序号")
//    private String applyOrderNumber;

//    /**
//     * 复议上传Id
//     */
//    @TableField("resultId")
//    @ExcelField(value ="复议上传Id")
//    private String resultId;

    /**
     * 剔除类型
     */
    @TableField("resetType")
    //@ExcelField(value = "剔除类型")
    private Integer resetType;

    /**
     * 关联Id
     */
    @TableField("relatelDataId")
    @ExcelField(value = "关联Id")
    private String relatelDataId;


    /**
     * 剔除人代码
     */
    @TableField("resetPersonId")
    @ExcelField(value = "剔除人代码")
    private Long resetPersonId;

    /**
     * 剔除人
     */
    @TableField("resetPersonName")
    @ExcelField(value = "剔除人")
    private String resetPersonName;

    /**
     * 剔除日期
     */
    @TableField("resetDate")
    @ExcelField(value = "剔除日期")
    private Date resetDate;
    private transient String resetDateFrom;
    private transient String resetDateTo;

    /**
     * 备注
     */
    @TableField("COMMENTS")
    //@ExcelField(value = "备注")
    private String comments;

    /**
     * 状态
     */
    @TableField("STATE")
    //@ExcelField(value = "状态")
    private Integer state;

    /**
     * 是否删除
     */
    @TableField("IS_DELETEMARK")
    //@ExcelField(value = "是否删除")
    private Integer isDeletemark;


    public static final String ID = "id";

    public static final String PID = "pid";

    public static final String SERIALNO = "serialNo";

    public static final String BILLNO = "billNo";

    public static final String PROJECTCODE = "projectCode";

    public static final String PROJECTNAME = "projectName";

    public static final String MEDICALPRICE = "medicalPrice";

    public static final String RULENAME = "ruleName";

    public static final String DEDUCTPRICE = "deductPrice";

    public static final String DEDUCTREASON = "deductReason";

    public static final String REPAYMENTREASON = "repaymentReason";

    public static final String DOCTORNAME = "doctorName";

    public static final String DEPTCODE = "deptCode";

    public static final String DEPTNAME = "deptName";

    public static final String COSTDATE = "costDate";

    public static final String COSTDATESTR = "costDateStr";

    public static final String HOSPITALIZEDNO = "hospitalizedNo";

    public static final String TREATMENTMODE = "treatmentMode";

    public static final String SETTLEMENTDATE = "settlementDate";

    public static final String SETTLEMENTDATESTR = "settlementDateStr";

    public static final String PERSONALNO = "personalNo";

    public static final String INSUREDNAME = "insuredName";

    public static final String CARDNUMBER = "cardNumber";

    public static final String AREANAME = "areaName";

    public static final String INSUREDTYPE = "insuredType";

    public static final String DATATYPE = "dataType";

    public static final String ORDERNUMBER = "orderNumber";

    public static final String ORDERNUM = "orderNum";

    public static final String COMMENTS = "COMMENTS";

    public static final String STATE = "STATE";

    public static final String SEEKSTATE = "seekState";

    public static final String REPAYMENTPRICE = "repaymentPrice";

//    public static final String APPLYDATAID = "applyDataId";

//    public static final String APPLYORDERNUMBER = "applyOrderNumber";

    public static final String RELATELDATAID = "relatelDataId";

    public static final String RESETPERSONID = "resetPersonId";

    public static final String RESETPERSONNAME = "resetPersonName";

//    public static final String RESULTID = "resultId";

    public static final String RESETDATE = "resetDate";

    public static final String IS_DELETEMARK = "IS_DELETEMARK";


    @Override
    public int compareTo(YbReconsiderResetData o) {
        if (this.getOrderNum() != null && o.getOrderNum() != null) {
            return this.getOrderNum().compareTo(o.getOrderNum());
        } else if (this.getOrderNum() != null) {
            return 1;
        } else {
            return 0;
        }
    }

}