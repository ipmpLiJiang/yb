package cc.mrbird.febs.yb.service.impl;

import cc.mrbird.febs.com.controller.DataTypeHelpers;
import cc.mrbird.febs.com.entity.ComConfiguremanage;
import cc.mrbird.febs.com.entity.ComSms;
import cc.mrbird.febs.com.service.IComConfiguremanageService;
import cc.mrbird.febs.com.service.IComSmsService;
import cc.mrbird.febs.common.domain.QueryRequest;
import cc.mrbird.febs.common.properties.FebsProperties;
import cc.mrbird.febs.common.utils.SortUtil;
import cc.mrbird.febs.system.domain.User;
import cc.mrbird.febs.system.service.UserService;
import cc.mrbird.febs.yb.entity.*;
import cc.mrbird.febs.yb.dao.YbHandleVerifyDataMapper;
import cc.mrbird.febs.yb.service.*;
import cn.hutool.core.lang.Validator;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.aspectj.lang.annotation.Around;
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
 * @since 2020-08-28
 */
@Slf4j
@Service("IYbHandleVerifyDataService")
@Transactional(propagation = Propagation.SUPPORTS, readOnly = true, rollbackFor = Exception.class)
public class YbHandleVerifyDataServiceImpl extends ServiceImpl<YbHandleVerifyDataMapper, YbHandleVerifyData> implements IYbHandleVerifyDataService {

    @Autowired
    IYbAppealResultService iYbAppealResultService;
    @Autowired
    IYbHandleVerifyService iYbHandleVerifyService;
    @Autowired
    IComConfiguremanageService iComConfiguremanageService;
    @Autowired
    IYbAppealManageService iYbAppealManageService;

    @Autowired
    IYbReconsiderApplyService iYbReconsiderApplyService;

    @Autowired
    IComSmsService iComSmsService;

    @Autowired
    FebsProperties febsProperties;

    @Autowired
    IYbPersonService iYbPersonService;

    @Override
    public IPage<YbHandleVerifyData> findYbHandleVerifyDatas(QueryRequest request, YbHandleVerifyData ybHandleVerifyData) {
        try {
            LambdaQueryWrapper<YbHandleVerifyData> queryWrapper = new LambdaQueryWrapper<>();
            queryWrapper.eq(YbHandleVerifyData::getIsDeletemark, 1);//1是未删 0是已删


            Page<YbHandleVerifyData> page = new Page<>();
            SortUtil.handlePageSort(request, page, false);//true 是属性  false是数据库字段可两个
            return this.page(page, queryWrapper);
        } catch (Exception e) {
            log.error("获取字典信息失败", e);
            return null;
        }
    }

    @Override
    public IPage<YbHandleVerifyData> findYbHandleVerifyDataList(QueryRequest request, YbHandleVerifyData ybHandleVerifyData) {
        try {
            Page<YbHandleVerifyData> page = new Page<>();
            SortUtil.handlePageSort(request, page, false);//true 是属性  false是数据库字段可两个
            return this.baseMapper.findYbHandleVerifyData(page, ybHandleVerifyData);
        } catch (Exception e) {
            log.error("获取失败", e);
            return null;
        }
    }

    @Override
    @Transactional
    public void createYbHandleVerifyData(YbHandleVerifyData ybHandleVerifyData) {
        ybHandleVerifyData.setId(UUID.randomUUID().toString());
        ybHandleVerifyData.setCreateTime(new Date());
        ybHandleVerifyData.setIsDeletemark(1);
        this.save(ybHandleVerifyData);
    }

    @Override
    @Transactional
    public void updateYbHandleVerifyData(YbHandleVerifyData ybHandleVerifyData) {
        ybHandleVerifyData.setModifyTime(new Date());
        this.baseMapper.updateYbHandleVerifyData(ybHandleVerifyData);
    }

    @Override
    @Transactional
    public void deleteYbHandleVerifyDatas(String[] Ids) {
        List<String> list = Arrays.asList(Ids);
        this.baseMapper.deleteBatchIds(list);
    }

