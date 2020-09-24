package cc.mrbird.febs.yb.service;

import cc.mrbird.febs.yb.entity.YbAppealResultDeductimplement;
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
 * @since 2020-09-24
 */
public interface IYbAppealResultDeductimplementService extends IService<YbAppealResultDeductimplement> {

        IPage<YbAppealResultDeductimplement> findYbAppealResultDeductimplements(QueryRequest request, YbAppealResultDeductimplement ybAppealResultDeductimplement);

        IPage<YbAppealResultDeductimplement> findYbAppealResultDeductimplementList(QueryRequest request, YbAppealResultDeductimplement ybAppealResultDeductimplement);

        void createYbAppealResultDeductimplement(YbAppealResultDeductimplement ybAppealResultDeductimplement);

        void updateYbAppealResultDeductimplement(YbAppealResultDeductimplement ybAppealResultDeductimplement);

        void deleteYbAppealResultDeductimplements(String[]Ids);

        boolean createAppealResultDeductimplement(YbAppealResultDeductimplement ybAppealResultDeductimplement);
        }
