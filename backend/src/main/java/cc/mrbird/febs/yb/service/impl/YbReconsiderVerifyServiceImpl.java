package cc.mrbird.febs.yb.service.impl;

import cc.mrbird.febs.com.controller.DataTypeHelpers;
import cc.mrbird.febs.com.entity.ComSms;
import cc.mrbird.febs.com.service.IComConfiguremanageService;
import cc.mrbird.febs.com.service.IComSmsService;
import cc.mrbird.febs.common.domain.QueryRequest;
import cc.mrbird.febs.common.properties.FebsProperties;
import cc.mrbird.febs.common.utils.SortUtil;
import cc.mrbird.febs.yb.dao.YbReconsiderVerifyMapper;
import cc.mrbird.febs.yb.entity.*;
import cc.mrbird.febs.yb.service.*;
import cn.hutool.core.lang.Validator;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.text.DateFormat;
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
    IYbPersonService iYbPersonService;

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
    public int findReconsiderVerifyResetCheckCounts(String applyDateStr) {
        return this.baseMapper.findReconsiderVerifyResetCheckCount(applyDateStr);
    }

    @Override
    @Transactional
    public void insertReconsiderVerifyImports(String applyDateStr, Long matchPersonId, String matchPersonName) {
        //this.baseMapper.insertReconsiderVerifyImport(applyDate, matchPersonId, matchPersonName);

        YbReconsiderApply ybReconsiderApply = this.iYbReconsiderApplyService.findReconsiderApplyByApplyDateStrs(applyDateStr);
        boolean isCreate = true;
        int state = ybReconsiderApply.getState();
        if (state == YbDefaultValue.APPLYSTATE_2 || state == YbDefaultValue.APPLYSTATE_3 || state == YbDefaultValue.APPLYSTATE_4 || state == YbDefaultValue.APPLYSTATE_5) {
            int typeno = state == YbDefaultValue.APPLYSTATE_2 || state == YbDefaultValue.APPLYSTATE_3 ? YbDefaultValue.TYPENO_1 : YbDefaultValue.TYPENO_2;
            List<YbReconsiderApplyData> radList = iYbReconsiderApplyDataService.findReconsiderApplyDataByNotVerifys(ybReconsiderApply.getApplyDateStr(), YbDefaultValue.DATATYPE_0, typeno);

            if (radList.size() > 0) {
                isCreate = false;
                List<YbReconsiderPriorityLevel> rplList = iYbReconsiderPriorityLevelService.findReconsiderPriorityLevelList();
                YbReconsiderInpatientfees waquery = new YbReconsiderInpatientfees();
                waquery.setApplyDateStr(applyDateStr);
                waquery.setTypeno(typeno);
                waquery.setDataType(YbDefaultValue.DATATYPE_0);
                List<YbReconsiderInpatientfees> rifList = iYbReconsiderInpatientfeesService.findReconsiderInpatientfeesList(waquery);

                List<YbReconsiderPriorityLevel> rlProjectList = rplList.stream().filter(s -> s.getState() == YbReconsiderPriorityLevel.PL_STATE_2).collect(Collectors.toList());
                List<YbReconsiderPriorityLevel> rlRuleList = rplList.stream().filter(s -> s.getState() == YbReconsiderPriorityLevel.PL_STATE_1).collect(Collectors.toList());
                List<YbReconsiderPriorityLevel> rlDeptList = rplList.stream().filter(s -> s.getState() == YbReconsiderPriorityLevel.PL_STATE_3).collect(Collectors.toList());

                List<YbReconsiderInpatientfees> queryRifList = new ArrayList<>();
                List<YbReconsiderPriorityLevel> queryRlList = new ArrayList<>();
                List<YbPerson> personList = new ArrayList<>();
                personList = iYbPersonService.list();
                List<YbPerson> queryPersontList = new ArrayList<>();
                List<YbReconsiderVerify> createList = new ArrayList<>();
                Date thisDate = new Date();
                for (YbReconsiderApplyData item : radList) {
                    isCreate = false;

                    queryRifList = rifList.stream().filter(s ->
                            s.getApplyDateStr().equals(applyDateStr) &&
                                    s.getApplyDataId().equals(item.getId())
                    ).collect(Collectors.toList());

                    YbReconsiderVerify ybReconsiderVerify = new YbReconsiderVerify();
                    ybReconsiderVerify.setApplyDataId(item.getId());

                    queryRlList = new ArrayList<>();

                    //规则
                    if (item.getRuleName() != null) {
                        queryRlList = rlRuleList.stream().filter(
                                s -> s.getRplName().trim().equals(item.getRuleName().trim())
                        ).collect(Collectors.toList());
                    }
                    //项目
                    if (queryRlList.size() == 0) {
                        if (item.getProjectName() != null) {
                            queryRlList = rlProjectList.stream().filter(
                                    s -> s.getRplName().trim().equals(item.getProjectName().trim())
                            ).collect(Collectors.toList());
                        }
                    }
                    if (queryRlList.size() > 0) {
                        ybReconsiderVerify.setVerifyDeptCode(queryRlList.get(0).getDeptCode());
                        ybReconsiderVerify.setVerifyDeptName(queryRlList.get(0).getDeptName());
                        ybReconsiderVerify.setVerifyDoctorCode(queryRlList.get(0).getDoctorCode());
                        ybReconsiderVerify.setVerifyDoctorName(queryRlList.get(0).getDoctorName());

                        isCreate = true;
                    } else {
                        if (queryRifList.size() > 0) {
                            YbReconsiderInpatientfees entityRif = queryRifList.get(0);
                            String deptCode = entityRif.getExcuteDeptId();
                            //科室
                            if (deptCode != null) {
                                queryRlList = rlDeptList.stream().filter(
                                        s -> s.getDeptCode().equals(deptCode)
                                ).collect(Collectors.toList());
                            } else {
                                queryRlList = new ArrayList<>();
                            }

                            String strDocId = "";
                            String strDocName = "";
                            String strDeptId = "";
                            String strDeptName = "";
                            if (queryRlList.size() > 0) {
                                YbReconsiderPriorityLevel entityRpl = queryRlList.get(0);
                                if (entityRpl.getPersonType().equals(YbReconsiderPriorityLevel.PERSON_TYPE_4)) {
                                    ybReconsiderVerify.setVerifyDeptCode(entityRpl.getDeptCode());
                                    ybReconsiderVerify.setVerifyDeptName(entityRpl.getDeptName());
                                    ybReconsiderVerify.setVerifyDoctorCode(entityRpl.getDoctorCode());
                                    ybReconsiderVerify.setVerifyDoctorName(entityRpl.getDoctorName());
                                    isCreate = true;
                                } else {
                                    if (entityRpl.getPersonType().equals(YbReconsiderPriorityLevel.PERSON_TYPE_1)) {
                                        strDocId = entityRif.getOrderDocId();
                                        strDocName = this.getInpatientfeesDocName(entityRif, personList, YbReconsiderPriorityLevel.PERSON_TYPE_1);
                                        strDeptId = entityRif.getDeptId();
                                        strDeptName = entityRif.getDeptName();
                                    }
                                    if (entityRpl.getPersonType().equals(YbReconsiderPriorityLevel.PERSON_TYPE_2)) {
                                        strDocId = entityRif.getExcuteDocId();
                                        strDocName = this.getInpatientfeesDocName(entityRif, personList, YbReconsiderPriorityLevel.PERSON_TYPE_2);
                                        strDeptId = entityRif.getExcuteDeptId();
                                        strDeptName = entityRif.getExcuteDeptName();
                                    }
                                    if (entityRpl.getPersonType().equals(YbReconsiderPriorityLevel.PERSON_TYPE_3)) {
                                        strDocId = entityRif.getFeeOperatorId();
                                        strDocName = this.getInpatientfeesDocName(entityRif, personList, YbReconsiderPriorityLevel.PERSON_TYPE_3);
                                        strDeptId = entityRif.getFeeDeptId();
                                        strDeptName = entityRif.getFeeDeptName();
                                    }
                                    if (strDeptId != null && strDeptName != null && strDocId != null && strDocName != null) {
                                        ybReconsiderVerify.setVerifyDeptCode(entityRif.getDeptId());
                                        ybReconsiderVerify.setVerifyDeptName(entityRif.getDeptName());
                                        ybReconsiderVerify.setVerifyDoctorCode(strDocId);
                                        ybReconsiderVerify.setVerifyDoctorName(strDocName);
                                        isCreate = true;
                                    }
                                }
                            } else {
                                strDocId = entityRif.getOrderDocId();
                                strDocName = this.getInpatientfeesDocName(entityRif, personList, YbReconsiderPriorityLevel.PERSON_TYPE_1);
                                strDeptId = entityRif.getDeptId();
                                strDeptName = entityRif.getDeptName();
                                if (strDeptId != null && strDeptName != null && strDocId != null && strDocName != null) {
                                    ybReconsiderVerify.setVerifyDeptCode(strDeptId);
                                    ybReconsiderVerify.setVerifyDeptName(strDeptName);
                                    ybReconsiderVerify.setVerifyDoctorCode(strDocId);
                                    ybReconsiderVerify.setVerifyDoctorName(strDocName);
                                    isCreate = true;
                                }
                            }
                        }
                    }
                    if (isCreate) {
                        if (ybReconsiderVerify.getId() == null || "".equals(ybReconsiderVerify.getId())) {
                            ybReconsiderVerify.setId(UUID.randomUUID().toString());
                        }
                        if (queryRifList.size() > 0) {
                            ybReconsiderVerify.setOrderDeptCode(queryRifList.get(0).getDeptId());
                            ybReconsiderVerify.setOrderDeptName(queryRifList.get(0).getDeptName());
                            ybReconsiderVerify.setOrderDoctorCode(queryRifList.get(0).getOrderDocId());
                            ybReconsiderVerify.setOrderDoctorName(queryRifList.get(0).getOrderDocName());
                        }

                        ybReconsiderVerify.setApplyDateStr(ybReconsiderApply.getApplyDateStr());
                        ybReconsiderVerify.setTypeno(item.getTypeno());
                        ybReconsiderVerify.setOrderNumber(item.getOrderNumber());
                        ybReconsiderVerify.setOrderNum(item.getOrderNum());
                        ybReconsiderVerify.setOperateDate(thisDate);//'操作日期'
                        ybReconsiderVerify.setMatchDate(thisDate);//'匹配日期'
                        ybReconsiderVerify.setMatchPersonId(matchPersonId);//'匹配人代码'
                        ybReconsiderVerify.setMatchPersonName(matchPersonName);//'匹配人'
                        ybReconsiderVerify.setDataType(YbDefaultValue.DATATYPE_0);//扣款类型  0 明细扣款 1 主单扣款
                        ybReconsiderVerify.setState(YbDefaultValue.VERIFYSTATE_1);//1 待审核、2已审核、3已发送
                        ybReconsiderVerify.setIsDeletemark(1);
                        ybReconsiderVerify.setCreateUserId(matchPersonId);
                        ybReconsiderVerify.setCreateTime(thisDate);
                        createList.add(ybReconsiderVerify);
                    }
                    isCreate = false;
                }

                isCreate = true;//判断状态是否更新
                if (createList.size() > 0) {
                    this.saveBatch(createList);
                }
            }
        }
        if ((state == YbDefaultValue.APPLYSTATE_2 || state == YbDefaultValue.APPLYSTATE_4) && isCreate) {
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

    /**
     * 1type 开单医生 2type 执行医生 3type计费人
     */
    private String getInpatientfeesDocName(YbReconsiderInpatientfees item, List<YbPerson> personList, int type) {
        List<YbPerson> queryPersontList = new ArrayList<>();
        String strDocId = "";
        String strDocName = "";
        if (type == 1) {
            strDocId = item.getOrderDocId();
            strDocName = item.getOrderDocName();
        }
        if (type == 2) {
            strDocId = item.getExcuteDocId();
            strDocName = item.getExcuteDocName();
        }
        if (type == 3) {
            strDocId = item.getFeeOperatorId();
            strDocName = item.getFeeOperatorName();
        }
        if (strDocId != null && strDocName == null) {
            String docId = strDocId;
            queryPersontList = personList.stream().filter(p ->
                    p.getPersonCode().equals(docId)).collect(Collectors.toList());
            if (queryPersontList.size() > 0) {
                strDocName = queryPersontList.get(0).getPersonName();
            }
        }
        return strDocName;
    }

    @Override
    @Transactional
    public void insertMainReconsiderVerifyImports(String applyDateStr, Long matchPersonId, String matchPersonName) {
//        this.baseMapper.insertMainReconsiderVerifyImport(applyDateStr, matchPersonId, matchPersonName);

        YbReconsiderApply ybReconsiderApply = this.iYbReconsiderApplyService.findReconsiderApplyByApplyDateStrs(applyDateStr);
        boolean isCreate = true;
        int state = ybReconsiderApply.getState();
        if (state == YbDefaultValue.APPLYSTATE_2 || state == YbDefaultValue.APPLYSTATE_3 || state == YbDefaultValue.APPLYSTATE_4 || state == YbDefaultValue.APPLYSTATE_5) {
            int typeno = state == YbDefaultValue.APPLYSTATE_2 || state == YbDefaultValue.APPLYSTATE_3 ? YbDefaultValue.TYPENO_1 : YbDefaultValue.TYPENO_2;
            List<YbReconsiderApplyData> radList = iYbReconsiderApplyDataService.findReconsiderApplyDataByNotVerifys(ybReconsiderApply.getApplyDateStr(), YbDefaultValue.DATATYPE_1, typeno);

            if (radList.size() > 0) {
                isCreate = false;
                YbReconsiderInpatientfees waquery = new YbReconsiderInpatientfees();
                waquery.setApplyDateStr(applyDateStr);
                waquery.setTypeno(typeno);
                waquery.setDataType(YbDefaultValue.DATATYPE_1);
                List<YbReconsiderInpatientfees> rifList = iYbReconsiderInpatientfeesService.findReconsiderInpatientfeesList(waquery);

                List<YbReconsiderInpatientfees> queryRifList = new ArrayList<>();
                List<YbPerson> personList = new ArrayList<>();
                personList = iYbPersonService.list();
                List<YbPerson> queryPersontList = new ArrayList<>();
                List<YbReconsiderVerify> createList = new ArrayList<>();
                Date thisDate = new Date();
                for (YbReconsiderApplyData item : radList) {
                    YbReconsiderVerify ybReconsiderVerify = new YbReconsiderVerify();
                    ybReconsiderVerify.setApplyDataId(item.getId());

                    queryRifList = rifList.stream().filter(s ->
                            s.getApplyDateStr().equals(applyDateStr) &&
                                    s.getApplyDataId().equals(item.getId())
                    ).collect(Collectors.toList());

                    if (queryRifList.size() > 0) {
                        YbReconsiderInpatientfees entityRif = queryRifList.get(0);
                        String strDocId = entityRif.getOrderDocId();
                        String strDocName = this.getInpatientfeesDocName(entityRif, personList, 1);
                        if (entityRif.getDeptId() != null && entityRif.getDeptName() != null && strDocId != null && strDocName != null) {
                            ybReconsiderVerify.setVerifyDeptCode(entityRif.getDeptId());
                            ybReconsiderVerify.setVerifyDeptName(entityRif.getDeptName());
                            ybReconsiderVerify.setVerifyDoctorCode(strDocId);
                            ybReconsiderVerify.setVerifyDoctorName(strDocName);
                            isCreate = true;
                        }
                    }

                    if (isCreate) {
                        if (ybReconsiderVerify.getId() == null || "".equals(ybReconsiderVerify.getId())) {
                            ybReconsiderVerify.setId(UUID.randomUUID().toString());
                        }

                        ybReconsiderVerify.setApplyDateStr(ybReconsiderApply.getApplyDateStr());
                        ybReconsiderVerify.setTypeno(item.getTypeno());
                        ybReconsiderVerify.setOrderNumber(item.getOrderNumber());
                        ybReconsiderVerify.setOrderNum(item.getOrderNum());
                        ybReconsiderVerify.setOperateDate(thisDate);//'操作日期'
                        ybReconsiderVerify.setMatchDate(thisDate);//'匹配日期'
                        ybReconsiderVerify.setMatchPersonId(matchPersonId);//'匹配人代码'
                        ybReconsiderVerify.setMatchPersonName(matchPersonName);//'匹配人'
                        ybReconsiderVerify.setDataType(YbDefaultValue.DATATYPE_1);//扣款类型  0 明细扣款 1 主单扣款
                        ybReconsiderVerify.setState(YbDefaultValue.VERIFYSTATE_1);//1 待审核、2已审核、3已发送
                        ybReconsiderVerify.setIsDeletemark(1);
                        ybReconsiderVerify.setCreateUserId(matchPersonId);
                        ybReconsiderVerify.setCreateTime(thisDate);
                        createList.add(ybReconsiderVerify);
                    }
                    isCreate = false;
                }

                isCreate = true;//判断状态是否更新
                if (createList.size() > 0) {
                    this.saveBatch(createList);
                }
            }
        }
        if ((state == YbDefaultValue.APPLYSTATE_2 || state == YbDefaultValue.APPLYSTATE_4) && isCreate) {
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

    @Override
    @Transactional
    public void importReconsiderDataVerifys(String applyDateStr, int dataType, int typeno, List<YbReconsiderVerify> verifyList) {
        LambdaQueryWrapper<YbReconsiderVerify> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(YbReconsiderVerify::getApplyDateStr, applyDateStr);
        wrapper.eq(YbReconsiderVerify::getDataType, dataType);
        wrapper.eq(YbReconsiderVerify::getTypeno, typeno);
        List<YbReconsiderVerify> list = this.list(wrapper);
        List<YbReconsiderVerify> queryList = new ArrayList<>();
        List<YbReconsiderVerify> createList = new ArrayList<>();
        List<YbReconsiderVerify> updateList = new ArrayList<>();

        if (list.size() > 0) {
            for (YbReconsiderVerify item : verifyList) {
                queryList = list.stream().filter(
                        s -> s.getApplyDataId().equals(item.getApplyDataId())
                ).collect(Collectors.toList());
                if (queryList.size() > 0) {
                    YbReconsiderVerify rv = queryList.get(0);
                    if (!item.getVerifyDeptCode().equals(rv.getVerifyDeptCode()) || !item.getVerifyDeptName().equals(rv.getVerifyDeptName()) ||
                            !item.getVerifyDoctorCode().equals(rv.getVerifyDoctorCode()) || !item.getVerifyDoctorName().equals(rv.getVerifyDoctorName()) ||
                            !item.getOrderDeptCode().equals(rv.getOrderDeptCode()) || !item.getOrderDeptName().equals(rv.getOrderDeptName()) ||
                            !item.getOrderDoctorCode().equals(rv.getOrderDoctorCode()) || !item.getOrderDoctorName().equals(rv.getOrderDoctorName())
                    ) {
                        YbReconsiderVerify udpate = new YbReconsiderVerify();
                        udpate.setId(rv.getId());
                        udpate.setVerifyDeptCode(item.getVerifyDeptCode());
                        udpate.setVerifyDeptName(item.getVerifyDeptName());
                        udpate.setVerifyDoctorCode(item.getVerifyDoctorCode());
                        udpate.setVerifyDoctorName(item.getVerifyDoctorName());

                        udpate.setOrderDeptCode(item.getOrderDeptCode());
                        udpate.setOrderDeptName(item.getOrderDeptName());
                        udpate.setOrderDoctorCode(item.getOrderDoctorCode());
                        udpate.setOrderDoctorName(item.getOrderDoctorName());
                        updateList.add(udpate);
                    }
                } else {
                    createList.add(item);
                }
            }
        } else {
            this.saveBatch(verifyList);
        }
        if (createList.size() > 0) {
            this.saveBatch(createList);
        }

        if (updateList.size() > 0) {
            this.updateBatchById(updateList);
        }
    }

    private List<YbPerson> findPerson(List<YbReconsiderVerify> list) {
        List<YbPerson> personList = new ArrayList<>();
        ArrayList<String> personCodeList = new ArrayList<>();
        for (YbReconsiderVerify item : list) {
            if (item.getVerifyDoctorCode() != "" && item.getVerifyDoctorCode() != null) {
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

    private List<YbReconsiderInpatientfees> findReconsiderInpatientfees(List<YbReconsiderVerify> list) {
        List<YbReconsiderInpatientfees> rifList = new ArrayList<>();
        ArrayList<String> applyDataIdList = new ArrayList<>();
        for (YbReconsiderVerify item : list) {
            applyDataIdList.add(item.getApplyDataId());
        }
        if (applyDataIdList.size() > 0) {
            rifList = this.iYbReconsiderInpatientfeesService.findReconsiderInpatientfeesList(applyDataIdList);
        }
        return rifList;
    }

    @Override
    @Transactional
    public void updateSendStates(List<YbReconsiderVerify> list, Integer dataType, Long uId, String Uname) {
        Date thisDate = new java.sql.Timestamp(new Date().getTime());
        int day = iComConfiguremanageService.getConfigDay();

        Date addDate = DataTypeHelpers.addDateMethod(thisDate, day);
        String applyDateStr = "";
        int typeno = 0;
        List<YbPerson> personList = this.findPerson(list);
        List<YbPerson> queryPersonList = new ArrayList<>();
        List<ComSms> smsList = new ArrayList<>();
        int nOpenSms = febsProperties.getOpenSms();
        boolean isOpenSms = nOpenSms == 1 ? true : false;

        String sendContent = "";
        if (isOpenSms && list.size() > 0) {
            ComSms qu = new ComSms();
            qu.setState(ComSms.STATE_0);
            qu.setSendType(ComSms.SENDTYPE_1);
            smsList = iComSmsService.findLmdSmsList(qu);
            typeno = list.get(0).getTypeno();
            applyDateStr = list.get(0).getApplyDateStr();
            sendContent = this.iYbReconsiderApplyService.getSendMessage(applyDateStr, addDate, typeno);
        }
        for (YbReconsiderVerify ybReconsiderVerify : list) {
            if (ybReconsiderVerify.getVerifyDeptCode() != "" && ybReconsiderVerify.getVerifyDeptCode() != null &&
                    ybReconsiderVerify.getVerifyDeptName() != "" && ybReconsiderVerify.getVerifyDeptName() != null &&
                    ybReconsiderVerify.getVerifyDoctorCode() != "" && ybReconsiderVerify.getVerifyDoctorCode() != null &&
                    ybReconsiderVerify.getVerifyDoctorName() != "" && ybReconsiderVerify.getVerifyDoctorName() != null) {
                queryPersonList = personList.stream().filter(
                        s -> s.getPersonCode().equals(ybReconsiderVerify.getVerifyDoctorCode())
                ).collect(Collectors.toList());
                if (queryPersonList.size() > 0) {
                    //更新
                    ybReconsiderVerify.setState(YbDefaultValue.VERIFYSTATE_3);
                    ybReconsiderVerify.setModifyUserId(uId);
                    ybReconsiderVerify.setModifyTime(thisDate);
                    ybReconsiderVerify.setSendPersonId(uId);
                    ybReconsiderVerify.setSendPersonName(Uname);
                    ybReconsiderVerify.setSendDate(thisDate);
                    ybReconsiderVerify.setOperateDate(thisDate);

                    String strVerifyDeptName = DataTypeHelpers.stringReplaceSetString(ybReconsiderVerify.getVerifyDeptName(), ybReconsiderVerify.getVerifyDeptCode() + "-");
                    ybReconsiderVerify.setVerifyDeptName(strVerifyDeptName);
                    String strVerifyDoctorName = DataTypeHelpers.stringReplaceSetString(ybReconsiderVerify.getVerifyDoctorName(), ybReconsiderVerify.getVerifyDoctorCode() + "-");
                    ybReconsiderVerify.setVerifyDoctorName(strVerifyDoctorName);

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

                    ybAppealManage.setApplyDateStr(ybReconsiderVerify.getApplyDateStr());
                    ybAppealManage.setTypeno(ybReconsiderVerify.getTypeno());
                    ybAppealManage.setOrderNumber(ybReconsiderVerify.getOrderNumber());
                    ybAppealManage.setOrderNum(ybReconsiderVerify.getOrderNum());

                    ybAppealManage.setOperateDate(thisDate);
                    if (dataType == 0) {
                        ybAppealManage.setOrderDoctorCode(ybReconsiderVerify.getOrderDoctorCode());
                        ybAppealManage.setOrderDoctorName(ybReconsiderVerify.getOrderDoctorName());
                        ybAppealManage.setOrderDeptCode(ybReconsiderVerify.getOrderDeptCode());
                        ybAppealManage.setOrderDeptName(ybReconsiderVerify.getOrderDeptName());
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
                        List<String> userCodeList = new ArrayList<>();
                        if (userCodeList.stream().filter(s -> s.equals(ybReconsiderVerify.getVerifyDoctorCode())).count() == 0) {
                            if (smsList.stream().filter(s -> s.getSendcode().equals(ybReconsiderVerify.getVerifyDoctorCode())).count() == 0) {
                                userCodeList.add(ybReconsiderVerify.getVerifyDoctorCode());
                                if (Validator.isMobile(queryPersonList.get(0).getTel())) {
                                    ComSms comSms = new ComSms();
                                    comSms.setId(UUID.randomUUID().toString());
                                    comSms.setSendcode(queryPersonList.get(0).getPersonCode());
                                    comSms.setSendname(queryPersonList.get(0).getPersonName());
                                    comSms.setMobile(queryPersonList.get(0).getTel());
                                    comSms.setSendType(ComSms.SENDTYPE_1);
                                    comSms.setState(ComSms.STATE_0);

                                    comSms.setSendcontent(sendContent);
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
    }

    @Override
    @Transactional
    public void updateAllSendStates(String applyDateStr, Integer state, Integer dataType, Long uId, String Uname) {
        Date thisDate = new java.sql.Timestamp(new Date().getTime());
        YbReconsiderApply reconsiderApply = iYbReconsiderApplyService.findReconsiderApplyByApplyDateStrs(applyDateStr);

        if (reconsiderApply != null) {
            int applyState = reconsiderApply.getState();
            int typeno = applyState == YbDefaultValue.APPLYSTATE_2 || applyState == YbDefaultValue.APPLYSTATE_3 ? YbDefaultValue.TYPENO_1 :
                    applyState == YbDefaultValue.APPLYSTATE_4 || applyState == YbDefaultValue.APPLYSTATE_5 ? YbDefaultValue.TYPENO_2 : 0;

            if (typeno == YbDefaultValue.TYPENO_1 || typeno == YbDefaultValue.TYPENO_2) {
                int day = iComConfiguremanageService.getConfigDay();

                Date addDate = DataTypeHelpers.addDateMethod(thisDate, day);

                List<YbPerson> personList = iYbPersonService.findPersonList(new YbPerson(), 0);
                List<YbPerson> queryPersonList = new ArrayList<>();
                List<ComSms> smsList = new ArrayList<>();
                int nOpenSms = febsProperties.getOpenSms();
                List<YbReconsiderVerify> list = this.baseMapper.findReconsiderVerifyList(applyDateStr, dataType, state, typeno);

                String sendContent = "";
                boolean isOpenSms = nOpenSms == 1 ? true : false;
                if (isOpenSms && list.size() > 0) {
                    ComSms qu = new ComSms();
                    qu.setState(ComSms.STATE_0);
                    qu.setSendType(ComSms.SENDTYPE_1);
                    smsList = iComSmsService.findLmdSmsList(qu);

                    sendContent = this.iYbReconsiderApplyService.getSendMessage(applyDateStr, addDate, typeno);
                }
                for (YbReconsiderVerify ybReconsiderVerify : list) {
                    if (ybReconsiderVerify.getState() != YbDefaultValue.VERIFYSTATE_3) {
                        queryPersonList = personList.stream().filter(
                                s -> s.getPersonCode().equals(ybReconsiderVerify.getVerifyDoctorCode())
                        ).collect(Collectors.toList());

                        if (queryPersonList.size() > 0) {
                            //更新
                            ybReconsiderVerify.setState(YbDefaultValue.VERIFYSTATE_3);
                            ybReconsiderVerify.setModifyUserId(uId);
                            ybReconsiderVerify.setModifyTime(thisDate);
                            ybReconsiderVerify.setSendPersonId(uId);
                            ybReconsiderVerify.setSendPersonName(Uname);
                            ybReconsiderVerify.setSendDate(thisDate);
                            ybReconsiderVerify.setOperateDate(thisDate);

                            String strVerifyDeptName = DataTypeHelpers.stringReplaceSetString(ybReconsiderVerify.getVerifyDeptName(), ybReconsiderVerify.getVerifyDeptCode() + "-");
                            ybReconsiderVerify.setVerifyDeptName(strVerifyDeptName);
                            String strVerifyDoctorName = DataTypeHelpers.stringReplaceSetString(ybReconsiderVerify.getVerifyDoctorName(), ybReconsiderVerify.getVerifyDoctorCode() + "-");
                            ybReconsiderVerify.setVerifyDoctorName(strVerifyDoctorName);

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

                            ybAppealManage.setApplyDateStr(ybReconsiderVerify.getApplyDateStr());
                            ybAppealManage.setTypeno(ybReconsiderVerify.getTypeno());
                            ybAppealManage.setOrderNumber(ybReconsiderVerify.getOrderNumber());
                            ybAppealManage.setOrderNum(ybReconsiderVerify.getOrderNum());

                            ybAppealManage.setOperateDate(thisDate);
                            if (dataType == 0) {
                                ybAppealManage.setOrderDoctorCode(ybReconsiderVerify.getOrderDoctorCode());
                                ybAppealManage.setOrderDoctorName(ybReconsiderVerify.getOrderDoctorName());
                                ybAppealManage.setOrderDeptCode(ybReconsiderVerify.getOrderDeptCode());
                                ybAppealManage.setOrderDeptName(ybReconsiderVerify.getOrderDeptName());
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
                                List<String> userCodeList = new ArrayList<>();
                                if (userCodeList.stream().filter(s -> s.equals(ybReconsiderVerify.getVerifyDoctorCode())).count() == 0) {
                                    if (smsList.stream().filter(s -> s.getSendcode().equals(ybReconsiderVerify.getVerifyDoctorCode())).count() == 0) {
                                        userCodeList.add(ybReconsiderVerify.getVerifyDoctorCode());

                                        if (Validator.isMobile(queryPersonList.get(0).getTel())) {
                                            ComSms comSms = new ComSms();
                                            comSms.setId(UUID.randomUUID().toString());
                                            comSms.setSendcode(queryPersonList.get(0).getPersonCode());
                                            comSms.setSendname(queryPersonList.get(0).getPersonName());
                                            comSms.setMobile(queryPersonList.get(0).getTel());
                                            comSms.setSendType(ComSms.SENDTYPE_1);
                                            comSms.setState(ComSms.STATE_0);
                                            ;
                                            comSms.setSendcontent(sendContent);
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
            }
        }
    }

    //单个，多个核对
    @Override
    @Transactional
    public void updateReviewerStates(List<YbReconsiderVerify> list, Long uId, String Uname) {
        List<YbPerson> personList = this.findPerson(list);
        for (YbReconsiderVerify item : list) {
            Date thisDate = new Date();
            if (item.getVerifyDeptCode() != "" && item.getVerifyDeptCode() != null &&
                    item.getVerifyDeptName() != "" && item.getVerifyDeptName() != null &&
                    item.getVerifyDoctorCode() != "" && item.getVerifyDoctorCode() != null &&
                    item.getVerifyDoctorName() != "" && item.getVerifyDoctorName() != null) {
                if (personList.stream().filter(s -> s.getPersonCode().equals(item.getVerifyDoctorCode())).count() > 0) {
                    String strVerifyDeptName = DataTypeHelpers.stringReplaceSetString(item.getVerifyDeptName(), item.getVerifyDeptCode() + "-");
                    item.setVerifyDeptName(strVerifyDeptName);
                    String strVerifyDoctorName = DataTypeHelpers.stringReplaceSetString(item.getVerifyDoctorName(), item.getVerifyDoctorCode() + "-");
                    item.setVerifyDoctorName(strVerifyDoctorName);

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
    }

    //全部核对
    @Override
    @Transactional
    public void updateAllReviewerStates(String applyDateStr, int state, int dataType, Long uId, String Uname) {
        YbReconsiderApply reconsiderApply = iYbReconsiderApplyService.findReconsiderApplyByApplyDateStrs(applyDateStr);
        if (reconsiderApply != null) {
            int applyState = reconsiderApply.getState();
            int typeno = applyState == YbDefaultValue.APPLYSTATE_2 || applyState == YbDefaultValue.APPLYSTATE_3 ? YbDefaultValue.TYPENO_1 :
                    applyState == YbDefaultValue.APPLYSTATE_4 || applyState == YbDefaultValue.APPLYSTATE_5 ? YbDefaultValue.TYPENO_2 : 0;

            if (typeno == YbDefaultValue.TYPENO_1 || typeno == YbDefaultValue.TYPENO_2) {
                List<YbReconsiderVerify> list = this.baseMapper.findReconsiderVerifyList(applyDateStr, dataType, state, typeno);
                Date thisDate = new Date();
                List<YbReconsiderVerify> updateList = new ArrayList<>();
                List<YbPerson> personList = this.iYbPersonService.findPersonList(new YbPerson(), 0);
                for (YbReconsiderVerify item : list) {
                    if (item.getVerifyDeptCode() != "" && item.getVerifyDeptCode() != null &&
                            item.getVerifyDeptName() != "" && item.getVerifyDeptName() != null &&
                            item.getVerifyDoctorCode() != "" && item.getVerifyDoctorCode() != null &&
                            item.getVerifyDoctorName() != "" && item.getVerifyDoctorName() != null) {
                        if (personList.stream().filter(s -> s.getPersonCode().equals(item.getVerifyDoctorCode())).count() > 0) {
                            YbReconsiderVerify update = new YbReconsiderVerify();
                            update.setId(item.getId());
                            update.setState(YbDefaultValue.VERIFYSTATE_2);
                            update.setReviewerId(uId);
                            update.setReviewerName(Uname);
                            update.setReviewerDate(thisDate);
                            updateList.add(update);
                        }
                    }
                }
                if (updateList.size() > 0) {
                    this.updateBatchById(updateList);
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

                String strVerifyDeptName = DataTypeHelpers.stringReplaceSetString(item.getVerifyDeptName(), item.getVerifyDeptCode() + "-");
                item.setVerifyDeptName(strVerifyDeptName);
                String strVerifyDoctorName = DataTypeHelpers.stringReplaceSetString(item.getVerifyDoctorName(), item.getVerifyDoctorCode() + "-");
                item.setVerifyDoctorName(strVerifyDoctorName);

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