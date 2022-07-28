package cc.mrbird.febs.drg.service.impl;

import cc.mrbird.febs.com.controller.DataTypeHelpers;
import cc.mrbird.febs.com.entity.ComFile;
import cc.mrbird.febs.com.service.IComFileService;
import cc.mrbird.febs.common.domain.QueryRequest;
import cc.mrbird.febs.common.properties.FebsProperties;
import cc.mrbird.febs.common.utils.SortUtil;
import cc.mrbird.febs.drg.dao.YbDrgResultViewMapper;
import cc.mrbird.febs.drg.entity.YbDrgApply;
import cc.mrbird.febs.drg.entity.YbDrgApplyData;
import cc.mrbird.febs.drg.entity.YbDrgResult;
import cc.mrbird.febs.drg.entity.YbDrgResultView;
import cc.mrbird.febs.drg.service.IYbDrgApplyDataService;
import cc.mrbird.febs.drg.service.IYbDrgApplyService;
import cc.mrbird.febs.drg.service.IYbDrgResultService;
import cc.mrbird.febs.drg.service.IYbDrgResultViewService;
import cc.mrbird.febs.yb.entity.YbDefaultValue;
import cc.mrbird.febs.yb.service.IYbPersonService;
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

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

/**
 * <p>
 * VIEW 服务实现类
 * </p>
 *
 * @author viki
 * @since 2020-07-30
 */
@Slf4j
@Service("IYbDrgResultViewService")
@Transactional(propagation = Propagation.SUPPORTS, readOnly = true, rollbackFor = Exception.class)
public class YbDrgResultViewServiceImpl extends ServiceImpl<YbDrgResultViewMapper, YbDrgResultView> implements IYbDrgResultViewService {

    @Autowired
    IYbDrgApplyService iYbDrgApplyService;

    @Autowired
    IYbDrgResultService iYbDrgResultService;

    @Autowired
    IYbPersonService iYbPersonService;

    @Autowired
    IYbDrgApplyDataService iYbDrgApplyDataService;

    @Autowired
    IComFileService iComFileService;

    @Autowired
    FebsProperties febsProperties;

    @Override
    public IPage<YbDrgResultView> findDrgResultView(QueryRequest request, YbDrgResultView ybDrgResultView, String keyField) {
        try {
            String applyDateStr = ybDrgResultView.getApplyDateStr();
            Integer areaType = ybDrgResultView.getAreaType();
            YbDrgApply drgApply = iYbDrgApplyService.findDrgApplyByApplyDateStrs(applyDateStr, areaType);
            Page<YbDrgResultView> page = new Page<>();
            if (drgApply != null) {
                String value = ybDrgResultView.getCurrencyField();
                Integer state = ybDrgResultView.getState();
                List<String> strList = new ArrayList<>();
                List<YbDrgResultView> list = new ArrayList<>();
                List<YbDrgResult> resultList = new ArrayList<>();
                if (StringUtils.isNotBlank(value) && keyField.equals("doctorName")) {
                    LambdaQueryWrapper<YbDrgResult> queryWrapper = new LambdaQueryWrapper<>();
                    queryWrapper.eq(YbDrgResult::getApplyDateStr, applyDateStr);
                    queryWrapper.eq(YbDrgResult::getAreaType, areaType);
                    queryWrapper.eq(YbDrgResult::getState, state);

                    strList = this.iYbPersonService.findPersonCodeList(value);
                    if (strList.size() > 0) {
                        if (strList.size() == 1) {
                            queryWrapper.eq(YbDrgResult::getDoctorCode, strList.get(0));
                        } else {
                            queryWrapper.in(YbDrgResult::getDoctorCode, strList);
                        }
                    }

                    resultList = iYbDrgResultService.list(queryWrapper);
                } else {
                    YbDrgResult queryResult = new YbDrgResult();
                    queryResult.setApplyDateStr(applyDateStr);
                    queryResult.setAreaType(areaType);
                    queryResult.setState(state);

                    if (StringUtils.isNotBlank(value) && keyField.equals("doctorCode")) {
                        queryResult.setDoctorCode(value);
                    }
                    if (ybDrgResultView.getDoctorCode() != null) {
                        queryResult.setDoctorCode(ybDrgResultView.getDoctorCode());
                    }
                    if (StringUtils.isNotBlank(value) && keyField.equals("orderNumber")) {
                        queryResult.setOrderNumber(value);
                    }
                    resultList = iYbDrgResultService.findDrgResultList(queryResult);
                }
                List<YbDrgApplyData> applyDataList = this.iYbDrgApplyDataService.getApplyDataByKeyFieldList(drgApply.getId(), keyField, value);

                if (resultList.size() > 0 && applyDataList.size() > 0) {
                    if (resultList.size() > applyDataList.size()) {
                        List<YbDrgResult> queryList = new ArrayList<>();
                        for (YbDrgApplyData item : applyDataList) {
                            queryList = resultList.stream().filter(s -> s.getApplyDataId().equals(item.getId())).collect(Collectors.toList());
                            if (queryList.size() > 0) {
                                YbDrgResultView drv = this.getYbDrgResultView(queryList.get(0), item);
                                list.add(drv);
                            }
                        }
                    } else {
                        for (YbDrgResult item : resultList) {
                            List<YbDrgApplyData> queryList = new ArrayList<>();
                            queryList = applyDataList.stream().filter(s -> s.getId().equals(item.getApplyDataId())).collect(Collectors.toList());
                            if (queryList.size() > 0) {
                                YbDrgResultView drv = this.getYbDrgResultView(item, queryList.get(0));
                                list.add(drv);
                            }
                        }
                    }
                }
                if (list.size() > 0) {
                    page.setSearchCount(false);
                    SortUtil.handlePageSort(request, page, false);//true 是属性  false是数据库字段可两个
                    page.setTotal(list.size());
                    long current = page.getCurrent() == 1 ? 0 : (page.getCurrent() - 1) * page.getSize();
                    list = list.stream().sorted(Comparator.comparing(YbDrgResultView::getOrderNum)).skip(current).limit(page.getSize()).collect(Collectors.toList());
                    page.setRecords(list);
                }
            }
            return page;

        } catch (Exception e) {
            log.error("获取字典信息失败", e);
            return null;
        }
    }

