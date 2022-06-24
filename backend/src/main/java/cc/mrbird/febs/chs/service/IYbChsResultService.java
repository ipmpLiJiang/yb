package cc.mrbird.febs.chs.service;

import cc.mrbird.febs.chs.entity.YbChsResult;
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
 * @since 2022-06-20
 */
public interface IYbChsResultService extends IService<YbChsResult> {

        IPage<YbChsResult> findYbChsResults(QueryRequest request, YbChsResult ybChsResult);

        IPage<YbChsResult> findYbChsResultList(QueryRequest request, YbChsResult ybChsResult);

        void createYbChsResult(YbChsResult ybChsResult);

        void updateYbChsResult(YbChsResult ybChsResult);

        void deleteYbChsResults(String[]Ids);
        }
