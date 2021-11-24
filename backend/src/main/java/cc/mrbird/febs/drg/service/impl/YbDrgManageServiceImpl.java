package cc.mrbird.febs.drg.service.impl;

import cc.mrbird.febs.common.domain.QueryRequest;
import cc.mrbird.febs.common.utils.SortUtil;
import cc.mrbird.febs.drg.entity.YbDrgManage;
import cc.mrbird.febs.drg.dao.YbDrgManageMapper;
import cc.mrbird.febs.drg.service.IYbDrgManageService;
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
 * @since 2021-11-23
 */
@Slf4j
@Service("IYbDrgManageService")
@Transactional(propagation = Propagation.SUPPORTS, readOnly = true, rollbackFor = Exception.class)
public class YbDrgManageServiceImpl extends ServiceImpl<YbDrgManageMapper, YbDrgManage> implements IYbDrgManageService {


    @Override
    public IPage<YbDrgManage> findYbDrgManages(QueryRequest request, YbDrgManage ybDrgManage) {
        try {
            LambdaQueryWrapper<YbDrgManage> queryWrapper = new LambdaQueryWrapper<>();
//            queryWrapper.eq(YbDrgManage::getIsDeletemark, 1);//1是未删 0是已删


            Page<YbDrgManage> page = new Page<>();
            SortUtil.handlePageSort(request, page, false);//true 是属性  false是数据库字段可两个
            return this.page(page, queryWrapper);
        } catch (Exception e) {
            log.error("获取字典信息失败", e);
            return null;
        }
    }

    @Override
    public IPage<YbDrgManage> findYbDrgManageList(QueryRequest request, YbDrgManage ybDrgManage) {
        try {
            Page<YbDrgManage> page = new Page<>();
            SortUtil.handlePageSort(request, page, false);//true 是属性  false是数据库字段可两个
            return this.baseMapper.findYbDrgManage(page, ybDrgManage);
        } catch (Exception e) {
            log.error("获取失败", e);
            return null;
        }
    }

    @Override
    @Transactional
    public void createYbDrgManage(YbDrgManage ybDrgManage) {
        ybDrgManage.setId(UUID.randomUUID().toString());
//        ybDrgManage.setCreateTime(new Date());
//        ybDrgManage.setIsDeletemark(1);
        this.save(ybDrgManage);
    }

    @Override
    @Transactional
    public void updateYbDrgManage(YbDrgManage ybDrgManage) {
//        ybDrgManage.setModifyTime(new Date());
        this.baseMapper.updateYbDrgManage(ybDrgManage);
    }

    @Override
    @Transactional
    public void deleteYbDrgManages(String[] Ids) {
        List<String> list = Arrays.asList(Ids);
        this.baseMapper.deleteBatchIds(list);
    }


}