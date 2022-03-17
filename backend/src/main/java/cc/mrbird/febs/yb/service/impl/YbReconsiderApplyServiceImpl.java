package cc.mrbird.febs.yb.service.impl;

import cc.mrbird.febs.com.controller.DataTypeHelpers;
import cc.mrbird.febs.com.entity.ComConfiguremanage;
import cc.mrbird.febs.com.entity.ComSms;
import cc.mrbird.febs.com.service.IComConfiguremanageService;
import cc.mrbird.febs.com.service.IComSmsService;
import cc.mrbird.febs.common.domain.QueryRequest;
import cc.mrbird.febs.common.properties.FebsProperties;
import cc.mrbird.febs.common.utils.SortUtil;
import cc.mrbird.febs.job.domain.Job;
import cc.mrbird.febs.job.service.JobService;
import cc.mrbird.febs.yb.dao.YbReconsiderApplyMapper;
import cc.mrbird.febs.yb.entity.YbAppealManage;
import cc.mrbird.febs.yb.entity.YbAppealManageView;
import cc.mrbird.febs.yb.entity.YbDefaultValue;
import cc.mrbird.febs.yb.entity.YbReconsiderApply;
import cc.mrbird.febs.yb.service.IYbAppealManageService;
import cc.mrbird.febs.yb.service.IYbAppealManageViewService;
import cc.mrbird.febs.yb.service.IYbReconsiderApplyService;
import cn.hutool.core.date.DateUnit;
import cn.hutool.core.date.DateUtil;
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

import java.text.DateFormat;
import java.text.ParseException;
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
 * @since 2020-07-23
 */
@Slf4j
@Service("IYbReconsiderApplyService")
@Transactional(propagation = Propagation.SUPPORTS, readOnly = true, rollbackFor = Exception.class)
public class YbReconsiderApplyServiceImpl extends ServiceImpl<YbReconsiderApplyMapper, YbReconsiderApply> implements IYbReconsiderApplyService {

    @Autowired
    private IYbAppealManageViewService iYbAppealManageViewService;

    @Autowired
    private IYbAppealManageService iYbAppealManageService;

    @Autowired
    private JobService jobService;

    @Autowired
    FebsProperties febsProperties;

    @Autowired
    IComConfiguremanageService iComConfiguremanageService;

    @Autowired
    private IComSmsService iComSmsService;

    @Override
    public IPage<YbReconsiderApply> findYbReconsiderApplys(QueryRequest request, YbReconsiderApply ybReconsiderApply) {
        try {
            LambdaQueryWrapper<YbReconsiderApply> queryWrapper = new LambdaQueryWrapper<>();
            queryWrapper.eq(YbReconsiderApply::getAreaType, ybReconsiderApply.getAreaType());
            if (StringUtils.isNotBlank(ybReconsiderApply.getCreateTimeFrom()) && StringUtils.isNotBlank(ybReconsiderApply.getCreateTimeTo())) {
                queryWrapper
                        .ge(YbReconsiderApply::getCreateTime, ybReconsiderApply.getCreateTimeFrom())
                        .le(YbReconsiderApply::getCreateTime, ybReconsiderApply.getCreateTimeTo());
            }
            queryWrapper.eq(YbReconsiderApply::getIsDeletemark, 1);//1是未删 0是已删

            Page<YbReconsiderApply> page = new Page<>();
            SortUtil.handlePageSort(request, page, false);//true 是属性  false是数据库字段可两个
            return this.page(page, queryWrapper);
        } catch (Exception e) {
            log.error("获取字典信息失败", e);
            return null;
        }
    }

    @Override
    public IPage<YbReconsiderApply> findYbReconsiderApplyList(QueryRequest request, YbReconsiderApply ybReconsiderApply) {
        try {
            Page<YbReconsiderApply> page = new Page<>();
            SortUtil.handlePageSort(request, page, false);//true 是属性  false是数据库字段可两个
            return this.baseMapper.findYbReconsiderApply(page, ybReconsiderApply);
        } catch (Exception e) {
            log.error("获取失败", e);
            return null;
        }
    }

    @Override
    @Transactional
    public void createYbReconsiderApply(YbReconsiderApply ybReconsiderApply) {
        ybReconsiderApply.setCreateTime(new Date());
        if (ybReconsiderApply.getId() == null || "".equals(ybReconsiderApply.getId())) {
            ybReconsiderApply.setId(UUID.randomUUID().toString());
        }
        ybReconsiderApply.setIsDeletemark(1);
        this.save(ybReconsiderApply);
    }

