package cc.mrbird.febs.yb.service.impl;

import cc.mrbird.febs.common.domain.QueryRequest;
import cc.mrbird.febs.common.utils.SortUtil;
import cc.mrbird.febs.job.service.JobService;
import cc.mrbird.febs.yb.dao.YbReconsiderApplyDataMapper;
import cc.mrbird.febs.yb.entity.YbDefaultValue;
import cc.mrbird.febs.yb.entity.YbReconsiderApply;
import cc.mrbird.febs.yb.entity.YbReconsiderApplyData;
import cc.mrbird.febs.yb.service.IYbReconsiderApplyDataService;
import cc.mrbird.febs.yb.service.IYbReconsiderApplyService;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.UUID;

/**
 * <p>
 * 服务实现类
 * </p>
 *
 * @author viki
 * @since 2020-07-17
 */
@Slf4j
@Service("IYbReconsiderApplyDataService")
@Transactional(propagation = Propagation.SUPPORTS, readOnly = true, rollbackFor = Exception.class)
public class YbReconsiderApplyDataServiceImpl extends ServiceImpl<YbReconsiderApplyDataMapper, YbReconsiderApplyData> implements IYbReconsiderApplyDataService {

    @Autowired
    private IYbReconsiderApplyService iYbReconsiderApplyService;


    @Override
    public IPage<YbReconsiderApplyData> findYbReconsiderApplyDatas(QueryRequest request, YbReconsiderApplyData ybReconsiderApplyData) {
        try {
            LambdaQueryWrapper<YbReconsiderApplyData> queryWrapper = new LambdaQueryWrapper<>();
            queryWrapper.eq(YbReconsiderApplyData::getIsDeletemark, 1);//1是未删 0是已删


            Page<YbReconsiderApplyData> page = new Page<>();
            SortUtil.handlePageSort(request, page, false);//true 是属性  false是数据库字段可两个
            return this.page(page, queryWrapper);
        } catch (Exception e) {
            log.error("获取字典信息失败", e);
            return null;
        }
    }

    @Override
    public IPage<YbReconsiderApplyData> findYbReconsiderApplyDataList(QueryRequest request, YbReconsiderApplyData ybReconsiderApplyData) {
        try {
            Page<YbReconsiderApplyData> page = new Page<>();
            ybReconsiderApplyData.setIsDeletemark(1);
            SortUtil.handlePageSort(request, page, false);//true 是属性  false是数据库字段可两个
            return this.baseMapper.findYbReconsiderApplyData(page, ybReconsiderApplyData);
        } catch (Exception e) {
            log.error("获取失败", e);
            return null;
        }
    }

    @Override
    @Transactional
    public void createYbReconsiderApplyData(YbReconsiderApplyData ybReconsiderApplyData) {
        ybReconsiderApplyData.setCreateTime(new Date());
        if (ybReconsiderApplyData.getId() == null || "".equals(ybReconsiderApplyData.getId())) {
            ybReconsiderApplyData.setId(UUID.randomUUID().toString());
        }
        ybReconsiderApplyData.setIsDeletemark(1);
        this.save(ybReconsiderApplyData);
    }

    @Override
    @Transactional
    public void createBatchYbReconsiderApplyData(List<YbReconsiderApplyData> list, YbReconsiderApply ybReconsiderApply) {

        this.saveBatch(list);
//        for(YbReconsiderApplyData item : list)
//        {
//            this.save(item);
//        }
//        this.baseMapper.createBatchData(list);

        iYbReconsiderApplyService.updateYbReconsiderApply(ybReconsiderApply);
    }

    @Override
    @Transactional
    public void updateYbReconsiderApplyData(YbReconsiderApplyData ybReconsiderApplyData) {
        ybReconsiderApplyData.setModifyTime(new Date());
        this.baseMapper.updateYbReconsiderApplyData(ybReconsiderApplyData);
    }

    @Override
    @Transactional
    public void deleteYbReconsiderApplyDatas(String[] Ids) {
        List<String> list = Arrays.asList(Ids);
        this.baseMapper.deleteBatchIds(list);
    }

    @Override
    @Transactional
    public int deleteReconsiderApplyDataByPid(YbReconsiderApplyData ybReconsiderApplyData) {
        int count = 0;
        LambdaQueryWrapper<YbReconsiderApply> wrapperApply = new LambdaQueryWrapper<>();
        wrapperApply.eq(YbReconsiderApply::getId, ybReconsiderApplyData.getPid());
        List<YbReconsiderApply> applyList = iYbReconsiderApplyService.list(wrapperApply);
        if (applyList.size() > 0) {
            if (applyList.get(0).getState() == YbDefaultValue.APPLYSTATE_4 || applyList.get(0).getState() == YbDefaultValue.APPLYSTATE_2) {
                LambdaQueryWrapper<YbReconsiderApplyData> wrapper = new LambdaQueryWrapper<>();
                wrapper.eq(YbReconsiderApplyData::getPid, applyList.get(0).getId());
                wrapper.eq(YbReconsiderApplyData::getTypeno, ybReconsiderApplyData.getTypeno());
                this.baseMapper.delete(wrapper);


                int state = applyList.get(0).getState() == YbDefaultValue.APPLYSTATE_2 ? YbDefaultValue.APPLYSTATE_1 : YbDefaultValue.APPLYSTATE_3;
                YbReconsiderApply updateApply = new YbReconsiderApply();
                updateApply.setId(applyList.get(0).getId());
                updateApply.setState(state);
                iYbReconsiderApplyService.updateYbReconsiderApply(updateApply);
                count = 1;
            }
        }
        return count;
    }

    @Override
    public List<YbReconsiderApplyData> findReconsiderApplyDataByApplyDates(String applyDateStr, Integer dataType) {
        return this.baseMapper.findReconsiderApplyDataByApplyDate(applyDateStr, dataType);
    }

    @Override
    public List<YbReconsiderApplyData> findReconsiderApplyDataByNotVerifys(String applyDateStr, Integer dataType, Integer typeno) {
        return this.baseMapper.findReconsiderApplyDataByNotVerify(applyDateStr, dataType, typeno);
    }

    @Override
    @Transactional
    public void createBatchDatas(List<YbReconsiderApplyData> listReconsiderApplyData) {
        this.baseMapper.createBatchData(listReconsiderApplyData);
    }

    @Override
    @Transactional
    public void importReconsiderApply(YbReconsiderApply ybReconsiderApply, List<YbReconsiderApplyData> listData, List<YbReconsiderApplyData> listMain) {
        this.iYbReconsiderApplyService.updateYbReconsiderApply(ybReconsiderApply);
        if (listData.size() > 0) {
            this.saveBatch(listData);
        }
        if (listMain.size() > 0) {
            this.saveBatch(listMain);
        }
    }



}