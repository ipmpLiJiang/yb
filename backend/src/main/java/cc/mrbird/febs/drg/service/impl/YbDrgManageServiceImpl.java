package cc.mrbird.febs.drg.service.impl;

import cc.mrbird.febs.com.controller.DataTypeHelpers;
import cc.mrbird.febs.com.entity.ComSms;
import cc.mrbird.febs.com.service.IComSmsService;
import cc.mrbird.febs.common.domain.QueryRequest;
import cc.mrbird.febs.common.properties.FebsProperties;
import cc.mrbird.febs.common.utils.SortUtil;
import cc.mrbird.febs.drg.entity.YbDrgManage;
import cc.mrbird.febs.drg.dao.YbDrgManageMapper;
import cc.mrbird.febs.drg.entity.YbDrgResult;
import cc.mrbird.febs.drg.service.IYbDrgApplyService;
import cc.mrbird.febs.drg.service.IYbDrgManageService;
import cc.mrbird.febs.drg.service.IYbDrgResultService;
import cc.mrbird.febs.yb.entity.YbDefaultValue;
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
import java.util.stream.Collectors;

/**
 * <p>
 * 服务实现类
 * </p>
 *
 * @author viki
 * @since 2021-11-23
 */
@Slf4j
@Service("IYbDrgManageService")
@Transactional(propagation = Propagation.SUPPORTS, readOnly = true, rollbackFor = Exception.class)
public class YbDrgManageServiceImpl extends ServiceImpl<YbDrgManageMapper, YbDrgManage> implements IYbDrgManageService {

    @Autowired
    IYbDrgApplyService iYbDrgApplyService;

    @Autowired
    IYbDrgResultService iYbDrgResultService;

    @Autowired
    FebsProperties febsProperties;

    @Autowired
    IComSmsService iComSmsService;

    @Override
    public IPage<YbDrgManage> findYbDrgManages(QueryRequest request, YbDrgManage ybDrgManage) {
        try {
            LambdaQueryWrapper<YbDrgManage> queryWrapper = new LambdaQueryWrapper<>();
            if (ybDrgManage.getApplyDateStr() != null) {
                queryWrapper.eq(YbDrgManage::getApplyDateStr, ybDrgManage.getApplyDateStr());
            }
            if (ybDrgManage.getApplyDataId() != null) {
                queryWrapper.eq(YbDrgManage::getApplyDataId, ybDrgManage.getApplyDataId());
            }

            if (ybDrgManage.getState() != null) {
                queryWrapper.eq(YbDrgManage::getState, ybDrgManage.getState());
            }

            Page<YbDrgManage> page = new Page<>();
            SortUtil.handlePageSort(request, page, false);//true 是属性  false是数据库字段可两个
            return this.page(page, queryWrapper);
        } catch (Exception e) {
            log.error("获取字典信息失败", e);
            return null;
        }
    }

    @Override
    public IPage<YbDrgManage> findYbDrgManageList(QueryRequest request, YbDrgManage ybDrgManage) {
        try {
            Page<YbDrgManage> page = new Page<>();
            SortUtil.handlePageSort(request, page, false);//true 是属性  false是数据库字段可两个
            return this.baseMapper.findYbDrgManage(page, ybDrgManage);
        } catch (Exception e) {
            log.error("获取失败", e);
            return null;
        }
    }

    @Override
    @Transactional
    public void createYbDrgManage(YbDrgManage ybDrgManage) {
        if (ybDrgManage.getId() == null || "".equals(ybDrgManage.getId())) {
            ybDrgManage.setId(UUID.randomUUID().toString());
        }
//        ybDrgManage.setCreateTime(new Date());
//        ybDrgManage.setIsDeletemark(1);
        this.save(ybDrgManage);
    }


    @Override
    @Transactional
    public void updateYbDrgManage(YbDrgManage ybDrgManage) {
//        ybDrgManage.setModifyTime(new Date());
        this.baseMapper.updateYbDrgManage(ybDrgManage);
    }

    @Override
    @Transactional
    public void deleteYbDrgManages(String[] Ids) {
        List<String> list = Arrays.asList(Ids);
        this.baseMapper.deleteBatchIds(list);
    }

