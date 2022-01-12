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
 * @since 2021-01-11
 */

@Excel("yb_appeal_confire_data")
@Data
@Accessors(chain = true)
public class YbAppealConfireData implements Serializable, Comparable<YbAppealConfireData> {

    private static final long serialVersionUID = 1L;

    /**
     * 申诉配置明细
     */
    @ExcelField(value = "申诉配置明细")
    private String id;

    /**
     * 申诉配置
     */
    @ExcelField(value = "申诉配置")
    private String pid;

    /**
     * 科室编码
     */
    @TableField("deptId")
    @ExcelField(value = "科室编码")
    private String deptId;

    /**
     * 科室名称
     */
    @TableField("deptName")
    @ExcelField(value = "科室名称")
    private String deptName;

    /**
     * 科室
     */
    @TableField("dksName")
    @ExcelField(value ="科室")
    private String dksName;

    public static final String ID = "id";

    public static final String PID = "pid";

    public static final String DEPTID = "deptId";

    public static final String DEPTNAME = "deptName";

    public static final String DKSNAME ="dksName" ;


    @Override
    public int compareTo(YbAppealConfireData o) {
        if (this.getId() != null && o.getId() != null) {
            return this.getId().compareTo(o.getId());
        } else if (this.getId() != null) {
            return 1;
        } else {
            return 0;
        }
    }
}