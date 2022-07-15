package cc.mrbird.febs.chs.service.impl;

import cc.mrbird.febs.common.domain.QueryRequest;
import cc.mrbird.febs.common.utils.SortUtil;
import cc.mrbird.febs.chs.entity.YbChsPriorityLevel;
import cc.mrbird.febs.chs.dao.YbChsPriorityLevelMapper;
import cc.mrbird.febs.chs.service.IYbChsPriorityLevelService;
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
 * @since 2022-07-13
 */
@Slf4j
@Service("IYbChsPriorityLevelService")
@Transactional(propagation = Propagation.SUPPORTS, readOnly = true, rollbackFor = Exception.class)
public class YbChsPriorityLevelServiceImpl extends ServiceImpl<YbChsPriorityLevelMapper, YbChsPriorityLevel> implements IYbChsPriorityLevelService {


    @Override
    public IPage<YbChsPriorityLevel> findYbChsPriorityLevels(QueryRequest request, YbChsPriorityLevel ybChsPriorityLevel) {
        try {
            LambdaQueryWrapper<YbChsPriorityLevel> queryWrapper = new LambdaQueryWrapper<>();
            queryWrapper.eq(YbChsPriorityLevel::getIsDeletemark, 1);//1是未删 0是已删
            if (ybChsPriorityLevel.getAreaType() != null) {
                queryWrapper.eq(YbChsPriorityLevel::getAreaType, ybChsPriorityLevel.getAreaType());
            }
            if (ybChsPriorityLevel.getState() != null) {
                queryWrapper.eq(YbChsPriorityLevel::getState, ybChsPriorityLevel.getState());
            }
            if (ybChsPriorityLevel.getZymzType() != null) {
                queryWrapper.eq(YbChsPriorityLevel::getZymzType, ybChsPriorityLevel.getZymzType());
            }

            if (ybChsPriorityLevel.getPlType() != null) {
                queryWrapper.eq(YbChsPriorityLevel::getPlType, ybChsPriorityLevel.getPlType());
            }

            if (StringUtils.isNotBlank(ybChsPriorityLevel.getCurrencyField())) {
//                queryWrapper.like(YbChsPriorityLevel::getRuleName, ybChsPriorityLevel.getCurrencyField());
//                queryWrapper.like(YbChsPriorityLevel::getProjectName, ybChsPriorityLevel.getCurrencyField());
                String sql = "(ruleName like '%" + ybChsPriorityLevel.getCurrencyField() + "%' or projectName like '%" + ybChsPriorityLevel.getCurrencyField() + "%')";
                queryWrapper.apply(sql);
            }
            Page<YbChsPriorityLevel> page = new Page<>();
            SortUtil.handlePageSort(request, page, false);//true 是属性  false是数据库字段可两个
            return this.page(page, queryWrapper);
        } catch (Exception e) {
            log.error("获取字典信息失败", e);
            return null;
        }
    }

    @Override
    public IPage<YbChsPriorityLevel> findYbChsPriorityLevelList(QueryRequest request, YbChsPriorityLevel ybChsPriorityLevel) {
        try {
            Page<YbChsPriorityLevel> page = new Page<>();
            SortUtil.handlePageSort(request, page, false);//true 是属性  false是数据库字段可两个
            return this.baseMapper.findYbChsPriorityLevel(page, ybChsPriorityLevel);
        } catch (Exception e) {
            log.error("获取失败", e);
            return null;
        }
    }

    @Override
    @Transactional
    public void createYbChsPriorityLevel(YbChsPriorityLevel ybChsPriorityLevel) {
        ybChsPriorityLevel.setCreateTime(new Date());
        ybChsPriorityLevel.setIsDeletemark(1);
        this.save(ybChsPriorityLevel);
    }

    @Override
    @Transactional
    public void updateYbChsPriorityLevel(YbChsPriorityLevel ybChsPriorityLevel) {
        ybChsPriorityLevel.setModifyTime(new Date());
        this.baseMapper.updateYbChsPriorityLevel(ybChsPriorityLevel);
    }

    @Override
    @Transactional
    public void deleteYbChsPriorityLevels(String[] Ids) {
        List<String> list = Arrays.asList(Ids);
        this.baseMapper.deleteBatchIds(list);
    }


}