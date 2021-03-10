package cc.mrbird.febs.yb.service.impl;

import cc.mrbird.febs.common.domain.QueryRequest;
import cc.mrbird.febs.common.utils.SortUtil;
import cc.mrbird.febs.yb.entity.YbHandleVerify;
import cc.mrbird.febs.yb.dao.YbHandleVerifyMapper;
import cc.mrbird.febs.yb.service.IYbHandleVerifyService;
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
 * @since 2020-08-28
 */
@Slf4j
@Service("IYbHandleVerifyService")
@Transactional(propagation = Propagation.SUPPORTS, readOnly = true, rollbackFor = Exception.class)
public class YbHandleVerifyServiceImpl extends ServiceImpl<YbHandleVerifyMapper, YbHandleVerify> implements IYbHandleVerifyService {


    @Override
    public IPage<YbHandleVerify> findYbHandleVerifys(QueryRequest request, YbHandleVerify ybHandleVerify) {
        try {
            LambdaQueryWrapper<YbHandleVerify> queryWrapper = new LambdaQueryWrapper<>();
            queryWrapper.eq(YbHandleVerify::getIsDeletemark, 1);//1是未删 0是已删


            Page<YbHandleVerify> page = new Page<>();
            SortUtil.handlePageSort(request, page, false);//true 是属性  false是数据库字段可两个
            return this.page(page, queryWrapper);
        } catch (Exception e) {
            log.error("获取字典信息失败", e);
            return null;
        }
    }

    @Override
    public IPage<YbHandleVerify> findYbHandleVerifyList(QueryRequest request, YbHandleVerify ybHandleVerify) {
        try {
            Page<YbHandleVerify> page = new Page<>();
            SortUtil.handlePageSort(request, page, false);//true 是属性  false是数据库字段可两个
            return this.baseMapper.findYbHandleVerify(page, ybHandleVerify);
        } catch (Exception e) {
            log.error("获取失败", e);
            return null;
        }
    }

    @Override
    public YbHandleVerify findYbHandleVerifyApplyDateStr(String applyDateStr) {
        LambdaQueryWrapper<YbHandleVerify> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(YbHandleVerify::getIsDeletemark, 1);
        queryWrapper.eq(YbHandleVerify::getApplyDateStr, applyDateStr);
        List<YbHandleVerify> list = this.list(queryWrapper);
        if (list.size() > 0) {
            return list.get(0);
        }
        return null;
    }

    @Override
    @Transactional
    public void createYbHandleVerify(YbHandleVerify ybHandleVerify) {
        ybHandleVerify.setId(UUID.randomUUID().toString());
        ybHandleVerify.setCreateTime(new Date());
        ybHandleVerify.setIsDeletemark(1);
        this.save(ybHandleVerify);
    }

    @Override
    @Transactional
    public void updateYbHandleVerify(YbHandleVerify ybHandleVerify) {
        ybHandleVerify.setModifyTime(new Date());
        this.baseMapper.updateYbHandleVerify(ybHandleVerify);
    }

    @Override
    @Transactional
    public void deleteYbHandleVerifys(String[] Ids) {
        List<String> list = Arrays.asList(Ids);
        this.baseMapper.deleteBatchIds(list);
    }


}