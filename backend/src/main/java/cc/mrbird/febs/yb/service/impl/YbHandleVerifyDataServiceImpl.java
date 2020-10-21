package cc.mrbird.febs.yb.service.impl;

import cc.mrbird.febs.com.controller.DataTypeHelpers;
import cc.mrbird.febs.com.entity.ComConfiguremanage;
import cc.mrbird.febs.com.service.IComConfiguremanageService;
import cc.mrbird.febs.common.domain.QueryRequest;
import cc.mrbird.febs.common.utils.SortUtil;
import cc.mrbird.febs.yb.entity.*;
import cc.mrbird.febs.yb.dao.YbHandleVerifyDataMapper;
import cc.mrbird.febs.yb.service.*;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.aspectj.lang.annotation.Around;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.time.LocalDate;

/**
 * <p>
 * 服务实现类
 * </p>
 *
 * @author viki
 * @since 2020-08-28
 */
@Slf4j
@Service("IYbHandleVerifyDataService")
@Transactional(propagation = Propagation.SUPPORTS, readOnly = true, rollbackFor = Exception.class)
public class YbHandleVerifyDataServiceImpl extends ServiceImpl<YbHandleVerifyDataMapper, YbHandleVerifyData> implements IYbHandleVerifyDataService {

    @Autowired
    IYbAppealResultService iYbAppealResultService;
    @Autowired
    IYbHandleVerifyService iYbHandleVerifyService;
    @Autowired
    IComConfiguremanageService iComConfiguremanageService;
    @Autowired
    IYbAppealManageService iYbAppealManageService;

    @Autowired
    IYbReconsiderApplyService iYbReconsiderApplyService;


    @Override
    public IPage<YbHandleVerifyData> findYbHandleVerifyDatas(QueryRequest request, YbHandleVerifyData ybHandleVerifyData) {
        try {
            LambdaQueryWrapper<YbHandleVerifyData> queryWrapper = new LambdaQueryWrapper<>();
            queryWrapper.eq(YbHandleVerifyData::getIsDeletemark, 1);//1是未删 0是已删


            Page<YbHandleVerifyData> page = new Page<>();
            SortUtil.handlePageSort(request, page, false);//true 是属性  false是数据库字段可两个
            return this.page(page, queryWrapper);
        } catch (Exception e) {
            log.error("获取字典信息失败", e);
            return null;
        }
    }

    @Override
    public IPage<YbHandleVerifyData> findYbHandleVerifyDataList(QueryRequest request, YbHandleVerifyData ybHandleVerifyData) {
        try {
            Page<YbHandleVerifyData> page = new Page<>();
            SortUtil.handlePageSort(request, page, false);//true 是属性  false是数据库字段可两个
            return this.baseMapper.findYbHandleVerifyData(page, ybHandleVerifyData);
        } catch (Exception e) {
            log.error("获取失败", e);
            return null;
        }
    }

    @Override
    @Transactional
    public void createYbHandleVerifyData(YbHandleVerifyData ybHandleVerifyData) {
        ybHandleVerifyData.setId(UUID.randomUUID().toString());
        ybHandleVerifyData.setCreateTime(new Date());
        ybHandleVerifyData.setIsDeletemark(1);
        this.save(ybHandleVerifyData);
    }

    @Override
    @Transactional
    public void updateYbHandleVerifyData(YbHandleVerifyData ybHandleVerifyData) {
        ybHandleVerifyData.setModifyTime(new Date());
        this.baseMapper.updateYbHandleVerifyData(ybHandleVerifyData);
    }

    @Override
    @Transactional
    public void deleteYbHandleVerifyDatas(String[] Ids) {
        List<String> list = Arrays.asList(Ids);
        this.baseMapper.deleteBatchIds(list);
    }

