package cc.mrbird.febs.yb.dao;

import cc.mrbird.febs.yb.entity.YbReconsiderApply;
import cc.mrbird.febs.yb.entity.YbReconsiderApplyData;
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
 * @since 2020-07-17
 */
public interface YbReconsiderApplyDataMapper extends BaseMapper<YbReconsiderApplyData> {
    void updateYbReconsiderApplyData(YbReconsiderApplyData ybReconsiderApplyData);

    IPage<YbReconsiderApplyData> findYbReconsiderApplyData(Page page, @Param("ybReconsiderApplyData") YbReconsiderApplyData ybReconsiderApplyData);

    void createBatchData(@Param("listReconsiderApplyData") List<YbReconsiderApplyData> listReconsiderApplyData);

    List<YbReconsiderApplyData> findReconsiderApplyDataByNotVerify(@Param("pid") String pid,@Param("applyDateStr") String applyDateStr,@Param("areaType") Integer areaType,@Param("dataType") Integer dataType, @Param("typeno") Integer typeno);

    List<YbReconsiderApplyData> findReconsiderApplyDataBetween(@Param("pid") String pid, @Param("dataType") Integer dataType, @Param("typeno") Integer typeno, @Param("startNum") Integer startNum, @Param("endNum") Integer endNum);

    List<YbReconsiderApplyData> findReconsiderApplyDataNotBetween(@Param("pid") String pid, @Param("dataType") Integer dataType, @Param("typeno") Integer typeno, @Param("startNum") Integer startNum, @Param("endNum") Integer endNum);

    List<YbReconsiderApplyData> findReconsiderApplyDataNotInpatientfees(@Param("pid") String pid,@Param("applyDateStr") String applyDateStr,@Param("areaType") Integer areaType, @Param("dataType") Integer dataType, @Param("typeno") Integer typeno);

    int findReconsiderApplyDataCount(@Param("pid") String pid, @Param("dataType") Integer dataType, @Param("typeno") Integer typeno);

    int findReconsiderApplyDataNotCount(@Param("pid") String pid,@Param("applyDateStr") String applyDateStr,@Param("areaType") Integer areaType, @Param("dataType") Integer dataType, @Param("typeno") Integer typeno);
}
