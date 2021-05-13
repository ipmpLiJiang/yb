package cc.mrbird.febs.yb.service.impl;

import cc.mrbird.febs.com.controller.DataTypeHelpers;
import cc.mrbird.febs.common.domain.QueryRequest;
import cc.mrbird.febs.common.properties.FebsProperties;
import cc.mrbird.febs.common.utils.OracleDB;
import cc.mrbird.febs.common.utils.SortUtil;
import cc.mrbird.febs.yb.dao.YbReconsiderApplyDataMapper;
import cc.mrbird.febs.yb.entity.*;
import cc.mrbird.febs.yb.service.*;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.annotations.Param;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;

/**
 * <p>
 * 服务实现类
 * </p>
 *
 * @author viki
 * @since 2020-07-17
 */
@Slf4j
@Service("IYbReconsiderApplyDataService")
@Transactional(propagation = Propagation.SUPPORTS, readOnly = true, rollbackFor = Exception.class)
public class YbReconsiderApplyDataServiceImpl extends ServiceImpl<YbReconsiderApplyDataMapper, YbReconsiderApplyData> implements IYbReconsiderApplyDataService {

    @Autowired
    private IYbReconsiderApplyService iYbReconsiderApplyService;
    @Autowired
    private IYbReconsiderApplyTaskService iYbReconsiderApplyTaskService;
    @Autowired
    private IYbReconsiderInpatientfeesService iYbReconsiderInpatientfeesService;
    @Autowired
    private IYbDeptService iYbDeptService;

    @Autowired
    private IYbPersonService iYbPersonService;

    @Autowired
    FebsProperties febsProperties;

    @Override
    public IPage<YbReconsiderApplyData> findYbReconsiderApplyDatas(QueryRequest request, YbReconsiderApplyData ybReconsiderApplyData) {
        try {
            LambdaQueryWrapper<YbReconsiderApplyData> queryWrapper = new LambdaQueryWrapper<>();
            queryWrapper.eq(YbReconsiderApplyData::getIsDeletemark, 1);//1是未删 0是已删


            Page<YbReconsiderApplyData> page = new Page<>();
            SortUtil.handlePageSort(request, page, false);//true 是属性  false是数据库字段可两个
            return this.page(page, queryWrapper);
        } catch (Exception e) {
            log.error("获取字典信息失败", e);
            return null;
        }
    }

    @Override
    public IPage<YbReconsiderApplyData> findYbReconsiderApplyDataList(QueryRequest request, YbReconsiderApplyData ybReconsiderApplyData) {
        try {
            Page<YbReconsiderApplyData> page = new Page<>();
            ybReconsiderApplyData.setIsDeletemark(1);
            SortUtil.handlePageSort(request, page, false);//true 是属性  false是数据库字段可两个
            return this.baseMapper.findYbReconsiderApplyData(page, ybReconsiderApplyData);
        } catch (Exception e) {
            log.error("获取失败", e);
            return null;
        }
    }

    @Override
    @Transactional
    public void createYbReconsiderApplyData(YbReconsiderApplyData ybReconsiderApplyData) {
        if (ybReconsiderApplyData.getId() == null || "".equals(ybReconsiderApplyData.getId())) {
            ybReconsiderApplyData.setId(UUID.randomUUID().toString());
        }
        ybReconsiderApplyData.setIsDeletemark(1);
        this.save(ybReconsiderApplyData);
    }

    @Override
    @Transactional
    public void createBatchYbReconsiderApplyData(List<YbReconsiderApplyData> list, YbReconsiderApply ybReconsiderApply) {

        this.saveBatch(list);
//        for(YbReconsiderApplyData item : list)
//        {
//            this.save(item);
//        }
//        this.baseMapper.createBatchData(list);

        iYbReconsiderApplyService.updateYbReconsiderApply(ybReconsiderApply);
    }

    @Override
    @Transactional
    public void updateYbReconsiderApplyData(YbReconsiderApplyData ybReconsiderApplyData) {
        this.baseMapper.updateYbReconsiderApplyData(ybReconsiderApplyData);
    }

    @Override
    @Transactional
    public void deleteYbReconsiderApplyDatas(String[] Ids) {
        List<String> list = Arrays.asList(Ids);
        this.baseMapper.deleteBatchIds(list);
    }

    @Override
    @Transactional
    public int deleteReconsiderApplyDataByPid(YbReconsiderApplyData ybReconsiderApplyData) {
        int count = 0;
        LambdaQueryWrapper<YbReconsiderApply> wrapperApply = new LambdaQueryWrapper<>();
        wrapperApply.eq(YbReconsiderApply::getId, ybReconsiderApplyData.getPid());
        List<YbReconsiderApply> applyList = iYbReconsiderApplyService.list(wrapperApply);
        if (applyList.size() > 0) {
            if (applyList.get(0).getState() == YbDefaultValue.APPLYSTATE_4 || applyList.get(0).getState() == YbDefaultValue.APPLYSTATE_2) {
                LambdaQueryWrapper<YbReconsiderApplyData> wrapper = new LambdaQueryWrapper<>();
                wrapper.eq(YbReconsiderApplyData::getPid, applyList.get(0).getId());
                wrapper.eq(YbReconsiderApplyData::getTypeno, ybReconsiderApplyData.getTypeno());
                this.baseMapper.delete(wrapper);


                int state = applyList.get(0).getState() == YbDefaultValue.APPLYSTATE_2 ? YbDefaultValue.APPLYSTATE_1 : YbDefaultValue.APPLYSTATE_3;
                YbReconsiderApply updateApply = new YbReconsiderApply();
                updateApply.setId(applyList.get(0).getId());
                updateApply.setState(state);
                iYbReconsiderApplyService.updateYbReconsiderApply(updateApply);
                count = 1;
            }
        }
        return count;
    }

