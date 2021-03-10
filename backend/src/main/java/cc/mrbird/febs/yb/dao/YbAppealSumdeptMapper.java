package cc.mrbird.febs.yb.dao;

import cc.mrbird.febs.yb.entity.YbAppealSumdept;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.Param;

/**
 * <p>
 * Mapper 接口
 * </p>
 *
 * @author viki
 * @since 2021-03-08
 */
public interface YbAppealSumdeptMapper extends BaseMapper<YbAppealSumdept> {
    void updateYbAppealSumdept(YbAppealSumdept ybAppealSumdept);

    IPage<YbAppealSumdept> findYbAppealSumdept(Page page, @Param("ybAppealSumdept") YbAppealSumdept ybAppealSumdept);

        IPage<YbAppealSumdept> findAppealSumdeptView(Page page, @Param("currencyField") String currencyField);
}
