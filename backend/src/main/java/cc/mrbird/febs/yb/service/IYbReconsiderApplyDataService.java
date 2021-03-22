package cc.mrbird.febs.yb.service;

import cc.mrbird.febs.yb.entity.YbReconsiderApply;
import cc.mrbird.febs.yb.entity.YbReconsiderApplyData;
import com.baomidou.mybatisplus.extension.service.IService;
import java.util.List;

import com.baomidou.mybatisplus.core.metadata.IPage;
import cc.mrbird.febs.common.domain.QueryRequest;
import com.baomidou.mybatisplus.core.metadata.IPage;
import org.apache.ibatis.annotations.Param;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author viki
 * @since 2020-07-17
 */
public interface IYbReconsiderApplyDataService extends IService<YbReconsiderApplyData> {

        IPage<YbReconsiderApplyData> findYbReconsiderApplyDatas(QueryRequest request, YbReconsiderApplyData ybReconsiderApplyData);

        IPage<YbReconsiderApplyData> findYbReconsiderApplyDataList(QueryRequest request, YbReconsiderApplyData ybReconsiderApplyData);

        void createYbReconsiderApplyData(YbReconsiderApplyData ybReconsiderApplyData);

        void createBatchYbReconsiderApplyData(List<YbReconsiderApplyData> list, YbReconsiderApply ybReconsiderApply);

        void updateYbReconsiderApplyData(YbReconsiderApplyData ybReconsiderApplyData);

        void deleteYbReconsiderApplyDatas(String[]Ids);

        int deleteReconsiderApplyDataByPid(YbReconsiderApplyData ybReconsiderApplyData);

        List<YbReconsiderApplyData> findReconsiderApplyDataByApplyDates(String applyDateStr,Integer areaType, Integer dataType);

        List<YbReconsiderApplyData> findReconsiderApplyDataList(YbReconsiderApplyData reconsiderApplyData);

        List<YbReconsiderApplyData> findReconsiderApplyDataByNotVerifys(String pid,String applyDateStr,Integer areaType, Integer dataType,Integer typeno);

        void createBatchDatas(List<YbReconsiderApplyData> listReconsiderApplyData);

        void  importReconsiderApply(YbReconsiderApply ybReconsiderApply, List<YbReconsiderApplyData> listData, List<YbReconsiderApplyData> listMain);


//        List<YbReconsiderApplyData> findReconsiderApplyDataBetween(String applyDateStr,Integer dataType,Integer startNum,Integer endNum);

//        int findReconsiderApplyDataCount(String applyDateStr,Integer dataType);

        void findReconsiderApplyDataTask(String applyDateStr,Integer areaType);


//        void findReconsiderApplyDataNotTask(String applyDateStr);
}
