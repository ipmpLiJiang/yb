package cc.mrbird.febs.chs.service.impl;

import cc.mrbird.febs.chs.entity.YbChsApply;
import cc.mrbird.febs.chs.entity.YbChsResult;
import cc.mrbird.febs.chs.service.IYbChsApplyService;
import cc.mrbird.febs.chs.service.IYbChsResultService;
import cc.mrbird.febs.com.controller.DataTypeHelpers;
import cc.mrbird.febs.com.entity.ComFile;
import cc.mrbird.febs.com.entity.ComSms;
import cc.mrbird.febs.com.service.IComFileService;
import cc.mrbird.febs.com.service.IComSmsService;
import cc.mrbird.febs.common.domain.QueryRequest;
import cc.mrbird.febs.common.properties.FebsProperties;
import cc.mrbird.febs.common.utils.SortUtil;
import cc.mrbird.febs.chs.entity.YbChsManage;
import cc.mrbird.febs.chs.dao.YbChsManageMapper;
import cc.mrbird.febs.chs.service.IYbChsManageService;
import cc.mrbird.febs.yb.entity.YbDefaultValue;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.google.common.collect.Lists;
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
 * @since 2022-06-20
 */
@Slf4j
@Service("IYbChsManageService")
@Transactional(propagation = Propagation.SUPPORTS, readOnly = true, rollbackFor = Exception.class)
public class YbChsManageServiceImpl extends ServiceImpl<YbChsManageMapper, YbChsManage> implements IYbChsManageService {

    @Autowired
    IYbChsApplyService iYbChsApplyService;

    @Autowired
    IYbChsResultService iYbChsResultService;

    @Autowired
    IComSmsService iComSmsService;

    @Autowired
    FebsProperties febsProperties;

    @Autowired
    IComFileService iComFileService;

    @Override
    public IPage<YbChsManage> findYbChsManages(QueryRequest request, YbChsManage ybChsManage) {
        try {
            LambdaQueryWrapper<YbChsManage> queryWrapper = new LambdaQueryWrapper<>();
            if (ybChsManage.getApplyDateStr() != null) {
                queryWrapper.eq(YbChsManage::getApplyDateStr, ybChsManage.getApplyDateStr());
            }
            if (ybChsManage.getApplyDataId() != null) {
                queryWrapper.eq(YbChsManage::getApplyDataId, ybChsManage.getApplyDataId());
            }

            if (ybChsManage.getState() != null) {
                queryWrapper.eq(YbChsManage::getState, ybChsManage.getState());
            }


            Page<YbChsManage> page = new Page<>();
            SortUtil.handlePageSort(request, page, false);//true 是属性  false是数据库字段可两个
            return this.page(page, queryWrapper);
        } catch (Exception e) {
            log.error("获取字典信息失败", e);
            return null;
        }
    }

    @Override
    public IPage<YbChsManage> findYbChsManageList(QueryRequest request, YbChsManage ybChsManage) {
        try {
            Page<YbChsManage> page = new Page<>();
            SortUtil.handlePageSort(request, page, false);//true 是属性  false是数据库字段可两个
            return this.baseMapper.findYbChsManage(page, ybChsManage);
        } catch (Exception e) {
            log.error("获取失败", e);
            return null;
        }
    }

    @Override
    @Transactional
    public void createYbChsManage(YbChsManage ybChsManage) {
        ybChsManage.setId(UUID.randomUUID().toString());
//        ybChsManage.setCreateTime(new Date());
//        ybChsManage.setIsDeletemark(1);
        this.save(ybChsManage);
    }

    @Override
    @Transactional
    public void updateYbChsManage(YbChsManage ybChsManage) {
//        ybChsManage.setModifyTime(new Date());
        this.baseMapper.updateYbChsManage(ybChsManage);
    }

    @Override
    @Transactional
    public void deleteYbChsManages(String[] Ids) {
        List<String> list = Arrays.asList(Ids);
        this.baseMapper.deleteBatchIds(list);
    }

