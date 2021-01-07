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
import cc.mrbird.febs.yb.service.IYbPersonService;
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

import java.util.*;
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

    @Override
    public IPage<ComSms> findComSmss(QueryRequest request, ComSms comSms) {
        try {
            LambdaQueryWrapper<ComSms> queryWrapper = new LambdaQueryWrapper<>();
            queryWrapper.eq(ComSms::getIsDeletemark, 1);//1是未删 0是已删

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
        if (comSms.getState() != null) {
            wrapper.eq(ComSms::getState, comSms.getState());
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
    public void sendSmsService(ArrayList<String> personCodeList, int sendType, String sendContent, Long uId, String Uname) {
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
                    comSms.setOperatorId(uId);
                    comSms.setOperatorName(Uname);
                    comSms.setIsDeletemark(1);
                    comSms.setCreateUserId(uId);
                    comSms.setCreateTime(new Date());
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
    public void sendSmsService(String personCode, int sendType, String sendContent, Long uId, String Uname) {
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
                    querySms.setSendType(sendType);
                    querySms.setMobile(item.getTel());
                    querySms.setSendcode(item.getPersonCode());
                    querySms = this.baseMapper.findSmsOne(querySms);

                    if (querySms != null) {
                        try {
                            Date d1 = new Date();
                            Date d2 = querySms.getCreateTime();
                            long diff = d1.getTime() - d2.getTime();
                            double diffHours = (double)diff / (60 * 60 * 1000);
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
                        comSms.setOperatorId(uId);
                        comSms.setOperatorName(Uname);
                        comSms.setIsDeletemark(1);
                        comSms.setCreateUserId(uId);
                        comSms.setCreateTime(new Date());

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
    public void sendSms() {
        String msg = "";
        ComSms comSms = new ComSms();
        comSms.setState(ComSms.STATE_0);
        List<ComSms> list = this.baseMapper.findSmsTopList(comSms);
        int nOpenSms = febsProperties.getOpenSms();
        boolean isOpenSms = nOpenSms == 1 ? true : false;
        if (list.size() > 0 && isOpenSms) {
            List<ComSms> sendList = new ArrayList<>();
            ComSms t1 = list.get(0);
            sendList = list.stream().filter(s -> s.getSendType() == t1.getSendType()).collect(Collectors.toList());
            String mobiles = "";
            List<String> mobList = new ArrayList<>();
            if (sendList.size() > 0) {
                List<ComSms> updateList = new ArrayList<>();
                for (ComSms item : sendList) {
                    ComSms updateSms = new ComSms();
                    updateSms.setId(item.getId());
                    updateSms.setState(ComSms.STATE_1);
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

}