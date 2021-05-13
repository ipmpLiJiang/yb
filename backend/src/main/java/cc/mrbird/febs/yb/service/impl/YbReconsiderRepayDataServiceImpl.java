package cc.mrbird.febs.yb.service.impl;

import cc.mrbird.febs.common.domain.QueryRequest;
import cc.mrbird.febs.common.utils.SortUtil;
import cc.mrbird.febs.yb.entity.*;
import cc.mrbird.febs.yb.dao.YbReconsiderRepayDataMapper;
import cc.mrbird.febs.yb.service.*;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.mchange.v2.beans.swing.TestBean;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.xerces.impl.dv.xs.DecimalDV;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.OrderComparator;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.*;
import java.time.LocalDate;
import java.util.stream.Collectors;

/**
 * <p>
 * 服务实现类
 * </p>
 *
 * @author viki
 * @since 2020-09-07
 */
@Slf4j
@Service("IYbReconsiderRepayDataService")
@Transactional(propagation = Propagation.SUPPORTS, readOnly = true, rollbackFor = Exception.class)
public class YbReconsiderRepayDataServiceImpl extends ServiceImpl<YbReconsiderRepayDataMapper, YbReconsiderRepayData> implements IYbReconsiderRepayDataService {

    @Autowired
    IYbReconsiderResetDataService iYbReconsiderResetDataService;

    @Autowired
    IYbAppealResultService iYbAppealResultService;
    @Autowired
    IYbReconsiderRepayService iYbReconsiderRepayService;

    @Override
    public IPage<YbReconsiderRepayData> findYbReconsiderRepayDatas(QueryRequest request, YbReconsiderRepayData ybReconsiderRepayData) {
        try {
            LambdaQueryWrapper<YbReconsiderRepayData> queryWrapper = new LambdaQueryWrapper<>();
            queryWrapper.eq(YbReconsiderRepayData::getIsDeletemark, 1);//1是未删 0是已删

            if (ybReconsiderRepayData.getPid() != null) {
                queryWrapper.eq(YbReconsiderRepayData::getPid, ybReconsiderRepayData.getPid());
            }

            if (ybReconsiderRepayData.getBelongDateStr() != null) {
                queryWrapper.eq(YbReconsiderRepayData::getBelongDateStr, ybReconsiderRepayData.getBelongDateStr());
            }

            if (ybReconsiderRepayData.getRepayType() != null) {
                queryWrapper.eq(YbReconsiderRepayData::getRepayType, ybReconsiderRepayData.getRepayType());
            }

            if (ybReconsiderRepayData.getDataType() != null) {
                queryWrapper.eq(YbReconsiderRepayData::getDataType, ybReconsiderRepayData.getDataType());
            }
            if (ybReconsiderRepayData.getState() != null) {
                queryWrapper.eq(YbReconsiderRepayData::getState, ybReconsiderRepayData.getState());
            }
            if (ybReconsiderRepayData.getSeekState() != null) {
                queryWrapper.eq(YbReconsiderRepayData::getSeekState, ybReconsiderRepayData.getSeekState());
            }

            Page<YbReconsiderRepayData> page = new Page<>();
            SortUtil.handlePageSort(request, page, false);//true 是属性  false是数据库字段可两个
            return this.page(page, queryWrapper);
        } catch (Exception e) {
            log.error("获取字典信息失败", e);
            return null;
        }
    }

    @Override
    public IPage<YbReconsiderRepayData> findYbReconsiderRepayDataList(QueryRequest request, YbReconsiderRepayData ybReconsiderRepayData) {
        try {
            Page<YbReconsiderRepayData> page = new Page<>();
            SortUtil.handlePageSort(request, page, false);//true 是属性  false是数据库字段可两个
            return this.baseMapper.findYbReconsiderRepayData(page, ybReconsiderRepayData);
        } catch (Exception e) {
            log.error("获取失败", e);
            return null;
        }
    }

