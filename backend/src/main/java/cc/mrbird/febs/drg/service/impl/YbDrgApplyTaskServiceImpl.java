package cc.mrbird.febs.drg.service.impl;

import cc.mrbird.febs.common.domain.QueryRequest;
import cc.mrbird.febs.common.utils.SortUtil;
import cc.mrbird.febs.drg.entity.YbDrgApplyTask;
import cc.mrbird.febs.drg.dao.YbDrgApplyTaskMapper;
import cc.mrbird.febs.drg.service.IYbDrgApplyTaskService;
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
 * @since 2021-11-23
 */
@Slf4j
@Service("IYbDrgApplyTaskService")
@Transactional(propagation = Propagation.SUPPORTS, readOnly = true, rollbackFor = Exception.class)
public class YbDrgApplyTaskServiceImpl extends ServiceImpl<YbDrgApplyTaskMapper, YbDrgApplyTask> implements IYbDrgApplyTaskService {


    @Override
    public IPage<YbDrgApplyTask> findYbDrgApplyTasks(QueryRequest request, YbDrgApplyTask ybDrgApplyTask) {
        try {
            LambdaQueryWrapper<YbDrgApplyTask> queryWrapper = new LambdaQueryWrapper<>();
//            queryWrapper.eq(YbDrgApplyTask::getIsDeletemark, 1);//1是未删 0是已删


            Page<YbDrgApplyTask> page = new Page<>();
            SortUtil.handlePageSort(request, page, false);//true 是属性  false是数据库字段可两个
            return this.page(page, queryWrapper);
        } catch (Exception e) {
            log.error("获取字典信息失败", e);
            return null;
        }
    }

    @Override
    public IPage<YbDrgApplyTask> findYbDrgApplyTaskList(QueryRequest request, YbDrgApplyTask ybDrgApplyTask) {
        try {
            Page<YbDrgApplyTask> page = new Page<>();
            SortUtil.handlePageSort(request, page, false);//true 是属性  false是数据库字段可两个
            return this.baseMapper.findYbDrgApplyTask(page, ybDrgApplyTask);
        } catch (Exception e) {
            log.error("获取失败", e);
            return null;
        }
    }

    @Override
    @Transactional
    public void createYbDrgApplyTask(YbDrgApplyTask ybDrgApplyTask) {
        ybDrgApplyTask.setId(UUID.randomUUID().toString());
//        ybDrgApplyTask.setCreateTime(new Date());
//        ybDrgApplyTask.setIsDeletemark(1);
        this.save(ybDrgApplyTask);
    }

    @Override
    @Transactional
    public void updateYbDrgApplyTask(YbDrgApplyTask ybDrgApplyTask) {
//        ybDrgApplyTask.setModifyTime(new Date());
        this.baseMapper.updateYbDrgApplyTask(ybDrgApplyTask);
    }

    @Override
    @Transactional
    public void deleteYbDrgApplyTasks(String[] Ids) {
        List<String> list = Arrays.asList(Ids);
        this.baseMapper.deleteBatchIds(list);
    }


}