package cc.mrbird.febs.drg.service;

import cc.mrbird.febs.common.domain.QueryRequest;
import cc.mrbird.febs.drg.entity.YbDrgResultView;
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
public interface IYbDrgResultViewService extends IService<YbDrgResultView> {
    IPage<YbDrgResultView> findDrgResultView(QueryRequest request, YbDrgResultView ybDrgResultView, String keyField);

    List<YbDrgResultView> findDrgResultViewLists(YbDrgResultView ybDrgResultView);

    List<YbDrgResultView> findDrgResultViewInnerLists(YbDrgResultView ybDrgResultView);

    List<YbDrgResultView> findDrgResultFileSizeByOrderNumberList(String applyDateStr, int areaType, List<String> orderNumberList) throws IOException;
}
