package cc.mrbird.febs.chs.service;

import cc.mrbird.febs.chs.entity.YbChsVerify;
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
 * @since 2022-06-20
 */
public interface IYbChsVerifyService extends IService<YbChsVerify> {

        IPage<YbChsVerify> findYbChsVerifys(QueryRequest request, YbChsVerify ybChsVerify);

        IPage<YbChsVerify> findYbChsVerifyList(QueryRequest request, YbChsVerify ybChsVerify);

        void createYbChsVerify(YbChsVerify ybChsVerify);

        void updateYbChsVerify(YbChsVerify ybChsVerify);

        void deleteYbChsVerifys(String[]Ids);
        }
