package cc.mrbird.febs.yb.dao;

import cc.mrbird.febs.yb.entity.YbAppealResultView;
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
public interface YbAppealResultViewMapper extends BaseMapper<YbAppealResultView> {
        void updateYbAppealResultView(YbAppealResultView ybAppealResultView);
        IPage<YbAppealResultView> findYbAppealResultView(Page page, @Param("ybAppealResultView") YbAppealResultView ybAppealResultView);

        List<YbAppealResultView> findAppealResultList(@Param("ybAppealResultView") YbAppealResultView ybAppealResultView);
}
