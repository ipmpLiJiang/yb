package cc.mrbird.febs.yb.service;

import cc.mrbird.febs.yb.entity.YbReconsiderApplyTask;
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
 * @since 2020-11-23
 */
public interface IYbReconsiderApplyTaskService extends IService<YbReconsiderApplyTask> {

        IPage<YbReconsiderApplyTask> findYbReconsiderApplyTasks(QueryRequest request, YbReconsiderApplyTask ybReconsiderApplyTask);

        IPage<YbReconsiderApplyTask> findYbReconsiderApplyTaskList(QueryRequest request, YbReconsiderApplyTask ybReconsiderApplyTask);

        void createYbReconsiderApplyTask(YbReconsiderApplyTask ybReconsiderApplyTask);

        void updateYbReconsiderApplyTask(YbReconsiderApplyTask ybReconsiderApplyTask);

        void deleteYbReconsiderApplyTasks(String[]Ids);

        List<YbReconsiderApplyTask>  findReconsiderApplyTaskList(YbReconsiderApplyTask ybReconsiderApplyTask);
        }