    @Override
    public List<YbReconsiderApplyData> findReconsiderApplyDataByApplyDates(String applyDateStr,Integer areaType, Integer dataType) {
        List<YbReconsiderApplyData> list = new ArrayList<>();
        YbReconsiderApply reconsiderApply = iYbReconsiderApplyService.findReconsiderApplyByApplyDateStrs(applyDateStr,areaType);
        if(reconsiderApply!=null) {
            YbReconsiderApplyData reconsiderApplyData = new YbReconsiderApplyData();
            reconsiderApplyData.setPid(reconsiderApply.getId());
            reconsiderApplyData.setDataType(dataType);
            return  this.findList(reconsiderApplyData);
        }
        return  list;
    }
    private List<YbReconsiderApplyData> findList(YbReconsiderApplyData reconsiderApplyData){
        LambdaQueryWrapper<YbReconsiderApplyData> wrapper = new LambdaQueryWrapper<>();
        if(reconsiderApplyData.getId() !=null){
            wrapper.eq(YbReconsiderApplyData::getId,reconsiderApplyData.getId());
        }
        if(reconsiderApplyData.getPid() !=null){
            wrapper.eq(YbReconsiderApplyData::getPid,reconsiderApplyData.getPid());
        }
        if(reconsiderApplyData.getTypeno() !=null){
            wrapper.eq(YbReconsiderApplyData::getTypeno,reconsiderApplyData.getTypeno());
        }
        if(reconsiderApplyData.getDataType() !=null){
            wrapper.eq(YbReconsiderApplyData::getDataType,reconsiderApplyData.getDataType());
        }
        return this.list(wrapper);

    }

    @Override
    public List<YbReconsiderApplyData> findReconsiderApplyDataList(YbReconsiderApplyData reconsiderApplyData){
        return  this.findList(reconsiderApplyData);
    }

    @Override
    public List<YbReconsiderApplyData> findReconsiderApplyDataByNotVerifys(String pid,String applyDateStr,Integer areaType, Integer dataType, Integer typeno) {
        return this.baseMapper.findReconsiderApplyDataByNotVerify(pid,applyDateStr,areaType, dataType, typeno);
    }

    @Override
    @Transactional
    public void createBatchDatas(List<YbReconsiderApplyData> listReconsiderApplyData) {
        this.baseMapper.createBatchData(listReconsiderApplyData);
    }

    @Override
    @Transactional
    public void importReconsiderApply(YbReconsiderApply ybReconsiderApply, List<YbReconsiderApplyData> listData, List<YbReconsiderApplyData> listMain) {
        this.iYbReconsiderApplyService.updateYbReconsiderApply(ybReconsiderApply);
        List<YbReconsiderApplyData> createDataList = new ArrayList<>();
        List<YbReconsiderApplyData> createMainList = new ArrayList<>();
        listData = listData.stream().sorted(Comparator.comparing(YbReconsiderApplyData::getSettlementDate)).collect(Collectors.toList());
        listMain = listMain.stream().sorted(Comparator.comparing(YbReconsiderApplyData::getSettlementDate)).collect(Collectors.toList());
        int i = 1;
        for (YbReconsiderApplyData item : listData) {
            YbReconsiderApplyData rrData = new YbReconsiderApplyData();
            rrData.setId(item.getId());
            rrData.setPid(item.getPid());
            rrData.setOrderNumber(item.getOrderNumber());//序号
            rrData.setOrderNum(item.getOrderNum());//排序
            rrData.setSerialNo(item.getSerialNo());//交易流水号
            rrData.setBillNo(item.getBillNo());//'单据号
            rrData.setProposalCode(item.getProposalCode());//意见书编码
            rrData.setProjectCode(item.getProjectCode());//'项目编码
            rrData.setProjectName(item.getProjectName());//'项目名称
            rrData.setNum(item.getNum());//'数量
            rrData.setMedicalPrice(item.getMedicalPrice());//'医保内金额
            rrData.setRuleName(item.getRuleName());//'规则名称
            rrData.setDeductPrice(item.getDeductPrice());//'扣除金额
            rrData.setDeductReason(item.getDeductReason());//'扣除原因
            rrData.setRepaymentReason(item.getRepaymentReason());//'还款原因
            rrData.setDoctorName(item.getDoctorName());//'医生姓名
            rrData.setDeptCode(item.getDeptCode());// '科室编码
            rrData.setDeptName(item.getDeptName());//'科室名称
            rrData.setEnterHospitalDateStr(item.getEnterHospitalDateStr());//'入院日期str
            rrData.setOutHospitalDateStr(item.getOutHospitalDateStr());//'出院日期str
            rrData.setCostDateStr(item.getCostDateStr());//'费用日期str
            rrData.setCostDate(item.getCostDate());
            rrData.setHospitalizedNo(item.getHospitalizedNo());//'住院号
            rrData.setTreatmentMode(item.getTreatmentMode());//'就医方式
            rrData.setSettlementDateStr(item.getSettlementDateStr());//'结算日期Str
            rrData.setSettlementDate(item.getSettlementDate());
            rrData.setPersonalNo(item.getPersonalNo());//'个人编号
            rrData.setInsuredName(item.getInsuredName());//'参保人姓名
            rrData.setCardNumber(item.getCardNumber());//'医保卡号
            rrData.setAreaName(item.getAreaName());//'统筹区名称
            rrData.setVersionNumber(item.getVersionNumber());
            rrData.setBackAppeal(item.getBackAppeal());//'反馈申诉
            rrData.setTypeno(item.getTypeno());
            rrData.setDataType(item.getDataType());
            rrData.setOrderSettlementNum(i);
            rrData.setIsDeletemark(1);
            rrData.setState(item.getState());
            createDataList.add(rrData);
            i++;
        }
        i = 1;
        for (YbReconsiderApplyData item : listMain) {
            YbReconsiderApplyData rrMain = new YbReconsiderApplyData();
            rrMain.setId(item.getId());
            rrMain.setPid(item.getPid());
            rrMain.setOrderNumber(item.getOrderNumber());//序号
            rrMain.setOrderNum(item.getOrderNum());//排序
            rrMain.setSerialNo(item.getSerialNo());//交易流水号
            rrMain.setProposalCode(item.getProposalCode());//意见书编码
            rrMain.setBillNo(item.getBillNo());//'单据号
            rrMain.setMedicalPrice(item.getMedicalPrice());//'医保内金额
            rrMain.setRuleName(item.getRuleName());//'规则名称
            rrMain.setDeductPrice(item.getDeductPrice());//'扣除金额
            rrMain.setSettlementDateStr(item.getSettlementDateStr());//'结算日期Str
            rrMain.setSettlementDate(item.getSettlementDate());
            rrMain.setHospitalizedNo(item.getHospitalizedNo());//'住院号
            rrMain.setEnterHospitalDateStr(item.getEnterHospitalDateStr());//'入院日期str
            rrMain.setOutHospitalDateStr(item.getOutHospitalDateStr());//'出院日期str
            rrMain.setTreatmentMode(item.getTreatmentMode());//'就医方式
            rrMain.setPersonalNo(item.getPersonalNo());//'个人编号
            rrMain.setInsuredName(item.getInsuredName());//'参保人姓名
            rrMain.setInsuredType(item.getInsuredType());//'参保类型
            rrMain.setAreaName(item.getAreaName());//'统筹区名称
            rrMain.setVersionNumber(item.getVersionNumber());
            rrMain.setBackAppeal(item.getBackAppeal());//'反馈申诉
            rrMain.setTypeno(item.getTypeno());
            rrMain.setDataType(item.getDataType());
            rrMain.setOrderSettlementNum(i);//结算日期排序
            rrMain.setIsDeletemark(item.getIsDeletemark());
            rrMain.setState(item.getState());
            createMainList.add(rrMain);
            i++;
        }
        if (createDataList.size() > 0) {
            this.saveBatch(createDataList);
        }

        if (createMainList.size() > 0) {
            this.saveBatch(createMainList);
        }
    }


