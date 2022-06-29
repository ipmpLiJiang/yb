package cc.mrbird.febs.chs.service.impl;

import cc.mrbird.febs.chs.entity.*;
import cc.mrbird.febs.chs.service.*;
import cc.mrbird.febs.com.controller.DataTypeHelpers;
import cc.mrbird.febs.com.entity.ComSms;
import cc.mrbird.febs.com.service.IComConfiguremanageService;
import cc.mrbird.febs.com.service.IComSmsService;
import cc.mrbird.febs.common.domain.QueryRequest;
import cc.mrbird.febs.common.properties.FebsProperties;
import cc.mrbird.febs.common.utils.SortUtil;
import cc.mrbird.febs.chs.dao.YbChsVerifyMapper;
import cc.mrbird.febs.job.domain.Job;
import cc.mrbird.febs.job.service.JobService;
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
 * @since 2022-06-20
 */
@Slf4j
@Service("IYbChsVerifyService")
@Transactional(propagation = Propagation.SUPPORTS, readOnly = true, rollbackFor = Exception.class)
public class YbChsVerifyServiceImpl extends ServiceImpl<YbChsVerifyMapper, YbChsVerify> implements IYbChsVerifyService {

    @Autowired
    IYbChsApplyService iYbChsApplyService;

    @Autowired
    IYbChsApplyDataService iYbChsApplyDataService;

    @Autowired
    IYbChsJkService iYbChsJkService;

    @Autowired
    IYbDksService iYbDksService;

    @Autowired
    IYbDeptService iYbDeptService;

    @Autowired
    IYbPersonService iYbPersonService;

    @Autowired
    FebsProperties febsProperties;

    @Autowired
    IComSmsService iComSmsService;

    @Autowired
    IYbChsManageService iYbChsManageService;

    @Autowired
    JobService jobService;

    @Autowired
    IComConfiguremanageService iComConfiguremanageService;

    @Override
    public IPage<YbChsVerify> findYbChsVerifys(QueryRequest request, YbChsVerify ybChsVerify) {
        try {
            LambdaQueryWrapper<YbChsVerify> queryWrapper = new LambdaQueryWrapper<>();
//            queryWrapper.eq(YbChsVerify::getIsDeletemark, 1);//1是未删 0是已删


            Page<YbChsVerify> page = new Page<>();
            SortUtil.handlePageSort(request, page, false);//true 是属性  false是数据库字段可两个
            return this.page(page, queryWrapper);
        } catch (Exception e) {
            log.error("获取字典信息失败", e);
            return null;
        }
    }

    @Override
    public IPage<YbChsVerify> findYbChsVerifyList(QueryRequest request, YbChsVerify ybChsVerify) {
        try {
            Page<YbChsVerify> page = new Page<>();
            SortUtil.handlePageSort(request, page, false);//true 是属性  false是数据库字段可两个
            return this.baseMapper.findYbChsVerify(page, ybChsVerify);
        } catch (Exception e) {
            log.error("获取失败", e);
            return null;
        }
    }

    @Override
    @Transactional
    public void createYbChsVerify(YbChsVerify ybChsVerify) {
        ybChsVerify.setId(UUID.randomUUID().toString());
//        ybChsVerify.setCreateTime(new Date());
//        ybChsVerify.setIsDeletemark(1);
        this.save(ybChsVerify);
    }

    @Override
    @Transactional
    public void updateYbChsVerify(YbChsVerify ybChsVerify) {
//        ybChsVerify.setModifyTime(new Date());
        this.baseMapper.updateYbChsVerify(ybChsVerify);
    }

    @Override
    @Transactional
    public void deleteYbChsVerifys(String[] Ids) {
        List<String> list = Arrays.asList(Ids);
        this.baseMapper.deleteBatchIds(list);
    }

