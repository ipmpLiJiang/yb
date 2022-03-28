package cc.mrbird.febs.yb.service.impl;

import cc.mrbird.febs.common.domain.QueryRequest;
import cc.mrbird.febs.common.utils.SortUtil;
import cc.mrbird.febs.yb.dao.YbAppealResultResetViewMapper;
import cc.mrbird.febs.yb.entity.*;
import cc.mrbird.febs.yb.manager.YbApplyDataManager;
import cc.mrbird.febs.yb.service.*;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.annotations.Param;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
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
@Service("IYbAppealResultResetViewService")
@Transactional(propagation = Propagation.SUPPORTS, readOnly = true, rollbackFor = Exception.class)
public class YbAppealResultResetViewServiceImpl extends ServiceImpl<YbAppealResultResetViewMapper, YbAppealResultResetView> implements IYbAppealResultResetViewService {

    @Autowired
    IYbAppealResultResetViewService iYbAppealResultResetViewService;

    @Autowired
    IYbReconsiderResetService iYbReconsiderResetService;

    @Autowired
    IYbAppealManageService iYbAppealManageService;

    @Autowired
    IYbHandleVerifyDataViewService iYbHandleVerifyDataViewService;

    @Override
    public IPage<YbAppealResultResetView> findAppealResultResetViews(QueryRequest request, YbAppealResultResetView ybAppealResultResetView, String keyField) {
        try {
            YbReconsiderReset reconsiderReset = iYbReconsiderResetService.findReconsiderResetByApplyDateStr(ybAppealResultResetView.getApplyDateStr(), ybAppealResultResetView.getAreaType());
            Page<YbAppealResultResetView> page = new Page<>();
            if (reconsiderReset != null && reconsiderReset.getState() == 1) {
                ybAppealResultResetView.setPid(reconsiderReset.getId());
                int count = this.baseMapper.findAppealResultResetCount(ybAppealResultResetView, keyField);
                if (count > 0) {
                    page.setSearchCount(false);
                    SortUtil.handlePageSort(request, page, false);//true 是属性  false是数据库字段可两个
                    IPage<YbAppealResultResetView> pg = null;
                    pg = this.baseMapper.findAppealResultResetView(page, ybAppealResultResetView, keyField);
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
    public List<YbAppealResultResetView> findAppealResultResetLists(YbAppealResultResetView appealResultResetView) {
        YbReconsiderReset reconsiderReset = iYbReconsiderResetService.findReconsiderResetByApplyDateStr(appealResultResetView.getApplyDateStr(), appealResultResetView.getAreaType());
        if (reconsiderReset != null && reconsiderReset.getState() == 1) {
            appealResultResetView.setPid(reconsiderReset.getId());
            // 与 Result 做关联 ApplyDataId 无值 OrderNumber 是 resetData 的值
            List<YbAppealResultResetView> list = this.baseMapper.findAppealResultResetList(appealResultResetView);
//            LambdaQueryWrapper<YbAppealManage> wrapperManage = new LambdaQueryWrapper<>();
//            wrapperManage.eq(YbAppealManage::getApplyDateStr, reconsiderReset.getApplyDateStr());
//            wrapperManage.eq(YbAppealManage::getAreaType, reconsiderReset.getAreaType());
//            wrapperManage.eq(YbAppealManage::getSourceType, YbDefaultValue.SOURCETYPE_1);
//            wrapperManage.eq(YbAppealManage::getAcceptState, YbDefaultValue.ACCEPTSTATE_1);
//            List<YbAppealManage> manageList = iYbAppealManageService.list(wrapperManage);
//            YbAppealManage query = new YbAppealManage();
//            query.setApplyDateStr(reconsiderReset.getApplyDateStr());
//            query.setAreaType(reconsiderReset.getAreaType());
//            query.setSourceType(YbDefaultValue.SOURCETYPE_1);
//            List<Integer> asList = new ArrayList<>();
//            asList.add(6);
//            List<YbAppealManage> manageList = iYbAppealManageService.findAppealManageBySoutInActList(query,asList);
//            List<YbAppealManage> queryManageList = new ArrayList<>();
//            if(manageList.size() > 0) {
//                for (YbAppealResultResetView item : list) {
//                    queryManageList = manageList.stream().filter(s -> s.getApplyDataId().equals(item.getApplyDataId())).collect(Collectors.toList());
//                    if (queryManageList.size() > 0) {
//                        if (item.getArDoctorCode() == null) {
//                            item.setArDoctorCode(queryManageList.get(0).getReadyDoctorCode());
//                            item.setArDoctorName(queryManageList.get(0).getReadyDoctorName());
//                            item.setArDeptCode(queryManageList.get(0).getReadyDeptCode());
//                            item.setArDeptName(queryManageList.get(0).getReadyDeptName());
//                        }
////                        item.setProposalCode(queryManageList.get(0).getComments());
//                    }
//                }
//            }

            if (list.size() > 0) {
                List<YbHandleVerifyDataView> verifyDataList = iYbHandleVerifyDataViewService.findHVerifyAndManageDataList(reconsiderReset.getApplyDateStr(), reconsiderReset.getAreaType());
                if (verifyDataList.size() > 0) {
                    List<YbHandleVerifyDataView> queryList = new ArrayList<>();
                    for (YbAppealResultResetView item : list) {
                        queryList = verifyDataList.stream().filter(s ->
                                s.getRelatelDataId().equals(item.getRelatelDataId())).collect(Collectors.toList());
                        if (queryList.size() > 0 && item.getArDoctorCode() == null) {
                            item.setArDoctorCode(queryList.get(0).getHvDoctorCode());
                            item.setArDoctorName(queryList.get(0).getHvDoctorName());
                            item.setArDeptCode(queryList.get(0).getHvDeptCode());
                            item.setArDeptName(queryList.get(0).getHvDeptName());
                        }
                    }
                }
            }

            return list;
        }
        return new ArrayList<>();
    }
}