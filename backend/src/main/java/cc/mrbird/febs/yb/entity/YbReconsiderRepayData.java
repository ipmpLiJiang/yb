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
 * @since 2020-09-07
 */

@Excel("yb_reconsider_repay_data")
@Data
@Accessors(chain = true)
public class YbReconsiderRepayData implements Serializable{

private static final long serialVersionUID=1L;

    /**
     * 还款核对明细
     */
                                @ExcelField(value ="还款核对明细")
    private String id;

    /**
     * 还款核对Id
     */
            @ExcelField(value ="还款核对Id")
    private String pid;

    /**
     * 所属期
     */
    @TableField("belongDate")
            @ExcelField(value ="所属期")
    private Date belongDate;
    private transient String belongDateFrom;
    private transient String belongDateTo;

    /**
     * 所属期Str
     */
    @TableField("belongDateStr")
            @ExcelField(value ="所属期Str")
    private String belongDateStr;

    /**
     * 医院编号
     */
    @TableField("hospitalCode")
            @ExcelField(value ="医院编号")
    private String hospitalCode;

    /**
     * 医院名称
     */
    @TableField("hospitalName")
            @ExcelField(value ="医院名称")
    private String hospitalName;

    /**
     * 序号
     */
    @TableField("orderNumber")
            @ExcelField(value ="序号")
    private String orderNumber;

    /**
     * 交易流水号
     */
    @TableField("serialNo")
            @ExcelField(value ="交易流水号")
    private String serialNo;

    /**
     * 单据号
     */
    @TableField("billNo")
            @ExcelField(value ="单据号")
    private String billNo;

    /**
     * 项目编码
     */
    @TableField("projectCode")
            @ExcelField(value ="项目编码")
    private String projectCode;

    /**
     * 项目名称
     */
    @TableField("projectName")
            @ExcelField(value ="项目名称")
    private String projectName;

    /**
     * 医保内金额
     */
    @TableField("medicalPrice")
            @ExcelField(value ="医保内金额")
    private BigDecimal medicalPrice;

    /**
     * 规则名称
     */
    @TableField("ruleName")
            @ExcelField(value ="规则名称")
    private String ruleName;

    /**
     * 扣除金额
     */
    @TableField("deductPrice")
            @ExcelField(value ="扣除金额")
    private BigDecimal deductPrice;

    /**
     * 扣除原因
     */
    @TableField("deductReason")
            @ExcelField(value ="扣除原因")
    private String deductReason;

    /**
     * 还款金额
     */
    @TableField("repaymentPrice")
            @ExcelField(value ="还款金额")
    private BigDecimal repaymentPrice;

    /**
     * 还款原因
     */
    @TableField("repaymentReason")
            @ExcelField(value ="还款原因")
    private String repaymentReason;

    /**
     * 医生姓名
     */
    @TableField("doctorName")
            @ExcelField(value ="医生姓名")
    private String doctorName;

    /**
     * 科室编码
     */
    @TableField("deptCode")
            @ExcelField(value ="科室编码")
    private String deptCode;

    /**
     * 科室名称
     */
    @TableField("deptName")
            @ExcelField(value ="科室名称")
    private String deptName;

    /**
     * 费用日期
     */
    @TableField("costDate")
            @ExcelField(value ="费用日期")
    private Date costDate;
    private transient String costDateFrom;
    private transient String costDateTo;

    /**
     * 费用日期str
     */
    @TableField("costDateStr")
            @ExcelField(value ="费用日期str")
    private String costDateStr;

    /**
     * 住院号
     */
    @TableField("hospitalizedNo")
            @ExcelField(value ="住院号")
    private String hospitalizedNo;

    /**
     * 就医方式
     */
    @TableField("treatmentMode")
            @ExcelField(value ="就医方式")
    private String treatmentMode;

    /**
     * 结算日期
     */
    @TableField("settlementDate")
            @ExcelField(value ="结算日期")
    private Date settlementDate;
    private transient String settlementDateFrom;
    private transient String settlementDateTo;

    /**
     * 结算日期Str
     */
    @TableField("settlementDateStr")
            @ExcelField(value ="结算日期Str")
    private String settlementDateStr;

    /**
     * 个人编号
     */
    @TableField("personalNo")
            @ExcelField(value ="个人编号")
    private String personalNo;

    /**
     * 参保人姓名
     */
    @TableField("insuredName")
            @ExcelField(value ="参保人姓名")
    private String insuredName;

    /**
     * 数据类型
     */
    @TableField("dataType")
            @ExcelField(value ="数据类型")
    private Integer dataType;

    /**
     * 查找状态
     */
    @TableField("seekState")
            @ExcelField(value ="查找状态")
    private Integer seekState;

