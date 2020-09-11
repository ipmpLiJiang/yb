package cc.mrbird.febs.yb.dao;

import cc.mrbird.febs.yb.entity.YbAppealManage;
import cc.mrbird.febs.yb.entity.YbAppealManageView;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.Param;

/**
 * <p>
 * Mapper 接口
 * </p>
 *
 * @author viki
 * @since 2020-07-30
 */
public interface YbAppealManageMapper extends BaseMapper<YbAppealManage> {
    void updateYbAppealManage(YbAppealManage ybAppealManage);

    IPage<YbAppealManage> findYbAppealManage(Page page, @Param("ybAppealManage") YbAppealManage ybAppealManage);

}