    //    @Override
    @Transactional
    public void findReconsiderApplyDataNotTask(YbReconsiderApply reconsiderApply, int typeno) {
        int dataType = 0;
        String msg = "";
        int state = 1;
        YbReconsiderApplyTask ybReconsiderApplyTask = new YbReconsiderApplyTask();
        ybReconsiderApplyTask.setApplyDateStr(reconsiderApply.getApplyDateStr());
        ybReconsiderApplyTask.setAreaType(reconsiderApply.getAreaType());
        ybReconsiderApplyTask.setTypeno(typeno);
        ybReconsiderApplyTask.setState(state);
        YbReconsiderApplyTask raTask = this.iYbReconsiderApplyTaskService.findReconsiderApplyTasks(ybReconsiderApplyTask);
        //总数
        int totalRow = 0;
        //当前页
        int currentPage = 1;
        boolean noUpdate = false;
        YbReconsiderApplyTask createTask = new YbReconsiderApplyTask();
        if (raTask == null) {
            totalRow = this.baseMapper.findReconsiderApplyDataNotCount(reconsiderApply.getId(),reconsiderApply.getApplyDateStr(),reconsiderApply.getAreaType(), dataType, typeno);
            if (totalRow == 0) {
                noUpdate = true;
            } else {
                createTask = createReconsiderApplyTask(reconsiderApply.getApplyDateStr(), reconsiderApply.getAreaType(),1, dataType, typeno, currentPage, totalRow);

                List<YbReconsiderApplyData> reconsiderApplyDataList = this.baseMapper.findReconsiderApplyDataNotInpatientfees(reconsiderApply.getId(),reconsiderApply.getApplyDateStr(),reconsiderApply.getAreaType(), dataType, typeno);
                if (reconsiderApplyDataList.size() > 0) {
                    List<YbReconsiderApplyData> updateList = new ArrayList<>();
                    int i = 1;
                    for (YbReconsiderApplyData item : reconsiderApplyDataList) {
                        YbReconsiderApplyData update = new YbReconsiderApplyData();
                        update.setId(item.getId());
                        update.setState(1);
                        update.setOrderSettlementNum(i);
                        i++;
                        updateList.add(update);
                    }

                    if (updateList.size() > 0) {
                        this.updateBatchById(updateList);
                    }
                }
            }
        } else {
            if (raTask.getCurrentPage().equals(raTask.getTotalPage())) {
                noUpdate = true;
            } else {
                currentPage = raTask.getCurrentPage() + 1;
                totalRow = raTask.getTotalRow();
                dataType = raTask.getDataType();
                createTask = createReconsiderApplyTask(reconsiderApply.getApplyDateStr(), reconsiderApply.getAreaType(), state, dataType, typeno, currentPage, totalRow);
            }
        }
        if (!noUpdate) {
            List<YbDeptHis> departList = new ArrayList<>();
            List<YbDeptHis> queryDepartList = new ArrayList<>();
            List<YbPerson> personList = new ArrayList<>();
            List<YbPerson> queryPersontList = new ArrayList<>();
            //从orderNum开始
            int startNum = createTask.getStartNum();
            //从orderNum结束
            int endNum = createTask.getEndNum();

            List<YbReconsiderApplyData> reconsiderApplyDataList = this.baseMapper.findReconsiderApplyDataNotBetween(reconsiderApply.getId(), dataType, typeno, startNum, endNum);
            if (reconsiderApplyDataList.size() > 0) {
                String hisSql = "";
                String hisWhere = "";

                //String[] dateArr = hisTaskDate(reconsiderApplyDataList);
                //查询his日期区间 开始
                String dateStrForm = reconsiderApply.getApplyDateStr() + "-01";
//                String dateStrForm = dateArr[0];
                //查询his日期区间 结束
                String dateStrTo = DataTypeHelpers.stringDateFormatAddMonth(1, dateStrForm, "", false);
//                String dateStrTo = dateArr[1];
                if (dataType == 0) {
                    hisWhere = hisTaskWhere(reconsiderApplyDataList, 0, state);

                    if (!hisWhere.equals("")) {
                        hisSql = "select * from his.V_SAP_INPFEES where " + hisWhere +
                                "settlementdate >= to_date('" + dateStrForm + "',' yyyy-mm-dd') and " +
                                "settlementdate < to_date('" + dateStrTo + "',' yyyy-mm-dd') ";
                    }
                }

                if (!hisSql.equals("")) {
                    try {
                        departList = new ArrayList<>();
                        List<YbDept> deptList = iYbDeptService.findDeptList(new YbDept(), 0);
                        for (YbDept item : deptList) {
                            YbDeptHis his = new YbDeptHis();
                            his.setDeptId(item.getDeptId());
                            his.setDeptName(item.getDeptName());
                            his.setSpellCode(item.getSpellCode());
                            departList.add(his);
                        }

                        personList = iYbPersonService.list();

                        if (departList.size() > 0) {
                            List<YbReconsiderInpatientfees> createList = new ArrayList<>();
                            if (dataType == 0) {
                                List<YbReconsiderInpatientfeesData> rifDataList = new ArrayList<>();
                                List<YbReconsiderInpatientfeesData> queryRifDataList = new ArrayList<>();

                                OracleDB<YbReconsiderInpatientfeesData> oracleDB = new OracleDB<>();
                                rifDataList = oracleDB.excuteSqlRS(new YbReconsiderInpatientfeesData(), hisSql);
                                if (rifDataList.size() > 0) {
                                    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                                    for (YbReconsiderApplyData item : reconsiderApplyDataList) {
                                        queryRifDataList = rifDataList.stream().filter(
                                                s -> s.getTransNo().equals(item.getSerialNo()) &&
                                                        s.getHisName().equals(item.getProjectName()) &&
                                                        sdf.format(s.getFeeDate()).equals(sdf.format(item.getCostDate()))
                                        ).collect(Collectors.toList());

                                        if (queryRifDataList.size() == 0) {
                                            queryRifDataList = rifDataList.stream().filter(
                                                    s -> s.getTransNo().equals(item.getSerialNo()) &&
                                                            s.getHisName().equals(item.getProjectName())
                                            ).collect(Collectors.toList());
                                        }

                                        if (queryRifDataList.size() > 0) {
                                            YbReconsiderInpatientfeesData obj = queryRifDataList.get(0);
                                            YbReconsiderInpatientfees reconsiderInpatientfees = new YbReconsiderInpatientfees();
                                            reconsiderInpatientfees.setId(UUID.randomUUID().toString());
                                            reconsiderInpatientfees.setInpatientId(obj.getInpatientId());//住院号
                                            reconsiderInpatientfees.setPatientName(obj.getPatientName());//患者姓名
                                            reconsiderInpatientfees.setSettlementId(obj.getSettlementId());//HIS结算序号
                                            reconsiderInpatientfees.setBillNo(obj.getBillNo());//'单据号'
                                            reconsiderInpatientfees.setTransNo(obj.getTransNo());//'交易流水号'
                                            reconsiderInpatientfees.setItemId(obj.getItemId());//'项目代码'
                                            reconsiderInpatientfees.setItemCode(obj.getMiCode());//'项目医保编码'
                                            reconsiderInpatientfees.setItemName(obj.getMiName());//'项目名称'
                                            reconsiderInpatientfees.setItemCount(obj.getItemCount());//'项目数量'
                                            reconsiderInpatientfees.setItemPrice(obj.getItemPrice());//'项目单价'
                                            reconsiderInpatientfees.setItemAmount(obj.getItemAmount());//'项目金额'
                                            reconsiderInpatientfees.setFeeDate(obj.getFeeDate());//'费用日期'
                                            reconsiderInpatientfees.setDeptId(obj.getDeptId());//'住院科室代码'
                                            if (obj.getDeptId() != null && (obj.getDeptName() == null || obj.getDeptName().equals(""))) {
                                                queryDepartList = departList.stream().filter(
                                                        s -> s.getDeptId().equals(obj.getDeptId())
                                                ).collect(Collectors.toList());
                                                if (queryDepartList.size() > 0) {
                                                    reconsiderInpatientfees.setDeptName(queryDepartList.get(0).getDeptName());//'执行科室名称'
                                                }
                                            } else {
                                                reconsiderInpatientfees.setDeptName(obj.getDeptName());//'住院科室名称'
                                            }
                                            reconsiderInpatientfees.setOrderDocId(obj.getOrderDocId());//'开方医生代码'
                                            if (personList.size() > 0 && obj.getOrderDocId() != null && obj.getOrderDocName() == null) {
                                                queryPersontList = personList.stream().filter(p ->
                                                        p.getPersonCode().equals(obj.getOrderDocId())).collect(Collectors.toList());
                                                if (queryPersontList.size() > 0) {
                                                    reconsiderInpatientfees.setOrderDocName(queryPersontList.get(0).getPersonName());//'开方医生名称'
                                                }
                                            } else {
                                                reconsiderInpatientfees.setOrderDocName(obj.getOrderDocName());//'开方医生名称'
                                            }
                                            reconsiderInpatientfees.setExcuteDeptId(obj.getExcuteDeptId());//'执行科室代码'
                                            if (obj.getExcuteDeptId() != null && (obj.getExcuteDeptName() == null || obj.getExcuteDeptName().equals(""))) {
                                                queryDepartList = departList.stream().filter(
                                                        s -> s.getDeptId().equals(obj.getExcuteDeptId())
                                                ).collect(Collectors.toList());
                                                if (queryDepartList.size() > 0) {
                                                    reconsiderInpatientfees.setExcuteDeptName(queryDepartList.get(0).getDeptName());//'执行科室名称'
                                                }
                                            } else {
                                                reconsiderInpatientfees.setExcuteDeptName(obj.getExcuteDeptName());//'执行科室名称'
                                            }

                                            reconsiderInpatientfees.setExcuteDocId(obj.getExcuteDocId());//'执行医生代码'
                                            if (personList.size() > 0 && obj.getExcuteDocId() != null && obj.getExcuteDocName() == null) {
                                                queryPersontList = personList.stream().filter(p ->
                                                        p.getPersonCode().equals(obj.getExcuteDocId())).collect(Collectors.toList());
                                                if (queryPersontList.size() > 0) {
                                                    reconsiderInpatientfees.setExcuteDocName(queryPersontList.get(0).getPersonName());//'开方医生名称'
                                                }
                                            } else {
                                                reconsiderInpatientfees.setExcuteDocName(obj.getExcuteDocName());//'执行医生名称'
                                            }

                                            reconsiderInpatientfees.setSettlementDate(obj.getSettlementDate());//'结算时间'

                                            reconsiderInpatientfees.setMiCode(obj.getMiCode());
                                            reconsiderInpatientfees.setHisName(obj.getHisName());
                                            reconsiderInpatientfees.setMiName(obj.getMiName());

                                            reconsiderInpatientfees.setDyyz(obj.getDyyz());
                                            reconsiderInpatientfees.setAttendDocId(obj.getAttendDocId());
                                            reconsiderInpatientfees.setAttendDocName(obj.getAttendDocName());
                                            reconsiderInpatientfees.setItemTypeCode(obj.getItemTypeCode());
                                            reconsiderInpatientfees.setItemTypeName(obj.getItemTypeName());

                                            reconsiderInpatientfees.setFeeOperatorId(obj.getFeeOperatorId());
                                            reconsiderInpatientfees.setFeeOperatorName(obj.getFeeOperatorName());
                                            reconsiderInpatientfees.setFeeDeptId(obj.getFeeDeptId());
                                            reconsiderInpatientfees.setFeeDeptName(obj.getFeeDeptName());

                                            reconsiderInpatientfees.setApplyDataId(item.getId());
                                            reconsiderInpatientfees.setOrderNumber(item.getOrderNumber());//序号
                                            reconsiderInpatientfees.setApplyDateStr(reconsiderApply.getApplyDateStr());
                                            reconsiderInpatientfees.setDataType(dataType);
                                            reconsiderInpatientfees.setTypeno(typeno);
                                            reconsiderInpatientfees.setIsDeletemark(1);
//                                            reconsiderInpatientfees.setCreateTime(new Date());
                                            reconsiderInpatientfees.setState(state);
                                            reconsiderInpatientfees.setAreaType(reconsiderApply.getAreaType());
                                            createList.add(reconsiderInpatientfees);
                                        }
                                    }
                                } else {
                                    msg = "his接口明细扣款无数据.";
                                    log.error(msg);
                                }
                            }

                            if (createList.size() > 0) {
                                msg = "His更新数据：" + createList.size() + "条";
                                iYbReconsiderInpatientfeesService.saveBatch(createList);
                                log.error(msg);
                            }

                            this.iYbReconsiderApplyTaskService.createYbReconsiderApplyTask(createTask);
                        } else {
                            msg = "his接口科室无数据.";
                            log.error(msg);
                        }
                    } catch (Exception e) {
                        msg = e.getMessage();
                        log.error(msg);
                    }
                } else {
                    msg = "his接口Where为空.";
                    log.error(msg);
                }
                System.out.println(msg);
            }
        }

    }

