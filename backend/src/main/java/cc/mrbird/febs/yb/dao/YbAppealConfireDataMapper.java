package cc.mrbird.febs.yb.dao;

import cc.mrbird.febs.yb.entity.YbAppealConfireData;
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
public interface YbAppealConfireDataMapper extends BaseMapper<YbAppealConfireData> {
        void updateYbAppealConfireData(YbAppealConfireData ybAppealConfireData);
        IPage<YbAppealConfireData> findYbAppealConfireData(Page page, @Param("ybAppealConfireData") YbAppealConfireData ybAppealConfireData);
        }
