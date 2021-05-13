package cc.mrbird.febs.yb.service.impl;

import cc.mrbird.febs.common.domain.QueryRequest;
import cc.mrbird.febs.common.utils.SortUtil;
import cc.mrbird.febs.yb.dao.YbReconsiderResetDataMapper;
import cc.mrbird.febs.yb.entity.*;
import cc.mrbird.febs.yb.service.*;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
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
 * @since 2020-08-17
 */
@Slf4j
@Service("IYbReconsiderResetDataService")
@Transactional(propagation = Propagation.SUPPORTS, readOnly = true, rollbackFor = Exception.class)
public class YbReconsiderResetDataServiceImpl extends ServiceImpl<YbReconsiderResetDataMapper, YbReconsiderResetData> implements IYbReconsiderResetDataService {

    @Autowired
    public IYbReconsiderApplyDataService iYbReconsiderApplyDataService;

    @Autowired
    public IYbReconsiderResetDataViewService iYbReconsiderResetDataViewService;

    @Autowired
    public IYbReconsiderResetService iYbReconsiderResetService;

    @Autowired
    public IYbAppealResultService iYbAppealResultService;

    @Autowired
    public IYbAppealManageService iYbAppealManageService;

    @Autowired
    public IYbReconsiderVerifyService iYbReconsiderVerifyService;

    @Override
    public IPage<YbReconsiderResetData> findYbReconsiderResetDatas(QueryRequest request, YbReconsiderResetData ybReconsiderResetData) {
        try {
            LambdaQueryWrapper<YbReconsiderResetData> queryWrapper = new LambdaQueryWrapper<>();
            queryWrapper.eq(YbReconsiderResetData::getIsDeletemark, 1);//1是未删 0是已删


            Page<YbReconsiderResetData> page = new Page<>();
            SortUtil.handlePageSort(request, page, false);//true 是属性  false是数据库字段可两个
            return this.page(page, queryWrapper);
        } catch (Exception e) {
            log.error("获取字典信息失败", e);
            return null;
        }
    }

    @Override
    public IPage<YbReconsiderResetData> findYbReconsiderResetDataList(QueryRequest request, YbReconsiderResetData ybReconsiderResetData) {
        try {
            Page<YbReconsiderResetData> page = new Page<>();
            SortUtil.handlePageSort(request, page, false);//true 是属性  false是数据库字段可两个
            return this.baseMapper.findYbReconsiderResetData(page, ybReconsiderResetData);
        } catch (Exception e) {
            log.error("获取失败", e);
            return null;
        }
    }

    @Override
    @Transactional
    public void createYbReconsiderResetData(YbReconsiderResetData ybReconsiderResetData) {
        if (ybReconsiderResetData.getId() == null || "".equals(ybReconsiderResetData.getId())) {
            ybReconsiderResetData.setId(UUID.randomUUID().toString());
        }
        ybReconsiderResetData.setIsDeletemark(1);
        this.save(ybReconsiderResetData);
    }

    @Override
    @Transactional
    public void updateYbReconsiderResetData(YbReconsiderResetData ybReconsiderResetData) {
        this.baseMapper.updateYbReconsiderResetData(ybReconsiderResetData);
    }

    @Override
    @Transactional
    public void deleteYbReconsiderResetDatas(String[] Ids) {
        List<String> list = Arrays.asList(Ids);
        this.baseMapper.deleteBatchIds(list);
    }

    @Override
    public List<YbReconsiderResetData> findReconsiderResetDataByApplyDates(String applyDateStr, Integer areaType, Integer dataType) {
        List<YbReconsiderResetData> list = new ArrayList<>();
        YbReconsiderReset reconsiderReset = iYbReconsiderResetService.findReconsiderResetByApplyDateStr(applyDateStr,areaType);
        if (reconsiderReset != null) {
            LambdaQueryWrapper<YbReconsiderResetData> wrapper = new LambdaQueryWrapper<>();
            wrapper.eq(YbReconsiderResetData::getPid,reconsiderReset.getId());
            if(dataType !=null){
                wrapper.eq(YbReconsiderResetData::getDataType,dataType);
            }
            list = this.list(wrapper);
        }
        return  list;
    }

    @Override
    public List<YbReconsiderResetData> findResetNotExistsRepayByApplyDates(String applyDateStr, Integer areaType, Integer dataType) {
        return this.baseMapper.findResetNotExistsRepayByApplyDate(applyDateStr,areaType, dataType);
    }

