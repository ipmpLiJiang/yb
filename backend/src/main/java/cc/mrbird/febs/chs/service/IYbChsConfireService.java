package cc.mrbird.febs.chs.service;

import cc.mrbird.febs.chs.entity.YbChsConfire;
import cc.mrbird.febs.chs.entity.YbChsConfireData;
import cc.mrbird.febs.system.domain.User;
import com.baomidou.mybatisplus.extension.service.IService;
import java.util.List;

import com.baomidou.mybatisplus.core.metadata.IPage;
import cc.mrbird.febs.common.domain.QueryRequest;
import com.baomidou.mybatisplus.core.metadata.IPage;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author viki
 * @since 2022-06-30
 */
public interface IYbChsConfireService extends IService<YbChsConfire> {

        IPage<YbChsConfire> findYbChsConfires(QueryRequest request, YbChsConfire ybChsConfire);

        IPage<YbChsConfire> findYbChsConfireList(QueryRequest request, YbChsConfire ybChsConfire);

        void createYbChsConfire(YbChsConfire ybChsConfire);

        void updateYbChsConfire(YbChsConfire ybChsConfire);

        void deleteYbChsConfires(String[]Ids) throws Exception;

        void createChsConfire(YbChsConfire ybChsConfire, List<YbChsConfireData> createDataList) throws Exception;

        void updateChsConfire(YbChsConfire ybChsConfire, List<YbChsConfireData> createDataList, List<YbChsConfireData> updateDataList) throws Exception;

        YbChsConfire findChsConfire(YbChsConfire appealConfire);

        IPage<YbChsConfire> findChsConfireView(QueryRequest request, String doctorContent, Integer adminType,Integer areaType, String deptContent,String operatorName,String type);

        IPage<YbChsConfire> findChsConfireUserView(QueryRequest request, String doctorContent, Integer adminType, Integer areaType, String deptContent, User currentUser, String operatorName, String type);
}
