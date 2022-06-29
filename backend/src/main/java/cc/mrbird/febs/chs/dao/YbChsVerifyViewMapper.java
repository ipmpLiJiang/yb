package cc.mrbird.febs.chs.dao;

import cc.mrbird.febs.chs.entity.YbChsVerifyView;
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
public interface YbChsVerifyViewMapper extends BaseMapper<YbChsVerifyView> {

    int findChsVerifyApplyDateCount(@Param("ybChsVerifyView") YbChsVerifyView ybChsVerifyView);

    IPage<YbChsVerifyView> findChsVerifyView(Page page, @Param("ybChsVerifyView") YbChsVerifyView ybChsVerifyView);

    int findChsVerifyCount(@Param("ybChsVerifyView") YbChsVerifyView ybChsVerifyView);

    List<YbChsVerifyView> findChsVerifyViewList(@Param("ybChsVerifyView") YbChsVerifyView ybChsVerifyView);



}