    @Override
    public String updateHandleResetCancelData(String resetId, String applyDateStr,Integer areaType) {
        String message = "";
        YbReconsiderReset reconsiderReset = iYbReconsiderResetService.findReconsiderResetByApplyDateStr(applyDateStr,areaType);
        if (reconsiderReset != null) {
            if (reconsiderReset.getState() == 0) {
                LambdaQueryWrapper<YbReconsiderResetData> wrapperReset = new LambdaQueryWrapper<>();
                wrapperReset.eq(YbReconsiderResetData::getId, resetId);
                List<YbReconsiderResetData> resetDataList = this.list(wrapperReset);
                if (resetDataList.size() > 0) {
                    YbReconsiderResetData resetData = resetDataList.get(0);
                    wrapperReset = new LambdaQueryWrapper<>();
                    wrapperReset.eq(YbReconsiderResetData::getRelatelDataId, resetData.getRelatelDataId());
                    resetDataList = this.list(wrapperReset);
                    if (resetData.getSeekState() == YbDefaultValue.SEEKSTATE_1) {
                        LambdaQueryWrapper<YbAppealResult> wrapperResult = new LambdaQueryWrapper<>();
                        wrapperResult.eq(YbAppealResult::getApplyDateStr, applyDateStr);
                        wrapperResult.eq(YbAppealResult::getAreaType, areaType);
                        wrapperResult.eq(YbAppealResult::getRelatelDataId, resetData.getRelatelDataId());
                        wrapperResult.eq(YbAppealResult::getIsDeletemark, 1);
                        List<YbAppealResult> resultList = this.iYbAppealResultService.list(wrapperResult);
                        if (resetDataList.size() > 0 && resultList.size() > 0) {
                            int count = 0;
                            count = this.baseMapper.updateReconsiderResetCancelData(resetDataList);

                            if (count > 0) {
                                count = this.iYbAppealResultService.updatAppealResultCancelData(resultList);
                                if (count > 0) {
                                    message = "ok";
                                } else {
                                    message = "该剔除数据取消失败.";
                                }
                            } else {
                                message = "该剔除数据取消失败.";
                            }

                        } else {
                            message = "未找到该剔除数据的复议数据.";
                        }
                    } else {
                        message = "该剔除数据已取消成功.";
                    }


                } else {
                    message = "未找到该剔除数据.";
                }
            } else {
                message = "该年月 " + reconsiderReset.getApplyDateStr() + " 已完成剔除，无法取消剔除.";
            }
        } else {
            message = "该年月 " + reconsiderReset.getApplyDateStr() + " 无法取消剔除.";
        }

        return message;
    }

