package cc.mrbird.febs.yb.dao;

import cc.mrbird.febs.yb.entity.YbReconsiderInpatientfees;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.Param;

/**
 * <p>
 *  Mapper 接口
 * </p>findYbReconsiderInpatientfees
 *
 * @author viki
 * @since 2020-07-22
 */
public interface YbReconsiderInpatientfeesMapper extends BaseMapper<YbReconsiderInpatientfees> {
        void updateYbReconsiderInpatientfees(YbReconsiderInpatientfees ybReconsiderInpatientfees);
        IPage<YbReconsiderInpatientfees> findYbReconsiderInpatientfees(Page page, @Param("ybReconsiderInpatientfees") YbReconsiderInpatientfees ybReconsiderInpatientfees);
        IPage<YbReconsiderInpatientfees> findYbReconsiderInpatientfeesEq(Page page, @Param("ybReconsiderInpatientfees") YbReconsiderInpatientfees ybReconsiderInpatientfees);
}
