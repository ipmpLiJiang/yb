package cc.mrbird.febs.com.service.impl;

import cc.mrbird.febs.cn.webxml.sms.SmsService;
import cc.mrbird.febs.cn.webxml.sms.SmsServicePortType;
import cc.mrbird.febs.common.domain.QueryRequest;
import cc.mrbird.febs.common.properties.FebsProperties;
import cc.mrbird.febs.common.utils.SortUtil;
import cc.mrbird.febs.com.entity.ComSms;
import cc.mrbird.febs.com.dao.ComSmsMapper;
import cc.mrbird.febs.com.service.IComSmsService;
import cc.mrbird.febs.yb.entity.YbPerson;
import cc.mrbird.febs.yb.entity.YbReconsiderApply;
import cc.mrbird.febs.yb.entity.YbReconsiderApplyTask;
import cc.mrbird.febs.yb.service.IYbPersonService;
import cc.mrbird.febs.yb.service.IYbReconsiderApplyService;
import cn.hutool.core.lang.Validator;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
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
 * @since 2020-11-13
 */
@Slf4j
@Service("IComSmsService")
@Transactional(propagation = Propagation.SUPPORTS, readOnly = true, rollbackFor = Exception.class)
public class ComSmsServiceImpl extends ServiceImpl<ComSmsMapper, ComSms> implements IComSmsService {

    @Autowired
    FebsProperties febsProperties;

    @Autowired
    IYbPersonService iYbPersonService;

    @Autowired
    IYbReconsiderApplyService iYbReconsiderApplyService;

    @Override
    public IPage<ComSms> findComSmss(QueryRequest request, ComSms comSms) {
        try {
            LambdaQueryWrapper<ComSms> queryWrapper = new LambdaQueryWrapper<>();
            String sql = "1=1";
            if (comSms.getApplyDateStr() != null) {
//                queryWrapper.eq(ComSms::getApplyDateStr,comSms.getApplyDateStr());
                sql += " and applyDateStr = '" + comSms.getApplyDateStr() + "'";
            }
            if (comSms.getRefTableId() != null) {
                sql += " and refTableId = '" + comSms.getRefTableId() + "'";
            }
            if (comSms.getAreaType() != null) {
//                queryWrapper.eq(ComSms::getAreaType, comSms.getAreaType());
                sql += " and areaType = " + comSms.getAreaType();
            }
            if (comSms.getTypeno() != null) {
//                queryWrapper.eq(ComSms::getTypeno, comSms.getTypeno());
                sql += " and typeno = " + comSms.getTypeno();
            }
            if (comSms.getSendType() != null) {
//                queryWrapper.eq(ComSms::getSendType, comSms.getSendType());
                sql += " and sendType = " + comSms.getSendType();
            }
            if (comSms.getState() != null) {
//                queryWrapper.eq(ComSms::getState, comSms.getState());
                sql += " and state = " + comSms.getState();
            }
            if (comSms.getComments() != null && comSms.getComments() != "") {
                sql += " and (sendcode = '" + comSms.getComments() + "' or sendname = '" + comSms.getComments() + "' or mobile = '" + comSms.getComments() + "')";
            }
//            queryWrapper.eq(ComSms::getIsDeletemark, 1);//1是未删 0是已删
            queryWrapper.apply(sql);
            Page<ComSms> page = new Page<>();
            SortUtil.handlePageSort(request, page, false);//true 是属性  false是数据库字段可两个
            return this.page(page, queryWrapper);
        } catch (Exception e) {
            log.error("获取字典信息失败", e);
            return null;
        }
    }

    @Override
    public IPage<ComSms> findComSmsList(QueryRequest request, ComSms comSms) {
        try {
            Page<ComSms> page = new Page<>();
            SortUtil.handlePageSort(request, page, false);//true 是属性  false是数据库字段可两个
            return this.baseMapper.findComSms(page, comSms);
        } catch (Exception e) {
            log.error("获取失败", e);
            return null;
        }
    }

