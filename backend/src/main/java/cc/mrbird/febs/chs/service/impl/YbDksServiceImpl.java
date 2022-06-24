package cc.mrbird.febs.chs.service.impl;

import cc.mrbird.febs.common.domain.QueryRequest;
import cc.mrbird.febs.common.utils.SortUtil;
import cc.mrbird.febs.chs.entity.YbDks;
import cc.mrbird.febs.chs.dao.YbDksMapper;
import cc.mrbird.febs.chs.service.IYbDksService;
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
 *  服务实现类
 * </p>
 *
 * @author viki
 * @since 2022-06-24
 */
@Slf4j
@Service("IYbDksService")
@Transactional(propagation = Propagation.SUPPORTS, readOnly = true, rollbackFor = Exception.class)
public class YbDksServiceImpl extends ServiceImpl<YbDksMapper, YbDks> implements IYbDksService {


@Override
public IPage<YbDks> findYbDkss(QueryRequest request, YbDks ybDks){
        try{
        LambdaQueryWrapper<YbDks> queryWrapper=new LambdaQueryWrapper<>();
        queryWrapper.eq(YbDks::getIsDeletemark, 1);//1是未删 0是已删


        Page<YbDks> page=new Page<>();
        SortUtil.handlePageSort(request,page,false);//true 是属性  false是数据库字段可两个
        return this.page(page,queryWrapper);
        }catch(Exception e){
        log.error("获取字典信息失败" ,e);
        return null;
        }
        }
@Override
public IPage<YbDks> findYbDksList (QueryRequest request, YbDks ybDks){
        try{
        Page<YbDks> page=new Page<>();
        SortUtil.handlePageSort(request,page,false);//true 是属性  false是数据库字段可两个
        return  this.baseMapper.findYbDks(page,ybDks);
        }catch(Exception e){
        log.error("获取失败" ,e);
        return null;
        }
        }
@Override
@Transactional
public void createYbDks(YbDks ybDks){
                ybDks.setCreateTime(new Date());
        ybDks.setIsDeletemark(1);
        this.save(ybDks);
        }

@Override
@Transactional
public void updateYbDks(YbDks ybDks){
        ybDks.setModifyTime(new Date());
        this.baseMapper.updateYbDks(ybDks);
        }

@Override
@Transactional
public void deleteYbDkss(String[]Ids){
        List<String> list=Arrays.asList(Ids);
        this.baseMapper.deleteBatchIds(list);
        }


        }