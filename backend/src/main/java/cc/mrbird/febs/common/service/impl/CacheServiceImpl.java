package cc.mrbird.febs.common.service.impl;

import cc.mrbird.febs.com.entity.ComConfiguremanage;
import cc.mrbird.febs.com.service.IComConfiguremanageService;
import cc.mrbird.febs.common.domain.FebsConstant;
import cc.mrbird.febs.common.service.CacheService;
import cc.mrbird.febs.common.service.RedisService;
import cc.mrbird.febs.system.dao.UserMapper;
import cc.mrbird.febs.system.domain.Menu;
import cc.mrbird.febs.system.domain.Role;
import cc.mrbird.febs.system.domain.User;
import cc.mrbird.febs.system.domain.UserConfig;
import cc.mrbird.febs.system.service.MenuService;
import cc.mrbird.febs.system.service.RoleService;
import cc.mrbird.febs.system.service.UserConfigService;
import cc.mrbird.febs.system.service.UserService;
import cc.mrbird.febs.yb.entity.*;
import cc.mrbird.febs.yb.service.IYbDeptService;
import cc.mrbird.febs.yb.service.IYbPersonService;
import cc.mrbird.febs.yb.service.IYbReconsiderApplyDataService;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service("cacheService")
public class CacheServiceImpl implements CacheService {

    @Autowired
    private RedisService redisService;

    @Autowired
    private RoleService roleService;

    @Autowired
    private MenuService menuService;

    @Autowired
    private UserService userService;

    @Autowired
    private UserConfigService userConfigService;

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private ObjectMapper mapper;

    @Autowired
    private IComConfiguremanageService iComConfiguremanageService;

    @Autowired
    private IYbPersonService iYbPersonService;

    @Autowired
    private IYbDeptService iYbDeptService;

    @Autowired
    private IYbReconsiderApplyDataService iYbReconsiderApplyDataService;

    @Override
    public void testConnect() throws Exception {
        this.redisService.exists("test");
    }

    @Override
    public User getUser(String username) throws Exception {
        String userString = this.redisService.get(FebsConstant.USER_CACHE_PREFIX + username);
        if (StringUtils.isBlank(userString))
            throw new Exception();
        else
            return this.mapper.readValue(userString, User.class);
    }

    @Override
    public List<Role> getRoles(String username) throws Exception {
        String roleListString = this.redisService.get(FebsConstant.USER_ROLE_CACHE_PREFIX + username);
        if (StringUtils.isBlank(roleListString)) {
            throw new Exception();
        } else {
            JavaType type = mapper.getTypeFactory().constructParametricType(List.class, Role.class);
            return this.mapper.readValue(roleListString, type);
        }
    }

    @Override
    public List<Menu> getPermissions(String username) throws Exception {
        String permissionListString = this.redisService.get(FebsConstant.USER_PERMISSION_CACHE_PREFIX + username);
        if (StringUtils.isBlank(permissionListString)) {
            throw new Exception();
        } else {
            JavaType type = mapper.getTypeFactory().constructParametricType(List.class, Menu.class);
            return this.mapper.readValue(permissionListString, type);
        }
    }

    @Override
    public UserConfig getUserConfig(String userId) throws Exception {
        String userConfigString = this.redisService.get(FebsConstant.USER_CONFIG_CACHE_PREFIX + userId);
        if (StringUtils.isBlank(userConfigString))
            throw new Exception();
        else
            return this.mapper.readValue(userConfigString, UserConfig.class);
    }

    @Override
    public void saveUser(User user) throws Exception {
        String username = user.getUsername();
        this.deleteUser(username);
        redisService.set(FebsConstant.USER_CACHE_PREFIX + username, mapper.writeValueAsString(user));
    }

    @Override
    public void saveUser(String username) throws Exception {
        User user = userMapper.findDetail(username);
        this.deleteUser(username);
        redisService.set(FebsConstant.USER_CACHE_PREFIX + username, mapper.writeValueAsString(user));
    }

    @Override
    public void saveRoles(String username) throws Exception {
        List<Role> roleList = this.roleService.findUserRole(username);
        if (!roleList.isEmpty()) {
            this.deleteRoles(username);
            redisService.set(FebsConstant.USER_ROLE_CACHE_PREFIX + username, mapper.writeValueAsString(roleList));
        }

    }

