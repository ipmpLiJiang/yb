package cc.mrbird.febs.yb.service.impl;

import cc.mrbird.febs.common.domain.QueryRequest;
import cc.mrbird.febs.common.utils.SortUtil;
import cc.mrbird.febs.yb.dao.YbReconsiderApplyMapper;
import cc.mrbird.febs.yb.entity.YbReconsiderApply;
import cc.mrbird.febs.yb.entity.YbReconsiderApplyData;
import cc.mrbird.febs.yb.dao.YbReconsiderApplyDataMapper;
import cc.mrbird.febs.yb.service.IYbReconsiderApplyDataService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.google.common.collect.Lists;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.Date;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;
import java.time.LocalDate;

import static jdk.nashorn.internal.runtime.regexp.joni.Syntax.Java;

/**
 * <p>
 * 服务实现类
 * </p>
 *
 * @author viki
 * @since 2020-07-17
 */
@Slf4j
@Service("IYbReconsiderApplyDataService")
@Transactional(propagation = Propagation.SUPPORTS, readOnly = true, rollbackFor = Exception.class)
public class YbReconsiderApplyDataServiceImpl extends ServiceImpl<YbReconsiderApplyDataMapper, YbReconsiderApplyData> implements IYbReconsiderApplyDataService {

    @Autowired
    public YbReconsiderApplyMapper ybReconsiderApplyMapper;

    @Override
    public IPage<YbReconsiderApplyData> findYbReconsiderApplyDatas(QueryRequest request, YbReconsiderApplyData ybReconsiderApplyData) {
        try {
            LambdaQueryWrapper<YbReconsiderApplyData> queryWrapper = new LambdaQueryWrapper<>();
            queryWrapper.eq(YbReconsiderApplyData::getIsDeletemark, 1);//1是未删 0是已删


            Page<YbReconsiderApplyData> page = new Page<>();
            SortUtil.handlePageSort(request, page, false);//true 是属性  false是数据库字段可两个
            return this.page(page, queryWrapper);
        } catch (Exception e) {
            log.error("获取字典信息失败", e);
            return null;
        }
    }

    @Override
    public IPage<YbReconsiderApplyData> findYbReconsiderApplyDataList(QueryRequest request, YbReconsiderApplyData ybReconsiderApplyData) {
        try {
            Page<YbReconsiderApplyData> page = new Page<>();
            SortUtil.handlePageSort(request, page, false);//true 是属性  false是数据库字段可两个
            return this.baseMapper.findYbReconsiderApplyData(page, ybReconsiderApplyData);
        } catch (Exception e) {
            log.error("获取失败", e);
            return null;
        }
    }

    @Override
    @Transactional
    public void createYbReconsiderApplyData(YbReconsiderApplyData ybReconsiderApplyData) {
        ybReconsiderApplyData.setCreateTime(new Date());
        if(ybReconsiderApplyData.getId() == null || "".equals(ybReconsiderApplyData.getId())) {
            ybReconsiderApplyData.setId(UUID.randomUUID().toString());
        }
        ybReconsiderApplyData.setIsDeletemark(1);
        this.save(ybReconsiderApplyData);
    }

    @Override
    @Transactional
    public void createBatchYbReconsiderApplyData(List<YbReconsiderApplyData> list,YbReconsiderApply ybReconsiderApply) {

        this.saveBatch(list);
//        for(YbReconsiderApplyData item : list)
//        {
//            this.save(item);
//        }
//        this.baseMapper.createBatchData(list);
        ybReconsiderApplyMapper.updateYbReconsiderApply(ybReconsiderApply);
    }

    @Override
    @Transactional
    public void updateYbReconsiderApplyData(YbReconsiderApplyData ybReconsiderApplyData) {
        ybReconsiderApplyData.setModifyTime(new Date());
        this.baseMapper.updateYbReconsiderApplyData(ybReconsiderApplyData);
    }

    @Override
    @Transactional
    public void deleteYbReconsiderApplyDatas(String[] Ids) {
        List<String> list = Arrays.asList(Ids);
        this.baseMapper.deleteBatchIds(list);
    }

    @Override
    public List<YbReconsiderApplyData> findReconsiderApplyDataByApplyDates(String applyDateStr, Integer dataType) {
        return  this.baseMapper.findReconsiderApplyDataByApplyDate(applyDateStr,dataType);
    }

    @Override
    @Transactional
    public void createBatchDatas(List<YbReconsiderApplyData> listReconsiderApplyData) {
        this.baseMapper.createBatchData(listReconsiderApplyData);
    }
    @Override
    @Transactional
    public void importReconsiderApply(YbReconsiderApply ybReconsiderApply, List<YbReconsiderApplyData> listData, List<YbReconsiderApplyData> listMain) {
        this.ybReconsiderApplyMapper.updateYbReconsiderApply(ybReconsiderApply);
        if (listData.size() > 0) {
            this.saveBatch(listData);
        }
        if (listMain.size() > 0) {
            this.saveBatch(listMain);
        }
    }

}