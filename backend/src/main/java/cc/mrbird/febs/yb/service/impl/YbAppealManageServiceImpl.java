package cc.mrbird.febs.yb.service.impl;

import cc.mrbird.febs.com.entity.ComConfiguremanage;
import cc.mrbird.febs.com.service.IComConfiguremanageService;
import cc.mrbird.febs.common.domain.QueryRequest;
import cc.mrbird.febs.common.utils.SortUtil;
import cc.mrbird.febs.yb.entity.YbAppealManage;
import cc.mrbird.febs.yb.dao.YbAppealManageMapper;
import cc.mrbird.febs.yb.entity.YbAppealManageView;
import cc.mrbird.febs.yb.entity.YbAppealResult;
import cc.mrbird.febs.yb.entity.YbReconsiderVerify;
import cc.mrbird.febs.yb.service.IYbAppealManageService;
import cc.mrbird.febs.yb.service.IYbAppealResultService;
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

import java.text.ParsePosition;
import java.text.SimpleDateFormat;
import java.util.*;
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
@Service("IYbAppealManageService")
@Transactional(propagation = Propagation.SUPPORTS, readOnly = true, rollbackFor = Exception.class)
public class YbAppealManageServiceImpl extends ServiceImpl<YbAppealManageMapper, YbAppealManage> implements IYbAppealManageService {

    @Autowired
    public IYbAppealResultService iYbAppealResultService;
    @Autowired
    public IComConfiguremanageService iComConfiguremanageService;

    @Override
    public IPage<YbAppealManage> findYbAppealManages(QueryRequest request, YbAppealManage ybAppealManage) {
        try {
            LambdaQueryWrapper<YbAppealManage> queryWrapper = new LambdaQueryWrapper<>();
            queryWrapper.eq(YbAppealManage::getIsDeletemark, 1);//1是未删 0是已删

            if (ybAppealManage.getAcceptState() != null) {
                queryWrapper.eq(YbAppealManage::getAcceptState, ybAppealManage.getAcceptState());
            }
            if (ybAppealManage.getApplyDataId() != null) {
                queryWrapper.eq(YbAppealManage::getApplyDataId, ybAppealManage.getApplyDataId());
            }

            Page<YbAppealManage> page = new Page<>();
            SortUtil.handlePageSort(request, page, false);//true 是属性  false是数据库字段可两个
            return this.page(page, queryWrapper);
        } catch (Exception e) {
            log.error("获取字典信息失败", e);
            return null;
        }
    }

    @Override
    public IPage<YbAppealManage> findYbAppealManageList(QueryRequest request, YbAppealManage ybAppealManage) {
        try {
            Page<YbAppealManage> page = new Page<>();
            SortUtil.handlePageSort(request, page, false);//true 是属性  false是数据库字段可两个
            return this.baseMapper.findYbAppealManage(page, ybAppealManage);
        } catch (Exception e) {
            log.error("获取失败", e);
            return null;
        }
    }

    @Override
    @Transactional
    public void createYbAppealManage(YbAppealManage ybAppealManage) {
        ybAppealManage.setCreateTime(new Date());
        if (ybAppealManage.getId() == null || "".equals(ybAppealManage.getId())) {
            ybAppealManage.setId(UUID.randomUUID().toString());
        }
        ybAppealManage.setIsDeletemark(1);
        this.save(ybAppealManage);
    }

    @Override
    @Transactional
    public void updateYbAppealManage(YbAppealManage ybAppealManage) {
        ybAppealManage.setModifyTime(new Date());
        this.baseMapper.updateYbAppealManage(ybAppealManage);
    }

    @Override
    @Transactional
    public void deleteYbAppealManages(String[] Ids) {
        List<String> list = Arrays.asList(Ids);
        this.baseMapper.deleteBatchIds(list);
    }

