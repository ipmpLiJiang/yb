package cc.mrbird.febs.yb.service.impl;

import cc.mrbird.febs.common.domain.QueryRequest;
import cc.mrbird.febs.common.utils.SortUtil;
import cc.mrbird.febs.yb.entity.YbAppealResultDeductimplement;
import cc.mrbird.febs.yb.dao.YbAppealResultDeductimplementMapper;
import cc.mrbird.febs.yb.service.IYbAppealResultDeductimplementService;
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
 * @since 2020-09-24
 */
@Slf4j
@Service("IYbAppealResultDeductimplementService")
@Transactional(propagation = Propagation.SUPPORTS, readOnly = true, rollbackFor = Exception.class)
public class YbAppealResultDeductimplementServiceImpl extends ServiceImpl<YbAppealResultDeductimplementMapper, YbAppealResultDeductimplement> implements IYbAppealResultDeductimplementService {


    @Override
    public IPage<YbAppealResultDeductimplement> findYbAppealResultDeductimplements(QueryRequest request, YbAppealResultDeductimplement ybAppealResultDeductimplement) {
        try {
            LambdaQueryWrapper<YbAppealResultDeductimplement> queryWrapper = new LambdaQueryWrapper<>();
            queryWrapper.eq(YbAppealResultDeductimplement::getIsDeletemark, 1);//1是未删 0是已删


            Page<YbAppealResultDeductimplement> page = new Page<>();
            SortUtil.handlePageSort(request, page, false);//true 是属性  false是数据库字段可两个
            return this.page(page, queryWrapper);
        } catch (Exception e) {
            log.error("获取字典信息失败", e);
            return null;
        }
    }

    @Override
    public IPage<YbAppealResultDeductimplement> findYbAppealResultDeductimplementList(QueryRequest request, YbAppealResultDeductimplement ybAppealResultDeductimplement) {
        try {
            Page<YbAppealResultDeductimplement> page = new Page<>();
            SortUtil.handlePageSort(request, page, false);//true 是属性  false是数据库字段可两个
            return this.baseMapper.findYbAppealResultDeductimplement(page, ybAppealResultDeductimplement);
        } catch (Exception e) {
            log.error("获取失败", e);
            return null;
        }
    }

    @Override
    @Transactional
    public void createYbAppealResultDeductimplement(YbAppealResultDeductimplement ybAppealResultDeductimplement) {
        ybAppealResultDeductimplement.setCreateTime(new Date());
        if (ybAppealResultDeductimplement.getId() == null || "".equals(ybAppealResultDeductimplement.getId())) {
            ybAppealResultDeductimplement.setId(UUID.randomUUID().toString());
        }
        ybAppealResultDeductimplement.setCreateTime(new Date());
        ybAppealResultDeductimplement.setIsDeletemark(1);
        this.save(ybAppealResultDeductimplement);
    }

    @Override
    @Transactional
    public String createAppealResultDeductimplement(YbAppealResultDeductimplement ybAppealResultDeductimplement) {
        LambdaQueryWrapper<YbAppealResultDeductimplement> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(YbAppealResultDeductimplement::getResultId,ybAppealResultDeductimplement.getResultId());
        wrapper.eq(YbAppealResultDeductimplement::getResetDataId,ybAppealResultDeductimplement.getResetDataId());
        List<YbAppealResultDeductimplement> list = this.list(wrapper);
        if(list.size()==0) {
            ybAppealResultDeductimplement.setCreateTime(new Date());
            if (ybAppealResultDeductimplement.getId() == null || "".equals(ybAppealResultDeductimplement.getId())) {
                ybAppealResultDeductimplement.setId(UUID.randomUUID().toString());
            }
            ybAppealResultDeductimplement.setCreateTime(new Date());
            ybAppealResultDeductimplement.setIsDeletemark(1);
            boolean bl = this.save(ybAppealResultDeductimplement);
            if(bl){
                return  "ok";
            }else{
                return "n1";
            }
        }else{
            return "n2";
        }
    }

    @Override
    @Transactional
    public void updateYbAppealResultDeductimplement(YbAppealResultDeductimplement ybAppealResultDeductimplement) {
        ybAppealResultDeductimplement.setModifyTime(new Date());
        this.baseMapper.updateYbAppealResultDeductimplement(ybAppealResultDeductimplement);
    }

    @Override
    @Transactional
    public void deleteYbAppealResultDeductimplements(String[] Ids) {
        List<String> list = Arrays.asList(Ids);
        this.baseMapper.deleteBatchIds(list);
    }


}