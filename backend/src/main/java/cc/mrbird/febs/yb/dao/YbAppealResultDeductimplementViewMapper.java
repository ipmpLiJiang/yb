package cc.mrbird.febs.yb.dao;

import cc.mrbird.febs.yb.entity.YbAppealResultDeductimplementView;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.Param;

import java.sql.Date;

/**
 * <p>
 * VIEW Mapper 接口
 * </p>
 *
 * @author viki
 * @since 2020-09-24
 */
public interface YbAppealResultDeductimplementViewMapper extends BaseMapper<YbAppealResultDeductimplementView> {
        void updateYbAppealResultDeductimplementView(YbAppealResultDeductimplementView ybAppealResultDeductimplementView);
        IPage<YbAppealResultDeductimplementView> findYbAppealResultDeductimplementView(Page page, @Param("ybAppealResultDeductimplementView") YbAppealResultDeductimplementView ybAppealResultDeductimplementView);

        IPage<YbAppealResultDeductimplementView> findAppealResultDeductimplementView(Page page, @Param("ybAppealResultDeductimplementView") YbAppealResultDeductimplementView ybAppealResultDeductimplementView);

}