    @Override
    public List<ComSms> findLmdSmsList(ComSms comSms) {
        List<ComSms> list = new ArrayList<>();
        LambdaQueryWrapper<ComSms> wrapper = new LambdaQueryWrapper<>();
        if (comSms.getSendcode() != null) {
            wrapper.eq(ComSms::getSendcode, comSms.getSendcode());
        }
        if (comSms.getSendname() != null) {
            wrapper.eq(ComSms::getSendname, comSms.getSendname());
        }
        if (comSms.getMobile() != null) {
            wrapper.eq(ComSms::getMobile, comSms.getMobile());
        }
        if (comSms.getSendType() != null) {
            wrapper.eq(ComSms::getSendType, comSms.getSendType());
        }
        if (comSms.getState() != null) {
            wrapper.eq(ComSms::getState, comSms.getState());
        }
        if (comSms.getAreaType() != null) {
            wrapper.eq(ComSms::getAreaType, comSms.getAreaType());
        }
        wrapper.eq(ComSms::getIsDeletemark, 1);
        list = this.list(wrapper);
        return list;
    }

    @Override
    public List<ComSms> findSmsTopLists(ComSms comSms) {
        return this.baseMapper.findSmsTopList(comSms);
    }

    @Override
    public List<ComSms> findSmsList(ComSms comSms) {
        LambdaQueryWrapper<ComSms> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(ComSms::getIsDeletemark, 1);
        if (comSms.getSendcode() != null) {
            wrapper.eq(ComSms::getSendcode, comSms.getSendcode());
        }
        if (comSms.getMobile() != null) {
            wrapper.eq(ComSms::getMobile, comSms.getMobile());
        }
        if (comSms.getAreaType() != null) {
            wrapper.eq(ComSms::getAreaType, comSms.getAreaType());
        }
        return this.list(wrapper);
    }

    @Override
    @Transactional
    public void createComSms(ComSms comSms) {
        comSms.setId(UUID.randomUUID().toString());
        comSms.setCreateTime(new Date());
        comSms.setIsDeletemark(1);
        this.save(comSms);
    }

    @Override
    @Transactional
    public void updateComSms(ComSms comSms) {
        comSms.setModifyTime(new Date());
        this.baseMapper.updateComSms(comSms);
    }

    @Override
    @Transactional
    public void deleteComSmss(String[] Ids) {
        List<String> list = Arrays.asList(Ids);
        this.baseMapper.deleteBatchIds(list);

    }

    @Override
    @Transactional
    public void sendSmsService(String applyDateStr, Integer typeno, ArrayList<String> personCodeList, int sendType, int areaType, String sendContent, Long uId, String Uname) {
        if (personCodeList.size() > 0) {
            List<YbPerson> personList = iYbPersonService.findPersonList(personCodeList);
            if (personList.size() > 0) {
                String mobiles = "";
                List<String> mobList = new ArrayList<>();
                List<ComSms> createList = new ArrayList<>();
                for (YbPerson item : personList) {
                    ComSms comSms = new ComSms();
                    comSms.setId(UUID.randomUUID().toString());
                    comSms.setSendcode(item.getPersonCode());
                    comSms.setSendname(item.getPersonName());
                    comSms.setMobile(item.getTel());
                    if (item.getTel() != null) {
                        if (mobList.stream().filter(s -> s.equals(item.getTel())).count() == 0) {
                            if (Validator.isMobile(item.getTel())) {
                                if (mobiles.equals("")) {
                                    mobiles = item.getTel();
                                } else {
                                    mobiles += "," + item.getTel();
                                }
                                mobList.add(item.getTel());
                            }
                        }
                    }
                    comSms.setSendType(sendType);
                    comSms.setState(ComSms.STATE_1);
                    comSms.setSendcontent(sendContent);
                    comSms.setAreaType(areaType);
                    comSms.setOperatorId(uId);
                    comSms.setOperatorName(Uname);
                    comSms.setIsDeletemark(1);
                    comSms.setCreateUserId(uId);
                    comSms.setCreateTime(new Date());
                    comSms.setTypeno(typeno);
                    comSms.setApplyDateStr(applyDateStr);
                    if (item.getTel() != null) {
                        createList.add(comSms);
                    }
                }

                if (createList.size() > 0) {
                    String msg = sendMsg(mobiles, sendContent);
                    if (msg.equals("0")) {
                        this.saveBatch(createList);
                    } else {
                        //0 0/3 21-23 1/1 * ? //0 0/3 * * * ?
                        log.error("发短信Error Service", msg);
                    }
                }
            }
        }
    }

