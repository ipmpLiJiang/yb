package cc.mrbird.febs.chs.entity;

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
 * @since 2022-06-20
 */

@Excel("yb_chs_manage")
@Data
@Accessors(chain = true)
public class YbChsManage implements Serializable , Comparable<YbChsManage>{

private static final long serialVersionUID=1L;

    /**
     * chs核对申请
     */
                                @ExcelField(value ="chs核对申请")
    private String id;

    /**
     * chs申请明细
     */
    @TableField("applyDataId")
            @ExcelField(value ="chs申请明细")
    private String applyDataId;

    /**
     * 核对Id
     */
    @TableField("verifyId")
            @ExcelField(value ="核对Id")
    private String verifyId;

    /**
     * chs医生编码
     */
    @TableField("readyDoctorCode")
            @ExcelField(value ="chs医生编码")
    private String readyDoctorCode;

    /**
     * chs医生
     */
    @TableField("readyDoctorName")
            @ExcelField(value ="chs医生")
    private String readyDoctorName;

    /**
     * dks编码
     */
    @TableField("readyDksId")
            @ExcelField(value ="dks编码")
    private String readyDksId;

    /**
     * 科室
     */
    @TableField("readyDksName")
            @ExcelField(value ="科室")
    private String readyDksName;

    /**
     * chs变更医生编码
     */
    @TableField("changeDoctorCode")
            @ExcelField(value ="chs变更医生编码")
    private String changeDoctorCode;

    /**
     * chs变更医生
     */
    @TableField("changeDoctorName")
            @ExcelField(value ="chs变更医生")
    private String changeDoctorName;

    /**
     * changeDks编码
     */
    @TableField("changeDksId")
            @ExcelField(value ="changeDks编码")
    private String changeDksId;

    /**
     * 科室To
     */
    @TableField("changeDksName")
            @ExcelField(value ="科室To")
    private String changeDksName;

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
     * 拒绝人代码
     */
    @TableField("refuseId")
            @ExcelField(value ="拒绝人代码")
    private Long refuseId;

    /**
     * 拒绝人
     */
    @TableField("refuseName")
            @ExcelField(value ="拒绝人")
    private String refuseName;

    /**
     * 拒绝理由
     */
    @TableField("refuseReason")
            @ExcelField(value ="拒绝理由")
    private String refuseReason;

    /**
     * 拒绝日期
     */
    @TableField("refuseDate")
            @ExcelField(value ="拒绝日期")
    private Date refuseDate;
    private transient String refuseDateFrom;
    private transient String refuseDateTo;

    /**
     * 管理员代码
     */
    @TableField("adminPersonId")
            @ExcelField(value ="管理员代码")
    private Long adminPersonId;

    /**
     * 管理员
     */
    @TableField("adminPersonName")
            @ExcelField(value ="管理员")
    private String adminPersonName;

    /**
     * 更改日期
     */
    @TableField("adminChangeDate")
            @ExcelField(value ="更改日期")
    private Date adminChangeDate;
    private transient String adminChangeDateFrom;
    private transient String adminChangeDateTo;

    /**
     * 管理员理由
     */
    @TableField("adminReason")
            @ExcelField(value ="管理员理由")
    private String adminReason;

    /**
     * 状态
     */
    @TableField("STATE")
            @ExcelField(value ="状态")
    private Integer state;

    /**
     * 可操作日期
     */
    @TableField("enableDate")
            @ExcelField(value ="可操作日期")
    private Date enableDate;
    private transient String enableDateFrom;
    private transient String enableDateTo;

    /**
     * 操作过程
     */
    @TableField("operateProcess")
            @ExcelField(value ="操作过程")
    private String operateProcess;

    /**
     * 审批状态
     */
    @TableField("approvalState")
            @ExcelField(value ="审批状态")
    private Integer approvalState;

    /**
     * 复议年月Str
     */
    @TableField("applyDateStr")
            @ExcelField(value ="复议年月Str")
    private String applyDateStr;

    /**
     * 序号
     */
    @TableField("orderNum")
            @ExcelField(value ="序号")
    private Integer orderNum;

    /**
     * 院区
     */
    @TableField("areaType")
            @ExcelField(value ="院区")
    private Integer areaType;



    public static final String ID ="id" ;

    public static final String APPLYDATAID ="applyDataId" ;

    public static final String VERIFYID ="verifyId" ;

    public static final String READYDOCTORCODE ="readyDoctorCode" ;

    public static final String READYDOCTORNAME ="readyDoctorName" ;

    public static final String READYDKSID ="readyDksId" ;

    public static final String READYDKSNAME ="readyDksName" ;

    public static final String CHANGEDOCTORCODE ="changeDoctorCode" ;

    public static final String CHANGEDOCTORNAME ="changeDoctorName" ;

    public static final String CHANGEDKSID ="changeDksId" ;

    public static final String CHANGEDKSNAME ="changeDksName" ;

    public static final String OPERATEREASON ="operateReason" ;

    public static final String OPERATEDATE ="operateDate" ;

    public static final String REFUSEID ="refuseId" ;

    public static final String REFUSENAME ="refuseName" ;

    public static final String REFUSEREASON ="refuseReason" ;

    public static final String REFUSEDATE ="refuseDate" ;

    public static final String ADMINPERSONID ="adminPersonId" ;

    public static final String ADMINPERSONNAME ="adminPersonName" ;

    public static final String ADMINCHANGEDATE ="adminChangeDate" ;

    public static final String ADMINREASON ="adminReason" ;

    public static final String STATE ="STATE" ;

    public static final String ENABLEDATE ="enableDate" ;

    public static final String OPERATEPROCESS ="operateProcess" ;

    public static final String APPROVALSTATE ="approvalState" ;

    public static final String APPLYDATESTR ="applyDateStr" ;

    public static final String ORDERNUM ="orderNum" ;

    public static final String AREATYPE ="areaType" ;

@Override
public int compareTo(YbChsManage o) {
        if (this.getId() != null && o.getId() != null) {
        return this.getId().compareTo(o.getId());
        } else if (this.getId() != null) {
        return 1;
        } else {
        return 0;
        }
        }
}