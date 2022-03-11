package cc.mrbird.febs.yb.service.impl;

import cc.mrbird.febs.common.domain.QueryRequest;
import cc.mrbird.febs.common.utils.SortUtil;
import cc.mrbird.febs.yb.dao.YbReconsiderVerifyViewMapper;
import cc.mrbird.febs.yb.entity.*;
import cc.mrbird.febs.yb.manager.YbApplyDataManager;
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
 * findYbReconsiderVerifyViews
 * <p>
 * VIEW 服务实现类
 * </p>
 *
 * @author viki
 * @since 2020-07-30
 */
@Slf4j
@Service("IYbReconsiderVerifyViewService")
@Transactional(propagation = Propagation.SUPPORTS, readOnly = true, rollbackFor = Exception.class)
public class YbReconsiderVerifyViewServiceImpl extends ServiceImpl<YbReconsiderVerifyViewMapper, YbReconsiderVerifyView> implements IYbReconsiderVerifyViewService {

    @Autowired
    IYbReconsiderApplyService iYbReconsiderApplyService;

    @Autowired
    IYbReconsiderApplyDataService iYbReconsiderApplyDataService;

    @Autowired
    YbApplyDataManager ybApplyDataManager;

    @Autowired
    IYbPersonService iYbPersonService;

    @Autowired
    IYbReconsiderVerifyService iYbReconsiderVerifyService;

    @Override
    public IPage<YbReconsiderVerifyView> findYbReconsiderVerifyViews(QueryRequest request, YbReconsiderVerifyView ybReconsiderVerifyView) {
        try {
            Page<YbReconsiderVerifyView> page = new Page<>();
            YbReconsiderApply reconsiderApply = iYbReconsiderApplyService.findReconsiderApplyByApplyDateStrs(ybReconsiderVerifyView.getApplyDateStr(), ybReconsiderVerifyView.getAreaType());
            if (reconsiderApply != null) {
                Integer typeno = ybReconsiderVerifyView.getTypeno();
                if (typeno == null) {
                    int applyState = reconsiderApply.getState();
                    typeno = applyState == YbDefaultValue.APPLYSTATE_2 || applyState == YbDefaultValue.APPLYSTATE_3 ? YbDefaultValue.TYPENO_1 :
                            applyState == YbDefaultValue.APPLYSTATE_4 || applyState == YbDefaultValue.APPLYSTATE_5 ? YbDefaultValue.TYPENO_2 : 0;
                }
                ybReconsiderVerifyView.setPid(reconsiderApply.getId());
                if(typeno != 0) {
                    ybReconsiderVerifyView.setTypeno(typeno);
                }

                int count = this.baseMapper.findReconsiderVerifyCount(ybReconsiderVerifyView);
                if (count > 0) {
                    page.setSearchCount(false);
                    SortUtil.handlePageSort(request, page, false);//true 是属性  false是数据库字段可两个
                    IPage<YbReconsiderVerifyView> pg = this.baseMapper.findReconsiderVerifyView(page, ybReconsiderVerifyView);
                    pg.setTotal(count);
                    return pg;
                } else {
                    return page;
                }
            } else {
                return page;
            }
        } catch (Exception e) {
            log.error("获取字典信息失败", e);
            return null;
        }
    }

