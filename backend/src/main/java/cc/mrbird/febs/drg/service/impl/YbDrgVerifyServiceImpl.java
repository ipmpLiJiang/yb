package cc.mrbird.febs.drg.service.impl;

import cc.mrbird.febs.com.controller.DataTypeHelpers;
import cc.mrbird.febs.com.entity.ComSms;
import cc.mrbird.febs.com.service.IComSmsService;
import cc.mrbird.febs.common.domain.QueryRequest;
import cc.mrbird.febs.common.properties.FebsProperties;
import cc.mrbird.febs.common.utils.SortUtil;
import cc.mrbird.febs.drg.entity.YbDrgApply;
import cc.mrbird.febs.drg.entity.YbDrgApplyData;
import cc.mrbird.febs.drg.entity.YbDrgManage;
import cc.mrbird.febs.drg.entity.YbDrgVerify;
import cc.mrbird.febs.drg.dao.YbDrgVerifyMapper;
import cc.mrbird.febs.drg.service.IYbDrgApplyDataService;
import cc.mrbird.febs.drg.service.IYbDrgApplyService;
import cc.mrbird.febs.drg.service.IYbDrgManageService;
import cc.mrbird.febs.drg.service.IYbDrgVerifyService;
import cc.mrbird.febs.yb.entity.YbDefaultValue;
import cc.mrbird.febs.yb.entity.YbDept;
import cc.mrbird.febs.yb.entity.YbPerson;
import cc.mrbird.febs.yb.service.IYbDeptService;
import cc.mrbird.febs.yb.service.IYbPersonService;
import cn.hutool.core.lang.Validator;
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
@Service("IYbDrgVerifyService")
@Transactional(propagation = Propagation.SUPPORTS, readOnly = true, rollbackFor = Exception.class)
public class YbDrgVerifyServiceImpl extends ServiceImpl<YbDrgVerifyMapper, YbDrgVerify> implements IYbDrgVerifyService {

    @Autowired
    IYbDeptService iYbDeptService;
    @Autowired
    IYbDrgApplyService iYbDrgApplyService;

    @Autowired
    IYbDrgApplyDataService iYbDrgApplyDataService;

    @Autowired
    FebsProperties febsProperties;

    @Autowired
    IYbPersonService iYbPersonService;

    @Autowired
    IComSmsService iComSmsService;

    @Autowired
    IYbDrgManageService iYbDrgManageService;

    @Override
    public IPage<YbDrgVerify> findYbDrgVerifys(QueryRequest request, YbDrgVerify ybDrgVerify) {
        try {
            LambdaQueryWrapper<YbDrgVerify> queryWrapper = new LambdaQueryWrapper<>();
//            queryWrapper.eq(YbDrgVerify::getIsDeletemark, 1);//1是未删 0是已删


            Page<YbDrgVerify> page = new Page<>();
            SortUtil.handlePageSort(request, page, false);//true 是属性  false是数据库字段可两个
            return this.page(page, queryWrapper);
        } catch (Exception e) {
            log.error("获取字典信息失败", e);
            return null;
        }
    }

    @Override
    public IPage<YbDrgVerify> findYbDrgVerifyList(QueryRequest request, YbDrgVerify ybDrgVerify) {
        try {
            Page<YbDrgVerify> page = new Page<>();
            SortUtil.handlePageSort(request, page, false);//true 是属性  false是数据库字段可两个
            return this.baseMapper.findYbDrgVerify(page, ybDrgVerify);
        } catch (Exception e) {
            log.error("获取失败", e);
            return null;
        }
    }

    @Override
    @Transactional
    public void createYbDrgVerify(YbDrgVerify ybDrgVerify) {
        ybDrgVerify.setId(UUID.randomUUID().toString());
//        ybDrgVerify.setCreateTime(new Date());
//        ybDrgVerify.setIsDeletemark(1);
        this.save(ybDrgVerify);
    }

    @Override
    @Transactional
    public void updateYbDrgVerify(YbDrgVerify ybDrgVerify) {
//        ybDrgVerify.setModifyTime(new Date());
        this.baseMapper.updateYbDrgVerify(ybDrgVerify);
    }

    @Override
    @Transactional
    public void deleteYbDrgVerifys(String[] Ids) {
        List<String> list = Arrays.asList(Ids);
        this.baseMapper.deleteBatchIds(list);
    }

