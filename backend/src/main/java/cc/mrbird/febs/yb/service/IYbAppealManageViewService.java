package cc.mrbird.febs.yb.service;

import cc.mrbird.febs.yb.entity.YbAppealManage;
import cc.mrbird.febs.yb.entity.YbAppealManageView;
import cc.mrbird.febs.yb.entity.YbReconsiderVerify;
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
public interface IYbAppealManageViewService extends IService<YbAppealManageView> {

    IPage<YbAppealManageView> findYbAppealManageViews(QueryRequest request, YbAppealManageView ybAppealManageView);

    IPage<YbAppealManageView> findYbAppealManageViewList(QueryRequest request, YbAppealManageView ybAppealManageView);

    IPage<YbAppealManageView> findAppealManageUserViews(QueryRequest request, YbAppealManageView ybAppealManageView);

    void createYbAppealManageView(YbAppealManageView ybAppealManageView);

    void updateYbAppealManageView(YbAppealManageView ybAppealManageView);

    void deleteYbAppealManageViews(String[] Ids);


}
