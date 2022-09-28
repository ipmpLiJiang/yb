package cc.mrbird.febs.chs.service.impl;

import cc.mrbird.febs.com.controller.DataTypeHelpers;
import cc.mrbird.febs.common.domain.QueryRequest;
import cc.mrbird.febs.common.utils.SortUtil;
import cc.mrbird.febs.chs.dao.YbChsManageViewMapper;
import cc.mrbird.febs.chs.entity.*;
import cc.mrbird.febs.chs.service.*;
import cc.mrbird.febs.yb.service.IYbPersonService;
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

import java.util.ArrayList;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

/**
 * <p>
 * VIEW 服务实现类
 * </p>
 *
 * @author viki
 * @since 2020-07-30
 */
@Slf4j
@Service("IYbChsManageViewService")
@Transactional(propagation = Propagation.SUPPORTS, readOnly = true, rollbackFor = Exception.class)
public class YbChsManageViewServiceImpl extends ServiceImpl<YbChsManageViewMapper, YbChsManageView> implements IYbChsManageViewService {

    @Autowired
    IYbChsApplyService iYbChsApplyService;

    @Autowired
    IYbChsManageService iYbChsManageService;

    @Autowired
    IYbPersonService iYbPersonService;

    @Autowired
    IYbChsApplyDataService iYbChsApplyDataService;

    @Autowired
    IYbChsConfireDataService iYbChsConfireDataService;

