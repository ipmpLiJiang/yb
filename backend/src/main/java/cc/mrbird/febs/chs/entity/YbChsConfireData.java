package cc.mrbird.febs.chs.entity;

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
 * @since 2022-06-30
 */

@Excel("yb_chs_confire_data")
@Data
@Accessors(chain = true)
public class YbChsConfireData implements Serializable, Comparable<YbChsConfireData> {

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
     * 分院
     */
    @TableField("fyid")
    @ExcelField(value = "分院")
    private String fyid;


    public static final String ID = "id";

    public static final String PID = "pid";

    public static final String DKSID = "dksId";

    public static final String DKSNAME = "dksName";

    public static final String FYID = "fyid";

    @Override
    public int compareTo(YbChsConfireData o) {
        if (this.getId() != null && o.getId() != null) {
            return this.getId().compareTo(o.getId());
        } else if (this.getId() != null) {
            return 1;
        } else {
            return 0;
        }
    }
}