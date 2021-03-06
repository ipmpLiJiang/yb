package cc.mrbird.febs.yb.service.impl;

import cc.mrbird.febs.common.domain.QueryRequest;
import cc.mrbird.febs.common.utils.SortUtil;
import cc.mrbird.febs.yb.entity.YbAppealResultDeductimplement;
import cc.mrbird.febs.yb.dao.YbAppealResultDeductimplementMapper;
import cc.mrbird.febs.yb.entity.YbReconsiderResetDataView;
import cc.mrbird.febs.yb.entity.YbReconsiderResetDeductimplement;
import cc.mrbird.febs.yb.entity.YbReconsiderResetResultView;
import cc.mrbird.febs.yb.service.IYbAppealResultDeductimplementService;
import cc.mrbird.febs.yb.service.IYbReconsiderResetDataViewService;
import cc.mrbird.febs.yb.service.IYbReconsiderResetResultViewService;
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
 * @since 2020-09-24
 */
@Slf4j
@Service("IYbAppealResultDeductimplementService")
@Transactional(propagation = Propagation.SUPPORTS, readOnly = true, rollbackFor = Exception.class)
public class YbAppealResultDeductimplementServiceImpl extends ServiceImpl<YbAppealResultDeductimplementMapper, YbAppealResultDeductimplement> implements IYbAppealResultDeductimplementService {

    @Autowired
    private IYbReconsiderResetDataViewService iYbReconsiderResetDataViewService;

    @Override
    public IPage<YbAppealResultDeductimplement> findYbAppealResultDeductimplements(QueryRequest request, YbAppealResultDeductimplement ybAppealResultDeductimplement) {
        try {
            LambdaQueryWrapper<YbAppealResultDeductimplement> queryWrapper = new LambdaQueryWrapper<>();
//            queryWrapper.eq(YbAppealResultDeductimplement::getIsDeletemark, 1);//1是未删 0是已删


            Page<YbAppealResultDeductimplement> page = new Page<>();
            SortUtil.handlePageSort(request, page, false);//true 是属性  false是数据库字段可两个
            return this.page(page, queryWrapper);
        } catch (Exception e) {
            log.error("获取字典信息失败", e);
            return null;
        }
    }

    @Override
    public IPage<YbAppealResultDeductimplement> findYbAppealResultDeductimplementList(QueryRequest request, YbAppealResultDeductimplement ybAppealResultDeductimplement) {
        try {
            Page<YbAppealResultDeductimplement> page = new Page<>();
            SortUtil.handlePageSort(request, page, false);//true 是属性  false是数据库字段可两个
            return this.baseMapper.findYbAppealResultDeductimplement(page, ybAppealResultDeductimplement);
        } catch (Exception e) {
            log.error("获取失败", e);
            return null;
        }
    }

    @Override
    @Transactional
    public void createYbAppealResultDeductimplement(YbAppealResultDeductimplement ybAppealResultDeductimplement) {
//        ybAppealResultDeductimplement.setCreateTime(new Date());
        if (ybAppealResultDeductimplement.getId() == null || "".equals(ybAppealResultDeductimplement.getId())) {
            ybAppealResultDeductimplement.setId(UUID.randomUUID().toString());
        }
//        ybAppealResultDeductimplement.setIsDeletemark(1);
        this.save(ybAppealResultDeductimplement);
    }

    @Override
    @Transactional
    public String createAppealResultDeductimplement(YbAppealResultDeductimplement ybAppealResultDeductimplement) {
        LambdaQueryWrapper<YbAppealResultDeductimplement> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(YbAppealResultDeductimplement::getRelatelDataId, ybAppealResultDeductimplement.getRelatelDataId());
        wrapper.eq(YbAppealResultDeductimplement::getResetDataId, ybAppealResultDeductimplement.getResetDataId());
        List<YbAppealResultDeductimplement> list = this.list(wrapper);
        if (list.size() == 0) {
//            ybAppealResultDeductimplement.setCreateTime(new Date());
            if (ybAppealResultDeductimplement.getId() == null || "".equals(ybAppealResultDeductimplement.getId())) {
                ybAppealResultDeductimplement.setId(UUID.randomUUID().toString());
            }
//            ybAppealResultDeductimplement.setIsDeletemark(1);
            boolean bl = this.save(ybAppealResultDeductimplement);
            if (bl) {
                return "ok";
            } else {
                return "n1";
            }
        } else {
            return "n2";
        }
    }

