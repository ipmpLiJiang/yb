package cc.mrbird.febs.yb.dao;

import cc.mrbird.febs.yb.entity.YbReconsiderPriorityLevel;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.Param;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author viki
 * @since 2020-10-13
 */
public interface YbReconsiderPriorityLevelMapper extends BaseMapper<YbReconsiderPriorityLevel> {
        void updateYbReconsiderPriorityLevel(YbReconsiderPriorityLevel ybReconsiderPriorityLevel);
        IPage<YbReconsiderPriorityLevel> findYbReconsiderPriorityLevel(Page page, @Param("ybReconsiderPriorityLevel") YbReconsiderPriorityLevel ybReconsiderPriorityLevel);
        }
