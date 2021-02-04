package cc.mrbird.febs.yb.service.impl;

import cc.mrbird.febs.common.domain.QueryRequest;
import cc.mrbird.febs.common.utils.SortUtil;
import cc.mrbird.febs.yb.entity.YbAppealResultView;
import cc.mrbird.febs.yb.entity.YbReconsiderResetDataView;
import cc.mrbird.febs.yb.dao.YbReconsiderResetDataViewMapper;
import cc.mrbird.febs.yb.service.IYbReconsiderResetDataViewService;
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
 * @since 2020-08-18
 */
@Slf4j
@Service("IYbReconsiderResetDataViewService")
@Transactional(propagation = Propagation.SUPPORTS, readOnly = true, rollbackFor = Exception.class)
public class YbReconsiderResetDataViewServiceImpl extends ServiceImpl<YbReconsiderResetDataViewMapper, YbReconsiderResetDataView> implements IYbReconsiderResetDataViewService {

    @Override
    public IPage<YbReconsiderResetDataView> findYbReconsiderResetDataViews(QueryRequest request, YbReconsiderResetDataView ybReconsiderResetDataView) {
        try {
            LambdaQueryWrapper<YbReconsiderResetDataView> queryWrapper = new LambdaQueryWrapper<>();
            String sql = "(";
            sql += " applyDateStr='" + ybReconsiderResetDataView.getApplyDateStr() + "' ";

            if (ybReconsiderResetDataView.getDataType() != null) {
                sql += " AND dataType = " + ybReconsiderResetDataView.getDataType();
            }
            if (ybReconsiderResetDataView.getState() != null) {
                sql += " AND STATE = " + ybReconsiderResetDataView.getState();
            }
            if (ybReconsiderResetDataView.getSeekState() != null) {
                sql += " AND seekState = " + ybReconsiderResetDataView.getSeekState();
            }

            if (ybReconsiderResetDataView.getResetType() != null) {
                sql += " AND resetType = " + ybReconsiderResetDataView.getResetType();
            }

            sql += ")";
            if (ybReconsiderResetDataView.getCurrencyField() != null && !"".equals(ybReconsiderResetDataView.getCurrencyField())) {
                if (ybReconsiderResetDataView.getDataType() != null) {
                    if (ybReconsiderResetDataView.getDataType() == 0) {
                        sql += " and (serialNo like'%" + ybReconsiderResetDataView.getCurrencyField() + "%'" +
                                " or billNo like'%" + ybReconsiderResetDataView.getCurrencyField() + "%'" +
                                " or projectCode like'%" + ybReconsiderResetDataView.getCurrencyField() + "%'" +
                                " or projectName like'%" + ybReconsiderResetDataView.getCurrencyField() + "%'" +
                                " or ruleName like'%" + ybReconsiderResetDataView.getCurrencyField() + "%'" +
                                " or deductReason like'%" + ybReconsiderResetDataView.getCurrencyField() + "%')";
                    } else {
                        sql += " and (serialNo like'%" + ybReconsiderResetDataView.getCurrencyField() + "%'" +
                                " or billNo like'%" + ybReconsiderResetDataView.getCurrencyField() + "%'" +
                                " or ruleName like'%" + ybReconsiderResetDataView.getCurrencyField() + "%'" +
                                " or personalNo like'%" + ybReconsiderResetDataView.getCurrencyField() + "%')";
                    }
                } else {
                    sql += " and (serialNo like'%" + ybReconsiderResetDataView.getCurrencyField() + "%'" +
                            " or billNo like'%" + ybReconsiderResetDataView.getCurrencyField() + "%'" +
                            " or projectCode like'%" + ybReconsiderResetDataView.getCurrencyField() + "%'" +
                            " or projectName like'%" + ybReconsiderResetDataView.getCurrencyField() + "%'" +
                            " or ruleName like'%" + ybReconsiderResetDataView.getCurrencyField() + "%'" +
                            " or deductReason like'%" + ybReconsiderResetDataView.getCurrencyField() + "%')";
                }
            }
            queryWrapper.apply(sql);

            Page<YbReconsiderResetDataView> page = new Page<>();
            SortUtil.handlePageSort(request, page, false);//true 是属性  false是数据库字段可两个
            return this.page(page, queryWrapper);
        } catch (Exception e) {
            log.error("获取字典信息失败", e);
            return null;
        }
    }

