package cc.mrbird.febs.drg.service.impl;

import cc.mrbird.febs.common.domain.QueryRequest;
import cc.mrbird.febs.common.utils.SortUtil;
import cc.mrbird.febs.drg.entity.YbDrgDks;
import cc.mrbird.febs.drg.dao.YbDrgDksMapper;
import cc.mrbird.febs.drg.service.IYbDrgDksService;
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

import java.util.*;
import java.time.LocalDate;

/**
 * <p>
 * 服务实现类
 * </p>
 *
 * @author viki
 * @since 2022-03-16
 */
@Slf4j
@Service("IYbDrgDksService")
@Transactional(propagation = Propagation.SUPPORTS, readOnly = true, rollbackFor = Exception.class)
public class YbDrgDksServiceImpl extends ServiceImpl<YbDrgDksMapper, YbDrgDks> implements IYbDrgDksService {


    @Override
    public IPage<YbDrgDks> findYbDrgDkss(QueryRequest request, YbDrgDks ybDrgDks) {
        try {
            LambdaQueryWrapper<YbDrgDks> queryWrapper = new LambdaQueryWrapper<>();
//        queryWrapper.eq(YbDrgDks::getIsDeletemark, 1);//1是未删 0是已删

            if (StringUtils.isNotBlank(ybDrgDks.getCurrencyField())) {
                String sql = "dksId like '%"+ybDrgDks.getCurrencyField()+"%'";
                sql += "or dksName like '%"+ybDrgDks.getCurrencyField()+"%'";
                sql += "or areaName like '%"+ybDrgDks.getCurrencyField()+"%'";
                sql += "or spellCode like '%"+ybDrgDks.getCurrencyField()+"%'";
                queryWrapper.apply(sql);
            }

            Page<YbDrgDks> page = new Page<>();
            SortUtil.handlePageSort(request, page, false);//true 是属性  false是数据库字段可两个
            return this.page(page, queryWrapper);
        } catch (Exception e) {
            log.error("获取字典信息失败", e);
            return null;
        }
    }

    @Override
    public IPage<YbDrgDks> findYbDrgDksList(QueryRequest request, YbDrgDks ybDrgDks) {
        try {
            Page<YbDrgDks> page = new Page<>();
            SortUtil.handlePageSort(request, page, false);//true 是属性  false是数据库字段可两个
            return this.baseMapper.findYbDrgDks(page, ybDrgDks);
        } catch (Exception e) {
            log.error("获取失败", e);
            return null;
        }
    }

    @Override
    @Transactional
    public void createYbDrgDks(YbDrgDks ybDrgDks) {
//                ybDrgDks.setCreateTime(new Date());
//        ybDrgDks.setIsDeletemark(1);
        this.save(ybDrgDks);
    }

    @Override
    @Transactional
    public void updateYbDrgDks(YbDrgDks ybDrgDks) {
//        ybDrgDks.setModifyTime(new Date());
        this.baseMapper.updateYbDrgDks(ybDrgDks);
    }

    @Override
    @Transactional
    public void deleteYbDrgDkss(String[] Ids) {
        List<String> list = Arrays.asList(Ids);
        this.baseMapper.deleteBatchIds(list);
    }

    @Override
    public List<YbDrgDks> findDksList(YbDrgDks ybDrgDks, int type) {
        LambdaQueryWrapper<YbDrgDks> queryWrapper = new LambdaQueryWrapper<>();
        List<YbDrgDks> list = new ArrayList<>();
        if (type == 1) {
            String sql = "areaId = " + ybDrgDks.getAreaId();
            if (ybDrgDks.getComments() != null) {
                sql += " and (dksId like '%" + ybDrgDks.getComments() + "%' or dksName like '%" + ybDrgDks.getComments() + "%' or spellCode like '%" + ybDrgDks.getComments() + "%')";
            } else {
                sql += " and 1=2";
            }
            queryWrapper.apply(sql);
            list = this.list(queryWrapper);
            int count = 15;
            if (list.size() >= count) {
                list = list.subList(0, count);
            }
        } else {
            if (ybDrgDks.getAreaId() != null) {
                queryWrapper.eq(YbDrgDks::getAreaId, ybDrgDks.getAreaId());
            }
            if (ybDrgDks.getDksName() != null) {
                queryWrapper.eq(YbDrgDks::getDksName, ybDrgDks.getDksName());
            }
            if (ybDrgDks.getDksId() != null) {
                queryWrapper.eq(YbDrgDks::getDksId, ybDrgDks.getDksId());
            }
            list = this.list(queryWrapper);
        }
        return list;
    }

}