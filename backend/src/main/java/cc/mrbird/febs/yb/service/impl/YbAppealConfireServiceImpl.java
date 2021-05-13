package cc.mrbird.febs.yb.service.impl;

import cc.mrbird.febs.common.domain.QueryRequest;
import cc.mrbird.febs.common.utils.SortUtil;
import cc.mrbird.febs.yb.entity.YbAppealConfire;
import cc.mrbird.febs.yb.dao.YbAppealConfireMapper;
import cc.mrbird.febs.yb.entity.YbAppealConfireData;
import cc.mrbird.febs.yb.entity.YbAppealSumdept;
import cc.mrbird.febs.yb.service.IYbAppealConfireDataService;
import cc.mrbird.febs.yb.service.IYbAppealConfireService;
import cc.mrbird.febs.yb.service.IYbReconsiderApplyDataService;
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
 * 服务实现类
 * </p>
 *
 * @author viki
 * @since 2021-01-11
 */
@Slf4j
@Service("IYbAppealConfireService")
@Transactional(propagation = Propagation.SUPPORTS, readOnly = true, rollbackFor = Exception.class)
public class YbAppealConfireServiceImpl extends ServiceImpl<YbAppealConfireMapper, YbAppealConfire> implements IYbAppealConfireService {

    @Autowired
    public IYbAppealConfireDataService iYbAppealConfireDataService;

    @Override
    public IPage<YbAppealConfire> findYbAppealConfires(QueryRequest request, YbAppealConfire ybAppealConfire) {
        try {
            LambdaQueryWrapper<YbAppealConfire> queryWrapper = new LambdaQueryWrapper<>();
            queryWrapper.eq(YbAppealConfire::getIsDeletemark, 1);//1是未删 0是已删

            if (StringUtils.isNotBlank(ybAppealConfire.getCurrencyField())) {
                queryWrapper.like(YbAppealConfire::getCurrencyField, ybAppealConfire.getCurrencyField());
            }

            Page<YbAppealConfire> page = new Page<>();
            SortUtil.handlePageSort(request, page, false);//true 是属性  false是数据库字段可两个
            return this.page(page, queryWrapper);
        } catch (Exception e) {
            log.error("获取字典信息失败", e);
            return null;
        }
    }


    @Override
    public IPage<YbAppealConfire> findAppealConfireView(QueryRequest request, String doctorContent, Integer adminType, Integer areaType, String deptContent) {
        try {
            Page<YbAppealConfire> page = new Page<>();
            int count = this.baseMapper.findAppealConfireCount(doctorContent, adminType, areaType, deptContent);
            if (count > 0) {
                page.setSearchCount(false);
                SortUtil.handlePageSort(request, page, false);//true 是属性  false是数据库字段可两个
                IPage<YbAppealConfire> pg = this.baseMapper.findAppealConfireView(page, doctorContent, adminType, areaType, deptContent);
                pg.setTotal(count);
                return pg;
            }
            return page;
        } catch (Exception e) {
            log.error("获取失败", e);
            return null;
        }
    }

    @Override
    public IPage<YbAppealConfire> findAppealConfireUserView(QueryRequest request, String doctorContent, Integer adminType, Integer areaType, String deptContent, Long uid) {
        try {
            Page<YbAppealConfire> page = new Page<>();
            int count = this.baseMapper.findAppealConfireUserCount(doctorContent, adminType, areaType, deptContent, uid);
            if (count > 0) {
                page.setSearchCount(false);
                SortUtil.handlePageSort(request, page, false);//true 是属性  false是数据库字段可两个
                IPage<YbAppealConfire> pg = this.baseMapper.findAppealConfireUserView(page, doctorContent, adminType, areaType, deptContent, uid);
                pg.setTotal(count);
                return pg;
            }
            return page;
        } catch (Exception e) {
            log.error("获取失败", e);
            return null;
        }
    }

    @Override
    public IPage<YbAppealConfire> findYbAppealConfireList(QueryRequest request, YbAppealConfire ybAppealConfire) {
        try {
            Page<YbAppealConfire> page = new Page<>();
            SortUtil.handlePageSort(request, page, false);//true 是属性  false是数据库字段可两个
            return this.baseMapper.findYbAppealConfire(page, ybAppealConfire);
        } catch (Exception e) {
            log.error("获取失败", e);
            return null;
        }
    }


    @Override
    @Transactional
    public void createYbAppealConfire(YbAppealConfire ybAppealConfire) {
        ybAppealConfire.setId(UUID.randomUUID().toString());
        ybAppealConfire.setCreateTime(new Date());
        ybAppealConfire.setIsDeletemark(1);
        this.save(ybAppealConfire);
    }

    @Override
    @Transactional
    public void updateYbAppealConfire(YbAppealConfire ybAppealConfire) {
        ybAppealConfire.setModifyTime(new Date());
        this.baseMapper.updateYbAppealConfire(ybAppealConfire);
    }

    @Override
    @Transactional
    public void createAppealConfire(YbAppealConfire ybAppealConfire, List<YbAppealConfireData> createDataList) {
        this.save(ybAppealConfire);
        if (createDataList.size() > 0) {
            iYbAppealConfireDataService.saveBatch(createDataList);
        }
    }

    @Override
    @Transactional
    public void updateAppealConfire(YbAppealConfire ybAppealConfire, List<YbAppealConfireData> createDataList, List<YbAppealConfireData> updateDataList) {
        this.updateById(ybAppealConfire);
        iYbAppealConfireDataService.saveBatch(createDataList);
    }

    @Override
    @Transactional
    public void deleteYbAppealConfires(String[] Ids) {
        List<String> list = Arrays.asList(Ids);
        this.iYbAppealConfireDataService.deleteAppealConfireDataPids(Ids);
        this.baseMapper.deleteBatchIds(list);

    }

    @Override
    public YbAppealConfire findAppealConfire(YbAppealConfire appealConfire) {
        LambdaQueryWrapper<YbAppealConfire> wrapper = new LambdaQueryWrapper<>();
        if (appealConfire.getId() != null) {
            wrapper.eq(YbAppealConfire::getId, appealConfire.getId());
        }
        if (appealConfire.getAreaType() != null) {
            wrapper.eq(YbAppealConfire::getAreaType, appealConfire.getAreaType());
        }
        if (appealConfire.getDoctorCode() != null) {
            wrapper.eq(YbAppealConfire::getDoctorCode, appealConfire.getDoctorCode());
        }
        wrapper.eq(YbAppealConfire::getIsDeletemark, 1);
        List<YbAppealConfire> list = this.baseMapper.selectList(wrapper);
        if (list.size() > 0) {
            return list.get(0);
        } else {
            return null;
        }
    }

    @Override
    public List<YbAppealConfire> findAppealConfireATList(List<Integer> atIds,Integer areaType) {
        LambdaQueryWrapper<YbAppealConfire> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(YbAppealConfire::getIsDeletemark, 1);
        wrapper.eq(YbAppealConfire::getAreaType, areaType);
        wrapper.in(YbAppealConfire::getAdminType, atIds);
        return this.list(wrapper);
    }

}