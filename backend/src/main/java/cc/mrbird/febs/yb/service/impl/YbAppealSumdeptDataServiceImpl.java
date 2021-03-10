package cc.mrbird.febs.yb.service.impl;

import cc.mrbird.febs.common.domain.QueryRequest;
import cc.mrbird.febs.common.utils.SortUtil;
import cc.mrbird.febs.yb.entity.YbAppealConfireData;
import cc.mrbird.febs.yb.entity.YbAppealSumdeptData;
import cc.mrbird.febs.yb.dao.YbAppealSumdeptDataMapper;
import cc.mrbird.febs.yb.service.IYbAppealSumdeptDataService;
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
 * @since 2021-03-08
 */
@Slf4j
@Service("IYbAppealSumdeptDataService")
@Transactional(propagation = Propagation.SUPPORTS, readOnly = true, rollbackFor = Exception.class)
public class YbAppealSumdeptDataServiceImpl extends ServiceImpl<YbAppealSumdeptDataMapper, YbAppealSumdeptData> implements IYbAppealSumdeptDataService {


    @Override
    public IPage<YbAppealSumdeptData> findYbAppealSumdeptDatas(QueryRequest request, YbAppealSumdeptData ybAppealSumdeptData) {
        try {
            LambdaQueryWrapper<YbAppealSumdeptData> queryWrapper = new LambdaQueryWrapper<>();
//        queryWrapper.eq(YbAppealSumdeptData::getIsDeletemark, 1);//1是未删 0是已删
            if (ybAppealSumdeptData.getPid() != null) {
                queryWrapper.eq(YbAppealSumdeptData::getPid, ybAppealSumdeptData.getPid());
            }

            Page<YbAppealSumdeptData> page = new Page<>();
            SortUtil.handlePageSort(request, page, false);//true 是属性  false是数据库字段可两个
            return this.page(page, queryWrapper);
        } catch (Exception e) {
            log.error("获取字典信息失败", e);
            return null;
        }
    }

    @Override
    public IPage<YbAppealSumdeptData> findYbAppealSumdeptDataList(QueryRequest request, YbAppealSumdeptData ybAppealSumdeptData) {
        try {
            Page<YbAppealSumdeptData> page = new Page<>();
            SortUtil.handlePageSort(request, page, false);//true 是属性  false是数据库字段可两个
            return this.baseMapper.findYbAppealSumdeptData(page, ybAppealSumdeptData);
        } catch (Exception e) {
            log.error("获取失败", e);
            return null;
        }
    }

    @Override
    @Transactional
    public void createYbAppealSumdeptData(YbAppealSumdeptData ybAppealSumdeptData) {
//                ybAppealSumdeptData.setCreateTime(new Date());
//        ybAppealSumdeptData.setIsDeletemark(1);
        this.save(ybAppealSumdeptData);
    }

    @Override
    @Transactional
    public void updateYbAppealSumdeptData(YbAppealSumdeptData ybAppealSumdeptData) {
//        ybAppealSumdeptData.setModifyTime(new Date());
        this.baseMapper.updateYbAppealSumdeptData(ybAppealSumdeptData);
    }

    @Override
    @Transactional
    public void deleteYbAppealSumdeptDatas(String[] Ids) {
        List<String> list = Arrays.asList(Ids);
        this.baseMapper.deleteBatchIds(list);
    }

    @Override
    @Transactional
    public void deleteAppealSumdeptDataPids(String[] pIds) {
        List<String> list = Arrays.asList(pIds);
        LambdaQueryWrapper<YbAppealSumdeptData> wrapper = new LambdaQueryWrapper<>();
        wrapper.in(YbAppealSumdeptData::getPid, list);
        this.baseMapper.delete(wrapper);
    }

        @Override
        public List<YbAppealSumdeptData> findAppealSumdeptDataList(YbAppealSumdeptData appealSumdeptData) {
                LambdaQueryWrapper<YbAppealSumdeptData> wrapper = new LambdaQueryWrapper<>();

                if(appealSumdeptData.getPid() !=null){
                        wrapper.eq(YbAppealSumdeptData::getPid,appealSumdeptData.getPid());
                }

                if(appealSumdeptData.getDeptId() !=null){
                        wrapper.eq(YbAppealSumdeptData::getDeptId,appealSumdeptData.getDeptId());
                }

                if(appealSumdeptData.getDeptName() !=null){
                        wrapper.eq(YbAppealSumdeptData::getDeptName,appealSumdeptData.getDeptName());
                }

                return this.list(wrapper);
        }

}