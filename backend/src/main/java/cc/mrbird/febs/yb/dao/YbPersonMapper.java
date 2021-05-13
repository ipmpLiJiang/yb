package cc.mrbird.febs.yb.dao;

import cc.mrbird.febs.yb.entity.YbPerson;
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
 * @since 2020-07-21
 */
public interface YbPersonMapper extends BaseMapper<YbPerson> {
    void updateYbPerson(YbPerson ybPerson);

    IPage<YbPerson> findYbPerson(Page page, @Param("ybPerson") YbPerson ybPerson);

    List<YbPerson> findPersonList(@Param("type") Integer type);

    List<YbPerson> findPersonWarnList(@Param("applyDateStr") String applyDateStr, @Param("areaType") Integer areaType, @Param("acceptState") Integer acceptState, @Param("typeno") Integer typeno, @Param("sourceType") Integer sourceType);


    List<YbPerson> findPersonResultList(@Param("applyDateStr") String applyDateStr, @Param("areaType") Integer areaType);
}
