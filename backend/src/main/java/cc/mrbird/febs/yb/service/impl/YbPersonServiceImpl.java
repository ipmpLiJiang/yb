package cc.mrbird.febs.yb.service.impl;

import cc.mrbird.febs.common.domain.QueryRequest;
import cc.mrbird.febs.common.utils.SortUtil;
import cc.mrbird.febs.yb.dao.YbPersonMapper;
import cc.mrbird.febs.yb.entity.YbPerson;
import cc.mrbird.febs.yb.service.IYbPersonService;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.Date;
import java.util.List;
/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author viki
 * @since 2020-07-21
 */
@Slf4j
@Service("IYbPersonService")
@Transactional(propagation = Propagation.SUPPORTS, readOnly = true, rollbackFor = Exception.class)
public class YbPersonServiceImpl extends ServiceImpl<YbPersonMapper, YbPerson> implements IYbPersonService {


@Override
public IPage<YbPerson> findYbPersons(QueryRequest request, YbPerson ybPerson){
        try{
        LambdaQueryWrapper<YbPerson> queryWrapper=new LambdaQueryWrapper<>();
        queryWrapper.eq(YbPerson::getIsDeletemark, 1);//1是未删 0是已删


        Page<YbPerson> page=new Page<>();
        SortUtil.handlePageSort(request,page,false);//true 是属性  false是数据库字段可两个
        return this.page(page,queryWrapper);
        }catch(Exception e){
        log.error("获取字典信息失败" ,e);
        return null;
        }
        }
@Override
public IPage<YbPerson> findYbPersonList (QueryRequest request, YbPerson ybPerson){
        try{
        Page<YbPerson> page=new Page<>();
        SortUtil.handlePageSort(request,page,false);//true 是属性  false是数据库字段可两个
        return  this.baseMapper.findYbPerson(page,ybPerson);
        }catch(Exception e){
        log.error("获取失败" ,e);
        return null;
        }
        }
@Override
@Transactional
public void createYbPerson(YbPerson ybPerson){
                ybPerson.setCreateTime(new Date());
        ybPerson.setIsDeletemark(1);
        this.save(ybPerson);
        }

@Override
@Transactional
public void updateYbPerson(YbPerson ybPerson){
        ybPerson.setModifyTime(new Date());
        this.baseMapper.updateYbPerson(ybPerson);
        }

@Override
@Transactional
public void deleteYbPersons(String[]Ids){
        List<String> list=Arrays.asList(Ids);
        this.baseMapper.deleteBatchIds(list);
        }


        }