    @Override
    public List<YbReconsiderVerifyView> findReconsiderVerifyViewLists(YbReconsiderVerifyView ybReconsiderVerifyView) {
        return this.baseMapper.findReconsiderVerifyViewList(ybReconsiderVerifyView);
    }
    /*
    @Override
    public IPage<YbReconsiderVerifyView> findYbReconsiderVerifyViews(QueryRequest request, YbReconsiderVerifyView ybReconsiderVerifyView, String[] searchType) {
        try {
            Page<YbReconsiderVerifyView> page = new Page<>();
            YbReconsiderApply reconsiderApply = iYbReconsiderApplyService.findReconsiderApplyByApplyDateStrs(ybReconsiderVerifyView.getApplyDateStr(), ybReconsiderVerifyView.getAreaType());
            if (reconsiderApply != null) {
                Integer typeno = ybReconsiderVerifyView.getTypeno();
                if (typeno == null) {
                    int applyState = reconsiderApply.getState();
                    typeno = applyState == YbDefaultValue.APPLYSTATE_2 || applyState == YbDefaultValue.APPLYSTATE_3 ? YbDefaultValue.TYPENO_1 :
                            applyState == YbDefaultValue.APPLYSTATE_4 || applyState == YbDefaultValue.APPLYSTATE_5 ? YbDefaultValue.TYPENO_2 : 0;
                }
                ybReconsiderVerifyView.setPid(reconsiderApply.getId());
                ybReconsiderVerifyView.setTypeno(typeno);
                int count = this.baseMapper.findReconsiderVerifyCount(ybReconsiderVerifyView);
                if (count > 0) {
                    page.setSearchCount(false);
                    SortUtil.handlePageSort(request, page, false);//true 是属性  false是数据库字段可两个
                    IPage<YbReconsiderVerifyView> pg = this.baseMapper.findReconsiderVerifyView(page, ybReconsiderVerifyView);
                    pg.setTotal(count);
                    return pg;
                } else {
                    return page;
                }
            } else {
                return page;
            }
        } catch (Exception e) {
            log.error("获取字典信息失败", e);
            return null;
        }
    }
    */
    @Override
    public IPage<YbReconsiderVerifyView> findReconsiderVerifyViewNew(QueryRequest request, YbReconsiderVerifyView ybReconsiderVerifyView) {
        try {
            String applyDateStr = ybReconsiderVerifyView.getApplyDateStr();
            Integer areaType = ybReconsiderVerifyView.getAreaType();
            YbReconsiderApply reconsiderApply = iYbReconsiderApplyService.findReconsiderApplyByApplyDateStrs(applyDateStr, areaType);
            Page<YbReconsiderVerifyView> page = new Page<>();
            if (reconsiderApply != null) {
                Integer typeno = ybReconsiderVerifyView.getTypeno();
                if (typeno == null) {
                    int applyState = reconsiderApply.getState();
                    typeno = applyState == YbDefaultValue.APPLYSTATE_2 || applyState == YbDefaultValue.APPLYSTATE_3 ? YbDefaultValue.TYPENO_1 :
                            applyState == YbDefaultValue.APPLYSTATE_4 || applyState == YbDefaultValue.APPLYSTATE_5 ? YbDefaultValue.TYPENO_2 : 0;
                }
                Integer dataType = ybReconsiderVerifyView.getDataType();
                List<YbReconsiderApplyData> applyDataList = ybApplyDataManager.getApplyDatas(reconsiderApply.getId(), applyDateStr + "-" + areaType);
                List<YbReconsiderVerifyView> list = new ArrayList<>();
                List<YbReconsiderVerify> verifyList = new ArrayList<>();

                LambdaQueryWrapper<YbReconsiderVerify> queryWrapper = new LambdaQueryWrapper<>();
                queryWrapper.eq(YbReconsiderVerify::getApplyDateStr, applyDateStr);
                queryWrapper.eq(YbReconsiderVerify::getAreaType, areaType);

                if (typeno != null) {
                    queryWrapper.eq(YbReconsiderVerify::getTypeno, typeno);
                }
                if (dataType != null) {
                    queryWrapper.eq(YbReconsiderVerify::getDataType, dataType);
                }
                if (ybReconsiderVerifyView.getState() != null) {
                    queryWrapper.eq(YbReconsiderVerify::getState, ybReconsiderVerifyView.getState());
                }
                if (ybReconsiderVerifyView.getOrderNumber() != null) {
                    queryWrapper.eq(YbReconsiderVerify::getOrderNumber, ybReconsiderVerifyView.getOrderNumber());
                }
                if (ybReconsiderVerifyView.getVerifyDoctorCode() != null) {
                    queryWrapper.eq(YbReconsiderVerify::getVerifyDoctorCode, ybReconsiderVerifyView.getVerifyDoctorCode());
                }
                if (ybReconsiderVerifyView.getVerifyDoctorName() != null) {
                    queryWrapper.eq(YbReconsiderVerify::getVerifyDoctorName, ybReconsiderVerifyView.getVerifyDoctorName());
                }
                if (ybReconsiderVerifyView.getVerifyDeptName() != null) {
                    queryWrapper.eq(YbReconsiderVerify::getVerifyDeptName, ybReconsiderVerifyView.getVerifyDeptName());
                }
                verifyList = iYbReconsiderVerifyService.list(queryWrapper);

                applyDataList = this.getApplyDataListView(ybReconsiderVerifyView, applyDataList, typeno, dataType);

                if (verifyList.size() > 0 && applyDataList.size() > 0) {
                    if (verifyList.size() > applyDataList.size()) {
                        List<YbReconsiderVerify> queryList = new ArrayList<>();
                        for (YbReconsiderApplyData item : applyDataList) {
                            queryList = verifyList.stream().filter(s -> s.getApplyDataId().equals(item.getId())).collect(Collectors.toList());
                            if (queryList.size() > 0) {
                                YbReconsiderVerifyView arv = this.getYbReconsiderVerifyView(queryList.get(0), item);
                                list.add(arv);
                            }
                        }
                    } else {
                        for (YbReconsiderVerify item : verifyList) {
                            List<YbReconsiderApplyData> queryList = new ArrayList<>();
                            queryList = applyDataList.stream().filter(s -> s.getId().equals(item.getApplyDataId())).collect(Collectors.toList());
                            if (queryList.size() > 0) {
                                YbReconsiderVerifyView arv = this.getYbReconsiderVerifyView(item, queryList.get(0));
                                list.add(arv);
                            }
                        }
                    }
                }
                if (list.size() > 0) {
                    page.setSearchCount(false);
                    SortUtil.handlePageSort(request, page, false);//true 是属性  false是数据库字段可两个
                    page.setTotal(list.size());
                    long current = page.getCurrent() == 1 ? 0 : (page.getCurrent() - 1) * page.getSize();
                    list = list.stream().sorted(Comparator.comparing(YbReconsiderVerifyView::getOrderNum)).skip(current).limit(page.getSize()).collect(Collectors.toList());
                    page.setRecords(list);
                }
            }
            return page;

        } catch (Exception e) {
            log.error("获取字典信息失败", e);
            return null;
        }
    }