    //批量接收或单个拒绝
    @Override
    @Transactional
    public void updateAcceptRejectStates(List<YbAppealManage> list, Long uId, String Uname) {
        for (YbAppealManage item : list) {
            Date thisDate = new Date();
            item.setModifyUserId(uId);
            item.setModifyTime(thisDate);
            if (item.getAcceptState() == 2) {
                item.setOperateDate(thisDate);
                item.setOperateProcess("接受申请-已拒绝");
            } else if (item.getAcceptState() == 1) {
                item.setOperateDate(thisDate);
                item.setOperateProcess("接受申请-待申诉");
            }
            LambdaQueryWrapper<YbAppealManage> queryWrapper = new LambdaQueryWrapper<>();
            queryWrapper.eq(YbAppealManage::getAcceptState,0);
            queryWrapper.eq(YbAppealManage::getId,item.getId());
            this.baseMapper.update(item,queryWrapper);
        }

    }

    private boolean createUpdateAcceptAppealResult(YbAppealManage ybAppealManage,Date thisDate,Long uId, String Uname){
        YbAppealResult newAppealResult = new YbAppealResult();
        newAppealResult.setId(ybAppealManage.getId());
        newAppealResult.setSourceType(ybAppealManage.getSourceType());
        newAppealResult.setDataType(ybAppealManage.getDataType());
        newAppealResult.setApplyDataId(ybAppealManage.getApplyDataId());
        newAppealResult.setVerifyId(ybAppealManage.getVerifyId());
        newAppealResult.setManageId(ybAppealManage.getId());
        newAppealResult.setDeptCode(ybAppealManage.getReadyDeptCode());
        newAppealResult.setDeptName(ybAppealManage.getReadyDeptName());
        newAppealResult.setDoctorCode(ybAppealManage.getReadyDoctorCode());
        newAppealResult.setDoctorName(ybAppealManage.getReadyDoctorName());
        newAppealResult.setOperateDate(thisDate);
        newAppealResult.setOperateReason(ybAppealManage.getOperateReason());
        newAppealResult.setCreateUserId(uId);
        newAppealResult.setCreateTime(thisDate);
        newAppealResult.setState(1);
        newAppealResult.setIsDeletemark(1);
        return this.iYbAppealResultService.saveOrUpdate(newAppealResult);
    }

    @Override
    @Transactional
    public void updateUploadStates(YbAppealManage ybAppealManage, Long uId, String Uname) throws Exception {
        Date thisDate = new Date();
        boolean isTrue = true;
        YbAppealManage updateAppealManage = new YbAppealManage();
        if (ybAppealManage.getAcceptState() == 6) {
            isTrue = this.createUpdateAcceptAppealResult(ybAppealManage,thisDate,uId,Uname);

            updateAppealManage.setOperateProcess("待申诉-已申诉");
        }

        updateAppealManage.setId(ybAppealManage.getId());
        updateAppealManage.setModifyUserId(uId);
        updateAppealManage.setModifyTime(thisDate);
        updateAppealManage.setOperateDate(thisDate);
        updateAppealManage.setOperateReason(ybAppealManage.getOperateReason());
        updateAppealManage.setAcceptState(ybAppealManage.getAcceptState());
        if(isTrue) {
            LambdaQueryWrapper<YbAppealManage> queryWrapper = new LambdaQueryWrapper<>();
            queryWrapper.eq(YbAppealManage::getAcceptState,1);
            queryWrapper.eq(YbAppealManage::getId,updateAppealManage.getId());
            int count = this.baseMapper.update(updateAppealManage,queryWrapper);
            if(count == 0){
                throw  new Exception("复议申诉状态更新失败.");
            }
        } else {
            throw new Exception("复议上传数据创建失败.");
        }
    }

