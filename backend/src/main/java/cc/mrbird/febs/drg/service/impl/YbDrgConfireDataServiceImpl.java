package cc.mrbird.febs.drg.service.impl;

import cc.mrbird.febs.common.domain.QueryRequest;
import cc.mrbird.febs.common.utils.SortUtil;
import cc.mrbird.febs.drg.dao.YbDrgConfireDataMapper;
import cc.mrbird.febs.drg.entity.YbDrgConfireData;
import cc.mrbird.febs.drg.service.IYbDrgConfireDataService;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.List;
import java.util.UUID;

/**
 * <p>
 * 服务实现类
 * </p>
 *
 * @author viki
 * @since 2021-01-11
 */
@Slf4j
@Service("IYbDrgConfireDataService")
@Transactional(propagation = Propagation.SUPPORTS, readOnly = true, rollbackFor = Exception.class)
public class YbDrgConfireDataServiceImpl extends ServiceImpl<YbDrgConfireDataMapper, YbDrgConfireData> implements IYbDrgConfireDataService {


    @Override
    public IPage<YbDrgConfireData> findYbDrgConfireDatas(QueryRequest request, YbDrgConfireData ybDrgConfireData) {
        try {
            LambdaQueryWrapper<YbDrgConfireData> queryWrapper = new LambdaQueryWrapper<>();
//            queryWrapper.eq(YbDrgConfireData::getIsDeletemark, 1);//1是未删 0是已删
            if (ybDrgConfireData.getPid() != null) {
                queryWrapper.eq(YbDrgConfireData::getPid, ybDrgConfireData.getPid());
            }

            Page<YbDrgConfireData> page = new Page<>();
            SortUtil.handlePageSort(request, page, false);//true 是属性  false是数据库字段可两个
            return this.page(page, queryWrapper);
        } catch (Exception e) {
            log.error("获取字典信息失败", e);
            return null;
        }
    }

    @Override
    public IPage<YbDrgConfireData> findYbDrgConfireDataList(QueryRequest request, YbDrgConfireData ybDrgConfireData) {
        try {
            Page<YbDrgConfireData> page = new Page<>();
            SortUtil.handlePageSort(request, page, false);//true 是属性  false是数据库字段可两个
            return this.baseMapper.findYbDrgConfireData(page, ybDrgConfireData);
        } catch (Exception e) {
            log.error("获取失败", e);
            return null;
        }
    }

    @Override
    @Transactional
    public void createYbDrgConfireData(YbDrgConfireData ybDrgConfireData) {
        ybDrgConfireData.setId(UUID.randomUUID().toString());
//        ybDrgConfireData.setCreateTime(new Date());
//        ybDrgConfireData.setIsDeletemark(1);
        this.save(ybDrgConfireData);
    }

    @Override
    @Transactional
    public void updateYbDrgConfireData(YbDrgConfireData ybDrgConfireData) {
//        ybDrgConfireData.setModifyTime(new Date());
        this.baseMapper.updateYbDrgConfireData(ybDrgConfireData);
    }

    @Override
    @Transactional
    public void deleteYbDrgConfireDatas(String[] Ids) {
        List<String> list = Arrays.asList(Ids);
        this.baseMapper.deleteBatchIds(list);
    }

    @Override
    @Transactional
    public void deleteDrgConfireDataPids(String[] pIds) {
        List<String> list = Arrays.asList(pIds);
        LambdaQueryWrapper<YbDrgConfireData> wrapper = new LambdaQueryWrapper<>();
        wrapper.in(YbDrgConfireData::getPid, list);
        this.baseMapper.delete(wrapper);
    }


    @Override
    public List<YbDrgConfireData> findDrgConfireDataByInDoctorCodeList(String doctorCode,Integer areaType) {
        LambdaQueryWrapper<YbDrgConfireData> wrapper = new LambdaQueryWrapper<>();
        String sql = " pid in (select id from  yb_Drg_confire where doctorCode = '" + doctorCode + "' and areaType = "+areaType+" and IS_DELETEMARK = 1)";
        wrapper.apply(sql);
        return this.list(wrapper);
    }
    @Override
    public List<YbDrgConfireData> findDrgConfireDataList(YbDrgConfireData drgConfireData) {
        LambdaQueryWrapper<YbDrgConfireData> wrapper = new LambdaQueryWrapper<>();
//        wrapper.eq(YbDrgConfireData::getIsDeletemark,1);

        if(drgConfireData.getPid() !=null){
            wrapper.eq(YbDrgConfireData::getPid,drgConfireData.getPid());
        }

        if(drgConfireData.getDksId() !=null){
            wrapper.eq(YbDrgConfireData::getDksId,drgConfireData.getDksId());
        }

        if(drgConfireData.getDksName() !=null){
            wrapper.eq(YbDrgConfireData::getDksName,drgConfireData.getDksName());
        }

        return this.list(wrapper);
    }

}