package cc.mrbird.febs.yb.service;

import cc.mrbird.febs.yb.entity.YbReconsiderInpatientfees;
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
 * @since 2020-07-22
 */
public interface IYbReconsiderInpatientfeesService extends IService<YbReconsiderInpatientfees> {

        IPage<YbReconsiderInpatientfees> findYbReconsiderInpatientfeess(QueryRequest request, YbReconsiderInpatientfees ybReconsiderInpatientfees);

        IPage<YbReconsiderInpatientfees> findYbReconsiderInpatientfeesList(QueryRequest request, YbReconsiderInpatientfees ybReconsiderInpatientfees);

        IPage<YbReconsiderInpatientfees> findYbReconsiderInpatientfeesEqs(QueryRequest request, YbReconsiderInpatientfees ybReconsiderInpatientfees);

        void createYbReconsiderInpatientfees(YbReconsiderInpatientfees ybReconsiderInpatientfees);

        void updateYbReconsiderInpatientfees(YbReconsiderInpatientfees ybReconsiderInpatientfees);

        void deleteYbReconsiderInpatientfeess(String[]Ids);
        }