    //批量接收或单个拒绝
    @Override
    @Transactional
    public void updateAcceptRejectStates(List<YbDrgManage> list) {
        List<YbDrgManage> findList = new ArrayList<>();
        List<YbDrgManage> queryList = new ArrayList<>();
        LambdaQueryWrapper<YbDrgManage> wrapper = new LambdaQueryWrapper<>();
        List<String> idList = new ArrayList<>();
        for (YbDrgManage item : list) {
            idList.add(item.getId());
        }
        if (idList.size() > 0) {
            wrapper.in(YbDrgManage::getId, idList);
            findList = this.list(wrapper);
        }
        if (findList.size() > 0) {
            for (YbDrgManage item : list) {
                queryList = findList.stream().filter(
                        s -> s.getId().equals(item.getId())
                ).collect(Collectors.toList());
                if (queryList.size() > 0) {
                    if (queryList.get(0).getState() == YbDefaultValue.AMSTATE_0) {
                        Date thisDate = new Date();
                        if (item.getState() == YbDefaultValue.AMSTATE_2) {
                            item.setOperateDate(thisDate);
                            item.setOperateProcess("接受申请-已拒绝");
                        } else if (item.getState() == YbDefaultValue.AMSTATE_1) {
                            item.setOperateDate(thisDate);
                            item.setOperateProcess("接受申请-待申诉");
                        }
                        LambdaQueryWrapper<YbDrgManage> queryWrapper = new LambdaQueryWrapper<>();
                        queryWrapper.eq(YbDrgManage::getState, YbDefaultValue.AMSTATE_0);
                        queryWrapper.eq(YbDrgManage::getId, item.getId());

                        String strChangeDeptName = DataTypeHelpers.stringReplaceSetString(item.getChangeDeptName(), item.getChangeDeptCode() + "-");
                        item.setChangeDeptName(strChangeDeptName);
                        String strChangeDoctorName = DataTypeHelpers.stringReplaceSetString(item.getChangeDoctorName(), item.getChangeDoctorCode() + "-");
                        item.setChangeDoctorName(strChangeDoctorName);

                        this.baseMapper.update(item, queryWrapper);
                    }
                }
            }
        }
    }

    @Override
    @Transactional
    public String updateUploadStates(YbDrgManage ybDrgManage) {
        String message = "";
        YbDrgManage entity = this.getById(ybDrgManage.getId());
        if (entity != null) {
            Date thisDate = new Date();
            boolean isUpdate = false;
            isUpdate = iYbDrgApplyService.findDrgApplyCheckEndDate(entity.getApplyDateStr(), entity.getAreaType());
            if (isUpdate) {
                if (entity.getState() == YbDefaultValue.AMSTATE_1) {
                    isUpdate = true;
                    YbDrgManage updateDrgManage = new YbDrgManage();
                    if (ybDrgManage.getState() == YbDefaultValue.AMSTATE_6) {
                        ybDrgManage.setApplyDateStr(entity.getApplyDateStr());
                        ybDrgManage.setOrderNum(entity.getOrderNum());
                        ybDrgManage.setOrderNumber(entity.getOrderNumber());
                        ybDrgManage.setAreaType(entity.getAreaType());
                        isUpdate = this.createUpdateAcceptDrgResult(ybDrgManage, thisDate);

                        updateDrgManage.setOperateProcess("待申诉-已申诉");
                    }
                    updateDrgManage.setId(ybDrgManage.getId());
                    updateDrgManage.setOperateDate(thisDate);
                    updateDrgManage.setOperateReason(ybDrgManage.getOperateReason());
                    updateDrgManage.setState(ybDrgManage.getState());

                    if (isUpdate) {
                        LambdaQueryWrapper<YbDrgManage> queryWrapper = new LambdaQueryWrapper<>();
                        queryWrapper.eq(YbDrgManage::getId, ybDrgManage.getId());
                        queryWrapper.eq(YbDrgManage::getState, YbDefaultValue.AMSTATE_1);
                        int count = this.baseMapper.update(updateDrgManage, queryWrapper);
                        if (count == 0) {
                            message = "DRG申诉状态更新失败.";
                        } else {
                            message = "ok";
                        }
                    } else {
                        message = "DRG结果数据创建失败.";
                    }
                } else {
                    message = "该DRG申诉数据已申诉.";
                }
            } else {
                message = "当前已过截止日期，无法修改.";
            }
        } else {
            message = "未找到申诉数据.";
        }

        return message;
    }

