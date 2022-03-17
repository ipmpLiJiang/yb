package cc.mrbird.febs.drg.service.impl;

import cc.mrbird.febs.common.domain.QueryRequest;
import cc.mrbird.febs.common.utils.SortUtil;
import cc.mrbird.febs.drg.entity.YbDrgRelate;
import cc.mrbird.febs.drg.dao.YbDrgRelateMapper;
import cc.mrbird.febs.drg.service.IYbDrgRelateService;
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
 * @since 2022-03-16
 */
@Slf4j
@Service("IYbDrgRelateService")
@Transactional(propagation = Propagation.SUPPORTS, readOnly = true, rollbackFor = Exception.class)
public class YbDrgRelateServiceImpl extends ServiceImpl<YbDrgRelateMapper, YbDrgRelate> implements IYbDrgRelateService {


    @Override
    public IPage<YbDrgRelate> findYbDrgRelates(QueryRequest request, YbDrgRelate ybDrgRelate) {
        try {
            LambdaQueryWrapper<YbDrgRelate> queryWrapper = new LambdaQueryWrapper<>();
//        queryWrapper.eq(YbDrgRelate::getIsDeletemark, 1);//1是未删 0是已删

            if (StringUtils.isNotBlank(ybDrgRelate.getCurrencyField())) {
                queryWrapper.like(YbDrgRelate::getCurrencyField, ybDrgRelate.getCurrencyField());
            }

            Page<YbDrgRelate> page = new Page<>();
            SortUtil.handlePageSort(request, page, false);//true 是属性  false是数据库字段可两个
            return this.page(page, queryWrapper);
        } catch (Exception e) {
            log.error("获取字典信息失败", e);
            return null;
        }
    }

    @Override
    public IPage<YbDrgRelate> findYbDrgRelateList(QueryRequest request, YbDrgRelate ybDrgRelate) {
        try {
            Page<YbDrgRelate> page = new Page<>();
            SortUtil.handlePageSort(request, page, false);//true 是属性  false是数据库字段可两个
            return this.baseMapper.findYbDrgRelate(page, ybDrgRelate);
        } catch (Exception e) {
            log.error("获取失败", e);
            return null;
        }
    }

    @Override
    @Transactional
    public void createYbDrgRelate(YbDrgRelate ybDrgRelate) {
//                ybDrgRelate.setCreateTime(new Date());
//        ybDrgRelate.setIsDeletemark(1);
        this.save(ybDrgRelate);
    }

    @Override
    @Transactional
    public void updateYbDrgRelate(YbDrgRelate ybDrgRelate) {
//        ybDrgRelate.setModifyTime(new Date());
        this.baseMapper.updateYbDrgRelate(ybDrgRelate);
    }

    @Override
    @Transactional
    public void deleteYbDrgRelates(String[] Ids) {
        List<String> list = Arrays.asList(Ids);
        this.baseMapper.deleteBatchIds(list);
    }


}