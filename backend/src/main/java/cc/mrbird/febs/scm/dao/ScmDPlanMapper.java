package cc.mrbird.febs.scm.dao;

import cc.mrbird.febs.scm.entity.ScmDPlan;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author viki
         * @since 2019-10-23
        */
public interface ScmDPlanMapper extends BaseMapper<ScmDPlan> {
        void updateScmDPlan(ScmDPlan scmDPlan);
}
