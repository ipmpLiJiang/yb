package cc.mrbird.febs.drg.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.wuwenze.poi.annotation.Excel;
import com.wuwenze.poi.annotation.ExcelField;
import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;

/**
 * <p>
 *
 * </p>
 *
 * @author viki
 * @since 2021-01-11
 */

@Excel("yb_drg_confire_data")
@Data
@Accessors(chain = true)
public class YbDrgConfireData implements Serializable, Comparable<YbDrgConfireData> {

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
     * 科室
     */
    @TableField("dksName")
    @ExcelField(value ="科室")
    private String dksName;

    public static final String ID = "id";

    public static final String PID = "pid";

    public static final String DKSNAME ="dksName" ;


    @Override
    public int compareTo(YbDrgConfireData o) {
        if (this.getId() != null && o.getId() != null) {
            return this.getId().compareTo(o.getId());
        } else if (this.getId() != null) {
            return 1;
        } else {
            return 0;
        }
    }
}