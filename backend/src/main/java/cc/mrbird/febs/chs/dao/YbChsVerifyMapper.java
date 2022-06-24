package cc.mrbird.febs.chs.dao;

import cc.mrbird.febs.chs.entity.YbChsVerify;
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
 * @since 2022-06-20
 */
public interface YbChsVerifyMapper extends BaseMapper<YbChsVerify> {
        void updateYbChsVerify(YbChsVerify ybChsVerify);
        IPage<YbChsVerify> findYbChsVerify(Page page, @Param("ybChsVerify") YbChsVerify ybChsVerify);
        }
