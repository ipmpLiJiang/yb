package cc.mrbird.febs.drg.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.wuwenze.poi.annotation.Excel;
import com.wuwenze.poi.annotation.ExcelField;
import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * <p>
 * VIEW
 * </p>
 *
 * @author viki
 * @since 2020-07-30
 */

@Excel("yb_drg_verify_view")
@Data
@Accessors(chain = true)
public class YbDrgVerifyView implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * id
     */
    @ExcelField(value = "id")
    private String id;


    /**
     * pid
     */
    @ExcelField(value = "pid")
    private String pid;

    /**
     * drg申请明细
     */
    @TableField("applyDataId")
    @ExcelField(value = "drg申请明细")
    private String applyDataId;

    /**
     * drg医生编码
     */
    @TableField("verifyDoctorCode")
    @ExcelField(value = "drg医生编码")
    private String verifyDoctorCode;

    /**
     * drg医生
     */
    @TableField("verifyDoctorName")
    @ExcelField(value = "drg医生")
    private String verifyDoctorName;

    /**
     * drg科室编码
     */
    @TableField("verifyDeptCode")
    @ExcelField(value = "drg科室编码")
    private String verifyDeptCode;

    /**
     * drg科室
     */
    @TableField("verifyDeptName")
    @ExcelField(value = "drg科室")
    private String verifyDeptName;

    /**
     * 状态
     */
    @TableField("STATE")
    @ExcelField(value = "状态")
    private Integer state;

    /**
     * 复议年月Str
     */
    @TableField("applyDateStr")
    @ExcelField(value = "复议年月Str")
    private String applyDateStr;

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
     * 院区
     */
    @TableField("areaType")
    @ExcelField(value = "院区")
    private Integer areaType;

    /**
     * 发送人代码
     */
    @TableField("sendPersonId")
    @ExcelField(value = "发送人代码")
    private Long sendPersonId;

    /**
     * 发送人
     */
    @TableField("sendPersonName")
    @ExcelField(value = "发送人")
    private String sendPersonName;

    /**
     * 发送日期
     */
    @TableField("sendDate")
    @ExcelField(value = "发送日期")
    private Date sendDate;
    private transient String sendDateFrom;
    private transient String sendDateTo;

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
     * 是否核对
     */
    @TableField("isVerify")
    @ExcelField(value = "是否核对")
    private Integer isVerify;

    /**
     * 是否医生
     */
    @TableField("isPerson")
    @ExcelField(value = "是否医生")
    private Integer isPerson;


    public static final String ID = "id";

    public static final String APPLYDATAID = "applyDataId";

    public static final String VERIFYDOCTORCODE = "verifyDoctorCode";

    public static final String VERIFYDOCTORNAME = "verifyDoctorName";

    public static final String VERIFYDEPTCODE = "verifyDeptCode";

    public static final String VERIFYDEPTNAME = "verifyDeptName";

    public static final String STATE = "STATE";

    public static final String APPLYDATESTR = "applyDateStr";

    public static final String ORDERNUMBER = "orderNumber";

    public static final String ORDERNUM = "orderNum";

    public static final String AREATYPE = "areaType";

    public static final String SENDPERSONID = "sendPersonId";

    public static final String SENDPERSONNAME = "sendPersonName";

    public static final String SENDDATE = "sendDate";

    public static final String KS = "ks";

    public static final String JZJLH = "jzjlh";

    public static final String BAH = "bah";

    public static final String WGLX = "wglx";

    public static final String WTMS = "wtms";

    public static final String YLZFY = "ylzfy";

    public static final String WGJE = "wgje";

    public static final String SFBMZCZJCW = "sfbmzczjcw";

    public static final String LY = "ly";

    public static final String ISVERIFY = "isVerify";

    public static final String ISPERSON = "isPerson";

}