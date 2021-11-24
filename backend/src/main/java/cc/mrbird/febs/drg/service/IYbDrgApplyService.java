package cc.mrbird.febs.drg.service;

import cc.mrbird.febs.drg.entity.YbDrgApply;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.Date;
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
public interface IYbDrgApplyService extends IService<YbDrgApply> {

    IPage<YbDrgApply> findYbDrgApplys(QueryRequest request, YbDrgApply ybDrgApply);

    IPage<YbDrgApply> findYbDrgApplyList(QueryRequest request, YbDrgApply ybDrgApply);

    void createYbDrgApply(YbDrgApply ybDrgApply);

    void updateYbDrgApply(YbDrgApply ybDrgApply);

    void deleteYbDrgApplys(String[] Ids,int state);

    List<YbDrgApply> findDrgApplyList(YbDrgApply ybDrgApply);

    YbDrgApply findDrgApplyByApplyDateStrs(String appltDateStr, Integer areaType);

    void updateDrgApplyState3(YbDrgApply drgApply);

    String getSendMessage(String applyDateStr, Date enableDate, Integer areaType, boolean isChange);

}
