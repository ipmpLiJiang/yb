package cc.mrbird.febs.chs.service.impl;

import cc.mrbird.febs.chs.entity.YbChsApply;
import cc.mrbird.febs.chs.entity.YbChsApplyTask;
import cc.mrbird.febs.chs.entity.YbChsJk;
import cc.mrbird.febs.chs.service.IYbChsApplyService;
import cc.mrbird.febs.chs.service.IYbChsApplyTaskService;
import cc.mrbird.febs.chs.service.IYbChsJkService;
import cc.mrbird.febs.com.controller.DataTypeHelpers;
import cc.mrbird.febs.common.domain.QueryRequest;
import cc.mrbird.febs.common.properties.FebsProperties;
import cc.mrbird.febs.common.utils.OracleDB;
import cc.mrbird.febs.common.utils.SortUtil;
import cc.mrbird.febs.chs.entity.YbChsApplyData;
import cc.mrbird.febs.chs.dao.YbChsApplyDataMapper;
import cc.mrbird.febs.chs.service.IYbChsApplyDataService;
import cc.mrbird.febs.yb.entity.*;
import cc.mrbird.febs.yb.service.IYbDeptService;
import cc.mrbird.febs.yb.service.IYbPersonService;
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

import java.text.SimpleDateFormat;
import java.util.*;
import java.time.LocalDate;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;
import java.util.stream.Collectors;

/**
 * <p>
 * 服务实现类
 * </p>
 *
 * @author viki
 * @since 2022-06-21
 */
@Slf4j
@Service("IYbChsApplyDataService")
@Transactional(propagation = Propagation.SUPPORTS, readOnly = true, rollbackFor = Exception.class)
public class YbChsApplyDataServiceImpl extends ServiceImpl<YbChsApplyDataMapper, YbChsApplyData> implements IYbChsApplyDataService {

    @Autowired
    IYbChsApplyService iYbChsApplyService;
    @Autowired
    IYbChsJkService iYbChsJkService;
    @Autowired
    IYbChsApplyTaskService iYbChsApplyTaskService;
    @Autowired
    FebsProperties febsProperties;
    @Autowired
    IYbDeptService iYbDeptService;
    @Autowired
    IYbPersonService iYbPersonService;

    @Override
    public IPage<YbChsApplyData> findYbChsApplyDatas(QueryRequest request, YbChsApplyData ybChsApplyData) {
        try {
            LambdaQueryWrapper<YbChsApplyData> queryWrapper = new LambdaQueryWrapper<>();
            queryWrapper.eq(YbChsApplyData::getIsDeletemark, 1);//1是未删 0是已删


            Page<YbChsApplyData> page = new Page<>();
            SortUtil.handlePageSort(request, page, false);//true 是属性  false是数据库字段可两个
            return this.page(page, queryWrapper);
        } catch (Exception e) {
            log.error("获取字典信息失败", e);
            return null;
        }
    }

    @Override
    public IPage<YbChsApplyData> findYbChsApplyDataList(QueryRequest request, YbChsApplyData ybChsApplyData) {
        try {
            Page<YbChsApplyData> page = new Page<>();
            SortUtil.handlePageSort(request, page, false);//true 是属性  false是数据库字段可两个
            return this.baseMapper.findYbChsApplyData(page, ybChsApplyData);
        } catch (Exception e) {
            log.error("获取失败", e);
            return null;
        }
    }

    @Override
    @Transactional
    public void createYbChsApplyData(YbChsApplyData ybChsApplyData) {
        ybChsApplyData.setId(UUID.randomUUID().toString());
//        ybChsApplyData.setCreateTime(new Date());
        ybChsApplyData.setIsDeletemark(1);
        this.save(ybChsApplyData);
    }

    @Override
    @Transactional
    public void updateYbChsApplyData(YbChsApplyData ybChsApplyData) {
//        ybChsApplyData.setModifyTime(new Date());
        this.baseMapper.updateYbChsApplyData(ybChsApplyData);
    }

    @Override
    @Transactional
    public void deleteYbChsApplyDatas(String[] Ids) {
        List<String> list = Arrays.asList(Ids);
        this.baseMapper.deleteBatchIds(list);
    }

    @Override
    @Transactional
    public void importChsApply(YbChsApply ybChsApply, List<YbChsApplyData> listData) {
        if (listData.size() > 0) {
            this.saveBatch(listData);
        }

        this.iYbChsApplyService.updateYbChsApply(ybChsApply);
    }

