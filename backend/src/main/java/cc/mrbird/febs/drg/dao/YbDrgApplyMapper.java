package cc.mrbird.febs.drg.dao;

import cc.mrbird.febs.drg.entity.YbDrgApply;
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
 * @since 2021-11-23
 */
public interface YbDrgApplyMapper extends BaseMapper<YbDrgApply> {
        void updateYbDrgApply(YbDrgApply ybDrgApply);
        IPage<YbDrgApply> findYbDrgApply(Page page, @Param("ybDrgApply") YbDrgApply ybDrgApply);

        YbDrgApply findDrgApplyByApplyDateStr(@Param("applyDateStr") String applyDateStr, @Param("areaType") Integer areaType);
        }
