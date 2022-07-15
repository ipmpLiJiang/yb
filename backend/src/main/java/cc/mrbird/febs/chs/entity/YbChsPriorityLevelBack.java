package cc.mrbird.febs.chs.entity;

import lombok.Data;

/**
 * <p>
 *
 * </p>
 *
 * @author viki
 * @since 2022-07-13
 */

@Data
public class YbChsPriorityLevelBack {

    private String id;

    /**
     * 规则名称
     */
    private String ruleName;

    /**
     * 项目名称
     */
    private String projectName;

}