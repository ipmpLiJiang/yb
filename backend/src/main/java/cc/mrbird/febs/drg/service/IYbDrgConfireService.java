package cc.mrbird.febs.drg.service;

import cc.mrbird.febs.common.domain.QueryRequest;
import cc.mrbird.febs.system.domain.User;
import cc.mrbird.febs.drg.entity.YbDrgConfire;
import cc.mrbird.febs.drg.entity.YbDrgConfireData;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * <p>
 * 服务类
 * </p>
 *
 * @author viki
 * @since 2021-01-11
 */
public interface IYbDrgConfireService extends IService<YbDrgConfire> {

    IPage<YbDrgConfire> findYbDrgConfires(QueryRequest request, YbDrgConfire ybDrgConfire);

    IPage<YbDrgConfire> findYbDrgConfireList(QueryRequest request, YbDrgConfire ybDrgConfire);

    IPage<YbDrgConfire> findDrgConfireView(QueryRequest request, String doctorContent, Integer adminType, Integer areaType, String deptContent, String operatorName, String type);

    IPage<YbDrgConfire> findDrgConfireUserView(QueryRequest request, String doctorContent, Integer adminType, Integer areaType, String deptContent, User currentUser, String operatorName, String type);

    void createYbDrgConfire(YbDrgConfire ybDrgConfire);

    void updateYbDrgConfire(YbDrgConfire ybDrgConfire);

    void deleteYbDrgConfires(String[] Ids) throws Exception;

    void updateDrgConfire(YbDrgConfire ybDrgConfire, List<YbDrgConfireData> createDataList, List<YbDrgConfireData> updateDataList) throws Exception;

    void createDrgConfire(YbDrgConfire ybDrgConfire, List<YbDrgConfireData> createDataList) throws Exception;

    YbDrgConfire findDrgConfire(YbDrgConfire DrgConfire);

    List<YbDrgConfire> findDrgConfireATList(List<Integer> atIds, Integer areaType);
}
