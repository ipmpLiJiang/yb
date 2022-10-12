package cc.mrbird.febs.chs.service.impl;

import cc.mrbird.febs.common.domain.QueryRequest;
import cc.mrbird.febs.common.utils.SortUtil;
import cc.mrbird.febs.chs.entity.YbChsProjectSet;
import cc.mrbird.febs.chs.dao.YbChsProjectSetMapper;
import cc.mrbird.febs.chs.service.IYbChsProjectSetService;
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
 * @since 2022-10-12
 */
@Slf4j
@Service("IYbChsProjectSetService")
@Transactional(propagation = Propagation.SUPPORTS, readOnly = true, rollbackFor = Exception.class)
public class YbChsProjectSetServiceImpl extends ServiceImpl<YbChsProjectSetMapper, YbChsProjectSet> implements IYbChsProjectSetService {


    @Override
    public IPage<YbChsProjectSet> findYbChsProjectSets(QueryRequest request, YbChsProjectSet ybChsProjectSet) {
        try {
            LambdaQueryWrapper<YbChsProjectSet> queryWrapper = new LambdaQueryWrapper<>();
            queryWrapper.eq(YbChsProjectSet::getIsDeletemark, 1);//1是未删 0是已删

            if (ybChsProjectSet.getAreaType() != null) {
                queryWrapper.eq(YbChsProjectSet::getAreaType, ybChsProjectSet.getAreaType());
            }

            if (StringUtils.isNotBlank(ybChsProjectSet.getCurrencyField())) {
                String sql = "(ruleName like '%" + ybChsProjectSet.getCurrencyField() + "%' or projectName like '%" + ybChsProjectSet.getCurrencyField() + "%')";
                queryWrapper.apply(sql);
            }

            Page<YbChsProjectSet> page = new Page<>();
            SortUtil.handlePageSort(request, page, false);//true 是属性  false是数据库字段可两个
            return this.page(page, queryWrapper);
        } catch (Exception e) {
            log.error("获取字典信息失败", e);
            return null;
        }
    }

    @Override
    public IPage<YbChsProjectSet> findYbChsProjectSetList(QueryRequest request, YbChsProjectSet ybChsProjectSet) {
        try {
            Page<YbChsProjectSet> page = new Page<>();
            SortUtil.handlePageSort(request, page, false);//true 是属性  false是数据库字段可两个
            return this.baseMapper.findYbChsProjectSet(page, ybChsProjectSet);
        } catch (Exception e) {
            log.error("获取失败", e);
            return null;
        }
    }

    @Override
    @Transactional
    public void createYbChsProjectSet(YbChsProjectSet ybChsProjectSet) {
        ybChsProjectSet.setCreateTime(new Date());
        ybChsProjectSet.setIsDeletemark(1);
        this.save(ybChsProjectSet);
    }

    @Override
    @Transactional
    public void updateYbChsProjectSet(YbChsProjectSet ybChsProjectSet) {
        ybChsProjectSet.setModifyTime(new Date());
        this.baseMapper.updateYbChsProjectSet(ybChsProjectSet);
    }

    @Override
    @Transactional
    public void deleteYbChsProjectSets(String[] Ids) {
        List<String> list = Arrays.asList(Ids);
        this.baseMapper.deleteBatchIds(list);
    }


}
