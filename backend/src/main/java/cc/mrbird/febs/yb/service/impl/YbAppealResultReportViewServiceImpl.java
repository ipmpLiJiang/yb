package cc.mrbird.febs.yb.service.impl;

import cc.mrbird.febs.com.controller.DataTypeHelpers;
import cc.mrbird.febs.common.domain.QueryRequest;
import cc.mrbird.febs.common.utils.SortUtil;
import cc.mrbird.febs.yb.entity.*;
import cc.mrbird.febs.yb.dao.YbAppealResultReportViewMapper;
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
@Service("IYbAppealResultReportViewService")
@Transactional(propagation = Propagation.SUPPORTS, readOnly = true, rollbackFor = Exception.class)
public class YbAppealResultReportViewServiceImpl extends ServiceImpl<YbAppealResultReportViewMapper, YbAppealResultReportView> implements IYbAppealResultReportViewService {

    @Autowired
    IYbReconsiderApplyService iYbReconsiderApplyService;

    @Autowired
    IYbReconsiderApplyDataService iYbReconsiderApplyDataService;

    @Autowired
    IYbAppealResultService iYbAppealResultService;

    @Autowired
    YbApplyDataManager ybApplyDataManager;

    @Autowired
    IYbPersonService iYbPersonService;

    @Override
    public IPage<YbAppealResultReportView> findYbAppealResultReportViews(QueryRequest request, YbAppealResultReportView ybAppealResultReportView) {
        try {
            LambdaQueryWrapper<YbAppealResultReportView> queryWrapper = new LambdaQueryWrapper<>();
            List<String> listStr = new ArrayList<>();
            //ApplyDateFrom getApplyDateTo 存储的格式是 2020-09
            if (ybAppealResultReportView.getApplyDateFrom().equals(ybAppealResultReportView.getApplyDateTo())) {
                listStr.add(ybAppealResultReportView.getApplyDateFrom());
            } else {
                listStr = DataTypeHelpers.stringApplyDateStrToList(ybAppealResultReportView.getApplyDateFrom(), ybAppealResultReportView.getApplyDateTo());
            }
            if (listStr.size() > 0) {
                String sql = "(";

                // String sqlApplyDateStr = "";
                if (listStr.size() == 1) {
//                    sqlApplyDateStr = listStr.get(0);
                    sql += " applyDateStr = '" + ybAppealResultReportView.getApplyDateFrom() + "'";
                } else {
                    //sqlApplyDateStr = "'" + DataTypeHelpers.stringListSeparate(listStr, "','") + "'";
                    String applyDateStrForm = ybAppealResultReportView.getApplyDateFrom() + "-01";
                    String applyDateStrTo = ybAppealResultReportView.getApplyDateTo() + "-" + String.valueOf(DataTypeHelpers.stringDateFormatMaxDay(ybAppealResultReportView.getApplyDateTo() + "-01", "", false));

                    sql += " applyDate >= '" + applyDateStrForm + "' and applyDate <= '" + applyDateStrTo + "' ";
                }

                if (ybAppealResultReportView.getTypeno() != null) {
                    if (ybAppealResultReportView.getTypeno() == YbDefaultValue.TYPENO_1) {
                        sql += " and typeno = " + YbDefaultValue.TYPENO_1;
                    } else {
                        sql += " and typeno = " + YbDefaultValue.TYPENO_2;
                    }
                }
                if (ybAppealResultReportView.getDataType() != null) {
                    if (ybAppealResultReportView.getDataType() == YbDefaultValue.DATATYPE_0) {
                        sql += " and dataType = " + YbDefaultValue.DATATYPE_0;
                    } else {
                        sql += " and dataType = " + YbDefaultValue.DATATYPE_1;
                    }
                }
                if (ybAppealResultReportView.getSourceType() != null) {
                    sql += " and sourceType = " + ybAppealResultReportView.getSourceType();
                }
                if (ybAppealResultReportView.getState() != null) {
                    if (ybAppealResultReportView.getState() == 1) {
                        sql += " and (STATE = 1 or (STATE = 2 and (repayState = 1 or repayState = 3)))";
                    } else {
                        sql += " and STATE = 2 and repayState = 2";
                    }
                }

                if (ybAppealResultReportView.getArDoctorCode() != null) {
                    sql += " and arDoctorCode = '" + ybAppealResultReportView.getArDoctorCode() + "'";
                }

                sql += " and raResetState = 1";

                sql += ")";
                if (ybAppealResultReportView.getCurrencyField() != null && !"".equals(ybAppealResultReportView.getCurrencyField())) {
                    if (ybAppealResultReportView.getDataType() != null) {
                        if (ybAppealResultReportView.getDataType() == 0) {
                            sql += " and (serialNo like'%" + ybAppealResultReportView.getCurrencyField() + "%'" +
                                    " or billNo like'%" + ybAppealResultReportView.getCurrencyField() + "%'" +
                                    " or proposalCode like'%" + ybAppealResultReportView.getCurrencyField() + "%'" +
                                    " or projectCode like'%" + ybAppealResultReportView.getCurrencyField() + "%'" +
                                    " or projectName like'%" + ybAppealResultReportView.getCurrencyField() + "%'" +
                                    " or ruleName like'%" + ybAppealResultReportView.getCurrencyField() + "%'" +
                                    " or deductReason like'%" + ybAppealResultReportView.getCurrencyField() + "%')";
                        } else {
                            sql += " and (serialNo like'%" + ybAppealResultReportView.getCurrencyField() + "%'" +
                                    " or billNo like'%" + ybAppealResultReportView.getCurrencyField() + "%'" +
                                    " or ruleName like'%" + ybAppealResultReportView.getCurrencyField() + "%'" +
                                    " or personalNo like'%" + ybAppealResultReportView.getCurrencyField() + "%')";
                        }
                    } else {
                        sql += " and (serialNo like'%" + ybAppealResultReportView.getCurrencyField() + "%'" +
                                " or billNo like'%" + ybAppealResultReportView.getCurrencyField() + "%'" +
                                " or proposalCode like'%" + ybAppealResultReportView.getCurrencyField() + "%'" +
                                " or projectCode like'%" + ybAppealResultReportView.getCurrencyField() + "%'" +
                                " or projectName like'%" + ybAppealResultReportView.getCurrencyField() + "%'" +
                                " or ruleName like'%" + ybAppealResultReportView.getCurrencyField() + "%'" +
                                " or deductReason like'%" + ybAppealResultReportView.getCurrencyField() + "%')";
                    }
                }
                queryWrapper.apply(sql);
                Page<YbAppealResultReportView> page = new Page<>();
                SortUtil.handlePageSort(request, page, false);//true 是属性  false是数据库字段可两个
                return this.page(page, queryWrapper);
            } else {
                return null;
            }

        } catch (Exception e) {
            log.error("获取字典信息失败", e);
            return null;
        }
    }