    @Override
    @Transactional
    public void deleteDrgVerifyState(YbDrgVerify delVerify) {
        LambdaQueryWrapper<YbDrgVerify> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(YbDrgVerify::getApplyDateStr, delVerify.getApplyDateStr());
        wrapper.eq(YbDrgVerify::getAreaType, delVerify.getAreaType());
        wrapper.eq(YbDrgVerify::getState, delVerify.getState());
        this.baseMapper.delete(wrapper);
    }

    private boolean isImportTrue(String v1, String v2) {
        v1 = v1 == null ? "" : v1;
        v2 = v2 == null ? "" : v2;
        return v1.equals(v2);
    }

    @Override
    @Transactional
    public void importDrgDataVerifys(YbDrgApply drgApply, List<YbDrgVerify> verifyList) {
        LambdaQueryWrapper<YbDrgVerify> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(YbDrgVerify::getApplyDateStr, drgApply.getApplyDateStr());
        wrapper.eq(YbDrgVerify::getAreaType, drgApply.getAreaType());
        List<YbDrgVerify> list = this.list(wrapper);
        List<YbDept> deptList = iYbDeptService.findDeptList(new YbDept(), 0);

        List<YbDept> createDeptList = new ArrayList<>();
        List<YbDrgVerify> queryList = new ArrayList<>();
        List<YbDrgVerify> createList = new ArrayList<>();
        List<YbDrgVerify> updateList = new ArrayList<>();
        long count = 0;
        boolean isCreate = false;
        boolean isDept = false;
        if (list.size() > 0) {
            for (YbDrgVerify item : verifyList) {
                queryList = list.stream().filter(
                        s -> s.getApplyDataId().equals(item.getApplyDataId())
                ).collect(Collectors.toList());
                if (queryList.size() > 0) {
                    YbDrgVerify rv = queryList.get(0);
                    if (!isImportTrue(item.getVerifyDeptCode(), rv.getVerifyDeptCode()) || !isImportTrue(item.getVerifyDeptName(), rv.getVerifyDeptName()) ||
                            !isImportTrue(item.getVerifyDoctorCode(), rv.getVerifyDoctorCode()) || !isImportTrue(item.getVerifyDoctorName(), rv.getVerifyDoctorName())
                    ) {
                        isDept = true;
                        YbDrgVerify udpate = new YbDrgVerify();
                        udpate.setId(rv.getId());
                        udpate.setVerifyDeptCode(item.getVerifyDeptCode());
                        udpate.setVerifyDeptName(item.getVerifyDeptName());
                        udpate.setVerifyDoctorCode(item.getVerifyDoctorCode());
                        udpate.setVerifyDoctorName(item.getVerifyDoctorName());
                        updateList.add(udpate);
                    }
                } else {
                    isDept = true;
                    createList.add(item);
                }
                if (isDept) {
                    String deptCode = item.getVerifyDeptCode();
                    count = deptList.stream().filter(s -> s.getDeptId().equals(deptCode)).count();
                    if (count == 0) {
                        count = createDeptList.stream().filter(s -> s.getDeptId().equals(deptCode)).count();
                        if (count == 0) {
                            YbDept create = new YbDept();
                            create.setDeptId(item.getVerifyDeptCode());
                            create.setDeptName(item.getVerifyDeptName());
                            create.setCreateTime(new Date());
                            create.setState(1);
                            create.setIsDeletemark(1);
                            createDeptList.add(create);
                        }
                    }
                }
                isDept = false;
            }
        } else {
            this.saveBatch(verifyList);
            isCreate = true;
        }
        if (createList.size() > 0) {
            this.saveBatch(createList);
            isCreate = true;
        }

        if (createDeptList.size() > 0) {
            iYbDeptService.saveBatch(createDeptList);
        }

        if (updateList.size() > 0) {
            this.updateBatchById(updateList);
            isCreate = true;
        }
        int state = drgApply.getState();
        if ((state == YbDefaultValue.APPLYSTATE_2) && isCreate) {
            this.iYbDrgApplyService.updateDrgApplyState3(drgApply);
        }
    }

