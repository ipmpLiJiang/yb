package cc.mrbird.febs.scm.service.impl;

import cc.mrbird.febs.common.domain.QueryRequest;
import cc.mrbird.febs.common.utils.SortUtil;
import cc.mrbird.febs.scm.entity.ScmDPlan;
import cc.mrbird.febs.scm.dao.ScmDPlanMapper;
import cc.mrbird.febs.scm.service.IScmDPlanService;
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
 * @since 2019-10-23
 */
@Slf4j
@Service("IScmDPlanService")
@Transactional(propagation = Propagation.SUPPORTS, readOnly = true, rollbackFor = Exception.class)
public class ScmDPlanServiceImpl extends ServiceImpl<ScmDPlanMapper, ScmDPlan> implements IScmDPlanService {


@Override
public IPage<ScmDPlan> findScmDPlans(QueryRequest request, ScmDPlan scmDPlan){
        try{
        LambdaQueryWrapper<ScmDPlan> queryWrapper=new LambdaQueryWrapper<>();

        Page<ScmDPlan> page=new Page<>();
        SortUtil.handlePageSort(request,page,true);
        return this.page(page,queryWrapper);
        }catch(Exception e){
        log.error("获取字典信息失败" ,e);
        return null;
        }
        }

@Override
@Transactional
public void createScmDPlan(ScmDPlan scmDPlan){
        scmDPlan.setId(UUID.randomUUID().toString());
        scmDPlan.setCreateTime(LocalDate.now());
        this.save(scmDPlan);
        }

@Override
@Transactional
public void updateScmDPlan(ScmDPlan scmDPlan){
        scmDPlan.setModifyTime(LocalDate.now());
        this.baseMapper.updateScmDPlan(scmDPlan);
        }

@Override
@Transactional
public void deleteScmDPlans(String[]Ids){
        List<String> list=Arrays.asList(Ids);
        this.baseMapper.deleteBatchIds(list);
        }


        }