    @Override
    public IPage<YbAppealResultReportView> findYbAppealResultReportViewList(QueryRequest request, YbAppealResultReportView ybAppealResultReportView) {
        try {
            Page<YbAppealResultReportView> page = new Page<>();
            SortUtil.handlePageSort(request, page, false);//true 是属性  false是数据库字段可两个
            return this.baseMapper.findYbAppealResultReportView(page, ybAppealResultReportView);
        } catch (Exception e) {
            log.error("获取VIEW失败", e);
            return null;
        }
    }

    @Override
    public IPage<YbAppealResultReportView> findAppealResultReportViews(QueryRequest request, YbAppealResultReportView ybAppealResultReportView, String keyField, boolean isUser) {
        try {
            List<String> listStr = new ArrayList<>();
            if (isUser) {
                listStr = this.applyDataStrList(ybAppealResultReportView);
            } else {
                listStr.add(ybAppealResultReportView.getApplyDateStr());
            }
            String pid = null;
            List<YbReconsiderApply> reconsiderApplyList = new ArrayList<>();
            if (listStr.size() > 0) {
                reconsiderApplyList = this.iYbReconsiderApplyService.findReconsiderApplyByApplyDateStrs(listStr, ybAppealResultReportView.getAreaType(),1);
                if (reconsiderApplyList.size() > 0) {
                    pid = reconsiderApplyList.get(0).getId();
                }
            }
            listStr.clear();
            for (YbReconsiderApply item : reconsiderApplyList) {
                listStr.add(item.getApplyDateStr());
            }
            ybAppealResultReportView.setPid(pid);
            boolean isLike = false;
            if (ybAppealResultReportView.getCurrencyField() != null && ybAppealResultReportView.getCurrencyField() != "") {
                if (!keyField.equals("arDoctorCode") && !keyField.equals("arDoctorName")) {
                    isLike = true;
                }
                if (keyField.equals("arDoctorCode")) {
                    ybAppealResultReportView.setArDoctorCode(ybAppealResultReportView.getCurrencyField());
                }
                if (keyField.equals("arDoctorName")) {
                    ybAppealResultReportView.setArDoctorName(ybAppealResultReportView.getCurrencyField());
                }
            }
            int count = 0;
            if (isLike) {
                count = this.baseMapper.findAppealResultReportLikeCount(ybAppealResultReportView, listStr, keyField);
            } else {
                count = this.baseMapper.findAppealResultReportCount(ybAppealResultReportView, listStr, keyField);
            }
            Page<YbAppealResultReportView> page = new Page<>();
            if (count > 0) {
                page.setSearchCount(false);
                SortUtil.handlePageSort(request, page, false);//true 是属性  false是数据库字段可两个
                IPage<YbAppealResultReportView> pg = null;
                if (isLike) {
                    pg = this.baseMapper.findAppealResultReportLikeView(page, ybAppealResultReportView, listStr, keyField);
                } else {
                    pg = this.baseMapper.findAppealResultReportView(page, ybAppealResultReportView, listStr, keyField);
                }

                pg.setTotal(count);
                return pg;
            } else {
                return page;
            }

        } catch (Exception e) {
            log.error("获取VIEW失败", e);
            return null;
        }
    }

