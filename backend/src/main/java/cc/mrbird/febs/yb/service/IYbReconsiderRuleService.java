package cc.mrbird.febs.yb.service;

import cc.mrbird.febs.yb.entity.YbReconsiderRule;
import com.baomidou.mybatisplus.extension.service.IService;
import java.util.List;

import com.baomidou.mybatisplus.core.metadata.IPage;
import cc.mrbird.febs.common.domain.QueryRequest;
import com.baomidou.mybatisplus.core.metadata.IPage;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author viki
 * @since 2020-07-15
 */
public interface IYbReconsiderRuleService extends IService<YbReconsiderRule> {

        IPage<YbReconsiderRule> findYbReconsiderRules(QueryRequest request, YbReconsiderRule ybReconsiderRule);

        IPage<YbReconsiderRule> findYbReconsiderRuleList(QueryRequest request, YbReconsiderRule ybReconsiderRule);

        void createYbReconsiderRule(YbReconsiderRule ybReconsiderRule);

        void updateYbReconsiderRule(YbReconsiderRule ybReconsiderRule);

        void deleteYbReconsiderRules(String[]Ids);

        List<YbReconsiderRule> findReconsiderRuleList(YbReconsiderRule ybReconsiderRule);
        }
