package cc.mrbird.febs.drg.service.impl;

import cc.mrbird.febs.com.controller.DataTypeHelpers;
import cc.mrbird.febs.com.entity.ComSms;
import cc.mrbird.febs.com.service.IComConfiguremanageService;
import cc.mrbird.febs.com.service.IComSmsService;
import cc.mrbird.febs.common.domain.QueryRequest;
import cc.mrbird.febs.common.properties.FebsProperties;
import cc.mrbird.febs.common.utils.SortUtil;
import cc.mrbird.febs.drg.entity.YbDrgApply;
import cc.mrbird.febs.drg.dao.YbDrgApplyMapper;
import cc.mrbird.febs.drg.entity.YbDrgManage;
import cc.mrbird.febs.drg.service.IYbDrgApplyService;
import cc.mrbird.febs.drg.service.IYbDrgManageService;
import cc.mrbird.febs.yb.entity.YbDefaultValue;
import cn.hutool.core.date.DateUnit;
import cn.hutool.core.date.DateUtil;
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

/**
 * <p>
 * 服务实现类
 * </p>
 *
 * @author viki
 * @since 2021-11-23
 */
@Slf4j
@Service("IYbDrgApplyService")
@Transactional(propagation = Propagation.SUPPORTS, readOnly = true, rollbackFor = Exception.class)
public class YbDrgApplyServiceImpl extends ServiceImpl<YbDrgApplyMapper, YbDrgApply> implements IYbDrgApplyService {

    @Autowired
    FebsProperties febsProperties;

    @Autowired
    IComConfiguremanageService iComConfiguremanageService;

    @Autowired
    IYbDrgManageService iYbDrgManageService;

    @Autowired
    IComSmsService iComSmsService;

