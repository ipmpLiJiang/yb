package cc.mrbird.febs.yb.service.impl;

import cc.mrbird.febs.common.domain.QueryRequest;
import cc.mrbird.febs.common.utils.SortUtil;
import cc.mrbird.febs.yb.entity.YbDept;
import cc.mrbird.febs.yb.dao.YbDeptMapper;
import cc.mrbird.febs.yb.service.IYbDeptService;
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
 * @since 2020-07-21
 */
@Slf4j
@Service("IYbDeptService")
@Transactional(propagation = Propagation.SUPPORTS, readOnly = true, rollbackFor = Exception.class)
public class YbDeptServiceImpl extends ServiceImpl<YbDeptMapper, YbDept> implements IYbDeptService {


    @Override
    public IPage<YbDept> findYbDepts(QueryRequest request, YbDept ybDept) {
        try {
            LambdaQueryWrapper<YbDept> queryWrapper = new LambdaQueryWrapper<>();
            queryWrapper.eq(YbDept::getIsDeletemark, 1);//1是未删 0是已删


            Page<YbDept> page = new Page<>();
            SortUtil.handlePageSort(request, page, false);//true 是属性  false是数据库字段可两个
            return this.page(page, queryWrapper);
        } catch (Exception e) {
            log.error("获取字典信息失败", e);
            return null;
        }
    }

    @Override
    public IPage<YbDept> findYbDeptList(QueryRequest request, YbDept ybDept) {
        try {
            Page<YbDept> page = new Page<>();
            SortUtil.handlePageSort(request, page, false);//true 是属性  false是数据库字段可两个
            return this.baseMapper.findYbDept(page, ybDept);
        } catch (Exception e) {
            log.error("获取失败", e);
            return null;
        }
    }

    @Override
    @Transactional
    public void createYbDept(YbDept ybDept) {
        ybDept.setCreateTime(new Date());
        ybDept.setIsDeletemark(1);
        this.save(ybDept);
    }

    @Override
    @Transactional
    public void updateYbDept(YbDept ybDept) {
        ybDept.setModifyTime(new Date());
        this.baseMapper.updateYbDept(ybDept);
    }

    @Override
    @Transactional
    public void deleteYbDepts(String[] Ids) {
        List<String> list = Arrays.asList(Ids);
        this.baseMapper.deleteBatchIds(list);
    }


}