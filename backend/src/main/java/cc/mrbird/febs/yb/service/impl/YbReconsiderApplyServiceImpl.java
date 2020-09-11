package cc.mrbird.febs.yb.service.impl;

import cc.mrbird.febs.common.domain.QueryRequest;
import cc.mrbird.febs.common.utils.SortUtil;
import cc.mrbird.febs.yb.entity.YbReconsiderApply;
import cc.mrbird.febs.yb.dao.YbReconsiderApplyMapper;
import cc.mrbird.febs.yb.entity.YbReconsiderApplyData;
import cc.mrbird.febs.yb.service.IYbReconsiderApplyService;
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
 * @since 2020-07-23
 */
@Slf4j
@Service("IYbReconsiderApplyService")
@Transactional(propagation = Propagation.SUPPORTS, readOnly = true, rollbackFor = Exception.class)
public class YbReconsiderApplyServiceImpl extends ServiceImpl<YbReconsiderApplyMapper, YbReconsiderApply> implements IYbReconsiderApplyService {


    @Override
    public IPage<YbReconsiderApply> findYbReconsiderApplys(QueryRequest request, YbReconsiderApply ybReconsiderApply) {
        try {
            LambdaQueryWrapper<YbReconsiderApply> queryWrapper = new LambdaQueryWrapper<>();
            queryWrapper.eq(YbReconsiderApply::getIsDeletemark, 1);//1是未删 0是已删

            if (StringUtils.isNotBlank(ybReconsiderApply.getCreateTimeFrom()) && StringUtils.isNotBlank(ybReconsiderApply.getCreateTimeTo())) {
                queryWrapper
                        .ge(YbReconsiderApply::getCreateTime, ybReconsiderApply.getCreateTimeFrom())
                        .le(YbReconsiderApply::getCreateTime, ybReconsiderApply.getCreateTimeTo());
            }

            Page<YbReconsiderApply> page = new Page<>();
            SortUtil.handlePageSort(request, page, false);//true 是属性  false是数据库字段可两个
            return this.page(page, queryWrapper);
        } catch (Exception e) {
            log.error("获取字典信息失败", e);
            return null;
        }
    }

    @Override
    public IPage<YbReconsiderApply> findYbReconsiderApplyList(QueryRequest request, YbReconsiderApply ybReconsiderApply) {
        try {
            Page<YbReconsiderApply> page = new Page<>();
            SortUtil.handlePageSort(request, page, false);//true 是属性  false是数据库字段可两个
            return this.baseMapper.findYbReconsiderApply(page, ybReconsiderApply);
        } catch (Exception e) {
            log.error("获取失败", e);
            return null;
        }
    }

    @Override
    @Transactional
    public void createYbReconsiderApply(YbReconsiderApply ybReconsiderApply) {
        ybReconsiderApply.setCreateTime(new Date());
        if(ybReconsiderApply.getId() == null || "".equals(ybReconsiderApply.getId())) {
            ybReconsiderApply.setId(UUID.randomUUID().toString());
        }
        ybReconsiderApply.setIsDeletemark(1);
        this.save(ybReconsiderApply);
    }

    @Override
    @Transactional
    public void updateYbReconsiderApply(YbReconsiderApply ybReconsiderApply) {
        ybReconsiderApply.setModifyTime(new Date());
        this.baseMapper.updateYbReconsiderApply(ybReconsiderApply);
    }

    @Override
    @Transactional
    public void deleteYbReconsiderApplys(String[] Ids) {
        List<String> list = Arrays.asList(Ids);
        this.baseMapper.deleteBatchIds(list);
    }

    @Override
    @Transactional
    public void deleteBatchStateIdsYbReconsiderApplys(String[] Ids, Integer state) {
        List<String> listString = Arrays.asList(Ids);
        this.baseMapper.deleteBatchStateIdsYbReconsiderApply(listString, state);
    }




}