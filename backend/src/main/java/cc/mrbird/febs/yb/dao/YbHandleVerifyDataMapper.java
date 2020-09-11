package cc.mrbird.febs.yb.dao;

import cc.mrbird.febs.yb.entity.YbHandleVerifyData;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.Param;

/**
 * <p>
 * Mapper 接口
 * </p>
 *
 * @author viki
 * @since 2020-08-28
 */
public interface YbHandleVerifyDataMapper extends BaseMapper<YbHandleVerifyData> {
    void updateYbHandleVerifyData(YbHandleVerifyData ybHandleVerifyData);

    IPage<YbHandleVerifyData> findYbHandleVerifyData(Page page, @Param("ybHandleVerifyData") YbHandleVerifyData ybHandleVerifyData);

}
