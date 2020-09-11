package cc.mrbird.febs.scm.service;

import cc.mrbird.febs.scm.entity.ScmDPlan;
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
 * @since 2019-10-23
 */
public interface IScmDPlanService extends IService<ScmDPlan> {

        IPage<ScmDPlan> findScmDPlans(QueryRequest request, ScmDPlan scmDPlan);

        void createScmDPlan(ScmDPlan scmDPlan);

        void updateScmDPlan(ScmDPlan scmDPlan);

        void deleteScmDPlans(String[]Ids);
        }
