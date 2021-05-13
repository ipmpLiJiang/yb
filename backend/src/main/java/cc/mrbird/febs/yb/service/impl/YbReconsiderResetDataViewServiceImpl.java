package cc.mrbird.febs.yb.service.impl;

import cc.mrbird.febs.common.domain.QueryRequest;
import cc.mrbird.febs.common.utils.SortUtil;
import cc.mrbird.febs.yb.entity.*;
import cc.mrbird.febs.yb.dao.YbReconsiderResetDataViewMapper;
import cc.mrbird.febs.yb.service.IYbReconsiderResetDataViewService;
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
@Autowired
    IYbReconsiderResetService iYbReconsiderResetService;
    @Override
    public IPage<YbReconsiderResetDataView> findYbReconsiderResetDataViews(QueryRequest request, YbReconsiderResetDataView ybReconsiderResetDataView) {
        try {
            Page<YbReconsiderResetDataView> page = new Page<>();
            YbReconsiderReset reconsiderReset = iYbReconsiderResetService.findReconsiderResetByApplyDateStr(ybReconsiderResetDataView.getApplyDateStr(),ybReconsiderResetDataView.getAreaType());
            if(reconsiderReset!=null){
                ybReconsiderResetDataView.setResetId(reconsiderReset.getId());
                int count = this.baseMapper.findReconsiderResetDataCount(ybReconsiderResetDataView);
                if(count>0) {
                    page.setSearchCount(false);
                    SortUtil.handlePageSort(request, page, false);//true 是属性  false是数据库字段可两个
                    IPage<YbReconsiderResetDataView> pg = this.baseMapper.findReconsiderResetDataView(page, ybReconsiderResetDataView);
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

    @Override
    public IPage<YbReconsiderResetDataView> findReconsiderResetDataViews(QueryRequest request, YbReconsiderResetDataView ybReconsiderResetDataView) {
        try {
            Page<YbReconsiderResetDataView> page = new Page<>();
            YbReconsiderReset reconsiderReset = iYbReconsiderResetService.findReconsiderResetByApplyDateStr(ybReconsiderResetDataView.getApplyDateStr(),ybReconsiderResetDataView.getAreaType());
            if(reconsiderReset!=null){
                ybReconsiderResetDataView.setResetId(reconsiderReset.getId());
                int count = this.baseMapper.findReconsiderResetDataNotCount(ybReconsiderResetDataView);
                if(count>0) {
                    page.setSearchCount(false);
                    SortUtil.handlePageSort(request, page, false);//true 是属性  false是数据库字段可两个
                    IPage<YbReconsiderResetDataView> pg = this.baseMapper.findReconsiderResetDataNotView(page, ybReconsiderResetDataView);
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

    @Override
    public IPage<YbReconsiderResetDataView> findYbReconsiderResetDataViewList(QueryRequest request, YbReconsiderResetDataView ybReconsiderResetDataView) {
        try {
            Page<YbReconsiderResetDataView> page = new Page<>();
            SortUtil.handlePageSort(request, page, false);//true 是属性  false是数据库字段可两个
            return this.baseMapper.findReconsiderResetDataView(page, ybReconsiderResetDataView);
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
        List<YbReconsiderResetDataView> list = new ArrayList<>();
        try {
            YbReconsiderReset reconsiderReset = iYbReconsiderResetService.findReconsiderResetByApplyDateStr(ybReconsiderResetDataView.getApplyDateStr(),ybReconsiderResetDataView.getAreaType());
            if(reconsiderReset!=null) {
                ybReconsiderResetDataView.setResetId(reconsiderReset.getId());
                list = this.baseMapper.findReconsiderResetDataList(ybReconsiderResetDataView);
            }
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