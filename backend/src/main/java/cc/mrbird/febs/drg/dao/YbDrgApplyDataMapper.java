package cc.mrbird.febs.drg.dao;

import cc.mrbird.febs.drg.entity.YbDrgApplyData;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * <p>
 * Mapper 接口
 * </p>
 *
 * @author viki
 * @since 2021-11-23
 */
public interface YbDrgApplyDataMapper extends BaseMapper<YbDrgApplyData> {
    void updateYbDrgApplyData(YbDrgApplyData ybDrgApplyData);

    IPage<YbDrgApplyData> findYbDrgApplyData(Page page, @Param("ybDrgApplyData") YbDrgApplyData ybDrgApplyData);

    List<YbDrgApplyData> findDrgApplyDataByNotVerify(@Param("pid") String pid, @Param("applyDateStr") String applyDateStr, @Param("areaType") Integer areaType);

}
