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

        IPage<YbAppealResultDeductimplementView> findYbAppealResultDeductimplementViewList(QueryRequest request, YbAppealResultDeductimplementView ybAppealResultDeductimplementView);

        void createYbAppealResultDeductimplementView(YbAppealResultDeductimplementView ybAppealResultDeductimplementView);

        void updateYbAppealResultDeductimplementView(YbAppealResultDeductimplementView ybAppealResultDeductimplementView);

        void deleteYbAppealResultDeductimplementViews(String[]Ids);
        }
