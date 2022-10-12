package cc.mrbird.febs.chs.dao;

import cc.mrbird.febs.chs.entity.YbChsProjectSet;
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
 * @since 2022-10-12
 */
public interface YbChsProjectSetMapper extends BaseMapper<YbChsProjectSet> {
        void updateYbChsProjectSet(YbChsProjectSet ybChsProjectSet);
        IPage<YbChsProjectSet> findYbChsProjectSet(Page page, @Param("ybChsProjectSet") YbChsProjectSet ybChsProjectSet);
        }
