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
public class YbNoticeJson {

    /**
     * 培训通知
     */
    private String id;


    /**
     * 标题
     */
    private String ntTitle;

    /**
     * 内容简介
     */
    private String ntExplain;

    /**
     * 内容详情
     */
    private String ntDetail;

    /**
     * 发送人员
     */
    private Integer sendType;

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
     * 培训通知明细
     */
    List<YbNoticeDataJson> child;


}