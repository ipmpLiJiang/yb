package cc.mrbird.febs.yb.service.impl;

import cc.mrbird.febs.common.domain.QueryRequest;
import cc.mrbird.febs.common.utils.SortUtil;
import cc.mrbird.febs.yb.entity.YbReconsiderRepay;
import cc.mrbird.febs.yb.dao.YbReconsiderRepayMapper;
import cc.mrbird.febs.yb.entity.YbReconsiderRepayData;
import cc.mrbird.febs.yb.service.IYbReconsiderRepayDataService;
import cc.mrbird.febs.yb.service.IYbReconsiderRepayService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
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
 * @since 2020-09-07
 */
@Slf4j
@Service("IYbReconsiderRepayService")
@Transactional(propagation = Propagation.SUPPORTS, readOnly = true, rollbackFor = Exception.class)
public class YbReconsiderRepayServiceImpl extends ServiceImpl<YbReconsiderRepayMapper, YbReconsiderRepay> implements IYbReconsiderRepayService {

    @Autowired
    IYbReconsiderRepayDataService iYbReconsiderRepayDataService;

    @Override
    public IPage<YbReconsiderRepay> findYbReconsiderRepays(QueryRequest request, YbReconsiderRepay ybReconsiderRepay) {
        try {
            LambdaQueryWrapper<YbReconsiderRepay> queryWrapper = new LambdaQueryWrapper<>();
            queryWrapper.eq(YbReconsiderRepay::getIsDeletemark, 1);//1是未删 0是已删


            Page<YbReconsiderRepay> page = new Page<>();
            SortUtil.handlePageSort(request, page, false);//true 是属性  false是数据库字段可两个
            return this.page(page, queryWrapper);
        } catch (Exception e) {
            log.error("获取字典信息失败", e);
            return null;
        }
    }

    @Override
    public IPage<YbReconsiderRepay> findYbReconsiderRepayList(QueryRequest request, YbReconsiderRepay ybReconsiderRepay) {
        try {
            Page<YbReconsiderRepay> page = new Page<>();
            SortUtil.handlePageSort(request, page, false);//true 是属性  false是数据库字段可两个
            return this.baseMapper.findYbReconsiderRepay(page, ybReconsiderRepay);
        } catch (Exception e) {
            log.error("获取失败", e);
            return null;
        }
    }

    @Override
    @Transactional
    public void createYbReconsiderRepay(YbReconsiderRepay ybReconsiderRepay) {
        if (ybReconsiderRepay.getId() == null || "".equals(ybReconsiderRepay.getId())) {
            ybReconsiderRepay.setId(UUID.randomUUID().toString());
        }
        ybReconsiderRepay.setCreateTime(new Date());
        ybReconsiderRepay.setIsDeletemark(1);
        this.save(ybReconsiderRepay);
    }

    @Override
    @Transactional
    public void updateYbReconsiderRepay(YbReconsiderRepay ybReconsiderRepay) {
        ybReconsiderRepay.setModifyTime(new Date());
        this.baseMapper.updateYbReconsiderRepay(ybReconsiderRepay);
    }

    @Override
    @Transactional
    public void deleteYbReconsiderRepays(String[] Ids) {
        List<String> list = Arrays.asList(Ids);
        this.baseMapper.deleteBatchIds(list);
    }

    @Override
    @Transactional
    public boolean importReconsiderRepay(List<YbReconsiderRepayData> list, Long uid, String uname, String uploadFileName) {
        YbReconsiderRepayData ybReconsiderRepayData = list.get(0);
        YbReconsiderRepay ybReconsiderRepay = new YbReconsiderRepay();
        ybReconsiderRepay.setId(ybReconsiderRepayData.getPid());
        ybReconsiderRepay.setIsDeletemark(1);
        ybReconsiderRepay.setCreateUserId(uid);
        ybReconsiderRepay.setCreateTime(new Date());
        ybReconsiderRepay.setOperatorId(uid);
        ybReconsiderRepay.setOperatorName(uname);
        ybReconsiderRepay.setRepayType(ybReconsiderRepayData.getRepayType());
        ybReconsiderRepay.setDataType(ybReconsiderRepayData.getDataType());
        ybReconsiderRepay.setUploadFileName(uploadFileName);
        ybReconsiderRepay.setState(0);

        boolean isTrue = this.save(ybReconsiderRepay);
        if (isTrue) {
                isTrue = this.iYbReconsiderRepayDataService.saveBatch(list);
        }

        return isTrue;
    }


}