package cc.mrbird.febs.yb.service;

import cc.mrbird.febs.yb.entity.YbReconsiderApply;
import cc.mrbird.febs.yb.entity.YbReconsiderApplyData;
import com.baomidou.mybatisplus.extension.service.IService;

import java.text.ParseException;
import java.util.Date;
import java.util.List;

import com.baomidou.mybatisplus.core.metadata.IPage;
import cc.mrbird.febs.common.domain.QueryRequest;
import com.baomidou.mybatisplus.core.metadata.IPage;
import org.apache.ibatis.annotations.Param;

/**
 * <p>
 * 服务类
 * </p>
 *
 * @author viki
 * @since 2020-07-23
 */
public interface IYbReconsiderApplyService extends IService<YbReconsiderApply> {

    IPage<YbReconsiderApply> findYbReconsiderApplys(QueryRequest request, YbReconsiderApply ybReconsiderApply);

    IPage<YbReconsiderApply> findYbReconsiderApplyList(QueryRequest request, YbReconsiderApply ybReconsiderApply);

    void createYbReconsiderApply(YbReconsiderApply ybReconsiderApply);

    String updateYbReconsiderApply(YbReconsiderApply ybReconsiderApply,boolean isUpOverdue) throws ParseException;

    void updateYbReconsiderApply(YbReconsiderApply ybReconsiderApply);

    void deleteYbReconsiderApplys(String[] Ids);

    void updateReconsiderApplyState2345(YbReconsiderApply reconsiderApply);

    void deleteBatchStateIdsYbReconsiderApplys(String[] Ids, Integer state);

    boolean updateYbReconsiderApplyState(String applyDateStr,Integer areaType, Integer state);

    String createReconsiderApplyCheck(YbReconsiderApply ybReconsiderApply);

    YbReconsiderApply findReconsiderApplyByApplyDateStrs(String applyDateStr,Integer areaType);

    List<YbReconsiderApply> findReconsiderApplyList(YbReconsiderApply ybReconsiderApply);

    boolean findReconsiderApplyCheckEndDate(String appltDateStr,Integer areaType, int typeno);

    String getSendMessage(String applyDateStr, Date enableDate,Integer areaType, int typeno,boolean isChange);

    String getSendMessage(String applyDateStr);

    void updateEnableOverdue(String applyDateStr,int areaType);

    void updateApplyEndDateOne(String applyDateStr,int areaType);

    void updateApplyEndDateTwo(String applyDateStr,int areaType);

    String createJobState(String applyDateStr,Integer areaType);

    int getReconsiderApplyTypeno(String applyDateStr,Integer areaType);
}
