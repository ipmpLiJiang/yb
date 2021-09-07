package cc.mrbird.febs.yb.service.impl;

import cc.mrbird.febs.common.domain.QueryRequest;
import cc.mrbird.febs.common.utils.SortUtil;
import cc.mrbird.febs.yb.dao.YbAppealResultResetViewMapper;
import cc.mrbird.febs.yb.entity.*;
import cc.mrbird.febs.yb.manager.YbApplyDataManager;
import cc.mrbird.febs.yb.service.*;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.annotations.Param;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

/**
 * <p>
 * VIEW 服务实现类
 * </p>
 *
 * @author viki
 * @since 2020-08-12
 */
@Slf4j
@Service("IYbAppealResultResetViewService")
@Transactional(propagation = Propagation.SUPPORTS, readOnly = true, rollbackFor = Exception.class)
public class YbAppealResultResetViewServiceImpl extends ServiceImpl<YbAppealResultResetViewMapper, YbAppealResultResetView> implements IYbAppealResultResetViewService {

    @Autowired
    IYbAppealResultResetViewService iYbAppealResultResetViewService;

    @Autowired
    IYbReconsiderResetService iYbReconsiderResetService;

    @Override
    public IPage<YbAppealResultResetView> findAppealResultResetViews(QueryRequest request, YbAppealResultResetView ybAppealResultResetView, String keyField) {
        try {
            YbReconsiderReset reconsiderReset = iYbReconsiderResetService.findReconsiderResetByApplyDateStr(ybAppealResultResetView.getApplyDateStr(), ybAppealResultResetView.getAreaType());
            Page<YbAppealResultResetView> page = new Page<>();
            if (reconsiderReset != null && reconsiderReset.getState() == 1) {
                ybAppealResultResetView.setPid(reconsiderReset.getId());
                int count = this.baseMapper.findAppealResultResetCount(ybAppealResultResetView, keyField);
                if (count > 0) {
                    page.setSearchCount(false);
                    SortUtil.handlePageSort(request, page, false);//true 是属性  false是数据库字段可两个
                    IPage<YbAppealResultResetView> pg = null;
                    pg = this.baseMapper.findAppealResultResetView(page, ybAppealResultResetView, keyField);
                    pg.setTotal(count);
                    return pg;
                } else {
                    return page;
                }
            } else {
                return page;
            }
        } catch (Exception e) {
            log.error("获取VIEW失败", e);
            return null;
        }
    }

    @Override
    public List<YbAppealResultResetView> findAppealResultResetLists(YbAppealResultResetView appealResultResetView){
        YbReconsiderReset reconsiderReset = iYbReconsiderResetService.findReconsiderResetByApplyDateStr(appealResultResetView.getApplyDateStr(), appealResultResetView.getAreaType());
        if (reconsiderReset != null && reconsiderReset.getState() == 1) {
            appealResultResetView.setPid(reconsiderReset.getId());
            return this.baseMapper.findAppealResultResetList(appealResultResetView);
        }
        return new ArrayList<>();
    }
}