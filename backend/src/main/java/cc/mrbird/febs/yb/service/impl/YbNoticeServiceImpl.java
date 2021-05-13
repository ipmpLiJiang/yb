package cc.mrbird.febs.yb.service.impl;

import cc.mrbird.febs.com.entity.ComSms;
import cc.mrbird.febs.com.service.IComSmsService;
import cc.mrbird.febs.common.domain.QueryRequest;
import cc.mrbird.febs.common.properties.FebsProperties;
import cc.mrbird.febs.common.utils.SortUtil;
import cc.mrbird.febs.system.domain.User;
import cc.mrbird.febs.yb.entity.*;
import cc.mrbird.febs.yb.dao.YbNoticeMapper;
import cc.mrbird.febs.yb.service.IYbAppealConfireService;
import cc.mrbird.febs.yb.service.IYbNoticeDataService;
import cc.mrbird.febs.yb.service.IYbNoticeService;
import cc.mrbird.febs.yb.service.IYbPersonService;
import cn.hutool.core.lang.Validator;
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

import java.util.*;
import java.time.LocalDate;
import java.util.stream.Collectors;

/**
 * <p>
 * 服务实现类
 * </p>
 *
 * @author viki
 * @since 2021-03-08
 */
@Slf4j
@Service("IYbNoticeService")
@Transactional(propagation = Propagation.SUPPORTS, readOnly = true, rollbackFor = Exception.class)
public class YbNoticeServiceImpl extends ServiceImpl<YbNoticeMapper, YbNotice> implements IYbNoticeService {

    @Autowired
    IYbNoticeDataService iYbNoticeDataService;

    @Autowired
    IYbPersonService iYbPersonService;

    @Autowired
    IComSmsService iComSmsService;

    @Autowired
    IYbAppealConfireService iYbAppealConfireService;

    @Autowired
    FebsProperties febsProperties;

    @Override
    public IPage<YbNotice> findYbNotices(QueryRequest request, YbNotice ybNotice) {
        try {
            LambdaQueryWrapper<YbNotice> queryWrapper = new LambdaQueryWrapper<>();

            String sql = "IS_DELETEMARK = 1";

            if(ybNotice.getAreaType() != null){
                sql+=" and areaType = " + ybNotice.getAreaType();
            }

            if (StringUtils.isNotBlank(ybNotice.getCurrencyField())) {
                sql += " and (ntTitle LIKE '%" + ybNotice.getCurrencyField() + "%' or ntExplain LIKE '%" + ybNotice.getCurrencyField() + "%' or ntDetail LIKE '%" + ybNotice.getCurrencyField() + "%')";
            }
            queryWrapper.apply(sql);
            Page<YbNotice> page = new Page<>();
            SortUtil.handlePageSort(request, page, false);//true 是属性  false是数据库字段可两个
            return this.page(page, queryWrapper);
        } catch (Exception e) {
            log.error("获取字典信息失败", e);
            return null;
        }
    }

    @Override
    public IPage<YbNotice> findNoticeView(QueryRequest request, YbNotice ybNotice, String usercode) {
        try {
            LambdaQueryWrapper<YbNotice> queryWrapper = new LambdaQueryWrapper<>();

            String sql = "IS_DELETEMARK = 1 and STATE =  " + YbNotice.STATE_2;
            if(ybNotice.getAreaType() != null){
                sql+=" and areaType = " + ybNotice.getAreaType();
            }
            sql += " and (sendType = " + YbNotice.SENDTYPE_1 + " or (sendType = " + YbNotice.SENDTYPE_2 + " and id in(SELECT yb_notice_data.pid FROM yb_notice_data inner join yb_appeal_confire on yb_notice_data.cmId = yb_appeal_confire.adminType and yb_appeal_confire.doctorCode = '" + usercode + "' WHERE yb_notice_data.ndType = " + YbNoticeData.NDTYPE_1 + ")) or (sendType = " + YbNotice.SENDTYPE_3 + " and id in(select pid from yb_notice_data where ndType = " + YbNoticeData.NDTYPE_2 + " and personCode = '" + usercode + "')))";
            if (StringUtils.isNotBlank(ybNotice.getCurrencyField())) {
                sql += " and (ntTitle LIKE '%" + ybNotice.getCurrencyField() + "%' or ntExplain LIKE '%" + ybNotice.getCurrencyField() + "%' or ntDetail LIKE '%" + ybNotice.getCurrencyField() + "%')";
            }
            queryWrapper.apply(sql);
            Page<YbNotice> page = new Page<>();
            SortUtil.handlePageSort(request, page, false);//true 是属性  false是数据库字段可两个
            return this.page(page, queryWrapper);
        } catch (Exception e) {
            log.error("获取字典信息失败", e);
            return null;
        }
    }

