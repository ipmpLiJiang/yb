package cc.mrbird.febs.chs.service.impl;

import cc.mrbird.febs.common.domain.QueryRequest;
import cc.mrbird.febs.common.utils.SortUtil;
import cc.mrbird.febs.chs.entity.YbChsManage;
import cc.mrbird.febs.chs.dao.YbChsManageMapper;
import cc.mrbird.febs.chs.service.IYbChsManageService;
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
 * @since 2022-06-20
 */
@Slf4j
@Service("IYbChsManageService")
@Transactional(propagation = Propagation.SUPPORTS, readOnly = true, rollbackFor = Exception.class)
public class YbChsManageServiceImpl extends ServiceImpl<YbChsManageMapper, YbChsManage> implements IYbChsManageService {


    @Override
    public IPage<YbChsManage> findYbChsManages(QueryRequest request, YbChsManage ybChsManage) {
        try {
            LambdaQueryWrapper<YbChsManage> queryWrapper = new LambdaQueryWrapper<>();
//            queryWrapper.eq(YbChsManage::getIsDeletemark, 1);//1是未删 0是已删


            Page<YbChsManage> page = new Page<>();
            SortUtil.handlePageSort(request, page, false);//true 是属性  false是数据库字段可两个
            return this.page(page, queryWrapper);
        } catch (Exception e) {
            log.error("获取字典信息失败", e);
            return null;
        }
    }

    @Override
    public IPage<YbChsManage> findYbChsManageList(QueryRequest request, YbChsManage ybChsManage) {
        try {
            Page<YbChsManage> page = new Page<>();
            SortUtil.handlePageSort(request, page, false);//true 是属性  false是数据库字段可两个
            return this.baseMapper.findYbChsManage(page, ybChsManage);
        } catch (Exception e) {
            log.error("获取失败", e);
            return null;
        }
    }

    @Override
    @Transactional
    public void createYbChsManage(YbChsManage ybChsManage) {
        ybChsManage.setId(UUID.randomUUID().toString());
//        ybChsManage.setCreateTime(new Date());
//        ybChsManage.setIsDeletemark(1);
        this.save(ybChsManage);
    }

    @Override
    @Transactional
    public void updateYbChsManage(YbChsManage ybChsManage) {
//        ybChsManage.setModifyTime(new Date());
        this.baseMapper.updateYbChsManage(ybChsManage);
    }

    @Override
    @Transactional
    public void deleteYbChsManages(String[] Ids) {
        List<String> list = Arrays.asList(Ids);
        this.baseMapper.deleteBatchIds(list);
    }

    @Override
    public List<YbChsManage> findChsManageList(YbChsManage ybChsManage) {
        LambdaQueryWrapper<YbChsManage> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(YbChsManage::getApplyDateStr, ybChsManage.getApplyDateStr());
        queryWrapper.eq(YbChsManage::getAreaType, ybChsManage.getAreaType());

        if (ybChsManage.getState() != null) {
            queryWrapper.eq(YbChsManage::getState, ybChsManage.getState());
        }
        if (ybChsManage.getReadyDoctorCode() != null) {
            queryWrapper.eq(YbChsManage::getReadyDoctorCode, ybChsManage.getReadyDoctorCode());
        }
        if (ybChsManage.getReadyDksId() != null) {
            queryWrapper.eq(YbChsManage::getReadyDksId, ybChsManage.getReadyDksId());
        }
        if (ybChsManage.getReadyDksName() != null) {
            queryWrapper.eq(YbChsManage::getReadyDksName, ybChsManage.getReadyDksName());
        }
        if (ybChsManage.getOrderNum() != null) {
            queryWrapper.eq(YbChsManage::getOrderNum, ybChsManage.getOrderNum());
        }
        return this.list(queryWrapper);
    }


}