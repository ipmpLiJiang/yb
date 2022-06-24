package cc.mrbird.febs.chs.service;

import cc.mrbird.febs.chs.entity.YbDks;
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
 * @since 2022-06-24
 */
public interface IYbDksService extends IService<YbDks> {

        IPage<YbDks> findYbDkss(QueryRequest request, YbDks ybDks);

        IPage<YbDks> findYbDksList(QueryRequest request, YbDks ybDks);

        void createYbDks(YbDks ybDks);

        void updateYbDks(YbDks ybDks);

        void deleteYbDkss(String[]Ids);
        }
