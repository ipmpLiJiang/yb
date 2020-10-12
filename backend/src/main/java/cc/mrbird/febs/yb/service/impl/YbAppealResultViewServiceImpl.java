package cc.mrbird.febs.yb.service.impl;

import cc.mrbird.febs.com.controller.DataTypeHelpers;
import cc.mrbird.febs.common.domain.QueryRequest;
import cc.mrbird.febs.common.utils.SortUtil;
import cc.mrbird.febs.yb.dao.YbAppealResultViewMapper;
import cc.mrbird.febs.yb.entity.YbAppealResultDownLoad;
import cc.mrbird.febs.yb.entity.YbAppealResultView;
import cc.mrbird.febs.yb.service.IYbAppealResultService;
import cc.mrbird.febs.yb.service.IYbAppealResultViewService;
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
 * @since 2020-08-12
 */
@Slf4j
@Service("IYbAppealResultViewService")
@Transactional(propagation = Propagation.SUPPORTS, readOnly = true, rollbackFor = Exception.class)
public class YbAppealResultViewServiceImpl extends ServiceImpl<YbAppealResultViewMapper, YbAppealResultView> implements IYbAppealResultViewService {

    @Autowired
    public IYbAppealResultService iYbAppealResultService;

    //打包下载
    @Override
    public IPage<YbAppealResultView> findYbAppealResultViews(QueryRequest request, YbAppealResultView ybAppealResultView) {
        try {
            LambdaQueryWrapper<YbAppealResultView> queryWrapper = new LambdaQueryWrapper<>();
            String sql = "(";
            sql += " applyDateStr ='" + ybAppealResultView.getApplyDateStr() + "' ";
            if (ybAppealResultView.getTypeno() != null) {
                if (ybAppealResultView.getTypeno() == 1) {
                    sql +=  " and typeno = 1";
                } else {
                    sql +=  " and typeno = 2";
                }
            }
            if (ybAppealResultView.getDataType() != null) {
                if (ybAppealResultView.getDataType() == 0) {
                    sql +=  " and dataType = 0";
                } else {
                    sql +=  " and dataType = 1";
                }
            }
            if (ybAppealResultView.getSourceType() != null) {
                sql +=  " and sourceType = " + ybAppealResultView.getSourceType();
            }
            if (ybAppealResultView.getState() != null) {
                if (ybAppealResultView.getState() == 12) {
                    sql +=  " and STATE IN (1,2)";
                }
                if (ybAppealResultView.getState() == 1) {
                    sql +=  " and STATE = 1";
                }
            }

            sql +=  ")";
            if (ybAppealResultView.getCurrencyField() != null && !"".equals(ybAppealResultView.getCurrencyField())) {
                if (ybAppealResultView.getDataType() != null) {
                    if (ybAppealResultView.getDataType() == 0) {
                        sql += " and (serialNo like'%" + ybAppealResultView.getCurrencyField() + "%'" +
                                " or billNo like'%" + ybAppealResultView.getCurrencyField() + "%'" +
                                " or proposalCode like'%" + ybAppealResultView.getCurrencyField() + "%'" +
                                " or projectCode like'%" + ybAppealResultView.getCurrencyField() + "%'" +
                                " or projectName like'%" + ybAppealResultView.getCurrencyField() + "%'" +
                                " or ruleName like'%" + ybAppealResultView.getCurrencyField() + "%'" +
                                " or deductReason like'%" + ybAppealResultView.getCurrencyField() + "%')";
                    } else {
                        sql += " and (serialNo like'%" + ybAppealResultView.getCurrencyField() + "%'" +
                                " or billNo like'%" + ybAppealResultView.getCurrencyField() + "%'" +
                                " or ruleName like'%" + ybAppealResultView.getCurrencyField() + "%'" +
                                " or personalNo like'%" + ybAppealResultView.getCurrencyField() + "%')";
                    }
                } else {
                    sql += " and (serialNo like'%" + ybAppealResultView.getCurrencyField() + "%'" +
                            " or billNo like'%" + ybAppealResultView.getCurrencyField() + "%'" +
                            " or proposalCode like'%" + ybAppealResultView.getCurrencyField() + "%'" +
                            " or projectCode like'%" + ybAppealResultView.getCurrencyField() + "%'" +
                            " or projectName like'%" + ybAppealResultView.getCurrencyField() + "%'" +
                            " or ruleName like'%" + ybAppealResultView.getCurrencyField() + "%'" +
                            " or deductReason like'%" + ybAppealResultView.getCurrencyField() + "%')";
                }
            }
            queryWrapper.apply(sql);

            Page<YbAppealResultView> page = new Page<>();
            SortUtil.handlePageSort(request, page, false);//true 是属性  false是数据库字段可两个
            return this.page(page, queryWrapper);
        } catch (Exception e) {
            log.error("获取字典信息失败", e);
            return null;
        }
    }

