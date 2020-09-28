package cc.mrbird.febs.yb.service.impl;

import cc.mrbird.febs.com.controller.DataTypeHelpers;
import cc.mrbird.febs.common.domain.QueryRequest;
import cc.mrbird.febs.common.utils.SortUtil;
import cc.mrbird.febs.yb.entity.YbAppealResultDeductimplementView;
import cc.mrbird.febs.yb.dao.YbAppealResultDeductimplementViewMapper;
import cc.mrbird.febs.yb.entity.YbAppealResultView;
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

import java.util.*;
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
    public IPage<YbAppealResultDeductimplementView> findYbAppealResultDeductimplementViews(QueryRequest request, YbAppealResultDeductimplementView ybAppealResultDeductimplementView) {
        try {
            LambdaQueryWrapper<YbAppealResultDeductimplementView> queryWrapper = new LambdaQueryWrapper<>();
            List<String> listStr = new ArrayList<>();
            //ApplyDateFrom getApplyDateTo 存储的格式是 2020-09
            if (ybAppealResultDeductimplementView.getApplyDateFrom().equals(ybAppealResultDeductimplementView.getApplyDateTo())) {
                listStr.add(ybAppealResultDeductimplementView.getApplyDateFrom());
            } else {
                listStr = DataTypeHelpers.stringApplyDateStrToList(ybAppealResultDeductimplementView.getApplyDateFrom(), ybAppealResultDeductimplementView.getApplyDateTo());
            }
            if (listStr.size() > 0) {
                String sql = "(";

                // String sqlApplyDateStr = "";
                if (listStr.size() == 1) {
//                    sqlApplyDateStr = listStr.get(0);
                    sql += " applyDateStr = '" + ybAppealResultDeductimplementView.getApplyDateFrom() + "'";
                } else {
                    //sqlApplyDateStr = "'" + DataTypeHelpers.stringListSeparate(listStr, "','") + "'";
                    String applyDateStrForm = ybAppealResultDeductimplementView.getApplyDateFrom() + "-01";
                    String applyDateStrTo = ybAppealResultDeductimplementView.getApplyDateTo() + "-" + String.valueOf(DataTypeHelpers.stringDateFormatMaxDay(ybAppealResultDeductimplementView.getApplyDateTo() + "-01", "", false));

                    sql += " applyDate >= '" + applyDateStrForm + "' and applyDate <= '" + applyDateStrTo + "' ";
                }

                if (ybAppealResultDeductimplementView.getTypeno() != null) {
                    if (ybAppealResultDeductimplementView.getTypeno() == 1) {
                        sql = sql + " and typeno = 1";
                    } else {
                        sql = sql + " and typeno = 2";
                    }
                }

                if (ybAppealResultDeductimplementView.getDataType() != null) {
                    if (ybAppealResultDeductimplementView.getDataType() == 0) {
                        sql = sql + " and dataType = 0";
                    } else {
                        sql = sql + " and dataType = 1";
                    }
                }

                if(ybAppealResultDeductimplementView.getDeductImplementId()!=null){
                    sql = sql + " and deductImplementId IS NOT NULL";
                }else{
                    sql = sql + " and deductImplementId IS NULL";
                }

                if (ybAppealResultDeductimplementView.getArDoctorcode() != null) {
                    sql = sql + " and arDoctorCode = '" + ybAppealResultDeductimplementView.getArDoctorcode() + "'";
                }

                sql = sql + " and STATE = 2 and raResetState = 1 and sourceType = 0 ";

                sql = sql + ")";
                if (ybAppealResultDeductimplementView.getCurrencyField() != null && !"".equals(ybAppealResultDeductimplementView.getCurrencyField())) {
                    if (ybAppealResultDeductimplementView.getDataType() != null) {
                        if (ybAppealResultDeductimplementView.getDataType() == 0) {
                            sql += " and (serialNo like'%" + ybAppealResultDeductimplementView.getCurrencyField() + "%'" +
                                    " or billNo like'%" + ybAppealResultDeductimplementView.getCurrencyField() + "%'" +
                                    " or proposalCode like'%" + ybAppealResultDeductimplementView.getCurrencyField() + "%'" +
                                    " or projectCode like'%" + ybAppealResultDeductimplementView.getCurrencyField() + "%'" +
                                    " or projectName like'%" + ybAppealResultDeductimplementView.getCurrencyField() + "%'" +
                                    " or ruleName like'%" + ybAppealResultDeductimplementView.getCurrencyField() + "%'" +
                                    " or deductReason like'%" + ybAppealResultDeductimplementView.getCurrencyField() + "%')";
                        } else {
                            sql += " and (serialNo like'%" + ybAppealResultDeductimplementView.getCurrencyField() + "%'" +
                                    " or billNo like'%" + ybAppealResultDeductimplementView.getCurrencyField() + "%'" +
                                    " or ruleName like'%" + ybAppealResultDeductimplementView.getCurrencyField() + "%'" +
                                    " or personalNo like'%" + ybAppealResultDeductimplementView.getCurrencyField() + "%')";
                        }
                    } else {
                        sql += " and (serialNo like'%" + ybAppealResultDeductimplementView.getCurrencyField() + "%'" +
                                " or billNo like'%" + ybAppealResultDeductimplementView.getCurrencyField() + "%'" +
                                " or proposalCode like'%" + ybAppealResultDeductimplementView.getCurrencyField() + "%'" +
                                " or projectCode like'%" + ybAppealResultDeductimplementView.getCurrencyField() + "%'" +
                                " or projectName like'%" + ybAppealResultDeductimplementView.getCurrencyField() + "%'" +
                                " or ruleName like'%" + ybAppealResultDeductimplementView.getCurrencyField() + "%'" +
                                " or deductReason like'%" + ybAppealResultDeductimplementView.getCurrencyField() + "%')";
                    }
                }
                queryWrapper.apply(sql);


                Page<YbAppealResultDeductimplementView> page = new Page<>();
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
    public IPage<YbAppealResultDeductimplementView> findYbAppealResultDeductimplementViewList(QueryRequest
                                                                                                      request, YbAppealResultDeductimplementView ybAppealResultDeductimplementView) {
        try {
            Page<YbAppealResultDeductimplementView> page = new Page<>();
            SortUtil.handlePageSort(request, page, false);//true 是属性  false是数据库字段可两个
            return this.baseMapper.findYbAppealResultDeductimplementView(page, ybAppealResultDeductimplementView);
        } catch (Exception e) {
            log.error("获取VIEW失败", e);
            return null;
        }
    }

    @Override
    @Transactional
    public void createYbAppealResultDeductimplementView(YbAppealResultDeductimplementView
                                                                ybAppealResultDeductimplementView) {
        ybAppealResultDeductimplementView.setCreateTime(new Date());
        ybAppealResultDeductimplementView.setIsDeletemark(1);
        this.save(ybAppealResultDeductimplementView);
    }

    @Override
    @Transactional
    public void updateYbAppealResultDeductimplementView(YbAppealResultDeductimplementView
                                                                ybAppealResultDeductimplementView) {
        ybAppealResultDeductimplementView.setModifyTime(new Date());
        this.baseMapper.updateYbAppealResultDeductimplementView(ybAppealResultDeductimplementView);
    }

    @Override
    @Transactional
    public void deleteYbAppealResultDeductimplementViews(String[] Ids) {
        List<String> list = Arrays.asList(Ids);
        this.baseMapper.deleteBatchIds(list);
    }


}