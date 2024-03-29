package cc.mrbird.febs.yb.service;

import cc.mrbird.febs.system.domain.User;
import cc.mrbird.febs.yb.entity.YbAppealConfire;
import cc.mrbird.febs.yb.entity.YbAppealConfireData;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

import com.baomidou.mybatisplus.core.metadata.IPage;
import cc.mrbird.febs.common.domain.QueryRequest;
import com.baomidou.mybatisplus.core.metadata.IPage;
import org.apache.ibatis.annotations.Param;

/**
 * <p>
 * 服务类
 * </p>
 *
 * @author viki
 * @since 2021-01-11
 */
public interface IYbAppealConfireService extends IService<YbAppealConfire> {

    IPage<YbAppealConfire> findYbAppealConfires(QueryRequest request, YbAppealConfire ybAppealConfire);

    IPage<YbAppealConfire> findYbAppealConfireList(QueryRequest request, YbAppealConfire ybAppealConfire);

    IPage<YbAppealConfire> findAppealConfireView(QueryRequest request, String doctorContent, Integer adminType,Integer areaType, String deptContent,String operatorName,String type);

    IPage<YbAppealConfire> findAppealConfireUserView(QueryRequest request, String doctorContent, Integer adminType,Integer areaType, String deptContent, User currentUser,String operatorName,String type);

    void createYbAppealConfire(YbAppealConfire ybAppealConfire);

    void updateYbAppealConfire(YbAppealConfire ybAppealConfire);

    void deleteYbAppealConfires(String[] Ids) throws Exception;

    void updateAppealConfire(YbAppealConfire ybAppealConfire, List<YbAppealConfireData> createDataList, List<YbAppealConfireData> updateDataList) throws Exception;

    void createAppealConfire(YbAppealConfire ybAppealConfire, List<YbAppealConfireData> createDataList) throws Exception;

    YbAppealConfire findAppealConfire(YbAppealConfire appealConfire);

    List<YbAppealConfire> findAppealConfireATList(List<Integer> atIds,Integer areaType);
}
