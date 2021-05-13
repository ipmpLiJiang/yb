package cc.mrbird.febs.yb.service.impl;

import cc.mrbird.febs.com.controller.DataTypeHelpers;
import cc.mrbird.febs.common.domain.QueryRequest;
import cc.mrbird.febs.common.utils.SortUtil;
import cc.mrbird.febs.yb.dao.YbAppealResultViewMapper;
import cc.mrbird.febs.yb.entity.*;
import cc.mrbird.febs.yb.manager.YbApplyDataManager;
import cc.mrbird.febs.yb.service.*;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.extern.slf4j.Slf4j;
import net.sf.jsqlparser.parser.CCJSqlParserUtil;
import org.apache.ibatis.annotations.Param;
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
 * @since 2020-08-12
 */
@Slf4j
@Service("IYbAppealResultViewService")
@Transactional(propagation = Propagation.SUPPORTS, readOnly = true, rollbackFor = Exception.class)
public class YbAppealResultViewServiceImpl extends ServiceImpl<YbAppealResultViewMapper, YbAppealResultView> implements IYbAppealResultViewService {

    @Autowired
    IYbAppealResultService iYbAppealResultService;

    @Autowired
    IYbAppealManageService iYbAppealManageService;

    @Autowired
    IYbReconsiderApplyService iYbReconsiderApplyService;

    @Autowired
    IYbReconsiderApplyDataService iYbReconsiderApplyDataService;

    @Autowired
    public IYbReconsiderInpatientfeesService iYbReconsiderInpatientfeesService;

    @Autowired
    YbApplyDataManager ybApplyDataManager;

    @Autowired
    IYbPersonService iYbPersonService;

    //打包下载
    @Override
    public IPage<YbAppealResultView> findYbAppealResultViews(QueryRequest request, YbAppealResultView ybAppealResultView) {
        try {
            LambdaQueryWrapper<YbAppealResultView> queryWrapper = new LambdaQueryWrapper<>();
            String sql = "(";
            sql += " applyDateStr ='" + ybAppealResultView.getApplyDateStr() + "' ";
            if (ybAppealResultView.getTypeno() != null) {
                if (ybAppealResultView.getTypeno() == YbDefaultValue.TYPENO_1) {
                    sql += " and typeno = " + YbDefaultValue.TYPENO_1;
                } else {
                    sql += " and typeno = " + YbDefaultValue.TYPENO_2;
                }
            }
            if (ybAppealResultView.getDataType() != null) {
                if (ybAppealResultView.getDataType() == YbDefaultValue.DATATYPE_0) {
                    sql += " and dataType = " + YbDefaultValue.DATATYPE_0;
                } else {
                    sql += " and dataType = " + YbDefaultValue.DATATYPE_1;
                }
            }
            if (ybAppealResultView.getSourceType() != null) {
                sql += " and sourceType = " + ybAppealResultView.getSourceType();
            }
            if (ybAppealResultView.getState() != null) {
                if (ybAppealResultView.getState() == 12) {
                    sql += " and STATE IN (1,2)";
                }
                if (ybAppealResultView.getState() == YbDefaultValue.RESULTSTATE_1) {
                    sql += " and STATE = " + YbDefaultValue.RESULTSTATE_1;
                }
            }

            sql += ")";
            if (ybAppealResultView.getCurrencyField() != null && !"".equals(ybAppealResultView.getCurrencyField())) {
                if (ybAppealResultView.getDataType() != null) {
                    if (ybAppealResultView.getDataType() == 0) {
                        sql += " and (serialNo like'%" + ybAppealResultView.getCurrencyField() + "%'" +
                                " or billNo like'%" + ybAppealResultView.getCurrencyField() + "%'" +
                                " or proposalCode like'%" + ybAppealResultView.getCurrencyField() + "%'" +
                                " or projectCode like'%" + ybAppealResultView.getCurrencyField() + "%'" +
                                " or projectName like'%" + ybAppealResultView.getCurrencyField() + "%'" +
                                " or ruleName like'%" + ybAppealResultView.getCurrencyField() + "%'" +
                                " or deductReason like'%" + ybAppealResultView.getCurrencyField() + "%')";
                    } else {
                        sql += " and (serialNo like'%" + ybAppealResultView.getCurrencyField() + "%'" +
                                " or billNo like'%" + ybAppealResultView.getCurrencyField() + "%'" +
                                " or ruleName like'%" + ybAppealResultView.getCurrencyField() + "%'" +
                                " or personalNo like'%" + ybAppealResultView.getCurrencyField() + "%')";
                    }
                } else {
                    sql += " and (serialNo like'%" + ybAppealResultView.getCurrencyField() + "%'" +
                            " or billNo like'%" + ybAppealResultView.getCurrencyField() + "%'" +
                            " or proposalCode like'%" + ybAppealResultView.getCurrencyField() + "%'" +
                            " or projectCode like'%" + ybAppealResultView.getCurrencyField() + "%'" +
                            " or projectName like'%" + ybAppealResultView.getCurrencyField() + "%'" +
                            " or ruleName like'%" + ybAppealResultView.getCurrencyField() + "%'" +
                            " or deductReason like'%" + ybAppealResultView.getCurrencyField() + "%')";
                }
            }
            queryWrapper.apply(sql);

            Page<YbAppealResultView> page = new Page<>();
            SortUtil.handlePageSort(request, page, false);//true 是属性  false是数据库字段可两个
            return this.page(page, queryWrapper);
        } catch (Exception e) {
            log.error("获取字典信息失败", e);
            return null;
        }
    }