    public List<YbReconsiderApplyData> getApplyDataListView(YbReconsiderVerifyView ybReconsiderVerifyView, List<YbReconsiderApplyData> applyDataList, Integer typeno, Integer dataType) {
        String s = ybReconsiderVerifyView.getSerialNo();
        String c = ybReconsiderVerifyView.getProjectCode();
        String p = ybReconsiderVerifyView.getProjectName();
        String r = ybReconsiderVerifyView.getRuleName();
        String o = ybReconsiderVerifyView.getOrderNumber();
        //1 scpro
        if (s != null && c == null && p == null && r == null && o == null) {
            applyDataList = applyDataList.stream().filter(z ->
                    z.getSerialNo().equals(s) && z.getTypeno().equals(typeno) && z.getDataType().equals(dataType)).collect(Collectors.toList());
        }
        //2 scpro
        if (s == null && c != null && p == null && r == null && o == null) {
            applyDataList = applyDataList.stream().filter(z ->
                    z.getProjectCode() != null && z.getProjectCode().equals(c) &&
                            z.getTypeno().equals(typeno) && z.getDataType().equals(dataType)).collect(Collectors.toList());
        }
        //3 scpro
        if (s == null && c == null && p != null && r == null && o == null) {
            applyDataList = applyDataList.stream().filter(z ->
                    z.getProjectName() != null && z.getProjectName().equals(p) &&
                            z.getTypeno().equals(typeno) && z.getDataType().equals(dataType)).collect(Collectors.toList());
        }
        //4 scpro
        if (s == null && c == null && p == null && r != null && o == null) {
            applyDataList = applyDataList.stream().filter(z ->
                    z.getRuleName().equals(r) && z.getTypeno().equals(typeno) && z.getDataType().equals(dataType)).collect(Collectors.toList());
        }
        //5 scpro
        if (s == null && c == null && p == null && r == null && o != null) {
            applyDataList = applyDataList.stream().filter(z ->
                    z.getOrderNumber().equals(o) && z.getTypeno().equals(typeno) && z.getDataType().equals(dataType)).collect(Collectors.toList());
        }
        //12 scpro
        if (s != null && c != null && p == null && r == null && o == null) {
            applyDataList = applyDataList.stream().filter(z ->
                    z.getSerialNo().equals(s) && z.getProjectCode() != null && z.getProjectCode().equals(c) &&
                            z.getTypeno().equals(typeno) && z.getDataType().equals(dataType)).collect(Collectors.toList());
        }
        //13 scpro
        if (s != null && c == null && p != null && r == null && o == null) {
            applyDataList = applyDataList.stream().filter(z ->
                    z.getSerialNo().equals(s) && z.getProjectName() != null && z.getProjectName().equals(p) &&
                            z.getTypeno().equals(typeno) && z.getDataType().equals(dataType)).collect(Collectors.toList());
        }
        //14 scpro
        if (s != null && c == null && p == null && r != null && o == null) {
            applyDataList = applyDataList.stream().filter(z ->
                    z.getSerialNo().equals(s) && z.getRuleName().equals(r) &&
                            z.getTypeno().equals(typeno) && z.getDataType().equals(dataType)).collect(Collectors.toList());
        }
        //15 scpro
        if (s != null && c == null && p == null && r == null && o != null) {
            applyDataList = applyDataList.stream().filter(z ->
                    z.getSerialNo().equals(s) && z.getOrderNumber().equals(o) &&
                            z.getTypeno().equals(typeno) && z.getDataType().equals(dataType)).collect(Collectors.toList());
        }
        //23 scpro
        if (s == null && c != null && p != null && r == null && o == null) {
            applyDataList = applyDataList.stream().filter(z ->
                    z.getProjectCode() != null && z.getProjectCode().equals(c) &&
                            z.getProjectName() != null && z.getProjectName().equals(p) &&
                            z.getTypeno().equals(typeno) && z.getDataType().equals(dataType)).collect(Collectors.toList());
        }
        //24 scpro
        if (s == null && c != null && p == null && r != null && o == null) {
            applyDataList = applyDataList.stream().filter(z ->
                    z.getProjectCode() != null && z.getProjectCode().equals(c) && z.getRuleName().equals(r) &&
                            z.getTypeno().equals(typeno) && z.getDataType().equals(dataType)).collect(Collectors.toList());
        }
        //25 scpro
        if (s == null && c != null && p == null && r == null && o != null) {
            applyDataList = applyDataList.stream().filter(z ->
                    z.getProjectCode() != null && z.getProjectCode().equals(c) && z.getOrderNumber().equals(o) &&
                            z.getTypeno().equals(typeno) && z.getDataType().equals(dataType)).collect(Collectors.toList());
        }
        //34 scpro
        if (s == null && c == null && p != null && r != null && o == null) {
            applyDataList = applyDataList.stream().filter(z ->
                    z.getProjectName() != null && z.getProjectName().equals(p) && z.getRuleName().equals(r) &&
                            z.getTypeno().equals(typeno) && z.getDataType().equals(dataType)).collect(Collectors.toList());
        }
        //35 scpro
        if (s == null && c == null && p != null && r == null && o != null) {
            applyDataList = applyDataList.stream().filter(z ->
                    z.getProjectName() != null && z.getProjectName().equals(p) && z.getOrderNumber().equals(o) &&
                            z.getTypeno().equals(typeno) && z.getDataType().equals(dataType)).collect(Collectors.toList());
        }
        //45 scpro
        if (s == null && c == null && p == null && r != null && o != null) {
            applyDataList = applyDataList.stream().filter(z ->
                    z.getRuleName().equals(r) && z.getOrderNumber().equals(o) &&
                            z.getTypeno().equals(typeno) && z.getDataType().equals(dataType)).collect(Collectors.toList());
        }
        //123 scpro
        if (s != null && c != null && p != null && r == null && o == null) {
            applyDataList = applyDataList.stream().filter(z ->
                    z.getSerialNo().equals(s) && z.getProjectCode() != null && z.getProjectCode().equals(c) &&
                            z.getProjectName() != null && z.getProjectName().equals(p) &&
                            z.getTypeno().equals(typeno) && z.getDataType().equals(dataType)).collect(Collectors.toList());
        }
        //124 scpro
        if (s != null && c != null && p == null && r != null && o == null) {
            applyDataList = applyDataList.stream().filter(z ->
                    z.getSerialNo().equals(s) && z.getProjectCode() != null && z.getProjectCode().equals(c) &&
                            z.getRuleName().equals(r) &&
                            z.getTypeno().equals(typeno) && z.getDataType().equals(dataType)).collect(Collectors.toList());
        }
        //125 scpro
        if (s != null && c != null && p == null && r == null && o != null) {
            applyDataList = applyDataList.stream().filter(z ->
                    z.getSerialNo().equals(s) && z.getProjectCode() != null && z.getProjectCode().equals(c) &&
                            z.getOrderNumber().equals(o) &&
                            z.getTypeno().equals(typeno) && z.getDataType().equals(dataType)).collect(Collectors.toList());
        }
        //134 scpro
        if (s != null && c == null && p != null && r != null && o == null) {
            applyDataList = applyDataList.stream().filter(z ->
                    z.getSerialNo().equals(s) && z.getProjectName() != null && z.getProjectName().equals(p) &&
                            z.getRuleName().equals(r) &&
                            z.getTypeno().equals(typeno) && z.getDataType().equals(dataType)).collect(Collectors.toList());
        }
        //135 scpro
        if (s != null && c == null && p != null && r == null && o != null) {
            applyDataList = applyDataList.stream().filter(z ->
                    z.getSerialNo().equals(s) && z.getProjectName() != null && z.getProjectName().equals(p) &&
                            z.getOrderNumber().equals(o) &&
                            z.getTypeno().equals(typeno) && z.getDataType().equals(dataType)).collect(Collectors.toList());
        }
        //145 scpro
        if (s != null && c == null && p == null && r != null && o != null) {
            applyDataList = applyDataList.stream().filter(z ->
                    z.getSerialNo().equals(s) && z.getRuleName().equals(r) && z.getOrderNumber().equals(o) &&
                            z.getTypeno().equals(typeno) && z.getDataType().equals(dataType)).collect(Collectors.toList());
        }
        //234 scpro
        if (s == null && c != null && p != null && r != null && o == null) {
            applyDataList = applyDataList.stream().filter(z ->
                    z.getProjectCode() != null && z.getProjectCode().equals(c) &&
                            z.getProjectName() != null && z.getProjectName().equals(p) && z.getRuleName().equals(r) &&
                            z.getTypeno().equals(typeno) && z.getDataType().equals(dataType)).collect(Collectors.toList());
        }
        //235 scpro
        if (s == null && c != null && p != null && r == null && o != null) {
            applyDataList = applyDataList.stream().filter(z ->
                    z.getProjectCode() != null && z.getProjectCode().equals(c) &&
                            z.getProjectName() != null && z.getProjectName().equals(p) && z.getOrderNumber().equals(o) &&
                            z.getTypeno().equals(typeno) && z.getDataType().equals(dataType)).collect(Collectors.toList());
        }
        //345 scpro
        if (s == null && c == null && p != null && r != null && o != null) {
            applyDataList = applyDataList.stream().filter(z ->
                    z.getProjectName() != null && z.getProjectName().equals(p) && z.getRuleName().equals(r) &&
                            z.getOrderNumber().equals(o) &&
                            z.getTypeno().equals(typeno) && z.getDataType().equals(dataType)).collect(Collectors.toList());
        }
        //1234 scpro
        if (s != null && c != null && p != null && r != null && o == null) {
            applyDataList = applyDataList.stream().filter(z ->
                    z.getSerialNo().equals(s) && z.getProjectCode() != null && z.getProjectCode().equals(c) &&
                            z.getProjectName() != null && z.getProjectName().equals(p) && z.getRuleName().equals(r) &&
                            z.getTypeno().equals(typeno) && z.getDataType().equals(dataType)).collect(Collectors.toList());
        }
        //1235 scpro
        if (s != null && c != null && p != null && r == null && o != null) {
            applyDataList = applyDataList.stream().filter(z ->
                    z.getSerialNo().equals(s) && z.getProjectCode() != null && z.getProjectCode().equals(c) &&
                            z.getProjectName() != null && z.getProjectName().equals(p) && z.getOrderNumber().equals(o) &&
                            z.getTypeno().equals(typeno) && z.getDataType().equals(dataType)).collect(Collectors.toList());
        }
        //1245 scpro
        if (s != null && c != null && p == null && r != null && o != null) {
            applyDataList = applyDataList.stream().filter(z ->
                    z.getSerialNo().equals(s) && z.getProjectCode() != null && z.getProjectCode().equals(c) &&
                            z.getRuleName().equals(r) && z.getOrderNumber().equals(o) &&
                            z.getTypeno().equals(typeno) && z.getDataType().equals(dataType)).collect(Collectors.toList());
        }
        //1345 scpro
        if (s != null && c == null && p != null && r != null && o != null) {
            applyDataList = applyDataList.stream().filter(z ->
                    z.getSerialNo().equals(s) && z.getProjectName() != null && z.getProjectName().equals(p) &&
                            z.getRuleName().equals(r) && z.getOrderNumber().equals(o) &&
                            z.getTypeno().equals(typeno) && z.getDataType().equals(dataType)).collect(Collectors.toList());
        }
        //2345 scpro
        if (s == null && c != null && p != null && r != null && o != null) {
            applyDataList = applyDataList.stream().filter(z ->
                    z.getProjectCode() != null && z.getProjectCode().equals(c) && z.getProjectName() != null &&
                            z.getProjectName().equals(p) && z.getRuleName().equals(r) && z.getOrderNumber().equals(o) &&
                            z.getTypeno().equals(typeno) && z.getDataType().equals(dataType)).collect(Collectors.toList());
        }
        //12345 scpro
        if (s != null && c != null && p != null && r != null && o != null) {
            applyDataList = applyDataList.stream().filter(z ->
                    z.getSerialNo().equals(s) && z.getProjectCode() != null && z.getProjectCode().equals(c) &&
                            z.getProjectName() != null && z.getProjectName().equals(p) && z.getRuleName().equals(r) &&
                            z.getOrderNumber().equals(o) &&
                            z.getTypeno().equals(typeno) && z.getDataType().equals(dataType)).collect(Collectors.toList());
        }
        return applyDataList;
    }

