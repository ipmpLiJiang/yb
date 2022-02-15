package cc.mrbird.febs.yb.service.impl;

import cc.mrbird.febs.com.controller.DataTypeHelpers;
import cc.mrbird.febs.common.domain.QueryRequest;
import cc.mrbird.febs.common.utils.SortUtil;
import cc.mrbird.febs.yb.entity.YbReconsiderPriorityLevel;
import cc.mrbird.febs.yb.dao.YbReconsiderPriorityLevelMapper;
import cc.mrbird.febs.yb.service.IYbReconsiderPriorityLevelService;
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
 * 服务实现类
 * </p>
 *
 * @author viki
 * @since 2020-10-13
 */
@Slf4j
@Service("IYbReconsiderPriorityLevelService")
@Transactional(propagation = Propagation.SUPPORTS, readOnly = true, rollbackFor = Exception.class)
public class YbReconsiderPriorityLevelServiceImpl extends ServiceImpl<YbReconsiderPriorityLevelMapper, YbReconsiderPriorityLevel> implements IYbReconsiderPriorityLevelService {


    @Override
    public IPage<YbReconsiderPriorityLevel> findYbReconsiderPriorityLevels(QueryRequest request, YbReconsiderPriorityLevel ybReconsiderPriorityLevel) {
        try {
            LambdaQueryWrapper<YbReconsiderPriorityLevel> queryWrapper = new LambdaQueryWrapper<>();
            String sql = " IS_DELETEMARK = 1 ";
            if (ybReconsiderPriorityLevel.getState() != null) {
                sql += " and state = " + ybReconsiderPriorityLevel.getState();
            }
            if (ybReconsiderPriorityLevel.getAreaType() != null) {
                sql += " and areaType = " + ybReconsiderPriorityLevel.getAreaType();
            }
            if (ybReconsiderPriorityLevel.getCurrencyField() != null) {
                if (ybReconsiderPriorityLevel.getState() == 1 || ybReconsiderPriorityLevel.getState() == 2) {
                    sql += " and (rplName like '%" + ybReconsiderPriorityLevel.getCurrencyField() + "%' or deptCode like '%" + ybReconsiderPriorityLevel.getCurrencyField() + "%' or deptName like '%" + ybReconsiderPriorityLevel.getCurrencyField() + "%')";
                } else if (ybReconsiderPriorityLevel.getState() == 3) {
                    sql += " and (deptCode like '%" + ybReconsiderPriorityLevel.getCurrencyField() + "%' or deptName like '%" + ybReconsiderPriorityLevel.getCurrencyField() + "%')";
                } else if (ybReconsiderPriorityLevel.getState() == 4) {
                    sql += " and (doctorName like '%" + ybReconsiderPriorityLevel.getCurrencyField() + "%' or dksNameTo like '%" + ybReconsiderPriorityLevel.getCurrencyField() + "%')";
                }
            }

            queryWrapper.apply(sql);

            Page<YbReconsiderPriorityLevel> page = new Page<>();
            SortUtil.handlePageSort(request, page, false);//true 是属性  false是数据库字段可两个
            return this.page(page, queryWrapper);
        } catch (Exception e) {
            log.error("获取字典信息失败", e);
            return null;
        }
    }

    @Override
    public IPage<YbReconsiderPriorityLevel> findYbReconsiderPriorityLevelList(QueryRequest request, YbReconsiderPriorityLevel ybReconsiderPriorityLevel) {
        try {
            Page<YbReconsiderPriorityLevel> page = new Page<>();
            SortUtil.handlePageSort(request, page, false);//true 是属性  false是数据库字段可两个
            return this.baseMapper.findYbReconsiderPriorityLevel(page, ybReconsiderPriorityLevel);
        } catch (Exception e) {
            log.error("获取失败", e);
            return null;
        }
    }

    @Override
    @Transactional
    public void createYbReconsiderPriorityLevel(YbReconsiderPriorityLevel ybReconsiderPriorityLevel) {
        ybReconsiderPriorityLevel.setId(UUID.randomUUID().toString());
        ybReconsiderPriorityLevel.setCreateTime(new Date());
        ybReconsiderPriorityLevel.setIsDeletemark(1);
        String strDeptName = DataTypeHelpers.stringReplaceSetString(ybReconsiderPriorityLevel.getDeptName(), ybReconsiderPriorityLevel.getDeptCode() + "-");
        ybReconsiderPriorityLevel.setDeptName(strDeptName);
        String strDoctorName = DataTypeHelpers.stringReplaceSetString(ybReconsiderPriorityLevel.getDoctorName(), ybReconsiderPriorityLevel.getDoctorCode() + "-");
        ybReconsiderPriorityLevel.setDoctorName(strDoctorName);
        if (ybReconsiderPriorityLevel.getState() == YbReconsiderPriorityLevel.PERSON_TYPE_4) {
            String strDoctorToName = DataTypeHelpers.stringReplaceSetString(ybReconsiderPriorityLevel.getDoctorNameTo(), ybReconsiderPriorityLevel.getDoctorCodeTo() + "-");
            ybReconsiderPriorityLevel.setDoctorNameTo(strDoctorToName);
        }
        this.save(ybReconsiderPriorityLevel);
    }

    @Override
    @Transactional
    public void updateYbReconsiderPriorityLevel(YbReconsiderPriorityLevel ybReconsiderPriorityLevel) {
        ybReconsiderPriorityLevel.setModifyTime(new Date());
        String strDeptName = DataTypeHelpers.stringReplaceSetString(ybReconsiderPriorityLevel.getDeptName(), ybReconsiderPriorityLevel.getDeptCode() + "-");
        ybReconsiderPriorityLevel.setDeptName(strDeptName);
        String strDoctorName = DataTypeHelpers.stringReplaceSetString(ybReconsiderPriorityLevel.getDoctorName(), ybReconsiderPriorityLevel.getDoctorCode() + "-");
        ybReconsiderPriorityLevel.setDoctorName(strDoctorName);
        if (ybReconsiderPriorityLevel.getState() == YbReconsiderPriorityLevel.PERSON_TYPE_4) {
            String strDoctorToName = DataTypeHelpers.stringReplaceSetString(ybReconsiderPriorityLevel.getDoctorNameTo(), ybReconsiderPriorityLevel.getDoctorCodeTo() + "-");
            ybReconsiderPriorityLevel.setDoctorNameTo(strDoctorToName);
        }
        this.baseMapper.updateYbReconsiderPriorityLevel(ybReconsiderPriorityLevel);
    }

    @Override
    @Transactional
    public void deleteYbReconsiderPriorityLevels(String[] Ids) {
        List<String> list = Arrays.asList(Ids);
        this.baseMapper.deleteBatchIds(list);
    }

    @Override
    public List<YbReconsiderPriorityLevel> findReconsiderPriorityLevelList(int areaType,List<Integer> stateList) {
        LambdaQueryWrapper<YbReconsiderPriorityLevel> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(YbReconsiderPriorityLevel::getIsDeletemark, 1);
        queryWrapper.eq(YbReconsiderPriorityLevel::getAreaType, areaType);
        queryWrapper.in(YbReconsiderPriorityLevel::getState,stateList);
        return this.list(queryWrapper);
    }
}