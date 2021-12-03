package cc.mrbird.febs.yb.service.impl;

import cc.mrbird.febs.com.controller.DataTypeHelpers;
import cc.mrbird.febs.com.entity.ComFile;
import cc.mrbird.febs.com.entity.ComSms;
import cc.mrbird.febs.com.entity.OutComArea;
import cc.mrbird.febs.com.service.IComConfiguremanageService;
import cc.mrbird.febs.com.service.IComFileService;
import cc.mrbird.febs.com.service.IComSmsService;
import cc.mrbird.febs.common.domain.QueryRequest;
import cc.mrbird.febs.common.properties.FebsProperties;
import cc.mrbird.febs.common.utils.SortUtil;
import cc.mrbird.febs.yb.dao.YbAppealManageMapper;
import cc.mrbird.febs.yb.entity.*;
import cc.mrbird.febs.yb.service.IYbAppealManageService;
import cc.mrbird.febs.yb.service.IYbAppealResultService;
import cc.mrbird.febs.yb.service.IYbPersonService;
import cc.mrbird.febs.yb.service.IYbReconsiderApplyService;
import cn.hutool.core.lang.Validator;
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
    public IYbPersonService iYbPersonService;

    @Autowired
    public IComFileService iComFileService;

    @Autowired
    FebsProperties febsProperties;

    @Override
    public IPage<YbAppealManage> findYbAppealManages(QueryRequest request, YbAppealManage ybAppealManage) {
        try {
            LambdaQueryWrapper<YbAppealManage> queryWrapper = new LambdaQueryWrapper<>();

            if (ybAppealManage.getApplyDateStr() != null) {
                queryWrapper.eq(YbAppealManage::getApplyDateStr, ybAppealManage.getApplyDateStr());
            }
            if (ybAppealManage.getApplyDataId() != null) {
                queryWrapper.eq(YbAppealManage::getApplyDataId, ybAppealManage.getApplyDataId());
            }

            if (ybAppealManage.getDataType() != null) {
                queryWrapper.eq(YbAppealManage::getDataType, ybAppealManage.getDataType());
            }

            if (ybAppealManage.getTypeno() != null) {
                queryWrapper.eq(YbAppealManage::getTypeno, ybAppealManage.getTypeno());
            }
            if (ybAppealManage.getAcceptState() != null) {
                queryWrapper.eq(YbAppealManage::getAcceptState, ybAppealManage.getAcceptState());
            }
            queryWrapper.eq(YbAppealManage::getIsDeletemark, 1);//1是未删 0是已删

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
//        ybAppealManage.setCreateTime(new Date());
        if (ybAppealManage.getId() == null || "".equals(ybAppealManage.getId())) {
            ybAppealManage.setId(UUID.randomUUID().toString());
        }
        ybAppealManage.setIsDeletemark(1);
        this.save(ybAppealManage);

    }

    @Override
    @Transactional
    public void updateYbAppealManage(YbAppealManage ybAppealManage) {
//        ybAppealManage.setModifyTime(new Date());
        this.baseMapper.updateYbAppealManage(ybAppealManage);
    }

    @Override
    @Transactional
    public void deleteYbAppealManages(String[] Ids) {
        List<String> list = Arrays.asList(Ids);
        this.baseMapper.deleteBatchIds(list);
    }

    @Override
    public int findAppealManageResetCheckCounts(String applyDateStr, Integer areaType) {
        return this.baseMapper.findAppealManageResetCheckCount(applyDateStr, areaType);
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
//                        item.setModifyUserId(uId);
//                        item.setModifyTime(thisDate);
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

    private boolean createUpdateAcceptAppealResult(YbAppealManage ybAppealManage, Date thisDate, Long uId, String Uname,YbAppealResult appealResult) {
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
        newAppealResult.setAreaType(ybAppealManage.getAreaType());

        newAppealResult.setOrderDoctorCode(ybAppealManage.getOrderDoctorCode());
        newAppealResult.setOrderDoctorName(ybAppealManage.getOrderDoctorName());
        newAppealResult.setOrderDeptCode(ybAppealManage.getOrderDeptCode());
        newAppealResult.setOrderDeptName(ybAppealManage.getOrderDeptName());
        if(appealResult != null) {
            newAppealResult.setRelatelDataId(appealResult.getRelatelDataId());
        }
        newAppealResult.setOperateDate(thisDate);
        newAppealResult.setOperateReason(ybAppealManage.getOperateReason());
//        newAppealResult.setCreateUserId(uId);
//        newAppealResult.setCreateTime(thisDate);
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
            isUpdate = iYbReconsiderApplyService.findReconsiderApplyCheckEndDate(entity.getApplyDateStr(), entity.getAreaType(), entity.getTypeno());
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
                        YbAppealResult appealResult = null;
                        if(entity.getSourceType() == 1) {
                            YbAppealResult queryAr = new YbAppealResult();
                            queryAr.setApplyDateStr(entity.getApplyDateStr());
                            queryAr.setAreaType(entity.getAreaType());
                            queryAr.setSourceType(0);
                            queryAr.setApplyDataId(entity.getApplyDataId());
                            List<YbAppealResult> ybAppealResultList = iYbAppealResultService.findAppealResultList(queryAr);
                            if(ybAppealResultList.size() > 0) {
                                appealResult = ybAppealResultList.get(0);
                            }
                        }
                        ybAppealManage.setApplyDateStr(entity.getApplyDateStr());
                        ybAppealManage.setOrderNum(entity.getOrderNum());
                        ybAppealManage.setOrderNumber(entity.getOrderNumber());
                        ybAppealManage.setTypeno(entity.getTypeno());

                        ybAppealManage.setOrderDoctorCode(entity.getOrderDoctorCode());
                        ybAppealManage.setOrderDoctorName(entity.getOrderDoctorName());
                        ybAppealManage.setOrderDeptCode(entity.getOrderDeptCode());
                        ybAppealManage.setOrderDeptName(entity.getOrderDeptName());
                        ybAppealManage.setAreaType(entity.getAreaType());
                        isUpdate = this.createUpdateAcceptAppealResult(ybAppealManage, thisDate, uId, Uname,appealResult);

                        updateAppealManage.setOperateProcess("待申诉-已申诉");
                    }
                    updateAppealManage.setId(ybAppealManage.getId());
//                    updateAppealManage.setModifyUserId(uId);
//                    updateAppealManage.setModifyTime(thisDate);
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
            isUpdate = iYbReconsiderApplyService.findReconsiderApplyCheckEndDate(entity.getApplyDateStr(), entity.getAreaType(), entity.getTypeno());
            if (entity.getTypeno() == YbDefaultValue.TYPENO_1) {
                mms = "第一版";
            } else {
                mms = "第二版";
            }
            if (isUpdate || entity.getSourceType() == YbDefaultValue.SOURCETYPE_1) {
                if (entity.getAcceptState() == YbDefaultValue.ACCEPTSTATE_6) {
                    YbAppealResult updateAppealResult = new YbAppealResult();
                    updateAppealResult.setId(ybAppealManage.getId());
//                    updateAppealResult.setModifyUserId(uId);
//                    updateAppealResult.setModifyTime(thisDate);
                    updateAppealResult.setOperateDate(thisDate);
                    updateAppealResult.setOperateReason(ybAppealManage.getOperateReason());
                    this.iYbAppealResultService.updateById(updateAppealResult);

                    YbAppealManage updateAppealManage = new YbAppealManage();
                    updateAppealManage.setId(ybAppealManage.getId());
//                    updateAppealManage.setModifyUserId(uId);
//                    updateAppealManage.setModifyTime(thisDate);
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
//            newAppealManage.setCreateUserId(uId);
//            newAppealManage.setCreateTime(thisDate);
            Date newDate = this.addSecond(thisDate,10);
            newAppealManage.setOperateDate(newDate);
            newAppealManage.setSourceType(ybAppealManage.getSourceType());
            if(entity.getSourceType() == 0) {
                newAppealManage.setVerifySendId(ybAppealManage.getVerifyId());
            }else {
                newAppealManage.setVerifySendId(entity.getVerifySendId());
            }
            newAppealManage.setDataType(ybAppealManage.getDataType());
            newAppealManage.setAreaType(entity.getAreaType());
//            int day = iComConfiguremanageService.getConfigDay();
            //变更日期不变 this.addDateMethod(thisDate, day);
            Date enableDate = entity.getEnableDate();
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
//            ybAppealManage.setModifyUserId(uId);
//            ybAppealManage.setModifyTime(thisDate);

            String strReadyDeptName = DataTypeHelpers.stringReplaceSetString(ybAppealManage.getReadyDeptName(), ybAppealManage.getReadyDeptCode() + "-");
            ybAppealManage.setReadyDeptName(strReadyDeptName);
            String strReadyDoctorName = DataTypeHelpers.stringReplaceSetString(ybAppealManage.getReadyDoctorName(), ybAppealManage.getReadyDoctorCode() + "-");
            ybAppealManage.setReadyDoctorName(strReadyDoctorName);

            String strChangeDeptName = DataTypeHelpers.stringReplaceSetString(ybAppealManage.getChangeDeptName(), ybAppealManage.getChangeDeptCode() + "-");
            ybAppealManage.setChangeDeptName(strChangeDeptName);
            String strChangeDoctorName = DataTypeHelpers.stringReplaceSetString(ybAppealManage.getChangeDoctorName(), ybAppealManage.getChangeDoctorCode() + "-");
            ybAppealManage.setChangeDoctorName(strChangeDoctorName);

            YbAppealManage updateManage = new YbAppealManage();
            updateManage.setAcceptState(YbDefaultValue.ACCEPTSTATE_4);
            updateManage.setOperateDate(thisDate);
            if (type == 2) {
                //拒绝
                newAppealManage.setReadyDeptCode(ybAppealManage.getReadyDeptCode());
                newAppealManage.setReadyDeptName(ybAppealManage.getReadyDeptName());
                newAppealManage.setReadyDoctorCode(ybAppealManage.getReadyDoctorCode());
                newAppealManage.setReadyDoctorName(ybAppealManage.getReadyDoctorName());

                updateManage.setRefuseId(uId);
                updateManage.setRefuseName(Uname);
                updateManage.setRefuseDate(thisDate);
                updateManage.setRefuseReason(ybAppealManage.getRefuseReason());
                updateManage.setOperateProcess("变更申请-拒绝");
                updateManage.setApprovalState(YbDefaultValue.APPROVALSTATE_2);

                personCode = ybAppealManage.getReadyDoctorCode();
            } else {
                //同意
                newAppealManage.setReadyDeptCode(ybAppealManage.getChangeDeptCode());
                newAppealManage.setReadyDeptName(ybAppealManage.getChangeDeptName());
                newAppealManage.setReadyDoctorCode(ybAppealManage.getChangeDoctorCode());
                newAppealManage.setReadyDoctorName(ybAppealManage.getChangeDoctorName());

//                updateManage.setRefuseReason("");
                updateManage.setOperateProcess("变更申请-同意");
                updateManage.setApprovalState(YbDefaultValue.APPROVALSTATE_1);

                personCode = ybAppealManage.getChangeDoctorCode();
            }

            LambdaQueryWrapper<YbAppealManage> queryWrapper = new LambdaQueryWrapper<>();
            queryWrapper.eq(YbAppealManage::getAcceptState, YbDefaultValue.ACCEPTSTATE_2);
            queryWrapper.eq(YbAppealManage::getId, ybAppealManage.getId());
            this.baseMapper.update(updateManage, queryWrapper);

            this.save(newAppealManage);
            int nOpenSms = febsProperties.getOpenSms();
            boolean isOpenSms = nOpenSms == 1 ? true : false;
            if (isOpenSms) {
                String sendContent = this.iYbReconsiderApplyService.getSendMessage(entity.getApplyDateStr(), enableDate, entity.getAreaType(), entity.getTypeno(), true);
                iComSmsService.sendSmsService(entity.getApplyDateStr(), entity.getTypeno(), personCode, ComSms.SENDTYPE_3, entity.getAreaType(), sendContent, uId, Uname);
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
    public List<YbAppealManage> findAppealManageList(YbAppealManage ybAppealManage) {
        LambdaQueryWrapper<YbAppealManage> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(YbAppealManage::getApplyDateStr, ybAppealManage.getApplyDateStr());
        queryWrapper.eq(YbAppealManage::getAreaType, ybAppealManage.getAreaType());

        if(ybAppealManage.getAcceptState() != null) {
            queryWrapper.eq(YbAppealManage::getAcceptState, ybAppealManage.getAcceptState());
        }
        if (ybAppealManage.getReadyDoctorCode() != null) {
            queryWrapper.eq(YbAppealManage::getReadyDoctorCode, ybAppealManage.getReadyDoctorCode());
        }
        if (ybAppealManage.getReadyDeptName() != null) {
            queryWrapper.eq(YbAppealManage::getReadyDeptName, ybAppealManage.getReadyDeptName());
        }
        if (ybAppealManage.getOrderDoctorCode() != null) {
            queryWrapper.eq(YbAppealManage::getOrderDoctorCode, ybAppealManage.getOrderDoctorCode());
        }
        if (ybAppealManage.getOrderNumber() != null) {
            queryWrapper.eq(YbAppealManage::getOrderNumber, ybAppealManage.getOrderNumber());
        }
        if (ybAppealManage.getTypeno() != null) {
            queryWrapper.eq(YbAppealManage::getTypeno, ybAppealManage.getTypeno());
        }
        if (ybAppealManage.getDataType() != null) {
            queryWrapper.eq(YbAppealManage::getDataType, ybAppealManage.getDataType());
        }
        if (ybAppealManage.getSourceType() != null) {
            queryWrapper.eq(YbAppealManage::getSourceType, ybAppealManage.getSourceType());
        }
        return this.list(queryWrapper);
    }

    @Override
    @Transactional
    public void updateCreateAdminAppealManage(YbAppealManage ybAppealManage, Long uId, String Uname) {
        Date thisDate = new Date();
        YbAppealManage entity = this.getById(ybAppealManage.getId());
        if (entity != null && (entity.getAcceptState() == YbDefaultValue.ACCEPTSTATE_0 ||
                entity.getAcceptState() == YbDefaultValue.ACCEPTSTATE_1 ||
                entity.getAcceptState() == YbDefaultValue.ACCEPTSTATE_6 ||
                entity.getAcceptState() == YbDefaultValue.ACCEPTSTATE_7)) {
            YbAppealManage updateAppealManage = new YbAppealManage();
            updateAppealManage.setId(ybAppealManage.getId());
            String strReadyDeptName = DataTypeHelpers.stringReplaceSetString(ybAppealManage.getReadyDeptName(), ybAppealManage.getReadyDeptCode() + "-");
            String strReadyDoctorName = DataTypeHelpers.stringReplaceSetString(ybAppealManage.getReadyDoctorName(), ybAppealManage.getReadyDoctorCode() + "-");
            boolean isChang = false;

            if (!entity.getReadyDoctorCode().equals(ybAppealManage.getReadyDoctorCode()) ||
                    !entity.getReadyDeptCode().equals(ybAppealManage.getReadyDeptCode())) {
                isChang = true;
            }
            int n7State = 10;
            if (entity.getAcceptState() == YbDefaultValue.ACCEPTSTATE_0){
                n7State = 0;
            }
            if (n7State != 0 && entity.getAcceptState() == YbDefaultValue.ACCEPTSTATE_7) {
                n7State = 7;
                if (isChang) {
                    n7State = 71;
                }
            }
            // 变更管理，未申诉，不新增，只修改
            if (n7State != 0 && n7State != 7 && n7State != 71 && isChang) {
                n7State = 0;
            }

            if (n7State != 0) {
                //修改当前 Change值
                if(n7State == 71) {
                    updateAppealManage.setReadyDoctorCode(ybAppealManage.getReadyDoctorCode());
                    updateAppealManage.setReadyDoctorName(strReadyDoctorName);
                    updateAppealManage.setReadyDeptCode(ybAppealManage.getReadyDeptCode());
                    updateAppealManage.setReadyDeptName(strReadyDeptName);
                    updateAppealManage.setChangeDoctorCode(entity.getReadyDoctorCode());
                    updateAppealManage.setChangeDoctorName(entity.getReadyDoctorName());
                    updateAppealManage.setChangeDeptCode(entity.getReadyDeptCode());
                    updateAppealManage.setChangeDeptName(entity.getReadyDeptName());
                }
                updateAppealManage.setAcceptState(ybAppealManage.getAcceptState());
                updateAppealManage.setOperateReason("");
            } else {
                updateAppealManage.setAcceptState(YbDefaultValue.ACCEPTSTATE_3);
                updateAppealManage.setChangeDoctorCode(ybAppealManage.getReadyDoctorCode());
                updateAppealManage.setChangeDoctorName(strReadyDoctorName);
                updateAppealManage.setChangeDeptCode(ybAppealManage.getReadyDeptCode());
                updateAppealManage.setChangeDeptName(strReadyDeptName);
            }
            if(entity.getAcceptState() == YbDefaultValue.ACCEPTSTATE_7 && ybAppealManage.getAcceptState() == YbDefaultValue.ACCEPTSTATE_6){
                updateAppealManage.setOperateProcess("未申诉-已申诉");
            }
            if(entity.getAcceptState() == YbDefaultValue.ACCEPTSTATE_6 && ybAppealManage.getAcceptState() == YbDefaultValue.ACCEPTSTATE_1){
                updateAppealManage.setOperateProcess("已申诉-待申诉");
            }
            if(entity.getAcceptState() == YbDefaultValue.ACCEPTSTATE_6 && ybAppealManage.getAcceptState() == YbDefaultValue.ACCEPTSTATE_0){
                updateAppealManage.setOperateProcess("已申诉-接受申请");
            }
            if(entity.getAcceptState() == YbDefaultValue.ACCEPTSTATE_1 && ybAppealManage.getAcceptState() == YbDefaultValue.ACCEPTSTATE_0){
                updateAppealManage.setOperateProcess("待申诉-接受申请");
            }
            if(entity.getAcceptState() == ybAppealManage.getAcceptState()){
                updateAppealManage.setOperateProcess("非状态变更");
            }
            updateAppealManage.setOperateDate(thisDate);
            updateAppealManage.setAdminPersonId(uId);
            updateAppealManage.setAdminPersonName(Uname);
            String adminReason = "管理员更改";
            if (entity.getAcceptState() == YbDefaultValue.ACCEPTSTATE_0) {
                adminReason += "-接受申请";
            } else if (entity.getAcceptState() == YbDefaultValue.ACCEPTSTATE_1) {
                adminReason += "-待申诉";
            } else if (entity.getAcceptState() == YbDefaultValue.ACCEPTSTATE_6) {
                adminReason += "-已申请";
            } else {
                adminReason += "-未申请";
            }

            updateAppealManage.setAdminReason(adminReason);
            updateAppealManage.setAdminChangeDate(thisDate);
            //方法 更改状态为0的数据 业务更改 管理员更改状态为1的数据 所有不调用该方法
            //方法 更改状态为1的数据
            LambdaQueryWrapper<YbAppealManage> queryWrapper = new LambdaQueryWrapper<>();
            queryWrapper.eq(YbAppealManage::getAcceptState, entity.getAcceptState());
            queryWrapper.eq(YbAppealManage::getId, ybAppealManage.getId());
            this.baseMapper.update(updateAppealManage, queryWrapper);
            // 未申诉，修改结果 科室、人员 值
            if(n7State == 71) {
                YbAppealResult ybAppealResult = new YbAppealResult();
                ybAppealResult.setId(entity.getId());
                ybAppealResult.setOperateReason("");
                ybAppealResult.setDoctorCode(ybAppealManage.getReadyDoctorCode());
                ybAppealResult.setDoctorName(strReadyDoctorName);
                ybAppealResult.setDeptCode(ybAppealManage.getReadyDeptCode());
                ybAppealResult.setDeptName(strReadyDeptName);
                iYbAppealResultService.updateById(ybAppealResult);
            }
            // 待申诉 和 已申诉 删除 结果和附件
            if (entity.getAcceptState() == YbDefaultValue.ACCEPTSTATE_1 ||
                    entity.getAcceptState() == YbDefaultValue.ACCEPTSTATE_6) {
                iYbAppealResultService.getBaseMapper().deleteById(entity.getId());
                List<ComFile> list = this.iComFileService.findListComFile(entity.getId(),null);
                if (list.size() > 0) {
                    this.iComFileService.batchRefIdDelete(entity.getId());
                }
                String strSourceType = entity.getSourceType() == 0 ? "In" : "Out";
                if (entity.getAreaType() != 0) {
                    strSourceType += entity.getAreaType();
                }
                String deptName = entity.getReadyDoctorCode() + "/" + strSourceType;
                String filePath = febsProperties.getUploadPath(); // 上传后的路径
                for (ComFile cf : list) {
                    String fileUrl = filePath + entity.getApplyDateStr() + "/" + deptName + "/" + cf.getServerName();
                    DataTypeHelpers.deleteFile(fileUrl);
                }
            }
            // 管理员变更 复制新增 数据
            if (n7State == 0) {
                YbAppealManage newAppealManage = new YbAppealManage();
                newAppealManage.setId(UUID.randomUUID().toString());
                newAppealManage.setDataType(ybAppealManage.getDataType());
                newAppealManage.setSourceType(ybAppealManage.getSourceType());
                newAppealManage.setApplyDataId(ybAppealManage.getApplyDataId());
                newAppealManage.setVerifyId(ybAppealManage.getVerifyId());

//            int day = iComConfiguremanageService.getConfigDay();
                ////变更日期不变 this.addDateMethod(thisDate, day);
                Date enableDate = entity.getEnableDate();
                newAppealManage.setEnableDate(enableDate);
                newAppealManage.setAcceptState(ybAppealManage.getAcceptState());
                newAppealManage.setIsDeletemark(1);
                thisDate = this.addSecond(thisDate,10);
                newAppealManage.setOperateDate(thisDate);

                newAppealManage.setApplyDateStr(entity.getApplyDateStr());
                newAppealManage.setOrderNum(entity.getOrderNum());
                newAppealManage.setOrderNumber(entity.getOrderNumber());
                newAppealManage.setTypeno(entity.getTypeno());

                newAppealManage.setOrderDoctorCode(entity.getOrderDoctorCode());
                newAppealManage.setOrderDoctorName(entity.getOrderDoctorName());
                newAppealManage.setOrderDeptCode(entity.getOrderDeptCode());
                newAppealManage.setOrderDeptName(entity.getOrderDeptName());

                ybAppealManage.setReadyDeptName(strReadyDeptName);
                ybAppealManage.setReadyDoctorName(strReadyDoctorName);

                newAppealManage.setReadyDeptCode(ybAppealManage.getReadyDeptCode());
                newAppealManage.setReadyDeptName(ybAppealManage.getReadyDeptName());
                newAppealManage.setReadyDoctorCode(ybAppealManage.getReadyDoctorCode());
                newAppealManage.setReadyDoctorName(ybAppealManage.getReadyDoctorName());
                if(entity.getSourceType() == 0) {
                    newAppealManage.setVerifySendId(ybAppealManage.getVerifyId());
                }else {
                    newAppealManage.setVerifySendId(entity.getVerifySendId());
                }
                newAppealManage.setAreaType(entity.getAreaType());
                newAppealManage.setOperateProcess("管理员更改-创建");

                String personCode = newAppealManage.getReadyDoctorCode();

                this.save(newAppealManage);

                int nOpenSms = febsProperties.getOpenSms();
                boolean isOpenSms = nOpenSms == 1 ? true : false;
                if (isOpenSms) {
                    if (entity.getAcceptState() == YbDefaultValue.ACCEPTSTATE_1) {
                        enableDate = null;
                    }
                    String sendContent = this.iYbReconsiderApplyService.getSendMessage(entity.getApplyDateStr(), enableDate, entity.getAreaType(), entity.getTypeno(), true);
                    iComSmsService.sendSmsService(entity.getApplyDateStr(), entity.getTypeno(), personCode, ComSms.SENDTYPE_4, entity.getAreaType(), sendContent, uId, Uname);
                }
            }
        }
    }
    private Date addSecond(Date date,int t){
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        cal.add(Calendar.SECOND, t);// 24小时制
        return cal.getTime();
    }
    //未使用
    @Override
    public List<YbAppealManage> getUpdateAppealManageList(List<YbAppealManageView> appealManageList, Date endDateOne) {
        List<YbAppealManage> updateAppealManageList = new ArrayList<>();
        Date thisDate = new java.sql.Timestamp(new Date().getTime());
        int day = iComConfiguremanageService.getConfigDay();
        //加1 表示忽略当前日期，从第二天开始
        Date addDate = DataTypeHelpers.addDateMethod(thisDate, day + 1);
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