    @Override
    public IPage<YbNotice> findYbNoticeList(QueryRequest request, YbNotice ybNotice) {
        try {
            Page<YbNotice> page = new Page<>();
            SortUtil.handlePageSort(request, page, false);//true 是属性  false是数据库字段可两个
            return this.baseMapper.findYbNotice(page, ybNotice);
        } catch (Exception e) {
            log.error("获取失败", e);
            return null;
        }
    }

    @Override
    @Transactional
    public void createYbNotice(YbNotice ybNotice) {
        ybNotice.setCreateTime(new Date());
        ybNotice.setIsDeletemark(1);
        this.save(ybNotice);
    }

    @Override
    @Transactional
    public void updateYbNotice(YbNotice ybNotice) {
        ybNotice.setModifyTime(new Date());
        this.baseMapper.updateYbNotice(ybNotice);
    }

    @Override
    @Transactional
    public void deleteYbNotices(String[] Ids) {
        List<String> list = Arrays.asList(Ids);
        this.baseMapper.deleteBatchIds(list);
    }

    @Override
    @Transactional
    public void deleteUpdateNotices(String[] Ids) {
        List<String> list = Arrays.asList(Ids);
        LambdaQueryWrapper<YbNotice> wrapper = new LambdaQueryWrapper<>();
        wrapper.in(YbNotice::getId, list);
        List<YbNotice> queryList = this.baseMapper.selectList(wrapper);
        List<YbNotice> noticeList = new ArrayList<>();
        for (YbNotice item : queryList) {
            if (item.getState() == YbNotice.STATE_0) {
                YbNotice notice = new YbNotice();
                notice.setId(item.getId());
                notice.setIsDeletemark(0);
                noticeList.add(notice);
            }
        }
        if (noticeList.size() > 0) {
            this.updateBatchById(noticeList);
        }
    }

    @Override
    @Transactional
    public void createNotice(YbNotice ybNotice, List<YbNoticeData> createDataList) {
        this.save(ybNotice);
        iYbNoticeDataService.saveBatch(createDataList);
    }

    private List<YbNoticeData> createData(YbNotice ybNotice, List<YbNoticeData> dataList) {
        List<YbNoticeData> createDataList = new ArrayList<>();
        for (YbNoticeData item : dataList) {
            YbNoticeData create = new YbNoticeData();
            create.setId(UUID.randomUUID().toString());
            create.setPid(ybNotice.getId());
            create.setPersonCode(item.getPersonCode());
            create.setPersonName(item.getPersonName());
            create.setCmId(item.getCmId());
            create.setCmName(item.getCmName());
            create.setNdType(item.getNdType());
            createDataList.add(create);
        }
        return createDataList;
    }

    @Override
    @Transactional
    public int updateNoticeClickNum(YbNotice ybNotice) {
        YbNotice query = new YbNotice();
        query.setId(ybNotice.getId());
        int clickNum = 0;
        YbNotice notice = this.findNotice(query);
        if (notice != null) {
            clickNum = notice.getClickNum();
            if (notice.getState() == YbNotice.STATE_2) {
                YbNotice update = new YbNotice();
                update.setId(notice.getId());
                update.setClickNum(clickNum + 1);
                boolean isTrue = this.updateById(update);
                if (isTrue) {
                    return clickNum + 1;
                }
            }
        }
        return clickNum;
    }

