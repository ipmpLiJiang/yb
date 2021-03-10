package cc.mrbird.febs.yb.service;

import cc.mrbird.febs.yb.entity.YbNoticeData;
import com.baomidou.mybatisplus.extension.service.IService;
import java.util.List;

import com.baomidou.mybatisplus.core.metadata.IPage;
import cc.mrbird.febs.common.domain.QueryRequest;
import com.baomidou.mybatisplus.core.metadata.IPage;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author viki
 * @since 2021-03-08
 */
public interface IYbNoticeDataService extends IService<YbNoticeData> {

        IPage<YbNoticeData> findYbNoticeDatas(QueryRequest request, YbNoticeData ybNoticeData);

        IPage<YbNoticeData> findYbNoticeDataList(QueryRequest request, YbNoticeData ybNoticeData);

        void createYbNoticeData(YbNoticeData ybNoticeData);

        void updateYbNoticeData(YbNoticeData ybNoticeData);

        void deleteYbNoticeDatas(String[]Ids);

        List<YbNoticeData> findNoticeDataList(YbNoticeData ybNoticeData);
        }
