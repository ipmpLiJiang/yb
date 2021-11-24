package cc.mrbird.febs.yb.service;

import cc.mrbird.febs.common.domain.QueryRequest;
import cc.mrbird.febs.yb.entity.YbDrgApply;
import cc.mrbird.febs.yb.entity.YbDrgApplyData;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author viki
 * @since 2020-07-17
 */
public interface IYbDrgApplyDataService extends IService<YbDrgApplyData> {

        IPage<YbDrgApplyData> findYbDrgApplyDatas(QueryRequest request, YbDrgApplyData ybDrgApplyData);

        IPage<YbDrgApplyData> findYbDrgApplyDataList(QueryRequest request, YbDrgApplyData ybDrgApplyData);

        void createYbDrgApplyData(YbDrgApplyData ybDrgApplyData);

        void createBatchYbDrgApplyData(List<YbDrgApplyData> list, YbDrgApply ybDrgApply);

        void updateYbDrgApplyData(YbDrgApplyData ybDrgApplyData);

        void deleteYbDrgApplyDatas(String[] Ids);

        int deleteDrgApplyDataByPid(YbDrgApplyData ybDrgApplyData);

        List<YbDrgApplyData> findDrgApplyDataByApplyDates(YbDrgApply reconsiderApply);

        List<YbDrgApplyData> findDrgApplyDataList(YbDrgApplyData reconsiderApplyData);
}
