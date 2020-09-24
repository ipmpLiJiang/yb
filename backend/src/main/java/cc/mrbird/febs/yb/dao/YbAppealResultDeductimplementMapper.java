package cc.mrbird.febs.yb.dao;

import cc.mrbird.febs.yb.entity.YbAppealResultDeductimplement;
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
 * @since 2020-09-24
 */
public interface YbAppealResultDeductimplementMapper extends BaseMapper<YbAppealResultDeductimplement> {
        void updateYbAppealResultDeductimplement(YbAppealResultDeductimplement ybAppealResultDeductimplement);
        IPage<YbAppealResultDeductimplement> findYbAppealResultDeductimplement(Page page, @Param("ybAppealResultDeductimplement") YbAppealResultDeductimplement ybAppealResultDeductimplement);
        }
