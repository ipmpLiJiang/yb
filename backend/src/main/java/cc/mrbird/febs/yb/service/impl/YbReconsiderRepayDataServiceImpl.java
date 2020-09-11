package cc.mrbird.febs.yb.service.impl;

import cc.mrbird.febs.common.domain.QueryRequest;
import cc.mrbird.febs.common.utils.SortUtil;
import cc.mrbird.febs.yb.entity.*;
import cc.mrbird.febs.yb.dao.YbReconsiderRepayDataMapper;
import cc.mrbird.febs.yb.service.IYbAppealResultService;
import cc.mrbird.febs.yb.service.IYbReconsiderApplyDataService;
import cc.mrbird.febs.yb.service.IYbReconsiderRepayDataService;
import cc.mrbird.febs.yb.service.IYbReconsiderResetDataService;
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

    @Override
    public IPage<YbReconsiderRepayData> findYbReconsiderRepayDatas(QueryRequest request, YbReconsiderRepayData ybReconsiderRepayData) {
        try {
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

    public String updateRepayDatas(YbReconsiderRepayData ybReconsiderRepayData, Long uid, String uname) {
        String message = "";
        LambdaQueryWrapper<YbReconsiderRepayData> queryWrapperRay = new LambdaQueryWrapper<>();
        queryWrapperRay.eq(YbReconsiderRepayData::getPid, ybReconsiderRepayData.getPid());
        queryWrapperRay.eq(YbReconsiderRepayData::getDataType, ybReconsiderRepayData.getDataType());
        queryWrapperRay.eq(YbReconsiderRepayData::getRepayType, ybReconsiderRepayData.getRepayType());
        queryWrapperRay.eq(YbReconsiderRepayData::getSeekState, ybReconsiderRepayData.getSeekState());
        queryWrapperRay.eq(YbReconsiderRepayData::getState, ybReconsiderRepayData.getState());
        queryWrapperRay.eq(YbReconsiderRepayData::getBelongDateStr, ybReconsiderRepayData.getBelongDateStr());
        int warnType = 0;
        List<YbReconsiderRepayData> repayDataList = this.list(queryWrapperRay);
        if (repayDataList.size() > 0) {
            List<YbReconsiderResetData> resetDataList = this.iYbReconsiderResetDataService.findReconsiderResetByApplyDates(ybReconsiderRepayData.getBelongDateStr(), ybReconsiderRepayData.getDataType());
            if (resetDataList.size() > 0) {
                List<YbAppealResult> resultList = this.iYbAppealResultService.findAppealResulDataByRepays(ybReconsiderRepayData.getBelongDateStr(), ybReconsiderRepayData.getDataType());
                if (resultList.size() == 0) {
                    message = "result0";
                }
                List<YbAppealResult> updateResultList = new ArrayList<YbAppealResult>();
                List<YbReconsiderRepayData> updateRepayDataList = new ArrayList<YbReconsiderRepayData>();
                List<YbReconsiderResetData> searchResetDataList = null;
                List<YbAppealResult> searchResultList = new ArrayList<YbAppealResult>();
                for (YbReconsiderRepayData rrd : repayDataList) {
                    YbReconsiderRepayData updateRepayData = new YbReconsiderRepayData();
                    updateRepayData.setId(rrd.getId());

                    searchResetDataList = new ArrayList<YbReconsiderResetData>();
                    if (rrd.getOrderNumber() != null && !"".equals(rrd.getOrderNumber())) {
                        searchResetDataList = resetDataList.stream().filter(
                                s -> s.getOrderNumber().equals(rrd.getOrderNumber())
                        ).collect(Collectors.toList());

                        //按序号匹配一个或多个
                        if (searchResetDataList.size() > 0) {
                            if (searchResetDataList.size() == 1) {
                                warnType = 1;//序号匹配一个 暂时 state = 0 上传
                            } else {
                                warnType = 5;//序号匹配多个 暂时 state = 1 一对多
                            }
                        }
                    }

                    if (rrd.getOrderNumber() == null || "".equals(rrd.getOrderNumber()) || searchResetDataList.size() == 0) {
                        // dataType 0 明细扣款 1主单扣款
                        // repayType 0 居保 1 职保
                        if (rrd.getRepayType() == 0) {
                            if (rrd.getDataType() == 0) {
                                searchResetDataList = resetDataList.stream().filter(
                                        s -> s.getSerialNo().equals(rrd.getSerialNo()) &&
                                                s.getBillNo().equals(rrd.getBillNo()) &&
                                                s.getProjectCode().equals(rrd.getProjectCode()) &&
                                                s.getProjectName().equals(rrd.getProjectName()) &&
                                                s.getRuleName().equals(rrd.getRuleName())
                                ).collect(Collectors.toList());
                            } else {
                                searchResetDataList = resetDataList.stream().filter(
                                        s -> s.getBillNo().equals(rrd.getBillNo())
                                ).collect(Collectors.toList());
                            }
                        } else {
                            if (rrd.getDataType() == 0) {
                                searchResetDataList = resetDataList.stream().filter(
                                        s -> s.getBillNo().equals(rrd.getBillNo()) &&
                                                s.getProjectName().equals(rrd.getProjectName())
                                ).collect(Collectors.toList());
                            } else {
                                searchResetDataList = resetDataList.stream().filter(
                                        s -> s.getBillNo().equals(rrd.getBillNo())
                                ).collect(Collectors.toList());
                            }
                        }
                        //序号和字段都无法匹配
                        if (searchResetDataList.size() == 0) {
                            warnType = 4; //序号和字段都无匹配 暂时 state = 2 未知
                        } else {
                            if (rrd.getOrderNumber() != null && !"".equals(rrd.getOrderNumber())) {
                                if (searchResetDataList.size() > 0) {
                                    warnType = 3; //有序号 没有匹配到, 字段匹配到了 暂时 state = 1 一对多(单个或多个 待确认还款)
                                }
                            } else {
                                if (searchResetDataList.size() == 1) {
                                    warnType = 2;//无序号 字段匹配到一个 暂时 state = 0 上传
                                } else {
                                    warnType = 6;//无序号 字段匹配到多个 暂时 state = 1 一对多
                                }
                            }
                        }
                    }

                    if (searchResetDataList.size() > 0) {
                        if (searchResetDataList.size() == 1 && (warnType == 1 || warnType == 2)) {
                            if (resultList.size() > 0) {
                                YbReconsiderResetData rAd = searchResetDataList.get(0);
                                searchResultList = resultList.stream().filter(
                                        s -> s.getResetDataId().equals(rAd.getId())
                                ).collect(Collectors.toList());
                                if (searchResultList.size() == 1) {
                                    YbAppealResult updateResult = new YbAppealResult();
                                    updateResult.setId(searchResultList.get(0).getId());
                                    updateResult.setRepayDataId(rrd.getId());
                                    updateResult.setRepayDate(new Date());
                                    updateResult.setRepayPersonId(uid);
                                    updateResult.setRepayPersonName(uname);

                                    updateRepayData.setWarnType(warnType);
                                    updateRepayData.setSeekState(1);

                                    updateRepayDataList.add(updateRepayData);
                                    updateResultList.add(updateResult);
                                }
                            }
                        } else {
                            //3 有序号 没有匹配到, 字段匹配到了 暂时 state = 1 一对多(单个或多个 待确认还款)
                            //6 无序号 字段匹配到多个 暂时 state = 1 一对多
                            if (warnType == 3 || warnType == 6) {
                                String orderNumberNews = "";
                                for (YbReconsiderResetData rad3 : searchResetDataList) {
                                    if ("".equals(orderNumberNews)) {
                                        orderNumberNews = rad3.getOrderNumber();
                                    } else {
                                        orderNumberNews = "," + rad3.getOrderNumber();
                                    }
                                }
                                updateRepayData.setOrderNumberNew(orderNumberNews);
                            }
                            updateRepayData.setWarnType(warnType);
                            updateRepayData.setState(1);
                            updateRepayDataList.add(updateRepayData);
                        }
                    } else {
                        updateRepayData.setWarnType(warnType);
                        updateRepayData.setState(2);
                        updateRepayDataList.add(updateRepayData);
                    }
                }

                if (updateResultList.size() > 0) {
                    this.iYbAppealResultService.updateBatchById(updateResultList);
                }
                if (updateRepayDataList.size() > 0) {
                    this.updateBatchById(updateRepayDataList);
                }
                if (updateResultList.size() == 0 && updateRepayDataList.size() == 0) {
                    if ("".equals(message)) {
                        message = "update0";
                    }
                }
            } else {
                message = "未找到复议审核数据";
            }
        } else {
            message = "未找到还款数据";
        }

        return message;
    }

    @Override
    @Transactional
    public String updateHandleRepayDatas(String resultId, String repayId, Long uid, String uname) {
        String message = "";
        YbReconsiderRepayData repayData = this.baseMapper.selectById(repayId);
        if (repayData != null) {
            if (repayData.getSeekState() == 0) {
                YbAppealResult appealResult = this.iYbAppealResultService.getById(resultId);
                if (appealResult != null) {
                    if (appealResult.getRepayDataId() == null) {
                        YbReconsiderRepayData updateRepayData = new YbReconsiderRepayData();
                        updateRepayData.setId(repayData.getId());
                        updateRepayData.setSeekState(1);
                        updateRepayData.setState(0);

                        YbAppealResult updateResult = new YbAppealResult();
                        updateResult.setId(appealResult.getId());
                        updateResult.setRepayDataId(repayData.getId());
                        updateResult.setRepayDate(new Date());
                        updateResult.setRepayPersonId(uid);
                        updateResult.setRepayPersonName(uname);

                        int count = this.baseMapper.updateById(updateRepayData);
                        if (count > 0) {
                            Boolean isTrue = this.iYbAppealResultService.updateById(updateResult);
                            if (isTrue) {
                                message = "ok";
                            } else {
                                message = "该申诉上传数据还款数据更新失败";
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