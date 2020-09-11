package cc.mrbird.febs.yb.dao;

import cc.mrbird.febs.yb.entity.YbHandleVerify;
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
 * @since 2020-08-28
 */
public interface YbHandleVerifyMapper extends BaseMapper<YbHandleVerify> {
        void updateYbHandleVerify(YbHandleVerify ybHandleVerify);
        IPage<YbHandleVerify> findYbHandleVerify(Page page, @Param("ybHandleVerify") YbHandleVerify ybHandleVerify);
        }
