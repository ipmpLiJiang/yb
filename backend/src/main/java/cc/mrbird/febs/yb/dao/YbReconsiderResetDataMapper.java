package cc.mrbird.febs.yb.dao;

import cc.mrbird.febs.yb.entity.YbReconsiderApplyData;
import cc.mrbird.febs.yb.entity.YbReconsiderResetData;
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
 * @since 2020-08-17
 */
public interface YbReconsiderResetDataMapper extends BaseMapper<YbReconsiderResetData> {
    void updateYbReconsiderResetData(YbReconsiderResetData ybReconsiderResetData);

    IPage<YbReconsiderResetData> findYbReconsiderResetData(Page page, @Param("ybReconsiderResetData") YbReconsiderResetData ybReconsiderResetData);

    List<YbReconsiderResetData> findReconsiderResetByApplyDate(@Param("applyDateStr") String applyDateStr, @Param("dataType") Integer dataType);

    List<YbReconsiderResetData> findResetNotExistsRepayByApplyDate(@Param("applyDateStr") String applyDateStr, @Param("dataType") Integer dataType);
}
