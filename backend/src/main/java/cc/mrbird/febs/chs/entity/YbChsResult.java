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

@Excel("yb_chs_result")
@Data
@Accessors(chain = true)
public class YbChsResult implements Serializable, Comparable<YbChsResult> {

    private static final long serialVersionUID = 1L;

    /**
     * dgr结果
     */
    @ExcelField(value = "dgr结果")
    private String id;

    /**
     * dgr申请明细
     */
    @TableField("applyDataId")
    @ExcelField(value = "dgr申请明细")
    private String applyDataId;

    /**
     * 核对Id
     */
    @TableField("verifyId")
    @ExcelField(value = "核对Id")
    private String verifyId;

    /**
     * 管理Id
     */
    @TableField("manageId")
    @ExcelField(value = "管理Id")
    private String manageId;

    /**
     * 医生编码
     */
    @TableField("doctorCode")
    @ExcelField(value = "医生编码")
    private String doctorCode;

    /**
     * 医生
     */
    @TableField("doctorName")
    @ExcelField(value = "医生")
    private String doctorName;

    /**
     * dks编码
     */
    @TableField("dksId")
    @ExcelField(value = "dks编码")
    private String dksId;

    /**
     * 科室
     */
    @TableField("dksName")
    @ExcelField(value = "科室")
    private String dksName;

    /**
     * 理由
     */
    @TableField("operateReason")
    @ExcelField(value = "理由")
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
     * 数据类型
     */
    @TableField("dataType")
    @ExcelField(value = "数据类型")
    private Integer dataType;

    /**
     * 分院
     */
    @TableField("fyid")
    @ExcelField(value = "分院")
    private String fyid;

    /**
     * 初始楼层编码
     */
    @TableField("initDeptId")
    @ExcelField(value = "初始楼层编码")
    private String initDeptId;

    /**
     * 初始楼层
     */
    @TableField("initDeptName")
    @ExcelField(value = "初始楼层")
    private String initDeptName;


    public static final String ID = "id";

    public static final String APPLYDATAID = "applyDataId";

    public static final String VERIFYID = "verifyId";

    public static final String MANAGEID = "manageId";

    public static final String DOCTORCODE = "doctorCode";

    public static final String DOCTORNAME = "doctorName";

    public static final String DKSID = "dksId";

    public static final String DKSNAME = "dksName";

    public static final String OPERATEREASON = "operateReason";

    public static final String OPERATEDATE = "operateDate";

    public static final String STATE = "STATE";

    public static final String APPLYDATESTR = "applyDateStr";

    public static final String ORDERNUM = "orderNum";

    public static final String AREATYPE = "areaType";

    public static final String DATATYPE = "dataType";

    public static final String FYID = "fyid";

    public static final String INITDEPTID = "initDeptId";

    public static final String INITDEPTNAME = "initDeptName";

    @Override
    public int compareTo(YbChsResult o) {
        if (this.getId() != null && o.getId() != null) {
            return this.getId().compareTo(o.getId());
        } else if (this.getId() != null) {
            return 1;
        } else {
            return 0;
        }
    }
}
