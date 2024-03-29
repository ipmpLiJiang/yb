package cc.mrbird.febs.yb.service;

import cc.mrbird.febs.yb.entity.YbHandleVerifyData;
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
 * @since 2020-08-28
 */
public interface IYbHandleVerifyDataService extends IService<YbHandleVerifyData> {

    IPage<YbHandleVerifyData> findYbHandleVerifyDatas(QueryRequest request, YbHandleVerifyData ybHandleVerifyData);

    IPage<YbHandleVerifyData> findYbHandleVerifyDataList(QueryRequest request, YbHandleVerifyData ybHandleVerifyData);

    void createYbHandleVerifyData(YbHandleVerifyData ybHandleVerifyData);

    void updateYbHandleVerifyData(YbHandleVerifyData ybHandleVerifyData);

    void deleteYbHandleVerifyDatas(String[] Ids);

    String importCreateHandleVerifyData(String applyDateStr, Integer areaType,Long uid, String uname);

    void updateSendStates(List<YbHandleVerifyData> list,String applyDateStr,Integer areaType,Long uId, String Uname);

    void updateAllSendStates(String applyDateStr,Integer dataType,Integer areaType,Integer state, Long uId, String Uname);

    YbHandleVerifyData getHandleVerifyDataByApplyDataId(String applyDataId);
}
