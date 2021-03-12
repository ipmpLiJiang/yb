package cc.mrbird.febs.yb.service.impl;

import cc.mrbird.febs.common.domain.QueryRequest;
import cc.mrbird.febs.common.utils.SortUtil;
import cc.mrbird.febs.yb.entity.YbAppealConfire;
import cc.mrbird.febs.yb.entity.YbAppealConfireData;
import cc.mrbird.febs.yb.entity.YbNotice;
import cc.mrbird.febs.yb.dao.YbNoticeMapper;
import cc.mrbird.febs.yb.entity.YbNoticeData;
import cc.mrbird.febs.yb.service.IYbNoticeDataService;
import cc.mrbird.febs.yb.service.IYbNoticeService;
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

    @Override
    public IPage<YbNotice> findYbNotices(QueryRequest request, YbNotice ybNotice) {
        try {
            LambdaQueryWrapper<YbNotice> queryWrapper = new LambdaQueryWrapper<>();
            queryWrapper.eq(YbNotice::getIsDeletemark, 1);//1是未删 0是已删

            if (StringUtils.isNotBlank(ybNotice.getCurrencyField())) {
                queryWrapper.like(YbNotice::getCurrencyField, ybNotice.getCurrencyField());
            }

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
        }
        else {
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
                    if (list.get(0).getNdType() == 1) {
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
            for(int i = 0;i<delDataList.size();i++){
                strArray[i] = delDataList.get(i);
            }
            iYbNoticeDataService.deleteYbNoticeDatas(strArray);
        }
    }

}