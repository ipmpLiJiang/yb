package cc.mrbird.febs.com.service.impl;

import cc.mrbird.febs.cn.webxml.sms.SmsService;
import cc.mrbird.febs.cn.webxml.sms.SmsServicePortType;
import cc.mrbird.febs.common.domain.QueryRequest;
import cc.mrbird.febs.common.properties.FebsProperties;
import cc.mrbird.febs.common.utils.SortUtil;
import cc.mrbird.febs.com.entity.ComSms;
import cc.mrbird.febs.com.dao.ComSmsMapper;
import cc.mrbird.febs.com.service.IComSmsService;
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
        boolean isOpenSms = febsProperties.isOpenSms();
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

                    if(mobList.stream().filter(s -> s.equals(item.getMobile())).count() == 0) {
                        if (mobiles.equals("")) {
                            mobiles = item.getMobile();
                        } else {
                            mobiles += "," + item.getMobile();
                        }
                    }
                }
                msg = sendMsg(mobiles, t1.getSendcontent());
                if(msg.equals("0")){
                    this.updateBatchById(updateList);
                }else{
                    //0 0/3 21-23 1/1 * ?
                    log.error("发短信Error",msg);
                }
            }
        }
    }

}