    private YbDrgResultView getYbDrgResultView(YbDrgResult dr, YbDrgApplyData drgApplyData) {
        YbDrgResultView drv = new YbDrgResultView();
        drv.setPid(drgApplyData.getPid());
        drv.setKs(drgApplyData.getKs());
        drv.setJzjlh(drgApplyData.getJzjlh());
        drv.setBah(drgApplyData.getBah());
        drv.setWglx(drgApplyData.getWglx());
        drv.setWtms(drgApplyData.getWtms());
        drv.setYlzfy(drgApplyData.getYlzfy());
        drv.setWgje(drgApplyData.getWgje());
        drv.setSfbmzczjcw(drgApplyData.getSfbmzczjcw());
        drv.setLy(drgApplyData.getLy());
        drv.setOrderNumber(drgApplyData.getOrderNumber());
        drv.setOrderNum(drgApplyData.getOrderNum());
        drv.setApplyDateStr(dr.getApplyDateStr());
        drv.setId(dr.getId());
        drv.setApplyDataId(drgApplyData.getId());
        drv.setOperateReason(dr.getOperateReason());
        drv.setOperateDate(dr.getOperateDate());
        drv.setManageId(dr.getManageId());
        drv.setDoctorCode(dr.getDoctorCode());
        drv.setDoctorName(dr.getDoctorName());
        drv.setDksId(dr.getDksId());
        drv.setDksName(dr.getDksName());
        drv.setState(dr.getState());
        drv.setAreaType(dr.getAreaType());
        return drv;
    }


    @Override
    public List<YbDrgResultView> findDrgResultViewLists(YbDrgResultView ybDrgResultView) {
        return this.baseMapper.findDrgResultViewList(ybDrgResultView);
    }