    @Override
    @Transactional
    public void insertDrgVerifyImports(String applyDateStr, Integer areaType, Long matchPersonId, String matchPersonName) {
        YbDrgApply ybDrgApply = this.iYbDrgApplyService.findDrgApplyByApplyDateStrs(applyDateStr, areaType);
        boolean isCreate = true;
        if (ybDrgApply != null) {
            int state = ybDrgApply.getState();
            if (state == YbDefaultValue.APPLYSTATE_2) {
                List<YbDrgApplyData> radList = iYbDrgApplyDataService.findDrgApplyDataByNotVerifys(ybDrgApply.getId(), ybDrgApply.getApplyDateStr(), areaType);
                if (radList.size() > 0) {
                    List<YbDrgVerify> createList = new ArrayList<>();

                    for (YbDrgApplyData item : radList) {
                        YbDrgVerify ybDrgVerify = new YbDrgVerify();
                        ybDrgVerify.setId(UUID.randomUUID().toString());
                        ybDrgVerify.setApplyDataId(item.getId());
                        ybDrgVerify.setApplyDateStr(ybDrgApply.getApplyDateStr());
                        ybDrgVerify.setOrderNumber(item.getOrderNumber());
                        ybDrgVerify.setOrderNum(item.getOrderNum());
                        ybDrgVerify.setState(YbDefaultValue.VERIFYSTATE_1);
                        ybDrgVerify.setAreaType(areaType);

                        createList.add(ybDrgVerify);
                    }

                    isCreate = true;//判断状态是否更新
                    if (createList.size() > 0) {
                        this.saveBatch(createList);
                    }
                }
            }
            if ((state == YbDefaultValue.APPLYSTATE_2) && isCreate) {
                this.iYbDrgApplyService.updateDrgApplyState3(ybDrgApply);
            }
        }
    }

    @Override
    @Transactional
    public void updateDrgVerifyImports(List<YbDrgVerify> list) {
        for (YbDrgVerify item : list) {
            if (item.getVerifyDeptCode() != null && !item.getVerifyDeptCode().equals("") &&
                    item.getVerifyDeptName() != null && !item.getVerifyDeptName().equals("") &&
                    item.getVerifyDoctorCode() != null && !item.getVerifyDoctorCode().equals("") &&
                    item.getVerifyDoctorName() != null && !item.getVerifyDoctorName().equals("")) {
                item.setState(YbDefaultValue.VERIFYSTATE_1);

                String strVerifyDeptName = DataTypeHelpers.stringReplaceSetString(item.getVerifyDeptName(), item.getVerifyDeptCode() + "-");
                item.setVerifyDeptName(strVerifyDeptName);
                String strVerifyDoctorName = DataTypeHelpers.stringReplaceSetString(item.getVerifyDoctorName(), item.getVerifyDoctorCode() + "-");
                item.setVerifyDoctorName(strVerifyDoctorName);

                if (item.getId() == null || item.getId().equals("")) {
                    item.setId(UUID.randomUUID().toString());
                    this.baseMapper.insert(item);
                } else {
                    LambdaQueryWrapper<YbDrgVerify> queryWrapper = new LambdaQueryWrapper<>();
                    queryWrapper.eq(YbDrgVerify::getId, item.getId());
                    queryWrapper.eq(YbDrgVerify::getState, YbDefaultValue.VERIFYSTATE_1);
                    YbDrgVerify updateVerify = new YbDrgVerify();
                    updateVerify.setVerifyDoctorCode(item.getVerifyDoctorCode());
                    updateVerify.setVerifyDoctorName(item.getVerifyDoctorName());
                    updateVerify.setVerifyDeptCode(item.getVerifyDeptCode());
                    updateVerify.setVerifyDeptName(item.getVerifyDeptName());
                    this.baseMapper.update(updateVerify, queryWrapper);
                }
            }

        }
    }

    private List<YbPerson> findPerson(List<YbDrgVerify> list) {
        List<YbPerson> personList = new ArrayList<>();
        ArrayList<String> personCodeList = new ArrayList<>();
        for (YbDrgVerify item : list) {
            if (item.getVerifyDoctorCode() != null && !item.getVerifyDoctorCode().equals("")) {
                if (personCodeList.stream().filter(s -> s.equals(item.getVerifyDoctorCode())).count() == 0) {
                    personCodeList.add(item.getVerifyDoctorCode());
                }
            }
        }
        if (personCodeList.size() > 0) {
            personList = this.iYbPersonService.findPersonList(personCodeList);
        }
        return personList;
    }