    @Override
    @Transactional
    public String createReconsiderApplyCheck(YbReconsiderApply ybReconsiderApply) {
        String message = "";
        YbReconsiderApply reconsiderApply = this.findReconsiderApplyByApplyDateStrs(ybReconsiderApply.getApplyDateStr(), ybReconsiderApply.getAreaType());
        if (reconsiderApply == null) {
            ybReconsiderApply.setCreateTime(new Date());
            if (ybReconsiderApply.getId() == null || "".equals(ybReconsiderApply.getId())) {
                ybReconsiderApply.setId(UUID.randomUUID().toString());
            }
            ybReconsiderApply.setIsDeletemark(1);
            boolean bl = this.save(ybReconsiderApply);
            if (bl) {
                message = "ok";
            }
        } else {
            List<Integer> atList = new ArrayList<>();
            atList.add(5);
            List<ComConfiguremanage> ccsList = iComConfiguremanageService.getConfigLists(atList);
            if (ccsList.size() > 0) {
                ccsList = ccsList.stream().filter(s -> s.getIntField().equals(ybReconsiderApply.getAreaType())).collect(Collectors.toList());
                message = ccsList.get(0).getStringField() + " 该年月 " + ybReconsiderApply.getApplyDateStr() + " 已创建过复议申请记录";
            } else {
                message = "该年月 " + ybReconsiderApply.getApplyDateStr() + " 已创建过复议申请记录";
            }

        }
        return message;
    }

    @Override
    @Transactional
    public String updateYbReconsiderApply(YbReconsiderApply ybReconsiderApply, boolean isUpOverdue) throws ParseException {
        String msg = "ok";
        ybReconsiderApply.setModifyTime(new Date());
        ybReconsiderApply.setApplyDateStr(null);

        if (isUpOverdue) {
            msg = this.updateAppealManage(ybReconsiderApply);
            if (msg.equals("ok")) {
                this.baseMapper.updateYbReconsiderApply(ybReconsiderApply);
            }
        } else {
            this.baseMapper.updateYbReconsiderApply(ybReconsiderApply);
        }

        return msg;
    }

