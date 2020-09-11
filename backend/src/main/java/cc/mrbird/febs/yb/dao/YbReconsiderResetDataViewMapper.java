package cc.mrbird.febs.yb.dao;

import cc.mrbird.febs.yb.entity.YbReconsiderResetDataView;
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
public interface YbReconsiderResetDataViewMapper extends BaseMapper<YbReconsiderResetDataView> {
        void updateYbReconsiderResetDataView(YbReconsiderResetDataView ybReconsiderResetDataView);
        IPage<YbReconsiderResetDataView> findYbReconsiderResetDataView(Page page, @Param("ybReconsiderResetDataView") YbReconsiderResetDataView ybReconsiderResetDataView);
        }
