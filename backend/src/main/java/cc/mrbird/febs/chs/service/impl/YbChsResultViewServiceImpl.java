package cc.mrbird.febs.chs.service.impl;

import cc.mrbird.febs.chs.dao.YbChsResultViewMapper;
import cc.mrbird.febs.chs.entity.*;
import cc.mrbird.febs.chs.service.*;
import cc.mrbird.febs.com.controller.DataTypeHelpers;
import cc.mrbird.febs.com.entity.ComFile;
import cc.mrbird.febs.com.service.IComFileService;
import cc.mrbird.febs.common.domain.QueryRequest;
import cc.mrbird.febs.common.properties.FebsProperties;
import cc.mrbird.febs.common.utils.SortUtil;
import cc.mrbird.febs.yb.service.IYbPersonService;
import cn.hutool.core.map.MapWrapper;
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
@Service("IYbChsResultViewService")
@Transactional(propagation = Propagation.SUPPORTS, readOnly = true, rollbackFor = Exception.class)
public class YbChsResultViewServiceImpl extends ServiceImpl<YbChsResultViewMapper, YbChsResultView> implements IYbChsResultViewService {

    @Autowired
    IYbChsApplyService iYbChsApplyService;

    @Autowired
    IYbChsResultService iYbChsResultService;

    @Autowired
    IYbPersonService iYbPersonService;

    @Autowired
    IYbChsApplyDataService iYbChsApplyDataService;

    @Autowired
    IYbChsConfireDataService iYbChsConfireDataService;

    @Autowired
    FebsProperties febsProperties;

    @Autowired
    IComFileService iComFileService;

    @Override
    public IPage<YbChsResultView> findYbChsResultLikeViews(QueryRequest request, YbChsResultView ybChsResultView, String keyField) {
        try {
            String applyDateStr = ybChsResultView.getApplyDateStr();
            Integer areaType = ybChsResultView.getAreaType();
            YbChsApply chsApply = iYbChsApplyService.findChsApplyByApplyDateStrs(applyDateStr, areaType);
            Page<YbChsResultView> page = new Page<>();
            if (chsApply != null) {
                String value = ybChsResultView.getCurrencyField();
                List<String> strList = new ArrayList<>();
                List<YbChsResultView> list = new ArrayList<>();
                List<YbChsResult> resultList = new ArrayList<>();

                LambdaQueryWrapper<YbChsResult> queryWrapper = new LambdaQueryWrapper<>();
                queryWrapper.eq(YbChsResult::getApplyDateStr, applyDateStr);
                queryWrapper.eq(YbChsResult::getAreaType, areaType);
                if (StringUtils.isNotBlank(value) && keyField.equals("doctorName")) {
                    strList = this.iYbPersonService.findPersonCodeList(value);
                    if (strList.size() > 0) {
                        if (strList.size() == 1) {
                            queryWrapper.eq(YbChsResult::getDoctorCode, strList.get(0));
                        } else {
                            queryWrapper.in(YbChsResult::getDoctorCode, strList);
                        }
                    }
                } else {
                    if (StringUtils.isNotBlank(value) && keyField.equals("doctorCode")) {
                        queryWrapper.eq(YbChsResult::getDoctorCode, value);
                    }
                    if (StringUtils.isNotBlank(value) && keyField.equals("orderNum")) {
                        queryWrapper.eq(YbChsResult::getOrderNum, value);
                    }
                }

                resultList = iYbChsResultService.list(queryWrapper);
                List<YbChsApplyData> applyDataList = this.iYbChsApplyDataService.getApplyDataByKeyFieldList(chsApply.getId(), keyField, value);

                if (resultList.size() > 0 && applyDataList.size() > 0) {
                    if (resultList.size() > applyDataList.size()) {
                        List<YbChsResult> queryList = new ArrayList<>();
                        for (YbChsApplyData item : applyDataList) {
                            queryList = resultList.stream().filter(s -> s.getApplyDataId().equals(item.getId())).collect(Collectors.toList());
                            if (queryList.size() > 0) {
                                YbChsResultView crv = this.getYbChsResultView(queryList.get(0), item);
                                list.add(crv);
                            }
                        }
                    } else {
                        for (YbChsResult item : resultList) {
                            List<YbChsApplyData> queryList = new ArrayList<>();
                            queryList = applyDataList.stream().filter(s -> s.getId().equals(item.getApplyDataId())).collect(Collectors.toList());
                            if (queryList.size() > 0) {
                                YbChsResultView crv = this.getYbChsResultView(item, queryList.get(0));
                                list.add(crv);
                            }
                        }
                    }
                }
                if (list.size() > 0) {
                    page.setSearchCount(false);
                    SortUtil.handlePageSort(request, page, false);//true 是属性  false是数据库字段可两个
                    page.setTotal(list.size());
                    long current = page.getCurrent() == 1 ? 0 : (page.getCurrent() - 1) * page.getSize();
                    list = list.stream().sorted(Comparator.comparing(YbChsResultView::getOrderNum)).skip(current).limit(page.getSize()).collect(Collectors.toList());

                    page.setRecords(list);
                }
            }
            return page;

        } catch (Exception e) {
            log.error("获取字典信息失败", e);
            return null;
        }
    }

