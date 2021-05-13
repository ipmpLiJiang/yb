package cc.mrbird.febs.yb.manager;

import cc.mrbird.febs.common.service.CacheService;
import cc.mrbird.febs.common.utils.FebsUtil;
import cc.mrbird.febs.yb.entity.YbReconsiderApplyData;
import cc.mrbird.febs.yb.entity.YbReconsiderResetData;
import cc.mrbird.febs.yb.service.IYbReconsiderApplyDataService;
import cc.mrbird.febs.yb.service.IYbReconsiderResetDataService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 封装一些和 User相关的业务操作
 */
@Service
public class YbApplyDataManager {

    @Autowired
    CacheService cacheService;
    @Autowired
    IYbReconsiderApplyDataService iYbReconsiderApplyDataService;

    @Autowired
    IYbReconsiderResetDataService iYbReconsiderResetDataService;

    public List<YbReconsiderApplyData> getApplyDatas(String pid,String con) {
        return FebsUtil.selectCacheByTemplate(
                () -> this.cacheService.getApplyDatas(con),
                () -> this.iYbReconsiderApplyDataService.findReconsiderApplyDataList(pid,con));
    }

    public void loadgetApplyDataCache(String pid,String con) throws Exception {
        this.cacheService.saveApplyDatas(pid,con);
    }

    public void saveApplyDataCache(String pid,String con) throws Exception {
        this.cacheService.saveApplyDatas(pid,con);
    }

    public void saveApplyDataCache(List<YbReconsiderApplyData> list,String con) throws Exception {
        this.cacheService.saveApplyDatas(list,con);
    }

    public List<YbReconsiderResetData> getResetDatas(String pid, String con) {
        return FebsUtil.selectCacheByTemplate(
                () -> this.cacheService.getResetDatas(con),
                () -> this.iYbReconsiderResetDataService.findReconsiderResetDataList(pid,con));
    }

    public void saveResetDataCache(List<YbReconsiderResetData> list,String con) throws Exception {
        this.cacheService.saveResetDatas(list,con);
    }
}
