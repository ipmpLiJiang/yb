package cc.mrbird.febs.yb.service;

import cc.mrbird.febs.yb.entity.YbReconsiderResetMain;
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
 * @since 2020-08-17
 */
public interface IYbReconsiderResetMainService extends IService<YbReconsiderResetMain> {

        IPage<YbReconsiderResetMain> findYbReconsiderResetMains(QueryRequest request, YbReconsiderResetMain ybReconsiderResetMain);

        IPage<YbReconsiderResetMain> findYbReconsiderResetMainList(QueryRequest request, YbReconsiderResetMain ybReconsiderResetMain);

        void createYbReconsiderResetMain(YbReconsiderResetMain ybReconsiderResetMain);

        void updateYbReconsiderResetMain(YbReconsiderResetMain ybReconsiderResetMain);

        void deleteYbReconsiderResetMains(String[]Ids);
        }
