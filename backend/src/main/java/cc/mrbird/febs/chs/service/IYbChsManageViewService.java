package cc.mrbird.febs.chs.service;

import cc.mrbird.febs.chs.entity.YbChsManageView;
import cc.mrbird.febs.common.domain.QueryRequest;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * <p>
 * VIEW 服务类
 * </p>
 *
 * @author viki
 * @since 2020-07-30
 */
public interface IYbChsManageViewService extends IService<YbChsManageView> {
    IPage<YbChsManageView> findYbChsManageLikeViews(QueryRequest request, YbChsManageView ybChsManageView, String keyField, boolean isConf);

    IPage<YbChsManageView> findYbChsManageViews(QueryRequest request, YbChsManageView ybChsManageView);
}