    @Override
    public IPage<YbChsResultView> findYbChsResultViews(QueryRequest request, YbChsResultView ybChsResultView) {
        try {
            YbChsApply chsApply = iYbChsApplyService.findChsApplyByApplyDateStrs(ybChsResultView.getApplyDateStr(), ybChsResultView.getAreaType());
            Page<YbChsResultView> page = new Page<>();
            if (chsApply != null) {
                ybChsResultView.setPid(chsApply.getId());
                int count = this.baseMapper.findChsResultCount(ybChsResultView);
                if (count > 0) {
                    page.setSearchCount(false);
                    SortUtil.handlePageSort(request, page, false);//true 是属性  false是数据库字段可两个
                    IPage<YbChsResultView> pg = this.baseMapper.findChsResultView(page, ybChsResultView);
                    pg.setTotal(count);
                    return pg;
                }
            }
            return page;

        } catch (Exception e) {
            log.error("获取字典信息失败", e);
            return null;
        }
    }


    private YbChsResultView getYbChsResultView(YbChsResult cr, YbChsApplyData chsApplyData) {
        YbChsResultView crv = new YbChsResultView();
        crv.setPid(chsApplyData.getPid());
        crv.setAppealEndDate(chsApplyData.getAppealEndDate());//申诉截止日期
        crv.setPayPlaceType(chsApplyData.getPayPlaceType());//支付地点类别
        crv.setYdState(chsApplyData.getYdState());//疑点状态
        crv.setAreaName(chsApplyData.getAreaName());//医保区划
        crv.setYyjgCode(chsApplyData.getYyjgCode());//医药机构编码
        crv.setYyjgName(chsApplyData.getYyjgName());//医药机构名称
        crv.setDeptName(chsApplyData.getDeptName());//科室
        crv.setDoctorName(chsApplyData.getDoctorName());//医生
        crv.setMedicalType(chsApplyData.getMedicalType());//医疗类别
        crv.setZymzNumber(chsApplyData.getZymzNumber());//住院门诊号
        crv.setInsuredName(chsApplyData.getInsuredName());//参保人
        crv.setEnterHospitalDate(chsApplyData.getEnterHospitalDate());//入院日期
        crv.setOutHospitalDate(chsApplyData.getOutHospitalDate());//出院日期
        crv.setSettlementDate(chsApplyData.getSettlementDate());//结算日期
        crv.setCardNumber(chsApplyData.getCardNumber());//身份证号
        crv.setProjectCode(chsApplyData.getProjectCode());//医保项目编码
        crv.setProjectName(chsApplyData.getProjectName());//医院项目名称
        crv.setRuleName(chsApplyData.getRuleName());//规则名称
        crv.setViolateCsPrice(chsApplyData.getViolateCsPrice());//初审违规金额（元）
        crv.setViolatePrice(chsApplyData.getViolatePrice());//违规金额（元）
        crv.setViolateReason(chsApplyData.getViolateReason());//违规内容
        crv.setZdNote(chsApplyData.getZdNote());//诊断信息
        crv.setCostDate(chsApplyData.getCostDate());//费用日期
        crv.setInsuredType(chsApplyData.getInsuredType());//险种类型
        crv.setPrice(chsApplyData.getPrice());//单价（元）
        crv.setNum(chsApplyData.getNum());//数量
        crv.setMedicalPrice(chsApplyData.getMedicalPrice());//金额（元）
        crv.setTcPayPrice(chsApplyData.getTcPayPrice());//统筹支付（元）
        crv.setSpecs(chsApplyData.getSpecs());//规格
        crv.setJx(chsApplyData.getJx());//剂型
        crv.setJgLevel(chsApplyData.getJgLevel());//机构等级

        crv.setOrderNum(chsApplyData.getOrderNum());
        crv.setApplyDateStr(cr.getApplyDateStr());
        crv.setId(cr.getId());
        crv.setApplyDataId(chsApplyData.getId());
//        crv.setVerifyId(cr.getVerifyId());
        crv.setResultDoctorCode(cr.getDoctorCode());
        crv.setResultDoctorName(cr.getDoctorName());
        crv.setResultDksId(cr.getDksId());
        crv.setResultDksName(cr.getDksName());
        crv.setOperateReason(cr.getOperateReason());
        crv.setOperateDate(cr.getOperateDate());
        crv.setState(cr.getState());
        crv.setAreaType(cr.getAreaType());
        return crv;
    }