    @Override
    public IPage<YbReconsiderResetDataView> findReconsiderResetDataViews(QueryRequest request, YbReconsiderResetDataView ybReconsiderResetDataView) {
        try {
            LambdaQueryWrapper<YbReconsiderResetDataView> queryWrapper = new LambdaQueryWrapper<>();
            if (ybReconsiderResetDataView.getId() != null) {
                queryWrapper.ne(YbReconsiderResetDataView::getId, ybReconsiderResetDataView.getId());
            }

            if (ybReconsiderResetDataView.getApplyDateStr() != null) {
                queryWrapper.eq(YbReconsiderResetDataView::getApplyDateStr, ybReconsiderResetDataView.getApplyDateStr());
            }

            if (ybReconsiderResetDataView.getBillNo() != null) {
                queryWrapper.eq(YbReconsiderResetDataView::getBillNo, ybReconsiderResetDataView.getBillNo());
            }

            if (ybReconsiderResetDataView.getSerialNo() != null) {
                queryWrapper.eq(YbReconsiderResetDataView::getSerialNo, ybReconsiderResetDataView.getSerialNo());
            }

            if (ybReconsiderResetDataView.getRuleName() != null) {
                queryWrapper.eq(YbReconsiderResetDataView::getRuleName, ybReconsiderResetDataView.getRuleName());
            }

            if (ybReconsiderResetDataView.getProjectCode() != null) {
                queryWrapper.eq(YbReconsiderResetDataView::getProjectCode, ybReconsiderResetDataView.getProjectCode());
            }

            if (ybReconsiderResetDataView.getProjectName() != null) {
                queryWrapper.eq(YbReconsiderResetDataView::getProjectName, ybReconsiderResetDataView.getProjectName());
            }

            if (ybReconsiderResetDataView.getPersonalNo() != null) {
                queryWrapper.eq(YbReconsiderResetDataView::getPersonalNo, ybReconsiderResetDataView.getPersonalNo());
            }

            if (ybReconsiderResetDataView.getDataType() != null) {
                queryWrapper.eq(YbReconsiderResetDataView::getDataType, ybReconsiderResetDataView.getDataType());
            }

            if (ybReconsiderResetDataView.getSeekState() != null) {
                queryWrapper.eq(YbReconsiderResetDataView::getSeekState, ybReconsiderResetDataView.getSeekState());
            }

            if (ybReconsiderResetDataView.getState() != null) {
                queryWrapper.eq(YbReconsiderResetDataView::getState, ybReconsiderResetDataView.getState());
            }

            if (ybReconsiderResetDataView.getOrderNumber() != null) {
                queryWrapper.eq(YbReconsiderResetDataView::getOrderNumber, ybReconsiderResetDataView.getOrderNumber());
            }

            if (ybReconsiderResetDataView.getResetType() != null) {
                queryWrapper.eq(YbReconsiderResetDataView::getResetType, ybReconsiderResetDataView.getResetType());
            }

            Page<YbReconsiderResetDataView> page = new Page<>();
            SortUtil.handlePageSort(request, page, false);//true 是属性  false是数据库字段可两个
            return this.page(page, queryWrapper);
        } catch (Exception e) {
            log.error("获取字典信息失败", e);
            return null;
        }
    }

    @Override
    public IPage<YbReconsiderResetDataView> findYbReconsiderResetDataViewList(QueryRequest request, YbReconsiderResetDataView ybReconsiderResetDataView) {
        try {
            Page<YbReconsiderResetDataView> page = new Page<>();
            SortUtil.handlePageSort(request, page, false);//true 是属性  false是数据库字段可两个
            return this.baseMapper.findYbReconsiderResetDataView(page, ybReconsiderResetDataView);
        } catch (Exception e) {
            log.error("获取VIEW失败", e);
            return null;
        }
    }

    @Override
    @Transactional
    public void createYbReconsiderResetDataView(YbReconsiderResetDataView ybReconsiderResetDataView) {
//        ybReconsiderResetDataView.setCreateTime(new Date());
//        ybReconsiderResetDataView.setIsDeletemark(1);
        this.save(ybReconsiderResetDataView);
    }

    @Override
    @Transactional
    public void updateYbReconsiderResetDataView(YbReconsiderResetDataView ybReconsiderResetDataView) {
//        ybReconsiderResetDataView.setModifyTime(new Date());
        this.baseMapper.updateYbReconsiderResetDataView(ybReconsiderResetDataView);
    }

    @Override
    public List<YbReconsiderResetDataView> findYbReconsiderResetDataList(YbReconsiderResetDataView ybReconsiderResetDataView) {
        List<YbReconsiderResetDataView> list = new ArrayList<YbReconsiderResetDataView>();
        try {
            LambdaQueryWrapper<YbReconsiderResetDataView> queryWrapper = new LambdaQueryWrapper<>();
            if (ybReconsiderResetDataView.getApplyDateStr() != null) {
                queryWrapper.eq(YbReconsiderResetDataView::getApplyDateStr, ybReconsiderResetDataView.getApplyDateStr());
            }
            if (ybReconsiderResetDataView.getState() != null) {
                queryWrapper.eq(YbReconsiderResetDataView::getState, ybReconsiderResetDataView.getState());
            }
            if (ybReconsiderResetDataView.getSeekState() != null) {
                queryWrapper.eq(YbReconsiderResetDataView::getSeekState, ybReconsiderResetDataView.getSeekState());
            }
            if (ybReconsiderResetDataView.getId() != null) {
                queryWrapper.eq(YbReconsiderResetDataView::getId, ybReconsiderResetDataView.getId());
            }
            if (ybReconsiderResetDataView.getDataType() != null) {
                queryWrapper.eq(YbReconsiderResetDataView::getDataType, ybReconsiderResetDataView.getDataType());
            }
            if (ybReconsiderResetDataView.getOrderNumber() != null) {
                queryWrapper.eq(YbReconsiderResetDataView::getOrderNumber, ybReconsiderResetDataView.getOrderNumber());
            }
            if (ybReconsiderResetDataView.getResetId() != null) {
                queryWrapper.eq(YbReconsiderResetDataView::getResetId, ybReconsiderResetDataView.getResetId());
            }
            list = this.baseMapper.selectList(queryWrapper);

            return list;
        } catch (Exception e) {
            log.error("获取字典信息失败", e);
            return list;
        }
    }

    @Override
    @Transactional
    public void deleteYbReconsiderResetDataViews(String[] Ids) {
        List<String> list = Arrays.asList(Ids);
        this.baseMapper.deleteBatchIds(list);
    }


}