package cc.mrbird.febs.chs.service;

import cc.mrbird.febs.chs.entity.YbChsJk;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

import com.baomidou.mybatisplus.core.metadata.IPage;
import cc.mrbird.febs.common.domain.QueryRequest;
import com.baomidou.mybatisplus.core.metadata.IPage;

/**
 * <p>
 * 服务类
 * </p>
 *
 * @author viki
 * @since 2022-06-21
 */
public interface IYbChsJkService extends IService<YbChsJk> {

    IPage<YbChsJk> findYbChsJks(QueryRequest request, YbChsJk ybChsJk);

    IPage<YbChsJk> findYbChsJkList(QueryRequest request, YbChsJk ybChsJk);

    void createYbChsJk(YbChsJk ybChsJk);

    void updateYbChsJk(YbChsJk ybChsJk);

    void deleteYbChsJks(String[] Ids);

    IPage<YbChsJk>  findYbChsJkByApplyDataId(QueryRequest request,String applyDataId);
}