    @Override
    public List<YbDrgResultView> findDrgResultFileSizeByOrderNumberList(String applyDateStr, int areaType, List<String> orderNumberList) throws IOException {
        List<YbDrgResultView> list = new ArrayList<>();
        YbDrgApply drgApply = iYbDrgApplyService.findDrgApplyByApplyDateStrs(applyDateStr, areaType);
        if (drgApply != null) {
            LambdaQueryWrapper<YbDrgResult> wrapperResult = new LambdaQueryWrapper<>();
            wrapperResult.eq(YbDrgResult::getApplyDateStr, drgApply.getApplyDateStr());
            wrapperResult.eq(YbDrgResult::getAreaType, drgApply.getAreaType());
            wrapperResult.eq(YbDrgResult::getState, 1);
//            wrapperResult.in(YbDrgResult::getOrderNumber, orderNumberList);
            List<YbDrgResult> resultAllList = iYbDrgResultService.list(wrapperResult);
            List<YbDrgResult> resultList = this.getInResult(resultAllList,orderNumberList);
            String path = febsProperties.getUploadPath();
            String address = path + "drg/" + drgApply.getApplyDateStr() + "/" + drgApply.getAreaType() + "/";
            if (resultList.size() > 0) {
                long sumSize = 0;
                int sumFileNumber = 0;
                String fileSizeWork = "";
                LambdaQueryWrapper<YbDrgApplyData> wrapperData = new LambdaQueryWrapper<>();
                wrapperData.eq(YbDrgApplyData::getPid, drgApply.getId());
//                wrapperData.in(YbDrgApplyData::getOrderNumber, orderNumberList);
                List<YbDrgApplyData> applyDataAllList = iYbDrgApplyDataService.list(wrapperData);
                List<YbDrgApplyData> applyDataList = this.getInData(applyDataAllList, orderNumberList);
                List<ComFile> fileList = iComFileService.findDrgResultComFiles(drgApply.getApplyDateStr(), drgApply.getAreaType());
                List<ComFile> fileQuery = new ArrayList<>();
                long size = 0;
                for (YbDrgResult item : resultList) {
                    fileSizeWork = "";
                    size = 0;
                    fileQuery = fileList.stream().filter(s -> s.getRefTabId().equals(item.getId())).collect(Collectors.toList());
                    for(ComFile cf :fileQuery) {
                        File file = new File(address + item.getOrderNumber() + "/" + cf.getServerName());
                        FileInputStream fis = new FileInputStream(file);
                        size += fis.available();
                    }
                    fileSizeWork = DataTypeHelpers.formatFileSize(size);
                    sumSize += size;
                    if (fileQuery.size() > 0) {
                        List<YbDrgApplyData> queryList = new ArrayList<>();
                        queryList = applyDataList.stream().filter(s -> s.getId().equals(item.getApplyDataId())).collect(Collectors.toList());
                        if (queryList.size() > 0) {
                            YbDrgResultView drv = this.getYbDrgResultView(item, queryList.get(0));
                            drv.setFileSizeWork(fileSizeWork);
                            drv.setFileNumber(String.valueOf(fileQuery.size()));
                            sumFileNumber += fileQuery.size();
                            list.add(drv);
                        }
                    }
                }

                if(list.size() > 0) {
                    fileSizeWork = DataTypeHelpers.formatFileSize(sumSize);
                    // 以下赋值在前段 有用处 不要删除
                    YbDrgResultView drv = new YbDrgResultView();
                    drv.setId(UUID.randomUUID().toString());
                    drv.setFileSizeWork(fileSizeWork);
                    drv.setApplyDateStr(applyDateStr);
                    drv.setAreaType(areaType);
                    drv.setKs("-");
                    drv.setOrderNumber(orderNumberList.get(0) + "-" + orderNumberList.get(orderNumberList.size() - 1));
                    drv.setJzjlh("-");
                    drv.setBah("-");
                    drv.setDksId("-");
                    drv.setDksName("-");
                    drv.setDoctorName("-");
                    drv.setFileNumber(String.valueOf(sumFileNumber));
                    drv.setOrderNum(0);
                    list.add(drv);
                }
            }
        }
        return list;
    }

    private List<YbDrgResult> getInResult(List<YbDrgResult> resultList,List<String> orderNumberList){
        List<YbDrgResult> list = new ArrayList<>();
        List<YbDrgResult> query = new ArrayList<>();
        for (String orderNumber : orderNumberList) {
            query = resultList.stream().filter(s->s.getOrderNumber().equals(orderNumber)).collect(Collectors.toList());
            if(query.size() > 0) {
                list.add(query.get(0));
            }
        }
        return list;
    }

    private List<YbDrgApplyData> getInData(List<YbDrgApplyData> dataList,List<String> orderNumberList){
        List<YbDrgApplyData> list = new ArrayList<>();
        List<YbDrgApplyData> query = new ArrayList<>();
        for (String orderNumber : orderNumberList) {
            query = dataList.stream().filter(s->s.getOrderNumber().equals(orderNumber)).collect(Collectors.toList());
            if(query.size() > 0) {
                list.add(query.get(0));
            }
        }
        return list;
    }
}