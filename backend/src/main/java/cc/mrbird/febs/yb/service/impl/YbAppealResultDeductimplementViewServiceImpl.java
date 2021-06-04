package cc.mrbird.febs.yb.service.impl;

import cc.mrbird.febs.com.controller.DataTypeHelpers;
import cc.mrbird.febs.common.domain.QueryRequest;
import cc.mrbird.febs.common.utils.SortUtil;
import cc.mrbird.febs.yb.entity.*;
import cc.mrbird.febs.yb.dao.YbAppealResultDeductimplementViewMapper;
import cc.mrbird.febs.yb.manager.YbApplyDataManager;
import cc.mrbird.febs.yb.service.*;
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
 * VIEW 服务实现类
 * </p>
 *
 * @author viki
 * @since 2020-09-24
 */
@Slf4j
@Service("IYbAppealResultDeductimplementViewService")
@Transactional(propagation = Propagation.SUPPORTS, readOnly = true, rollbackFor = Exception.class)
public class YbAppealResultDeductimplementViewServiceImpl extends ServiceImpl<YbAppealResultDeductimplementViewMapper, YbAppealResultDeductimplementView> implements IYbAppealResultDeductimplementViewService {

    @Autowired
    IYbReconsiderResetService iYbReconsiderResetService;

    @Autowired
    IYbReconsiderResetDataService iYbReconsiderResetDataService;

    @Autowired
    IYbAppealResultDeductimplementService iYbAppealResultDeductimplementService;

    @Autowired
    IYbAppealResultService iYbAppealResultService;

    @Autowired
    YbApplyDataManager ybApplyDataManager;

    @Autowired
    IYbPersonService iYbPersonService;