    @Override
    @Transactional
    public void insertChsVerifyImports(String applyDateStr, Integer areaType, Long matchPersonId, String matchPersonName) {
        YbChsApply ybChsApply = this.iYbChsApplyService.findChsApplyByApplyDateStrs(applyDateStr, areaType);
        boolean isCreate = true;
        if (ybChsApply != null) {
            int state = ybChsApply.getState();
            if (state == YbDefaultValue.APPLYSTATE_2 || state == YbDefaultValue.APPLYSTATE_3) {
                List<YbChsApplyData> radList = iYbChsApplyDataService.findChsApplyDataByNotVerifys(ybChsApply.getId(), ybChsApply.getApplyDateStr(), areaType);

                LambdaQueryWrapper<YbChsJk> wrapper = new LambdaQueryWrapper<>();
                wrapper.eq(YbChsJk::getApplyDateStr, applyDateStr);
                wrapper.eq(YbChsJk::getAreaType, areaType);
                List<YbChsJk> jkList = iYbChsJkService.list(wrapper);
                List<YbChsJk> queryJkList = new ArrayList<>();

                List<YbDept> deptList = iYbDeptService.findDeptList(new YbDept(), 0);
                List<YbDept> deptQueryList = new ArrayList<>();

                if (radList.size() > 0) {
                    List<YbChsVerify> createList = new ArrayList<>();
                    for (YbChsApplyData item : radList) {
                        YbChsVerify ybChsVerify = new YbChsVerify();
                        ybChsVerify.setId(UUID.randomUUID().toString());
                        ybChsVerify.setApplyDataId(item.getId());
                        ybChsVerify.setApplyDateStr(ybChsApply.getApplyDateStr());
                        ybChsVerify.setOrderNum(item.getOrderNum());
                        ybChsVerify.setState(YbDefaultValue.VERIFYSTATE_1);
                        ybChsVerify.setAreaType(areaType);

                        queryJkList = jkList.stream().filter(s -> s.getApplyDataId().equals(item.getId())).collect(Collectors.toList());
                        if (queryJkList.size() > 0) {
                            YbChsJk jk = queryJkList.get(0);
                            ybChsVerify.setVerifyDoctorCode(jk.getOrderDocId());
                            ybChsVerify.setVerifyDoctorName(jk.getOrderDocName());
                            deptQueryList = deptList.stream().filter(s->s.getDeptId().equals(jk.getDeptId())).collect(Collectors.toList());
                            if(deptQueryList.size() > 0) {
                                ybChsVerify.setVerifyDksId(deptQueryList.get(0).getDksId());
                                ybChsVerify.setVerifyDksName(deptQueryList.get(0).getDksName());
                            }
                        }
                        createList.add(ybChsVerify);
                    }

                    isCreate = true;//判断状态是否更新
                    if (createList.size() > 0) {
                        this.saveBatch(createList);
                    }
                }
            }
            if ((state == YbDefaultValue.APPLYSTATE_2) && isCreate) {
                this.iYbChsApplyService.updateChsApplyState3(ybChsApply);
            }
        }
    }

