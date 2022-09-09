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
import freemarker.template.utility.StringUtil;
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

    @Autowired
    IYbChsPriorityLevelService iYbChsPriorityLevelService;

    @Autowired
    IYbChsVerifyMsgService iYbChsVerifyMsgService;

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

    /**
     * 执行数据匹配
     *
     * @param applyDateStr
     * @param areaType
     * @param matchPersonId
     * @param matchPersonName
     */
    @Override
    @Transactional
    public void insertChsVerifyImports(String applyDateStr, Integer areaType, Long matchPersonId, String matchPersonName, List<YbChsVerifyMsg> backList) {
        YbChsApply ybChsApply = this.iYbChsApplyService.findChsApplyByApplyDateStrs(applyDateStr, areaType);
        boolean isCreate = true;
        if (ybChsApply != null) {
            int state = ybChsApply.getState();
            if (state == YbDefaultValue.APPLYSTATE_2 || state == YbDefaultValue.APPLYSTATE_3) {
                List<YbChsApplyData> radList = iYbChsApplyDataService.findChsApplyDataByNotVerifys(ybChsApply.getId(), ybChsApply.getApplyDateStr(), areaType);

                if (radList.size() > 0) {
                    radList.sort(Comparator.comparing(YbChsApplyData::getOrderNum));
                    LambdaQueryWrapper<YbChsJk> wrapper = new LambdaQueryWrapper<>();
                    wrapper.eq(YbChsJk::getApplyDateStr, applyDateStr);
                    wrapper.eq(YbChsJk::getAreaType, areaType);
                    List<YbChsJk> jkList = iYbChsJkService.list(wrapper);
                    List<YbChsJk> queryJkList = new ArrayList<>();

                    LambdaQueryWrapper<YbChsPriorityLevel> wrapperPl = new LambdaQueryWrapper<>();
                    wrapperPl.eq(YbChsPriorityLevel::getAreaType, areaType);
                    List<YbChsPriorityLevel> plList = iYbChsPriorityLevelService.list(wrapperPl);
                    // 规则一 门诊
                    List<YbChsPriorityLevel> plListMz1 = plList.stream().filter(s -> s.getState() == 1 && s.getZymzType() == 1).collect(Collectors.toList());
                    // 规则一 住院
                    List<YbChsPriorityLevel> plListZy1 = plList.stream().filter(s -> s.getState() == 1 && s.getZymzType() == 2).collect(Collectors.toList());
                    // 规则二 门诊
                    List<YbChsPriorityLevel> plListMz2 = plList.stream().filter(s -> s.getState() == 2 && s.getZymzType() == 1).collect(Collectors.toList());
                    // 规则二 住院
                    List<YbChsPriorityLevel> plListZy2 = plList.stream().filter(s -> s.getState() == 2 && s.getZymzType() == 2).collect(Collectors.toList());
                    List<YbChsPriorityLevel> plQuery = new ArrayList<>();
                    List<YbChsPriorityLevel> plQuery2 = new ArrayList<>();

                    List<YbDept> deptList = iYbDeptService.findDeptList(new YbDept(), 0);
                    List<YbDept> deptQueryList = new ArrayList<>();

                    List<YbChsVerify> createList = new ArrayList<>();
                    YbChsPriorityLevel cpl = new YbChsPriorityLevel();
                    YbChsPriorityLevel cpl2 = new YbChsPriorityLevel();

                    boolean isProjectCode = false;
                    int nid = 0;
                    for (YbChsApplyData item : radList) {
                        YbChsVerifyMsg back = new YbChsVerifyMsg();
                        back.setRuleName(item.getRuleName());
                        if (item.getIsOutpfees() == 1) {
                            back.setZymzName("门诊");
                        } else {
                            back.setZymzName("住院");
                        }
                        isProjectCode = false;
                        cpl = new YbChsPriorityLevel();
                        cpl2 = new YbChsPriorityLevel();
                        plQuery = new ArrayList<>();
                        plQuery2 = new ArrayList<>();
                        YbChsVerify ybChsVerify = new YbChsVerify();
                        ybChsVerify.setId(UUID.randomUUID().toString());
                        ybChsVerify.setApplyDataId(item.getId());
                        ybChsVerify.setApplyDateStr(ybChsApply.getApplyDateStr());
                        ybChsVerify.setOrderNum(item.getOrderNum());
                        ybChsVerify.setState(YbDefaultValue.VERIFYSTATE_1);
                        ybChsVerify.setAreaType(areaType);
                        ybChsVerify.setDataType(item.getDataType());

                        queryJkList = jkList.stream().filter(s -> s.getApplyDataId().equals(item.getId())).collect(Collectors.toList());
                        // 住院 明细扣款
//                        if (item.getDataType() == 0) {
                        if (queryJkList.size() > 0) {
                            back.setCurrencyField("是");
                            YbChsJk jk = queryJkList.get(0);
                            // 门诊
                            if (item.getIsOutpfees() == 1) {
                                // 项目 getItemName / 改 getHisName
                                if (item.getState() == 0) {
                                    // 规则一
                                    // 规则 + 项目
                                    plQuery = plListMz1.stream().filter(s -> s.getIsProject() == 1 && s.getIsRule() == 1 &&
                                            StringUtils.isNotBlank(s.getRuleName()) && s.getRuleName().equals(item.getRuleName()) &&
                                            StringUtils.isNotBlank(s.getProjectName()) && s.getProjectName().equals(jk.getHisName())
                                    ).collect(Collectors.toList());

                                    // 项目
                                    if (plQuery.size() == 0) {
                                        plQuery = plListMz1.stream().filter(s -> s.getIsProject() == 1 && s.getIsRule() == 2 &&
                                                StringUtils.isNotBlank(s.getProjectName()) && s.getProjectName().equals(jk.getHisName())
                                        ).collect(Collectors.toList());
                                    }
                                    // 规则二 科室
                                    // 规则 + 项目
                                    plQuery2 = plListMz2.stream().filter(s -> s.getIsProject() == 1 && s.getIsRule() == 1 &&
                                            StringUtils.isNotBlank(s.getRuleName()) && s.getRuleName().equals(item.getRuleName()) &&
                                            StringUtils.isNotBlank(s.getProjectName()) && s.getProjectName().equals(jk.getHisName())
                                    ).collect(Collectors.toList());

                                    // 项目
                                    if (plQuery2.size() == 0) {
                                        plQuery2 = plListMz2.stream().filter(s -> s.getIsProject() == 1 && s.getIsRule() == 2 &&
                                                StringUtils.isNotBlank(s.getProjectName()) && s.getProjectName().equals(jk.getHisName())
                                        ).collect(Collectors.toList());
                                    }
                                }
                                // 项目 getHisName / 改 getItemName
                                if (item.getState() == 1) {
                                    // 规则一
                                    // 规则 + 项目
                                    plQuery = plListMz1.stream().filter(s -> s.getIsProject() == 1 && s.getIsRule() == 1 &&
                                            StringUtils.isNotBlank(s.getRuleName()) && s.getRuleName().equals(item.getRuleName()) &&
                                            StringUtils.isNotBlank(s.getProjectName()) && s.getProjectName().equals(jk.getItemName())
                                    ).collect(Collectors.toList());

                                    // 项目
                                    if (plQuery.size() == 0) {
                                        plQuery = plListMz1.stream().filter(s -> s.getIsProject() == 1 && s.getIsRule() == 2 &&
                                                StringUtils.isNotBlank(s.getProjectName()) && s.getProjectName().equals(jk.getItemName())
                                        ).collect(Collectors.toList());
                                    }
                                    // 规则二 科室
                                    // 规则 + 项目
                                    plQuery2 = plListMz2.stream().filter(s -> s.getIsProject() == 1 && s.getIsRule() == 1 &&
                                            StringUtils.isNotBlank(s.getRuleName()) && s.getRuleName().equals(item.getRuleName()) &&
                                            StringUtils.isNotBlank(s.getProjectName()) && s.getProjectName().equals(jk.getItemName())
                                    ).collect(Collectors.toList());

                                    // 项目
                                    if (plQuery2.size() == 0) {
                                        plQuery2 = plListMz2.stream().filter(s -> s.getIsProject() == 1 && s.getIsRule() == 2 &&
                                                StringUtils.isNotBlank(s.getProjectName()) && s.getProjectName().equals(jk.getItemName())
                                        ).collect(Collectors.toList());
                                    }
                                }
                                if (item.getState() == 0 || item.getState() == 1) {
                                    if (plQuery.size() == 0) {
                                        plQuery = plListMz1.stream().filter(s -> s.getIsProject() == 2 && s.getIsRule() == 1 &&
                                                StringUtils.isNotBlank(s.getRuleName()) && s.getRuleName().equals(item.getRuleName())
                                        ).collect(Collectors.toList());
                                    }
                                    // 规则二 科室
                                    if (plQuery2.size() == 0) {
                                        plQuery2 = plListMz2.stream().filter(s -> s.getIsProject() == 2 && s.getIsRule() == 1 &&
                                                StringUtils.isNotBlank(s.getRuleName()) && s.getRuleName().equals(item.getRuleName())
                                        ).collect(Collectors.toList());
                                    }
                                }
                                // 项目编码匹配
                                if (item.getState() == 2) {
                                    // 规则 + 项目
                                    plQuery = plListMz1.stream().filter(s -> s.getIsProject() == 1 && s.getIsRule() == 1 &&
                                            StringUtils.isNotBlank(s.getRuleName()) && s.getRuleName().equals(item.getRuleName()) &&
                                            StringUtils.isNotBlank(s.getProjectName()) && s.getProjectName().equals(jk.getItemName())
                                    ).collect(Collectors.toList());
                                    // 规则二 科室 规则+项目
                                    plQuery2 = plListMz2.stream().filter(s -> s.getIsProject() == 1 && s.getIsRule() == 1 &&
                                            StringUtils.isNotBlank(s.getRuleName()) && s.getRuleName().equals(item.getRuleName()) &&
                                            StringUtils.isNotBlank(s.getProjectName()) && s.getProjectName().equals(jk.getItemName())
                                    ).collect(Collectors.toList());

                                    if (plQuery.size() == 0) {
                                        back.setProjectName(jk.getItemName());
                                        plQuery = plListMz1.stream().filter(s -> s.getIsProject() == 1 && s.getIsRule() == 1 &&
                                                StringUtils.isNotBlank(s.getRuleName()) && s.getRuleName().equals(item.getRuleName()) &&
                                                StringUtils.isNotBlank(s.getProjectName()) && s.getProjectName().equals(jk.getHisName())
                                        ).collect(Collectors.toList());
                                        // 规则二 科室 规则+项目
                                        plQuery2 = plListMz2.stream().filter(s -> s.getIsProject() == 1 && s.getIsRule() == 1 &&
                                                StringUtils.isNotBlank(s.getRuleName()) && s.getRuleName().equals(item.getRuleName()) &&
                                                StringUtils.isNotBlank(s.getProjectName()) && s.getProjectName().equals(jk.getHisName())
                                        ).collect(Collectors.toList());
                                    }
                                    if (plQuery.size() == 0) {
                                        back.setProjectName(jk.getHisName());
                                        plQuery = plListMz1.stream().filter(s -> s.getIsProject() == 1 && s.getIsRule() == 1 &&
                                                StringUtils.isNotBlank(s.getRuleName()) && s.getRuleName().equals(item.getRuleName()) &&
                                                StringUtils.isNotBlank(s.getProjectName()) && s.getProjectName().equals(item.getProjectName())
                                        ).collect(Collectors.toList());
                                        // 规则二 科室 规则+项目
                                        plQuery2 = plListMz2.stream().filter(s -> s.getIsProject() == 1 && s.getIsRule() == 1 &&
                                                StringUtils.isNotBlank(s.getRuleName()) && s.getRuleName().equals(item.getRuleName()) &&
                                                StringUtils.isNotBlank(s.getProjectName()) && s.getProjectName().equals(item.getProjectName())
                                        ).collect(Collectors.toList());
                                    }
                                    // 项目
                                    if (plQuery.size() == 0) {
                                        back.setProjectName(item.getProjectName());
                                        plQuery = plListMz1.stream().filter(s -> s.getIsProject() == 1 && s.getIsRule() == 2 &&
                                                StringUtils.isNotBlank(s.getProjectName()) && s.getProjectName().equals(jk.getItemName())
                                        ).collect(Collectors.toList());
                                        // 规则二 科室 项目
                                        plQuery2 = plListMz2.stream().filter(s -> s.getIsProject() == 1 && s.getIsRule() == 2 &&
                                                StringUtils.isNotBlank(s.getProjectName()) && s.getProjectName().equals(jk.getItemName())
                                        ).collect(Collectors.toList());

                                        if (plQuery.size() == 0) {
                                            back.setProjectName(jk.getItemName());
                                            plQuery = plListMz1.stream().filter(s -> s.getIsProject() == 1 && s.getIsRule() == 2 &&
                                                    StringUtils.isNotBlank(s.getProjectName()) && s.getProjectName().equals(jk.getHisName())
                                            ).collect(Collectors.toList());
                                            // 规则二 科室 项目
                                            plQuery2 = plListMz2.stream().filter(s -> s.getIsProject() == 1 && s.getIsRule() == 2 &&
                                                    StringUtils.isNotBlank(s.getProjectName()) && s.getProjectName().equals(jk.getHisName())
                                            ).collect(Collectors.toList());
                                        }

                                        if (plQuery.size() == 0) {
                                            back.setProjectName(jk.getHisName());
                                            plQuery = plListMz1.stream().filter(s -> s.getIsProject() == 1 && s.getIsRule() == 2 &&
                                                    StringUtils.isNotBlank(s.getProjectName()) && s.getProjectName().equals(item.getProjectName())
                                            ).collect(Collectors.toList());
                                            // 规则二 科室 项目
                                            plQuery2 = plListMz2.stream().filter(s -> s.getIsProject() == 1 && s.getIsRule() == 2 &&
                                                    StringUtils.isNotBlank(s.getProjectName()) && s.getProjectName().equals(item.getProjectName())
                                            ).collect(Collectors.toList());
                                        }
                                    } else {
                                        isProjectCode = true;
                                    }
                                }
                            } else {
                                // 住院
                                // 项目 getItemName / 改 getHisName
                                // 医保项目名称影响 itemName\hisName
                                if (item.getState() == 0) {
                                    // 规则一
                                    // 规则 + 项目
                                    plQuery = plListZy1.stream().filter(s -> s.getIsProject() == 1 && s.getIsRule() == 1 &&
                                            StringUtils.isNotBlank(s.getRuleName()) && s.getRuleName().equals(item.getRuleName()) &&
                                            StringUtils.isNotBlank(s.getProjectName()) && s.getProjectName().equals(jk.getHisName())
                                    ).collect(Collectors.toList());

                                    // 规则一
                                    // 项目
                                    if (plQuery.size() == 0) {
                                        plQuery = plListZy1.stream().filter(s -> s.getIsProject() == 1 && s.getIsRule() == 2 &&
                                                StringUtils.isNotBlank(s.getProjectName()) && s.getProjectName().equals(jk.getHisName())
                                        ).collect(Collectors.toList());
                                    }
                                    // 规则二 科室
                                    // 规则 + 项目
                                    plQuery2 = plListZy2.stream().filter(s -> s.getIsProject() == 1 && s.getIsRule() == 1 &&
                                            StringUtils.isNotBlank(s.getRuleName()) && s.getRuleName().equals(item.getRuleName()) &&
                                            StringUtils.isNotBlank(s.getProjectName()) && s.getProjectName().equals(jk.getHisName())
                                    ).collect(Collectors.toList());

                                    // 项目
                                    if (plQuery2.size() == 0) {
                                        plQuery2 = plListZy2.stream().filter(s -> s.getIsProject() == 1 && s.getIsRule() == 2 &&
                                                StringUtils.isNotBlank(s.getProjectName()) && s.getProjectName().equals(jk.getHisName())
                                        ).collect(Collectors.toList());
                                    }
                                }
                                // 项目 getHisName / 改 getItemName
                                if (item.getState() == 1) {
                                    // 规则一
                                    // 规则 + 项目
                                    plQuery = plListZy1.stream().filter(s -> s.getIsProject() == 1 && s.getIsRule() == 1 &&
                                            StringUtils.isNotBlank(s.getRuleName()) && s.getRuleName().equals(item.getRuleName()) &&
                                            StringUtils.isNotBlank(s.getProjectName()) && s.getProjectName().equals(jk.getItemName())
                                    ).collect(Collectors.toList());

                                    // 项目
                                    if (plQuery.size() == 0) {
                                        plQuery = plListZy1.stream().filter(s -> s.getIsProject() == 1 && s.getIsRule() == 2 &&
                                                StringUtils.isNotBlank(s.getProjectName()) && s.getProjectName().equals(jk.getItemName())
                                        ).collect(Collectors.toList());
                                    }
                                    // 规则二 科室
                                    // 规则 + 项目
                                    plQuery2 = plListZy2.stream().filter(s -> s.getIsProject() == 1 && s.getIsRule() == 1 &&
                                            StringUtils.isNotBlank(s.getRuleName()) && s.getRuleName().equals(item.getRuleName()) &&
                                            StringUtils.isNotBlank(s.getProjectName()) && s.getProjectName().equals(jk.getItemName())
                                    ).collect(Collectors.toList());

                                    // 项目
                                    if (plQuery2.size() == 0) {
                                        plQuery2 = plListZy2.stream().filter(s -> s.getIsProject() == 1 && s.getIsRule() == 2 &&
                                                StringUtils.isNotBlank(s.getProjectName()) && s.getProjectName().equals(jk.getItemName())
                                        ).collect(Collectors.toList());
                                    }
                                }
                                if (item.getState() == 0 || item.getState() == 1) {
                                    if (plQuery.size() == 0) {
                                        plQuery = plListZy1.stream().filter(s -> s.getIsProject() == 2 && s.getIsRule() == 1 &&
                                                StringUtils.isNotBlank(s.getRuleName()) && s.getRuleName().equals(item.getRuleName())
                                        ).collect(Collectors.toList());
                                    }
                                    // 规则二 科室
                                    if (plQuery2.size() == 0) {
                                        plQuery2 = plListZy2.stream().filter(s -> s.getIsProject() == 2 && s.getIsRule() == 1 &&
                                                StringUtils.isNotBlank(s.getRuleName()) && s.getRuleName().equals(item.getRuleName())
                                        ).collect(Collectors.toList());
                                    }
                                }
                                // 项目编码匹配
                                if (item.getState() == 2) {
                                    // 规则 + 项目

                                    plQuery = plListZy1.stream().filter(s -> s.getIsProject() == 1 && s.getIsRule() == 1 &&
                                            StringUtils.isNotBlank(s.getRuleName()) && s.getRuleName().equals(item.getRuleName()) &&
                                            StringUtils.isNotBlank(s.getProjectName()) && s.getProjectName().equals(jk.getItemName())
                                    ).collect(Collectors.toList());

                                    // 规则二 科室 规则+项目
                                    plQuery2 = plListZy2.stream().filter(s -> s.getIsProject() == 1 && s.getIsRule() == 1 &&
                                            StringUtils.isNotBlank(s.getRuleName()) && s.getRuleName().equals(item.getRuleName()) &&
                                            StringUtils.isNotBlank(s.getProjectName()) && s.getProjectName().equals(jk.getItemName())
                                    ).collect(Collectors.toList());

                                    if(plQuery.size() == 0) {
                                        back.setProjectName(jk.getItemName());
                                        plQuery = plListZy1.stream().filter(s -> s.getIsProject() == 1 && s.getIsRule() == 1 &&
                                                StringUtils.isNotBlank(s.getRuleName()) && s.getRuleName().equals(item.getRuleName()) &&
                                                StringUtils.isNotBlank(s.getProjectName()) && s.getProjectName().equals(jk.getHisName())
                                        ).collect(Collectors.toList());

                                        // 规则二 科室 规则+项目
                                        plQuery2 = plListZy2.stream().filter(s -> s.getIsProject() == 1 && s.getIsRule() == 1 &&
                                                StringUtils.isNotBlank(s.getRuleName()) && s.getRuleName().equals(item.getRuleName()) &&
                                                StringUtils.isNotBlank(s.getProjectName()) && s.getProjectName().equals(jk.getHisName())
                                        ).collect(Collectors.toList());
                                    }

                                    if(plQuery.size() == 0) {
                                        back.setProjectName(jk.getHisName());
                                        plQuery = plListZy1.stream().filter(s -> s.getIsProject() == 1 && s.getIsRule() == 1 &&
                                                StringUtils.isNotBlank(s.getRuleName()) && s.getRuleName().equals(item.getRuleName()) &&
                                                StringUtils.isNotBlank(s.getProjectName()) && s.getProjectName().equals(item.getProjectName())
                                        ).collect(Collectors.toList());

                                        // 规则二 科室 规则+项目
                                        plQuery2 = plListZy2.stream().filter(s -> s.getIsProject() == 1 && s.getIsRule() == 1 &&
                                                StringUtils.isNotBlank(s.getRuleName()) && s.getRuleName().equals(item.getRuleName()) &&
                                                StringUtils.isNotBlank(s.getProjectName()) && s.getProjectName().equals(item.getProjectName())
                                        ).collect(Collectors.toList());
                                    }

                                    // 规则一
                                    // 项目
                                    if (plQuery.size() == 0) {
                                        back.setProjectName(item.getProjectName());
                                        plQuery = plListZy1.stream().filter(s -> s.getIsProject() == 1 && s.getIsRule() == 2 &&
                                                StringUtils.isNotBlank(s.getProjectName()) && s.getProjectName().equals(jk.getItemName())
                                        ).collect(Collectors.toList());

                                        // 规则二 科室 项目
                                        plQuery2 = plListZy2.stream().filter(s -> s.getIsProject() == 1 && s.getIsRule() == 2 &&
                                                StringUtils.isNotBlank(s.getProjectName()) && s.getProjectName().equals(jk.getItemName())
                                        ).collect(Collectors.toList());

                                        if(plQuery.size() == 0) {
                                            back.setProjectName(jk.getItemName());
                                            plQuery = plListZy1.stream().filter(s -> s.getIsProject() == 1 && s.getIsRule() == 2 &&
                                                    StringUtils.isNotBlank(s.getProjectName()) && s.getProjectName().equals(jk.getHisName())
                                            ).collect(Collectors.toList());

                                            // 规则二 科室 项目
                                            plQuery2 = plListZy2.stream().filter(s -> s.getIsProject() == 1 && s.getIsRule() == 2 &&
                                                    StringUtils.isNotBlank(s.getProjectName()) && s.getProjectName().equals(jk.getHisName())
                                            ).collect(Collectors.toList());
                                        }

                                        if(plQuery.size() == 0) {
                                            back.setProjectName(jk.getHisName());
                                            plQuery = plListZy1.stream().filter(s -> s.getIsProject() == 1 && s.getIsRule() == 2 &&
                                                    StringUtils.isNotBlank(s.getProjectName()) && s.getProjectName().equals(item.getProjectName())
                                            ).collect(Collectors.toList());

                                            // 规则二 科室 项目
                                            plQuery2 = plListZy2.stream().filter(s -> s.getIsProject() == 1 && s.getIsRule() == 2 &&
                                                    StringUtils.isNotBlank(s.getProjectName()) && s.getProjectName().equals(item.getProjectName())
                                            ).collect(Collectors.toList());
                                        }
                                    } else {
                                        isProjectCode = true;
                                    }
                                }
                            }

                            if (plQuery.size() > 0) {
                                cpl = plQuery.get(0);
                                this.setDksAndDoctorValue(ybChsVerify, cpl, jk, deptList);
                            } else {
                                if (item.getState() == 0) {
                                    back.setProjectName(jk.getItemName());
                                }
                                if (item.getState() == 1) {
                                    back.setProjectName(jk.getHisName());
                                }
                                if (isProjectCode) {
                                    ybChsVerify.setVerifyDoctorCode(jk.getOrderDocId());
                                    ybChsVerify.setVerifyDoctorName(jk.getOrderDocName());
                                    deptQueryList = deptList.stream().filter(s -> s.getDeptId().equals(jk.getDeptId())).collect(Collectors.toList());
                                    if (deptQueryList.size() > 0) {
                                        ybChsVerify.setVerifyDksId(deptQueryList.get(0).getDksId() + "" + deptQueryList.get(0).getFyid());
                                        ybChsVerify.setVerifyDksName(deptQueryList.get(0).getDksName());
                                        ybChsVerify.setVerifyFyid(deptQueryList.get(0).getFyid());
                                    }
                                }
                            }

                            // 规则二 科室
                            if (StringUtils.isNotBlank(ybChsVerify.getVerifyDksId())) {
                                if (plQuery2.size() > 0) {
                                    plQuery2 = plQuery2.stream().filter(s -> s.getDksId().equals(ybChsVerify.getVerifyDksId())).collect(Collectors.toList());
                                } else {
                                    if (item.getIsOutpfees() == 1) {
                                        plQuery2 = plListMz2.stream().filter(s -> s.getIsProject() == 2 && s.getIsRule() == 2 &&
                                                s.getDksId().equals(ybChsVerify.getVerifyDksId())).collect(Collectors.toList());
                                    } else {
                                        plQuery2 = plListZy2.stream().filter(s -> s.getIsProject() == 2 && s.getIsRule() == 2 &&
                                                s.getDksId().equals(ybChsVerify.getVerifyDksId())).collect(Collectors.toList());
                                    }
                                }
                                if (plQuery2.size() > 0) {
                                    cpl2 = plQuery2.get(0);
                                    this.setDksAndDoctorValue(ybChsVerify, cpl2, jk, deptList);
                                }
                            }
                        } else {
                            back.setCurrencyField("否");
                            back.setProjectName(item.getProjectName());
                            // 无HIS项目
                            // 按照规则
                            if (item.getIsOutpfees() == 1) {
                                plQuery = plListMz1.stream().filter(s -> s.getIsProject() == 1 && s.getIsRule() == 1 &&
                                        StringUtils.isNotBlank(s.getProjectName()) && s.getProjectName().equals(item.getProjectName()) &&
                                        StringUtils.isNotBlank(s.getRuleName()) && s.getRuleName().equals(item.getRuleName())
                                ).collect(Collectors.toList());

                                if (plQuery.size() == 0) {
                                    plQuery = plListMz1.stream().filter(s -> s.getIsProject() == 1 && s.getIsRule() == 2 &&
                                            StringUtils.isNotBlank(s.getProjectName()) && s.getProjectName().equals(item.getProjectName())
                                    ).collect(Collectors.toList());
                                }

                                if (plQuery.size() == 0) {
                                    plQuery = plListMz1.stream().filter(s -> s.getIsProject() == 2 && s.getIsRule() == 1 &&
                                            StringUtils.isNotBlank(s.getRuleName()) && s.getRuleName().equals(item.getRuleName())
                                    ).collect(Collectors.toList());
                                }
                            } else {
                                plQuery = plListZy1.stream().filter(s -> s.getIsProject() == 1 && s.getIsRule() == 1 &&
                                        StringUtils.isNotBlank(s.getProjectName()) && s.getProjectName().equals(item.getProjectName()) &&
                                        StringUtils.isNotBlank(s.getRuleName()) && s.getRuleName().equals(item.getRuleName())
                                ).collect(Collectors.toList());

                                if (plQuery.size() == 0) {
                                    plQuery = plListZy1.stream().filter(s -> s.getIsProject() == 1 && s.getIsRule() == 2 &&
                                            StringUtils.isNotBlank(s.getProjectName()) && s.getProjectName().equals(item.getProjectName())
                                    ).collect(Collectors.toList());
                                }

                                if (plQuery.size() == 0) {
                                    plQuery = plListZy1.stream().filter(s -> s.getIsProject() == 2 && s.getIsRule() == 1 &&
                                            StringUtils.isNotBlank(s.getRuleName()) && s.getRuleName().equals(item.getRuleName())
                                    ).collect(Collectors.toList());
                                }
                            }

                            if (plQuery.size() > 0) {
                                cpl = plQuery.get(0);
                                // 汇总科室和医生都是固定值
                                if (cpl.getDeptType() == YbChsPriorityLevel.DEPT_TYPE_4 &&
                                        cpl.getPersonType() == YbChsPriorityLevel.PERSON_TYPE_4) {
                                    ybChsVerify.setVerifyDoctorCode(cpl.getDoctorCodeTo());
                                    ybChsVerify.setVerifyDoctorName(cpl.getDoctorNameTo());
                                    ybChsVerify.setVerifyDksId(cpl.getDksIdTo());
                                    ybChsVerify.setVerifyDksName(cpl.getDksNameTo());
                                    ybChsVerify.setVerifyFyid(cpl.getFyidTo());
                                }
                            }
                        }

                        if (StringUtils.isBlank(ybChsVerify.getVerifyDoctorCode()) ||
                                StringUtils.isBlank(ybChsVerify.getVerifyDksId())) {
                            String rp = back.getRuleName() + "-" + back.getProjectName();
                            long count = backList.stream().filter(s ->
                                    StringUtils.isNotBlank(s.getRuleProject()) && s.getRuleProject().equals(rp)
                            ).count();
                            if (count == 0) {
                                nid++;
                                back.setApplyDateStr(ybChsApply.getApplyDateStr());
                                back.setAreaType(ybChsApply.getAreaType());
                                back.setIds(UUID.randomUUID().toString());
                                back.setRuleProject(rp);
                                backList.add(back);
                            }
                        }
                        //region 主单
                        /*
                        }
                        else {
                            plQuery = plListZy1.stream().filter(s -> s.getIsProject() == 2 && s.getIsRule() == 1 &&
                                    StringUtils.isNotBlank(s.getRuleName()) && s.getRuleName().equals(item.getRuleName())
                            ).collect(Collectors.toList());
                            // 住院 主单扣款
                            if (queryJkList.size() > 0) {
                                back.setCurrencyField("是");
                                YbChsJk jk = queryJkList.get(0);
                                if (plQuery.size() > 0) {
                                    cpl = plQuery.get(0);
                                    this.setDksAndDoctorValue(ybChsVerify, cpl, jk, deptList);
                                }
                            } else {
                                back.setCurrencyField("否");
                                if (plQuery.size() > 0) {
                                    cpl = plQuery.get(0);
                                    if (cpl.getDeptType() == YbChsPriorityLevel.DEPT_TYPE_4 &&
                                            cpl.getPersonType() == YbChsPriorityLevel.PERSON_TYPE_4) {
                                        ybChsVerify.setVerifyDoctorCode(cpl.getDoctorCodeTo());
                                        ybChsVerify.setVerifyDoctorName(cpl.getDoctorNameTo());
                                        ybChsVerify.setVerifyDksId(cpl.getDksIdTo());
                                        ybChsVerify.setVerifyDksName(cpl.getDksNameTo());
                                        ybChsVerify.setVerifyFyid(cpl.getFyidTo());
                                    }
                                }
                            }

                            if (StringUtils.isBlank(ybChsVerify.getVerifyDoctorCode()) ||
                                    StringUtils.isBlank(ybChsVerify.getVerifyDksId())) {
                                String rp = back.getRuleName() + "-" + back.getProjectName();
                                long count = backList.stream().filter(s ->
                                        StringUtils.isNotBlank(s.getRuleProject()) && s.getRuleProject().equals(rp)
                                ).count();
                                if (count == 0) {
                                    nid++;
                                    back.setApplyDateStr(ybChsApply.getApplyDateStr());
                                    back.setAreaType(ybChsApply.getAreaType());
                                    back.setIds(UUID.randomUUID().toString());
                                    back.setRuleProject(rp);
                                    backList.add(back);
                                }
                            }
                        }
                         */
                        //endregion
                        createList.add(ybChsVerify);
                    }
                    LambdaQueryWrapper<YbChsVerifyMsg> vmWrapper = new LambdaQueryWrapper<>();
                    vmWrapper.eq(YbChsVerifyMsg::getAreaType, ybChsApply.getAreaType());
                    iYbChsVerifyMsgService.remove(vmWrapper);

                    isCreate = true;//判断状态是否更新
                    if (createList.size() > 0) {
                        this.saveBatch(createList);
                    }
                    if (backList.size() > 0) {
                        iYbChsVerifyMsgService.saveBatch(backList);
                    }
                }
            }
            if ((state == YbDefaultValue.APPLYSTATE_2) && isCreate) {
                this.iYbChsApplyService.updateChsApplyState3(ybChsApply);
            }
        }
    }

    private void setDksAndDoctorValue(YbChsVerify ybChsVerify, YbChsPriorityLevel cpl, YbChsJk jk, List<YbDept> deptList) {
        if (cpl.getDeptType() == YbChsPriorityLevel.DEPT_TYPE_4) {
            ybChsVerify.setVerifyDksId(cpl.getDksIdTo());
            ybChsVerify.setVerifyDksName(cpl.getDksNameTo());
            ybChsVerify.setVerifyFyid(cpl.getFyidTo());
        } else {
            List<YbDept> deptQueryList = new ArrayList<>();
            // 5主治科室 1开方科室、2执行科室、3计费科室
            if (cpl.getDeptType() == YbChsPriorityLevel.DEPT_TYPE_1 || cpl.getDeptType() == YbChsPriorityLevel.DEPT_TYPE_5) {
                deptQueryList = deptList.stream().filter(s -> s.getDeptId().equals(jk.getDeptId())).collect(Collectors.toList());
            }
            if (cpl.getDeptType() == YbChsPriorityLevel.DEPT_TYPE_2) {
                deptQueryList = deptList.stream().filter(s -> s.getDeptId().equals(jk.getExcuteDeptId())).collect(Collectors.toList());

            }
            if (cpl.getDeptType() == YbChsPriorityLevel.DEPT_TYPE_3) {
                deptQueryList = deptList.stream().filter(s -> s.getDeptId().equals(jk.getFeeDeptId())).collect(Collectors.toList());
            }
            if (deptQueryList.size() > 0) {
                ybChsVerify.setVerifyDksId(deptQueryList.get(0).getDksId() + "" + deptQueryList.get(0).getFyid());
                ybChsVerify.setVerifyDksName(deptQueryList.get(0).getDksName());
                ybChsVerify.setVerifyFyid(deptQueryList.get(0).getFyid());
            }
        }
        // 5主治医生 1开方医生、2执行医生、3计费人员
        if (cpl.getPersonType() == YbChsPriorityLevel.PERSON_TYPE_5) {
            ybChsVerify.setVerifyDoctorCode(jk.getAttendDocId());
            ybChsVerify.setVerifyDoctorName(jk.getAttendDocName());
        }
        if (cpl.getPersonType() == YbChsPriorityLevel.PERSON_TYPE_1) {
            ybChsVerify.setVerifyDoctorCode(jk.getOrderDocId());
            ybChsVerify.setVerifyDoctorName(jk.getOrderDocName());
        }
        if (cpl.getPersonType() == YbChsPriorityLevel.PERSON_TYPE_2) {
            ybChsVerify.setVerifyDoctorCode(jk.getExcuteDocId());
            ybChsVerify.setVerifyDoctorName(jk.getExcuteDocName());
        }
        if (cpl.getPersonType() == YbChsPriorityLevel.PERSON_TYPE_3) {
            ybChsVerify.setVerifyDoctorCode(jk.getFeeOperatorId());
            ybChsVerify.setVerifyDoctorName(jk.getFeeOperatorName());
        }
        if (cpl.getPersonType() == YbChsPriorityLevel.PERSON_TYPE_4) {
            ybChsVerify.setVerifyDoctorCode(cpl.getDoctorCodeTo());
            ybChsVerify.setVerifyDoctorName(cpl.getDoctorNameTo());
        }
    }

    /**
     * 批量更新数据
     *
     * @param list
     */
    @Override
    @Transactional
    public void updateChsVerifyImports(List<YbChsVerify> list) {
        for (YbChsVerify item : list) {
            if (StringUtils.isNotBlank(item.getVerifyDksId()) &&
                    StringUtils.isNotBlank(item.getVerifyDksName()) &&
                    StringUtils.isNotBlank(item.getVerifyDoctorCode()) &&
                    StringUtils.isNotBlank(item.getVerifyDoctorName()) &&
                    StringUtils.isNotBlank(item.getId())
            ) {
//                String strVerifyDksName = DataTypeHelpers.stringReplaceSetString(item.getVerifyDksName(), item.getVerifyDksId() + "-");
                item.setVerifyDksName(item.getVerifyDksName());
//                String strVerifyDoctorName = DataTypeHelpers.stringReplaceSetString(item.getVerifyDoctorName(), item.getVerifyDoctorCode() + "-");
                item.setVerifyDoctorName(item.getVerifyDoctorName());

                LambdaQueryWrapper<YbChsVerify> queryWrapper = new LambdaQueryWrapper<>();
                queryWrapper.eq(YbChsVerify::getId, item.getId());
                queryWrapper.eq(YbChsVerify::getState, YbDefaultValue.VERIFYSTATE_1);
                YbChsVerify updateVerify = new YbChsVerify();
                updateVerify.setVerifyDoctorCode(item.getVerifyDoctorCode());
                updateVerify.setVerifyDoctorName(item.getVerifyDoctorName());
                updateVerify.setVerifyDksId(item.getVerifyDksId());
                updateVerify.setVerifyDksName(item.getVerifyDksName());
                updateVerify.setVerifyFyid(item.getVerifyFyid());
                this.baseMapper.update(updateVerify, queryWrapper);

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
            int sendType21 = ComSms.SENDTYPE_21;
            if (isOpenSms && list.size() > 0) {
                ComSms qu = new ComSms();
                qu.setApplyDateStr(applyDateStr);
                qu.setAreaType(areaType);
                qu.setSendType(sendType21); // CHS 核对发送
                qu.setState(ComSms.STATE_0);
                smsList = iComSmsService.findLmdSmsList(qu);
                sendContent = this.iYbChsApplyService.getSendMessage(applyDateStr, addDate, areaType, false);

            }
            for (YbChsVerify ybChsVerify : list) {
                if (StringUtils.isNotBlank(ybChsVerify.getVerifyDksId()) &&
                        StringUtils.isNotBlank(ybChsVerify.getVerifyDksName()) &&
                        StringUtils.isNotBlank(ybChsVerify.getVerifyDoctorCode()) &&
                        StringUtils.isNotBlank(ybChsVerify.getVerifyDoctorName()) &&
                        StringUtils.isNotBlank(ybChsVerify.getId())
                ) {
                    queryPersonList = personList.stream().filter(
                            s -> s.getPersonCode().equals(ybChsVerify.getVerifyDoctorCode())
                    ).collect(Collectors.toList());
                    if (queryPersonList.size() > 0) {
//                        String strVerifyDksName = DataTypeHelpers.stringReplaceSetString(ybChsVerify.getVerifyDksName(), ybChsVerify.getVerifyDksId() + "-");
                        ybChsVerify.setVerifyDksName(ybChsVerify.getVerifyDksName());
//                        String strVerifyDoctorName = DataTypeHelpers.stringReplaceSetString(ybChsVerify.getVerifyDoctorName(), ybChsVerify.getVerifyDoctorCode() + "-");
                        ybChsVerify.setVerifyDoctorName(ybChsVerify.getVerifyDoctorName());

                        //插入申诉管理
                        YbChsManage ybChsManage = new YbChsManage();
                        ybChsManage.setVerifyId(ybChsVerify.getId());
                        ybChsManage.setApplyDataId(ybChsVerify.getApplyDataId());
                        ybChsManage.setReadyDksId(ybChsVerify.getVerifyDksId());
                        ybChsManage.setReadyDksName(ybChsVerify.getVerifyDksName());
                        ybChsManage.setReadyFyid(ybChsVerify.getVerifyFyid());
                        ybChsManage.setReadyDoctorCode(ybChsVerify.getVerifyDoctorCode());
                        ybChsManage.setReadyDoctorName(ybChsVerify.getVerifyDoctorName());

                        ybChsManage.setApplyDateStr(ybChsVerify.getApplyDateStr());
                        ybChsManage.setOrderNum(ybChsVerify.getOrderNum());

                        ybChsManage.setOperateDate(thisDate);
                        ybChsManage.setOperateProcess("发送操作-接受申请");
                        ybChsManage.setState(YbDefaultValue.ACCEPTSTATE_0); //接受申请
                        ybChsManage.setEnableDate(addDate);
                        ybChsManage.setAreaType(areaType);
                        ybChsManage.setDataType(ybChsVerify.getDataType());

                        iYbChsManageService.createYbChsManage(ybChsManage);
                        LambdaQueryWrapper<YbChsVerify> queryWrapper = new LambdaQueryWrapper<>();
                        queryWrapper.eq(YbChsVerify::getId, ybChsVerify.getId());
                        queryWrapper.eq(YbChsVerify::getState, YbDefaultValue.VERIFYSTATE_2); // 已核对

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
                                        comSms.setSendType(sendType21); // CHS 核对发送
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
                        updateVerify.setState(YbDefaultValue.VERIFYSTATE_3); // 已发送
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
            if (list.size() > 100) {
                personList = iYbPersonService.findPersonList(new YbPerson(), 0);
            } else {
                personList = this.findPerson(list);
            }
            String sendContent = "";
            boolean isOpenSms = nOpenSms == 1 ? true : false;
            int sendType21 = ComSms.SENDTYPE_21;
            if (isOpenSms && list.size() > 0) {
                ComSms qu = new ComSms();
                qu.setApplyDateStr(applyDateStr);
                qu.setAreaType(areaType);
                qu.setSendType(sendType21);// Chs核对发送
                qu.setState(ComSms.STATE_0);
                smsList = iComSmsService.findLmdSmsList(qu);
                sendContent = this.iYbChsApplyService.getSendMessage(applyDateStr, addDate, areaType, false);
            }
            for (YbChsVerify ybChsVerify : list) {
                // 不等于 已发送
                if (ybChsVerify.getState() != YbDefaultValue.VERIFYSTATE_3) {
                    queryPersonList = personList.stream().filter(
                            s -> s.getPersonCode().equals(ybChsVerify.getVerifyDoctorCode())
                    ).collect(Collectors.toList());

                    if (queryPersonList.size() > 0) {
//                        String strVerifyDksName = DataTypeHelpers.stringReplaceSetString(ybChsVerify.getVerifyDksName(), ybChsVerify.getVerifyDksId() + "-");
                        ybChsVerify.setVerifyDksName(ybChsVerify.getVerifyDksName());
//                        String strVerifyDoctorName = DataTypeHelpers.stringReplaceSetString(ybChsVerify.getVerifyDoctorName(), ybChsVerify.getVerifyDoctorCode() + "-");
                        ybChsVerify.setVerifyDoctorName(ybChsVerify.getVerifyDoctorName());

                        //插入申诉管理
                        YbChsManage ybChsManage = new YbChsManage();
                        ybChsManage.setVerifyId(ybChsVerify.getId());
                        ybChsManage.setApplyDataId(ybChsVerify.getApplyDataId());
                        ybChsManage.setReadyDksId(ybChsVerify.getVerifyDksId());
                        ybChsManage.setReadyDksName(ybChsVerify.getVerifyDksName());
                        ybChsManage.setReadyFyid(ybChsVerify.getVerifyFyid());
                        ybChsManage.setReadyDoctorCode(ybChsVerify.getVerifyDoctorCode());
                        ybChsManage.setReadyDoctorName(ybChsVerify.getVerifyDoctorName());

                        ybChsManage.setApplyDateStr(ybChsVerify.getApplyDateStr());
                        ybChsManage.setOrderNum(ybChsVerify.getOrderNum());

                        ybChsManage.setOperateDate(thisDate);
                        ybChsManage.setOperateProcess("发送操作-接受申请");
                        ybChsManage.setState(YbDefaultValue.ACCEPTSTATE_0);
                        ybChsManage.setEnableDate(addDate);
                        ybChsManage.setAreaType(areaType);
                        ybChsManage.setDataType(ybChsVerify.getDataType());

                        iYbChsManageService.createYbChsManage(ybChsManage);

                        LambdaQueryWrapper<YbChsVerify> queryWrapper = new LambdaQueryWrapper<>();
                        queryWrapper.eq(YbChsVerify::getId, ybChsVerify.getId());
                        queryWrapper.eq(YbChsVerify::getState, YbDefaultValue.VERIFYSTATE_2); // 已核对
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
                                            comSms.setSendType(sendType21); // Chs核对发送
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
                        updateVerify.setState(YbDefaultValue.VERIFYSTATE_3); // 已发送
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
                    StringUtils.isNotBlank(item.getVerifyDoctorName()) &&
                    StringUtils.isNotBlank(item.getId())
            ) {
                if (personList.stream().filter(s -> s.getPersonCode().equals(item.getVerifyDoctorCode())).count() > 0) {
//                    String strVerifyDksName = DataTypeHelpers.stringReplaceSetString(item.getVerifyDksName(), item.getVerifyDksId() + "-");
                    item.setVerifyDksName(item.getVerifyDksName());
//                    String strVerifyDoctorName = DataTypeHelpers.stringReplaceSetString(item.getVerifyDoctorName(), item.getVerifyDoctorCode() + "-");
                    item.setVerifyDoctorName(item.getVerifyDoctorName());

                    YbChsVerify updateVerify = new YbChsVerify();
                    updateVerify.setVerifyDoctorCode(item.getVerifyDoctorCode());
                    updateVerify.setVerifyDoctorName(item.getVerifyDoctorName());
                    updateVerify.setVerifyDksId(item.getVerifyDksId());
                    updateVerify.setVerifyDksName(item.getVerifyDksName());
                    updateVerify.setVerifyFyid(item.getVerifyFyid());
                    updateVerify.setState(YbDefaultValue.VERIFYSTATE_2);
                    LambdaQueryWrapper<YbChsVerify> queryWrapper = new LambdaQueryWrapper<>();
                    queryWrapper.eq(YbChsVerify::getId, item.getId());
                    queryWrapper.eq(YbChsVerify::getState, YbDefaultValue.VERIFYSTATE_1);
                    this.baseMapper.update(updateVerify, queryWrapper);

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
            if (list.size() > 100) {
                personList = iYbPersonService.findPersonList(new YbPerson(), 0);
            } else {
                personList = this.findPerson(list);
            }
            for (YbChsVerify item : list) {
                if (StringUtils.isNotBlank(item.getVerifyDksId()) &&
                        StringUtils.isNotBlank(item.getVerifyDksName()) &&
                        StringUtils.isNotBlank(item.getVerifyDoctorCode()) &&
                        StringUtils.isNotBlank(item.getVerifyDoctorName())
                ) {
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
                        if (rv.getState() == YbDefaultValue.VERIFYSTATE_1) {
                            YbChsVerify udpate = new YbChsVerify();
                            udpate.setId(rv.getId());
                            udpate.setVerifyDksId(item.getVerifyDksId());
                            udpate.setVerifyDksName(item.getVerifyDksName());
                            udpate.setVerifyFyid(item.getVerifyFyid());
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
    public void deleteChsVerifyState(YbChsVerify delVerify, YbChsApply ybChsApply) {
        delVerify.setState(1);
        LambdaQueryWrapper<YbChsVerify> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(YbChsVerify::getApplyDateStr, delVerify.getApplyDateStr());
        wrapper.eq(YbChsVerify::getAreaType, delVerify.getAreaType());
        wrapper.ne(YbChsVerify::getState, delVerify.getState());
        List<YbChsVerify> neChsVerifyState1 = this.list(wrapper);

        wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(YbChsVerify::getApplyDateStr, delVerify.getApplyDateStr());
        wrapper.eq(YbChsVerify::getAreaType, delVerify.getAreaType());
        wrapper.eq(YbChsVerify::getState, delVerify.getState());
        this.baseMapper.delete(wrapper);

        LambdaQueryWrapper<YbChsVerifyMsg> vmWrapper = new LambdaQueryWrapper<>();
        vmWrapper.eq(YbChsVerifyMsg::getAreaType, delVerify.getAreaType());
        iYbChsVerifyMsgService.remove(vmWrapper);

        if (neChsVerifyState1.size() == 0 && ybChsApply.getState() == YbDefaultValue.APPLYSTATE_3) {
            YbChsApply update = new YbChsApply();
            update.setId(ybChsApply.getId());
            update.setState(YbDefaultValue.APPLYSTATE_2);
            iYbChsApplyService.updateById(update);
        }
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

    // type == 1 截止日期, type == 2 执行时间 凌晨 0=3分、1=6分 确认日期, type == 3 执行时间 早上 0=6时、1=7时 提醒日期
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
                cron = "0 3 0 " + ri + " " + yue + " ? " + nian + "-" + nian;
            } else {
                cron = "0 6 0 " + ri + " " + yue + " ? " + nian + "-" + nian;
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
