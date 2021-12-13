package cc.mrbird.febs.yb.service.impl;

import cc.mrbird.febs.com.controller.DataTypeHelpers;
import cc.mrbird.febs.com.entity.ComConfiguremanage;
import cc.mrbird.febs.com.entity.ComSms;
import cc.mrbird.febs.com.service.IComConfiguremanageService;
import cc.mrbird.febs.com.service.IComSmsService;
import cc.mrbird.febs.common.domain.QueryRequest;
import cc.mrbird.febs.common.properties.FebsProperties;
import cc.mrbird.febs.common.utils.SortUtil;
import cc.mrbird.febs.job.domain.Job;
import cc.mrbird.febs.job.service.JobService;
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

    @Autowired
    IYbDeptService iYbDeptService;

    @Autowired
    private JobService jobService;

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
//        ybReconsiderVerify.setCreateTime(new Date());
        if (ybReconsiderVerify.getId() == null || "".equals(ybReconsiderVerify.getId())) {
            ybReconsiderVerify.setId(UUID.randomUUID().toString());
        }
        ybReconsiderVerify.setIsDeletemark(1);
        this.save(ybReconsiderVerify);
    }

    @Override
    @Transactional
    public void updateYbReconsiderVerify(YbReconsiderVerify ybReconsiderVerify) {
//        ybReconsiderVerify.setModifyTime(new Date());
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
    public void deleteReconsiderVerifyState(YbReconsiderVerify delVerify) {
        LambdaQueryWrapper<YbReconsiderVerify> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(YbReconsiderVerify::getApplyDateStr, delVerify.getApplyDateStr());
        wrapper.eq(YbReconsiderVerify::getAreaType, delVerify.getAreaType());
        wrapper.eq(YbReconsiderVerify::getTypeno, delVerify.getTypeno());
        wrapper.eq(YbReconsiderVerify::getDataType, delVerify.getDataType());
        wrapper.eq(YbReconsiderVerify::getState, delVerify.getState());
        this.baseMapper.delete(wrapper);
    }

    @Override
    public int findReconsiderVerifyResetCheckCounts(String applyDateStr, Integer areaType) {
        return this.baseMapper.findReconsiderVerifyResetCheckCount(applyDateStr, areaType);
    }

    @Override
    public int findReconsiderVerifyApplyDataCheckCounts(String pid, String applyDateStr, Integer areaType, Integer typeno) {
        return this.baseMapper.findReconsiderVerifyApplyDataCheckCount(pid, applyDateStr, areaType, typeno);
    }

    @Override
    @Transactional
    public void insertReconsiderVerifyImports(String applyDateStr, Integer areaType, Long matchPersonId, String matchPersonName, List<Integer> sxList) {
        //this.baseMapper.insertReconsiderVerifyImport(applyDate, matchPersonId, matchPersonName);

        YbReconsiderApply ybReconsiderApply = this.iYbReconsiderApplyService.findReconsiderApplyByApplyDateStrs(applyDateStr, areaType);
        boolean isCreate = true;
        if (ybReconsiderApply != null) {
            int state = ybReconsiderApply.getState();
            if (state == YbDefaultValue.APPLYSTATE_2 || state == YbDefaultValue.APPLYSTATE_3 || state == YbDefaultValue.APPLYSTATE_4 || state == YbDefaultValue.APPLYSTATE_5) {
                int typeno = state == YbDefaultValue.APPLYSTATE_2 || state == YbDefaultValue.APPLYSTATE_3 ? YbDefaultValue.TYPENO_1 : YbDefaultValue.TYPENO_2;
                List<YbReconsiderApplyData> radList = iYbReconsiderApplyDataService.findReconsiderApplyDataByNotVerifys(ybReconsiderApply.getId(), ybReconsiderApply.getApplyDateStr(), areaType, YbDefaultValue.DATATYPE_0, typeno);
                //多次点击匹配
//                iYbReconsiderApplyDataService.findReconsiderApplyDataByNotVerifyStates(ybReconsiderApply.getId(),ybReconsiderApply.getApplyDateStr(),areaType,YbDefaultValue.DATATYPE_0,typeno);
                if (radList.size() > 0) {
                    isCreate = false;
                    List<YbReconsiderPriorityLevel> rplList = iYbReconsiderPriorityLevelService.findReconsiderPriorityLevelList(areaType);
                    YbReconsiderInpatientfees waquery = new YbReconsiderInpatientfees();
                    waquery.setApplyDateStr(applyDateStr);
                    waquery.setAreaType(areaType);
                    waquery.setTypeno(typeno);
                    waquery.setDataType(YbDefaultValue.DATATYPE_0);
                    List<YbReconsiderInpatientfees> rifList = iYbReconsiderInpatientfeesService.findReconsiderInpatientfeesList(waquery);

                    List<YbReconsiderPriorityLevel> rlProjectList = rplList.stream().filter(s -> s.getState() == YbReconsiderPriorityLevel.PL_STATE_2).collect(Collectors.toList());
                    List<YbReconsiderPriorityLevel> rlRuleList = rplList.stream().filter(s -> s.getState() == YbReconsiderPriorityLevel.PL_STATE_1).collect(Collectors.toList());
                    List<YbReconsiderPriorityLevel> rlExcuteDeptList = rplList.stream().filter(s -> s.getState() == YbReconsiderPriorityLevel.PL_STATE_3 && s.getDeptState() == YbReconsiderPriorityLevel.DEPT_STATE_1).collect(Collectors.toList());
//                    List<YbReconsiderPriorityLevel> rlFeeDeptList = rplList.stream().filter(s -> s.getState() == YbReconsiderPriorityLevel.PL_STATE_3 && s.getDeptState() == YbReconsiderPriorityLevel.DEPT_STATE_2).collect(Collectors.toList());
                    List<YbDept> deptList = iYbDeptService.findDeptList(new YbDept(), 0);

                    List<YbDept> createDeptList = new ArrayList<>();
                    List<YbReconsiderInpatientfees> queryRifList = new ArrayList<>();
                    List<YbReconsiderPriorityLevel> queryRlList = new ArrayList<>();
                    List<YbPerson> personList = new ArrayList<>();
                    personList = iYbPersonService.findPersonList(new YbPerson(), 0);
                    List<Integer> intList = new ArrayList<>();
                    intList.add(4);// 住院默认顺序
                    intList.add(7);// 门诊默认顺序
                    List<ComConfiguremanage> configListA = this.iComConfiguremanageService.getConfigLists(intList);
                    List<ComConfiguremanage> configZyList = configListA.stream().filter(s -> s.getConfigureType().equals(4)).collect(Collectors.toList());
                    List<ComConfiguremanage> configMzList = configListA.stream().filter(s -> s.getConfigureType().equals(7)).collect(Collectors.toList());
                    List<ComConfiguremanage> configList = new ArrayList<>();

                    List<YbReconsiderVerify> createList = new ArrayList<>();
                    YbReconsiderInpatientfees entityRif = new YbReconsiderInpatientfees();
                    List<Integer> sunxuList = new ArrayList<>();
                    if (sxList == null || sxList.size() == 0) {
                        sunxuList.add(1);
                        sunxuList.add(2);
                        sunxuList.add(3);
                    } else {
                        sunxuList = sxList;
                    }
                    long count = 0;
                    for (YbReconsiderApplyData item : radList) {
                        YbReconsiderPriorityLevel entityRpl = new YbReconsiderPriorityLevel();
                        isCreate = false;
                        queryRifList = rifList.stream().filter(s ->
                                s.getApplyDateStr().equals(applyDateStr) &&
                                        s.getApplyDataId().equals(item.getId())
                        ).collect(Collectors.toList());

                        YbReconsiderVerify ybReconsiderVerify = new YbReconsiderVerify();
                        ybReconsiderVerify.setApplyDataId(item.getId());

                        queryRlList = new ArrayList<>();

                        for (int sx : sunxuList) {
                            if (sx == 1) {
                                //规则1
                                if (item.getRuleName() != null) {
                                    queryRlList = rlRuleList.stream().filter(
                                            s -> s.getRplName().trim().equals(item.getRuleName().trim())
                                    ).collect(Collectors.toList());
                                }
                            }
                            if (sx == 2) {
                                //项目2
                                if (item.getProjectName() != null) {
                                    queryRlList = rlProjectList.stream().filter(
                                            s -> s.getRplName().trim().equals(item.getProjectName().trim())
                                    ).collect(Collectors.toList());
                                }
                            }
                            if (sx == 1 || sx == 2) {
                                if (queryRlList.size() > 0) {
                                    entityRpl = queryRlList.get(0);
                                    if (queryRifList.size() > 0) {
                                        entityRif = queryRifList.get(0);
                                    } else {
                                        entityRif = null;
                                    }
                                    ybReconsiderVerify = this.getReconsiderVerifyImport(ybReconsiderVerify, entityRpl, entityRif);
                                    if (ybReconsiderVerify.getVerifyDoctorCode() != null) {
                                        isCreate = true;
                                    }
                                }
                            }
                            if (sx == 3) {
                                //科室3
                                if (!isCreate && queryRifList.size() > 0) {
                                    ybReconsiderVerify = this.getReconsiderVerifyDeptImport(ybReconsiderVerify, rlExcuteDeptList, personList, queryRifList.get(0));
                                    if (ybReconsiderVerify != null) {
                                        isCreate = true;
                                    } else {
                                        ybReconsiderVerify = new YbReconsiderVerify();
                                        ybReconsiderVerify.setApplyDataId(item.getId());
                                    }
                                }
                            }
                            if (isCreate) {
                                break;
                            }
                        }
                        //默认
                        if (!isCreate && queryRifList.size() > 0) {
                            if (item.getIsOutpfees() == 1) {
                                configList = configMzList;
                            } else {
                                configList = configZyList;
                            }
                            ybReconsiderVerify = this.getReconsiderVerifyConfImport(ybReconsiderVerify, personList, configList, queryRifList.get(0));
                            if (ybReconsiderVerify == null) {
                                ybReconsiderVerify = new YbReconsiderVerify();
                                ybReconsiderVerify.setApplyDataId(item.getId());
                            }
                        }
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
                        ybReconsiderVerify.setDataType(YbDefaultValue.DATATYPE_0);
                        ybReconsiderVerify.setState(YbDefaultValue.VERIFYSTATE_1);
                        ybReconsiderVerify.setAreaType(areaType);
                        ybReconsiderVerify.setIsDeletemark(1);

                        if (ybReconsiderVerify.getVerifyDeptCode() != null && !ybReconsiderVerify.getVerifyDeptCode().equals("")) {
                            String deptCode = ybReconsiderVerify.getVerifyDeptCode();
                            count = deptList.stream().filter(s -> s.getDeptId().equals(deptCode)).count();
                            if (count == 0) {
                                count = createDeptList.stream().filter(s -> s.getDeptId().equals(deptCode)).count();
                                if (count == 0) {
                                    YbDept create = new YbDept();
                                    create.setDeptId(ybReconsiderVerify.getVerifyDeptCode());
                                    create.setDeptName(ybReconsiderVerify.getVerifyDeptName());
                                    create.setCreateTime(new Date());
                                    create.setCreateUserId(matchPersonId);
                                    create.setState(1);
                                    create.setIsDeletemark(1);
                                    createDeptList.add(create);
                                }
                            }
                        }
                        createList.add(ybReconsiderVerify);
                    }

                    isCreate = true;//判断状态是否更新
                    if (createList.size() > 0) {
                        this.saveBatch(createList);
                    }
                    if (createDeptList.size() > 0) {
                        iYbDeptService.saveBatch(createDeptList);
                    }
                }
            }
            if ((state == YbDefaultValue.APPLYSTATE_2 || state == YbDefaultValue.APPLYSTATE_4) && isCreate) {
                this.iYbReconsiderApplyService.updateReconsiderApplyState2345(ybReconsiderApply);
            }
        }
    }

    private YbReconsiderVerify getReconsiderVerifyConfImport(YbReconsiderVerify ybReconsiderVerify, List<YbPerson> personList, List<ComConfiguremanage> configList, YbReconsiderInpatientfees entityRif) {
        String strDocId = "";
        String strDocName = "";
        String strDeptId = "";
        String strDeptName = "";
        String projectCode = "";
        List<ComConfiguremanage> queryConfigList = new ArrayList<>();
        ComConfiguremanage configuremanage = new ComConfiguremanage();

        boolean isKaiDan = false;
        projectCode = entityRif.getItemCode();
        if (projectCode != null && !projectCode.equals("")) {
            String strSub = projectCode.substring(0, 1);
            if (configList.size() > 0) {
                queryConfigList = configList.stream().filter(z -> z.getStringField().equals(strSub)).collect(Collectors.toList());
                if (queryConfigList.size() == 0) {
                    queryConfigList = configList.stream().filter(z -> z.getStringField().equals("zm")).collect(Collectors.toList());
                }
                if (queryConfigList.size() > 0) {
                    configuremanage = queryConfigList.get(0);
                    if (configuremanage.getIntField() == YbReconsiderPriorityLevel.PERSON_TYPE_1) {
                        isKaiDan = true;
                    } else if (configuremanage.getIntField() == YbReconsiderPriorityLevel.PERSON_TYPE_2) {
                        strDocId = entityRif.getExcuteDocId();
                        if (entityRif.getExcuteDocName() == null || entityRif.getExcuteDocName().equals("")) {
                            strDocName = this.getInpatientfeesDocName(entityRif, personList, YbReconsiderPriorityLevel.PERSON_TYPE_2);
                        } else {
                            strDocName = entityRif.getExcuteDocName();
                        }
                        strDeptId = entityRif.getExcuteDeptId();
                        strDeptName = entityRif.getExcuteDeptName();
                    } else if (configuremanage.getIntField() == YbReconsiderPriorityLevel.PERSON_TYPE_3) {
                        strDocId = entityRif.getFeeOperatorId();
                        if (entityRif.getFeeOperatorName() == null || entityRif.getFeeOperatorName().equals("")) {
                            strDocName = this.getInpatientfeesDocName(entityRif, personList, YbReconsiderPriorityLevel.PERSON_TYPE_3);
                        } else {
                            strDocName = entityRif.getFeeOperatorName();
                        }
                        strDeptId = entityRif.getFeeDeptId();
                        strDeptName = entityRif.getFeeDeptName();
                    }
                } else {
                    isKaiDan = true;
                }
            } else {
                isKaiDan = true;
            }
        } else {
            isKaiDan = true;
        }
        if (isKaiDan) {
            strDocId = entityRif.getOrderDocId();
            if (entityRif.getOrderDocName() == null || entityRif.getOrderDocName().equals("")) {
                strDocName = this.getInpatientfeesDocName(entityRif, personList, YbReconsiderPriorityLevel.PERSON_TYPE_1);
            } else {
                strDocName = entityRif.getOrderDocName();
            }
            strDeptId = entityRif.getDeptId();
            strDeptName = entityRif.getDeptName();
        }


        if (strDeptId != null && strDeptName != null && strDocId != null && strDocName != null) {
            ybReconsiderVerify.setVerifyDeptCode(strDeptId);
            ybReconsiderVerify.setVerifyDeptName(strDeptName);
            ybReconsiderVerify.setVerifyDoctorCode(strDocId);
            ybReconsiderVerify.setVerifyDoctorName(strDocName);
        } else {
            ybReconsiderVerify = null;
        }
        return ybReconsiderVerify;
    }

    private YbReconsiderVerify getReconsiderVerifyDeptImport(YbReconsiderVerify ybReconsiderVerify, List<YbReconsiderPriorityLevel> rlExcuteDeptList, List<YbPerson> personList, YbReconsiderInpatientfees entityRif) {
        String strDocId = "";
        String strDocName = "";
        String strDeptId = "";
        String strDeptName = "";
        List<YbReconsiderPriorityLevel> queryRlList = new ArrayList<>();
        YbReconsiderPriorityLevel entityRpl = new YbReconsiderPriorityLevel();
        String deptCode = entityRif.getExcuteDeptId();
        //科室
        if (deptCode != null) {
            queryRlList = rlExcuteDeptList.stream().filter(
                    s -> s.getDeptCode().equals(deptCode)
            ).collect(Collectors.toList());
        } else {
            queryRlList = new ArrayList<>();
        }
        if (queryRlList.size() > 0) {
            entityRpl = queryRlList.get(0);
            if (entityRpl.getPersonType().equals(YbReconsiderPriorityLevel.PERSON_TYPE_4)) {
                strDeptId = entityRpl.getDeptCode();
                strDeptName = entityRpl.getDeptName();
                strDocId = entityRpl.getDoctorCode();
                strDocName = entityRpl.getDoctorName();
            } else {
                if (entityRpl.getPersonType().equals(YbReconsiderPriorityLevel.PERSON_TYPE_1)) {
                    strDocId = entityRif.getOrderDocId();
                    if (entityRif.getOrderDocName() == null || entityRif.getOrderDocName().equals("")) {
                        strDocName = this.getInpatientfeesDocName(entityRif, personList, YbReconsiderPriorityLevel.PERSON_TYPE_1);
                    } else {
                        strDocName = entityRif.getOrderDocName();
                    }
                    if (entityRpl.getIsFixDept()) {
                        strDeptId = entityRpl.getDeptCode();
                        strDeptName = entityRpl.getDeptName();
                    } else {
                        strDeptId = entityRif.getDeptId();
                        strDeptName = entityRif.getDeptName();
                    }
                } else if (entityRpl.getPersonType().equals(YbReconsiderPriorityLevel.PERSON_TYPE_2)) {
                    strDocId = entityRif.getExcuteDocId();
                    if (entityRif.getExcuteDocName() == null || entityRif.getExcuteDocName().equals("")) {
                        strDocName = this.getInpatientfeesDocName(entityRif, personList, YbReconsiderPriorityLevel.PERSON_TYPE_2);
                    } else {
                        strDocName = entityRif.getExcuteDocName();
                    }
                    if (entityRpl.getIsFixDept()) {
                        strDeptId = entityRpl.getDeptCode();
                        strDeptName = entityRpl.getDeptName();
                    } else {
                        strDeptId = entityRif.getExcuteDeptId();
                        strDeptName = entityRif.getExcuteDeptName();
                    }
                } else if (entityRpl.getPersonType().equals(YbReconsiderPriorityLevel.PERSON_TYPE_3)) {
                    strDocId = entityRif.getFeeOperatorId();
                    if (entityRif.getFeeOperatorName() == null || entityRif.getFeeOperatorName().equals("")) {
                        strDocName = this.getInpatientfeesDocName(entityRif, personList, YbReconsiderPriorityLevel.PERSON_TYPE_3);
                    } else {
                        strDocName = entityRif.getFeeOperatorName();
                    }
                    if (entityRpl.getIsFixDept()) {
                        strDeptId = entityRpl.getDeptCode();
                        strDeptName = entityRpl.getDeptName();
                    } else {
                        strDeptId = entityRif.getFeeDeptId();
                        strDeptName = entityRif.getFeeDeptName();
                    }
                }
            }
            if (strDeptId != null && strDeptName != null && strDocId != null && strDocName != null) {
                ybReconsiderVerify.setVerifyDeptCode(strDeptId);
                ybReconsiderVerify.setVerifyDeptName(strDeptName);
                ybReconsiderVerify.setVerifyDoctorCode(strDocId);
                ybReconsiderVerify.setVerifyDoctorName(strDocName);
            } else {
                ybReconsiderVerify = null;
            }
        } else {
            ybReconsiderVerify = null;
        }
        return ybReconsiderVerify;
    }

    private YbReconsiderVerify getReconsiderVerifyImport(YbReconsiderVerify rv, YbReconsiderPriorityLevel rpl, YbReconsiderInpatientfees rif) {
        String strDocId = null;
        String strDocName = null;
        String strDeptId = null;
        String strDeptName = null;
        if (rpl.getDeptType() == YbReconsiderPriorityLevel.DEPT_TYPE_4) {
            strDeptId = rpl.getDeptCode();
            strDeptName = rpl.getDeptName();
        } else {
            if (rif != null) {
                if (rpl.getDeptType() == YbReconsiderPriorityLevel.DEPT_TYPE_1) {
                    if (rif.getDeptId() != null && rif.getDeptName() != null) {
                        strDeptId = rif.getDeptId();
                        strDeptName = rif.getDeptName();
                    }
                } else if (rpl.getDeptType() == YbReconsiderPriorityLevel.DEPT_TYPE_2) {
                    if (rif.getExcuteDeptId() != null && rif.getExcuteDeptName() != null) {
                        strDeptId = rif.getExcuteDeptId();
                        strDeptName = rif.getExcuteDeptName();
                    }
                } else if (rpl.getDeptType() == YbReconsiderPriorityLevel.DEPT_TYPE_3) {
                    if (rif.getFeeDeptId() != null && rif.getFeeDeptName() != null) {
                        strDeptId = rif.getFeeDeptId();
                        strDeptName = rif.getFeeDeptName();
                    }
                }
            }
        }
        if (rpl.getPersonType() == YbReconsiderPriorityLevel.PERSON_TYPE_4) {
            strDocId = rpl.getDoctorCode();
            strDocName = rpl.getDoctorName();
        } else {
            if (rif != null) {
                if (rpl.getPersonType() == YbReconsiderPriorityLevel.PERSON_TYPE_1) {
                    if (rif.getOrderDocId() != null && rif.getOrderDocName() != null) {
                        strDocId = rif.getOrderDocId();
                        strDocName = rif.getOrderDocName();
                    }
                } else if (rpl.getPersonType() == YbReconsiderPriorityLevel.PERSON_TYPE_2) {
                    if (rif.getExcuteDeptId() != null && rif.getExcuteDeptName() != null) {
                        strDocId = rif.getExcuteDocId();
                        strDocName = rif.getExcuteDocName();
                    }
                } else if (rpl.getPersonType() == YbReconsiderPriorityLevel.PERSON_TYPE_3) {
                    if (rif.getFeeOperatorId() != null && rif.getFeeOperatorName() != null) {
                        strDocId = rif.getFeeOperatorId();
                        strDocName = rif.getFeeOperatorName();
                    }
                }
            }
        }
        if (strDeptId != null && strDeptName != null && strDocId != null && strDocName != null) {
            rv.setVerifyDeptCode(strDeptId);
            rv.setVerifyDeptName(strDeptName);
            rv.setVerifyDoctorCode(strDocId);
            rv.setVerifyDoctorName(strDocName);
        } else {
            rv.setVerifyDeptCode(null);
            rv.setVerifyDeptName(null);
            rv.setVerifyDoctorCode(null);
            rv.setVerifyDoctorName(null);
        }
        return rv;
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
        } else if (type == 2) {
            strDocId = item.getExcuteDocId();
            strDocName = item.getExcuteDocName();
        } else if (type == 3) {
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
    public void insertMainReconsiderVerifyImports(String applyDateStr, Integer areaType, Long matchPersonId, String matchPersonName) {
//        this.baseMapper.insertMainReconsiderVerifyImport(applyDateStr, matchPersonId, matchPersonName);

        YbReconsiderApply ybReconsiderApply = this.iYbReconsiderApplyService.findReconsiderApplyByApplyDateStrs(applyDateStr, areaType);
        boolean isCreate = true;
        if (ybReconsiderApply != null) {
            int state = ybReconsiderApply.getState();
            if (state == YbDefaultValue.APPLYSTATE_2 || state == YbDefaultValue.APPLYSTATE_3 || state == YbDefaultValue.APPLYSTATE_4 || state == YbDefaultValue.APPLYSTATE_5) {
                int typeno = state == YbDefaultValue.APPLYSTATE_2 || state == YbDefaultValue.APPLYSTATE_3 ? YbDefaultValue.TYPENO_1 : YbDefaultValue.TYPENO_2;
                List<YbReconsiderApplyData> radList = iYbReconsiderApplyDataService.findReconsiderApplyDataByNotVerifys(ybReconsiderApply.getId(), ybReconsiderApply.getApplyDateStr(), areaType, YbDefaultValue.DATATYPE_1, typeno);

                if (radList.size() > 0) {
                    isCreate = false;
                    YbReconsiderInpatientfees waquery = new YbReconsiderInpatientfees();
                    waquery.setApplyDateStr(applyDateStr);
                    waquery.setAreaType(areaType);
                    waquery.setTypeno(typeno);
                    waquery.setDataType(YbDefaultValue.DATATYPE_1);
                    List<YbReconsiderInpatientfees> rifList = iYbReconsiderInpatientfeesService.findReconsiderInpatientfeesList(waquery);
                    List<YbDept> deptList = iYbDeptService.findDeptList(new YbDept(), 0);

                    List<YbDept> createDeptList = new ArrayList<>();
                    List<YbReconsiderInpatientfees> queryRifList = new ArrayList<>();
                    List<YbPerson> personList = new ArrayList<>();
                    personList = iYbPersonService.findPersonList(new YbPerson(), 0);
                    List<YbReconsiderVerify> createList = new ArrayList<>();
                    long count = 0;
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
//                            ybReconsiderVerify.setOperateDate(thisDate);//'操作日期'
//                            ybReconsiderVerify.setMatchDate(thisDate);//'匹配日期'
//                            ybReconsiderVerify.setMatchPersonId(matchPersonId);//'匹配人代码'
//                            ybReconsiderVerify.setMatchPersonName(matchPersonName);//'匹配人'
                            ybReconsiderVerify.setDataType(YbDefaultValue.DATATYPE_1);//扣款类型  0 明细扣款 1 主单扣款
                            ybReconsiderVerify.setState(YbDefaultValue.VERIFYSTATE_1);//1 待审核、2已审核、3已发送
                            ybReconsiderVerify.setAreaType(areaType);
                            ybReconsiderVerify.setIsDeletemark(1);
//                            ybReconsiderVerify.setCreateUserId(matchPersonId);
//                            ybReconsiderVerify.setCreateTime(thisDate);
                            String deptCode = ybReconsiderVerify.getVerifyDeptCode();
                            count = deptList.stream().filter(s -> s.getDeptId().equals(deptCode)).count();
                            if (count == 0) {
                                count = createDeptList.stream().filter(s -> s.getDeptId().equals(deptCode)).count();
                                if (count == 0) {
                                    YbDept create = new YbDept();
                                    create.setDeptId(ybReconsiderVerify.getVerifyDeptCode());
                                    create.setDeptName(ybReconsiderVerify.getVerifyDeptName());
                                    create.setCreateTime(new Date());
                                    create.setCreateUserId(matchPersonId);
                                    create.setState(1);
                                    create.setIsDeletemark(1);
                                    createDeptList.add(create);
                                }
                            }
                            createList.add(ybReconsiderVerify);
                        } else {
                            ybReconsiderVerify.setId(UUID.randomUUID().toString());
                            ybReconsiderVerify.setApplyDateStr(ybReconsiderApply.getApplyDateStr());
                            ybReconsiderVerify.setTypeno(item.getTypeno());
                            ybReconsiderVerify.setOrderNumber(item.getOrderNumber());
                            ybReconsiderVerify.setOrderNum(item.getOrderNum());
                            ybReconsiderVerify.setDataType(YbDefaultValue.DATATYPE_1);
                            ybReconsiderVerify.setState(YbDefaultValue.VERIFYSTATE_1);
                            ybReconsiderVerify.setAreaType(areaType);
                            ybReconsiderVerify.setIsDeletemark(1);
                            createList.add(ybReconsiderVerify);
                        }
                        isCreate = false;
                    }

                    isCreate = true;//判断状态是否更新
                    if (createList.size() > 0) {
                        this.saveBatch(createList);
                    }

                    if (createDeptList.size() > 0) {
                        iYbDeptService.saveBatch(createDeptList);
                    }
                }
            }
            if ((state == YbDefaultValue.APPLYSTATE_2 || state == YbDefaultValue.APPLYSTATE_4) && isCreate) {
                this.iYbReconsiderApplyService.updateReconsiderApplyState2345(ybReconsiderApply);
            }
        }

    }

    private boolean isImportTrue(String v1, String v2) {
        v1 = v1 == null ? "" : v1;
        v2 = v2 == null ? "" : v2;
        return v1.equals(v2);
    }

    @Override
    @Transactional
    public void importReconsiderDataVerifys(YbReconsiderApply reconsiderApply, int dataType, int typeno, List<YbReconsiderVerify> verifyList) {
        LambdaQueryWrapper<YbReconsiderVerify> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(YbReconsiderVerify::getApplyDateStr, reconsiderApply.getApplyDateStr());
        wrapper.eq(YbReconsiderVerify::getAreaType, reconsiderApply.getAreaType());
        wrapper.eq(YbReconsiderVerify::getDataType, dataType);
        wrapper.eq(YbReconsiderVerify::getTypeno, typeno);
//        wrapper.eq(YbReconsiderVerify::getIsDeletemark, 1);
        List<YbReconsiderVerify> list = this.list(wrapper);
        List<YbDept> deptList = iYbDeptService.findDeptList(new YbDept(), 0);

//        List<YbPerson> personList = iYbPersonService.findPersonList(new YbPerson(), 0);
//        List<YbPerson> queryPersonList = new ArrayList<>();

        List<YbDept> createDeptList = new ArrayList<>();
        List<YbReconsiderVerify> queryList = new ArrayList<>();
        List<YbReconsiderVerify> createList = new ArrayList<>();
        List<YbReconsiderVerify> updateList = new ArrayList<>();
        long count = 0;
        boolean isCreate = false;
        boolean isDept = false;
        if (list.size() > 0) {
            for (YbReconsiderVerify item : verifyList) {
                queryList = list.stream().filter(
                        s -> s.getApplyDataId().equals(item.getApplyDataId())
                ).collect(Collectors.toList());
                if (queryList.size() > 0) {
                    YbReconsiderVerify rv = queryList.get(0);
                    if (!isImportTrue(item.getVerifyDeptCode(), rv.getVerifyDeptCode()) || !isImportTrue(item.getVerifyDeptName(), rv.getVerifyDeptName()) ||
                            !isImportTrue(item.getVerifyDoctorCode(), rv.getVerifyDoctorCode()) || !isImportTrue(item.getVerifyDoctorName(), rv.getVerifyDoctorName()) ||
                            !isImportTrue(item.getOrderDeptCode(), rv.getOrderDeptCode()) || !isImportTrue(item.getOrderDeptName(), rv.getOrderDeptName()) ||
                            !isImportTrue(item.getOrderDoctorCode(), rv.getOrderDoctorCode()) || !isImportTrue(item.getOrderDoctorName(), rv.getOrderDoctorName())
                    ) {
                        isDept = true;
                        YbReconsiderVerify udpate = new YbReconsiderVerify();
                        udpate.setId(rv.getId());

                        udpate.setVerifyDeptCode(item.getVerifyDeptCode());
                        udpate.setVerifyDeptName(item.getVerifyDeptName());
                        udpate.setVerifyDoctorCode(item.getVerifyDoctorCode());
                        udpate.setVerifyDoctorName(item.getVerifyDoctorName());
//                        if(item.getVerifyDoctorCode()!=null) {
//                            queryPersonList = personList.stream().filter(s -> s.getPersonCode().equals()).collect(Collectors.toList());
//                            if(queryPersonList.size()>0){
//                                udpate.setVerifyDoctorName(queryPersonList.get(0).getPersonName());
//                            } else {
//                                udpate.setVerifyDoctorName(item.getVerifyDoctorName());
//                            }
//                        }

                        udpate.setOrderDeptCode(item.getOrderDeptCode());
                        udpate.setOrderDeptName(item.getOrderDeptName());
                        udpate.setOrderDoctorCode(item.getOrderDoctorCode());
                        udpate.setOrderDoctorName(item.getOrderDoctorName());

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
        int state = reconsiderApply.getState();
        if ((state == YbDefaultValue.APPLYSTATE_2 || state == YbDefaultValue.APPLYSTATE_4) && isCreate) {
            this.iYbReconsiderApplyService.updateReconsiderApplyState2345(reconsiderApply);
        }
    }

    private List<YbPerson> findPerson(List<YbReconsiderVerify> list) {
        List<YbPerson> personList = new ArrayList<>();
        ArrayList<String> personCodeList = new ArrayList<>();
        for (YbReconsiderVerify item : list) {
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
    public void updateSendStates(List<YbReconsiderVerify> list, Integer areaType, Integer dataType, Long uId, String Uname) {
        String applyDateStr = list.get(0).getApplyDateStr();
        YbReconsiderApply reconsiderApply = iYbReconsiderApplyService.findReconsiderApplyByApplyDateStrs(applyDateStr, areaType);
        if (reconsiderApply != null) {
            Date thisDate = new java.sql.Timestamp(new Date().getTime());
//        int day = iComConfiguremanageService.getConfigDay();
            int typeno = list.get(0).getTypeno();
            Date addDate = null;
            if (typeno == YbDefaultValue.TYPENO_1) {
                addDate = DataTypeHelpers.addDateMethod(reconsiderApply.getEnableDateOne(), 1);
            } else {
                addDate = DataTypeHelpers.addDateMethod(reconsiderApply.getEnableDateTwo(), 1);
            }
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
                qu.setSendType(ComSms.SENDTYPE_1);
                qu.setState(ComSms.STATE_0);
                smsList = iComSmsService.findLmdSmsList(qu);
                typeno = list.get(0).getTypeno();
                applyDateStr = list.get(0).getApplyDateStr();
                sendContent = this.iYbReconsiderApplyService.getSendMessage(applyDateStr, addDate, areaType, typeno, false);
            }
            for (YbReconsiderVerify ybReconsiderVerify : list) {
                if (ybReconsiderVerify.getVerifyDeptCode() != null && !ybReconsiderVerify.getVerifyDeptCode().equals("") &&
                        ybReconsiderVerify.getVerifyDeptName() != null && !ybReconsiderVerify.getVerifyDeptName().equals("") &&
                        ybReconsiderVerify.getVerifyDoctorCode() != null && !ybReconsiderVerify.getVerifyDoctorCode().equals("") &&
                        ybReconsiderVerify.getVerifyDoctorName() != null && !ybReconsiderVerify.getVerifyDoctorName().equals("")) {
                    queryPersonList = personList.stream().filter(
                            s -> s.getPersonCode().equals(ybReconsiderVerify.getVerifyDoctorCode())
                    ).collect(Collectors.toList());
                    if (queryPersonList.size() > 0) {
                        //更新
//                    ybReconsiderVerify.setState(YbDefaultValue.VERIFYSTATE_3);
//                    ybReconsiderVerify.setModifyUserId(uId);
//                    ybReconsiderVerify.setModifyTime(thisDate);
//                    ybReconsiderVerify.setSendPersonId(uId);
//                    ybReconsiderVerify.setSendPersonName(Uname);
//                    ybReconsiderVerify.setSendDate(thisDate);
//                    ybReconsiderVerify.setOperateDate(thisDate);

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
//                    ybAppealManage.setCreateUserId(uId);
//                    ybAppealManage.setCreateTime(thisDate);
                        ybAppealManage.setDataType(dataType);
                        ybAppealManage.setAreaType(areaType);

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
                                        comSms.setAreaType(areaType);
                                        comSms.setApplyDateStr(reconsiderApply.getApplyDateStr());
                                        comSms.setTypeno(typeno);
                                        iComSmsService.save(comSms);
                                    }
                                }
                            }
                        }
                        YbReconsiderVerify updateVerify = new YbReconsiderVerify();
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
    public void updateAllSendStates(String applyDateStr, Integer areaType, Integer state, Integer dataType, Long uId, String Uname) {
        Date thisDate = new java.sql.Timestamp(new Date().getTime());
        YbReconsiderApply reconsiderApply = iYbReconsiderApplyService.findReconsiderApplyByApplyDateStrs(applyDateStr, areaType);

        if (reconsiderApply != null) {
            int applyState = reconsiderApply.getState();
            int typeno = applyState == YbDefaultValue.APPLYSTATE_2 || applyState == YbDefaultValue.APPLYSTATE_3 ? YbDefaultValue.TYPENO_1 :
                    applyState == YbDefaultValue.APPLYSTATE_4 || applyState == YbDefaultValue.APPLYSTATE_5 ? YbDefaultValue.TYPENO_2 : 0;

            if (typeno == YbDefaultValue.TYPENO_1 || typeno == YbDefaultValue.TYPENO_2) {
//                int day = iComConfiguremanageService.getConfigDay();

                Date addDate = null;
                if (typeno == YbDefaultValue.TYPENO_1) {
                    addDate = DataTypeHelpers.addDateMethod(reconsiderApply.getEnableDateOne(), 1);
                } else {
                    addDate = DataTypeHelpers.addDateMethod(reconsiderApply.getEnableDateTwo(), 1);
                }
                List<YbPerson> personList = iYbPersonService.findPersonList(new YbPerson(), 0);
                List<YbPerson> queryPersonList = new ArrayList<>();
                List<ComSms> smsList = new ArrayList<>();
                List<String> userCodeList = new ArrayList<>();
                int nOpenSms = febsProperties.getOpenSms();
                List<YbReconsiderVerify> list = this.baseMapper.findReconsiderVerifyList(applyDateStr, areaType, dataType, state, typeno);

                String sendContent = "";
                boolean isOpenSms = nOpenSms == 1 ? true : false;
                if (isOpenSms && list.size() > 0) {
                    ComSms qu = new ComSms();
                    qu.setApplyDateStr(applyDateStr);
                    qu.setAreaType(areaType);
                    qu.setSendType(ComSms.SENDTYPE_1);
                    qu.setState(ComSms.STATE_0);
                    smsList = iComSmsService.findLmdSmsList(qu);

                    sendContent = this.iYbReconsiderApplyService.getSendMessage(applyDateStr, addDate, areaType, typeno, false);
                }
                for (YbReconsiderVerify ybReconsiderVerify : list) {
                    if (ybReconsiderVerify.getState() != YbDefaultValue.VERIFYSTATE_3) {
                        queryPersonList = personList.stream().filter(
                                s -> s.getPersonCode().equals(ybReconsiderVerify.getVerifyDoctorCode())
                        ).collect(Collectors.toList());

                        if (queryPersonList.size() > 0) {
                            //更新
//                            ybReconsiderVerify.setState(YbDefaultValue.VERIFYSTATE_3);
//                            ybReconsiderVerify.setModifyUserId(uId);
//                            ybReconsiderVerify.setModifyTime(thisDate);
//                            ybReconsiderVerify.setSendPersonId(uId);
//                            ybReconsiderVerify.setSendPersonName(Uname);
//                            ybReconsiderVerify.setSendDate(thisDate);
//                            ybReconsiderVerify.setOperateDate(thisDate);

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
//                            ybAppealManage.setCreateUserId(uId);
//                            ybAppealManage.setCreateTime(thisDate);
                            ybAppealManage.setDataType(dataType);
                            ybAppealManage.setAreaType(areaType);

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
                                        if (queryPersonList.get(0).getTel() != null && !queryPersonList.get(0).getTel().equals("")) {
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
                                                comSms.setAreaType(areaType);
                                                comSms.setApplyDateStr(reconsiderApply.getApplyDateStr());
                                                comSms.setTypeno(typeno);
                                                iComSmsService.save(comSms);
                                            }
                                        }
                                    }
                                }
                            }
                            YbReconsiderVerify updateVerify = new YbReconsiderVerify();
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
    }

    //单个，多个核对
    @Override
    @Transactional
    public void updateReviewerStates(List<YbReconsiderVerify> list, Long uId, String Uname) {
        List<YbPerson> personList = this.findPerson(list);
        for (YbReconsiderVerify item : list) {
            if ( item.getVerifyDeptCode() != null && !item.getVerifyDeptCode().equals("") &&
                    item.getVerifyDeptName() != null && !item.getVerifyDeptName().equals("") &&
                    item.getVerifyDoctorCode() != null && !item.getVerifyDoctorCode().equals("") &&
                    item.getVerifyDoctorName() != null && !item.getVerifyDoctorName().equals("")) {
                if (personList.stream().filter(s -> s.getPersonCode().equals(item.getVerifyDoctorCode())).count() > 0) {
                    String strVerifyDeptName = DataTypeHelpers.stringReplaceSetString(item.getVerifyDeptName(), item.getVerifyDeptCode() + "-");
                    item.setVerifyDeptName(strVerifyDeptName);
                    String strVerifyDoctorName = DataTypeHelpers.stringReplaceSetString(item.getVerifyDoctorName(), item.getVerifyDoctorCode() + "-");
                    item.setVerifyDoctorName(strVerifyDoctorName);

                    item.setState(YbDefaultValue.VERIFYSTATE_2);
//                    item.setModifyTime(thisDate);
//                    item.setModifyUserId(uId);
//                    item.setReviewerId(uId);
//                    item.setReviewerName(Uname);
//                    item.setReviewerDate(thisDate);
//                    item.setOperateDate(thisDate);
                    item.setAreaType(item.getAreaType());

                    if (item.getId() == null || item.getId().equals("")) {
                        item.setId(UUID.randomUUID().toString());
                        item.setIsDeletemark(1);
//                        item.setMatchPersonId(uId);
//                        item.setMatchPersonName(Uname);
//                        item.setMatchDate(thisDate);
//                        item.setCreateTime(thisDate);
//                        item.setCreateUserId(uId);
                        this.baseMapper.insert(item);
                    } else {
                        YbReconsiderVerify updateVerify = new YbReconsiderVerify();
                        updateVerify.setVerifyDoctorCode(item.getVerifyDoctorCode());
                        updateVerify.setVerifyDoctorName(strVerifyDoctorName);
                        updateVerify.setVerifyDeptCode(item.getVerifyDeptCode());
                        updateVerify.setVerifyDeptName(strVerifyDeptName);
                        updateVerify.setState(YbDefaultValue.VERIFYSTATE_2);
                        LambdaQueryWrapper<YbReconsiderVerify> queryWrapper = new LambdaQueryWrapper<>();
                        queryWrapper.eq(YbReconsiderVerify::getId, item.getId());
                        queryWrapper.eq(YbReconsiderVerify::getDataType, YbDefaultValue.DATATYPE_0);
                        queryWrapper.eq(YbReconsiderVerify::getState, YbDefaultValue.VERIFYSTATE_1);
                        this.baseMapper.update(updateVerify, queryWrapper);
                    }
                }
            }
        }
    }

    //全部核对
    @Override
    @Transactional
    public void updateAllReviewerStates(String applyDateStr, Integer areaType, int state, int dataType, Long uId, String Uname) {
        YbReconsiderApply reconsiderApply = iYbReconsiderApplyService.findReconsiderApplyByApplyDateStrs(applyDateStr, areaType);
        if (reconsiderApply != null) {
            int applyState = reconsiderApply.getState();
            int typeno = applyState == YbDefaultValue.APPLYSTATE_2 || applyState == YbDefaultValue.APPLYSTATE_3 ? YbDefaultValue.TYPENO_1 :
                    applyState == YbDefaultValue.APPLYSTATE_4 || applyState == YbDefaultValue.APPLYSTATE_5 ? YbDefaultValue.TYPENO_2 : 0;

            if (typeno == YbDefaultValue.TYPENO_1 || typeno == YbDefaultValue.TYPENO_2) {
                List<YbReconsiderVerify> list = this.baseMapper.findReconsiderVerifyList(applyDateStr, areaType, dataType, state, typeno);
                List<YbReconsiderVerify> updateList = new ArrayList<>();
                List<YbPerson> personList = this.iYbPersonService.findPersonList(new YbPerson(), 0);
                for (YbReconsiderVerify item : list) {
                    if (item.getVerifyDeptCode() != null && !item.getVerifyDeptCode().equals("") &&
                            item.getVerifyDeptName() != null && !item.getVerifyDeptName().equals("") &&
                            item.getVerifyDoctorCode() != null && !item.getVerifyDoctorCode().equals("") &&
                            item.getVerifyDoctorName() != null && !item.getVerifyDoctorName().equals("")) {
                        if (personList.stream().filter(s -> s.getPersonCode().equals(item.getVerifyDoctorCode())).count() > 0) {
                            YbReconsiderVerify update = new YbReconsiderVerify();
                            update.setId(item.getId());
                            update.setState(YbDefaultValue.VERIFYSTATE_2);
//                            update.setReviewerId(uId);
//                            update.setReviewerName(Uname);
//                            update.setReviewerDate(thisDate);
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

    //全部返回
    @Override
    @Transactional
    public void updateBackStates(String applyDateStr, Integer areaType, int state, int dataType) {
        YbReconsiderApply reconsiderApply = iYbReconsiderApplyService.findReconsiderApplyByApplyDateStrs(applyDateStr, areaType);
        if (reconsiderApply != null) {
            int applyState = reconsiderApply.getState();
            int typeno = applyState == YbDefaultValue.APPLYSTATE_2 || applyState == YbDefaultValue.APPLYSTATE_3 ? YbDefaultValue.TYPENO_1 :
                    applyState == YbDefaultValue.APPLYSTATE_4 || applyState == YbDefaultValue.APPLYSTATE_5 ? YbDefaultValue.TYPENO_2 : 0;

            if (typeno == YbDefaultValue.TYPENO_1 || typeno == YbDefaultValue.TYPENO_2) {
                List<YbReconsiderVerify> list = this.baseMapper.findReconsiderVerifyList(applyDateStr, areaType, dataType, state, typeno);
                List<YbReconsiderVerify> updateList = new ArrayList<>();
                for (YbReconsiderVerify item : list) {
                    YbReconsiderVerify update = new YbReconsiderVerify();
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


    @Override
    @Transactional
    public void updateReconsiderVerifyImports(List<YbReconsiderVerify> list, Long uId, String Uname) {
        for (YbReconsiderVerify item : list) {
//            Date thisDate = new Date();
            if (item.getVerifyDeptCode() != null && !item.getVerifyDeptCode().equals("") &&
                    item.getVerifyDeptName() != null && !item.getVerifyDeptName().equals("") &&
                    item.getVerifyDoctorCode() != null && !item.getVerifyDoctorCode().equals("") &&
                    item.getVerifyDoctorName() != null && !item.getVerifyDoctorName().equals("")) {
                item.setState(YbDefaultValue.VERIFYSTATE_1);
//                item.setModifyTime(thisDate);
//                item.setModifyUserId(uId);
//                item.setOperateDate(thisDate);

                String strVerifyDeptName = DataTypeHelpers.stringReplaceSetString(item.getVerifyDeptName(), item.getVerifyDeptCode() + "-");
                item.setVerifyDeptName(strVerifyDeptName);
                String strVerifyDoctorName = DataTypeHelpers.stringReplaceSetString(item.getVerifyDoctorName(), item.getVerifyDoctorCode() + "-");
                item.setVerifyDoctorName(strVerifyDoctorName);

                if (item.getId() == null || item.getId().equals("")) {
                    item.setId(UUID.randomUUID().toString());
                    item.setIsDeletemark(1);
//                    item.setMatchPersonId(uId);
//                    item.setMatchPersonName(Uname);
//                    item.setMatchDate(thisDate);
//                    item.setCreateTime(thisDate);
//                    item.setCreateUserId(uId);
                    this.baseMapper.insert(item);
                } else {
                    LambdaQueryWrapper<YbReconsiderVerify> queryWrapper = new LambdaQueryWrapper<>();
                    queryWrapper.eq(YbReconsiderVerify::getId, item.getId());
                    queryWrapper.eq(YbReconsiderVerify::getState, YbDefaultValue.VERIFYSTATE_1);
                    YbReconsiderVerify updateVerify = new YbReconsiderVerify();
                    updateVerify.setVerifyDoctorCode(item.getVerifyDoctorCode());
                    updateVerify.setVerifyDoctorName(item.getVerifyDoctorName());
                    updateVerify.setVerifyDeptCode(item.getVerifyDeptCode());
                    updateVerify.setVerifyDeptName(item.getVerifyDeptName());
                    this.baseMapper.update(updateVerify, queryWrapper);
                }
            }

        }
    }

    @Override
    @Transactional
    public String createEndJobState(String applyDateStr, Integer areaType, int[] jobTypeList) {
        String msg = "ok";
        YbReconsiderApply reconsiderApply = this.iYbReconsiderApplyService.findReconsiderApplyByApplyDateStrs(applyDateStr, areaType);
        if (reconsiderApply != null) {
            int typeno = 1;
            if (reconsiderApply.getState() == 2 || reconsiderApply.getState() == 3) {
                typeno = 1;
            } else if (reconsiderApply.getState() == 4 || reconsiderApply.getState() == 5) {
                typeno = 2;
            } else {
                return "";
            }
            String cron = "";
            Date thisDate = new Date();
            Date endDate = thisDate;
            Date enableDate = thisDate;
            String areaName = iComConfiguremanageService.getConfigAreaName(areaType);
            if (typeno == 1) {
                endDate = reconsiderApply.getEndDateOne();
                enableDate = DataTypeHelpers.addDateMethod(reconsiderApply.getEnableDateOne(), 1);
            } else {
                endDate = reconsiderApply.getEndDateTwo();
                enableDate = DataTypeHelpers.addDateMethod(reconsiderApply.getEnableDateTwo(), 1);
            }
            String bb = applyDateStr + " " + areaName + " " + (typeno == 1 ? "版本一" : "版本二");
            String remark = "";
            String methodName = "";
            String beanName = "";
            // Job state 0.正常    1.暂停
            //复议截止日期
            try {
                for (int type : jobTypeList) {
                    if (type == 1) {
                        remark = bb + " 复议截止日期";
                        methodName = typeno == 1 ? "oneDate" : "twoDate";
                        beanName = "appealManageTask";
                        cron = this.getCron(endDate, type, 5, areaType);
                    } else if (type == 2) {
                        remark = bb + " 确认截止日期";
                        beanName = "appealManageTask";
                        methodName = "enableDate";
                        cron = this.getCron(enableDate, 2, null, areaType);
                    } else if (type == 3) {
                        beanName = "smsTask";
                        remark = bb + " 提醒申诉";
                        methodName = "sendSmsWarnTask";
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
        // oneDate twoDate
        if (type == 1) {
            cron = "0 " + fen + " " + shi + " " + ri + " " + yue + " ? " + nian + "-" + nian;
        }
        //enableDate
        if (type == 2) {
            if (areaType == 0) {
                cron = "0 5 0 " + ri + " " + yue + " ? " + nian + "-" + nian;
            } else {
                cron = "0 5 1 " + ri + " " + yue + " ? " + nian + "-" + nian;
            }
        }
        //
        if (type == 3) {
            if (areaType == 0) {
                cron = "0 0/3 8 " + ri + " " + yue + " ? " + nian + "-" + nian;
            } else {
                cron = "0 0/3 9 " + ri + " " + yue + " ? " + nian + "-" + nian;
            }
        }
        return cron;
    }

}