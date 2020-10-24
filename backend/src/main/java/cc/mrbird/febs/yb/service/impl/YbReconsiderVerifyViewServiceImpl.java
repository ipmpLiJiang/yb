package cc.mrbird.febs.yb.service.impl;

import cc.mrbird.febs.common.domain.QueryRequest;
import cc.mrbird.febs.common.utils.SortUtil;
import cc.mrbird.febs.yb.dao.YbReconsiderVerifyViewMapper;
import cc.mrbird.febs.yb.entity.YbReconsiderVerifyView;
import cc.mrbird.febs.yb.service.IYbReconsiderVerifyViewService;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.Date;
import java.util.List;

/**
 * findYbReconsiderVerifyViews
 * <p>
 * VIEW 服务实现类
 * </p>
 *
 * @author viki
 * @since 2020-07-30
 */
@Slf4j
@Service("IYbReconsiderVerifyViewService")
@Transactional(propagation = Propagation.SUPPORTS, readOnly = true, rollbackFor = Exception.class)
public class YbReconsiderVerifyViewServiceImpl extends ServiceImpl<YbReconsiderVerifyViewMapper, YbReconsiderVerifyView> implements IYbReconsiderVerifyViewService {


    @Override
    public IPage<YbReconsiderVerifyView> findYbReconsiderVerifyViews(QueryRequest request, YbReconsiderVerifyView ybReconsiderVerifyView) {
        try {
            LambdaQueryWrapper<YbReconsiderVerifyView> queryWrapper = new LambdaQueryWrapper<>();
            //queryWrapper.eq(YbReconsiderVerifyView::getIsDeletemark, 1);//1是未删 0是已删
            queryWrapper.eq(YbReconsiderVerifyView::getApplyDateStr, ybReconsiderVerifyView.getApplyDateStr());//年月
            queryWrapper.eq(YbReconsiderVerifyView::getState, ybReconsiderVerifyView.getState());//状态
            Page<YbReconsiderVerifyView> page = new Page<>();
            SortUtil.handlePageSort(request, page, false);//true 是属性  false是数据库字段可两个
            return this.page(page, queryWrapper);
        } catch (Exception e) {
            log.error("获取字典信息失败", e);
            return null;
        }
    }

    @Override
    public IPage<YbReconsiderVerifyView> findYbReconsiderVerifyViewList(QueryRequest request, YbReconsiderVerifyView ybReconsiderVerifyView) {
        try {
            Page<YbReconsiderVerifyView> page = new Page<>();
            SortUtil.handlePageSort(request, page, false);//true 是属性  false是数据库字段可两个
            return this.baseMapper.findYbReconsiderVerifyView(page, ybReconsiderVerifyView);
        } catch (Exception e) {
            log.error("获取VIEW失败", e);
            return null;
        }
    }

    @Override
    @Transactional
    public void createYbReconsiderVerifyView(YbReconsiderVerifyView ybReconsiderVerifyView) {
        ybReconsiderVerifyView.setCreateTime(new Date());
        ybReconsiderVerifyView.setIsDeletemark(1);
        this.save(ybReconsiderVerifyView);
    }

    @Override
    @Transactional
    public void updateYbReconsiderVerifyView(YbReconsiderVerifyView ybReconsiderVerifyView) {
        ybReconsiderVerifyView.setModifyTime(new Date());
        this.baseMapper.updateYbReconsiderVerifyView(ybReconsiderVerifyView);
    }

    @Override
    @Transactional
    public void deleteYbReconsiderVerifyViews(String[] Ids) {
        List<String> list = Arrays.asList(Ids);
        this.baseMapper.deleteBatchIds(list);
    }

    @Override
    public int findReconsiderVerifyApplyDateCounts(String applyDate,Integer dataType) {
        return this.baseMapper.findReconsiderVerifyApplyDateCount(applyDate,dataType);
    }

    @Override
    public IPage<YbReconsiderVerifyView> findYbReconsiderVerifyViewNulls(QueryRequest request, YbReconsiderVerifyView ybReconsiderVerifyView,String[] searchType) {
        try {
            Page<YbReconsiderVerifyView> page = new Page<>();
            int count = this.findReconsiderVerifyApplyDateCounts(ybReconsiderVerifyView.getApplyDateStr(),ybReconsiderVerifyView.getDataType());
            if (count > 0) {
                SortUtil.handlePageSort(request, page, false);//true 是属性  false是数据库字段可两个
                return this.baseMapper.findYbReconsiderVerifyViewNull(page, ybReconsiderVerifyView,searchType);
            } else {
                return page;
            }
        } catch (Exception e) {
            log.error("获取VIEW失败", e);
            return null;
        }
    }


}