    @Override
    public IPage<YbDrgApply> findYbDrgApplys(QueryRequest request, YbDrgApply ybDrgApply) {
        try {
            LambdaQueryWrapper<YbDrgApply> queryWrapper = new LambdaQueryWrapper<>();
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
        ybDrgApply.setId(UUID.randomUUID().toString());
        ybDrgApply.setCreateTime(new Date());
        ybDrgApply.setIsDeletemark(1);
        this.save(ybDrgApply);
    }

    @Override
    @Transactional
    public void updateYbDrgApply(YbDrgApply ybDrgApply, Integer isChangDate) {
        ybDrgApply.setModifyTime(new Date());
        long endMinute = 0;
        long enableDay = 0;
        YbDrgApply entity = this.getById(ybDrgApply.getId());
        if (entity != null && entity.getState() == YbDefaultValue.DRGAPPLYSTATE_3) {
            endMinute = DateUtil.between(ybDrgApply.getEndDate(), entity.getEndDate(), DateUnit.MINUTE);
            enableDay = DateUtil.between(ybDrgApply.getEnableDate(), entity.getEnableDate(), DateUnit.DAY);
            if (isChangDate != null && isChangDate == 1) {
                List<ComSms> updateSmsList = new ArrayList<>();
                List<YbDrgManage> updateAmList = new ArrayList<>();
                List<ComSms> smsList = new ArrayList<>();
                List<YbDrgManage> drgManageList = new ArrayList<>();
                if (enableDay != 0 || endMinute != 0) {
                    LambdaQueryWrapper<ComSms> wrapperSms = new LambdaQueryWrapper<>();
                    wrapperSms.eq(ComSms::getApplyDateStr, entity.getApplyDateStr());
                    wrapperSms.eq(ComSms::getAreaType, entity.getAreaType());
                    wrapperSms.eq(ComSms::getSendType, ComSms.SENDTYPE_10);
                    wrapperSms.eq(ComSms::getState, ComSms.STATE_0);
                    smsList = this.iComSmsService.list(wrapperSms);
                }
                if (enableDay != 0) {
                    LambdaQueryWrapper<YbDrgManage> wrapperAm = new LambdaQueryWrapper<>();
                    wrapperAm.eq(YbDrgManage::getApplyDateStr, entity.getApplyDateStr());
                    wrapperAm.eq(YbDrgManage::getAreaType, entity.getAreaType());
                    drgManageList = iYbDrgManageService.list(wrapperAm);
                }
                Date enableDate = ybDrgApply.getEnableDate();
                Date endDate = ybDrgApply.getEndDate();
                enableDate = DataTypeHelpers.addDateMethod(enableDate, 1);

                if (smsList.size() > 0) {
                    String sendContent = this.getChangSendMessage(entity.getApplyDateStr(), endDate, enableDate, entity.getAreaType(), false);
                    for (ComSms item : smsList) {
                        ComSms update = new ComSms();
                        update.setId(item.getId());
                        update.setSendcontent(sendContent);
                        updateSmsList.add(update);
                    }
                }
                if (drgManageList.size() > 0) {
                    for (YbDrgManage item : drgManageList) {
                        YbDrgManage update = new YbDrgManage();
                        update.setId(item.getId());
                        update.setEnableDate(enableDate);
                        updateAmList.add(update);
                    }

                }
                if (updateSmsList.size() > 0) {
                    iComSmsService.updateBatchById(updateSmsList);
                }

                if (updateAmList.size() > 0) {
                    iYbDrgManageService.updateBatchById(updateAmList);
                }
            }
            if (enableDay != 0 || endMinute != 0) {
                this.baseMapper.updateYbDrgApply(ybDrgApply);
            }
        }

    }
    @Override
    @Transactional
    public void updateYbDrgApply(YbDrgApply ybDrgApply){
        ybDrgApply.setModifyTime(new Date());
        this.baseMapper.updateYbDrgApply(ybDrgApply);
    }


    @Override
    @Transactional
    public void deleteYbDrgApplys(String[] Ids, int state) {
        List<String> list = Arrays.asList(Ids);
//        this.baseMapper.deleteBatchIds(list);
        LambdaQueryWrapper<YbDrgApply> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(YbDrgApply::getState, state);
        wrapper.in(YbDrgApply::getId, list);
        this.baseMapper.delete(wrapper);
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
        return this.list(wrapper);
    }

    @Override
    public YbDrgApply findDrgApplyByApplyDateStrs(String appltDateStr, Integer areaType) {
        return this.baseMapper.findDrgApplyByApplyDateStr(appltDateStr, areaType);
    }

    @Override
    @Transactional
    public void updateDrgApplyState3(YbDrgApply drgApply) {
        YbDrgApply updateEntity = new YbDrgApply();
        updateEntity.setId(drgApply.getId());
        if (drgApply.getState() == YbDefaultValue.DRGAPPLYSTATE_2) { //上传
            updateEntity.setState(YbDefaultValue.DRGAPPLYSTATE_3);//核对
        }
        this.updateById(updateEntity);
    }

    @Override
    public String getSendMessage(String applyDateStr, Date enableDate, Integer areaType, boolean isChange) {
        YbDrgApply entity = this.findDrgApplyByApplyDateStrs(applyDateStr, areaType);
        Date endDate = entity.getEndDate();
        return this.getChangSendMessage(applyDateStr, endDate, enableDate, areaType, isChange);
    }

    public String getChangSendMessage(String applyDateStr, Date endDate, Date enableDate, Integer areaType, boolean isChange) {
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
                ssm = "武汉市医保" + applyDateStr + "月 DRG复议数据已发给您，请在复议截止时间前完成 责任人确认工作和复议工作，此次复议截止时间是 " + date1 + "，请及时登录医保管理系统处理。" + wangz;
            } else {
                ssm = "武汉市医保" + applyDateStr + "月 DRG复议数据已发给您，请在 " + date2 + "前完成责任人确认工作，否则默认责任人为本人。此次复议截止时间是 " + date1 + "，请及时登录医保管理系统处理。" + wangz;
            }
        } else {
            ssm = "您有其他医生转发的医保DRG违规项目需复议，此次复议截止时间是" + date1 + "，请及时登录医保管理系统处理。" + wangz;
        }
        return ssm + this.areaMsg(areaType);
    }

    public String areaMsg(Integer areaType) {
        String areaName = iComConfiguremanageService.getConfigAreaName(areaType);
        return "院区请选择“" + areaName + "”。（" + areaName + "）";
    }

    @Override
    public String getSendMessage(String applyDateStr, Date endDate,Integer areaType) {
        applyDateStr = applyDateStr.replace("-", "年");
        String wangz = febsProperties.getSmsWebsite();
        String ssm = "";
        Calendar now = Calendar.getInstance();
        now.setTime(endDate);
        String fen = "" + now.get(Calendar.MINUTE);
        if (fen.length() == 1) {
            fen = "0" + fen;
        }
        String shi = "" + now.get(Calendar.HOUR_OF_DAY);
        ssm = "武汉市医保" + applyDateStr + "月 DRG 复议将于今天" + shi + ":" + fen + "截止，您尚有未处理的扣款，请登陆系统及时查看并处理。" + wangz;
        return ssm + this.areaMsg(areaType);
    }

    @Override
    public boolean findDrgApplyCheckEndDate(String applyDateStr, Integer areaType) {
        YbDrgApply drgApply = this.findDrgApplyByApplyDateStrs(applyDateStr, areaType);
        boolean isUpdate = false;
        Date thisDate = new Date();
        Date compareDate = drgApply.getEndDate();
        if (compareDate.compareTo(thisDate) == 1) {
            isUpdate = true;
        }
        return isUpdate;
    }



}