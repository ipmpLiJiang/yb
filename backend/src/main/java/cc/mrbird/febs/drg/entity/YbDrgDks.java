package cc.mrbird.febs.drg.entity;

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
 * @since 2022-03-16
 */

@Excel("yb_drg_dks")
@Data
@Accessors(chain = true)
public class YbDrgDks implements Serializable, Comparable<YbDrgDks> {

    private static final long serialVersionUID = 1L;

    /**
     * Id
     */
    @TableId(value = "id", type = IdType.AUTO)
    @ExcelField(value = "Id")
    private Integer id;

    /**
     * 编码
     */
    @TableField("dksId")
    @ExcelField(value = "编码")
    private String dksId;

    /**
     * 名称
     */
    @TableField("dksName")
    @ExcelField(value = "名称")
    private String dksName;

    /**
     * 院区Id
     */
    @TableField("areaId")
    @ExcelField(value = "院区Id")
    private Integer areaId;

    /**
     * 院区
     */
    @TableField("areaName")
    @ExcelField(value = "院区")
    private String areaName;

    /**
     * 通用字段
     */
    @TableField("currencyField")
    @ExcelField(value = "通用字段")
    private String currencyField;

    /**
     * 备注
     */
    @TableField("COMMENTS")
    @ExcelField(value = "备注")
    private String comments;

    /**
     * 状态
     */
    @TableField("STATE")
    @ExcelField(value = "状态")
    private Integer state;

    /**
     * 拼音编码
     */
    @TableField("spellCode")
    @ExcelField(value = "拼音编码")
    private String spellCode;


    public static final String ID = "id";

    public static final String DKSID = "dksId";

    public static final String DKSNAME = "dksName";

    public static final String AREAID = "areaId";

    public static final String AREANAME = "areaName";

    public static final String CURRENCYFIELD = "currencyField";

    public static final String COMMENTS = "COMMENTS";

    public static final String STATE = "STATE";

    public static final String SPELLCODE = "spellCode";

    @Override
    public int compareTo(YbDrgDks o) {
        if (this.getId() != null && o.getId() != null) {
            return this.getId().compareTo(o.getId());
        } else if (this.getId() != null) {
            return 1;
        } else {
            return 0;
        }
    }
}