    //还款-查看还款明细
    @Override
    public IPage<YbAppealResultView> findAppealResultViewResets(QueryRequest request, YbAppealResultView ybAppealResultView) {
        try {
            YbReconsiderApply reconsiderApply = iYbReconsiderApplyService.findReconsiderApplyByApplyDateStrs(ybAppealResultView.getApplyDateStr(), ybAppealResultView.getAreaType());
            Page<YbAppealResultView> page = new Page<>();
            if (reconsiderApply != null) {
                ybAppealResultView.setPid(reconsiderApply.getId());
                int count = this.baseMapper.findAppealResultResetCount(ybAppealResultView);
                if (count > 0) {
                    page.setSearchCount(false);
                    SortUtil.handlePageSort(request, page, false);//true 是属性  false是数据库字段可两个
                    IPage<YbAppealResultView> pg = this.baseMapper.findAppealResultResetView(page, ybAppealResultView);
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

    //数据剔除——>查看剔除明细
    @Override
    public IPage<YbAppealResultView> findAppealResultViewRepays(QueryRequest request, YbAppealResultView ybAppealResultView) {
        try {
            LambdaQueryWrapper<YbAppealResultView> queryWrapper = new LambdaQueryWrapper<>();

            String sql = "(";
            sql += " applyDateStr ='" + ybAppealResultView.getApplyDateStr() + "' ";
            if (ybAppealResultView.getSourceType() != null) {
                sql += " and sourceType = " + ybAppealResultView.getSourceType();
            }
            if (ybAppealResultView.getState() != null) {
                sql += " and STATE = " + ybAppealResultView.getState();
            }

            sql += ")";
            if (ybAppealResultView.getOrderNumber() != null && !"".equals(ybAppealResultView.getOrderNumber())) {

            }
            queryWrapper.apply(sql);

            Page<YbAppealResultView> page = new Page<>();
            SortUtil.handlePageSort(request, page, false);//true 是属性  false是数据库字段可两个
            return this.page(page, queryWrapper);
        } catch (Exception e) {
            log.error("获取字典信息失败", e);
            return null;
        }
    }

    @Override
    public IPage<YbAppealResultView> findYbAppealResultViewList(QueryRequest request, YbAppealResultView ybAppealResultView) {
        try {
            Page<YbAppealResultView> page = new Page<>();
            SortUtil.handlePageSort(request, page, false);//true 是属性  false是数据库字段可两个
            return this.baseMapper.findYbAppealResultView(page, ybAppealResultView);
        } catch (Exception e) {
            log.error("获取VIEW失败", e);
            return null;
        }
    }

    @Override
    public IPage<YbAppealResultView> findAppealResultViews(QueryRequest request, YbAppealResultView ybAppealResultView, String keyField) {
        try {
            YbReconsiderApply reconsiderApply = iYbReconsiderApplyService.findReconsiderApplyByApplyDateStrs(ybAppealResultView.getApplyDateStr(), ybAppealResultView.getAreaType());
            Page<YbAppealResultView> page = new Page<>();
            if (reconsiderApply != null) {
                ybAppealResultView.setPid(reconsiderApply.getId());
                boolean isLike = false;
                if (ybAppealResultView.getCurrencyField() != null && ybAppealResultView.getCurrencyField() != "") {
                    if (!keyField.equals("doctorCode") && !keyField.equals("doctorName")) {
                        isLike = true;
                    }
                    if (keyField.equals("doctorCode")) {
                        ybAppealResultView.setArDoctorCode(ybAppealResultView.getCurrencyField());
                    }
                    if (keyField.equals("doctorName")) {
                        ybAppealResultView.setArDoctorName(ybAppealResultView.getCurrencyField());
                    }
                }
                int count = 0;
                if (isLike) {
                    count = this.baseMapper.findAppealResultLikeCount(ybAppealResultView, keyField);
                } else {
                    count = this.baseMapper.findAppealResultCount(ybAppealResultView);
                }
                if (count > 0) {
                    page.setSearchCount(false);
                    SortUtil.handlePageSort(request, page, false);//true 是属性  false是数据库字段可两个
                    IPage<YbAppealResultView> pg = null;
                    if (isLike) {
                        pg = this.baseMapper.findAppealResultLikeView(page, ybAppealResultView, keyField);
                    } else {
                        pg = this.baseMapper.findAppealResultView(page, ybAppealResultView);
                    }
                    pg.setTotal(count);
                    return pg;
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

    @Override
    public IPage<YbAppealResultView> findAppealResultViewNew(QueryRequest request, YbAppealResultView ybAppealResultView, String keyField) {
        try {
            String applyDateStr = ybAppealResultView.getApplyDateStr();
            Integer areaType = ybAppealResultView.getAreaType();
            YbReconsiderApply reconsiderApply = iYbReconsiderApplyService.findReconsiderApplyByApplyDateStrs(applyDateStr, areaType);
            Page<YbAppealResultView> page = new Page<>();
            if (reconsiderApply != null) {
                String value = ybAppealResultView.getCurrencyField();
                Integer typeno = ybAppealResultView.getTypeno();
                Integer dataType = ybAppealResultView.getDataType();
                Integer sourceType = ybAppealResultView.getSourceType();
                List<YbReconsiderApplyData> applyDataList = ybApplyDataManager.getApplyDatas(reconsiderApply.getId(), applyDateStr + "-" + areaType);
                List<YbAppealResultView> list = new ArrayList<>();
                List<YbAppealResult> resultList = new ArrayList<>();
                if (value != null && value != "" && keyField.equals("doctorName")) {
                    LambdaQueryWrapper<YbAppealResult> queryWrapper = new LambdaQueryWrapper<>();
                    queryWrapper.eq(YbAppealResult::getApplyDateStr, applyDateStr);
                    queryWrapper.eq(YbAppealResult::getAreaType, areaType);
                    List<String> strList = new ArrayList<>();
                    strList = this.iYbPersonService.findPersonCodeList(value);
                    if (strList.size() > 0) {
                        queryWrapper.in(YbAppealResult::getDoctorCode, strList);
                    }
                    if (typeno != null) {
                        queryWrapper.eq(YbAppealResult::getTypeno, typeno);
                    }
                    if (dataType != null) {
                        queryWrapper.eq(YbAppealResult::getDataType, dataType);
                    }
                    if (sourceType != null) {
                        queryWrapper.eq(YbAppealResult::getSourceType, sourceType);
                    }
                    resultList = iYbAppealResultService.list(queryWrapper);

                } else{
                    YbAppealResult queryResult = new YbAppealResult();
                    queryResult.setApplyDateStr(applyDateStr);
                    queryResult.setAreaType(areaType);
                    if (value != null && value != "" && keyField.equals("doctorCode")) {
                        queryResult.setDoctorCode(value);
                    }
                    if (ybAppealResultView.getArDoctorCode() != null) {
                        queryResult.setDoctorCode(ybAppealResultView.getArDoctorCode());
                    }
                    if (value != null && value != "" && keyField.equals("orderNumber")) {
                        queryResult.setOrderNumber(value);
                    }
                    if (typeno != null) {
                        queryResult.setTypeno(typeno);
                    }
                    if (dataType != null) {
                        queryResult.setDataType(dataType);
                    }
                    if (sourceType != null) {
                        queryResult.setSourceType(sourceType);
                    }
                    resultList = iYbAppealResultService.findAppealResultList(queryResult);
                }

                applyDataList = this.iYbReconsiderApplyDataService.getApplyDataListView(applyDataList, keyField, value, typeno, dataType);

                if (resultList.size() > 0 && applyDataList.size() > 0) {
                    if (resultList.size() > applyDataList.size()) {
                        List<YbAppealResult> queryList = new ArrayList<>();
                        for (YbReconsiderApplyData item : applyDataList) {
                            queryList = resultList.stream().filter(s -> s.getApplyDataId().equals(item.getId())).collect(Collectors.toList());
                            if (queryList.size() > 0) {
                                YbAppealResultView arv = this.getYbAppealResultView(queryList.get(0), item);
                                list.add(arv);
                            }
                        }
                    } else {
                        for (YbAppealResult item : resultList) {
                            List<YbReconsiderApplyData> queryList = new ArrayList<>();
                            queryList = applyDataList.stream().filter(s -> s.getId().equals(item.getApplyDataId())).collect(Collectors.toList());
                            if (queryList.size() > 0) {
                                YbAppealResultView arv = this.getYbAppealResultView(item, queryList.get(0));
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
                    list = list.stream().sorted(Comparator.comparing(YbAppealResultView::getOrderNum)).skip(current).limit(page.getSize()).collect(Collectors.toList());
                    page.setRecords(list);
                }
            }
            return page;

        } catch (Exception e) {
            log.error("获取字典信息失败", e);
            return null;
        }
    }

    private YbAppealResultView getYbAppealResultView(YbAppealResult ar, YbReconsiderApplyData reconsiderApplyData) {
        YbAppealResultView appealResultView = new YbAppealResultView();
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
        appealResultView.setOutHospitalDateStr(reconsiderApplyData.getOutHospitalDateStr());
        appealResultView.setCostDateStr(reconsiderApplyData.getCostDateStr());
        appealResultView.setHospitalizedNo(reconsiderApplyData.getHospitalizedNo());
        appealResultView.setTreatmentMode(reconsiderApplyData.getTreatmentMode());
        appealResultView.setSettlementDateStr(reconsiderApplyData.getSettlementDateStr());
        appealResultView.setPersonalNo(reconsiderApplyData.getPersonalNo());
        appealResultView.setInsuredName(reconsiderApplyData.getInsuredName());
        appealResultView.setInsuredType(reconsiderApplyData.getInsuredType());
        appealResultView.setCardNumber(reconsiderApplyData.getCardNumber());
        appealResultView.setAreaName(reconsiderApplyData.getAreaName());
        appealResultView.setVersionNumber(reconsiderApplyData.getVersionNumber());
        appealResultView.setBackAppeal(reconsiderApplyData.getBackAppeal());
        appealResultView.setTypeno(reconsiderApplyData.getTypeno());
        appealResultView.setApplyDateStr(ar.getApplyDateStr());
        appealResultView.setDataType(reconsiderApplyData.getDataType());
        appealResultView.setOrderNumber(reconsiderApplyData.getOrderNumber());
        appealResultView.setOrderNum(reconsiderApplyData.getOrderNum());
        appealResultView.setId(ar.getId());
        appealResultView.setApplyDataId(reconsiderApplyData.getId());
        appealResultView.setVerifyId(ar.getVerifyId());
        appealResultView.setArDoctorCode(ar.getDoctorCode());
        appealResultView.setArDoctorName(ar.getDoctorName());
        appealResultView.setArDeptCode(ar.getDeptCode());
        appealResultView.setArDeptName(ar.getDeptName());

        appealResultView.setArOrderDoctorCode(ar.getOrderDoctorCode());
        appealResultView.setArOrderDoctorName(ar.getOrderDoctorName());
        appealResultView.setArOrderDeptCode(ar.getOrderDeptCode());
        appealResultView.setArOrderDeptName(ar.getOrderDeptName());

        appealResultView.setOperateReason(ar.getOperateReason());
        appealResultView.setOperateDate(ar.getOperateDate());
        appealResultView.setSourceType(ar.getSourceType());
        appealResultView.setState(ar.getState());
        appealResultView.setRepayState(ar.getRepayState());
//        appealResultView.setCurrencyField();
        appealResultView.setRelatelDataId(ar.getRelatelDataId());
        appealResultView.setAreaType(ar.getAreaType());
        return appealResultView;
    }


    @Override
    @Transactional
    public void createYbAppealResultView(YbAppealResultView ybAppealResultView) {
//        ybAppealResultView.setCreateTime(new Date());
//        ybAppealResultView.setIsDeletemark(1);
        this.save(ybAppealResultView);
    }

    @Override
    @Transactional
    public void updateYbAppealResultView(YbAppealResultView ybAppealResultView) {
//        ybAppealResultView.setModifyTime(new Date());
        this.baseMapper.updateYbAppealResultView(ybAppealResultView);
    }

    @Override
    @Transactional
    public void deleteYbAppealResultViews(String[] Ids) {
        List<String> list = Arrays.asList(Ids);
        this.baseMapper.deleteBatchIds(list);
    }

    //打包下载导出列表
    @Override
    public List<YbAppealResultView> findAppealResultHandleViewLists(@Param("ybAppealResultView") YbAppealResultView ybAppealResultView) {
        List<YbAppealResultView> list = new ArrayList<>();
        try {
            YbReconsiderApply reconsiderApply = iYbReconsiderApplyService.findReconsiderApplyByApplyDateStrs(ybAppealResultView.getApplyDateStr(), ybAppealResultView.getAreaType());
            if (reconsiderApply != null) {
                ybAppealResultView.setPid(reconsiderApply.getId());
                YbReconsiderInpatientfees rif = new YbReconsiderInpatientfees();
                rif.setApplyDateStr(reconsiderApply.getApplyDateStr());
                rif.setAreaType(reconsiderApply.getAreaType());
                rif.setTypeno(ybAppealResultView.getTypeno());
                List<YbReconsiderInpatientfees> rifList = iYbReconsiderInpatientfeesService.findReconsiderInpatientfeesList(rif);

                LambdaQueryWrapper<YbAppealManage> wrapperManage = new LambdaQueryWrapper<>();
                wrapperManage.eq(YbAppealManage::getApplyDateStr, ybAppealResultView.getApplyDateStr());
                wrapperManage.eq(YbAppealManage::getAreaType, ybAppealResultView.getAreaType());
                wrapperManage.eq(YbAppealManage::getSourceType, ybAppealResultView.getSourceType());
                List<Integer> asList = new ArrayList<>();
                asList.add(0);
                asList.add(1);
                asList.add(2);
                wrapperManage.in(YbAppealManage::getAcceptState, asList);
                List<YbAppealManage> manageList = iYbAppealManageService.list(wrapperManage);

                List<YbReconsiderInpatientfees> queryRifList = new ArrayList<>();
                List<YbAppealManage> queryManageList = new ArrayList<>();
                list = this.baseMapper.findAppealResultHandleList(ybAppealResultView);
                for (int i = 0; i < list.size(); i++) {
                    String applyDataId = list.get(i).getApplyDataId();
                    queryRifList = rifList.stream().filter(s -> s.getApplyDataId().equals(applyDataId)).collect(Collectors.toList());
                    if (list.get(i).getArDoctorCode() == null) {
                        queryManageList = manageList.stream().filter(s -> s.getApplyDataId().equals(applyDataId)).collect(Collectors.toList());
                        if (queryManageList.size() > 0) {
                            list.get(i).setArDoctorCode(queryManageList.get(0).getReadyDoctorCode());
                            list.get(i).setArDoctorName(queryManageList.get(0).getReadyDoctorName());
                            list.get(i).setArDeptCode(queryManageList.get(0).getReadyDeptCode());
                            list.get(i).setArDeptName(queryManageList.get(0).getReadyDeptName());
                        }
                    }
                    if (queryRifList.size() > 0) {
                        list.get(i).setOrderDeptId(queryRifList.get(0).getDeptId());
                        list.get(i).setOrderDeptName(queryRifList.get(0).getDeptName());
                        list.get(i).setOrderDocId(queryRifList.get(0).getOrderDocId());
                        list.get(i).setOrderDocName(queryRifList.get(0).getOrderDocName());
                        list.get(i).setExcuteDeptId(queryRifList.get(0).getExcuteDeptId());
                        list.get(i).setExcuteDeptName(queryRifList.get(0).getExcuteDeptName());
                        list.get(i).setExcuteDocId(queryRifList.get(0).getExcuteDocId());
                        list.get(i).setExcuteDocName(queryRifList.get(0).getExcuteDocName());
                        list.get(i).setAttendDocId(queryRifList.get(0).getAttendDocId());
                        list.get(i).setAttendDocName(queryRifList.get(0).getAttendDocName());
                        list.get(i).setItemTypeCode(queryRifList.get(0).getItemTypeCode());
                        list.get(i).setItemTypeName(queryRifList.get(0).getItemTypeName());
                        list.get(i).setFeeOperatorId(queryRifList.get(0).getFeeOperatorId());
                        list.get(i).setFeeOperatorName(queryRifList.get(0).getFeeOperatorName());
                        list.get(i).setFeeDeptId(queryRifList.get(0).getFeeDeptId());
                        list.get(i).setFeeDeptName(queryRifList.get(0).getFeeDeptName());
                    }
                }
            }
            return list;
        } catch (Exception e) {
            log.error("获取字典信息失败", e);
            return list;
        }
    }

    //打包下载导出列表
    @Override
    public List<YbAppealResultView> findAppealResultViewLists(YbAppealResultView ybAppealResultView) {
        List<YbAppealResultView> list = new ArrayList<>();
        try {
            YbReconsiderApply reconsiderApply = iYbReconsiderApplyService.findReconsiderApplyByApplyDateStrs(ybAppealResultView.getApplyDateStr(), ybAppealResultView.getAreaType());
            if (reconsiderApply != null) {
                ybAppealResultView.setPid(reconsiderApply.getId());
                YbReconsiderInpatientfees rif = new YbReconsiderInpatientfees();
                rif.setApplyDateStr(reconsiderApply.getApplyDateStr());
                rif.setAreaType(reconsiderApply.getAreaType());
                rif.setTypeno(ybAppealResultView.getTypeno());
                List<YbReconsiderInpatientfees> rifList = iYbReconsiderInpatientfeesService.findReconsiderInpatientfeesList(rif);
                List<YbReconsiderInpatientfees> queryRifList = new ArrayList<>();
                List<YbAppealManage> queryManageList = new ArrayList<>();
                LambdaQueryWrapper<YbAppealManage> wrapperManage = new LambdaQueryWrapper<>();
                wrapperManage.eq(YbAppealManage::getApplyDateStr, ybAppealResultView.getApplyDateStr());
                wrapperManage.eq(YbAppealManage::getAreaType, ybAppealResultView.getAreaType());
                wrapperManage.eq(YbAppealManage::getSourceType, ybAppealResultView.getSourceType());
                wrapperManage.eq(YbAppealManage::getTypeno, ybAppealResultView.getTypeno());
                List<Integer> asList = new ArrayList<>();
                asList.add(0);
                asList.add(1);
                asList.add(2);
                wrapperManage.in(YbAppealManage::getAcceptState, asList);
                List<YbAppealManage> manageList = iYbAppealManageService.list(wrapperManage);
                list = this.baseMapper.findAppealResultList(ybAppealResultView);
                for (int i = 0; i < list.size(); i++) {
                    String applyDataId = list.get(i).getApplyDataId();
                    queryRifList = rifList.stream().filter(s -> s.getApplyDataId().equals(applyDataId)).collect(Collectors.toList());
                    if (list.get(i).getArDoctorCode() == null) {
                        queryManageList = manageList.stream().filter(s -> s.getApplyDataId().equals(applyDataId)).collect(Collectors.toList());
                        if (queryManageList.size() > 0) {
                            list.get(i).setArDoctorCode(queryManageList.get(0).getReadyDoctorCode());
                            list.get(i).setArDoctorName(queryManageList.get(0).getReadyDoctorName());
                            list.get(i).setArDeptCode(queryManageList.get(0).getReadyDeptCode());
                            list.get(i).setArDeptName(queryManageList.get(0).getReadyDeptName());
                        }
                    }
                    if (queryRifList.size() > 0) {
                        list.get(i).setOrderDeptId(queryRifList.get(0).getDeptId());
                        list.get(i).setOrderDeptName(queryRifList.get(0).getDeptName());
                        list.get(i).setOrderDocId(queryRifList.get(0).getOrderDocId());
                        list.get(i).setOrderDocName(queryRifList.get(0).getOrderDocName());
                        list.get(i).setExcuteDeptId(queryRifList.get(0).getExcuteDeptId());
                        list.get(i).setExcuteDeptName(queryRifList.get(0).getExcuteDeptName());
                        list.get(i).setExcuteDocId(queryRifList.get(0).getExcuteDocId());
                        list.get(i).setExcuteDocName(queryRifList.get(0).getExcuteDocName());
                        list.get(i).setAttendDocId(queryRifList.get(0).getAttendDocId());
                        list.get(i).setAttendDocName(queryRifList.get(0).getAttendDocName());
                        list.get(i).setItemTypeCode(queryRifList.get(0).getItemTypeCode());
                        list.get(i).setItemTypeName(queryRifList.get(0).getItemTypeName());
                        list.get(i).setFeeOperatorId(queryRifList.get(0).getFeeOperatorId());
                        list.get(i).setFeeOperatorName(queryRifList.get(0).getFeeOperatorName());
                        list.get(i).setFeeDeptId(queryRifList.get(0).getFeeDeptId());
                        list.get(i).setFeeDeptName(queryRifList.get(0).getFeeDeptName());
                    }
                }
            }
            return list;
        } catch (Exception e) {
            log.error("获取字典信息失败", e);
            return list;
        }
    }

    //打包下载 查找部门
    @Override
    public List<YbAppealResultDownLoad> findYbAppealResultDownLoadList(YbAppealResultView ybAppealResultView) {
        List<YbAppealResultDownLoad> downLoadList = new ArrayList<>();
        try {
            YbReconsiderApply reconsiderApply = iYbReconsiderApplyService.findReconsiderApplyByApplyDateStrs(ybAppealResultView.getApplyDateStr(), ybAppealResultView.getAreaType());
            if (reconsiderApply != null) {
                ybAppealResultView.setPid(reconsiderApply.getId());
                int typeNo = 0;

                if (ybAppealResultView.getTypeno() != null) {
                    typeNo = ybAppealResultView.getTypeno();
                }
                int dataType = ybAppealResultView.getDataType();
                // typeNo 可能为空
                List<YbResultDownLoad> deptList = this.iYbAppealResultService.findAppealResultGroupDepts(ybAppealResultView);

                String f = "";
                if (dataType == YbDefaultValue.DATATYPE_0) {
                    f = "明细扣款_";
                } else {
                    f = "主单扣款_";
                }

                String typeName = "";
                if (typeNo == YbDefaultValue.TYPENO_1) {
                    typeName = "_第一版_" + f;
                } else if (typeNo == YbDefaultValue.TYPENO_2) {
                    typeName = "_第二版_" + f;
                } else {
                    typeName = "_手动复议_";
                }
                for (YbResultDownLoad item : deptList) {
                    YbAppealResultDownLoad downLoad = new YbAppealResultDownLoad();
                    downLoad.setKey(item.getId());
                    downLoad.setDeptId(item.getDeptId());
                    downLoad.setDeptName(item.getDeptName());
                    downLoad.setFileName(ybAppealResultView.getApplyDateStr() + typeName + item.getDeptName() + ".zip");
                    downLoadList.add(downLoad);
                }
            }
            return downLoadList;
        } catch (Exception e) {
            log.error("获取字典信息失败", e);
            return downLoadList;
        }
    }

    //打包下载 汇总科室 未设置
    @Override
    public List<YbAppealResultDownLoad> findYbAppealResultNotDeptList(YbAppealResultView ybAppealResultView) {
        List<YbAppealResultDownLoad> downLoadList = new ArrayList<>();
        try {
            YbReconsiderApply reconsiderApply = iYbReconsiderApplyService.findReconsiderApplyByApplyDateStrs(ybAppealResultView.getApplyDateStr(), ybAppealResultView.getAreaType());
            if (reconsiderApply != null) {
                // typeNo 可能为空
                List<YbResultDownLoad> deptList = this.iYbAppealResultService.findAppealResultNotDepts(ybAppealResultView);

                for (YbResultDownLoad item : deptList) {
                    YbAppealResultDownLoad downLoad = new YbAppealResultDownLoad();
                    downLoad.setKey(item.getId());
                    downLoad.setDeptId(item.getDeptId());
                    downLoad.setDeptName(item.getDeptName());
                    downLoadList.add(downLoad);
                }
            }
            return downLoadList;
        } catch (Exception e) {
            log.error("获取字典信息失败", e);
            return downLoadList;
        }
    }

    //打包下载 查找部门
    @Override
    public List<YbAppealResultDownLoad> findAppealResultDownLoadSumList(YbAppealResultView ybAppealResultView) {
        List<YbAppealResultDownLoad> downLoadList = new ArrayList<>();
        try {
            YbReconsiderApply reconsiderApply = iYbReconsiderApplyService.findReconsiderApplyByApplyDateStrs(ybAppealResultView.getApplyDateStr(), ybAppealResultView.getAreaType());
            if (reconsiderApply != null) {
                ybAppealResultView.setApplyDateStr(reconsiderApply.getApplyDateStr());
                int typeNo = 0;

                if (ybAppealResultView.getTypeno() != null) {
                    typeNo = ybAppealResultView.getTypeno();
                }
                int dataType = ybAppealResultView.getDataType();
                // typeNo 可能为空
                List<YbResultDownLoad> deptList = this.iYbAppealResultService.findAppealResultGroupSumDepts(ybAppealResultView);

                String f = "";
                if (dataType == YbDefaultValue.DATATYPE_0) {
                    f = "明细扣款_";
                } else {
                    f = "主单扣款_";
                }

                String typeName = "";
                if (typeNo == YbDefaultValue.TYPENO_1) {
                    typeName = "_第一版_" + f;
                } else if (typeNo == YbDefaultValue.TYPENO_2) {
                    typeName = "_第二版_" + f;
                } else {
                    typeName = "_手动复议_";
                }

                for (YbResultDownLoad item : deptList) {
                    YbAppealResultDownLoad downLoad = new YbAppealResultDownLoad();
                    downLoad.setKey(item.getId());
                    downLoad.setDeptName(item.getDeptName());
                    downLoad.setFileName(ybAppealResultView.getApplyDateStr() + typeName + item.getDeptName() + ".zip");
                    downLoadList.add(downLoad);
                }
            }
            return downLoadList;
        } catch (Exception e) {
            log.error("获取字典信息失败", e);
            return downLoadList;
        }
    }


}