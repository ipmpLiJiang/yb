package cc.mrbird.febs.yb.service;

import cc.mrbird.febs.common.domain.QueryRequest;
import cc.mrbird.febs.yb.entity.YbDrgApply;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;

import java.text.ParseException;
import java.util.Date;
import java.util.List;

/**
 * <p>
 * 服务类
 * </p>
 *
 * @author viki
 * @since 2020-07-23
 */
public interface IYbDrgApplyService extends IService<YbDrgApply> {

    IPage<YbDrgApply> findYbDrgApplys(QueryRequest request, YbDrgApply ybDrgApply);

    IPage<YbDrgApply> findYbDrgApplyList(QueryRequest request, YbDrgApply ybDrgApply);

    List<YbDrgApply> findDrgApplyList(YbDrgApply ybDrgApply);

    YbDrgApply findDrgApplyByApplyDateStrs(String appltDateStr, Integer areaType);

    boolean updateYbDrgApplyState(String applyDateStr, Integer areaType, Integer state);

    String createDrgApplyCheck(YbDrgApply ybDrgApply);

    void createYbDrgApply(YbDrgApply ybDrgApply);

    void updateYbDrgApply(YbDrgApply ybDrgApply);

    void deleteYbDrgApplys(String[] Ids);

    String getSendMessage(String applyDateStr, Date enableDate,Integer areaType, int typeno,boolean isChange);

    String getSendMessage(String applyDateStr,Integer areaType);

    String getSendMessage(String applyDateStr, Date endDate, Integer typeno, Integer areaType);

    String createJobState(String applyDateStr,Integer areaType);

}