    @Override
    @Transactional
    public String delChsJk(String applyDateStr, Integer areaType) {
        String msg = "";
        try {
            YbChsApply drgApply = this.iYbChsApplyService.findChsApplyByApplyDateStrs(applyDateStr, areaType);
            if (drgApply != null) {
                LambdaQueryWrapper<YbChsJk> wrapper = new LambdaQueryWrapper<>();
                wrapper.eq(YbChsJk::getApplyDateStr, applyDateStr);
                wrapper.eq(YbChsJk::getAreaType, areaType);
                boolean istrue = iYbChsJkService.remove(wrapper);
                if (istrue) {
                    YbChsApply updateApply = new YbChsApply();
                    updateApply.setId(drgApply.getId());
                    updateApply.setState(YbDefaultValue.APPLYSTATE_2);
                    iYbChsApplyService.updateYbChsApply(updateApply);
                    msg = "ok";
                } else {
                    msg = "no1";
                }
            } else {
                msg = "no";
            }
        } catch (Exception e) {
            msg = e.getMessage();
            log.error(msg);
        }
        return msg;
    }

    @Override
    @Transactional
    public int deleteChsApplyDataByPid(YbChsApplyData ybChsApplyData) {
        int count = 0;
        LambdaQueryWrapper<YbChsApply> wrapperApply = new LambdaQueryWrapper<>();
        wrapperApply.eq(YbChsApply::getId, ybChsApplyData.getPid());
        List<YbChsApply> applyList = iYbChsApplyService.list(wrapperApply);
        if (applyList.size() > 0) {
            if (applyList.get(0).getState() == YbDefaultValue.APPLYSTATE_2) {
                LambdaQueryWrapper<YbChsApplyData> wrapper = new LambdaQueryWrapper<>();
                wrapper.eq(YbChsApplyData::getPid, applyList.get(0).getId());
                this.baseMapper.delete(wrapper);

                YbChsApply updateApply = new YbChsApply();
                updateApply.setId(applyList.get(0).getId());
                updateApply.setState(YbDefaultValue.APPLYSTATE_1);
                iYbChsApplyService.updateYbChsApply(updateApply);
                count = 1;
            }
        }
        return count;
    }

    private Lock lockChsApp = new ReentrantLock();
    private Lock lockChsAppNot = new ReentrantLock();
    private Lock lockChsProjCodeAppNot = new ReentrantLock();

