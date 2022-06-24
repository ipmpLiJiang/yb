package cc.mrbird.febs.chs.service;

import cc.mrbird.febs.chs.entity.YbChsApply;
import cc.mrbird.febs.chs.entity.YbChsApplyData;
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
 * @since 2022-06-21
 */
public interface IYbChsApplyDataService extends IService<YbChsApplyData> {

    IPage<YbChsApplyData> findYbChsApplyDatas(QueryRequest request, YbChsApplyData ybChsApplyData);

    IPage<YbChsApplyData> findYbChsApplyDataList(QueryRequest request, YbChsApplyData ybChsApplyData);

    void createYbChsApplyData(YbChsApplyData ybChsApplyData);

    void updateYbChsApplyData(YbChsApplyData ybChsApplyData);

    void deleteYbChsApplyDatas(String[] Ids);

    void importChsApply(YbChsApply ybChsApply, List<YbChsApplyData> listData);

    String delChsJk(String applyDateStr, Integer areaType);

    int deleteChsApplyDataByPid(YbChsApplyData ybChsApplyData);

    String findChsApplyDataTask(String applyDateStr, Integer areaType, Integer isOutpfees);

    String findChsApplyProjCodeTask(String applyDateStr, Integer areaType, Integer isOutpfees);

    String findChsApplyDataNotTask(String applyDateStr, Integer areaType, Integer isOutpfees);
}
