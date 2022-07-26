package cc.mrbird.febs.chs.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
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
 * @since 2022-07-18
 */

@Excel("yb_chs_verify_msg")
@Data
@Accessors(chain = true)
public class YbChsVerifyMsg implements Serializable, Comparable<YbChsVerifyMsg> {

    private static final long serialVersionUID = 1L;

    /**
     * id
     */
    @TableId(value = "id", type = IdType.AUTO)
    @ExcelField(value = "id")
    private Integer id;

    /**
     * 复议年月Str
     */
    @TableField("applyDateStr")
    @ExcelField(value = "复议年月Str")
    private String applyDateStr;

    /**
     * 项目名称
     */
    @TableField("projectName")
    @ExcelField(value = "项目名称")
    private String projectName;

    /**
     * 规则名称
     */
    @TableField("ruleName")
    @ExcelField(value = "规则名称")
    private String ruleName;

    /**
     * 住院门诊
     */
    @TableField("zymzName")
    @ExcelField(value = "住院门诊")
    private String zymzName;

    /**
     * 院区
     */
    @TableField("areaType")
    @ExcelField(value = "院区")
    private Integer areaType;

    /**
     * 辅助字段
     */
    @TableField("currencyField")
    @ExcelField(value ="辅助字段")
    private String currencyField;

    /**
     * 辅助字段2
     */
    @TableField("currencyField2")
    @ExcelField(value ="辅助字段2")
    private String currencyField2;

    private transient String ruleProject;

    private transient String ids;

    public static final String ID = "id";

    public static final String APPLYDATESTR = "applyDateStr";

    public static final String PROJECTNAME = "projectName";

    public static final String RULENAME = "ruleName";

    public static final String ZYMZNAME = "zymzName";

    public static final String AREATYPE = "areaType";

    public static final String CURRENCYFIELD ="currencyField" ;

    public static final String CURRENCYFIELD2 ="currencyField2" ;

    @Override
    public int compareTo(YbChsVerifyMsg o) {
        if (this.getId() != null && o.getId() != null) {
            return this.getId().compareTo(o.getId());
        } else if (this.getId() != null) {
            return 1;
        } else {
            return 0;
        }
    }
}