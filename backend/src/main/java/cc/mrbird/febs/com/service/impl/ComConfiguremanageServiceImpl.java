package cc.mrbird.febs.com.service.impl;

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
 * @since 2020-08-05
 */
@Slf4j
@Service("IComConfiguremanageService")
@Transactional(propagation = Propagation.SUPPORTS, readOnly = true, rollbackFor = Exception.class)
public class ComConfiguremanageServiceImpl extends ServiceImpl<ComConfiguremanageMapper, ComConfiguremanage> implements IComConfiguremanageService {


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
    public int getVerifyDay(int day) {
        LambdaQueryWrapper<ComConfiguremanage> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(ComConfiguremanage::getConfigureType, 1);
        List<ComConfiguremanage> listFile = this.list(queryWrapper);
        if (listFile.size() > 0) {
            int intField = listFile.get(0).getIntField();
            if (intField > 0) {
                day = intField;
            }
        }
        return day;
    }


}