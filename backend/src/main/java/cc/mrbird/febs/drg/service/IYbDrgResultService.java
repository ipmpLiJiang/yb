package cc.mrbird.febs.drg.service;

import cc.mrbird.febs.drg.entity.YbDrgResult;
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
public interface IYbDrgResultService extends IService<YbDrgResult> {

        IPage<YbDrgResult> findYbDrgResults(QueryRequest request, YbDrgResult ybDrgResult);

        IPage<YbDrgResult> findYbDrgResultList(QueryRequest request, YbDrgResult ybDrgResult);

        void createYbDrgResult(YbDrgResult ybDrgResult);

        void updateYbDrgResult(YbDrgResult ybDrgResult);

        void deleteYbDrgResults(String[]Ids);

        List<YbDrgResult> findDrgResultList(YbDrgResult drgResult);
        }
