package cc.mrbird.febs.chs.service.impl;

import cc.mrbird.febs.common.domain.QueryRequest;
import cc.mrbird.febs.common.utils.SortUtil;
import cc.mrbird.febs.chs.entity.YbChsVerify;
import cc.mrbird.febs.chs.dao.YbChsVerifyMapper;
import cc.mrbird.febs.chs.service.IYbChsVerifyService;
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
 * @since 2022-06-20
 */
@Slf4j
@Service("IYbChsVerifyService")
@Transactional(propagation = Propagation.SUPPORTS, readOnly = true, rollbackFor = Exception.class)
public class YbChsVerifyServiceImpl extends ServiceImpl<YbChsVerifyMapper, YbChsVerify> implements IYbChsVerifyService {


    @Override
    public IPage<YbChsVerify> findYbChsVerifys(QueryRequest request, YbChsVerify ybChsVerify) {
        try {
            LambdaQueryWrapper<YbChsVerify> queryWrapper = new LambdaQueryWrapper<>();
//            queryWrapper.eq(YbChsVerify::getIsDeletemark, 1);//1是未删 0是已删


            Page<YbChsVerify> page = new Page<>();
            SortUtil.handlePageSort(request, page, false);//true 是属性  false是数据库字段可两个
            return this.page(page, queryWrapper);
        } catch (Exception e) {
            log.error("获取字典信息失败", e);
            return null;
        }
    }

    @Override
    public IPage<YbChsVerify> findYbChsVerifyList(QueryRequest request, YbChsVerify ybChsVerify) {
        try {
            Page<YbChsVerify> page = new Page<>();
            SortUtil.handlePageSort(request, page, false);//true 是属性  false是数据库字段可两个
            return this.baseMapper.findYbChsVerify(page, ybChsVerify);
        } catch (Exception e) {
            log.error("获取失败", e);
            return null;
        }
    }

    @Override
    @Transactional
    public void createYbChsVerify(YbChsVerify ybChsVerify) {
        ybChsVerify.setId(UUID.randomUUID().toString());
//        ybChsVerify.setCreateTime(new Date());
//        ybChsVerify.setIsDeletemark(1);
        this.save(ybChsVerify);
    }

    @Override
    @Transactional
    public void updateYbChsVerify(YbChsVerify ybChsVerify) {
//        ybChsVerify.setModifyTime(new Date());
        this.baseMapper.updateYbChsVerify(ybChsVerify);
    }

    @Override
    @Transactional
    public void deleteYbChsVerifys(String[] Ids) {
        List<String> list = Arrays.asList(Ids);
        this.baseMapper.deleteBatchIds(list);
    }


}