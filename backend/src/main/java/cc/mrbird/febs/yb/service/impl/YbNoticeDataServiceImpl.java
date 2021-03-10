package cc.mrbird.febs.yb.service.impl;

import cc.mrbird.febs.common.domain.QueryRequest;
import cc.mrbird.febs.common.utils.SortUtil;
import cc.mrbird.febs.yb.entity.YbNoticeData;
import cc.mrbird.febs.yb.dao.YbNoticeDataMapper;
import cc.mrbird.febs.yb.service.IYbNoticeDataService;
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
 * @since 2021-03-08
 */
@Slf4j
@Service("IYbNoticeDataService")
@Transactional(propagation = Propagation.SUPPORTS, readOnly = true, rollbackFor = Exception.class)
public class YbNoticeDataServiceImpl extends ServiceImpl<YbNoticeDataMapper, YbNoticeData> implements IYbNoticeDataService {


    @Override
    public IPage<YbNoticeData> findYbNoticeDatas(QueryRequest request, YbNoticeData ybNoticeData) {
        try {
            LambdaQueryWrapper<YbNoticeData> queryWrapper = new LambdaQueryWrapper<>();
//        queryWrapper.eq(YbNoticeData::getIsDeletemark, 1);//1是未删 0是已删


            Page<YbNoticeData> page = new Page<>();
            SortUtil.handlePageSort(request, page, false);//true 是属性  false是数据库字段可两个
            return this.page(page, queryWrapper);
        } catch (Exception e) {
            log.error("获取字典信息失败", e);
            return null;
        }
    }

    @Override
    public IPage<YbNoticeData> findYbNoticeDataList(QueryRequest request, YbNoticeData ybNoticeData) {
        try {
            Page<YbNoticeData> page = new Page<>();
            SortUtil.handlePageSort(request, page, false);//true 是属性  false是数据库字段可两个
            return this.baseMapper.findYbNoticeData(page, ybNoticeData);
        } catch (Exception e) {
            log.error("获取失败", e);
            return null;
        }
    }

    @Override
    @Transactional
    public void createYbNoticeData(YbNoticeData ybNoticeData) {
//                ybNoticeData.setCreateTime(new Date());
//        ybNoticeData.setIsDeletemark(1);
        this.save(ybNoticeData);
    }

    @Override
    @Transactional
    public void updateYbNoticeData(YbNoticeData ybNoticeData) {
//        ybNoticeData.setModifyTime(new Date());
        this.baseMapper.updateYbNoticeData(ybNoticeData);
    }

    @Override
    @Transactional
    public void deleteYbNoticeDatas(String[] Ids) {
        List<String> list = Arrays.asList(Ids);
        this.baseMapper.deleteBatchIds(list);
    }

    @Override
    public List<YbNoticeData> findNoticeDataList(YbNoticeData ybNoticeData) {
        LambdaQueryWrapper<YbNoticeData> wrapper = new LambdaQueryWrapper<>();
        if (ybNoticeData.getId() != null) {
            wrapper.eq(YbNoticeData::getId, ybNoticeData.getId());
        }
        if (ybNoticeData.getPid() != null) {
            wrapper.eq(YbNoticeData::getPid, ybNoticeData.getPid());
        } else {
            wrapper.eq(YbNoticeData::getPid, null);
        }
        if (ybNoticeData.getNdType() != null) {
            wrapper.eq(YbNoticeData::getNdType, ybNoticeData.getNdType());
        }
        return this.list(wrapper);
    }


}