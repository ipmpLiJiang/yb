package cc.mrbird.febs.yb.dao;

import cc.mrbird.febs.yb.entity.YbAppealConfire;
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
 * @since 2021-01-11
 */
public interface YbAppealConfireMapper extends BaseMapper<YbAppealConfire> {
        void updateYbAppealConfire(YbAppealConfire ybAppealConfire);
        IPage<YbAppealConfire> findYbAppealConfire(Page page, @Param("ybAppealConfire") YbAppealConfire ybAppealConfire);

        IPage<YbAppealConfire>  findAppealConfireView(Page page, @Param("doctorContent") String doctorContent, @Param("adminType") Integer adminType,@Param("areaType") Integer areaType, @Param("deptContent") String deptContent);

        IPage<YbAppealConfire>  findAppealConfireUserView(Page page, @Param("doctorContent") String doctorContent, @Param("adminType") Integer adminType,@Param("areaType") Integer areaType, @Param("deptContent") String deptContent, @Param("uid") Long uid);

        int  findAppealConfireCount(@Param("doctorContent") String doctorContent, @Param("adminType") Integer adminType,@Param("areaType") Integer areaType, @Param("deptContent") String deptContent);

        int  findAppealConfireUserCount(@Param("doctorContent") String doctorContent, @Param("adminType") Integer adminType,@Param("areaType") Integer areaType, @Param("deptContent") String deptContent, @Param("uid") Long uid);
        }
