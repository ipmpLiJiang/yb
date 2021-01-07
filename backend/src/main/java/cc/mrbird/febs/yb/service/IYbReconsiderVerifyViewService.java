package cc.mrbird.febs.yb.service;

import cc.mrbird.febs.yb.entity.YbReconsiderVerifyView;
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
 * @since 2020-07-30
 */
public interface IYbReconsiderVerifyViewService extends IService<YbReconsiderVerifyView> {

    IPage<YbReconsiderVerifyView> findYbReconsiderVerifyViews(QueryRequest request, YbReconsiderVerifyView ybReconsiderVerifyView);

    IPage<YbReconsiderVerifyView> findYbReconsiderVerifyViews(QueryRequest request, YbReconsiderVerifyView ybReconsiderVerifyView, String[] searchType);

    IPage<YbReconsiderVerifyView> findYbReconsiderVerifyViewList(QueryRequest request, YbReconsiderVerifyView ybReconsiderVerifyView);

    List<YbReconsiderVerifyView> findReconsiderVerifyViewLists(YbReconsiderVerifyView ybReconsiderVerifyView);


    void createYbReconsiderVerifyView(YbReconsiderVerifyView ybReconsiderVerifyView);

    void updateYbReconsiderVerifyView(YbReconsiderVerifyView ybReconsiderVerifyView);

    void deleteYbReconsiderVerifyViews(String[] Ids);

    int findReconsiderVerifyApplyDateCounts(String applyDate,Integer dataType,Integer typeno);

    IPage<YbReconsiderVerifyView> findYbReconsiderVerifyViewNulls(QueryRequest request, YbReconsiderVerifyView ybReconsiderVerifyView,String[] searchType);
}
