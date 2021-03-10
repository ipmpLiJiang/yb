package cc.mrbird.febs.yb.dao;

import cc.mrbird.febs.yb.entity.YbHandleVerifyDataView;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.Param;

/**
 * <p>
 * VIEW Mapper 接口
 * </p>
 *
 * @author viki
 * @since 2020-08-28
 */
public interface YbHandleVerifyDataViewMapper extends BaseMapper<YbHandleVerifyDataView> {
        void updateYbHandleVerifyDataView(YbHandleVerifyDataView ybHandleVerifyDataView);
        IPage<YbHandleVerifyDataView> findYbHandleVerifyDataView(Page page, @Param("ybHandleVerifyDataView") YbHandleVerifyDataView ybHandleVerifyDataView);

        IPage<YbHandleVerifyDataView> findHandleVerifyDataView(Page page, @Param("ybHandleVerifyDataView") YbHandleVerifyDataView ybHandleVerifyDataView);

        int findHandleVerifyDataCount(@Param("ybHandleVerifyDataView") YbHandleVerifyDataView ybHandleVerifyDataView);
        }
