package cc.mrbird.febs.com.manager;

import cc.mrbird.febs.com.entity.ComConfiguremanage;
import cc.mrbird.febs.com.service.IComConfiguremanageService;
import cc.mrbird.febs.common.service.CacheService;
import cc.mrbird.febs.common.utils.FebsUtil;
import cc.mrbird.febs.system.domain.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 封装一些和 User相关的业务操作
 */
@Service
public class ComConfigureManager {

    @Autowired
    private CacheService cacheService;
    @Autowired
    private IComConfiguremanageService iComConfiguremanageService;


    /**
     * 通过类型和Key值获取ComConfiguremanage信息
     *
     * @param ctType 类型
     * @param con 键
     * @return ComConfigure信息
     */
    public List<ComConfiguremanage> getConfigures(int ctType,String con) {
        return FebsUtil.selectCacheByTemplate(
                () -> this.cacheService.getConfigures(ctType,con),
                () -> this.iComConfiguremanageService.getConfigLists(ctType));
    }

    /**
     * 将ComConfiguremanage相关信息添加到 Redis缓存中
     *
     * @param ctType 类型
     * @param con 键
     */
    public void loadgetConfigureCache(int ctType,String con) throws Exception {
        // 缓存ComConfigure
        this.cacheService.saveConfigures(ctType,con);
    }

    /**
     * 将ComConfiguremanage相关信息添加到 Redis缓存中
     *
     * @param ctType 类型
     * @param con 键
     */
    public void saveConfigureCache(int ctType,String con) throws Exception {
        // 缓存ComConfigure
        this.cacheService.saveConfigures(ctType,con);
    }
}
