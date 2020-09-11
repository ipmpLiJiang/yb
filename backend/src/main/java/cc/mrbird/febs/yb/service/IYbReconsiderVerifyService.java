package cc.mrbird.febs.yb.service;

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

    void insertReconsiderVerifyImports(String applyDate, Long matchPersonId, String matchPersonName);

    void insertMainReconsiderVerifyImports(String applyDate, Long matchPersonId, String matchPersonName);

    void updateReviewerStates(List<YbReconsiderVerify> list, Long uId, String Uname);

    void updateReconsiderVerifyImports(List<YbReconsiderVerify> list, Long uId, String Uname);

    void updateSendStates(List<YbReconsiderVerify> list, Long uId, String Uname);

    void updateMainSendStates(List<YbReconsiderVerify> list, Long uId, String Uname);
}
