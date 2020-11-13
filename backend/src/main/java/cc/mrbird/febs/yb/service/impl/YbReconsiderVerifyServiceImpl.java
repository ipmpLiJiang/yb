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
import cc.mrbird.febs.yb.dao.YbReconsiderVerifyMapper;
import cc.mrbird.febs.yb.entity.*;
import cc.mrbird.febs.yb.service.*;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

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
@Service("IYbReconsiderVerifyService")
@Transactional(propagation = Propagation.SUPPORTS, readOnly = true, rollbackFor = Exception.class)
public class YbReconsiderVerifyServiceImpl extends ServiceImpl<YbReconsiderVerifyMapper, YbReconsiderVerify> implements IYbReconsiderVerifyService {

    @Autowired
    private IYbAppealManageService iYbAppealManageService;

    @Autowired
    private IComConfiguremanageService iComConfiguremanageService;

    @Autowired
    private IYbReconsiderApplyService iYbReconsiderApplyService;

    @Autowired
    private IYbReconsiderApplyDataService iYbReconsiderApplyDataService;

    @Autowired
    private IYbReconsiderInpatientfeesService iYbReconsiderInpatientfeesService;

    @Autowired
    private IYbReconsiderPriorityLevelService iYbReconsiderPriorityLevelService;

    @Autowired
    IComSmsService iComSmsService;

    @Autowired
    FebsProperties febsProperties;

