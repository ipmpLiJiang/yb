package cc.mrbird.febs.drg.dao;

import cc.mrbird.febs.drg.entity.YbDrgJk;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * <p>
 * Mapper 接口
 * </p>
 *
 * @author viki
 * @since 2021-11-23
 */
public interface YbDrgJkMapper extends BaseMapper<YbDrgJk> {
    void updateYbDrgJk(YbDrgJk ybDrgJk);

    IPage<YbDrgJk> findYbDrgJk(Page page, @Param("ybDrgJk") YbDrgJk ybDrgJk);

      List<YbDrgJk> findDrgJkApplyDataByPid(String pid);

      int delDrgJkByPid(String pid);
}
