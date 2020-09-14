package cc.mrbird.febs.yb.service;

import cc.mrbird.febs.yb.entity.YbReconsiderReset;
import cc.mrbird.febs.yb.entity.YbReconsiderResetData;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

import com.baomidou.mybatisplus.core.metadata.IPage;
import cc.mrbird.febs.common.domain.QueryRequest;

/**
 * <p>
 * 服务类
 * </p>
 *
 * @author viki
 * @since 2020-08-17
 */
public interface IYbReconsiderResetService extends IService<YbReconsiderReset> {

    IPage<YbReconsiderReset> findYbReconsiderResets(QueryRequest request, YbReconsiderReset ybReconsiderReset);

    IPage<YbReconsiderReset> findYbReconsiderResetList(QueryRequest request, YbReconsiderReset ybReconsiderReset);

    void createYbReconsiderReset(YbReconsiderReset ybReconsiderReset);

    void updateYbReconsiderReset(YbReconsiderReset ybReconsiderReset);

    void deleteYbReconsiderResets(String[] Ids);

    void importReconsiderResets(YbReconsiderReset ybReconsiderReset, List<YbReconsiderResetData> listData, List<YbReconsiderResetData> listMain);

    YbReconsiderReset findReconsiderResetByApplyDateStr(String applyDateStr);
}
