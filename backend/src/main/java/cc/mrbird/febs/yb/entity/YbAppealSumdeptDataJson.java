package cc.mrbird.febs.yb.entity;

import lombok.Data;

/**
 * <p>
 * 
 * </p>
 *
 * @author viki
 * @since 2021-01-11
 */

@Data
public class YbAppealSumdeptDataJson {


    /**
     * 汇总科室明细
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