package cc.mrbird.febs.drg.service;

import cc.mrbird.febs.drg.entity.YbDrgApply;
import cc.mrbird.febs.drg.entity.YbDrgVerify;
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
 * @since 2021-11-23
 */
public interface IYbDrgVerifyService extends IService<YbDrgVerify> {

    IPage<YbDrgVerify> findYbDrgVerifys(QueryRequest request, YbDrgVerify ybDrgVerify);

    IPage<YbDrgVerify> findYbDrgVerifyList(QueryRequest request, YbDrgVerify ybDrgVerify);

    void createYbDrgVerify(YbDrgVerify ybDrgVerify);

    void updateYbDrgVerify(YbDrgVerify ybDrgVerify);

    void deleteYbDrgVerifys(String[] Ids);

    void deleteDrgVerifyState(YbDrgVerify delVerify);

    void importDrgDataVerifys(YbDrgApply drgApply, List<YbDrgVerify> verifyList);

    void insertDrgVerifyImports(String applyDateStr, Integer areaType, Long matchPersonId, String matchPersonName);

    void updateSendStates(List<YbDrgVerify> list, Integer areaType, Long uId, String Uname);

    void updateAllSendStates(String applyDateStr, Integer areaType, Integer state, Long uId, String Uname);

    void updateReviewerStates(List<YbDrgVerify> list);

    void updateAllReviewerStates(String applyDateStr, Integer areaType, int state);

    void updateBackStates(String applyDateStr, Integer areaType, int state);

    void updateDrgVerifyImports(List<YbDrgVerify> list);
}
