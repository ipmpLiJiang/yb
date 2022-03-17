package cc.mrbird.febs.drg.service;

import cc.mrbird.febs.drg.entity.YbDrgDks;
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
 * @since 2022-03-16
 */
public interface IYbDrgDksService extends IService<YbDrgDks> {

        IPage<YbDrgDks> findYbDrgDkss(QueryRequest request, YbDrgDks ybDrgDks);

        IPage<YbDrgDks> findYbDrgDksList(QueryRequest request, YbDrgDks ybDrgDks);

        List<YbDrgDks> findDksList(YbDrgDks ybDrgDks,int type);

        void createYbDrgDks(YbDrgDks ybDrgDks);

        void updateYbDrgDks(YbDrgDks ybDrgDks);

        void deleteYbDrgDkss(String[]Ids);
        }
