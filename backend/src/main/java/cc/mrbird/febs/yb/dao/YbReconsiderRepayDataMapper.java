package cc.mrbird.febs.yb.dao;

import cc.mrbird.febs.yb.entity.YbReconsiderRepayData;
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
 * @since 2020-09-07
 */
public interface YbReconsiderRepayDataMapper extends BaseMapper<YbReconsiderRepayData> {
    void updateYbReconsiderRepayData(YbReconsiderRepayData ybReconsiderRepayData);

    IPage<YbReconsiderRepayData> findYbReconsiderRepayData(Page page, @Param("ybReconsiderRepayData") YbReconsiderRepayData ybReconsiderRepayData);

    List<String> findGroupBelongDateStr(@Param("pid") String pid);
}
