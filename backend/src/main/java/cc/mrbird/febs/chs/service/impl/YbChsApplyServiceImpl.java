package cc.mrbird.febs.chs.service.impl;

import cc.mrbird.febs.chs.entity.YbChsManage;
import cc.mrbird.febs.chs.service.IYbChsManageService;
import cc.mrbird.febs.com.controller.DataTypeHelpers;
import cc.mrbird.febs.com.entity.ComConfiguremanage;
import cc.mrbird.febs.com.entity.ComSms;
import cc.mrbird.febs.com.service.IComConfiguremanageService;
import cc.mrbird.febs.com.service.IComSmsService;
import cc.mrbird.febs.common.domain.QueryRequest;
import cc.mrbird.febs.common.properties.FebsProperties;
import cc.mrbird.febs.common.utils.SortUtil;
import cc.mrbird.febs.chs.entity.YbChsApply;
import cc.mrbird.febs.chs.dao.YbChsApplyMapper;
import cc.mrbird.febs.chs.service.IYbChsApplyService;
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

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.time.LocalDate;
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
@Service("IYbChsApplyService")
@Transactional(propagation = Propagation.SUPPORTS, readOnly = true, rollbackFor = Exception.class)
public class YbChsApplyServiceImpl extends ServiceImpl<YbChsApplyMapper, YbChsApply> implements IYbChsApplyService {

    @Autowired
    IComConfiguremanageService iComConfiguremanageService;

    @Autowired
    IYbChsManageService iYbChsManageService;

    @Autowired
    IComSmsService iComSmsService;

    @Autowired
    FebsProperties febsProperties;

    @Override
    public IPage<YbChsApply> findYbChsApplys(QueryRequest request, YbChsApply ybChsApply) {
        try {
            LambdaQueryWrapper<YbChsApply> queryWrapper = new LambdaQueryWrapper<>();
            queryWrapper.eq(YbChsApply::getIsDeletemark, 1);//1是未删 0是已删


            Page<YbChsApply> page = new Page<>();
            SortUtil.handlePageSort(request, page, false);//true 是属性  false是数据库字段可两个
            return this.page(page, queryWrapper);
        } catch (Exception e) {
            log.error("获取字典信息失败", e);
            return null;
        }
    }

    @Override
    public IPage<YbChsApply> findYbChsApplyList(QueryRequest request, YbChsApply ybChsApply) {
        try {
            Page<YbChsApply> page = new Page<>();
            SortUtil.handlePageSort(request, page, false);//true 是属性  false是数据库字段可两个
            return this.baseMapper.findYbChsApply(page, ybChsApply);
        } catch (Exception e) {
            log.error("获取失败", e);
            return null;
        }
    }

    @Override
    @Transactional
    public void createYbChsApply(YbChsApply ybChsApply) {
        ybChsApply.setId(UUID.randomUUID().toString());
        ybChsApply.setCreateTime(new Date());
        ybChsApply.setIsDeletemark(1);
        this.save(ybChsApply);
    }

    @Override
    @Transactional
    public String createChsApplyCheck(YbChsApply ybChsApply) {
        String message = "";
        YbChsApply chsApply = this.findChsApplyByApplyDateStrs(ybChsApply.getApplyDateStr(), ybChsApply.getAreaType());
        if (chsApply == null) {
            ybChsApply.setCreateTime(new Date());
            if (ybChsApply.getId() == null || "".equals(ybChsApply.getId())) {
                ybChsApply.setId(UUID.randomUUID().toString());
            }
            ybChsApply.setIsDeletemark(1);
            boolean bl = this.save(ybChsApply);
            if (bl) {
                message = "ok";
            }
        } else {
            List<Integer> atList = new ArrayList<>();
            atList.add(5);
            List<ComConfiguremanage> ccsList = iComConfiguremanageService.getConfigLists(atList);
            if (ccsList.size() > 0) {
                ccsList = ccsList.stream().filter(s -> s.getIntField().equals(ybChsApply.getAreaType())).collect(Collectors.toList());
                message = ccsList.get(0).getStringField() + " 该审核年月 " + ybChsApply.getApplyDateStr() + " 已创建过复议申请记录";
            } else {
                message = "该审核年月 " + ybChsApply.getApplyDateStr() + " 已创建过复议申请记录";
            }

        }
        return message;
    }