    @Override
    public YbNotice findNotice(YbNotice ybNotice) {
        LambdaQueryWrapper<YbNotice> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(YbNotice::getIsDeletemark, 1);//1是未删 0是已删
        if (ybNotice.getId() != null) {
            queryWrapper.eq(YbNotice::getId, ybNotice.getId());
        }

        if (ybNotice.getAreaType() != null) {
            queryWrapper.eq(YbNotice::getAreaType, ybNotice.getAreaType());
        }

        if (ybNotice.getNtTitle() != null) {
            queryWrapper.eq(YbNotice::getNtTitle, ybNotice.getNtTitle());
        }
        if (ybNotice.getNtExplain() != null) {
            queryWrapper.eq(YbNotice::getNtExplain, ybNotice.getNtExplain());
        }
        if (ybNotice.getNtDetail() != null) {
            queryWrapper.eq(YbNotice::getNtDetail, ybNotice.getNtDetail());
        }
        if (ybNotice.getSmsState() != null) {
            queryWrapper.eq(YbNotice::getSmsState, ybNotice.getSmsState());
        }
        if (ybNotice.getSendType() != null) {
            queryWrapper.eq(YbNotice::getSendType, ybNotice.getSendType());
        }
        if (ybNotice.getState() != null) {
            queryWrapper.eq(YbNotice::getState, ybNotice.getState());
        }
        List<YbNotice> list = this.list(queryWrapper);
        if (list.size() > 0) {
            return list.get(0);
        } else {
            return null;
        }
    }

    @Override
    @Transactional
    public void updateNotice(YbNotice ybNotice, List<YbNoticeData> dataList) {
        YbNoticeData query = new YbNoticeData();
        query.setPid(ybNotice.getId());
        List<YbNoticeData> list = iYbNoticeDataService.findNoticeDataList(query);
        List<String> delDataList = new ArrayList<>();
        List<YbNoticeData> createDataList = new ArrayList<>();
        if (dataList.size() == 0) {
            for (YbNoticeData item : list) {
                delDataList.add(item.getId());
            }
        } else {
            if (list.size() == 0) {
                createDataList = this.createData(ybNotice, dataList);
            } else {
                if (list.get(0).getNdType() != dataList.get(0).getNdType()) {
                    for (YbNoticeData item : list) {
                        delDataList.add(item.getId());
                    }
                    createDataList = this.createData(ybNotice, dataList);
                } else {
                    long count = 0;
                    if (list.get(0).getNdType() == YbNoticeData.NDTYPE_1) {
                        List<Integer> existCmList = new ArrayList<>();
                        for (YbNoticeData item : list) {
                            count = dataList.stream().filter(s -> s.getCmId().equals(item.getCmId())).count();
                            if (count == 0) {
                                delDataList.add(item.getId());
                            } else {
                                existCmList.add(item.getCmId());
                            }
                        }
                        for (YbNoticeData item : dataList) {
                            if (!existCmList.contains(item.getCmId())) {
                                YbNoticeData create = new YbNoticeData();
                                create.setId(UUID.randomUUID().toString());
                                create.setPid(ybNotice.getId());
                                create.setCmId(item.getCmId());
                                create.setCmName(item.getCmName());
                                create.setNdType(item.getNdType());
                                createDataList.add(create);
                            }
                        }
                    } else {
                        List<String> existPersonList = new ArrayList<>();
                        for (YbNoticeData item : list) {
                            count = dataList.stream().filter(s -> s.getPersonCode().equals(item.getPersonCode())).count();
                            if (count == 0) {
                                delDataList.add(item.getId());
                            } else {
                                existPersonList.add(item.getPersonCode());
                            }
                        }
                        for (YbNoticeData item : dataList) {
                            if (!existPersonList.contains(item.getPersonCode())) {
                                YbNoticeData create = new YbNoticeData();
                                create.setId(UUID.randomUUID().toString());
                                create.setPid(ybNotice.getId());
                                create.setPersonCode(item.getPersonCode());
                                create.setPersonName(item.getPersonName());
                                create.setNdType(item.getNdType());
                                createDataList.add(create);
                            }
                        }
                    }
                }
            }
        }

        this.updateById(ybNotice);
        if (createDataList.size() > 0) {
            iYbNoticeDataService.saveBatch(createDataList);
        }
        if (delDataList.size() > 0) {
            String[] strArray = new String[delDataList.size()];
            for (int i = 0; i < delDataList.size(); i++) {
                strArray[i] = delDataList.get(i);
            }
            iYbNoticeDataService.deleteYbNoticeDatas(strArray);
        }
    }

