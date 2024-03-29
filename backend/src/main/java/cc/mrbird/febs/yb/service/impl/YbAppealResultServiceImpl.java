package cc.mrbird.febs.yb.service.impl;

import cc.mrbird.febs.common.domain.QueryRequest;
import cc.mrbird.febs.common.utils.SortUtil;
import cc.mrbird.febs.yb.entity.YbAppealResult;
import cc.mrbird.febs.yb.dao.YbAppealResultMapper;
import cc.mrbird.febs.yb.entity.YbAppealResultView;
import cc.mrbird.febs.yb.entity.YbResultDownLoad;
import cc.mrbird.febs.yb.service.IYbAppealResultService;
import cc.mrbird.febs.yb.service.IYbHandleVerifyService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.ibatis.annotations.Param;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.io.Serializable;
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
 * @since 2020-07-30
 */
@Slf4j
@Service("IYbAppealResultService")
@Transactional(propagation = Propagation.SUPPORTS, readOnly = true, rollbackFor = Exception.class)
public class YbAppealResultServiceImpl extends ServiceImpl<YbAppealResultMapper, YbAppealResult> implements IYbAppealResultService {

@Autowired
    IYbHandleVerifyService iYbHandleVerifyService;
    @Override
    public IPage<YbAppealResult> findYbAppealResults(QueryRequest request, YbAppealResult ybAppealResult) {
        try {
            LambdaQueryWrapper<YbAppealResult> queryWrapper = new LambdaQueryWrapper<>();
            queryWrapper.eq(YbAppealResult::getIsDeletemark, 1);//1是未删 0是已删

            Page<YbAppealResult> page = new Page<>();
            SortUtil.handlePageSort(request, page, false);//true 是属性  false是数据库字段可两个
            return this.page(page, queryWrapper);
        } catch (Exception e) {
            log.error("获取字典信息失败", e);
            return null;
        }
    }

    @Override
    public IPage<YbAppealResult> findYbAppealResultList(QueryRequest request, YbAppealResult ybAppealResult) {
        try {
            Page<YbAppealResult> page = new Page<>();
            SortUtil.handlePageSort(request, page, false);//true 是属性  false是数据库字段可两个
            return this.baseMapper.findYbAppealResult(page, ybAppealResult);
        } catch (Exception e) {
            log.error("获取失败", e);
            return null;
        }
    }

    @Override
    @Transactional
    public void createYbAppealResult(YbAppealResult ybAppealResult) {
        if (ybAppealResult.getId() == null || "".equals(ybAppealResult.getId())) {
            ybAppealResult.setId(UUID.randomUUID().toString());
        }
//        ybAppealResult.setCreateTime(new Date());
        ybAppealResult.setIsDeletemark(1);
        this.save(ybAppealResult);
    }

    @Override
    @Transactional
    public void updateYbAppealResult(YbAppealResult ybAppealResult) {
//        ybAppealResult.setModifyTime(new Date());
        this.baseMapper.updateYbAppealResult(ybAppealResult);
    }

    @Override
    @Transactional
    public void deleteYbAppealResults(String[] Ids) {
        List<String> list = Arrays.asList(Ids);
        this.baseMapper.deleteBatchIds(list);
    }

    @Override
    @Transactional
    public YbAppealResult findCreateAppealResult(YbAppealResult ybAppealResult, Long uId, String Uname) {
        LambdaQueryWrapper<YbAppealResult> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(YbAppealResult::getId, ybAppealResult.getId());
        List<YbAppealResult> list = this.baseMapper.selectList(queryWrapper);
        if (list.size() > 0) {
            ybAppealResult = list.get(0);
        } else {
            Date thisDate = new java.sql.Timestamp(new Date().getTime());
            ybAppealResult.setIsDeletemark(1);
//            ybAppealResult.setCreateUserId(uId);
//            ybAppealResult.setCreateTime(thisDate);
            ybAppealResult.setOperateDate(thisDate);
            //ybAppealResult.setOperateReason("");
            int count = this.baseMapper.insert(ybAppealResult);
            if (count == 0) {
                ybAppealResult = new YbAppealResult();
            }
        }

        return ybAppealResult;
    }

    //打包下载 查找部门
    @Override
    public List<YbResultDownLoad> findAppealResultGroupDepts(YbAppealResultView ybAppealResultView) {
        return this.baseMapper.findAppealResultGroupDept(ybAppealResultView);
    }

    //打包下载 查找部门
    @Override
    public List<YbResultDownLoad> findAppealResultGroupSumDepts(YbAppealResultView ybAppealResultView) {
        return this.baseMapper.findAppealResultGroupSumDept(ybAppealResultView);
    }

    //打包下载 查找部门
    @Override
    public List<YbResultDownLoad> findAppealResultGroupDksDepts(YbAppealResultView ybAppealResultView) {
        return this.baseMapper.findAppealResultGroupDks(ybAppealResultView);
    }

    //打包下载 汇总科室 未设置
    @Override
    public
    List<YbResultDownLoad> findAppealResultNotDepts(YbAppealResultView ybAppealResultView){
        return this.baseMapper.findAppealResultNotDept(ybAppealResultView);
    }