    @Autowired
    UserService userService;

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
        if (ybReconsiderVerify.getId() == null || "".equals(ybReconsiderVerify.getId())) {
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
        //this.baseMapper.insertReconsiderVerifyImport(applyDate, matchPersonId, matchPersonName);

        LambdaQueryWrapper<YbReconsiderApply> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(YbReconsiderApply::getIsDeletemark, 1);//1是未删 0是已删
        queryWrapper.eq(YbReconsiderApply::getApplyDateStr, applyDate);
        List<YbReconsiderApply> applyList = this.iYbReconsiderApplyService.list(queryWrapper);

        if (applyList.size() > 0) {
            YbReconsiderApply ybReconsiderApply = applyList.get(0);
            boolean isCreate = false;
            int state = ybReconsiderApply.getState();
            if (state == YbDefaultValue.APPLYSTATE_2 || state == YbDefaultValue.APPLYSTATE_3 || state == YbDefaultValue.APPLYSTATE_4 || state == YbDefaultValue.APPLYSTATE_5) {
                int typeno = state == YbDefaultValue.APPLYSTATE_2 || state == YbDefaultValue.APPLYSTATE_3 ? YbDefaultValue.TYPENO_1 : YbDefaultValue.TYPENO_2;
                List<YbReconsiderApplyData> radList = iYbReconsiderApplyDataService.findReconsiderApplyDataByNotVerifys(ybReconsiderApply.getApplyDateStr(), YbDefaultValue.DATATYPE_0, typeno);
                if (radList.size() > 0) {
                    List<YbReconsiderPriorityLevel> rplList = iYbReconsiderPriorityLevelService.findReconsiderPriorityLevelList();
                    List<YbReconsiderInpatientfees> rifList = iYbReconsiderInpatientfeesService.findReconsiderInpatientfeesLists(applyDate);

                    List<YbReconsiderPriorityLevel> rlProjectList = rplList.stream().filter(s -> s.getState() == YbReconsiderPriorityLevel.PL_STATE_2).collect(Collectors.toList());
                    List<YbReconsiderPriorityLevel> rlRuleList = rplList.stream().filter(s -> s.getState() == YbReconsiderPriorityLevel.PL_STATE_1).collect(Collectors.toList());
                    List<YbReconsiderPriorityLevel> rlDeptList = rplList.stream().filter(s -> s.getState() == YbReconsiderPriorityLevel.PL_STATE_3).collect(Collectors.toList());

                    List<YbReconsiderInpatientfees> queryRifList = new ArrayList<>();
                    List<YbReconsiderPriorityLevel> queryRlList = new ArrayList<>();
                    List<YbReconsiderVerify> createList = new ArrayList<>();
                    Date thisDate = new Date();
                    for (YbReconsiderApplyData item : radList) {
                        YbReconsiderVerify ybReconsiderVerify = new YbReconsiderVerify();
                        ybReconsiderVerify.setApplyDataId(item.getId());

                        //规则
                        queryRlList = rlRuleList.stream().filter(
                                s -> s.getRplName().equals(item.getRuleName())
                        ).collect(Collectors.toList());
                        //项目
                        if (queryRlList.size() == 0) {
                            queryRlList = rlProjectList.stream().filter(
                                    s -> s.getRplName().equals(item.getProjectName())
                            ).collect(Collectors.toList());
                        }
                        if (queryRlList.size() > 0) {
                            ybReconsiderVerify.setVerifyDeptCode(queryRlList.get(0).getDeptCode());
                            ybReconsiderVerify.setVerifyDeptName(queryRlList.get(0).getDeptName());
                            ybReconsiderVerify.setVerifyDoctorCode(queryRlList.get(0).getDoctorCode());
                            ybReconsiderVerify.setVerifyDoctorName(queryRlList.get(0).getDoctorName());
                            isCreate = true;
                        } else {
                            queryRifList = rifList.stream().filter(s ->
                                    s.getBillNo().equals(item.getBillNo()) &&
                                            s.getTransNo().equals(item.getSerialNo()) &&
                                            s.getItemCode().equals(item.getProjectCode()) &&
                                            s.getItemName().equals(item.getProjectName()) &&
                                            s.getItemCount().equals(item.getNum())
                            ).collect(Collectors.toList());

                            if (queryRifList.size() > 0) {
                                String deptName = queryRifList.get(0).getExcuteDeptName();
                                //科室
                                queryRlList = rlDeptList.stream().filter(
                                        s -> s.getDeptName().equals(deptName)
                                ).collect(Collectors.toList());

                                if (queryRlList.size() > 0) {
                                    ybReconsiderVerify.setVerifyDeptCode(queryRifList.get(0).getExcuteDeptId());
                                    ybReconsiderVerify.setVerifyDeptName(queryRifList.get(0).getExcuteDeptName());
                                    ybReconsiderVerify.setVerifyDoctorCode(queryRifList.get(0).getExcuteDocId());
                                    ybReconsiderVerify.setVerifyDoctorName(queryRifList.get(0).getExcuteDocName());
                                } else {
                                    ybReconsiderVerify.setVerifyDeptCode(queryRifList.get(0).getDeptId());
                                    ybReconsiderVerify.setVerifyDeptName(queryRifList.get(0).getDeptName());
                                    ybReconsiderVerify.setVerifyDoctorCode(queryRifList.get(0).getOrderDocId());
                                    ybReconsiderVerify.setVerifyDoctorName(queryRifList.get(0).getOrderDocName());
                                }

                                isCreate = true;
                            }
                        }
                        if (ybReconsiderVerify.getId() == null || "".equals(ybReconsiderVerify.getId())) {
                            ybReconsiderVerify.setId(UUID.randomUUID().toString());
                        }
                        ybReconsiderVerify.setOperateDate(thisDate);//'操作日期'
                        ybReconsiderVerify.setMatchDate(thisDate);//'匹配日期'
                        ybReconsiderVerify.setMatchPersonId(matchPersonId);//'匹配人代码'
                        ybReconsiderVerify.setMatchPersonName(matchPersonName);//'匹配人'
                        ybReconsiderVerify.setDataType(YbDefaultValue.DATATYPE_0);//扣款类型  0 明细扣款 1 主单扣款
                        ybReconsiderVerify.setState(YbDefaultValue.VERIFYSTATE_1);//1 待审核、2已审核、3已发送
                        ybReconsiderVerify.setIsDeletemark(1);
                        ybReconsiderVerify.setCreateUserId(matchPersonId);
                        ybReconsiderVerify.setCreateTime(thisDate);

                        if (isCreate) createList.add(ybReconsiderVerify);

                        isCreate = false;
                    }

                    isCreate = true;//判断状态是否更新
                    this.saveBatch(createList);
                }
            }
            if ((ybReconsiderApply.getState() == YbDefaultValue.APPLYSTATE_2 || ybReconsiderApply.getState() == YbDefaultValue.APPLYSTATE_4) && isCreate) {
                YbReconsiderApply updateEntity = new YbReconsiderApply();
                updateEntity.setId(ybReconsiderApply.getId());
                if (ybReconsiderApply.getState() == YbDefaultValue.APPLYSTATE_2) { //上传一
                    updateEntity.setState(YbDefaultValue.APPLYSTATE_3);//申诉一/核对一
                    this.iYbReconsiderApplyService.updateById(updateEntity);
                } else if (ybReconsiderApply.getState() == YbDefaultValue.APPLYSTATE_4) {
                    updateEntity.setState(YbDefaultValue.APPLYSTATE_5);//申诉二/核对二
                    this.iYbReconsiderApplyService.updateById(updateEntity);
                }
            }
        }
    }

