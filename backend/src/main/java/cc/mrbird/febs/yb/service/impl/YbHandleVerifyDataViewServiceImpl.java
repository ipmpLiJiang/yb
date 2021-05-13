package cc.mrbird.febs.yb.service.impl;

import cc.mrbird.febs.common.domain.QueryRequest;
import cc.mrbird.febs.common.utils.SortUtil;
import cc.mrbird.febs.yb.entity.YbAppealResultReportView;
import cc.mrbird.febs.yb.entity.YbHandleVerify;
import cc.mrbird.febs.yb.entity.YbHandleVerifyDataView;
import cc.mrbird.febs.yb.dao.YbHandleVerifyDataViewMapper;
import cc.mrbird.febs.yb.service.IYbHandleVerifyDataViewService;
import cc.mrbird.febs.yb.service.IYbHandleVerifyService;
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

    @Autowired
    IYbHandleVerifyService iYbHandleVerifyService;

    @Override
    public IPage<YbHandleVerifyDataView> findYbHandleVerifyDataViews(QueryRequest request, YbHandleVerifyDataView ybHandleVerifyDataView) {
        try {
            Page<YbHandleVerifyDataView> page = new Page<>();
            YbHandleVerify ybHandleVerify = iYbHandleVerifyService.findYbHandleVerifyApplyDateStr(ybHandleVerifyDataView.getApplyDateStr(),ybHandleVerifyDataView.getAreaType());
            if(ybHandleVerify!=null){
                ybHandleVerifyDataView.setPid(ybHandleVerify.getId());
                int count = this.baseMapper.findHandleVerifyDataCount(ybHandleVerifyDataView);
                if(count>0) {
                    page.setSearchCount(false);
                    SortUtil.handlePageSort(request, page, false);//true 是属性  false是数据库字段可两个
                    IPage<YbHandleVerifyDataView> pg = this.baseMapper.findHandleVerifyDataView(page, ybHandleVerifyDataView);
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
//        ybHandleVerifyDataView.setIsDeletemark(1);
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