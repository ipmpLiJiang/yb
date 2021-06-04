package cc.mrbird.febs.system.service.impl;

import cc.mrbird.febs.com.entity.ComConfiguremanage;
import cc.mrbird.febs.com.service.IComConfiguremanageService;
import cc.mrbird.febs.common.domain.FebsConstant;
import cc.mrbird.febs.common.domain.QueryRequest;
import cc.mrbird.febs.common.service.CacheService;
import cc.mrbird.febs.common.utils.SortUtil;
import cc.mrbird.febs.common.utils.MD5Util;
import cc.mrbird.febs.system.dao.UserMapper;
import cc.mrbird.febs.system.dao.UserRoleMapper;
import cc.mrbird.febs.system.domain.*;
import cc.mrbird.febs.system.manager.UserManager;
import cc.mrbird.febs.system.service.RoleService;
import cc.mrbird.febs.system.service.UserConfigService;
import cc.mrbird.febs.system.service.UserRoleService;
import cc.mrbird.febs.system.service.UserService;
import cc.mrbird.febs.yb.entity.YbAppealManageView;
import cc.mrbird.febs.yb.entity.YbPerson;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.StringPool;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

@Slf4j
@Service("userService")
@Transactional(propagation = Propagation.SUPPORTS, readOnly = true, rollbackFor = Exception.class)
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements UserService {

    @Autowired
    private UserRoleMapper userRoleMapper;
    @Autowired
    private UserConfigService userConfigService;
    @Autowired
    private CacheService cacheService;
    @Autowired
    private UserRoleService userRoleService;
    @Autowired
    private UserManager userManager;

    @Autowired
    private DeptServiceImpl deptService;

    @Autowired
    private RoleServiceImpl roleService;
    @Autowired
    IComConfiguremanageService iComConfiguremanageService;


    @Override
    public User findByName(String username) {
        return baseMapper.selectOne(new LambdaQueryWrapper<User>().eq(User::getUsername, username));
    }

    @Override
    public IPage<User> findUserDetail(User user, QueryRequest request) {
        try {
            Page<User> page = new Page<>();
            SortUtil.handlePageSort(request, page, "userId", FebsConstant.ORDER_ASC, false);
            return this.baseMapper.findUserDetail(page, user);
        } catch (Exception e) {
            log.error("查询用户异常", e);
            return null;
        }
    }

    @Override
    @Transactional
    public void updateLoginTime(User user) throws Exception {
        User updateUser = new User();
        updateUser.setLastLoginTime(new Date());
        String username = user.getUsername();

        this.baseMapper.update(user, new LambdaQueryWrapper<User>().eq(User::getUsername, username));
        UserConfig userConfig = this.userManager.getUserConfig(String.valueOf(user.getUserId()));
        // 重新将用户信息加载到 redis中
        cacheService.saveUser(username);
        if (userConfig == null) {
            // 创建用户默认的个性化配置
            userConfigService.initDefaultUserConfig(String.valueOf(user.getUserId()));

            cacheService.saveRoles(username);
            // 缓存用户权限
            cacheService.savePermissions(username);
            // 缓存用户个性化配置
            cacheService.saveUserConfigs(String.valueOf(user.getUserId()));
        }
    }

    @Override
    @Transactional
    public void createUser(User user) throws Exception {
        // 创建用户
        user.setCreateTime(new Date());
        user.setAvatar(User.DEFAULT_AVATAR);
        user.setPassword(MD5Util.encrypt(user.getUsername(), User.DEFAULT_PASSWORD));
        save(user);

        // 保存用户角色
        String[] roles = user.getRoleId().split(StringPool.COMMA);
        setUserRoles(user, roles);

        // 创建用户默认的个性化配置
        userConfigService.initDefaultUserConfig(String.valueOf(user.getUserId()));

        // 将用户相关信息保存到 Redis中
        userManager.loadUserRedisCache(user);
    }

    @Override
    @Transactional
    public void updateUser(User user) throws Exception {
        // 更新用户
        user.setPassword(null);
        user.setModifyTime(new Date());
        updateById(user);

        userRoleMapper.delete(new LambdaQueryWrapper<UserRole>().eq(UserRole::getUserId, user.getUserId()));

        String[] roles = user.getRoleId().split(StringPool.COMMA);
        setUserRoles(user, roles);

        // 重新将用户信息，用户角色信息，用户权限信息 加载到 redis中
        cacheService.saveUser(user.getUsername());
        cacheService.saveRoles(user.getUsername());
        cacheService.savePermissions(user.getUsername());
    }

    @Override
    @Transactional
    public void deleteUsers(String[] userIds) throws Exception {
        // 先删除相应的缓存
        this.userManager.deleteUserRedisCache(userIds);

        List<String> list = Arrays.asList(userIds);

        removeByIds(list);

        // 删除用户角色
        this.userRoleService.deleteUserRolesByUserId(userIds);
        // 删除用户个性化配置
        this.userConfigService.deleteByUserId(userIds);
    }

    @Override
    @Transactional
    public void updateProfile(User user) throws Exception {
        updateById(user);
        // 重新缓存用户信息
        cacheService.saveUser(user.getUsername());
    }

    @Override
    @Transactional
    public void updateAvatar(String username, String avatar) throws Exception {
        User user = new User();
        user.setAvatar(avatar);

        this.baseMapper.update(user, new LambdaQueryWrapper<User>().eq(User::getUsername, username));
        // 重新缓存用户信息
        cacheService.saveUser(username);
    }

    @Override
    @Transactional
    public void updatePassword(String username, String password) throws Exception {
        User user = new User();
        user.setPassword(MD5Util.encrypt(username, password));

        this.baseMapper.update(user, new LambdaQueryWrapper<User>().eq(User::getUsername, username));
        // 重新缓存用户信息
        cacheService.saveUser(username);
    }

    @Override
    @Transactional
    public void regist(String username, String password) throws Exception {
        User user = new User();
        user.setPassword(MD5Util.encrypt(username, password));
        user.setUsername(username);
        user.setCreateTime(new Date());
        user.setStatus(User.STATUS_VALID);
        user.setSsex(User.SEX_UNKNOW);
        user.setAvatar(User.DEFAULT_AVATAR);
        user.setDescription("注册用户");
        this.save(user);

        UserRole ur = new UserRole();
        ur.setUserId(user.getUserId());
        ur.setRoleId(2L); // 注册用户角色 ID
        this.userRoleMapper.insert(ur);

        // 创建用户默认的个性化配置
        userConfigService.initDefaultUserConfig(String.valueOf(user.getUserId()));
        // 将用户相关信息保存到 Redis中
        userManager.loadUserRedisCache(user);

    }

    /**
     * type 0 查询集合 type 1 like
     */
    @Override
    public List<User> findUserList(User user, int type) {
        LambdaQueryWrapper<User> queryWrapper = new LambdaQueryWrapper<>();
        List<User> list = new ArrayList<>();
        if (type == 1) {
            String sql = "";
            if (user.getDescription() != null) {
                sql += " USERNAME like '%" + user.getDescription() + "%' or XMNAME like '%" + user.getDescription() + "%'";
            }
            queryWrapper.apply(sql);

            list = this.list(queryWrapper);
            int count = 15;
            if (list.size() >= count) {
                list = list.subList(0, count);
            }
        } else {
            if (user.getUsername() != null) {
                queryWrapper.eq(User::getUsername, user.getUsername());
            }
            if (user.getXmname() != null) {
                queryWrapper.eq(User::getXmname, user.getXmname());
            }
            if (user.getMobile()!= null) {
                queryWrapper.eq(User::getMobile, user.getMobile());
            }
            list = this.list(queryWrapper);
        }
        return list;
    }

    @Override
    @Transactional
    public void resetPassword(String[] usernames) throws Exception {
        for (String username : usernames) {

            User user = new User();
            user.setPassword(MD5Util.encrypt(username, User.DEFAULT_PASSWORD));

            this.baseMapper.update(user, new LambdaQueryWrapper<User>().eq(User::getUsername, username));
            // 重新将用户信息加载到 redis中
            cacheService.saveUser(username);
        }

    }

    @Override
    public void saveConfUser(String[] userName,boolean isSaveOrDel) throws Exception {
        List<Role> roleList = roleService.findRoleConfLists(8);
        if(roleList.size() > 0) {
            List<UserRole> userRoleList = this.userRoleService.findUserRoleLists(userName);
            if (userRoleList.size() > 0) {
                List<Long> idList = new ArrayList<>();
                for (UserRole item:userRoleList){
                    if(idList.stream().filter(s->s.equals(item.getUserId())).count()==0){
                        idList.add(item.getUserId());
                    }
                }
                LambdaQueryWrapper<User> queryUserWrapper = new LambdaQueryWrapper<>();
                queryUserWrapper.in(User::getUserId,idList);
                List<User> userList = this.list(queryUserWrapper);
                for(User user : userList) {
                    Long userId = user.getUserId();
                    List<UserRole> queryUserRoleList = userRoleList.stream().filter(s->s.getUserId().equals(userId)).collect(Collectors.toList());
                    String roleId = this.getRoleId(roleList, queryUserRoleList, isSaveOrDel);
                    if (roleId != null && !roleId.equals("")) {
                        user.setRoleId(roleId);
                        this.updateUser(user);
                    }
                }
            }
        }
    }

    private String getRoleId(List<Role> roleList,List<UserRole> userRoleList,boolean isSaveOrDel){
        String roleId = "";
        if(isSaveOrDel) {
            boolean isUpdate = false;
            List<UserRole> query = new ArrayList<>();
            for (Role item : roleList) {
                query = userRoleList.stream().filter(s -> s.getRoleId().equals(item.getRoleId())).collect(Collectors.toList());
                if (query.size() == 0) {
                    if (roleId.equals("")) {
                        roleId = item.getRoleId().toString();
                    } else {
                        roleId += "," + item.getRoleId().toString();
                    }
                    isUpdate = true;
                }
            }
            if(isUpdate){
                for (UserRole userRole: userRoleList){
                    roleId += "," + userRole.getRoleId();
                }
            }
        }else{
            List<Role> query = new ArrayList<>();
            for (UserRole item : userRoleList) {
                query = roleList.stream().filter(s -> s.getRoleId().equals(item.getRoleId())).collect(Collectors.toList());
                if (query.size() == 0) {
                    if (roleId.equals("")) {
                        roleId = item.getRoleId().toString();
                    } else {
                        roleId += "," + item.getRoleId().toString();
                    }
                }
            }
        }

        return  roleId;
    }

    @Override
    @Transactional
    public String importUserRoles(List<UserRolesImport> userRoleList, List<String> strRoleList, List<String> strDeptList) throws Exception {
        String msg = "";
        List<Dept> deptList = new ArrayList<>();
        List<Role> roleList = new ArrayList<>();
        LambdaQueryWrapper<Dept> queryDeptWrapper = new LambdaQueryWrapper<>();
        String sql = "DEPT_NAME IN (";
        String strIn = "";
        for (String deptName : strDeptList) {
            if (strIn.equals("")) {
                strIn = "'" + deptName + "'";
            } else {
                strIn += ",'" + deptName + "'";
            }
        }

        sql += strIn + ")";
        queryDeptWrapper.apply(sql);

        deptList = deptService.list(queryDeptWrapper);
        if (deptList.size() == strDeptList.size()) {
            LambdaQueryWrapper<Role> queryRoleWrapper = new LambdaQueryWrapper<>();
            sql = "ROLE_NAME IN (";
            strIn = "";
            for (String roleName : strRoleList) {
                if (strIn.equals("")) {
                    strIn = "'" + roleName + "'";
                } else {
                    strIn += ",'" + roleName + "'";
                }
            }

            sql += strIn + ")";
            queryRoleWrapper.apply(sql);
            roleList = roleService.list(queryRoleWrapper);
        } else {
            return "deptError";
        }
        if (roleList.size() == strRoleList.size()) {
            List<Dept> searchDeptList = new ArrayList<>();
            List<Role> searchRoleList = new ArrayList<>();
            List<User> searchUserList = new ArrayList<>();
            List<User> userList = this.list();
            for (UserRolesImport userRole : userRoleList) {
                searchUserList = userList.stream().filter(
                        s -> s.getUsername().equals(userRole.getUserName())
                ).collect(Collectors.toList());
                if (searchUserList.size() == 0) {
                    User user = new User();
                    user.setCreateTime(new Date());
                    user.setAvatar(User.DEFAULT_AVATAR);
                    if(userRole.getPassword()!=null && !"".equals(userRole.getPassword())) {
                        user.setPassword(MD5Util.encrypt(userRole.getUserName(), userRole.getPassword()));
                    }else{
                        user.setPassword(MD5Util.encrypt(userRole.getUserName(), User.DEFAULT_PASSWORD));
                    }
                    searchDeptList = deptList.stream().filter(
                            s -> s.getDeptName().equals(userRole.getDeptName())
                    ).collect(Collectors.toList());

                    searchRoleList = roleList.stream().filter(
                            s -> s.getRoleName().equals(userRole.getRoleName())
                    ).collect(Collectors.toList());

                    user.setRoleId(searchRoleList.get(0).getRoleId().toString());
                    user.setDeptId(searchDeptList.get(0).getDeptId());
                    user.setUsername(userRole.getUserName());
                    user.setXmname(userRole.getXmname());
                    if (userRole.getEmail() != null) user.setEmail(userRole.getEmail());
                    if (userRole.getTel() != null) user.setMobile(userRole.getTel());
                    user.setCreateTime(new Date());
                    user.setStatus(User.STATUS_VALID);
                    if (userRole.getSex() != null) {
                        if (userRole.getSex().equals("男") || userRole.getSex().equals("0")) {
                            user.setSsex(User.SEX_MALE);
                        } else if (userRole.getSex().equals("女") || userRole.getSex().equals("1")) {
                            user.setSsex(User.SEX_FEMALE);
                        } else {
                            user.setSsex(User.SEX_UNKNOW);
                        }
                    } else {
                        user.setSsex(User.SEX_UNKNOW);
                    }
                    user.setAvatar(User.DEFAULT_AVATAR);
                    user.setDescription("注册用户");

                    save(user);

                    // 保存用户角色
                    String[] roles = user.getRoleId().split(StringPool.COMMA);
                    setUserRoles(user, roles);

                    // 创建用户默认的个性化配置
                    userConfigService.initDefaultUserConfig(String.valueOf(user.getUserId()));
                    // 将用户相关信息保存到 Redis中
                    userManager.loadUserRedisCache(user);
                }
            }
        } else {
            return "roleError";
        }
        return msg;
    }

    private void setUserRoles(User user, String[] roles) {
        Arrays.stream(roles).forEach(roleId -> {
            UserRole ur = new UserRole();
            ur.setUserId(user.getUserId());
            ur.setRoleId(Long.valueOf(roleId));
            this.userRoleMapper.insert(ur);
        });
    }

    @Override
    public List<User> findUserList(User user){
        List<User> list = new ArrayList<>();
        LambdaQueryWrapper<User> wrapper = new LambdaQueryWrapper<>();

        if(user.getUsername()!=null){
            wrapper.eq(User::getUsername,user.getUsername());
        }
        if(user.getUserId()!=null){
            wrapper.eq(User::getUserId,user.getUserId());
        }
        if(user.getXmname()!=null){
            wrapper.eq(User::getXmname,user.getXmname());
        }
        if(user.getMobile()!=null){
            wrapper.eq(User::getMobile,user.getMobile());
        }
        wrapper.eq(User::getStatus,User.STATUS_VALID);
        list = this.list(wrapper);
        return list;
    }
}
