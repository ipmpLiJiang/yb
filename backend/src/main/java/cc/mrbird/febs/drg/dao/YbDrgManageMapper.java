package cc.mrbird.febs.drg.dao;

import cc.mrbird.febs.drg.entity.YbDrgManage;
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
 * @since 2021-11-23
 */
public interface YbDrgManageMapper extends BaseMapper<YbDrgManage> {
        void updateYbDrgManage(YbDrgManage ybDrgManage);
        IPage<YbDrgManage> findYbDrgManage(Page page, @Param("ybDrgManage") YbDrgManage ybDrgManage);

        List<YbDrgManage> findDrgManageApplyEndDateList(@Param("pid") String pid,@Param("applyDateStr") String applyDateStr,
                                                        @Param("areaType") Integer areaType);

        List<YbDrgManage> findDrgManageEnableOverdueList(@Param("pid") String pid,@Param("applyDateStr") String applyDateStr,
                                                        @Param("areaType") Integer areaType,@Param("enableDate") String enableDate);

}
