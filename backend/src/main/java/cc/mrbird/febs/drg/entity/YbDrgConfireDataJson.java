package cc.mrbird.febs.drg.entity;

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
public class YbDrgConfireDataJson {


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
//    private String deptId;

    /**
     * 科室编码
     */
    private String dksId;

    /**
     * 科室名称
     */
//    private String deptName;

    private String dksName;

}