    @Override
    @Transactional
    public void insertMainReconsiderVerifyImports(String applyDate, Long matchPersonId, String matchPersonName) {
        this.baseMapper.insertMainReconsiderVerifyImport(applyDate, matchPersonId, matchPersonName);

        LambdaQueryWrapper<YbReconsiderApply> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(YbReconsiderApply::getIsDeletemark, 1);//1是未删 0是已删
        queryWrapper.eq(YbReconsiderApply::getApplyDateStr, applyDate);
        List<YbReconsiderApply> applyList = this.iYbReconsiderApplyService.list(queryWrapper);
        if (applyList.size() > 0) {
            YbReconsiderApply ybReconsiderApply = applyList.get(0);
            if (ybReconsiderApply.getState() == YbDefaultValue.APPLYSTATE_2 || ybReconsiderApply.getState() == YbDefaultValue.APPLYSTATE_4) {
                YbReconsiderApply updateEntity = new YbReconsiderApply();
                updateEntity.setId(ybReconsiderApply.getId());
                if (ybReconsiderApply.getState() == YbDefaultValue.APPLYSTATE_2) { //上传一
                    updateEntity.setState(YbDefaultValue.APPLYSTATE_3);//申诉一/核对一
                    this.iYbReconsiderApplyService.updateById(updateEntity);
                } else if (ybReconsiderApply.getState() == YbDefaultValue.APPLYSTATE_4) {
                    updateEntity.setState(YbDefaultValue.APPLYSTATE_5);//申诉二/核对二
                    this.iYbReconsiderApplyService.updateById(updateEntity);
                }
            }
        }
    }