    @Override
    @Transactional
    public void findReconsiderApplyDataTask(String applyDateStr,Integer areaType) {
        int dataType = 0;
        int typeno = 1;
        //当前复议年月
        if (applyDateStr == null || "".equals(applyDateStr)) {
            applyDateStr = DataTypeHelpers.getUpNianYue();
        }
        YbReconsiderApply reconsiderApply = iYbReconsiderApplyService.findReconsiderApplyByApplyDateStrs(applyDateStr,areaType);
        if (reconsiderApply == null) {
            return;
        }
        if (reconsiderApply.getState() == 2 || reconsiderApply.getState() == 3) {
            typeno = 1;
        } else if (reconsiderApply.getState() == 4 || reconsiderApply.getState() == 5) {
            typeno = 2;
        } else {
            return;
        }
        String msg = "";
        int state = 0;
        YbReconsiderApplyTask ybReconsiderApplyTask = new YbReconsiderApplyTask();
        ybReconsiderApplyTask.setApplyDateStr(applyDateStr);
        ybReconsiderApplyTask.setAreaType(areaType);
        ybReconsiderApplyTask.setTypeno(typeno);
        ybReconsiderApplyTask.setState(state);
        YbReconsiderApplyTask raTask = this.iYbReconsiderApplyTaskService.findReconsiderApplyTasks(ybReconsiderApplyTask);
        //总数
        int totalRow = 0;
        //当前页
        int currentPage = 1;
        boolean noUpdate = false;
        YbReconsiderApplyTask createTask = new YbReconsiderApplyTask();
        if (raTask == null) {
            totalRow = this.baseMapper.findReconsiderApplyDataCount(reconsiderApply.getId(), dataType, typeno);
            if (totalRow == 0) {
                dataType = 1;
                totalRow = this.baseMapper.findReconsiderApplyDataCount(reconsiderApply.getId(), dataType, typeno);
            }
            if (totalRow == 0) {
                noUpdate = true;
            } else {
                createTask = createReconsiderApplyTask(applyDateStr, areaType, state, dataType, typeno, currentPage, totalRow);
            }
        } else {
            if (raTask.getCurrentPage().equals(raTask.getTotalPage())) {
                if (raTask.getDataType() == 0) {
                    dataType = 1;
                    totalRow = this.baseMapper.findReconsiderApplyDataCount(reconsiderApply.getId(), dataType, typeno);
                    if (totalRow == 0) {
                        this.findReconsiderApplyDataNotTask(reconsiderApply, typeno);
                        noUpdate = true;
                    } else {
                        createTask = createReconsiderApplyTask(applyDateStr,areaType, state, dataType, typeno, currentPage, totalRow);
                    }
                } else {
                    this.findReconsiderApplyDataNotTask(reconsiderApply, typeno);
                    noUpdate = true;
                }
            } else {
                currentPage = raTask.getCurrentPage() + 1;
                totalRow = raTask.getTotalRow();
                dataType = raTask.getDataType();
                createTask = createReconsiderApplyTask(applyDateStr,areaType, state, dataType, typeno, currentPage, totalRow);
            }
        }
        if (!noUpdate) {
            List<YbDeptHis> departList = new ArrayList<>();
            List<YbDeptHis> queryDepartList = new ArrayList<>();
            List<YbPerson> personList = new ArrayList<>();
            List<YbPerson> queryPersontList = new ArrayList<>();
            //从orderNum开始
            int startNum = createTask.getStartNum();
            //从orderNum结束
            int endNum = createTask.getEndNum();

            List<YbReconsiderApplyData> reconsiderApplyDataList = this.baseMapper.findReconsiderApplyDataBetween(reconsiderApply.getId(), dataType, typeno, startNum, endNum);
            if (reconsiderApplyDataList.size() > 0) {
                String hisSql = "";
                String hisWhere = "";

                //String[] dateArr = hisTaskDate(reconsiderApplyDataList);
                //查询his日期区间 开始
                String dateStrForm = applyDateStr + "-01";
//                String dateStrForm = dateArr[0];
                //查询his日期区间 结束
                String dateStrTo = DataTypeHelpers.stringDateFormatAddMonth(1, dateStrForm, "", false);
//                String dateStrTo = dateArr[1];
                if (dataType == 0) {
                    hisWhere = hisTaskWhere(reconsiderApplyDataList, 0, state);

                    if (!hisWhere.equals("")) {
                        hisSql = "select * from his.V_SAP_INPFEES where " + hisWhere +
                                "settlementdate >= to_date('" + dateStrForm + "',' yyyy-mm-dd') and " +
                                "settlementdate < to_date('" + dateStrTo + "',' yyyy-mm-dd') ";
                    }
                } else {
                    hisWhere = hisTaskWhere(reconsiderApplyDataList, 1, state);
                    if (!hisWhere.equals("")) {
                        hisSql = "select * from his.V_SAP_INPSETTLEINFO where " + hisWhere +
                                "settledate >= to_date('" + dateStrForm + "',' yyyy-mm-dd') and " +
                                "settledate < to_date('" + dateStrTo + "',' yyyy-mm-dd') ";
                    }
                }

                if (!hisSql.equals("")) {
                    try {
                        if (raTask == null) {
                            OracleDB<YbDeptHis> oracleDB = new OracleDB<>();
                            departList = oracleDB.excuteSqlRS(new YbDeptHis(), "select * from his.V_SAP_DEPART");
                            if (departList.size() > 0) {
                                //iYbDeptService.deleteBatchDepts();
                                iYbDeptService.createBatchDepts(departList);
                            }
                        }
                        departList = new ArrayList<>();

                        List<YbDept> deptList = iYbDeptService.findDeptList(new YbDept(), 0);
                        for (YbDept item : deptList) {
                            YbDeptHis his = new YbDeptHis();
                            his.setDeptId(item.getDeptId());
                            his.setDeptName(item.getDeptName());
                            his.setSpellCode(item.getSpellCode());
                            departList.add(his);
                        }

                        personList = iYbPersonService.list();

                        if (departList.size() > 0) {
                            List<YbReconsiderInpatientfees> createList = new ArrayList<>();
                            if (dataType == 0) {
                                List<YbReconsiderInpatientfeesData> rifDataList = new ArrayList<>();
                                List<YbReconsiderInpatientfeesData> queryRifDataList = new ArrayList<>();

                                OracleDB<YbReconsiderInpatientfeesData> oracleDB = new OracleDB<>();
                                rifDataList = oracleDB.excuteSqlRS(new YbReconsiderInpatientfeesData(), hisSql);
                                if (rifDataList.size() > 0) {
                                    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                                    for (YbReconsiderApplyData item : reconsiderApplyDataList) {
                                        queryRifDataList = rifDataList.stream().filter(
                                                s -> s.getTransNo().equals(item.getSerialNo()) &&
                                                        s.getItemName().equals(item.getProjectName()) &&
                                                        sdf.format(s.getFeeDate()).equals(sdf.format(item.getCostDate()))
                                        ).collect(Collectors.toList());

                                        if (queryRifDataList.size() == 0) {
                                            queryRifDataList = rifDataList.stream().filter(
                                                    s -> s.getTransNo().equals(item.getSerialNo()) &&
                                                            s.getItemName().equals(item.getProjectName())
                                            ).collect(Collectors.toList());
                                        }

                                        if (queryRifDataList.size() > 0) {
                                            YbReconsiderInpatientfeesData obj = queryRifDataList.get(0);
                                            YbReconsiderInpatientfees reconsiderInpatientfees = new YbReconsiderInpatientfees();
                                            reconsiderInpatientfees.setId(UUID.randomUUID().toString());
                                            reconsiderInpatientfees.setInpatientId(obj.getInpatientId());//住院号
                                            reconsiderInpatientfees.setPatientName(obj.getPatientName());//患者姓名
                                            reconsiderInpatientfees.setSettlementId(obj.getSettlementId());//HIS结算序号
                                            reconsiderInpatientfees.setBillNo(obj.getBillNo());//'单据号'
                                            reconsiderInpatientfees.setTransNo(obj.getTransNo());//'交易流水号'
                                            reconsiderInpatientfees.setItemId(obj.getItemId());//'项目代码'
                                            reconsiderInpatientfees.setItemCode(obj.getItemCode());//'项目医保编码'
                                            reconsiderInpatientfees.setItemName(obj.getItemName());//'项目名称'
                                            reconsiderInpatientfees.setItemCount(obj.getItemCount());//'项目数量'
                                            reconsiderInpatientfees.setItemPrice(obj.getItemPrice());//'项目单价'
                                            reconsiderInpatientfees.setItemAmount(obj.getItemAmount());//'项目金额'
                                            reconsiderInpatientfees.setFeeDate(obj.getFeeDate());//'费用日期'
                                            reconsiderInpatientfees.setDeptId(obj.getDeptId());//'住院科室代码'
                                            if (obj.getDeptId() != null && (obj.getDeptName() == null || obj.getDeptName().equals(""))) {
                                                queryDepartList = departList.stream().filter(
                                                        s -> s.getDeptId().equals(obj.getDeptId())
                                                ).collect(Collectors.toList());
                                                if (queryDepartList.size() > 0) {
                                                    reconsiderInpatientfees.setDeptName(queryDepartList.get(0).getDeptName());//'执行科室名称'
                                                }
                                            } else {
                                                reconsiderInpatientfees.setDeptName(obj.getDeptName());//'住院科室名称'
                                            }
                                            reconsiderInpatientfees.setOrderDocId(obj.getOrderDocId());//'开方医生代码'
                                            if (personList.size() > 0 && obj.getOrderDocId() != null && obj.getOrderDocName() == null) {
                                                queryPersontList = personList.stream().filter(p ->
                                                        p.getPersonCode().equals(obj.getOrderDocId())).collect(Collectors.toList());
                                                if (queryPersontList.size() > 0) {
                                                    reconsiderInpatientfees.setOrderDocName(queryPersontList.get(0).getPersonName());//'开方医生名称'
                                                }
                                            } else {
                                                reconsiderInpatientfees.setOrderDocName(obj.getOrderDocName());//'开方医生名称'
                                            }
                                            reconsiderInpatientfees.setExcuteDeptId(obj.getExcuteDeptId());//'执行科室代码'
                                            if (obj.getExcuteDeptId() != null && (obj.getExcuteDeptName() == null || obj.getExcuteDeptName().equals(""))) {
                                                queryDepartList = departList.stream().filter(
                                                        s -> s.getDeptId().equals(obj.getExcuteDeptId())
                                                ).collect(Collectors.toList());
                                                if (queryDepartList.size() > 0) {
                                                    reconsiderInpatientfees.setExcuteDeptName(queryDepartList.get(0).getDeptName());//'执行科室名称'
                                                }
                                            } else {
                                                reconsiderInpatientfees.setExcuteDeptName(obj.getExcuteDeptName());//'执行科室名称'
                                            }

                                            reconsiderInpatientfees.setExcuteDocId(obj.getExcuteDocId());//'执行医生代码'
                                            if (personList.size() > 0 && obj.getExcuteDocId() != null && obj.getExcuteDocName() == null) {
                                                queryPersontList = personList.stream().filter(p ->
                                                        p.getPersonCode().equals(obj.getExcuteDocId())).collect(Collectors.toList());
                                                if (queryPersontList.size() > 0) {
                                                    reconsiderInpatientfees.setExcuteDocName(queryPersontList.get(0).getPersonName());//'开方医生名称'
                                                }
                                            } else {
                                                reconsiderInpatientfees.setExcuteDocName(obj.getExcuteDocName());//'执行医生名称'
                                            }

                                            reconsiderInpatientfees.setSettlementDate(obj.getSettlementDate());//'结算时间'

                                            reconsiderInpatientfees.setMiCode(obj.getMiCode());
                                            reconsiderInpatientfees.setHisName(obj.getHisName());
                                            reconsiderInpatientfees.setMiName(obj.getMiName());

                                            reconsiderInpatientfees.setDyyz(obj.getDyyz());
                                            reconsiderInpatientfees.setAttendDocId(obj.getAttendDocId());
                                            reconsiderInpatientfees.setAttendDocName(obj.getAttendDocName());
                                            reconsiderInpatientfees.setItemTypeCode(obj.getItemTypeCode());
                                            reconsiderInpatientfees.setItemTypeName(obj.getItemTypeName());

                                            reconsiderInpatientfees.setFeeOperatorId(obj.getFeeOperatorId());
                                            reconsiderInpatientfees.setFeeOperatorName(obj.getFeeOperatorName());
                                            reconsiderInpatientfees.setFeeDeptId(obj.getFeeDeptId());
                                            reconsiderInpatientfees.setFeeDeptName(obj.getFeeDeptName());

                                            reconsiderInpatientfees.setApplyDataId(item.getId());
                                            reconsiderInpatientfees.setOrderNumber(item.getOrderNumber());//序号
                                            reconsiderInpatientfees.setApplyDateStr(applyDateStr);
                                            reconsiderInpatientfees.setDataType(dataType);
                                            reconsiderInpatientfees.setTypeno(typeno);
                                            reconsiderInpatientfees.setIsDeletemark(1);
//                                            reconsiderInpatientfees.setCreateTime(new Date());
                                            reconsiderInpatientfees.setState(state);
                                            reconsiderInpatientfees.setAreaType(reconsiderApply.getAreaType());
                                            createList.add(reconsiderInpatientfees);
                                        }
                                    }
                                } else {
                                    msg = "his接口明细扣款无数据.";
                                    log.error(msg);
                                }
                            } else {
                                List<YbReconsiderInpatientfeesMain> rifMainList = new ArrayList<>();
                                List<YbReconsiderInpatientfeesMain> queryRifMainList = new ArrayList<>();
                                OracleDB<YbReconsiderInpatientfeesMain> oracleDB = new OracleDB<>();
                                rifMainList = oracleDB.excuteSqlRS(new YbReconsiderInpatientfeesMain(), hisSql);
                                if (rifMainList.size() > 0) {
                                    for (YbReconsiderApplyData item : reconsiderApplyDataList) {
                                        queryRifMainList = rifMainList.stream().filter(
                                                s -> s.getTransNo().equals(item.getSerialNo())
                                        ).collect(Collectors.toList());

                                        if (queryRifMainList.size() > 0) {
                                            YbReconsiderInpatientfeesMain obj = queryRifMainList.get(0);
                                            YbReconsiderInpatientfees reconsiderInpatientfees = new YbReconsiderInpatientfees();
                                            reconsiderInpatientfees.setId(UUID.randomUUID().toString());
                                            reconsiderInpatientfees.setInpatientId(obj.getInpatientId());//住院号
                                            reconsiderInpatientfees.setPatientName(obj.getPatientName());//患者姓名
                                            reconsiderInpatientfees.setSettlementId(obj.getSettlementId());//HIS结算序号
                                            reconsiderInpatientfees.setBillNo(obj.getBillNo());//'单据号'
                                            reconsiderInpatientfees.setTransNo(obj.getTransNo());//'交易流水号'

                                            reconsiderInpatientfees.setOrderDocId(obj.getInHospDocId());//'入院责任医生代码'
                                            reconsiderInpatientfees.setOrderDocName(obj.getInHospDocName());//'入院责任医生名称'

                                            reconsiderInpatientfees.setDeptId(obj.getInHosDeptId());//'入院科室代码
                                            reconsiderInpatientfees.setDeptName(obj.getInHosDeptName());//'入院科室名称

                                            reconsiderInpatientfees.setExcuteDocId(obj.getInHospOpterId());//办入院操作员代码
                                            reconsiderInpatientfees.setExcuteDocName(obj.getInHospOpterName());//办入院操作员名称

                                            reconsiderInpatientfees.setExcuteDeptId(obj.getOpterDeptId());//办入院操作员科室代码
                                            reconsiderInpatientfees.setExcuteDeptName(obj.getOpterDeptName());//办入院操作员科室名称

                                            reconsiderInpatientfees.setSettlementDate(obj.getSettleDate());//'结算时间'

                                            reconsiderInpatientfees.setApplyDataId(item.getId());
                                            reconsiderInpatientfees.setOrderNumber(item.getOrderNumber());//序号
                                            reconsiderInpatientfees.setApplyDateStr(applyDateStr);
                                            reconsiderInpatientfees.setDataType(dataType);
                                            reconsiderInpatientfees.setTypeno(typeno);
                                            reconsiderInpatientfees.setIsDeletemark(1);
//                                            reconsiderInpatientfees.setCreateTime(new Date());
                                            reconsiderInpatientfees.setState(state);
                                            createList.add(reconsiderInpatientfees);
                                        }
                                    }
                                } else {
                                    msg = "his接口主单扣款无数据.";
                                    log.error(msg);
                                }
                            }
                            if (createList.size() > 0) {
                                msg = "His更新数据：" + createList.size() + "条";
                                iYbReconsiderInpatientfeesService.saveBatch(createList);
                                log.error(msg);
                            }

                            this.iYbReconsiderApplyTaskService.createYbReconsiderApplyTask(createTask);
                        } else {
                            msg = "his接口科室无数据.";
                            log.error(msg);
                        }
                    } catch (Exception e) {
                        msg = e.getMessage();
                        log.error(msg);
                    }
                } else {
                    msg = "his接口Where为空.";
                    log.error(msg);
                }
                System.out.println(msg);
//                System.out.println(hisSql);
//                System.out.println(hisWhere);
            }
        }

    }

