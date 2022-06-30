package cc.mrbird.febs.chs.dao;

import cc.mrbird.febs.chs.entity.YbChsConfire;
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
 * @since 2022-06-30
 */
public interface YbChsConfireMapper extends BaseMapper<YbChsConfire> {
    void updateYbChsConfire(YbChsConfire ybChsConfire);

    IPage<YbChsConfire> findYbChsConfire(Page page, @Param("ybChsConfire") YbChsConfire ybChsConfire);

    IPage<YbChsConfire> findChsConfireView(Page page, @Param("doctorContent") String doctorContent, @Param("adminType") Integer adminType,
                                           @Param("areaType") Integer areaType, @Param("deptContent") String deptContent,
                                           @Param("operatorName") String operatorName, @Param("type") String type);


    IPage<YbChsConfire> findChsConfireUserView(Page page, @Param("doctorContent") String doctorContent, @Param("adminType") Integer adminType,
                                               @Param("areaType") Integer areaType, @Param("deptContent") String deptContent,
                                               @Param("uid") Long uid, @Param("doctorCode") String doctorCode,
                                               @Param("operatorName") String operatorName, @Param("type") String type);

    int findChsConfireCount(@Param("doctorContent") String doctorContent, @Param("adminType") Integer adminType,
                            @Param("areaType") Integer areaType, @Param("deptContent") String deptContent,
                            @Param("operatorName") String operatorName);

    int findChsConfireUserCount(@Param("doctorContent") String doctorContent, @Param("adminType") Integer adminType,
                                @Param("areaType") Integer areaType, @Param("deptContent") String deptContent,
                                @Param("uid") Long uid, @Param("doctorCode") String doctorCode,
                                @Param("operatorName") String operatorName);

    List<YbChsConfire> findChsConfireLlyList(@Param("ybChsConfire") YbChsConfire ybChsConfire);

}
