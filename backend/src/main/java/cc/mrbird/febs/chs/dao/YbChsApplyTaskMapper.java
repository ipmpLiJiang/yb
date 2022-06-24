package cc.mrbird.febs.chs.dao;

import cc.mrbird.febs.chs.entity.YbChsApplyTask;
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
 * @since 2022-06-24
 */
public interface YbChsApplyTaskMapper extends BaseMapper<YbChsApplyTask> {
    void updateYbChsApplyTask(YbChsApplyTask ybChsApplyTask);

    IPage<YbChsApplyTask> findYbChsApplyTask(Page page, @Param("ybChsApplyTask") YbChsApplyTask ybChsApplyTask);

    YbChsApplyTask findChsApplyTask(@Param("ybChsApplyTask") YbChsApplyTask ybChsApplyTask);
}
