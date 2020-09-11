package cc.mrbird.febs.yb.service.impl;

import cc.mrbird.febs.common.domain.QueryRequest;
import cc.mrbird.febs.common.utils.SortUtil;
import cc.mrbird.febs.yb.entity.YbHandleVerifyDataView;
import cc.mrbird.febs.yb.dao.YbHandleVerifyDataViewMapper;
import cc.mrbird.febs.yb.service.IYbHandleVerifyDataViewService;
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
 * @since 2020-08-28
 */
@Slf4j
@Service("IYbHandleVerifyDataViewService")
@Transactional(propagation = Propagation.SUPPORTS, readOnly = true, rollbackFor = Exception.class)
public class YbHandleVerifyDataViewServiceImpl extends ServiceImpl<YbHandleVerifyDataViewMapper, YbHandleVerifyDataView> implements IYbHandleVerifyDataViewService {


    @Override
    public IPage<YbHandleVerifyDataView> findYbHandleVerifyDataViews(QueryRequest request, YbHandleVerifyDataView ybHandleVerifyDataView) {
        try {
            LambdaQueryWrapper<YbHandleVerifyDataView> queryWrapper = new LambdaQueryWrapper<>();
            String sql = "(";
            sql += " applyDateStr='" + ybHandleVerifyDataView.getApplyDateStr() + "' ";

            if (ybHandleVerifyDataView.getState() != null) {
                sql += " AND STATE = " + ybHandleVerifyDataView.getState();
            }

            sql += ")";
            if (ybHandleVerifyDataView.getCurrencyField() != null && !"".equals(ybHandleVerifyDataView.getCurrencyField())) {
                sql += " and (serialNo like'%" + ybHandleVerifyDataView.getCurrencyField() + "%'" +
                        " or billNo like'%" + ybHandleVerifyDataView.getCurrencyField() + "%'" +
                        " or proposalCode like'%" + ybHandleVerifyDataView.getCurrencyField() + "%'" +
                        " or projectCode like'%" + ybHandleVerifyDataView.getCurrencyField() + "%'" +
                        " or projectName like'%" + ybHandleVerifyDataView.getCurrencyField() + "%'" +
                        " or ruleName like'%" + ybHandleVerifyDataView.getCurrencyField() + "%'" +
                        " or deductReason like'%" + ybHandleVerifyDataView.getCurrencyField() + "%')";

            }
            queryWrapper.apply(sql);

            Page<YbHandleVerifyDataView> page = new Page<>();
            SortUtil.handlePageSort(request, page, false);//true 是属性  false是数据库字段可两个
            return this.page(page, queryWrapper);
        } catch (Exception e) {
            log.error("获取字典信息失败", e);
            return null;
        }
    }

    @Override
    public IPage<YbHandleVerifyDataView> findYbHandleVerifyDataViewList(QueryRequest request, YbHandleVerifyDataView ybHandleVerifyDataView) {
        try {
            Page<YbHandleVerifyDataView> page = new Page<>();
            SortUtil.handlePageSort(request, page, false);//true 是属性  false是数据库字段可两个
            return this.baseMapper.findYbHandleVerifyDataView(page, ybHandleVerifyDataView);
        } catch (Exception e) {
            log.error("获取VIEW失败", e);
            return null;
        }
    }

    @Override
    @Transactional
    public void createYbHandleVerifyDataView(YbHandleVerifyDataView ybHandleVerifyDataView) {
        //ybHandleVerifyDataView.setCreateTime(new Date());
        ybHandleVerifyDataView.setIsDeletemark(1);
        this.save(ybHandleVerifyDataView);
    }

    @Override
    @Transactional
    public void updateYbHandleVerifyDataView(YbHandleVerifyDataView ybHandleVerifyDataView) {
        //ybHandleVerifyDataView.setModifyTime(new Date());
        this.baseMapper.updateYbHandleVerifyDataView(ybHandleVerifyDataView);
    }

    @Override
    @Transactional
    public void deleteYbHandleVerifyDataViews(String[] Ids) {
        List<String> list = Arrays.asList(Ids);
        this.baseMapper.deleteBatchIds(list);
    }


}