    @Override
    @Transactional
    public String updateHandleResetDatas(String resultIds, String resetIds, Long uid, String uname) {
        String message = "";
        LambdaQueryWrapper<YbReconsiderResetData> wrapperReset = new LambdaQueryWrapper<>();
        String[] resetIdArr = resetIds.split(",");
        wrapperReset.in(YbReconsiderResetData::getId, resetIdArr);
        List<YbReconsiderResetData> resetDataList = this.list(wrapperReset);
        if (resetDataList.size() > 0) {
            LambdaQueryWrapper<YbAppealResult> wrapperResult = new LambdaQueryWrapper<>();
            String[] resultIdArr = resultIds.split(",");
            wrapperResult.in(YbAppealResult::getId, resultIdArr);
            List<YbAppealResult> resultDataList = this.iYbAppealResultService.list(wrapperResult);
            if (resultDataList.size() > 0) {
                String relatelDataId = UUID.randomUUID().toString();
//                if (appealResult.getState() == YbDefaultValue.RESULTSTATE_1) {
                List<YbReconsiderResetData> updateResetList = new ArrayList<>();
                List<YbAppealResult> updateResultList = new ArrayList<>();
                for (YbReconsiderResetData resetData : resetDataList) {
                    if (resetData.getSeekState() == YbDefaultValue.SEEKSTATE_0) {
                        YbReconsiderResetData updateResetData = new YbReconsiderResetData();
                        updateResetData.setId(resetData.getId());
                        updateResetData.setSeekState(YbDefaultValue.SEEKSTATE_1);
                        updateResetData.setState(YbDefaultValue.RESETDATA_STATE_0);

                        updateResetData.setResetType(YbDefaultValue.RESETTYPE_2);
                        updateResetData.setRelatelDataId(relatelDataId);
                        updateResetData.setResetPersonId(uid);
                        updateResetData.setResetPersonName(uname);
//                            updateResetData.setResultId(appealResult.getId());
//                            updateResetData.setApplyDataId(appealResult.getApplyDataId());
//                            updateResetData.setApplyOrderNumber(appealResult.getOrderNumber());
                        updateResetData.setResetDate(new Date());
                        updateResetList.add(updateResetData);
                    }
                }
//                    if (updateResetList.size() > 0) {
                for (YbAppealResult result : resultDataList) {
                    YbAppealResult updateResult = new YbAppealResult();
                    updateResult.setId(result.getId());
                    updateResult.setResetDate(new Date());
                    updateResult.setRelatelDataId(relatelDataId);
                    updateResult.setRepayState(YbDefaultValue.RESULTREPAYSTATE_2);
                    updateResult.setState(YbDefaultValue.RESULTSTATE_2);
                    updateResultList.add(updateResult);
                }
                Boolean isTrue = this.updateBatchById(updateResetList);
                if (isTrue) {
                    isTrue = this.iYbAppealResultService.updateBatchById(updateResultList);
                    if (isTrue) {
                        message = "ok";
                    }
                }
                if (!isTrue) {
                    message = "该申诉上传数据剔除数据更新失败";
                }
//                    } else {
//                        message = "未查询到有效的剔除数据";
//                    }
//                } else {
//                    message = "该申诉上传数据已完成剔除更新";
//                }
            } else {
                message = "未查询到申诉上传数据";
            }
        } else {
            message = "未查询到剔除数据";
        }

        return message;
    }

