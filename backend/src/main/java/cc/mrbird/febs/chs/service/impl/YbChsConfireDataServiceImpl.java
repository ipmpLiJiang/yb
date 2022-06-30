package cc.mrbird.febs.chs.service.impl;

import cc.mrbird.febs.common.domain.QueryRequest;
import cc.mrbird.febs.common.utils.SortUtil;
import cc.mrbird.febs.chs.entity.YbChsConfireData;
import cc.mrbird.febs.chs.dao.YbChsConfireDataMapper;
import cc.mrbird.febs.chs.service.IYbChsConfireDataService;
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
 * @since 2022-06-30
 */
@Slf4j
@Service("IYbChsConfireDataService")
@Transactional(propagation = Propagation.SUPPORTS, readOnly = true, rollbackFor = Exception.class)
public class YbChsConfireDataServiceImpl extends ServiceImpl<YbChsConfireDataMapper, YbChsConfireData> implements IYbChsConfireDataService {


    @Override
    public IPage<YbChsConfireData> findYbChsConfireDatas(QueryRequest request, YbChsConfireData ybChsConfireData) {
        try {
            LambdaQueryWrapper<YbChsConfireData> queryWrapper = new LambdaQueryWrapper<>();
//        queryWrapper.eq(YbChsConfireData::getIsDeletemark, 1);//1是未删 0是已删
            if (ybChsConfireData.getPid() != null) {
                queryWrapper.eq(YbChsConfireData::getPid, ybChsConfireData.getPid());
            }

            Page<YbChsConfireData> page = new Page<>();
            SortUtil.handlePageSort(request, page, false);//true 是属性  false是数据库字段可两个
            return this.page(page, queryWrapper);
        } catch (Exception e) {
            log.error("获取字典信息失败", e);
            return null;
        }
    }

    @Override
    public IPage<YbChsConfireData> findYbChsConfireDataList(QueryRequest request, YbChsConfireData ybChsConfireData) {
        try {
            Page<YbChsConfireData> page = new Page<>();
            SortUtil.handlePageSort(request, page, false);//true 是属性  false是数据库字段可两个
            return this.baseMapper.findYbChsConfireData(page, ybChsConfireData);
        } catch (Exception e) {
            log.error("获取失败", e);
            return null;
        }
    }

    @Override
    @Transactional
    public void createYbChsConfireData(YbChsConfireData ybChsConfireData) {
        ybChsConfireData.setId(UUID.randomUUID().toString());
//        ybChsConfireData.setCreateTime(new Date());
//        ybChsConfireData.setIsDeletemark(1);
        this.save(ybChsConfireData);
    }

    @Override
    @Transactional
    public void updateYbChsConfireData(YbChsConfireData ybChsConfireData) {
//        ybChsConfireData.setModifyTime(new Date());
        this.baseMapper.updateYbChsConfireData(ybChsConfireData);
    }

    @Override
    @Transactional
    public void deleteYbChsConfireDatas(String[] Ids) {
        List<String> list = Arrays.asList(Ids);
        this.baseMapper.deleteBatchIds(list);
    }

    @Override
    @Transactional
    public void deleteChsConfireDataPids(String[] pIds) {
        List<String> list = Arrays.asList(pIds);
        LambdaQueryWrapper<YbChsConfireData> wrapper = new LambdaQueryWrapper<>();
        wrapper.in(YbChsConfireData::getPid, list);
        this.baseMapper.delete(wrapper);
    }

    @Override
    public List<YbChsConfireData> findChsConfireDataByInDoctorCodeList(String doctorCode,Integer areaType) {
        LambdaQueryWrapper<YbChsConfireData> wrapper = new LambdaQueryWrapper<>();
        String sql = " pid in (select id from  yb_chs_confire where doctorCode = '" + doctorCode + "' and areaType = "+areaType+" and IS_DELETEMARK = 1)";
        wrapper.apply(sql);
        return this.list(wrapper);
    }

    @Override
    public List<YbChsConfireData> findChsConfireDataList(YbChsConfireData chsConfireData) {
        LambdaQueryWrapper<YbChsConfireData> wrapper = new LambdaQueryWrapper<>();
//        wrapper.eq(YbChsConfireData::getIsDeletemark,1);

        if(chsConfireData.getPid() !=null){
            wrapper.eq(YbChsConfireData::getPid,chsConfireData.getPid());
        }

        if(chsConfireData.getDksId() !=null){
            wrapper.eq(YbChsConfireData::getDksId,chsConfireData.getDksId());
        }

        if(chsConfireData.getDksName() !=null){
            wrapper.eq(YbChsConfireData::getDksName,chsConfireData.getDksName());
        }

        if(chsConfireData.getDksName() !=null){
            wrapper.eq(YbChsConfireData::getDksName,chsConfireData.getDksName());
        }

        return this.list(wrapper);
    }
}