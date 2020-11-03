package cc.mrbird.febs.yb.service.impl;

import cc.mrbird.febs.com.controller.DataTypeHelpers;
import cc.mrbird.febs.com.entity.ComConfiguremanage;
import cc.mrbird.febs.com.service.IComConfiguremanageService;
import cc.mrbird.febs.common.domain.QueryRequest;
import cc.mrbird.febs.common.utils.SortUtil;
import cc.mrbird.febs.yb.entity.*;
import cc.mrbird.febs.yb.dao.YbReconsiderApplyMapper;
import cc.mrbird.febs.yb.service.IYbAppealManageService;
import cc.mrbird.febs.yb.service.IYbAppealManageViewService;
import cc.mrbird.febs.yb.service.IYbReconsiderApplyService;
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

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.time.LocalDate;

/**
 * <p>
 * 服务实现类
 * </p>
 *
 * @author viki
 * @since 2020-07-23
 */
@Slf4j
@Service("IYbReconsiderApplyService")
@Transactional(propagation = Propagation.SUPPORTS, readOnly = true, rollbackFor = Exception.class)
public class YbReconsiderApplyServiceImpl extends ServiceImpl<YbReconsiderApplyMapper, YbReconsiderApply> implements IYbReconsiderApplyService {

    @Autowired
    private IYbAppealManageViewService iYbAppealManageViewService;
    @Autowired
    private IComConfiguremanageService iComConfiguremanageService;

    @Autowired
    private IYbAppealManageService iYbAppealManageService;

    @Override
    public IPage<YbReconsiderApply> findYbReconsiderApplys(QueryRequest request, YbReconsiderApply ybReconsiderApply) {
        try {
            LambdaQueryWrapper<YbReconsiderApply> queryWrapper = new LambdaQueryWrapper<>();
            queryWrapper.eq(YbReconsiderApply::getIsDeletemark, 1);//1是未删 0是已删

            if (StringUtils.isNotBlank(ybReconsiderApply.getCreateTimeFrom()) && StringUtils.isNotBlank(ybReconsiderApply.getCreateTimeTo())) {
                queryWrapper
                        .ge(YbReconsiderApply::getCreateTime, ybReconsiderApply.getCreateTimeFrom())
                        .le(YbReconsiderApply::getCreateTime, ybReconsiderApply.getCreateTimeTo());
            }

            Page<YbReconsiderApply> page = new Page<>();
            SortUtil.handlePageSort(request, page, false);//true 是属性  false是数据库字段可两个
            return this.page(page, queryWrapper);
        } catch (Exception e) {
            log.error("获取字典信息失败", e);
            return null;
        }
    }

    @Override
    public IPage<YbReconsiderApply> findYbReconsiderApplyList(QueryRequest request, YbReconsiderApply ybReconsiderApply) {
        try {
            Page<YbReconsiderApply> page = new Page<>();
            SortUtil.handlePageSort(request, page, false);//true 是属性  false是数据库字段可两个
            return this.baseMapper.findYbReconsiderApply(page, ybReconsiderApply);
        } catch (Exception e) {
            log.error("获取失败", e);
            return null;
        }
    }

    @Override
    @Transactional
    public void createYbReconsiderApply(YbReconsiderApply ybReconsiderApply) {
        ybReconsiderApply.setCreateTime(new Date());
        if (ybReconsiderApply.getId() == null || "".equals(ybReconsiderApply.getId())) {
            ybReconsiderApply.setId(UUID.randomUUID().toString());
        }
        ybReconsiderApply.setIsDeletemark(1);
        this.save(ybReconsiderApply);
    }

    @Override
    @Transactional
    public String createReconsiderApplyCheck(YbReconsiderApply ybReconsiderApply) {
        String message = "";
        LambdaQueryWrapper<YbReconsiderApply> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(YbReconsiderApply::getApplyDateStr, ybReconsiderApply.getApplyDateStr());
        List<YbReconsiderApply> list = this.list(wrapper);
        if (list.size() == 0) {
            ybReconsiderApply.setCreateTime(new Date());
            if (ybReconsiderApply.getId() == null || "".equals(ybReconsiderApply.getId())) {
                ybReconsiderApply.setId(UUID.randomUUID().toString());
            }
            ybReconsiderApply.setIsDeletemark(1);
            boolean bl = this.save(ybReconsiderApply);
            if (bl) {
                message = "ok";
            }
        } else {
            message = "该年月 " + ybReconsiderApply.getApplyDateStr() + " 已创建过复议申请记录";
        }
        return message;
    }

    @Override
    @Transactional
    public String updateYbReconsiderApply(YbReconsiderApply ybReconsiderApply, boolean isUpOverdue) throws ParseException {
        String msg = "ok";
        ybReconsiderApply.setModifyTime(new Date());
        ybReconsiderApply.setApplyDateStr(null);

        if (isUpOverdue) {
            msg = this.updateAppealManage(ybReconsiderApply);
            if (msg.equals("ok")) {
                this.baseMapper.updateYbReconsiderApply(ybReconsiderApply);
            }
        } else {
            this.baseMapper.updateYbReconsiderApply(ybReconsiderApply);
        }

        return msg;
    }

