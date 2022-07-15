package cc.mrbird.febs.drg.dao;

import cc.mrbird.febs.drg.entity.YbDrgResultView;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * <p>
 * VIEW Mapper 接口
 * </p>
 *
 * @author viki
 * @since 2020-07-30
 */
public interface YbDrgResultViewMapper extends BaseMapper<YbDrgResultView> {

    List<YbDrgResultView> findDrgResultViewList(@Param("ybDrgResultView") YbDrgResultView ybDrgResultView);
}
