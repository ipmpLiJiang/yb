package cc.mrbird.febs.chs.service;

import cc.mrbird.febs.chs.entity.YbChsResultView;
import cc.mrbird.febs.common.domain.QueryRequest;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;

import java.io.IOException;
import java.util.List;

/**
 * <p>
 * VIEW 服务类
 * </p>
 *
 * @author viki
 * @since 2020-07-30
 */
public interface IYbChsResultViewService extends IService<YbChsResultView> {
    IPage<YbChsResultView> findYbChsResultLikeViews(QueryRequest request, YbChsResultView ybChsResultView, String keyField);

    IPage<YbChsResultView> findYbChsResultViews(QueryRequest request, YbChsResultView ybChsResultView);

    List<YbChsResultView> findChsResultLeftDetailViewLists(YbChsResultView ybChsResultView);

    List<YbChsResultView> findChsResultViewLists(YbChsResultView ybChsResultView);

    List<YbChsResultView> findChsResultFileSizeByOrderNumList(String applyDateStr, int areaType, List<Integer> orderNumList,Integer maxCount) throws IOException;
}