    private YbReconsiderVerifyView getYbReconsiderVerifyView(YbReconsiderVerify verify, YbReconsiderApplyData reconsiderApplyData) {
        YbReconsiderVerifyView reconsiderVerifyView = new YbReconsiderVerifyView();
        reconsiderVerifyView.setPid(reconsiderApplyData.getPid());
        reconsiderVerifyView.setSerialNo(reconsiderApplyData.getSerialNo());
        reconsiderVerifyView.setBillNo(reconsiderApplyData.getBillNo());
        reconsiderVerifyView.setProposalCode(reconsiderApplyData.getProposalCode());
        reconsiderVerifyView.setProjectCode(reconsiderApplyData.getProjectCode());
        reconsiderVerifyView.setProjectName(reconsiderApplyData.getProjectName());
        reconsiderVerifyView.setNum(reconsiderApplyData.getNum());
        reconsiderVerifyView.setMedicalPrice(reconsiderApplyData.getMedicalPrice());
        reconsiderVerifyView.setRuleName(reconsiderApplyData.getRuleName());
        reconsiderVerifyView.setDeductPrice(reconsiderApplyData.getDeductPrice());
        reconsiderVerifyView.setDeductReason(reconsiderApplyData.getDeductReason());
        reconsiderVerifyView.setRepaymentReason(reconsiderApplyData.getRepaymentReason());
        reconsiderVerifyView.setDoctorName(reconsiderApplyData.getDoctorName());
        reconsiderVerifyView.setDeptCode(reconsiderApplyData.getDeptCode());
        reconsiderVerifyView.setDeptName(reconsiderApplyData.getDeptName());
        reconsiderVerifyView.setEnterHospitalDateStr(reconsiderApplyData.getEnterHospitalDateStr());
        reconsiderVerifyView.setOutHospitalDateStr(reconsiderApplyData.getOutHospitalDateStr());
        reconsiderVerifyView.setCostDateStr(reconsiderApplyData.getCostDateStr());
        reconsiderVerifyView.setHospitalizedNo(reconsiderApplyData.getHospitalizedNo());
        reconsiderVerifyView.setTreatmentMode(reconsiderApplyData.getTreatmentMode());
        reconsiderVerifyView.setSettlementDateStr(reconsiderApplyData.getSettlementDateStr());
        reconsiderVerifyView.setPersonalNo(reconsiderApplyData.getPersonalNo());
        reconsiderVerifyView.setInsuredName(reconsiderApplyData.getInsuredName());
        reconsiderVerifyView.setInsuredType(reconsiderApplyData.getInsuredType());
        reconsiderVerifyView.setCardNumber(reconsiderApplyData.getCardNumber());
        reconsiderVerifyView.setAreaName(reconsiderApplyData.getAreaName());
        reconsiderVerifyView.setVersionNumber(reconsiderApplyData.getVersionNumber());
        reconsiderVerifyView.setBackAppeal(reconsiderApplyData.getBackAppeal());
        reconsiderVerifyView.setTypeno(reconsiderApplyData.getTypeno());
        reconsiderVerifyView.setApplyDateStr(verify.getApplyDateStr());
        reconsiderVerifyView.setDataType(reconsiderApplyData.getDataType());
        reconsiderVerifyView.setOrderNumber(reconsiderApplyData.getOrderNumber());
        reconsiderVerifyView.setOrderNum(reconsiderApplyData.getOrderNum());
        reconsiderVerifyView.setId(verify.getId());
        reconsiderVerifyView.setApplyDataId(reconsiderApplyData.getId());
        reconsiderVerifyView.setVerifyDoctorCode(verify.getVerifyDoctorCode());
        reconsiderVerifyView.setVerifyDoctorName(verify.getVerifyDoctorName());
        reconsiderVerifyView.setVerifyDeptCode(verify.getVerifyDeptCode());
        reconsiderVerifyView.setVerifyDeptName(verify.getVerifyDeptName());

        reconsiderVerifyView.setOrderDoctorCode(verify.getOrderDoctorCode());
        reconsiderVerifyView.setOrderDoctorName(verify.getOrderDoctorName());
        reconsiderVerifyView.setOrderDeptCode(verify.getOrderDeptCode());
        reconsiderVerifyView.setOrderDeptName(verify.getOrderDeptName());
        reconsiderVerifyView.setState(verify.getState());
        reconsiderVerifyView.setSendDate(verify.getSendDate());
        reconsiderVerifyView.setSendPersonId(verify.getSendPersonId());
        reconsiderVerifyView.setSendPersonName(verify.getSendPersonName());
        reconsiderVerifyView.setAreaType(verify.getAreaType());
        return reconsiderVerifyView;
    }

