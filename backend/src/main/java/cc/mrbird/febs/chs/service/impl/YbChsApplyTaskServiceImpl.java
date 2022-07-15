package cc.mrbird.febs.chs.service.impl;

import cc.mrbird.febs.chs.dao.YbChsApplyTaskMapper;
import cc.mrbird.febs.chs.entity.YbChsApplyTask;
import cc.mrbird.febs.chs.service.IYbChsApplyTaskService;
import cc.mrbird.febs.common.domain.QueryRequest;
import cc.mrbird.febs.common.utils.SortUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.UUID;

/**
 * <p>
 * 服务实现类
 * </p>
 *
 * @author viki
 * @since 2022-06-24
 */
@Slf4j
@Service("IYbChsApplyTaskService")
@Transactional(propagation = Propagation.SUPPORTS, readOnly = true, rollbackFor = Exception.class)
public class YbChsApplyTaskServiceImpl extends ServiceImpl<YbChsApplyTaskMapper, YbChsApplyTask> implements IYbChsApplyTaskService {


    @Override
    public IPage<YbChsApplyTask> findYbChsApplyTasks(QueryRequest request, YbChsApplyTask ybChsApplyTask) {
        try {
            LambdaQueryWrapper<YbChsApplyTask> queryWrapper = new LambdaQueryWrapper<>();
            queryWrapper.eq(YbChsApplyTask::getIsDeletemark, 1);//1是未删 0是已删
            if(ybChsApplyTask.getApplyDateStr() != null) {
                queryWrapper.eq(YbChsApplyTask::getApplyDateStr,ybChsApplyTask.getApplyDateStr());
            }
            if(ybChsApplyTask.getAreaType() != null) {
                queryWrapper.eq(YbChsApplyTask::getAreaType,ybChsApplyTask.getAreaType());
            }
            if(ybChsApplyTask.getIsOutpfees() != null) {
                queryWrapper.eq(YbChsApplyTask::getIsOutpfees,ybChsApplyTask.getIsOutpfees());
            }

            Page<YbChsApplyTask> page = new Page<>();
            SortUtil.handlePageSort(request, page, false);//true 是属性  false是数据库字段可两个
            return this.page(page, queryWrapper);
        } catch (Exception e) {
            log.error("获取字典信息失败", e);
            return null;
        }
    }

    @Override
    public IPage<YbChsApplyTask> findYbChsApplyTaskList(QueryRequest request, YbChsApplyTask ybChsApplyTask) {
        try {
            Page<YbChsApplyTask> page = new Page<>();
            SortUtil.handlePageSort(request, page, false);//true 是属性  false是数据库字段可两个
            return this.baseMapper.findYbChsApplyTask(page, ybChsApplyTask);
        } catch (Exception e) {
            log.error("获取失败", e);
            return null;
        }
    }

    @Override
    @Transactional
    public void createYbChsApplyTask(YbChsApplyTask ybChsApplyTask) {
        ybChsApplyTask.setId(UUID.randomUUID().toString());
        ybChsApplyTask.setCreateTime(new Date());
        ybChsApplyTask.setIsDeletemark(1);
        this.save(ybChsApplyTask);
    }

    @Override
    @Transactional
    public void updateYbChsApplyTask(YbChsApplyTask ybChsApplyTask) {
        ybChsApplyTask.setModifyTime(new Date());
        this.baseMapper.updateYbChsApplyTask(ybChsApplyTask);
    }

    @Override
    @Transactional
    public void deleteYbChsApplyTasks(String[] Ids) {
        List<String> list = Arrays.asList(Ids);
        this.baseMapper.deleteBatchIds(list);
    }

        @Override
        public YbChsApplyTask findChsApplyTasks(YbChsApplyTask ybChsApplyTask) {
                return this.baseMapper.findChsApplyTask(ybChsApplyTask);
        }
}