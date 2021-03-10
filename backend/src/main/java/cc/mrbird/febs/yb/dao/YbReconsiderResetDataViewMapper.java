package cc.mrbird.febs.yb.dao;

import cc.mrbird.febs.yb.entity.YbReconsiderResetDataView;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.Param;

import java.util.List;

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

    IPage<YbReconsiderResetDataView> findReconsiderResetDataView(Page page, @Param("ybReconsiderResetDataView") YbReconsiderResetDataView ybReconsiderResetDataView);

    int findReconsiderResetDataCount(@Param("ybReconsiderResetDataView") YbReconsiderResetDataView ybReconsiderResetDataView);

    IPage<YbReconsiderResetDataView> findReconsiderResetDataNotView(Page page, @Param("ybReconsiderResetDataView") YbReconsiderResetDataView ybReconsiderResetDataView);

    int findReconsiderResetDataNotCount(@Param("ybReconsiderResetDataView") YbReconsiderResetDataView ybReconsiderResetDataView);

    List<YbReconsiderResetDataView> findReconsiderResetDataList(@Param("ybReconsiderResetDataView") YbReconsiderResetDataView ybReconsiderResetDataView);
}