    //sql 语句更新剔除相关数据 已有方法代替 此方法先留着
    @Override
    @Transactional
    public void updateAppealResulResetDatas(String applyDateStr, Long resetPersonId, String resetPersonName) {
        Date resetDate = new Date();
        this.baseMapper.updateAppealResulResetData(applyDateStr, resetPersonId, resetPersonName, resetDate);
    }

    //数据剔除业务
    @Override
    public List<YbAppealResult> findAppealResulDataByResets(String applyDateStr,Integer areaType, Integer dataType) {
        return this.baseMapper.findAppealResulDataByReset(applyDateStr,areaType, dataType);
    }

    //数据还款业务
    @Override
    public List<YbAppealResult> findAppealResulDataByRepays(String applyDateStr,Integer areaType, Integer dataType) {
        return this.baseMapper.findAppealResulDataByRepay(applyDateStr,areaType, dataType);
    }

    //手动剔除业务
    @Override
    public List<YbAppealResult> findAppealResulDataHandles(String applyDateStr,String hvId,Integer areaType) {
        return this.baseMapper.findAppealResulDataHandle(applyDateStr,hvId,areaType);
    }

    //手动剔除业务
    @Override
    public List<YbAppealResult> findAppealResulRelateGroups(String applyDateStr,Integer areaType) {
        return this.baseMapper.findAppealResulRelateGroup(applyDateStr,areaType);
    }

    @Override
    public List<YbAppealResult> findAppealResultList(YbAppealResult appealResult) {
        LambdaQueryWrapper<YbAppealResult> wrapper = new LambdaQueryWrapper<>();
        if (appealResult.getApplyDateStr() != null) {
            wrapper.eq(YbAppealResult::getApplyDateStr, appealResult.getApplyDateStr());
        }

        if (appealResult.getAreaType() != null) {
            wrapper.eq(YbAppealResult::getAreaType, appealResult.getAreaType());
        }

        if (appealResult.getSourceType() != null) {
            wrapper.eq(YbAppealResult::getSourceType, appealResult.getSourceType());
        }

        if (appealResult.getDataType() != null) {
            wrapper.eq(YbAppealResult::getDataType, appealResult.getDataType());
        }

        if (appealResult.getTypeno() != null) {
            wrapper.eq(YbAppealResult::getTypeno, appealResult.getTypeno());
        }

        if (appealResult.getState() != null) {
            wrapper.eq(YbAppealResult::getState, appealResult.getState());
        }

        if (appealResult.getRepayState() != null) {
            wrapper.eq(YbAppealResult::getRepayState, appealResult.getRepayState());
        }

        if (appealResult.getOrderNumber() != null) {
            wrapper.eq(YbAppealResult::getOrderNumber, appealResult.getOrderNumber());
        }

        if (appealResult.getOrderNum() != null) {
            wrapper.eq(YbAppealResult::getOrderNum, appealResult.getOrderNum());
        }

        if (appealResult.getRelatelDataId() != null) {
            wrapper.eq(YbAppealResult::getRelatelDataId, appealResult.getRelatelDataId());
        }

        if (appealResult.getApplyDataId() != null) {
            wrapper.eq(YbAppealResult::getApplyDataId, appealResult.getApplyDataId());
        }

        if (appealResult.getId() != null) {
            wrapper.eq(YbAppealResult::getId, appealResult.getId());
        }

        if (appealResult.getDoctorCode() != null) {
            wrapper.eq(YbAppealResult::getDoctorCode, appealResult.getDoctorCode());
        }

        if (appealResult.getDoctorName() != null) {
            wrapper.eq(YbAppealResult::getDoctorName, appealResult.getDoctorName());
        }

        if (appealResult.getDeptCode() != null) {
            wrapper.eq(YbAppealResult::getDeptCode, appealResult.getDeptCode());
        }

        if (appealResult.getDeptName() != null) {
            wrapper.eq(YbAppealResult::getDeptName, appealResult.getDeptName());
        }

        return this.list(wrapper);
    }

    //申请，查询第一次复议记录
    @Override
    public YbAppealResult findLoadLastAppealResulData(YbAppealResult appealResult) {
        YbAppealResult ybAppealResult = null;
        LambdaQueryWrapper<YbAppealResult> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(YbAppealResult::getApplyDataId, appealResult.getApplyDataId());
        queryWrapper.eq(YbAppealResult::getVerifyId, appealResult.getVerifyId());
        List<YbAppealResult> list = this.baseMapper.selectList(queryWrapper);
        if (list.size() > 0) {
            ybAppealResult = list.get(0);
        }

        return ybAppealResult;
    }

    @Override
    public int updatAppealResultCancelData(List<YbAppealResult> list){
        return this.baseMapper.updateAppealResultCancelData(list);
    }

    @Override
    public int updateDksNameByDateAndAreaData(String applyDateStr,Integer areaType){
        return this.baseMapper.updateDksNameByDateAndArea(applyDateStr,areaType);
    }

    @Override
    public List<YbAppealResult> findDeptCheckDksNameByDateAndAreaData(String applyDateStr,Integer areaType){
        return this.baseMapper.findDeptCheckDksNameByDateAndArea(applyDateStr,areaType);
    }
}