    //得到定时任务最后一次创建日期的数据
//    private YbReconsiderApplyTask maxReconsiderApplyTask(List<YbReconsiderApplyTask> list) {
//        YbReconsiderApplyTask reconsiderApplyTask = new YbReconsiderApplyTask();
//        reconsiderApplyTask = list.get(0);
//        for (YbReconsiderApplyTask item : list) {
//            int n = item.getCreateTime().compareTo(reconsiderApplyTask.getCreateTime());
//            if (n > 0) {
//                reconsiderApplyTask = item;
//            } else if (n == 0) {
//
//            }
//        }
//        return reconsiderApplyTask;
//    }

    //创建定时任务Task数据
    private YbReconsiderApplyTask createReconsiderApplyTask(String applyDateStr,int areaType, int state, int dataType, int typeno, int currentPage, int totalRow) {
        YbReconsiderApplyTask createTask = new YbReconsiderApplyTask();
        //从orderNum开始
        int startNum = 0;
        //从orderNum结束
        int endNum = 0;

        //配置页数
        int taskInpatientCount = febsProperties.getTaskInpatientCount();
        //页数
        int pageSize = taskInpatientCount == 0 ? 500 : taskInpatientCount;
        //总页数
        int totalPage = 0;

        //第一次进入时会进行判断，设置默认值，当查询总数小于页数时，页数等于 查询总数， 总页数等于1
        if (totalRow <= pageSize) {
            pageSize = totalRow;
            totalPage = 1;
        } else {
            totalPage = (totalRow + pageSize - 1) / pageSize;
        }

        if (totalRow == pageSize) {
            startNum = 1;
            endNum = totalRow;
        } else {
            //第一次进入时会进行判断，设置默认值
            if (currentPage == 1) {
                startNum = 1;
                endNum = pageSize;
            } else {
                startNum = (currentPage - 1) * pageSize + 1;

                endNum = pageSize * currentPage;

                if (endNum >= totalRow) {
                    endNum = totalRow;
                }
            }
        }

        createTask.setApplyDateStr(applyDateStr);
        createTask.setDataType(dataType);
        createTask.setTypeno(typeno);
        createTask.setStartNum(startNum);
        createTask.setEndNum(endNum);
        createTask.setTotalRow(totalRow);
        createTask.setCurrentPage(currentPage);
        createTask.setPageSize(pageSize);
        createTask.setTotalPage(totalPage);
        createTask.setState(state);
        createTask.setAreaType(areaType);
//        createTask.setIsDeletemark(1);
//        createTask.setCreateTime(new Date());
        return createTask;
    }

