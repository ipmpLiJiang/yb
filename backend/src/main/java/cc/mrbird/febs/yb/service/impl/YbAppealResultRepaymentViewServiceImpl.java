package cc.mrbird.febs.yb.service.impl;

import cc.mrbird.febs.com.controller.DataTypeHelpers;
import cc.mrbird.febs.common.domain.QueryRequest;
import cc.mrbird.febs.common.utils.SortUtil;
import cc.mrbird.febs.yb.entity.YbAppealResultDeductimplementView;
import cc.mrbird.febs.yb.entity.YbAppealResultRepaymentView;
import cc.mrbird.febs.yb.dao.YbAppealResultRepaymentViewMapper;
import cc.mrbird.febs.yb.service.IYbAppealResultRepaymentViewService;
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
 * VIEW 服务实现类
 * </p>
 *
 * @author viki
 * @since 2020-10-09
 */
@Slf4j
@Service("IYbAppealResultRepaymentViewService")
@Transactional(propagation = Propagation.SUPPORTS, readOnly = true, rollbackFor = Exception.class)
public class YbAppealResultRepaymentViewServiceImpl extends ServiceImpl<YbAppealResultRepaymentViewMapper, YbAppealResultRepaymentView> implements IYbAppealResultRepaymentViewService {


    @Override
    public IPage<YbAppealResultRepaymentView> findYbAppealResultRepaymentViews(QueryRequest request, YbAppealResultRepaymentView ybAppealResultRepaymentView) {
        try {
            LambdaQueryWrapper<YbAppealResultRepaymentView> queryWrapper = new LambdaQueryWrapper<>();
            List<String> listStr = new ArrayList<>();
            //ApplyDateFrom getApplyDateTo 存储的格式是 2020-09
            if (ybAppealResultRepaymentView.getApplyDateFrom().equals(ybAppealResultRepaymentView.getApplyDateTo())) {
                listStr.add(ybAppealResultRepaymentView.getApplyDateFrom());
            } else {
                listStr = DataTypeHelpers.stringApplyDateStrToList(ybAppealResultRepaymentView.getApplyDateFrom(), ybAppealResultRepaymentView.getApplyDateTo());
            }
            if (listStr.size() > 0) {
                String sql = "(";

                if (listStr.size() == 1) {
                    sql += " applyDateStr = '" + ybAppealResultRepaymentView.getApplyDateFrom() + "'";
                } else {
                    String applyDateStrForm = ybAppealResultRepaymentView.getApplyDateFrom() + "-01";
                    String applyDateStrTo = ybAppealResultRepaymentView.getApplyDateTo() + "-" + String.valueOf(DataTypeHelpers.stringDateFormatMaxDay(ybAppealResultRepaymentView.getApplyDateTo() + "-01", "", false));

                    sql += " applyDate >= '" + applyDateStrForm + "' and applyDate <= '" + applyDateStrTo + "' ";
                }

                if (ybAppealResultRepaymentView.getDataType() != null) {
                    if (ybAppealResultRepaymentView.getDataType() == 0) {
                        sql += " and dataType = 0";
                    } else {
                        sql += " and dataType = 1";
                    }
                }

                if (ybAppealResultRepaymentView.getRepaymentId() != null) {
                    sql += " and repaymentId IS NOT NULL";
                } else {
                    sql += " and repaymentId IS NULL";
                }

                if (ybAppealResultRepaymentView.getArDoctorCode() != null) {
                    sql += " and arDoctorCode = '" + ybAppealResultRepaymentView.getArDoctorCode() + "'";
                }

                sql += ")";

                if (ybAppealResultRepaymentView.getCurrencyField() != null && !"".equals(ybAppealResultRepaymentView.getCurrencyField())) {
                    sql += " and (billNo like'%" + ybAppealResultRepaymentView.getCurrencyField() + "%'" +
                            " or projectName like'%" + ybAppealResultRepaymentView.getCurrencyField() + ")";

                }
                queryWrapper.apply(sql);

                Page<YbAppealResultRepaymentView> page = new Page<>();
                SortUtil.handlePageSort(request, page, false);//true 是属性  false是数据库字段可两个
                return this.page(page, queryWrapper);
            } else {
                return null;
            }
        } catch (Exception e) {
            log.error("获取字典信息失败", e);
            return null;
        }
    }

    @Override
    public IPage<YbAppealResultRepaymentView> findYbAppealResultRepaymentViewList(QueryRequest request, YbAppealResultRepaymentView ybAppealResultRepaymentView) {
        try {
            Page<YbAppealResultRepaymentView> page = new Page<>();
            SortUtil.handlePageSort(request, page, false);//true 是属性  false是数据库字段可两个
            return this.baseMapper.findYbAppealResultRepaymentView(page, ybAppealResultRepaymentView);
        } catch (Exception e) {
            log.error("获取VIEW失败", e);
            return null;
        }
    }

    @Override
    @Transactional
    public void createYbAppealResultRepaymentView(YbAppealResultRepaymentView ybAppealResultRepaymentView) {
        //ybAppealResultRepaymentView.setCreateTime(new Date());
        //ybAppealResultRepaymentView.setIsDeletemark(1);
        this.save(ybAppealResultRepaymentView);
    }

    @Override
    @Transactional
    public void updateYbAppealResultRepaymentView(YbAppealResultRepaymentView ybAppealResultRepaymentView) {
        //ybAppealResultRepaymentView.setModifyTime(new Date());
        this.baseMapper.updateYbAppealResultRepaymentView(ybAppealResultRepaymentView);
    }

    @Override
    @Transactional
    public void deleteYbAppealResultRepaymentViews(String[] Ids) {
        List<String> list = Arrays.asList(Ids);
        this.baseMapper.deleteBatchIds(list);
    }


}