    @Override
    public List<YbReconsiderRepayData> findReconsiderRepayLists(YbReconsiderRepayData ybReconsiderRepayData) {
        LambdaQueryWrapper<YbReconsiderRepayData> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(YbReconsiderRepayData::getIsDeletemark, 1);//1是未删 0是已删

        if (ybReconsiderRepayData.getBelongDateStr() != null) {
            queryWrapper.eq(YbReconsiderRepayData::getBelongDateStr, ybReconsiderRepayData.getBelongDateStr());
        }

        if (ybReconsiderRepayData.getRepayType() != null) {
            queryWrapper.eq(YbReconsiderRepayData::getRepayType, ybReconsiderRepayData.getRepayType());
        }

        if (ybReconsiderRepayData.getDataType() != null) {
            queryWrapper.eq(YbReconsiderRepayData::getDataType, ybReconsiderRepayData.getDataType());
        }
        if (ybReconsiderRepayData.getState() != null) {
            queryWrapper.eq(YbReconsiderRepayData::getState, ybReconsiderRepayData.getState());
        }
        if (ybReconsiderRepayData.getSeekState() != null) {
            queryWrapper.eq(YbReconsiderRepayData::getSeekState, ybReconsiderRepayData.getSeekState());
        }

        return this.list(queryWrapper);
    }

    @Override
    @Transactional
    public void createYbReconsiderRepayData(YbReconsiderRepayData ybReconsiderRepayData) {
        if (ybReconsiderRepayData.getId() == null || "".equals(ybReconsiderRepayData.getId())) {
            ybReconsiderRepayData.setId(UUID.randomUUID().toString());
        }
        ybReconsiderRepayData.setCreateTime(new Date());
        ybReconsiderRepayData.setIsDeletemark(1);
        this.save(ybReconsiderRepayData);
    }

    @Override
    @Transactional
    public void updateYbReconsiderRepayData(YbReconsiderRepayData ybReconsiderRepayData) {
        ybReconsiderRepayData.setModifyTime(new Date());
        this.baseMapper.updateYbReconsiderRepayData(ybReconsiderRepayData);
    }

    @Override
    @Transactional
    public void deleteYbReconsiderRepayDatas(String[] Ids) {
        List<String> list = Arrays.asList(Ids);
        this.baseMapper.deleteBatchIds(list);
    }

    @Override
    public List<String> findGroupBelongDateStrs(String pid) {
        return this.baseMapper.findGroupBelongDateStr(pid);
    }