    @Override
    @Transactional
    public String updateYbReconsiderApply(YbReconsiderApply ybReconsiderApply, Integer isChangDate) {
        String msg = "";
        ybReconsiderApply.setModifyTime(new Date());
        YbReconsiderApply entity = this.getById(ybReconsiderApply.getId());
        if (entity != null) {
            if(entity.getState() == YbDefaultValue.APPLYSTATE_3 || entity.getState() == YbDefaultValue.APPLYSTATE_5) {
                long endMinute = 0;
                long enableDay = 0;
                int typeno = entity.getState() == YbDefaultValue.APPLYSTATE_3 ? 1 : 2;
                // 当前Apply日期
                Date enableDate = typeno == 1 ? entity.getEnableDateOne() : entity.getEnableDateTwo();
                Date endDate = typeno == 1 ? entity.getEndDateOne() : entity.getEndDateTwo();
                // 更改日期
                Date upEnableDate = typeno == 1 ? ybReconsiderApply.getEnableDateOne() : ybReconsiderApply.getEnableDateTwo();
                Date upEndDate = typeno == 1 ? ybReconsiderApply.getEndDateOne() : ybReconsiderApply.getEndDateTwo();

                endMinute = DateUtil.between(upEndDate, endDate, DateUnit.MINUTE);
                enableDay = DateUtil.between(upEnableDate, enableDate, DateUnit.DAY);

                if (isChangDate != null && isChangDate == 1) {
                    List<ComSms> updateSmsList = new ArrayList<>();
                    List<YbAppealManage> updateAmList = new ArrayList<>();

                    List<ComSms> smsList = new ArrayList<>();
                    List<YbAppealManage> appealManageList = new ArrayList<>();
                    if (enableDay != 0 || endMinute != 0) {
                        LambdaQueryWrapper<ComSms> wrapperSms = new LambdaQueryWrapper<>();
                        wrapperSms.eq(ComSms::getApplyDateStr, entity.getApplyDateStr());
                        wrapperSms.eq(ComSms::getAreaType, entity.getAreaType());
                        wrapperSms.eq(ComSms::getTypeno, typeno);
                        wrapperSms.eq(ComSms::getSendType, ComSms.SENDTYPE_1);
                        wrapperSms.eq(ComSms::getState, ComSms.STATE_0);
                        smsList = this.iComSmsService.list(wrapperSms);
                    }

                    if (enableDay != 0) {
                        LambdaQueryWrapper<YbAppealManage> wrapperAm = new LambdaQueryWrapper<>();
                        wrapperAm.eq(YbAppealManage::getApplyDateStr, entity.getApplyDateStr());
                        wrapperAm.eq(YbAppealManage::getAreaType, entity.getAreaType());
                        wrapperAm.eq(YbAppealManage::getTypeno, typeno);
                        appealManageList = iYbAppealManageService.list(wrapperAm);
                    }

                    enableDate = DataTypeHelpers.addDateMethod(upEnableDate, 1);

                    if (smsList.size() > 0) {
                        String sendContent = this.getChangSendMessage(entity.getApplyDateStr(), upEndDate, enableDate, entity.getAreaType(), typeno, false);
                        for (ComSms item : smsList) {
                            ComSms update = new ComSms();
                            update.setId(item.getId());
                            update.setSendcontent(sendContent);
                            updateSmsList.add(update);
                        }
                    }
                    if (appealManageList.size() > 0) {
                        for (YbAppealManage item : appealManageList) {
                            YbAppealManage update = new YbAppealManage();
                            update.setId(item.getId());
                            update.setEnableDate(enableDate);
                            updateAmList.add(update);
                        }
                    }

                    if (updateSmsList.size() > 0) {
                        iComSmsService.updateBatchById(updateSmsList);
                    }

                    if (updateAmList.size() > 0) {
                        iYbAppealManageService.updateBatchById(updateAmList);
                    }
                }

                if (enableDay != 0 || endMinute != 0) {
                    this.baseMapper.updateYbReconsiderApply(ybReconsiderApply);
                }
            } else {
                msg = entity.getApplyDateStr() + "当前状态无法更改数据.";
            }
        } else {
            msg = "未找到当前复议年月的申请数据.";
        }
        return  msg;
    }

    @Override
    @Transactional
    public String updateReconsiderApplyEndResetDate(YbReconsiderApply ybReconsiderApply) {
        String msg = "";
        YbReconsiderApply entity = this.getById(ybReconsiderApply.getId());
        if (entity != null && ybReconsiderApply.getEndDateReset() != null && entity.getState() == YbDefaultValue.APPLYSTATE_6 &&
                entity.getResetState() == 1){
            YbReconsiderApply update = new YbReconsiderApply();
            update.setId(ybReconsiderApply.getId());
            update.setEndDateReset(ybReconsiderApply.getEndDateReset());
            update.setModifyTime(new Date());
            this.baseMapper.updateYbReconsiderApply(update);
        } else {
            msg = "no";
        }
        return msg;
    }
    @Override
    @Transactional
    public void updateYbReconsiderApply(YbReconsiderApply ybReconsiderApply) {
        ybReconsiderApply.setModifyTime(new Date());
        this.baseMapper.updateYbReconsiderApply(ybReconsiderApply);
    }

    private Lock lockEnableOverdue = new ReentrantLock();

    @Override
    public void updateEnableOverdue(String applyDateStr, int areaType) {
        if (applyDateStr == null || "".equals(applyDateStr)) {
            applyDateStr = DataTypeHelpers.getUpNianYue();
        }
        if (lockEnableOverdue.tryLock()) {
            try {
                Map<String, Object> map = new HashMap<>();
                map.put("applyDateStr", applyDateStr);
                map.put("areaType", areaType);
                this.baseMapper.updateAppealManageEnableOverdue(map);
                String message = (String) map.get("message");
                log.info(applyDateStr + " 复议确认截止日期:" + message);
                System.out.println(applyDateStr + " 复议确认截止日期:" + message);
            } catch (Exception e) {
                log.error(applyDateStr + " 复议确认截止日期:" + e.getMessage());
                System.out.println(applyDateStr + " 复议确认截止日期:" + e.getMessage());
            } finally {
                lockEnableOverdue.unlock();
                System.out.println(applyDateStr + " 复议确认截止日期: 执行结束");
            }
        }
    }

