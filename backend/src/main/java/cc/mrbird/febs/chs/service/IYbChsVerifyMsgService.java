package cc.mrbird.febs.chs.service;

import cc.mrbird.febs.chs.entity.YbChsVerifyMsg;
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
 * @since 2022-07-18
 */
public interface IYbChsVerifyMsgService extends IService<YbChsVerifyMsg> {

        IPage<YbChsVerifyMsg> findYbChsVerifyMsgs(QueryRequest request, YbChsVerifyMsg ybChsVerifyMsg);

        IPage<YbChsVerifyMsg> findYbChsVerifyMsgList(QueryRequest request, YbChsVerifyMsg ybChsVerifyMsg);

        void createYbChsVerifyMsg(YbChsVerifyMsg ybChsVerifyMsg);

        void updateYbChsVerifyMsg(YbChsVerifyMsg ybChsVerifyMsg);

        void deleteYbChsVerifyMsgs(String[]Ids);
        }
