package cc.mrbird.febs.yb.service.impl;

import cc.mrbird.febs.common.domain.QueryRequest;
import cc.mrbird.febs.common.utils.SortUtil;
import cc.mrbird.febs.yb.entity.YbAppealManage;
import cc.mrbird.febs.yb.entity.YbAppealManageView;
import cc.mrbird.febs.yb.dao.YbAppealManageViewMapper;
import cc.mrbird.febs.yb.entity.YbReconsiderVerify;
import cc.mrbird.febs.yb.entity.YbReconsiderVerifyView;
import cc.mrbird.febs.yb.service.IYbAppealManageViewService;
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
 * @since 2020-07-30
 */
@Slf4j
@Service("IYbAppealManageViewService")
@Transactional(propagation = Propagation.SUPPORTS, readOnly = true, rollbackFor = Exception.class)
public class YbAppealManageViewServiceImpl extends ServiceImpl<YbAppealManageViewMapper, YbAppealManageView> implements IYbAppealManageViewService {

    @Override
    public IPage<YbAppealManageView> findYbAppealManageViews(QueryRequest request, YbAppealManageView ybAppealManageView) {
        try {
            LambdaQueryWrapper<YbAppealManageView> queryWrapper = new LambdaQueryWrapper<>();
            Page<YbAppealManageView> page = new Page<>();

            String sql = "(";
            sql += " applyDateStr='" + ybAppealManageView.getApplyDateStr() + "' ";

            if (ybAppealManageView.getAcceptState() != null) {
                sql += " and acceptState = " + ybAppealManageView.getAcceptState();
            }
            sql += ")";
            if (ybAppealManageView.getCurrencyField() != null && !"".equals(ybAppealManageView.getCurrencyField())) {
                sql += " and (serialNo like'%" + ybAppealManageView.getCurrencyField() + "%'" +
                        " or billNo like'%" + ybAppealManageView.getCurrencyField() + "%'" +
                        " or orderNumber = '" + ybAppealManageView.getCurrencyField() + "'" +
                        " or proposalCode like'%" + ybAppealManageView.getCurrencyField() + "%'" +
                        " or projectCode like'%" + ybAppealManageView.getCurrencyField() + "%'" +
                        " or ruleName like'%" + ybAppealManageView.getCurrencyField() + "%'" +
                        " or projectName like'%" + ybAppealManageView.getCurrencyField() + "%'" +
                        " or deductReason like'%" + ybAppealManageView.getCurrencyField() + "%')";
            }
            queryWrapper.apply(sql);
            SortUtil.handlePageSort(request, page, false);//true 是属性  false是数据库字段可两个
            return this.page(page, queryWrapper);
        } catch (Exception e) {
            log.error("获取字典信息失败", e);
            return null;
        }
    }

    @Override
    public IPage<YbAppealManageView> findAppealManageUserViews(QueryRequest request, YbAppealManageView ybAppealManageView) {
        try {
            LambdaQueryWrapper<YbAppealManageView> queryWrapper = new LambdaQueryWrapper<>();
            Page<YbAppealManageView> page = new Page<>();

            String sql = "(";
            sql += " applyDateStr='" + ybAppealManageView.getApplyDateStr() + "' ";

            sql += " and readyDoctorCode ='" + ybAppealManageView.getReadyDoctorCode() + "'";

            if (ybAppealManageView.getAcceptState() != null) {
                sql += " and acceptState = " + ybAppealManageView.getAcceptState();
            }
            sql += ")";
            if (ybAppealManageView.getCurrencyField() != null && !"".equals(ybAppealManageView.getCurrencyField())) {
                sql += " and (serialNo like'%" + ybAppealManageView.getCurrencyField() + "%'" +
                        " or orderNumber = '" + ybAppealManageView.getCurrencyField() + "'" +
                        " or billNo like'%" + ybAppealManageView.getCurrencyField() + "%'" +
                        " or proposalCode like'%" + ybAppealManageView.getCurrencyField() + "%'" +
                        " or projectCode like'%" + ybAppealManageView.getCurrencyField() + "%'" +
                        " or ruleName like'%" + ybAppealManageView.getCurrencyField() + "%'" +
                        " or projectName like'%" + ybAppealManageView.getCurrencyField() + "%'" +
                        " or deductReason like'%" + ybAppealManageView.getCurrencyField() + "%')";
            }
            queryWrapper.apply(sql);
            SortUtil.handlePageSort(request, page, false);//true 是属性  false是数据库字段可两个
            return this.page(page, queryWrapper);
        } catch (Exception e) {
            log.error("获取字典信息失败", e);
            return null;
        }
    }

    @Override
    public IPage<YbAppealManageView> findYbAppealManageViewList(QueryRequest request, YbAppealManageView ybAppealManageView) {
        try {
            Page<YbAppealManageView> page = new Page<>();
            SortUtil.handlePageSort(request, page, false);//true 是属性  false是数据库字段可两个
            return this.baseMapper.findYbAppealManageView(page, ybAppealManageView);
        } catch (Exception e) {
            log.error("获取VIEW失败", e);
            return null;
        }
    }

    @Override
    public List<YbAppealManageView> findAppealManageViewList(YbAppealManageView ybAppealManageView) {
        LambdaQueryWrapper<YbAppealManageView> wrapper = new LambdaQueryWrapper<>();
        if (ybAppealManageView.getTypeno() != null) {
            wrapper.eq(YbAppealManageView::getTypeno, ybAppealManageView.getTypeno());
        }
        if (ybAppealManageView.getAcceptState() != null) {
            wrapper.eq(YbAppealManageView::getAcceptState, ybAppealManageView.getAcceptState());
        }
        if (ybAppealManageView.getApplyDateStr() != null) {
            wrapper.eq(YbAppealManageView::getApplyDateStr, ybAppealManageView.getApplyDateStr());
        }
        if (ybAppealManageView.getSourceType() != null) {
            wrapper.eq(YbAppealManageView::getSourceType, ybAppealManageView.getSourceType());//
        }
        if (ybAppealManageView.getDataType() != null) {
            wrapper.eq(YbAppealManageView::getDataType, ybAppealManageView.getDataType());
        }
        return this.list(wrapper);
    }

    @Override
    @Transactional
    public void createYbAppealManageView(YbAppealManageView ybAppealManageView) {
        ybAppealManageView.setCreateTime(new Date());
        ybAppealManageView.setIsDeletemark(1);
        this.save(ybAppealManageView);
    }

    @Override
    @Transactional
    public void updateYbAppealManageView(YbAppealManageView ybAppealManageView) {
        ybAppealManageView.setModifyTime(new Date());
        this.baseMapper.updateYbAppealManageView(ybAppealManageView);
    }

    @Override
    @Transactional
    public void deleteYbAppealManageViews(String[] Ids) {
        List<String> list = Arrays.asList(Ids);
        this.baseMapper.deleteBatchIds(list);
    }


}