    @Override
    public List<YbChsManage> findChsManageList(YbChsManage ybChsManage) {
        LambdaQueryWrapper<YbChsManage> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(YbChsManage::getApplyDateStr, ybChsManage.getApplyDateStr());
        queryWrapper.eq(YbChsManage::getAreaType, ybChsManage.getAreaType());

        if (ybChsManage.getState() != null) {
            queryWrapper.eq(YbChsManage::getState, ybChsManage.getState());
        }
        if (ybChsManage.getReadyDoctorCode() != null) {
            queryWrapper.eq(YbChsManage::getReadyDoctorCode, ybChsManage.getReadyDoctorCode());
        }
        if (ybChsManage.getReadyDksId() != null) {
            queryWrapper.eq(YbChsManage::getReadyDksId, ybChsManage.getReadyDksId());
        }
        if (ybChsManage.getReadyDksName() != null) {
            queryWrapper.eq(YbChsManage::getReadyDksName, ybChsManage.getReadyDksName());
        }
        if (ybChsManage.getOrderNum() != null) {
            queryWrapper.eq(YbChsManage::getOrderNum, ybChsManage.getOrderNum());
        }
        return this.list(queryWrapper);
    }


    private Lock lockChsApplyEndDate = new ReentrantLock();

    @Override
    @Transactional
    public void updateChsApplyEndDate(String applyDateStr, Integer areaType) {
        if (StringUtils.isNotBlank(applyDateStr) && areaType != null) {
            if (lockChsApplyEndDate.tryLock()) {
                try {
                    YbChsApply chsApply = this.iYbChsApplyService.findChsApplyByApplyDateStrs(applyDateStr, areaType);
                    if (chsApply != null) {
                        Date thisDate = new Date();
                        if (thisDate.compareTo(chsApply.getEndDate()) > 0) {
                            List<YbChsManage> list = this.baseMapper.findChsManageApplyEndDateList(chsApply.getId(), applyDateStr, areaType);
                            LambdaQueryWrapper<YbChsResult> resultWrapper = new LambdaQueryWrapper<>();
                            resultWrapper.eq(YbChsResult::getApplyDateStr, applyDateStr);
                            resultWrapper.eq(YbChsResult::getAreaType, areaType);
                            List<YbChsResult> resultList = iYbChsResultService.list(resultWrapper);

                            List<YbChsManage> updateList = new ArrayList<>();
                            List<YbChsResult> createList = new ArrayList<>();
                            List<YbChsResult> resultQueryList = new ArrayList<>();
                            for (YbChsManage item : list) {
                                YbChsManage update = new YbChsManage();
                                update.setId(item.getId());
                                update.setState(YbDefaultValue.ACCEPTSTATE_7);
                                update.setOperateDate(thisDate);
                                update.setOperateReason("");
                                update.setOperateProcess(
                                        item.getState() == 0 ? "接受申请-未申诉" : item.getState() == 1 ? "待申诉-未申诉" : "已拒绝-未申诉"
                                );
                                updateList.add(update);

                                resultQueryList = resultList.stream().filter(s -> s.getId().equals(item.getId())).collect(Collectors.toList());
                                if (resultQueryList.size() == 0) {
                                    YbChsResult create = new YbChsResult();
                                    create.setId(item.getId());
                                    create.setApplyDataId(item.getApplyDataId());
                                    create.setVerifyId(item.getVerifyId());
                                    create.setManageId(item.getId());
                                    create.setDoctorCode(item.getReadyDoctorCode());
                                    create.setDoctorName(item.getReadyDoctorName());
                                    create.setDksId(item.getReadyDksId());
                                    create.setDksName(item.getReadyDksName());
                                    create.setFyid(item.getReadyFyid());
                                    create.setOperateReason("未申诉");
                                    create.setOperateDate(thisDate);
                                    create.setState(2);
                                    create.setApplyDateStr(item.getApplyDateStr());
                                    create.setOrderNum(item.getOrderNum());
                                    create.setAreaType(item.getAreaType());
                                    create.setDataType(item.getDataType());
                                    createList.add(create);
                                }
                            }
                            if (updateList.size() > 0) {
                                this.updateBatchById(updateList);
                            }

                            if (createList.size() > 0) {
                                this.iYbChsResultService.saveBatch(createList);
                                log.info(applyDateStr + " CHS截止日期: OK");
                                System.out.println(applyDateStr + " CHS截止日期: OK");
                            }
                        }
                    } else {
                        log.error(applyDateStr + " CHS截止日期: 未查询到CHS申请数据");
                        System.out.println(applyDateStr + " CHS截止日期: 未查询到CHS申请数据");
                    }
                } catch (Exception e) {
                    log.error(applyDateStr + " CHS截止日期:" + e.getMessage());
                    System.out.println(applyDateStr + " CHS截止日期:" + e.getMessage());
                } finally {
                    lockChsApplyEndDate.unlock();
                    System.out.println(applyDateStr + " CHS截止日期: 执行结束");
                }
            }
        } else {
            log.info(applyDateStr + " CHS截止日期，传入的参数有误.");
            System.out.println(applyDateStr + " CHS截止日期，传入的参数有误.");
        }
    }