    @Override
    public IPage<YbChsManageView> findYbChsManageLikeViews(QueryRequest request, YbChsManageView ybChsManageView, String keyField, boolean isConf) {
        try {
            String applyDateStr = ybChsManageView.getApplyDateStr();
            Integer areaType = ybChsManageView.getAreaType();
            YbChsApply chsApply = iYbChsApplyService.findChsApplyByApplyDateStrs(applyDateStr, areaType);
            Page<YbChsManageView> page = new Page<>();
            if (chsApply != null) {
                String value = ybChsManageView.getCurrencyField();
                Integer state = ybChsManageView.getState();
                List<String> strList = new ArrayList<>();
                List<YbChsManageView> list = new ArrayList<>();
                List<YbChsManage> manageList = new ArrayList<>();

                LambdaQueryWrapper<YbChsManage> queryWrapper = new LambdaQueryWrapper<>();
                queryWrapper.eq(YbChsManage::getApplyDateStr, applyDateStr);
                queryWrapper.eq(YbChsManage::getAreaType, areaType);
                queryWrapper.eq(YbChsManage::getState, state);
                if (StringUtils.isNotBlank(value) && keyField.equals("readyDoctorName")) {
                    strList = this.iYbPersonService.findPersonCodeList(value);
                    if (strList.size() > 0) {
                        if (strList.size() == 1) {
                            queryWrapper.eq(YbChsManage::getReadyDoctorCode, strList.get(0));
                        } else {
                            queryWrapper.in(YbChsManage::getReadyDoctorCode, strList);
                        }
                    }
                } else {
                    if (StringUtils.isNotBlank(value) && keyField.equals("readyDoctorCode")) {
                        queryWrapper.eq(YbChsManage::getReadyDoctorCode, value);
                    }
                    if (ybChsManageView.getReadyDoctorCode() != null && !isConf) {
                        queryWrapper.eq(YbChsManage::getReadyDoctorCode, ybChsManageView.getReadyDoctorCode());
                    }
                    if (StringUtils.isNotBlank(value) && keyField.equals("readyDksName")) {
                        queryWrapper.eq(YbChsManage::getReadyDksName, value);
                    }
                    if (StringUtils.isNotBlank(value) && keyField.equals("orderNum")) {
                        queryWrapper.eq(YbChsManage::getOrderNum, value);
                    }
                }

                if (ybChsManageView.getReadyDoctorCode() != null && isConf) {
                    List<YbChsConfireData> confireDataList = iYbChsConfireDataService.findChsConfireDataByInDoctorCodeList(ybChsManageView.getReadyDoctorCode(), areaType);
                    if (confireDataList.size() > 0) {
                        strList = new ArrayList<>();
                        for (YbChsConfireData item : confireDataList) {
                            if (!strList.contains(item.getDksId())) {
                                strList.add(item.getDksId());
                            }
                        }
                        if (strList.size() > 0) {
                            if (strList.size() == 1) {
                                queryWrapper.eq(YbChsManage::getReadyDksId, strList.get(0));
                            } else {
                                queryWrapper.in(YbChsManage::getReadyDksId, strList);
                            }
                        }
                    } else {
                        queryWrapper.eq(YbChsManage::getReadyDksId, "chs_dks");
                    }
                }

                manageList = iYbChsManageService.list(queryWrapper);
                List<YbChsApplyData> applyDataList = this.iYbChsApplyDataService.getApplyDataByKeyFieldList(chsApply.getId(), keyField, value);

                if (manageList.size() > 0 && applyDataList.size() > 0) {
                    if (manageList.size() > applyDataList.size()) {
                        List<YbChsManage> queryList = new ArrayList<>();
                        for (YbChsApplyData item : applyDataList) {
                            queryList = manageList.stream().filter(s -> s.getApplyDataId().equals(item.getId())).collect(Collectors.toList());
                            if (queryList.size() > 0) {
                                YbChsManageView amv = this.getYbChsManageView(queryList.get(0), item);
                                list.add(amv);
                            }
                        }
                    } else {
                        for (YbChsManage item : manageList) {
                            List<YbChsApplyData> queryList = new ArrayList<>();
                            queryList = applyDataList.stream().filter(s -> s.getId().equals(item.getApplyDataId())).collect(Collectors.toList());
                            if (queryList.size() > 0) {
                                YbChsManageView amv = this.getYbChsManageView(item, queryList.get(0));
                                list.add(amv);
                            }
                        }
                    }
                }
                if (list.size() > 0) {
                    page.setSearchCount(false);
                    SortUtil.handlePageSort(request, page, false);//true 是属性  false是数据库字段可两个
                    page.setTotal(list.size());
                    long current = page.getCurrent() == 1 ? 0 : (page.getCurrent() - 1) * page.getSize();
                    list = list.stream().sorted(Comparator.comparing(YbChsManageView::getOrderNum)).skip(current).limit(page.getSize()).collect(Collectors.toList());
                    Date thisDate = new Date();
                    for (YbChsManageView item : list) {
                        int nValue = item.getEnableDate().after(chsApply.getEndDate()) ? 0 : 1;
                        item.setApplyEndDate(chsApply.getEndDate());
                        item.setIsEnableDate(nValue);//计算
                        nValue = chsApply.getEndDate().after(thisDate) ? 0 : 1;
                        item.setIsEnd(nValue);//计算
                        nValue = item.getEnableDate().after(thisDate) ? 1 : 0;
                        item.setEnableType(nValue);
                        Date enableDate = item.getEnableDate();
                        //前面enableDate需要跟其他字段计算，所以最后赋值
                        if (chsApply.getEndDate().after(enableDate)) {
                            enableDate = DataTypeHelpers.addDateMethod(item.getEnableDate(), -1);
                        } else {
                            enableDate = chsApply.getEndDate();
                        }
                        item.setEnableDate(enableDate);//计算
                    }
                    page.setRecords(list);
                }
            }
            return page;

        } catch (Exception e) {
            log.error("获取字典信息失败", e);
            return null;
        }
    }

    @Override
    public IPage<YbChsManageView> findYbChsManageViews(QueryRequest request, YbChsManageView ybChsManageView) {
        try {
            YbChsApply chsApply = iYbChsApplyService.findChsApplyByApplyDateStrs(ybChsManageView.getApplyDateStr(), ybChsManageView.getAreaType());
            Page<YbChsManageView> page = new Page<>();
            if (chsApply != null) {
                ybChsManageView.setPid(chsApply.getId());
                int count = this.baseMapper.findChsManageCount(ybChsManageView);
                if (count > 0) {
                    page.setSearchCount(false);
                    SortUtil.handlePageSort(request, page, false);//true 是属性  false是数据库字段可两个
                    IPage<YbChsManageView> pg = this.baseMapper.findChsManageView(page, ybChsManageView);
                    pg.setTotal(count);
                    return pg;
                }
            }
            return page;

        } catch (Exception e) {
            log.error("获取字典信息失败", e);
            return null;
        }
    }


