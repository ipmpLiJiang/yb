package cc.mrbird.febs.yb.service.impl;

import cc.mrbird.febs.common.domain.QueryRequest;
import cc.mrbird.febs.common.utils.SortUtil;
import cc.mrbird.febs.yb.dao.YbReconsiderVerifyViewMapper;
import cc.mrbird.febs.yb.entity.YbAppealResultReportView;
import cc.mrbird.febs.yb.entity.YbDefaultValue;
import cc.mrbird.febs.yb.entity.YbReconsiderApply;
import cc.mrbird.febs.yb.entity.YbReconsiderVerifyView;
import cc.mrbird.febs.yb.service.IYbReconsiderApplyService;
import cc.mrbird.febs.yb.service.IYbReconsiderVerifyViewService;
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

/**
 * findYbReconsiderVerifyViews
 * <p>
 * VIEW 服务实现类
 * </p>
 *
 * @author viki
 * @since 2020-07-30
 */
@Slf4j
@Service("IYbReconsiderVerifyViewService")
@Transactional(propagation = Propagation.SUPPORTS, readOnly = true, rollbackFor = Exception.class)
public class YbReconsiderVerifyViewServiceImpl extends ServiceImpl<YbReconsiderVerifyViewMapper, YbReconsiderVerifyView> implements IYbReconsiderVerifyViewService {

    @Autowired
    IYbReconsiderApplyService iYbReconsiderApplyService;

    @Override
    public IPage<YbReconsiderVerifyView> findYbReconsiderVerifyViews(QueryRequest request, YbReconsiderVerifyView ybReconsiderVerifyView) {
        try {
            Page<YbReconsiderVerifyView> page = new Page<>();
            YbReconsiderApply reconsiderApply = iYbReconsiderApplyService.findReconsiderApplyByApplyDateStrs(ybReconsiderVerifyView.getApplyDateStr());
            if (reconsiderApply != null) {
                /*
                LambdaQueryWrapper<YbReconsiderVerifyView> queryWrapper = new LambdaQueryWrapper<>();
                queryWrapper.eq(YbReconsiderVerifyView::getPid, reconsiderApply.getId());
                if (ybReconsiderVerifyView.getState() != null) {
                    queryWrapper.eq(YbReconsiderVerifyView::getState, ybReconsiderVerifyView.getState());//状态
                }
                if (ybReconsiderVerifyView.getDataType() != null) {
                    queryWrapper.eq(YbReconsiderVerifyView::getDataType, ybReconsiderVerifyView.getDataType());
                }
                if (ybReconsiderVerifyView.getTypeno() != null) {
                    queryWrapper.eq(YbReconsiderVerifyView::getTypeno, ybReconsiderVerifyView.getTypeno());
                }
                SortUtil.handlePageSort(request, page, false);//true 是属性  false是数据库字段可两个
                return this.page(page, queryWrapper);*/
                ybReconsiderVerifyView.setPid(reconsiderApply.getId());
                int count = this.baseMapper.findReconsiderVerifyCount(ybReconsiderVerifyView, null);
                if (count > 0) {
                    page.setSearchCount(false);
                    SortUtil.handlePageSort(request, page, false);//true 是属性  false是数据库字段可两个
                    IPage<YbReconsiderVerifyView> pg = this.baseMapper.findReconsiderVerifyView(page, ybReconsiderVerifyView, null);
                    pg.setTotal(count);
                    return pg;
                } else {
                    return page;
                }
            }else{
                return page;
            }
        } catch (Exception e) {
            log.error("获取字典信息失败", e);
            return null;
        }
    }

    @Override
    public List<YbReconsiderVerifyView> findReconsiderVerifyViewLists(YbReconsiderVerifyView ybReconsiderVerifyView) {
        return this.baseMapper.findReconsiderVerifyViewList(ybReconsiderVerifyView);
    }

