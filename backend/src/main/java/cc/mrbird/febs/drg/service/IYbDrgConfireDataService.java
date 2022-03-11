package cc.mrbird.febs.drg.service;

import cc.mrbird.febs.common.domain.QueryRequest;
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
public interface IYbDrgConfireDataService extends IService<YbDrgConfireData> {

    IPage<YbDrgConfireData> findYbDrgConfireDatas(QueryRequest request, YbDrgConfireData ybDrgConfireData);

    IPage<YbDrgConfireData> findYbDrgConfireDataList(QueryRequest request, YbDrgConfireData ybDrgConfireData);

    void createYbDrgConfireData(YbDrgConfireData ybDrgConfireData);

    void updateYbDrgConfireData(YbDrgConfireData ybDrgConfireData);

    void deleteYbDrgConfireDatas(String[] Ids);

    void deleteDrgConfireDataPids(String[] pIds);

    List<YbDrgConfireData> findDrgConfireDataByInDoctorCodeList(String doctorCode, Integer areaType);

    List<YbDrgConfireData> findDrgConfireDataList(YbDrgConfireData drgConfireData);

}
