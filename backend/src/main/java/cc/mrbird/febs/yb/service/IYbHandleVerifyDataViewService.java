package cc.mrbird.febs.yb.service;

import cc.mrbird.febs.yb.entity.YbHandleVerifyDataView;
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
 * @since 2020-08-28
 */
public interface IYbHandleVerifyDataViewService extends IService<YbHandleVerifyDataView> {

        IPage<YbHandleVerifyDataView> findYbHandleVerifyDataViews(QueryRequest request, YbHandleVerifyDataView ybHandleVerifyDataView);

        IPage<YbHandleVerifyDataView> findYbHandleVerifyDataViewList(QueryRequest request, YbHandleVerifyDataView ybHandleVerifyDataView);

        void createYbHandleVerifyDataView(YbHandleVerifyDataView ybHandleVerifyDataView);

        void updateYbHandleVerifyDataView(YbHandleVerifyDataView ybHandleVerifyDataView);

        void deleteYbHandleVerifyDataViews(String[]Ids);
        }