    @Override
    public void savePermissions(String username) throws Exception {
        List<Menu> permissionList = this.menuService.findUserPermissions(username);
        if (!permissionList.isEmpty()) {
            this.deletePermissions(username);
            redisService.set(FebsConstant.USER_PERMISSION_CACHE_PREFIX + username, mapper.writeValueAsString(permissionList));
        }
    }

    @Override
    public void saveUserConfigs(String userId) throws Exception {
        UserConfig userConfig = this.userConfigService.findByUserId(userId);
        if (userConfig != null) {
            this.deleteUserConfigs(userId);
            redisService.set(FebsConstant.USER_CONFIG_CACHE_PREFIX + userId, mapper.writeValueAsString(userConfig));
        }
    }

    @Override
    public void deleteUser(String username) throws Exception {
        username = username.toLowerCase();
        redisService.del(FebsConstant.USER_CACHE_PREFIX + username);
    }

    @Override
    public void deleteRoles(String username) throws Exception {
        username = username.toLowerCase();
        redisService.del(FebsConstant.USER_ROLE_CACHE_PREFIX + username);
    }

    @Override
    public void deletePermissions(String username) throws Exception {
        username = username.toLowerCase();
        redisService.del(FebsConstant.USER_PERMISSION_CACHE_PREFIX + username);
    }

    @Override
    public void deleteUserConfigs(String userId) throws Exception {
        redisService.del(FebsConstant.USER_CONFIG_CACHE_PREFIX + userId);
    }

    @Override
    public void saveConfigures(int ctType,String con) throws Exception {
        con = con.toLowerCase();
        List<ComConfiguremanage> configuremanageList = this.iComConfiguremanageService.getConfigLists(ctType);
        if (!configuremanageList.isEmpty()) {
            this.deleteConfigures(con);
            redisService.set(FebsConstant.COM_CONF_PREFIX + con, mapper.writeValueAsString(configuremanageList));
        }
    }

    @Override
    public List<ComConfiguremanage> getConfigures(int ctType,String con) throws Exception {
        con = con.toLowerCase();
        String configuremanageListString = this.redisService.get(FebsConstant.COM_CONF_PREFIX + con);
        if (StringUtils.isBlank(configuremanageListString)) {
            throw new Exception();
        } else {
            JavaType type = mapper.getTypeFactory().constructParametricType(List.class, ComConfiguremanage.class);
            return this.mapper.readValue(configuremanageListString, type);
        }
    }

    @Override
    public void deleteConfigures(String con) throws Exception {
        con = con.toLowerCase();
        redisService.del(FebsConstant.COM_CONF_PREFIX + con);
    }

    @Override
    public void savePersons(String con) throws Exception {
        con = con.toLowerCase();
        List<YbPerson> personList = this.iYbPersonService.list();
        if (!personList.isEmpty()) {
            this.deletePersons(con);
            redisService.set(FebsConstant.YBPERSON_PREFIX + con, mapper.writeValueAsString(personList));
        }
    }

    @Override
    public List<YbPerson> getPersons(String con) throws Exception {
        con = con.toLowerCase();
        String personListString = this.redisService.get(FebsConstant.YBPERSON_PREFIX + con);
        if (StringUtils.isBlank(personListString)) {
            throw new Exception();
        } else {
            JavaType type = mapper.getTypeFactory().constructParametricType(List.class, YbPerson.class);
            return this.mapper.readValue(personListString, type);
        }
    }

    @Override
    public void deletePersons(String con) throws Exception {
        con = con.toLowerCase();
        redisService.del(FebsConstant.YBPERSON_PREFIX + con);
    }

    @Override
    public void saveDepts(String con) throws Exception {
        con = con.toLowerCase();
        List<YbDept> deptList = this.iYbDeptService.list();
        if (!deptList.isEmpty()) {
            this.deleteDepts(con);
            redisService.set(FebsConstant.YBDEPT_PREFIX + con, mapper.writeValueAsString(deptList));
        }
    }