    private Lock lockChsEnableOverdue = new ReentrantLock();

    @Override
    @Transactional
    public void updateChsEnableOverdue(String applyDateStr, Integer areaType) {
        if (StringUtils.isNotBlank(applyDateStr) && areaType != null) {
            if (lockChsEnableOverdue.tryLock()) {
                try {
                    YbChsApply chsApply = this.iYbChsApplyService.findChsApplyByApplyDateStrs(applyDateStr, areaType);
                    if (chsApply != null) {
                        Date thisDate = new Date();
                        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
                        String enableDate = formatter.format(thisDate);
                        List<YbChsManage> list = this.baseMapper.findChsManageEnableOverdueList(chsApply.getId(), applyDateStr, areaType, enableDate);
                        List<YbChsManage> updateList = new ArrayList<>();
                        for (YbChsManage item : list) {
                            YbChsManage update = new YbChsManage();
                            update.setId(item.getId());
                            update.setState(YbDefaultValue.ACCEPTSTATE_1);
                            update.setOperateReason("");
                            update.setOperateDate(thisDate);
                            update.setOperateProcess("待申诉-自动接受");
                            updateList.add(update);
                        }

                        if (updateList.size() > 0) {
                            this.updateBatchById(updateList);
                            log.info(applyDateStr + " CHS确认截止日期: OK");
                            System.out.println(applyDateStr + " CHS确认截止日期: OK");
                        }
                    } else {
                        log.error(applyDateStr + " CHS确认截止日期: 未查询到CHS申请数据");
                        System.out.println(applyDateStr + " CHS确认截止日期: 未查询到CHS申请数据");
                    }
                } catch (Exception e) {
                    log.error(applyDateStr + " CHS确认截止日期:" + e.getMessage());
                    System.out.println(applyDateStr + " CHS确认截止日期:" + e.getMessage());
                } finally {
                    lockChsEnableOverdue.unlock();
                    System.out.println(applyDateStr + " CHS确认截止日期: 执行结束");
                }
            } else {
                log.info(applyDateStr + " CHS确认截止日期，传入的参数有误.");
                System.out.println(applyDateStr + " CHS确认截止日期，传入的参数有误.");
            }
        }
    }


    //批量接收或单个拒绝
    @Override
    @Transactional
    public void updateAcceptRejectStates(List<YbChsManage> list) {
        List<YbChsManage> findList = new ArrayList<>();
        List<YbChsManage> queryList = new ArrayList<>();
        LambdaQueryWrapper<YbChsManage> wrapper = new LambdaQueryWrapper<>();
        List<String> idList = new ArrayList<>();
        for (YbChsManage item : list) {
            idList.add(item.getId());
        }
        if (idList.size() > 0) {
            wrapper.in(YbChsManage::getId, idList);
            findList = this.list(wrapper);
        }
        if (findList.size() > 0) {
            for (YbChsManage item : list) {
                queryList = findList.stream().filter(s -> s.getId().equals(item.getId())).collect(Collectors.toList());
                if (queryList.size() > 0) {
                    if (queryList.get(0).getState() == YbDefaultValue.AMSTATE_0) {
                        Date thisDate = new Date();
                        if (item.getState() == YbDefaultValue.AMSTATE_2) {
                            item.setOperateDate(thisDate);
                            item.setOperateProcess("接受申请-已拒绝");
                        } else if (item.getState() == YbDefaultValue.AMSTATE_1) {
                            item.setOperateDate(thisDate);
                            item.setOperateProcess("接受申请-待申诉");
                        }
                        LambdaQueryWrapper<YbChsManage> queryWrapper = new LambdaQueryWrapper<>();
                        queryWrapper.eq(YbChsManage::getState, YbDefaultValue.AMSTATE_0);
                        queryWrapper.eq(YbChsManage::getId, item.getId());

//                        String strChangeDksName = DataTypeHelpers.stringReplaceSetString(item.getChangeDksName(), item.getChangeDksId() + "-");
                        item.setChangeDksName(item.getChangeDksName());
//                        String strChangeDoctorName = DataTypeHelpers.stringReplaceSetString(item.getChangeDoctorName(), item.getChangeDoctorCode() + "-");
                        item.setChangeDoctorName(item.getChangeDoctorName());

                        this.baseMapper.update(item, queryWrapper);
                    }
                }
            }
        }
    }

