package cc.mrbird.febs.drg.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.wuwenze.poi.annotation.ExcelField;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * <p>
 *
 * </p>
 *
 * @author viki
 * @since 2021-11-23
 */

@Data
public class YbDrgJkDataDept implements Serializable {

    /**
     * bq_id
     */
    @TableField("bq_id")
    @ExcelField(value = "bq_id")
    private int bq_id;

    /**
     * bq_code
     */
    @TableField("bq_code")
    @ExcelField(value = "bq_code")
    private String bq_code;

    /**
     * bq_name
     */
    @TableField("bq_name")
    @ExcelField(value = "bq_name")
    private String bq_name;

    /**
     * ks_id
     */
    @TableField("ks_id")
    @ExcelField(value = "ks_id")
    private String ks_id;

    /**
     * ks_name
     */
    @TableField("ks_name")
    @ExcelField(value = "ks_name")
    private String ks_name;

    /**
     * dzy_id
     */
    @TableField("dzy_id")
    @ExcelField(value = "dzy_id")
    private String dzy_id;


    /**
     * dzy_name
     */
    @TableField("dzy_name")
    @ExcelField(value = "dzy_name")
    private String dzy_name;

    /**
     * yq
     */
    @TableField("yq")
    @ExcelField(value = "yq")
    private String yq;


}