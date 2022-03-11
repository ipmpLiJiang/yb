package cc.mrbird.febs.drg.dao;

import cc.mrbird.febs.drg.entity.YbDrgConfireData;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * <p>
 * Mapper 接口
 * </p>
 *
 * @author viki
 * @since 2021-01-11
 */
public interface YbDrgConfireDataMapper extends BaseMapper<YbDrgConfireData> {
    void updateYbDrgConfireData(YbDrgConfireData ybDrgConfireData);

    IPage<YbDrgConfireData> findYbDrgConfireData(Page page, @Param("ybDrgConfireData") YbDrgConfireData ybDrgConfireData);

}