    /*
        @Override
        @Transactional
        public void updateUploadStates(YbAppealManage ybAppealManage, Long uId, String Uname) {
            Date thisDate = new Date();
            YbAppealResult newAppealResult = new YbAppealResult();
            newAppealResult.setId(ybAppealManage.getId());
    //        newAppealResult.setApplyDataId(ybAppealManage.getApplyDataId());
    //        newAppealResult.setVerifyId(ybAppealManage.getVerifyId());
    //        newAppealResult.setManageId(ybAppealManage.getId());
    //        newAppealResult.setDeptCode(ybAppealManage.getReadyDeptCode());
    //        newAppealResult.setDeptName(ybAppealManage.getReadyDeptName());
    //        newAppealResult.setDoctorCode(ybAppealManage.getReadyDoctorCode());
    //        newAppealResult.setDoctorName(ybAppealManage.getReadyDoctorName());
            newAppealResult.setOperateDate(thisDate);
            newAppealResult.setOperateReason(ybAppealManage.getOperateReason());
            newAppealResult.setModifyUserId(uId);
            newAppealResult.setModifyTime(thisDate);
            if (ybAppealManage.getAcceptState() == 6) {
                newAppealResult.setState(1);
            }
            iYbAppealResultService.updateYbAppealResult(newAppealResult);
    //        ybAppealManage.setOperateReason("");
            ybAppealManage.setModifyUserId(uId);
            ybAppealManage.setModifyTime(thisDate);
            ybAppealManage.setOperateDate(thisDate);
            ybAppealManage.setOperateProcess("待申诉-已申诉");
            this.baseMapper.updateAcceptEndState(ybAppealManage);
        }
    */
    @Override
    @Transactional
    public void updateCreateYbAppealManage(YbAppealManage ybAppealManage, Long uId, String Uname, Integer type) {
        YbAppealManage newAppealManage = new YbAppealManage();
        Date thisDate = new Date();
        newAppealManage.setApplyDataId(ybAppealManage.getApplyDataId());
        newAppealManage.setVerifyId(ybAppealManage.getVerifyId());
        newAppealManage.setAcceptState(0);
        newAppealManage.setIsDeletemark(1);
        newAppealManage.setCreateUserId(uId);
        newAppealManage.setCreateTime(thisDate);
        newAppealManage.setOperateDate(thisDate);
        newAppealManage.setSourceType(ybAppealManage.getSourceType());
        newAppealManage.setVerifySendId(ybAppealManage.getVerifyId());
        newAppealManage.setDataType(ybAppealManage.getDataType());
        int day = getDay();
        Date enableDate = this.addDateMethod(thisDate, day);
        newAppealManage.setEnableDate(enableDate);
        newAppealManage.setOperateProcess("变更申请-创建");
        //状态改为4  医保已审核
        ybAppealManage.setAcceptState(4);
        ybAppealManage.setModifyUserId(uId);
        ybAppealManage.setModifyTime(thisDate);
        ybAppealManage.setOperateDate(thisDate);
        if (type == 2) {
            //拒绝
            ybAppealManage.setRefuseId(uId);
            ybAppealManage.setRefuseName(Uname);
            ybAppealManage.setRefuseDate(thisDate);
            ybAppealManage.setOperateProcess("变更申请-拒绝");

            newAppealManage.setReadyDeptCode(ybAppealManage.getReadyDeptCode());
            newAppealManage.setReadyDeptName(ybAppealManage.getReadyDeptName());
            newAppealManage.setReadyDoctorCode(ybAppealManage.getReadyDoctorCode());
            newAppealManage.setReadyDoctorName(ybAppealManage.getReadyDoctorName());

            this.updateExamineStates(ybAppealManage);
        } else {
            //同意
            newAppealManage.setReadyDeptCode(ybAppealManage.getChangeDeptCode());
            newAppealManage.setReadyDeptName(ybAppealManage.getChangeDeptName());
            newAppealManage.setReadyDoctorCode(ybAppealManage.getChangeDoctorCode());
            newAppealManage.setReadyDoctorName(ybAppealManage.getChangeDoctorName());
            ybAppealManage.setRefuseReason("");
            ybAppealManage.setOperateProcess("变更申请-同意");

            LambdaQueryWrapper<YbAppealManage> queryWrapper = new LambdaQueryWrapper<>();
            queryWrapper.eq(YbAppealManage::getAcceptState,2);
            queryWrapper.eq(YbAppealManage::getId,ybAppealManage.getId());
            this.baseMapper.update(ybAppealManage,queryWrapper);
        }
        this.createYbAppealManage(newAppealManage);
    }

