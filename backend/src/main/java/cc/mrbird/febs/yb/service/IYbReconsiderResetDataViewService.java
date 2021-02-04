package cc.mrbird.febs.yb.service;

import cc.mrbird.febs.yb.entity.YbReconsiderResetDataView;
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
public interface IYbReconsiderResetDataViewService extends IService<YbReconsiderResetDataView> {

        IPage<YbReconsiderResetDataView> findYbReconsiderResetDataViews(QueryRequest request, YbReconsiderResetDataView ybReconsiderResetDataView);

        IPage<YbReconsiderResetDataView> findYbReconsiderResetDataViewList(QueryRequest request, YbReconsiderResetDataView ybReconsiderResetDataView);

        IPage<YbReconsiderResetDataView> findReconsiderResetDataViews(QueryRequest request, YbReconsiderResetDataView ybReconsiderResetDataView);

        void createYbReconsiderResetDataView(YbReconsiderResetDataView ybReconsiderResetDataView);

        void updateYbReconsiderResetDataView(YbReconsiderResetDataView ybReconsiderResetDataView);

        List<YbReconsiderResetDataView> findYbReconsiderResetDataList(YbReconsiderResetDataView ybReconsiderResetDataView);

        void deleteYbReconsiderResetDataViews(String[]Ids);
        }
