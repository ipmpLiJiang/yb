package cc.mrbird.febs.chs.service.impl;

import cc.mrbird.febs.common.domain.QueryRequest;
import cc.mrbird.febs.common.utils.SortUtil;
import cc.mrbird.febs.chs.entity.YbChsJk;
import cc.mrbird.febs.chs.dao.YbChsJkMapper;
import cc.mrbird.febs.chs.service.IYbChsJkService;
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
 * @since 2022-06-21
 */
@Slf4j
@Service("IYbChsJkService")
@Transactional(propagation = Propagation.SUPPORTS, readOnly = true, rollbackFor = Exception.class)
public class YbChsJkServiceImpl extends ServiceImpl<YbChsJkMapper, YbChsJk> implements IYbChsJkService {


    @Override
    public IPage<YbChsJk> findYbChsJks(QueryRequest request, YbChsJk ybChsJk) {
        try {
            LambdaQueryWrapper<YbChsJk> queryWrapper = new LambdaQueryWrapper<>();
            queryWrapper.eq(YbChsJk::getIsDeletemark, 1);//1是未删 0是已删


            Page<YbChsJk> page = new Page<>();
            SortUtil.handlePageSort(request, page, false);//true 是属性  false是数据库字段可两个
            return this.page(page, queryWrapper);
        } catch (Exception e) {
            log.error("获取字典信息失败", e);
            return null;
        }
    }

    @Override
    public IPage<YbChsJk> findYbChsJkList(QueryRequest request, YbChsJk ybChsJk) {
        try {
            Page<YbChsJk> page = new Page<>();
            SortUtil.handlePageSort(request, page, false);//true 是属性  false是数据库字段可两个
            return this.baseMapper.findYbChsJk(page, ybChsJk);
        } catch (Exception e) {
            log.error("获取失败", e);
            return null;
        }
    }

    @Override
    @Transactional
    public void createYbChsJk(YbChsJk ybChsJk) {
        ybChsJk.setId(UUID.randomUUID().toString());
//        ybChsJk.setCreateTime(new Date());
        ybChsJk.setIsDeletemark(1);
        this.save(ybChsJk);
    }

    @Override
    @Transactional
    public void updateYbChsJk(YbChsJk ybChsJk) {
//        ybChsJk.setModifyTime(new Date());
        this.baseMapper.updateYbChsJk(ybChsJk);
    }

    @Override
    @Transactional
    public void deleteYbChsJks(String[] Ids) {
        List<String> list = Arrays.asList(Ids);
        this.baseMapper.deleteBatchIds(list);
    }


}