    @Override
    public IPage<YbAppealResultDeductimplementView> findYbAppealResultDeductimplementViews(QueryRequest request, YbAppealResultDeductimplementView ybAppealResultDeductimplementView) {
        try {
            /*
            LambdaQueryWrapper<YbAppealResultDeductimplementView> queryWrapper = new LambdaQueryWrapper<>();
            List<String> listStr = new ArrayList<>();
            //ApplyDateFrom getApplyDateTo 存储的格式是 2020-09
            if (ybAppealResultDeductimplementView.getApplyDateFrom().equals(ybAppealResultDeductimplementView.getApplyDateTo())) {
                listStr.add(ybAppealResultDeductimplementView.getApplyDateFrom());
            } else {
                listStr = DataTypeHelpers.stringApplyDateStrToList(ybAppealResultDeductimplementView.getApplyDateFrom(), ybAppealResultDeductimplementView.getApplyDateTo());
            }
            if (listStr.size() > 0) {
                String sql = "(";

                if (listStr.size() == 1) {
                    if(ybAppealResultDeductimplementView.getDeductImplementId()!=null) {
                        sql += " implementDateStr = '" + ybAppealResultDeductimplementView.getApplyDateFrom() + "'";
                    }else{
                        sql += " applyDateStr = '" + ybAppealResultDeductimplementView.getApplyDateFrom() + "'";
                    }
                } else {
                    String applyDateStrForm = ybAppealResultDeductimplementView.getApplyDateFrom() + "-01";
                    String applyDateStrTo = ybAppealResultDeductimplementView.getApplyDateTo() + "-" + String.valueOf(DataTypeHelpers.stringDateFormatMaxDay(ybAppealResultDeductimplementView.getApplyDateTo() + "-01", "", false));

                    if(ybAppealResultDeductimplementView.getDeductImplementId()!=null) {
                        sql += " implementDate >= '" + applyDateStrForm + "' and implementDate <= '" + applyDateStrTo + "' ";
                    }else{
                        sql += " applyDate >= '" + applyDateStrForm + "' and applyDate <= '" + applyDateStrTo + "' ";
                    }
                }

                if (ybAppealResultDeductimplementView.getTypeno() != null) {
                    if (ybAppealResultDeductimplementView.getTypeno() == 1) {
                        sql +=  " and typeno = 1";
                    } else {
                        sql +=  " and typeno = 2";
                    }
                }

                if (ybAppealResultDeductimplementView.getDataType() != null) {
                    if (ybAppealResultDeductimplementView.getDataType() == 0) {
                        sql +=  " and dataType = 0";
                    } else {
                        sql +=  " and dataType = 1";
                    }
                }

                if (ybAppealResultDeductimplementView.getArDoctorCode() != null) {
                    sql +=  " and arDoctorCode = '" + ybAppealResultDeductimplementView.getArDoctorCode() + "'";
                }

                sql +=  ")";
                if (ybAppealResultDeductimplementView.getCurrencyField() != null && !"".equals(ybAppealResultDeductimplementView.getCurrencyField())) {
                    if (ybAppealResultDeductimplementView.getDataType() != null) {
                        if (ybAppealResultDeductimplementView.getDataType() == 0) {
                            sql += " and (serialNo like'%" + ybAppealResultDeductimplementView.getCurrencyField() + "%'" +
                                    " or billNo like'%" + ybAppealResultDeductimplementView.getCurrencyField() + "%'" +
                                    " or projectCode like'%" + ybAppealResultDeductimplementView.getCurrencyField() + "%'" +
                                    " or projectName like'%" + ybAppealResultDeductimplementView.getCurrencyField() + "%'" +
                                    " or ruleName like'%" + ybAppealResultDeductimplementView.getCurrencyField() + "%'" +
                                    " or deductReason like'%" + ybAppealResultDeductimplementView.getCurrencyField() + "%')";
                        } else {
                            sql += " and (serialNo like'%" + ybAppealResultDeductimplementView.getCurrencyField() + "%'" +
                                    " or billNo like'%" + ybAppealResultDeductimplementView.getCurrencyField() + "%'" +
                                    " or ruleName like'%" + ybAppealResultDeductimplementView.getCurrencyField() + "%'" +
                                    " or personalNo like'%" + ybAppealResultDeductimplementView.getCurrencyField() + "%')";
                        }
                    } else {
                        sql += " and (serialNo like'%" + ybAppealResultDeductimplementView.getCurrencyField() + "%'" +
                                " or billNo like'%" + ybAppealResultDeductimplementView.getCurrencyField() + "%'" +
                                " or projectCode like'%" + ybAppealResultDeductimplementView.getCurrencyField() + "%'" +
                                " or projectName like'%" + ybAppealResultDeductimplementView.getCurrencyField() + "%'" +
                                " or ruleName like'%" + ybAppealResultDeductimplementView.getCurrencyField() + "%'" +
                                " or deductReason like'%" + ybAppealResultDeductimplementView.getCurrencyField() + "%')";
                    }
                }
                queryWrapper.apply(sql);
                Page<YbAppealResultDeductimplementView> page = new Page<>();
                SortUtil.handlePageSort(request, page, false);//true 是属性  false是数据库字段可两个
                return this.page(page, queryWrapper);

            } else {
                return null;
            }*/
            return null;
        } catch (Exception e) {
            log.error("获取字典信息失败", e);
            return null;
        }

    }


    //还款管理 编辑
    @Override
    public IPage<YbAppealResultDeductimplementView> findYbAppealResultDeductimplementViewList(QueryRequest
                                                                                                      request, YbAppealResultDeductimplementView ybAppealResultDeductimplementView) {
        try {
            LambdaQueryWrapper<YbAppealResultDeductimplementView> queryWrapper = new LambdaQueryWrapper<>();
            if (ybAppealResultDeductimplementView.getDeductImplementId() != null) {
                queryWrapper.eq(YbAppealResultDeductimplementView::getDeductImplementId, ybAppealResultDeductimplementView.getDeductImplementId());
            }
            Page<YbAppealResultDeductimplementView> page = new Page<>();
            SortUtil.handlePageSort(request, page, false);//true 是属性  false是数据库字段可两个
//            return this.baseMapper.findYbAppealResultDeductimplementView(page, ybAppealResultDeductimplementView);
            return this.page(page, queryWrapper);
        } catch (Exception e) {
            log.error("获取VIEW失败", e);
            return null;
        }
    }

