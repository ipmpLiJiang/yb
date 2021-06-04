package cc.mrbird.febs.yb.dao;

import cc.mrbird.febs.yb.entity.YbReconsiderVerifyView;
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
public interface YbReconsiderVerifyViewMapper extends BaseMapper<YbReconsiderVerifyView> {
    void updateYbReconsiderVerifyView(YbReconsiderVerifyView ybReconsiderVerifyView);

    IPage<YbReconsiderVerifyView> findYbReconsiderVerifyView(Page page, @Param("ybReconsiderVerifyView") YbReconsiderVerifyView ybReconsiderVerifyView);

    int findReconsiderVerifyApplyDateCount(@Param("ybReconsiderVerifyView") YbReconsiderVerifyView ybReconsiderVerifyView);

    IPage<YbReconsiderVerifyView> findReconsiderVerifyViewNull(Page page, @Param("ybReconsiderVerifyView") YbReconsiderVerifyView ybReconsiderVerifyView, @Param("searchType") String[] searchType);

    int findReconsiderVerifyCountNull(@Param("ybReconsiderVerifyView") YbReconsiderVerifyView ybReconsiderVerifyView, @Param("searchType") String[] searchType);

//    IPage<YbReconsiderVerifyView> findReconsiderVerifyView(Page page, @Param("ybReconsiderVerifyView") YbReconsiderVerifyView ybReconsiderVerifyView, @Param("searchType") String[] searchType);

//    int findReconsiderVerifyCount(@Param("ybReconsiderVerifyView") YbReconsiderVerifyView ybReconsiderVerifyView, @Param("searchType") String[] searchType);

    IPage<YbReconsiderVerifyView> findReconsiderVerifyView(Page page, @Param("ybReconsiderVerifyView") YbReconsiderVerifyView ybReconsiderVerifyView);

    int findReconsiderVerifyCount(@Param("ybReconsiderVerifyView") YbReconsiderVerifyView ybReconsiderVerifyView);


    List<YbReconsiderVerifyView> findReconsiderVerifyViewList(@Param("ybReconsiderVerifyView") YbReconsiderVerifyView ybReconsiderVerifyView);



}