    @Override
    @Transactional
    public void sendSmsService(String applyDateStr, Integer typeno, String personCode, int sendType, int areaType, String sendContent, Long uId, String Uname) {
        if (!personCode.equals("")) {
            ArrayList<String> personCodeList = new ArrayList<>();
            personCodeList.add(personCode);
            List<YbPerson> personList = iYbPersonService.findPersonList(personCodeList);
            if (personList.size() > 0) {
                boolean isAdd = false;
                YbPerson item = personList.get(0);
                if (item.getTel() != null) {
                    if (Validator.isMobile(item.getTel())) {
                        isAdd = true;
                    }
                }
                if (isAdd) {
                    ComSms querySms = new ComSms();
                    querySms.setState(1);
                    querySms.setApplyDateStr(applyDateStr);
                    querySms.setTypeno(typeno);
                    querySms.setSendType(sendType);
                    querySms.setMobile(item.getTel());
                    querySms.setSendcode(item.getPersonCode());
                    querySms.setAreaType(areaType);
                    querySms = this.baseMapper.findSmsOne(querySms);

                    if (querySms != null) {
                        try {
                            Date d1 = new Date();
                            Date d2 = querySms.getCreateTime();
                            long diff = d1.getTime() - d2.getTime();
                            double diffHours = (double) diff / (60 * 60 * 1000);
                            if (diffHours > 2) {
                                isAdd = true;
                            } else {
                                isAdd = false;
                            }
                        } catch (Exception e) {
                            isAdd = false;
                        }
                    }

                    if (isAdd) {
                        ComSms comSms = new ComSms();

                        comSms.setMobile(item.getTel());
                        comSms.setId(UUID.randomUUID().toString());
                        comSms.setSendcode(item.getPersonCode());
                        comSms.setSendname(item.getPersonName());

                        comSms.setSendType(sendType);
                        comSms.setState(ComSms.STATE_1);
                        comSms.setSendcontent(sendContent);
                        comSms.setAreaType(areaType);
                        comSms.setOperatorId(uId);
                        comSms.setOperatorName(Uname);
                        comSms.setIsDeletemark(1);
                        comSms.setCreateUserId(uId);
                        comSms.setCreateTime(new Date());
                        comSms.setTypeno(typeno);
                        comSms.setApplyDateStr(applyDateStr);
                        String msg = sendMsg(item.getTel(), sendContent);
                        if (msg.equals("0")) {
                            this.save(comSms);
                        } else {
                            //0 0/3 21-23 1/1 * ? //0 0/3 * * * ?
                            log.error("发短信Error Service", msg);
                        }
                    }
                }
            }
        }
    }

    private String sendMsg(String mobiles, String sendContent) {
        String sms = "";
        try {
            SmsService smsService = new SmsService();
            SmsServicePortType ssp = smsService.getSmsServiceHttpPort();

            String in0 = febsProperties.getIn0();
            String in1 = febsProperties.getIn1();
            String in2 = febsProperties.getIn2();
            String in3 = febsProperties.getIn3();
            String in4 = mobiles;
            String in5 = sendContent;
            sms = ssp.service(in0, in1, in2, in3, in4, in5);

        } catch (Exception e) {
            sms = e.getMessage();
        }

        return sms;
    }

