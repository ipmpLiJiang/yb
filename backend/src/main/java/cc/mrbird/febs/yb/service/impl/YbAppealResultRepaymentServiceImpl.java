package cc.mrbird.febs.yb.service.impl;

import cc.mrbird.febs.common.domain.QueryRequest;
import cc.mrbird.febs.common.utils.SortUtil;
import cc.mrbird.febs.yb.entity.YbAppealResultDeductimplement;
import cc.mrbird.febs.yb.entity.YbAppealResultRepayment;
import cc.mrbird.febs.yb.dao.YbAppealResultRepaymentMapper;
import cc.mrbird.febs.yb.entity.YbAppealResultRepaymentView;
import cc.mrbird.febs.yb.service.IYbAppealResultRepaymentService;
import cc.mrbird.febs.yb.service.IYbAppealResultRepaymentViewService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.time.LocalDate;
import java.util.stream.Collectors;

/**
 * <p>
 * 服务实现类
 * </p>
 *
 * @author viki
 * @since 2020-10-09
 */
@Slf4j
@Service("IYbAppealResultRepaymentService")
@Transactional(propagation = Propagation.SUPPORTS, readOnly = true, rollbackFor = Exception.class)
public class YbAppealResultRepaymentServiceImpl extends ServiceImpl<YbAppealResultRepaymentMapper, YbAppealResultRepayment> implements IYbAppealResultRepaymentService {

@Autowired
private IYbAppealResultRepaymentViewService iYbAppealResultRepaymentViewService;

    @Override
    public IPage<YbAppealResultRepayment> findYbAppealResultRepayments(QueryRequest request, YbAppealResultRepayment ybAppealResultRepayment) {
        try {
            LambdaQueryWrapper<YbAppealResultRepayment> queryWrapper = new LambdaQueryWrapper<>();
            queryWrapper.eq(YbAppealResultRepayment::getIsDeletemark, 1);//1是未删 0是已删


            Page<YbAppealResultRepayment> page = new Page<>();
            SortUtil.handlePageSort(request, page, false);//true 是属性  false是数据库字段可两个
            return this.page(page, queryWrapper);
        } catch (Exception e) {
            log.error("获取字典信息失败", e);
            return null;
        }
    }

    @Override
    public IPage<YbAppealResultRepayment> findYbAppealResultRepaymentList(QueryRequest request, YbAppealResultRepayment ybAppealResultRepayment) {
        try {
            Page<YbAppealResultRepayment> page = new Page<>();
            SortUtil.handlePageSort(request, page, false);//true 是属性  false是数据库字段可两个
            return this.baseMapper.findYbAppealResultRepayment(page, ybAppealResultRepayment);
        } catch (Exception e) {
            log.error("获取失败", e);
            return null;
        }
    }

    @Override
    @Transactional
    public void createYbAppealResultRepayment(YbAppealResultRepayment ybAppealResultRepayment) {
        ybAppealResultRepayment.setId(UUID.randomUUID().toString());
        ybAppealResultRepayment.setCreateTime(new Date());
        ybAppealResultRepayment.setIsDeletemark(1);
        this.save(ybAppealResultRepayment);
    }