    @Override
    public IPage<YbAppealResultReportView> findAppealResultReportViewNew(QueryRequest request, YbAppealResultReportView ybAppealResultReportView, String keyField) {
        try {
            String applyDateStr = ybAppealResultReportView.getApplyDateStr();
            Integer areaType = ybAppealResultReportView.getAreaType();
            YbReconsiderApply reconsiderApply = iYbReconsiderApplyService.findReconsiderApplyByApplyDateStrs(applyDateStr, areaType);
            if(reconsiderApply!= null && !reconsiderApply.getResetState().equals(1)){
                reconsiderApply = null;
            }
            Page<YbAppealResultReportView> page = new Page<>();
            if (reconsiderApply != null) {
                String value = ybAppealResultReportView.getCurrencyField();
                Integer typeno = ybAppealResultReportView.getTypeno();
                Integer state = ybAppealResultReportView.getState();
                Integer dataType = ybAppealResultReportView.getDataType();
                if(dataType!= null && dataType == 2){
                    dataType = 0;
                }
                Integer sourceType = ybAppealResultReportView.getSourceType();
                List<YbReconsiderApplyData> applyDataList = ybApplyDataManager.getApplyDatas(reconsiderApply.getId(), applyDateStr + "-" + areaType);
                List<YbAppealResultReportView> list = new ArrayList<>();
                List<YbAppealResult> resultList = new ArrayList<>();
                String sql = "";
                LambdaQueryWrapper<YbAppealResult> queryWrapper = new LambdaQueryWrapper<>();
                sql = " applyDateStr = '" + applyDateStr + "' and areaType = " + areaType;
//                queryWrapper.eq(YbAppealResult::getApplyDateStr, applyDateStr);
//                queryWrapper.eq(YbAppealResult::getAreaType, areaType);
                if (value != null && value != "" && keyField.equals("arDoctorCode")) {
                    queryWrapper.eq(YbAppealResult::getDoctorCode, value);
                }
                if (ybAppealResultReportView.getArDoctorCode() != null) {
                    sql += " and doctorCode = '" + ybAppealResultReportView.getArDoctorCode() + "'";
//                    queryWrapper.eq(YbAppealResult::getDoctorCode, ybAppealResultReportView.getArDoctorCode());
                }
                if (value != null && value != "" && keyField.equals("orderNumber")) {
                    sql += " and orderNumber = '" + value + "'";
//                    queryWrapper.eq(YbAppealResult::getOrderNumber, value);
                }
                if (typeno != null) {
                    sql += " and typeno = " + typeno;
//                    queryWrapper.eq(YbAppealResult::getTypeno, typeno);
                }
                if (dataType != null) {
                    sql += " and dataType = " + dataType;
//                    queryWrapper.eq(YbAppealResult::getDataType, dataType);
                }
                if (sourceType != null) {
                    sql += " and sourceType = " + sourceType;
//                    queryWrapper.eq(YbAppealResult::getSourceType, sourceType);
                }
                if (state != null && state == 2) {
                    sql += " and state = 2 and repayState = 2";
                }
                if (value != null && value != "" && keyField.equals("arDoctorName")) {
                    List<String> strList = new ArrayList<>();
                    strList = this.iYbPersonService.findPersonCodeList(value);
                    if (strList.size() > 0) {
                        String docIn = "";
                        for (String code : strList) {
                            if (docIn.equals("")) {
                                docIn = "'" + code + "'";
                            } else {
                                docIn = ",'" + code + "'";
                            }
                        }
                        sql += " and doctorCode in (" + docIn + ")";
//                        queryWrapper.in(YbAppealResult::getDoctorCode, strList);
                    }
                }
                if (state != null && state == 1) {
                    sql += " and (state = 1 or (state = 2 and repayState = 1 ))";
                }
                queryWrapper.apply(sql);
                resultList = iYbAppealResultService.list(queryWrapper);

                applyDataList = this.iYbReconsiderApplyDataService.getApplyDataListView(applyDataList, keyField, value, typeno, dataType);

                if (resultList.size() > 0 && applyDataList.size() > 0) {
                    if (resultList.size() > applyDataList.size()) {
                        List<YbAppealResult> queryList = new ArrayList<>();
                        for (YbReconsiderApplyData item : applyDataList) {
                            queryList = resultList.stream().filter(s -> s.getApplyDataId().equals(item.getId())).collect(Collectors.toList());
                            if (queryList.size() > 0) {
                                YbAppealResultReportView arv = this.getYbAppealResultView(queryList.get(0), item);
                                list.add(arv);
                            }
                        }
                    } else {
                        for (YbAppealResult item : resultList) {
                            List<YbReconsiderApplyData> queryList = new ArrayList<>();
                            queryList = applyDataList.stream().filter(s -> s.getId().equals(item.getApplyDataId())).collect(Collectors.toList());
                            if (queryList.size() > 0) {
                                YbAppealResultReportView arv = this.getYbAppealResultView(item, queryList.get(0));
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
                            Comparator.comparing(YbAppealResultReportView::getTypeno)
                                    .thenComparing(YbAppealResultReportView::getDataType)
                                    .thenComparing(YbAppealResultReportView::getOrderNum)
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
    public IPage<YbAppealResultReportView> findAppealResultReportViewUserNew(QueryRequest request, YbAppealResultReportView ybAppealResultReportView, String keyField) {
        try {
            Integer areaType = ybAppealResultReportView.getAreaType();
            List<String> listStr = new ArrayList<>();
            listStr = this.applyDataStrList(ybAppealResultReportView);
            List<YbReconsiderApply> reconsiderApplyList = new ArrayList<>();
            if (listStr.size() > 0) {
                reconsiderApplyList = this.iYbReconsiderApplyService.findReconsiderApplyByApplyDateStrs(listStr, areaType,1);
            }
            Page<YbAppealResultReportView> page = new Page<>();
            if (reconsiderApplyList.size() > 0) {
                listStr.clear();
                for (YbReconsiderApply item : reconsiderApplyList) {
                    listStr.add(item.getApplyDateStr());
                }
                String value = ybAppealResultReportView.getCurrencyField();
                Integer typeno = ybAppealResultReportView.getTypeno();
                Integer dataType = ybAppealResultReportView.getDataType();
                if(dataType!= null && dataType == 2){
                    dataType = 0;
                }
                Integer state = ybAppealResultReportView.getState();
                Integer sourceType = ybAppealResultReportView.getSourceType();
                List<YbReconsiderApplyData> applyDataList = new ArrayList<>();
                List<YbAppealResultReportView> list = new ArrayList<>();
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
                sql = " applyDateStr in(" + dateStrIn + ") and areaType = " + areaType;
                sql += " and doctorCode = '" + ybAppealResultReportView.getArDoctorCode() + "'";
                if (value != null && value != "" && keyField.equals("orderNumber")) {
                    sql += " and orderNumber = '" + value + "'";
//                    queryWrapper.eq(YbAppealResult::getOrderNumber, value);
                }
                if (typeno != null) {
                    sql += " and typeno = " + typeno;
//                    queryWrapper.eq(YbAppealResult::getTypeno, typeno);
                }
                if (dataType != null) {
                    sql += " and dataType = " + dataType;
//                    queryWrapper.eq(YbAppealResult::getDataType, dataType);
                }
                if (sourceType != null) {
                    sql += " and sourceType = " + sourceType;
//                    queryWrapper.eq(YbAppealResult::getSourceType, sourceType);
                }
                if (state != null && state == 2) {
                    sql += " and state = 2 and repayState = 2";
                }
                if (state != null && state == 1) {
                    sql += " and (state = 1 or (state = 2 and repayState = 1 ))";
                }
                queryWrapper.apply(sql);
                resultList = iYbAppealResultService.list(queryWrapper);

                if (resultList.size() > 0) {
                    applyDataList = this.getApplyDataListUserView(resultList, listStr, keyField, value);
                }

                if (resultList.size() > 0 && applyDataList.size() > 0) {
                    if (resultList.size() > applyDataList.size()) {
                        List<YbAppealResult> queryList = new ArrayList<>();
                        for (YbReconsiderApplyData item : applyDataList) {
                            queryList = resultList.stream().filter(s -> s.getApplyDataId().equals(item.getId())).collect(Collectors.toList());
                            if (queryList.size() > 0) {
                                YbAppealResultReportView arv = this.getYbAppealResultView(queryList.get(0), item);
                                list.add(arv);
                            }
                        }
                    } else {
                        for (YbAppealResult item : resultList) {
                            List<YbReconsiderApplyData> queryList = new ArrayList<>();
                            queryList = applyDataList.stream().filter(s -> s.getId().equals(item.getApplyDataId())).collect(Collectors.toList());
                            if (queryList.size() > 0) {
                                YbAppealResultReportView arv = this.getYbAppealResultView(item, queryList.get(0));
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
                            Comparator.comparing(YbAppealResultReportView::getApplyDateStr)
                                    .thenComparing(YbAppealResultReportView::getTypeno)
                                    .thenComparing(YbAppealResultReportView::getDataType)
                                    .thenComparing(YbAppealResultReportView::getOrderNum)
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

    public List<YbReconsiderApplyData> getApplyDataListUserView(List<YbAppealResult> resultList, List<String> dateList, String keyField, String value) {
        dateList.clear();
        LambdaQueryWrapper<YbReconsiderApplyData> wrapperApplyData = new LambdaQueryWrapper<>();
        for (YbAppealResult item : resultList) {
            dateList.add(item.getApplyDataId());
        }
        wrapperApplyData.in(YbReconsiderApplyData::getId, dateList);
        if (value != null && value != "" && !keyField.equals("readyDoctorCode") && !keyField.equals("readyDoctorName") && !keyField.equals("orderNumber")) {
            if (keyField.equals("serialNo")) {
                wrapperApplyData.eq(YbReconsiderApplyData::getSerialNo, value);
            }
            if (keyField.equals("projectCode")) {
                wrapperApplyData.eq(YbReconsiderApplyData::getProjectCode, value);
            }
            if (keyField.equals("projectName")) {
                wrapperApplyData.eq(YbReconsiderApplyData::getProjectName, value);
            }
            if (keyField.equals("ruleName")) {
                wrapperApplyData.eq(YbReconsiderApplyData::getRuleName, value);
            }
        }

        return this.iYbReconsiderApplyDataService.list(wrapperApplyData);
    }

    private YbAppealResultReportView getYbAppealResultView(YbAppealResult ar, YbReconsiderApplyData reconsiderApplyData) {
        YbAppealResultReportView appealResultView = new YbAppealResultReportView();
        appealResultView.setPid(reconsiderApplyData.getPid());
        appealResultView.setSerialNo(reconsiderApplyData.getSerialNo());
        appealResultView.setBillNo(reconsiderApplyData.getBillNo());
        appealResultView.setProposalCode(reconsiderApplyData.getProposalCode());
        appealResultView.setProjectCode(reconsiderApplyData.getProjectCode());
        appealResultView.setProjectName(reconsiderApplyData.getProjectName());
        appealResultView.setNum(reconsiderApplyData.getNum());
        appealResultView.setMedicalPrice(reconsiderApplyData.getMedicalPrice());
        appealResultView.setRuleName(reconsiderApplyData.getRuleName());
        appealResultView.setDeductPrice(reconsiderApplyData.getDeductPrice());
        appealResultView.setDeductReason(reconsiderApplyData.getDeductReason());
        appealResultView.setRepaymentReason(reconsiderApplyData.getRepaymentReason());
        appealResultView.setDoctorName(reconsiderApplyData.getDoctorName());
        appealResultView.setDeptCode(reconsiderApplyData.getDeptCode());
        appealResultView.setDeptName(reconsiderApplyData.getDeptName());
        appealResultView.setEnterHospitalDateStr(reconsiderApplyData.getEnterHospitalDateStr());
        appealResultView.setEnterHospitalDate(reconsiderApplyData.getEnterHospitalDate());
        appealResultView.setOutHospitalDateStr(reconsiderApplyData.getOutHospitalDateStr());
        appealResultView.setOutHospitalDate(reconsiderApplyData.getOutHospitalDate());
        appealResultView.setCostDate(reconsiderApplyData.getCostDate());
        appealResultView.setCostDateStr(reconsiderApplyData.getCostDateStr());
        appealResultView.setHospitalizedNo(reconsiderApplyData.getHospitalizedNo());
        appealResultView.setTreatmentMode(reconsiderApplyData.getTreatmentMode());
        appealResultView.setSettlementDateStr(reconsiderApplyData.getSettlementDateStr());
        appealResultView.setSettlementDate(reconsiderApplyData.getSettlementDate());
        appealResultView.setPersonalNo(reconsiderApplyData.getPersonalNo());
        appealResultView.setInsuredName(reconsiderApplyData.getInsuredName());
        appealResultView.setInsuredType(reconsiderApplyData.getInsuredType());
        appealResultView.setCardNumber(reconsiderApplyData.getCardNumber());
        appealResultView.setAreaName(reconsiderApplyData.getAreaName());
        appealResultView.setVersionNumber(reconsiderApplyData.getVersionNumber());
        appealResultView.setBackAppeal(reconsiderApplyData.getBackAppeal());
        appealResultView.setTypeno(reconsiderApplyData.getTypeno());

        appealResultView.setDataType(reconsiderApplyData.getDataType());
        appealResultView.setOrderNumber(ar.getOrderNumber());
        appealResultView.setOrderNum(ar.getOrderNum());
        appealResultView.setApplyDateStr(ar.getApplyDateStr());
        appealResultView.setId(ar.getId());
        appealResultView.setManageId(ar.getManageId());
        appealResultView.setApplyDataId(reconsiderApplyData.getId());
        appealResultView.setVerifyId(ar.getVerifyId());
        appealResultView.setArDoctorCode(ar.getDoctorCode());
        appealResultView.setArDoctorName(ar.getDoctorName());
        appealResultView.setArDeptCode(ar.getDeptCode());
        appealResultView.setArDeptName(ar.getDeptName());

        appealResultView.setOperateReason(ar.getOperateReason());
        appealResultView.setOperateDate(ar.getOperateDate());
        appealResultView.setState(ar.getState());
        appealResultView.setSourceType(ar.getSourceType());
        appealResultView.setRepayState(ar.getRepayState());
//        appealResultView.setCurrencyField();
        appealResultView.setRelatelDataId(ar.getRelatelDataId());
        if (ar.getState() == YbDefaultValue.RESULTSTATE_1 || (ar.getState() == YbDefaultValue.RESULTSTATE_2 && ar.getRepayState() == YbDefaultValue.RESULTREPAYSTATE_1)) {
            appealResultView.setIsSuccess(1);
        } else if (ar.getState() == YbDefaultValue.RESULTSTATE_2 && ar.getRepayState() == YbDefaultValue.RESULTREPAYSTATE_2) {
            appealResultView.setIsSuccess(0);
        } else {
            appealResultView.setIsSuccess(2);
        }
//        appealResultView.setAdjustPrice(0);
        appealResultView.setAreaType(ar.getAreaType());
        return appealResultView;
    }


    @Override
    @Transactional
    public void createYbAppealResultReportView(YbAppealResultReportView ybAppealResultReportView) {
//        ybAppealResultReportView.setCreateTime(new Date());
//        ybAppealResultReportView.setIsDeletemark(1);
        this.save(ybAppealResultReportView);
    }

    @Override
    @Transactional
    public void updateYbAppealResultReportView(YbAppealResultReportView ybAppealResultReportView) {
//        ybAppealResultReportView.setModifyTime(new Date());
        this.baseMapper.updateYbAppealResultReportView(ybAppealResultReportView);
    }

    @Override
    @Transactional
    public void deleteYbAppealResultReportViews(String[] Ids) {
        List<String> list = Arrays.asList(Ids);
        this.baseMapper.deleteBatchIds(list);
    }

    private List<String> applyDataStrList(YbAppealResultReportView ybAppealResultReportView) {
        List<String> listStr = new ArrayList<>();
        //ApplyDateFrom getApplyDateTo 存储的格式是 2020-09
        if (ybAppealResultReportView.getApplyDateFrom().equals(ybAppealResultReportView.getApplyDateTo())) {
            listStr.add(ybAppealResultReportView.getApplyDateFrom());
        } else {
            listStr = DataTypeHelpers.stringApplyDateStrToList(ybAppealResultReportView.getApplyDateFrom(), ybAppealResultReportView.getApplyDateTo());
        }
        return listStr;
    }



    @Override
    public List<YbAppealResultReportView> findYbAppealResultReportLists(YbAppealResultReportView ybAppealResultReportView, String keyField, boolean isUser) {
        try {
            List<YbAppealResultReportView> list = new ArrayList<>();
            List<String> listStr = new ArrayList<>();
            if (isUser) {
                listStr = this.applyDataStrList(ybAppealResultReportView);
            } else {
                listStr.add(ybAppealResultReportView.getApplyDateStr());
            }
            String pid = null;
            List<YbReconsiderApply> reconsiderApplyList = new ArrayList<>();
            if (listStr.size() > 0) {
                reconsiderApplyList = this.iYbReconsiderApplyService.findReconsiderApplyByApplyDateStrs(listStr, ybAppealResultReportView.getAreaType(),1);
                if (reconsiderApplyList.size() > 0) {
                    pid = reconsiderApplyList.get(0).getId();
                }
            }
            listStr.clear();
            for (YbReconsiderApply item : reconsiderApplyList) {
                listStr.add(item.getApplyDateStr());
            }
            if (pid != null) {
                ybAppealResultReportView.setPid(pid);
                boolean isLike = false;
                if (ybAppealResultReportView.getCurrencyField() != null && ybAppealResultReportView.getCurrencyField() != "") {
                    if (!keyField.equals("arDoctorCode") && !keyField.equals("arDoctorName")) {
                        isLike = true;
                    }
                    if (keyField.equals("arDoctorCode")) {
                        ybAppealResultReportView.setArDoctorCode(ybAppealResultReportView.getCurrencyField());
                    }
                    if (keyField.equals("arDoctorName")) {
                        ybAppealResultReportView.setArDoctorName(ybAppealResultReportView.getCurrencyField());
                    }
                }
                if (isLike) {
                    list = this.baseMapper.findAppealResultReportLikeList(ybAppealResultReportView, listStr, keyField);
                } else {
                    list = this.baseMapper.findAppealResultReportList(ybAppealResultReportView, listStr, keyField);
                }
            }
            return list;
        } catch (Exception e) {
            log.error("获取字典信息失败", e);
            return null;
        }
    }
}