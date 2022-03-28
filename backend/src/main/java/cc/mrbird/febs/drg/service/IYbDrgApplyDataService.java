package cc.mrbird.febs.drg.service;

import cc.mrbird.febs.drg.entity.YbDrgApply;
import cc.mrbird.febs.drg.entity.YbDrgApplyData;
import cc.mrbird.febs.drg.entity.YbDrgVerifyView;
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
public interface IYbDrgApplyDataService extends IService<YbDrgApplyData> {

    IPage<YbDrgApplyData> findYbDrgApplyDatas(QueryRequest request, YbDrgApplyData ybDrgApplyData);

    IPage<YbDrgApplyData> findYbDrgApplyDataList(QueryRequest request, YbDrgApplyData ybDrgApplyData);

    void createYbDrgApplyData(YbDrgApplyData ybDrgApplyData);

    void updateYbDrgApplyData(YbDrgApplyData ybDrgApplyData);

    void deleteYbDrgApplyDatas(String[] Ids);

    void importDrgApply(YbDrgApply ybDrgApply, List<YbDrgApplyData> listData);

    int deleteDrgApplyDataByPid(YbDrgApplyData ybDrgApplyData);

    List<YbDrgApplyData> getApplyDatas(YbDrgApply ybDrgApply, YbDrgVerifyView ybDrgVerifyView);

    List<YbDrgApplyData> findDrgApplyDataByNotVerifys(String pid, String applyDateStr, Integer areaType);

    List<YbDrgApplyData> getApplyDataByKeyFieldList(String pid, String keyField, String value);

    void findDrgJk (String applyDateStr, Integer areaType);

    String getDrgJk (String applyDateStr, Integer areaType);

    String delDrgJk(String applyDateStr, Integer areaType);
}
