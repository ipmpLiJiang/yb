package cc.mrbird.febs.yb.service.impl;

import cc.mrbird.febs.com.controller.DataTypeHelpers;
import cc.mrbird.febs.common.domain.QueryRequest;
import cc.mrbird.febs.common.properties.FebsProperties;
import cc.mrbird.febs.common.utils.OracleDB;
import cc.mrbird.febs.common.utils.OracleTest;
import cc.mrbird.febs.common.utils.SortUtil;
import cc.mrbird.febs.job.service.JobService;
import cc.mrbird.febs.yb.dao.YbReconsiderApplyDataMapper;
import cc.mrbird.febs.yb.entity.*;
import cc.mrbird.febs.yb.service.IYbReconsiderApplyDataService;
import cc.mrbird.febs.yb.service.IYbReconsiderApplyService;
import cc.mrbird.febs.yb.service.IYbReconsiderApplyTaskService;
import cc.mrbird.febs.yb.service.IYbReconsiderInpatientfeesService;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
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
        ybReconsiderApplyData.setCreateTime(new Date());
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
        ybReconsiderApplyData.setModifyTime(new Date());
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
    public List<YbReconsiderApplyData> findReconsiderApplyDataByApplyDates(String applyDateStr, Integer dataType) {
        return this.baseMapper.findReconsiderApplyDataByApplyDate(applyDateStr, dataType);
    }

    @Override
    public List<YbReconsiderApplyData> findReconsiderApplyDataByNotVerifys(String applyDateStr, Integer dataType, Integer typeno) {
        return this.baseMapper.findReconsiderApplyDataByNotVerify(applyDateStr, dataType, typeno);
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
            createMainList.add(rrMain);
            i++;
        }
        /*
        String sql = "";
        String sql1 = "";
        String sql2 = "";
        int count = 100;
        List<String> strList = new ArrayList<>();
        List<String> strList1 = new ArrayList<>();
        List<String> strList2 = new ArrayList<>();
        int i = 0;
        for (YbReconsiderApplyData item : listData) {
            if(strList1.stream().filter(s -> s.equals(item.getBillNo())).count()==0) {
                strList1.add(item.getBillNo());
                if (sql.equals("")) {
                    sql = "'" + item.getBillNo() + "'";
                } else {
                    sql += ",'" + item.getBillNo() + "'";
                }
                if (i == count) {
                    break;
                }
                i++;
            }
        }
        i=0;
        for (YbReconsiderApplyData item : listData) {
            if(strList.stream().filter(s -> s.equals(item.getProjectName())).count()==0) {
                strList.add(item.getProjectName());
                if (sql1.equals("")) {
                    sql1 = "'" + item.getProjectName() + "'";
                } else {
                    sql1 += ",'" + item.getProjectName() + "'";
                }
                if (i == count) {
                    break;
                }
                i++;
            }
        }

        i=0;
        for (YbReconsiderApplyData item : listData) {
            String pj = item.getBillNo() + "" + item.getProjectName();
            if (strList2.stream().filter(s -> s.equals(pj)).count() == 0) {
                strList2.add(pj);
                if (sql2.equals("")) {
                    sql2 = "'" + pj + "'";
                } else {
                    sql2 += ",'" + pj + "'";
                }
                if (i == count) {
                    break;
                }
                i++;
            }
        }

        System.out.println(sql);
        System.out.println(sql1);
        System.out.println(sql2);*/
        if (createDataList.size() > 0) {
            this.saveBatch(createDataList);
        }

        //Thread.sleep(1000);
        if (createMainList.size() > 0) {
            this.saveBatch(createMainList);
        }
    }

