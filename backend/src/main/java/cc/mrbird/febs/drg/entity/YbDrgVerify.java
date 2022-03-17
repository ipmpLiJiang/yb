package cc.mrbird.febs.drg.entity;

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
 * @since 2021-11-23
 */

@Excel("yb_drg_verify")
@Data
@Accessors(chain = true)
public class YbDrgVerify implements Serializable, Comparable<YbDrgVerify> {

    private static final long serialVersionUID = 1L;

    /**
     * drg核对申请
     */
    @ExcelField(value = "drg核对申请")
    private String id;

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
//    @TableField("verifyDeptCode")
//    @ExcelField(value = "drg科室编码")
//    private String verifyDeptCode;

    /**
     * drg科室
     */
//    @TableField("verifyDeptName")
//    @ExcelField(value = "drg科室")
//    private String verifyDeptName;

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
     * 科室编码
     */
    @TableField("verifyDksId")
    @ExcelField(value ="科室编码")
    private String verifyDksId;

    /**
     * 科室
     */
    @TableField("verifyDksName")
    @ExcelField(value = "科室")
    private String verifyDksName;


    public static final String ID = "id";

    public static final String PID = "pid";

    public static final String APPLYDATAID = "applyDataId";

    public static final String VERIFYDOCTORCODE = "verifyDoctorCode";

    public static final String VERIFYDOCTORNAME = "verifyDoctorName";

//    public static final String VERIFYDEPTCODE = "verifyDeptCode";

//    public static final String VERIFYDEPTNAME = "verifyDeptName";

    public static final String STATE = "STATE";

    public static final String APPLYDATESTR = "applyDateStr";

    public static final String ORDERNUMBER = "orderNumber";

    public static final String ORDERNUM = "orderNum";

    public static final String AREATYPE = "areaType";

    public static final String SENDPERSONID = "sendPersonId";

    public static final String SENDPERSONNAME = "sendPersonName";

    public static final String SENDDATE = "sendDate";

    public static final String VERIFYDKSNAME = "verifyDksName";

    @Override
    public int compareTo(YbDrgVerify o) {
        if (this.getId() != null && o.getId() != null) {
            return this.getId().compareTo(o.getId());
        } else if (this.getId() != null) {
            return 1;
        } else {
            return 0;
        }
    }
}