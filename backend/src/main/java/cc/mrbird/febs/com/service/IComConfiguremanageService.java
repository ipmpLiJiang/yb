package cc.mrbird.febs.com.service;

import cc.mrbird.febs.com.entity.ComConfiguremanage;
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
 * @since 2020-08-05
 */
public interface IComConfiguremanageService extends IService<ComConfiguremanage> {

        IPage<ComConfiguremanage> findComConfiguremanages(QueryRequest request, ComConfiguremanage comConfiguremanage);

        IPage<ComConfiguremanage> findComConfiguremanageList(QueryRequest request, ComConfiguremanage comConfiguremanage);

        void createComConfiguremanage(ComConfiguremanage comConfiguremanage);

        void updateComConfiguremanage(ComConfiguremanage comConfiguremanage);

        void deleteComConfiguremanages(String[]Ids);

        List<ComConfiguremanage> getConfigLists(List<Integer> ctypeList);

        int getConfigDay();

        String getConfigAreaName(int areaType);
        }
