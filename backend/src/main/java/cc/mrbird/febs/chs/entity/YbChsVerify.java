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

@Excel("yb_chs_verify")
@Data
@Accessors(chain = true)
public class YbChsVerify implements Serializable , Comparable<YbChsVerify>{

private static final long serialVersionUID=1L;

    /**
     * id
     */
                                @ExcelField(value ="id")
    private String id;

    /**
     * chs申请明细
     */
    @TableField("applyDataId")
            @ExcelField(value ="chs申请明细")
    private String applyDataId;

    /**
     * chs医生编码
     */
    @TableField("verifyDoctorCode")
            @ExcelField(value ="chs医生编码")
    private String verifyDoctorCode;

    /**
     * chs医生
     */
    @TableField("verifyDoctorName")
            @ExcelField(value ="chs医生")
    private String verifyDoctorName;

    /**
     * chs编码
     */
    @TableField("verifyDksId")
            @ExcelField(value ="chs编码")
    private String verifyDksId;

    /**
     * chs科室
     */
    @TableField("verifyDksName")
            @ExcelField(value ="chs科室")
    private String verifyDksName;

    /**
     * 状态
     */
    @TableField("STATE")
            @ExcelField(value ="状态")
    private Integer state;

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



    public static final String ID ="id" ;

    public static final String APPLYDATAID ="applyDataId" ;

    public static final String VERIFYDOCTORCODE ="verifyDoctorCode" ;

    public static final String VERIFYDOCTORNAME ="verifyDoctorName" ;

    public static final String VERIFYDKSID ="verifyDksId" ;

    public static final String VERIFYDKSNAME ="verifyDksName" ;

    public static final String STATE ="STATE" ;

    public static final String APPLYDATESTR ="applyDateStr" ;

    public static final String ORDERNUM ="orderNum" ;

    public static final String AREATYPE ="areaType" ;

    public static final String SENDPERSONID ="sendPersonId" ;

    public static final String SENDPERSONNAME ="sendPersonName" ;

    public static final String SENDDATE ="sendDate" ;

@Override
public int compareTo(YbChsVerify o) {
        if (this.getId() != null && o.getId() != null) {
        return this.getId().compareTo(o.getId());
        } else if (this.getId() != null) {
        return 1;
        } else {
        return 0;
        }
        }
}