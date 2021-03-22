package cc.mrbird.febs.yb.service;

import cc.mrbird.febs.yb.entity.YbAppealResult;
import cc.mrbird.febs.yb.entity.YbAppealResultView;
import cc.mrbird.febs.yb.entity.YbResultDownLoad;
import com.baomidou.mybatisplus.extension.service.IService;

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
 * @since 2020-07-30
 */
public interface IYbAppealResultService extends IService<YbAppealResult> {

    IPage<YbAppealResult> findYbAppealResults(QueryRequest request, YbAppealResult ybAppealResult);

    IPage<YbAppealResult> findYbAppealResultList(QueryRequest request, YbAppealResult ybAppealResult);

    void createYbAppealResult(YbAppealResult ybAppealResult);

    void updateYbAppealResult(YbAppealResult ybAppealResult);

    void deleteYbAppealResults(String[] Ids);

    YbAppealResult findCreateAppealResult(YbAppealResult ybAppealResult, Long uId, String Uname);

    List<YbResultDownLoad> findAppealResultGroupDepts(YbAppealResultView ybAppealResultView);

    List<YbResultDownLoad> findAppealResultGroupSumDepts(YbAppealResultView ybAppealResultView);

    void updateAppealResulResetDatas(String applyDateStr, Long resetPersonId, String resetPersonName);

    List<YbAppealResult> findAppealResulDataByResets(String applyDateStr, Integer dataType);

    List<YbAppealResult> findAppealResulDataByRepays(String applyDateStr, Integer dataType);

    List<YbAppealResult> findAppealResulDataHandles(String applyDateStr,String hvId);

    List<YbAppealResult> findAppealResultList(YbAppealResult appealResult);

    YbAppealResult findLoadLastAppealResulData(YbAppealResult appealResult);

    int updatAppealResultCancelData(List<YbAppealResult> appealResultList);
}
