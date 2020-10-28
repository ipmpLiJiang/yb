package cc.mrbird.febs.yb.service;

import cc.mrbird.febs.yb.entity.YbAppealResultRepayment;
import com.baomidou.mybatisplus.extension.service.IService;
import java.util.List;

import com.baomidou.mybatisplus.core.metadata.IPage;
import cc.mrbird.febs.common.domain.QueryRequest;
import com.baomidou.mybatisplus.core.metadata.IPage;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author viki
 * @since 2020-10-09
 */
public interface IYbAppealResultRepaymentService extends IService<YbAppealResultRepayment> {

        IPage<YbAppealResultRepayment> findYbAppealResultRepayments(QueryRequest request, YbAppealResultRepayment ybAppealResultRepayment);

        IPage<YbAppealResultRepayment> findYbAppealResultRepaymentList(QueryRequest request, YbAppealResultRepayment ybAppealResultRepayment);

        void createYbAppealResultRepayment(YbAppealResultRepayment ybAppealResultRepayment);

        void updateYbAppealResultRepayment(YbAppealResultRepayment ybAppealResultRepayment);

        String createAppealResultRepayment(YbAppealResultRepayment ybAppealResultRepayment);

        void deleteYbAppealResultRepayments(String[]Ids);

        void batchCreateAppealResultRepaymentNull(List<YbAppealResultRepayment> appealResultRepaymentList,String applyDateStr, Long uid);

        void batchCreateAppealResultRepaymentNull(String applyDateStr, Long uid);
        }
