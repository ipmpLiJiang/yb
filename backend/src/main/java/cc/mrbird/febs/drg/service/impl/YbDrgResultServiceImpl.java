package cc.mrbird.febs.drg.service.impl;

import cc.mrbird.febs.common.domain.QueryRequest;
import cc.mrbird.febs.common.utils.SortUtil;
import cc.mrbird.febs.drg.entity.YbDrgResult;
import cc.mrbird.febs.drg.dao.YbDrgResultMapper;
import cc.mrbird.febs.drg.service.IYbDrgResultService;
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
@Service("IYbDrgResultService")
@Transactional(propagation = Propagation.SUPPORTS, readOnly = true, rollbackFor = Exception.class)
public class YbDrgResultServiceImpl extends ServiceImpl<YbDrgResultMapper, YbDrgResult> implements IYbDrgResultService {


    @Override
    public IPage<YbDrgResult> findYbDrgResults(QueryRequest request, YbDrgResult ybDrgResult) {
        try {
            LambdaQueryWrapper<YbDrgResult> queryWrapper = new LambdaQueryWrapper<>();
//            queryWrapper.eq(YbDrgResult::getIsDeletemark, 1);//1是未删 0是已删


            Page<YbDrgResult> page = new Page<>();
            SortUtil.handlePageSort(request, page, false);//true 是属性  false是数据库字段可两个
            return this.page(page, queryWrapper);
        } catch (Exception e) {
            log.error("获取字典信息失败", e);
            return null;
        }
    }

    @Override
    public IPage<YbDrgResult> findYbDrgResultList(QueryRequest request, YbDrgResult ybDrgResult) {
        try {
            Page<YbDrgResult> page = new Page<>();
            SortUtil.handlePageSort(request, page, false);//true 是属性  false是数据库字段可两个
            return this.baseMapper.findYbDrgResult(page, ybDrgResult);
        } catch (Exception e) {
            log.error("获取失败", e);
            return null;
        }
    }

    @Override
    @Transactional
    public void createYbDrgResult(YbDrgResult ybDrgResult) {
        ybDrgResult.setId(UUID.randomUUID().toString());
//        ybDrgResult.setCreateTime(new Date());
//        ybDrgResult.setIsDeletemark(1);
        this.save(ybDrgResult);
    }

    @Override
    @Transactional
    public void updateYbDrgResult(YbDrgResult ybDrgResult) {
//        ybDrgResult.setModifyTime(new Date());
        this.baseMapper.updateYbDrgResult(ybDrgResult);
    }

    @Override
    @Transactional
    public void deleteYbDrgResults(String[] Ids) {
        List<String> list = Arrays.asList(Ids);
        this.baseMapper.deleteBatchIds(list);
    }

    @Override
    public List<YbDrgResult> findDrgResultList(YbDrgResult drgResult) {
        LambdaQueryWrapper<YbDrgResult> wrapper = new LambdaQueryWrapper<>();
        if (drgResult.getApplyDateStr() != null) {
            wrapper.eq(YbDrgResult::getApplyDateStr, drgResult.getApplyDateStr());
        }

        if (drgResult.getAreaType() != null) {
            wrapper.eq(YbDrgResult::getAreaType, drgResult.getAreaType());
        }

        if (drgResult.getState() != null) {
            wrapper.eq(YbDrgResult::getState, drgResult.getState());
        }

        if (drgResult.getOrderNumber() != null) {
            wrapper.eq(YbDrgResult::getOrderNumber, drgResult.getOrderNumber());
        }

        if (drgResult.getOrderNum() != null) {
            wrapper.eq(YbDrgResult::getOrderNum, drgResult.getOrderNum());
        }

        if (drgResult.getApplyDataId() != null) {
            wrapper.eq(YbDrgResult::getApplyDataId, drgResult.getApplyDataId());
        }

        if (drgResult.getId() != null) {
            wrapper.eq(YbDrgResult::getId, drgResult.getId());
        }

        if (drgResult.getDoctorCode() != null) {
            wrapper.eq(YbDrgResult::getDoctorCode, drgResult.getDoctorCode());
        }

        if (drgResult.getDoctorName() != null) {
            wrapper.eq(YbDrgResult::getDoctorName, drgResult.getDoctorName());
        }

        if (drgResult.getDksId() != null) {
            wrapper.eq(YbDrgResult::getDksId, drgResult.getDksId());
        }

        if (drgResult.getDksName() != null) {
            wrapper.eq(YbDrgResult::getDksName, drgResult.getDksName());
        }

        return this.list(wrapper);
    }



}