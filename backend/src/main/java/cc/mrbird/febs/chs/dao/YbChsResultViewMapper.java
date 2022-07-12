package cc.mrbird.febs.chs.dao;

import cc.mrbird.febs.chs.entity.YbChsResultView;
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
 * @since 2020-07-30
 */
public interface YbChsResultViewMapper extends BaseMapper<YbChsResultView> {

    IPage<YbChsResultView> findChsResultView(Page page, @Param("ybChsResultView") YbChsResultView ybChsResultView);

    int findChsResultCount(@Param("ybChsResultView") YbChsResultView ybChsResultView);

    List<YbChsResultView> findChsResultLeftDetailViewList(@Param("ybChsResultView") YbChsResultView ybChsResultView);

    List<YbChsResultView> findChsResultViewList(@Param("ybChsResultView") YbChsResultView ybChsResultView);
}
