package cc.mrbird.febs.drg.service.impl;

import cc.mrbird.febs.com.entity.ComConfiguremanage;
import cc.mrbird.febs.common.domain.QueryRequest;
import cc.mrbird.febs.common.utils.SortUtil;
import cc.mrbird.febs.drg.entity.YbDrgApply;
import cc.mrbird.febs.drg.entity.YbDrgApplyData;
import cc.mrbird.febs.drg.dao.YbDrgApplyDataMapper;
import cc.mrbird.febs.drg.entity.YbDrgVerifyView;
import cc.mrbird.febs.drg.service.IYbDrgApplyDataService;
import cc.mrbird.febs.drg.service.IYbDrgApplyService;
import cc.mrbird.febs.yb.entity.YbDefaultValue;
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
 * @since 2021-11-23
 */
@Slf4j
@Service("IYbDrgApplyDataService")
@Transactional(propagation = Propagation.SUPPORTS, readOnly = true, rollbackFor = Exception.class)
public class YbDrgApplyDataServiceImpl extends ServiceImpl<YbDrgApplyDataMapper, YbDrgApplyData> implements IYbDrgApplyDataService {

    @Autowired
    private IYbDrgApplyService iYbDrgApplyService;

    @Override
    public IPage<YbDrgApplyData> findYbDrgApplyDatas(QueryRequest request, YbDrgApplyData ybDrgApplyData) {
        try {
            LambdaQueryWrapper<YbDrgApplyData> queryWrapper = new LambdaQueryWrapper<>();
//            queryWrapper.eq(YbDrgApplyData::getIsDeletemark, 1);//1是未删 0是已删


            Page<YbDrgApplyData> page = new Page<>();
            SortUtil.handlePageSort(request, page, false);//true 是属性  false是数据库字段可两个
            return this.page(page, queryWrapper);
        } catch (Exception e) {
            log.error("获取字典信息失败", e);
            return null;
        }
    }

    @Override
    public IPage<YbDrgApplyData> findYbDrgApplyDataList(QueryRequest request, YbDrgApplyData ybDrgApplyData) {
        try {
            Page<YbDrgApplyData> page = new Page<>();
            SortUtil.handlePageSort(request, page, false);//true 是属性  false是数据库字段可两个
            return this.baseMapper.findYbDrgApplyData(page, ybDrgApplyData);
        } catch (Exception e) {
            log.error("获取失败", e);
            return null;
        }
    }

    @Override
    @Transactional
    public void createYbDrgApplyData(YbDrgApplyData ybDrgApplyData) {
        ybDrgApplyData.setId(UUID.randomUUID().toString());
//        ybDrgApplyData.setCreateTime(new Date());
//        ybDrgApplyData.setIsDeletemark(1);
        this.save(ybDrgApplyData);
    }

    @Override
    @Transactional
    public void updateYbDrgApplyData(YbDrgApplyData ybDrgApplyData) {
//        ybDrgApplyData.setModifyTime(new Date());
        this.baseMapper.updateYbDrgApplyData(ybDrgApplyData);
    }

    @Override
    @Transactional
    public void deleteYbDrgApplyDatas(String[] Ids) {
        List<String> list = Arrays.asList(Ids);
        this.baseMapper.deleteBatchIds(list);
    }

    @Override
    @Transactional
    public int deleteDrgApplyDataByPid(YbDrgApplyData ybDrgApplyData) {
        int count = 0;
        LambdaQueryWrapper<YbDrgApply> wrapperApply = new LambdaQueryWrapper<>();
        wrapperApply.eq(YbDrgApply::getId, ybDrgApplyData.getPid());
        List<YbDrgApply> applyList = iYbDrgApplyService.list(wrapperApply);
        if (applyList.size() > 0) {
            if (applyList.get(0).getState() == YbDefaultValue.APPLYSTATE_2) {
                LambdaQueryWrapper<YbDrgApplyData> wrapper = new LambdaQueryWrapper<>();
                wrapper.eq(YbDrgApplyData::getPid, applyList.get(0).getId());
                this.baseMapper.delete(wrapper);

                YbDrgApply updateApply = new YbDrgApply();
                updateApply.setId(applyList.get(0).getId());
                updateApply.setState(YbDefaultValue.APPLYSTATE_1);
                iYbDrgApplyService.updateYbDrgApply(updateApply);
                count = 1;
            }
        }
        return count;
    }


    @Override
    @Transactional
    public void importDrgApply(YbDrgApply ybDrgApply, List<YbDrgApplyData> listData) {
//        List<YbDrgApplyData> createDataList = new ArrayList<>();
//        for (YbDrgApplyData item : listData) {
//            YbDrgApplyData rrData = new YbDrgApplyData();
//            rrData.setId(item.getId());
//            rrData.setPid(item.getPid());
//            rrData.setOrderNumber(item.getOrderNumber());//序号
//            rrData.setOrderNum(item.getOrderNum());//排序
//            rrData.setKs(item.getKs())
//        }
        if (listData.size() > 0) {
            this.saveBatch(listData);
        }

        this.iYbDrgApplyService.updateYbDrgApply(ybDrgApply);
    }

    @Override
    public List<YbDrgApplyData> getApplyDatas(YbDrgApply ybDrgApply, YbDrgVerifyView ybDrgVerifyView) {
        String k = ybDrgVerifyView.getKs();
        String j = ybDrgVerifyView.getJzjlh();
        String p = ybDrgVerifyView.getBah();
        String o = ybDrgVerifyView.getOrderNumber();

        LambdaQueryWrapper<YbDrgApplyData> wrapperApplyData = new LambdaQueryWrapper<>();
        wrapperApplyData.eq(YbDrgApplyData::getPid,ybDrgApply.getId());
        if(k != null && !k.equals("")) {
            wrapperApplyData.eq(YbDrgApplyData::getKs,k);
        }
        if(j != null && !j.equals("")) {
            wrapperApplyData.eq(YbDrgApplyData::getJzjlh,j);
        }
        if(p != null && !p.equals("")) {
            wrapperApplyData.eq(YbDrgApplyData::getBah,p);
        }
        if(o != null && !o.equals("")) {
            wrapperApplyData.eq(YbDrgApplyData::getOrderNumber,o);
        }

        return this.list(wrapperApplyData);
    }


    @Override
    public List<YbDrgApplyData> findDrgApplyDataByNotVerifys(String pid, String applyDateStr, Integer areaType) {
        return this.baseMapper.findDrgApplyDataByNotVerify(pid, applyDateStr, areaType);
    }


}