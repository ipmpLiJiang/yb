package cc.mrbird.febs.yb.service.impl;

import cc.mrbird.febs.com.entity.ComConfiguremanage;
import cc.mrbird.febs.com.service.IComConfiguremanageService;
import cc.mrbird.febs.com.service.impl.ComConfiguremanageServiceImpl;
import cc.mrbird.febs.common.domain.QueryRequest;
import cc.mrbird.febs.common.utils.SortUtil;
import cc.mrbird.febs.system.domain.UserRolesImport;
import cc.mrbird.febs.system.service.UserService;
import cc.mrbird.febs.system.service.impl.UserServiceImpl;
import cc.mrbird.febs.yb.dao.YbPersonMapper;
import cc.mrbird.febs.yb.entity.YbDept;
import cc.mrbird.febs.yb.entity.YbPerson;
import cc.mrbird.febs.yb.service.IYbPersonService;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.annotations.Param;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

/**
 * <p>
 * 服务实现类
 * </p>
 *
 * @author viki
 * @since 2020-07-21
 */
@Slf4j
@Service("IYbPersonService")
@Transactional(propagation = Propagation.SUPPORTS, readOnly = true, rollbackFor = Exception.class)
public class YbPersonServiceImpl extends ServiceImpl<YbPersonMapper, YbPerson> implements IYbPersonService {

    @Autowired
    private UserService userService;
    @Autowired
    private IComConfiguremanageService iComConfiguremanageService;

    @Override
    public IPage<YbPerson> findYbPersons(QueryRequest request, YbPerson ybPerson) {
        try {
            LambdaQueryWrapper<YbPerson> queryWrapper = new LambdaQueryWrapper<>();
            queryWrapper.eq(YbPerson::getIsDeletemark, 1);//1是未删 0是已删


            Page<YbPerson> page = new Page<>();
            SortUtil.handlePageSort(request, page, false);//true 是属性  false是数据库字段可两个
            return this.page(page, queryWrapper);
        } catch (Exception e) {
            log.error("获取字典信息失败", e);
            return null;
        }
    }

    @Override
    public IPage<YbPerson> findYbPersonList(QueryRequest request, YbPerson ybPerson) {
        try {
            Page<YbPerson> page = new Page<>();
            SortUtil.handlePageSort(request, page, false);//true 是属性  false是数据库字段可两个
            return this.baseMapper.findYbPerson(page, ybPerson);
        } catch (Exception e) {
            log.error("获取失败", e);
            return null;
        }
    }

    @Override
    public String importUserRoles(Integer type) throws Exception {
        String msg = "";
        List<YbPerson> userList = this.baseMapper.findPersonList(0);

        if (userList.size() > 0) {
            String str = "医生";
            String strDept = str;
            String strRole = str;

            List<String> strDeptList = new ArrayList<>();
            List<String> strRoleList = new ArrayList<>();
            List<UserRolesImport> userRoleList = new ArrayList<>();

            List<Integer> intList = new ArrayList<>();
            intList.add(2);//部门
            intList.add(3);//角色
            List<ComConfiguremanage> configList = iComConfiguremanageService.getConfigLists(intList);
            if (configList.size() > 0) {
                List<ComConfiguremanage> configDeptList = configList.stream().filter(
                        s -> s.getConfigureType().equals(2)
                ).collect(Collectors.toList());
                strDept = configDeptList.size() > 0 ? configDeptList.get(0).getStringField() : str;

                List<ComConfiguremanage> configRoleList = configList.stream().filter(
                        s -> s.getConfigureType().equals(3)
                ).collect(Collectors.toList());
                strRole = configRoleList.size() > 0 ? configRoleList.get(0).getStringField() : str;
            }
            strRoleList.add(strRole);
            if (type.equals(1)) {
                strDeptList.add(strDept);
                for (YbPerson person : userList) {
                    UserRolesImport userRolesImport = new UserRolesImport();
                    userRolesImport.setUserName(person.getPersonCode());
                    userRolesImport.setXmname(person.getPersonName());
                    userRolesImport.setSex(person.getSex());
                    userRolesImport.setEmail(person.getEmail());
                    userRolesImport.setTel(person.getTel());
                    userRolesImport.setDeptName(strDept);
                    userRolesImport.setRoleName(strRole);
                    userRoleList.add(userRolesImport);
                }
            } else {
                for (YbPerson person : userList) {
                    UserRolesImport userRolesImport = new UserRolesImport();
                    userRolesImport.setUserName(person.getPersonCode());
                    userRolesImport.setXmname(person.getPersonName());
                    userRolesImport.setSex(person.getSex());
                    userRolesImport.setEmail(person.getEmail());
                    userRolesImport.setTel(person.getTel());
                    userRolesImport.setDeptName(person.getDeptName());
                    if (!strDeptList.stream().anyMatch(task -> task.equals(person.getDeptName()))) {
                        strDeptList.add(person.getDeptName());
                    }
                    userRolesImport.setRoleName(strRole);
                    userRoleList.add(userRolesImport);
                }
            }

            msg = userService.importUserRoles(userRoleList, strRoleList, strDeptList);
        }
        return msg;
    }


    @Override
    public List<YbPerson> findPersonList(YbPerson ybPerson) {
        LambdaQueryWrapper<YbPerson> queryWrapper = new LambdaQueryWrapper<>();
        String sql = " IS_DELETEMARK = 1 ";
        if (ybPerson.getComments() != null) {
            sql += " and (personName like '%" + ybPerson.getComments() + "%' or personCode like '%" + ybPerson.getComments() + "%')";
        } else {
            sql += " and 1=2";
        }
        queryWrapper.apply(sql);
        List<YbPerson> list = this.list(queryWrapper);
        return list;
    }

    @Override
    @Transactional
    public void createYbPerson(YbPerson ybPerson) {
        ybPerson.setCreateTime(new Date());
        ybPerson.setIsDeletemark(1);
        this.save(ybPerson);
    }

    @Override
    @Transactional
    public void updateYbPerson(YbPerson ybPerson) {
        ybPerson.setModifyTime(new Date());
        this.baseMapper.updateYbPerson(ybPerson);
    }

    @Override
    @Transactional
    public void deleteYbPersons(String[] Ids) {
        List<String> list = Arrays.asList(Ids);
        this.baseMapper.deleteBatchIds(list);
    }


}