    @Override
    public List<YbDept> getDepts(String con) throws Exception {
        con = con.toLowerCase();
        String deptListString = this.redisService.get(FebsConstant.YBDEPT_PREFIX + con);
        if (StringUtils.isBlank(deptListString)) {
            throw new Exception();
        } else {
            JavaType type = mapper.getTypeFactory().constructParametricType(List.class, YbDept.class);
            return this.mapper.readValue(deptListString, type);
        }
    }

    @Override
    public void deleteDepts(String con) throws Exception {
        con = con.toLowerCase();
        redisService.del(FebsConstant.YBDEPT_PREFIX + con);
    }


    @Override
    public void saveApplyDatas(String pid,String con) throws Exception{
        con = con.toLowerCase();
        YbReconsiderApplyData rad = new YbReconsiderApplyData();
        rad.setPid(pid);
        List<YbReconsiderApplyData> list = this.iYbReconsiderApplyDataService.findReconsiderApplyDataList(rad);
        if (!list.isEmpty()) {
            this.deleteApplyDatas(con);
            redisService.set(FebsConstant.APPLYDATA_PREFIX + con, mapper.writeValueAsString(list));
        }
    }

    @Override
    public void saveApplyDatas(List<YbReconsiderApplyData> list,String con) throws Exception{
        con = con.toLowerCase();
        if (!list.isEmpty()) {
            this.deleteApplyDatas(con);
            redisService.set(FebsConstant.APPLYDATA_PREFIX + con, mapper.writeValueAsString(list));
        }
    }

    @Override
    public void deleteApplyDatas(String con) throws Exception{
        con = con.toLowerCase();
        redisService.del(FebsConstant.APPLYDATA_PREFIX + con);
    }

    @Override
    public List<YbReconsiderApplyData> getApplyDatas(String con) throws Exception{
        con = con.toLowerCase();
        String listString = this.redisService.get(FebsConstant.APPLYDATA_PREFIX + con);
        if (StringUtils.isBlank(listString)) {
            throw new Exception();
        } else {
            JavaType type = mapper.getTypeFactory().constructParametricType(List.class, YbReconsiderApplyData.class);
            return this.mapper.readValue(listString, type);
        }
    }
    @Override
    public void saveResetDatas(List<YbReconsiderResetData> list,String con) throws Exception{
        con = con.toLowerCase();
        if (!list.isEmpty()) {
            this.deleteResetDatas(con);
            redisService.set(FebsConstant.RESETDATA_PREFIX + con, mapper.writeValueAsString(list));
        }
    }

    @Override
    public void deleteResetDatas(String con) throws Exception{
        con = con.toLowerCase();
        redisService.del(FebsConstant.RESETDATA_PREFIX + con);
    }

    @Override
    public List<YbReconsiderResetData> getResetDatas(String con) throws Exception{
        con = con.toLowerCase();
        String listString = this.redisService.get(FebsConstant.RESETDATA_PREFIX + con);
        if (StringUtils.isBlank(listString)) {
            throw new Exception();
        } else {
            JavaType type = mapper.getTypeFactory().constructParametricType(List.class, YbReconsiderResetData.class);
            return this.mapper.readValue(listString, type);
        }
    }

//    @Override
//    public YbPerson getPerson(String personCode) throws Exception {
//        String personString = this.redisService.get(FebsConstant.YBPERSON_PREFIX + personCode);
//        if (StringUtils.isBlank(personString))
//            throw new Exception();
//        else
//            return this.mapper.readValue(personString, YbPerson.class);
//    }
//    @Override
//    public void savePerson(YbPerson person) throws Exception {
//        String personCode = person.getPersonCode();
//        this.deleteUser(personCode);
//        redisService.set(FebsConstant.YBPERSON_PREFIX + personCode, mapper.writeValueAsString(person));
//    }
//    @Override
//    public void savePerson(String personCode) throws Exception {
//        YbPerson person = iYbPersonService.findByName(personCode);
//        this.deletePerson(personCode);
//        redisService.set(FebsConstant.YBPERSON_PREFIX + personCode, mapper.writeValueAsString(person));
//    }
//    @Override
//    public void deletePerson(String personCode) throws Exception {
//        personCode = personCode.toLowerCase();
//        redisService.del(FebsConstant.YBPERSON_PREFIX + personCode);
//    }
}
