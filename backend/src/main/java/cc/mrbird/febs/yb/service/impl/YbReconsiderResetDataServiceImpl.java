package cc.mrbird.febs.yb.service.impl;

import cc.mrbird.febs.common.domain.QueryRequest;
import cc.mrbird.febs.common.utils.SortUtil;
import cc.mrbird.febs.yb.dao.YbReconsiderApplyMapper;
import cc.mrbird.febs.yb.entity.*;
import cc.mrbird.febs.yb.dao.YbReconsiderResetDataMapper;
import cc.mrbird.febs.yb.service.IYbAppealResultService;
import cc.mrbird.febs.yb.service.IYbReconsiderApplyDataService;
import cc.mrbird.febs.yb.service.IYbReconsiderResetDataService;
import cc.mrbird.febs.yb.service.IYbReconsiderResetDataViewService;
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
    public IYbAppealResultService iYbAppealResultService;

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
        ybReconsiderResetData.setCreateTime(new Date());
        ybReconsiderResetData.setIsDeletemark(1);
        this.save(ybReconsiderResetData);
    }

    @Override
    @Transactional
    public void updateYbReconsiderResetData(YbReconsiderResetData ybReconsiderResetData) {
        ybReconsiderResetData.setModifyTime(new Date());
        this.baseMapper.updateYbReconsiderResetData(ybReconsiderResetData);
    }

    @Override
    @Transactional
    public void deleteYbReconsiderResetDatas(String[] Ids) {
        List<String> list = Arrays.asList(Ids);
        this.baseMapper.deleteBatchIds(list);
    }

    @Override
    public List<YbReconsiderResetData> findReconsiderResetByApplyDates(String applyDateStr, Integer dataType) {
        return  this.baseMapper.findReconsiderResetByApplyDate(applyDateStr,dataType);
    }

    @Override
    @Transactional
    public String updateHandleResetDatas(String resultId,String resetId, Long uid, String uname) {
        String message = "";
        YbReconsiderResetData resetData =  this.baseMapper.selectById(resetId);
        if(resetData!=null){
            if(resetData.getSeekState() == 0){
                YbAppealResult appealResult = this.iYbAppealResultService.getById(resultId);
                if(appealResult!=null){
                    if(appealResult.getResetDataId()==null){
                        YbReconsiderResetData updateResetData = new YbReconsiderResetData();
                        updateResetData.setId(resetData.getId());
                        updateResetData.setSeekState(1);
                        updateResetData.setState(0);

                        YbAppealResult updateResult = new YbAppealResult();
                        updateResult.setId(appealResult.getId());
                        updateResult.setResetDataId(resetData.getId());
                        updateResult.setResetDate(new Date());
                        updateResult.setResetPersonId(uid);
                        updateResult.setResetPersonName(uname);
                        updateResult.setState(2);

                        int count = this.baseMapper.updateById(updateResetData);
                        if(count>0) {
                            Boolean isTrue = this.iYbAppealResultService.updateById(updateResult);
                            if(isTrue){
                                message = "ok";
                            }else{
                                message = "该申诉上传数据剔除数据更新失败";
                            }
                        }else{
                            message = "该剔除数据剔除状态更新失败";
                        }

                    }else{
                        message = "该申诉上传数据已完成剔除更新";
                    }
                }else{
                    message = "未查询到申诉上传数据";
                }
            }else{
                message = "该剔除数据已完成剔除状态";
            }
        }else {
            message = "未查询到该剔除数据";
        }

        return  message;
    }

    @Override
    @Transactional
    public String updateResetDatas(String applyDateStr, Long uid, String uname, Integer dataType) {
        String message = "";
        LambdaQueryWrapper<YbReconsiderResetDataView> queryWrapperRdv = new LambdaQueryWrapper<>();
        queryWrapperRdv.eq(YbReconsiderResetDataView::getApplyDateStr, applyDateStr);
        queryWrapperRdv.eq(YbReconsiderResetDataView::getDataType, dataType);
        queryWrapperRdv.eq(YbReconsiderResetDataView::getSeekState, 0);
        List<YbReconsiderResetDataView> resetDataList = this.iYbReconsiderResetDataViewService.list(queryWrapperRdv);
        if (resetDataList.size() > 0) {
            List<YbReconsiderApplyData> applyDataList = this.iYbReconsiderApplyDataService.findReconsiderApplyDataByApplyDates(applyDateStr, dataType);
            if (applyDataList.size() > 0) {
                List<YbAppealResult> resultList = this.iYbAppealResultService.findAppealResulDataByResets(applyDateStr, dataType);
                if (resultList.size() == 0) {
                    message = "result0";
                }
                List<YbAppealResult> updateResultList = new ArrayList<YbAppealResult>();
                List<YbReconsiderResetData> updateResetDataList = new ArrayList<YbReconsiderResetData>();
                List<YbReconsiderApplyData> searchApplyDataList = new ArrayList<YbReconsiderApplyData>();
                List<YbAppealResult> searchResultList = new ArrayList<YbAppealResult>();
                for (YbReconsiderResetDataView rdv : resetDataList) {
                    YbReconsiderResetData updateResetData = new YbReconsiderResetData();
                    updateResetData.setId(rdv.getId());

                    //DataType 0 明细扣款 1 主单扣款
                    if (rdv.getDataType() == 0) {
                        searchApplyDataList = applyDataList.stream().filter(
                                s -> s.getSerialNo().equals(rdv.getSerialNo()) &&
                                        s.getBillNo().equals(rdv.getBillNo()) &&
                                        s.getProjectCode().equals(rdv.getProjectCode()) &&
                                        s.getProjectName().equals(rdv.getProjectName()) &&
                                        s.getRuleName().equals(rdv.getRuleName())
                        ).collect(Collectors.toList());
                    } else {
                        searchApplyDataList = applyDataList.stream().filter(
                                s -> s.getSerialNo().equals(rdv.getSerialNo()) &&
                                        s.getBillNo().equals(rdv.getBillNo()) &&
                                        s.getPersonalNo().equals(rdv.getPersonalNo()) &&
                                        s.getRuleName().equals(rdv.getRuleName())
                        ).collect(Collectors.toList());
                    }

                    if (searchApplyDataList.size() > 0) {
                        if (searchApplyDataList.size() == 1) {
                            if (resultList.size() > 0) {
                                YbReconsiderApplyData rAd = searchApplyDataList.get(0);
                                searchResultList = resultList.stream().filter(
                                        s -> s.getApplyDataId().equals(rAd.getId())
                                ).collect(Collectors.toList());

                                if (searchResultList.size() == 1) {
                                    YbAppealResult updateResult = new YbAppealResult();
                                    updateResult.setId(searchResultList.get(0).getId());
                                    updateResult.setState(2);
                                    updateResult.setResetDataId(rdv.getId());
                                    updateResult.setResetDate(new Date());
                                    updateResult.setResetPersonId(uid);
                                    updateResult.setResetPersonName(uname);

                                    updateResetData.setSeekState(1);

                                    updateResetDataList.add(updateResetData);
                                    updateResultList.add(updateResult);
                                }
                            }
                        } else {
                            updateResetData.setState(1);
                            updateResetDataList.add(updateResetData);
                        }
                    } else {
                        updateResetData.setState(2);
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
                    if("".equals(message)) {
                        message = "update0";
                    }
                }
            } else {
                message = "未找到复议审核数据";
            }
        } else {
            message = "未找到剔除数据";
        }

        return message;
    }


}