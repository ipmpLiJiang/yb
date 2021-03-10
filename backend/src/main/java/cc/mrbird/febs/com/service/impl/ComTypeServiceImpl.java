package cc.mrbird.febs.com.service.impl;

import cc.mrbird.febs.common.domain.QueryRequest;
import cc.mrbird.febs.common.utils.SortUtil;
import cc.mrbird.febs.com.entity.ComType;
import cc.mrbird.febs.com.dao.ComTypeMapper;
import cc.mrbird.febs.com.service.IComTypeService;
import cc.mrbird.febs.system.domain.User;
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
 * @since 2021-03-05
 */
@Slf4j
@Service("IComTypeService")
@Transactional(propagation = Propagation.SUPPORTS, readOnly = true, rollbackFor = Exception.class)
public class ComTypeServiceImpl extends ServiceImpl<ComTypeMapper, ComType> implements IComTypeService {


    @Override
    public IPage<ComType> findComTypes(QueryRequest request, ComType comType) {
        try {
            LambdaQueryWrapper<ComType> queryWrapper = new LambdaQueryWrapper<>();
            queryWrapper.eq(ComType::getIsDeletemark, 1);//1是未删 0是已删

            if (comType.getCtType() != null) {
                queryWrapper.eq(ComType::getCtType, comType.getCtType());
            } else {
                queryWrapper.eq(ComType::getId, 0);
            }

            if (StringUtils.isNotBlank(comType.getCurrencyField())) {
                queryWrapper.like(ComType::getCurrencyField, comType.getCurrencyField());
            }

            Page<ComType> page = new Page<>();
            SortUtil.handlePageSort(request, page, false);//true 是属性  false是数据库字段可两个
            return this.page(page, queryWrapper);
        } catch (Exception e) {
            log.error("获取字典信息失败", e);
            return null;
        }
    }

    @Override
    public IPage<ComType> findComTypeList(QueryRequest request, ComType comType) {
        try {
            Page<ComType> page = new Page<>();
            SortUtil.handlePageSort(request, page, false);//true 是属性  false是数据库字段可两个
            return this.baseMapper.findComType(page, comType);
        } catch (Exception e) {
            log.error("获取失败", e);
            return null;
        }
    }

    @Override
    @Transactional
    public void createComType(ComType comType) {
        comType.setCreateTime(new Date());
        comType.setIsDeletemark(1);
        this.save(comType);
    }

    @Override
    @Transactional
    public void updateComType(ComType comType) {
        comType.setModifyTime(new Date());
        this.baseMapper.updateComType(comType);
    }

    @Override
    @Transactional
    public void editComType(ComType comType, User currentUser) {
        Date thisDate = new Date();
        if (comType.getId() == null) {
            comType.setIsDeletemark(1);
            comType.setCreateTime(thisDate);
            comType.setCreateUserId(currentUser.getUserId());
            this.save(comType);
        } else {
            comType.setModifyTime(thisDate);
            comType.setModifyUserId(currentUser.getUserId());
            this.baseMapper.updateComType(comType);
        }
    }

    @Override
    public List<ComType> findComTypeList(ComType comType) {
        LambdaQueryWrapper<ComType> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(ComType::getIsDeletemark, 1);//1是未删 0是已删

        if (comType.getCtType() != null) {
            queryWrapper.eq(ComType::getCtType, comType.getCtType());
        } else {
            queryWrapper.eq(ComType::getId, 0);
        }
        if (comType.getCtName() != null) {
            queryWrapper.eq(ComType::getCtName, comType.getCtName());
        }
        if (comType.getCurrencyField() != null) {
            queryWrapper.like(ComType::getCtName, comType.getCurrencyField());
        }
        return this.list(queryWrapper);
    }

    @Override
    @Transactional
    public void deleteComTypes(String[] Ids) {
        List<String> list = Arrays.asList(Ids);
        this.baseMapper.deleteBatchIds(list);
    }


}