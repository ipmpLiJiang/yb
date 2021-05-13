package cc.mrbird.febs.yb.dao;

import cc.mrbird.febs.yb.entity.YbReconsiderVerify;
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
 * @since 2020-07-30
 */
public interface YbReconsiderVerifyMapper extends BaseMapper<YbReconsiderVerify> {
    void updateYbReconsiderVerify(YbReconsiderVerify ybReconsiderVerify);

    IPage<YbReconsiderVerify> findYbReconsiderVerify(Page page, @Param("ybReconsiderVerify") YbReconsiderVerify ybReconsiderVerify);

    void insertReconsiderVerifyImport(@Param("applyDate") String applyDate, @Param("matchPersonId") long matchPersonId, @Param("matchPersonName") String matchPersonName);

    void insertMainReconsiderVerifyImport(@Param("applyDate") String applyDate, @Param("matchPersonId") long matchPersonId, @Param("matchPersonName") String matchPersonName);


    List<YbReconsiderVerify> findReconsiderVerifyList(@Param("applyDateStr") String applyDateStr,@Param("areaType") Integer areaType,@Param("dataType") Integer dataType,@Param("state") Integer state,@Param("typeno") Integer typeno);

    int findReconsiderVerifyResetCheckCount(@Param("applyDateStr") String applyDateStr,@Param("areaType") Integer areaType);

    int findReconsiderVerifyApplyDataCheckCount(@Param("pid") String pid,@Param("applyDateStr") String applyDateStr,@Param("areaType") Integer areaType,@Param("typeno") Integer typeno);
}
