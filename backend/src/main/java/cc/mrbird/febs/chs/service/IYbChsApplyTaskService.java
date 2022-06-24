package cc.mrbird.febs.chs.service;

import cc.mrbird.febs.chs.entity.YbChsApplyTask;
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
 * @since 2022-06-24
 */
public interface IYbChsApplyTaskService extends IService<YbChsApplyTask> {

    IPage<YbChsApplyTask> findYbChsApplyTasks(QueryRequest request, YbChsApplyTask ybChsApplyTask);

    IPage<YbChsApplyTask> findYbChsApplyTaskList(QueryRequest request, YbChsApplyTask ybChsApplyTask);

    void createYbChsApplyTask(YbChsApplyTask ybChsApplyTask);

    void updateYbChsApplyTask(YbChsApplyTask ybChsApplyTask);

    void deleteYbChsApplyTasks(String[] Ids);

    YbChsApplyTask findChsApplyTasks(YbChsApplyTask ybChsApplyTask);
}
