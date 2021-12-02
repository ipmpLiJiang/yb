package cc.mrbird.febs.drg.service.impl;

import cc.mrbird.febs.common.domain.QueryRequest;
import cc.mrbird.febs.common.utils.SortUtil;
import cc.mrbird.febs.drg.entity.YbDrgJk;
import cc.mrbird.febs.drg.dao.YbDrgJkMapper;
import cc.mrbird.febs.drg.service.IYbDrgJkService;
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
@Service("IYbDrgJkService")
@Transactional(propagation = Propagation.SUPPORTS, readOnly = true, rollbackFor = Exception.class)
public class YbDrgJkServiceImpl extends ServiceImpl<YbDrgJkMapper, YbDrgJk> implements IYbDrgJkService {


    @Override
    public IPage<YbDrgJk> findYbDrgJks(QueryRequest request, YbDrgJk ybDrgJk) {
        try {
            LambdaQueryWrapper<YbDrgJk> queryWrapper = new LambdaQueryWrapper<>();
//            queryWrapper.eq(YbDrgJk::getIsDeletemark, 1);//1是未删 0是已删

            Page<YbDrgJk> page = new Page<>();
            SortUtil.handlePageSort(request, page, false);//true 是属性  false是数据库字段可两个
            return this.page(page, queryWrapper);
        } catch (Exception e) {
            log.error("获取字典信息失败", e);
            return null;
        }
    }

    @Override
    public IPage<YbDrgJk> findYbDrgJkList(QueryRequest request, YbDrgJk ybDrgJk) {
        try {
            Page<YbDrgJk> page = new Page<>();
            SortUtil.handlePageSort(request, page, false);//true 是属性  false是数据库字段可两个
            return this.baseMapper.findYbDrgJk(page, ybDrgJk);
        } catch (Exception e) {
            log.error("获取失败", e);
            return null;
        }
    }

    @Override
    @Transactional
    public void createYbDrgJk(YbDrgJk ybDrgJk) {
        ybDrgJk.setId(UUID.randomUUID().toString());
//        ybDrgJk.setCreateTime(new Date());
//        ybDrgJk.setIsDeletemark(1);
        this.save(ybDrgJk);
    }

    @Override
    @Transactional
    public void updateYbDrgJk(YbDrgJk ybDrgJk) {
//        ybDrgJk.setModifyTime(new Date());
        this.baseMapper.updateYbDrgJk(ybDrgJk);
    }

    @Override
    @Transactional
    public void deleteYbDrgJks(String[] Ids) {
        List<String> list = Arrays.asList(Ids);
        this.baseMapper.deleteBatchIds(list);
    }

    @Override
    public YbDrgJk findYbDrgJkByApplyDataId(String applyDataId) {
        LambdaQueryWrapper<YbDrgJk> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(YbDrgJk::getApplyDataId, applyDataId);
        List<YbDrgJk> list = this.baseMapper.selectList(queryWrapper);
        if (list.size() > 0) {
            return list.get(0);
        } else {
            return null;
        }
    }

}