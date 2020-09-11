package cc.mrbird.febs.yb.dao;

import cc.mrbird.febs.yb.entity.YbReconsiderResetResultView;
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
 * @since 2020-09-10
 */
public interface YbReconsiderResetResultViewMapper extends BaseMapper<YbReconsiderResetResultView> {
        void updateYbReconsiderResetResultView(YbReconsiderResetResultView ybReconsiderResetResultView);
        IPage<YbReconsiderResetResultView> findYbReconsiderResetResultView(Page page, @Param("ybReconsiderResetResultView") YbReconsiderResetResultView ybReconsiderResetResultView);
        }
