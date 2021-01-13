package cc.mrbird.febs.yb.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.wuwenze.poi.annotation.Excel;
import com.wuwenze.poi.annotation.ExcelField;
import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * <p>
 * 
 * </p>
 *
 * @author viki
 * @since 2021-01-11
 */

@Data
public class YbAppealConfireJson{

    /**
     * 申诉配置
     */
    private String id;

    /**
     * 医生编码
     */
    private String doctorCode;

    /**
     * 医生名称
     */
    private String doctorName;

    /**
     * 管理员状态
     */
    private Integer adminType;

    /**
     * 备注
     */
    private String comments;

    /**
     * 状态
     */
    private Integer state;

    /**
     * 是否删除
     */
    private Integer isDeletemark;


    /**
     * 申诉配置明细
     */
    List<YbAppealConfireDataJson> child;


}