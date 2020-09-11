package cc.mrbird.febs.yb.dao;

import cc.mrbird.febs.yb.entity.YbReconsiderResetMain;
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
 * @since 2020-08-17
 */
public interface YbReconsiderResetMainMapper extends BaseMapper<YbReconsiderResetMain> {
        void updateYbReconsiderResetMain(YbReconsiderResetMain ybReconsiderResetMain);
        IPage<YbReconsiderResetMain> findYbReconsiderResetMain(Page page, @Param("ybReconsiderResetMain") YbReconsiderResetMain ybReconsiderResetMain);
        }
