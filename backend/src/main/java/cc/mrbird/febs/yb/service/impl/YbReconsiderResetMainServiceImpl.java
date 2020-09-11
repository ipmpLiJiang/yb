package cc.mrbird.febs.yb.service.impl;

import cc.mrbird.febs.common.domain.QueryRequest;
import cc.mrbird.febs.common.utils.SortUtil;
import cc.mrbird.febs.yb.entity.YbReconsiderResetMain;
import cc.mrbird.febs.yb.dao.YbReconsiderResetMainMapper;
import cc.mrbird.febs.yb.service.IYbReconsiderResetMainService;
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
 * @since 2020-08-17
 */
@Slf4j
@Service("IYbReconsiderResetMainService")
@Transactional(propagation = Propagation.SUPPORTS, readOnly = true, rollbackFor = Exception.class)
public class YbReconsiderResetMainServiceImpl extends ServiceImpl<YbReconsiderResetMainMapper, YbReconsiderResetMain> implements IYbReconsiderResetMainService {


@Override
public IPage<YbReconsiderResetMain> findYbReconsiderResetMains(QueryRequest request, YbReconsiderResetMain ybReconsiderResetMain){
        try{
        LambdaQueryWrapper<YbReconsiderResetMain> queryWrapper=new LambdaQueryWrapper<>();
        queryWrapper.eq(YbReconsiderResetMain::getIsDeletemark, 1);//1是未删 0是已删


        Page<YbReconsiderResetMain> page=new Page<>();
        SortUtil.handlePageSort(request,page,false);//true 是属性  false是数据库字段可两个
        return this.page(page,queryWrapper);
        }catch(Exception e){
        log.error("获取字典信息失败" ,e);
        return null;
        }
        }
@Override
public IPage<YbReconsiderResetMain> findYbReconsiderResetMainList (QueryRequest request, YbReconsiderResetMain ybReconsiderResetMain){
        try{
        Page<YbReconsiderResetMain> page=new Page<>();
        SortUtil.handlePageSort(request,page,false);//true 是属性  false是数据库字段可两个
        return  this.baseMapper.findYbReconsiderResetMain(page,ybReconsiderResetMain);
        }catch(Exception e){
        log.error("获取失败" ,e);
        return null;
        }
        }
@Override
@Transactional
public void createYbReconsiderResetMain(YbReconsiderResetMain ybReconsiderResetMain){
        if(ybReconsiderResetMain.getId() == null || "".equals(ybReconsiderResetMain.getId())) {
                ybReconsiderResetMain.setId(UUID.randomUUID().toString());
        }
        ybReconsiderResetMain.setCreateTime(new Date());
        ybReconsiderResetMain.setIsDeletemark(1);
        this.save(ybReconsiderResetMain);
        }

@Override
@Transactional
public void updateYbReconsiderResetMain(YbReconsiderResetMain ybReconsiderResetMain){
        ybReconsiderResetMain.setModifyTime(new Date());
        this.baseMapper.updateYbReconsiderResetMain(ybReconsiderResetMain);
        }

@Override
@Transactional
public void deleteYbReconsiderResetMains(String[]Ids){
        List<String> list=Arrays.asList(Ids);
        this.baseMapper.deleteBatchIds(list);
        }


        }