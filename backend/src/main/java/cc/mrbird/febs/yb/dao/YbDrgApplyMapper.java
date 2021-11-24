package cc.mrbird.febs.yb.dao;

import cc.mrbird.febs.yb.entity.YbDrgApply;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * <p>
 * Mapper 接口
 * </p>
 *
 * @author viki
 * @since 2020-07-23
 */
public interface YbDrgApplyMapper extends BaseMapper<YbDrgApply> {
    void updateYbDrgApply(YbDrgApply ybDrgApply);

    IPage<YbDrgApply> findYbDrgApply(Page page, @Param("ybDrgApply") YbDrgApply ybDrgApply);

    YbDrgApply findDrgApplyByApplyDateStr(@Param("applyDateStr") String applyDateStr,@Param("areaType") Integer areaType);

}
