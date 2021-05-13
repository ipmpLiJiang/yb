package cc.mrbird.febs.common.runner;

import cc.mrbird.febs.com.manager.ComConfigureManager;
import cc.mrbird.febs.common.exception.RedisConnectException;
import cc.mrbird.febs.common.service.CacheService;
import cc.mrbird.febs.system.domain.User;
import cc.mrbird.febs.system.manager.UserManager;
import cc.mrbird.febs.system.service.UserService;

//import cc.mrbird.febs.yb.entity.YbReconsiderApply;
import cc.mrbird.febs.yb.manager.YbApplyDataManager;
//import cc.mrbird.febs.yb.service.IYbReconsiderApplyDataService;
import cc.mrbird.febs.yb.service.IYbReconsiderApplyService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * 缓存初始化
 */
@Slf4j
@Component
public class CacheInitRunner implements ApplicationRunner {

    @Autowired
    private UserService userService;

    @Autowired
    private CacheService cacheService;
    @Autowired
    private UserManager userManager;

    @Autowired
    private YbApplyDataManager ybApplyDataManager;

    @Autowired
    private ComConfigureManager configureManager;

    @Autowired
    private ConfigurableApplicationContext context;

//    @Autowired
//    private IYbReconsiderApplyService iYbReconsiderApplyService;

    @Override
    public void run(ApplicationArguments args) {
        try {
            log.info("Redis连接中 ······");
            cacheService.testConnect();

            log.info("缓存初始化 ······");
            log.info("缓存用户数据 ······");
            //服务器开启缓存User相关数据
            List<User> list = this.userService.list();
            for (User user : list) {
                userManager.loadUserRedisCache(user);
            }
            configureManager.loadgetConfigureCache(5,"area");

//            YbReconsiderApply apply = iYbReconsiderApplyService.findReconsiderApplyByApplyDateStrs("2021-02",0);
//            ybApplyDataManager.loadgetApplyDataCache(apply.getId(),apply.getApplyDateStr() + "-0");
        } catch (Exception e) {
            log.error("缓存初始化失败，{}", e.getMessage());
            log.error(" ____   __    _   _ ");
            log.error("| |_   / /\\  | | | |");
            log.error("|_|   /_/--\\ |_| |_|__");
            log.error("                        ");
            log.error("FEBS启动失败              ");
            if (e instanceof RedisConnectException)
                log.error("Redis连接异常，请检查Redis连接配置并确保Redis服务已启动");
            // 关闭 FEBS
            context.close();
        }
    }
}
