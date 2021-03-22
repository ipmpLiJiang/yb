package cc.mrbird.febs.yb.service;

import cc.mrbird.febs.yb.entity.YbReconsiderApplyData;
import cc.mrbird.febs.yb.entity.YbReconsiderResetData;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

import com.baomidou.mybatisplus.core.metadata.IPage;
import cc.mrbird.febs.common.domain.QueryRequest;
import com.baomidou.mybatisplus.core.metadata.IPage;

/**
 * <p>
 * 服务类
 * </p>
 *
 * @author viki
 * @since 2020-08-17
 */
public interface IYbReconsiderResetDataService extends IService<YbReconsiderResetData> {

    IPage<YbReconsiderResetData> findYbReconsiderResetDatas(QueryRequest request, YbReconsiderResetData ybReconsiderResetData);

    IPage<YbReconsiderResetData> findYbReconsiderResetDataList(QueryRequest request, YbReconsiderResetData ybReconsiderResetData);

    void createYbReconsiderResetData(YbReconsiderResetData ybReconsiderResetData);

    void updateYbReconsiderResetData(YbReconsiderResetData ybReconsiderResetData);

    void deleteYbReconsiderResetDatas(String[] Ids);

    String updateResetDatas(String applyDateStr,Integer areaType, Long uid, String uname, Integer dataType);

    String updateHandleResetDatas(String resultIds, String resetIds, Long uid, String uname);

    List<YbReconsiderResetData> findReconsiderResetDataByApplyDates(String applyDateStr, Integer dataType);

    List<YbReconsiderResetData> findResetNotExistsRepayByApplyDates(String applyDateStr, Integer dataType);

    String updateHandleResetCancelData(String resetId,String applyDateStr);
}
