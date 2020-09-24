package cc.mrbird.febs.yb.service.impl;

import cc.mrbird.febs.common.domain.QueryRequest;
import cc.mrbird.febs.common.utils.SortUtil;
import cc.mrbird.febs.yb.entity.YbAppealResultDeductimplementView;
import cc.mrbird.febs.yb.dao.YbAppealResultDeductimplementViewMapper;
import cc.mrbird.febs.yb.service.IYbAppealResultDeductimplementViewService;
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
 * VIEW 服务实现类
 * </p>
 *
 * @author viki
 * @since 2020-09-24
 */
@Slf4j
@Service("IYbAppealResultDeductimplementViewService")
@Transactional(propagation = Propagation.SUPPORTS, readOnly = true, rollbackFor = Exception.class)
public class YbAppealResultDeductimplementViewServiceImpl extends ServiceImpl<YbAppealResultDeductimplementViewMapper, YbAppealResultDeductimplementView> implements IYbAppealResultDeductimplementViewService {


@Override
public IPage<YbAppealResultDeductimplementView> findYbAppealResultDeductimplementViews(QueryRequest request, YbAppealResultDeductimplementView ybAppealResultDeductimplementView){
        try{
        LambdaQueryWrapper<YbAppealResultDeductimplementView> queryWrapper=new LambdaQueryWrapper<>();
        queryWrapper.eq(YbAppealResultDeductimplementView::getIsDeletemark, 1);//1是未删 0是已删


        Page<YbAppealResultDeductimplementView> page=new Page<>();
        SortUtil.handlePageSort(request,page,false);//true 是属性  false是数据库字段可两个
        return this.page(page,queryWrapper);
        }catch(Exception e){
        log.error("获取字典信息失败" ,e);
        return null;
        }
        }
@Override
public IPage<YbAppealResultDeductimplementView> findYbAppealResultDeductimplementViewList (QueryRequest request, YbAppealResultDeductimplementView ybAppealResultDeductimplementView){
        try{
        Page<YbAppealResultDeductimplementView> page=new Page<>();
        SortUtil.handlePageSort(request,page,false);//true 是属性  false是数据库字段可两个
        return  this.baseMapper.findYbAppealResultDeductimplementView(page,ybAppealResultDeductimplementView);
        }catch(Exception e){
        log.error("获取VIEW失败" ,e);
        return null;
        }
        }
@Override
@Transactional
public void createYbAppealResultDeductimplementView(YbAppealResultDeductimplementView ybAppealResultDeductimplementView){
        ybAppealResultDeductimplementView.setCreateTime(new Date());
        ybAppealResultDeductimplementView.setIsDeletemark(1);
        this.save(ybAppealResultDeductimplementView);
        }

@Override
@Transactional
public void updateYbAppealResultDeductimplementView(YbAppealResultDeductimplementView ybAppealResultDeductimplementView){
        ybAppealResultDeductimplementView.setModifyTime(new Date());
        this.baseMapper.updateYbAppealResultDeductimplementView(ybAppealResultDeductimplementView);
        }

@Override
@Transactional
public void deleteYbAppealResultDeductimplementViews(String[]Ids){
        List<String> list=Arrays.asList(Ids);
        this.baseMapper.deleteBatchIds(list);
        }


        }