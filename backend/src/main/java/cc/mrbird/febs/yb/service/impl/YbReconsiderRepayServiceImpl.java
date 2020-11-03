package cc.mrbird.febs.yb.service.impl;

import cc.mrbird.febs.common.domain.QueryRequest;
import cc.mrbird.febs.common.utils.SortUtil;
import cc.mrbird.febs.yb.dao.YbReconsiderRepayMapper;
import cc.mrbird.febs.yb.entity.*;
import cc.mrbird.febs.yb.service.IYbReconsiderApplyService;
import cc.mrbird.febs.yb.service.IYbReconsiderRepayDataService;
import cc.mrbird.febs.yb.service.IYbReconsiderRepayService;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
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
 * @since 2020-09-07
 */
@Slf4j
@Service("IYbReconsiderRepayService")
@Transactional(propagation = Propagation.SUPPORTS, readOnly = true, rollbackFor = Exception.class)
public class YbReconsiderRepayServiceImpl extends ServiceImpl<YbReconsiderRepayMapper, YbReconsiderRepay> implements IYbReconsiderRepayService {

    @Autowired
    IYbReconsiderRepayDataService iYbReconsiderRepayDataService;

    @Autowired
    IYbReconsiderApplyService iYbReconsiderApplyService;

    @Override
    public IPage<YbReconsiderRepay> findYbReconsiderRepays(QueryRequest request, YbReconsiderRepay ybReconsiderRepay) {
        try {
            LambdaQueryWrapper<YbReconsiderRepay> queryWrapper = new LambdaQueryWrapper<>();
            String sql = " IS_DELETEMARK = 1 ";
            // queryWrapper.eq(YbReconsiderRepay::getIsDeletemark, 1);//1是未删 0是已删
            if (ybReconsiderRepay.getCreateTimeFrom() != null) {
                sql += " AND CREATE_TIME >= '" + ybReconsiderRepay.getCreateTimeFrom() + "' ";
            }
            if (ybReconsiderRepay.getCreateTimeTo() != null) {
                sql += " AND CREATE_TIME <= '" + ybReconsiderRepay.getCreateTimeTo() + "' ";
            }

            queryWrapper.apply(sql);
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
    public String deleteReconsiderRepay(String Id) {
        String message = "";
        LambdaQueryWrapper<YbReconsiderRepay> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(YbReconsiderRepay::getId, Id);
        List<YbReconsiderRepay> repayList = this.list(queryWrapper);
        if (repayList.size() == 1) {
            YbReconsiderRepay ybReconsiderRepay = repayList.get(0);
            if (ybReconsiderRepay.getState() == 0) {
                LambdaQueryWrapper<YbReconsiderRepayData> queryWrapperData = new LambdaQueryWrapper<>();
                queryWrapperData.eq(YbReconsiderRepayData::getPid, ybReconsiderRepay.getId());
                boolean bl = iYbReconsiderRepayDataService.remove(queryWrapperData);
                if (bl) {
                    int count = this.baseMapper.deleteById(ybReconsiderRepay.getId());
                    if (count > 0) {
                        message = "ok";
                    }
                }
            } else {
                message = "该数据已经操作过还款,无法删除,请重新刷新.";
            }
        } else {
            message = "未找到该数据,请重新刷新.";
        }

        return message;
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
        ybReconsiderRepay.setApplyDate(ybReconsiderRepayData.getBelongDate());
        ybReconsiderRepay.setApplyDateStr(ybReconsiderRepayData.getBelongDateStr());
        ybReconsiderRepay.setState(0);

        boolean isTrue = this.save(ybReconsiderRepay);
        if (isTrue) {
            isTrue = this.iYbReconsiderRepayDataService.saveBatch(list);
        }

        return isTrue;
    }

    @Override
    @Transactional
    public String updateReconsiderApplyState(YbReconsiderRepay ybReconsiderRepay) {
        String message = "";
        LambdaQueryWrapper<YbReconsiderApply> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(YbReconsiderApply::getApplyDateStr, ybReconsiderRepay.getApplyDateStr());
        List<YbReconsiderApply> list = this.iYbReconsiderApplyService.list(wrapper);
        if (list.size() > 0) {
            //剔除完成
            if (list.get(0).getResetState() == 1) {
                //还款完成
                if (list.get(0).getRepayState() == 0) {
                    LambdaQueryWrapper<YbReconsiderRepay> wrapperRepay = new LambdaQueryWrapper<>();
                    wrapperRepay.eq(YbReconsiderRepay::getId, ybReconsiderRepay.getId());
                    List<YbReconsiderRepay> listRepay = this.list(wrapperRepay);
                    if (listRepay.size() > 0) {
                        YbReconsiderRepay searchReconsiderRepay = listRepay.get(0);
                        LambdaQueryWrapper<YbReconsiderRepayData> wrapperRepayData = new LambdaQueryWrapper<>();
                        wrapperRepayData.eq(YbReconsiderRepayData::getPid, searchReconsiderRepay.getId());
                        List<YbReconsiderRepayData> listRepayData = this.iYbReconsiderRepayDataService.list(wrapperRepayData);
                        if (listRepayData.size() > 0) {
                            long count = listRepayData.stream().filter(s -> (s.getState() == 0 || s.getState() == 1) &&
                                    s.getSeekState() == YbDefaultValue.SEEKSTATE_0).count();
                            if (count == 0) {
                                YbReconsiderApply update = new YbReconsiderApply();
                                update.setId(list.get(0).getId());
                                update.setModifyTime(new Date());
                                update.setState(YbDefaultValue.APPLYSTATE_7);
                                update.setRepayState(1);
                                boolean bl = this.iYbReconsiderApplyService.updateById(update);
                                if (bl) {
                                    message = "ok";
                                }
                            } else {
                                message = "该年月 " + ybReconsiderRepay.getApplyDateStr() + " 还款业务还有未处理完的数据.";
                            }
                        } else {
                            message = "该年月 " + ybReconsiderRepay.getApplyDateStr() + " 未找到还款上传明细.";
                        }
                    } else {
                        message = "该年月 " + ybReconsiderRepay.getApplyDateStr() + " 未上传还款数据.";
                    }
                } else {
                    message = "该年月 " + ybReconsiderRepay.getApplyDateStr() + " 已完成还款完成操作.";
                }
            } else {
                message = "该年月 " + ybReconsiderRepay.getApplyDateStr() + " 复议审核剔除状态未更新.";
            }
        } else {
            message = "该年月 " + ybReconsiderRepay.getApplyDateStr() + " 未找到复议审核数据或还未上传复议数据.";
        }

        return message;
    }

}