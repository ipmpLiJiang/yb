package cc.mrbird.febs.yb.dao;

import cc.mrbird.febs.yb.entity.YbAppealResult;
import cc.mrbird.febs.yb.entity.YbAppealResultView;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.Param;

import java.util.Date;
import java.util.List;

/**
 * <p>
 * Mapper 接口
 * </p>
 *
 * @author vikifindAppealResultGroupDept
 * @since 2020-07-30
 */
public interface YbAppealResultMapper extends BaseMapper<YbAppealResult> {
    void updateYbAppealResult(YbAppealResult ybAppealResult);

    IPage<YbAppealResult> findYbAppealResult(Page page, @Param("ybAppealResult") YbAppealResult ybAppealResult);

    List<String> findAppealResultGroupDept(@Param("ybAppealResultView") YbAppealResultView ybAppealResultView);

    List<YbAppealResult> findAppealResulDataByReset(@Param("applyDateStr") String applyDateStr,@Param("dataType") Integer dataType);

    List<YbAppealResult> findAppealResulDataByRepay(@Param("applyDateStr") String applyDateStr,@Param("dataType") Integer dataType);

    List<YbAppealResult> findAppealResulDataHandle(@Param("applyDateStr") String applyDateStr);

    void updateAppealResulResetData(@Param("applyDateStr") String applyDateStr,@Param("resetPersonId") Long resetPersonId,@Param("resetPersonName") String resetPersonName,@Param("resetDate") Date resetDate);


}
