package cc.mrbird.febs.yb.service;

import cc.mrbird.febs.yb.entity.YbAppealManage;
import cc.mrbird.febs.yb.entity.YbAppealManageView;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.Date;
import java.util.List;

import com.baomidou.mybatisplus.core.metadata.IPage;
import cc.mrbird.febs.common.domain.QueryRequest;
import com.baomidou.mybatisplus.core.metadata.IPage;

/**
 * <p>
 * 服务类
 * </p>
 *
 * @author viki
 * @since 2020-07-30
 */
public interface IYbAppealManageService extends IService<YbAppealManage> {

    IPage<YbAppealManage> findYbAppealManages(QueryRequest request, YbAppealManage ybAppealManage);

    IPage<YbAppealManage> findYbAppealManageList(QueryRequest request, YbAppealManage ybAppealManage);

    void createYbAppealManage(YbAppealManage ybAppealManage);

    void updateCreateAppealManage(YbAppealManage ybAppealManage, Long uId, String Uname, Integer type);

    void updateCreateAdminAppealManage(YbAppealManage ybAppealManage, Long uId, String Uname);

    void updateYbAppealManage(YbAppealManage ybAppealManage);

    void deleteYbAppealManages(String[] Ids);

    void updateAcceptRejectStates(List<YbAppealManage> list, Long uId, String Uname);

    String updateUploadStates(YbAppealManage ybAppealManage, Long uId, String Uname);

    String updateUploadStateCompleteds(YbAppealManage ybAppealManage, Long uId, String Uname);

    List<YbAppealManage> getUpdateAppealManageList(List<YbAppealManageView> appealManageList, Date endDateOne);

    int findAppealManageResetCheckCounts(String applyDateStr,Integer areaType);
}
