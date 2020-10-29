package cc.mrbird.febs.yb.service;

import cc.mrbird.febs.yb.entity.YbReconsiderApply;
import cc.mrbird.febs.yb.entity.YbReconsiderApplyData;
import com.baomidou.mybatisplus.extension.service.IService;

import java.text.ParseException;
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

    void deleteBatchStateIdsYbReconsiderApplys(String[] Ids, Integer state);

    boolean updateYbReconsiderApplyState(String applyDateStr, Integer state);

    String createReconsiderApplyCheck(YbReconsiderApply ybReconsiderApply);

    YbReconsiderApply findReconsiderApplyByApplyDateStrs(String applyDateStr);

    void updateEnableOverdue(String applyDateStr);

    void updateApplyEndDateOne(String applyDateStr);

    void updateApplyEndDateTwo(String applyDateStr);
}
