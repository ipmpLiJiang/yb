package cc.mrbird.febs.drg.dao;

import cc.mrbird.febs.drg.entity.YbDrgVerify;
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
 * @since 2021-11-23
 */
public interface YbDrgVerifyMapper extends BaseMapper<YbDrgVerify> {
    void updateYbDrgVerify(YbDrgVerify ybDrgVerify);

    IPage<YbDrgVerify> findYbDrgVerify(Page page, @Param("ybDrgVerify") YbDrgVerify ybDrgVerify);

    List<YbDrgVerify> findDrgVerifyList(@Param("applyDateStr") String applyDateStr,
                                        @Param("areaType") Integer areaType,
                                        @Param("state") Integer state,
                                        @Param("isDksName") boolean isDksName);
}
