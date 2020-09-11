package cc.mrbird.febs.yb.entity;

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
 * @since 2020-07-30
 */

@Excel("yb_reconsider_verify")
@Data
@Accessors(chain = true)
public class YbReconsiderVerify implements Serializable{

private static final long serialVersionUID=1L;

    /**
     * 核对申请
     */
                    @TableId(value = "id" )
                    @ExcelField(value ="核对申请")
    private String id;

    /**
     * 复议申请明细
     */
    @TableField("applyDataId")
            @ExcelField(value ="复议申请明细")
    private String applyDataId;

    /**
     * 参考复议医生编码
     */
    @TableField("verifyDoctorCode")
            @ExcelField(value ="参考复议医生编码")
    private String verifyDoctorCode;

    /**
     * 参考复议医生
     */
    @TableField("verifyDoctorName")
            @ExcelField(value ="参考复议医生")
    private String verifyDoctorName;

    /**
     * 参考复议科室编码
     */
    @TableField("verifyDeptCode")
            @ExcelField(value ="参考复议科室编码")
    private String verifyDeptCode;

    /**
     * 参考复议科室
     */
    @TableField("verifyDeptName")
            @ExcelField(value ="参考复议科室")
    private String verifyDeptName;

    /**
     * 操作理由
     */
    @TableField("operateReason")
            @ExcelField(value ="操作理由")
    private String operateReason;

    /**
     * 操作日期
     */
    @TableField("operateDate")
            @ExcelField(value ="操作日期")
    private Date operateDate;
    private transient String operateDateFrom;
    private transient String operateDateTo;

    /**
     * 匹配人代码
     */
    @TableField("matchPersonId")
            @ExcelField(value ="匹配人代码")
    private Long matchPersonId;

    /**
     * 匹配人
     */
    @TableField("matchPersonName")
            @ExcelField(value ="匹配人")
    private String matchPersonName;

    /**
     * 匹配日期
     */
    @TableField("matchDate")
            @ExcelField(value ="匹配日期")
    private Date matchDate;
    private transient String matchDateFrom;
    private transient String matchDateTo;

    /**
     * 审核人代码
     */
    @TableField("reviewerId")
            @ExcelField(value ="审核人代码")
    private Long reviewerId;

    /**
     * 审核人
     */
    @TableField("reviewerName")
            @ExcelField(value ="审核人")
    private String reviewerName;

    /**
     * 审核日期
     */
    @TableField("reviewerDate")
            @ExcelField(value ="审核日期")
    private Date reviewerDate;
    private transient String reviewerDateFrom;
    private transient String reviewerDateTo;

    /**
     * 发送人代码
     */
    @TableField("sendPersonId")
            @ExcelField(value ="发送人代码")
    private Long sendPersonId;

    /**
     * 发送人
     */
    @TableField("sendPersonName")
            @ExcelField(value ="发送人")
    private String sendPersonName;

    /**
     * 发送日期
     */
    @TableField("sendDate")
            @ExcelField(value ="发送日期")
    private Date sendDate;
    private transient String sendDateFrom;
    private transient String sendDateTo;

    /**
     * 通用
     */
    @TableField("currencyField")
            @ExcelField(value ="通用")
    private String currencyField;

    /**
     * 数据类型
     */
    @TableField("dataType")
    //@ExcelField(value ="数据类型")
    private Integer dataType;

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

    public static final String APPLYDATAID ="applyDataId" ;

    public static final String VERIFYDOCTORCODE ="verifyDoctorCode" ;

    public static final String VERIFYDOCTORNAME ="verifyDoctorName" ;

    public static final String VERIFYDEPTCODE ="verifyDeptCode" ;

    public static final String VERIFYDEPTNAME ="verifyDeptName" ;

    public static final String OPERATEREASON ="operateReason" ;

    public static final String OPERATEDATE ="operateDate" ;

    public static final String MATCHPERSONID ="matchPersonId" ;

    public static final String MATCHPERSONNAME ="matchPersonName" ;

    public static final String MATCHDATE ="matchDate" ;

    public static final String REVIEWERID ="reviewerId" ;

    public static final String REVIEWERNAME ="reviewerName" ;

    public static final String REVIEWERDATE ="reviewerDate" ;

    public static final String SENDPERSONID ="sendPersonId" ;

    public static final String SENDPERSONNAME ="sendPersonName" ;

    public static final String SENDDATE ="sendDate" ;

    public static final String CURRENCYFIELD ="currencyField" ;

    public static final String DATATYPE ="dataType" ;

    public static final String COMMENTS ="COMMENTS" ;

    public static final String STATE ="STATE" ;

    public static final String IS_DELETEMARK ="IS_DELETEMARK" ;

    public static final String MODIFY_TIME ="MODIFY_TIME" ;

    public static final String CREATE_TIME ="CREATE_TIME" ;

    public static final String CREATE_USER_ID ="CREATE_USER_ID" ;

    public static final String MODIFY_USER_ID ="MODIFY_USER_ID" ;

        }