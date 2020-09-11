package cc.mrbird.febs.yb.service.impl;

import cc.mrbird.febs.common.domain.QueryRequest;
import cc.mrbird.febs.common.utils.SortUtil;
import cc.mrbird.febs.yb.entity.YbReconsiderResetResultView;
import cc.mrbird.febs.yb.dao.YbReconsiderResetResultViewMapper;
import cc.mrbird.febs.yb.service.IYbReconsiderResetResultViewService;
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
 * @since 2020-09-10
 */
@Slf4j
@Service("IYbReconsiderResetResultViewService")
@Transactional(propagation = Propagation.SUPPORTS, readOnly = true, rollbackFor = Exception.class)
public class YbReconsiderResetResultViewServiceImpl extends ServiceImpl<YbReconsiderResetResultViewMapper, YbReconsiderResetResultView> implements IYbReconsiderResetResultViewService {


    @Override
    public IPage<YbReconsiderResetResultView> findYbReconsiderResetResultViews(QueryRequest request, YbReconsiderResetResultView ybReconsiderResetResultView) {
        try {
            LambdaQueryWrapper<YbReconsiderResetResultView> queryWrapper = new LambdaQueryWrapper<>();
            //queryWrapper.eq(YbReconsiderResetResultView::getIsDeletemark, 1);//1是未删 0是已删
            String sql = "(";
            sql += " applyDateStr ='" + ybReconsiderResetResultView.getApplyDateStr() + "' ";
            if (ybReconsiderResetResultView.getDataType() != null) {
                sql += " and dataType = " + ybReconsiderResetResultView.getDataType();
            }

            sql = sql + ")";
            if (ybReconsiderResetResultView.getOrderNumber() != null) {
                String orderNumbers = ybReconsiderResetResultView.getOrderNumber();
                String[] orderNumberArr = orderNumbers.split(",");
                sql += " and (";
                for (int i = 0; i < orderNumberArr.length; i++) {
                    if (i == 0) {
                        sql += " orderNumber = '" + orderNumberArr[i] + "'";
                    } else {
                        sql += " or orderNumber = '" + orderNumberArr[i] + "'";
                    }
                }
                sql += " )";
            }

            queryWrapper.apply(sql);

            Page<YbReconsiderResetResultView> page = new Page<>();
            SortUtil.handlePageSort(request, page, false);//true 是属性  false是数据库字段可两个
            return this.page(page, queryWrapper);
        } catch (
                Exception e) {
            log.error("获取字典信息失败", e);
            return null;
        }

    }

    @Override
    public IPage<YbReconsiderResetResultView> findYbReconsiderResetResultViewList(QueryRequest request, YbReconsiderResetResultView ybReconsiderResetResultView) {
        try {
            Page<YbReconsiderResetResultView> page = new Page<>();
            SortUtil.handlePageSort(request, page, false);//true 是属性  false是数据库字段可两个
            return this.baseMapper.findYbReconsiderResetResultView(page, ybReconsiderResetResultView);
        } catch (Exception e) {
            log.error("获取VIEW失败", e);
            return null;
        }
    }

    @Override
    @Transactional
    public void createYbReconsiderResetResultView(YbReconsiderResetResultView ybReconsiderResetResultView) {
//        ybReconsiderResetResultView.setCreateTime(new Date());
//        ybReconsiderResetResultView.setIsDeletemark(1);
        this.save(ybReconsiderResetResultView);
    }

    @Override
    @Transactional
    public void updateYbReconsiderResetResultView(YbReconsiderResetResultView ybReconsiderResetResultView) {
//        ybReconsiderResetResultView.setModifyTime(new Date());
        this.baseMapper.updateYbReconsiderResetResultView(ybReconsiderResetResultView);
    }

    @Override
    @Transactional
    public void deleteYbReconsiderResetResultViews(String[] Ids) {
        List<String> list = Arrays.asList(Ids);
        this.baseMapper.deleteBatchIds(list);
    }


}