    //获取state=2 已剔除数据
    @Override
    @Transactional
    public void importCreateHandleVerifyData(String applyDateStr,Integer areaType, Long uid, String uname) {
        YbReconsiderApply ybReconsiderApply = this.iYbReconsiderApplyService.findReconsiderApplyByApplyDateStrs(applyDateStr,areaType);
        if (ybReconsiderApply != null) {
            if (ybReconsiderApply.getResetState() == 1) {
                Date thisDate = new java.sql.Timestamp(new Date().getTime());
                YbHandleVerify handleVerify = iYbHandleVerifyService.findYbHandleVerifyApplyDateStr(applyDateStr);
                String hvId = handleVerify == null ? null : handleVerify.getId();
                List<YbAppealResult> appealResultList = this.iYbAppealResultService.findAppealResulDataHandles(applyDateStr, hvId);
                if (appealResultList.size() > 0) {
                    LambdaQueryWrapper<YbHandleVerify> wrapper = new LambdaQueryWrapper<>();
                    wrapper.eq(YbHandleVerify::getApplyDateStr, applyDateStr);
                    List<YbHandleVerify> handleVerifyList = this.iYbHandleVerifyService.list(wrapper);
                    List<YbHandleVerifyData> insertHandleDataList = new ArrayList<>();
                    String guid = UUID.randomUUID().toString();

                    if (handleVerifyList.size() == 0) {
                        YbHandleVerify insert = new YbHandleVerify();
                        insert.setId(guid);
                        insert.setApplyDateStr(applyDateStr);
                        insert.setIsDeletemark(1);
                        insert.setCreateTime(thisDate);
                        insert.setCreateUserId(uid);
                        this.iYbHandleVerifyService.save(insert);
                    } else {
                        guid = handleVerifyList.get(0).getId();
                    }

                    for (YbAppealResult item : appealResultList) {
                        YbHandleVerifyData insertData = new YbHandleVerifyData();
                        insertData.setId(UUID.randomUUID().toString());
                        insertData.setPid(guid);
                        insertData.setState(YbDefaultValue.VERIFYDATASTATE_1);
                        insertData.setIsDeletemark(1);
                        insertData.setApplyDataId(item.getApplyDataId());
                        insertData.setVerifyId(item.getVerifyId());
                        insertData.setManageId(item.getManageId());
                        insertData.setResultId(item.getId());
//                        insertData.setResetId(item.getResetDataId());
                        insertData.setDataType(item.getDataType());
                        insertData.setApplyDateStr(item.getApplyDateStr());
                        insertData.setOrderNum(item.getOrderNum());
                        insertData.setOrderNumber(item.getOrderNumber());
                        insertData.setTypeno(item.getTypeno());
                        insertData.setDeptCode(item.getDeptCode());
                        insertData.setDeptName(item.getDeptName());
                        insertData.setDoctorCode(item.getDoctorCode());
                        insertData.setDoctorName(item.getDoctorName());
                        insertData.setMatchPersonId(uid);
                        insertData.setMatchPersonName(uname);
                        insertData.setMatchDate(thisDate);

                        insertData.setCreateTime(thisDate);
                        insertData.setCreateUserId(uid);
                        insertData.setRelatelDataId(item.getRelatelDataId());

                        insertData.setOrderDoctorCode(item.getOrderDoctorCode());
                        insertData.setOrderDoctorName(item.getOrderDoctorName());
                        insertData.setOrderDeptCode(item.getOrderDeptCode());
                        insertData.setOrderDeptName(item.getOrderDeptName());

                        insertHandleDataList.add(insertData);
                    }
                    this.saveBatch(insertHandleDataList);
                }
            }
        }

    }

    private List<YbPerson> findPerson(List<YbHandleVerifyData> list) {
        List<YbPerson> personList = new ArrayList<>();
        ArrayList<String> personCodeList = new ArrayList<>();
        for (YbHandleVerifyData item : list) {
            if (item.getDoctorCode() != "" && item.getDoctorCode() != null) {
                if (personCodeList.stream().filter(s -> s.equals(item.getDoctorCode())).count() == 0) {
                    personCodeList.add(item.getDoctorCode());
                }
            }
        }
        if (personCodeList.size() > 0) {
            personList = this.iYbPersonService.findPersonList(personCodeList);
        }
        return personList;
    }

    private List<YbHandleVerifyData> findHandleVerifyDataByIdList(List<YbHandleVerifyData> list) {
        List<YbHandleVerifyData> hvdList = new ArrayList<>();
        ArrayList<String> hvdIdList = new ArrayList<>();
        for (YbHandleVerifyData item : list) {
            hvdIdList.add(item.getId());
        }
        if (hvdIdList.size() > 0) {
            LambdaQueryWrapper<YbHandleVerifyData> wrapper = new LambdaQueryWrapper<>();
            wrapper.in(YbHandleVerifyData::getId, hvdIdList);
            hvdList = this.list(wrapper);
        }
        return hvdList;
    }

