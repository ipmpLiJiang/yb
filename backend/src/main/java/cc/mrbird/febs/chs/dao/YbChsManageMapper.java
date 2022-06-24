package cc.mrbird.febs.chs.dao;

import cc.mrbird.febs.chs.entity.YbChsManage;
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
 * @since 2022-06-20
 */
public interface YbChsManageMapper extends BaseMapper<YbChsManage> {
        void updateYbChsManage(YbChsManage ybChsManage);
        IPage<YbChsManage> findYbChsManage(Page page, @Param("ybChsManage") YbChsManage ybChsManage);
        }
