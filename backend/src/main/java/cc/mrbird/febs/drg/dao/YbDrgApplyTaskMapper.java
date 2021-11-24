package cc.mrbird.febs.drg.dao;

import cc.mrbird.febs.drg.entity.YbDrgApplyTask;
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
public interface YbDrgApplyTaskMapper extends BaseMapper<YbDrgApplyTask> {
        void updateYbDrgApplyTask(YbDrgApplyTask ybDrgApplyTask);
        IPage<YbDrgApplyTask> findYbDrgApplyTask(Page page, @Param("ybDrgApplyTask") YbDrgApplyTask ybDrgApplyTask);
        }
