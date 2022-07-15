package cc.mrbird.febs.chs.dao;

import cc.mrbird.febs.chs.entity.YbChsPriorityLevel;
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
 * @since 2022-07-13
 */
public interface YbChsPriorityLevelMapper extends BaseMapper<YbChsPriorityLevel> {
        void updateYbChsPriorityLevel(YbChsPriorityLevel ybChsPriorityLevel);
        IPage<YbChsPriorityLevel> findYbChsPriorityLevel(Page page, @Param("ybChsPriorityLevel") YbChsPriorityLevel ybChsPriorityLevel);
        }
