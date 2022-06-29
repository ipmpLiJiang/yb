package cc.mrbird.febs.chs.service.impl;

import cc.mrbird.febs.common.domain.QueryRequest;
import cc.mrbird.febs.common.utils.SortUtil;
import cc.mrbird.febs.chs.dao.YbChsVerifyViewMapper;
import cc.mrbird.febs.chs.entity.YbChsApply;
import cc.mrbird.febs.chs.entity.YbChsApplyData;
import cc.mrbird.febs.chs.entity.YbChsVerify;
import cc.mrbird.febs.chs.entity.YbChsVerifyView;
import cc.mrbird.febs.chs.service.IYbChsApplyDataService;
import cc.mrbird.febs.chs.service.IYbChsApplyService;
import cc.mrbird.febs.chs.service.IYbChsVerifyService;
import cc.mrbird.febs.chs.service.IYbChsVerifyViewService;
import cc.mrbird.febs.yb.service.IYbPersonService;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

/**
 * findYbChsVerifyViews
 * <p>
 * VIEW 服务实现类
 * </p>
 *
 * @author viki
 * @since 2020-07-30
 */
@Slf4j
@Service("IYbChsVerifyViewService")
@Transactional(propagation = Propagation.SUPPORTS, readOnly = true, rollbackFor = Exception.class)
public class YbChsVerifyViewServiceImpl extends ServiceImpl<YbChsVerifyViewMapper, YbChsVerifyView> implements IYbChsVerifyViewService {

    @Autowired
    IYbChsApplyService iYbChsApplyService;

    @Autowired
    IYbChsApplyDataService iYbChsApplyDataService;

    @Autowired
    IYbPersonService iYbPersonService;

    @Autowired
    IYbChsVerifyService iYbChsVerifyService;

