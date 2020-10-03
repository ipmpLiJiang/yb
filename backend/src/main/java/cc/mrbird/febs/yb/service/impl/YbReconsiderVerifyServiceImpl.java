package cc.mrbird.febs.yb.service.impl;

import cc.mrbird.febs.com.controller.DataTypeHelpers;
import cc.mrbird.febs.com.service.IComConfiguremanageService;
import cc.mrbird.febs.common.domain.QueryRequest;
import cc.mrbird.febs.common.utils.SortUtil;
import cc.mrbird.febs.yb.dao.YbReconsiderVerifyMapper;
import cc.mrbird.febs.yb.entity.YbAppealManage;
import cc.mrbird.febs.yb.entity.YbReconsiderVerify;
import cc.mrbird.febs.yb.service.IYbAppealManageService;
import cc.mrbird.febs.yb.service.IYbReconsiderVerifyService;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.UUID;

/**
 * <p>
 * 服务实现类
 * </p>
 *
 * @author viki
 * @since 2020-07-30
 */
@Slf4j
@Service("IYbReconsiderVerifyService")
@Transactional(propagation = Propagation.SUPPORTS, readOnly = true, rollbackFor = Exception.class)
public class YbReconsiderVerifyServiceImpl extends ServiceImpl<YbReconsiderVerifyMapper, YbReconsiderVerify> implements IYbReconsiderVerifyService {

    @Autowired
    public IYbAppealManageService iYbAppealManageService;
    @Autowired
    public IComConfiguremanageService iComConfiguremanageService;

    @Override
    public IPage<YbReconsiderVerify> findYbReconsiderVerifys(QueryRequest request, YbReconsiderVerify ybReconsiderVerify) {
        try {
            LambdaQueryWrapper<YbReconsiderVerify> queryWrapper = new LambdaQueryWrapper<>();
            queryWrapper.eq(YbReconsiderVerify::getIsDeletemark, 1);//1是未删 0是已删


            Page<YbReconsiderVerify> page = new Page<>();
            SortUtil.handlePageSort(request, page, false);//true 是属性  false是数据库字段可两个
            return this.page(page, queryWrapper);
        } catch (Exception e) {
            log.error("获取字典信息失败", e);
            return null;
        }
    }

    @Override
    public IPage<YbReconsiderVerify> findYbReconsiderVerifyList(QueryRequest request, YbReconsiderVerify ybReconsiderVerify) {
        try {
            Page<YbReconsiderVerify> page = new Page<>();
            SortUtil.handlePageSort(request, page, false);//true 是属性  false是数据库字段可两个
            return this.baseMapper.findYbReconsiderVerify(page, ybReconsiderVerify);
        } catch (Exception e) {
            log.error("获取失败", e);
            return null;
        }
    }

    @Override
    @Transactional
    public void createYbReconsiderVerify(YbReconsiderVerify ybReconsiderVerify) {
        ybReconsiderVerify.setCreateTime(new Date());
        if(ybReconsiderVerify.getId() == null || "".equals(ybReconsiderVerify.getId())) {
            ybReconsiderVerify.setId(UUID.randomUUID().toString());
        }
        ybReconsiderVerify.setIsDeletemark(1);
        this.save(ybReconsiderVerify);
    }

    @Override
    @Transactional
    public void updateYbReconsiderVerify(YbReconsiderVerify ybReconsiderVerify) {
        ybReconsiderVerify.setModifyTime(new Date());
        this.baseMapper.updateYbReconsiderVerify(ybReconsiderVerify);
    }

    @Override
    @Transactional
    public void deleteYbReconsiderVerifys(String[] Ids) {
        List<String> list = Arrays.asList(Ids);
        this.baseMapper.deleteBatchIds(list);
    }


    @Override
    @Transactional
    public void insertReconsiderVerifyImports(String applyDate, Long matchPersonId, String matchPersonName) {
        this.baseMapper.insertReconsiderVerifyImport(applyDate, matchPersonId, matchPersonName);
    }

    @Override
    @Transactional
    public void insertMainReconsiderVerifyImports(String applyDate, Long matchPersonId, String matchPersonName) {
        this.baseMapper.insertMainReconsiderVerifyImport(applyDate, matchPersonId, matchPersonName);
    }

