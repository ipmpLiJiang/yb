package cc.mrbird.febs.drg.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.wuwenze.poi.annotation.Excel;
import com.wuwenze.poi.annotation.ExcelField;
import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

/**
 * <p>
 * VIEW
 * </p>
 *
 * @author viki
 * @since 2020-07-30
 */

@Excel("yb_drg_result_view")
@Data
@Accessors(chain = true)
public class YbDrgResultView implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * pid
     */
    @TableField("pid")
    @ExcelField(value = "pid")
    private String pid;

    /**
     * 复议年月Str
     */
    @TableField("applyDateStr")
    @ExcelField(value = "复议年月Str")
    private String applyDateStr;

    /**
     * 核对申请
     */
    @ExcelField(value = "核对申请")
    private String id;

    /**
     * 申请数据明细
     */
    @TableField("applyDataId")
    @ExcelField(value = "申请数据明细")
    private String applyDataId;

    /**
     * 核对申请
     */
    @TableField("verifyId")
    @ExcelField(value = "核对申请")
    private String verifyId;


    /**
     * 申诉
     */
    @TableField("manageId")
    @ExcelField(value = "申诉")
    private String manageId;

    /**
     * 复议医生编码
     */
    @TableField("doctorCode")
    @ExcelField(value = "复议医生编码")
    private String doctorCode;

    /**
     * 复议医生
     */
    @TableField("doctorName")
    @ExcelField(value = "复议医生")
    private String doctorName;

    /**
     * 复议科室编码
     */
//    @TableField("deptCode")
//    @ExcelField(value = "复议科室编码")
//    private String deptCode;

    /**
     * 复议科室
     */
//    @TableField("deptName")
//    @ExcelField(value = "复议科室")
//    private String deptName;

    /**
     * 操作理由
     */
    @TableField("operateReason")
    @ExcelField(value = "操作理由")
    private String operateReason;

    /**
     * 操作日期
     */
    @TableField("operateDate")
    @ExcelField(value = "操作日期")
    private Date operateDate;
    private transient String operateDateFrom;
    private transient String operateDateTo;

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
     * 状态
     */
    @TableField("STATE")
    @ExcelField(value = "状态")
    private Integer state;

    /**
     * 通用
     */
    @TableField("currencyField")
    @ExcelField(value = "通用")
    private String currencyField;

    /**
     * 院区
     */
    @TableField("areaType")
    @ExcelField(value = "院区")
    private Integer areaType;

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
     * 科室编码
     */
    @ExcelField(value ="科室编码")
    private String dksId;

    /**
     * 科室
     */
    @ExcelField(value ="科室")
    private String dksName;

    private transient List<String> listPid;

    private transient Integer fileSize;

    private transient String fileNumber;

    private transient String fileSizeWork;

    public static final String ID = "id";
    public static final String PID = "pid";
    public static final String APPLYDATAID = "applyDataId";
    public static final String VERIFYID = "verifyId";
    public static final String MANAGEID = "manageId";
    public static final String APPLYDATESTR = "applyDateStr";
    public static final String DOCTORCODE = "doctorCode";
    public static final String DOCTORNAME = "doctorName";
//    public static final String DEPTCODE = "deptCode";
//    public static final String DEPTNAME = "deptName";
    public static final String OPERATEREASON = "operateReason";
    public static final String OPERATEDATE = "operateDate";
    public static final String ORDERNUMBER ="orderNumber";
    public static final String ORDERNUM ="orderNum";
    public static final String STATE = "STATE";
    public static final String CURRENCYFIELD = "currencyField";
    public static final String AREATYPE = "areaType";

    public static final String DKSNAME ="dksName" ;

    public static final String KS = "ks";

    public static final String JZJLH = "jzjlh";

    public static final String BAH = "bah";

    public static final String WGLX = "wglx";

    public static final String WTMS = "wtms";

    public static final String YLZFY = "ylzfy";

    public static final String WGJE = "wgje";

    public static final String SFBMZCZJCW = "sfbmzczjcw";

    public static final String LY = "ly";
}