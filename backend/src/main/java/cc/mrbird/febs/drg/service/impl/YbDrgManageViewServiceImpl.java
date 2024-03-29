package cc.mrbird.febs.drg.service.impl;

import cc.mrbird.febs.com.controller.DataTypeHelpers;
import cc.mrbird.febs.common.domain.QueryRequest;
import cc.mrbird.febs.common.utils.SortUtil;
import cc.mrbird.febs.system.domain.User;
import cc.mrbird.febs.drg.dao.YbDrgManageViewMapper;
import cc.mrbird.febs.drg.entity.*;
import cc.mrbird.febs.drg.service.*;
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

import java.util.*;
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
@Service("IYbDrgManageViewService")
@Transactional(propagation = Propagation.SUPPORTS, readOnly = true, rollbackFor = Exception.class)
public class YbDrgManageViewServiceImpl extends ServiceImpl<YbDrgManageViewMapper, YbDrgManageView> implements IYbDrgManageViewService {

    @Autowired
    IYbDrgApplyService iYbDrgApplyService;

    @Autowired
    IYbDrgManageService iYbDrgManageService;

    @Autowired
    IYbPersonService iYbPersonService;

    @Autowired
    IYbDrgApplyDataService iYbDrgApplyDataService;

    @Autowired
    IYbDrgConfireDataService iYbDrgConfireDataService;

