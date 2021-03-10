package cc.mrbird.febs.com.dao;

import cc.mrbird.febs.com.entity.ComType;
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
 * @since 2021-03-05
 */
public interface ComTypeMapper extends BaseMapper<ComType> {
        void updateComType(ComType comType);
        IPage<ComType> findComType(Page page, @Param("comType") ComType comType);
        }
