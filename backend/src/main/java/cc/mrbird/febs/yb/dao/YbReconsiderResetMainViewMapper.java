package cc.mrbird.febs.yb.dao;

import cc.mrbird.febs.yb.entity.YbReconsiderResetMainView;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.Param;

/**
 * <p>
 * VIEW Mapper 接口
 * </p>
 *
 * @author viki
 * @since 2020-08-18
 */
public interface YbReconsiderResetMainViewMapper extends BaseMapper<YbReconsiderResetMainView> {
        void updateYbReconsiderResetMainView(YbReconsiderResetMainView ybReconsiderResetMainView);
        IPage<YbReconsiderResetMainView> findYbReconsiderResetMainView(Page page, @Param("ybReconsiderResetMainView") YbReconsiderResetMainView ybReconsiderResetMainView);
        }
