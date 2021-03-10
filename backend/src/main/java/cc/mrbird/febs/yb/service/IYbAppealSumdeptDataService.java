package cc.mrbird.febs.yb.service;

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
public interface IYbAppealSumdeptDataService extends IService<YbAppealSumdeptData> {

        IPage<YbAppealSumdeptData> findYbAppealSumdeptDatas(QueryRequest request, YbAppealSumdeptData ybAppealSumdeptData);

        IPage<YbAppealSumdeptData> findYbAppealSumdeptDataList(QueryRequest request, YbAppealSumdeptData ybAppealSumdeptData);

        void createYbAppealSumdeptData(YbAppealSumdeptData ybAppealSumdeptData);

        void updateYbAppealSumdeptData(YbAppealSumdeptData ybAppealSumdeptData);

        void deleteYbAppealSumdeptDatas(String[]Ids);

        List<YbAppealSumdeptData> findAppealSumdeptDataList(YbAppealSumdeptData appealSumdeptData);

        void deleteAppealSumdeptDataPids(String[] pIds);
        }