    @Override
    @Transactional
    public void updateChsVerifyImports(List<YbChsVerify> list) {
        for (YbChsVerify item : list) {
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
                    LambdaQueryWrapper<YbChsVerify> queryWrapper = new LambdaQueryWrapper<>();
                    queryWrapper.eq(YbChsVerify::getId, item.getId());
                    queryWrapper.eq(YbChsVerify::getState, YbDefaultValue.VERIFYSTATE_1);
                    YbChsVerify updateVerify = new YbChsVerify();
                    updateVerify.setVerifyDoctorCode(item.getVerifyDoctorCode());
                    updateVerify.setVerifyDoctorName(item.getVerifyDoctorName());
                    updateVerify.setVerifyDksId(item.getVerifyDksId());
                    updateVerify.setVerifyDksName(item.getVerifyDksName());
                    this.baseMapper.update(updateVerify, queryWrapper);
                }
            }

        }
    }


    private List<YbPerson> findPerson(List<YbChsVerify> list) {
        List<YbPerson> personList = new ArrayList<>();
        ArrayList<String> personCodeList = new ArrayList<>();
        for (YbChsVerify item : list) {
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
    public void updateSendStates(List<YbChsVerify> list, Integer areaType, Long uId, String Uname) {
        String applyDateStr = list.get(0).getApplyDateStr();
        YbChsApply drgApply = iYbChsApplyService.findChsApplyByApplyDateStrs(applyDateStr, areaType);
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
                qu.setSendType(ComSms.SENDTYPE_21);
                qu.setState(ComSms.STATE_0);
                smsList = iComSmsService.findLmdSmsList(qu);
                sendContent = this.iYbChsApplyService.getSendMessage(applyDateStr, addDate, areaType, false);

            }
            for (YbChsVerify ybChsVerify : list) {
                if (StringUtils.isNotBlank(ybChsVerify.getVerifyDksId()) &&
                        StringUtils.isNotBlank(ybChsVerify.getVerifyDksName()) &&
                        StringUtils.isNotBlank(ybChsVerify.getVerifyDoctorCode()) &&
                        StringUtils.isNotBlank(ybChsVerify.getVerifyDoctorName())) {
                    queryPersonList = personList.stream().filter(
                            s -> s.getPersonCode().equals(ybChsVerify.getVerifyDoctorCode())
                    ).collect(Collectors.toList());
                    if (queryPersonList.size() > 0) {
                        String strVerifyDksName = DataTypeHelpers.stringReplaceSetString(ybChsVerify.getVerifyDksName(), ybChsVerify.getVerifyDksId() + "-");
                        ybChsVerify.setVerifyDksName(strVerifyDksName);
                        String strVerifyDoctorName = DataTypeHelpers.stringReplaceSetString(ybChsVerify.getVerifyDoctorName(), ybChsVerify.getVerifyDoctorCode() + "-");
                        ybChsVerify.setVerifyDoctorName(strVerifyDoctorName);

                        //插入申诉管理
                        YbChsManage ybChsManage = new YbChsManage();
                        ybChsManage.setVerifyId(ybChsVerify.getId());
                        ybChsManage.setApplyDataId(ybChsVerify.getApplyDataId());
                        ybChsManage.setReadyDksId(ybChsVerify.getVerifyDksId());
                        ybChsManage.setReadyDksName(ybChsVerify.getVerifyDksName());
                        ybChsManage.setReadyDoctorCode(ybChsVerify.getVerifyDoctorCode());
                        ybChsManage.setReadyDoctorName(ybChsVerify.getVerifyDoctorName());

                        ybChsManage.setApplyDateStr(ybChsVerify.getApplyDateStr());
                        ybChsManage.setOrderNum(ybChsVerify.getOrderNum());

                        ybChsManage.setOperateDate(thisDate);
                        ybChsManage.setOperateProcess("发送操作-接受申请");
                        ybChsManage.setState(YbDefaultValue.ACCEPTSTATE_0);
                        ybChsManage.setEnableDate(addDate);
                        ybChsManage.setAreaType(areaType);

                        iYbChsManageService.createYbChsManage(ybChsManage);
                        LambdaQueryWrapper<YbChsVerify> queryWrapper = new LambdaQueryWrapper<>();
                        queryWrapper.eq(YbChsVerify::getId, ybChsVerify.getId());
                        queryWrapper.eq(YbChsVerify::getState, YbDefaultValue.VERIFYSTATE_2);

                        if (isOpenSms) {
                            if (userCodeList.stream().filter(s -> s.equals(ybChsVerify.getVerifyDoctorCode())).count() == 0) {
                                if (smsList.stream().filter(s -> s.getSendcode().equals(ybChsVerify.getVerifyDoctorCode())).count() == 0) {
                                    userCodeList.add(ybChsVerify.getVerifyDoctorCode());
                                    if (Validator.isMobile(queryPersonList.get(0).getTel())) {
                                        ComSms comSms = new ComSms();
                                        comSms.setId(UUID.randomUUID().toString());
                                        comSms.setSendcode(queryPersonList.get(0).getPersonCode());
                                        comSms.setSendname(queryPersonList.get(0).getPersonName());
                                        comSms.setMobile(queryPersonList.get(0).getTel());
                                        comSms.setSendType(ComSms.SENDTYPE_21);
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
                        YbChsVerify updateVerify = new YbChsVerify();
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
        YbChsApply drgApply = iYbChsApplyService.findChsApplyByApplyDateStrs(applyDateStr, areaType);

        if (drgApply != null) {
            Date addDate = DataTypeHelpers.addDateMethod(drgApply.getEnableDate(), 1);
            List<YbPerson> personList = new ArrayList<>();
            List<YbPerson> queryPersonList = new ArrayList<>();
            List<ComSms> smsList = new ArrayList<>();
            List<String> userCodeList = new ArrayList<>();
            int nOpenSms = febsProperties.getOpenSms();
            List<YbChsVerify> list = this.baseMapper.findChsVerifyList(applyDateStr, areaType, state, false);
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
                qu.setSendType(ComSms.SENDTYPE_21);
                qu.setState(ComSms.STATE_0);
                smsList = iComSmsService.findLmdSmsList(qu);
                sendContent = this.iYbChsApplyService.getSendMessage(applyDateStr, addDate, areaType, false);
            }
            for (YbChsVerify ybChsVerify : list) {
                if (ybChsVerify.getState() != YbDefaultValue.VERIFYSTATE_3) {
                    queryPersonList = personList.stream().filter(
                            s -> s.getPersonCode().equals(ybChsVerify.getVerifyDoctorCode())
                    ).collect(Collectors.toList());

                    if (queryPersonList.size() > 0) {
                        String strVerifyDksName = DataTypeHelpers.stringReplaceSetString(ybChsVerify.getVerifyDksName(), ybChsVerify.getVerifyDksId() + "-");
                        ybChsVerify.setVerifyDksName(strVerifyDksName);
                        String strVerifyDoctorName = DataTypeHelpers.stringReplaceSetString(ybChsVerify.getVerifyDoctorName(), ybChsVerify.getVerifyDoctorCode() + "-");
                        ybChsVerify.setVerifyDoctorName(strVerifyDoctorName);

                        //插入申诉管理
                        YbChsManage ybChsManage = new YbChsManage();
                        ybChsManage.setVerifyId(ybChsVerify.getId());
                        ybChsManage.setApplyDataId(ybChsVerify.getApplyDataId());
                        ybChsManage.setReadyDksId(ybChsVerify.getVerifyDksId());
                        ybChsManage.setReadyDksName(ybChsVerify.getVerifyDksName());
                        ybChsManage.setReadyDoctorCode(ybChsVerify.getVerifyDoctorCode());
                        ybChsManage.setReadyDoctorName(ybChsVerify.getVerifyDoctorName());

                        ybChsManage.setApplyDateStr(ybChsVerify.getApplyDateStr());
                        ybChsManage.setOrderNum(ybChsVerify.getOrderNum());

                        ybChsManage.setOperateDate(thisDate);
                        ybChsManage.setOperateProcess("发送操作-接受申请");
                        ybChsManage.setState(YbDefaultValue.ACCEPTSTATE_0);
                        ybChsManage.setEnableDate(addDate);
                        ybChsManage.setAreaType(areaType);

                        iYbChsManageService.createYbChsManage(ybChsManage);

                        LambdaQueryWrapper<YbChsVerify> queryWrapper = new LambdaQueryWrapper<>();
                        queryWrapper.eq(YbChsVerify::getId, ybChsVerify.getId());
                        queryWrapper.eq(YbChsVerify::getState, YbDefaultValue.VERIFYSTATE_2);
                        if (isOpenSms) {
                            if (userCodeList.stream().filter(s -> s.equals(ybChsVerify.getVerifyDoctorCode())).count() == 0) {
                                if (smsList.stream().filter(s -> s.getSendcode().equals(ybChsVerify.getVerifyDoctorCode())).count() == 0) {
                                    userCodeList.add(ybChsVerify.getVerifyDoctorCode());
                                    if (StringUtils.isNotBlank(queryPersonList.get(0).getTel())) {
                                        if (Validator.isMobile(queryPersonList.get(0).getTel())) {
                                            ComSms comSms = new ComSms();
                                            comSms.setId(UUID.randomUUID().toString());
                                            comSms.setSendcode(queryPersonList.get(0).getPersonCode());
                                            comSms.setSendname(queryPersonList.get(0).getPersonName());
                                            comSms.setMobile(queryPersonList.get(0).getTel());
                                            comSms.setSendType(ComSms.SENDTYPE_21);
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
                        YbChsVerify updateVerify = new YbChsVerify();
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
    public void updateReviewerStates(List<YbChsVerify> list) {
        List<YbPerson> personList = this.findPerson(list);
        for (YbChsVerify item : list) {
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
                        YbChsVerify updateVerify = new YbChsVerify();
                        updateVerify.setVerifyDoctorCode(item.getVerifyDoctorCode());
                        updateVerify.setVerifyDoctorName(strVerifyDoctorName);
                        updateVerify.setVerifyDksId(item.getVerifyDksId());
                        updateVerify.setVerifyDksName(item.getVerifyDksName());
                        updateVerify.setState(YbDefaultValue.VERIFYSTATE_2);
                        LambdaQueryWrapper<YbChsVerify> queryWrapper = new LambdaQueryWrapper<>();
                        queryWrapper.eq(YbChsVerify::getId, item.getId());
                        queryWrapper.eq(YbChsVerify::getState, YbDefaultValue.VERIFYSTATE_1);
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
        YbChsApply drgApply = iYbChsApplyService.findChsApplyByApplyDateStrs(applyDateStr, areaType);
        if (drgApply != null) {
            List<YbChsVerify> list = this.baseMapper.findChsVerifyList(applyDateStr, areaType, state, false);
            List<YbChsVerify> updateList = new ArrayList<>();
            List<YbPerson> personList = new ArrayList<>();
            if(list.size() > 100) {
                personList = iYbPersonService.findPersonList(new YbPerson(), 0);
            } else {
                personList = this.findPerson(list);
            }
            for (YbChsVerify item : list) {
                if (StringUtils.isNotBlank(item.getVerifyDksId()) &&
                        StringUtils.isNotBlank(item.getVerifyDksName()) &&
                        StringUtils.isNotBlank(item.getVerifyDoctorCode()) &&
                        StringUtils.isNotBlank(item.getVerifyDoctorName())) {
                    if (personList.stream().filter(s -> s.getPersonCode().equals(item.getVerifyDoctorCode())).count() > 0) {
                        YbChsVerify update = new YbChsVerify();
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
        YbChsApply drgApply = iYbChsApplyService.findChsApplyByApplyDateStrs(applyDateStr, areaType);
        if (drgApply != null) {
            List<YbChsVerify> list = this.baseMapper.findChsVerifyList(applyDateStr, areaType, state, false);
            List<YbChsVerify> updateList = new ArrayList<>();
            for (YbChsVerify item : list) {
                YbChsVerify update = new YbChsVerify();
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
    public void importChsDataVerifys(YbChsApply drgApply, List<YbChsVerify> verifyList) {
        LambdaQueryWrapper<YbChsVerify> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(YbChsVerify::getApplyDateStr, drgApply.getApplyDateStr());
        wrapper.eq(YbChsVerify::getAreaType, drgApply.getAreaType());
        List<YbChsVerify> list = this.list(wrapper);

        List<YbChsVerify> queryList = new ArrayList<>();
        List<YbChsVerify> createList = new ArrayList<>();
        List<YbChsVerify> updateList = new ArrayList<>();

        boolean isCreate = false;

        if (list.size() > 0) {
            for (YbChsVerify item : verifyList) {
                queryList = list.stream().filter(
                        s -> s.getApplyDataId().equals(item.getApplyDataId())
                ).collect(Collectors.toList());
                if (queryList.size() > 0) {
                    YbChsVerify rv = queryList.get(0);

                    if (!isImportTrue(item.getVerifyDksId(), rv.getVerifyDksId()) ||
                            !isImportTrue(item.getVerifyDksName(), rv.getVerifyDksName()) ||
                            !isImportTrue(item.getVerifyDoctorCode(), rv.getVerifyDoctorCode()) ||
                            !isImportTrue(item.getVerifyDoctorName(), rv.getVerifyDoctorName())
                    ) {
                        if(rv.getState() == YbDefaultValue.VERIFYSTATE_1) {
                            YbChsVerify udpate = new YbChsVerify();
                            udpate.setId(rv.getId());
                            udpate.setVerifyDksId(item.getVerifyDksId());
                            udpate.setVerifyDksName(item.getVerifyDksName());
                            udpate.setVerifyDoctorCode(item.getVerifyDoctorCode());
                            udpate.setVerifyDoctorName(item.getVerifyDoctorName());
                            updateList.add(udpate);
                        }
                    }
                } else {
                    createList.add(item);
                }
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
        if ((state == YbDefaultValue.APPLYSTATE_2) && isCreate) {
            this.iYbChsApplyService.updateChsApplyState3(drgApply);
        }
    }
    private boolean isImportTrue(String v1, String v2) {
        v1 = v1 == null ? "" : v1;
        v2 = v2 == null ? "" : v2;
        return v1.equals(v2);
    }


    @Override
    @Transactional
    public void deleteChsVerifyState(YbChsVerify delVerify) {
        LambdaQueryWrapper<YbChsVerify> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(YbChsVerify::getApplyDateStr, delVerify.getApplyDateStr());
        wrapper.eq(YbChsVerify::getAreaType, delVerify.getAreaType());
        wrapper.eq(YbChsVerify::getState, delVerify.getState());
        this.baseMapper.delete(wrapper);
    }

    @Override
    @Transactional
    public String createEndJobState(String applyDateStr, Integer areaType, int[] jobTypeList) {
        String msg = "ok";
        YbChsApply chsApply = this.iYbChsApplyService.findChsApplyByApplyDateStrs(applyDateStr, areaType);
        if (chsApply != null) {
            String cron = "";
            Date thisDate = new Date();
            Date endDate = chsApply.getEndDate();
            Date enableDate = DataTypeHelpers.addDateMethod(chsApply.getEnableDate(), 1);
            String areaName = iComConfiguremanageService.getConfigAreaName(areaType);

            String bb = "CHS " + applyDateStr + " " + areaName;
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
                        beanName = "chsManageTask";
                        cron = this.getCron(endDate, type, 6, areaType);
                    } else if (type == 2) {
                        remark = bb + " 确认日期";
                        beanName = "chsManageTask";
                        methodName = "enableDate";
                        cron = this.getCron(enableDate, 2, null, areaType);
                    } else if (type == 3) {
                        beanName = "smsTask";
                        remark = bb + " 提醒申诉";
                        methodName = "sendChsSmsWarnTask";
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
        // chsDate
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
                cron = "0 0/3 7 " + ri + " " + yue + " ? " + nian + "-" + nian;
            } else {
                cron = "0 0/3 8 " + ri + " " + yue + " ? " + nian + "-" + nian;
            }
        }
        return cron;
    }


}