    //还款-查看还款明细
    @Override
    public IPage<YbAppealResultView> findAppealResultViewResets(QueryRequest request, YbAppealResultView ybAppealResultView) {
        try {
            LambdaQueryWrapper<YbAppealResultView> queryWrapper = new LambdaQueryWrapper<>();
            queryWrapper.eq(YbAppealResultView::getApplyDateStr, ybAppealResultView.getApplyDateStr());
            queryWrapper.eq(YbAppealResultView::getBillNo, ybAppealResultView.getBillNo());
            queryWrapper.eq(YbAppealResultView::getSerialNo, ybAppealResultView.getSerialNo());
            queryWrapper.eq(YbAppealResultView::getRuleName, ybAppealResultView.getRuleName());
            queryWrapper.eq(YbAppealResultView::getDataType, ybAppealResultView.getDataType());
            queryWrapper.eq(YbAppealResultView::getSourceType, ybAppealResultView.getSourceType());
            queryWrapper.eq(YbAppealResultView::getState, ybAppealResultView.getState());
            if (ybAppealResultView.getDataType() == 0) {
                queryWrapper.eq(YbAppealResultView::getProjectCode, ybAppealResultView.getProjectCode());
                queryWrapper.eq(YbAppealResultView::getProjectName, ybAppealResultView.getProjectName());
            } else {
                queryWrapper.eq(YbAppealResultView::getPersonalNo, ybAppealResultView.getPersonalNo());
            }

            Page<YbAppealResultView> page = new Page<>();
            SortUtil.handlePageSort(request, page, false);//true 是属性  false是数据库字段可两个
            return this.page(page, queryWrapper);
        } catch (Exception e) {
            log.error("获取字典信息失败", e);
            return null;
        }
    }

    //数据剔除——>查看剔除明细
    @Override
    public IPage<YbAppealResultView> findAppealResultViewRepays(QueryRequest request, YbAppealResultView ybAppealResultView) {
        try {
            LambdaQueryWrapper<YbAppealResultView> queryWrapper = new LambdaQueryWrapper<>();

            String sql = "(";
            sql += " applyDateStr ='" + ybAppealResultView.getApplyDateStr() + "' ";
            if (ybAppealResultView.getSourceType() != null) {
                sql +=  " and sourceType = " + ybAppealResultView.getSourceType();
            }
            if (ybAppealResultView.getState() != null) {
                sql +=  " and STATE = " + ybAppealResultView.getState();
            }

            sql +=  ")";
            if (ybAppealResultView.getOrderNumber() != null && !"".equals(ybAppealResultView.getOrderNumber())) {

            }
            queryWrapper.apply(sql);

            Page<YbAppealResultView> page = new Page<>();
            SortUtil.handlePageSort(request, page, false);//true 是属性  false是数据库字段可两个
            return this.page(page, queryWrapper);
        } catch (Exception e) {
            log.error("获取字典信息失败", e);
            return null;
        }
    }

    @Override
    public IPage<YbAppealResultView> findYbAppealResultViewList(QueryRequest request, YbAppealResultView ybAppealResultView) {
        try {
            Page<YbAppealResultView> page = new Page<>();
            SortUtil.handlePageSort(request, page, false);//true 是属性  false是数据库字段可两个
            return this.baseMapper.findYbAppealResultView(page, ybAppealResultView);
        } catch (Exception e) {
            log.error("获取VIEW失败", e);
            return null;
        }
    }

