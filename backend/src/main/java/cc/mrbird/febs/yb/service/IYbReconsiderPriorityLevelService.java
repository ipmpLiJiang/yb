package cc.mrbird.febs.yb.service;

import cc.mrbird.febs.yb.entity.YbReconsiderPriorityLevel;
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
 * @since 2020-10-13
 */
public interface IYbReconsiderPriorityLevelService extends IService<YbReconsiderPriorityLevel> {

        IPage<YbReconsiderPriorityLevel> findYbReconsiderPriorityLevels(QueryRequest request, YbReconsiderPriorityLevel ybReconsiderPriorityLevel);

        IPage<YbReconsiderPriorityLevel> findYbReconsiderPriorityLevelList(QueryRequest request, YbReconsiderPriorityLevel ybReconsiderPriorityLevel);

        void createYbReconsiderPriorityLevel(YbReconsiderPriorityLevel ybReconsiderPriorityLevel);

        void updateYbReconsiderPriorityLevel(YbReconsiderPriorityLevel ybReconsiderPriorityLevel);

        void deleteYbReconsiderPriorityLevels(String[]Ids);

        List<YbReconsiderPriorityLevel> findReconsiderPriorityLevelList(int areaType);
        }
