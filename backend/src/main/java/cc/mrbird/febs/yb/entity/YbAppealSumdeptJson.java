package cc.mrbird.febs.yb.entity;

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
public class YbAppealSumdeptJson {

    /**
     * 汇总科室
     */
    private String id;

    /**
     * 编码
     */
    private String asCode;
    /**
     * 名称
     */
    private String asName;

    /**
     * 备注
     */
    private String comments;

    /**
     * 院区
     */
    private Integer areaType;

    /**
     * 状态
     */
    private Integer state;

    /**
     * 是否删除
     */
    private Integer isDeletemark;


    /**
     * 汇总科室明细
     */
    List<YbAppealSumdeptDataJson> child;


}