    @Override
    @Transactional
    public String updateUploadStates(YbChsManage ybChsManage) {
        String message = "";
        YbChsManage entity = this.getById(ybChsManage.getId());
        if (entity != null) {
            Date thisDate = new Date();
            boolean isUpdate = false;
            isUpdate = iYbChsApplyService.findChsApplyCheckEndDate(entity.getApplyDateStr(), entity.getAreaType());
            if (isUpdate) {
                if (entity.getState() == YbDefaultValue.ACCEPTSTATE_1) {
                    isUpdate = true;
                    YbChsManage updateChsManage = new YbChsManage();
                    if (ybChsManage.getState() == YbDefaultValue.ACCEPTSTATE_6) {
                        List<ComFile> list = this.iComFileService.findListComFile(entity.getId(), null);
                        if (list.size() > 0) {
                            ybChsManage.setApplyDateStr(entity.getApplyDateStr());
                            ybChsManage.setOrderNum(entity.getOrderNum());
                            ybChsManage.setAreaType(entity.getAreaType());
                            ybChsManage.setDataType(entity.getDataType());
                            ybChsManage.setReadyFyid(entity.getReadyFyid());
                            isUpdate = this.createUpdateAcceptChsResult(ybChsManage, thisDate);

                            updateChsManage.setOperateProcess("待申诉-已申诉");
                        } else {
                            isUpdate = false;
                            message = "未上传申诉附件，无法提交！";
                        }
                    }
                    updateChsManage.setId(ybChsManage.getId());
                    updateChsManage.setOperateDate(thisDate);
                    updateChsManage.setOperateReason(ybChsManage.getOperateReason());
                    updateChsManage.setState(ybChsManage.getState());

                    if (isUpdate) {
                        LambdaQueryWrapper<YbChsManage> queryWrapper = new LambdaQueryWrapper<>();
                        queryWrapper.eq(YbChsManage::getId, ybChsManage.getId());
                        queryWrapper.eq(YbChsManage::getState, YbDefaultValue.ACCEPTSTATE_1);
                        int count = this.baseMapper.update(updateChsManage, queryWrapper);
                        if (count == 0) {
                            message = "申诉状态更新失败.";
                        } else {
                            message = "ok";
                        }
                    } else {
                        if(StringUtils.isBlank(message))
                            message = "结果数据创建失败.";
                    }
                } else {
                    message = "该申诉数据已申诉.";
                }
            } else {
                message = "当前已过截止日期，无法修改.";
            }
        } else {
            message = "未找到申诉数据.";
        }

        return message;
    }

    private boolean createUpdateAcceptChsResult(YbChsManage ybChsManage, Date thisDate) {
        YbChsResult newChsResult = new YbChsResult();
        newChsResult.setId(ybChsManage.getId());
        newChsResult.setApplyDataId(ybChsManage.getApplyDataId());
        newChsResult.setVerifyId(ybChsManage.getVerifyId());
        newChsResult.setManageId(ybChsManage.getId());

//        String strReadyDksName = DataTypeHelpers.stringReplaceSetString(ybChsManage.getReadyDksName(), ybChsManage.getReadyDksId() + "-");
        ybChsManage.setReadyDksName(ybChsManage.getReadyDksName());
//        String strReadyDoctorName = DataTypeHelpers.stringReplaceSetString(ybChsManage.getReadyDoctorName(), ybChsManage.getReadyDoctorCode() + "-");
        ybChsManage.setReadyDoctorName(ybChsManage.getReadyDoctorName());

        newChsResult.setDksId(ybChsManage.getReadyDksId());
        newChsResult.setDksName(ybChsManage.getReadyDksName());
        newChsResult.setFyid(ybChsManage.getReadyFyid());
        newChsResult.setDoctorCode(ybChsManage.getReadyDoctorCode());
        newChsResult.setDoctorName(ybChsManage.getReadyDoctorName());

        newChsResult.setApplyDateStr(ybChsManage.getApplyDateStr());
        newChsResult.setOrderNum(ybChsManage.getOrderNum());
        newChsResult.setAreaType(ybChsManage.getAreaType());
        newChsResult.setDataType(ybChsManage.getDataType());
        newChsResult.setOperateDate(thisDate);
        newChsResult.setOperateReason(ybChsManage.getOperateReason());
        newChsResult.setState(1);
        return this.iYbChsResultService.saveOrUpdate(newChsResult);
    }


