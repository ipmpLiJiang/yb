package cc.mrbird.febs.chs.dao;

import cc.mrbird.febs.chs.entity.YbChsConfireData;
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
 * @since 2022-06-30
 */
public interface YbChsConfireDataMapper extends BaseMapper<YbChsConfireData> {
        void updateYbChsConfireData(YbChsConfireData ybChsConfireData);
        IPage<YbChsConfireData> findYbChsConfireData(Page page, @Param("ybChsConfireData") YbChsConfireData ybChsConfireData);
        }
