package cc.mrbird.febs.drg.service;

import cc.mrbird.febs.drg.entity.YbDrgJk;
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
 * @since 2021-11-23
 */
public interface IYbDrgJkService extends IService<YbDrgJk> {

        IPage<YbDrgJk> findYbDrgJks(QueryRequest request, YbDrgJk ybDrgJk);

        IPage<YbDrgJk> findYbDrgJkList(QueryRequest request, YbDrgJk ybDrgJk);

        void createYbDrgJk(YbDrgJk ybDrgJk);

        void updateYbDrgJk(YbDrgJk ybDrgJk);

        void deleteYbDrgJks(String[]Ids);

        YbDrgJk findYbDrgJkByApplyDataId(String applyDataId);

        List<YbDrgJk> findDrgJkApplyDataByPid(String pid);
        }
