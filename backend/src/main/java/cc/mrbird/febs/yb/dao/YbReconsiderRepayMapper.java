package cc.mrbird.febs.yb.dao;

import cc.mrbird.febs.yb.entity.YbReconsiderRepay;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.Param;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author viki
 * @since 2020-09-07
 */
public interface YbReconsiderRepayMapper extends BaseMapper<YbReconsiderRepay> {
        void updateYbReconsiderRepay(YbReconsiderRepay ybReconsiderRepay);
        IPage<YbReconsiderRepay> findYbReconsiderRepay(Page page, @Param("ybReconsiderRepay") YbReconsiderRepay ybReconsiderRepay);
        }
