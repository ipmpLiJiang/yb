package cc.mrbird.febs.yb.service.impl;

import cc.mrbird.febs.common.domain.QueryRequest;
import cc.mrbird.febs.common.utils.SortUtil;
import cc.mrbird.febs.yb.entity.*;
import cc.mrbird.febs.yb.dao.YbAppealSumdeptMapper;
import cc.mrbird.febs.yb.service.IYbAppealSumdeptDataService;
import cc.mrbird.febs.yb.service.IYbAppealSumdeptService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
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
 * @since 2021-03-08
 */
@Slf4j
@Service("IYbAppealSumdeptService")
@Transactional(propagation = Propagation.SUPPORTS, readOnly = true, rollbackFor = Exception.class)
public class YbAppealSumdeptServiceImpl extends ServiceImpl<YbAppealSumdeptMapper, YbAppealSumdept> implements IYbAppealSumdeptService {

    @Autowired
    IYbAppealSumdeptDataService iYbAppealSumdeptDataService;

    @Override
    public IPage<YbAppealSumdept> findYbAppealSumdepts(QueryRequest request, YbAppealSumdept ybAppealSumdept) {
        try {
            LambdaQueryWrapper<YbAppealSumdept> queryWrapper = new LambdaQueryWrapper<>();
            queryWrapper.eq(YbAppealSumdept::getIsDeletemark, 1);//1是未删 0是已删

            if (StringUtils.isNotBlank(ybAppealSumdept.getCurrencyField())) {
                queryWrapper.like(YbAppealSumdept::getCurrencyField, ybAppealSumdept.getCurrencyField());
            }

            Page<YbAppealSumdept> page = new Page<>();
            SortUtil.handlePageSort(request, page, false);//true 是属性  false是数据库字段可两个
            return this.page(page, queryWrapper);
        } catch (Exception e) {
            log.error("获取字典信息失败", e);
            return null;
        }
    }

    @Override
    public IPage<YbAppealSumdept> findAppealSumdeptView(QueryRequest request, YbAppealSumdept ybAppealSumdept) {
        try {
            Page<YbAppealSumdept> page = new Page<>();
            int count = this.baseMapper.findAppealSumdeptCount(ybAppealSumdept);
            if (count > 0) {
                page.setSearchCount(false);
                SortUtil.handlePageSort(request, page, false);//true 是属性  false是数据库字段可两个
                IPage<YbAppealSumdept> pg = this.baseMapper.findAppealSumdeptView(page, ybAppealSumdept);
                pg.setTotal(count);
                return pg;
            }
            return page;
        } catch (Exception e) {
            log.error("获取失败", e);
            return null;
        }
    }

    @Override
    public IPage<YbAppealSumdept> findYbAppealSumdeptList(QueryRequest request, YbAppealSumdept ybAppealSumdept) {
        try {
            Page<YbAppealSumdept> page = new Page<>();
            SortUtil.handlePageSort(request, page, false);//true 是属性  false是数据库字段可两个
            return this.baseMapper.findYbAppealSumdept(page, ybAppealSumdept);
        } catch (Exception e) {
            log.error("获取失败", e);
            return null;
        }
    }

    @Override
    @Transactional
    public void createYbAppealSumdept(YbAppealSumdept ybAppealSumdept) {
        ybAppealSumdept.setCreateTime(new Date());
        ybAppealSumdept.setIsDeletemark(1);
        this.save(ybAppealSumdept);
    }

    @Override
    @Transactional
    public void updateYbAppealSumdept(YbAppealSumdept ybAppealSumdept) {
        ybAppealSumdept.setModifyTime(new Date());
        this.baseMapper.updateYbAppealSumdept(ybAppealSumdept);
    }

    @Override
    @Transactional
    public void deleteYbAppealSumdepts(String[] Ids) {
        List<String> list = Arrays.asList(Ids);
        this.iYbAppealSumdeptDataService.deleteAppealSumdeptDataPids(Ids);
        this.baseMapper.deleteBatchIds(list);
    }

    @Override
    @Transactional
    public void createAppealSumdept(YbAppealSumdept ybAppealSumdept, List<YbAppealSumdeptData> createDataList) {
        this.save(ybAppealSumdept);
        iYbAppealSumdeptDataService.saveBatch(createDataList);
    }

    @Override
    @Transactional
    public void updateAppealSumdept(YbAppealSumdept ybAppealSumdept, List<YbAppealSumdeptData> createDataList, List<YbAppealSumdeptData> updateDataList) {
        this.updateById(ybAppealSumdept);
        if (createDataList.size() > 0) {
            iYbAppealSumdeptDataService.saveBatch(createDataList);
        }
    }

    @Override
    public YbAppealSumdept findAppealSumdept(YbAppealSumdept appealSumdept) {
        LambdaQueryWrapper<YbAppealSumdept> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(YbAppealSumdept::getIsDeletemark, 1);
        if (appealSumdept.getId() != null) {
            wrapper.eq(YbAppealSumdept::getId, appealSumdept.getId());
        }
        if (appealSumdept.getAsCode() != null) {
            wrapper.eq(YbAppealSumdept::getAsCode, appealSumdept.getAsCode());
        }
        if (appealSumdept.getAsName() != null) {
            wrapper.eq(YbAppealSumdept::getAsName, appealSumdept.getAsName());
        }
        List<YbAppealSumdept> list = this.baseMapper.selectList(wrapper);
        if (list.size() > 0) {
            return list.get(0);
        } else {
            return null;
        }
    }


}