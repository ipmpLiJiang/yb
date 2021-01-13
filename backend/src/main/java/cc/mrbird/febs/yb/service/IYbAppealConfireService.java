package cc.mrbird.febs.yb.service;

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
 *  服务类
 * </p>
 *
 * @author viki
 * @since 2021-01-11
 */
public interface IYbAppealConfireService extends IService<YbAppealConfire> {

        IPage<YbAppealConfire> findYbAppealConfires(QueryRequest request, YbAppealConfire ybAppealConfire);

        IPage<YbAppealConfire> findYbAppealConfireList(QueryRequest request, YbAppealConfire ybAppealConfire);

        IPage<YbAppealConfire>  findAppealConfireView(QueryRequest request, String doctorContent, Integer adminType, String deptContent);

        void createYbAppealConfire(YbAppealConfire ybAppealConfire);

        void updateYbAppealConfire(YbAppealConfire ybAppealConfire);

        void deleteYbAppealConfires(String[]Ids);

        void updateAppealConfire(YbAppealConfire ybAppealConfire, List<YbAppealConfireData> createDataList, List<YbAppealConfireData> updateDataList);

        void createAppealConfire(YbAppealConfire ybAppealConfire, List<YbAppealConfireData> createDataList);

        YbAppealConfire findAppealConfire(YbAppealConfire appealConfire);
        }
