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
public class YbNoticeDataJson {


    /**
     * 培训通知明细
     */
    private String id;

    /**
     * 申诉配置
     */
    private String pid;

    /**
     * 人员编码
     */
    private String personCode;

    /**
     * 人员名称
     */
    private String personName;

    /**
     * 医管id
     */
    private Integer cmId;

    /**
     * 医管名称
     */
    private String cmName;

    /**
     * 类型 1 医管人员类型  2人员
     */
    private Integer ndType;

}