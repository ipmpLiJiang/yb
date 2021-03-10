package cc.mrbird.febs.yb.dao;

import cc.mrbird.febs.yb.entity.YbNotice;
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
 * @since 2021-03-08
 */
public interface YbNoticeMapper extends BaseMapper<YbNotice> {
        void updateYbNotice(YbNotice ybNotice);
        IPage<YbNotice> findYbNotice(Page page, @Param("ybNotice") YbNotice ybNotice);
        }
