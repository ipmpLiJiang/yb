package cc.mrbird.febs.yb.service.impl;

import cc.mrbird.febs.common.domain.QueryRequest;
import cc.mrbird.febs.common.utils.SortUtil;
import cc.mrbird.febs.yb.entity.YbReconsiderApplyTask;
import cc.mrbird.febs.yb.dao.YbReconsiderApplyTaskMapper;
import cc.mrbird.febs.yb.service.IYbReconsiderApplyTaskService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;
import java.time.LocalDate;

/**
 * <p>
 * 服务实现类
 * </p>
 *
 * @author viki
 * @since 2020-11-23
 */
@Slf4j
@Service("IYbReconsiderApplyTaskService")
@Transactional(propagation = Propagation.SUPPORTS, readOnly = true, rollbackFor = Exception.class)
public class YbReconsiderApplyTaskServiceImpl extends ServiceImpl<YbReconsiderApplyTaskMapper, YbReconsiderApplyTask> implements IYbReconsiderApplyTaskService {


    @Override
    public IPage<YbReconsiderApplyTask> findYbReconsiderApplyTasks(QueryRequest request, YbReconsiderApplyTask ybReconsiderApplyTask) {
        try {
            LambdaQueryWrapper<YbReconsiderApplyTask> queryWrapper = new LambdaQueryWrapper<>();
            queryWrapper.eq(YbReconsiderApplyTask::getIsDeletemark, 1);//1是未删 0是已删


            Page<YbReconsiderApplyTask> page = new Page<>();
            SortUtil.handlePageSort(request, page, false);//true 是属性  false是数据库字段可两个
            return this.page(page, queryWrapper);
        } catch (Exception e) {
            log.error("获取字典信息失败", e);
            return null;
        }
    }

    @Override
    public IPage<YbReconsiderApplyTask> findYbReconsiderApplyTaskList(QueryRequest request, YbReconsiderApplyTask ybReconsiderApplyTask) {
        try {
            Page<YbReconsiderApplyTask> page = new Page<>();
            SortUtil.handlePageSort(request, page, false);//true 是属性  false是数据库字段可两个
            return this.baseMapper.findYbReconsiderApplyTask(page, ybReconsiderApplyTask);
        } catch (Exception e) {
            log.error("获取失败", e);
            return null;
        }
    }

    @Override
    @Transactional
    public void createYbReconsiderApplyTask(YbReconsiderApplyTask ybReconsiderApplyTask) {
        if (ybReconsiderApplyTask.getId() == null) {
            ybReconsiderApplyTask.setId(UUID.randomUUID().toString());
        }
        ybReconsiderApplyTask.setCreateTime(new Date());
        ybReconsiderApplyTask.setIsDeletemark(1);
        this.save(ybReconsiderApplyTask);
    }

    @Override
    @Transactional
    public void updateYbReconsiderApplyTask(YbReconsiderApplyTask ybReconsiderApplyTask) {
        ybReconsiderApplyTask.setModifyTime(new Date());
        this.baseMapper.updateYbReconsiderApplyTask(ybReconsiderApplyTask);
    }

    @Override
    @Transactional
    public void deleteYbReconsiderApplyTasks(String[] Ids) {
        List<String> list = Arrays.asList(Ids);
        this.baseMapper.deleteBatchIds(list);
    }

    @Override
    public List<YbReconsiderApplyTask> findReconsiderApplyTaskList(YbReconsiderApplyTask ybReconsiderApplyTask) {
        LambdaQueryWrapper<YbReconsiderApplyTask> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(YbReconsiderApplyTask::getIsDeletemark, 1);//1是未删 0是已删

        if (ybReconsiderApplyTask.getApplyDateStr() != null) {
            queryWrapper.eq(YbReconsiderApplyTask::getApplyDateStr, ybReconsiderApplyTask.getApplyDateStr());
        }

        if (ybReconsiderApplyTask.getDataType() != null) {
            queryWrapper.eq(YbReconsiderApplyTask::getDataType, ybReconsiderApplyTask.getDataType());
        }

        if (ybReconsiderApplyTask.getTypeno() != null) {
            queryWrapper.eq(YbReconsiderApplyTask::getTypeno, ybReconsiderApplyTask.getTypeno());
        }

        if (ybReconsiderApplyTask.getState() != null) {
            queryWrapper.eq(YbReconsiderApplyTask::getState, ybReconsiderApplyTask.getState());
        }

        if (ybReconsiderApplyTask.getAreaType() != null) {
            queryWrapper.eq(YbReconsiderApplyTask::getAreaType, ybReconsiderApplyTask.getAreaType());
        }

        return this.list(queryWrapper);
    }


}