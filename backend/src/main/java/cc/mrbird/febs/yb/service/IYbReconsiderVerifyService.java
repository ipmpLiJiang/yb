package cc.mrbird.febs.yb.service;

import cc.mrbird.febs.yb.entity.YbReconsiderApply;
import cc.mrbird.febs.yb.entity.YbReconsiderVerify;
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
 * @since 2020-07-30
 */
public interface IYbReconsiderVerifyService extends IService<YbReconsiderVerify> {

    IPage<YbReconsiderVerify> findYbReconsiderVerifys(QueryRequest request, YbReconsiderVerify ybReconsiderVerify);

    IPage<YbReconsiderVerify> findYbReconsiderVerifyList(QueryRequest request, YbReconsiderVerify ybReconsiderVerify);

    void createYbReconsiderVerify(YbReconsiderVerify ybReconsiderVerify);

    void updateYbReconsiderVerify(YbReconsiderVerify ybReconsiderVerify);

    void deleteYbReconsiderVerifys(String[] Ids);

    void insertReconsiderVerifyImports(String applyDateStr, Integer areaType, Long matchPersonId, String matchPersonName);

    void insertMainReconsiderVerifyImports(String applyDateStr, Integer areaType, Long matchPersonId, String matchPersonName);

    void updateReviewerStates(List<YbReconsiderVerify> list, Long uId, String Uname);

    void  updateAllReviewerStates(String applyDateStr,Integer areaType,int state,int dataType, Long uId, String Uname);

    void updateReconsiderVerifyImports(List<YbReconsiderVerify> list, Long uId, String Uname);

    void updateSendStates(List<YbReconsiderVerify> list, Integer areaType,Integer dataType, Long uId, String Uname);

    void updateAllSendStates(String applyDateStr,Integer areaType,Integer state,Integer dataType, Long uId, String Uname);

    int findReconsiderVerifyResetCheckCounts(String applyDateStr,Integer areaType);

    void importReconsiderDataVerifys(YbReconsiderApply reconsiderApply, int dataType, int typeno, List<YbReconsiderVerify> verifyList);
}