    @Override
    public YbChsApply findChsApplyByApplyDateStrs(String appltDateStr, Integer areaType) {
        return this.baseMapper.findChsApplyByApplyDateStr(appltDateStr, areaType);
    }

    @Override
    @Transactional
    public void updateYbChsApply(YbChsApply ybChsApply) {
        ybChsApply.setModifyTime(new Date());
        this.baseMapper.updateYbChsApply(ybChsApply);
    }

    // 申请修改数据，包含数据过期更改
    @Override
    @Transactional
    public String updateYbChsApply(YbChsApply ybChsApply, boolean isUpOverdue) throws ParseException {
        String msg = "ok";
        ybChsApply.setModifyTime(new Date());
        ybChsApply.setApplyDateStr(null);

        if (isUpOverdue) {
            YbChsApply yra = this.getById(ybChsApply.getId());
            int state = yra.getState();
            if (state == YbDefaultValue.APPLYSTATE_3) {
                SimpleDateFormat f = new SimpleDateFormat("yyyy-MM-dd hh:mm");
                Date aEndDate = f.parse(f.format(yra.getEndDate()));
                Date bEndDate = f.parse(f.format(ybChsApply.getEndDate()));
                if (aEndDate.before(bEndDate)) {
                    YbChsManage query = new YbChsManage();
                    query.setApplyDateStr(yra.getApplyDateStr());
                    query.setAreaType(yra.getAreaType());
                    query.setState(YbDefaultValue.ACCEPTSTATE_7);
                    List<YbChsManage> manageList = iYbChsManageService.findChsManageList(query);
                    if (manageList.size() > 0) {
                        List<YbChsManage> updateList = new ArrayList<>();
                        for (YbChsManage item : manageList) {
                            YbChsManage update = new YbChsManage();
                            update.setId(item.getId());
                            update.setState(YbDefaultValue.ACCEPTSTATE_1);
                            updateList.add(update);
                        }
                        boolean isTrue = iYbChsManageService.updateBatchById(updateList);
                        if (isTrue) {
                            msg = "ok";
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
            if (msg.equals("ok")) {
                this.baseMapper.updateYbChsApply(ybChsApply);
            }
        } else {
            this.baseMapper.updateYbChsApply(ybChsApply);
        }

        return msg;
    }

    @Override
    @Transactional
    public void updateYbChsApply(YbChsApply ybChsApply, Integer isChangDate) {
        ybChsApply.setModifyTime(new Date());
        long endMinute = 0;
        long enableDay = 0;
        YbChsApply entity = this.getById(ybChsApply.getId());
        if (entity != null && entity.getState() == YbDefaultValue.APPLYSTATE_3) {
            endMinute = DateUtil.between(ybChsApply.getEndDate(), entity.getEndDate(), DateUnit.MINUTE);
            enableDay = DateUtil.between(ybChsApply.getEnableDate(), entity.getEnableDate(), DateUnit.DAY);
            if (isChangDate != null && isChangDate == 1) {
                List<ComSms> updateSmsList = new ArrayList<>();
                List<YbChsManage> updateAmList = new ArrayList<>();
                List<ComSms> smsList = new ArrayList<>();
                List<YbChsManage> chsManageList = new ArrayList<>();
                if (enableDay != 0 || endMinute != 0) {
                    LambdaQueryWrapper<ComSms> wrapperSms = new LambdaQueryWrapper<>();
                    wrapperSms.eq(ComSms::getApplyDateStr, entity.getApplyDateStr());
                    wrapperSms.eq(ComSms::getAreaType, entity.getAreaType());
                    wrapperSms.eq(ComSms::getSendType, ComSms.SENDTYPE_21);
                    wrapperSms.eq(ComSms::getState, ComSms.STATE_0);
                    smsList = this.iComSmsService.list(wrapperSms);
                }
                if (enableDay != 0) {
                    LambdaQueryWrapper<YbChsManage> wrapperAm = new LambdaQueryWrapper<>();
                    wrapperAm.eq(YbChsManage::getApplyDateStr, entity.getApplyDateStr());
                    wrapperAm.eq(YbChsManage::getAreaType, entity.getAreaType());
                    chsManageList = iYbChsManageService.list(wrapperAm);
                }
                Date enableDate = ybChsApply.getEnableDate();
                Date endDate = ybChsApply.getEndDate();
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
                if (chsManageList.size() > 0) {
                    for (YbChsManage item : chsManageList) {
                        YbChsManage update = new YbChsManage();
                        update.setId(item.getId());
                        update.setEnableDate(enableDate);
                        updateAmList.add(update);
                    }

                }
                if (updateSmsList.size() > 0) {
                    iComSmsService.updateBatchById(updateSmsList);
                }

                if (updateAmList.size() > 0) {
                    iYbChsManageService.updateBatchById(updateAmList);
                }
            }
            if (enableDay != 0 || endMinute != 0) {
                this.baseMapper.updateYbChsApply(ybChsApply);
            }
        }

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
                ssm = "武汉市医保" + applyDateStr + "月 医保复议数据已发给您，请在复议截止时间前完成 责任人确认工作和复议工作，此次复议截止时间是 " + date1 + "，请及时登录医保管理系统处理。" + wangz;
            } else {
                ssm = "武汉市医保" + applyDateStr + "月 医保复议数据已发给您，请在 " + date2 + "前完成责任人确认工作，否则默认责任人为本人。此次复议截止时间是 " + date1 + "，请及时登录医保管理系统处理。" + wangz;
            }
        } else {
            ssm = "您有其他医生转发的医保医保违规项目需复议，此次复议截止时间是" + date1 + "，请及时登录医保管理系统处理。" + wangz;
        }
        return ssm + this.areaMsg(areaType);
    }

    public String areaMsg(Integer areaType) {
        String areaName = iComConfiguremanageService.getConfigAreaName(areaType);
        return "院区请选择“" + areaName + "”。（" + areaName + "）";
    }

    @Override
    public String getSendMessage(String applyDateStr, Date endDate, Integer areaType) {
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
        ssm = "武汉市医保" + applyDateStr + "月 复议将于今天" + shi + ":" + fen + "截止，您尚有未处理的扣款，请登陆系统及时查看并处理。" + wangz;
        return ssm + this.areaMsg(areaType);
    }

    @Override
    @Transactional
    public void deleteYbChsApplys(String[] Ids, int state) {
        List<String> list = Arrays.asList(Ids);
//        this.baseMapper.deleteBatchIds(list);
        LambdaQueryWrapper<YbChsApply> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(YbChsApply::getState, state);
        wrapper.in(YbChsApply::getId, list);
        this.baseMapper.delete(wrapper);
    }


    @Override
    @Transactional
    public void deleteYbChsApplys(String[] Ids) {
        List<String> list = Arrays.asList(Ids);
        this.baseMapper.deleteBatchIds(list);
    }


    @Override
    public List<YbChsApply> findChsApplyList(YbChsApply ybChsApply) {
        LambdaQueryWrapper<YbChsApply> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(YbChsApply::getIsDeletemark, 1);

        if (ybChsApply.getId() != null) {
            wrapper.eq(YbChsApply::getId, ybChsApply.getId());
        }
        if (ybChsApply.getApplyDateStr() != null) {
            wrapper.eq(YbChsApply::getApplyDateStr, ybChsApply.getApplyDateStr());
        }
        if (ybChsApply.getAreaType() != null) {
            wrapper.eq(YbChsApply::getAreaType, ybChsApply.getAreaType());
        }
        if (ybChsApply.getState() != null) {
            wrapper.eq(YbChsApply::getState, ybChsApply.getState());
        }
        return this.list(wrapper);
    }

    @Override
    @Transactional
    public void updateChsApplyState3(YbChsApply drgApply) {
        YbChsApply updateEntity = new YbChsApply();
        updateEntity.setId(drgApply.getId());
        if (drgApply.getState() == YbDefaultValue.DRGAPPLYSTATE_2) { //上传
            updateEntity.setState(YbDefaultValue.DRGAPPLYSTATE_3);//核对
        }
        this.updateById(updateEntity);
    }

    @Override
    public String getSendMessage(String applyDateStr, Date enableDate, Integer areaType, boolean isChange) {
        YbChsApply entity = this.findChsApplyByApplyDateStrs(applyDateStr, areaType);
        Date endDate = entity.getEndDate();
        return this.getChangSendMessage(applyDateStr, endDate, enableDate, areaType, isChange);
    }

}