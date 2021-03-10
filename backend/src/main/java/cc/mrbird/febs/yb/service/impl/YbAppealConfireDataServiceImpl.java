package cc.mrbird.febs.yb.service.impl;

import cc.mrbird.febs.common.domain.QueryRequest;
import cc.mrbird.febs.common.utils.SortUtil;
import cc.mrbird.febs.yb.entity.YbAppealConfireData;
import cc.mrbird.febs.yb.dao.YbAppealConfireDataMapper;
import cc.mrbird.febs.yb.entity.YbReconsiderApply;
import cc.mrbird.febs.yb.service.IYbAppealConfireDataService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;
import java.time.LocalDate;

/**
 * <p>
 * 服务实现类
 * </p>
 *
 * @author viki
 * @since 2021-01-11
 */
@Slf4j
@Service("IYbAppealConfireDataService")
@Transactional(propagation = Propagation.SUPPORTS, readOnly = true, rollbackFor = Exception.class)
public class YbAppealConfireDataServiceImpl extends ServiceImpl<YbAppealConfireDataMapper, YbAppealConfireData> implements IYbAppealConfireDataService {


    @Override
    public IPage<YbAppealConfireData> findYbAppealConfireDatas(QueryRequest request, YbAppealConfireData ybAppealConfireData) {
        try {
            LambdaQueryWrapper<YbAppealConfireData> queryWrapper = new LambdaQueryWrapper<>();
            queryWrapper.eq(YbAppealConfireData::getIsDeletemark, 1);//1是未删 0是已删
            if (ybAppealConfireData.getPid() != null) {
                queryWrapper.eq(YbAppealConfireData::getPid, ybAppealConfireData.getPid());
            }

            Page<YbAppealConfireData> page = new Page<>();
            SortUtil.handlePageSort(request, page, false);//true 是属性  false是数据库字段可两个
            return this.page(page, queryWrapper);
        } catch (Exception e) {
            log.error("获取字典信息失败", e);
            return null;
        }
    }

    @Override
    public IPage<YbAppealConfireData> findYbAppealConfireDataList(QueryRequest request, YbAppealConfireData ybAppealConfireData) {
        try {
            Page<YbAppealConfireData> page = new Page<>();
            SortUtil.handlePageSort(request, page, false);//true 是属性  false是数据库字段可两个
            return this.baseMapper.findYbAppealConfireData(page, ybAppealConfireData);
        } catch (Exception e) {
            log.error("获取失败", e);
            return null;
        }
    }

    @Override
    @Transactional
    public void createYbAppealConfireData(YbAppealConfireData ybAppealConfireData) {
        ybAppealConfireData.setId(UUID.randomUUID().toString());
        ybAppealConfireData.setCreateTime(new Date());
        ybAppealConfireData.setIsDeletemark(1);
        this.save(ybAppealConfireData);
    }

    @Override
    @Transactional
    public void updateYbAppealConfireData(YbAppealConfireData ybAppealConfireData) {
        ybAppealConfireData.setModifyTime(new Date());
        this.baseMapper.updateYbAppealConfireData(ybAppealConfireData);
    }

    @Override
    @Transactional
    public void deleteYbAppealConfireDatas(String[] Ids) {
        List<String> list = Arrays.asList(Ids);
        this.baseMapper.deleteBatchIds(list);
    }

    @Override
    @Transactional
    public void deleteAppealConfireDataPids(String[] pIds) {
        List<String> list = Arrays.asList(pIds);
        LambdaQueryWrapper<YbAppealConfireData> wrapper = new LambdaQueryWrapper<>();
        wrapper.in(YbAppealConfireData::getPid, list);
        this.baseMapper.delete(wrapper);
    }


    @Override
    public List<YbAppealConfireData> findAppealConfireDataByInDoctorCodeList(String doctorCode) {
        LambdaQueryWrapper<YbAppealConfireData> wrapper = new LambdaQueryWrapper<>();
        String sql = " IS_DELETEMARK = 1 and pid in (select id from  yb_appeal_confire where doctorCode = '" + doctorCode + "' and IS_DELETEMARK = 1)";
        wrapper.apply(sql);
        return this.list(wrapper);
    }
    @Override
    public List<YbAppealConfireData> findAppealConfireDataList(YbAppealConfireData appealConfireData) {
        LambdaQueryWrapper<YbAppealConfireData> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(YbAppealConfireData::getIsDeletemark,1);

        if(appealConfireData.getPid() !=null){
            wrapper.eq(YbAppealConfireData::getPid,appealConfireData.getPid());
        }

        if(appealConfireData.getDeptId() !=null){
            wrapper.eq(YbAppealConfireData::getDeptId,appealConfireData.getDeptId());
        }

        if(appealConfireData.getDeptName() !=null){
            wrapper.eq(YbAppealConfireData::getDeptName,appealConfireData.getDeptName());
        }

        return this.list(wrapper);
    }

}