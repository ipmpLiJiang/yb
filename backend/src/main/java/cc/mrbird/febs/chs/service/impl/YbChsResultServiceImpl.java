package cc.mrbird.febs.chs.service.impl;

import cc.mrbird.febs.common.domain.QueryRequest;
import cc.mrbird.febs.common.utils.SortUtil;
import cc.mrbird.febs.chs.entity.YbChsResult;
import cc.mrbird.febs.chs.dao.YbChsResultMapper;
import cc.mrbird.febs.chs.service.IYbChsResultService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import freemarker.template.utility.StringUtil;
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
 * @since 2022-06-20
 */
@Slf4j
@Service("IYbChsResultService")
@Transactional(propagation = Propagation.SUPPORTS, readOnly = true, rollbackFor = Exception.class)
public class YbChsResultServiceImpl extends ServiceImpl<YbChsResultMapper, YbChsResult> implements IYbChsResultService {


    @Override
    public IPage<YbChsResult> findYbChsResults(QueryRequest request, YbChsResult ybChsResult) {
        try {
            LambdaQueryWrapper<YbChsResult> queryWrapper = new LambdaQueryWrapper<>();
//            queryWrapper.eq(YbChsResult::getIsDeletemark, 1);//1是未删 0是已删


            Page<YbChsResult> page = new Page<>();
            SortUtil.handlePageSort(request, page, false);//true 是属性  false是数据库字段可两个
            return this.page(page, queryWrapper);
        } catch (Exception e) {
            log.error("获取字典信息失败", e);
            return null;
        }
    }

    @Override
    public IPage<YbChsResult> findYbChsResultList(QueryRequest request, YbChsResult ybChsResult) {
        try {
            Page<YbChsResult> page = new Page<>();
            SortUtil.handlePageSort(request, page, false);//true 是属性  false是数据库字段可两个
            return this.baseMapper.findYbChsResult(page, ybChsResult);
        } catch (Exception e) {
            log.error("获取失败", e);
            return null;
        }
    }

    @Override
    @Transactional
    public void createYbChsResult(YbChsResult ybChsResult) {
        ybChsResult.setId(UUID.randomUUID().toString());
//        ybChsResult.setCreateTime(new Date());
//        ybChsResult.setIsDeletemark(1);
        this.save(ybChsResult);
    }

    @Override
    @Transactional
    public void updateYbChsResult(YbChsResult ybChsResult) {
//        ybChsResult.setModifyTime(new Date());
        this.baseMapper.updateYbChsResult(ybChsResult);
    }

    @Override
    @Transactional
    public void deleteYbChsResults(String[] Ids) {
        List<String> list = Arrays.asList(Ids);
        this.baseMapper.deleteBatchIds(list);
    }

    @Override
    public List<YbChsResult> findChsResultList(YbChsResult ybChsResult) {
        List<YbChsResult> list = new ArrayList<>();
        LambdaQueryWrapper<YbChsResult> wrapper = new LambdaQueryWrapper<>();
        if(StringUtils.isNotBlank(ybChsResult.getApplyDateStr())) {
            wrapper.eq(YbChsResult::getApplyDateStr,ybChsResult.getApplyDateStr());
        }
        if(ybChsResult.getAreaType() != null) {
            wrapper.eq(YbChsResult::getAreaType,ybChsResult.getAreaType());
        }
        if(ybChsResult.getState() != null) {
            wrapper.eq(YbChsResult::getState,ybChsResult.getState());
        }
        return list;
    }
}