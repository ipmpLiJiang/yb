package cc.mrbird.febs.yb.service.impl;

import cc.mrbird.febs.com.controller.DataTypeHelpers;
import cc.mrbird.febs.common.domain.QueryRequest;
import cc.mrbird.febs.common.utils.SortUtil;
import cc.mrbird.febs.system.domain.User;
import cc.mrbird.febs.yb.dao.YbAppealManageViewMapper;
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
 * <p>
 * VIEW 服务实现类
 * </p>
 *
 * @author viki
 * @since 2020-07-30
 */
@Slf4j
@Service("IYbAppealManageViewService")
@Transactional(propagation = Propagation.SUPPORTS, readOnly = true, rollbackFor = Exception.class)
public class YbAppealManageViewServiceImpl extends ServiceImpl<YbAppealManageViewMapper, YbAppealManageView> implements IYbAppealManageViewService {

    @Autowired
    IYbAppealConfireDataService iYbAppealConfireDataService;

    @Autowired
    IYbAppealManageService iYbAppealManageService;

    @Autowired
    IYbReconsiderApplyService iYbReconsiderApplyService;

    @Autowired
    IYbReconsiderApplyDataService iYbReconsiderApplyDataService;

    @Autowired
    YbApplyDataManager ybApplyDataManager;

    @Autowired
    IYbPersonService iYbPersonService;

