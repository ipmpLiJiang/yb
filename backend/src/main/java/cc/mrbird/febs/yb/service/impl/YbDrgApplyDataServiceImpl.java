package cc.mrbird.febs.yb.service.impl;

import cc.mrbird.febs.com.controller.DataTypeHelpers;
import cc.mrbird.febs.com.entity.ComConfiguremanage;
import cc.mrbird.febs.com.service.IComConfiguremanageService;
import cc.mrbird.febs.common.domain.QueryRequest;
import cc.mrbird.febs.common.properties.FebsProperties;
import cc.mrbird.febs.common.utils.OracleDB;
import cc.mrbird.febs.common.utils.SortUtil;
import cc.mrbird.febs.yb.dao.YbDrgApplyDataMapper;
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

import java.text.SimpleDateFormat;
import java.util.*;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;
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
@Service("IYbDrgApplyDataService")
@Transactional(propagation = Propagation.SUPPORTS, readOnly = true, rollbackFor = Exception.class)
public class YbDrgApplyDataServiceImpl extends ServiceImpl<YbDrgApplyDataMapper, YbDrgApplyData> implements IYbDrgApplyDataService {

    @Autowired
    private IYbDrgApplyService iYbDrgApplyService;

//    @Autowired
//    private IYbDrgInpatientfeesService iYbDrgInpatientfeesService;
    @Autowired
    private IYbDeptService iYbDeptService;

    @Autowired
    private IYbPersonService iYbPersonService;

    @Autowired
    FebsProperties febsProperties;


    @Autowired
    IComConfiguremanageService iComConfiguremanageService;

    @Override
    public IPage<YbDrgApplyData> findYbDrgApplyDatas(QueryRequest request, YbDrgApplyData ybDrgApplyData) {
        try {
            LambdaQueryWrapper<YbDrgApplyData> queryWrapper = new LambdaQueryWrapper<>();

            Page<YbDrgApplyData> page = new Page<>();
            SortUtil.handlePageSort(request, page, false);//true 是属性  false是数据库字段可两个
            return this.page(page, queryWrapper);
        } catch (Exception e) {
            log.error("获取字典信息失败", e);
            return null;
        }
    }

    @Override
    public IPage<YbDrgApplyData> findYbDrgApplyDataList(QueryRequest request, YbDrgApplyData ybDrgApplyData) {
        try {
            Page<YbDrgApplyData> page = new Page<>();
//            ybDrgApplyData.setIsDeletemark(1);
            SortUtil.handlePageSort(request, page, false);//true 是属性  false是数据库字段可两个
            return this.baseMapper.findYbDrgApplyData(page, ybDrgApplyData);
        } catch (Exception e) {
            log.error("获取失败", e);
            return null;
        }
    }

    @Override
    @Transactional
    public void createYbDrgApplyData(YbDrgApplyData ybDrgApplyData) {
        if (ybDrgApplyData.getId() == null || "".equals(ybDrgApplyData.getId())) {
            ybDrgApplyData.setId(UUID.randomUUID().toString());
        }
        this.save(ybDrgApplyData);
    }

    @Override
    @Transactional
    public void createBatchYbDrgApplyData(List<YbDrgApplyData> list, YbDrgApply ybDrgApply) {

        this.saveBatch(list);

        iYbDrgApplyService.updateYbDrgApply(ybDrgApply);
    }

    @Override
    @Transactional
    public void updateYbDrgApplyData(YbDrgApplyData ybDrgApplyData) {
        this.baseMapper.updateYbDrgApplyData(ybDrgApplyData);
    }

    @Override
    @Transactional
    public void deleteYbDrgApplyDatas(String[] Ids) {
        List<String> list = Arrays.asList(Ids);
        this.baseMapper.deleteBatchIds(list);
    }

    @Override
    @Transactional
    public int deleteDrgApplyDataByPid(YbDrgApplyData ybDrgApplyData) {
        int count = 0;
        LambdaQueryWrapper<YbDrgApply> wrapperApply = new LambdaQueryWrapper<>();
        wrapperApply.eq(YbDrgApply::getId, ybDrgApplyData.getPid());
        List<YbDrgApply> applyList = iYbDrgApplyService.list(wrapperApply);
        if (applyList.size() > 0) {
            if (applyList.get(0).getState() == YbDefaultValue.APPLYSTATE_4 || applyList.get(0).getState() == YbDefaultValue.APPLYSTATE_2) {
                LambdaQueryWrapper<YbDrgApplyData> wrapper = new LambdaQueryWrapper<>();
                wrapper.eq(YbDrgApplyData::getPid, applyList.get(0).getId());
                this.baseMapper.delete(wrapper);


                int state = applyList.get(0).getState() == YbDefaultValue.APPLYSTATE_2 ? YbDefaultValue.APPLYSTATE_1 : YbDefaultValue.APPLYSTATE_3;
                YbDrgApply updateApply = new YbDrgApply();
                updateApply.setId(applyList.get(0).getId());
                updateApply.setState(state);
                iYbDrgApplyService.updateYbDrgApply(updateApply);
                count = 1;
            }
        }
        return count;
    }

