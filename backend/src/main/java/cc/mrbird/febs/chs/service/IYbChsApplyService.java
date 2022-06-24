package cc.mrbird.febs.chs.service;

import cc.mrbird.febs.chs.entity.YbChsApply;
import com.baomidou.mybatisplus.extension.service.IService;

import java.text.ParseException;
import java.util.List;

import com.baomidou.mybatisplus.core.metadata.IPage;
import cc.mrbird.febs.common.domain.QueryRequest;
import com.baomidou.mybatisplus.core.metadata.IPage;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author viki
 * @since 2022-06-20
 */
public interface IYbChsApplyService extends IService<YbChsApply> {

        IPage<YbChsApply> findYbChsApplys(QueryRequest request, YbChsApply ybChsApply);

        IPage<YbChsApply> findYbChsApplyList(QueryRequest request, YbChsApply ybChsApply);

        YbChsApply findChsApplyByApplyDateStrs(String appltDateStr, Integer areaType);

        void createYbChsApply(YbChsApply ybChsApply);

        void updateYbChsApply(YbChsApply ybChsApply);

        void deleteYbChsApplys(String[]Ids);

        String createChsApplyCheck(YbChsApply ybChsApply);

        String updateYbChsApply(YbChsApply ybChsApply, boolean isUpOverdue) throws ParseException;

        void updateYbChsApply(YbChsApply ybChsApply, Integer isChangDate);

        void deleteYbChsApplys(String[] Ids, int state);

        List<YbChsApply> findChsApplyList(YbChsApply ybChsApply);
        }
