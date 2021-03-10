package cc.mrbird.febs.yb.dao;

import cc.mrbird.febs.yb.entity.YbNoticeData;
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
public interface YbNoticeDataMapper extends BaseMapper<YbNoticeData> {
        void updateYbNoticeData(YbNoticeData ybNoticeData);
        IPage<YbNoticeData> findYbNoticeData(Page page, @Param("ybNoticeData") YbNoticeData ybNoticeData);
        }
