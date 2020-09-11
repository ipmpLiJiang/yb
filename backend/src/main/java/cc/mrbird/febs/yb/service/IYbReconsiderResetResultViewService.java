package cc.mrbird.febs.yb.service;

import cc.mrbird.febs.yb.entity.YbReconsiderResetResultView;
import com.baomidou.mybatisplus.extension.service.IService;
import java.util.List;

import com.baomidou.mybatisplus.core.metadata.IPage;
import cc.mrbird.febs.common.domain.QueryRequest;
import com.baomidou.mybatisplus.core.metadata.IPage;

/**
 * <p>
 * VIEW 服务类
 * </p>
 *
 * @author viki
 * @since 2020-09-10
 */
public interface IYbReconsiderResetResultViewService extends IService<YbReconsiderResetResultView> {

        IPage<YbReconsiderResetResultView> findYbReconsiderResetResultViews(QueryRequest request, YbReconsiderResetResultView ybReconsiderResetResultView);

        IPage<YbReconsiderResetResultView> findYbReconsiderResetResultViewList(QueryRequest request, YbReconsiderResetResultView ybReconsiderResetResultView);

        void createYbReconsiderResetResultView(YbReconsiderResetResultView ybReconsiderResetResultView);

        void updateYbReconsiderResetResultView(YbReconsiderResetResultView ybReconsiderResetResultView);

        void deleteYbReconsiderResetResultViews(String[]Ids);
        }
