package cc.mrbird.febs.yb.service;

import cc.mrbird.febs.yb.entity.YbReconsiderRepayData;
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
 * @since 2020-09-07
 */
public interface IYbReconsiderRepayDataService extends IService<YbReconsiderRepayData> {

        IPage<YbReconsiderRepayData> findYbReconsiderRepayDatas(QueryRequest request, YbReconsiderRepayData ybReconsiderRepayData);

        IPage<YbReconsiderRepayData> findYbReconsiderRepayDataList(QueryRequest request, YbReconsiderRepayData ybReconsiderRepayData);

        void createYbReconsiderRepayData(YbReconsiderRepayData ybReconsiderRepayData);

        void updateYbReconsiderRepayData(YbReconsiderRepayData ybReconsiderRepayData);

        void deleteYbReconsiderRepayDatas(String[]Ids);

        List<String> findGroupBelongDateStrs(String pid);

        String updateRepayDatas(YbReconsiderRepayData ybReconsiderRepayData,Long uid,String uname);

        String updateHandleRepayDatas(String resultId,String resetId, Long uid, String uname);
        }
