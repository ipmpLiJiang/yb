package cc.mrbird.febs.yb.service;

import cc.mrbird.febs.system.domain.User;
import cc.mrbird.febs.yb.entity.YbNotice;
import cc.mrbird.febs.yb.entity.YbNoticeData;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

import com.baomidou.mybatisplus.core.metadata.IPage;
import cc.mrbird.febs.common.domain.QueryRequest;
import com.baomidou.mybatisplus.core.metadata.IPage;
import org.springframework.transaction.annotation.Transactional;

/**
 * <p>
 * 服务类
 * </p>
 *
 * @author viki
 * @since 2021-03-08
 */
public interface IYbNoticeService extends IService<YbNotice> {

    IPage<YbNotice> findYbNotices(QueryRequest request, YbNotice ybNotice);

    IPage<YbNotice> findYbNoticeList(QueryRequest request, YbNotice ybNotice);

    IPage<YbNotice> findNoticeView(QueryRequest request, YbNotice ybNotice,String usercode);

    void createYbNotice(YbNotice ybNotice);

    void updateYbNotice(YbNotice ybNotice);

    void deleteYbNotices(String[] Ids);

    void createNotice(YbNotice ybNotice, List<YbNoticeData> createDataList);

    void updateNotice(YbNotice ybNotice, List<YbNoticeData> dataList);

    void updateReleaseNotice(YbNotice ybNotice, User currentUser);

    void deleteUpdateNotices(String[] Ids);

    int updateNoticeClickNum(YbNotice ybNotice);

    YbNotice findNotice(YbNotice ybNotice);
}