    @Override
    public List<YbChsResultView> findChsResultViewLists(YbChsResultView ybChsResultView) {
        return this.baseMapper.findChsResultViewList(ybChsResultView);
    }
    /*
    // 一个个打包
    @Override
    public List<YbChsResultView> findChsResultFileSizeByOrderNumList(String applyDateStr, int areaType, List<Integer> orderNumList) throws IOException {
        List<YbChsResultView> list = new ArrayList<>();
        YbChsApply drgApply = iYbChsApplyService.findChsApplyByApplyDateStrs(applyDateStr, areaType);
        if (drgApply != null) {
            LambdaQueryWrapper<YbChsResult> wrapperResult = new LambdaQueryWrapper<>();
            wrapperResult.eq(YbChsResult::getApplyDateStr, drgApply.getApplyDateStr());
            wrapperResult.eq(YbChsResult::getAreaType, drgApply.getAreaType());
//            wrapperResult.in(YbChsResult::getOrderNum, orderNumList);
            List<YbChsResult> resultAllList = iYbChsResultService.list(wrapperResult);
            List<YbChsResult> resultList = this.getInResult(resultAllList,orderNumList);
            String path = febsProperties.getUploadPath();
            String address = path + "chs/" + drgApply.getApplyDateStr()+ "/" + drgApply.getAreaType() + "/";
            if (resultList.size() > 0) {
                long sumSize = 0;
                int sumFileNumber = 0;
                String fileSizeWork = "";
                LambdaQueryWrapper<YbChsApplyData> wrapperData = new LambdaQueryWrapper<>();
                wrapperData.eq(YbChsApplyData::getPid, drgApply.getId());
//                wrapperData.in(YbChsApplyData::getOrderNumber, orderNumList);
                List<YbChsApplyData> applyDataAllList = iYbChsApplyDataService.list(wrapperData);
                List<YbChsApplyData> applyDataList = this.getInData(applyDataAllList,orderNumList);
                List<ComFile> fileList = iComFileService.findChsResultComFiles(drgApply.getApplyDateStr(), drgApply.getAreaType());
                List<ComFile> fileQuery = new ArrayList<>();
                long size = 0;
                for (YbChsResult item : resultList) {
                    fileSizeWork = "";
                    size = 0;
                    fileQuery = fileList.stream().filter(s -> s.getRefTabId().equals(item.getId())).collect(Collectors.toList());
                    for(ComFile cf :fileQuery) {
                        File file = new File(address + cf.getServerName());
                        FileInputStream fis = new FileInputStream(file);
                        size += fis.available();
                    }
                    fileSizeWork = DataTypeHelpers.formatFileSize(size);
                    sumSize += size;
                    if (fileQuery.size() > 0) {
                        List<YbChsApplyData> queryList = new ArrayList<>();
                        queryList = applyDataList.stream().filter(s -> s.getId().equals(item.getApplyDataId())).collect(Collectors.toList());
                        if (queryList.size() > 0) {
                            YbChsResultView drv = this.getYbChsResultView(item, queryList.get(0));
                            drv.setFileSizeWork(fileSizeWork);
                            drv.setFileNumber(String.valueOf(fileQuery.size()));
                            drv.setCurrencyField(item.getOrderNum().toString());
                            sumFileNumber += fileQuery.size();
                            list.add(drv);
                        }
                    }
                }

                if(list.size() > 0) {
                    fileSizeWork = DataTypeHelpers.formatFileSize(sumSize);
                    // 以下赋值在前段 有用处 不要删除
                    YbChsResultView crv = new YbChsResultView();
                    crv.setId(UUID.randomUUID().toString());
                    crv.setFileSizeWork(fileSizeWork);
                    crv.setApplyDateStr(applyDateStr);
                    crv.setAreaType(areaType);
                    crv.setMedicalType("-");
                    crv.setZymzNumber("-");
                    crv.setCurrencyField(orderNumList.get(0) + "-" + orderNumList.get(orderNumList.size() - 1));
                    crv.setInsuredName("-");
                    crv.setResultDksId("-");
                    crv.setResultDksName("-");
                    crv.setResultDoctorName("-");
                    crv.setFileNumber(String.valueOf(sumFileNumber));
                    crv.setOrderNum(0);
                    list.add(crv);
                }
            }
        }
        return list;
    }
    */

