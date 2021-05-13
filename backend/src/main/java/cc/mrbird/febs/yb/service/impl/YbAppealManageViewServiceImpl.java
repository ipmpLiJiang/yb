package cc.mrbird.febs.yb.service.impl;

import cc.mrbird.febs.common.domain.QueryRequest;
import cc.mrbird.febs.common.utils.SortUtil;
import cc.mrbird.febs.system.domain.User;
import cc.mrbird.febs.yb.dao.YbAppealManageViewMapper;
import cc.mrbird.febs.yb.entity.YbAppealConfireData;
import cc.mrbird.febs.yb.entity.YbAppealManageView;
import cc.mrbird.febs.yb.entity.YbAppealResultView;
import cc.mrbird.febs.yb.entity.YbReconsiderApply;
import cc.mrbird.febs.yb.service.IYbAppealConfireDataService;
import cc.mrbird.febs.yb.service.IYbAppealManageViewService;
import cc.mrbird.febs.yb.service.IYbReconsiderApplyService;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

/**
 * <p>
 * VIEW 服务实现类
 * </p>
 *
 * @author viki
 * @since 2020-07-30
 */
@Slf4j
@Service("IYbAppealManageViewService")
@Transactional(propagation = Propagation.SUPPORTS, readOnly = true, rollbackFor = Exception.class)
public class YbAppealManageViewServiceImpl extends ServiceImpl<YbAppealManageViewMapper, YbAppealManageView> implements IYbAppealManageViewService {

    @Autowired
    public IYbAppealConfireDataService iYbAppealConfireDataService;


    @Autowired
    public IYbReconsiderApplyService iYbReconsiderApplyService;

    @Override
    public IPage<YbAppealManageView> findYbAppealManageViews(QueryRequest request, YbAppealManageView ybAppealManageView) {
        try {
            YbReconsiderApply reconsiderApply = iYbReconsiderApplyService.findReconsiderApplyByApplyDateStrs(ybAppealManageView.getApplyDateStr(),ybAppealManageView.getAreaType());
            Page<YbAppealManageView> page = new Page<>();
            if (reconsiderApply != null) {
                ybAppealManageView.setPid(reconsiderApply.getId());
                boolean isLike = false;
                if (ybAppealManageView.getCurrencyField() != null && ybAppealManageView.getCurrencyField() != "") {
                    isLike = true;
                }
                int count = 0;
                if (isLike) {
                    count = this.baseMapper.findAppealManageLikeCount(ybAppealManageView, null);
                } else {
                    count = this.baseMapper.findAppealManageCount(ybAppealManageView, null);
                }
                if (count > 0) {
                    page.setSearchCount(false);
                    SortUtil.handlePageSort(request, page, false);//true 是属性  false是数据库字段可两个
                    IPage<YbAppealManageView> pg = null;
                    if (isLike) {
                        pg = this.baseMapper.findAppealManageLikeView(page, ybAppealManageView, null);
                    } else {
                        pg = this.baseMapper.findAppealManageView(page, ybAppealManageView, null);
                    }
                    pg.setTotal(count);
                    return pg;
                }
            }
            return page;

        } catch (Exception e) {
            log.error("获取字典信息失败", e);
            return null;
        }
    }

    @Override
    public IPage<YbAppealManageView> findAppealManageUserViews(QueryRequest request, YbAppealManageView ybAppealManageView) {
        try {
            YbReconsiderApply reconsiderApply = iYbReconsiderApplyService.findReconsiderApplyByApplyDateStrs(ybAppealManageView.getApplyDateStr(),ybAppealManageView.getAreaType());
            Page<YbAppealManageView> page = new Page<>();
            if (reconsiderApply != null) {
                ybAppealManageView.setPid(reconsiderApply.getId());
                boolean isLike = false;
                if (ybAppealManageView.getCurrencyField() != null && ybAppealManageView.getCurrencyField() != "") {
                    isLike = true;
                }
                int count = 0;
                if (isLike) {
                    count = this.baseMapper.findAppealManageLikeCount(ybAppealManageView, null);
                } else {
                    count = this.baseMapper.findAppealManageCount(ybAppealManageView, null);
                }
                if (count > 0) {
                    page.setSearchCount(false);
                    SortUtil.handlePageSort(request, page, false);//true 是属性  false是数据库字段可两个
                    IPage<YbAppealManageView> pg = null;
                    if (isLike) {
                        pg = this.baseMapper.findAppealManageLikeView(page, ybAppealManageView, null);
                    } else {
                        pg = this.baseMapper.findAppealManageView(page, ybAppealManageView, null);
                    }
                    pg.setTotal(count);
                    return pg;
                }
            }
            return page;

        } catch (Exception e) {
            log.error("获取字典信息失败", e);
            return null;
        }
    }

