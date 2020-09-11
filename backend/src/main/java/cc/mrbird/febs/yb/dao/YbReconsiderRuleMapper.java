package cc.mrbird.febs.yb.dao;

import cc.mrbird.febs.yb.entity.YbReconsiderRule;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.Param;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author viki
 * @since 2020-07-15
 */
public interface YbReconsiderRuleMapper extends BaseMapper<YbReconsiderRule> {
        void updateYbReconsiderRule(YbReconsiderRule ybReconsiderRule);
        IPage<YbReconsiderRule> findYbReconsiderRule(Page page, @Param("ybReconsiderRule") YbReconsiderRule ybReconsiderRule);
        }