    @Override
    @Transactional
    public void updateSendStates(List<YbReconsiderVerify> list, Integer dataType, Long uId, String Uname) {
        Date thisDate = new java.sql.Timestamp(new Date().getTime());
        int day = iComConfiguremanageService.getConfigDay();

        Date addDate = DataTypeHelpers.addDateMethod(thisDate, day);

        List<User> userList = new ArrayList<>();
        List<User> queryUserList = new ArrayList<>();
        List<ComSms> smsList = new ArrayList<>();
        List<String> userCodeList = new ArrayList<>();
        boolean isOpenSms = febsProperties.isOpenSms();
        if (isOpenSms) {
            userList = userService.findUserList(new User());
            ComSms qu = new ComSms();
            qu.setState(ComSms.STATE_0);
            qu.setSendType(ComSms.SENDTYPE_1);
            smsList = iComSmsService.findLmdSmsList(qu);
        }

        for (YbReconsiderVerify ybReconsiderVerify : list) {
            //更新
            ybReconsiderVerify.setState(YbDefaultValue.VERIFYSTATE_3);
            ybReconsiderVerify.setModifyUserId(uId);
            ybReconsiderVerify.setModifyTime(thisDate);
            ybReconsiderVerify.setSendPersonId(uId);
            ybReconsiderVerify.setSendPersonName(Uname);
            ybReconsiderVerify.setSendDate(thisDate);
            ybReconsiderVerify.setOperateDate(thisDate);
            //插入申诉管理
            YbAppealManage ybAppealManage = new YbAppealManage();
            ybAppealManage.setSourceType(YbDefaultValue.SOURCETYPE_0);
            ybAppealManage.setVerifyId(ybReconsiderVerify.getId());
            ybAppealManage.setVerifySendId(ybReconsiderVerify.getId());
            ybAppealManage.setApplyDataId(ybReconsiderVerify.getApplyDataId());
            ybAppealManage.setReadyDeptCode(ybReconsiderVerify.getVerifyDeptCode());
            ybAppealManage.setReadyDeptName(ybReconsiderVerify.getVerifyDeptName());
            ybAppealManage.setReadyDoctorCode(ybReconsiderVerify.getVerifyDoctorCode());
            ybAppealManage.setReadyDoctorName(ybReconsiderVerify.getVerifyDoctorName());
            ybAppealManage.setOperateDate(thisDate);
            if (dataType == 0) {
                ybAppealManage.setOperateProcess("发送操作-接受申请");
                ybAppealManage.setAcceptState(YbDefaultValue.ACCEPTSTATE_0);
            } else {
                ybAppealManage.setOperateProcess("发送操作-待申诉");
                ybAppealManage.setAcceptState(YbDefaultValue.ACCEPTSTATE_1);
            }
            ybAppealManage.setEnableDate(addDate);
            ybAppealManage.setIsDeletemark(1);
            ybAppealManage.setCreateUserId(uId);
            ybAppealManage.setCreateTime(thisDate);
            ybAppealManage.setDataType(dataType);

            iYbAppealManageService.createYbAppealManage(ybAppealManage);
            LambdaQueryWrapper<YbReconsiderVerify> queryWrapper = new LambdaQueryWrapper<>();
            queryWrapper.eq(YbReconsiderVerify::getId, ybReconsiderVerify.getId());
            queryWrapper.eq(YbReconsiderVerify::getDataType, dataType);

            if (dataType == 0) {
                queryWrapper.eq(YbReconsiderVerify::getState, YbDefaultValue.VERIFYSTATE_2);
            } else {
                queryWrapper.eq(YbReconsiderVerify::getState, YbDefaultValue.VERIFYSTATE_1);
            }

            if (isOpenSms) {
                if (userCodeList.stream().filter(s -> s.equals(ybReconsiderVerify.getVerifyDoctorCode())).count() == 0) {
                    if (smsList.stream().filter(s -> s.getSendcode().equals(ybReconsiderVerify.getVerifyDoctorCode())).count() == 0) {
                        userCodeList.add(ybReconsiderVerify.getVerifyDoctorCode());
                        queryUserList = userList.stream().filter(
                                s -> s.getUsername().equals(ybReconsiderVerify.getVerifyDoctorCode())
                        ).collect(Collectors.toList());
                        if (queryUserList.size() > 0) {
                            ComSms comSms = new ComSms();
                            comSms.setId(UUID.randomUUID().toString());
                            comSms.setSendcode(queryUserList.get(0).getUsername());
                            comSms.setSendname(queryUserList.get(0).getXmname());
                            comSms.setMobile(queryUserList.get(0).getMobile());
                            comSms.setSendType(ComSms.SENDTYPE_1);
                            comSms.setState(ComSms.STATE_0);
                            ;
                            comSms.setSendcontent("医保管理平台提醒您，您的医保复议任务已发布，请尽快处理。");
                            comSms.setOperatorId(uId);
                            comSms.setOperatorName(Uname);
                            comSms.setIsDeletemark(1);
                            comSms.setCreateUserId(uId);
                            comSms.setCreateTime(thisDate);
                            iComSmsService.save(comSms);
                        }
                    }
                }
            }

            this.baseMapper.update(ybReconsiderVerify, queryWrapper);
        }
    }

