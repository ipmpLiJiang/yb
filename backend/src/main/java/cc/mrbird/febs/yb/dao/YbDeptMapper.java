package cc.mrbird.febs.yb.dao;

import cc.mrbird.febs.yb.entity.YbDept;
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
 * @since 2020-07-21
 */
public interface YbDeptMapper extends BaseMapper<YbDept> {
        void updateYbDept(YbDept ybDept);
        IPage<YbDept> findYbDept(Page page, @Param("ybDept") YbDept ybDept);
        }
