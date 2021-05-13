package cc.mrbird.febs.yb.manager;

import cc.mrbird.febs.common.service.CacheService;
import cc.mrbird.febs.common.utils.FebsUtil;
import cc.mrbird.febs.yb.entity.YbDept;
import cc.mrbird.febs.yb.entity.YbPerson;
import cc.mrbird.febs.yb.service.IYbDeptService;
import cc.mrbird.febs.yb.service.IYbPersonService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 封装一些和 User相关的业务操作
 */
@Service
public class YbManager {

    @Autowired
    private CacheService cacheService;
    @Autowired
    private IYbPersonService iYbPersonService;
    @Autowired
    private IYbDeptService iYbDeptService;

    public List<YbPerson> getPersons(String con) {
        return FebsUtil.selectCacheByTemplate(
                () -> this.cacheService.getPersons(con),
                () -> this.iYbPersonService.list());
    }


    public List<YbDept> getDepts(String con) {
        return FebsUtil.selectCacheByTemplate(
                () -> this.cacheService.getDepts(con),
                () -> this.iYbDeptService.list());
    }

    public void savePersonCache(String con) throws Exception {
        // 缓存医生
        this.cacheService.savePersons(con);
    }

    public void saveDeptCache(String con) throws Exception {
        // 缓存科室
        this.cacheService.saveDepts(con);
    }
}
