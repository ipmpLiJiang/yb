package cc.mrbird.febs.yb.dao;

import cc.mrbird.febs.yb.entity.YbAppealManageView;
import cc.mrbird.febs.yb.entity.YbReconsiderVerify;
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
public interface YbAppealManageViewMapper extends BaseMapper<YbAppealManageView> {
    void updateYbAppealManageView(YbAppealManageView ybAppealManageView);

    IPage<YbAppealManageView> findYbAppealManageView(Page page, @Param("ybAppealManageView") YbAppealManageView ybAppealManageView);

    IPage<YbAppealManageView> findAppealManageView(Page page, @Param("ybAppealManageView") YbAppealManageView ybAppealManageView,@Param("keyField") String keyField,@Param("appealConfireId") String appealConfireId);

    int findAppealManageCount(@Param("ybAppealManageView") YbAppealManageView ybAppealManageView,@Param("keyField") String keyField, @Param("appealConfireId") String appealConfireId);

    IPage<YbAppealManageView> findAppealManageLikeView(Page page, @Param("ybAppealManageView") YbAppealManageView ybAppealManageView,@Param("keyField") String keyField,@Param("appealConfireId") String appealConfireId);

    int findAppealManageLikeCount(@Param("ybAppealManageView") YbAppealManageView ybAppealManageView,@Param("keyField") String keyField,@Param("appealConfireId") String appealConfireId);

    List<YbAppealManageView> findAppealManageList(@Param("ybAppealManageView") YbAppealManageView ybAppealManageView);
}
