package cc.mrbird.febs.chs.entity;

import cc.mrbird.febs.yb.entity.YbAppealConfireDataJson;
import lombok.Data;

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
public class YbChsConfireJson {

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
     * 院区
     */
    private Integer areaType;

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
    List<YbChsConfireDataJson> child;


}