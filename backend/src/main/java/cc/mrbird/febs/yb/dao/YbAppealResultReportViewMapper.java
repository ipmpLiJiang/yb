package cc.mrbird.febs.yb.dao;

import cc.mrbird.febs.yb.entity.YbAppealResultReportView;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.Param;

import java.util.List;

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

    IPage<YbAppealResultReportView> findAppealResultReportView(Page page, @Param("ybAppealResultReportView") YbAppealResultReportView ybAppealResultReportView,@Param("pidList") List<String> pidList,@Param("keyField") String keyField);

    int findAppealResultReportCount(@Param("ybAppealResultReportView") YbAppealResultReportView ybAppealResultReportView,@Param("pidList") List<String> pidList,@Param("keyField") String keyField);

    List<YbAppealResultReportView> findAppealResultReportList(@Param("ybAppealResultReportView") YbAppealResultReportView ybAppealResultReportView,@Param("pidList") List<String> pidList,@Param("keyField") String keyField);

    IPage<YbAppealResultReportView> findAppealResultReportLikeView(Page page, @Param("ybAppealResultReportView") YbAppealResultReportView ybAppealResultReportView,@Param("pidList") List<String> pidList,@Param("keyField") String keyField);

    int findAppealResultReportLikeCount(@Param("ybAppealResultReportView") YbAppealResultReportView ybAppealResultReportView,@Param("pidList") List<String> pidList,@Param("keyField") String keyField);

    List<YbAppealResultReportView> findAppealResultReportLikeList(@Param("ybAppealResultReportView") YbAppealResultReportView ybAppealResultReportView,@Param("pidList") List<String> pidList,@Param("keyField") String keyField);



}