    //扣款落实 已扣款
    @Override
    public IPage<YbAppealResultDeductimplementView> findAppealResultDeductimplementViews(QueryRequest request, YbAppealResultDeductimplementView ybAppealResultDeductimplementView, String keyField, boolean isUser,String confDocCode) {
        try {
            Page<YbAppealResultDeductimplementView> page = new Page<>();
            List<String> listStr = new ArrayList<>();
            //ApplyDateFrom getApplyDateTo 存储的格式是 2020-09
            if (ybAppealResultDeductimplementView.getApplyDateFrom().equals(ybAppealResultDeductimplementView.getApplyDateTo())) {
                listStr.add(ybAppealResultDeductimplementView.getApplyDateFrom());
            } else {
                listStr = DataTypeHelpers.stringApplyDateStrToList(ybAppealResultDeductimplementView.getApplyDateFrom(), ybAppealResultDeductimplementView.getApplyDateTo());
            }
            if (listStr.size() > 0) {
                if (listStr.size() == 1) {
                    ybAppealResultDeductimplementView.setImplementDateStr(ybAppealResultDeductimplementView.getApplyDateFrom());
                } else {
                    String applyDateStrForm = ybAppealResultDeductimplementView.getApplyDateFrom() + "-01";
                    String applyDateStrTo = ybAppealResultDeductimplementView.getApplyDateTo() + "-" + String.valueOf(DataTypeHelpers.stringDateFormatMaxDay(ybAppealResultDeductimplementView.getApplyDateTo() + "-01", "", false));

                    ybAppealResultDeductimplementView.setImplementDateFrom(applyDateStrForm);
                    ybAppealResultDeductimplementView.setImplementDateTo(applyDateStrTo);
                }
                if (!isUser && ybAppealResultDeductimplementView.getCurrencyField() != null && ybAppealResultDeductimplementView.getCurrencyField() != "") {
                    if (keyField.equals("arDoctorCode")) {
                        ybAppealResultDeductimplementView.setArDoctorCode(ybAppealResultDeductimplementView.getCurrencyField());
                    }
                    if (keyField.equals("arDoctorName")) {
                        ybAppealResultDeductimplementView.setArDoctorName(ybAppealResultDeductimplementView.getCurrencyField());
                    }
                }
                int count = this.baseMapper.findAppealResultDeductimplementCount(ybAppealResultDeductimplementView, keyField,confDocCode);
                if (count > 0) {
                    page.setSearchCount(false);
                    SortUtil.handlePageSort(request, page, false);//true 是属性  false是数据库字段可两个
                    IPage<YbAppealResultDeductimplementView> pg = this.baseMapper.findAppealResultDeductimplementView(page, ybAppealResultDeductimplementView, keyField,confDocCode);
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


    //扣款落实 管理 待扣款
    @Override
    public IPage<YbAppealResultDeductimplementView> findAppealResultDmtView(QueryRequest request, YbAppealResultDeductimplementView ybAppealResultDeductimplementView, String keyField,String confDocCode) {
        try {
            Page<YbAppealResultDeductimplementView> page = new Page<>();
            YbReconsiderReset reset = iYbReconsiderResetService.findReconsiderResetByApplyDateStr(ybAppealResultDeductimplementView.getApplyDateStr(), ybAppealResultDeductimplementView.getAreaType());
            if (reset != null && reset.getState() == 1) {
                ybAppealResultDeductimplementView.setPid(reset.getId());
                ybAppealResultDeductimplementView.setApplyDateStr(reset.getApplyDateStr());
                if (ybAppealResultDeductimplementView.getCurrencyField() != null && ybAppealResultDeductimplementView.getCurrencyField() != "") {
                    if (keyField.equals("arDoctorCode")) {
                        ybAppealResultDeductimplementView.setArDoctorCode(ybAppealResultDeductimplementView.getCurrencyField());
                    }
                    if (keyField.equals("arDoctorName")) {
                        ybAppealResultDeductimplementView.setArDoctorName(ybAppealResultDeductimplementView.getCurrencyField());
                    }
                }
                int count = this.baseMapper.findAppealResultDmtCount(ybAppealResultDeductimplementView, keyField,confDocCode);
                if (count > 0) {
                    page.setSearchCount(false);
                    SortUtil.handlePageSort(request, page, false);//true 是属性  false是数据库字段可两个
                    IPage<YbAppealResultDeductimplementView> pg = this.baseMapper.findAppealResultDmtView(page, ybAppealResultDeductimplementView, keyField,confDocCode);
                    pg.setTotal(count);
                    return pg;
                }
            }
            return page;

        } catch (Exception e) {
            log.error("获取VIEW失败", e);
            return null;
        }
    }


    public List<YbReconsiderResetData> getResetDataListView(List<YbAppealResult> resultList, List<String> dateStrList, String keyField, String value, Integer areaType, Integer dataType) {
        List<String> relatelDataList = new ArrayList<>();
        String relatelDataStr = "";
        String sql = "";
        LambdaQueryWrapper<YbReconsiderResetData> wrapperResetData = new LambdaQueryWrapper<>();
        for (YbAppealResult item : resultList) {
            if (!relatelDataList.contains(item.getRelatelDataId())) {
                relatelDataList.add(item.getRelatelDataId());
                if (relatelDataStr.equals("")) {
                    relatelDataStr = "'" + item.getRelatelDataId() + "'";
                } else {
                    relatelDataStr += ",'" + item.getRelatelDataId() + "'";
                }
            }
        }
        String sqlWhere1 = "";
        if (dataType != null) {
            sqlWhere1 = " and dataType = " + dataType;
        }
        if (dateStrList.size() == 1) {
            sql = " id not in(select resetDataId from yb_appeal_result_deductimplement where applyDateStr = '" + dateStrList.get(0) + "' and areaType = " + areaType + sqlWhere1 + ")";
        } else {
            String dateStr = "";
            for (String dae : dateStrList) {
                if (dateStr.equals("")) {
                    dateStr = "'" + dae + "'";
                } else {
                    dateStr += ",'" + dae + "'";
                }
            }
            sql = " id not in(select resetDataId from yb_appeal_result_deductimplement where applyDateStr in(" + dateStr + ") and areaType = " + areaType + sqlWhere1 + ")";
        }
        sql += " and relatelDataId in(" + relatelDataStr + ")";
        if (value != null && value != "" && !keyField.equals("readyDoctorCode") && !keyField.equals("readyDoctorName") && !keyField.equals("orderNumber")) {
            if (keyField.equals("serialNo")) {
                sql += " and serialNo = '" + value + "'";
//                wrapperResetData.eq(YbReconsiderResetData::getSerialNo, value);
            }
            if (keyField.equals("projectCode")) {
                sql += " and projectCode = '" + value + "'";
//                wrapperResetData.eq(YbReconsiderResetData::getProjectCode, value);
            }
            if (keyField.equals("projectName")) {
                sql += " and projectName = '" + value + "'";
//                wrapperResetData.eq(YbReconsiderResetData::getProjectName, value);
            }
            if (keyField.equals("ruleName")) {
                sql += " and ruleName = '" + value + "'";
//                wrapperResetData.eq(YbReconsiderResetData::getRuleName, value);
            }
        }
        wrapperResetData.apply(sql);
        return this.iYbReconsiderResetDataService.list(wrapperResetData);
    }


    //扣款落实 医生 待扣款
    @Override
    public IPage<YbAppealResultDeductimplementView> findAppealResultDmtUserView(QueryRequest request, YbAppealResultDeductimplementView ybAppealResultDeductimplementView, String keyField) {
        try {
            Page<YbAppealResultDeductimplementView> page = new Page<>();
            List<String> listStr = new ArrayList<>();
            List<String> listApplyDateStr = new ArrayList<>();
            //ApplyDateFrom getApplyDateTo 存储的格式是 2020-09
            if (ybAppealResultDeductimplementView.getApplyDateFrom().equals(ybAppealResultDeductimplementView.getApplyDateTo())) {
                listStr.add(ybAppealResultDeductimplementView.getApplyDateFrom());
            } else {
                listStr = DataTypeHelpers.stringApplyDateStrToList(ybAppealResultDeductimplementView.getApplyDateFrom(), ybAppealResultDeductimplementView.getApplyDateTo());
            }
            if (listStr.size() > 0) {
                List<YbReconsiderReset> resetList = iYbReconsiderResetService.findReconsiderResetByApplyDateStr(listStr, ybAppealResultDeductimplementView.getAreaType(), 1);
                listStr.clear();
                for (YbReconsiderReset rs : resetList) {
                    listStr.add(rs.getId());
                }
                if (listStr.size() > 0) {
                    if (listStr.size() > 1) {
                        List<YbReconsiderReset> orderResetList = resetList.stream().sorted(Comparator.comparing(YbReconsiderReset::getApplyDate)).collect(Collectors.toList());
                        YbReconsiderReset resetForm = orderResetList.get(0);
                        YbReconsiderReset resetTo = orderResetList.get(orderResetList.size() - 1);
                        String applyDateStrForm = resetForm.getApplyDateStr() + "-01";
                        String applyDateStrTo = resetTo.getApplyDateStr() + "-" + String.valueOf(DataTypeHelpers.stringDateFormatMaxDay(resetTo.getApplyDateStr() + "-01", "", false));
                        ybAppealResultDeductimplementView.setPid(null);
                        ybAppealResultDeductimplementView.setApplyDateStr(null);
                        ybAppealResultDeductimplementView.setApplyDateFrom(applyDateStrForm);
                        ybAppealResultDeductimplementView.setApplyDateTo(applyDateStrTo);
                        listStr.clear();
                        for (YbReconsiderReset item : orderResetList) {
                            listStr.add(item.getId());
                            listApplyDateStr.add(item.getApplyDateStr());
                        }
                    } else {
                        ybAppealResultDeductimplementView.setApplyDateFrom(null);
                        ybAppealResultDeductimplementView.setApplyDateTo(null);
                        ybAppealResultDeductimplementView.setPid(resetList.get(0).getId());
                        listApplyDateStr.add(resetList.get(0).getApplyDateStr());
                        ybAppealResultDeductimplementView.setApplyDateStr(resetList.get(0).getApplyDateStr());
                        listStr.clear();
                    }
                    int count = this.baseMapper.findAppealResultDmtUserCount(ybAppealResultDeductimplementView, listStr,listApplyDateStr, keyField);
                    if (count > 0) {
                        page.setSearchCount(false);
                        SortUtil.handlePageSort(request, page, false);//true 是属性  false是数据库字段可两个
                        IPage<YbAppealResultDeductimplementView> pg = this.baseMapper.findAppealResultDmtUserView(page, ybAppealResultDeductimplementView, listStr,listApplyDateStr, keyField);
                        pg.setTotal(count);
                        return pg;
                    }
                }
            }
            return page;

        } catch (Exception e) {
            log.error("获取VIEW失败", e);
            return null;
        }
    }

    @Override
    public IPage<YbAppealResultDeductimplementView> findAppealResultDmtViewNew(QueryRequest request, YbAppealResultDeductimplementView ybAppealResultDeductimplementView, String keyField) {
        try {
            String applyDateStr = ybAppealResultDeductimplementView.getApplyDateStr();
            Integer areaType = ybAppealResultDeductimplementView.getAreaType();
            YbReconsiderReset reconsiderReset = iYbReconsiderResetService.findReconsiderResetByApplyDateStr(applyDateStr, areaType);
            if (!reconsiderReset.getState().equals(1)) {
                reconsiderReset = null;
            }
            Page<YbAppealResultDeductimplementView> page = new Page<>();
            List<String> strList = new ArrayList<>();
            if (reconsiderReset != null) {
                String value = ybAppealResultDeductimplementView.getCurrencyField();
                Integer dataType = ybAppealResultDeductimplementView.getDataType();
                List<YbReconsiderResetData> resetDataList = new ArrayList<>();//ybApplyDataManager.getResetDatas(reconsiderReset.getId(), applyDateStr + "-" + areaType);
                List<YbAppealResultDeductimplementView> list = new ArrayList<>();
                List<YbAppealResult> resultList = new ArrayList<>();
                String sql = "";
                LambdaQueryWrapper<YbAppealResult> queryWrapper = new LambdaQueryWrapper<>();
                sql = " applyDateStr = '" + applyDateStr + "' and areaType = " + areaType + " and state = 2 ";
                if (value != null && value != "" && keyField.equals("arDoctorCode")) {
                    queryWrapper.eq(YbAppealResult::getDoctorCode, value);
                }
                if (ybAppealResultDeductimplementView.getArDoctorCode() != null) {
                    sql += " and doctorCode = '" + ybAppealResultDeductimplementView.getArDoctorCode() + "'";
                }
                if (dataType != null) {
                    sql += " and dataType = " + dataType;
                }
                if (value != null && value != "" && keyField.equals("arDoctorName")) {
                    strList = this.iYbPersonService.findPersonCodeList(value);
                    if (strList.size() > 0) {
                        String docIn = "";
                        for (String code : strList) {
                            if (docIn.equals("")) {
                                docIn = "'" + code + "'";
                            } else {
                                docIn += ",'" + code + "'";
                            }
                        }
                        sql += " and doctorCode in (" + docIn + ")";
                    }
                }
                queryWrapper.apply(sql);
                resultList = iYbAppealResultService.list(queryWrapper);
                if (resultList.size() > 0) {
                    strList.clear();
                    strList.add(reconsiderReset.getApplyDateStr());
                    resetDataList = this.getResetDataListView(resultList, strList, keyField, value, areaType, dataType);
                }
                if (resultList.size() > 0 && resetDataList.size() > 0) {
                    if (resultList.size() > resetDataList.size()) {
                        List<YbAppealResult> queryList = new ArrayList<>();
                        for (YbReconsiderResetData item : resetDataList) {
                            queryList = resultList.stream().filter(s -> s.getRelatelDataId().equals(item.getRelatelDataId())).collect(Collectors.toList());
                            if (queryList.size() > 0) {
                                YbAppealResultDeductimplementView arv = this.getYbResultView(queryList.get(0), item);
                                list.add(arv);
                            }
                        }
                    } else {
                        for (YbAppealResult item : resultList) {
                            List<YbReconsiderResetData> queryList = new ArrayList<>();
                            queryList = resetDataList.stream().filter(s -> s.getRelatelDataId().equals(item.getRelatelDataId())).collect(Collectors.toList());
                            if (queryList.size() > 0) {
                                YbAppealResultDeductimplementView arv = this.getYbResultView(item, queryList.get(0));
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
                    list = list.stream().sorted(
                            Comparator.comparing(YbAppealResultDeductimplementView::getOrderNum)
                    ).skip(current).limit(page.getSize()).collect(Collectors.toList());
                    page.setRecords(list);
                }
            }
            return page;

        } catch (Exception e) {
            log.error("获取字典信息失败", e);
            return null;
        }
    }

    private YbAppealResultDeductimplementView getYbResultView(YbAppealResult ar, YbReconsiderResetData reconsiderResetData) {
        YbAppealResultDeductimplementView appealResultDeductimplementView = new YbAppealResultDeductimplementView();
        appealResultDeductimplementView.setPid(reconsiderResetData.getPid());
        appealResultDeductimplementView.setResetDataId(reconsiderResetData.getId());
        appealResultDeductimplementView.setRelatelDataId(ar.getRelatelDataId());
//        appealResultDeductimplementView.setImplementDate();
//        appealResultDeductimplementView.setImplementDateStr();
//        appealResultDeductimplementView.setShareState();
//        appealResultDeductimplementView.setShareProgramme();
//        appealResultDeductimplementView.setDeductImplementId();
        appealResultDeductimplementView.setSerialNo(reconsiderResetData.getSerialNo());
        appealResultDeductimplementView.setBillNo(reconsiderResetData.getBillNo());
        appealResultDeductimplementView.setProjectCode(reconsiderResetData.getProjectCode());
        appealResultDeductimplementView.setProjectName(reconsiderResetData.getProjectName());
        appealResultDeductimplementView.setMedicalPrice(reconsiderResetData.getMedicalPrice());
        appealResultDeductimplementView.setRuleName(reconsiderResetData.getRuleName());
        appealResultDeductimplementView.setDeductPrice(reconsiderResetData.getDeductPrice());
        appealResultDeductimplementView.setDeductReason(reconsiderResetData.getDeductReason());
        appealResultDeductimplementView.setRepaymentReason(reconsiderResetData.getRepaymentReason());
        appealResultDeductimplementView.setDoctorName(reconsiderResetData.getDoctorName());
        appealResultDeductimplementView.setDeptCode(reconsiderResetData.getDeptCode());
        appealResultDeductimplementView.setDeptName(reconsiderResetData.getDeptName());
        appealResultDeductimplementView.setCostDateStr(reconsiderResetData.getCostDateStr());
        appealResultDeductimplementView.setHospitalizedNo(reconsiderResetData.getHospitalizedNo());
        appealResultDeductimplementView.setTreatmentMode(reconsiderResetData.getTreatmentMode());
        appealResultDeductimplementView.setSettlementDateStr(reconsiderResetData.getSettlementDateStr());
        appealResultDeductimplementView.setPersonalNo(reconsiderResetData.getPersonalNo());
        appealResultDeductimplementView.setInsuredName(reconsiderResetData.getInsuredName());
        appealResultDeductimplementView.setInsuredType(reconsiderResetData.getInsuredType());
        appealResultDeductimplementView.setCardNumber(reconsiderResetData.getCardNumber());
        appealResultDeductimplementView.setAreaName(reconsiderResetData.getAreaName());

        appealResultDeductimplementView.setDataType(reconsiderResetData.getDataType());
        appealResultDeductimplementView.setOrderNumber(reconsiderResetData.getOrderNumber());
        appealResultDeductimplementView.setOrderNum(reconsiderResetData.getOrderNum());
        appealResultDeductimplementView.setId(ar.getId());
        appealResultDeductimplementView.setApplyDateStr(ar.getApplyDateStr());
        appealResultDeductimplementView.setApplyDataId(reconsiderResetData.getId());
        appealResultDeductimplementView.setArDoctorCode(ar.getDoctorCode());
        appealResultDeductimplementView.setArDoctorName(ar.getDoctorName());
        appealResultDeductimplementView.setArDeptCode(ar.getDeptCode());
        appealResultDeductimplementView.setArDeptName(ar.getDeptName());
//        appealResultDeductimplementView.setCurrencyField();
        appealResultDeductimplementView.setAreaType(ar.getAreaType());
        return appealResultDeductimplementView;
    }

    @Override
    public IPage<YbAppealResultDeductimplementView> findAppealResultDmtUserNew(QueryRequest request, YbAppealResultDeductimplementView ybAppealResultDeductimplementView, String keyField) {
        try {
            Integer areaType = ybAppealResultDeductimplementView.getAreaType();
            List<String> listStr = new ArrayList<>();
            if (ybAppealResultDeductimplementView.getApplyDateFrom().equals(ybAppealResultDeductimplementView.getApplyDateTo())) {
                listStr.add(ybAppealResultDeductimplementView.getApplyDateFrom());
            } else {
                listStr = DataTypeHelpers.stringApplyDateStrToList(ybAppealResultDeductimplementView.getApplyDateFrom(), ybAppealResultDeductimplementView.getApplyDateTo());
            }
            List<YbReconsiderReset> reconsiderResetList = new ArrayList<>();
            if (listStr.size() > 0) {
                reconsiderResetList = this.iYbReconsiderResetService.findReconsiderResetByApplyDateStr(listStr, areaType, 1);
            }
            Page<YbAppealResultDeductimplementView> page = new Page<>();
            if (reconsiderResetList.size() > 0) {
                listStr.clear();
                for (YbReconsiderReset item : reconsiderResetList) {
                    listStr.add(item.getApplyDateStr());
                }
                String value = ybAppealResultDeductimplementView.getCurrencyField();
                Integer dataType = ybAppealResultDeductimplementView.getDataType();
                if (dataType != null && dataType == 2) {
                    dataType = 0;
                }
                List<YbReconsiderResetData> resetDataList = new ArrayList<>();
                List<YbAppealResultDeductimplementView> list = new ArrayList<>();
                List<YbAppealResult> resultList = new ArrayList<>();
                String sql = "";
                LambdaQueryWrapper<YbAppealResult> queryWrapper = new LambdaQueryWrapper<>();
                String dateStrIn = "";
                for (String code : listStr) {
                    if (dateStrIn.equals("")) {
                        dateStrIn = "'" + code + "'";
                    } else {
                        dateStrIn += ",'" + code + "'";
                    }
                }
                sql = " applyDateStr in(" + dateStrIn + ") and areaType = " + areaType + " and state = 2 ";
                sql += " and doctorCode = '" + ybAppealResultDeductimplementView.getArDoctorCode() + "'";
                if (dataType != null) {
                    sql += " and dataType = " + dataType;
                }

                queryWrapper.apply(sql);
                resultList = iYbAppealResultService.list(queryWrapper);

                if (resultList.size() > 0) {
                    resetDataList = this.getResetDataListView(resultList, listStr, keyField, value, areaType, dataType);
                }

                if (resultList.size() > 0 && resetDataList.size() > 0) {
                    if (resultList.size() > resetDataList.size()) {
                        List<YbAppealResult> queryList = new ArrayList<>();
                        for (YbReconsiderResetData item : resetDataList) {
                            queryList = resultList.stream().filter(s -> s.getRelatelDataId().equals(item.getRelatelDataId())).collect(Collectors.toList());
                            if (queryList.size() > 0) {
                                YbAppealResultDeductimplementView arv = this.getYbResultView(queryList.get(0), item);
                                list.add(arv);
                            }
                        }
                    } else {
                        for (YbAppealResult item : resultList) {
                            List<YbReconsiderResetData> queryList = new ArrayList<>();
                            queryList = resetDataList.stream().filter(s -> s.getRelatelDataId().equals(item.getRelatelDataId())).collect(Collectors.toList());
                            if (queryList.size() > 0) {
                                YbAppealResultDeductimplementView arv = this.getYbResultView(item, queryList.get(0));
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
                    list = list.stream().sorted(
                            Comparator.comparing(YbAppealResultDeductimplementView::getApplyDateStr)
                                    .thenComparing(YbAppealResultDeductimplementView::getDataType)
                                    .thenComparing(YbAppealResultDeductimplementView::getOrderNum)
                    ).skip(current).limit(page.getSize()).collect(Collectors.toList());

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
    @Transactional
    public void createYbAppealResultDeductimplementView(YbAppealResultDeductimplementView
                                                                ybAppealResultDeductimplementView) {

        this.save(ybAppealResultDeductimplementView);
    }

    @Override
    @Transactional
    public void updateYbAppealResultDeductimplementView(YbAppealResultDeductimplementView
                                                                ybAppealResultDeductimplementView) {
        this.baseMapper.updateYbAppealResultDeductimplementView(ybAppealResultDeductimplementView);
    }

    @Override
    @Transactional
    public void deleteYbAppealResultDeductimplementViews(String[] Ids) {
        List<String> list = Arrays.asList(Ids);
        this.baseMapper.deleteBatchIds(list);
    }


}