    @Override
    @Transactional
    public void updateSendStates(List<YbHandleVerifyData> list, Long uId, String Uname) {
        Date thisDate = new java.sql.Timestamp(new Date().getTime());
        int day = iComConfiguremanageService.getConfigDay();
        List<YbHandleVerifyData> updateHandleVerifyList = new ArrayList<>();
        List<YbAppealManage> appealManageList = new ArrayList<>();
        Date addDate = DataTypeHelpers.addDateMethod(thisDate, day);

        List<YbPerson> personList = this.findPerson(list);
        List<YbPerson> queryPersonList = new ArrayList<>();
        List<YbHandleVerifyData> hvdList = this.findHandleVerifyDataByIdList(list);
        List<YbHandleVerifyData> queryHvdList = new ArrayList<>();
        List<ComSms> smsList = new ArrayList<>();
        List<ComSms> saveSmsList = new ArrayList<>();
        List<String> userCodeList = new ArrayList<>();
        int nOpenSms = febsProperties.getOpenSms();
        boolean isOpenSms = nOpenSms == 1 ? true : false;
        if (isOpenSms) {
            ComSms qu = new ComSms();
            qu.setState(ComSms.STATE_0);
            qu.setSendType(ComSms.SENDTYPE_6);
            smsList = iComSmsService.findLmdSmsList(qu);
        }
        String applyDateStr = "";
        for (YbHandleVerifyData ybHandleVerifyData : list) {
            queryPersonList = personList.stream().filter(
                    s -> s.getPersonCode().equals(ybHandleVerifyData.getDoctorCode())
            ).collect(Collectors.toList());
            if (queryPersonList.size() > 0) {
                //更新
                YbHandleVerifyData updateHandleVerify = new YbHandleVerifyData();
                updateHandleVerify.setId(ybHandleVerifyData.getId());
                updateHandleVerify.setState(YbDefaultValue.VERIFYDATASTATE_3);
                updateHandleVerify.setModifyUserId(uId);
                updateHandleVerify.setModifyTime(thisDate);
                updateHandleVerify.setSendPersonId(uId);
                updateHandleVerify.setSendPersonName(Uname);
                updateHandleVerify.setSendDate(thisDate);
                updateHandleVerify.setOperateDate(thisDate);
                //插入申诉管理
                YbAppealManage ybAppealManage = new YbAppealManage();
                ybAppealManage.setId(UUID.randomUUID().toString());
                ybAppealManage.setVerifyId(ybHandleVerifyData.getId());
                ybAppealManage.setVerifySendId(ybHandleVerifyData.getVerifyId());
                ybAppealManage.setApplyDataId(ybHandleVerifyData.getApplyDataId());
                ybAppealManage.setReadyDeptCode(ybHandleVerifyData.getDeptCode());
                ybAppealManage.setReadyDeptName(ybHandleVerifyData.getDeptName());
                ybAppealManage.setReadyDoctorCode(ybHandleVerifyData.getDoctorCode());
                ybAppealManage.setReadyDoctorName(ybHandleVerifyData.getDoctorName());
                ybAppealManage.setOperateDate(thisDate);
                ybAppealManage.setOperateProcess("发送操作-待申诉");
                ybAppealManage.setEnableDate(addDate);

                ybAppealManage.setIsDeletemark(1);
                ybAppealManage.setCreateUserId(uId);
                ybAppealManage.setApprovalState(0);
                ybAppealManage.setCreateTime(thisDate);

                ybAppealManage.setSourceType(YbDefaultValue.SOURCETYPE_1);
                ybAppealManage.setAcceptState(YbDefaultValue.ACCEPTSTATE_1);
                ybAppealManage.setDataType(ybHandleVerifyData.getDataType());

                queryHvdList = hvdList.stream().filter(s -> s.getId().equals(ybHandleVerifyData.getId())).collect(Collectors.toList());
                if (queryHvdList.size() > 0) {
                    ybAppealManage.setOrderDoctorCode(queryHvdList.get(0).getOrderDoctorCode());
                    ybAppealManage.setOrderDoctorName(queryHvdList.get(0).getOrderDoctorName());
                    ybAppealManage.setOrderDeptCode(queryHvdList.get(0).getOrderDeptCode());
                    ybAppealManage.setOrderDeptName(queryHvdList.get(0).getOrderDeptName());

                    ybAppealManage.setApplyDateStr(queryHvdList.get(0).getApplyDateStr());
                    ybAppealManage.setOrderNum(queryHvdList.get(0).getOrderNum());
                    ybAppealManage.setOrderNumber(queryHvdList.get(0).getOrderNumber());
                    ybAppealManage.setTypeno(queryHvdList.get(0).getTypeno());
                    if(applyDateStr.equals("")) {
                        applyDateStr = queryHvdList.get(0).getApplyDateStr();
                    }
                }

                updateHandleVerifyList.add(updateHandleVerify);
                appealManageList.add(ybAppealManage);

                if (isOpenSms) {
                    if (userCodeList.stream().filter(s -> s.equals(ybHandleVerifyData.getDoctorCode())).count() == 0) {
                        if (smsList.stream().filter(s -> s.getSendcode().equals(ybHandleVerifyData.getDoctorCode())).count() == 0) {
                            userCodeList.add(ybHandleVerifyData.getDoctorCode());

                            if (Validator.isMobile(queryPersonList.get(0).getTel())) {
                                ComSms comSms = new ComSms();
                                comSms.setId(UUID.randomUUID().toString());
                                comSms.setSendcode(queryPersonList.get(0).getPersonCode());
                                comSms.setSendname(queryPersonList.get(0).getPersonName());
                                comSms.setMobile(queryPersonList.get(0).getTel());
                                comSms.setSendType(ComSms.SENDTYPE_6);
                                comSms.setState(ComSms.STATE_0);

                                comSms.setSendcontent("医保管理平台提醒您，" + applyDateStr + "人工复议任务已发布，请尽快处理。");
                                comSms.setOperatorId(uId);
                                comSms.setOperatorName(Uname);
                                comSms.setIsDeletemark(1);
                                comSms.setCreateUserId(uId);
                                comSms.setCreateTime(thisDate);
                                saveSmsList.add(comSms);
                            }
                        }
                    }
                }
            }

        }

        if (appealManageList.size() > 0) {
            this.updateBatchById(updateHandleVerifyList);
            this.iYbAppealManageService.saveBatch(appealManageList);
            if (isOpenSms && saveSmsList.size() > 0) {
                this.iComSmsService.saveBatch(saveSmsList);
            }
        }
    }

