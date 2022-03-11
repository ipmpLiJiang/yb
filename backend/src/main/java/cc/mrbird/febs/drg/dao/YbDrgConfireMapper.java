package cc.mrbird.febs.drg.dao;

import cc.mrbird.febs.drg.entity.YbDrgConfire;
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
public interface YbDrgConfireMapper extends BaseMapper<YbDrgConfire> {
    void updateYbDrgConfire(YbDrgConfire ybDrgConfire);

    IPage<YbDrgConfire> findYbDrgConfire(Page page, @Param("ybDrgConfire") YbDrgConfire ybDrgConfire);

    IPage<YbDrgConfire> findDrgConfireView(Page page, @Param("doctorContent") String doctorContent, @Param("adminType") Integer adminType,
                                                 @Param("areaType") Integer areaType, @Param("deptContent") String deptContent,
                                                 @Param("operatorName") String operatorName, @Param("type") String type);

    IPage<YbDrgConfire> findDrgConfireUserView(Page page, @Param("doctorContent") String doctorContent, @Param("adminType") Integer adminType,
                                                     @Param("areaType") Integer areaType, @Param("deptContent") String deptContent,
                                                     @Param("uid") Long uid, @Param("doctorCode") String doctorCode,
                                                     @Param("operatorName") String operatorName, @Param("type") String type);

    int findDrgConfireCount(@Param("doctorContent") String doctorContent, @Param("adminType") Integer adminType,
                               @Param("areaType") Integer areaType, @Param("deptContent") String deptContent,
                               @Param("operatorName") String operatorName);

    int findDrgConfireUserCount(@Param("doctorContent") String doctorContent, @Param("adminType") Integer adminType,
                                   @Param("areaType") Integer areaType, @Param("deptContent") String deptContent,
                                   @Param("uid") Long uid, @Param("doctorCode") String doctorCode,
                                   @Param("operatorName") String operatorName);

    List<YbDrgConfire> findDrgConfireLlyList(@Param("ybDrgConfire") YbDrgConfire ybDrgConfire);
}
