package cc.mrbird.febs.yb.service.impl;

import cc.mrbird.febs.com.controller.DataTypeHelpers;
import cc.mrbird.febs.common.domain.QueryRequest;
import cc.mrbird.febs.common.utils.SortUtil;
import cc.mrbird.febs.yb.entity.YbAppealResultReportView;
import cc.mrbird.febs.yb.dao.YbAppealResultReportViewMapper;
import cc.mrbird.febs.yb.entity.YbAppealResultView;
import cc.mrbird.febs.yb.service.IYbAppealResultReportViewService;
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
@Service("IYbAppealResultReportViewService")
@Transactional(propagation = Propagation.SUPPORTS, readOnly = true, rollbackFor = Exception.class)
public class YbAppealResultReportViewServiceImpl extends ServiceImpl<YbAppealResultReportViewMapper, YbAppealResultReportView> implements IYbAppealResultReportViewService {


    @Override
    public IPage<YbAppealResultReportView> findYbAppealResultReportViews(QueryRequest request, YbAppealResultReportView ybAppealResultReportView) {
        try {
            LambdaQueryWrapper<YbAppealResultReportView> queryWrapper = new LambdaQueryWrapper<>();
            List<String> listStr = new ArrayList<>();
            //ApplyDateFrom getApplyDateTo 存储的格式是 2020-09
            if (ybAppealResultReportView.getApplyDateFrom().equals(ybAppealResultReportView.getApplyDateTo())) {
                listStr.add(ybAppealResultReportView.getApplyDateFrom());
            } else {
                listStr = DataTypeHelpers.stringApplyDateStrToList(ybAppealResultReportView.getApplyDateFrom(), ybAppealResultReportView.getApplyDateTo());
            }
            if (listStr.size() > 0) {
                String sql = "(";

                // String sqlApplyDateStr = "";
                if (listStr.size() == 1) {
//                    sqlApplyDateStr = listStr.get(0);
                    sql += " applyDateStr = '" + ybAppealResultReportView.getApplyDateFrom() + "'";
                } else {
                    //sqlApplyDateStr = "'" + DataTypeHelpers.stringListSeparate(listStr, "','") + "'";
                    String applyDateStrForm = ybAppealResultReportView.getApplyDateFrom() + "-01";
                    String applyDateStrTo = ybAppealResultReportView.getApplyDateTo() + "-" + String.valueOf(DataTypeHelpers.stringDateFormatMaxDay(ybAppealResultReportView.getApplyDateTo() + "-01", "", false));

                    sql += " applyDate >= '" + applyDateStrForm + "' and applyDate <= '" + applyDateStrTo + "' ";
                }

                if (ybAppealResultReportView.getTypeno() != null) {
                    if (ybAppealResultReportView.getTypeno() == 1) {
                        sql +=  " and typeno = 1";
                    } else {
                        sql +=  " and typeno = 2";
                    }
                }
                if (ybAppealResultReportView.getDataType() != null) {
                    if (ybAppealResultReportView.getDataType() == 0) {
                        sql +=  " and dataType = 0";
                    } else {
                        sql +=  " and dataType = 1";
                    }
                }
                if (ybAppealResultReportView.getSourceType() != null) {
                    sql +=  " and sourceType = " + ybAppealResultReportView.getSourceType();
                }
                if (ybAppealResultReportView.getState() != null) {
                    if (ybAppealResultReportView.getState() == 1) {
                        sql +=  " and (STATE = 1 or (STATE = 2 and (repayState = 1 or repayState = 3)))";
                    } else {
                        sql +=  " and STATE = 2 and repayState = 2";
                    }
                }

                if (ybAppealResultReportView.getArDoctorCode() != null) {
                    sql +=  " and arDoctorCode = '" + ybAppealResultReportView.getArDoctorCode() + "'";
                }

                sql +=  " and raResetState = 1";

                sql +=  ")";
                if (ybAppealResultReportView.getCurrencyField() != null && !"".equals(ybAppealResultReportView.getCurrencyField())) {
                    if (ybAppealResultReportView.getDataType() != null) {
                        if (ybAppealResultReportView.getDataType() == 0) {
                            sql += " and (serialNo like'%" + ybAppealResultReportView.getCurrencyField() + "%'" +
                                    " or billNo like'%" + ybAppealResultReportView.getCurrencyField() + "%'" +
                                    " or proposalCode like'%" + ybAppealResultReportView.getCurrencyField() + "%'" +
                                    " or projectCode like'%" + ybAppealResultReportView.getCurrencyField() + "%'" +
                                    " or projectName like'%" + ybAppealResultReportView.getCurrencyField() + "%'" +
                                    " or ruleName like'%" + ybAppealResultReportView.getCurrencyField() + "%'" +
                                    " or deductReason like'%" + ybAppealResultReportView.getCurrencyField() + "%')";
                        } else {
                            sql += " and (serialNo like'%" + ybAppealResultReportView.getCurrencyField() + "%'" +
                                    " or billNo like'%" + ybAppealResultReportView.getCurrencyField() + "%'" +
                                    " or ruleName like'%" + ybAppealResultReportView.getCurrencyField() + "%'" +
                                    " or personalNo like'%" + ybAppealResultReportView.getCurrencyField() + "%')";
                        }
                    } else {
                        sql += " and (serialNo like'%" + ybAppealResultReportView.getCurrencyField() + "%'" +
                                " or billNo like'%" + ybAppealResultReportView.getCurrencyField() + "%'" +
                                " or proposalCode like'%" + ybAppealResultReportView.getCurrencyField() + "%'" +
                                " or projectCode like'%" + ybAppealResultReportView.getCurrencyField() + "%'" +
                                " or projectName like'%" + ybAppealResultReportView.getCurrencyField() + "%'" +
                                " or ruleName like'%" + ybAppealResultReportView.getCurrencyField() + "%'" +
                                " or deductReason like'%" + ybAppealResultReportView.getCurrencyField() + "%')";
                    }
                }
                queryWrapper.apply(sql);
                Page<YbAppealResultReportView> page = new Page<>();
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
    public IPage<YbAppealResultReportView> findYbAppealResultReportViewList(QueryRequest request, YbAppealResultReportView ybAppealResultReportView) {
        try {
            Page<YbAppealResultReportView> page = new Page<>();
            SortUtil.handlePageSort(request, page, false);//true 是属性  false是数据库字段可两个
            return this.baseMapper.findYbAppealResultReportView(page, ybAppealResultReportView);
        } catch (Exception e) {
            log.error("获取VIEW失败", e);
            return null;
        }
    }

    @Override
    @Transactional
    public void createYbAppealResultReportView(YbAppealResultReportView ybAppealResultReportView) {
        ybAppealResultReportView.setCreateTime(new Date());
        ybAppealResultReportView.setIsDeletemark(1);
        this.save(ybAppealResultReportView);
    }

    @Override
    @Transactional
    public void updateYbAppealResultReportView(YbAppealResultReportView ybAppealResultReportView) {
        ybAppealResultReportView.setModifyTime(new Date());
        this.baseMapper.updateYbAppealResultReportView(ybAppealResultReportView);
    }

    @Override
    @Transactional
    public void deleteYbAppealResultReportViews(String[] Ids) {
        List<String> list = Arrays.asList(Ids);
        this.baseMapper.deleteBatchIds(list);
    }

    @Override
    public List<YbAppealResultReportView> findYbAppealResultReportLists(YbAppealResultReportView ybAppealResultReportView) {
        try {
            LambdaQueryWrapper<YbAppealResultReportView> queryWrapper = new LambdaQueryWrapper<>();
            List<String> listStr = new ArrayList<>();
            //ApplyDateFrom getApplyDateTo 存储的格式是 2020-09
            if (ybAppealResultReportView.getApplyDateFrom().equals(ybAppealResultReportView.getApplyDateTo())) {
                listStr.add(ybAppealResultReportView.getApplyDateFrom());
            } else {
                listStr = DataTypeHelpers.stringApplyDateStrToList(ybAppealResultReportView.getApplyDateFrom(), ybAppealResultReportView.getApplyDateTo());
            }
            if (listStr.size() > 0) {
                String sql = "(";

                // String sqlApplyDateStr = "";
                if (listStr.size() == 1) {
//                    sqlApplyDateStr = listStr.get(0);
                    sql += " applyDateStr = '" + ybAppealResultReportView.getApplyDateFrom() + "'";
                } else {
                    //sqlApplyDateStr = "'" + DataTypeHelpers.stringListSeparate(listStr, "','") + "'";
                    String applyDateStrForm = ybAppealResultReportView.getApplyDateFrom() + "-01";
                    String applyDateStrTo = ybAppealResultReportView.getApplyDateTo() + "-" + String.valueOf(DataTypeHelpers.stringDateFormatMaxDay(ybAppealResultReportView.getApplyDateTo() + "-01", "", false));

                    sql += " applyDate >= '" + applyDateStrForm + "' and applyDate <= '" + applyDateStrTo + "' ";
                }

                if (ybAppealResultReportView.getTypeno() != null) {
                    if (ybAppealResultReportView.getTypeno() == 1) {
                        sql +=  " and typeno = 1";
                    } else {
                        sql +=  " and typeno = 2";
                    }
                }
                if (ybAppealResultReportView.getDataType() != null) {
                    if (ybAppealResultReportView.getDataType() == 0) {
                        sql +=  " and dataType = 0";
                    } else {
                        sql +=  " and dataType = 1";
                    }
                }
                if (ybAppealResultReportView.getSourceType() != null) {
                    sql +=  " and sourceType = " + ybAppealResultReportView.getSourceType();
                }
                if (ybAppealResultReportView.getState() != null) {
                    if (ybAppealResultReportView.getState() == 1) {
                        sql +=  " and (STATE = 1 or (STATE = 2 and (repayState = 1 or repayState = 3)))";
                    } else {
                        sql +=  " and STATE = 2 and repayState = 2";
                    }
                }

                if (ybAppealResultReportView.getArDoctorCode() != null) {
                    sql +=  " and arDoctorCode = '" + ybAppealResultReportView.getArDoctorCode() + "'";
                }

                sql +=  " and raResetState = 1";

                sql +=  ")";
                if (ybAppealResultReportView.getCurrencyField() != null && !"".equals(ybAppealResultReportView.getCurrencyField())) {
                    if (ybAppealResultReportView.getDataType() != null) {
                        if (ybAppealResultReportView.getDataType() == 0) {
                            sql += " and (serialNo like'%" + ybAppealResultReportView.getCurrencyField() + "%'" +
                                    " or billNo like'%" + ybAppealResultReportView.getCurrencyField() + "%'" +
                                    " or proposalCode like'%" + ybAppealResultReportView.getCurrencyField() + "%'" +
                                    " or projectCode like'%" + ybAppealResultReportView.getCurrencyField() + "%'" +
                                    " or projectName like'%" + ybAppealResultReportView.getCurrencyField() + "%'" +
                                    " or ruleName like'%" + ybAppealResultReportView.getCurrencyField() + "%'" +
                                    " or deductReason like'%" + ybAppealResultReportView.getCurrencyField() + "%')";
                        } else {
                            sql += " and (serialNo like'%" + ybAppealResultReportView.getCurrencyField() + "%'" +
                                    " or billNo like'%" + ybAppealResultReportView.getCurrencyField() + "%'" +
                                    " or ruleName like'%" + ybAppealResultReportView.getCurrencyField() + "%'" +
                                    " or personalNo like'%" + ybAppealResultReportView.getCurrencyField() + "%')";
                        }
                    } else {
                        sql += " and (serialNo like'%" + ybAppealResultReportView.getCurrencyField() + "%'" +
                                " or billNo like'%" + ybAppealResultReportView.getCurrencyField() + "%'" +
                                " or proposalCode like'%" + ybAppealResultReportView.getCurrencyField() + "%'" +
                                " or projectCode like'%" + ybAppealResultReportView.getCurrencyField() + "%'" +
                                " or projectName like'%" + ybAppealResultReportView.getCurrencyField() + "%'" +
                                " or ruleName like'%" + ybAppealResultReportView.getCurrencyField() + "%'" +
                                " or deductReason like'%" + ybAppealResultReportView.getCurrencyField() + "%')";
                    }
                }
                queryWrapper.apply(sql);

                return this.list(queryWrapper);
            } else {
                return null;
            }

        } catch (Exception e) {
            log.error("获取字典信息失败", e);
            return null;
        }
    }
}