    // 一条复议只上传一个附件此处改成10个进行打包
    @Override
    public List<YbChsResultView> findChsResultFileSizeByOrderNumList(String applyDateStr, int areaType, List<Integer> orderNumList, Integer maxCount) throws IOException {
        List<YbChsResultView> list = new ArrayList<>();
        YbChsApply drgApply = iYbChsApplyService.findChsApplyByApplyDateStrs(applyDateStr, areaType);
        if (drgApply != null) {
            maxCount = maxCount == null ? 10 : maxCount;
            LambdaQueryWrapper<YbChsResult> wrapperResult = new LambdaQueryWrapper<>();
            wrapperResult.eq(YbChsResult::getApplyDateStr, drgApply.getApplyDateStr());
            wrapperResult.eq(YbChsResult::getAreaType, drgApply.getAreaType());
//            wrapperResult.in(YbChsResult::getOrderNum, orderNumList);
            List<YbChsResult> resultAllList = iYbChsResultService.list(wrapperResult);
            if(resultAllList.size() > 0) {
                String path = febsProperties.getUploadPath();
                String address = path + "chs/" + drgApply.getApplyDateStr() + "/" + drgApply.getAreaType() + "/";

                List<ComFile> fileList = iComFileService.findChsResultComFiles(drgApply.getApplyDateStr(), drgApply.getAreaType());
                List<ComFile> fileQuery = new ArrayList<>();

                Map<Integer, List<Integer>> listMap = this.getOrderNumMap(orderNumList, maxCount);

                int forOrderNum = 1;
                long sumSize = 0;
                int sumFileNumber = 0;
                long forSize = 0;
                int forFileNumber = 0;
                String fileSizeWork = "";
                long size = 0;
                for (Integer key : listMap.keySet()) {
                    forSize = 0;
                    forFileNumber = 0;
                    fileSizeWork = "";
                    List<Integer> keyOrderNumList = listMap.get(key);
                    List<YbChsResult> resultList = this.getInResult(resultAllList, keyOrderNumList);
                    if (resultList.size() > 0) {
                        YbChsResultView crv = new YbChsResultView();
                        crv.setId(UUID.randomUUID().toString());
                        crv.setApplyDateStr(applyDateStr);
                        crv.setCurrencyField(keyOrderNumList.get(0) + "-" + keyOrderNumList.get(keyOrderNumList.size()-1));
                        for (YbChsResult item : resultList) {
                            size = 0;
                            fileQuery = fileList.stream().filter(s -> s.getRefTabId().equals(item.getId())).collect(Collectors.toList());
                            for (ComFile cf : fileQuery) {
                                File file = new File(address + cf.getServerName());
                                if(file.exists()) {
                                    FileInputStream fis = new FileInputStream(file);
                                    size += fis.available();
                                }
                            }

                            sumSize += size;
                            forSize += size;
                            if (fileQuery.size() > 0) {
                                sumFileNumber += fileQuery.size();
                                forFileNumber += fileQuery.size();
                            }
                        }
                        fileSizeWork = DataTypeHelpers.formatFileSize(forSize);
                        crv.setFileNumber(String.valueOf(forFileNumber));
                        crv.setFileSizeWork(fileSizeWork);
                        crv.setAreaType(areaType);
                        crv.setOrderNum(forOrderNum);
                        list.add(crv);
                        forOrderNum ++;
                    }
                }

                if (list.size() > 0) {
                    fileSizeWork = DataTypeHelpers.formatFileSize(sumSize);
                    // 以下赋值在前段 有用处 不要删除
                    YbChsResultView crv = new YbChsResultView();
                    crv.setId(UUID.randomUUID().toString());
                    crv.setFileSizeWork(fileSizeWork);
                    crv.setApplyDateStr(applyDateStr);
                    crv.setAreaType(areaType);
                    crv.setCurrencyField(orderNumList.get(0) + "-" + orderNumList.get(orderNumList.size() - 1));
                    crv.setFileNumber(String.valueOf(sumFileNumber));
                    crv.setOrderNum(0);
                    list.add(crv);
                }
            }
        }
        return list;
    }

