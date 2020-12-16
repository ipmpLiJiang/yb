package cc.mrbird.febs.system.controller;

import cc.mrbird.febs.com.controller.DataTypeHelpers;
import cc.mrbird.febs.export.excel.ExportExcelUtils;
import cc.mrbird.febs.com.controller.ImportExcelUtils;
import cc.mrbird.febs.common.annotation.Log;
import cc.mrbird.febs.common.controller.BaseController;
import cc.mrbird.febs.common.domain.FebsResponse;
import cc.mrbird.febs.common.domain.QueryRequest;
import cc.mrbird.febs.common.exception.FebsException;
import cc.mrbird.febs.common.properties.FebsProperties;
import cc.mrbird.febs.common.utils.MD5Util;
import cc.mrbird.febs.system.domain.*;
import cc.mrbird.febs.system.service.RoleService;
import cc.mrbird.febs.system.service.UserConfigService;
import cc.mrbird.febs.system.service.UserService;
import cc.mrbird.febs.yb.domain.ResponseResultData;
import com.baomidou.mybatisplus.core.toolkit.StringPool;
import com.wuwenze.poi.ExcelKit;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import java.io.File;
import java.util.*;
import java.util.stream.Collectors;

@Slf4j
@Validated
@RestController
@RequestMapping("user")
public class UserController extends BaseController {

    private String message;

    @Autowired
    private UserService userService;
    @Autowired
    private UserConfigService userConfigService;
    @Autowired
    private RoleService roleService;
    @Autowired
    private FebsProperties febsProperties;

    @GetMapping("check/{username}")
    public boolean checkUserName(@NotBlank(message = "{required}") @PathVariable String username) {
        return this.userService.findByName(username) == null;
    }

    @GetMapping("/{username}")
    public User detail(@NotBlank(message = "{required}") @PathVariable String username) {
        User user = this.userService.findByName(username);
        //修复用户修改自己的个人信息第二次提示roleId不能为空
        List<Role> roles = roleService.findUserRole(username);
        List<Long> roleIds = roles.stream().map(role -> role.getRoleId()).collect(Collectors.toList());
        String roleIdStr = StringUtils.join(roleIds.toArray(new Long[roleIds.size()]), ",");
        user.setRoleId(roleIdStr);
        return user;
    }

    @GetMapping
    @RequiresPermissions("user:view")
    public Map<String, Object> userList(QueryRequest queryRequest, User user) {
        return getDataTable(userService.findUserDetail(user, queryRequest));
    }

    @Log("新增用户")
    @PostMapping
    @RequiresPermissions("user:add")
    public void addUser(@Valid User user) throws FebsException {
        try {
            this.userService.createUser(user);
        } catch (Exception e) {
            message = "新增用户失败";
            log.error(message, e);
            throw new FebsException(message);
        }
    }

    @PostMapping("importUser")
    @RequiresPermissions("user:import")
    public FebsResponse importUsers(@RequestParam MultipartFile file) {
        int success = 0;
        if (file.isEmpty()) {
            message = "空文件";
        } else {
            try {
                File getFile = DataTypeHelpers.multipartFileToFile(file);
                List<Object[]> obj = ImportExcelUtils.importExcelBySheetIndex(getFile, 0, 0, 0);
                List<UserRolesImport> list = new ArrayList<>();
                List<String> strRoleList = new ArrayList<>();
                List<String> strDeptList = new ArrayList<>();
                String strError = "";
                if (obj.size() > 1) {
                    for (int i = 1; i < obj.size(); i++) {
                        UserRolesImport userRole = new UserRolesImport();
                        String userName = DataTypeHelpers.importTernaryOperate(obj.get(i), 0);
                        String name = DataTypeHelpers.importTernaryOperate(obj.get(i), 1);
                        String password = DataTypeHelpers.importTernaryOperate(obj.get(i), 2);
                        String sex = DataTypeHelpers.importTernaryOperate(obj.get(i), 3);
                        String email = DataTypeHelpers.importTernaryOperate(obj.get(i), 4);
                        String tel = DataTypeHelpers.importTernaryOperate(obj.get(i), 5);
                        String deptName = DataTypeHelpers.importTernaryOperate(obj.get(i), 6);
                        String roleName = DataTypeHelpers.importTernaryOperate(obj.get(i), 7);

                        userRole.setUserName(userName);
                        userRole.setXmname(name);
                        userRole.setPassword(password);
                        userRole.setSex(sex);
                        userRole.setEmail(email);
                        userRole.setTel(tel);
                        userRole.setDeptName(deptName);
                        userRole.setRoleName(roleName);
                        if (!DataTypeHelpers.isNullOrEmpty(roleName)) {
                            if (!strRoleList.stream().anyMatch(task -> task.equals(roleName))) {
                                strRoleList.add(roleName);
                            }
                        } else {
                            strError = "角色不能为空";
                            break;
                        }
                        if (!DataTypeHelpers.isNullOrEmpty(deptName)) {
                            if (!strDeptList.stream().anyMatch(task -> task.equals(deptName))) {
                                strDeptList.add(deptName);
                            }
                        } else {
                            strError = "部门不能为空";
                            break;
                        }
                        list.add(userRole);
                    }
                }
                if (strError.equals("")) {
                    String msg = this.userService.importUserRoles(list, strRoleList, strDeptList);
                    if (msg.equals("roleError")) {
                        message = "角色未创建";
                    } else if (msg.equals("deptError")) {
                        message = "部门未创建";
                    } else {
                        message = "新增用户成功";
                        success = 1;
                    }
                } else {
                    message = strError;
                }

            } catch (Exception e) {
                message = "新增用户失败";
                log.error(message, e);
            }
        }
        ResponseResultData rrd = new ResponseResultData();
        rrd.setSuccess(success);
        rrd.setMessage(message);
        return new FebsResponse().data(rrd);

    }

