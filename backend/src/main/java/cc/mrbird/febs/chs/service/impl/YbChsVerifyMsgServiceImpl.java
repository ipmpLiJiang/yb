package cc.mrbird.febs.chs.service.impl;

import cc.mrbird.febs.common.domain.QueryRequest;
import cc.mrbird.febs.common.utils.SortUtil;
import cc.mrbird.febs.chs.entity.YbChsVerifyMsg;
import cc.mrbird.febs.chs.dao.YbChsVerifyMsgMapper;
import cc.mrbird.febs.chs.service.IYbChsVerifyMsgService;
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
 * @since 2022-07-18
 */
@Slf4j
@Service("IYbChsVerifyMsgService")
@Transactional(propagation = Propagation.SUPPORTS, readOnly = true, rollbackFor = Exception.class)
public class YbChsVerifyMsgServiceImpl extends ServiceImpl<YbChsVerifyMsgMapper, YbChsVerifyMsg> implements IYbChsVerifyMsgService {


    @Override
    public IPage<YbChsVerifyMsg> findYbChsVerifyMsgs(QueryRequest request, YbChsVerifyMsg ybChsVerifyMsg) {
        try {
            LambdaQueryWrapper<YbChsVerifyMsg> queryWrapper = new LambdaQueryWrapper<>();
//            queryWrapper.eq(YbChsVerifyMsg::getIsDeletemark, 1);//1是未删 0是已删


            Page<YbChsVerifyMsg> page = new Page<>();
            SortUtil.handlePageSort(request, page, false);//true 是属性  false是数据库字段可两个
            return this.page(page, queryWrapper);
        } catch (Exception e) {
            log.error("获取字典信息失败", e);
            return null;
        }
    }

    @Override
    public IPage<YbChsVerifyMsg> findYbChsVerifyMsgList(QueryRequest request, YbChsVerifyMsg ybChsVerifyMsg) {
        try {
            Page<YbChsVerifyMsg> page = new Page<>();
            SortUtil.handlePageSort(request, page, false);//true 是属性  false是数据库字段可两个
            return this.baseMapper.findYbChsVerifyMsg(page, ybChsVerifyMsg);
        } catch (Exception e) {
            log.error("获取失败", e);
            return null;
        }
    }

    @Override
    @Transactional
    public void createYbChsVerifyMsg(YbChsVerifyMsg ybChsVerifyMsg) {
//        ybChsVerifyMsg.setCreateTime(new Date());
//        ybChsVerifyMsg.setIsDeletemark(1);
        this.save(ybChsVerifyMsg);
    }

    @Override
    @Transactional
    public void updateYbChsVerifyMsg(YbChsVerifyMsg ybChsVerifyMsg) {
//        ybChsVerifyMsg.setModifyTime(new Date());
        this.baseMapper.updateYbChsVerifyMsg(ybChsVerifyMsg);
    }

    @Override
    @Transactional
    public void deleteYbChsVerifyMsgs(String[] Ids) {
        List<String> list = Arrays.asList(Ids);
        this.baseMapper.deleteBatchIds(list);
    }


}