    private Map<Integer, List<Integer>> getOrderNumMap(List<Integer> orderNumList, Integer maxCount) {
        Map<Integer, List<Integer>> listMap = new HashMap<>();
        maxCount = orderNumList.size() < maxCount ? orderNumList.size() : maxCount;
        Integer end = orderNumList.get(orderNumList.size() - 1);
        Integer ys = end % maxCount;
        Integer count = 1;
        Integer jscount = 1;
        List<Integer> fdList = new ArrayList<>();
        for (Integer it : orderNumList) {
            if (jscount != maxCount) {
                fdList.add(it);
                jscount++;
            } else {
                jscount = 1;
                fdList.add(it);
                listMap.put(count, fdList);
                count++;
                fdList = new ArrayList<>();
            }
            if (ys !=0 && it == end) {
                listMap.put(count, fdList);
            }
        }
        return listMap;
    }

    private List<YbChsResult> getInResult(List<YbChsResult> resultList, List<Integer> orderNumList) {
        List<YbChsResult> list = new ArrayList<>();
        List<YbChsResult> query = new ArrayList<>();
        for (Integer orderNum : orderNumList) {
            query = resultList.stream().filter(s -> s.getOrderNum().equals(orderNum)).collect(Collectors.toList());
            if (query.size() > 0) {
                list.add(query.get(0));
            }
        }
        return list;
    }

    private List<YbChsApplyData> getInData(List<YbChsApplyData> dataList, List<Integer> orderNumList) {
        List<YbChsApplyData> list = new ArrayList<>();
        List<YbChsApplyData> query = new ArrayList<>();
        for (Integer orderNum : orderNumList) {
            query = dataList.stream().filter(s -> s.getOrderNum().equals(orderNum)).collect(Collectors.toList());
            if (query.size() > 0) {
                list.add(query.get(0));
            }
        }
        return list;
    }

}