    @PostMapping("exportUser")
    @RequiresPermissions("user:import")
    public void export1(QueryRequest request, HttpServletResponse response) throws FebsException {
        try {
            String filePath = febsProperties.getUploadPath(); // 上传后的路径
            ExportExcelUtils.exportExcel(response, UserRolesImport.class, null, filePath,"userRolesExport", "", "用户信息");
        } catch (Exception e) {
            message = "导出Excel失败";
            log.error(message, e);
            throw new FebsException(message);
        }
    }

    @Log("修改用户")
    @PutMapping
    @RequiresPermissions("user:update")
    public void updateUser(@Valid User user) throws FebsException {
        try {
            this.userService.updateUser(user);
        } catch (Exception e) {
            message = "修改用户失败";
            log.error(message, e);
            throw new FebsException(message);
        }
    }

    @Log("删除用户")
    @DeleteMapping("/{userIds}")
    @RequiresPermissions("user:delete")
    public void deleteUsers(@NotBlank(message = "{required}") @PathVariable String userIds) throws FebsException {
        try {
            String[] ids = userIds.split(StringPool.COMMA);
            this.userService.deleteUsers(ids);
        } catch (Exception e) {
            message = "删除用户失败";
            log.error(message, e);
            throw new FebsException(message);
        }
    }

    @PutMapping("profile")
    public void updateProfile(@Valid User user) throws FebsException {
        try {
            this.userService.updateProfile(user);
        } catch (Exception e) {
            message = "修改个人信息失败";
            log.error(message, e);
            throw new FebsException(message);
        }
    }

    @PutMapping("avatar")
    public void updateAvatar(
            @NotBlank(message = "{required}") String username,
            @NotBlank(message = "{required}") String avatar) throws FebsException {
        try {
            this.userService.updateAvatar(username, avatar);
        } catch (Exception e) {
            message = "修改头像失败";
            log.error(message, e);
            throw new FebsException(message);
        }
    }

    @PutMapping("userconfig")
    public void updateUserConfig(@Valid UserConfig userConfig) throws FebsException {
        try {
            this.userConfigService.update(userConfig);
        } catch (Exception e) {
            message = "修改个性化配置失败";
            log.error(message, e);
            throw new FebsException(message);
        }
    }

    @GetMapping("password/check")
    public boolean checkPassword(
            @NotBlank(message = "{required}") String username,
            @NotBlank(message = "{required}") String password) {
        String encryptPassword = MD5Util.encrypt(username, password);
        User user = userService.findByName(username);
        if (user != null)
            return StringUtils.equals(user.getPassword(), encryptPassword);
        else
            return false;
    }

    @PutMapping("password")
    public void updatePassword(
            @NotBlank(message = "{required}") String username,
            @NotBlank(message = "{required}") String password) throws FebsException {
        try {
            userService.updatePassword(username, password);
        } catch (Exception e) {
            message = "修改密码失败";
            log.error(message, e);
            throw new FebsException(message);
        }
    }

    @PutMapping("password/reset")
    @RequiresPermissions("user:reset")
    public void resetPassword(@NotBlank(message = "{required}") String usernames) throws FebsException {
        try {
            String[] usernameArr = usernames.split(StringPool.COMMA);
            this.userService.resetPassword(usernameArr);
        } catch (Exception e) {
            message = "重置用户密码失败";
            log.error(message, e);
            throw new FebsException(message);
        }
    }

    @PostMapping("excel")
    @RequiresPermissions("user:export")
    public void export(QueryRequest queryRequest, User user, HttpServletResponse response) throws FebsException {
        try {
            List<User> users = this.userService.findUserDetail(user, queryRequest).getRecords();
            ExcelKit.$Export(User.class, response).downXlsx(users, false);
        } catch (Exception e) {
            message = "导出Excel失败";
            log.error(message, e);
            throw new FebsException(message);
        }
    }
}
