package cc.mrbird.febs.yb.service.impl;

import cc.mrbird.febs.com.controller.DataTypeHelpers;
import cc.mrbird.febs.com.entity.ComSms;
import cc.mrbird.febs.com.service.IComConfiguremanageService;
import cc.mrbird.febs.com.service.IComSmsService;
import cc.mrbird.febs.common.domain.QueryRequest;
import cc.mrbird.febs.common.properties.FebsProperties;
import cc.mrbird.febs.common.utils.SortUtil;
import cc.mrbird.febs.yb.dao.YbAppealManageMapper;
import cc.mrbird.febs.yb.entity.*;
import cc.mrbird.febs.yb.service.IYbAppealManageService;
import cc.mrbird.febs.yb.service.IYbAppealResultService;
import cc.mrbird.febs.yb.service.IYbReconsiderApplyService;
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

import java.text.ParsePosition;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;

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

    @Autowired
    public IComSmsService iComSmsService;

    @Autowired
    public IYbReconsiderApplyService iYbReconsiderApplyService;

    @Autowired
    FebsProperties febsProperties;

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

            if (ybAppealManage.getApplyDateStr() != null) {
                queryWrapper.eq(YbAppealManage::getApplyDateStr, ybAppealManage.getApplyDateStr());
            }

            if (ybAppealManage.getDataType() != null) {
                queryWrapper.eq(YbAppealManage::getDataType, ybAppealManage.getDataType());
            }

            if (ybAppealManage.getTypeno() != null) {
                queryWrapper.eq(YbAppealManage::getTypeno, ybAppealManage.getTypeno());
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

    @Override
    public int findAppealManageResetCheckCounts(String applyDateStr) {
        return this.baseMapper.findAppealManageResetCheckCount(applyDateStr);
    }

    //批量接收或单个拒绝
    @Override
    @Transactional
    public void updateAcceptRejectStates(List<YbAppealManage> list, Long uId, String Uname) {
        List<YbAppealManage> findList = new ArrayList<>();
        List<YbAppealManage> queryList = new ArrayList<>();
        LambdaQueryWrapper<YbAppealManage> wrapper = new LambdaQueryWrapper<>();
        List<String> idList = new ArrayList<>();
        for (YbAppealManage item : list) {
            idList.add(item.getId());
        }
        if (idList.size() > 0) {
            wrapper.in(YbAppealManage::getId, idList);
            findList = this.list(wrapper);
        }
        if (findList.size() > 0) {
            for (YbAppealManage item : list) {
                queryList = findList.stream().filter(
                        s -> s.getId().equals(item.getId())
                ).collect(Collectors.toList());
                if (queryList.size() > 0) {
                    if (queryList.get(0).getAcceptState() == YbDefaultValue.ACCEPTSTATE_0) {
                        Date thisDate = new Date();
                        item.setModifyUserId(uId);
                        item.setModifyTime(thisDate);
                        if (item.getAcceptState() == YbDefaultValue.ACCEPTSTATE_2) {
                            item.setOperateDate(thisDate);
                            item.setOperateProcess("接受申请-已拒绝");
                        } else if (item.getAcceptState() == YbDefaultValue.ACCEPTSTATE_1) {
                            item.setOperateDate(thisDate);
                            item.setOperateProcess("接受申请-待申诉");
                        }
                        LambdaQueryWrapper<YbAppealManage> queryWrapper = new LambdaQueryWrapper<>();
                        queryWrapper.eq(YbAppealManage::getAcceptState, YbDefaultValue.ACCEPTSTATE_0);
                        queryWrapper.eq(YbAppealManage::getId, item.getId());

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

    private boolean createUpdateAcceptAppealResult(YbAppealManage ybAppealManage, Date thisDate, Long uId, String Uname) {
        YbAppealResult newAppealResult = new YbAppealResult();
        newAppealResult.setId(ybAppealManage.getId());
        newAppealResult.setSourceType(ybAppealManage.getSourceType());
        newAppealResult.setDataType(ybAppealManage.getDataType());
        newAppealResult.setApplyDataId(ybAppealManage.getApplyDataId());
        newAppealResult.setVerifyId(ybAppealManage.getVerifyId());
        newAppealResult.setManageId(ybAppealManage.getId());

        String strReadyDeptName = DataTypeHelpers.stringReplaceSetString(ybAppealManage.getReadyDeptName(), ybAppealManage.getReadyDeptCode() + "-");
        ybAppealManage.setReadyDeptName(strReadyDeptName);
        String strReadyDoctorName = DataTypeHelpers.stringReplaceSetString(ybAppealManage.getReadyDoctorName(), ybAppealManage.getReadyDoctorCode() + "-");
        ybAppealManage.setReadyDoctorName(strReadyDoctorName);

        newAppealResult.setDeptCode(ybAppealManage.getReadyDeptCode());
        newAppealResult.setDeptName(ybAppealManage.getReadyDeptName());
        newAppealResult.setDoctorCode(ybAppealManage.getReadyDoctorCode());
        newAppealResult.setDoctorName(ybAppealManage.getReadyDoctorName());

        newAppealResult.setApplyDateStr(ybAppealManage.getApplyDateStr());
        newAppealResult.setOrderNum(ybAppealManage.getOrderNum());
        newAppealResult.setOrderNumber(ybAppealManage.getOrderNumber());
        newAppealResult.setTypeno(ybAppealManage.getTypeno());

        newAppealResult.setOrderDoctorCode(ybAppealManage.getOrderDoctorCode());
        newAppealResult.setOrderDoctorName(ybAppealManage.getOrderDoctorName());
        newAppealResult.setOrderDeptCode(ybAppealManage.getOrderDeptCode());
        newAppealResult.setOrderDeptName(ybAppealManage.getOrderDeptName());

        newAppealResult.setOperateDate(thisDate);
        newAppealResult.setOperateReason(ybAppealManage.getOperateReason());
        newAppealResult.setCreateUserId(uId);
        newAppealResult.setCreateTime(thisDate);
        newAppealResult.setState(1);
        newAppealResult.setRepayState(YbDefaultValue.RESULTREPAYSTATE_1);

        newAppealResult.setIsDeletemark(1);
        return this.iYbAppealResultService.saveOrUpdate(newAppealResult);
    }

    @Override
    @Transactional
    public String updateUploadStates(YbAppealManage ybAppealManage, Long uId, String Uname) {
        String message = "";
        YbAppealManage entity = this.getById(ybAppealManage.getId());
        if (entity != null) {
            Date thisDate = new Date();
            boolean isUpdate = false;
            String mms = "";
            isUpdate = iYbReconsiderApplyService.findReconsiderApplyCheckEndDate(entity.getApplyDateStr(), entity.getTypeno());
            if (entity.getTypeno() == YbDefaultValue.TYPENO_1) {
                mms = "第一版";
            } else {
                mms = "第二版";
            }
            if (isUpdate || entity.getSourceType() == YbDefaultValue.SOURCETYPE_1) {
                if (entity.getAcceptState() == YbDefaultValue.ACCEPTSTATE_1) {
                    isUpdate = true;
                    YbAppealManage updateAppealManage = new YbAppealManage();
                    if (ybAppealManage.getAcceptState() == YbDefaultValue.ACCEPTSTATE_6) {
                        ybAppealManage.setApplyDateStr(entity.getApplyDateStr());
                        ybAppealManage.setOrderNum(entity.getOrderNum());
                        ybAppealManage.setOrderNumber(entity.getOrderNumber());
                        ybAppealManage.setTypeno(entity.getTypeno());

                        ybAppealManage.setOrderDoctorCode(entity.getOrderDoctorCode());
                        ybAppealManage.setOrderDoctorName(entity.getOrderDoctorName());
                        ybAppealManage.setOrderDeptCode(entity.getOrderDeptCode());
                        ybAppealManage.setOrderDeptName(entity.getOrderDeptName());

                        isUpdate = this.createUpdateAcceptAppealResult(ybAppealManage, thisDate, uId, Uname);

                        updateAppealManage.setOperateProcess("待申诉-已申诉");
                    }
                    updateAppealManage.setId(ybAppealManage.getId());
                    updateAppealManage.setModifyUserId(uId);
                    updateAppealManage.setModifyTime(thisDate);
                    updateAppealManage.setOperateDate(thisDate);
                    updateAppealManage.setOperateReason(ybAppealManage.getOperateReason());
                    updateAppealManage.setAcceptState(ybAppealManage.getAcceptState());

                    if (isUpdate) {
                        LambdaQueryWrapper<YbAppealManage> queryWrapper = new LambdaQueryWrapper<>();
                        queryWrapper.eq(YbAppealManage::getId, ybAppealManage.getId());
                        queryWrapper.eq(YbAppealManage::getAcceptState, YbDefaultValue.ACCEPTSTATE_1);
                        int count = this.baseMapper.update(updateAppealManage, queryWrapper);
                        if (count == 0) {
                            message = "复议申诉状态更新失败.";
                        } else {
                            message = "ok";
                        }
                    } else {
                        message = "复议结果数据创建失败.";
                    }
                } else {
                    message = "该申诉数据已申诉.";
                }
            } else {
                message = "当前已过 " + mms + " 截止日期，无法修改.";
            }
        } else {
            message = "未找到申诉数据.";
        }

        return message;
    }

    @Override
    @Transactional
    public String updateUploadStateCompleteds(YbAppealManage ybAppealManage, Long uId, String Uname) {
        String message = "";
        YbAppealManage entity = this.getById(ybAppealManage.getId());
        if (entity != null) {
            Date thisDate = new Date();
            boolean isUpdate = false;
            String mms = "";
            isUpdate = iYbReconsiderApplyService.findReconsiderApplyCheckEndDate(entity.getApplyDateStr(), entity.getTypeno());
            if (entity.getTypeno() == YbDefaultValue.TYPENO_1) {
                mms = "第一版";
            } else {
                mms = "第二版";
            }
            if (isUpdate || entity.getSourceType() == YbDefaultValue.SOURCETYPE_1) {
                if (entity.getAcceptState() == YbDefaultValue.ACCEPTSTATE_6) {
                    YbAppealResult updateAppealResult = new YbAppealResult();
                    updateAppealResult.setId(ybAppealManage.getId());
                    updateAppealResult.setModifyUserId(uId);
                    updateAppealResult.setModifyTime(thisDate);
                    updateAppealResult.setOperateDate(thisDate);
                    updateAppealResult.setOperateReason(ybAppealManage.getOperateReason());
                    this.iYbAppealResultService.updateById(updateAppealResult);

                    YbAppealManage updateAppealManage = new YbAppealManage();
                    updateAppealManage.setId(ybAppealManage.getId());
                    updateAppealManage.setModifyUserId(uId);
                    updateAppealManage.setModifyTime(thisDate);
                    updateAppealManage.setOperateDate(thisDate);
                    updateAppealManage.setOperateReason(ybAppealManage.getOperateReason());

                    int count = this.baseMapper.updateById(updateAppealManage);
                    if (count == 0) {
                        message = "复议申诉状态更新失败.";
                    } else {
                        message = "ok";
                    }
                } else {
                    message = "未找到申诉数据1.";
                }
            } else {
                message = "当前已过 " + mms + " 截止日期，无法修改.";
            }
        } else {
            message = "未找到申诉数据2.";
        }

        return message;
    }

    @Override
    @Transactional
    public void updateCreateAppealManage(YbAppealManage ybAppealManage, Long uId, String Uname, Integer type) {
        YbAppealManage entity = this.getById(ybAppealManage.getId());
        if (entity != null && entity.getAcceptState() == YbDefaultValue.APPROVALSTATE_2) {

            YbAppealManage newAppealManage = new YbAppealManage();
            String personCode = "";
            Date thisDate = new Date();
            newAppealManage.setId(UUID.randomUUID().toString());
            newAppealManage.setApplyDataId(ybAppealManage.getApplyDataId());
            newAppealManage.setVerifyId(ybAppealManage.getVerifyId());
            newAppealManage.setAcceptState(YbDefaultValue.ACCEPTSTATE_0);
            newAppealManage.setIsDeletemark(1);
            newAppealManage.setCreateUserId(uId);
            newAppealManage.setCreateTime(thisDate);
            newAppealManage.setOperateDate(thisDate);
            newAppealManage.setSourceType(ybAppealManage.getSourceType());
            newAppealManage.setVerifySendId(ybAppealManage.getVerifyId());
            newAppealManage.setDataType(ybAppealManage.getDataType());
            int day = iComConfiguremanageService.getConfigDay();
            Date enableDate = this.addDateMethod(thisDate, day);
            newAppealManage.setEnableDate(enableDate);
            newAppealManage.setOperateProcess("变更申请-创建");

            newAppealManage.setApplyDateStr(entity.getApplyDateStr());
            newAppealManage.setOrderNum(entity.getOrderNum());
            newAppealManage.setOrderNumber(entity.getOrderNumber());
            newAppealManage.setTypeno(entity.getTypeno());

            newAppealManage.setOrderDoctorCode(entity.getOrderDoctorCode());
            newAppealManage.setOrderDoctorName(entity.getOrderDoctorName());
            newAppealManage.setOrderDeptCode(entity.getOrderDeptCode());
            newAppealManage.setOrderDeptName(entity.getOrderDeptName());
            //状态改为4  医保已审核
            ybAppealManage.setAcceptState(YbDefaultValue.ACCEPTSTATE_4);
            ybAppealManage.setModifyUserId(uId);
            ybAppealManage.setModifyTime(thisDate);
            ybAppealManage.setOperateDate(thisDate);

            String strReadyDeptName = DataTypeHelpers.stringReplaceSetString(ybAppealManage.getReadyDeptName(), ybAppealManage.getReadyDeptCode() + "-");
            ybAppealManage.setReadyDeptName(strReadyDeptName);
            String strReadyDoctorName = DataTypeHelpers.stringReplaceSetString(ybAppealManage.getReadyDoctorName(), ybAppealManage.getReadyDoctorCode() + "-");
            ybAppealManage.setReadyDoctorName(strReadyDoctorName);

            String strChangeDeptName = DataTypeHelpers.stringReplaceSetString(ybAppealManage.getChangeDeptName(), ybAppealManage.getChangeDeptCode() + "-");
            ybAppealManage.setChangeDeptName(strChangeDeptName);
            String strChangeDoctorName = DataTypeHelpers.stringReplaceSetString(ybAppealManage.getChangeDoctorName(), ybAppealManage.getChangeDoctorCode() + "-");
            ybAppealManage.setChangeDoctorName(strChangeDoctorName);


            String msg = "";
            if (type == 2) {
                //拒绝
                ybAppealManage.setRefuseId(uId);
                ybAppealManage.setRefuseName(Uname);
                ybAppealManage.setRefuseDate(thisDate);
                ybAppealManage.setOperateProcess("变更申请-拒绝");
                ybAppealManage.setApprovalState(YbDefaultValue.APPROVALSTATE_2);

                newAppealManage.setReadyDeptCode(ybAppealManage.getReadyDeptCode());
                newAppealManage.setReadyDeptName(ybAppealManage.getReadyDeptName());
                newAppealManage.setReadyDoctorCode(ybAppealManage.getReadyDoctorCode());
                newAppealManage.setReadyDoctorName(ybAppealManage.getReadyDoctorName());

                personCode = ybAppealManage.getReadyDoctorCode();

                msg = "拒绝已驳回";
                this.updateExamineStates(ybAppealManage);
            } else {
                //同意
                newAppealManage.setReadyDeptCode(ybAppealManage.getChangeDeptCode());
                newAppealManage.setReadyDeptName(ybAppealManage.getChangeDeptName());
                newAppealManage.setReadyDoctorCode(ybAppealManage.getChangeDoctorCode());

                newAppealManage.setReadyDoctorName(ybAppealManage.getChangeDoctorName());
                ybAppealManage.setRefuseReason("");
                ybAppealManage.setOperateProcess("变更申请-同意");
                ybAppealManage.setApprovalState(YbDefaultValue.APPROVALSTATE_1);

                LambdaQueryWrapper<YbAppealManage> queryWrapper = new LambdaQueryWrapper<>();
                queryWrapper.eq(YbAppealManage::getAcceptState, YbDefaultValue.ACCEPTSTATE_2);
                queryWrapper.eq(YbAppealManage::getId, ybAppealManage.getId());
                this.baseMapper.update(ybAppealManage, queryWrapper);

                msg = "已发布";
                personCode = ybAppealManage.getChangeDoctorCode();
            }

            this.save(newAppealManage);
            int nOpenSms = febsProperties.getOpenSms();
            boolean isOpenSms = nOpenSms == 1 ? true : false;
            if (isOpenSms) {
                String sendContent = this.iYbReconsiderApplyService.getSendMessage(entity.getApplyDateStr(), enableDate, entity.getTypeno());
                iComSmsService.sendSmsService(personCode, ComSms.SENDTYPE_3, sendContent, uId, Uname);
            }
        }
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
    public void updateCreateAdminAppealManage(YbAppealManage ybAppealManage, Long uId, String Uname) {
        Date thisDate = new Date();
        YbAppealManage entity = this.getById(ybAppealManage.getId());
        if (entity != null && (entity.getAcceptState() == YbDefaultValue.ACCEPTSTATE_0 || entity.getAcceptState() == YbDefaultValue.ACCEPTSTATE_1 || entity.getAcceptState() == YbDefaultValue.ACCEPTSTATE_2)) {
            YbAppealManage updateAppealManage = new YbAppealManage();
            updateAppealManage.setId(ybAppealManage.getId());
            updateAppealManage.setAcceptState(YbDefaultValue.ACCEPTSTATE_3);
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
            queryWrapper.eq(YbAppealManage::getAcceptState, entity.getAcceptState());
            queryWrapper.eq(YbAppealManage::getId, ybAppealManage.getId());
            this.baseMapper.update(updateAppealManage, queryWrapper);

            YbAppealManage newAppealManage = new YbAppealManage();
            newAppealManage.setId(UUID.randomUUID().toString());
            newAppealManage.setDataType(ybAppealManage.getDataType());
            newAppealManage.setSourceType(ybAppealManage.getSourceType());
            newAppealManage.setApplyDataId(ybAppealManage.getApplyDataId());
            newAppealManage.setVerifyId(ybAppealManage.getVerifyId());

            int day = iComConfiguremanageService.getConfigDay();
            Date enableDate = this.addDateMethod(thisDate, day);
            newAppealManage.setEnableDate(enableDate);
            //赋值 新增状态为0的数据 业务更改 管理员更改状态为1的数据 所有不赋值为0
            //newAppealManage.setAcceptState(0);
            //因业务需求管理员更改状态为1(接受)的数据
//        newAppealManage.setAcceptState(YbDefaultValue.ACCEPTSTATE_1);

            //因业务需求管理员更改状态传进来是什么就是什么
            if(entity.getAcceptState() == YbDefaultValue.ACCEPTSTATE_2){
                newAppealManage.setAcceptState(YbDefaultValue.ACCEPTSTATE_0);
            }else {
                newAppealManage.setAcceptState(entity.getAcceptState());
            }
            newAppealManage.setIsDeletemark(1);
            newAppealManage.setCreateUserId(uId);
            newAppealManage.setCreateTime(thisDate);
            newAppealManage.setOperateDate(thisDate);

            newAppealManage.setApplyDateStr(entity.getApplyDateStr());
            newAppealManage.setOrderNum(entity.getOrderNum());
            newAppealManage.setOrderNumber(entity.getOrderNumber());
            newAppealManage.setTypeno(entity.getTypeno());

            newAppealManage.setOrderDoctorCode(entity.getOrderDoctorCode());
            newAppealManage.setOrderDoctorName(entity.getOrderDoctorName());
            newAppealManage.setOrderDeptCode(entity.getOrderDeptCode());
            newAppealManage.setOrderDeptName(entity.getOrderDeptName());

            String strReadyDeptName = DataTypeHelpers.stringReplaceSetString(ybAppealManage.getReadyDeptName(), ybAppealManage.getReadyDeptCode() + "-");
            ybAppealManage.setReadyDeptName(strReadyDeptName);
            String strReadyDoctorName = DataTypeHelpers.stringReplaceSetString(ybAppealManage.getReadyDoctorName(), ybAppealManage.getReadyDoctorCode() + "-");
            ybAppealManage.setReadyDoctorName(strReadyDoctorName);

            newAppealManage.setReadyDeptCode(ybAppealManage.getReadyDeptCode());
            newAppealManage.setReadyDeptName(ybAppealManage.getReadyDeptName());
            newAppealManage.setReadyDoctorCode(ybAppealManage.getReadyDoctorCode());
            newAppealManage.setReadyDoctorName(ybAppealManage.getReadyDoctorName());
            newAppealManage.setVerifySendId(ybAppealManage.getVerifyId());
            newAppealManage.setOperateProcess("管理员更改-创建");

            String personCode = newAppealManage.getReadyDoctorCode();

            this.save(newAppealManage);
            int nOpenSms = febsProperties.getOpenSms();
            boolean isOpenSms = nOpenSms == 1 ? true : false;
            if (isOpenSms) {
                if(entity.getAcceptState() == YbDefaultValue.ACCEPTSTATE_1){
                    enableDate = null;
                }
                String sendContent = this.iYbReconsiderApplyService.getSendMessage(entity.getApplyDateStr(), enableDate, entity.getTypeno());
                iComSmsService.sendSmsService(personCode, ComSms.SENDTYPE_4, sendContent, uId, Uname);
            }
        }
    }


    @Override
    @Transactional
    public void updateExamineStates(YbAppealManage ybAppealManage) {
        LambdaQueryWrapper<YbAppealManage> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(YbAppealManage::getAcceptState, YbDefaultValue.ACCEPTSTATE_2);
        queryWrapper.eq(YbAppealManage::getId, ybAppealManage.getId());
        this.baseMapper.update(ybAppealManage, queryWrapper);
    }

    @Override
    public List<YbAppealManage> getUpdateAppealManageList(List<YbAppealManageView> appealManageList, Date endDateOne) {
        List<YbAppealManage> updateAppealManageList = new ArrayList<>();
        Date thisDate = new java.sql.Timestamp(new Date().getTime());
        int day = iComConfiguremanageService.getConfigDay();
        Date addDate = DataTypeHelpers.addDateMethod(thisDate, day);
        for (YbAppealManageView item : appealManageList) {
            YbAppealManage updateAppealManage = new YbAppealManage();
            updateAppealManage.setId(item.getId());
            updateAppealManage.setAcceptState(YbDefaultValue.ACCEPTSTATE_1);
            updateAppealManage.setEnableDate(addDate);
            updateAppealManageList.add(updateAppealManage);
        }

        return updateAppealManageList;
    }
}