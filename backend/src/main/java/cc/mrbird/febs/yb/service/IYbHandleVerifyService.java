package cc.mrbird.febs.yb.service;

import cc.mrbird.febs.yb.entity.YbHandleVerify;
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
 * @since 2020-08-28
 */
public interface IYbHandleVerifyService extends IService<YbHandleVerify> {

        IPage<YbHandleVerify> findYbHandleVerifys(QueryRequest request, YbHandleVerify ybHandleVerify);

        IPage<YbHandleVerify> findYbHandleVerifyList(QueryRequest request, YbHandleVerify ybHandleVerify);

        void createYbHandleVerify(YbHandleVerify ybHandleVerify);

        void updateYbHandleVerify(YbHandleVerify ybHandleVerify);

        void deleteYbHandleVerifys(String[]Ids);

        YbHandleVerify findYbHandleVerifyApplyDateStr(String applyDateStr,Integer areaType);
        }