    @Override
    @Transactional
    public void updateAllSendStates(String applyDateStr, Integer dataType, Integer state, Long uId, String Uname) {
        YbHandleVerify handleVerify = iYbHandleVerifyService.findYbHandleVerifyApplyDateStr(applyDateStr);
        if (handleVerify != null) {
            Date thisDate = new java.sql.Timestamp(new Date().getTime());
            int day = iComConfiguremanageService.getConfigDay();
            List<YbHandleVerifyData> updateHandleVerifyList = new ArrayList<>();
            List<YbAppealManage> appealManageList = new ArrayList<>();
            Date addDate = DataTypeHelpers.addDateMethod(thisDate, day);
            List<YbHandleVerifyData> list = this.baseMapper.findHandleVerifyDataList(handleVerify.getId(), dataType, state);

            List<YbPerson> personList = iYbPersonService.findPersonList(new YbPerson(), 0);
            List<YbPerson> queryPersonList = new ArrayList<>();
            List<ComSms> smsList = new ArrayList<>();
            List<ComSms> saveSmsList = new ArrayList<>();
            List<String> userCodeList = new ArrayList<>();
            int nOpenSms = febsProperties.getOpenSms();
            boolean isOpenSms = nOpenSms == 1 ? true : false;
            if (isOpenSms) {
                ComSms qu = new ComSms();
                qu.setState(ComSms.STATE_0);
                qu.setSendType(ComSms.SENDTYPE_6);
                smsList = iComSmsService.findLmdSmsList(qu);
            }
            for (YbHandleVerifyData ybHandleVerifyData : list) {
                if (ybHandleVerifyData.getState() != YbDefaultValue.VERIFYDATASTATE_3) {
                    queryPersonList = personList.stream().filter(
                            s -> s.getPersonCode().equals(ybHandleVerifyData.getDoctorCode())
                    ).collect(Collectors.toList());
                    if (queryPersonList.size() > 0) {
                        //更新
                        YbHandleVerifyData updateHandleVerify = new YbHandleVerifyData();
                        updateHandleVerify.setId(ybHandleVerifyData.getId());
                        updateHandleVerify.setState(YbDefaultValue.VERIFYDATASTATE_3);
                        updateHandleVerify.setModifyUserId(uId);
                        updateHandleVerify.setModifyTime(thisDate);
                        updateHandleVerify.setSendPersonId(uId);
                        updateHandleVerify.setSendPersonName(Uname);
                        updateHandleVerify.setSendDate(thisDate);
                        updateHandleVerify.setOperateDate(thisDate);
                        //插入申诉管理
                        YbAppealManage ybAppealManage = new YbAppealManage();
                        ybAppealManage.setId(UUID.randomUUID().toString());
                        ybAppealManage.setVerifyId(ybHandleVerifyData.getId());
                        ybAppealManage.setVerifySendId(ybHandleVerifyData.getVerifyId());
                        ybAppealManage.setApplyDataId(ybHandleVerifyData.getApplyDataId());
                        ybAppealManage.setReadyDeptCode(ybHandleVerifyData.getDeptCode());
                        ybAppealManage.setReadyDeptName(ybHandleVerifyData.getDeptName());
                        ybAppealManage.setReadyDoctorCode(ybHandleVerifyData.getDoctorCode());
                        ybAppealManage.setReadyDoctorName(ybHandleVerifyData.getDoctorName());
                        ybAppealManage.setOperateDate(thisDate);
                        ybAppealManage.setOperateProcess("发送操作-待申诉");
                        ybAppealManage.setEnableDate(addDate);

                        ybAppealManage.setIsDeletemark(1);
                        ybAppealManage.setCreateUserId(uId);
                        ybAppealManage.setCreateTime(thisDate);

                        ybAppealManage.setSourceType(YbDefaultValue.SOURCETYPE_1);
                        ybAppealManage.setAcceptState(YbDefaultValue.ACCEPTSTATE_1);
                        ybAppealManage.setDataType(ybHandleVerifyData.getDataType());

                        ybAppealManage.setApplyDateStr(ybHandleVerifyData.getApplyDateStr());
                        ybAppealManage.setOrderNum(ybHandleVerifyData.getOrderNum());
                        ybAppealManage.setOrderNumber(ybHandleVerifyData.getOrderNumber());
                        ybAppealManage.setTypeno(ybHandleVerifyData.getTypeno());

                        ybAppealManage.setOrderDoctorCode(ybHandleVerifyData.getOrderDoctorCode());
                        ybAppealManage.setOrderDoctorName(ybHandleVerifyData.getOrderDoctorName());
                        ybAppealManage.setOrderDeptCode(ybHandleVerifyData.getOrderDeptCode());
                        ybAppealManage.setOrderDeptName(ybHandleVerifyData.getOrderDeptName());

                        updateHandleVerifyList.add(updateHandleVerify);
                        appealManageList.add(ybAppealManage);

                        if (isOpenSms) {
                            if (userCodeList.stream().filter(s -> s.equals(ybHandleVerifyData.getDoctorCode())).count() == 0) {
                                if (smsList.stream().filter(s -> s.getSendcode().equals(ybHandleVerifyData.getDoctorCode())).count() == 0) {
                                    userCodeList.add(ybHandleVerifyData.getDoctorCode());

                                    if (Validator.isMobile(queryPersonList.get(0).getTel())) {
                                        ComSms comSms = new ComSms();
                                        comSms.setId(UUID.randomUUID().toString());
                                        comSms.setSendcode(queryPersonList.get(0).getPersonCode());
                                        comSms.setSendname(queryPersonList.get(0).getPersonName());
                                        comSms.setMobile(queryPersonList.get(0).getTel());
                                        comSms.setSendType(ComSms.SENDTYPE_6);
                                        comSms.setState(ComSms.STATE_0);

                                        comSms.setSendcontent("医保管理平台提醒您，" + applyDateStr + " 人工复议任务已发布，请尽快处理。");
                                        comSms.setOperatorId(uId);
                                        comSms.setOperatorName(Uname);
                                        comSms.setIsDeletemark(1);
                                        comSms.setCreateUserId(uId);
                                        comSms.setCreateTime(thisDate);
                                        saveSmsList.add(comSms);
                                    }
                                }
                            }
                        }
                    }
                }
            }

            if (appealManageList.size() > 0) {
                this.updateBatchById(updateHandleVerifyList);
                this.iYbAppealManageService.saveBatch(appealManageList);
                if (isOpenSms && saveSmsList.size() > 0) {
                    this.iComSmsService.saveBatch(saveSmsList);
                }
            }
        }
    }


}