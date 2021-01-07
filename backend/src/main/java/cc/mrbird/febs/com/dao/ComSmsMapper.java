package cc.mrbird.febs.com.dao;

import cc.mrbird.febs.com.entity.ComSms;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author viki
 * @since 2020-11-13
 */
public interface ComSmsMapper extends BaseMapper<ComSms> {
        void updateComSms(ComSms comSms);
        IPage<ComSms> findComSms(Page page, @Param("comSms") ComSms comSms);

        List<ComSms> findSmsTopList(@Param("comSms") ComSms comSms);

        ComSms findSmsOne(@Param("comSms") ComSms comSms);
        }