    @Override
    @Transactional
    public String updateResetDatas(String applyDateStr,Integer areaType, Long uid, String uname, Integer dataType) {
        String message = "";
        int rvCheckCount = iYbReconsiderVerifyService.findReconsiderVerifyResetCheckCounts(applyDateStr,areaType);
        if (rvCheckCount == 0) {
            int amCheckCount = iYbAppealManageService.findAppealManageResetCheckCounts(applyDateStr,areaType);
            if (amCheckCount == 0) {
                YbReconsiderResetDataView queryRrd = new YbReconsiderResetDataView();
                queryRrd.setApplyDateStr(applyDateStr);
                queryRrd.setAreaType(areaType);
                queryRrd.setDataType(dataType);
                queryRrd.setSeekState(YbDefaultValue.SEEKSTATE_0);
                List<YbReconsiderResetDataView> resetDataList = this.iYbReconsiderResetDataViewService.findYbReconsiderResetDataList(queryRrd);
                if (resetDataList.size() > 0) {
                    List<YbReconsiderApplyData> applyDataList = this.iYbReconsiderApplyDataService.findReconsiderApplyDataByApplyDates(applyDateStr,areaType, dataType);
                    if (applyDataList.size() > 0) {
                        List<YbAppealResult> resultList = this.iYbAppealResultService.findAppealResulDataByResets(applyDateStr,areaType, dataType);
                        if (resultList.size() == 0) {
                            message = "result0";
                        } else {
                            List<YbAppealResult> updateResultList = new ArrayList<>();
                            List<YbReconsiderResetData> updateResetDataList = new ArrayList<>();
                            List<YbReconsiderApplyData> queryApplyDataList = new ArrayList<>();
                            List<YbAppealResult> queryResultList = new ArrayList<>();
                            List<YbReconsiderResetDataView> queryResetDataList = new ArrayList<>();

                            for (YbReconsiderResetDataView rdv : resetDataList) {
                                YbReconsiderResetData updateResetData = new YbReconsiderResetData();
                                updateResetData.setId(rdv.getId());
                                String relatelDataId = UUID.randomUUID().toString();

                                //DataType 0 明细扣款 1 主单扣款
                                if (rdv.getDataType() == 0) {
                                    queryApplyDataList = applyDataList.stream().filter(
                                            s -> s.getSerialNo().equals(rdv.getSerialNo()) &&
                                                    s.getBillNo().equals(rdv.getBillNo()) &&
                                                    s.getProjectCode().equals(rdv.getProjectCode()) &&
                                                    s.getProjectName().equals(rdv.getProjectName())
//                                                s.getRuleName().equals(rdv.getRuleName())
                                    ).collect(Collectors.toList());

                                    queryResetDataList = resetDataList.stream().filter(
                                            s -> s.getSerialNo().equals(rdv.getSerialNo()) &&
                                                    s.getBillNo().equals(rdv.getBillNo()) &&
                                                    s.getProjectCode().equals(rdv.getProjectCode()) &&
                                                    s.getProjectName().equals(rdv.getProjectName())
//                                                s.getRuleName().equals(rdv.getRuleName())
                                    ).collect(Collectors.toList());
                                } else {
                                    queryApplyDataList = applyDataList.stream().filter(
                                            s -> s.getSerialNo().equals(rdv.getSerialNo()) &&
                                                    s.getBillNo().equals(rdv.getBillNo()) &&
                                                    s.getPersonalNo().equals(rdv.getPersonalNo())
//                                                    s.getRuleName().equals(rdv.getRuleName())
                                    ).collect(Collectors.toList());

                                    queryResetDataList = resetDataList.stream().filter(
                                            s -> s.getSerialNo().equals(rdv.getSerialNo()) &&
                                                    s.getBillNo().equals(rdv.getBillNo()) &&
                                                    s.getPersonalNo().equals(rdv.getPersonalNo())
//                                                    s.getRuleName().equals(rdv.getRuleName())
                                    ).collect(Collectors.toList());
                                }

                                if (queryApplyDataList.size() > 0) {
                                    if (queryApplyDataList.size() == 1 && queryResetDataList.size() == 1) {
                                        YbReconsiderApplyData rAd = queryApplyDataList.get(0);
                                        queryResultList = resultList.stream().filter(
                                                s -> s.getApplyDataId().equals(rAd.getId())
                                        ).collect(Collectors.toList());

                                        if (queryResultList.size() == 1) {
                                            YbAppealResult appealResult = queryResultList.get(0);

                                            YbAppealResult updateResult = new YbAppealResult();
                                            updateResult.setId(appealResult.getId());
                                            updateResult.setState(YbDefaultValue.RESULTSTATE_2);
                                            updateResult.setRelatelDataId(relatelDataId);
//                                            updateResult.setResetDataId(rdv.getId());
                                            updateResult.setResetDate(new Date());
//                                            updateResult.setResetPersonId(uid);
//                                            updateResult.setResetPersonName(uname);
                                            updateResult.setRepayState(YbDefaultValue.RESULTREPAYSTATE_2);

                                            updateResetData.setSeekState(YbDefaultValue.SEEKSTATE_1);
                                            updateResetData.setResetType(YbDefaultValue.RESETTYPE_1);//自动剔除
                                            updateResetData.setRelatelDataId(relatelDataId);
                                            updateResetData.setResetPersonId(uid);
                                            updateResetData.setResetPersonName(uname);
//                                            updateResetData.setResultId(appealResult.getId());
//                                            updateResetData.setApplyDataId(appealResult.getApplyDataId());
//                                            updateResetData.setApplyOrderNumber(appealResult.getOrderNumber());
                                            updateResetData.setResetDate(new Date());

                                            updateResetDataList.add(updateResetData);
                                            updateResultList.add(updateResult);
                                        } else {
                                            updateResetData.setState(YbDefaultValue.RESETDATA_STATE_2);
                                            updateResetDataList.add(updateResetData);
                                        }
                                    } else {
                                        updateResetData.setState(YbDefaultValue.RESETDATA_STATE_1);
                                        updateResetDataList.add(updateResetData);
                                    }
                                } else {
                                    updateResetData.setState(YbDefaultValue.RESETDATA_STATE_2);
                                    updateResetDataList.add(updateResetData);
                                }
                            }

                            if (updateResultList.size() > 0) {
                                this.iYbAppealResultService.updateBatchById(updateResultList);
                            }
                            if (updateResetDataList.size() > 0) {
                                this.updateBatchById(updateResetDataList);
                            }
                            if (updateResultList.size() == 0 && updateResetDataList.size() == 0) {
                                if ("".equals(message)) {
                                    message = "update0";
                                }
                            }
                        }
                    } else {
                        message = "未找到复议审核数据";
                    }
                } else {
                    message = "未找到剔除数据";
                }
            } else {
                message = "申诉管理" + applyDateStr + "有未处理的复议数据.";
            }
        } else {
            message = "核对申请" + applyDateStr + "有未完成发送的审核数据.";
        }

        return message;
    }


}