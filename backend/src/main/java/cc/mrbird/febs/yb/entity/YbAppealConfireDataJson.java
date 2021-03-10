package cc.mrbird.febs.yb.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.wuwenze.poi.annotation.Excel;
import com.wuwenze.poi.annotation.ExcelField;
import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.util.Date;

/**
 * <p>
 * 
 * </p>
 *
 * @author viki
 * @since 2021-01-11
 */

@Data
public class YbAppealConfireDataJson{


    /**
     * 申诉配置明细
     */
    private String id;

    /**
     * 申诉配置
     */
    private String pid;

    /**
     * 科室编码
     */
    private String deptId;

    /**
     * 科室名称
     */
    private String deptName;

}