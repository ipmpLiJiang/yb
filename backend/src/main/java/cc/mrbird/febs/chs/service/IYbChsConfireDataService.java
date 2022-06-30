package cc.mrbird.febs.chs.service;

import cc.mrbird.febs.chs.entity.YbChsConfireData;
import com.baomidou.mybatisplus.extension.service.IService;

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
 * @since 2022-06-30
 */
public interface IYbChsConfireDataService extends IService<YbChsConfireData> {

    IPage<YbChsConfireData> findYbChsConfireDatas(QueryRequest request, YbChsConfireData ybChsConfireData);

    IPage<YbChsConfireData> findYbChsConfireDataList(QueryRequest request, YbChsConfireData ybChsConfireData);

    void createYbChsConfireData(YbChsConfireData ybChsConfireData);

    void updateYbChsConfireData(YbChsConfireData ybChsConfireData);

    void deleteYbChsConfireDatas(String[] Ids);

    void deleteChsConfireDataPids(String[] pIds);

    List<YbChsConfireData> findChsConfireDataByInDoctorCodeList(String doctorCode, Integer areaType);

    List<YbChsConfireData> findChsConfireDataList(YbChsConfireData chsConfireData);
}
