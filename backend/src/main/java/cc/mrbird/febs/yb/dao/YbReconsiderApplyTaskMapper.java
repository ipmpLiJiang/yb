package cc.mrbird.febs.yb.dao;

import cc.mrbird.febs.yb.entity.YbReconsiderApplyTask;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.Param;

/**
 * <p>
 * Mapper 接口
 * </p>
 *
 * @author viki
 * @since 2020-11-23
 */
public interface YbReconsiderApplyTaskMapper extends BaseMapper<YbReconsiderApplyTask> {
    void updateYbReconsiderApplyTask(YbReconsiderApplyTask ybReconsiderApplyTask);

    IPage<YbReconsiderApplyTask> findYbReconsiderApplyTask(Page page, @Param("ybReconsiderApplyTask") YbReconsiderApplyTask ybReconsiderApplyTask);

    YbReconsiderApplyTask findReconsiderApplyTask(@Param("ybReconsiderApplyTask") YbReconsiderApplyTask ybReconsiderApplyTask);
}
