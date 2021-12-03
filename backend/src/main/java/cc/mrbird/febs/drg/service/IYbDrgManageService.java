package cc.mrbird.febs.drg.service;

import cc.mrbird.febs.drg.entity.YbDrgManage;
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
 * @since 2021-11-23
 */
public interface IYbDrgManageService extends IService<YbDrgManage> {

        IPage<YbDrgManage> findYbDrgManages(QueryRequest request, YbDrgManage ybDrgManage);

        IPage<YbDrgManage> findYbDrgManageList(QueryRequest request, YbDrgManage ybDrgManage);

        void createYbDrgManage(YbDrgManage ybDrgManage);

        void updateYbDrgManage(YbDrgManage ybDrgManage);

        void deleteYbDrgManages(String[]Ids);

        List<YbDrgManage> findDrgManageList(YbDrgManage ybDrgManage);

        void updateAcceptRejectStates(List<YbDrgManage> list);

        String updateUploadStates(YbDrgManage ybDrgManage);

        String updateUploadStateCompleteds(YbDrgManage ybDrgManage);

        void updateCreateDrgManage(YbDrgManage ybDrgManage, Long uId, String Uname, Integer type);
        }
