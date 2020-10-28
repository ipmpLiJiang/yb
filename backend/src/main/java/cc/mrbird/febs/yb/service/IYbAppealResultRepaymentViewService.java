package cc.mrbird.febs.yb.service;

import cc.mrbird.febs.yb.entity.YbAppealResultRepaymentView;
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
 * @since 2020-10-09
 */
public interface IYbAppealResultRepaymentViewService extends IService<YbAppealResultRepaymentView> {

        IPage<YbAppealResultRepaymentView> findYbAppealResultRepaymentViews(QueryRequest request, YbAppealResultRepaymentView ybAppealResultRepaymentView);

        IPage<YbAppealResultRepaymentView> findYbAppealResultRepaymentViewList(QueryRequest request, YbAppealResultRepaymentView ybAppealResultRepaymentView);

        void createYbAppealResultRepaymentView(YbAppealResultRepaymentView ybAppealResultRepaymentView);

        void updateYbAppealResultRepaymentView(YbAppealResultRepaymentView ybAppealResultRepaymentView);

        void deleteYbAppealResultRepaymentViews(String[]Ids);

        List<YbAppealResultRepaymentView> findAppealResultRepaymentViewList(YbAppealResultRepaymentView ybAppealResultRepaymentView);
        }
