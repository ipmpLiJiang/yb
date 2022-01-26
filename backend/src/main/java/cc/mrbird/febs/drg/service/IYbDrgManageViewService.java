package cc.mrbird.febs.drg.service;

import cc.mrbird.febs.common.domain.QueryRequest;
import cc.mrbird.febs.system.domain.User;
import cc.mrbird.febs.drg.entity.YbDrgManageView;
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
public interface IYbDrgManageViewService extends IService<YbDrgManageView> {
    IPage<YbDrgManageView> findDrgManageView(QueryRequest request, YbDrgManageView ybDrgManageView, String keyField, boolean isConf);
}