    @Override
    @Transactional
    public String findChsApplyProjCodeTask(String applyDateStr, Integer areaType, Integer isOutpfees) {
        String msg = "";
        if (lockChsProjCodeAppNot.tryLock()) {
            try {
                //当前复议年月
                if (StringUtils.isBlank(applyDateStr)) {
                    return "";
//                    applyDateStr = DataTypeHelpers.getUpNianYue();
                }
                YbChsApply chsApply = iYbChsApplyService.findChsApplyByApplyDateStrs(applyDateStr, areaType);
                if (chsApply == null) {
                    return "";
                }
                int state = 2;
                // isOutpfees 1 是门诊 2是住院 0未区分
                YbChsApplyTask raTask = this.getApplyTask(applyDateStr, areaType, state, isOutpfees);
                //总数
                int totalRow = 0;
                //当前页
                int currentPage = 1;
                boolean noUpdate = false;
                YbChsApplyTask createTask = new YbChsApplyTask();
                if (raTask == null) {
                    totalRow = this.baseMapper.findChsApplyDataNotCount(chsApply.getId(), chsApply.getApplyDateStr(), chsApply.getAreaType(), isOutpfees);
                    if (totalRow == 0) {
                        noUpdate = true;
                    } else {
                        createTask = createChsApplyTask(chsApply.getApplyDateStr(), chsApply.getAreaType(), state, currentPage, totalRow, isOutpfees);
                        List<YbChsApplyData> chsApplyDataList = this.baseMapper.findChsApplyDataNotJk(chsApply.getId(), chsApply.getApplyDateStr(), chsApply.getAreaType(), isOutpfees);
//                        chsApplyDataList = chsApplyDataList.stream().sorted(Comparator.comparing(YbChsApplyData::getOrderNum)).collect(Collectors.toList());
                        if (chsApplyDataList.size() > 0) {
                            List<YbChsApplyData> updateList = new ArrayList<>();
                            int i = 1;
                            for (YbChsApplyData item : chsApplyDataList) {
                                YbChsApplyData update = new YbChsApplyData();
                                update.setId(item.getId());
                                update.setState(state);
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
                        msg = "no";
                        System.out.println("notTask ok");
                    } else {
                        currentPage = raTask.getCurrentPage() + 1;
                        totalRow = raTask.getTotalRow();
                        createTask = createChsApplyTask(chsApply.getApplyDateStr(), chsApply.getAreaType(), state, currentPage, totalRow, isOutpfees);
                    }
                }
                if (!noUpdate) {
                    //从orderNum开始
                    int startNum = createTask.getStartNum();
                    //从orderNum结束
                    int endNum = createTask.getEndNum();

                    List<YbChsApplyData> chsApplyDataList = this.baseMapper.findChsApplyDataBetween(chsApply.getId(), startNum, endNum, state, isOutpfees);
//                    chsApplyDataList = chsApplyDataList.stream().sorted(Comparator.comparing(YbChsApplyData::getOrderNum)).collect(Collectors.toList());
                    if (chsApplyDataList.size() > 0) {
                        String hisSql = this.getHisSql(chsApply, chsApplyDataList, state, isOutpfees);

                        if (!hisSql.equals("")) {
                            List<YbDeptHis> departList = this.getDeptHisList();
                            List<YbPerson> personList = iYbPersonService.findPersonList(new YbPerson(), 0);

                            if (departList.size() > 0) {
                                List<YbChsJk> createList = new ArrayList<>();
                                List<YbReconsiderInpatientfeesData> rifDataList = new ArrayList<>();

                                OracleDB<YbReconsiderInpatientfeesData> oracleDB = new OracleDB<>();
                                rifDataList = oracleDB.excuteSqlRS(new YbReconsiderInpatientfeesData(), hisSql);
                                if (rifDataList.size() > 0) {
                                    createList = this.getCreateDataList(chsApplyDataList, rifDataList, departList, personList, chsApply.getApplyDateStr(), state, chsApply.getAreaType(), isOutpfees);
                                } else {
                                    msg = "hisDataNo";
                                    log.error("his接口明细扣款无数据.");
                                }

                                if (createList.size() > 0) {
                                    createTask.setHisCount(createList.size());
                                    msg = "update";
                                    iYbChsJkService.saveBatch(createList);
                                    log.error("His更新数据：" + createList.size() + "条");
                                }
                                createTask.setPageSize(chsApplyDataList.size());
                                this.iYbChsApplyTaskService.createYbChsApplyTask(createTask);
                                if (msg.equals("update")) {
                                    msg = "ok";
                                }
                            } else {
                                msg = "deptNo";
                                log.error("his接口科室无数据.");
                            }
                        } else {
                            msg = "hisSqlNo";
                            log.error("his接口Where为空.");
                        }
                    }
                }
            } catch (Exception e) {
                msg = e.getMessage();
                log.error(msg);
            } finally {
                lockChsProjCodeAppNot.unlock();
            }
        } else {
            msg = "lockNo";
        }
        System.out.println("findChsApplyProjCodeTask end:" + msg);
        return msg;
    }

    @Override
    @Transactional
    public String findChsApplyDataNotTask(String applyDateStr, Integer areaType, Integer isOutpfees) {
        String msg = "";
        if (lockChsAppNot.tryLock()) {
            try {
                //当前复议年月
                if (StringUtils.isBlank(applyDateStr)) {
                    return "";
//                    applyDateStr = DataTypeHelpers.getUpNianYue();
                }
                YbChsApply chsApply = iYbChsApplyService.findChsApplyByApplyDateStrs(applyDateStr, areaType);
                if (chsApply == null) {
                    return "";
                }

                int state = 1;
                // isOutpfees 1 是门诊 2是住院 0未区分
                YbChsApplyTask raTask = this.getApplyTask(applyDateStr, areaType, state, isOutpfees);
                //总数
                int totalRow = 0;
                //当前页
                int currentPage = 1;
                boolean noUpdate = false;
                YbChsApplyTask createTask = new YbChsApplyTask();
                if (raTask == null) {
                    totalRow = this.baseMapper.findChsApplyDataNotCount(chsApply.getId(), chsApply.getApplyDateStr(), chsApply.getAreaType(), isOutpfees);
                    if (totalRow == 0) {
                        noUpdate = true;
                    } else {
                        createTask = createChsApplyTask(chsApply.getApplyDateStr(), chsApply.getAreaType(), state, currentPage, totalRow, isOutpfees);
                        List<YbChsApplyData> chsApplyDataList = this.baseMapper.findChsApplyDataNotJk(chsApply.getId(), chsApply.getApplyDateStr(), chsApply.getAreaType(), isOutpfees);
//                        chsApplyDataList = chsApplyDataList.stream().sorted(Comparator.comparing(YbChsApplyData::getOrderNum)).collect(Collectors.toList());
                        if (chsApplyDataList.size() > 0) {
                            List<YbChsApplyData> updateList = new ArrayList<>();
                            int i = 1;
                            for (YbChsApplyData item : chsApplyDataList) {
                                YbChsApplyData update = new YbChsApplyData();
                                update.setId(item.getId());
                                update.setState(state);
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
                        msg = "no";
                        System.out.println("notTask ok");
                    } else {
                        currentPage = raTask.getCurrentPage() + 1;
                        totalRow = raTask.getTotalRow();
                        createTask = createChsApplyTask(chsApply.getApplyDateStr(), chsApply.getAreaType(), state, currentPage, totalRow, isOutpfees);
                    }
                }
                if (!noUpdate) {
                    //从orderNum开始
                    int startNum = createTask.getStartNum();
                    //从orderNum结束
                    int endNum = createTask.getEndNum();

                    List<YbChsApplyData> chsApplyDataList = this.baseMapper.findChsApplyDataBetween(chsApply.getId(), startNum, endNum, state, isOutpfees);
//                    chsApplyDataList = chsApplyDataList.stream().sorted(Comparator.comparing(YbChsApplyData::getOrderNum)).collect(Collectors.toList());
                    if (chsApplyDataList.size() > 0) {
                        String hisSql = this.getHisSql(chsApply, chsApplyDataList, state, isOutpfees);

                        if (!hisSql.equals("")) {
                            List<YbDeptHis> departList = this.getDeptHisList();
                            List<YbPerson> personList = iYbPersonService.findPersonList(new YbPerson(), 0);

                            if (departList.size() > 0) {
                                List<YbChsJk> createList = new ArrayList<>();
                                List<YbReconsiderInpatientfeesData> rifDataList = new ArrayList<>();
                                OracleDB<YbReconsiderInpatientfeesData> oracleDB = new OracleDB<>();
                                rifDataList = oracleDB.excuteSqlRS(new YbReconsiderInpatientfeesData(), hisSql);
                                if (rifDataList.size() > 0) {
                                    createList = this.getCreateDataList(chsApplyDataList, rifDataList, departList, personList, chsApply.getApplyDateStr(), state, chsApply.getAreaType(), isOutpfees);
                                } else {
                                    msg = "hisDataNo";
                                    log.error("his接口明细扣款无数据.");
                                }

                                if (createList.size() > 0) {
                                    createTask.setHisCount(createList.size());
                                    msg = "update";
                                    iYbChsJkService.saveBatch(createList);
                                    log.error("His更新数据：" + createList.size() + "条");
                                }
                                createTask.setPageSize(chsApplyDataList.size());
                                this.iYbChsApplyTaskService.createYbChsApplyTask(createTask);
                                if (msg.equals("update")) {
                                    msg = "ok";
                                }
                            } else {
                                msg = "deptNo";
                                log.error("his接口科室无数据.");
                            }
                        } else {
                            msg = "hisSqlNo";
                            log.error("his接口Where为空.");
                        }
                    }
                }
            } catch (Exception e) {
                msg = e.getMessage();
                log.error(msg);
            } finally {
                lockChsAppNot.unlock();
            }
        } else {
            msg = "lockNo";
        }
        System.out.println("findChsApplyDataNotTask end:" + msg);
        return msg;
    }

    @Override
    @Transactional
    public String findChsApplyDataTask(String applyDateStr, Integer areaType, Integer isOutpfees) {
        String msg = "";
        if (lockChsApp.tryLock()) {
            try {
                //当前复议年月
                if (StringUtils.isBlank(applyDateStr)) {
                    return "";
                }
                YbChsApply chsApply = iYbChsApplyService.findChsApplyByApplyDateStrs(applyDateStr, areaType);
                if (chsApply == null) {
                    return "";
                }
                int state = 0;
                // isOutpfees 1 是门诊 2是住院 0未区分
                YbChsApplyTask raTask = this.getApplyTask(applyDateStr, areaType, state, isOutpfees);
                //总数
                int totalRow = 0;
                //当前页
                int currentPage = 1;
                boolean noUpdate = false;
                YbChsApplyTask createTask = new YbChsApplyTask();
                if (raTask == null) {
                    totalRow = this.baseMapper.findChsApplyDataCount(chsApply.getId(), isOutpfees);
                    if (totalRow == 0) {
                        noUpdate = true;
                    } else {
                        createTask = createChsApplyTask(applyDateStr, areaType, state, currentPage, totalRow, isOutpfees);
                    }
                } else {
                    if (raTask.getCurrentPage().equals(raTask.getTotalPage())) {
                        msg = "no";
                        log.error("findChsApplyDataTask 没有数据了");
                        noUpdate = true;
                    } else {
                        currentPage = raTask.getCurrentPage() + 1;
                        totalRow = raTask.getTotalRow();
                        createTask = createChsApplyTask(applyDateStr, areaType, state, currentPage, totalRow, isOutpfees);
                    }
                }
                if (!noUpdate) {
                    //从orderNum开始
                    int startNum = createTask.getStartNum();
                    //从orderNum结束
                    int endNum = createTask.getEndNum();

                    List<YbChsApplyData> chsApplyDataList = this.baseMapper.findChsApplyDataBetween(chsApply.getId(), startNum, endNum, state, isOutpfees);
                    //chsApplyDataList = chsApplyDataList.stream().sorted(Comparator.comparing(YbChsApplyData::getOrderNum)).collect(Collectors.toList());
                    if (chsApplyDataList.size() > 0) {
                        String hisSql = this.getHisSql(chsApply, chsApplyDataList, state, isOutpfees);

                        if (!hisSql.equals("")) {
                            List<YbDeptHis> departList = new ArrayList<>();
                            if (raTask == null) {
                                OracleDB<YbDeptHis> oracleDB = new OracleDB<>();
                                departList = oracleDB.excuteSqlRS(new YbDeptHis(), "select * from his.V_SAP_DEPART");
                                if (departList.size() > 0) {
                                    //iYbDeptService.deleteBatchDepts();
                                    iYbDeptService.createBatchDepts(departList);
                                }
                            }
                            departList = this.getDeptHisList();

                            List<YbPerson> personList = iYbPersonService.findPersonList(new YbPerson(), 0);

                            if (departList.size() > 0) {
                                List<YbChsJk> createList = new ArrayList<>();
                                List<YbReconsiderInpatientfeesData> rifDataList = new ArrayList<>();

                                OracleDB<YbReconsiderInpatientfeesData> oracleDB = new OracleDB<>();
                                rifDataList = oracleDB.excuteSqlRS(new YbReconsiderInpatientfeesData(), hisSql);
                                if (rifDataList.size() > 0) {
                                    createList = this.getCreateDataList(chsApplyDataList, rifDataList, departList, personList, applyDateStr, state, areaType, isOutpfees);

                                } else {
                                    msg = "hisDataNo";
                                    log.error("his接口明细扣款无数据.");
                                }
                                if (createList.size() > 0) {
                                    createTask.setHisCount(createList.size());
                                    msg = "update";
                                    iYbChsJkService.saveBatch(createList);
                                    log.error("His更新数据：" + createList.size() + "条");
                                }
                                createTask.setPageSize(chsApplyDataList.size());
                                this.iYbChsApplyTaskService.createYbChsApplyTask(createTask);
                                if (msg.equals("update")) {
                                    msg = "ok";
                                }
                            } else {
                                msg = "deptNo";
                                log.error("his接口科室无数据.");
                            }
                        } else {
                            msg = "hisSqlNo";
                            log.error("his接口Where为空.");
                        }
                    } else {
                        msg = "dataNo";
                    }
                }
            } catch (Exception e) {
                msg = e.getMessage();
                log.error(msg);
            } finally {
                lockChsApp.unlock();
            }
        } else {
            msg = "lockNo";
        }
        System.out.println("findChsApplyDataTask end:" + msg);
        return msg;
    }

    private YbChsApplyTask getApplyTask(String applyDateStr, int areaType, int state, int isOutpfees) {
        YbChsApplyTask ybChsApplyTask = new YbChsApplyTask();
        ybChsApplyTask.setApplyDateStr(applyDateStr);
        ybChsApplyTask.setAreaType(areaType);
        ybChsApplyTask.setState(state);
        ybChsApplyTask.setIsOutpfees(isOutpfees);
        return this.iYbChsApplyTaskService.findChsApplyTasks(ybChsApplyTask);
    }

    private YbChsApplyTask createChsApplyTask(String applyDateStr, int areaType, int state, int currentPage, int totalRow, int isOutpfees) {
        YbChsApplyTask createTask = new YbChsApplyTask();
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
        createTask.setStartNum(startNum);
        createTask.setEndNum(endNum);
        createTask.setTotalRow(totalRow);
        createTask.setCurrentPage(currentPage);
        createTask.setPageSize(pageSize);
        createTask.setTotalPage(totalPage);
        createTask.setState(state);
        createTask.setAreaType(areaType);
        createTask.setIsOutpfees(isOutpfees);
//        createTask.setIsDeletemark(1);
//        createTask.setCreateTime(new Date());
        return createTask;
    }

    private String getHisSql(YbChsApply chsApply, List<YbChsApplyData> chsApplyDataList, int state, int isOutpfees) {
        String hisSql = "";
        String hisWhere = "";

        //String[] dateArr = hisTaskDate(chsApplyDataList);
        //查询his日期区间 开始
        String dateStrForm = chsApply.getApplyDateStr() + "-01";
//                String dateStrForm = dateArr[0];
        //查询his日期区间 结束
        String dateStrTo = DataTypeHelpers.stringDateFormatAddMonth(1, dateStrForm, "", false);
//                String dateStrTo = dateArr[1];
        if (isOutpfees == 0) {
            hisWhere = hisTaskWhere(chsApplyDataList, state);

            if (!hisWhere.equals("")) {
                String tab = "V_SAP_INPFEES";
                if (isOutpfees == 1) {
                    tab = "V_SAP_OUTPFEES";
                }
                hisSql = "select * from his." + tab + " where " + hisWhere +
                        "settlementdate >= to_date('" + dateStrForm + "',' yyyy-mm-dd') and " +
                        "settlementdate < to_date('" + dateStrTo + "',' yyyy-mm-dd') ";
            }
        } else {
            hisWhere = hisTaskWhere(chsApplyDataList, state);
            if (!hisWhere.equals("")) {
                hisSql = "select * from his.V_SAP_INPSETTLEINFO where " + hisWhere +
                        "settledate >= to_date('" + dateStrForm + "',' yyyy-mm-dd') and " +
                        "settledate < to_date('" + dateStrTo + "',' yyyy-mm-dd') ";
            }
        }
        return hisSql;
    }

    //获取集合中，拼接his Where in 语句
    private String hisTaskWhere(List<YbChsApplyData> chsApplyDataList, int state) {
        String hisWhere = "";
        String sql = "";
        String sql1 = "";
//        String sql2 = "";
        List<String> strList = new ArrayList<>();
        List<String> strList1 = new ArrayList<>();
//        List<String> strList2 = new ArrayList<>();
        for (YbChsApplyData item : chsApplyDataList) {
//            String[] arr = item.getSerialNo().split("-");
//            String transno1 = state == 2 ? arr[1] + "-" + item.getProjectCode() : arr[1] + "-" + item.getProjectName();
            String zymzNumber = item.getZymzNumber();
            String project1 = state == 2 ? item.getProjectCode() : item.getProjectName();
            project1 = project1.replace("，", ",");
            String[] parr = project1.split(",");
            project1 = parr[0];
            if (state == 2 && StringUtils.isNotBlank(project1)) {
                project1 = project1.toUpperCase();
            }
            String project = project1;
            if (strList.stream().filter(s -> s.equals(zymzNumber)).count() == 0) {
                strList.add(zymzNumber);
                if (sql.equals("")) {
                    sql = "'" + zymzNumber + "'";
                } else {
                    sql += ",'" + zymzNumber + "'";
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
        }

        hisWhere = " inpatientId in(" + sql + ")";

        if (state == 0) {
            hisWhere += " and itemname in(" + sql1 + ") and ";
        } else if (state == 1) {
            hisWhere += " and HisName in(" + sql1 + ") and ";
        } else {
            hisWhere += " and itemcode in(" + sql1 + ") and ";
        }
        return hisWhere;
    }

    private List<YbDeptHis> getDeptHisList() {
        List<YbDeptHis> departList = new ArrayList<>();
        List<YbDept> deptList = iYbDeptService.findDeptList(new YbDept(), 0);
        for (YbDept item : deptList) {
            YbDeptHis his = new YbDeptHis();
            his.setDeptId(item.getDeptId());
            his.setDeptName(item.getDeptName());
            his.setSpellCode(item.getSpellCode());
            his.setParentCode(item.getDksId());
            his.setBm_mc(item.getDksName());
            departList.add(his);
        }
        return departList;
    }


    private List<YbChsJk> getCreateDataList(List<YbChsApplyData> chsApplyDataList,
                                            List<YbReconsiderInpatientfeesData> rifDataList,
                                            List<YbDeptHis> departList,
                                            List<YbPerson> personList,
                                            String applyDateStr,
                                            int state, int areaType, int isOutpfees) {
        List<YbChsJk> createList = new ArrayList<>();
        List<YbReconsiderInpatientfeesData> queryRifDataList = new ArrayList<>();
        List<YbDeptHis> queryDepartList = new ArrayList<>();
        List<YbPerson> queryPersontList = new ArrayList<>();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        for (YbChsApplyData item : chsApplyDataList) {
            if (state == 1) {
                queryRifDataList = rifDataList.stream().filter(
                        s -> s.getInpatientId().equals(item.getZymzNumber()) &&
                                s.getHisName().equals(item.getProjectName()) &&
                                sdf.format(s.getFeeDate()).equals(sdf.format(item.getCostDate()))
                ).collect(Collectors.toList());

                if (queryRifDataList.size() == 0) {
                    queryRifDataList = rifDataList.stream().filter(
                            s -> s.getInpatientId().equals(item.getZymzNumber()) &&
                                    s.getHisName().equals(item.getProjectName())
                    ).collect(Collectors.toList());
                }
            } else if (state == 2) {
                if (item.getProjectCode() != null && !item.getProjectCode().equals("")) {
                    queryRifDataList = rifDataList.stream().filter(
                            s -> s.getInpatientId().equals(item.getZymzNumber()) &&
                                    s.getItemCode().equals(item.getProjectCode().toUpperCase()) &&
                                    sdf.format(s.getFeeDate()).equals(sdf.format(item.getCostDate()))
                    ).collect(Collectors.toList());

                    if (queryRifDataList.size() == 0) {
                        queryRifDataList = rifDataList.stream().filter(
                                s -> s.getInpatientId().equals(item.getZymzNumber()) &&
                                        s.getItemCode().equals(item.getProjectCode().toUpperCase())
                        ).collect(Collectors.toList());
                    }
                }
            } else {
                queryRifDataList = rifDataList.stream().filter(
                        s -> s.getInpatientId().equals(item.getZymzNumber()) &&
                                s.getItemName().equals(item.getProjectName()) &&
                                sdf.format(s.getFeeDate()).equals(sdf.format(item.getCostDate()))
                ).collect(Collectors.toList());

                if (queryRifDataList.size() == 0) {
                    queryRifDataList = rifDataList.stream().filter(
                            s -> s.getInpatientId().equals(item.getZymzNumber()) &&
                                    s.getItemName().equals(item.getProjectName())
                    ).collect(Collectors.toList());
                }
            }
            if (queryRifDataList.size() > 0) {
                YbReconsiderInpatientfeesData obj = queryRifDataList.get(0);
                YbChsJk chsJk = new YbChsJk();
                chsJk.setId(UUID.randomUUID().toString());
                chsJk.setInpatientId(obj.getInpatientId());//住院号
                chsJk.setPatientName(obj.getPatientName());//患者姓名
                chsJk.setSettlementId(obj.getSettlementId());//HIS结算序号
                chsJk.setBillNo(obj.getBillNo());//'单据号'
                chsJk.setTransNo(obj.getTransNo());//'交易流水号'
                chsJk.setItemId(obj.getItemId());//'项目代码'
                chsJk.setItemCode(obj.getItemCode());//'项目医保编码'
                chsJk.setItemName(obj.getItemName());//'项目名称'
                chsJk.setItemCount(obj.getItemCount());//'项目数量'
                chsJk.setItemPrice(obj.getItemPrice());//'项目单价'
                chsJk.setItemAmount(obj.getItemAmount());//'项目金额'
                chsJk.setFeeDate(obj.getFeeDate());//'费用日期'
                chsJk.setDeptId(obj.getDeptId());//'住院科室代码'
                if (obj.getDeptId() != null && (obj.getDeptName() == null || obj.getDeptName().equals(""))) {
                    queryDepartList = departList.stream().filter(
                            s -> s.getDeptId().equals(obj.getDeptId())
                    ).collect(Collectors.toList());
                    if (queryDepartList.size() > 0) {
                        chsJk.setDeptName(queryDepartList.get(0).getDeptName());//'执行科室名称'
                    }
                } else {
                    chsJk.setDeptName(obj.getDeptName());//'住院科室名称'
                }
                chsJk.setOrderDocId(obj.getOrderDocId());//'开方医生代码'
                if (personList.size() > 0 && obj.getOrderDocId() != null && obj.getOrderDocName() == null) {
                    queryPersontList = personList.stream().filter(p ->
                            p.getPersonCode().equals(obj.getOrderDocId())).collect(Collectors.toList());
                    if (queryPersontList.size() > 0) {
                        chsJk.setOrderDocName(queryPersontList.get(0).getPersonName());//'开方医生名称'
                    }
                } else {
                    chsJk.setOrderDocName(obj.getOrderDocName());//'开方医生名称'
                }
                chsJk.setExcuteDeptId(obj.getExcuteDeptId());//'执行科室代码'
                if (obj.getExcuteDeptId() != null && (obj.getExcuteDeptName() == null || obj.getExcuteDeptName().equals(""))) {
                    queryDepartList = departList.stream().filter(
                            s -> s.getDeptId().equals(obj.getExcuteDeptId())
                    ).collect(Collectors.toList());
                    if (queryDepartList.size() > 0) {
                        chsJk.setExcuteDeptName(queryDepartList.get(0).getDeptName());//'执行科室名称'
                    }
                } else {
                    chsJk.setExcuteDeptName(obj.getExcuteDeptName());//'执行科室名称'
                }

                chsJk.setExcuteDocId(obj.getExcuteDocId());//'执行医生代码'
                if (personList.size() > 0 && obj.getExcuteDocId() != null && obj.getExcuteDocName() == null) {
                    queryPersontList = personList.stream().filter(p ->
                            p.getPersonCode().equals(obj.getExcuteDocId())).collect(Collectors.toList());
                    if (queryPersontList.size() > 0) {
                        chsJk.setExcuteDocName(queryPersontList.get(0).getPersonName());//'开方医生名称'
                    }
                } else {
                    chsJk.setExcuteDocName(obj.getExcuteDocName());//'执行医生名称'
                }

                chsJk.setSettlementDate(obj.getSettlementDate());//'结算时间'

                chsJk.setMiCode(obj.getMiCode());
                chsJk.setHisName(obj.getHisName());
                chsJk.setMiName(obj.getMiName());

                chsJk.setDyyz(obj.getDyyz());
                chsJk.setAttendDocId(obj.getAttendDocId());
                chsJk.setAttendDocName(obj.getAttendDocName());
                chsJk.setItemTypeCode(obj.getItemTypeCode());
                chsJk.setItemTypeName(obj.getItemTypeName());

                chsJk.setFeeOperatorId(obj.getFeeOperatorId());
                chsJk.setFeeOperatorName(obj.getFeeOperatorName());
                chsJk.setFeeDeptId(obj.getFeeDeptId());
                chsJk.setFeeDeptName(obj.getFeeDeptName());

                chsJk.setApplyDataId(item.getId());
                chsJk.setOrderNum(item.getOrderNum());//序号
                chsJk.setApplyDateStr(applyDateStr);
                chsJk.setIsDeletemark(1);
//                                            chsJk.setCreateTime(new Date());
                chsJk.setState(state);
                chsJk.setAreaType(areaType);
                chsJk.setIsOutpfees(isOutpfees);
                chsJk.setJzkh(obj.getJzkh());
                createList.add(chsJk);
            }
        }
        return createList;
    }


}