    @Override
    public IPage<YbReconsiderVerifyView> findYbReconsiderVerifyViews(QueryRequest request, YbReconsiderVerifyView ybReconsiderVerifyView, String[] searchType) {
        try {
            Page<YbReconsiderVerifyView> page = new Page<>();
            YbReconsiderApply reconsiderApply = iYbReconsiderApplyService.findReconsiderApplyByApplyDateStrs(ybReconsiderVerifyView.getApplyDateStr());
            if (reconsiderApply != null) {
                /*
                LambdaQueryWrapper<YbReconsiderVerifyView> queryWrapper = new LambdaQueryWrapper<>();
                queryWrapper.eq(YbReconsiderVerifyView::getPid, reconsiderApply.getId());
                queryWrapper.eq(YbReconsiderVerifyView::getState, ybReconsiderVerifyView.getState());//状态

                String strSearchType = "";
                if (ybReconsiderVerifyView.getProjectName() != null) {
                    strSearchType = searchType[0];
                    if (strSearchType.equals("LIKE")) {
                        queryWrapper.like(YbReconsiderVerifyView::getProjectName, ybReconsiderVerifyView.getProjectName());
                    } else if (strSearchType.equals("EQ")) {
                        queryWrapper.eq(YbReconsiderVerifyView::getProjectName, ybReconsiderVerifyView.getProjectName());
                    } else if (strSearchType.equals("NOTLIKE")) {
                        queryWrapper.notLike(YbReconsiderVerifyView::getProjectName, ybReconsiderVerifyView.getProjectName());
                    }
                }

                if (ybReconsiderVerifyView.getRuleName() != null) {
                    strSearchType = searchType[1];
                    if (strSearchType.equals("LIKE")) {
                        queryWrapper.like(YbReconsiderVerifyView::getRuleName, ybReconsiderVerifyView.getRuleName());
                    } else if (strSearchType.equals("EQ")) {
                        queryWrapper.eq(YbReconsiderVerifyView::getRuleName, ybReconsiderVerifyView.getRuleName());
                    } else if (strSearchType.equals("NOTLIKE")) {
                        queryWrapper.notLike(YbReconsiderVerifyView::getRuleName, ybReconsiderVerifyView.getRuleName());
                    }
                }

                if (ybReconsiderVerifyView.getVerifyDeptName() != null) {
                    strSearchType = searchType[2];
                    if (strSearchType.equals("LIKE")) {
                        queryWrapper.like(YbReconsiderVerifyView::getVerifyDeptName, ybReconsiderVerifyView.getVerifyDeptName());
                    } else if (strSearchType.equals("EQ")) {
                        queryWrapper.eq(YbReconsiderVerifyView::getVerifyDeptName, ybReconsiderVerifyView.getVerifyDeptName());
                    } else if (strSearchType.equals("NOTLIKE")) {
                        queryWrapper.notLike(YbReconsiderVerifyView::getVerifyDeptName, ybReconsiderVerifyView.getVerifyDeptName());
                    }
                }

                if (ybReconsiderVerifyView.getOrderNumber() != null) {
                    strSearchType = searchType[3];
                    if (strSearchType.equals("LIKE")) {
                        queryWrapper.like(YbReconsiderVerifyView::getOrderNumber, ybReconsiderVerifyView.getOrderNumber());
                    } else if (strSearchType.equals("EQ")) {
                        queryWrapper.eq(YbReconsiderVerifyView::getOrderNumber, ybReconsiderVerifyView.getOrderNumber());
                    } else if (strSearchType.equals("NOTLIKE")) {
                        queryWrapper.notLike(YbReconsiderVerifyView::getOrderNumber, ybReconsiderVerifyView.getOrderNumber());
                    }
                }

                if (ybReconsiderVerifyView.getDataType() != null) {
                    queryWrapper.eq(YbReconsiderVerifyView::getDataType, ybReconsiderVerifyView.getDataType());
                }

                if (ybReconsiderVerifyView.getTypeno() != null) {
                    queryWrapper.eq(YbReconsiderVerifyView::getTypeno, ybReconsiderVerifyView.getTypeno());
                }

                SortUtil.handlePageSort(request, page, false);//true 是属性  false是数据库字段可两个
                return this.page(page, queryWrapper);*/
                ybReconsiderVerifyView.setPid(reconsiderApply.getId());
                int count = this.baseMapper.findReconsiderVerifyCount(ybReconsiderVerifyView, searchType);
                if (count > 0) {
                    page.setSearchCount(false);
                    SortUtil.handlePageSort(request, page, false);//true 是属性  false是数据库字段可两个
                    IPage<YbReconsiderVerifyView> pg = this.baseMapper.findReconsiderVerifyView(page, ybReconsiderVerifyView, searchType);
                    pg.setTotal(count);
                    return pg;
                } else {
                    return page;
                }
            }else{
                return page;
            }
        } catch (Exception e) {
            log.error("获取字典信息失败", e);
            return null;
        }
    }

