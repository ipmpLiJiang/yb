package cc.mrbird.febs.yb.service;

import cc.mrbird.febs.yb.entity.YbAppealManageView;
import cc.mrbird.febs.yb.entity.YbAppealResultDownLoad;
import cc.mrbird.febs.yb.entity.YbAppealResultView;
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
 * @since 2020-08-12
 */
public interface IYbAppealResultViewService extends IService<YbAppealResultView> {

        IPage<YbAppealResultView> findYbAppealResultViews(QueryRequest request, YbAppealResultView ybAppealResultView);

        IPage<YbAppealResultView> findYbAppealResultViewList(QueryRequest request, YbAppealResultView ybAppealResultView);

        IPage<YbAppealResultView> findAppealResultViewResets(QueryRequest request, YbAppealResultView ybAppealResultView);

        IPage<YbAppealResultView> findAppealResultViewRepays(QueryRequest request, YbAppealResultView ybAppealResultView);

        void createYbAppealResultView(YbAppealResultView ybAppealResultView);

        void updateYbAppealResultView(YbAppealResultView ybAppealResultView);

        void deleteYbAppealResultViews(String[]Ids);

        List<YbAppealResultView> findAppealResultViewLists(YbAppealResultView ybAppealResultView);

        List<YbAppealResultDownLoad> findYbAppealResultDownLoadList(YbAppealResultView ybAppealResultView);
        }