    private Lock lockApplyEndDateOne = new ReentrantLock();

    @Override
    public void updateApplyEndDateOne(String applyDateStr, int areaType) {
        if (applyDateStr == null || "".equals(applyDateStr)) {
            applyDateStr = DataTypeHelpers.getUpNianYue();
        }
        if (lockApplyEndDateOne.tryLock()) {
            try {
                Map<String, Object> map = new HashMap<>();
                map.put("applyDateStr", applyDateStr);
                map.put("areaType", areaType);
                this.baseMapper.updateAppealManageApplyEndDateOne(map);
                String message = (String) map.get("message");
                log.info(applyDateStr + " One复议截止日期:" +message);
                System.out.println(applyDateStr + " One复议截止日期:" +message);
            } catch (Exception e) {
                log.error(applyDateStr + " One复议截止日期:" + e.getMessage());
                System.out.println(applyDateStr + " One复议截止日期:" + e.getMessage());
            } finally {
                lockApplyEndDateOne.unlock();
                System.out.println(applyDateStr + " One复议截止日期: 执行结束");
            }
        }
    }

    private Lock lockApplyEndDateTwo = new ReentrantLock();

    @Override
    public void updateApplyEndDateTwo(String applyDateStr, int areaType) {
        if (applyDateStr == null || "".equals(applyDateStr)) {
            applyDateStr = DataTypeHelpers.getUpNianYue();
        }
        if (lockApplyEndDateTwo.tryLock()) {
            try {
                Map<String, Object> map = new HashMap<>();
                map.put("applyDateStr", applyDateStr);
                map.put("areaType", areaType);
                this.baseMapper.updateAppealManageApplyEndDateTwo(map);
                String message = (String) map.get("message");
                log.info(applyDateStr + " Two复议截止日期:" + message);
                System.out.println(applyDateStr + " Two复议截止日期:" + message);
            } catch (Exception e) {
                log.error(applyDateStr + " Two复议截止日期:" + e.getMessage());
                System.out.println(applyDateStr + " Two复议截止日期:" + e.getMessage());
            } finally {
                lockApplyEndDateTwo.unlock();
                System.out.println(applyDateStr + " Two复议截止日期: 执行结束");
            }
        }
    }


    @Override
    @Transactional
    public void deleteYbReconsiderApplys(String[] Ids) {
        List<String> list = Arrays.asList(Ids);
        this.baseMapper.deleteBatchIds(list);
    }

    @Override
    @Transactional
    public void deleteBatchStateIdsYbReconsiderApplys(String[] Ids, Integer state) {
        List<String> list = Arrays.asList(Ids);
//        this.baseMapper.deleteBatchStateIdsYbReconsiderApply(listString, state);
        LambdaQueryWrapper<YbReconsiderApply> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(YbReconsiderApply::getState, state);
        wrapper.in(YbReconsiderApply::getId, list);
        this.baseMapper.delete(wrapper);
    }

    @Override
    @Transactional
    public boolean updateYbReconsiderApplyState(String applyDateStr, Integer areaType, Integer state) {
        boolean bl = false;
        YbReconsiderApply reconsiderApply = this.findReconsiderApplyByApplyDateStrs(applyDateStr, areaType);
        if (reconsiderApply != null) {
            YbReconsiderApply update = new YbReconsiderApply();
            update.setId(reconsiderApply.getId());
            update.setModifyTime(new Date());
            update.setState(state);
            int count = this.baseMapper.updateById(update);
            if (count > 0) {
                bl = true;
            }
        }

        return bl;
    }

