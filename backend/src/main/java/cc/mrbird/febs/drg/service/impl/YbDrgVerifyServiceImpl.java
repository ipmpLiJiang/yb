package cc.mrbird.febs.drg.service.impl;

import cc.mrbird.febs.com.controller.DataTypeHelpers;
import cc.mrbird.febs.com.entity.ComSms;
import cc.mrbird.febs.com.entity.ComType;
import cc.mrbird.febs.com.service.IComConfiguremanageService;
import cc.mrbird.febs.com.service.IComSmsService;
import cc.mrbird.febs.com.service.IComTypeService;
import cc.mrbird.febs.common.domain.QueryRequest;
import cc.mrbird.febs.common.properties.FebsProperties;
import cc.mrbird.febs.common.utils.SortUtil;
import cc.mrbird.febs.drg.entity.*;
import cc.mrbird.febs.drg.dao.YbDrgVerifyMapper;
import cc.mrbird.febs.drg.service.*;
import cc.mrbird.febs.job.domain.Job;
import cc.mrbird.febs.job.service.JobService;
import cc.mrbird.febs.yb.entity.YbDefaultValue;
import cc.mrbird.febs.yb.entity.YbPerson;
import cc.mrbird.febs.yb.entity.YbReconsiderPriorityLevel;
import cc.mrbird.febs.yb.service.IYbPersonService;
import cc.mrbird.febs.yb.service.IYbReconsiderPriorityLevelService;
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

    @Autowired
    IComConfiguremanageService iComConfiguremanageService;

    @Autowired
    JobService jobService;

    @Autowired
    IYbDrgJkService iYbDrgJkService;

    @Autowired
    IYbDrgDksService iYbDrgDksService;

    @Autowired
    IYbReconsiderPriorityLevelService iYbReconsiderPriorityLevelService;

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

//        List<YbDrgDks> dksList = iYbDrgDksService.findDksList(new YbDrgDks(), 0);
//        List<YbDrgDks> createDrgList = new ArrayList<>();
        List<YbDrgVerify> queryList = new ArrayList<>();
        List<YbDrgVerify> createList = new ArrayList<>();
        List<YbDrgVerify> updateList = new ArrayList<>();
//        long count = 0;
        boolean isCreate = false;
