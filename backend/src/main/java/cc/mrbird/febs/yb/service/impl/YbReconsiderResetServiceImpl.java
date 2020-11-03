package cc.mrbird.febs.yb.service.impl;

import cc.mrbird.febs.common.domain.QueryRequest;
import cc.mrbird.febs.common.utils.SortUtil;
import cc.mrbird.febs.yb.entity.YbDefaultValue;
import cc.mrbird.febs.yb.entity.YbReconsiderApply;
import cc.mrbird.febs.yb.entity.YbReconsiderReset;
import cc.mrbird.febs.yb.dao.YbReconsiderResetMapper;
import cc.mrbird.febs.yb.entity.YbReconsiderResetData;
import cc.mrbird.febs.yb.service.*;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

/**
 * <p>
 * 服务实现类
 * </p>
 *
 * @author viki
 * @since 2020-08-17
 */
@Slf4j
@Service("IYbReconsiderResetService")
@Transactional(propagation = Propagation.SUPPORTS, readOnly = true, rollbackFor = Exception.class)
public class YbReconsiderResetServiceImpl extends ServiceImpl<YbReconsiderResetMapper, YbReconsiderReset> implements IYbReconsiderResetService {

    @Autowired
    public IYbReconsiderResetDataService iYbReconsiderResetDataService;

    @Autowired
    IYbReconsiderApplyService iYbReconsiderApplyService;

    @Override
    public IPage<YbReconsiderReset> findYbReconsiderResets(QueryRequest request, YbReconsiderReset ybReconsiderReset) {
        try {
            LambdaQueryWrapper<YbReconsiderReset> queryWrapper = new LambdaQueryWrapper<>();
            queryWrapper.eq(YbReconsiderReset::getIsDeletemark, 1);//1是未删 0是已删


            Page<YbReconsiderReset> page = new Page<>();
            SortUtil.handlePageSort(request, page, false);//true 是属性  false是数据库字段可两个
            return this.page(page, queryWrapper);
        } catch (Exception e) {
            log.error("获取字典信息失败", e);
            return null;
        }
    }

    @Override
    public IPage<YbReconsiderReset> findYbReconsiderResetList(QueryRequest request, YbReconsiderReset ybReconsiderReset) {
        try {
            Page<YbReconsiderReset> page = new Page<>();
            SortUtil.handlePageSort(request, page, false);//true 是属性  false是数据库字段可两个
            return this.baseMapper.findYbReconsiderReset(page, ybReconsiderReset);
        } catch (Exception e) {
            log.error("获取失败", e);
            return null;
        }
    }

    @Override
    @Transactional
    public void createYbReconsiderReset(YbReconsiderReset ybReconsiderReset) {
        if (ybReconsiderReset.getId() == null || "".equals(ybReconsiderReset.getId())) {
            ybReconsiderReset.setId(UUID.randomUUID().toString());
        }
        ybReconsiderReset.setCreateTime(new Date());
        ybReconsiderReset.setIsDeletemark(1);
        this.save(ybReconsiderReset);
    }

    @Override
    @Transactional
    public void updateYbReconsiderReset(YbReconsiderReset ybReconsiderReset) {
        ybReconsiderReset.setModifyTime(new Date());
        this.baseMapper.updateYbReconsiderReset(ybReconsiderReset);
    }

    @Override
    @Transactional
    public void deleteYbReconsiderResets(String[] Ids) {
        List<String> list = Arrays.asList(Ids);
        this.baseMapper.deleteBatchIds(list);
    }

    @Override
    @Transactional
    public void importReconsiderResets(YbReconsiderReset ybReconsiderReset, List<YbReconsiderResetData> listData, List<YbReconsiderResetData> listMain) {
        this.baseMapper.insert(ybReconsiderReset);
        if (listData.size() > 0) {
            this.iYbReconsiderResetDataService.saveBatch(listData);
        }
        if (listMain.size() > 0) {
            this.iYbReconsiderResetDataService.saveBatch(listMain);
        }
    }

    @Override
    public YbReconsiderReset findReconsiderResetByApplyDateStr(String applyDateStr) {
        LambdaQueryWrapper<YbReconsiderReset> wrapper = new LambdaQueryWrapper<YbReconsiderReset>();
        wrapper.eq(YbReconsiderReset::getApplyDateStr, applyDateStr);
        List<YbReconsiderReset> list = this.list(wrapper);
        if (list.size() > 0) {
            return list.get(0);
        } else {
            return null;
        }
    }

    @Override
    @Transactional
    public String updateReconsiderApplyState(YbReconsiderReset ybReconsiderReset) {
        String message = "";
        LambdaQueryWrapper<YbReconsiderApply> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(YbReconsiderApply::getApplyDateStr, ybReconsiderReset.getApplyDateStr());
        wrapper.ne(YbReconsiderApply::getState, YbDefaultValue.APPLYSTATE_1);
        List<YbReconsiderApply> list = this.iYbReconsiderApplyService.list(wrapper);
        if (list.size() > 0) {
            if (list.get(0).getResetState() == 0) {
                LambdaQueryWrapper<YbReconsiderReset> wrapperReset = new LambdaQueryWrapper<>();
                wrapperReset.eq(YbReconsiderReset::getApplyDateStr, ybReconsiderReset.getApplyDateStr());
                List<YbReconsiderReset> listReset = this.list(wrapperReset);
                if (listReset.size() > 0) {
                    YbReconsiderReset searchReconsiderReset = listReset.get(0);
                    LambdaQueryWrapper<YbReconsiderResetData> wrapperResetData = new LambdaQueryWrapper<>();
                    wrapperResetData.eq(YbReconsiderResetData::getPid, searchReconsiderReset.getId());
                    List<YbReconsiderResetData> listResetData = this.iYbReconsiderResetDataService.list(wrapperResetData);
                    if (listResetData.size() > 0) {
                        long count = listResetData.stream().filter(s ->
                                (s.getState() == YbDefaultValue.RESETDATA_STATE_0 ||
                                s.getState() == YbDefaultValue.RESETDATA_STATE_1) &&
                                s.getSeekState() == YbDefaultValue.SEEKSTATE_0).count();

                        if (count == 0) {
                            YbReconsiderApply update = new YbReconsiderApply();
                            update.setId(list.get(0).getId());
                            update.setModifyTime(new Date());
                            update.setState(YbDefaultValue.APPLYSTATE_6);
                            update.setResetState(1);
                            boolean bl = this.iYbReconsiderApplyService.updateById(update);
                            if (bl) {
                                message = "ok";
                            }
                        } else {
                            message = "该年月 " + ybReconsiderReset.getApplyDateStr() + " 剔除业务还有未处理完的数据.";
                        }
                    } else {
                        message = "该年月 " + ybReconsiderReset.getApplyDateStr() + " 未找到剔除上传明细.";
                    }
                } else {
                    message = "该年月 " + ybReconsiderReset.getApplyDateStr() + " 未上传剔除数据.";
                }
            }else{
                message = "该年月 " + ybReconsiderReset.getApplyDateStr() + " 已完成剔除完成操作.";
            }
        } else {
            message = "该年月 " + ybReconsiderReset.getApplyDateStr() + " 未找到复议审核数据或还未上传复议数据.";
        }

        return message;
    }

}