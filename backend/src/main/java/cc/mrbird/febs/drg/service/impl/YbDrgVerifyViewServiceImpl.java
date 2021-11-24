package cc.mrbird.febs.drg.service.impl;

import cc.mrbird.febs.common.domain.QueryRequest;
import cc.mrbird.febs.common.utils.SortUtil;
import cc.mrbird.febs.drg.dao.YbDrgVerifyViewMapper;
import cc.mrbird.febs.drg.entity.YbDrgVerifyView;
import cc.mrbird.febs.drg.entity.YbDrgVerify;
import cc.mrbird.febs.drg.entity.YbDrgApply;
import cc.mrbird.febs.drg.entity.YbDrgApplyData;
import cc.mrbird.febs.drg.service.IYbDrgApplyDataService;
import cc.mrbird.febs.drg.service.IYbDrgApplyService;
import cc.mrbird.febs.drg.service.IYbDrgVerifyService;
import cc.mrbird.febs.drg.service.IYbDrgVerifyViewService;
import cc.mrbird.febs.yb.service.IYbPersonService;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

/**
 * findYbDrgVerifyViews
 * <p>
 * VIEW 服务实现类
 * </p>
 *
 * @author viki
 * @since 2020-07-30
 */
@Slf4j
@Service("IYbDrgVerifyViewService")
@Transactional(propagation = Propagation.SUPPORTS, readOnly = true, rollbackFor = Exception.class)
public class YbDrgVerifyViewServiceImpl extends ServiceImpl<YbDrgVerifyViewMapper, YbDrgVerifyView> implements IYbDrgVerifyViewService {

    @Autowired
    IYbDrgApplyService iYbDrgApplyService;

    @Autowired
    IYbDrgApplyDataService iYbDrgApplyDataService;

    @Autowired
    IYbPersonService iYbPersonService;

    @Autowired
    IYbDrgVerifyService iYbDrgVerifyService;

    @Override
    public IPage<YbDrgVerifyView> findYbDrgVerifyViews(QueryRequest request, YbDrgVerifyView ybDrgVerifyView) {
        try {
            Page<YbDrgVerifyView> page = new Page<>();
            YbDrgApply drgApply = iYbDrgApplyService.findDrgApplyByApplyDateStrs(ybDrgVerifyView.getApplyDateStr(), ybDrgVerifyView.getAreaType());
            if (drgApply != null) {
                ybDrgVerifyView.setPid(drgApply.getId());
                int count = this.baseMapper.findDrgVerifyCount(ybDrgVerifyView);
                if (count > 0) {
                    page.setSearchCount(false);
                    SortUtil.handlePageSort(request, page, false);//true 是属性  false是数据库字段可两个
                    IPage<YbDrgVerifyView> pg = this.baseMapper.findDrgVerifyView(page, ybDrgVerifyView);
                    pg.setTotal(count);
                    return pg;
                } else {
                    return page;
                }
            } else {
                return page;
            }
        } catch (Exception e) {
            log.error("获取字典信息失败", e);
            return null;
        }
    }

    @Override
    public List<YbDrgVerifyView> findDrgVerifyViewLists(YbDrgVerifyView ybDrgVerifyView) {
        return this.baseMapper.findDrgVerifyViewList(ybDrgVerifyView);
    }