//        boolean isDept = false;
        if (list.size() > 0) {
            for (YbDrgVerify item : verifyList) {
                queryList = list.stream().filter(
                        s -> s.getApplyDataId().equals(item.getApplyDataId())
                ).collect(Collectors.toList());
                if (queryList.size() > 0) {
                    YbDrgVerify rv = queryList.get(0);

                    if (!isImportTrue(item.getVerifyDksId(), rv.getVerifyDksId()) ||
                            !isImportTrue(item.getVerifyDksName(), rv.getVerifyDksName()) ||
                            !isImportTrue(item.getVerifyDoctorCode(), rv.getVerifyDoctorCode()) ||
                            !isImportTrue(item.getVerifyDoctorName(), rv.getVerifyDoctorName())
                    ) {
//                        isDept = true;
                        YbDrgVerify udpate = new YbDrgVerify();
                        udpate.setId(rv.getId());
                        udpate.setVerifyDksId(item.getVerifyDksId());
                        udpate.setVerifyDksName(item.getVerifyDksName());
                        udpate.setVerifyDoctorCode(item.getVerifyDoctorCode());
                        udpate.setVerifyDoctorName(item.getVerifyDoctorName());
                        updateList.add(udpate);
                    }
                } else {
//                    isDept = true;
                    createList.add(item);
                }
//                if (isDept) {
//                    String dksId = item.getVerifyDksId();
//                    count = dksList.stream().filter(s -> s.getDksId().equals(dksId)).count();
//                    if (count == 0) {
//                        count = createDrgList.stream().filter(s -> s.getDksId().equals(dksId)).count();
//                        if (count == 0) {
//                        }
//                    }
//                }
//                isDept = false;
            }
        } else {
            this.saveBatch(verifyList);
            isCreate = true;
        }
        if (createList.size() > 0) {
            this.saveBatch(createList);
            isCreate = true;
        }

        if (updateList.size() > 0) {
            this.updateBatchById(updateList);
            isCreate = true;
        }
        int state = drgApply.getState();
        if ((state == YbDefaultValue.DRGAPPLYSTATE_2) && isCreate) {
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
            if (state == YbDefaultValue.DRGAPPLYSTATE_2 || state == YbDefaultValue.DRGAPPLYSTATE_3) {
                List<YbDrgApplyData> radList = iYbDrgApplyDataService.findDrgApplyDataByNotVerifys(ybDrgApply.getId(), ybDrgApply.getApplyDateStr(), areaType);

                LambdaQueryWrapper<YbDrgJk> wrapper = new LambdaQueryWrapper<>();
                wrapper.eq(YbDrgJk::getApplyDateStr, applyDateStr);
                wrapper.eq(YbDrgJk::getAreaType, areaType);
                List<YbDrgJk> jkList = iYbDrgJkService.list(wrapper);
                List<YbDrgJk> queryJkList = new ArrayList<>();
                List<Integer> stateList = new ArrayList<>();
                stateList.add(YbReconsiderPriorityLevel.PL_STATE_4);
                List<YbReconsiderPriorityLevel> rplList = iYbReconsiderPriorityLevelService.findReconsiderPriorityLevelList(areaType, stateList);
                List<YbReconsiderPriorityLevel> rplQueryList = new ArrayList<>();
                List<YbDrgDks> dksList = iYbDrgDksService.findDksList(new YbDrgDks(), 0);
                List<YbDrgDks> dksQueryList = new ArrayList<>();

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

                        queryJkList = jkList.stream().filter(s -> s.getApplyDataId().equals(item.getId())).collect(Collectors.toList());
                        if (queryJkList.size() > 0 && this.isNotNull(queryJkList.get(0))) {
                            YbDrgJk jk = queryJkList.get(0);
                            rplQueryList = rplList.stream().filter(s -> s.getDoctorCode().equals(jk.getYlzDocId())).collect(Collectors.toList());
                            if (rplQueryList.size() == 0) {
                                ybDrgVerify.setVerifyDoctorCode(jk.getYlzDocId());
                                ybDrgVerify.setVerifyDoctorName(jk.getYlzDocName());
                                dksQueryList = dksList.stream().filter(s -> s.getDksId().equals(jk.getDeptId())).collect(Collectors.toList());
                                if (dksQueryList.size() > 0 && StringUtils.isNotBlank(dksQueryList.get(0).getDksId()) && StringUtils.isNotBlank(dksQueryList.get(0).getDksName())) {
                                    ybDrgVerify.setVerifyDksName(dksQueryList.get(0).getDksName() + "(" + dksQueryList.get(0).getAreaName() + ")");
                                    ybDrgVerify.setVerifyDksId(dksQueryList.get(0).getDksId());
                                }
                            } else {
                                YbReconsiderPriorityLevel rpl = rplQueryList.get(0);
                                if (this.isNotNullRpl(rpl)) {
                                    ybDrgVerify.setVerifyDoctorCode(rpl.getDoctorCodeTo());
                                    ybDrgVerify.setVerifyDoctorName(rpl.getDoctorNameTo());
                                    if(rpl.getDeptType() == 1) {
                                        ybDrgVerify.setVerifyDksId(jk.getDeptId());
                                        ybDrgVerify.setVerifyDksName(jk.getDeptName());
                                    } else {
                                        ybDrgVerify.setVerifyDksId(rpl.getDksIdTo());
                                        ybDrgVerify.setVerifyDksName(rpl.getDksNameTo());
                                    }
                                }
                            }
                        }
                        createList.add(ybDrgVerify);
                    }

                    isCreate = true;//判断状态是否更新
                    if (createList.size() > 0) {
                        this.saveBatch(createList);
                    }
                } else {
                    List<YbDrgVerify> updateList = new ArrayList<>();
                    List<YbDrgVerify> verifyList = this.baseMapper.findDrgVerifyList(applyDateStr, areaType, YbDefaultValue.VERIFYSTATE_1, true);
                    for (YbDrgVerify item : verifyList) {
                        YbDrgVerify update = new YbDrgVerify();
                        update.setId(item.getId());
                        queryJkList = jkList.stream().filter(s -> s.getApplyDataId().equals(item.getApplyDataId())).collect(Collectors.toList());
                        if (queryJkList.size() > 0 && this.isNotNull(queryJkList.get(0))) {
                            YbDrgJk jk = queryJkList.get(0);
                            rplQueryList = rplList.stream().filter(s -> s.getDoctorCode().equals(jk.getYlzDocId())).collect(Collectors.toList());
                            if (rplQueryList.size() == 0) {
                                dksQueryList = dksList.stream().filter(s -> s.getDksId().equals(jk.getDeptId())).collect(Collectors.toList());
                                if (dksQueryList.size() > 0 && StringUtils.isNotBlank(dksQueryList.get(0).getDksId()) && StringUtils.isNotBlank(dksQueryList.get(0).getDksName())) {
                                    update.setVerifyDksName(dksQueryList.get(0).getDksName() + "(" + dksQueryList.get(0).getAreaName() + ")");
                                    update.setVerifyDksId(dksQueryList.get(0).getDksId());
                                }
                            } else {
                                YbReconsiderPriorityLevel rpl = rplQueryList.get(0);
                                if (this.isNotNullRpl(rpl)) {
                                    if(rpl.getDeptType() == 1) {
                                        update.setVerifyDksId(jk.getDeptId());
                                        update.setVerifyDksName(jk.getDeptName());
                                    } else {
                                        update.setVerifyDksId(rpl.getDksIdTo());
                                        update.setVerifyDksName(rpl.getDksNameTo());
                                    }
                                    update.setVerifyDoctorCode(rpl.getDoctorCodeTo());
                                    update.setVerifyDoctorName(rpl.getDoctorNameTo());
                                }
                            }
                        }

                        if (StringUtils.isNotBlank(update.getVerifyDksId()) &&
                                StringUtils.isNotBlank(update.getVerifyDksName())) {
                            updateList.add(update);
                        }
                    }
                    if (updateList.size() > 0) {
                        this.updateBatchById(updateList);
                    }
                }
            }
            if ((state == YbDefaultValue.DRGAPPLYSTATE_2) && isCreate) {
                this.iYbDrgApplyService.updateDrgApplyState3(ybDrgApply);
            }
        }
    }

    private boolean isNotNull(YbDrgJk jk) {
        if (StringUtils.isNotBlank(jk.getYlzDocId()) &&
                StringUtils.isNotBlank(jk.getYlzDocName())) {
            return true;
        }
        return false;
    }

    private boolean isNotNullRpl(YbReconsiderPriorityLevel rpl) {
        if (rpl.getDeptType() == 2 && StringUtils.isNotBlank(rpl.getDoctorCodeTo()) &&
                StringUtils.isNotBlank(rpl.getDoctorNameTo()) &&
                StringUtils.isNotBlank(rpl.getDksIdTo()) &&
                StringUtils.isNotBlank(rpl.getDksNameTo())) {
            return true;
        }
        if (rpl.getDeptType() == 1 && StringUtils.isNotBlank(rpl.getDoctorCodeTo()) &&
                StringUtils.isNotBlank(rpl.getDoctorNameTo())) {
            return true;
        }
        return false;
    }

    @Override
    @Transactional
    public void updateDrgVerifyImports(List<YbDrgVerify> list) {
        for (YbDrgVerify item : list) {
            if (StringUtils.isNotBlank(item.getVerifyDksId()) &&
                    StringUtils.isNotBlank(item.getVerifyDksName()) &&
                    StringUtils.isNotBlank(item.getVerifyDoctorCode()) &&
                    StringUtils.isNotBlank(item.getVerifyDoctorName())) {

                item.setState(YbDefaultValue.VERIFYSTATE_1);
                String strVerifyDksName = DataTypeHelpers.stringReplaceSetString(item.getVerifyDksName(), item.getVerifyDksId() + "-");
                item.setVerifyDksName(strVerifyDksName);
                String strVerifyDoctorName = DataTypeHelpers.stringReplaceSetString(item.getVerifyDoctorName(), item.getVerifyDoctorCode() + "-");
                item.setVerifyDoctorName(strVerifyDoctorName);

                if (StringUtils.isBlank(item.getId())) {
                    item.setId(UUID.randomUUID().toString());
                    this.baseMapper.insert(item);
                } else {
                    LambdaQueryWrapper<YbDrgVerify> queryWrapper = new LambdaQueryWrapper<>();
                    queryWrapper.eq(YbDrgVerify::getId, item.getId());
                    queryWrapper.eq(YbDrgVerify::getState, YbDefaultValue.VERIFYSTATE_1);
                    YbDrgVerify updateVerify = new YbDrgVerify();
                    updateVerify.setVerifyDoctorCode(item.getVerifyDoctorCode());
                    updateVerify.setVerifyDoctorName(item.getVerifyDoctorName());
                    updateVerify.setVerifyDksId(item.getVerifyDksId());
                    updateVerify.setVerifyDksName(item.getVerifyDksName());
                    this.baseMapper.update(updateVerify, queryWrapper);
                }
            }

        }
    }

    private List<YbPerson> findPerson(List<YbDrgVerify> list) {
        List<YbPerson> personList = new ArrayList<>();
        ArrayList<String> personCodeList = new ArrayList<>();
        for (YbDrgVerify item : list) {
            if (StringUtils.isNotBlank(item.getVerifyDoctorCode())) {
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
                if (StringUtils.isNotBlank(ybDrgVerify.getVerifyDksId()) &&
                        StringUtils.isNotBlank(ybDrgVerify.getVerifyDksName()) &&
                        StringUtils.isNotBlank(ybDrgVerify.getVerifyDoctorCode()) &&
                        StringUtils.isNotBlank(ybDrgVerify.getVerifyDoctorName())) {
                    queryPersonList = personList.stream().filter(
                            s -> s.getPersonCode().equals(ybDrgVerify.getVerifyDoctorCode())
                    ).collect(Collectors.toList());
                    if (queryPersonList.size() > 0) {
                        String strVerifyDksName = DataTypeHelpers.stringReplaceSetString(ybDrgVerify.getVerifyDksName(), ybDrgVerify.getVerifyDksId() + "-");
                        ybDrgVerify.setVerifyDksName(strVerifyDksName);
                        String strVerifyDoctorName = DataTypeHelpers.stringReplaceSetString(ybDrgVerify.getVerifyDoctorName(), ybDrgVerify.getVerifyDoctorCode() + "-");
                        ybDrgVerify.setVerifyDoctorName(strVerifyDoctorName);

                        //插入申诉管理
                        YbDrgManage ybDrgManage = new YbDrgManage();
                        ybDrgManage.setVerifyId(ybDrgVerify.getId());
                        ybDrgManage.setApplyDataId(ybDrgVerify.getApplyDataId());
                        ybDrgManage.setReadyDksId(ybDrgVerify.getVerifyDksId());
                        ybDrgManage.setReadyDksName(ybDrgVerify.getVerifyDksName());
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
            List<YbPerson> personList = new ArrayList<>();
            List<YbPerson> queryPersonList = new ArrayList<>();
            List<ComSms> smsList = new ArrayList<>();
            List<String> userCodeList = new ArrayList<>();
            int nOpenSms = febsProperties.getOpenSms();
            List<YbDrgVerify> list = this.baseMapper.findDrgVerifyList(applyDateStr, areaType, state, false);
            if(list.size() > 100) {
                personList = iYbPersonService.findPersonList(new YbPerson(), 0);
            } else {
                personList = this.findPerson(list);
            }
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
                        String strVerifyDksName = DataTypeHelpers.stringReplaceSetString(ybDrgVerify.getVerifyDksName(), ybDrgVerify.getVerifyDksId() + "-");
                        ybDrgVerify.setVerifyDksName(strVerifyDksName);
                        String strVerifyDoctorName = DataTypeHelpers.stringReplaceSetString(ybDrgVerify.getVerifyDoctorName(), ybDrgVerify.getVerifyDoctorCode() + "-");
                        ybDrgVerify.setVerifyDoctorName(strVerifyDoctorName);

                        //插入申诉管理
                        YbDrgManage ybDrgManage = new YbDrgManage();
                        ybDrgManage.setVerifyId(ybDrgVerify.getId());
                        ybDrgManage.setApplyDataId(ybDrgVerify.getApplyDataId());
                        ybDrgManage.setReadyDksId(ybDrgVerify.getVerifyDksId());
                        ybDrgManage.setReadyDksName(ybDrgVerify.getVerifyDksName());
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
                                    if (StringUtils.isNotBlank(queryPersonList.get(0).getTel())) {
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
            if (StringUtils.isNotBlank(item.getVerifyDksId()) &&
                    StringUtils.isNotBlank(item.getVerifyDksName()) &&
                    StringUtils.isNotBlank(item.getVerifyDoctorCode()) &&
                    StringUtils.isNotBlank(item.getVerifyDoctorName())) {
                if (personList.stream().filter(s -> s.getPersonCode().equals(item.getVerifyDoctorCode())).count() > 0) {
                    String strVerifyDksName = DataTypeHelpers.stringReplaceSetString(item.getVerifyDksName(), item.getVerifyDksId() + "-");
                    item.setVerifyDksName(strVerifyDksName);
                    String strVerifyDoctorName = DataTypeHelpers.stringReplaceSetString(item.getVerifyDoctorName(), item.getVerifyDoctorCode() + "-");
                    item.setVerifyDoctorName(strVerifyDoctorName);

                    item.setState(YbDefaultValue.VERIFYSTATE_2);
                    item.setAreaType(item.getAreaType());

                    if (StringUtils.isBlank(item.getId())) {
                        item.setId(UUID.randomUUID().toString());
                        this.baseMapper.insert(item);
                    } else {
                        YbDrgVerify updateVerify = new YbDrgVerify();
                        updateVerify.setVerifyDoctorCode(item.getVerifyDoctorCode());
                        updateVerify.setVerifyDoctorName(strVerifyDoctorName);
                        updateVerify.setVerifyDksId(item.getVerifyDksId());
                        updateVerify.setVerifyDksName(item.getVerifyDksName());
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
            List<YbDrgVerify> list = this.baseMapper.findDrgVerifyList(applyDateStr, areaType, state, false);
            List<YbDrgVerify> updateList = new ArrayList<>();
            List<YbPerson> personList = new ArrayList<>();
            if(list.size() > 100) {
                personList = iYbPersonService.findPersonList(new YbPerson(), 0);
            } else {
                personList = this.findPerson(list);
            }
            for (YbDrgVerify item : list) {
                if (StringUtils.isNotBlank(item.getVerifyDksId()) &&
                        StringUtils.isNotBlank(item.getVerifyDksName()) &&
                        StringUtils.isNotBlank(item.getVerifyDoctorCode()) &&
                        StringUtils.isNotBlank(item.getVerifyDoctorName())) {
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
            List<YbDrgVerify> list = this.baseMapper.findDrgVerifyList(applyDateStr, areaType, state, false);
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

    @Override
    @Transactional
    public String createEndJobState(String applyDateStr, Integer areaType, int[] jobTypeList) {
        String msg = "ok";
        YbDrgApply drgApply = this.iYbDrgApplyService.findDrgApplyByApplyDateStrs(applyDateStr, areaType);
        if (drgApply != null) {
            String cron = "";
            Date thisDate = new Date();
            Date endDate = drgApply.getEndDate();
            Date enableDate = DataTypeHelpers.addDateMethod(drgApply.getEnableDate(), 1);
            String areaName = iComConfiguremanageService.getConfigAreaName(areaType);

            String bb = "DRG " + applyDateStr + " " + areaName;
            String remark = "";
            String methodName = "";
            String beanName = "";
            // Job state 0.正常    1.暂停
            //复议截止日期
            try {
                for (int type : jobTypeList) {
                    if (type == 1) {
                        remark = bb + " 截止日期";
                        methodName = "endDate";
                        beanName = "drgManageTask";
                        cron = this.getCron(endDate, type, 6, areaType);
                    } else if (type == 2) {
                        remark = bb + " 确认日期";
                        beanName = "drgManageTask";
                        methodName = "enableDate";
                        cron = this.getCron(enableDate, 2, null, areaType);
                    } else if (type == 3) {
                        beanName = "smsTask";
                        remark = bb + " 提醒申诉";
                        methodName = "sendDrgSmsWarnTask";
                        cron = this.getCron(endDate, 3, null, areaType);
                    } else {
                        msg = "noType";
                        break;
                    }
                    Job job = new Job();
                    job.setBeanName(beanName);
                    job.setMethodName(methodName);
                    job.setParams(applyDateStr + "|" + Integer.toString(areaType));
                    //查询当前正在运行的任务
                    List<Job> findList = jobService.jobList(job);
                    job.setCronExpression(cron);
                    job.setStatus("0");
                    job.setCreateTime(thisDate);
                    job.setRemark(remark);
                    //将正在运行的任务关闭/删除
                    if (findList.size() > 0) {
                        findList = findList.stream().filter(s -> s.getStatus().equals("0")).collect(Collectors.toList());
                        if (findList.size() > 0) {
                            String[] jobs = new String[findList.size()];
                            int j = 0;
                            for (Job item : findList) {
                                jobs[j] = item.getJobId().toString();
                            }
                            //将正在运行的任务关闭
                            if (jobs != null) {
                                jobService.deleteJobs(jobs);
                            }
                        }
                    }
                    //创建任务
                    jobService.createJob(job);
                    //启用当前任务
                    List<Job> jobList = jobService.jobList(job);
                    jobService.resume(jobList.get(0).getJobId().toString());
                }
            } catch (Exception e) {
                msg = e.getMessage();
                log.error(msg);
            }
        } else {
            msg = "noApply";
        }
        return msg;
    }

    private String getCron(Date date, int type, Integer addTime, int areaType) {
        Calendar now = Calendar.getInstance();
        now.setTime(date);
        if (type == 1) {
            now.add(now.MINUTE, addTime);
        }
        String fen = "" + now.get(Calendar.MINUTE);
        String shi = "" + now.get(Calendar.HOUR_OF_DAY);
        String ri = "" + now.get(Calendar.DAY_OF_MONTH);
        String yue = "" + (now.get(Calendar.MONTH) + 1);
        String nian = "" + now.get(Calendar.YEAR);
//        String miao = "" + now.get(Calendar.SECOND);
//        String haomiao = "" + now.getTimeInMillis();

        String cron = "";
        // drgDate
        if (type == 1) {
            cron = "0 " + fen + " " + shi + " " + ri + " " + yue + " ? " + nian + "-" + nian;
        }
        //enableDate
        if (type == 2) {
            if (areaType == 0) {
                cron = "0 10 0 " + ri + " " + yue + " ? " + nian + "-" + nian;
            } else {
                cron = "0 15 0 " + ri + " " + yue + " ? " + nian + "-" + nian;
            }
        }
        //
        if (type == 3) {
            if (areaType == 0) {
                cron = "0 0/3 6 " + ri + " " + yue + " ? " + nian + "-" + nian;
            } else {
                cron = "0 0/3 7 " + ri + " " + yue + " ? " + nian + "-" + nian;
            }
        }
        return cron;
    }


}