    @Override
    public List<YbReconsiderApply> findReconsiderApplyList(YbReconsiderApply ybReconsiderApply) {
        LambdaQueryWrapper<YbReconsiderApply> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(YbReconsiderApply::getIsDeletemark, 1);

        if (ybReconsiderApply.getId() != null) {
            wrapper.eq(YbReconsiderApply::getId, ybReconsiderApply.getId());
        }
        if (ybReconsiderApply.getApplyDateStr() != null) {
            wrapper.eq(YbReconsiderApply::getApplyDateStr, ybReconsiderApply.getApplyDateStr());
        }
        if (ybReconsiderApply.getAreaType() != null) {
            wrapper.eq(YbReconsiderApply::getAreaType, ybReconsiderApply.getAreaType());
        }
        if (ybReconsiderApply.getState() != null) {
            wrapper.eq(YbReconsiderApply::getState, ybReconsiderApply.getState());
        }
        if (ybReconsiderApply.getResetState() != null) {
            wrapper.eq(YbReconsiderApply::getResetState, ybReconsiderApply.getResetState());
        }
        if (ybReconsiderApply.getTaskState() != null) {
            wrapper.eq(YbReconsiderApply::getTaskState, ybReconsiderApply.getTaskState());
        }
        if (ybReconsiderApply.getRepayState() != null) {
            wrapper.eq(YbReconsiderApply::getRepayState, ybReconsiderApply.getRepayState());
        }
        return this.list(wrapper);
    }

    @Override
    public YbReconsiderApply findReconsiderApplyByApplyDateStrs(String appltDateStr, Integer areaType) {
        return this.baseMapper.findReconsiderApplyByApplyDateStr(appltDateStr, areaType);
    }

    @Override
    public boolean findReconsiderApplyCheckEndDate(String appltDateStr, Integer areaType, Integer typeno,int sourceType) {
        YbReconsiderApply reconsiderApply = this.findReconsiderApplyByApplyDateStrs(appltDateStr, areaType);
        boolean isUpdate = false;
        Date thisDate = new Date();
        Date compareDate = new Date();
        if(sourceType==YbDefaultValue.SOURCETYPE_0) {
            if (typeno == YbDefaultValue.TYPENO_1) {
                compareDate = reconsiderApply.getEndDateOne();
            } else {
                compareDate = reconsiderApply.getEndDateTwo();
            }
        } else {
            compareDate = reconsiderApply.getEndDateReset();
        }

        if (compareDate.compareTo(thisDate) == 1) {
            isUpdate = true;
        }
        return isUpdate;
    }

    @Transactional
    public String updateAppealManage(YbReconsiderApply ybReconsiderApply) throws ParseException {
        String msg = "";
        YbReconsiderApply yra = this.getById(ybReconsiderApply.getId());
        int state = yra.getState();

        YbAppealManageView queryAppealManage = new YbAppealManageView();
        queryAppealManage.setAcceptState(YbDefaultValue.ACCEPTSTATE_7);
        queryAppealManage.setApplyDateStr(yra.getApplyDateStr());
        queryAppealManage.setPid(yra.getId());
        queryAppealManage.setAreaType(ybReconsiderApply.getAreaType());

        SimpleDateFormat f = new SimpleDateFormat("yyyy-MM-dd hh:mm");
        Date aEndDateOne = null;
        Date bEndDateOne = null;
        boolean isTrue = false;
        if (state == YbDefaultValue.APPLYSTATE_3) {
            aEndDateOne = f.parse(f.format(yra.getEndDateOne()));
            bEndDateOne = f.parse(f.format(ybReconsiderApply.getEndDateOne()));
            queryAppealManage.setTypeno(YbDefaultValue.TYPENO_1);
            isTrue = true;
        }
        if (state == YbDefaultValue.APPLYSTATE_5) {
            aEndDateOne = f.parse(f.format(yra.getEndDateTwo()));
            bEndDateOne = f.parse(f.format(ybReconsiderApply.getEndDateTwo()));
            queryAppealManage.setTypeno(YbDefaultValue.TYPENO_2);
            isTrue = true;
        }

        if (isTrue) {
            List<YbAppealManageView> appealManageList = new ArrayList<>();
            List<YbAppealManage> updateAppealManageList = new ArrayList<>();
            if (aEndDateOne.before(bEndDateOne)) {
                appealManageList = iYbAppealManageViewService.findAppealManageViewList(queryAppealManage);
                if(appealManageList.size()>0) {
                    updateAppealManageList = iYbAppealManageService.getUpdateAppealManageList(appealManageList, bEndDateOne);
                    if (updateAppealManageList.size() > 0) {
                        isTrue = iYbAppealManageService.updateBatchById(updateAppealManageList);
                        if (isTrue) {
                            msg = "ok";
                        }
                    }
                } else {
                    msg = "nodata";
                }
            } else {
                msg = "date";
            }
        } else {
            msg = "nostate";
        }
        return msg;
    }