    @Override
    public List<YbDrgApplyData> findDrgApplyDataByApplyDates(YbDrgApply reconsiderApply) {
        List<YbDrgApplyData> list = new ArrayList<>();
        if (reconsiderApply != null) {
            YbDrgApplyData reconsiderApplyData = new YbDrgApplyData();
            reconsiderApplyData.setPid(reconsiderApply.getId());
            return this.findList(reconsiderApplyData);
        }
        return list;
    }

    private List<YbDrgApplyData> findList(YbDrgApplyData reconsiderApplyData) {
        LambdaQueryWrapper<YbDrgApplyData> wrapper = new LambdaQueryWrapper<>();
        if (reconsiderApplyData.getId() != null) {
            wrapper.eq(YbDrgApplyData::getId, reconsiderApplyData.getId());
        }
        if (reconsiderApplyData.getPid() != null) {
            wrapper.eq(YbDrgApplyData::getPid, reconsiderApplyData.getPid());
        }
        return this.list(wrapper);

    }

    @Override
    public List<YbDrgApplyData> findDrgApplyDataList(YbDrgApplyData reconsiderApplyData) {
        return this.findList(reconsiderApplyData);
    }

    private List<YbPerson> getPersonList(){
        return iYbPersonService.findPersonList(new YbPerson(), 0);
    }

    private List<YbDeptHis> getDeptHisList() {
        List<YbDeptHis> departList = new ArrayList<>();
        List<YbDept> deptList = iYbDeptService.findDeptList(new YbDept(), 0);
        for (YbDept item : deptList) {
            YbDeptHis his = new YbDeptHis();
            his.setDeptId(item.getDeptId());
            his.setDeptName(item.getDeptName());
            his.setSpellCode(item.getSpellCode());
            departList.add(his);
        }
        return departList;
    }
/*
    private List<YbDrgInpatientfees> getCreateDataList(List<YbDrgApplyData> reconsiderApplyDataList,
                                                              List<YbDrgInpatientfeesData> rifDataList, List<YbDeptHis> departList, List<YbPerson> personList, String applyDateStr, int state, int areaType,int isOutpfees) {
        List<YbDrgInpatientfees> createList = new ArrayList<>();
        List<YbDrgInpatientfeesData> queryRifDataList = new ArrayList<>();
        List<YbDeptHis> queryDepartList = new ArrayList<>();
        List<YbPerson> queryPersontList = new ArrayList<>();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        for (YbDrgApplyData item : reconsiderApplyDataList) {
            if (state == 1) {
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
            } else if (state == 2) {
                if(item.getProjectCode() != null && item.getProjectCode() != "") {
                    queryRifDataList = rifDataList.stream().filter(
                            s -> s.getTransNo().equals(item.getSerialNo()) &&
                                    s.getItemCode().equals(item.getProjectCode().toUpperCase()) &&
                                    sdf.format(s.getFeeDate()).equals(sdf.format(item.getCostDate()))
                    ).collect(Collectors.toList());

                    if (queryRifDataList.size() == 0) {
                        queryRifDataList = rifDataList.stream().filter(
                                s -> s.getTransNo().equals(item.getSerialNo()) &&
                                        s.getItemCode().equals(item.getProjectCode().toUpperCase())
                        ).collect(Collectors.toList());
                    }
                }
            } else {
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
            }
            if (queryRifDataList.size() > 0) {
                YbDrgInpatientfeesData obj = queryRifDataList.get(0);
                YbDrgInpatientfees reconsiderInpatientfees = new YbDrgInpatientfees();
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
                reconsiderInpatientfees.setDataType(item.getDataType());
                reconsiderInpatientfees.setTypeno(item.getTypeno());
                reconsiderInpatientfees.setIsDeletemark(1);
//                                            reconsiderInpatientfees.setCreateTime(new Date());
                reconsiderInpatientfees.setState(state);
                reconsiderInpatientfees.setAreaType(areaType);
                reconsiderInpatientfees.setIsOutpfees(isOutpfees);
                reconsiderInpatientfees.setJzkh(obj.getJzkh());
                createList.add(reconsiderInpatientfees);
            }
        }
        return createList;
    }
*/
}