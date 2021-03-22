package cc.mrbird.febs.yb.service;

import cc.mrbird.febs.yb.entity.YbAppealSumdept;
import cc.mrbird.febs.yb.entity.YbAppealSumdeptData;
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
 * @since 2021-03-08
 */
public interface IYbAppealSumdeptService extends IService<YbAppealSumdept> {

        IPage<YbAppealSumdept> findYbAppealSumdepts(QueryRequest request, YbAppealSumdept ybAppealSumdept);

        IPage<YbAppealSumdept> findYbAppealSumdeptList(QueryRequest request, YbAppealSumdept ybAppealSumdept);

        IPage<YbAppealSumdept> findAppealSumdeptView(QueryRequest request, YbAppealSumdept ybAppealSumdept);

        void createYbAppealSumdept(YbAppealSumdept ybAppealSumdept);

        void updateYbAppealSumdept(YbAppealSumdept ybAppealSumdept);

        void deleteYbAppealSumdepts(String[]Ids);

        void createAppealSumdept(YbAppealSumdept ybAppealSumdept, List<YbAppealSumdeptData> createDataList);

        void updateAppealSumdept(YbAppealSumdept ybAppealSumdept, List<YbAppealSumdeptData> createDataList, List<YbAppealSumdeptData> updateDataList);

        YbAppealSumdept findAppealSumdept(YbAppealSumdept appealSumdept);
}