    @Override
    @Transactional
    public String updateOrderNumberRepayDatas(YbReconsiderRepayData ybReconsiderRepayData,Integer areaType, Long uid, String uname) {
        String message = "";
        String sql = "";
        LambdaQueryWrapper<YbReconsiderRepayData> queryWrapperRay = new LambdaQueryWrapper<>();
        sql += " pid = '" + ybReconsiderRepayData.getPid() + "'";
        sql += " and dataType = " + ybReconsiderRepayData.getDataType();
        sql += " and seekState = " + ybReconsiderRepayData.getSeekState();
        sql += " and state = " + ybReconsiderRepayData.getState();
        sql += " and belongDateStr = '" + ybReconsiderRepayData.getBelongDateStr() + "'";
        sql += " and orderNumber IS NOT NULL AND orderNumber != ''";
        queryWrapperRay.apply(sql);
        List<YbReconsiderRepayData> repayDataList = this.list(queryWrapperRay);
        if (repayDataList.size() > 0) {
            List<YbReconsiderResetData> resetDataList = this.iYbReconsiderResetDataService.findReconsiderResetDataByApplyDates(ybReconsiderRepayData.getBelongDateStr(),areaType, ybReconsiderRepayData.getDataType());
            if (resetDataList.size() > 0) {
                List<YbAppealResult> resultList = this.iYbAppealResultService.findAppealResulDataByRepays(ybReconsiderRepayData.getBelongDateStr(), areaType,ybReconsiderRepayData.getDataType());
                if (resultList.size() == 0) {
                    message = "result0";
                }
                //更新申诉上传
                List<YbAppealResult> updateResultList = new ArrayList<>();
                //更新还款
                List<YbReconsiderRepayData> updateRepayDataList = new ArrayList<>();
                //更新剔除
                List<YbReconsiderResetData> updateResetDataList = new ArrayList<>();

                List<YbReconsiderResetData> searchResetDataList = null;
                List<YbAppealResult> searchResultList = new ArrayList<>();

                //有序号优先匹配
                for (YbReconsiderRepayData rrd : repayDataList) {
                    YbReconsiderRepayData updateRepayData = new YbReconsiderRepayData();
                    updateRepayData.setId(rrd.getId());

                    searchResetDataList = new ArrayList<>();

                    searchResetDataList = resetDataList.stream().filter(
                            s -> s.getOrderNumber().equals(rrd.getOrderNumber()) &&
                                    s.getDeductPrice().equals(rrd.getDeductPrice())
                    ).collect(Collectors.toList());

                    if (searchResetDataList.size() == 1) {
                        if (resultList.size() > 0) {
                            YbReconsiderResetData rAd = searchResetDataList.get(0);

                            searchResultList = resultList.stream().filter(
                                    s -> s.getRelatelDataId().equals(rAd.getRelatelDataId())
                            ).collect(Collectors.toList());

                            if (searchResultList.size() == 1) {
                                YbAppealResult appealResult = searchResultList.get(0);

                                YbAppealResult updateResult = new YbAppealResult();
                                updateResult.setId(appealResult.getId());

                                YbReconsiderResetData updateResetData = new YbReconsiderResetData();
                                updateResetData.setId(rAd.getId());

                                BigDecimal addDec = null;
                                if (rrd.getRepaymentPrice() != null) {
                                    addDec = new BigDecimal(rrd.getRepaymentPrice().toString());
                                } else {
                                    addDec = new BigDecimal("0");
                                }
                                BigDecimal repaymentPrice = new BigDecimal("0");
                                if (rAd.getRepaymentPrice() != null) {
                                    repaymentPrice = addDec.add(rAd.getRepaymentPrice());
                                } else {
                                    repaymentPrice = addDec;
                                }

                                if (repaymentPrice.compareTo(rAd.getDeductPrice()) >= 0) {
                                    updateResult.setRepayState(YbDefaultValue.RESULTREPAYSTATE_1);
                                } else {
                                    //调整
//                                    updateResult.setRepayState(YbDefaultValue.RESULTREPAYSTATE_3);
                                }

                                updateResetData.setRepaymentPrice(repaymentPrice);
                                updateRepayData.setResultId(updateResult.getId());
                                updateRepayData.setResetDataId(rAd.getId());
                                updateRepayData.setResultId(appealResult.getId());

                                updateRepayData.setApplyDataId(appealResult.getApplyDataId());
                                updateRepayData.setWarnType(YbDefaultValue.WARNTYPE_1);
                                updateRepayData.setSeekState(YbDefaultValue.SEEKSTATE_1);

                                updateResetDataList.add(updateResetData);
                                updateRepayDataList.add(updateRepayData);
                                updateResultList.add(updateResult);
                            }
                        }
                    }
                }
                boolean bl = true;
                if (updateResultList.size() > 0) {
                    bl = this.iYbAppealResultService.updateBatchById(updateResultList);
                }
                if (bl) {
                    if (updateResetDataList.size() > 0) {
                        bl = this.iYbReconsiderResetDataService.updateBatchById(updateResetDataList);
                    }
                }
                if (bl) {
                    if (updateRepayDataList.size() > 0) {
                        bl = this.updateBatchById(updateRepayDataList);
                        if (bl) {
                            YbReconsiderRepay ybReconsiderRepay = new YbReconsiderRepay();
                            ybReconsiderRepay.setId(ybReconsiderRepayData.getPid());
                            ybReconsiderRepay.setState(1);
                            bl = iYbReconsiderRepayService.updateById(ybReconsiderRepay);
                        }
                    }
                }

                if (bl) {
                    message = "ok";
                }
            } else {
                message = "序号匹配，未找到剔除数据";
            }
        } else {
            message = "ok";
        }

        return message;
    }

