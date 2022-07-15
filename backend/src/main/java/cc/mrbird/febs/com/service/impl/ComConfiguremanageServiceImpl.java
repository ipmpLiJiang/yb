package cc.mrbird.febs.com.service.impl;

import cc.mrbird.febs.com.manager.ComConfigureManager;
import cc.mrbird.febs.common.domain.QueryRequest;
import cc.mrbird.febs.common.utils.SortUtil;
import cc.mrbird.febs.com.entity.ComConfiguremanage;
import cc.mrbird.febs.com.dao.ComConfiguremanageMapper;
import cc.mrbird.febs.com.service.IComConfiguremanageService;
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

import java.util.*;
import java.time.LocalDate;
import java.util.stream.Collectors;

/**
 * <p>
 * 服务实现类
 * </p>
 *
 * @author viki
 * @since 2020-08-05
 */
@Slf4j
@Service("IComConfiguremanageService")
@Transactional(propagation = Propagation.SUPPORTS, readOnly = true, rollbackFor = Exception.class)
public class ComConfiguremanageServiceImpl extends ServiceImpl<ComConfiguremanageMapper, ComConfiguremanage> implements IComConfiguremanageService {

    @Autowired
    ComConfigureManager configureManager;
    @Override
    public IPage<ComConfiguremanage> findComConfiguremanages(QueryRequest request, ComConfiguremanage comConfiguremanage) {
        try {
            LambdaQueryWrapper<ComConfiguremanage> queryWrapper = new LambdaQueryWrapper<>();
            queryWrapper.eq(ComConfiguremanage::getIsDeletemark, 1);//1是未删 0是已删


            Page<ComConfiguremanage> page = new Page<>();
            SortUtil.handlePageSort(request, page, false);//true 是属性  false是数据库字段可两个
            return this.page(page, queryWrapper);
        } catch (Exception e) {
            log.error("获取字典信息失败", e);
            return null;
        }
    }

    @Override
    public IPage<ComConfiguremanage> findComConfiguremanageList(QueryRequest request, ComConfiguremanage comConfiguremanage) {
        try {
            Page<ComConfiguremanage> page = new Page<>();
            SortUtil.handlePageSort(request, page, false);//true 是属性  false是数据库字段可两个
            return this.baseMapper.findComConfiguremanage(page, comConfiguremanage);
        } catch (Exception e) {
            log.error("获取失败", e);
            return null;
        }
    }

    @Override
    @Transactional
    public void createComConfiguremanage(ComConfiguremanage comConfiguremanage) {
        comConfiguremanage.setCreateTime(new Date());
        comConfiguremanage.setIsDeletemark(1);
        this.save(comConfiguremanage);
    }

    @Override
    @Transactional
    public void updateComConfiguremanage(ComConfiguremanage comConfiguremanage) {
        comConfiguremanage.setModifyTime(new Date());
        this.baseMapper.updateComConfiguremanage(comConfiguremanage);
    }

    @Override
    @Transactional
    public void deleteComConfiguremanages(String[] Ids) {
        List<String> list = Arrays.asList(Ids);
        this.baseMapper.deleteBatchIds(list);
    }

    @Override
    public List<ComConfiguremanage> getConfigLists(List<Integer> ctypeList) {
        LambdaQueryWrapper<ComConfiguremanage> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.in(ComConfiguremanage::getConfigureType,ctypeList);
        List<ComConfiguremanage> configList = this.list(queryWrapper);
        return configList;
    }

    @Override
    public List<ComConfiguremanage> getConfigLists(int cType) {
        List<Integer> ctypeList = new ArrayList<>();
        ctypeList.add(cType);
        return this.getConfigLists(ctypeList);
    }

    @Override
    public String getConfigAreaName(int areaType){
        int ctype = 5; // 院区
        List<ComConfiguremanage> configList = new ArrayList<>();
        configList = configureManager.getConfigures(ctype,"area");
        if (configList.size() == 0) {
            LambdaQueryWrapper<ComConfiguremanage> queryWrapper = new LambdaQueryWrapper<>();
            queryWrapper.eq(ComConfiguremanage::getConfigureType, ctype);
            configList = this.list(queryWrapper);
        }
        if (configList.size() > 0) {
            configList = configList.stream().filter(s->s.getIntField().equals(areaType)).collect(Collectors.toList());
            if (configList.size() > 0) {
                return configList.get(0).getStringField();
            }
        }
        return "";
    }

    @Override
    public int getConfigDay() {
        List<Integer> intList = new ArrayList<>();
        intList.add(1);//日期增加天数
        List<ComConfiguremanage> configList = this.getConfigLists(intList);
        return configList.size() > 0 ? configList.get(0).getIntField() : 2;
    }


}