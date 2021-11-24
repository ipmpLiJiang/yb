package cc.mrbird.febs.yb.service.impl;

import cc.mrbird.febs.com.controller.DataTypeHelpers;
import cc.mrbird.febs.com.entity.ComConfiguremanage;
import cc.mrbird.febs.com.service.IComConfiguremanageService;
import cc.mrbird.febs.common.domain.QueryRequest;
import cc.mrbird.febs.common.properties.FebsProperties;
import cc.mrbird.febs.common.utils.SortUtil;
import cc.mrbird.febs.job.domain.Job;
import cc.mrbird.febs.job.service.JobService;
import cc.mrbird.febs.yb.dao.YbDrgApplyMapper;
import cc.mrbird.febs.yb.entity.YbDefaultValue;
import cc.mrbird.febs.yb.entity.YbDrgApply;
import cc.mrbird.febs.yb.service.IYbDrgApplyService;
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

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
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
@Service("IYbDrgApplyService")
@Transactional(propagation = Propagation.SUPPORTS, readOnly = true, rollbackFor = Exception.class)
public class YbDrgApplyServiceImpl extends ServiceImpl<YbDrgApplyMapper, YbDrgApply> implements IYbDrgApplyService {


    @Autowired
    private JobService jobService;

    @Autowired
    FebsProperties febsProperties;

    @Autowired
    IComConfiguremanageService iComConfiguremanageService;

    @Override
    public IPage<YbDrgApply> findYbDrgApplys(QueryRequest request, YbDrgApply ybDrgApply) {
        try {
            LambdaQueryWrapper<YbDrgApply> queryWrapper = new LambdaQueryWrapper<>();
            queryWrapper.eq(YbDrgApply::getAreaType, ybDrgApply.getAreaType());
            if (StringUtils.isNotBlank(ybDrgApply.getCreateTimeFrom()) && StringUtils.isNotBlank(ybDrgApply.getCreateTimeTo())) {
                queryWrapper
                        .ge(YbDrgApply::getCreateTime, ybDrgApply.getCreateTimeFrom())
                        .le(YbDrgApply::getCreateTime, ybDrgApply.getCreateTimeTo());
            }
            queryWrapper.eq(YbDrgApply::getIsDeletemark, 1);//1是未删 0是已删

            Page<YbDrgApply> page = new Page<>();
            SortUtil.handlePageSort(request, page, false);//true 是属性  false是数据库字段可两个
            return this.page(page, queryWrapper);
        } catch (Exception e) {
            log.error("获取字典信息失败", e);
            return null;
        }
    }

    @Override
    public IPage<YbDrgApply> findYbDrgApplyList(QueryRequest request, YbDrgApply ybDrgApply) {
        try {
            Page<YbDrgApply> page = new Page<>();
            SortUtil.handlePageSort(request, page, false);//true 是属性  false是数据库字段可两个
            return this.baseMapper.findYbDrgApply(page, ybDrgApply);
        } catch (Exception e) {
            log.error("获取失败", e);
            return null;
        }
    }

    @Override
    @Transactional
    public void createYbDrgApply(YbDrgApply ybDrgApply) {
        ybDrgApply.setCreateTime(new Date());
        if (ybDrgApply.getId() == null || "".equals(ybDrgApply.getId())) {
            ybDrgApply.setId(UUID.randomUUID().toString());
        }
        ybDrgApply.setIsDeletemark(1);
        this.save(ybDrgApply);
    }

    @Override
    @Transactional
    public String createDrgApplyCheck(YbDrgApply ybDrgApply) {
        String message = "";
        YbDrgApply reconsiderApply = this.findDrgApplyByApplyDateStrs(ybDrgApply.getApplyDateStr(), ybDrgApply.getAreaType());
        if (reconsiderApply == null) {
            ybDrgApply.setCreateTime(new Date());
            if (ybDrgApply.getId() == null || "".equals(ybDrgApply.getId())) {
                ybDrgApply.setId(UUID.randomUUID().toString());
            }
            ybDrgApply.setIsDeletemark(1);
            boolean bl = this.save(ybDrgApply);
            if (bl) {
                message = "ok";
            }
        } else {
            List<Integer> atList = new ArrayList<>();
            atList.add(5);
            List<ComConfiguremanage> ccsList = iComConfiguremanageService.getConfigLists(atList);
            if (ccsList.size() > 0) {
                ccsList = ccsList.stream().filter(s -> s.getIntField().equals(ybDrgApply.getAreaType())).collect(Collectors.toList());
                message = ccsList.get(0).getStringField() + " 该年月 " + ybDrgApply.getApplyDateStr() + " 已创建过复议申请记录";
            } else {
                message = "该年月 " + ybDrgApply.getApplyDateStr() + " 已创建过复议申请记录";
            }

        }
        return message;
    }


