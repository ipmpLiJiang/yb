package cc.mrbird.febs.yb.service;

import cc.mrbird.febs.yb.entity.YbAppealConfireData;
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
 * @since 2021-01-11
 */
public interface IYbAppealConfireDataService extends IService<YbAppealConfireData> {

        IPage<YbAppealConfireData> findYbAppealConfireDatas(QueryRequest request, YbAppealConfireData ybAppealConfireData);

        IPage<YbAppealConfireData> findYbAppealConfireDataList(QueryRequest request, YbAppealConfireData ybAppealConfireData);

        void createYbAppealConfireData(YbAppealConfireData ybAppealConfireData);

        void updateYbAppealConfireData(YbAppealConfireData ybAppealConfireData);

        void deleteYbAppealConfireDatas(String[]Ids);

        void deleteAppealConfireDataPids(String[] pIds);

        List<YbAppealConfireData> findAppealConfireDataByInDoctorCodeList(String doctorCode);

        List<YbAppealConfireData> findAppealConfireDataList(YbAppealConfireData appealConfireData);
        }