    @Override
    @Transactional
    public void updateSendStates(List<YbDrgVerify> list, Integer areaType, Long uId, String Uname) {
        String applyDateStr = list.get(0).getApplyDateStr();
        YbDrgApply drgApply = iYbDrgApplyService.findDrgApplyByApplyDateStrs(applyDateStr, areaType);
        if (drgApply != null) {
            Date thisDate = new java.sql.Timestamp(new Date().getTime());

            Date addDate = DataTypeHelpers.addDateMethod(drgApply.getEnableDate(), 1);
            List<YbPerson> personList = this.findPerson(list);
            List<YbPerson> queryPersonList = new ArrayList<>();
            List<ComSms> smsList = new ArrayList<>();
            List<String> userCodeList = new ArrayList<>();
            int nOpenSms = febsProperties.getOpenSms();
            boolean isOpenSms = nOpenSms == 1 ? true : false;

            String sendContent = "";
            if (isOpenSms && list.size() > 0) {
                ComSms qu = new ComSms();
                qu.setApplyDateStr(applyDateStr);
                qu.setAreaType(areaType);
                qu.setSendType(ComSms.SENDTYPE_10);
                qu.setState(ComSms.STATE_0);
                smsList = iComSmsService.findLmdSmsList(qu);
                sendContent = this.iYbDrgApplyService.getSendMessage(applyDateStr, addDate, areaType, false);

            }
            for (YbDrgVerify ybDrgVerify : list) {
                if (ybDrgVerify.getVerifyDeptCode() != null && !ybDrgVerify.getVerifyDeptCode().equals("") &&
                        ybDrgVerify.getVerifyDeptName() != null && !ybDrgVerify.getVerifyDeptName().equals("") &&
                        ybDrgVerify.getVerifyDoctorCode() != null && !ybDrgVerify.getVerifyDoctorCode().equals("") &&
                        ybDrgVerify.getVerifyDoctorName() != null && !ybDrgVerify.getVerifyDoctorName().equals("")) {
                    queryPersonList = personList.stream().filter(
                            s -> s.getPersonCode().equals(ybDrgVerify.getVerifyDoctorCode())
                    ).collect(Collectors.toList());
                    if (queryPersonList.size() > 0) {
                        String strVerifyDeptName = DataTypeHelpers.stringReplaceSetString(ybDrgVerify.getVerifyDeptName(), ybDrgVerify.getVerifyDeptCode() + "-");
                        ybDrgVerify.setVerifyDeptName(strVerifyDeptName);
                        String strVerifyDoctorName = DataTypeHelpers.stringReplaceSetString(ybDrgVerify.getVerifyDoctorName(), ybDrgVerify.getVerifyDoctorCode() + "-");
                        ybDrgVerify.setVerifyDoctorName(strVerifyDoctorName);

                        //插入申诉管理
                        YbDrgManage ybDrgManage = new YbDrgManage();
                        ybDrgManage.setVerifyId(ybDrgVerify.getId());
                        ybDrgManage.setApplyDataId(ybDrgVerify.getApplyDataId());
                        ybDrgManage.setReadyDeptCode(ybDrgVerify.getVerifyDeptCode());
                        ybDrgManage.setReadyDeptName(ybDrgVerify.getVerifyDeptName());
                        ybDrgManage.setReadyDoctorCode(ybDrgVerify.getVerifyDoctorCode());
                        ybDrgManage.setReadyDoctorName(ybDrgVerify.getVerifyDoctorName());

                        ybDrgManage.setApplyDateStr(ybDrgVerify.getApplyDateStr());
                        ybDrgManage.setOrderNumber(ybDrgVerify.getOrderNumber());
                        ybDrgManage.setOrderNum(ybDrgVerify.getOrderNum());

                        ybDrgManage.setOperateDate(thisDate);
                        ybDrgManage.setOperateProcess("发送操作-接受申请");
                        ybDrgManage.setState(YbDefaultValue.AMSTATE_0);
                        ybDrgManage.setEnableDate(addDate);
                        ybDrgManage.setAreaType(areaType);

                        iYbDrgManageService.createYbDrgManage(ybDrgManage);
                        LambdaQueryWrapper<YbDrgVerify> queryWrapper = new LambdaQueryWrapper<>();
                        queryWrapper.eq(YbDrgVerify::getId, ybDrgVerify.getId());
                        queryWrapper.eq(YbDrgVerify::getState, YbDefaultValue.VERIFYSTATE_2);

                        if (isOpenSms) {
                            if (userCodeList.stream().filter(s -> s.equals(ybDrgVerify.getVerifyDoctorCode())).count() == 0) {
                                if (smsList.stream().filter(s -> s.getSendcode().equals(ybDrgVerify.getVerifyDoctorCode())).count() == 0) {
                                    userCodeList.add(ybDrgVerify.getVerifyDoctorCode());
                                    if (Validator.isMobile(queryPersonList.get(0).getTel())) {
                                        ComSms comSms = new ComSms();
                                        comSms.setId(UUID.randomUUID().toString());
                                        comSms.setSendcode(queryPersonList.get(0).getPersonCode());
                                        comSms.setSendname(queryPersonList.get(0).getPersonName());
                                        comSms.setMobile(queryPersonList.get(0).getTel());
                                        comSms.setSendType(ComSms.SENDTYPE_10);
                                        comSms.setState(ComSms.STATE_0);

                                        comSms.setSendcontent(sendContent);
                                        comSms.setOperatorId(uId);
                                        comSms.setOperatorName(Uname);
                                        comSms.setIsDeletemark(1);
                                        comSms.setCreateUserId(uId);
                                        comSms.setCreateTime(thisDate);
                                        comSms.setAreaType(areaType);
                                        comSms.setApplyDateStr(drgApply.getApplyDateStr());
                                        iComSmsService.save(comSms);
                                    }
                                }
                            }
                        }
                        YbDrgVerify updateVerify = new YbDrgVerify();
                        updateVerify.setSendPersonId(uId);
                        updateVerify.setSendPersonName(Uname);
                        updateVerify.setSendDate(thisDate);
                        updateVerify.setState(YbDefaultValue.VERIFYSTATE_3);
                        this.baseMapper.update(updateVerify, queryWrapper);
                    }
                }
            }
        }
    }