    private YbChsManageView getYbChsManageView(YbChsManage am, YbChsApplyData chsApplyData) {
        YbChsManageView amv = new YbChsManageView();
        amv.setPid(chsApplyData.getPid());
        amv.setAppealEndDate(chsApplyData.getAppealEndDate());//申诉截止日期
        amv.setPayPlaceType(chsApplyData.getPayPlaceType());//支付地点类别
        amv.setYdState(chsApplyData.getYdState());//疑点状态
        amv.setAreaName(chsApplyData.getAreaName());//医保区划
        amv.setYyjgCode(chsApplyData.getYyjgCode());//医药机构编码
        amv.setYyjgName(chsApplyData.getYyjgName());//医药机构名称
        amv.setDeptName(chsApplyData.getDeptName());//科室
        amv.setDoctorName(chsApplyData.getDoctorName());//医生
        amv.setMedicalType(chsApplyData.getMedicalType());//医疗类别
        amv.setZymzNumber(chsApplyData.getZymzNumber());//住院门诊号
        amv.setInsuredName(chsApplyData.getInsuredName());//参保人
        amv.setEnterHospitalDate(chsApplyData.getEnterHospitalDate());//入院日期
        amv.setOutHospitalDate(chsApplyData.getOutHospitalDate());//出院日期
        amv.setSettlementDate(chsApplyData.getSettlementDate());//结算日期
        amv.setCardNumber(chsApplyData.getCardNumber());//身份证号
        amv.setProjectCode(chsApplyData.getProjectCode());//医保项目编码
        amv.setProjectName(chsApplyData.getProjectName());//医院项目名称
        amv.setRuleName(chsApplyData.getRuleName());//规则名称
        amv.setViolateCsPrice(chsApplyData.getViolateCsPrice());//初审违规金额（元）
        amv.setViolatePrice(chsApplyData.getViolatePrice());//违规金额（元）
        amv.setViolateReason(chsApplyData.getViolateReason());//违规内容
        amv.setZdNote(chsApplyData.getZdNote());//诊断信息
        amv.setCostDate(chsApplyData.getCostDate());//费用日期
        amv.setInsuredType(chsApplyData.getInsuredType());//险种类型
        amv.setPrice(chsApplyData.getPrice());//单价（元）
        amv.setNum(chsApplyData.getNum());//数量
        amv.setMedicalPrice(chsApplyData.getMedicalPrice());//金额（元）
        amv.setTcPayPrice(chsApplyData.getTcPayPrice());//统筹支付（元）
        amv.setSpecs(chsApplyData.getSpecs());//规格
        amv.setJx(chsApplyData.getJx());//剂型
        amv.setJgLevel(chsApplyData.getJgLevel());//机构等级

        amv.setOrderNum(chsApplyData.getOrderNum());
        amv.setApplyDateStr(am.getApplyDateStr());
        amv.setId(am.getId());
        amv.setApplyDataId(chsApplyData.getId());
        amv.setVerifyId(am.getVerifyId());
        amv.setReadyDoctorCode(am.getReadyDoctorCode());
        amv.setReadyDoctorName(am.getReadyDoctorName());
        amv.setReadyDksId(am.getReadyDksId());
        amv.setReadyDksName(am.getReadyDksName());
        amv.setReadyFyid(am.getReadyFyid());
        amv.setInitDeptId(am.getInitDeptId());
        amv.setInitDeptName(am.getInitDeptName());
        amv.setChangeDoctorCode(am.getChangeDoctorCode());
        amv.setChangeDoctorName(am.getChangeDoctorName());
        amv.setChangeDksId(am.getChangeDksId());
        amv.setChangeDksName(am.getChangeDksName());
        amv.setChangeFyid(am.getChangeFyid());
        amv.setOperateReason(am.getOperateReason());
        amv.setOperateDate(am.getOperateDate());
        amv.setRefuseId(am.getRefuseId());
        amv.setRefuseName(am.getRefuseName());
        amv.setRefuseReason(am.getRefuseReason());
        amv.setRefuseDate(am.getRefuseDate());
        amv.setAdminPersonId(am.getAdminPersonId());
        amv.setAdminPersonName(am.getAdminPersonName());
        amv.setAdminChangeDate(am.getAdminChangeDate());
        amv.setAdminReason(am.getAdminReason());
        amv.setEnableDate(am.getEnableDate()); //分页时重新赋值，减一天
        amv.setState(am.getState());
        amv.setOperateProcess(am.getOperateProcess());
        amv.setApprovalState(am.getApprovalState());
//        amv.setIsEnd(); //分页时赋值
//        amv.setEnableType();//分页时赋值
        amv.setDataType(am.getDataType());
        amv.setAreaType(am.getAreaType());
        return amv;
    }


}
