package cc.mrbird.febs.yb.service;

import cc.mrbird.febs.yb.entity.YbReconsiderRepay;
import cc.mrbird.febs.yb.entity.YbReconsiderRepayData;
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
 * @since 2020-09-07
 */
public interface IYbReconsiderRepayService extends IService<YbReconsiderRepay> {

        IPage<YbReconsiderRepay> findYbReconsiderRepays(QueryRequest request, YbReconsiderRepay ybReconsiderRepay);

        IPage<YbReconsiderRepay> findYbReconsiderRepayList(QueryRequest request, YbReconsiderRepay ybReconsiderRepay);

        void createYbReconsiderRepay(YbReconsiderRepay ybReconsiderRepay);

        void updateYbReconsiderRepay(YbReconsiderRepay ybReconsiderRepay);

        void deleteYbReconsiderRepays(String[]Ids);

        boolean importReconsiderRepay(List<YbReconsiderRepayData> list,Long uid,String uname,String uploadFileName);
        }