    @Override
    public IPage<YbAppealManageView> findAppealManageOperateRoomViews(QueryRequest request, YbAppealManageView ybAppealManageView) {
        try {
            YbReconsiderApply reconsiderApply = iYbReconsiderApplyService.findReconsiderApplyByApplyDateStrs(ybAppealManageView.getApplyDateStr(),ybAppealManageView.getAreaType());
            Page<YbAppealManageView> page = new Page<>();
            if (reconsiderApply != null) {
                ybAppealManageView.setPid(reconsiderApply.getId());
//                未完成
                ybAppealManageView.setReadyDeptName("手术室");
                boolean isLike = false;
                if (ybAppealManageView.getCurrencyField() != null && ybAppealManageView.getCurrencyField() != "") {
                    isLike = true;
                }
                int count = 0;
                if (isLike) {
                    count = this.baseMapper.findAppealManageLikeCount(ybAppealManageView, null);
                } else {
                    count = this.baseMapper.findAppealManageCount(ybAppealManageView, null);
                }
                if (count > 0) {
                    page.setSearchCount(false);
                    SortUtil.handlePageSort(request, page, false);//true 是属性  false是数据库字段可两个
                    IPage<YbAppealManageView> pg = null;
                    if (isLike) {
                        pg = this.baseMapper.findAppealManageLikeView(page, ybAppealManageView, null);
                    } else {
                        pg = this.baseMapper.findAppealManageView(page, ybAppealManageView, null);
                    }
                    pg.setTotal(count);
                    return pg;
                }
            }
            return page;
        } catch (Exception e) {
            log.error("获取字典信息失败", e);
            return null;
        }
    }

    @Override
    public IPage<YbAppealManageView> findAppealManageConfireViews(QueryRequest request, YbAppealManageView ybAppealManageView, User currentUser) {
        try {
            YbReconsiderApply reconsiderApply = iYbReconsiderApplyService.findReconsiderApplyByApplyDateStrs(ybAppealManageView.getApplyDateStr(),ybAppealManageView.getAreaType());
            Page<YbAppealManageView> page = new Page<>();
            if (reconsiderApply != null) {
                List<YbAppealConfireData> acdlist = iYbAppealConfireDataService.findAppealConfireDataByInDoctorCodeList(currentUser.getUsername(),ybAppealManageView.getAreaType());
                if (acdlist.size() > 0) {
                    String appealConfireId = acdlist.get(0).getPid();
                    ybAppealManageView.setPid(reconsiderApply.getId());
                    boolean isLike = false;
                    if (ybAppealManageView.getCurrencyField() != null && ybAppealManageView.getCurrencyField() != "") {
                        isLike = true;
                    }
                    int count = 0;
                    if (isLike) {
                        count = this.baseMapper.findAppealManageLikeCount(ybAppealManageView, appealConfireId);
                    } else {
                        count = this.baseMapper.findAppealManageCount(ybAppealManageView, appealConfireId);
                    }
                    if (count > 0) {
                        page.setSearchCount(false);
                        SortUtil.handlePageSort(request, page, false);//true 是属性  false是数据库字段可两个
                        IPage<YbAppealManageView> pg = null;
                        if (isLike) {
                            pg = this.baseMapper.findAppealManageLikeView(page, ybAppealManageView, appealConfireId);
                        } else {
                            pg = this.baseMapper.findAppealManageView(page, ybAppealManageView, appealConfireId);
                        }
                        pg.setTotal(count);
                        return pg;
                    }
                }
            }
            return page;
        } catch (Exception e) {
            log.error("获取字典信息失败", e);
            return null;
        }
    }


    @Override
    public IPage<YbAppealManageView> findYbAppealManageViewList(QueryRequest request, YbAppealManageView ybAppealManageView) {
        try {
            Page<YbAppealManageView> page = new Page<>();
            SortUtil.handlePageSort(request, page, false);//true 是属性  false是数据库字段可两个
            return this.baseMapper.findYbAppealManageView(page, ybAppealManageView);
        } catch (Exception e) {
            log.error("获取VIEW失败", e);
            return null;
        }
    }

    @Override
    public List<YbAppealManageView> findAppealManageViewList(YbAppealManageView ybAppealManageView) {
        List<YbAppealManageView> list = new ArrayList<>();

        if (ybAppealManageView.getPid() != null) {
            return this.baseMapper.findAppealManageList(ybAppealManageView);
        } else {
            return list;
        }
    }

    @Override
    @Transactional
    public void createYbAppealManageView(YbAppealManageView ybAppealManageView) {
//        ybAppealManageView.setCreateTime(new Date());
//        ybAppealManageView.setIsDeletemark(1);
        this.save(ybAppealManageView);
    }

    @Override
    @Transactional
    public void updateYbAppealManageView(YbAppealManageView ybAppealManageView) {
//        ybAppealManageView.setModifyTime(new Date());
        this.baseMapper.updateYbAppealManageView(ybAppealManageView);
    }

    @Override
    @Transactional
    public void deleteYbAppealManageViews(String[] Ids) {
        List<String> list = Arrays.asList(Ids);
        this.baseMapper.deleteBatchIds(list);
    }


}