package cc.mrbird.febs.chs.service;

import cc.mrbird.febs.chs.entity.YbChsApply;
import cc.mrbird.febs.chs.entity.YbChsVerify;
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
 * @since 2022-06-20
 */
public interface IYbChsVerifyService extends IService<YbChsVerify> {

    IPage<YbChsVerify> findYbChsVerifys(QueryRequest request, YbChsVerify ybChsVerify);

    IPage<YbChsVerify> findYbChsVerifyList(QueryRequest request, YbChsVerify ybChsVerify);

    void createYbChsVerify(YbChsVerify ybChsVerify);

    void updateYbChsVerify(YbChsVerify ybChsVerify);

    void deleteYbChsVerifys(String[] Ids);

    void insertChsVerifyImports(String applyDateStr, Integer areaType, Long matchPersonId, String matchPersonName);

    void updateReviewerStates(List<YbChsVerify> list);

    void updateSendStates(List<YbChsVerify> list, Integer areaType, Long uId, String Uname);

    void updateAllSendStates(String applyDateStr, Integer areaType, Integer state, Long uId, String Uname);

    void updateAllReviewerStates(String applyDateStr, Integer areaType, int state);

    void updateBackStates(String applyDateStr, Integer areaType, int state);

    void updateChsVerifyImports(List<YbChsVerify> list);

    void importChsDataVerifys(YbChsApply drgApply, List<YbChsVerify> verifyList);

    void deleteChsVerifyState(YbChsVerify delVerify);

    String createEndJobState(String applyDateStr, Integer areaType, int[] jobTypeList);
}