    @Override
    @Transactional
    public void sendSms(int sendType, int areaType) {
        String msg = "";
        ComSms comSms = new ComSms();
        comSms.setState(ComSms.STATE_0);
        comSms.setSendType(sendType);
        comSms.setAreaType(areaType);
        List<ComSms> list = this.baseMapper.findSmsTopList(comSms);
        int nOpenSms = febsProperties.getOpenSms();
        boolean isOpenSms = nOpenSms == 1 ? true : false;
        if (list.size() > 0 && isOpenSms) {
            List<ComSms> sendList = new ArrayList<>();
            ComSms t1 = list.get(0);
            sendList = list.stream().filter(s -> s.getSendType() == t1.getSendType() && s.getSendcontent().equals(t1.getSendcontent())).collect(Collectors.toList());
            String mobiles = "";
            List<String> mobList = new ArrayList<>();
            if (sendList.size() > 0) {
                List<ComSms> updateList = new ArrayList<>();
                for (ComSms item : sendList) {
                    ComSms updateSms = new ComSms();
                    updateSms.setId(item.getId());
                    updateSms.setState(ComSms.STATE_1);
                    updateSms.setModifyTime(new Date());
                    updateList.add(updateSms);

                    if (mobList.stream().filter(s -> s.equals(item.getMobile())).count() == 0) {
                        if (Validator.isMobile(item.getMobile())) {
                            if (mobiles.equals("")) {
                                mobiles = item.getMobile();
                            } else {
                                mobiles += "," + item.getMobile();
                            }
                            mobList.add(item.getMobile());
                        }
                    }
                }
                msg = sendMsg(mobiles, t1.getSendcontent());

                if (msg.equals("0")) {
                    this.updateBatchById(updateList);
                } else {
                    //0 0/3 21-23 1/1 * ? //0 0/3 * * * ?
                    log.error("发短信Error", msg);
                }
            }
        }

    }

    @Override
    @Transactional
    public String sendSms(ComSms comSms, List<ComSms> list) {
        String msg = "";
        if (list == null) {
            list = this.baseMapper.findSmsTopList(comSms);
        }
        int nOpenSms = febsProperties.getOpenSms();
        boolean isOpenSms = nOpenSms == 1 ? true : false;
        if (list.size() > 0 && isOpenSms) {
            List<ComSms> sendList = new ArrayList<>();
            ComSms t1 = list.get(0);
            sendList = list.stream().filter(s -> s.getSendType() == t1.getSendType() && s.getSendcontent().equals(t1.getSendcontent())).collect(Collectors.toList());
            String mobiles = "";
            List<String> mobList = new ArrayList<>();
            if (sendList.size() > 0) {
                List<ComSms> updateList = new ArrayList<>();
                for (ComSms item : sendList) {
                    ComSms updateSms = new ComSms();
                    updateSms.setId(item.getId());
                    updateSms.setState(ComSms.STATE_1);
                    updateSms.setModifyTime(new Date());
                    updateList.add(updateSms);

                    if (mobList.stream().filter(s -> s.equals(item.getMobile())).count() == 0) {
                        if (Validator.isMobile(item.getMobile())) {
                            if (mobiles.equals("")) {
                                mobiles = item.getMobile();
                            } else {
                                mobiles += "," + item.getMobile();
                            }
                            mobList.add(item.getMobile());
                        }
                    }
                }
                msg = sendMsg(mobiles, t1.getSendcontent());

                if (msg.equals("0")) {
                    this.updateBatchById(updateList);
                    msg = "ok";
                } else {
                    //0 0/3 21-23 1/1 * ? //0 0/3 * * * ?
                    log.error("发短信Error", msg);
                }
            }
        } else {
            msg = "未找到数据或发送短信未开启.";
        }
        return msg;
    }

