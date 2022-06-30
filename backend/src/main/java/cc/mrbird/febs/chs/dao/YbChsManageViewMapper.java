package cc.mrbird.febs.chs.dao;

import cc.mrbird.febs.chs.entity.YbChsManageView;
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
public interface YbChsManageViewMapper extends BaseMapper<YbChsManageView> {

    IPage<YbChsManageView> findChsManageView(Page page, @Param("ybChsManageView") YbChsManageView ybChsManageView);

    int findChsManageCount(@Param("ybChsManageView") YbChsManageView ybChsManageView);

    List<YbChsManageView> findChsManageList(@Param("ybChsManageView") YbChsManageView ybChsManageView);
}
