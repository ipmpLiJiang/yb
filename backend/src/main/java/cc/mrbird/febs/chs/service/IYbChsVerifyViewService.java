package cc.mrbird.febs.chs.service;

import cc.mrbird.febs.chs.entity.YbChsVerifyView;
import cc.mrbird.febs.common.domain.QueryRequest;
import cc.mrbird.febs.chs.entity.YbChsVerifyView;
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
public interface IYbChsVerifyViewService extends IService<YbChsVerifyView> {

    IPage<YbChsVerifyView> findYbChsVerifyViews(QueryRequest request, YbChsVerifyView ybChsVerifyView);

    List<YbChsVerifyView> findChsVerifyViewLists(YbChsVerifyView ybChsVerifyView);

    IPage<YbChsVerifyView> findChsVerifyViewNew(QueryRequest request, YbChsVerifyView ybChsVerifyView);

    int findChsVerifyApplyDateCounts(YbChsVerifyView ybChsVerifyView);

}
