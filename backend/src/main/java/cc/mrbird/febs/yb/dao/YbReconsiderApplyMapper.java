package cc.mrbird.febs.yb.dao;

import cc.mrbird.febs.yb.entity.YbReconsiderApply;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author viki
 * @since 2020-07-23
 */
public interface YbReconsiderApplyMapper extends BaseMapper<YbReconsiderApply> {
        void updateYbReconsiderApply(YbReconsiderApply ybReconsiderApply);
        IPage<YbReconsiderApply> findYbReconsiderApply(Page page, @Param("ybReconsiderApply") YbReconsiderApply ybReconsiderApply);
        void deleteBatchStateIdsYbReconsiderApply(@Param("listString") List<String> listString, @Param("state") Integer state);
}
