package cc.mrbird.febs.yb.dao;

import cc.mrbird.febs.yb.entity.YbReconsiderReset;
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
 * @since 2020-08-17
 */
public interface YbReconsiderResetMapper extends BaseMapper<YbReconsiderReset> {
        void updateYbReconsiderReset(YbReconsiderReset ybReconsiderReset);
        IPage<YbReconsiderReset> findYbReconsiderReset(Page page, @Param("ybReconsiderReset") YbReconsiderReset ybReconsiderReset);
        }