    /**
     * 更新类型
     */
    @TableField("updateType")
    @ExcelField(value ="更新类型")
    private Integer updateType;

    /**
     * 所属期Upload
     */
    @TableField("belongDateUpload")
    @ExcelField(value ="所属期Upload")
    private String belongDateUpload;


    /**
     * 序号New
     */
    @TableField("orderNumberNew")
    @ExcelField(value ="序号New")
    private String orderNumberNew;

    /**
     * 提醒类型
     */
    @TableField("warnType")
    @ExcelField(value ="提醒类型")
    private Integer warnType;

    /**
     * 保险类型
     */
    @TableField("repayType")
    @ExcelField(value ="保险类型")
    private Integer repayType;

    /**
     * 剔除明细扣款
     */
    @TableField("resetDataId")
    @ExcelField(value ="剔除明细扣款")
    private String resetDataId;

    /**
     * 备注
     */
    @TableField("COMMENTS")
            @ExcelField(value ="备注")
    private String comments;

    /**
     * 状态
     */
    @TableField("STATE")
            @ExcelField(value ="状态")
    private Integer state;

    /**
     * 是否删除
     */
    @TableField("IS_DELETEMARK")
            @ExcelField(value ="是否删除")
    private Integer isDeletemark;

    /**
     * 修改时间
     */
    @TableField("MODIFY_TIME")
            @ExcelField(value ="修改时间")
    private Date modifyTime;
    private transient String modifyTimeFrom;
    private transient String modifyTimeTo;

    /**
     * 创建时间
     */
    @TableField("CREATE_TIME")
            @ExcelField(value ="创建时间")
    private Date createTime;
    private transient String createTimeFrom;
    private transient String createTimeTo;

    /**
     * 创建人
     */
    @TableField("CREATE_USER_ID")
            @ExcelField(value ="创建人")
    private Long createUserId;

    /**
     * 修改人
     */
    @TableField("MODIFY_USER_ID")
            @ExcelField(value ="修改人")
    private Long modifyUserId;



    public static final String ID ="id" ;

    public static final String PID ="pid" ;

    public static final String BELONGDATE ="belongDate" ;

    public static final String BELONGDATESTR ="belongDateStr" ;

    public static final String HOSPITALCODE ="hospitalCode" ;

    public static final String HOSPITALNAME ="hospitalName" ;

    public static final String ORDERNUMBER ="orderNumber" ;

    public static final String SERIALNO ="serialNo" ;

    public static final String BILLNO ="billNo" ;

    public static final String PROJECTCODE ="projectCode" ;

    public static final String PROJECTNAME ="projectName" ;

    public static final String MEDICALPRICE ="medicalPrice" ;

    public static final String RULENAME ="ruleName" ;

    public static final String DEDUCTPRICE ="deductPrice" ;

    public static final String DEDUCTREASON ="deductReason" ;

    public static final String REPAYMENTPRICE ="repaymentPrice" ;

    public static final String REPAYMENTREASON ="repaymentReason" ;

    public static final String DOCTORNAME ="doctorName" ;

    public static final String DEPTCODE ="deptCode" ;

    public static final String DEPTNAME ="deptName" ;

    public static final String COSTDATE ="costDate" ;

    public static final String COSTDATESTR ="costDateStr" ;

    public static final String HOSPITALIZEDNO ="hospitalizedNo" ;

    public static final String TREATMENTMODE ="treatmentMode" ;

    public static final String SETTLEMENTDATE ="settlementDate" ;

    public static final String SETTLEMENTDATESTR ="settlementDateStr" ;

    public static final String PERSONALNO ="personalNo" ;

    public static final String INSUREDNAME ="insuredName" ;

    public static final String DATATYPE ="dataType" ;

    public static final String SEEKSTATE ="seekState" ;

    public static final String UPDATETYPE ="updateType" ;

    public static final String BELONGDATEUPLOAD ="belongDateUpload" ;

    public static final String ORDERNUMBERNEW ="orderNumberNew" ;

    public static final String WARNTYPE ="warnType" ;

    public static final String REPAYTYPE ="repayType" ;

    public static final String RESETDATAID ="resetDataId";

    public static final String COMMENTS ="COMMENTS" ;

    public static final String STATE ="STATE" ;

    public static final String IS_DELETEMARK ="IS_DELETEMARK" ;

    public static final String MODIFY_TIME ="MODIFY_TIME" ;

    public static final String CREATE_TIME ="CREATE_TIME" ;

    public static final String CREATE_USER_ID ="CREATE_USER_ID" ;

    public static final String MODIFY_USER_ID ="MODIFY_USER_ID" ;
    
}