    @Override
    @Transactional
    public void updateSendStates(List<YbReconsiderVerify> list, Long uId, String Uname) {
        Date thisDate = new java.sql.Timestamp(new Date().getTime());
        int day = this.iComConfiguremanageService.getVerifyDay(2);

        Date addDate = DataTypeHelpers.addDateMethod(thisDate, day);
        for (YbReconsiderVerify ybReconsiderVerify : list) {
            //更新
            ybReconsiderVerify.setState(3);
            ybReconsiderVerify.setModifyUserId(uId);
            ybReconsiderVerify.setModifyTime(thisDate);
            ybReconsiderVerify.setSendPersonId(uId);
            ybReconsiderVerify.setSendPersonName(Uname);
            ybReconsiderVerify.setSendDate(thisDate);
            ybReconsiderVerify.setOperateDate(thisDate);
            //插入申诉管理
            YbAppealManage ybAppealManage = new YbAppealManage();
            ybAppealManage.setSourceType(0);
            ybAppealManage.setVerifyId(ybReconsiderVerify.getId());
            ybAppealManage.setVerifySendId(ybReconsiderVerify.getId());
            ybAppealManage.setApplyDataId(ybReconsiderVerify.getApplyDataId());
            ybAppealManage.setReadyDeptCode(ybReconsiderVerify.getVerifyDeptCode());
            ybAppealManage.setReadyDeptName(ybReconsiderVerify.getVerifyDeptName());
            ybAppealManage.setReadyDoctorCode(ybReconsiderVerify.getVerifyDoctorCode());
            ybAppealManage.setReadyDoctorName(ybReconsiderVerify.getVerifyDoctorName());
            ybAppealManage.setOperateDate(thisDate);
            ybAppealManage.setOperateProcess("发送操作-接受申请");
            ybAppealManage.setEnableDate(addDate);
            ybAppealManage.setAcceptState(0);
            ybAppealManage.setIsDeletemark(1);
            ybAppealManage.setCreateUserId(uId);
            ybAppealManage.setCreateTime(thisDate);
            ybAppealManage.setDataType(0);

            iYbAppealManageService.createYbAppealManage(ybAppealManage);
            LambdaQueryWrapper<YbReconsiderVerify> queryWrapper = new LambdaQueryWrapper<>();
            queryWrapper.eq(YbReconsiderVerify::getId,ybReconsiderVerify.getId());
            queryWrapper.eq(YbReconsiderVerify::getDataType,0);
            queryWrapper.eq(YbReconsiderVerify::getState,2);
            this.baseMapper.update(ybReconsiderVerify,queryWrapper);
        }
    }
    //主单扣款更新
    @Override
    @Transactional
    public void updateMainSendStates(List<YbReconsiderVerify> list, Long uId, String Uname) {
        Date thisDate = new java.sql.Timestamp(new Date().getTime());
        int day = this.iComConfiguremanageService.getVerifyDay(2);

        Date addDate = DataTypeHelpers.addDateMethod(thisDate, day);
        for (YbReconsiderVerify ybReconsiderVerify : list) {
            //更新
            ybReconsiderVerify.setState(3);
            ybReconsiderVerify.setModifyUserId(uId);
            ybReconsiderVerify.setModifyTime(thisDate);
            ybReconsiderVerify.setSendPersonId(uId);
            ybReconsiderVerify.setSendPersonName(Uname);
            ybReconsiderVerify.setSendDate(thisDate);
            ybReconsiderVerify.setOperateDate(thisDate);
            //插入申诉管理
            YbAppealManage ybAppealManage = new YbAppealManage();
            ybAppealManage.setSourceType(0);
            ybAppealManage.setVerifyId(ybReconsiderVerify.getId());
            ybAppealManage.setVerifySendId(ybReconsiderVerify.getId());
            ybAppealManage.setApplyDataId(ybReconsiderVerify.getApplyDataId());
            ybAppealManage.setReadyDeptCode(ybReconsiderVerify.getVerifyDeptCode());
            ybAppealManage.setReadyDeptName(ybReconsiderVerify.getVerifyDeptName());
            ybAppealManage.setReadyDoctorCode(ybReconsiderVerify.getVerifyDoctorCode());
            ybAppealManage.setReadyDoctorName(ybReconsiderVerify.getVerifyDoctorName());
            ybAppealManage.setOperateDate(thisDate);
            ybAppealManage.setOperateProcess("发送操作-待申诉");
            ybAppealManage.setEnableDate(addDate);
            ybAppealManage.setAcceptState(1);
            ybAppealManage.setIsDeletemark(1);
            ybAppealManage.setCreateUserId(uId);
            ybAppealManage.setCreateTime(thisDate);
            ybAppealManage.setDataType(1);

            iYbAppealManageService.createYbAppealManage(ybAppealManage);
            LambdaQueryWrapper<YbReconsiderVerify> queryWrapper = new LambdaQueryWrapper<>();
            queryWrapper.eq(YbReconsiderVerify::getId,ybReconsiderVerify.getId());
            queryWrapper.eq(YbReconsiderVerify::getDataType,1);
            queryWrapper.eq(YbReconsiderVerify::getState,1);
            this.baseMapper.update(ybReconsiderVerify,queryWrapper);
        }
    }