    @Override
    public IPage<YbDrgVerifyView> findDrgVerifyViewNew(QueryRequest request, YbDrgVerifyView ybDrgVerifyView) {
        try {
            String applyDateStr = ybDrgVerifyView.getApplyDateStr();
            Integer areaType = ybDrgVerifyView.getAreaType();
            YbDrgApply drgApply = iYbDrgApplyService.findDrgApplyByApplyDateStrs(applyDateStr, areaType);
            Page<YbDrgVerifyView> page = new Page<>();
            if (drgApply != null) {
                List<YbDrgApplyData> applyDataList = iYbDrgApplyDataService.getApplyDatas(drgApply, ybDrgVerifyView);
                List<YbDrgVerifyView> list = new ArrayList<>();
                List<YbDrgVerify> verifyList = new ArrayList<>();

                LambdaQueryWrapper<YbDrgVerify> queryWrapper = new LambdaQueryWrapper<>();
                queryWrapper.eq(YbDrgVerify::getApplyDateStr, applyDateStr);
                queryWrapper.eq(YbDrgVerify::getAreaType, areaType);

                if (ybDrgVerifyView.getState() != null) {
                    queryWrapper.eq(YbDrgVerify::getState, ybDrgVerifyView.getState());
                }
                if (ybDrgVerifyView.getOrderNumber() != null) {
                    queryWrapper.eq(YbDrgVerify::getOrderNumber, ybDrgVerifyView.getOrderNumber());
                }
                if (ybDrgVerifyView.getVerifyDoctorCode() != null) {
                    queryWrapper.eq(YbDrgVerify::getVerifyDoctorCode, ybDrgVerifyView.getVerifyDoctorCode());
                }
                if (ybDrgVerifyView.getVerifyDoctorName() != null) {
                    queryWrapper.eq(YbDrgVerify::getVerifyDoctorName, ybDrgVerifyView.getVerifyDoctorName());
                }
                if (ybDrgVerifyView.getVerifyDeptName() != null) {
                    queryWrapper.eq(YbDrgVerify::getVerifyDeptName, ybDrgVerifyView.getVerifyDeptName());
                }
                verifyList = iYbDrgVerifyService.list(queryWrapper);

                if (verifyList.size() > 0 && applyDataList.size() > 0) {
                    if (verifyList.size() > applyDataList.size()) {
                        List<YbDrgVerify> queryList = new ArrayList<>();
                        for (YbDrgApplyData item : applyDataList) {
                            queryList = verifyList.stream().filter(s -> s.getApplyDataId().equals(item.getId())).collect(Collectors.toList());
                            if (queryList.size() > 0) {
                                YbDrgVerifyView arv = this.getYbDrgVerifyView(queryList.get(0), item);
                                list.add(arv);
                            }
                        }
                    } else {
                        for (YbDrgVerify item : verifyList) {
                            List<YbDrgApplyData> queryList = new ArrayList<>();
                            queryList = applyDataList.stream().filter(s -> s.getId().equals(item.getApplyDataId())).collect(Collectors.toList());
                            if (queryList.size() > 0) {
                                YbDrgVerifyView arv = this.getYbDrgVerifyView(item, queryList.get(0));
                                list.add(arv);
                            }
                        }
                    }
                }
                if (list.size() > 0) {
                    page.setSearchCount(false);
                    SortUtil.handlePageSort(request, page, false);//true 是属性  false是数据库字段可两个
                    page.setTotal(list.size());
                    long current = page.getCurrent() == 1 ? 0 : (page.getCurrent() - 1) * page.getSize();
                    list = list.stream().sorted(Comparator.comparing(YbDrgVerifyView::getOrderNum)).skip(current).limit(page.getSize()).collect(Collectors.toList());
                    page.setRecords(list);
                }
            }
            return page;

        } catch (Exception e) {
            log.error("获取字典信息失败", e);
            return null;
        }
    }

    private YbDrgVerifyView getYbDrgVerifyView(YbDrgVerify verify, YbDrgApplyData drgApplyData) {
        YbDrgVerifyView drgVerifyView = new YbDrgVerifyView();
        drgVerifyView.setPid(drgApplyData.getPid());
        drgVerifyView.setKs(drgApplyData.getKs());
        drgVerifyView.setJzjlh(drgApplyData.getJzjlh());
        drgVerifyView.setBah(drgApplyData.getBah());
        drgVerifyView.setWglx(drgApplyData.getWglx());
        drgVerifyView.setWtms(drgApplyData.getWtms());
        drgVerifyView.setYlzfy(drgApplyData.getYlzfy());
        drgVerifyView.setWgje(drgApplyData.getWgje());
        drgVerifyView.setSfbmzczjcw(drgApplyData.getSfbmzczjcw());
        drgVerifyView.setLy(drgApplyData.getLy());
        drgVerifyView.setApplyDateStr(verify.getApplyDateStr());
        drgVerifyView.setOrderNumber(drgApplyData.getOrderNumber());
        drgVerifyView.setOrderNum(drgApplyData.getOrderNum());
        drgVerifyView.setId(verify.getId());
        drgVerifyView.setApplyDataId(drgApplyData.getId());
        drgVerifyView.setVerifyDoctorCode(verify.getVerifyDoctorCode());
        drgVerifyView.setVerifyDoctorName(verify.getVerifyDoctorName());
        drgVerifyView.setVerifyDeptCode(verify.getVerifyDeptCode());
        drgVerifyView.setVerifyDeptName(verify.getVerifyDeptName());

        drgVerifyView.setState(verify.getState());
        drgVerifyView.setSendDate(verify.getSendDate());
        drgVerifyView.setSendPersonId(verify.getSendPersonId());
        drgVerifyView.setSendPersonName(verify.getSendPersonName());
        drgVerifyView.setAreaType(verify.getAreaType());
        return drgVerifyView;
    }


    @Override
    public int findDrgVerifyApplyDateCounts(YbDrgVerifyView ybDrgVerifyView) {
        return this.baseMapper.findDrgVerifyApplyDateCount(ybDrgVerifyView);
    }

}