    @Override
    @Transactional
    public String updateUploadStateCompleteds(YbChsManage ybChsManage) {
        String message = "";
        YbChsManage entity = this.getById(ybChsManage.getId());
        if (entity != null) {
            Date thisDate = new Date();
            boolean isUpdate = false;
            isUpdate = iYbChsApplyService.findChsApplyCheckEndDate(entity.getApplyDateStr(), entity.getAreaType());

            if (isUpdate) {
                if (entity.getState() == YbDefaultValue.ACCEPTSTATE_6) {
                    YbChsResult updateChsResult = new YbChsResult();
                    updateChsResult.setId(ybChsManage.getId());
                    updateChsResult.setOperateDate(thisDate);
                    updateChsResult.setOperateReason(ybChsManage.getOperateReason());
                    this.iYbChsResultService.updateById(updateChsResult);

                    YbChsManage updateChsManage = new YbChsManage();
                    updateChsManage.setId(ybChsManage.getId());
                    updateChsManage.setOperateDate(thisDate);
                    updateChsManage.setOperateReason(ybChsManage.getOperateReason());

                    int count = this.baseMapper.updateById(updateChsManage);
                    if (count == 0) {
                        message = "申诉状态更新失败.";
                    } else {
                        message = "ok";
                    }
                } else {
                    message = "未找到申诉数据1.";
                }
            } else {
                message = "当前已过截止日期，无法修改.";
            }
        } else {
            message = "未找到申诉数据2.";
        }

        return message;
    }

    /**
     * 审核变更 同意、拒绝
     * @param ybChsManage
     * @param uId
     * @param Uname
     * @param type
     */
    @Override
    @Transactional
    public void updateCreateChsManage(YbChsManage ybChsManage, Long uId, String Uname, Integer type) {
        YbChsManage entity = this.getById(ybChsManage.getId());
        if (entity != null && entity.getState() == YbDefaultValue.ACCEPTSTATE_2) {
            YbChsManage newChsManage = new YbChsManage();
            String personCode = "";
            Date thisDate = new Date();
            newChsManage.setId(UUID.randomUUID().toString());
            newChsManage.setApplyDataId(ybChsManage.getApplyDataId());
            newChsManage.setVerifyId(ybChsManage.getVerifyId());
            newChsManage.setState(YbDefaultValue.ACCEPTSTATE_0);

            Date newDate = this.addSecond(thisDate, 10);
            newChsManage.setOperateDate(newDate);
            newChsManage.setAreaType(entity.getAreaType());

            Date enableDate = entity.getEnableDate();
            newChsManage.setEnableDate(enableDate);
            newChsManage.setOperateProcess("变更申请-创建");

            newChsManage.setApplyDateStr(entity.getApplyDateStr());
            newChsManage.setOrderNum(entity.getOrderNum());
            newChsManage.setDataType(entity.getDataType());

//            String strReadyDksName = DataTypeHelpers.stringReplaceSetString(ybChsManage.getReadyDksName(), ybChsManage.getReadyDksId() + "-");
            ybChsManage.setReadyDksName(ybChsManage.getReadyDksName());
//            String strReadyDoctorName = DataTypeHelpers.stringReplaceSetString(ybChsManage.getReadyDoctorName(), ybChsManage.getReadyDoctorCode() + "-");
            ybChsManage.setReadyDoctorName(ybChsManage.getReadyDoctorName());

//            String strChangeDksName = DataTypeHelpers.stringReplaceSetString(ybChsManage.getChangeDksName(), ybChsManage.getChangeDksId() + "-");
            ybChsManage.setChangeDksName(ybChsManage.getChangeDksName());
//            String strChangeDoctorName = DataTypeHelpers.stringReplaceSetString(ybChsManage.getChangeDoctorName(), ybChsManage.getChangeDoctorCode() + "-");
            ybChsManage.setChangeDoctorName(ybChsManage.getChangeDoctorName());

            YbChsManage updateManage = new YbChsManage();
            updateManage.setState(YbDefaultValue.ACCEPTSTATE_4);
            updateManage.setOperateDate(thisDate);
            if (type == 2) {
                //拒绝
                newChsManage.setReadyDksId(ybChsManage.getReadyDksId());
                newChsManage.setReadyDksName(ybChsManage.getReadyDksName());
                newChsManage.setReadyFyid(ybChsManage.getReadyFyid());
                newChsManage.setReadyDoctorCode(ybChsManage.getReadyDoctorCode());
                newChsManage.setReadyDoctorName(ybChsManage.getReadyDoctorName());

                updateManage.setRefuseId(uId);
                updateManage.setRefuseName(Uname);
                updateManage.setRefuseDate(thisDate);
                updateManage.setRefuseReason(ybChsManage.getRefuseReason());
                updateManage.setOperateProcess("变更申请-拒绝");
                updateManage.setApprovalState(YbDefaultValue.APPROVALSTATE_2);

                personCode = ybChsManage.getReadyDoctorCode();
            } else {
                //同意
                newChsManage.setReadyDksId(ybChsManage.getChangeDksId());
                newChsManage.setReadyDksName(ybChsManage.getChangeDksName());
                newChsManage.setReadyFyid(ybChsManage.getReadyFyid());
                newChsManage.setReadyDoctorCode(ybChsManage.getChangeDoctorCode());
                newChsManage.setReadyDoctorName(ybChsManage.getChangeDoctorName());

                updateManage.setOperateProcess("变更申请-同意");
                updateManage.setApprovalState(YbDefaultValue.APPROVALSTATE_1);

                updateManage.setChangeDksId(ybChsManage.getChangeDksId());
                updateManage.setChangeDksName(ybChsManage.getChangeDksName());
                updateManage.setChangeFyid(ybChsManage.getChangeFyid());
                updateManage.setChangeDoctorCode(ybChsManage.getChangeDoctorCode());
                updateManage.setChangeDoctorName(ybChsManage.getChangeDoctorName());

                personCode = ybChsManage.getChangeDoctorCode();
            }

            LambdaQueryWrapper<YbChsManage> queryWrapper = new LambdaQueryWrapper<>();
            queryWrapper.eq(YbChsManage::getState, YbDefaultValue.ACCEPTSTATE_2);
            queryWrapper.eq(YbChsManage::getId, ybChsManage.getId());
            this.baseMapper.update(updateManage, queryWrapper);

            this.save(newChsManage);
            int nOpenSms = febsProperties.getOpenSms();
            boolean isOpenSms = nOpenSms == 1 ? true : false;
            if (isOpenSms) {
                String sendContent = this.iYbChsApplyService.getSendMessage(entity.getApplyDateStr(), enableDate, entity.getAreaType(), true);
                int sendType23 = ComSms.SENDTYPE_23; // Chs医保办变更
                iComSmsService.sendSmsService(entity.getApplyDateStr(), null, personCode, sendType23, entity.getAreaType(), sendContent, uId, Uname);
            }
        }
    }