    @Override
    @Transactional
    public void updateReleaseNotice(YbNotice ybNotice, User currentUser) {
        List<YbPerson> personList = iYbPersonService.findPersonList(new YbPerson(), 0);

        List<ComSms> createSms = new ArrayList<>();
        int nOpenSms = febsProperties.getOpenSms();
        boolean isOpenSms = nOpenSms == 1 ? true : false;
        String sendContent = ybNotice.getNtTitle();
        if (ybNotice.getSendType() == YbNotice.SENDTYPE_1) {
            for(YbPerson person : personList) {
                this.addSms(ybNotice,personList, createSms, person.getPersonCode(), sendContent, currentUser);
            }
        } else if (ybNotice.getSendType() == YbNotice.SENDTYPE_2) {
            if (isOpenSms) {
                YbNoticeData query = new YbNoticeData();
                query.setPid(ybNotice.getId());
                query.setNdType(YbNoticeData.NDTYPE_1);
                List<YbNoticeData> noticeDatalist = iYbNoticeDataService.findNoticeDataList(query);
                List<Integer> atIds = new ArrayList<>();
                for (YbNoticeData item : noticeDatalist) {
                    atIds.add(item.getCmId());
                }
                List<YbAppealConfire> acList = iYbAppealConfireService.findAppealConfireATList(atIds,ybNotice.getAreaType());
                for (YbAppealConfire obj : acList) {
                    this.addSms(ybNotice,personList, createSms, obj.getDoctorCode(), sendContent, currentUser);
                }
            }
        } else {
            if (isOpenSms) {
                YbNoticeData query = new YbNoticeData();
                query.setPid(ybNotice.getId());
                query.setNdType(YbNoticeData.NDTYPE_2);
                List<YbNoticeData> noticeDatalist = iYbNoticeDataService.findNoticeDataList(query);
                for (YbNoticeData obj : noticeDatalist) {
                    this.addSms(ybNotice,personList, createSms, obj.getPersonCode(), sendContent, currentUser);
                }
            }
        }
        if (createSms.size() > 0) {
            iComSmsService.saveBatch(createSms);
        }
        ybNotice.setReleaseDate(new Date());
        this.updateById(ybNotice);

    }

    private void addSms(YbNotice ybNotice,List<YbPerson> personList, List<ComSms> createSms, String personCode, String sendContent, User currentUser) {
        Date thisDate = new Date();
        List<YbPerson> queryPersonList = new ArrayList<>();
        queryPersonList = personList.stream().filter(s -> s.getPersonCode().equals(personCode)).collect(Collectors.toList());
        long count = 0;
        if (queryPersonList.size() > 0) {
            count = createSms.stream().filter(s -> s.getSendcode().equals(personCode)).count();
            if (count == 0) {
                if (queryPersonList.get(0).getTel() != null && queryPersonList.get(0).getTel() != "") {
                    if (Validator.isMobile(queryPersonList.get(0).getTel())) {
                        ComSms comSms = new ComSms();
                        comSms.setId(UUID.randomUUID().toString());
                        comSms.setSendcode(queryPersonList.get(0).getPersonCode());
                        comSms.setSendname(queryPersonList.get(0).getPersonName());
                        comSms.setMobile(queryPersonList.get(0).getTel());
                        comSms.setSendType(ComSms.SENDTYPE_7);
                        comSms.setState(ComSms.STATE_0);
                        comSms.setAreaType(ybNotice.getAreaType());
                        comSms.setSendcontent(sendContent);
                        comSms.setOperatorId(currentUser.getUserId());
                        comSms.setOperatorName(currentUser.getUsername());
                        comSms.setIsDeletemark(1);
                        comSms.setCreateUserId(currentUser.getUserId());
                        comSms.setCreateTime(thisDate);
                        comSms.setRefTableId(ybNotice.getId());
                        createSms.add(comSms);
                    }
                }
            }
        }
    }

}