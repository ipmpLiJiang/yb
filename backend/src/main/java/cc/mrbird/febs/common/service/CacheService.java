package cc.mrbird.febs.common.service;

import cc.mrbird.febs.com.entity.ComConfiguremanage;
import cc.mrbird.febs.com.entity.ComType;
import cc.mrbird.febs.common.domain.FebsConstant;
import cc.mrbird.febs.system.domain.Menu;
import cc.mrbird.febs.system.domain.Role;
import cc.mrbird.febs.system.domain.User;
import cc.mrbird.febs.system.domain.UserConfig;
import cc.mrbird.febs.yb.entity.*;
import org.apache.commons.lang3.StringUtils;

import java.util.List;

public interface CacheService {

    /**
     * 测试 Redis是否连接成功
     */
    void testConnect() throws Exception;

    /**
     * 从缓存中获取用户
     *
     * @param username 用户名
     * @return User
     */
    User getUser(String username) throws Exception;

    /**
     * 从缓存中获取用户角色
     *
     * @param username 用户名
     * @return 角色集
     */
    List<Role> getRoles(String username) throws Exception;

    /**
     * 从缓存中获取用户权限
     *
     * @param username 用户名
     * @return 权限集
     */
    List<Menu> getPermissions(String username) throws Exception;

    /**
     * 从缓存中获取用户个性化配置
     *
     * @param userId 用户 ID
     * @return 个性化配置
     */
    UserConfig getUserConfig(String userId) throws Exception;

    /**
     * 缓存用户信息，只有当用户信息是查询出来的，完整的，才应该调用这个方法
     * 否则需要调用下面这个重载方法
     *
     * @param user 用户信息
     */
    void saveUser(User user) throws Exception;

    /**
     * 缓存用户信息
     *
     * @param username 用户名
     */
    void saveUser(String username) throws Exception;

    /**
     * 缓存用户角色信息
     *
     * @param username 用户名
     */
    void saveRoles(String username) throws Exception;

    /**
     * 缓存用户权限信息
     *
     * @param username 用户名
     */
    void savePermissions(String username) throws Exception;

    /**
     * 缓存用户个性化配置
     *
     * @param userId 用户 ID
     */
    void saveUserConfigs(String userId) throws Exception;

    /**
     * 删除用户信息
     *
     * @param username 用户名
     */
    void deleteUser(String username) throws Exception;

    /**
     * 删除用户角色信息
     *
     * @param username 用户名
     */
    void deleteRoles(String username) throws Exception;

    /**
     * 删除用户权限信息
     *
     * @param username 用户名
     */
    void deletePermissions(String username) throws Exception;

    /**
     * 删除用户个性化配置
     *
     * @param userId 用户 ID
     */
    void deleteUserConfigs(String userId) throws Exception;

    void saveConfigures(int ctType,String con) throws Exception;

    List<ComConfiguremanage> getConfigures(int ctType,String con) throws Exception;

    void deleteConfigures(String con) throws Exception;


//    YbPerson getPerson(String personCode) throws Exception;
//    void savePerson(YbPerson person) throws Exception;
//    void savePerson(String personCode) throws Exception;
//    void deletePerson(String personCode) throws Exception;

    void savePersons(String con) throws Exception;
    List<YbPerson> getPersons(String con) throws Exception;
    void deletePersons(String con) throws Exception;

    void saveDepts(String con) throws Exception;
    List<YbDept> getDepts(String con) throws Exception;
    void deleteDepts(String con) throws Exception;

    void saveApplyDatas(String pid,String con) throws Exception;

    void deleteApplyDatas(String con) throws Exception;

    List<YbReconsiderApplyData> getApplyDatas(String con) throws Exception;

    void saveApplyDatas(List<YbReconsiderApplyData> list,String con) throws Exception;

    void saveResetDatas(List<YbReconsiderResetData> list, String con) throws Exception;
    void deleteResetDatas(String con) throws Exception;
    List<YbReconsiderResetData> getResetDatas(String con) throws Exception;
}
