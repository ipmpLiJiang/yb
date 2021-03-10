package cc.mrbird.febs.yb.service;

import cc.mrbird.febs.yb.entity.YbAppealResultReportView;
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
 * @since 2020-09-24
 */
public interface IYbAppealResultReportViewService extends IService<YbAppealResultReportView> {

        IPage<YbAppealResultReportView> findYbAppealResultReportViews(QueryRequest request, YbAppealResultReportView ybAppealResultReportView);

        IPage<YbAppealResultReportView> findYbAppealResultReportViewList(QueryRequest request, YbAppealResultReportView ybAppealResultReportView);

        IPage<YbAppealResultReportView> findAppealResultReportViews(QueryRequest request, YbAppealResultReportView ybAppealResultReportView,boolean isUser);

        void createYbAppealResultReportView(YbAppealResultReportView ybAppealResultReportView);

        void updateYbAppealResultReportView(YbAppealResultReportView ybAppealResultReportView);

        void deleteYbAppealResultReportViews(String[]Ids);

        List<YbAppealResultReportView> findYbAppealResultReportLists(YbAppealResultReportView ybAppealResultReportView,boolean isUser);
        }
