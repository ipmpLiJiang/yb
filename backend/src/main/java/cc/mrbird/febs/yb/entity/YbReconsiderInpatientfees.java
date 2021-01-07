package cc.mrbird.febs.yb.entity;

import java.math.BigDecimal;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
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
 * @since 2020-07-22
 */

@Excel("yb_reconsider_inpatientfees")
@Data
@Accessors(chain = true)
public class YbReconsiderInpatientfees implements Serializable {

private static final long serialVersionUID=1L;

    /**
     * 医保住院His
     */
                    @TableId(value = "id" )
                    @ExcelField(value ="医保住院His")
    private String id;

    /**
     * 住院号
     */
    @TableField("inpatientId")
            @ExcelField(value ="住院号")
    private String inpatientId;

    /**
     * 患者姓名
     */
    @TableField("patientName")
            @ExcelField(value ="患者姓名")
    private String patientName;

    /**
     * HIS结算序号
     */
    @TableField("settlementId")
            @ExcelField(value ="HIS结算序号")
    private String settlementId;

    /**
     * 单据号
     */
    @TableField("billNo")
            @ExcelField(value ="单据号")
    private String billNo;

    /**
     * 交易流水号
     */
    @TableField("transNo")
            @ExcelField(value ="交易流水号")
    private String transNo;

    /**
     * 项目代码
     */
    @TableField("itemId")
            @ExcelField(value ="项目代码")
    private String itemId;

    /**
     * 项目医保编码
     */
    @TableField("itemCode")
            @ExcelField(value ="项目医保编码")
    private String itemCode;

    /**
     * 项目名称
     */
    @TableField("itemName")
            @ExcelField(value ="项目名称")
    private String itemName;

    /**
     * 项目数量
     */
    @TableField("itemCount")
            @ExcelField(value ="项目数量")
    private BigDecimal itemCount;

    /**
     * 项目单价
     */
    @TableField("itemPrice")
            @ExcelField(value ="项目单价")
    private BigDecimal itemPrice;

    /**
     * 项目金额
     */
    @TableField("itemAmount")
            @ExcelField(value ="项目金额")
    private BigDecimal itemAmount;

    /**
     * 费用日期
     */
    @TableField("feeDate")
            @ExcelField(value ="费用日期")
    private Date feeDate;
    private transient String feeDateFrom;
    private transient String feeDateTo;

    /**
     * 住院科室代码
     */
    @TableField("deptId")
            @ExcelField(value ="住院科室代码")
    private String deptId;

    /**
     * 住院科室名称
     */
    @TableField("deptName")
            @ExcelField(value ="住院科室名称")
    private String deptName;

    /**
     * 开方医生代码
     */
    @TableField("orderDocId")
            @ExcelField(value ="开方医生代码")
    private String orderDocId;

    /**
     * 开方医生名称
     */
    @TableField("orderDocName")
            @ExcelField(value ="开方医生名称")
    private String orderDocName;

    /**
     * 执行科室代码
     */
    @TableField("excuteDeptId")
            @ExcelField(value ="执行科室代码")
    private String excuteDeptId;

    /**
     * 执行科室名称
     */
    @TableField("excuteDeptName")
            @ExcelField(value ="执行科室名称")
    private String excuteDeptName;

    /**
     * 执行医生代码
     */
    @TableField("excuteDocId")
            @ExcelField(value ="执行医生代码")
    private String excuteDocId;

    /**
     * 执行医生名称
     */
    @TableField("excuteDocName")
            @ExcelField(value ="执行医生名称")
    private String excuteDocName;

    /**
     * 结算时间
     */
    @TableField("settlementDate")
            @ExcelField(value ="结算时间")
    private Date settlementDate;
    private transient String settlementDateFrom;
    private transient String settlementDateTo;

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
    @ExcelField(value ="数据类型")
    private Integer dataType;

    /**
     * 版本类型
     */
    @TableField("typeno")
    //@ExcelField(value ="版本类型")
    private Integer typeno;

    /**
     * 序号
     */
    @TableField("orderNumber")
    @ExcelField(value = "序号")
    private String orderNumber;

    /**
     * 复议申请明细
     */
    @TableField("applyDataId")
    @ExcelField(value = "复议申请明细")
    private String applyDataId;

    /**
     * 医保项目编码
     */
    @TableField("miCode")
    @ExcelField(value = "医保项目编码")
    private String miCode;

    /**
     * 医保项目名称
     */
    @TableField("hisName")
    @ExcelField(value = "医保项目名称")
    private String hisName;

    /**
     * 医保项目名称
     */
    @TableField("miName")
    @ExcelField(value = "医保项目名称1")
    private String miName;

    /**
     * 类型
     */
    @TableField("dyyz")
    @ExcelField(value = "类型")
    private String dyyz;

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

    public static final String INPATIENTID ="inpatientId" ;

    public static final String PATIENTNAME ="patientName" ;

    public static final String SETTLEMENTID ="settlementId" ;

    public static final String BILLNO ="billNo" ;

    public static final String TRANSNO ="transNo" ;

    public static final String ITEMID ="itemId" ;

    public static final String ITEMCODE ="itemCode" ;

    public static final String ITEMNAME ="itemName" ;

    public static final String ITEMCOUNT ="itemCount" ;

    public static final String ITEMPRICE ="itemPrice" ;

    public static final String ITEMAMOUNT ="itemAmount" ;

    public static final String FEEDATE ="feeDate" ;

    public static final String DEPTID ="deptId" ;

    public static final String DEPTNAME ="deptName" ;

    public static final String ORDERDOCID ="orderDocId" ;

    public static final String ORDERDOCNAME ="orderDocName" ;

    public static final String EXCUTEDEPTID ="excuteDeptId" ;

    public static final String EXCUTEDEPTNAME ="excuteDeptName" ;

    public static final String EXCUTEDOCID ="excuteDocId" ;

    public static final String EXCUTEDOCNAME ="excuteDocName" ;

    public static final String SETTLEMENTDATE ="settlementDate" ;

    public static final String APPLYDATESTR = "applyDateStr";

    public static final String DATATYPE ="dataType" ;

    public static final String TYPENO = "typeno";

    public static final String ORDERNUMBER = "orderNumber";

    public static final String APPLYDATAID = "applyDataId";

    public static final String COMMENTS ="COMMENTS" ;

    public static final String STATE ="STATE" ;

    public static final String IS_DELETEMARK ="IS_DELETEMARK" ;

    public static final String MODIFY_TIME ="MODIFY_TIME" ;

    public static final String CREATE_TIME ="CREATE_TIME" ;

    public static final String CREATE_USER_ID ="CREATE_USER_ID" ;

    public static final String MODIFY_USER_ID ="MODIFY_USER_ID" ;

        }