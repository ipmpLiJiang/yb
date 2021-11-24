package cc.mrbird.febs.drg.service;

import cc.mrbird.febs.drg.entity.YbDrgApplyTask;
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
public interface IYbDrgApplyTaskService extends IService<YbDrgApplyTask> {

        IPage<YbDrgApplyTask> findYbDrgApplyTasks(QueryRequest request, YbDrgApplyTask ybDrgApplyTask);

        IPage<YbDrgApplyTask> findYbDrgApplyTaskList(QueryRequest request, YbDrgApplyTask ybDrgApplyTask);

        void createYbDrgApplyTask(YbDrgApplyTask ybDrgApplyTask);

        void updateYbDrgApplyTask(YbDrgApplyTask ybDrgApplyTask);

        void deleteYbDrgApplyTasks(String[]Ids);
        }
