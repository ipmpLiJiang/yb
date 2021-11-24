package cc.mrbird.febs.drg.dao;

import cc.mrbird.febs.drg.entity.YbDrgResult;
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
 * @since 2021-11-23
 */
public interface YbDrgResultMapper extends BaseMapper<YbDrgResult> {
        void updateYbDrgResult(YbDrgResult ybDrgResult);
        IPage<YbDrgResult> findYbDrgResult(Page page, @Param("ybDrgResult") YbDrgResult ybDrgResult);
        }