    @Override
    @Transactional
    public void importCreateAppealResultDeductimplement(YbAppealResultDeductimplement ybAppealResultDeductimplement, List<YbReconsiderResetDeductimplement> listResetDeductimplement) {
        String applyDateStr = ybAppealResultDeductimplement.getApplyDateStr();
        YbReconsiderResetDataView ybReconsiderResetDataView = new YbReconsiderResetDataView();
        ybReconsiderResetDataView.setApplyDateStr(applyDateStr);
        ybReconsiderResetDataView.setAreaType(ybAppealResultDeductimplement.getAreaType());
        List<YbReconsiderResetDataView> resetDataViewList = this.iYbReconsiderResetDataViewService.findYbReconsiderResetDataList(ybReconsiderResetDataView);
        List<YbAppealResultDeductimplement> createList = new ArrayList<>();
        List<YbReconsiderResetDataView> queryList = new ArrayList<>();

        LambdaQueryWrapper<YbAppealResultDeductimplement> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(YbAppealResultDeductimplement::getApplyDateStr, applyDateStr);
        wrapper.eq(YbAppealResultDeductimplement::getAreaType, ybAppealResultDeductimplement.getAreaType());
//        wrapper.eq(YbAppealResultDeductimplement::getIsDeletemark, 1);
        List<YbAppealResultDeductimplement> ardList = this.list(wrapper);

        Date applyDate = ybAppealResultDeductimplement.getApplyDate();
//        Long userId = ybAppealResultDeductimplement.getCreateUserId();

        for (YbReconsiderResetDeductimplement item : listResetDeductimplement) {
            queryList = resetDataViewList.stream().filter(
                    s -> s.getOrderNumber().equals(item.getOrderNumber()) &&
                            s.getDataType().equals(item.getDataType())
            ).collect(Collectors.toList());
            if (queryList.size() > 0) {
                YbReconsiderResetDataView rrr = queryList.get(0);
                if (ardList.stream().filter(
                        s -> s.getApplyDateStr().equals(applyDateStr) &&
                                s.getRelatelDataId().equals(rrr.getRelatelDataId()) &&
                                s.getResetDataId().equals(rrr.getId()) &&
                                s.getDataType().equals(rrr.getDataType())
                ).count() == 0) {
                    YbAppealResultDeductimplement createDeductimplement = new YbAppealResultDeductimplement();
                    createDeductimplement.setId(UUID.randomUUID().toString());
                    createDeductimplement.setApplyDate(applyDate);
                    createDeductimplement.setApplyDateStr(applyDateStr);
                    createDeductimplement.setAreaType(ybAppealResultDeductimplement.getAreaType());
                    createDeductimplement.setRelatelDataId(rrr.getRelatelDataId());
                    createDeductimplement.setResetDataId(rrr.getId());
                    createDeductimplement.setImplementDate(item.getImplementDate());
                    createDeductimplement.setImplementDateStr(item.getImplementDateStr());
                    createDeductimplement.setOrderNum(rrr.getOrderNum());
                    createDeductimplement.setOrderNumber(rrr.getOrderNumber());
                    if(item.getShareProgramme()!=null && !item.getShareProgramme().equals("")) {
                        createDeductimplement.setShareProgramme(item.getShareProgramme());
                    }
                    createDeductimplement.setShareState(item.getShareState());
                    createDeductimplement.setDataType(item.getDataType());

//                    createDeductimplement.setCreateUserId(userId);
//                    createDeductimplement.setCreateTime(new Date());
//                    createDeductimplement.setIsDeletemark(1);
                    createList.add(createDeductimplement);
                }
            }
        }

        if (createList.size() > 0) {
            this.saveBatch(createList);
        }
    }

    @Override
    @Transactional
    public void updateYbAppealResultDeductimplement(YbAppealResultDeductimplement ybAppealResultDeductimplement) {
//        ybAppealResultDeductimplement.setModifyTime(new Date());
        this.baseMapper.updateYbAppealResultDeductimplement(ybAppealResultDeductimplement);
    }

    @Override
    @Transactional
    public void deleteYbAppealResultDeductimplements(String[] Ids) {
        List<String> list = Arrays.asList(Ids);
        this.baseMapper.deleteBatchIds(list);
    }
    @Override
    public  List<YbAppealResultDeductimplement> findAppealResultDeductimplement(List<String> listStr,Integer areaType,Integer dataType){
        List<YbAppealResultDeductimplement> resultDeductimplementList = new ArrayList<>();
        if(listStr.size()>0){
            LambdaQueryWrapper<YbAppealResultDeductimplement> wrapper = new LambdaQueryWrapper<>();
            if(listStr.size() == 1) {
                wrapper.eq(YbAppealResultDeductimplement::getApplyDateStr,listStr.get(0));
            }else{
                wrapper.in(YbAppealResultDeductimplement::getApplyDateStr,listStr);
            }
            if(areaType!=null){
                wrapper.eq(YbAppealResultDeductimplement::getAreaType,areaType);
            }

            if(dataType!=null){
                wrapper.eq(YbAppealResultDeductimplement::getDataType,dataType);
            }
            resultDeductimplementList = this.list(wrapper);
        }
        return resultDeductimplementList;
    }

}