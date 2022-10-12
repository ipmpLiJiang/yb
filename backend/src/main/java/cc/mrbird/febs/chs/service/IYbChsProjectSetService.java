package cc.mrbird.febs.chs.service;

import cc.mrbird.febs.chs.entity.YbChsProjectSet;
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
 * @since 2022-10-12
 */
public interface IYbChsProjectSetService extends IService<YbChsProjectSet> {

        IPage<YbChsProjectSet> findYbChsProjectSets(QueryRequest request, YbChsProjectSet ybChsProjectSet);

        IPage<YbChsProjectSet> findYbChsProjectSetList(QueryRequest request, YbChsProjectSet ybChsProjectSet);

        void createYbChsProjectSet(YbChsProjectSet ybChsProjectSet);

        void updateYbChsProjectSet(YbChsProjectSet ybChsProjectSet);

        void deleteYbChsProjectSets(String[]Ids);
        }