    @Override
    @Transactional
    public String updateFieldRepayDatas(YbReconsiderRepayData ybReconsiderRepayData,Integer areaType, Long uid, String uname) {
        String message = "";

        LambdaQueryWrapper<YbReconsiderRepayData> queryWrapperRay = new LambdaQueryWrapper<>();
        queryWrapperRay.eq(YbReconsiderRepayData::getPid, ybReconsiderRepayData.getPid());
        queryWrapperRay.eq(YbReconsiderRepayData::getDataType, ybReconsiderRepayData.getDataType());
        // queryWrapperRay.eq(YbReconsiderRepayData::getRepayType, ybReconsiderRepayData.getRepayType());
        queryWrapperRay.eq(YbReconsiderRepayData::getSeekState, ybReconsiderRepayData.getSeekState());
        queryWrapperRay.eq(YbReconsiderRepayData::getState, ybReconsiderRepayData.getState());
        queryWrapperRay.eq(YbReconsiderRepayData::getBelongDateStr, ybReconsiderRepayData.getBelongDateStr());

        List<YbReconsiderRepayData> repayDataList = this.list(queryWrapperRay);
        if (repayDataList.size() > 0) {
            List<YbReconsiderResetData> resetDataList = this.iYbReconsiderResetDataService.findResetNotExistsRepayByApplyDates(ybReconsiderRepayData.getBelongDateStr(),areaType, ybReconsiderRepayData.getDataType());
            if (resetDataList.size() > 0) {
                List<YbAppealResult> resultList = this.iYbAppealResultService.findAppealResulDataByRepays(ybReconsiderRepayData.getBelongDateStr(),areaType, ybReconsiderRepayData.getDataType());
                if (resultList.size() == 0) {
                    message = "result0";
                }
                //更新申诉上传
                List<YbAppealResult> updateResultList = new ArrayList<>();
                //更新还款
                List<YbReconsiderRepayData> updateRepayDataList = new ArrayList<>();
                //更新剔除
                List<YbReconsiderResetData> updateResetDataList = new ArrayList<>();

                List<YbReconsiderResetData> searchResetDataList = null;
                List<YbAppealResult> searchResultList = new ArrayList<>();

                //模糊(字段)匹配
                for (YbReconsiderRepayData rrd : repayDataList) {
                    YbReconsiderRepayData updateRepayData = new YbReconsiderRepayData();
                    updateRepayData.setId(rrd.getId());

                    searchResetDataList = new ArrayList<YbReconsiderResetData>();

                    if (rrd.getDataType() == 0) {
                        searchResetDataList = resetDataList.stream().filter(
                                s -> s.getBillNo().equals(rrd.getBillNo()) &&
                                        s.getProjectName().equals(rrd.getProjectName()) &&
                                        s.getDeductPrice().equals(rrd.getDeductPrice())
                        ).collect(Collectors.toList());
                    } else {
                        searchResetDataList = resetDataList.stream().filter(
                                s -> s.getBillNo().equals(rrd.getBillNo()) &&
                                        s.getDeductPrice().equals(rrd.getDeductPrice())
                        ).collect(Collectors.toList());
                    }

                    if (searchResetDataList.size() == 1) {
                        if (resultList.size() > 0) {
                            YbReconsiderResetData rAd = searchResetDataList.get(0);

                            searchResultList = resultList.stream().filter(
                                    s -> s.getRelatelDataId().equals(rAd.getRelatelDataId())
                            ).collect(Collectors.toList());

                            if (searchResultList.size() == 1) {
                                YbAppealResult appealResult = searchResultList.get(0);

                                YbAppealResult updateResult = new YbAppealResult();
                                updateResult.setId(appealResult.getId());

                                YbReconsiderResetData updateResetData = new YbReconsiderResetData();
                                updateResetData.setId(rAd.getId());

                                BigDecimal addDec = null;
                                if (rrd.getRepaymentPrice() != null) {
                                    addDec = new BigDecimal(rrd.getRepaymentPrice().toString());
                                } else {
                                    addDec = new BigDecimal("0");
                                }
                                BigDecimal repaymentPrice = new BigDecimal("0");
                                if (rAd.getRepaymentPrice() != null) {
                                    repaymentPrice = addDec.add(rAd.getRepaymentPrice());
                                } else {
                                    repaymentPrice = addDec;
                                }

                                if (repaymentPrice.compareTo(rAd.getDeductPrice()) >= 0) {
                                    updateResult.setRepayState(YbDefaultValue.RESULTREPAYSTATE_1);
                                } else {
                                    //调整
//                                    updateResult.setRepayState(YbDefaultValue.RESULTREPAYSTATE_3);
                                }

                                updateResetData.setRepaymentPrice(repaymentPrice);

                                updateRepayData.setOrderNumberNew(rAd.getOrderNumber());
                                updateRepayData.setResetDataId(rAd.getId());
                                updateRepayData.setResultId(appealResult.getId());
                                updateRepayData.setApplyDataId(appealResult.getApplyDataId());
                                updateRepayData.setWarnType(YbDefaultValue.WARNTYPE_2);//字段匹配
                                updateRepayData.setUpdateType(YbDefaultValue.UPDATETYPE_1);
                                updateRepayData.setSeekState(YbDefaultValue.SEEKSTATE_1);

                                updateResetDataList.add(updateResetData);
                                updateRepayDataList.add(updateRepayData);
                                updateResultList.add(updateResult);
                            }
                        }
                    } else if (searchResetDataList.size() == 0) {
                        updateRepayData.setState(YbDefaultValue.REPAYDATA_STATE_2);
                        updateRepayData.setWarnType(YbDefaultValue.WARNTYPE_4);//无匹配
                        updateRepayDataList.add(updateRepayData);
                    } else {
                        String orderNumberNews = "";
                        for (YbReconsiderResetData rad3 : searchResetDataList) {
                            if ("".equals(orderNumberNews)) {
                                orderNumberNews = rad3.getOrderNumber();
                            } else {
                                orderNumberNews += "," + rad3.getOrderNumber();
                            }
                        }
                        updateRepayData.setOrderNumberNew(orderNumberNews);
                        updateRepayData.setState(YbDefaultValue.REPAYDATA_STATE_1);
                        updateRepayData.setWarnType(YbDefaultValue.WARNTYPE_3);//字段匹配多个
                        updateRepayDataList.add(updateRepayData);
                    }
                }
                boolean bl = true;
                if (updateResultList.size() > 0) {
                    bl = this.iYbAppealResultService.updateBatchById(updateResultList);
                }
                if (bl) {
                    if (updateResetDataList.size() > 0) {
                        bl = this.iYbReconsiderResetDataService.updateBatchById(updateResetDataList);
                    }
                }
                if (bl) {
                    if (updateRepayDataList.size() > 0) {
                        bl = this.updateBatchById(updateRepayDataList);
                        if (bl) {
                            YbReconsiderRepay ybReconsiderRepay = new YbReconsiderRepay();
                            ybReconsiderRepay.setId(ybReconsiderRepayData.getPid());
                            ybReconsiderRepay.setState(1);
                            bl = iYbReconsiderRepayService.updateById(ybReconsiderRepay);
                        }
                    }
                }
                if (bl) {
                    message = "ok";
                }
            } else {
                message = "模糊匹配,未找到剔除数据";
            }
        } else {
            message = "repay0";
        }

        return message;
    }