    @Override
    @Transactional
    public void createYbAppealResultView(YbAppealResultView ybAppealResultView) {
        ybAppealResultView.setCreateTime(new Date());
        ybAppealResultView.setIsDeletemark(1);
        this.save(ybAppealResultView);
    }

    @Override
    @Transactional
    public void updateYbAppealResultView(YbAppealResultView ybAppealResultView) {
        ybAppealResultView.setModifyTime(new Date());
        this.baseMapper.updateYbAppealResultView(ybAppealResultView);
    }

    @Override
    @Transactional
    public void deleteYbAppealResultViews(String[] Ids) {
        List<String> list = Arrays.asList(Ids);
        this.baseMapper.deleteBatchIds(list);
    }

    //打包下载导出列表
    @Override
    public List<YbAppealResultView> findAppealResultViewLists(YbAppealResultView ybAppealResultView) {
        List<YbAppealResultView> list = new ArrayList<YbAppealResultView>();
        try {
            LambdaQueryWrapper<YbAppealResultView> queryWrapper = new LambdaQueryWrapper<>();
            String sql = "";
            sql += " applyDateStr ='" + ybAppealResultView.getApplyDateStr() + "'";
            if (ybAppealResultView.getTypeno() != null) {
                if (ybAppealResultView.getTypeno() == 1) {
                    sql += " and typeno = 1";
                } else if (ybAppealResultView.getTypeno() == 2) {
                    sql += " and typeno = 2";
                }
            }
            if (ybAppealResultView.getState() != null) {
                if (ybAppealResultView.getState() == 12) {
                    sql += " and state IN (1,2)";
                } else {
                    sql += " and state =" + ybAppealResultView.getState();
                }

            }

            if (ybAppealResultView.getId() != null) {
                sql += " and id = '" + ybAppealResultView.getId() + "'";
            }

            if (ybAppealResultView.getSourceType() != null) {
                sql +=  " and sourceType = " + ybAppealResultView.getSourceType();
            }

            if (ybAppealResultView.getArDeptName() != null) {
                sql += " and arDeptName = '" + ybAppealResultView.getArDeptName() + "'";
            }

            queryWrapper.apply(sql);
            list = this.baseMapper.selectList(queryWrapper);

            return list;
        } catch (Exception e) {
            log.error("获取字典信息失败", e);
            return list;
        }
    }

    //打包下载 查找部门
    @Override
    public List<YbAppealResultDownLoad> findYbAppealResultDownLoadList(YbAppealResultView ybAppealResultView) {
        List<YbAppealResultDownLoad> downLoadList = new ArrayList<YbAppealResultDownLoad>();
        try {
            int typeNo = 0;

            if (ybAppealResultView.getTypeno() != null) {
                typeNo = ybAppealResultView.getTypeno();
            }
            int dataType = ybAppealResultView.getDataType();
            // typeNo 可能为空
            List<String> deptList = this.iYbAppealResultService.findAppealResultGroupDepts(ybAppealResultView);

            String f = "";
            if (dataType == 0) {
                f = "明细扣款_";
            } else {
                f = "主单扣款_";
            }

            String typeName = "";
            if (typeNo == 1) {
                typeName = "_审核一_" + f;
            } else if (typeNo == 2) {
                typeName = "_审核二_" + f;
            } else {
                typeName = "_手动复议_";
            }
            int i = 1;
            for (String item : deptList) {
                YbAppealResultDownLoad downLoad = new YbAppealResultDownLoad();
                downLoad.setKey(i);
                downLoad.setDeptName(item);
                downLoad.setFileName(ybAppealResultView.getApplyDateStr() + typeName + item + ".zip");
                downLoadList.add(downLoad);
                i++;
            }
            return downLoadList;
        } catch (Exception e) {
            log.error("获取字典信息失败", e);
            return downLoadList;
        }
    }


}