    @Override
    public IPage<YbReconsiderVerifyView> findYbReconsiderVerifyViewList(QueryRequest request, YbReconsiderVerifyView ybReconsiderVerifyView) {
        try {
            Page<YbReconsiderVerifyView> page = new Page<>();
            SortUtil.handlePageSort(request, page, false);//true 是属性  false是数据库字段可两个
            return this.baseMapper.findYbReconsiderVerifyView(page, ybReconsiderVerifyView);
        } catch (Exception e) {
            log.error("获取VIEW失败", e);
            return null;
        }
    }

    @Override
    @Transactional
    public void createYbReconsiderVerifyView(YbReconsiderVerifyView ybReconsiderVerifyView) {
//        ybReconsiderVerifyView.setCreateTime(new Date());
//        ybReconsiderVerifyView.setIsDeletemark(1);
        this.save(ybReconsiderVerifyView);
    }

    @Override
    @Transactional
    public void updateYbReconsiderVerifyView(YbReconsiderVerifyView ybReconsiderVerifyView) {
//        ybReconsiderVerifyView.setModifyTime(new Date());
        this.baseMapper.updateYbReconsiderVerifyView(ybReconsiderVerifyView);
    }

    @Override
    @Transactional
    public void deleteYbReconsiderVerifyViews(String[] Ids) {
        List<String> list = Arrays.asList(Ids);
        this.baseMapper.deleteBatchIds(list);
    }

    @Override
    public int findReconsiderVerifyApplyDateCounts(YbReconsiderVerifyView ybReconsiderVerifyView) {
        return this.baseMapper.findReconsiderVerifyApplyDateCount(ybReconsiderVerifyView);
    }

    @Override
    public IPage<YbReconsiderVerifyView> findYbReconsiderVerifyViewNulls(QueryRequest request, YbReconsiderVerifyView ybReconsiderVerifyView, String[] searchType) {
        try {
            Page<YbReconsiderVerifyView> page = new Page<>();
            YbReconsiderApply reconsiderApply = iYbReconsiderApplyService.findReconsiderApplyByApplyDateStrs(ybReconsiderVerifyView.getApplyDateStr());
            if (reconsiderApply != null) {
                ybReconsiderVerifyView.setPid(reconsiderApply.getId());
                int applyState = reconsiderApply.getState();
                int typeno = applyState == YbDefaultValue.APPLYSTATE_2 || applyState == YbDefaultValue.APPLYSTATE_3 ? YbDefaultValue.TYPENO_1 :
                        applyState == YbDefaultValue.APPLYSTATE_4 || applyState == YbDefaultValue.APPLYSTATE_5 ? YbDefaultValue.TYPENO_2 : 0;
                if (typeno == YbDefaultValue.TYPENO_1 || typeno == YbDefaultValue.TYPENO_2) {
                    YbReconsiderVerifyView rvvQv = new YbReconsiderVerifyView();
                    rvvQv.setApplyDateStr(reconsiderApply.getApplyDateStr());
                    rvvQv.setPid(reconsiderApply.getId());
                    rvvQv.setDataType(ybReconsiderVerifyView.getDataType());
                    rvvQv.setTypeno(typeno);
                    int count = this.findReconsiderVerifyApplyDateCounts(rvvQv);
                    if (count > 0) {
                        ybReconsiderVerifyView.setTypeno(typeno);
                        /*
                        SortUtil.handlePageSort(request, page, false);//true 是属性  false是数据库字段可两个
                        return this.baseMapper.findYbReconsiderVerifyViewNull(page, ybReconsiderVerifyView, searchType);
                        */
                        count = this.baseMapper.findReconsiderVerifyCountNull(ybReconsiderVerifyView, searchType);
                        if (count > 0) {
                            page.setSearchCount(false);
                            SortUtil.handlePageSort(request, page, false);//true 是属性  false是数据库字段可两个
                            IPage<YbReconsiderVerifyView> pg = this.baseMapper.findReconsiderVerifyViewNull(page, ybReconsiderVerifyView, searchType);
                            pg.setTotal(count);
                            return pg;
                        } else {
                            return page;
                        }
                    } else {
                        return page;
                    }
                } else {
                    return page;
                }
            } else {
                return page;
            }
        } catch (Exception e) {
            log.error("获取VIEW失败", e);
            return null;
        }
    }


}