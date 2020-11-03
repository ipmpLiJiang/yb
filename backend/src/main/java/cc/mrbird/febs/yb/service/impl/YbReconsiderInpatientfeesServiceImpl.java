package cc.mrbird.febs.yb.service.impl;

import cc.mrbird.febs.common.domain.QueryRequest;
import cc.mrbird.febs.common.utils.SortUtil;
import cc.mrbird.febs.yb.dao.YbReconsiderInpatientfeesMapper;
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
            queryWrapper.eq(YbReconsiderInpatientfees::getIsDeletemark, 1);//1是未删 0是已删


            Page<YbReconsiderInpatientfees> page = new Page<>();
            SortUtil.handlePageSort(request, page, false);//true 是属性  false是数据库字段可两个
            return this.page(page, queryWrapper);
        } catch (Exception e) {
            log.error("获取字典信息失败", e);
            return null;
        }
    }

    @Override
    public IPage<YbReconsiderInpatientfees> findYbReconsiderInpatientfeesList(QueryRequest request, YbReconsiderInpatientfees ybReconsiderInpatientfees) {
        try {
            Page<YbReconsiderInpatientfees> page = new Page<>();
            SortUtil.handlePageSort(request, page, false);//true 是属性  false是数据库字段可两个
            return this.baseMapper.findYbReconsiderInpatientfees(page, ybReconsiderInpatientfees);
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
        ybReconsiderInpatientfees.setCreateTime(new Date());
        if (ybReconsiderInpatientfees.getId() == null || "".equals(ybReconsiderInpatientfees.getId())) {
            ybReconsiderInpatientfees.setId(UUID.randomUUID().toString());
        }
        ybReconsiderInpatientfees.setIsDeletemark(1);
        this.save(ybReconsiderInpatientfees);
    }

    @Override
    @Transactional
    public void updateYbReconsiderInpatientfees(YbReconsiderInpatientfees ybReconsiderInpatientfees) {
        ybReconsiderInpatientfees.setModifyTime(new Date());
        this.baseMapper.updateYbReconsiderInpatientfees(ybReconsiderInpatientfees);
    }

    @Override
    @Transactional
    public void deleteYbReconsiderInpatientfeess(String[] Ids) {
        List<String> list = Arrays.asList(Ids);
        this.baseMapper.deleteBatchIds(list);
    }

    @Override
    public List<YbReconsiderInpatientfees> findReconsiderInpatientfeesLists(String applyDateStr){
        return this.baseMapper.findReconsiderInpatientfeesList(applyDateStr);
    }


}