    @Override
    @Transactional
    public void updateAllSendStates(String applyDateStr, Integer areaType, Integer state, Long uId, String Uname) {
        Date thisDate = new java.sql.Timestamp(new Date().getTime());
        YbDrgApply drgApply = iYbDrgApplyService.findDrgApplyByApplyDateStrs(applyDateStr, areaType);

        if (drgApply != null) {
            Date addDate = DataTypeHelpers.addDateMethod(drgApply.getEnableDate(), 1);
            List<YbPerson> personList = iYbPersonService.findPersonList(new YbPerson(), 0);
            List<YbPerson> queryPersonList = new ArrayList<>();
            List<ComSms> smsList = new ArrayList<>();
            List<String> userCodeList = new ArrayList<>();
            int nOpenSms = febsProperties.getOpenSms();
            List<YbDrgVerify> list = this.baseMapper.findDrgVerifyList(applyDateStr, areaType, state);

            String sendContent = "";
            boolean isOpenSms = nOpenSms == 1 ? true : false;
            if (isOpenSms && list.size() > 0) {
                ComSms qu = new ComSms();
                qu.setApplyDateStr(applyDateStr);
                qu.setAreaType(areaType);
                qu.setSendType(ComSms.SENDTYPE_10);
                qu.setState(ComSms.STATE_0);
                smsList = iComSmsService.findLmdSmsList(qu);
                sendContent = this.iYbDrgApplyService.getSendMessage(applyDateStr, addDate, areaType, false);
            }
            for (YbDrgVerify ybDrgVerify : list) {
                if (ybDrgVerify.getState() != YbDefaultValue.VERIFYSTATE_3) {
                    queryPersonList = personList.stream().filter(
                            s -> s.getPersonCode().equals(ybDrgVerify.getVerifyDoctorCode())
                    ).collect(Collectors.toList());

                    if (queryPersonList.size() > 0) {
                        String strVerifyDeptName = DataTypeHelpers.stringReplaceSetString(ybDrgVerify.getVerifyDeptName(), ybDrgVerify.getVerifyDeptCode() + "-");
                        ybDrgVerify.setVerifyDeptName(strVerifyDeptName);
                        String strVerifyDoctorName = DataTypeHelpers.stringReplaceSetString(ybDrgVerify.getVerifyDoctorName(), ybDrgVerify.getVerifyDoctorCode() + "-");
                        ybDrgVerify.setVerifyDoctorName(strVerifyDoctorName);

                        //插入申诉管理
                        YbDrgManage ybDrgManage = new YbDrgManage();
                        ybDrgManage.setVerifyId(ybDrgVerify.getId());
                        ybDrgManage.setApplyDataId(ybDrgVerify.getApplyDataId());
                        ybDrgManage.setReadyDeptCode(ybDrgVerify.getVerifyDeptCode());
                        ybDrgManage.setReadyDeptName(ybDrgVerify.getVerifyDeptName());
                        ybDrgManage.setReadyDoctorCode(ybDrgVerify.getVerifyDoctorCode());
                        ybDrgManage.setReadyDoctorName(ybDrgVerify.getVerifyDoctorName());

                        ybDrgManage.setApplyDateStr(ybDrgVerify.getApplyDateStr());
                        ybDrgManage.setOrderNumber(ybDrgVerify.getOrderNumber());
                        ybDrgManage.setOrderNum(ybDrgVerify.getOrderNum());

                        ybDrgManage.setOperateDate(thisDate);
                        ybDrgManage.setOperateProcess("发送操作-接受申请");
                        ybDrgManage.setState(YbDefaultValue.AMSTATE_0);
                        ybDrgManage.setEnableDate(addDate);
                        ybDrgManage.setAreaType(areaType);

                        iYbDrgManageService.createYbDrgManage(ybDrgManage);

                        LambdaQueryWrapper<YbDrgVerify> queryWrapper = new LambdaQueryWrapper<>();
                        queryWrapper.eq(YbDrgVerify::getId, ybDrgVerify.getId());
                        queryWrapper.eq(YbDrgVerify::getState, YbDefaultValue.VERIFYSTATE_2);
                        if (isOpenSms) {
                            if (userCodeList.stream().filter(s -> s.equals(ybDrgVerify.getVerifyDoctorCode())).count() == 0) {
                                if (smsList.stream().filter(s -> s.getSendcode().equals(ybDrgVerify.getVerifyDoctorCode())).count() == 0) {
                                    userCodeList.add(ybDrgVerify.getVerifyDoctorCode());
                                    if (queryPersonList.get(0).getTel() != null && !queryPersonList.get(0).getTel().equals("")) {
                                        if (Validator.isMobile(queryPersonList.get(0).getTel())) {
                                            ComSms comSms = new ComSms();
                                            comSms.setId(UUID.randomUUID().toString());
                                            comSms.setSendcode(queryPersonList.get(0).getPersonCode());
                                            comSms.setSendname(queryPersonList.get(0).getPersonName());
                                            comSms.setMobile(queryPersonList.get(0).getTel());
                                            comSms.setSendType(ComSms.SENDTYPE_10);
                                            comSms.setState(ComSms.STATE_0);
                                            comSms.setSendcontent(sendContent);
                                            comSms.setOperatorId(uId);
                                            comSms.setOperatorName(Uname);
                                            comSms.setIsDeletemark(1);
                                            comSms.setCreateUserId(uId);
                                            comSms.setCreateTime(thisDate);
                                            comSms.setAreaType(areaType);
                                            comSms.setApplyDateStr(drgApply.getApplyDateStr());
                                            iComSmsService.save(comSms);
                                        }
                                    }
                                }
                            }
                        }
                        YbDrgVerify updateVerify = new YbDrgVerify();
                        updateVerify.setSendPersonId(uId);
                        updateVerify.setSendPersonName(Uname);
                        updateVerify.setSendDate(thisDate);
                        updateVerify.setState(YbDefaultValue.VERIFYSTATE_3);
                        this.baseMapper.update(updateVerify, queryWrapper);
                    }
                }
            }
        }
    }


