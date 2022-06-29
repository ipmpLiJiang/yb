package cc.mrbird.febs.chs.service.impl;

import cc.mrbird.febs.chs.entity.YbChsApply;
import cc.mrbird.febs.chs.entity.YbChsResult;
import cc.mrbird.febs.chs.service.IYbChsApplyService;
import cc.mrbird.febs.chs.service.IYbChsResultService;
import cc.mrbird.febs.common.domain.QueryRequest;
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

    @Override
    public IPage<YbChsManage> findYbChsManages(QueryRequest request, YbChsManage ybChsManage) {
        try {
            LambdaQueryWrapper<YbChsManage> queryWrapper = new LambdaQueryWrapper<>();
//            queryWrapper.eq(YbChsManage::getIsDeletemark, 1);//1是未删 0是已删


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
                            resultWrapper.eq(YbChsResult::getApplyDateStr,applyDateStr);
                            resultWrapper.eq(YbChsResult::getAreaType,areaType);
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

                                resultQueryList = resultList.stream().filter(s->s.getId().equals(item.getId())).collect(Collectors.toList());
                                if(resultQueryList.size() == 0) {
                                    YbChsResult create = new YbChsResult();
                                    create.setId(item.getId());
                                    create.setApplyDataId(item.getApplyDataId());
                                    create.setVerifyId(item.getVerifyId());
                                    create.setManageId(item.getId());
                                    create.setDoctorCode(item.getReadyDoctorCode());
                                    create.setDoctorName(item.getReadyDoctorName());
                                    create.setDksId(item.getReadyDksId());
                                    create.setDksName(item.getReadyDksName());
                                    create.setOperateReason("未申诉");
                                    create.setOperateDate(thisDate);
                                    create.setState(1);
                                    create.setApplyDateStr(item.getApplyDateStr());
                                    create.setOrderNum(item.getOrderNum());
                                    create.setAreaType(item.getAreaType());
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


}