    @Override
    @Transactional
    public String updateUploadStateCompleteds(YbDrgManage ybDrgManage) {
        String message = "";
        YbDrgManage entity = this.getById(ybDrgManage.getId());
        if (entity != null) {
            Date thisDate = new Date();
            boolean isUpdate = false;
            isUpdate = iYbDrgApplyService.findDrgApplyCheckEndDate(entity.getApplyDateStr(), entity.getAreaType());

            if (isUpdate) {
                if (entity.getState() == YbDefaultValue.AMSTATE_6) {
                    YbDrgResult updateDrgResult = new YbDrgResult();
                    updateDrgResult.setId(ybDrgManage.getId());
                    updateDrgResult.setOperateDate(thisDate);
                    updateDrgResult.setOperateReason(ybDrgManage.getOperateReason());
                    this.iYbDrgResultService.updateById(updateDrgResult);

                    YbDrgManage updateDrgManage = new YbDrgManage();
                    updateDrgManage.setId(ybDrgManage.getId());
                    updateDrgManage.setOperateDate(thisDate);
                    updateDrgManage.setOperateReason(ybDrgManage.getOperateReason());

                    int count = this.baseMapper.updateById(updateDrgManage);
                    if (count == 0) {
                        message = "DRG申诉状态更新失败.";
                    } else {
                        message = "ok";
                    }
                } else {
                    message = "未找到申诉数据1.";
                }
            } else {
                message = "当前已过截止日期，无法修改.";
            }
        } else {
            message = "未找到申诉数据2.";
        }

        return message;
    }


    private boolean createUpdateAcceptDrgResult(YbDrgManage ybDrgManage, Date thisDate) {
        YbDrgResult newDrgResult = new YbDrgResult();
        newDrgResult.setId(ybDrgManage.getId());
        newDrgResult.setApplyDataId(ybDrgManage.getApplyDataId());
        newDrgResult.setVerifyId(ybDrgManage.getVerifyId());
        newDrgResult.setManageId(ybDrgManage.getId());

        String strReadyDeptName = DataTypeHelpers.stringReplaceSetString(ybDrgManage.getReadyDeptName(), ybDrgManage.getReadyDeptCode() + "-");
        ybDrgManage.setReadyDeptName(strReadyDeptName);
        String strReadyDoctorName = DataTypeHelpers.stringReplaceSetString(ybDrgManage.getReadyDoctorName(), ybDrgManage.getReadyDoctorCode() + "-");
        ybDrgManage.setReadyDoctorName(strReadyDoctorName);

        newDrgResult.setDeptCode(ybDrgManage.getReadyDeptCode());
        newDrgResult.setDeptName(ybDrgManage.getReadyDeptName());
        newDrgResult.setDoctorCode(ybDrgManage.getReadyDoctorCode());
        newDrgResult.setDoctorName(ybDrgManage.getReadyDoctorName());

        newDrgResult.setApplyDateStr(ybDrgManage.getApplyDateStr());
        newDrgResult.setOrderNum(ybDrgManage.getOrderNum());
        newDrgResult.setOrderNumber(ybDrgManage.getOrderNumber());
        newDrgResult.setAreaType(ybDrgManage.getAreaType());
        newDrgResult.setOperateDate(thisDate);
        newDrgResult.setOperateReason(ybDrgManage.getOperateReason());
        newDrgResult.setState(1);
        return this.iYbDrgResultService.saveOrUpdate(newDrgResult);
    }


    @Override
    public List<YbDrgManage> findDrgManageList(YbDrgManage ybDrgManage) {
        LambdaQueryWrapper<YbDrgManage> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(YbDrgManage::getApplyDateStr, ybDrgManage.getApplyDateStr());
        queryWrapper.eq(YbDrgManage::getAreaType, ybDrgManage.getAreaType());

        if (ybDrgManage.getState() != null) {
            queryWrapper.eq(YbDrgManage::getState, ybDrgManage.getState());
        }
        if (ybDrgManage.getReadyDoctorCode() != null) {
            queryWrapper.eq(YbDrgManage::getReadyDoctorCode, ybDrgManage.getReadyDoctorCode());
        }
        if (ybDrgManage.getReadyDeptName() != null) {
            queryWrapper.eq(YbDrgManage::getReadyDeptName, ybDrgManage.getReadyDeptName());
        }
        if (ybDrgManage.getOrderNumber() != null) {
            queryWrapper.eq(YbDrgManage::getOrderNumber, ybDrgManage.getOrderNumber());
        }
        return this.list(queryWrapper);
    }