    //单个，多个核对
    @Override
    @Transactional
    public void updateReviewerStates(List<YbDrgVerify> list) {
        List<YbPerson> personList = this.findPerson(list);
        for (YbDrgVerify item : list) {
            if (item.getVerifyDeptCode() != null && !item.getVerifyDeptCode().equals("") &&
                    item.getVerifyDeptName() != null && !item.getVerifyDeptName().equals("") &&
                    item.getVerifyDoctorCode() != null && !item.getVerifyDoctorCode().equals("") &&
                    item.getVerifyDoctorName() != null && !item.getVerifyDoctorName().equals("")) {
                if (personList.stream().filter(s -> s.getPersonCode().equals(item.getVerifyDoctorCode())).count() > 0) {
                    String strVerifyDeptName = DataTypeHelpers.stringReplaceSetString(item.getVerifyDeptName(), item.getVerifyDeptCode() + "-");
                    item.setVerifyDeptName(strVerifyDeptName);
                    String strVerifyDoctorName = DataTypeHelpers.stringReplaceSetString(item.getVerifyDoctorName(), item.getVerifyDoctorCode() + "-");
                    item.setVerifyDoctorName(strVerifyDoctorName);

                    item.setState(YbDefaultValue.VERIFYSTATE_2);
                    item.setAreaType(item.getAreaType());

                    if (item.getId() == null || item.getId().equals("")) {
                        item.setId(UUID.randomUUID().toString());
                        this.baseMapper.insert(item);
                    } else {
                        YbDrgVerify updateVerify = new YbDrgVerify();
                        updateVerify.setVerifyDoctorCode(item.getVerifyDoctorCode());
                        updateVerify.setVerifyDoctorName(strVerifyDoctorName);
                        updateVerify.setVerifyDeptCode(item.getVerifyDeptCode());
                        updateVerify.setVerifyDeptName(strVerifyDeptName);
                        updateVerify.setState(YbDefaultValue.VERIFYSTATE_2);
                        LambdaQueryWrapper<YbDrgVerify> queryWrapper = new LambdaQueryWrapper<>();
                        queryWrapper.eq(YbDrgVerify::getId, item.getId());
                        queryWrapper.eq(YbDrgVerify::getState, YbDefaultValue.VERIFYSTATE_1);
                        this.baseMapper.update(updateVerify, queryWrapper);
                    }
                }
            }
        }
    }