    //获取state=2 已剔除数据
    @Override
    @Transactional
    public void importCreateHandleVerifyData(String applyDateStr, Long uid, String uname) {
        YbReconsiderApply ybReconsiderApply = this.iYbReconsiderApplyService.findReconsiderApplyByApplyDateStrs(applyDateStr);
        if (ybReconsiderApply != null) {
            if (ybReconsiderApply.getResetState() == 1) {
                Date thisDate = new java.sql.Timestamp(new Date().getTime());
                List<YbAppealResult> appealResultList = this.iYbAppealResultService.findAppealResulDataHandles(applyDateStr);
                if (appealResultList.size() > 0) {
                    LambdaQueryWrapper<YbHandleVerify> wrapper = new LambdaQueryWrapper<YbHandleVerify>();
                    wrapper.eq(YbHandleVerify::getApplyDateStr, applyDateStr);
                    List<YbHandleVerify> handleVerifyList = this.iYbHandleVerifyService.list(wrapper);
                    List<YbHandleVerifyData> insertHandleDataList = new ArrayList<YbHandleVerifyData>();
                    String guid = UUID.randomUUID().toString();

                    if (handleVerifyList.size() == 0) {
                        YbHandleVerify insert = new YbHandleVerify();
                        insert.setId(guid);
                        insert.setApplyDateStr(applyDateStr);
                        insert.setIsDeletemark(1);
                        insert.setCreateTime(thisDate);
                        insert.setCreateUserId(uid);
                        this.iYbHandleVerifyService.save(insert);
                    } else {
                        guid = handleVerifyList.get(0).getId();
                    }

                    for (YbAppealResult item : appealResultList) {
                        YbHandleVerifyData insertData = new YbHandleVerifyData();
                        insertData.setId(UUID.randomUUID().toString());
                        insertData.setPid(guid);
                        insertData.setState(0);
                        insertData.setIsDeletemark(1);
                        insertData.setApplyDataId(item.getApplyDataId());
                        insertData.setVerifyId(item.getVerifyId());
                        insertData.setManageId(item.getManageId());
                        insertData.setResultId(item.getId());
                        insertData.setResetId(item.getResetDataId());
                        insertData.setDataType(item.getDataType());
                        insertData.setDeptCode(item.getDeptCode());
                        insertData.setDeptName(item.getDeptName());
                        insertData.setDoctorCode(item.getDoctorCode());
                        insertData.setDoctorName(item.getDoctorName());
                        insertData.setMatchPersonId(uid);
                        insertData.setMatchPersonName(uname);
                        insertData.setMatchDate(thisDate);
                        insertHandleDataList.add(insertData);
                    }
                    this.saveBatch(insertHandleDataList);
                }
            }
        }

    }

    private  int getDay(){
        List<Integer> intList = new ArrayList<>();
        intList.add(1);//日期增加天数
        List<ComConfiguremanage> configList = iComConfiguremanageService.getConfigLists(intList);
        return configList.size() > 0 ? configList.get(0).getIntField() : 2;
    }

    @Override
    @Transactional
    public void updateSendStates(List<YbHandleVerifyData> list, Long uId, String Uname) {
        Date thisDate = new java.sql.Timestamp(new Date().getTime());
        int day = getDay();
        List<YbHandleVerifyData> updateHandleVerifyList = new ArrayList<YbHandleVerifyData>();
        List<YbAppealManage> appealManageList = new ArrayList<YbAppealManage>();
        Date addDate = DataTypeHelpers.addDateMethod(thisDate, day);
        for (YbHandleVerifyData ybHandleVerifyData : list) {
            //更新
            YbHandleVerifyData updateHandleVerify = new YbHandleVerifyData();
            updateHandleVerify.setId(ybHandleVerifyData.getId());
            updateHandleVerify.setState(3);
            updateHandleVerify.setModifyUserId(uId);
            updateHandleVerify.setModifyTime(thisDate);
            updateHandleVerify.setSendPersonId(uId);
            updateHandleVerify.setSendPersonName(Uname);
            updateHandleVerify.setSendDate(thisDate);
            updateHandleVerify.setOperateDate(thisDate);
            //插入申诉管理
            YbAppealManage ybAppealManage = new YbAppealManage();
            ybAppealManage.setId(UUID.randomUUID().toString());
            ybAppealManage.setVerifyId(ybHandleVerifyData.getId());
            ybAppealManage.setVerifySendId(ybHandleVerifyData.getVerifyId());
            ybAppealManage.setApplyDataId(ybHandleVerifyData.getApplyDataId());
            ybAppealManage.setReadyDeptCode(ybHandleVerifyData.getDeptCode());
            ybAppealManage.setReadyDeptName(ybHandleVerifyData.getDeptName());
            ybAppealManage.setReadyDoctorCode(ybHandleVerifyData.getDoctorCode());
            ybAppealManage.setReadyDoctorName(ybHandleVerifyData.getDoctorName());
            ybAppealManage.setOperateDate(thisDate);
            ybAppealManage.setOperateProcess("发送操作-待申诉");
            ybAppealManage.setEnableDate(addDate);

            ybAppealManage.setIsDeletemark(1);
            ybAppealManage.setCreateUserId(uId);
            ybAppealManage.setCreateTime(thisDate);

            ybAppealManage.setSourceType(1);
            ybAppealManage.setAcceptState(1);
            ybAppealManage.setDataType(ybHandleVerifyData.getDataType());

            updateHandleVerifyList.add(updateHandleVerify);
            appealManageList.add(ybAppealManage);
        }

        if (appealManageList.size() > 0) {
            this.updateBatchById(updateHandleVerifyList);
            this.iYbAppealManageService.saveBatch(appealManageList);
        }
    }


}