    private int getDay() {
        LambdaQueryWrapper<ComConfiguremanage> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(ComConfiguremanage::getConfigureType, 1);
        List<ComConfiguremanage> listFile = iComConfiguremanageService.list(queryWrapper);
        int day = 2;
        if (listFile.size() > 0) {
            int intField = listFile.get(0).getIntField();
            if (intField > 0) {
                day = intField;
            }
        }
        return day;
    }

    private Date addDateMethod(Date date, int day) {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
        String today = simpleDateFormat.format(date);
        ParsePosition pos = new ParsePosition(0);
        Calendar calendar = new GregorianCalendar();
        Date newDate = simpleDateFormat.parse(today, pos);
        calendar.setTime(newDate);
        calendar.add(calendar.DATE, day + 1);
        Date addDate = calendar.getTime();

        return addDate;
    }

    @Override
    @Transactional
    public void updateCreateAdminYbAppealManage(YbAppealManage ybAppealManage, Long uId, String Uname) {
        Date thisDate = new Date();
        YbAppealManage updateAppealManage = new YbAppealManage();
        updateAppealManage.setId(ybAppealManage.getId());
        updateAppealManage.setAcceptState(3);
        //updateAppealManage.setOperateReason("管理员更改");
        ybAppealManage.setOperateProcess("管理员更改");
        updateAppealManage.setOperateDate(thisDate);
        updateAppealManage.setAdminPersonId(uId);
        updateAppealManage.setAdminPersonName(Uname);
        updateAppealManage.setAdminReason("管理员更改");
        updateAppealManage.setAdminChangeDate(thisDate);
        //方法 更改状态为0的数据 业务更改 管理员更改状态为1的数据 所有不调用该方法
        //方法 更改状态为1的数据
        LambdaQueryWrapper<YbAppealManage> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(YbAppealManage::getAcceptState,1);
        queryWrapper.eq(YbAppealManage::getId,ybAppealManage.getId());
        this.baseMapper.update(updateAppealManage,queryWrapper);

        YbAppealManage newAppealManage = new YbAppealManage();
        newAppealManage.setId(UUID.randomUUID().toString());
        newAppealManage.setDataType(ybAppealManage.getDataType());
        newAppealManage.setSourceType(ybAppealManage.getSourceType());
        newAppealManage.setApplyDataId(ybAppealManage.getApplyDataId());
        newAppealManage.setVerifyId(ybAppealManage.getVerifyId());

        int day = getDay();
        Date enableDate = this.addDateMethod(thisDate, day);
        newAppealManage.setEnableDate(enableDate);
        //赋值 新增状态为0的数据 业务更改 管理员更改状态为1的数据 所有不赋值为0
        //newAppealManage.setAcceptState(0);
        //因业务需求管理员更改状态为1(接受)的数据
        newAppealManage.setAcceptState(1);
        newAppealManage.setIsDeletemark(1);
        newAppealManage.setCreateUserId(uId);
        newAppealManage.setCreateTime(thisDate);
        newAppealManage.setOperateDate(thisDate);
        newAppealManage.setReadyDeptCode(ybAppealManage.getReadyDeptCode());
        newAppealManage.setReadyDeptName(ybAppealManage.getReadyDeptName());
        newAppealManage.setReadyDoctorCode(ybAppealManage.getReadyDoctorCode());
        newAppealManage.setReadyDoctorName(ybAppealManage.getReadyDoctorName());
        newAppealManage.setVerifySendId(ybAppealManage.getVerifyId());
        newAppealManage.setOperateProcess("管理员更改-创建");

        this.createYbAppealManage(newAppealManage);
    }


    @Override
    @Transactional
    public void updateExamineStates(YbAppealManage ybAppealManage) {
        LambdaQueryWrapper<YbAppealManage> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(YbAppealManage::getAcceptState,2);
        queryWrapper.eq(YbAppealManage::getId,ybAppealManage.getId());
        this.baseMapper.update(ybAppealManage,queryWrapper);
    }

}