    private Date addSecond(Date date, int t) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        cal.add(Calendar.SECOND, t);// 24小时制
        return cal.getTime();
    }

    /**
     * 管理员变更
     * @param ybChsManage
     * @param uId
     * @param Uname
     */
    @Override
    @Transactional
    public void updateCreateAdminChsManage(YbChsManage ybChsManage, Long uId, String Uname) {
        Date thisDate = new Date();
        YbChsManage entity = this.getById(ybChsManage.getId());
        if (entity != null && (entity.getState() == YbDefaultValue.ACCEPTSTATE_0 ||
                entity.getState() == YbDefaultValue.ACCEPTSTATE_1 ||
                entity.getState() == YbDefaultValue.ACCEPTSTATE_6 ||
                entity.getState() == YbDefaultValue.ACCEPTSTATE_7)) {
            YbChsManage updateChsManage = new YbChsManage();
            updateChsManage.setId(ybChsManage.getId());
//            String strReadyDksName = DataTypeHelpers.stringReplaceSetString(ybChsManage.getReadyDksName(), ybChsManage.getReadyDksId() + "-");
//            String strReadyDoctorName = DataTypeHelpers.stringReplaceSetString(ybChsManage.getReadyDoctorName(), ybChsManage.getReadyDoctorCode() + "-");
            String strReadyDksName = ybChsManage.getReadyDksName();
            String strReadyDoctorName = ybChsManage.getReadyDoctorName();
            String strReadyFyid = ybChsManage.getReadyFyid();
            boolean isChang = false;

            if (!entity.getReadyDoctorCode().equals(ybChsManage.getReadyDoctorCode()) ||
                    !entity.getReadyDksId().equals(ybChsManage.getReadyDksId())) {
                isChang = true;
            }
            int n7State = 10;
            if (entity.getState() == YbDefaultValue.ACCEPTSTATE_0) {
                n7State = 0;
            }
            if (n7State != 0 && entity.getState() == YbDefaultValue.ACCEPTSTATE_7) {
                n7State = 7;
                if (isChang) {
                    n7State = 71;
                }
            }
            // 变更管理，未申诉，不新增，只修改
            if (n7State != 0 && n7State != 7 && n7State != 71 && isChang) {
                n7State = 0;
            }

            if (n7State != 0) {
                //修改当前 Change值
                if (n7State == 71) {
                    updateChsManage.setReadyDoctorCode(ybChsManage.getReadyDoctorCode());
                    updateChsManage.setReadyDoctorName(strReadyDoctorName);
                    updateChsManage.setReadyDksId(ybChsManage.getReadyDksId());
                    updateChsManage.setReadyDksName(strReadyDksName);
                    updateChsManage.setReadyFyid(strReadyFyid);
                    updateChsManage.setChangeDoctorCode(entity.getReadyDoctorCode());
                    updateChsManage.setChangeDoctorName(entity.getReadyDoctorName());
                    updateChsManage.setChangeDksId(entity.getReadyDksId());
                    updateChsManage.setChangeDksName(entity.getReadyDksName());
                    updateChsManage.setChangeFyid(entity.getReadyFyid());
                }
                updateChsManage.setState(ybChsManage.getState());
                updateChsManage.setOperateReason("");
            } else {
                updateChsManage.setState(YbDefaultValue.ACCEPTSTATE_3);
                updateChsManage.setChangeDoctorCode(ybChsManage.getReadyDoctorCode());
                updateChsManage.setChangeDoctorName(strReadyDoctorName);
                updateChsManage.setChangeDksId(ybChsManage.getReadyDksId());
                updateChsManage.setChangeDksName(strReadyDksName);
                updateChsManage.setChangeFyid(strReadyFyid);
            }
            if (entity.getState() == YbDefaultValue.ACCEPTSTATE_7 && ybChsManage.getState() == YbDefaultValue.ACCEPTSTATE_6) {
                updateChsManage.setOperateProcess("未申诉-已申诉");
            }
            if (entity.getState() == YbDefaultValue.ACCEPTSTATE_6 && ybChsManage.getState() == YbDefaultValue.ACCEPTSTATE_1) {
                updateChsManage.setOperateProcess("已申诉-待申诉");
            }
            if (entity.getState() == YbDefaultValue.ACCEPTSTATE_6 && ybChsManage.getState() == YbDefaultValue.ACCEPTSTATE_0) {
                updateChsManage.setOperateProcess("已申诉-接受申请");
            }
            if (entity.getState() == YbDefaultValue.ACCEPTSTATE_1 && ybChsManage.getState() == YbDefaultValue.ACCEPTSTATE_0) {
                updateChsManage.setOperateProcess("待申诉-接受申请");
            }
            if (entity.getState() == ybChsManage.getState()) {
                updateChsManage.setOperateProcess("非状态变更");
            }
            updateChsManage.setOperateDate(thisDate);
            updateChsManage.setAdminPersonId(uId);
            updateChsManage.setAdminPersonName(Uname);
            String adminReason = "管理员更改";
            if (entity.getState() == YbDefaultValue.ACCEPTSTATE_0) {
                adminReason += "-接受申请";
            } else if (entity.getState() == YbDefaultValue.ACCEPTSTATE_1) {
                adminReason += "-待申诉";
            } else if (entity.getState() == YbDefaultValue.ACCEPTSTATE_6) {
                adminReason += "-已申请";
            } else {
                adminReason += "-未申请";
            }

            updateChsManage.setAdminReason(adminReason);
            updateChsManage.setAdminChangeDate(thisDate);
            //方法 更改状态为0的数据 业务更改 管理员更改状态为1的数据 所有不调用该方法
            //方法 更改状态为1的数据
            LambdaQueryWrapper<YbChsManage> queryWrapper = new LambdaQueryWrapper<>();
            queryWrapper.eq(YbChsManage::getState, entity.getState());
            queryWrapper.eq(YbChsManage::getId, ybChsManage.getId());
            this.baseMapper.update(updateChsManage, queryWrapper);
            // 未申诉，修改结果 科室、人员 值
            if (n7State == 71) {
                YbChsResult ybChsResult = new YbChsResult();
                ybChsResult.setId(entity.getId());
                ybChsResult.setOperateReason("");
                ybChsResult.setDoctorCode(ybChsManage.getReadyDoctorCode());
                ybChsResult.setDoctorName(strReadyDoctorName);
                ybChsResult.setDksId(ybChsManage.getReadyDksId());
                ybChsResult.setDksName(strReadyDksName);
                ybChsResult.setFyid(strReadyFyid);
                iYbChsResultService.updateById(ybChsResult);
            }
            // 待申诉 和 已申诉 删除 结果和附件
            if (entity.getState() == YbDefaultValue.ACCEPTSTATE_1 ||
                    entity.getState() == YbDefaultValue.ACCEPTSTATE_6) {
                iYbChsResultService.getBaseMapper().deleteById(entity.getId());
                List<ComFile> list = this.iComFileService.findListComFile(entity.getId(), null);
                if (list.size() > 0) {
                    this.iComFileService.batchRefIdDelete(entity.getId());
                }

                String filePath = febsProperties.getUploadPath(); // 上传后的路径
                for (ComFile cf : list) {
                    String fileUrl = filePath + entity.getApplyDateStr() + "/CHS" + entity.getAreaType() +
                            "/" + entity.getOrderNum() + "/" + cf.getServerName();
                    DataTypeHelpers.deleteFile(fileUrl);
                }
            }
            // 管理员变更 复制新增 数据
            if (n7State == 0) {
                YbChsManage newChsManage = new YbChsManage();
                newChsManage.setId(UUID.randomUUID().toString());
                newChsManage.setApplyDataId(ybChsManage.getApplyDataId());
                newChsManage.setVerifyId(ybChsManage.getVerifyId());

//            int day = iComConfiguremanageService.getConfigDay();
                ////变更日期不变 this.addDateMethod(thisDate, day);
                Date enableDate = entity.getEnableDate();
                newChsManage.setEnableDate(enableDate);
                newChsManage.setState(ybChsManage.getState());

                thisDate = this.addSecond(thisDate, 10);
                newChsManage.setOperateDate(thisDate);

                newChsManage.setApplyDateStr(entity.getApplyDateStr());
                newChsManage.setOrderNum(entity.getOrderNum());
                ybChsManage.setReadyDksName(strReadyDksName);
                ybChsManage.setReadyFyid(ybChsManage.getReadyFyid());
                ybChsManage.setReadyDksId(ybChsManage.getReadyDksId());
                ybChsManage.setReadyDoctorName(strReadyDoctorName);

                newChsManage.setReadyDksId(ybChsManage.getReadyDksId());
                newChsManage.setReadyDksName(strReadyDksName);
                newChsManage.setReadyFyid(strReadyFyid);
                newChsManage.setReadyDoctorCode(ybChsManage.getReadyDoctorCode());
                newChsManage.setReadyDoctorName(ybChsManage.getReadyDoctorName());

                newChsManage.setAreaType(entity.getAreaType());
                newChsManage.setDataType(entity.getDataType());
                newChsManage.setOperateProcess("管理员更改-创建");

                String personCode = newChsManage.getReadyDoctorCode();

                this.save(newChsManage);

                int nOpenSms = febsProperties.getOpenSms();
                boolean isOpenSms = nOpenSms == 1 ? true : false;
                if (isOpenSms) {
                    if (entity.getState() == YbDefaultValue.ACCEPTSTATE_1) {
                        enableDate = null;
                    }
                    String sendContent = this.iYbChsApplyService.getSendMessage(entity.getApplyDateStr(), enableDate, entity.getAreaType(), true);
                    int sendType24 = ComSms.SENDTYPE_24;// Chs管理员变更
                    iComSmsService.sendSmsService(entity.getApplyDateStr(), null, personCode, sendType24, entity.getAreaType(), sendContent, uId, Uname);
                }
            }
        }
    }

    @Override
    public List<YbChsManage> findChsResultByStateList(YbChsManage ybChsManage, boolean ispp) {
        List<YbChsManage> list = new ArrayList<>();
        LambdaQueryWrapper<YbChsManage> wrapper = new LambdaQueryWrapper<>();
        if (StringUtils.isNotBlank(ybChsManage.getApplyDateStr())) {
            wrapper.eq(YbChsManage::getApplyDateStr, ybChsManage.getApplyDateStr());
        }
        if (ybChsManage.getAreaType() != null) {
            wrapper.eq(YbChsManage::getAreaType, ybChsManage.getAreaType());
        }

        if (ispp) {
            List<Integer> stateList = Lists.newArrayList(0, 1, 2);
            wrapper.in(YbChsManage::getState, stateList);
        } else {
            if (ybChsManage.getState() != null) {
                wrapper.eq(YbChsManage::getState, ybChsManage.getState());
            }
        }

        list = this.list(wrapper);
        return list;
    }
}