    @Override
    @Transactional
    public void updateYbReconsiderApply(YbReconsiderApply ybReconsiderApply) {
        ybReconsiderApply.setModifyTime(new Date());
        this.baseMapper.updateYbReconsiderApply(ybReconsiderApply);
    }

    @Override
    public void updateEnableOverdue(String applyDateStr) {
        if (applyDateStr == null || "".equals(applyDateStr)) {
            applyDateStr = this.getNianYue();
        }
        Map<String, Object> map = new HashMap<>();
        map.put("applyDateStr", applyDateStr);
        this.baseMapper.updateAppealManageEnableOverdue(map);
        String message = (String) map.get("message");
        log.info(message);
    }

    @Override
    public void updateApplyEndDateOne(String applyDateStr) {
        if (applyDateStr == null || "".equals(applyDateStr)) {
            applyDateStr = this.getNianYue();
        }
        Map<String, Object> map = new HashMap<>();
        map.put("applyDateStr", applyDateStr);
        this.baseMapper.updateAppealManageApplyEndDateOne(map);
        String message = (String) map.get("message");
        log.info(message);
    }

    @Override
    public void updateApplyEndDateTwo(String applyDateStr) {
        if (applyDateStr == null || "".equals(applyDateStr)) {
            applyDateStr = this.getNianYue();
        }
        Map<String, Object> map = new HashMap<>();
        map.put("applyDateStr", applyDateStr);
        this.baseMapper.updateAppealManageApplyEndDateTwo(map);
        String message = (String) map.get("message");
        log.info(message);
    }


    private String getNianYue() {
        Date date = new Date();//获取当前时间
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.MONTH, 0);
        calendar.set(Calendar.DAY_OF_MONTH, 1);
        Date getDate = calendar.getTime();
        SimpleDateFormat f = new SimpleDateFormat("yyyy-MM");
        String ny = f.format(getDate);
        return ny;
    }

    @Override
    @Transactional
    public void deleteYbReconsiderApplys(String[] Ids) {
        List<String> list = Arrays.asList(Ids);
        this.baseMapper.deleteBatchIds(list);
    }

    @Override
    @Transactional
    public void deleteBatchStateIdsYbReconsiderApplys(String[] Ids, Integer state) {
        List<String> listString = Arrays.asList(Ids);
        this.baseMapper.deleteBatchStateIdsYbReconsiderApply(listString, state);
    }

    @Override
    @Transactional
    public boolean updateYbReconsiderApplyState(String applyDateStr, Integer state) {
        boolean bl = false;
        LambdaQueryWrapper<YbReconsiderApply> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(YbReconsiderApply::getApplyDateStr, applyDateStr);
        List<YbReconsiderApply> list = this.list(wrapper);
        if (list.size() > 0) {
            YbReconsiderApply update = new YbReconsiderApply();
            update.setId(list.get(0).getId());
            update.setModifyTime(new Date());
            update.setState(state);
            int count = this.baseMapper.updateById(update);
            if (count > 0) {
                bl = true;
            }
        }

        return bl;
    }

    @Override
    public YbReconsiderApply findReconsiderApplyByApplyDateStrs(String appltDateStr) {
        return this.baseMapper.findReconsiderApplyByApplyDateStr(appltDateStr);
    }

    @Transactional
    public String updateAppealManage(YbReconsiderApply ybReconsiderApply) throws ParseException {
        String msg = "";
        YbReconsiderApply yra = this.getById(ybReconsiderApply.getId());
        int state = yra.getState();

        YbAppealManageView queryAppealManage = new YbAppealManageView();
        queryAppealManage.setAcceptState(YbDefaultValue.ACCEPTSTATE_7);
        queryAppealManage.setApplyDateStr(yra.getApplyDateStr());

        SimpleDateFormat f = new SimpleDateFormat("yyyy-MM-dd");
        Date aEndDateOne = null;
        Date bEndDateOne = null;
        boolean isTrue = false;
        if (state == YbDefaultValue.APPLYSTATE_3) {
            aEndDateOne = f.parse(f.format(yra.getEndDateOne()));
            bEndDateOne = f.parse(f.format(ybReconsiderApply.getEndDateOne()));
            queryAppealManage.setTypeno(YbDefaultValue.TYPENO_1);
            isTrue = true;
        }
        if (state == YbDefaultValue.APPLYSTATE_5) {
            aEndDateOne = f.parse(f.format(yra.getEndDateTwo()));
            bEndDateOne = f.parse(f.format(ybReconsiderApply.getEndDateTwo()));
            queryAppealManage.setTypeno(YbDefaultValue.TYPENO_2);
            isTrue = true;
        }

        if (isTrue) {
            List<YbAppealManageView> appealManageList = new ArrayList<>();
            List<YbAppealManage> updateAppealManageList = new ArrayList<>();
            if (aEndDateOne.before(bEndDateOne)) {
                appealManageList = iYbAppealManageViewService.findAppealManageViewList(queryAppealManage);
                updateAppealManageList = iYbAppealManageService.getUpdateAppealManageList(appealManageList, bEndDateOne);
                if (updateAppealManageList.size() > 0) {
                    isTrue = iYbAppealManageService.updateBatchById(updateAppealManageList);
                    if (isTrue) {
                        msg = "ok";
                    }
                }
            } else {
                msg = "date";
            }
        } else {
            msg = "nostate";
        }
        return msg;
    }


}