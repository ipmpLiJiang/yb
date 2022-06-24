package cc.mrbird.febs.chs.dao;

import cc.mrbird.febs.chs.entity.YbDks;
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
 * @since 2022-06-24
 */
public interface YbDksMapper extends BaseMapper<YbDks> {
        void updateYbDks(YbDks ybDks);
        IPage<YbDks> findYbDks(Page page, @Param("ybDks") YbDks ybDks);
        }