    //全部核对
    @Override
    @Transactional
    public void updateAllReviewerStates(String applyDateStr, Integer areaType, int state) {
        YbDrgApply drgApply = iYbDrgApplyService.findDrgApplyByApplyDateStrs(applyDateStr, areaType);
        if (drgApply != null) {
            List<YbDrgVerify> list = this.baseMapper.findDrgVerifyList(applyDateStr, areaType, state);
            List<YbDrgVerify> updateList = new ArrayList<>();
            List<YbPerson> personList = this.iYbPersonService.findPersonList(new YbPerson(), 0);
            for (YbDrgVerify item : list) {
                if (item.getVerifyDeptCode() != null && !item.getVerifyDeptCode().equals("") &&
                        item.getVerifyDeptName() != null && !item.getVerifyDeptName().equals("") &&
                        item.getVerifyDoctorCode() != null && !item.getVerifyDoctorCode().equals("") &&
                        item.getVerifyDoctorName() != null && !item.getVerifyDoctorName().equals("")) {
                    if (personList.stream().filter(s -> s.getPersonCode().equals(item.getVerifyDoctorCode())).count() > 0) {
                        YbDrgVerify update = new YbDrgVerify();
                        update.setId(item.getId());
                        update.setState(YbDefaultValue.VERIFYSTATE_2);
                        updateList.add(update);
                    }
                }
            }
            if (updateList.size() > 0) {
                this.updateBatchById(updateList);
            }
        }
    }

    //全部返回
    @Override
    @Transactional
    public void updateBackStates(String applyDateStr, Integer areaType, int state) {
        YbDrgApply drgApply = iYbDrgApplyService.findDrgApplyByApplyDateStrs(applyDateStr, areaType);
        if (drgApply != null) {
            List<YbDrgVerify> list = this.baseMapper.findDrgVerifyList(applyDateStr, areaType, state);
            List<YbDrgVerify> updateList = new ArrayList<>();
            for (YbDrgVerify item : list) {
                YbDrgVerify update = new YbDrgVerify();
                update.setId(item.getId());
                update.setState(YbDefaultValue.VERIFYSTATE_1);
                updateList.add(update);
            }
            if (updateList.size() > 0) {
                this.updateBatchById(updateList);
            }

        }
    }


}