package cc.mrbird.febs.chs.dao;

import cc.mrbird.febs.chs.entity.YbChsApplyData;
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
 * @since 2022-06-21
 */
public interface YbChsApplyDataMapper extends BaseMapper<YbChsApplyData> {
    void updateYbChsApplyData(YbChsApplyData ybChsApplyData);

    IPage<YbChsApplyData> findYbChsApplyData(Page page, @Param("ybChsApplyData") YbChsApplyData ybChsApplyData);

    int findChsApplyDataCount(@Param("pid") String pid,@Param("dataType")  Integer dataType,@Param("isOutpfees")  Integer isOutpfees);

    int findChsApplyDataNotCount(@Param("pid") String pid,@Param("applyDateStr") String applyDateStr,
                                 @Param("areaType") Integer areaType,@Param("dataType")  Integer dataType,@Param("isOutpfees")  Integer isOutpfees);

    List<YbChsApplyData> findChsApplyDataNotJk(@Param("pid") String pid,@Param("applyDateStr") String applyDateStr,
                                               @Param("areaType") Integer areaType,@Param("dataType")  Integer dataType,@Param("isOutpfees")  Integer isOutpfees);

    List<YbChsApplyData> findChsApplyDataBetween(@Param("pid") String pid, @Param("startNum") Integer startNum, @Param("endNum") Integer endNum,
                                                 @Param("state") Integer state,@Param("dataType")  Integer dataType, @Param("isOutpfees") Integer isOutpfees);

    List<YbChsApplyData> findChsApplyDataByNotVerify(@Param("pid") String pid,@Param("applyDateStr") String applyDateStr,
                                                     @Param("areaType") Integer areaType);
}