    @Override
    @Transactional
    public void updateAllSendStates(String applyDateStr, Integer state, Integer dataType, Long uId, String Uname) {
        Date thisDate = new java.sql.Timestamp(new Date().getTime());
        int day = iComConfiguremanageService.getConfigDay();

        Date addDate = DataTypeHelpers.addDateMethod(thisDate, day);

        List<User> userList = new ArrayList<>();
        List<User> queryUserList = new ArrayList<>();
        List<ComSms> smsList = new ArrayList<>();
        List<String> userCodeList = new ArrayList<>();
        boolean isOpenSms = febsProperties.isOpenSms();
        if (isOpenSms) {
            userList = userService.findUserList(new User());
            ComSms qu = new ComSms();
            qu.setState(ComSms.STATE_0);
            qu.setSendType(ComSms.SENDTYPE_1);
            smsList = iComSmsService.findLmdSmsList(qu);
        }

        List<YbReconsiderVerify> list = this.baseMapper.findReconsiderVerifyList(applyDateStr, dataType, state);
        for (YbReconsiderVerify ybReconsiderVerify : list) {
            if (ybReconsiderVerify.getState() != YbDefaultValue.VERIFYSTATE_3) {
                //更新
                ybReconsiderVerify.setState(YbDefaultValue.VERIFYSTATE_3);
                ybReconsiderVerify.setModifyUserId(uId);
                ybReconsiderVerify.setModifyTime(thisDate);
                ybReconsiderVerify.setSendPersonId(uId);
                ybReconsiderVerify.setSendPersonName(Uname);
                ybReconsiderVerify.setSendDate(thisDate);
                ybReconsiderVerify.setOperateDate(thisDate);
                //插入申诉管理
                YbAppealManage ybAppealManage = new YbAppealManage();
                ybAppealManage.setSourceType(YbDefaultValue.SOURCETYPE_0);
                ybAppealManage.setVerifyId(ybReconsiderVerify.getId());
                ybAppealManage.setVerifySendId(ybReconsiderVerify.getId());
                ybAppealManage.setApplyDataId(ybReconsiderVerify.getApplyDataId());
                ybAppealManage.setReadyDeptCode(ybReconsiderVerify.getVerifyDeptCode());
                ybAppealManage.setReadyDeptName(ybReconsiderVerify.getVerifyDeptName());
                ybAppealManage.setReadyDoctorCode(ybReconsiderVerify.getVerifyDoctorCode());
                ybAppealManage.setReadyDoctorName(ybReconsiderVerify.getVerifyDoctorName());
                ybAppealManage.setOperateDate(thisDate);
                if (dataType == 0) {
                    ybAppealManage.setOperateProcess("发送操作-接受申请");
                    ybAppealManage.setAcceptState(YbDefaultValue.ACCEPTSTATE_0);
                } else {
                    ybAppealManage.setOperateProcess("发送操作-待申诉");
                    ybAppealManage.setAcceptState(YbDefaultValue.ACCEPTSTATE_1);
                }
                ybAppealManage.setEnableDate(addDate);
                ybAppealManage.setIsDeletemark(1);
                ybAppealManage.setCreateUserId(uId);
                ybAppealManage.setCreateTime(thisDate);
                ybAppealManage.setDataType(dataType);

                iYbAppealManageService.createYbAppealManage(ybAppealManage);

                LambdaQueryWrapper<YbReconsiderVerify> queryWrapper = new LambdaQueryWrapper<>();
                queryWrapper.eq(YbReconsiderVerify::getId, ybReconsiderVerify.getId());
                queryWrapper.eq(YbReconsiderVerify::getDataType, dataType);
                if (dataType == 0) {
                    queryWrapper.eq(YbReconsiderVerify::getState, YbDefaultValue.VERIFYSTATE_2);
                } else {
                    queryWrapper.eq(YbReconsiderVerify::getState, YbDefaultValue.VERIFYSTATE_1);
                }
                if (isOpenSms) {
                    if (userCodeList.stream().filter(s -> s.equals(ybReconsiderVerify.getVerifyDoctorCode())).count() == 0) {
                        if (smsList.stream().filter(s -> s.getSendcode().equals(ybReconsiderVerify.getVerifyDoctorCode())).count() == 0) {
                            userCodeList.add(ybReconsiderVerify.getVerifyDoctorCode());
                            queryUserList = userList.stream().filter(
                                    s -> s.getUsername().equals(ybReconsiderVerify.getVerifyDoctorCode())
                            ).collect(Collectors.toList());
                            if (queryUserList.size() > 0) {
                                ComSms comSms = new ComSms();
                                comSms.setId(UUID.randomUUID().toString());
                                comSms.setSendcode(queryUserList.get(0).getUsername());
                                comSms.setSendname(queryUserList.get(0).getXmname());
                                comSms.setMobile(queryUserList.get(0).getMobile());
                                comSms.setSendType(ComSms.SENDTYPE_1);
                                comSms.setState(ComSms.STATE_0);
                                ;
                                comSms.setSendcontent("医保管理平台提醒您，您的医保复议任务已发布，请尽快处理。");
                                comSms.setOperatorId(uId);
                                comSms.setOperatorName(Uname);
                                comSms.setIsDeletemark(1);
                                comSms.setCreateUserId(uId);
                                comSms.setCreateTime(thisDate);
                                iComSmsService.save(comSms);
                            }
                        }
                    }
                }
                this.baseMapper.update(ybReconsiderVerify, queryWrapper);
            }
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
                item.setState(YbDefaultValue.VERIFYSTATE_2);
                item.setModifyTime(thisDate);
                item.setModifyUserId(uId);
                item.setReviewerId(uId);
                item.setReviewerName(Uname);
                item.setReviewerDate(thisDate);
                item.setOperateDate(thisDate);
                if (item.getId() == null || item.getId().equals("")) {
                    item.setId(UUID.randomUUID().toString());
                    item.setIsDeletemark(1);
                    item.setMatchPersonId(uId);
                    item.setMatchPersonName(Uname);
                    item.setMatchDate(thisDate);
                    item.setCreateTime(thisDate);
                    item.setCreateUserId(uId);
                    this.baseMapper.insert(item);
                } else {
                    LambdaQueryWrapper<YbReconsiderVerify> queryWrapper = new LambdaQueryWrapper<>();
                    queryWrapper.eq(YbReconsiderVerify::getId, item.getId());
                    queryWrapper.eq(YbReconsiderVerify::getDataType, YbDefaultValue.DATATYPE_0);
                    queryWrapper.eq(YbReconsiderVerify::getState, YbDefaultValue.VERIFYSTATE_1);
                    this.baseMapper.update(item, queryWrapper);
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
                item.setState(YbDefaultValue.VERIFYSTATE_1);
                item.setModifyTime(thisDate);
                item.setModifyUserId(uId);
                item.setOperateDate(thisDate);

                if (item.getId() == null || item.getId().equals("")) {
                    item.setId(UUID.randomUUID().toString());
                    item.setIsDeletemark(1);
                    item.setMatchPersonId(uId);
                    item.setMatchPersonName(Uname);
                    item.setMatchDate(thisDate);
                    item.setCreateTime(thisDate);
                    item.setCreateUserId(uId);
                    this.baseMapper.insert(item);
                } else {
                    LambdaQueryWrapper<YbReconsiderVerify> queryWrapper = new LambdaQueryWrapper<>();
                    queryWrapper.eq(YbReconsiderVerify::getId, item.getId());
                    queryWrapper.eq(YbReconsiderVerify::getState, YbDefaultValue.VERIFYSTATE_1);
                    this.baseMapper.update(item, queryWrapper);
                }
            }
        }
    }
}