    @Override
    public String createJobState(String applyDateStr, Integer areaType) {
        String msg = "";
        YbReconsiderApply ybReconsiderApply = this.findReconsiderApplyByApplyDateStrs(applyDateStr, areaType);
        boolean isTrue = false;
        //无任务并且状态是上传一
        if (ybReconsiderApply.getTaskState() == YbReconsiderApply.TASK_STATE_0 && ybReconsiderApply.getState() == YbDefaultValue.APPLYSTATE_2) {
            isTrue = true;
        }
        //任务一并且状态是上传二
        if (ybReconsiderApply.getTaskState() == YbReconsiderApply.TASK_STATE_1 && ybReconsiderApply.getState() == YbDefaultValue.APPLYSTATE_4) {
            isTrue = true;
        }
        if (isTrue) {
            Date date = new Date();
            Calendar now = Calendar.getInstance();
            now.add(now.DATE, 1);
            String fen = "" + now.get(Calendar.MINUTE);
            String shi = "" + now.get(Calendar.HOUR_OF_DAY);
            String ri = "" + now.get(Calendar.DAY_OF_MONTH);
            String yue = "" + (now.get(Calendar.MONTH) + 1);
            String nian = "" + now.get(Calendar.YEAR);
            String miao = "" + now.get(Calendar.SECOND);
            String haomiao = "" + now.getTimeInMillis();

            String cron = "0 0/3 0 " + ri + " " + yue + " ? " + nian + "-" + nian;
//        String cron = "0" + " " + fen + " " + shi + " " + ri + " " + yue + " ? " + nian + "-" + nian;
            String remark = nian + "年" + yue + "月" + ri + "日" + shi + "时" + fen + "分" + miao + "秒" + haomiao;

            // Job state 0.正常    1.暂停
            Job job = new Job();
            job.setBeanName("reconsiderApplyTask");
            job.setMethodName("applyTask");
//            if (areaType == YbDefaultValue.AREATYPE_0) {
//                job.setMethodName("applyTask");
//            } else if (areaType == YbDefaultValue.AREATYPE_1) {
//                job.setMethodName("applyTaskWest");
//            } else {
//                job.setMethodName("applyTaskOther");
//            }
            job.setParams(applyDateStr + "|" + Integer.toString(areaType));
            //查询当前正在运行的任务
            List<Job> findList = jobService.jobList(job);

            job.setCronExpression(cron);
            job.setStatus("0");
            job.setCreateTime(date);
            job.setRemark("His:" + remark);
            //将正在运行的任务关闭
            String jobs = "";
            for (Job item : findList) {
                if (item.getStatus().equals("0")) {
                    if (jobs.equals("")) {
                        jobs = item.getJobId().toString();
                    } else {
                        jobs += "," + item.getJobId().toString();
                    }
                }
            }
            //将正在运行的任务关闭
            if (!jobs.equals("")) {
                jobService.pause(jobs);
            }
            //创建任务
            jobService.createJob(job);

            //启用当前任务
            List<Job> jobList = jobService.jobList(job);

            jobService.resume(jobList.get(0).getJobId().toString());

            YbReconsiderApply update = new YbReconsiderApply();
            update.setId(ybReconsiderApply.getId());
            update.setTaskState(ybReconsiderApply.getTaskState() == YbReconsiderApply.TASK_STATE_0 ? YbReconsiderApply.TASK_STATE_1 : YbReconsiderApply.TASK_STATE_2);

            this.updateById(update);
        } else {
            msg = "当前状态无法创建定时任务 或 任务已创建.";
        }
        return msg;
    }

    @Override
    public List<YbReconsiderApply> findReconsiderApplyByApplyDateStrs(List<String> listStr, Integer areaType, Integer resetState) {
        LambdaQueryWrapper<YbReconsiderApply> wrapper = new LambdaQueryWrapper<>();
        wrapper.in(YbReconsiderApply::getApplyDateStr, listStr);
        wrapper.eq(YbReconsiderApply::getAreaType, areaType);
        if (resetState != null) {
            wrapper.eq(YbReconsiderApply::getResetState, 1);
        }
        wrapper.eq(YbReconsiderApply::getIsDeletemark, 1);
        return this.list(wrapper);
    }