    @Override
    @Transactional
    public void updateYbDrgApply(YbDrgApply ybDrgApply) {
        ybDrgApply.setModifyTime(new Date());
        this.baseMapper.updateYbDrgApply(ybDrgApply);
    }


    @Override
    @Transactional
    public void deleteYbDrgApplys(String[] Ids) {
        List<String> list = Arrays.asList(Ids);
        this.baseMapper.deleteBatchIds(list);
    }

    @Override
    @Transactional
    public boolean updateYbDrgApplyState(String applyDateStr, Integer areaType, Integer state) {
        boolean bl = false;
        YbDrgApply reconsiderApply = this.findDrgApplyByApplyDateStrs(applyDateStr, areaType);
        if (reconsiderApply != null) {
            YbDrgApply update = new YbDrgApply();
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
    public List<YbDrgApply> findDrgApplyList(YbDrgApply ybDrgApply) {
        LambdaQueryWrapper<YbDrgApply> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(YbDrgApply::getIsDeletemark, 1);

        if (ybDrgApply.getId() != null) {
            wrapper.eq(YbDrgApply::getId, ybDrgApply.getId());
        }
        if (ybDrgApply.getApplyDateStr() != null) {
            wrapper.eq(YbDrgApply::getApplyDateStr, ybDrgApply.getApplyDateStr());
        }
        if (ybDrgApply.getAreaType() != null) {
            wrapper.eq(YbDrgApply::getAreaType, ybDrgApply.getAreaType());
        }
        if (ybDrgApply.getState() != null) {
            wrapper.eq(YbDrgApply::getState, ybDrgApply.getState());
        }
        if (ybDrgApply.getTaskState() != null) {
            wrapper.eq(YbDrgApply::getTaskState, ybDrgApply.getTaskState());
        }
        return this.list(wrapper);
    }


    @Override
    public String createJobState(String applyDateStr, Integer areaType) {
        String msg = "";
        /*
        YbDrgApply ybDrgApply = this.findDrgApplyByApplyDateStrs(applyDateStr, areaType);
        boolean isTrue = false;
        //无任务并且状态是上传一
        if (ybDrgApply.getTaskState() == YbDrgApply.TASK_STATE_0 && ybDrgApply.getState() == YbDefaultValue.APPLYSTATE_2) {
            isTrue = true;
        }
        //任务一并且状态是上传二
        if (ybDrgApply.getTaskState() == YbDrgApply.TASK_STATE_1 && ybDrgApply.getState() == YbDefaultValue.APPLYSTATE_4) {
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

            YbDrgApply update = new YbDrgApply();
            update.setId(ybDrgApply.getId());
            update.setTaskState(ybDrgApply.getTaskState() == YbDrgApply.TASK_STATE_0 ? YbDrgApply.TASK_STATE_1 : YbDrgApply.TASK_STATE_2);

            this.updateById(update);
        } else {
            msg = "当前状态无法创建定时任务 或 任务已创建.";
        }*/
        return msg;
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
        YbDrgApply entity = this.findDrgApplyByApplyDateStrs(applyDateStr, areaType);
        Date endDate = entity.getEndDate();
        SimpleDateFormat sdf1 = new SimpleDateFormat("yyyy年MM月dd日 E HH:mm点");//HH时mm分ss秒
        SimpleDateFormat sdf2 = new SimpleDateFormat("yyyy年MM月dd日 E");
        String ssm = "";
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

    @Override
    public YbDrgApply findDrgApplyByApplyDateStrs(String appltDateStr, Integer areaType) {
        return this.baseMapper.findDrgApplyByApplyDateStr(appltDateStr, areaType);
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
//        YbDrgApply entity = this.findDrgApplyByApplyDateStrs(applyDateStr);
        applyDateStr = applyDateStr.replace("-", "年");
        String wangz = febsProperties.getSmsWebsite();
//        String ssm = "武汉市医保" + applyDateStr + "月复议结果已反馈，请登录医保管理系统-复议结果查询界面进行查看。" + wangz;
        String ssm = "医保中心已反馈武汉市医保" + applyDateStr + "复议结果，请登录医保管理系统-复议结果查询界面进行查看，" + wangz + "选择时间，点搜索即可。";

        return ssm + this.areaMsg(areaType);
    }


    public String areaMsg(Integer areaType) {
        String areaName = iComConfiguremanageService.getConfigAreaName(areaType);
        return "院区请选择“" + areaName + "”。（" + areaName + "）";
    }

}