    @Override
    public IPage<YbDrgManageView> findDrgManageView(QueryRequest request, YbDrgManageView ybDrgManageView, String keyField, boolean isConf) {
        try {
            String applyDateStr = ybDrgManageView.getApplyDateStr();
            Integer areaType = ybDrgManageView.getAreaType();
            YbDrgApply drgApply = iYbDrgApplyService.findDrgApplyByApplyDateStrs(applyDateStr, areaType);
            Page<YbDrgManageView> page = new Page<>();
            if (drgApply != null) {
                String value = ybDrgManageView.getCurrencyField();
                Integer state = ybDrgManageView.getState();
                List<String> strList = new ArrayList<>();
                List<YbDrgManageView> list = new ArrayList<>();
                List<YbDrgManage> manageList = new ArrayList<>();

                LambdaQueryWrapper<YbDrgManage> queryWrapper = new LambdaQueryWrapper<>();
                queryWrapper.eq(YbDrgManage::getApplyDateStr, applyDateStr);
                queryWrapper.eq(YbDrgManage::getAreaType, areaType);
                queryWrapper.eq(YbDrgManage::getState, state);
                if (StringUtils.isNotBlank(value) && keyField.equals("readyDoctorName")) {
                    strList = this.iYbPersonService.findPersonCodeList(value);
                    if (strList.size() > 0) {
                        if (strList.size() == 1) {
                            queryWrapper.eq(YbDrgManage::getReadyDoctorCode, strList.get(0));
                        } else {
                            queryWrapper.in(YbDrgManage::getReadyDoctorCode, strList);
                        }
                    }
                } else {
                    if (StringUtils.isNotBlank(value) && keyField.equals("readyDoctorCode")) {
                        queryWrapper.eq(YbDrgManage::getReadyDoctorCode, value);
                    }
                    if (ybDrgManageView.getReadyDoctorCode() != null && !isConf) {
                        queryWrapper.eq(YbDrgManage::getReadyDoctorCode, ybDrgManageView.getReadyDoctorCode());
                    }
                    if (StringUtils.isNotBlank(value) && keyField.equals("orderNumber")) {
                        queryWrapper.eq(YbDrgManage::getOrderNumber, value);
                    }
                }

                if (ybDrgManageView.getReadyDoctorCode() != null && isConf) {
                    List<YbDrgConfireData> confireDataList = iYbDrgConfireDataService.findDrgConfireDataByInDoctorCodeList(ybDrgManageView.getReadyDoctorCode(), areaType);
                    if (confireDataList.size() > 0) {
                        strList = new ArrayList<>();
                        for (YbDrgConfireData item : confireDataList) {
                            if (!strList.contains(item.getDksId())) {
                                strList.add(item.getDksId());
                            }
                        }
                        if (strList.size() > 0) {
                            if (strList.size() == 1) {
                                queryWrapper.eq(YbDrgManage::getReadyDksId, strList.get(0));
                            } else {
                                queryWrapper.in(YbDrgManage::getReadyDksId, strList);
                            }
                        }
                    } else {
                        queryWrapper.eq(YbDrgManage::getReadyDksId, "dks");
                    }
                }

                manageList = iYbDrgManageService.list(queryWrapper);
                List<YbDrgApplyData> applyDataList = this.iYbDrgApplyDataService.getApplyDataByKeyFieldList(drgApply.getId(), keyField, value);

                if (manageList.size() > 0 && applyDataList.size() > 0) {
                    if (manageList.size() > applyDataList.size()) {
                        List<YbDrgManage> queryList = new ArrayList<>();
                        for (YbDrgApplyData item : applyDataList) {
                            queryList = manageList.stream().filter(s -> s.getApplyDataId().equals(item.getId())).collect(Collectors.toList());
                            if (queryList.size() > 0) {
                                YbDrgManageView amv = this.getYbDrgManageView(queryList.get(0), item);
                                list.add(amv);
                            }
                        }
                    } else {
                        for (YbDrgManage item : manageList) {
                            List<YbDrgApplyData> queryList = new ArrayList<>();
                            queryList = applyDataList.stream().filter(s -> s.getId().equals(item.getApplyDataId())).collect(Collectors.toList());
                            if (queryList.size() > 0) {
                                YbDrgManageView amv = this.getYbDrgManageView(item, queryList.get(0));
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
                    list = list.stream().sorted(Comparator.comparing(YbDrgManageView::getOrderNum)).skip(current).limit(page.getSize()).collect(Collectors.toList());
                    Date thisDate = new Date();
                    for (YbDrgManageView item : list) {
                        int nValue = item.getEnableDate().after(drgApply.getEndDate()) ? 0 : 1;
                        item.setApplyEndDate(drgApply.getEndDate());
                        item.setIsEnableDate(nValue);//计算
                        nValue = drgApply.getEndDate().after(thisDate) ? 0 : 1;
                        item.setIsEnd(nValue);//计算
                        nValue = item.getEnableDate().after(thisDate) ? 1 : 0;
                        item.setEnableType(nValue);
                        Date enableDate = item.getEnableDate();
                        //前面enableDate需要跟其他字段计算，所以最后赋值
                        if (drgApply.getEndDate().after(enableDate)) {
                            enableDate = DataTypeHelpers.addDateMethod(item.getEnableDate(), -1);
                        } else {
                            enableDate = drgApply.getEndDate();
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

    private YbDrgManageView getYbDrgManageView(YbDrgManage am, YbDrgApplyData drgApplyData) {
        YbDrgManageView amv = new YbDrgManageView();
        amv.setPid(drgApplyData.getPid());
        amv.setKs(drgApplyData.getKs());
        amv.setJzjlh(drgApplyData.getJzjlh());
        amv.setBah(drgApplyData.getBah());
        amv.setWglx(drgApplyData.getWglx());
        amv.setWtms(drgApplyData.getWtms());
        amv.setYlzfy(drgApplyData.getYlzfy());
        amv.setWgje(drgApplyData.getWgje());
        amv.setSfbmzczjcw(drgApplyData.getSfbmzczjcw());
        amv.setLy(drgApplyData.getLy());
        amv.setOrderNumber(drgApplyData.getOrderNumber());
        amv.setOrderNum(drgApplyData.getOrderNum());
        amv.setApplyDateStr(am.getApplyDateStr());
        amv.setId(am.getId());
        amv.setApplyDataId(drgApplyData.getId());
        amv.setVerifyId(am.getVerifyId());
        amv.setReadyDoctorCode(am.getReadyDoctorCode());
        amv.setReadyDoctorName(am.getReadyDoctorName());
        amv.setReadyDksId(am.getReadyDksId());
        amv.setReadyDksName(am.getReadyDksName());
        amv.setChangeDoctorCode(am.getChangeDoctorCode());
        amv.setChangeDoctorName(am.getChangeDoctorName());
        amv.setChangeDksId(am.getChangeDksId());
        amv.setChangeDksName(am.getChangeDksName());
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
        amv.setAreaType(am.getAreaType());
        return amv;
    }


}