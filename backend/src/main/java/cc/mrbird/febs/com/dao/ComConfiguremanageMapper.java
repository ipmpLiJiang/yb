package cc.mrbird.febs.com.dao;

import cc.mrbird.febs.com.entity.ComConfiguremanage;
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
 * @since 2020-08-05
 */
public interface ComConfiguremanageMapper extends BaseMapper<ComConfiguremanage> {
        void updateComConfiguremanage(ComConfiguremanage comConfiguremanage);
        IPage<ComConfiguremanage> findComConfiguremanage(Page page, @Param("comConfiguremanage") ComConfiguremanage comConfiguremanage);
        }