    @Override
    public IPage<YbReconsiderVerifyView> findYbReconsiderVerifyViewList(QueryRequest request, YbReconsiderVerifyView ybReconsiderVerifyView) {
        try {
            Page<YbReconsiderVerifyView> page = new Page<>();
            SortUtil.handlePageSort(request, page, false);//true 是属性  false是数据库字段可两个
            return this.baseMapper.findYbReconsiderVerifyView(page, ybReconsiderVerifyView);
        } catch (Exception e) {
            log.error("获取VIEW失败", e);
            return null;
        }
    }

    @Override
    @Transactional
    public void createYbReconsiderVerifyView(YbReconsiderVerifyView ybReconsiderVerifyView) {
//        ybReconsiderVerifyView.setCreateTime(new Date());
//        ybReconsiderVerifyView.setIsDeletemark(1);
        this.save(ybReconsiderVerifyView);
    }

    @Override
    @Transactional
    public void updateYbReconsiderVerifyView(YbReconsiderVerifyView ybReconsiderVerifyView) {
//        ybReconsiderVerifyView.setModifyTime(new Date());
        this.baseMapper.updateYbReconsiderVerifyView(ybReconsiderVerifyView);
    }

    @Override
    @Transactional
    public void deleteYbReconsiderVerifyViews(String[] Ids) {
        List<String> list = Arrays.asList(Ids);
        this.baseMapper.deleteBatchIds(list);
    }