    @Override
    public int findYbAppealManageCounts(YbAppealManageView ybAppealManageView, String keyField){
        int count = 0;
        try {
            YbReconsiderApply reconsiderApply = iYbReconsiderApplyService.findReconsiderApplyByApplyDateStrs(ybAppealManageView.getApplyDateStr(),ybAppealManageView.getAreaType());
            Page<YbAppealManageView> page = new Page<>();
            if (reconsiderApply != null) {
                ybAppealManageView.setPid(reconsiderApply.getId());
                boolean isLike = false;
                String val = ybAppealManageView.getCurrencyField();
                if (val!= null && !val.equals("")) {
                    if(!keyField.equals("readyDoctorCode") && !keyField.equals("readyDoctorName") ) {
                        isLike = true;
                    }
                    if(keyField.equals("readyDoctorCode")){
                        ybAppealManageView.setReadyDoctorCode(ybAppealManageView.getCurrencyField());
                    }
                    if(keyField.equals("readyDoctorName")){
                        ybAppealManageView.setReadyDoctorName(ybAppealManageView.getCurrencyField());
                    }
                }
                if (isLike) {
                    count = this.baseMapper.findAppealManageLikeCount(ybAppealManageView,keyField, null);
                } else {
                    count = this.baseMapper.findAppealManageCount(ybAppealManageView,keyField, null);
                }
            }

        } catch (Exception e) {
            log.error("获取字典信息失败", e);
        }

        return count;
    }
    @Override
    public IPage<YbAppealManageView> findYbAppealManageViews(QueryRequest request, YbAppealManageView ybAppealManageView, String keyField) {
        try {
            YbReconsiderApply reconsiderApply = iYbReconsiderApplyService.findReconsiderApplyByApplyDateStrs(ybAppealManageView.getApplyDateStr(),ybAppealManageView.getAreaType());
            Page<YbAppealManageView> page = new Page<>();
            if (reconsiderApply != null) {
                ybAppealManageView.setPid(reconsiderApply.getId());
                boolean isLike = false;
                if (ybAppealManageView.getCurrencyField() != null && !ybAppealManageView.getCurrencyField().equals("")) {
                    if(!keyField.equals("readyDoctorCode") && !keyField.equals("readyDoctorName") ) {
                        isLike = true;
                    }
                    if(keyField.equals("readyDoctorCode")){
                        ybAppealManageView.setReadyDoctorCode(ybAppealManageView.getCurrencyField());
                    }
                    if(keyField.equals("readyDoctorName")){
                        ybAppealManageView.setReadyDoctorName(ybAppealManageView.getCurrencyField());
                    }
                }
                int count = 0;
                if (isLike) {
                    count = this.baseMapper.findAppealManageLikeCount(ybAppealManageView,keyField, null);
                } else {
                    count = this.baseMapper.findAppealManageCount(ybAppealManageView,keyField, null);
                }
                if (count > 0) {
                    page.setSearchCount(false);
                    SortUtil.handlePageSort(request, page, false);//true 是属性  false是数据库字段可两个
                    IPage<YbAppealManageView> pg = null;
                    if (isLike) {
                        pg = this.baseMapper.findAppealManageLikeView(page, ybAppealManageView,keyField, null);
                    } else {
                        pg = this.baseMapper.findAppealManageView(page, ybAppealManageView,keyField, null);
                    }
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

    @Override
    public IPage<YbAppealManageView> findAppealManageViewNew(QueryRequest request, YbAppealManageView ybAppealManageView, String keyField,boolean isConf) {
        try {
            String applyDateStr =ybAppealManageView.getApplyDateStr();
            Integer areaType = ybAppealManageView.getAreaType();
            YbReconsiderApply reconsiderApply = iYbReconsiderApplyService.findReconsiderApplyByApplyDateStrs(applyDateStr,areaType);
            Page<YbAppealManageView> page = new Page<>();
            if (reconsiderApply != null) {
                String value = ybAppealManageView.getCurrencyField();
                Integer typeno = ybAppealManageView.getTypeno();
                Integer dataType = ybAppealManageView.getDataType();
                if(typeno == 0 || typeno == 3){
                    if( typeno == 3) {
                        ybAppealManageView.setSourceType(YbDefaultValue.SOURCETYPE_1);
                    }
                    ybAppealManageView.setTypeno(null);
                    typeno = null;
                } else {
                    ybAppealManageView.setSourceType(YbDefaultValue.SOURCETYPE_0);
                }
                Integer sourceType = ybAppealManageView.getSourceType();
                Integer acceptState = ybAppealManageView.getAcceptState();
                List<String> strList = new ArrayList<>();
                List<YbReconsiderApplyData> applyDataList = ybApplyDataManager.getApplyDatas(reconsiderApply.getId(),applyDateStr + "-" + areaType);
                List<YbAppealManageView> list = new ArrayList<>();
                List<YbAppealManage> manageList = new ArrayList<>();
                if(!isConf) {
                    if (value != null && !value.equals("") && keyField.equals("readyDoctorName")) {
                        LambdaQueryWrapper<YbAppealManage> queryWrapper = new LambdaQueryWrapper<>();
                        queryWrapper.eq(YbAppealManage::getApplyDateStr,applyDateStr);
                        queryWrapper.eq(YbAppealManage::getAreaType,areaType);
                        queryWrapper.eq(YbAppealManage::getAcceptState,acceptState);
                        if (sourceType != null) {
                            queryWrapper.eq(YbAppealManage::getSourceType,sourceType);
                        }
                        if (ybAppealManageView.getOrderDoctorCode() != null) {
                            queryWrapper.eq(YbAppealManage::getReadyDeptName,"手术室");
                            queryWrapper.eq(YbAppealManage::getOrderDoctorCode,ybAppealManageView.getOrderDoctorCode());
                        }
                        strList = this.iYbPersonService.findPersonCodeList(value);
                        if(strList.size()>0){
                            if(strList.size() == 1){
                                queryWrapper.eq(YbAppealManage::getReadyDoctorCode, strList.get(0));
                            }else {
                                queryWrapper.in(YbAppealManage::getReadyDoctorCode, strList);
                            }
                        }
                        if (typeno != null) {
                            queryWrapper.eq(YbAppealManage::getTypeno,typeno);
                        }
                        if (dataType != null) {
                            queryWrapper.eq(YbAppealManage::getDataType,dataType);
                        }
                        manageList = iYbAppealManageService.list(queryWrapper);
                    } else {
                        YbAppealManage queryManage = new YbAppealManage();
                        queryManage.setApplyDateStr(applyDateStr);
                        queryManage.setAreaType(areaType);
                        queryManage.setAcceptState(acceptState);
                        if (sourceType != null) {
                            queryManage.setSourceType(sourceType);
                        }
                        if (ybAppealManageView.getOrderDoctorCode() != null) {
                            queryManage.setReadyDeptName("手术室");
                            queryManage.setOrderDoctorCode(ybAppealManageView.getOrderDoctorCode());
                        }
                        if (value != null && !value.equals("") && keyField.equals("readyDoctorCode")) {
                            queryManage.setReadyDoctorCode(value);
                        }
                        if (ybAppealManageView.getReadyDoctorCode() != null) {
                            queryManage.setReadyDoctorCode(ybAppealManageView.getReadyDoctorCode());
                        }
                        if (value != null && !value.equals("") && keyField.equals("orderNumber")) {
                            queryManage.setOrderNumber(value);
                        }
                        if (typeno != null) {
                            queryManage.setTypeno(typeno);
                        }
                        if (dataType != null) {
                            queryManage.setDataType(dataType);
                        }
                        manageList = iYbAppealManageService.findAppealManageList(queryManage);
                    }
                } else{
                    LambdaQueryWrapper<YbAppealManage> queryWrapper = new LambdaQueryWrapper<>();
                    queryWrapper.eq(YbAppealManage::getApplyDateStr,applyDateStr);
                    queryWrapper.eq(YbAppealManage::getAreaType,areaType);
                    queryWrapper.eq(YbAppealManage::getAcceptState,acceptState);
                    List<YbAppealConfireData> acdlist = iYbAppealConfireDataService.findAppealConfireDataByInDoctorCodeList(ybAppealManageView.getReadyDoctorCode(),areaType);
                    if (acdlist.size() > 0) {
                        if (ybAppealManageView.getSourceType() != null) {
                            queryWrapper.eq(YbAppealManage::getSourceType,sourceType);
                        }
                        strList = new ArrayList<>();
                        for (YbAppealConfireData item : acdlist){
                            strList.add(item.getDeptId());
                        }
                        queryWrapper.in(YbAppealManage::getReadyDeptCode,strList);
                        if (value != null && !value.equals("") && keyField.equals("readyDoctorName")) {
                            strList = this.iYbPersonService.findPersonCodeList(value);
                            if(strList.size()>0){
                                if(strList.size() == 1){
                                    queryWrapper.eq(YbAppealManage::getReadyDoctorCode, strList.get(0));
                                }else {
                                    queryWrapper.in(YbAppealManage::getReadyDoctorCode, strList);
                                }
                            }
                        }
                        if (value != null && !value.equals("") && keyField.equals("orderNumber")) {
                            queryWrapper.eq(YbAppealManage::getOrderNumber,value);
                        }
                        if (typeno != null) {
                            queryWrapper.eq(YbAppealManage::getTypeno,typeno);
                        }
                        if (dataType != null) {
                            queryWrapper.eq(YbAppealManage::getDataType,dataType);
                        }
                        manageList = iYbAppealManageService.list(queryWrapper);
                    }
                }

                applyDataList = this.iYbReconsiderApplyDataService.getApplyDataListView(applyDataList,keyField,value,typeno,dataType);

                if(manageList.size() > 0 && applyDataList.size() > 0){
                    if(manageList.size() > applyDataList.size()){
                        List<YbAppealManage> queryList = new ArrayList<>();
                        for (YbReconsiderApplyData item : applyDataList){
                            queryList = manageList.stream().filter(s->s.getApplyDataId().equals(item.getId())).collect(Collectors.toList());
                            if(queryList.size()>0) {
                                YbAppealManageView amv = this.getYbAppealManageView(queryList.get(0),item);
                                list.add(amv);
                            }
                        }
                    }else{
                        for (YbAppealManage item : manageList){
                            List<YbReconsiderApplyData> queryList = new ArrayList<>();
                            queryList = applyDataList.stream().filter(s->s.getId().equals(item.getApplyDataId())).collect(Collectors.toList());
                            if(queryList.size()>0) {
                                YbAppealManageView amv = this.getYbAppealManageView(item,queryList.get(0));
                                list.add(amv);
                            }
                        }
                    }
                }
                if(list.size()>0) {
                    page.setSearchCount(false);
                    SortUtil.handlePageSort(request, page, false);//true 是属性  false是数据库字段可两个
                    page.setTotal(list.size());
                    long current = page.getCurrent() == 1 ? 0 : (page.getCurrent() - 1) * page.getSize();
                    list = list.stream().sorted(Comparator.comparing(YbAppealManageView::getOrderNum)).skip(current).limit(page.getSize()).collect(Collectors.toList());
                    Date thisDate = new Date();
                    for(YbAppealManageView item : list){
                        int nValue = 0;
                        if(item.getTypeno() == YbDefaultValue.TYPENO_1){
                            nValue = item.getEnableDate().after(reconsiderApply.getEndDateOne()) ? 0 : 1;
                        }else{
                            nValue = item.getEnableDate().after(reconsiderApply.getEndDateTwo()) ? 0 : 1;
                        }
                        item.setApplyEndDate(item.getTypeno()==YbDefaultValue.TYPENO_1?reconsiderApply.getEndDateOne():reconsiderApply.getEndDateTwo());
                        item.setIsEnableDate(nValue);//计算

                        if(item.getSourceType() == 0){
                            if(item.getTypeno() == YbDefaultValue.TYPENO_1){
                                nValue = reconsiderApply.getEndDateOne().after(thisDate) ? 0 : 1;
                            }else{
                                nValue = reconsiderApply.getEndDateTwo().after(thisDate) ? 0 : 1;
                            }
                        } else {
                            nValue = 0;
                        }
                        item.setIsEnd(nValue);//计算
                        nValue = item.getEnableDate().after(thisDate) ? 1 : 0;
                        item.setEnableType(nValue);
                        Date enableDate = item.getEnableDate();
                        //前面enableDate需要跟其他字段计算，所以最后赋值
                        if(item.getTypeno() == YbDefaultValue.TYPENO_1){
                            if(reconsiderApply.getEndDateOne().after(enableDate)){
                                enableDate = DataTypeHelpers.addDateMethod(item.getEnableDate(),-1);
                            }else{
                                enableDate = reconsiderApply.getEndDateOne();
                            }
                        }else if(item.getTypeno() == YbDefaultValue.TYPENO_2){
                            if(reconsiderApply.getEndDateTwo().after(enableDate)){
                                enableDate = DataTypeHelpers.addDateMethod(item.getEnableDate(),-1);
                            }else{
                                enableDate = reconsiderApply.getEndDateTwo();
                            }
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

    private  YbAppealManageView getYbAppealManageView(YbAppealManage am,YbReconsiderApplyData reconsiderApplyData){
        YbAppealManageView amv = new YbAppealManageView();
        amv.setPid(reconsiderApplyData.getPid());
        amv.setSerialNo(reconsiderApplyData.getSerialNo());
        amv.setBillNo(reconsiderApplyData.getBillNo());
        amv.setProposalCode(reconsiderApplyData.getProposalCode());
        amv.setProjectCode(reconsiderApplyData.getProjectCode());
        amv.setProjectName(reconsiderApplyData.getProjectName());
        amv.setNum(reconsiderApplyData.getNum());
        amv.setMedicalPrice(reconsiderApplyData.getMedicalPrice());
        amv.setRuleName(reconsiderApplyData.getRuleName());
        amv.setDeductPrice(reconsiderApplyData.getDeductPrice());
        amv.setDeductReason(reconsiderApplyData.getDeductReason());
        amv.setRepaymentReason(reconsiderApplyData.getRepaymentReason());
        amv.setDoctorName(reconsiderApplyData.getDoctorName());
        amv.setDeptCode(reconsiderApplyData.getDeptCode());
        amv.setDeptName(reconsiderApplyData.getDeptName());
        amv.setEnterHospitalDateStr(reconsiderApplyData.getEnterHospitalDateStr());
        amv.setOutHospitalDateStr(reconsiderApplyData.getOutHospitalDateStr());
        amv.setCostDateStr(reconsiderApplyData.getCostDateStr());
        amv.setHospitalizedNo(reconsiderApplyData.getHospitalizedNo());
        amv.setTreatmentMode(reconsiderApplyData.getTreatmentMode());
        amv.setSettlementDateStr(reconsiderApplyData.getSettlementDateStr());
        amv.setPersonalNo(reconsiderApplyData.getPersonalNo());
        amv.setInsuredName(reconsiderApplyData.getInsuredName());
        amv.setCardNumber(reconsiderApplyData.getCardNumber());
        amv.setAreaName(reconsiderApplyData.getAreaName());
        amv.setVersionNumber(reconsiderApplyData.getVersionNumber());
        amv.setBackAppeal(reconsiderApplyData.getBackAppeal());
        amv.setTypeno(reconsiderApplyData.getTypeno());
//        amv.setInsuredType(reconsiderApplyData.getInsuredType);//YbAppealManageView没有
        amv.setDataType(reconsiderApplyData.getDataType());
        amv.setOrderNumber(reconsiderApplyData.getOrderNumber());
        amv.setOrderNum(reconsiderApplyData.getOrderNum());
        amv.setApplyDateStr(am.getApplyDateStr());
//        amv.setApplyEndDate(); //分页时赋值
        amv.setId(am.getId());
        amv.setApplyDataId(reconsiderApplyData.getId());
        amv.setVerifyId(am.getVerifyId());
        amv.setReadyDoctorCode(am.getReadyDoctorCode());
        amv.setReadyDoctorName(am.getReadyDoctorName());
        amv.setReadyDeptCode(am.getReadyDeptCode());
        amv.setReadyDeptName(am.getReadyDeptName());
        amv.setChangeDoctorCode(am.getChangeDoctorCode());
        amv.setChangeDoctorName(am.getChangeDoctorName());
        amv.setChangeDeptCode(am.getChangeDeptCode());
        amv.setChangeDeptName(am.getChangeDeptName());
        amv.setOperateReason(am.getOperateReason());
        amv.setOperateDate(am.getOperateDate());
        amv.setAcceptState(am.getAcceptState());
        amv.setRefuseId(am.getRefuseId());
        amv.setRefuseName(am.getRefuseName());
        amv.setRefuseReason(am.getRefuseReason());
        amv.setRefuseDate(am.getRefuseDate());
        amv.setAdminPersonId(am.getAdminPersonId());
        amv.setAdminPersonName(am.getAdminPersonName());
        amv.setAdminChangeDate(am.getAdminChangeDate());
        amv.setAdminReason(am.getAdminReason());
//        amv.setIsEnableDate(); //分页时赋值
        amv.setEnableDate(am.getEnableDate()); //分页时重新赋值，减一天
        amv.setState(am.getState());
//        amv.setCurrencyField();
        amv.setSourceType(am.getSourceType());
        amv.setOperateProcess(am.getOperateProcess());
        amv.setVerifySendId(am.getVerifySendId());
        amv.setApprovalState(am.getApprovalState());
        amv.setOrderDoctorCode(am.getOrderDoctorCode());
        amv.setOrderDoctorName(am.getOrderDoctorName());
        amv.setOrderDeptCode(am.getOrderDeptCode());
        amv.setOrderDeptName(am.getOrderDeptName());
//        amv.setIsEnd(); //分页时赋值
//        amv.setEnableType();//分页时赋值
        amv.setAreaType(am.getAreaType());
        return amv;
    }

    @Override
    public IPage<YbAppealManageView> findAppealManageUserViews(QueryRequest request, YbAppealManageView ybAppealManageView, String keyField) {
        try {
            YbReconsiderApply reconsiderApply = iYbReconsiderApplyService.findReconsiderApplyByApplyDateStrs(ybAppealManageView.getApplyDateStr(),ybAppealManageView.getAreaType());
            Page<YbAppealManageView> page = new Page<>();
            if (reconsiderApply != null) {
                ybAppealManageView.setPid(reconsiderApply.getId());
                boolean isLike = false;
                if (ybAppealManageView.getCurrencyField() != null && !ybAppealManageView.getCurrencyField().equals("")) {
                    isLike = true;
                }

                int count = 0;
                if (isLike) {
                    count = this.baseMapper.findAppealManageLikeCount(ybAppealManageView,keyField, null);
                } else {
                    count = this.baseMapper.findAppealManageCount(ybAppealManageView,keyField, null);
                }
                if (count > 0) {
                    page.setSearchCount(false);
                    SortUtil.handlePageSort(request, page, false);//true 是属性  false是数据库字段可两个
                    IPage<YbAppealManageView> pg = null;
                    if (isLike) {
                        pg = this.baseMapper.findAppealManageLikeView(page, ybAppealManageView,keyField, null);
                    } else {
                        pg = this.baseMapper.findAppealManageView(page, ybAppealManageView,keyField, null);
                    }
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

    @Override
    public IPage<YbAppealManageView> findAppealManageOperateRoomViews(QueryRequest request, YbAppealManageView ybAppealManageView, String keyField) {
        try {
            YbReconsiderApply reconsiderApply = iYbReconsiderApplyService.findReconsiderApplyByApplyDateStrs(ybAppealManageView.getApplyDateStr(),ybAppealManageView.getAreaType());
            Page<YbAppealManageView> page = new Page<>();
            if (reconsiderApply != null) {
                ybAppealManageView.setPid(reconsiderApply.getId());
//                未完成
                ybAppealManageView.setReadyDeptName("手术室");
                boolean isLike = false;
                if (ybAppealManageView.getCurrencyField() != null && !ybAppealManageView.getCurrencyField().equals("")) {
                    if(!keyField.equals("readyDoctorCode") && !keyField.equals("readyDoctorName") ) {
                        isLike = true;
                    }
                    if(keyField.equals("readyDoctorCode")){
                        ybAppealManageView.setReadyDoctorCode(ybAppealManageView.getCurrencyField());
                    }
                    if(keyField.equals("readyDoctorName")){
                        ybAppealManageView.setReadyDoctorName(ybAppealManageView.getCurrencyField());
                    }
                }

                int count = 0;
                if (isLike) {
                    count = this.baseMapper.findAppealManageLikeCount(ybAppealManageView,keyField, null);
                } else {
                    count = this.baseMapper.findAppealManageCount(ybAppealManageView,keyField, null);
                }
                if (count > 0) {
                    page.setSearchCount(false);
                    SortUtil.handlePageSort(request, page, false);//true 是属性  false是数据库字段可两个
                    IPage<YbAppealManageView> pg = null;
                    if (isLike) {
                        pg = this.baseMapper.findAppealManageLikeView(page, ybAppealManageView,keyField, null);
                    } else {
                        pg = this.baseMapper.findAppealManageView(page, ybAppealManageView,keyField, null);
                    }
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

    @Override
    public IPage<YbAppealManageView> findAppealManageConfireViews(QueryRequest request, YbAppealManageView ybAppealManageView, User currentUser, String keyField) {
        try {
            YbReconsiderApply reconsiderApply = iYbReconsiderApplyService.findReconsiderApplyByApplyDateStrs(ybAppealManageView.getApplyDateStr(),ybAppealManageView.getAreaType());
            Page<YbAppealManageView> page = new Page<>();
            if (reconsiderApply != null) {
                List<YbAppealConfireData> acdlist = iYbAppealConfireDataService.findAppealConfireDataByInDoctorCodeList(currentUser.getUsername(),ybAppealManageView.getAreaType());
                if (acdlist.size() > 0) {
                    String appealConfireId = acdlist.get(0).getPid();
                    ybAppealManageView.setPid(reconsiderApply.getId());
                    boolean isLike = false;
                    if (ybAppealManageView.getCurrencyField() != null && !ybAppealManageView.getCurrencyField().equals("")) {
                        if(!keyField.equals("readyDoctorCode") && !keyField.equals("readyDoctorName") ) {
                            isLike = true;
                        }
                        if(keyField.equals("readyDoctorCode")){
                            ybAppealManageView.setReadyDoctorCode(ybAppealManageView.getCurrencyField());
                        }
                        if(keyField.equals("readyDoctorName")){
                            ybAppealManageView.setReadyDoctorName(ybAppealManageView.getCurrencyField());
                        }
                    }

                    int count = 0;
                    if (isLike) {
                        count = this.baseMapper.findAppealManageLikeCount(ybAppealManageView,keyField, appealConfireId);
                    } else {
                        count = this.baseMapper.findAppealManageCount(ybAppealManageView,keyField, appealConfireId);
                    }
                    if (count > 0) {
                        page.setSearchCount(false);
                        SortUtil.handlePageSort(request, page, false);//true 是属性  false是数据库字段可两个
                        IPage<YbAppealManageView> pg = null;
                        if (isLike) {
                            pg = this.baseMapper.findAppealManageLikeView(page, ybAppealManageView,keyField, appealConfireId);
                        } else {
                            pg = this.baseMapper.findAppealManageView(page, ybAppealManageView,keyField, appealConfireId);
                        }
                        pg.setTotal(count);
                        return pg;
                    }
                }
            }
            return page;
        } catch (Exception e) {
            log.error("获取字典信息失败", e);
            return null;
        }
    }


    @Override
    public IPage<YbAppealManageView> findYbAppealManageViewList(QueryRequest request, YbAppealManageView ybAppealManageView) {
        try {
            Page<YbAppealManageView> page = new Page<>();
            SortUtil.handlePageSort(request, page, false);//true 是属性  false是数据库字段可两个
            return this.baseMapper.findYbAppealManageView(page, ybAppealManageView);
        } catch (Exception e) {
            log.error("获取VIEW失败", e);
            return null;
        }
    }

    @Override
    public List<YbAppealManageView> findAppealManageViewList(YbAppealManageView ybAppealManageView) {
        List<YbAppealManageView> list = new ArrayList<>();

        if (ybAppealManageView.getPid() != null) {
            return this.baseMapper.findAppealManageList(ybAppealManageView);
        } else {
            return list;
        }
    }

    @Override
    @Transactional
    public void createYbAppealManageView(YbAppealManageView ybAppealManageView) {
//        ybAppealManageView.setCreateTime(new Date());
//        ybAppealManageView.setIsDeletemark(1);
        this.save(ybAppealManageView);
    }

    @Override
    @Transactional
    public void updateYbAppealManageView(YbAppealManageView ybAppealManageView) {
//        ybAppealManageView.setModifyTime(new Date());
        this.baseMapper.updateYbAppealManageView(ybAppealManageView);
    }

    @Override
    @Transactional
    public void deleteYbAppealManageViews(String[] Ids) {
        List<String> list = Arrays.asList(Ids);
        this.baseMapper.deleteBatchIds(list);
    }


}