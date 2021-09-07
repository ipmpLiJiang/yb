package cc.mrbird.febs.yb.dao;

import cc.mrbird.febs.yb.entity.YbAppealResultResetView;
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
 * @since 2020-08-12
 */
public interface YbAppealResultResetViewMapper extends BaseMapper<YbAppealResultResetView> {
        IPage<YbAppealResultResetView> findAppealResultResetView(Page page, @Param("ybAppealResultResetView") YbAppealResultResetView appealResultResetView,@Param("keyField") String keyField);

        int findAppealResultResetCount( @Param("ybAppealResultResetView") YbAppealResultResetView appealResultResetView,@Param("keyField") String keyField);

        List<YbAppealResultResetView> findAppealResultResetList(@Param("ybAppealResultResetView") YbAppealResultResetView appealResultResetView);
}