//    @Override
//    public List<YbReconsiderApplyData> findReconsiderApplyDataBetween(String applyDateStr, Integer dataType, Integer startNum, Integer endNum) {
//        return this.baseMapper.findReconsiderApplyDataBetween(applyDateStr,  dataType,  startNum,  endNum);
//    }
//
//    @Override
//    public int findReconsiderApplyDataCount(String applyDateStr, Integer dataType) {
//        return this.baseMapper.findReconsiderApplyDataCount(applyDateStr, dataType);
//    }

    @Override
    public void findReconsiderApplyDataTask() {
        int dataType = 0;
        //当前复议年月
        String applyDateStr = "2020-10";

        YbReconsiderApplyTask ybReconsiderApplyTask = new YbReconsiderApplyTask();
        ybReconsiderApplyTask.setApplyDateStr(applyDateStr);
        List<YbReconsiderApplyTask> raTaskList = this.iYbReconsiderApplyTaskService.findReconsiderApplyTaskList(ybReconsiderApplyTask);
        //List<YbReconsiderApplyTask> queryReconsiderApplyTask = new ArrayList<>();
        //总数
        int totalRow = 0;
        //当前页
        int currentPage = 1;

        boolean noUpdate = false;
        YbReconsiderApplyTask createTask = new YbReconsiderApplyTask();
        if (raTaskList.size() == 0) {
            totalRow = this.baseMapper.findReconsiderApplyDataCount(applyDateStr, dataType);
            if (totalRow == 0) {
                dataType = 1;
                totalRow = this.baseMapper.findReconsiderApplyDataCount(applyDateStr, dataType);
            }
            if (totalRow == 0) {
                noUpdate = true;
            } else {
                createTask = createReconsiderApplyTask(applyDateStr, dataType, currentPage, totalRow);
            }
        } else {
            YbReconsiderApplyTask reconsiderApplyTask = maxReconsiderApplyTask(raTaskList);
            if (reconsiderApplyTask.getCurrentPage().equals(reconsiderApplyTask.getTotalPage())) {
                if (reconsiderApplyTask.getDataType() == 0) {
                    dataType = 1;
                    totalRow = this.baseMapper.findReconsiderApplyDataCount(applyDateStr, dataType);
                    if (totalRow == 0) {
                        noUpdate = true;
                    } else {
                        createTask = createReconsiderApplyTask(applyDateStr, dataType, currentPage, totalRow);
                    }
                } else {
                    noUpdate = true;
                }
            } else {
                currentPage = reconsiderApplyTask.getCurrentPage() + 1;
                totalRow = reconsiderApplyTask.getTotalRow();
                dataType = reconsiderApplyTask.getDataType();
                createTask = createReconsiderApplyTask(applyDateStr, dataType, currentPage, totalRow);
            }
        }
        if (!noUpdate) {
            //从orderNum开始
            int startNum = createTask.getStartNum();
            //从orderNum结束
            int endNum = createTask.getEndNum();

            List<YbReconsiderApplyData> reconsiderApplyDataList = this.baseMapper.findReconsiderApplyDataBetween(applyDateStr, dataType, startNum, endNum);
            if (reconsiderApplyDataList.size() > 0) {
                String hisSql = "";
                String hisWhere = "";

                String[] dateArr = hisTaskDate(reconsiderApplyDataList);
                //查询his日期区间 开始
                String dateStrForm = dateArr[0];
                //查询his日期区间 结束
                String dateStrTo = dateArr[1];
                if (dataType == 0) {
                    hisWhere = hisTaskWhere(reconsiderApplyDataList, 0);

                    if (!hisWhere.equals("")) {
                        hisSql = "select * from his.V_SAP_INPFEES where " + hisWhere +
                                "settlementdate >= to_date('" + dateStrForm + "',' yyyy-mm-dd') and " +
                                "settlementdate < to_date('" + dateStrTo + "',' yyyy-mm-dd') ";
                    }
                } else {
                    hisWhere = hisTaskWhere(reconsiderApplyDataList, 1);
                    if (!hisWhere.equals("")) {
                        hisSql = "select * from his.V_SAP_INPSETTLEINFO where " + hisWhere +
                                "settledate >= to_date('" + dateStrForm + "',' yyyy-mm-dd') and " +
                                "settledate < to_date('" + dateStrTo + "',' yyyy-mm-dd') ";
                    }
                }

//                List<YbReconsiderInpatientfeesHis> hisList = new ArrayList<>();
//                List<YbReconsiderInpatientfeesHis> queryHisList = new ArrayList<>();
//
//                List<YbReconsiderInpatientfees> createList = new ArrayList<>();

//                if (!hisSql.equals("")) {
//                    OracleDB<YbReconsiderInpatientfeesHis> oracleDB = new OracleDB<>();
//                    //hisSql = "SELECT *  FROM his.V_SAP_INPFEES WHERE transno = '579988318-JSXH676208373' AND settlementdate >= to_date ( '2020-10-01', ' yyyy-mm-dd' ) AND settlementdate < to_date ( '2020-11-1', ' yyyy-mm-dd' ) AND ROWNUM < 1000";
//                    //hisList = oracleDB.excuteSqlRS(new YbReconsiderInpatientfeesHis(), hisSql);
//
//                    if (hisList.size() > 0) {
//                        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
//                        for (YbReconsiderApplyData item : reconsiderApplyDataList) {
//                            queryHisList = hisList.stream().filter(
//                                    s -> s.getTransNo().equals(item.getSerialNo()) &&
//                                            s.getItemName().equals(item.getProjectName()) &&
//                                            sdf.format(s.getFeeDate()).equals(sdf.format(item.getCostDate()))
//                            ).collect(Collectors.toList());
//
//                            if (queryHisList.size() > 0) {
//                                YbReconsiderInpatientfeesHis obj = queryHisList.get(0);
//                                YbReconsiderInpatientfees reconsiderInpatientfees = new YbReconsiderInpatientfees();
//                                reconsiderInpatientfees.setId(UUID.randomUUID().toString());
//                                reconsiderInpatientfees.setInpatientId(obj.getInpatientId());//住院号
//                                reconsiderInpatientfees.setPatientName(obj.getPatientName());//患者姓名
//                                reconsiderInpatientfees.setSettlementId(obj.getSettlementId());//HIS结算序号
//                                reconsiderInpatientfees.setBillNo(obj.getBillNo());//'单据号'
//                                reconsiderInpatientfees.setTransNo(obj.getTransNo());//'交易流水号'
//                                reconsiderInpatientfees.setItemId(obj.getTransNo());//'项目代码'
//                                reconsiderInpatientfees.setItemCode(obj.getItemCode());//'项目医保编码'
//                                reconsiderInpatientfees.setItemName(obj.getItemName());//'项目名称'
//                                reconsiderInpatientfees.setItemCount(obj.getItemCount());//'项目数量'
//                                reconsiderInpatientfees.setItemPrice(obj.getItemPrice());//'项目单价'
//                                reconsiderInpatientfees.setItemAmount(obj.getItemAmount());//'项目金额'
//                                reconsiderInpatientfees.setFeeDate(obj.getFeeDate());//'费用日期'
//                                reconsiderInpatientfees.setDeptId(obj.getDeptId());//'住院科室代码'
//                                reconsiderInpatientfees.setDeptName(obj.getDeptName());//'住院科室名称'
//                                reconsiderInpatientfees.setOrderDocId(obj.getOrderDocId());//'开方医生代码'
//                                reconsiderInpatientfees.setOrderDocName(obj.getOrderDocName());//'开方医生名称'
//                                reconsiderInpatientfees.setExcuteDeptId(obj.getExcuteDeptId());//'执行科室代码'
//                                reconsiderInpatientfees.setExcuteDeptName(obj.getExcuteDeptName());//'执行科室名称'
//                                reconsiderInpatientfees.setExcuteDocId(obj.getExcuteDocId());//'执行医生代码'
//                                reconsiderInpatientfees.setExcuteDocName(obj.getExcuteDocName());//'执行医生名称'
//                                reconsiderInpatientfees.setSettlementDate(obj.getSettlementDate());//'结算时间'
//
//                                reconsiderInpatientfees.setApplyDateStr(applyDateStr);
//                                reconsiderInpatientfees.setDataType(dataType);
//                                reconsiderInpatientfees.setIsDeletemark(1);
//                                reconsiderInpatientfees.setCreateTime(new Date());
//                                createList.add(reconsiderInpatientfees);
//                            }
//                        }
//                        if (createList.size() > 0) {
//                            iYbReconsiderInpatientfeesService.saveBatch(createList);
//                        }
//                    }
//
//                    this.iYbReconsiderApplyTaskService.createYbReconsiderApplyTask(createTask);
//                }
                this.iYbReconsiderApplyTaskService.createYbReconsiderApplyTask(createTask);
                System.out.println(hisSql);
                System.out.println(dateStrForm);
                System.out.println(dateStrTo);
                //System.out.println(hisWhere);
            }


        }
    }

    private YbReconsiderApplyTask maxReconsiderApplyTask(List<YbReconsiderApplyTask> list) {
        YbReconsiderApplyTask reconsiderApplyTask = new YbReconsiderApplyTask();
        reconsiderApplyTask = list.get(0);
        for (YbReconsiderApplyTask item : list) {
            int n = item.getCreateTime().compareTo(reconsiderApplyTask.getCreateTime());
            if (n > 0) {
                reconsiderApplyTask = item;
            } else if (n == 0) {

            }
        }
        return reconsiderApplyTask;
    }

    private YbReconsiderApplyTask createReconsiderApplyTask(String applyDateStr, int dataType, int currentPage, int totalRow) {
        YbReconsiderApplyTask createTask = new YbReconsiderApplyTask();
        //从orderNum开始
        int startNum = 0;
        //从orderNum结束
        int endNum = 0;

        //配置页数
        int taskInpatientCount = febsProperties.getTaskInpatientCount();
        //页数
        int pageSize = taskInpatientCount == 0 ? 100 : taskInpatientCount;
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
        createTask.setStartNum(startNum);
        createTask.setEndNum(endNum);
        createTask.setTotalRow(totalRow);
        createTask.setCurrentPage(currentPage);
        createTask.setPageSize(pageSize);
        createTask.setTotalPage(totalPage);

//        createTask.setIsDeletemark(1);
//        createTask.setCreateTime(new Date());
        return createTask;
    }

    private String hisTaskWhere(List<YbReconsiderApplyData> reconsiderApplyDataList, int dateType) {
        String hisWhere = "";
        String sql = "";
        String sql1 = "";
//        String sql2 = "";
        List<String> strList = new ArrayList<>();
        List<String> strList1 = new ArrayList<>();
//        List<String> strList2 = new ArrayList<>();
        for (YbReconsiderApplyData item : reconsiderApplyDataList) {
            String[] arr = item.getSerialNo().split("-");
//            String transno1 = arr[1] + "-" + item.getProjectCode();
            String transno = item.getSerialNo();
            String projectcode = item.getProjectCode();

            if (dateType == 0) {
                if (strList.stream().filter(s -> s.equals(transno)).count() == 0) {
                    strList.add(transno);
                    if (sql.equals("")) {
                        sql = "'" + transno + "'";
                    } else {
                        sql += ",'" + transno + "'";
                    }
                }

                if (strList1.stream().filter(s -> s.equals(projectcode)).count() == 0) {
                    strList1.add(projectcode);
                    if (sql1.equals("")) {
                        sql1 = "'" + projectcode + "'";
                    } else {
                        sql1 += ",'" + projectcode + "'";
                    }
                }

//                if (strList2.stream().filter(s -> s.equals(transno1)).count() == 0) {
//                    strList2.add(transno1);
//                    if (sql2.equals("")) {
//                        sql2 = "'" + transno1 + "'";
//                    } else {
//                        sql2 += ",'" + transno1 + "'";
//                    }
//                }
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
            hisWhere += " and itemcode in(" + sql1 + ") and ";

            //hisWhere = " itemybcode in(" + sql1 + ") and ";
        } else {
            hisWhere = " transno in(" + sql + ") and ";
        }
        return hisWhere;
    }

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