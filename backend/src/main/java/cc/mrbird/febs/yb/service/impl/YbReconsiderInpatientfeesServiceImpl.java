package cc.mrbird.febs.yb.service.impl;

import cc.mrbird.febs.common.domain.QueryRequest;
import cc.mrbird.febs.common.utils.SortUtil;
import cc.mrbird.febs.yb.dao.YbReconsiderInpatientfeesMapper;
import cc.mrbird.febs.yb.entity.YbPerson;
import cc.mrbird.febs.yb.entity.YbReconsiderInpatientfees;
import cc.mrbird.febs.yb.service.IYbReconsiderInpatientfeesService;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

/**
 * <p>
 * 服务实现类
 * </p>
 *
 * @author viki
 * @since 2020-07-22
 */
@Slf4j
@Service("IYbReconsiderInpatientfeesService")
@Transactional(propagation = Propagation.SUPPORTS, readOnly = true, rollbackFor = Exception.class)
public class YbReconsiderInpatientfeesServiceImpl extends ServiceImpl<YbReconsiderInpatientfeesMapper, YbReconsiderInpatientfees> implements IYbReconsiderInpatientfeesService {


    @Override
    public IPage<YbReconsiderInpatientfees> findYbReconsiderInpatientfeess(QueryRequest request, YbReconsiderInpatientfees ybReconsiderInpatientfees) {
        try {
            LambdaQueryWrapper<YbReconsiderInpatientfees> queryWrapper = new LambdaQueryWrapper<>();
            queryWrapper.eq(YbReconsiderInpatientfees::getAreaType, ybReconsiderInpatientfees.getAreaType());
            queryWrapper.eq(YbReconsiderInpatientfees::getIsDeletemark, 1);//1是未删 0是已删

            Page<YbReconsiderInpatientfees> page = new Page<>();
            SortUtil.handlePageSort(request, page, false);//true 是属性  false是数据库字段可两个
            return this.page(page, queryWrapper);
        } catch (Exception e) {
            log.error("获取字典信息失败", e);
            return null;
        }
    }

    //    @Override
//    public IPage<YbReconsiderInpatientfees> findYbReconsiderInpatientfeesList(QueryRequest request, YbReconsiderInpatientfees ybReconsiderInpatientfees) {
//        try {
//            Page<YbReconsiderInpatientfees> page = new Page<>();
//            SortUtil.handlePageSort(request, page, false);//true 是属性  false是数据库字段可两个
//            return this.baseMapper.findYbReconsiderInpatientfees(page, ybReconsiderInpatientfees);
//        } catch (Exception e) {
//            log.error("获取失败", e);
//            return null;
//        }
//    }
    @Override
    public IPage<YbReconsiderInpatientfees> findYbReconsiderInpatientfeesList(QueryRequest request, YbReconsiderInpatientfees ybReconsiderInpatientfees) {
        try {
            LambdaQueryWrapper<YbReconsiderInpatientfees> queryWrapper = new LambdaQueryWrapper<>();
            queryWrapper.eq(YbReconsiderInpatientfees::getApplyDateStr, ybReconsiderInpatientfees.getApplyDateStr());
            queryWrapper.eq(YbReconsiderInpatientfees::getAreaType, ybReconsiderInpatientfees.getAreaType());
            queryWrapper.eq(YbReconsiderInpatientfees::getDataType, ybReconsiderInpatientfees.getDataType());
            queryWrapper.eq(YbReconsiderInpatientfees::getIsDeletemark, 1);//1是未删 0是已删
            Page<YbReconsiderInpatientfees> page = new Page<>();
            SortUtil.handlePageSort(request, page, false);//true 是属性  false是数据库字段可两个
            return this.page(page, queryWrapper);
        } catch (Exception e) {
            log.error("获取失败", e);
            return null;
        }
    }

    @Override
    public IPage<YbReconsiderInpatientfees> findYbReconsiderInpatientfeesEqs(QueryRequest request, YbReconsiderInpatientfees ybReconsiderInpatientfees) {
        try {
            Page<YbReconsiderInpatientfees> page = new Page<>();
            SortUtil.handlePageSort(request, page, false);//true 是属性  false是数据库字段可两个
            return this.baseMapper.findYbReconsiderInpatientfeesEq(page, ybReconsiderInpatientfees);
        } catch (Exception e) {
            log.error("获取失败", e);
            return null;
        }
    }

    @Override
    @Transactional
    public void createYbReconsiderInpatientfees(YbReconsiderInpatientfees ybReconsiderInpatientfees) {
//        ybReconsiderInpatientfees.setCreateTime(new Date());
        if (ybReconsiderInpatientfees.getId() == null || "".equals(ybReconsiderInpatientfees.getId())) {
            ybReconsiderInpatientfees.setId(UUID.randomUUID().toString());
        }
        ybReconsiderInpatientfees.setIsDeletemark(1);
        this.save(ybReconsiderInpatientfees);
    }

    @Override
    @Transactional
    public void updateYbReconsiderInpatientfees(YbReconsiderInpatientfees ybReconsiderInpatientfees) {
//        ybReconsiderInpatientfees.setModifyTime(new Date());
        this.baseMapper.updateYbReconsiderInpatientfees(ybReconsiderInpatientfees);
    }

    @Override
    @Transactional
    public void deleteYbReconsiderInpatientfeess(String[] Ids) {
        List<String> list = Arrays.asList(Ids);
        this.baseMapper.deleteBatchIds(list);
    }

    @Override
    public List<YbReconsiderInpatientfees> findReconsiderInpatientfeesList(YbReconsiderInpatientfees ybReconsiderInpatientfees) {
        LambdaQueryWrapper<YbReconsiderInpatientfees> wrapper = new LambdaQueryWrapper<>();
        if (ybReconsiderInpatientfees.getApplyDateStr() != null) {
            wrapper.eq(YbReconsiderInpatientfees::getApplyDateStr, ybReconsiderInpatientfees.getApplyDateStr());
        }
        if (ybReconsiderInpatientfees.getAreaType() != null) {
            wrapper.eq(YbReconsiderInpatientfees::getAreaType, ybReconsiderInpatientfees.getAreaType());
        }
        if (ybReconsiderInpatientfees.getTypeno() != null) {
            wrapper.eq(YbReconsiderInpatientfees::getTypeno, ybReconsiderInpatientfees.getTypeno());
        }
        if (ybReconsiderInpatientfees.getDataType() != null) {
            wrapper.eq(YbReconsiderInpatientfees::getDataType, ybReconsiderInpatientfees.getDataType());
        }
        wrapper.eq(YbReconsiderInpatientfees::getIsDeletemark, 1);
        return this.list(wrapper);
    }


    @Override
    public List<YbReconsiderInpatientfees> findReconsiderInpatientfeesList(ArrayList<String> applyDataIdList) {
        LambdaQueryWrapper<YbReconsiderInpatientfees> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(YbReconsiderInpatientfees::getIsDeletemark, 1);
        wrapper.in(YbReconsiderInpatientfees::getApplyDataId,applyDataIdList);
        List<YbReconsiderInpatientfees> list = this.list(wrapper);
        return list;
    }


}