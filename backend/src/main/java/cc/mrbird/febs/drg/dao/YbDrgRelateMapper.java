package cc.mrbird.febs.drg.dao;

import cc.mrbird.febs.drg.entity.YbDrgRelate;
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
 * @since 2022-03-16
 */
public interface YbDrgRelateMapper extends BaseMapper<YbDrgRelate> {
        void updateYbDrgRelate(YbDrgRelate ybDrgRelate);
        IPage<YbDrgRelate> findYbDrgRelate(Page page, @Param("ybDrgRelate") YbDrgRelate ybDrgRelate);
        }
