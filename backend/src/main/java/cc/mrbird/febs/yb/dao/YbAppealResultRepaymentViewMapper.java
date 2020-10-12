package cc.mrbird.febs.yb.dao;

import cc.mrbird.febs.yb.entity.YbAppealResultRepaymentView;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.Param;

/**
 * <p>
 * VIEW Mapper 接口
 * </p>
 *
 * @author viki
 * @since 2020-10-09
 */
public interface YbAppealResultRepaymentViewMapper extends BaseMapper<YbAppealResultRepaymentView> {
        void updateYbAppealResultRepaymentView(YbAppealResultRepaymentView ybAppealResultRepaymentView);
        IPage<YbAppealResultRepaymentView> findYbAppealResultRepaymentView(Page page, @Param("ybAppealResultRepaymentView") YbAppealResultRepaymentView ybAppealResultRepaymentView);
        }
