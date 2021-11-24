package cc.mrbird.febs.drg.service;

import cc.mrbird.febs.common.domain.QueryRequest;
import cc.mrbird.febs.drg.entity.YbDrgVerifyView;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * <p>
 * VIEW 服务类
 * </p>
 *
 * @author viki
 * @since 2020-07-30
 */
public interface IYbDrgVerifyViewService extends IService<YbDrgVerifyView> {

    IPage<YbDrgVerifyView> findYbDrgVerifyViews(QueryRequest request, YbDrgVerifyView ybDrgVerifyView);

    List<YbDrgVerifyView> findDrgVerifyViewLists(YbDrgVerifyView ybDrgVerifyView);

    IPage<YbDrgVerifyView> findDrgVerifyViewNew(QueryRequest request, YbDrgVerifyView ybDrgVerifyView);

    int findDrgVerifyApplyDateCounts(YbDrgVerifyView ybDrgVerifyView);

}