    /**
     * 核对发送和申诉变更短信内容
     *
     * @param applyDateStr 年月
     * @param enableDate   确认截止日期
     * @param areaType     院区
     * @param typeno       版本类型
     * @param isChange     是否申诉变更
     * @return 短信内容
     */
    @Override
    public String getSendMessage(String applyDateStr, Date enableDate, Integer areaType, int typeno, boolean isChange) {
        YbReconsiderApply entity = this.findReconsiderApplyByApplyDateStrs(applyDateStr, areaType);
        Date endDate = new Date();
        if (typeno == YbDefaultValue.TYPENO_1) {
            endDate = entity.getEndDateOne();
        } else {
            endDate = entity.getEndDateTwo();
        }
        return this.getChangSendMessage(applyDateStr, endDate, enableDate, areaType, typeno, isChange);

//        SimpleDateFormat sdf1 = new SimpleDateFormat("yyyy年MM月dd日 E HH:mm点");//HH时mm分ss秒
//        SimpleDateFormat sdf2 = new SimpleDateFormat("yyyy年MM月dd日 E");
//        String ssm = "";
//        if (typeno == YbDefaultValue.TYPENO_1) {
//            ssm = "第一版";
//            endDate = entity.getEndDateOne();
//        } else {
//            ssm = "第二版";
//            endDate = entity.getEndDateTwo();
//        }
//        String date1 = sdf1.format(endDate);
//
//        String wangz = febsProperties.getSmsWebsite();
//        applyDateStr = applyDateStr.replace("-", "年");
//        if (!isChange) {
//            Calendar cal = Calendar.getInstance();//使用默认时区和语言环境获得一个日历。
//            cal.setTime(enableDate);
//            cal.add(Calendar.DATE, -1);
//
//            String date2 = sdf2.format(cal.getTime()) + " 24:00 ";
//            if (enableDate.compareTo(endDate) == 1) {
//                ssm = "武汉市医保" + applyDateStr + "月" + ssm + " 复议数据已发给您，请在复议截止时间前完成 责任人确认工作和复议工作，此次复议截止时间是 " + date1 + "，请及时登录医保管理系统处理。" + wangz;
//            } else {
//                ssm = "武汉市医保" + applyDateStr + "月" + ssm + " 复议数据已发给您，请在 " + date2 + "前完成责任人确认工作，否则默认责任人为本人。此次复议截止时间是 " + date1 + "，请及时登录医保管理系统处理。" + wangz;
//            }
//        } else {
//            ssm = "您有其他医生转发的医保违规项目需复议，此次复议截止时间是" + date1 + "，请及时登录医保管理系统处理。" + wangz;
//        }
//        return ssm + this.areaMsg(areaType);
    }

    public String getChangSendMessage(String applyDateStr, Date endDate, Date enableDate, Integer areaType, int typeno, boolean isChange) {
        SimpleDateFormat sdf1 = new SimpleDateFormat("yyyy年MM月dd日 E HH:mm点");//HH时mm分ss秒
        SimpleDateFormat sdf2 = new SimpleDateFormat("yyyy年MM月dd日 E");
        String ssm = "";
        if (typeno == YbDefaultValue.TYPENO_1) {
            ssm = "第一版";
        } else {
            ssm = "第二版";
        }
        String date1 = sdf1.format(endDate);

        String wangz = febsProperties.getSmsWebsite();
        applyDateStr = applyDateStr.replace("-", "年");
        if (!isChange) {
            Calendar cal = Calendar.getInstance();//使用默认时区和语言环境获得一个日历。
            cal.setTime(enableDate);
            cal.add(Calendar.DATE, -1);

            String date2 = sdf2.format(cal.getTime()) + " 24:00 ";
            if (enableDate.compareTo(endDate) == 1) {
                ssm = "武汉市医保" + applyDateStr + "月" + ssm + " 复议数据已发给您，请在复议截止时间前完成 责任人确认工作和复议工作，此次复议截止时间是 " + date1 + "，请及时登录医保管理系统处理。" + wangz;
            } else {
                ssm = "武汉市医保" + applyDateStr + "月" + ssm + " 复议数据已发给您，请在 " + date2 + "前完成责任人确认工作，否则默认责任人为本人。此次复议截止时间是 " + date1 + "，请及时登录医保管理系统处理。" + wangz;
            }
        } else {
            ssm = "您有其他医生转发的医保违规项目需复议，此次复议截止时间是" + date1 + "，请及时登录医保管理系统处理。" + wangz;
        }
        return ssm + this.areaMsg(areaType);
    }


