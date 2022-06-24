package cc.mrbird.febs.chs.dao;

import cc.mrbird.febs.chs.entity.YbChsApply;
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
 * @since 2022-06-20
 */
public interface YbChsApplyMapper extends BaseMapper<YbChsApply> {
        void updateYbChsApply(YbChsApply ybChsApply);
        IPage<YbChsApply> findYbChsApply(Page page, @Param("ybChsApply") YbChsApply ybChsApply);

        YbChsApply findChsApplyByApplyDateStr(@Param("appltDateStr") String appltDateStr,@Param("areaType") Integer areaType);
        }
