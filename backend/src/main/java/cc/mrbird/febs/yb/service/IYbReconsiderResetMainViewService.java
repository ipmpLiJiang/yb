package cc.mrbird.febs.yb.service;

import cc.mrbird.febs.yb.entity.YbReconsiderResetMainView;
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
 * @since 2020-08-18
 */
public interface IYbReconsiderResetMainViewService extends IService<YbReconsiderResetMainView> {

        IPage<YbReconsiderResetMainView> findYbReconsiderResetMainViews(QueryRequest request, YbReconsiderResetMainView ybReconsiderResetMainView);

        IPage<YbReconsiderResetMainView> findYbReconsiderResetMainViewList(QueryRequest request, YbReconsiderResetMainView ybReconsiderResetMainView);

        void createYbReconsiderResetMainView(YbReconsiderResetMainView ybReconsiderResetMainView);

        void updateYbReconsiderResetMainView(YbReconsiderResetMainView ybReconsiderResetMainView);

        void deleteYbReconsiderResetMainViews(String[]Ids);
        }
