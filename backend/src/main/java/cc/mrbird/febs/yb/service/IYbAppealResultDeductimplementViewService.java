package cc.mrbird.febs.yb.service;

import cc.mrbird.febs.yb.entity.YbAppealResultDeductimplementView;
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
public interface IYbAppealResultDeductimplementViewService extends IService<YbAppealResultDeductimplementView> {

        IPage<YbAppealResultDeductimplementView> findYbAppealResultDeductimplementViews(QueryRequest request, YbAppealResultDeductimplementView ybAppealResultDeductimplementView);

        IPage<YbAppealResultDeductimplementView> findAppealResultDeductimplementViews(QueryRequest request, YbAppealResultDeductimplementView ybAppealResultDeductimplementView,String keyField,boolean isUser);

        IPage<YbAppealResultDeductimplementView> findYbAppealResultDeductimplementViewList(QueryRequest request, YbAppealResultDeductimplementView ybAppealResultDeductimplementView);

        IPage<YbAppealResultDeductimplementView> findAppealResultDmtView(QueryRequest request, YbAppealResultDeductimplementView ybAppealResultDeductimplementView,String keyField);

        IPage<YbAppealResultDeductimplementView> findAppealResultDmtUserView(QueryRequest request, YbAppealResultDeductimplementView ybAppealResultDeductimplementView,String keyField);

        IPage<YbAppealResultDeductimplementView> findAppealResultDmtViewNew(QueryRequest request, YbAppealResultDeductimplementView ybAppealResultDeductimplementView, String keyField);

        IPage<YbAppealResultDeductimplementView> findAppealResultDmtUserNew(QueryRequest request, YbAppealResultDeductimplementView ybAppealResultDeductimplementView, String keyField);
        void createYbAppealResultDeductimplementView(YbAppealResultDeductimplementView ybAppealResultDeductimplementView);

        void updateYbAppealResultDeductimplementView(YbAppealResultDeductimplementView ybAppealResultDeductimplementView);

        void deleteYbAppealResultDeductimplementViews(String[]Ids);
        }
