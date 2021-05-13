package cc.mrbird.febs.yb.service.impl;

import cc.mrbird.febs.com.controller.DataTypeHelpers;
import cc.mrbird.febs.common.domain.QueryRequest;
import cc.mrbird.febs.common.utils.SortUtil;
import cc.mrbird.febs.yb.entity.*;
import cc.mrbird.febs.yb.dao.YbAppealResultDeductimplementViewMapper;
import cc.mrbird.febs.yb.service.IYbAppealResultDeductimplementViewService;
import cc.mrbird.febs.yb.service.IYbAppealResultReportViewService;
import cc.mrbird.febs.yb.service.IYbReconsiderResetService;
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

    @Autowired
    IYbReconsiderResetService iYbReconsiderResetService;

    @Override
    public IPage<YbAppealResultDeductimplementView> findYbAppealResultDeductimplementViews(QueryRequest request, YbAppealResultDeductimplementView ybAppealResultDeductimplementView) {
        try {
            /*
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

                if (listStr.size() == 1) {
                    if(ybAppealResultDeductimplementView.getDeductImplementId()!=null) {
                        sql += " implementDateStr = '" + ybAppealResultDeductimplementView.getApplyDateFrom() + "'";
                    }else{
                        sql += " applyDateStr = '" + ybAppealResultDeductimplementView.getApplyDateFrom() + "'";
                    }
                } else {
                    String applyDateStrForm = ybAppealResultDeductimplementView.getApplyDateFrom() + "-01";
                    String applyDateStrTo = ybAppealResultDeductimplementView.getApplyDateTo() + "-" + String.valueOf(DataTypeHelpers.stringDateFormatMaxDay(ybAppealResultDeductimplementView.getApplyDateTo() + "-01", "", false));

                    if(ybAppealResultDeductimplementView.getDeductImplementId()!=null) {
                        sql += " implementDate >= '" + applyDateStrForm + "' and implementDate <= '" + applyDateStrTo + "' ";
                    }else{
                        sql += " applyDate >= '" + applyDateStrForm + "' and applyDate <= '" + applyDateStrTo + "' ";
                    }
                }

                if (ybAppealResultDeductimplementView.getTypeno() != null) {
                    if (ybAppealResultDeductimplementView.getTypeno() == 1) {
                        sql +=  " and typeno = 1";
                    } else {
                        sql +=  " and typeno = 2";
                    }
                }

                if (ybAppealResultDeductimplementView.getDataType() != null) {
                    if (ybAppealResultDeductimplementView.getDataType() == 0) {
                        sql +=  " and dataType = 0";
                    } else {
                        sql +=  " and dataType = 1";
                    }
                }

                if (ybAppealResultDeductimplementView.getArDoctorCode() != null) {
                    sql +=  " and arDoctorCode = '" + ybAppealResultDeductimplementView.getArDoctorCode() + "'";
                }

                sql +=  ")";
                if (ybAppealResultDeductimplementView.getCurrencyField() != null && !"".equals(ybAppealResultDeductimplementView.getCurrencyField())) {
                    if (ybAppealResultDeductimplementView.getDataType() != null) {
                        if (ybAppealResultDeductimplementView.getDataType() == 0) {
                            sql += " and (serialNo like'%" + ybAppealResultDeductimplementView.getCurrencyField() + "%'" +
                                    " or billNo like'%" + ybAppealResultDeductimplementView.getCurrencyField() + "%'" +
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
            }*/
            return null;
        } catch (Exception e) {
            log.error("获取字典信息失败", e);
            return null;
        }

    }


    //还款管理 编辑
    @Override
    public IPage<YbAppealResultDeductimplementView> findYbAppealResultDeductimplementViewList(QueryRequest
                                                                                                      request, YbAppealResultDeductimplementView ybAppealResultDeductimplementView) {
        try {
            LambdaQueryWrapper<YbAppealResultDeductimplementView> queryWrapper = new LambdaQueryWrapper<>();
            if (ybAppealResultDeductimplementView.getDeductImplementId() != null) {
                queryWrapper.eq(YbAppealResultDeductimplementView::getDeductImplementId, ybAppealResultDeductimplementView.getDeductImplementId());
            }
            Page<YbAppealResultDeductimplementView> page = new Page<>();
            SortUtil.handlePageSort(request, page, false);//true 是属性  false是数据库字段可两个
//            return this.baseMapper.findYbAppealResultDeductimplementView(page, ybAppealResultDeductimplementView);
            return this.page(page, queryWrapper);
        } catch (Exception e) {
            log.error("获取VIEW失败", e);
            return null;
        }
    }

    //扣款落实 已扣款
    @Override
    public IPage<YbAppealResultDeductimplementView> findAppealResultDeductimplementViews(QueryRequest request, YbAppealResultDeductimplementView ybAppealResultDeductimplementView, boolean isUser) {
        try {
            Page<YbAppealResultDeductimplementView> page = new Page<>();
            List<String> listStr = new ArrayList<>();
            //ApplyDateFrom getApplyDateTo 存储的格式是 2020-09
            if (ybAppealResultDeductimplementView.getApplyDateFrom().equals(ybAppealResultDeductimplementView.getApplyDateTo())) {
                listStr.add(ybAppealResultDeductimplementView.getApplyDateFrom());
            } else {
                listStr = DataTypeHelpers.stringApplyDateStrToList(ybAppealResultDeductimplementView.getApplyDateFrom(), ybAppealResultDeductimplementView.getApplyDateTo());
            }
            if (listStr.size() > 0) {
                if (listStr.size() == 1) {
                    ybAppealResultDeductimplementView.setImplementDateStr(ybAppealResultDeductimplementView.getApplyDateFrom());
                } else {
                    String applyDateStrForm = ybAppealResultDeductimplementView.getApplyDateFrom() + "-01";
                    String applyDateStrTo = ybAppealResultDeductimplementView.getApplyDateTo() + "-" + String.valueOf(DataTypeHelpers.stringDateFormatMaxDay(ybAppealResultDeductimplementView.getApplyDateTo() + "-01", "", false));

                    ybAppealResultDeductimplementView.setImplementDateFrom(applyDateStrForm);
                    ybAppealResultDeductimplementView.setImplementDateTo(applyDateStrTo);
                }

                int count = this.baseMapper.findAppealResultDeductimplementCount(ybAppealResultDeductimplementView);
                if (count > 0) {
                    page.setSearchCount(false);
                    SortUtil.handlePageSort(request, page, false);//true 是属性  false是数据库字段可两个
                    IPage<YbAppealResultDeductimplementView> pg = this.baseMapper.findAppealResultDeductimplementView(page, ybAppealResultDeductimplementView);
                    pg.setTotal(count);
                    return pg;
                }
            }
            return page;
        } catch (Exception e) {
            log.error("获取字典信息失败", e);
            return null;
        }

    }


    //扣款落实 管理 待扣款
    @Override
    public IPage<YbAppealResultDeductimplementView> findAppealResultDmtView(QueryRequest request, YbAppealResultDeductimplementView ybAppealResultDeductimplementView) {
        try {
            Page<YbAppealResultDeductimplementView> page = new Page<>();
            YbReconsiderReset reset = iYbReconsiderResetService.findReconsiderResetByApplyDateStr(ybAppealResultDeductimplementView.getApplyDateStr(),ybAppealResultDeductimplementView.getAreaType());
            if (reset != null && reset.getState() == 1) {
                ybAppealResultDeductimplementView.setPid(reset.getId());
                ybAppealResultDeductimplementView.setApplyDateStr(reset.getApplyDateStr());
                int count = this.baseMapper.findAppealResultDmtCount(ybAppealResultDeductimplementView);
                if (count > 0) {
                    page.setSearchCount(false);
                    SortUtil.handlePageSort(request, page, false);//true 是属性  false是数据库字段可两个
                    IPage<YbAppealResultDeductimplementView> pg = this.baseMapper.findAppealResultDmtView(page, ybAppealResultDeductimplementView);
                    pg.setTotal(count);
                    return pg;
                }
            }
            return page;

        } catch (Exception e) {
            log.error("获取VIEW失败", e);
            return null;
        }
    }

    //扣款落实 医生 待扣款
    @Override
    public IPage<YbAppealResultDeductimplementView> findAppealResultDmtUserView(QueryRequest request, YbAppealResultDeductimplementView ybAppealResultDeductimplementView) {
        try {
            Page<YbAppealResultDeductimplementView> page = new Page<>();
            List<String> listStr = new ArrayList<>();
            //ApplyDateFrom getApplyDateTo 存储的格式是 2020-09
            if (ybAppealResultDeductimplementView.getApplyDateFrom().equals(ybAppealResultDeductimplementView.getApplyDateTo())) {
                listStr.add(ybAppealResultDeductimplementView.getApplyDateFrom());
            } else {
                listStr = DataTypeHelpers.stringApplyDateStrToList(ybAppealResultDeductimplementView.getApplyDateFrom(), ybAppealResultDeductimplementView.getApplyDateTo());
            }
            if (listStr.size() > 0) {
                List<YbReconsiderReset> resetList = iYbReconsiderResetService.findReconsiderResetByApplyDateStr(listStr,ybAppealResultDeductimplementView.getAreaType());
                listStr.clear();
                for (YbReconsiderReset rs : resetList) {
                    if (rs.getState() == 1) {
                        listStr.add(rs.getId());
                    }
                }
                if (listStr.size() > 0) {
                    if (listStr.size() > 1) {
                        List<YbReconsiderReset> orderResetList = resetList.stream().sorted(Comparator.comparing(YbReconsiderReset::getApplyDate)).collect(Collectors.toList());
                        YbReconsiderReset resetForm = orderResetList.get(0);
                        YbReconsiderReset resetTo = orderResetList.get(orderResetList.size() - 1);
                        String applyDateStrForm = resetForm.getApplyDateStr() + "-01";
                        String applyDateStrTo = resetTo.getApplyDateStr() + "-" + String.valueOf(DataTypeHelpers.stringDateFormatMaxDay(resetTo.getApplyDateStr() + "-01", "", false));
                        ybAppealResultDeductimplementView.setPid(null);
                        ybAppealResultDeductimplementView.setApplyDateStr(null);
                        ybAppealResultDeductimplementView.setApplyDateFrom(applyDateStrForm);
                        ybAppealResultDeductimplementView.setApplyDateTo(applyDateStrTo);
                        listStr.clear();
                        for(YbReconsiderReset item : orderResetList){
                            listStr.add(item.getId());
                        }
                    } else {
//                        String idStr = listStr.get(0);
//                        resetList = resetList.stream().filter(s->s.getId().equals(idStr)).collect(Collectors.toList());
                        ybAppealResultDeductimplementView.setApplyDateFrom(null);
                        ybAppealResultDeductimplementView.setApplyDateTo(null);
                        ybAppealResultDeductimplementView.setPid(resetList.get(0).getId());
                        ybAppealResultDeductimplementView.setApplyDateStr(resetList.get(0).getApplyDateStr());
                        listStr.clear();
                    }
                    int count = this.baseMapper.findAppealResultDmtUserCount(ybAppealResultDeductimplementView,listStr);
                    if (count > 0) {
                        page.setSearchCount(false);
                        SortUtil.handlePageSort(request, page, false);//true 是属性  false是数据库字段可两个
                        IPage<YbAppealResultDeductimplementView> pg = this.baseMapper.findAppealResultDmtUserView(page, ybAppealResultDeductimplementView,listStr);
                        pg.setTotal(count);
                        return pg;
                    }
                }
            }
            return page;

        } catch (Exception e) {
            log.error("获取VIEW失败", e);
            return null;
        }
    }

    @Override
    @Transactional
    public void createYbAppealResultDeductimplementView(YbAppealResultDeductimplementView
                                                                ybAppealResultDeductimplementView) {

        this.save(ybAppealResultDeductimplementView);
    }

    @Override
    @Transactional
    public void updateYbAppealResultDeductimplementView(YbAppealResultDeductimplementView
                                                                ybAppealResultDeductimplementView) {
        this.baseMapper.updateYbAppealResultDeductimplementView(ybAppealResultDeductimplementView);
    }

    @Override
    @Transactional
    public void deleteYbAppealResultDeductimplementViews(String[] Ids) {
        List<String> list = Arrays.asList(Ids);
        this.baseMapper.deleteBatchIds(list);
    }


}