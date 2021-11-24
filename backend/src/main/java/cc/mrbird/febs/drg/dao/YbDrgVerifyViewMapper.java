package cc.mrbird.febs.drg.dao;

import cc.mrbird.febs.drg.entity.YbDrgVerifyView;
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
public interface YbDrgVerifyViewMapper extends BaseMapper<YbDrgVerifyView> {

    int findDrgVerifyApplyDateCount(@Param("ybDrgVerifyView") YbDrgVerifyView ybDrgVerifyView);

    IPage<YbDrgVerifyView> findDrgVerifyView(Page page, @Param("ybDrgVerifyView") YbDrgVerifyView ybDrgVerifyView);

    int findDrgVerifyCount(@Param("ybDrgVerifyView") YbDrgVerifyView ybDrgVerifyView);

    List<YbDrgVerifyView> findDrgVerifyViewList(@Param("ybDrgVerifyView") YbDrgVerifyView ybDrgVerifyView);



}
