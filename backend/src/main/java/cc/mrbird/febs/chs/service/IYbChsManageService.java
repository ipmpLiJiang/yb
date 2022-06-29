package cc.mrbird.febs.chs.service;

import cc.mrbird.febs.chs.entity.YbChsManage;
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
 * @since 2022-06-20
 */
public interface IYbChsManageService extends IService<YbChsManage> {

    IPage<YbChsManage> findYbChsManages(QueryRequest request, YbChsManage ybChsManage);

    IPage<YbChsManage> findYbChsManageList(QueryRequest request, YbChsManage ybChsManage);

    void createYbChsManage(YbChsManage ybChsManage);

    void updateYbChsManage(YbChsManage ybChsManage);

    void deleteYbChsManages(String[] Ids);

    List<YbChsManage> findChsManageList(YbChsManage ybChsManage);

    void updateChsApplyEndDate(String applyDateStr, Integer areaType);

    void updateChsEnableOverdue(String applyDateStr, Integer areaType);
}
