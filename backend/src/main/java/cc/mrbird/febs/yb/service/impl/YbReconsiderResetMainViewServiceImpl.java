package cc.mrbird.febs.yb.service.impl;

import cc.mrbird.febs.common.domain.QueryRequest;
import cc.mrbird.febs.common.utils.SortUtil;
import cc.mrbird.febs.yb.entity.YbReconsiderResetMainView;
import cc.mrbird.febs.yb.dao.YbReconsiderResetMainViewMapper;
import cc.mrbird.febs.yb.service.IYbReconsiderResetMainViewService;
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
 * @since 2020-08-18
 */
@Slf4j
@Service("IYbReconsiderResetMainViewService")
@Transactional(propagation = Propagation.SUPPORTS, readOnly = true, rollbackFor = Exception.class)
public class YbReconsiderResetMainViewServiceImpl extends ServiceImpl<YbReconsiderResetMainViewMapper, YbReconsiderResetMainView> implements IYbReconsiderResetMainViewService {


@Override
public IPage<YbReconsiderResetMainView> findYbReconsiderResetMainViews(QueryRequest request, YbReconsiderResetMainView ybReconsiderResetMainView){
        try{
        LambdaQueryWrapper<YbReconsiderResetMainView> queryWrapper=new LambdaQueryWrapper<>();
                String sql = "";
                sql = " (applyDateStr='" + ybReconsiderResetMainView.getApplyDateStr() + "' )";

                if (ybReconsiderResetMainView.getCurrencyField() != null && !"".equals(ybReconsiderResetMainView.getCurrencyField())) {
                        sql = sql + " and (serialNo like'%" + ybReconsiderResetMainView.getCurrencyField() + "%'" +
                                " or billNo like'%" + ybReconsiderResetMainView.getCurrencyField() + "%'" +
                                " or ruleName like'%" + ybReconsiderResetMainView.getCurrencyField() + "%')";
                }
                queryWrapper.apply(sql);

        Page<YbReconsiderResetMainView> page=new Page<>();
        SortUtil.handlePageSort(request,page,false);//true 是属性  false是数据库字段可两个
        return this.page(page,queryWrapper);
        }catch(Exception e){
        log.error("获取字典信息失败" ,e);
        return null;
        }
        }
@Override
public IPage<YbReconsiderResetMainView> findYbReconsiderResetMainViewList (QueryRequest request, YbReconsiderResetMainView ybReconsiderResetMainView){
        try{
        Page<YbReconsiderResetMainView> page=new Page<>();
        SortUtil.handlePageSort(request,page,false);//true 是属性  false是数据库字段可两个
        return  this.baseMapper.findYbReconsiderResetMainView(page,ybReconsiderResetMainView);
        }catch(Exception e){
        log.error("获取VIEW失败" ,e);
        return null;
        }
        }
@Override
@Transactional
public void createYbReconsiderResetMainView(YbReconsiderResetMainView ybReconsiderResetMainView){
//        ybReconsiderResetMainView.setCreateTime(new Date());
//        ybReconsiderResetMainView.setIsDeletemark(1);
        this.save(ybReconsiderResetMainView);
        }

@Override
@Transactional
public void updateYbReconsiderResetMainView(YbReconsiderResetMainView ybReconsiderResetMainView){
//        ybReconsiderResetMainView.setModifyTime(new Date());
        this.baseMapper.updateYbReconsiderResetMainView(ybReconsiderResetMainView);
        }

@Override
@Transactional
public void deleteYbReconsiderResetMainViews(String[]Ids){
        List<String> list=Arrays.asList(Ids);
        this.baseMapper.deleteBatchIds(list);
        }


        }