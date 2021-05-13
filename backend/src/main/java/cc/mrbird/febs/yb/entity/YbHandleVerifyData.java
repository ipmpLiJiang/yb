package cc.mrbird.febs.yb.entity;

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
 * @since 2020-08-28
 */

@Excel("yb_handle_verify_data")
@Data
@Accessors(chain = true)
public class YbHandleVerifyData implements Serializable{

private static final long serialVersionUID=1L;

    /**
     * 手动核对明细
     */
                                @ExcelField(value ="手动核对明细")
    private String id;

    /**
     * 手动核对Id
     */
            @ExcelField(value ="手动核对Id")
    private String pid;

//    /**
//     * 剔除明细Id
//     */
//    @TableField("resetId")
//            @ExcelField(value ="剔除明细Id")
//    private String resetId;

    /**
     * 复议申请明细
     */
    @TableField("applyDataId")
            @ExcelField(value ="复议申请明细")
    private String applyDataId;

    /**
     * 核对Id
     */
    @TableField("verifyId")
            @ExcelField(value ="核对Id")
    private String verifyId;

    /**
     * 管理Id
     */
    @TableField("manageId")
            @ExcelField(value ="管理Id")
    private String manageId;

    /**
     * 复议医生编码
     */
    @TableField("doctorCode")
            @ExcelField(value ="复议医生编码")
    private String doctorCode;

    /**
     * 复议医生
     */
    @TableField("doctorName")
            @ExcelField(value ="复议医生")
    private String doctorName;

    /**
     * 复议科室编码
     */
    @TableField("deptCode")
            @ExcelField(value ="复议科室编码")
    private String deptCode;

    /**
     * 复议科室
     */
    @TableField("deptName")
            @ExcelField(value ="复议科室")
    private String deptName;

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
     * 数据类型
     */
    @TableField("dataType")
            @ExcelField(value ="数据类型")
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
     * 版本类型
     */
    @TableField("typeno")
    //@ExcelField(value ="版本类型")
    private Integer typeno;

    /**
     * 开方医生编码
     */
    @TableField("orderDoctorCode")
    @ExcelField(value ="开方医生编码")
    private String orderDoctorCode;

    /**
     * 开方医生名称
     */
    @TableField("orderDoctorName")
    @ExcelField(value ="开方医生名称")
    private String orderDoctorName;

    /**
     * 开方科室编码
     */
    @TableField("orderDeptCode")
    @ExcelField(value ="开方科室编码")
    private String orderDeptCode;

    /**
     * 开方科室名称
     */
    @TableField("orderDeptName")
    @ExcelField(value ="开方科室名称")
    private String orderDeptName;

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
     * 复议上传Id
     */
    @TableField("resultId")
            @ExcelField(value ="复议上传Id")
    private String resultId;

    /**
     * 关联Id
     */
    @TableField("relatelDataId")
    @ExcelField(value = "关联Id")
    private String relatelDataId;

    public static final String ID ="id" ;

    public static final String PID ="pid" ;

    public static final String RESETID ="resetId" ;

    public static final String APPLYDATAID ="applyDataId" ;

    public static final String VERIFYID ="verifyId" ;

    public static final String MANAGEID ="manageId" ;

    public static final String DOCTORCODE ="doctorCode" ;

    public static final String DOCTORNAME ="doctorName" ;

    public static final String DEPTCODE ="deptCode" ;

    public static final String DEPTNAME ="deptName" ;

    public static final String SENDPERSONID ="sendPersonId" ;

    public static final String SENDPERSONNAME ="sendPersonName" ;

    public static final String SENDDATE ="sendDate" ;

    public static final String DATATYPE ="dataType" ;

    public static final String ORDERNUMBER = "orderNumber";

    public static final String ORDERNUM = "orderNum";

    public static final String TYPENO = "typeno";

    public static final String ORDERDOCTORCODE = "orderDoctorCode";

    public static final String ORDERDOCTORNAME = "orderDoctorName";

    public static final String ORDERDEPTCODE = "orderDeptCode";

    public static final String ORDERDEPTNAME = "orderDeptName";

    public static final String STATE ="STATE" ;

    public static final String IS_DELETEMARK ="IS_DELETEMARK" ;

    public static final String RESULTID ="resultId" ;

    public static final String RELATELDATAID = "relatelDataId";

        }