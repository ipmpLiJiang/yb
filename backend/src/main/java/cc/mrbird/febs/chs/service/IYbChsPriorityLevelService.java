package cc.mrbird.febs.chs.service;

import cc.mrbird.febs.chs.entity.YbChsPriorityLevel;
import com.baomidou.mybatisplus.extension.service.IService;
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
 * @since 2022-07-13
 */
public interface IYbChsPriorityLevelService extends IService<YbChsPriorityLevel> {

        IPage<YbChsPriorityLevel> findYbChsPriorityLevels(QueryRequest request, YbChsPriorityLevel ybChsPriorityLevel);

        IPage<YbChsPriorityLevel> findYbChsPriorityLevelList(QueryRequest request, YbChsPriorityLevel ybChsPriorityLevel);

        void createYbChsPriorityLevel(YbChsPriorityLevel ybChsPriorityLevel);

        void updateYbChsPriorityLevel(YbChsPriorityLevel ybChsPriorityLevel);

        void deleteYbChsPriorityLevels(String[]Ids);
        }
