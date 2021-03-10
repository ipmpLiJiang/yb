package cc.mrbird.febs.yb.service.impl;

import cc.mrbird.febs.com.controller.DataTypeHelpers;
import cc.mrbird.febs.common.domain.QueryRequest;
import cc.mrbird.febs.common.utils.SortUtil;
import cc.mrbird.febs.yb.dao.YbAppealResultViewMapper;
import cc.mrbird.febs.yb.entity.*;
import cc.mrbird.febs.yb.service.IYbAppealResultService;
import cc.mrbird.febs.yb.service.IYbAppealResultViewService;
import cc.mrbird.febs.yb.service.IYbReconsiderApplyService;
import cc.mrbird.febs.yb.service.IYbReconsiderInpatientfeesService;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.extern.slf4j.Slf4j;
import net.sf.jsqlparser.parser.CCJSqlParserUtil;
import org.apache.ibatis.annotations.Param;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

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

    @Autowired
    public IYbReconsiderApplyService iYbReconsiderApplyService;

    @Autowired
    public IYbReconsiderInpatientfeesService iYbReconsiderInpatientfeesService;

    //打包下载
    @Override
    public IPage<YbAppealResultView> findYbAppealResultViews(QueryRequest request, YbAppealResultView ybAppealResultView) {
        try {
            LambdaQueryWrapper<YbAppealResultView> queryWrapper = new LambdaQueryWrapper<>();
            String sql = "(";
            sql += " applyDateStr ='" + ybAppealResultView.getApplyDateStr() + "' ";
            if (ybAppealResultView.getTypeno() != null) {
                if (ybAppealResultView.getTypeno() == YbDefaultValue.TYPENO_1) {
                    sql += " and typeno = " + YbDefaultValue.TYPENO_1;
                } else {
                    sql += " and typeno = " + YbDefaultValue.TYPENO_2;
                }
            }
            if (ybAppealResultView.getDataType() != null) {
                if (ybAppealResultView.getDataType() == YbDefaultValue.DATATYPE_0) {
                    sql += " and dataType = " + YbDefaultValue.DATATYPE_0;
                } else {
                    sql += " and dataType = " + YbDefaultValue.DATATYPE_1;
                }
            }
            if (ybAppealResultView.getSourceType() != null) {
                sql += " and sourceType = " + ybAppealResultView.getSourceType();
            }
            if (ybAppealResultView.getState() != null) {
                if (ybAppealResultView.getState() == 12) {
                    sql += " and STATE IN (1,2)";
                }
                if (ybAppealResultView.getState() == YbDefaultValue.RESULTSTATE_1) {
                    sql += " and STATE = " + YbDefaultValue.RESULTSTATE_1;
                }
            }

            sql += ")";
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
            YbReconsiderApply reconsiderApply = iYbReconsiderApplyService.findReconsiderApplyByApplyDateStrs(ybAppealResultView.getApplyDateStr());
            Page<YbAppealResultView> page = new Page<>();
            if (reconsiderApply != null) {
//                LambdaQueryWrapper<YbAppealResultView> queryWrapper = new LambdaQueryWrapper<>();
//                queryWrapper.eq(YbAppealResultView::getApplyDateStr, ybAppealResultView.getApplyDateStr());
//
//                if (ybAppealResultView.getBillNo() != null && ybAppealResultView.getBillNo() != "") {
//                    queryWrapper.eq(YbAppealResultView::getBillNo, ybAppealResultView.getBillNo());
//                }
//
//                if (ybAppealResultView.getSerialNo() != null && ybAppealResultView.getSerialNo() != "") {
//                    queryWrapper.eq(YbAppealResultView::getSerialNo, ybAppealResultView.getSerialNo());
//                }
//
//                if (ybAppealResultView.getDataType() != null) {
//                    queryWrapper.eq(YbAppealResultView::getDataType, ybAppealResultView.getDataType());
//                }
//
//                if (ybAppealResultView.getSourceType() != null) {
//                    queryWrapper.eq(YbAppealResultView::getSourceType, ybAppealResultView.getSourceType());
//                }
//
//                if (ybAppealResultView.getState() != null) {
//                    queryWrapper.eq(YbAppealResultView::getState, ybAppealResultView.getState());
//                }
//
//                if (ybAppealResultView.getDataType() == YbDefaultValue.DATATYPE_0) {
//                    if (ybAppealResultView.getProjectCode() != null && ybAppealResultView.getProjectCode() != "") {
//                        queryWrapper.eq(YbAppealResultView::getProjectCode, ybAppealResultView.getProjectCode());
//                    }
//                    if (ybAppealResultView.getProjectName() != null && ybAppealResultView.getProjectName() != "") {
//                        queryWrapper.eq(YbAppealResultView::getProjectName, ybAppealResultView.getProjectName());
//                    }
//                } else {
//                    if (ybAppealResultView.getPersonalNo() != null && ybAppealResultView.getPersonalNo() != "") {
//                        queryWrapper.eq(YbAppealResultView::getPersonalNo, ybAppealResultView.getPersonalNo());
//                    }
//                }
//
//                if (ybAppealResultView.getRuleName() != null && ybAppealResultView.getRuleName() != "") {
//                    queryWrapper.eq(YbAppealResultView::getRuleName, ybAppealResultView.getRuleName());
//                }
//
//                if (ybAppealResultView.getId() != null && ybAppealResultView.getId() != "") {
//                    queryWrapper.eq(YbAppealResultView::getId, ybAppealResultView.getId());
//                }
//
//                if (ybAppealResultView.getRelatelDataId() != null && ybAppealResultView.getRelatelDataId() != "") {
//                    queryWrapper.eq(YbAppealResultView::getRelatelDataId, ybAppealResultView.getRelatelDataId());
//                }
//                SortUtil.handlePageSort(request, page, false);//true 是属性  false是数据库字段可两个
//                return this.page(page, queryWrapper);
                ybAppealResultView.setPid(reconsiderApply.getId());
                int count = this.baseMapper.findAppealResultResetCount(ybAppealResultView);
                if (count > 0) {
                    page.setSearchCount(false);
                    SortUtil.handlePageSort(request, page, false);//true 是属性  false是数据库字段可两个
                    IPage<YbAppealResultView> pg = this.baseMapper.findAppealResultResetView(page, ybAppealResultView);
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

    //数据剔除——>查看剔除明细
    @Override
    public IPage<YbAppealResultView> findAppealResultViewRepays(QueryRequest request, YbAppealResultView ybAppealResultView) {
        try {
            LambdaQueryWrapper<YbAppealResultView> queryWrapper = new LambdaQueryWrapper<>();

            String sql = "(";
            sql += " applyDateStr ='" + ybAppealResultView.getApplyDateStr() + "' ";
            if (ybAppealResultView.getSourceType() != null) {
                sql += " and sourceType = " + ybAppealResultView.getSourceType();
            }
            if (ybAppealResultView.getState() != null) {
                sql += " and STATE = " + ybAppealResultView.getState();
            }

            sql += ")";
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
    public IPage<YbAppealResultView> findAppealResultViews(QueryRequest request, YbAppealResultView ybAppealResultView) {
        try {
            YbReconsiderApply reconsiderApply = iYbReconsiderApplyService.findReconsiderApplyByApplyDateStrs(ybAppealResultView.getApplyDateStr());
            Page<YbAppealResultView> page = new Page<>();
            if (reconsiderApply != null) {
                ybAppealResultView.setPid(reconsiderApply.getId());
                int count = this.baseMapper.findAppealResultCount(ybAppealResultView);
                if (count > 0) {
                    page.setSearchCount(false);
                    SortUtil.handlePageSort(request, page, false);//true 是属性  false是数据库字段可两个
                    IPage<YbAppealResultView> pg = this.baseMapper.findAppealResultView(page, ybAppealResultView);
                    pg.setTotal(count);
                    return pg;
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

    @Override
    @Transactional
    public void createYbAppealResultView(YbAppealResultView ybAppealResultView) {
//        ybAppealResultView.setCreateTime(new Date());
//        ybAppealResultView.setIsDeletemark(1);
        this.save(ybAppealResultView);
    }

    @Override
    @Transactional
    public void updateYbAppealResultView(YbAppealResultView ybAppealResultView) {
//        ybAppealResultView.setModifyTime(new Date());
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
    public List<YbAppealResultView> findAppealResultHandleViewLists(@Param("ybAppealResultView") YbAppealResultView ybAppealResultView) {
        List<YbAppealResultView> list = new ArrayList<>();
        try {
            YbReconsiderApply reconsiderApply = iYbReconsiderApplyService.findReconsiderApplyByApplyDateStrs(ybAppealResultView.getApplyDateStr());
            if (reconsiderApply != null) {
                ybAppealResultView.setPid(reconsiderApply.getId());
                YbReconsiderInpatientfees rif = new YbReconsiderInpatientfees();
                rif.setApplyDateStr(reconsiderApply.getApplyDateStr());
                rif.setTypeno(ybAppealResultView.getTypeno());
                List<YbReconsiderInpatientfees> rifList = iYbReconsiderInpatientfeesService.findReconsiderInpatientfeesList(rif);
                List<YbReconsiderInpatientfees> queryRifList = new ArrayList<>();
                list = this.baseMapper.findAppealResultList(ybAppealResultView);
                for (int i = 0; i < list.size(); i++) {
                    String applyDataId = list.get(i).getApplyDataId();
                    queryRifList = rifList.stream().filter(s -> s.getApplyDataId().equals(applyDataId)).collect(Collectors.toList());
                    if (queryRifList.size() > 0) {
                        list.get(i).setOrderDeptId(queryRifList.get(0).getDeptId());
                        list.get(i).setOrderDeptName(queryRifList.get(0).getDeptName());
                        list.get(i).setOrderDocId(queryRifList.get(0).getOrderDocId());
                        list.get(i).setOrderDocName(queryRifList.get(0).getOrderDocName());
                        list.get(i).setExcuteDeptId(queryRifList.get(0).getExcuteDeptId());
                        list.get(i).setExcuteDeptName(queryRifList.get(0).getExcuteDeptName());
                        list.get(i).setExcuteDocId(queryRifList.get(0).getExcuteDocId());
                        list.get(i).setExcuteDocName(queryRifList.get(0).getExcuteDocName());
                        list.get(i).setAttendDocId(queryRifList.get(0).getAttendDocId());
                        list.get(i).setAttendDocName(queryRifList.get(0).getAttendDocName());
                        list.get(i).setItemTypeCode(queryRifList.get(0).getItemTypeCode());
                        list.get(i).setItemTypeName(queryRifList.get(0).getItemTypeName());
                        list.get(i).setFeeOperatorId(queryRifList.get(0).getFeeOperatorId());
                        list.get(i).setFeeOperatorName(queryRifList.get(0).getFeeOperatorName());
                        list.get(i).setFeeDeptId(queryRifList.get(0).getFeeDeptId());
                        list.get(i).setFeeDeptName(queryRifList.get(0).getFeeDeptName());
                    }
                }
            }
            return list;
        } catch (Exception e) {
            log.error("获取字典信息失败", e);
            return list;
        }
    }

    //打包下载导出列表
    @Override
    public List<YbAppealResultView> findAppealResultViewLists(YbAppealResultView ybAppealResultView) {
        List<YbAppealResultView> list = new ArrayList<>();
        try {
            /*
            LambdaQueryWrapper<YbAppealResultView> queryWrapper = new LambdaQueryWrapper<>();
            String sql = "";
            sql += " applyDateStr ='" + ybAppealResultView.getApplyDateStr() + "'";
            if (ybAppealResultView.getTypeno() != null) {
                if (ybAppealResultView.getTypeno() == YbDefaultValue.TYPENO_1) {
                    sql += " and typeno = " + YbDefaultValue.TYPENO_1;
                } else if (ybAppealResultView.getTypeno() == YbDefaultValue.TYPENO_2) {
                    sql += " and typeno = " + YbDefaultValue.TYPENO_2;
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
                sql += " and sourceType = " + ybAppealResultView.getSourceType();
            }

            if (ybAppealResultView.getArDeptName() != null) {
                sql += " and arDeptName = '" + ybAppealResultView.getArDeptName() + "'";
            }

            queryWrapper.apply(sql);
            list = this.baseMapper.selectList(queryWrapper);

            return list;
            */
            YbReconsiderApply reconsiderApply = iYbReconsiderApplyService.findReconsiderApplyByApplyDateStrs(ybAppealResultView.getApplyDateStr());
            if (reconsiderApply != null) {
                ybAppealResultView.setPid(reconsiderApply.getId());
                YbReconsiderInpatientfees rif = new YbReconsiderInpatientfees();
                rif.setApplyDateStr(reconsiderApply.getApplyDateStr());
                rif.setTypeno(ybAppealResultView.getTypeno());
                List<YbReconsiderInpatientfees> rifList = iYbReconsiderInpatientfeesService.findReconsiderInpatientfeesList(rif);
                List<YbReconsiderInpatientfees> queryRifList = new ArrayList<>();
                list = this.baseMapper.findAppealResultList(ybAppealResultView);
                for (int i = 0; i < list.size(); i++) {
                    String applyDataId = list.get(i).getApplyDataId();
                    queryRifList = rifList.stream().filter(s -> s.getApplyDataId().equals(applyDataId)).collect(Collectors.toList());
                    if (queryRifList.size() > 0) {
                        list.get(i).setOrderDeptId(queryRifList.get(0).getDeptId());
                        list.get(i).setOrderDeptName(queryRifList.get(0).getDeptName());
                        list.get(i).setOrderDocId(queryRifList.get(0).getOrderDocId());
                        list.get(i).setOrderDocName(queryRifList.get(0).getOrderDocName());
                        list.get(i).setExcuteDeptId(queryRifList.get(0).getExcuteDeptId());
                        list.get(i).setExcuteDeptName(queryRifList.get(0).getExcuteDeptName());
                        list.get(i).setExcuteDocId(queryRifList.get(0).getExcuteDocId());
                        list.get(i).setExcuteDocName(queryRifList.get(0).getExcuteDocName());
                        list.get(i).setAttendDocId(queryRifList.get(0).getAttendDocId());
                        list.get(i).setAttendDocName(queryRifList.get(0).getAttendDocName());
                        list.get(i).setItemTypeCode(queryRifList.get(0).getItemTypeCode());
                        list.get(i).setItemTypeName(queryRifList.get(0).getItemTypeName());
                        list.get(i).setFeeOperatorId(queryRifList.get(0).getFeeOperatorId());
                        list.get(i).setFeeOperatorName(queryRifList.get(0).getFeeOperatorName());
                        list.get(i).setFeeDeptId(queryRifList.get(0).getFeeDeptId());
                        list.get(i).setFeeDeptName(queryRifList.get(0).getFeeDeptName());
                    }
                }
            }
            return list;
        } catch (Exception e) {
            log.error("获取字典信息失败", e);
            return list;
        }
    }

    //打包下载 查找部门
    @Override
    public List<YbAppealResultDownLoad> findYbAppealResultDownLoadList(YbAppealResultView ybAppealResultView) {
        List<YbAppealResultDownLoad> downLoadList = new ArrayList<>();
        try {
            YbReconsiderApply reconsiderApply = iYbReconsiderApplyService.findReconsiderApplyByApplyDateStrs(ybAppealResultView.getApplyDateStr());
            if (reconsiderApply != null) {
                ybAppealResultView.setPid(reconsiderApply.getId());
                int typeNo = 0;

                if (ybAppealResultView.getTypeno() != null) {
                    typeNo = ybAppealResultView.getTypeno();
                }
                int dataType = ybAppealResultView.getDataType();
                // typeNo 可能为空
                List<String> deptList = this.iYbAppealResultService.findAppealResultGroupDepts(ybAppealResultView);

                String f = "";
                if (dataType == YbDefaultValue.DATATYPE_0) {
                    f = "明细扣款_";
                } else {
                    f = "主单扣款_";
                }

                String typeName = "";
                if (typeNo == YbDefaultValue.TYPENO_1) {
                    typeName = "_第一版_" + f;
                } else if (typeNo == YbDefaultValue.TYPENO_2) {
                    typeName = "_第二版_" + f;
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
            }
            return downLoadList;
        } catch (Exception e) {
            log.error("获取字典信息失败", e);
            return downLoadList;
        }
    }


}