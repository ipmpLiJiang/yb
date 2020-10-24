package cc.mrbird.febs.yb.service.impl;

import cc.mrbird.febs.common.domain.QueryRequest;
import cc.mrbird.febs.common.utils.SortUtil;
import cc.mrbird.febs.yb.dao.YbReconsiderRuleMapper;
import cc.mrbird.febs.yb.entity.YbReconsiderRule;
import cc.mrbird.febs.yb.service.IYbReconsiderRuleService;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
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
 * @since 2020-07-15
 */
@Slf4j
@Service("IYbReconsiderRuleService")
@Transactional(propagation = Propagation.SUPPORTS, readOnly = true, rollbackFor = Exception.class)
public class YbReconsiderRuleServiceImpl extends ServiceImpl<YbReconsiderRuleMapper, YbReconsiderRule> implements IYbReconsiderRuleService {


    @Override
    public IPage<YbReconsiderRule> findYbReconsiderRules(QueryRequest request, YbReconsiderRule ybReconsiderRule) {
        try {
            LambdaQueryWrapper<YbReconsiderRule> queryWrapper = new LambdaQueryWrapper<>();
            queryWrapper.eq(YbReconsiderRule::getIsDeletemark, 1);//1是未删 0是已删

            if (StringUtils.isNotBlank(ybReconsiderRule.getRdescribe())) {
                queryWrapper.like(YbReconsiderRule::getRdescribe, ybReconsiderRule.getRdescribe());
            }

            Page<YbReconsiderRule> page = new Page<>();
            SortUtil.handlePageSort(request, page, false);//true 是属性  false是数据库字段可两个
            return this.page(page, queryWrapper);
        } catch (Exception e) {
            log.error("获取字典信息失败", e);
            return null;
        }
    }

    @Override
    public IPage<YbReconsiderRule> findYbReconsiderRuleList(QueryRequest request, YbReconsiderRule ybReconsiderRule) {
        try {
            Page<YbReconsiderRule> page = new Page<>();
            SortUtil.handlePageSort(request, page, false);//true 是属性  false是数据库字段可两个
            return this.baseMapper.findYbReconsiderRule(page, ybReconsiderRule);
        } catch (Exception e) {
            log.error("获取失败", e);
            return null;
        }
    }

    @Override
    @Transactional
    public void createYbReconsiderRule(YbReconsiderRule ybReconsiderRule) {
        Date thisDate = new java.sql.Timestamp(new Date().getTime());
        if(ybReconsiderRule.getId() == null || "".equals(ybReconsiderRule.getId())) {
            ybReconsiderRule.setId(UUID.randomUUID().toString());
        }
        ybReconsiderRule.setCreateTime(thisDate);
        ybReconsiderRule.setIsDeletemark(1);
        this.save(ybReconsiderRule);
    }

    @Override
    @Transactional
    public void updateYbReconsiderRule(YbReconsiderRule ybReconsiderRule) {
        Date thisDate = new java.sql.Timestamp(new Date().getTime());
        ybReconsiderRule.setModifyTime(thisDate);
        this.baseMapper.updateYbReconsiderRule(ybReconsiderRule);
    }

    @Override
    @Transactional
    public void deleteYbReconsiderRules(String[] Ids) {
        List<String> list = Arrays.asList(Ids);
        this.baseMapper.deleteBatchIds(list);
    }


}