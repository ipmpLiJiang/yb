package cc.mrbird.febs.yb.dao;

import cc.mrbird.febs.yb.entity.YbDept;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author viki
 * @since 2020-07-21
 */
public interface YbDeptMapper extends BaseMapper<YbDept> {
        void updateYbDept(YbDept ybDept);
        IPage<YbDept> findYbDept(Page page, @Param("ybDept") YbDept ybDept);

        List<YbDept> findDeptAppealConfireList(@Param("appealConfireId") String appealConfireId,@Param("comments") String comments, @Param("areaType") Integer areaType);


        List<YbDept> findDeptAppealConfireByUserList(@Param("doctorCode") String doctorCode, @Param("areaType") Integer areaType);

}