    /**
     * 复议截止当天短信内容
     *
     * @param applyDateStr 年月
     * @param endDate      复议截止日期
     * @param typeno       版本类型
     * @param areaType     院区
     * @return 短信内容
     */
    @Override
    public String getSendMessage(String applyDateStr, Date endDate, Integer typeno, Integer areaType) {
        applyDateStr = applyDateStr.replace("-", "年");
        String wangz = febsProperties.getSmsWebsite();
        String ssm = "";
        if (typeno == YbDefaultValue.TYPENO_1) {
            ssm = "第一版";
        } else {
            ssm = "第二版";
        }
        Calendar now = Calendar.getInstance();
        now.setTime(endDate);
        String fen = "" + now.get(Calendar.MINUTE);
        if (fen.length() == 1) {
            fen = "0" + fen;
        }
        String shi = "" + now.get(Calendar.HOUR_OF_DAY);
        ssm = "武汉市医保" + applyDateStr + "月" + ssm + "复议将于今天" + shi + ":" + fen + "截止，您尚有未处理的扣款，请登陆系统及时查看并处理。" + wangz;
        return ssm + this.areaMsg(areaType);
    }

    /**
     * 剔除短信内容
     *
     * @param applyDateStr 年月
     * @param areaType     院区
     * @return 短信内容
     */
    @Override
    public String getSendMessage(String applyDateStr, Integer areaType) {
//        YbReconsiderApply entity = this.findReconsiderApplyByApplyDateStrs(applyDateStr);
        applyDateStr = applyDateStr.replace("-", "年");
        String wangz = febsProperties.getSmsWebsite();
//        String ssm = "武汉市医保" + applyDateStr + "月复议结果已反馈，请登录医保管理系统-复议结果查询界面进行查看。" + wangz;
        String ssm = "医保中心已反馈武汉市医保" + applyDateStr + "复议结果，请登录医保管理系统-复议结果查询界面进行查看，" + wangz + "选择时间，点搜索即可。";

        return ssm + this.areaMsg(areaType);
    }

    @Override
    public String getSendNoticeMessage(String title, Integer areaType) {
        String wangz = febsProperties.getSmsWebsite();
        String ssm = "医保办发布了新通知《" + title + "》，请登陆医保管理系统及时查看并处理。" + wangz;
        return ssm + this.areaMsg(areaType);
    }

    public String areaMsg(Integer areaType) {
        String areaName = iComConfiguremanageService.getConfigAreaName(areaType);
        return "院区请选择“" + areaName + "”。（" + areaName + "）";
    }

    @Override
    public int getReconsiderApplyTypeno(String applyDateStr, Integer areaType) {
        int typeno = 1;
        YbReconsiderApply reconsiderApply = this.findReconsiderApplyByApplyDateStrs(applyDateStr, areaType);
        if (reconsiderApply != null) {
            typeno = this.getTypeno(reconsiderApply);

        }
        return typeno;
    }

    @Override
    public int getTypeno(YbReconsiderApply reconsiderApply) {
        int state = reconsiderApply.getState() != null ? reconsiderApply.getState() : 1;
        int typeno = state == YbDefaultValue.APPLYSTATE_2 || state == YbDefaultValue.APPLYSTATE_3 ? YbDefaultValue.TYPENO_1 :
                state == YbDefaultValue.APPLYSTATE_4 || state == YbDefaultValue.APPLYSTATE_5 ? YbDefaultValue.TYPENO_2 :
                        state > 5 ? 3 : 1;
        return typeno;
    }

    @Override
    @Transactional
    public void updateReconsiderApplyState2345(YbReconsiderApply reconsiderApply) {
        YbReconsiderApply updateEntity = new YbReconsiderApply();
        updateEntity.setId(reconsiderApply.getId());
        if (reconsiderApply.getState() == YbDefaultValue.APPLYSTATE_2) { //上传一
            updateEntity.setState(YbDefaultValue.APPLYSTATE_3);//申诉一/核对一
        } else if (reconsiderApply.getState() == YbDefaultValue.APPLYSTATE_4) {
            updateEntity.setState(YbDefaultValue.APPLYSTATE_5);//申诉二/核对二
        }
        this.updateById(updateEntity);
    }
}