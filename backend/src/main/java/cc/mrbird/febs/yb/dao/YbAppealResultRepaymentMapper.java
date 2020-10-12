package cc.mrbird.febs.yb.dao;

import cc.mrbird.febs.yb.entity.YbAppealResultRepayment;
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
 * @since 2020-10-09
 */
public interface YbAppealResultRepaymentMapper extends BaseMapper<YbAppealResultRepayment> {
        void updateYbAppealResultRepayment(YbAppealResultRepayment ybAppealResultRepayment);
        IPage<YbAppealResultRepayment> findYbAppealResultRepayment(Page page, @Param("ybAppealResultRepayment") YbAppealResultRepayment ybAppealResultRepayment);
        }
