package cc.mrbird.febs.chs.dao;

import cc.mrbird.febs.chs.entity.YbChsVerifyMsg;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.Param;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author viki
 * @since 2022-07-18
 */
public interface YbChsVerifyMsgMapper extends BaseMapper<YbChsVerifyMsg> {
        void updateYbChsVerifyMsg(YbChsVerifyMsg ybChsVerifyMsg);
        IPage<YbChsVerifyMsg> findYbChsVerifyMsg(Page page, @Param("ybChsVerifyMsg") YbChsVerifyMsg ybChsVerifyMsg);
        }