    //获取集合中，拼接his Where in 语句
    private String hisTaskWhere(List<YbReconsiderApplyData> reconsiderApplyDataList, int dateType, int state) {
        String hisWhere = "";
        String sql = "";
        String sql1 = "";
        String sql2 = "";
        List<String> strList = new ArrayList<>();
        List<String> strList1 = new ArrayList<>();
        List<String> strList2 = new ArrayList<>();
        for (YbReconsiderApplyData item : reconsiderApplyDataList) {
            String[] arr = item.getSerialNo().split("-");
            String transno1 = arr[1] + "-" + item.getProjectName();
            String transno = item.getSerialNo();
            String project = item.getProjectName();
//            String project = item.getProjectCode();

            if (dateType == 0) {
                if (strList.stream().filter(s -> s.equals(transno)).count() == 0) {
                    strList.add(transno);
                    if (sql.equals("")) {
                        sql = "'" + transno + "'";
                    } else {
                        sql += ",'" + transno + "'";
                    }
                }

                if (strList1.stream().filter(s -> s.equals(project)).count() == 0) {
                    strList1.add(project);
                    if (sql1.equals("")) {
                        sql1 = "'" + project + "'";
                    } else {
                        sql1 += ",'" + project + "'";
                    }
                }

                if (strList2.stream().filter(s -> s.equals(transno1)).count() == 0) {
                    strList2.add(transno1);
                    if (sql2.equals("")) {
                        sql2 = "'" + transno1 + "'";
                    } else {
                        sql2 += ",'" + transno1 + "'";
                    }
                }
            } else {
                if (strList.stream().filter(s -> s.equals(transno)).count() == 0) {
                    strList.add(transno);
                    if (sql.equals("")) {
                        sql = "'" + transno + "'";
                    } else {
                        sql += ",'" + transno + "'";
                    }
                }
            }
        }
        if (dateType == 0) {
            //hisWhere = "itemKey in(" + sql + ")";
            hisWhere = " transno in(" + sql + ")";
//            hisWhere += " and itemcode in(" + sql1 + ") and ";

            if (state == 0) {
                hisWhere += " and itemname in(" + sql1 + ") and ";
            } else {
                hisWhere += " and HisName in(" + sql1 + ") and ";
            }
            //hisWhere = " itemybcode in(" + sql1 + ") and ";
            System.out.println(sql2);
        } else {
            hisWhere = " transno in(" + sql + ") and ";
        }
        return hisWhere;
    }

    //获取集合中，结算日期最小和最大日期（最大日期加一天）
    private String[] hisTaskDate(List<YbReconsiderApplyData> reconsiderApplyDataList) {
        String[] dateArr = new String[2];
        Date minDate = reconsiderApplyDataList.get(0).getSettlementDate();
        Date maxDate = reconsiderApplyDataList.get(0).getSettlementDate();
        for (YbReconsiderApplyData item : reconsiderApplyDataList) {
            if (item.getSettlementDate().compareTo(minDate) == -1) {
                minDate = item.getSettlementDate();
            }

            if (item.getSettlementDate().compareTo(maxDate) == 1) {
                maxDate = item.getSettlementDate();
            }
        }
        Calendar calendar = new GregorianCalendar();
        calendar.setTime(maxDate);
        calendar.add(calendar.DATE, 1);//把日期往后增加一天.整数往后推,负数往前移动
        maxDate = calendar.getTime();   //这个时间就是日期往后推一天的结果

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        dateArr[0] = sdf.format(minDate);
        dateArr[1] = sdf.format(maxDate);
        return dateArr;
    }
}