    //单个，多个核对
    @Override
    @Transactional
    public void updateReviewerStates(List<YbReconsiderVerify> list, Long uId, String Uname) {
        for (YbReconsiderVerify item : list) {
            Date thisDate = new Date();
            if (item.getVerifyDeptCode() != "" && item.getVerifyDeptCode() != null &&
                    item.getVerifyDeptName() != "" && item.getVerifyDeptName() != null &&
                    item.getVerifyDoctorCode() != "" && item.getVerifyDoctorCode() != null &&
                    item.getVerifyDoctorName() != "" && item.getVerifyDoctorName() != null) {
                item.setState(2);
                item.setModifyTime(thisDate);
                item.setModifyUserId(uId);
                item.setReviewerId(uId);
                item.setReviewerName(Uname);
                item.setReviewerDate(thisDate);
                if (item.getId() == null || item.getId() == "00000000-0000-0000-0000-000000000000") {
                    item.setIsDeletemark(1);
                    item.setMatchPersonId(uId);
                    item.setMatchPersonName(Uname);
                    item.setMatchDate(thisDate);
                    item.setCreateTime(thisDate);
                    item.setCreateUserId(uId);
                    this.baseMapper.insert(item);
                } else {
                    LambdaQueryWrapper<YbReconsiderVerify> queryWrapper = new LambdaQueryWrapper<>();
                    queryWrapper.eq(YbReconsiderVerify::getId,item.getId());
                    queryWrapper.eq(YbReconsiderVerify::getDataType,0);
                    queryWrapper.eq(YbReconsiderVerify::getState,1);
                    this.baseMapper.update(item,queryWrapper);
                }
            }
        }
    }

    @Override
    @Transactional
    public void updateReconsiderVerifyImports(List<YbReconsiderVerify> list, Long uId, String Uname) {
        for (YbReconsiderVerify item : list) {
            Date thisDate = new Date();
            if (item.getVerifyDeptCode() != "" && item.getVerifyDeptCode() != null &&
                    item.getVerifyDeptName() != "" && item.getVerifyDeptName() != null &&
                    item.getVerifyDoctorCode() != "" && item.getVerifyDoctorCode() != null &&
                    item.getVerifyDoctorName() != "" && item.getVerifyDoctorName() != null) {
                item.setState(1);
                item.setModifyTime(thisDate);
                item.setModifyUserId(uId);

                if (item.getId() == null || item.getId() == "00000000-0000-0000-0000-000000000000") {
                    item.setIsDeletemark(1);
                    item.setMatchPersonId(uId);
                    item.setMatchPersonName(Uname);
                    item.setMatchDate(thisDate);
                    item.setCreateTime(thisDate);
                    item.setCreateUserId(uId);
                    this.baseMapper.insert(item);
                } else {
                    LambdaQueryWrapper<YbReconsiderVerify> queryWrapper = new LambdaQueryWrapper<>();
                    queryWrapper.eq(YbReconsiderVerify::getId,item.getId());
                    queryWrapper.eq(YbReconsiderVerify::getDataType,0);
                    queryWrapper.eq(YbReconsiderVerify::getState,1);
                    this.baseMapper.update(item,queryWrapper);
                }
            }
        }
    }
}