    @Override
    @Transactional
    public void updateCreateDrgManage(YbDrgManage ybDrgManage, Long uId, String Uname, Integer type) {
        YbDrgManage entity = this.getById(ybDrgManage.getId());
        if (entity != null && entity.getState() == YbDefaultValue.AMSTATE_2) {
            YbDrgManage newDrgManage = new YbDrgManage();
            String personCode = "";
            Date thisDate = new Date();
            newDrgManage.setId(UUID.randomUUID().toString());
            newDrgManage.setApplyDataId(ybDrgManage.getApplyDataId());
            newDrgManage.setVerifyId(ybDrgManage.getVerifyId());
            newDrgManage.setState(YbDefaultValue.AMSTATE_0);

            Date newDate = this.addSecond(thisDate,10);
            newDrgManage.setOperateDate(newDate);
            newDrgManage.setAreaType(entity.getAreaType());

            Date enableDate = entity.getEnableDate();
            newDrgManage.setEnableDate(enableDate);
            newDrgManage.setOperateProcess("变更申请-创建");

            newDrgManage.setApplyDateStr(entity.getApplyDateStr());
            newDrgManage.setOrderNum(entity.getOrderNum());
            newDrgManage.setOrderNumber(entity.getOrderNumber());

            String strReadyDeptName = DataTypeHelpers.stringReplaceSetString(ybDrgManage.getReadyDeptName(), ybDrgManage.getReadyDeptCode() + "-");
            ybDrgManage.setReadyDeptName(strReadyDeptName);
            String strReadyDoctorName = DataTypeHelpers.stringReplaceSetString(ybDrgManage.getReadyDoctorName(), ybDrgManage.getReadyDoctorCode() + "-");
            ybDrgManage.setReadyDoctorName(strReadyDoctorName);

            String strChangeDeptName = DataTypeHelpers.stringReplaceSetString(ybDrgManage.getChangeDeptName(), ybDrgManage.getChangeDeptCode() + "-");
            ybDrgManage.setChangeDeptName(strChangeDeptName);
            String strChangeDoctorName = DataTypeHelpers.stringReplaceSetString(ybDrgManage.getChangeDoctorName(), ybDrgManage.getChangeDoctorCode() + "-");
            ybDrgManage.setChangeDoctorName(strChangeDoctorName);

            YbDrgManage updateManage = new YbDrgManage();
            updateManage.setState(YbDefaultValue.AMSTATE_4);
            updateManage.setOperateDate(thisDate);
            if (type == 2) {
                //拒绝
                newDrgManage.setReadyDeptCode(ybDrgManage.getReadyDeptCode());
                newDrgManage.setReadyDeptName(ybDrgManage.getReadyDeptName());
                newDrgManage.setReadyDoctorCode(ybDrgManage.getReadyDoctorCode());
                newDrgManage.setReadyDoctorName(ybDrgManage.getReadyDoctorName());

                updateManage.setRefuseId(uId);
                updateManage.setRefuseName(Uname);
                updateManage.setRefuseDate(thisDate);
                updateManage.setRefuseReason(ybDrgManage.getRefuseReason());
                updateManage.setOperateProcess("变更申请-拒绝");
                updateManage.setApprovalState(YbDefaultValue.APPROVALSTATE_2);

                personCode = ybDrgManage.getReadyDoctorCode();
            } else {
                //同意
                newDrgManage.setReadyDeptCode(ybDrgManage.getChangeDeptCode());
                newDrgManage.setReadyDeptName(ybDrgManage.getChangeDeptName());
                newDrgManage.setReadyDoctorCode(ybDrgManage.getChangeDoctorCode());
                newDrgManage.setReadyDoctorName(ybDrgManage.getChangeDoctorName());

                updateManage.setOperateProcess("变更申请-同意");
                updateManage.setApprovalState(YbDefaultValue.APPROVALSTATE_1);

                personCode = ybDrgManage.getChangeDoctorCode();
            }

            LambdaQueryWrapper<YbDrgManage> queryWrapper = new LambdaQueryWrapper<>();
            queryWrapper.eq(YbDrgManage::getState, YbDefaultValue.AMSTATE_2);
            queryWrapper.eq(YbDrgManage::getId, ybDrgManage.getId());
            this.baseMapper.update(updateManage, queryWrapper);

            this.save(newDrgManage);
            int nOpenSms = febsProperties.getOpenSms();
            boolean isOpenSms = nOpenSms == 1 ? true : false;
            if (isOpenSms) {
                String sendContent = this.iYbDrgApplyService.getSendMessage(entity.getApplyDateStr(), enableDate, entity.getAreaType(), true);
                iComSmsService.sendSmsService(entity.getApplyDateStr(), null, personCode, ComSms.SENDTYPE_11, entity.getAreaType(), sendContent, uId, Uname);
            }
        }
    }

    private Date addSecond(Date date,int t){
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        cal.add(Calendar.SECOND, t);// 24小时制
        return cal.getTime();
    }

}