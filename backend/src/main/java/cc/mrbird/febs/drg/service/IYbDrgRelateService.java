package cc.mrbird.febs.drg.service;

import cc.mrbird.febs.drg.entity.YbDrgRelate;
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
public interface IYbDrgRelateService extends IService<YbDrgRelate> {

        IPage<YbDrgRelate> findYbDrgRelates(QueryRequest request, YbDrgRelate ybDrgRelate);

        IPage<YbDrgRelate> findYbDrgRelateList(QueryRequest request, YbDrgRelate ybDrgRelate);

        void createYbDrgRelate(YbDrgRelate ybDrgRelate);

        void updateYbDrgRelate(YbDrgRelate ybDrgRelate);

        void deleteYbDrgRelates(String[]Ids);
        }