    @Override
    @Transactional
    public String createAppealResultRepayment(YbAppealResultRepayment ybAppealResultRepayment) {
        LambdaQueryWrapper<YbAppealResultRepayment> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(YbAppealResultRepayment::getResultId, ybAppealResultRepayment.getResultId());
        wrapper.eq(YbAppealResultRepayment::getResetDataId, ybAppealResultRepayment.getResetDataId());
        List<YbAppealResultRepayment> list = this.list(wrapper);
        if (list.size() == 0) {
            if (ybAppealResultRepayment.getId() == null || "".equals(ybAppealResultRepayment.getId())) {
                ybAppealResultRepayment.setId(UUID.randomUUID().toString());
            }
            ybAppealResultRepayment.setCreateTime(new Date());
            ybAppealResultRepayment.setIsDeletemark(1);
            boolean bl = this.save(ybAppealResultRepayment);
            if (bl) {
                return "ok";
            } else {
                return "n1";
            }
        } else {
            return "n2";
        }
    }
    @Override
    @Transactional
    public void batchCreateAppealResultRepaymentNull(String applyDateStr, Long uid) {
        YbAppealResultRepaymentView ybAppealResultRepaymentView = new YbAppealResultRepaymentView();
        ybAppealResultRepaymentView.setApplyDateStr(applyDateStr);
        ybAppealResultRepaymentView.setShareProgramme("null");
        List<YbAppealResultRepaymentView> queryRepaymentList = iYbAppealResultRepaymentViewService.findAppealResultRepaymentViewList(ybAppealResultRepaymentView);
        if(queryRepaymentList.size()>0){
            LambdaQueryWrapper<YbAppealResultRepayment> wrapper = new LambdaQueryWrapper<>();
            wrapper.eq(YbAppealResultRepayment::getApplyDateStr, applyDateStr);
            List<YbAppealResultRepayment> queryList = this.list(wrapper);

            List<YbAppealResultRepayment> createList = new ArrayList<>();
            for (YbAppealResultRepaymentView item : queryRepaymentList){
                if (queryList.stream().filter(s -> s.getResultId().equals(item.getResultId()) &&
                        s.getResetDataId().equals(item.getResetDataId())
                ).count()==0) {
                    YbAppealResultRepayment ybAppealResultRepayment = new YbAppealResultRepayment();
                    ybAppealResultRepayment.setId(UUID.randomUUID().toString());

                    ybAppealResultRepayment.setApplyDate(item.getApplyDate());
                    ybAppealResultRepayment.setApplyDateStr(item.getApplyDateStr());
                    ybAppealResultRepayment.setResultId(item.getResultId());
                    ybAppealResultRepayment.setResetDataId(item.getResetDataId());
                    ybAppealResultRepayment.setDeductImplementId(item.getDeductImplementId());
                    ybAppealResultRepayment.setRepaymentProgramme("");
                    ybAppealResultRepayment.setDataType(item.getDataType());

                    ybAppealResultRepayment.setCreateUserId(uid);
                    ybAppealResultRepayment.setCreateTime(new Date());
                    ybAppealResultRepayment.setIsDeletemark(1);

                    createList.add(ybAppealResultRepayment);
                }
            }

            if (createList.size() > 0) {
                this.saveBatch(createList);
            }
        }

    }
    @Override
    @Transactional
    public void batchCreateAppealResultRepaymentNull(List<YbAppealResultRepayment> appealResultRepaymentList,String applyDateStr, Long uid) {
        LambdaQueryWrapper<YbAppealResultRepayment> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(YbAppealResultRepayment::getApplyDateStr, applyDateStr);
        List<YbAppealResultRepayment> queryList = this.list(wrapper);
        List<YbAppealResultRepayment> createList = new ArrayList<>();

        for (YbAppealResultRepayment item : appealResultRepaymentList) {
            if (queryList.stream().filter(s -> s.getResultId().equals(item.getResultId()) &&
                    s.getResetDataId().equals(item.getResetDataId())
            ).count()==0) {
                YbAppealResultRepayment ybAppealResultRepayment = new YbAppealResultRepayment();
                ybAppealResultRepayment.setId(UUID.randomUUID().toString());

                ybAppealResultRepayment.setApplyDate(item.getApplyDate());
                ybAppealResultRepayment.setApplyDateStr(item.getApplyDateStr());
                ybAppealResultRepayment.setResultId(item.getResultId());
                ybAppealResultRepayment.setResetDataId(item.getResetDataId());
                ybAppealResultRepayment.setDeductImplementId(item.getDeductImplementId());
                ybAppealResultRepayment.setRepaymentProgramme("");
                ybAppealResultRepayment.setDataType(item.getDataType());

                ybAppealResultRepayment.setCreateUserId(uid);
                ybAppealResultRepayment.setCreateTime(new Date());
                ybAppealResultRepayment.setIsDeletemark(1);

                createList.add(ybAppealResultRepayment);
            }
        }
        if (createList.size() > 0) {
            this.saveBatch(createList);
        }
    }

    @Override
    @Transactional
    public void updateYbAppealResultRepayment(YbAppealResultRepayment ybAppealResultRepayment) {
        ybAppealResultRepayment.setModifyTime(new Date());
        this.baseMapper.updateYbAppealResultRepayment(ybAppealResultRepayment);
    }

    @Override
    @Transactional
    public void deleteYbAppealResultRepayments(String[] Ids) {
        List<String> list = Arrays.asList(Ids);
        this.baseMapper.deleteBatchIds(list);
    }


}