    @Override
    @Transactional
    public String updateHandleRepayDatas(String resultId, String repayId, Long uid, String uname) {
        String message = "";
        YbReconsiderRepayData repayData = this.baseMapper.selectById(repayId);
        if (repayData != null) {
            if (repayData.getSeekState() == YbDefaultValue.SEEKSTATE_0) {
                List<YbReconsiderRepayData> dataList = new ArrayList<>();
                if (repayData.getWarnType() == YbDefaultValue.WARNTYPE_3) {
                    LambdaQueryWrapper<YbReconsiderRepayData> queryWrapper = new LambdaQueryWrapper<>();
                    queryWrapper.eq(YbReconsiderRepayData::getIsDeletemark, 1);//1是未删 0是已删
                    queryWrapper.eq(YbReconsiderRepayData::getPid, repayData.getPid());
                    queryWrapper.eq(YbReconsiderRepayData::getRepayType, repayData.getRepayType());
                    queryWrapper.eq(YbReconsiderRepayData::getDataType, repayData.getDataType());
                    queryWrapper.eq(YbReconsiderRepayData::getOrderNumberNew, repayData.getOrderNumberNew());
                    queryWrapper.ne(YbReconsiderRepayData::getId, repayData.getId());
                    queryWrapper.eq(YbReconsiderRepayData::getWarnType, YbDefaultValue.WARNTYPE_3);
                    queryWrapper.eq(YbReconsiderRepayData::getState, YbDefaultValue.REPAYDATA_STATE_1);
                    queryWrapper.eq(YbReconsiderRepayData::getSeekState, YbDefaultValue.SEEKSTATE_0);
                    dataList = this.list(queryWrapper);
                }
                YbAppealResult appealResult = this.iYbAppealResultService.getById(resultId);
                if (appealResult != null) {
                    YbReconsiderResetData reconsiderResetData = null;//this.iYbReconsiderResetDataService.getById(appealResult.getResetDataId());
                    if (appealResult.getRepayState() == YbDefaultValue.RESULTREPAYSTATE_2 || appealResult.getRepayState() == YbDefaultValue.RESULTREPAYSTATE_3) {
                        YbReconsiderRepayData updateRepayData = new YbReconsiderRepayData();
                        updateRepayData.setId(repayData.getId());
                        updateRepayData.setSeekState(YbDefaultValue.SEEKSTATE_1);
                        updateRepayData.setWarnType(YbDefaultValue.WARNTYPE_5);//异常匹配(字段匹配多个，选择一个匹配，在异常数据中匹配)
                        updateRepayData.setResetDataId(reconsiderResetData.getId());
                        updateRepayData.setResultId(appealResult.getId());
                        updateRepayData.setApplyDataId(appealResult.getApplyDataId());
                        updateRepayData.setUpdateType(YbDefaultValue.UPDATETYPE_1);
                        updateRepayData.setState(YbDefaultValue.REPAYDATA_STATE_0);
                        updateRepayData.setOrderNumberNew(reconsiderResetData.getOrderNumber());

                        YbAppealResult updateResult = new YbAppealResult();
                        updateResult.setId(appealResult.getId());

                        BigDecimal addDec = null;
                        if (repayData.getRepaymentPrice() != null) {
                            addDec = new BigDecimal(repayData.getRepaymentPrice().toString());
                        } else {
                            addDec = new BigDecimal("0");
                        }
                        BigDecimal repaymentPrice = new BigDecimal("0");
                        if (reconsiderResetData.getRepaymentPrice() != null) {
                            repaymentPrice = addDec.add(reconsiderResetData.getRepaymentPrice());
                        } else {
                            repaymentPrice = addDec;
                        }

                        YbReconsiderResetData udpateResetData = new YbReconsiderResetData();
                        udpateResetData.setId(reconsiderResetData.getId());
                        udpateResetData.setRepaymentPrice(repaymentPrice);

                        if (repaymentPrice.compareTo(reconsiderResetData.getDeductPrice()) >= 0) {
                            updateResult.setRepayState(YbDefaultValue.RESULTREPAYSTATE_1);
                        } else {
                            //调整
//                            updateResult.setRepayState(YbDefaultValue.RESULTREPAYSTATE_3);
                        }
                        List<YbReconsiderRepayData> updateRepayDataList = new ArrayList<>();
                        if (repayData.getWarnType() == YbDefaultValue.WARNTYPE_3 && dataList != null) {
                            if (dataList.size() > 0) {
                                for (YbReconsiderRepayData rrd : dataList) {
                                    if (rrd.getOrderNumberNew() != null) {
                                        if (repayData.getOrderNumberNew().equals(rrd.getOrderNumberNew())) {
                                            String[] strNewArr = rrd.getOrderNumberNew().split(",");
                                            String newOrderNumber = "";
                                            for (String o : strNewArr) {
                                                if (!o.equals(reconsiderResetData.getOrderNumber())) {
                                                    if (newOrderNumber.equals("")) {
                                                        newOrderNumber = o;
                                                    } else {
                                                        newOrderNumber += "," + o;
                                                    }
                                                }
                                            }
                                            if (!newOrderNumber.equals("")) {
                                                YbReconsiderRepayData u = new YbReconsiderRepayData();
                                                u.setId(rrd.getId());
                                                u.setOrderNumberNew(newOrderNumber);
                                                updateRepayDataList.add(u);
                                            }
                                        }
                                    }
                                }
                            }
                        }
                        updateRepayDataList.add(updateRepayData);
                        boolean isTrue = this.updateBatchById(updateRepayDataList);
                        //int count = this.baseMapper.updateById(updateRepayData);
                        if (isTrue) {
                            isTrue = this.iYbReconsiderResetDataService.updateById(udpateResetData);
                            if (isTrue) {
                                isTrue = this.iYbAppealResultService.updateById(updateResult);
                                if (isTrue) {
                                    message = "ok";
                                } else {
                                    message = "该申诉上传数据还款状态更新失败";
                                }
                            } else {
                                message = "该剔除数据还款金额更新失败";
                            }
                        } else {
                            message = "该还款数据还款状态更新失败";
                        }

                    } else {
                        message = "该申诉上传数据已完成还款更新";
                    }
                } else {
                    message = "未查询到申诉上传数据";
                }
            } else {
                message = "该还款数据已完成还款状态";
            }
        } else {
            message = "未查询到该还款数据";
        }

        return message;
    }

}