    private Lock lockSendWarn = new ReentrantLock();

    @Override
    @Transactional
    public String sendAppealManageWarnSms(String applyDateStr, Integer areaType) {
        String msg = "ok";
        if (lockSendWarn.tryLock()) {
            try {
                YbReconsiderApply reconsiderApply = this.iYbReconsiderApplyService.findReconsiderApplyByApplyDateStrs(applyDateStr, areaType);

                if (reconsiderApply != null) {
                    Date endDate = null;
                    int typeno = 1;
                    if (reconsiderApply.getState() == 2 || reconsiderApply.getState() == 3) {
                        typeno = 1;
                        endDate = reconsiderApply.getEndDateOne();
                    } else if (reconsiderApply.getState() == 4 || reconsiderApply.getState() == 5) {
                        typeno = 2;
                        endDate = reconsiderApply.getEndDateTwo();
                    } else {
                        return "该" + applyDateStr + "年月已完成复议";
                    }
                    SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMdd");
                    String thisDateStr = dateFormat.format(new Date());
                    String endDateStr = dateFormat.format(endDate);
                    if(thisDateStr.equals(endDateStr)){
                        int sendType = 8;
                        ComSms querySms = new ComSms();
                        querySms.setApplyDateStr(applyDateStr);
                        querySms.setAreaType(areaType);
                        querySms.setSendType(sendType);
                        querySms.setTypeno(typeno);
                        querySms.setState(ComSms.STATE_0);
                        List<ComSms> querySmsList = this.findSmsTopLists(querySms);
                        if (querySmsList.size() == 0) {
                            querySms.setState(ComSms.STATE_1);
                            querySmsList = this.findSmsTopLists(querySms);
                            if (querySmsList.size() == 0) {
                                String sendContent = iYbReconsiderApplyService.getSendMessage(applyDateStr, endDate, typeno, areaType);
                                List<YbPerson> list = iYbPersonService.findPersonWarnLists(applyDateStr, areaType, 1, typeno, 0);
                                List<ComSms> smsList = new ArrayList<>();
                                if (list.size() > 0) {
                                    long uid = 1;
                                    for (YbPerson item : list) {
                                        ComSms comSms = new ComSms();
                                        comSms.setId(UUID.randomUUID().toString());
                                        comSms.setSendcode(item.getPersonCode());
                                        comSms.setSendname(item.getPersonName());
                                        comSms.setMobile(item.getTel());

                                        comSms.setSendType(sendType);
                                        comSms.setState(ComSms.STATE_0);
                                        comSms.setSendcontent(sendContent);
                                        comSms.setAreaType(areaType);
                                        comSms.setOperatorId(uid);
                                        comSms.setOperatorName("mrbird");
                                        comSms.setIsDeletemark(1);
                                        comSms.setCreateUserId(uid);
                                        comSms.setCreateTime(new Date());
                                        comSms.setTypeno(typeno);
                                        comSms.setApplyDateStr(applyDateStr);
                                        smsList.add(comSms);
                                    }
                                    this.saveBatch(smsList);
                                } else {
                                    msg = "未找到" + applyDateStr + "年月申诉数据.";
                                }
                            } else {
                                msg = "该" + applyDateStr + "年月已经创建过截止提醒.";
                            }
                        } else {
                            msg = this.sendSms(null, querySmsList);
                        }
                    } else {
                        msg = applyDateStr + "年月,还未到执行日期.";
                    }
                } else {
                    msg = "未找到" + applyDateStr + "年月数据.";
                }
            } catch (Exception e) {
                msg = e.getMessage();
                log.error(msg);
            } finally {
                lockSendWarn.unlock();
            }
        } else {
            msg = "未获取到数据.";
        }
        System.out.println("sendAppealManageWarnSms:" + msg);
        return msg;
    }
}