    @Override
    public int findReconsiderVerifyApplyDateCounts(YbReconsiderVerifyView ybReconsiderVerifyView) {
        return this.baseMapper.findReconsiderVerifyApplyDateCount(ybReconsiderVerifyView);
    }

    @Override
    public IPage<YbReconsiderVerifyView> findYbReconsiderVerifyViewNulls(QueryRequest request, YbReconsiderVerifyView ybReconsiderVerifyView, String[] searchType) {
        try {
            Page<YbReconsiderVerifyView> page = new Page<>();
            YbReconsiderApply reconsiderApply = iYbReconsiderApplyService.findReconsiderApplyByApplyDateStrs(ybReconsiderVerifyView.getApplyDateStr(), ybReconsiderVerifyView.getAreaType());
            if (reconsiderApply != null) {
                ybReconsiderVerifyView.setPid(reconsiderApply.getId());
                int applyState = reconsiderApply.getState();
                int typeno = applyState == YbDefaultValue.APPLYSTATE_2 || applyState == YbDefaultValue.APPLYSTATE_3 ? YbDefaultValue.TYPENO_1 :
                        applyState == YbDefaultValue.APPLYSTATE_4 || applyState == YbDefaultValue.APPLYSTATE_5 ? YbDefaultValue.TYPENO_2 : 0;
                if (typeno == YbDefaultValue.TYPENO_1 || typeno == YbDefaultValue.TYPENO_2) {
                    YbReconsiderVerifyView rvvQv = new YbReconsiderVerifyView();
                    rvvQv.setApplyDateStr(reconsiderApply.getApplyDateStr());
                    rvvQv.setPid(reconsiderApply.getId());
                    rvvQv.setDataType(ybReconsiderVerifyView.getDataType());
                    rvvQv.setAreaType(ybReconsiderVerifyView.getAreaType());
                    rvvQv.setTypeno(typeno);
                    int count = this.findReconsiderVerifyApplyDateCounts(rvvQv);
                    if (count > 0) {
                        ybReconsiderVerifyView.setTypeno(typeno);
                        count = this.baseMapper.findReconsiderVerifyCountNull(ybReconsiderVerifyView, searchType);
                        if (count > 0) {
                            page.setSearchCount(false);
                            SortUtil.handlePageSort(request, page, false);//true 是属性  false是数据库字段可两个
                            IPage<YbReconsiderVerifyView> pg = this.baseMapper.findReconsiderVerifyViewNull(page, ybReconsiderVerifyView, searchType);
                            pg.setTotal(count);
                            return pg;
                        } else {
                            return page;
                        }
                    } else {
                        return page;
                    }
                } else {
                    return page;
                }
            } else {
                return page;
            }
        } catch (Exception e) {
            log.error("获取VIEW失败", e);
            return null;
        }
    }


}