package cc.mrbird.febs.yb.dao;

import cc.mrbird.febs.yb.entity.YbDrgApplyData;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * <p>
 * Mapper 接口
 * </p>
 *
 * @author viki
 * @since 2020-07-17
 */
public interface YbDrgApplyDataMapper extends BaseMapper<YbDrgApplyData> {
    void updateYbDrgApplyData(YbDrgApplyData ybDrgApplyData);

    IPage<YbDrgApplyData> findYbDrgApplyData(Page page, @Param("ybDrgApplyData") YbDrgApplyData ybDrgApplyData);

    List<YbDrgApplyData> findDrgApplyDataByNotVerify(@Param("pid") String pid, @Param("applyDateStr") String applyDateStr, @Param("areaType") Integer areaType);

    List<YbDrgApplyData> findDrgApplyDataByNotVerifyState(@Param("pid") String pid, @Param("applyDateStr") String applyDateStr, @Param("areaType") Integer areaType);

    @Select("select id from yb_reconsider_apply_data where pid = #{pid}")
    List<String> findDataId(@Param("pid") String pid);
}
