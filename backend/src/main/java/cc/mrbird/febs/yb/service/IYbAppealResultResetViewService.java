package cc.mrbird.febs.yb.service;

import cc.mrbird.febs.common.domain.QueryRequest;
import cc.mrbird.febs.yb.entity.YbAppealResultResetView;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * <p>
 * VIEW 服务类
 * </p>
 *
 * @author viki
 * @since 2020-08-12
 */
public interface IYbAppealResultResetViewService extends IService<YbAppealResultResetView> {

    IPage<YbAppealResultResetView> findAppealResultResetViews(QueryRequest request, YbAppealResultResetView appealResultResetView, String keyField);

    List<YbAppealResultResetView> findAppealResultResetLists(YbAppealResultResetView appealResultResetView);
}