    @Override
    public IPage<YbChsVerifyView> findYbChsVerifyViews(QueryRequest request, YbChsVerifyView ybChsVerifyView) {
        try {
            Page<YbChsVerifyView> page = new Page<>();
            YbChsApply drgApply = iYbChsApplyService.findChsApplyByApplyDateStrs(ybChsVerifyView.getApplyDateStr(), ybChsVerifyView.getAreaType());
            if (drgApply != null) {
                ybChsVerifyView.setPid(drgApply.getId());
                int count = this.baseMapper.findChsVerifyCount(ybChsVerifyView);
                if (count > 0) {
                    page.setSearchCount(false);
                    SortUtil.handlePageSort(request, page, false);//true 是属性  false是数据库字段可两个
                    IPage<YbChsVerifyView> pg = this.baseMapper.findChsVerifyView(page, ybChsVerifyView);
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
    public List<YbChsVerifyView> findChsVerifyViewLists(YbChsVerifyView ybChsVerifyView) {
        return this.baseMapper.findChsVerifyViewList(ybChsVerifyView);
    }

    @Override
    public IPage<YbChsVerifyView> findChsVerifyViewNew(QueryRequest request, YbChsVerifyView ybChsVerifyView) {
        try {
            String applyDateStr = ybChsVerifyView.getApplyDateStr();
            Integer areaType = ybChsVerifyView.getAreaType();
            YbChsApply drgApply = iYbChsApplyService.findChsApplyByApplyDateStrs(applyDateStr, areaType);
            Page<YbChsVerifyView> page = new Page<>();
            if (drgApply != null) {
                List<YbChsApplyData> applyDataList = null;//iYbChsApplyDataService.getApplyDatas(drgApply, ybChsVerifyView);
                List<YbChsVerifyView> list = new ArrayList<>();
                List<YbChsVerify> verifyList = new ArrayList<>();

                LambdaQueryWrapper<YbChsVerify> queryWrapper = new LambdaQueryWrapper<>();
                queryWrapper.eq(YbChsVerify::getApplyDateStr, applyDateStr);
                queryWrapper.eq(YbChsVerify::getAreaType, areaType);

                if (ybChsVerifyView.getState() != null) {
                    queryWrapper.eq(YbChsVerify::getState, ybChsVerifyView.getState());
                }
                if (ybChsVerifyView.getOrderNum() != null) {
                    queryWrapper.eq(YbChsVerify::getOrderNum, ybChsVerifyView.getOrderNum());
                }
                if (ybChsVerifyView.getVerifyDoctorCode() != null) {
                    queryWrapper.eq(YbChsVerify::getVerifyDoctorCode, ybChsVerifyView.getVerifyDoctorCode());
                }
                if (ybChsVerifyView.getVerifyDoctorName() != null) {
                    queryWrapper.eq(YbChsVerify::getVerifyDoctorName, ybChsVerifyView.getVerifyDoctorName());
                }
                if (ybChsVerifyView.getVerifyDksId() != null) {
                    queryWrapper.eq(YbChsVerify::getVerifyDksId, ybChsVerifyView.getVerifyDksId());
                }
                if (ybChsVerifyView.getVerifyDksName() != null) {
                    queryWrapper.eq(YbChsVerify::getVerifyDksName, ybChsVerifyView.getVerifyDksName());
                }
                verifyList = iYbChsVerifyService.list(queryWrapper);

                if (verifyList.size() > 0 && applyDataList.size() > 0) {
                    if (verifyList.size() > applyDataList.size()) {
                        List<YbChsVerify> queryList = new ArrayList<>();
                        for (YbChsApplyData item : applyDataList) {
                            queryList = verifyList.stream().filter(s -> s.getApplyDataId().equals(item.getId())).collect(Collectors.toList());
                            if (queryList.size() > 0) {
                                YbChsVerifyView arv = this.getYbChsVerifyView(queryList.get(0), item);
                                list.add(arv);
                            }
                        }
                    } else {
                        for (YbChsVerify item : verifyList) {
                            List<YbChsApplyData> queryList = new ArrayList<>();
                            queryList = applyDataList.stream().filter(s -> s.getId().equals(item.getApplyDataId())).collect(Collectors.toList());
                            if (queryList.size() > 0) {
                                YbChsVerifyView arv = this.getYbChsVerifyView(item, queryList.get(0));
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
                    list = list.stream().sorted(Comparator.comparing(YbChsVerifyView::getOrderNum)).skip(current).limit(page.getSize()).collect(Collectors.toList());
                    page.setRecords(list);
                }
            }
            return page;

        } catch (Exception e) {
            log.error("获取字典信息失败", e);
            return null;
        }
    }

    private YbChsVerifyView getYbChsVerifyView(YbChsVerify verify, YbChsApplyData drgApplyData) {
        YbChsVerifyView drgVerifyView = new YbChsVerifyView();
        drgVerifyView.setAppealEndDate(drgApplyData.getAppealEndDate());//申诉截止日期
        drgVerifyView.setPayPlaceType(drgApplyData.getPayPlaceType());//支付地点类别
        drgVerifyView.setYdState(drgApplyData.getYdState());//疑点状态
        drgVerifyView.setAreaName(drgApplyData.getAreaName());//医保区划
        drgVerifyView.setYyjgCode(drgApplyData.getYyjgCode());//医药机构编码
        drgVerifyView.setYyjgName(drgApplyData.getYyjgName());//医药机构名称
        drgVerifyView.setDeptName(drgApplyData.getDeptName());//科室
        drgVerifyView.setDoctorName(drgApplyData.getDoctorName());//医生
        drgVerifyView.setMedicalType(drgApplyData.getMedicalType());//医疗类别
        drgVerifyView.setZymzNumber(drgApplyData.getZymzNumber());//住院门诊号
        drgVerifyView.setInsuredName(drgApplyData.getInsuredName());//参保人
        drgVerifyView.setEnterHospitalDate(drgApplyData.getEnterHospitalDate());//入院日期
        drgVerifyView.setOutHospitalDate(drgApplyData.getOutHospitalDate());//出院日期
        drgVerifyView.setSettlementDate(drgApplyData.getSettlementDate());//结算日期
        drgVerifyView.setCardNumber(drgApplyData.getCardNumber());//身份证号
        drgVerifyView.setProjectCode(drgApplyData.getProjectCode());//医保项目编码
        drgVerifyView.setProjectName(drgApplyData.getProjectName());//医院项目名称
        drgVerifyView.setRuleName(drgApplyData.getRuleName());//规则名称
        drgVerifyView.setViolateCsPrice(drgApplyData.getViolateCsPrice());//初审违规金额（元）
        drgVerifyView.setViolatePrice(drgApplyData.getViolatePrice());//违规金额（元）
        drgVerifyView.setViolateReason(drgApplyData.getViolateReason());//违规内容
        drgVerifyView.setZdNote(drgApplyData.getZdNote());//诊断信息
        drgVerifyView.setCostDate(drgApplyData.getCostDate());//费用日期
        drgVerifyView.setInsuredType(drgApplyData.getInsuredType());//险种类型
        drgVerifyView.setPrice(drgApplyData.getPrice());//单价（元）
        drgVerifyView.setNum(drgApplyData.getNum());//数量
        drgVerifyView.setMedicalPrice(drgApplyData.getMedicalPrice());//金额（元）
        drgVerifyView.setTcPayPrice(drgApplyData.getTcPayPrice());//统筹支付（元）
        drgVerifyView.setSpecs(drgApplyData.getSpecs());//规格
        drgVerifyView.setJx(drgApplyData.getJx());//剂型
        drgVerifyView.setJgLevel(drgApplyData.getJgLevel());//机构等级
        drgVerifyView.setOrderNum(drgApplyData.getOrderNum());
        drgVerifyView.setId(verify.getId());
        drgVerifyView.setApplyDataId(drgApplyData.getId());
        drgVerifyView.setVerifyDoctorCode(verify.getVerifyDoctorCode());
        drgVerifyView.setVerifyDoctorName(verify.getVerifyDoctorName());
        drgVerifyView.setVerifyDksId(verify.getVerifyDksId());
        drgVerifyView.setVerifyDksName(verify.getVerifyDksName());

        drgVerifyView.setState(verify.getState());
        drgVerifyView.setSendDate(verify.getSendDate());
        drgVerifyView.setSendPersonId(verify.getSendPersonId());
        drgVerifyView.setSendPersonName(verify.getSendPersonName());
        drgVerifyView.setAreaType(verify.getAreaType());
        return drgVerifyView;
    }


    @Override
    public int findChsVerifyApplyDateCounts(YbChsVerifyView ybChsVerifyView) {
        return this.baseMapper.findChsVerifyApplyDateCount(ybChsVerifyView);
    }

}