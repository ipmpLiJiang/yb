package cc.mrbird.febs.yb.dao;

import cc.mrbird.febs.yb.entity.YbAppealResultReportView;
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
 * @since 2020-09-24
 */
public interface YbAppealResultReportViewMapper extends BaseMapper<YbAppealResultReportView> {
        void updateYbAppealResultReportView(YbAppealResultReportView ybAppealResultReportView);
        IPage<YbAppealResultReportView> findYbAppealResultReportView(Page page, @Param("ybAppealResultReportView") YbAppealResultReportView ybAppealResultReportView);
        }
