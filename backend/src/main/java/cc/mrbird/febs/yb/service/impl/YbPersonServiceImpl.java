package cc.mrbird.febs.yb.service.impl;

import cc.mrbird.febs.com.entity.ComConfiguremanage;
import cc.mrbird.febs.com.service.IComConfiguremanageService;
import cc.mrbird.febs.com.service.impl.ComConfiguremanageServiceImpl;
import cc.mrbird.febs.common.domain.QueryRequest;
import cc.mrbird.febs.common.utils.SortUtil;
import cc.mrbird.febs.system.domain.Dept;
import cc.mrbird.febs.system.domain.User;
import cc.mrbird.febs.system.domain.UserRolesImport;
import cc.mrbird.febs.system.service.DeptService;
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
    private DeptService deptService;
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
    @Transactional
    public String importUserRoles(Integer type) throws Exception {
        String msg = "";
        //type 0 user_id is null
        List<YbPerson> userList = this.baseMapper.findPersonList(0);

        if (userList.size() > 0) {
            String str = "医生";
            String strDept = str;
            String strRole = str;

            List<String> strDeptList = new ArrayList<>();
            List<String> strRoleList = new ArrayList<>();
            List<UserRolesImport> userRoleList = new ArrayList<>();
            if (type.equals(1)) {
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
            }
            strRoleList.add(strRole);
            //导入类型 type 1 按配置文件导入角色和部门，type 0 角色和部门 都为医生
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
    @Transactional
    public boolean importPerson(User logUser) {
        boolean isTrue = false;
        List<User> userList = userService.findUserList(new User());
        List<YbPerson> personList = this.findPersonList(new YbPerson(), 0);
        List<Dept> deptList = this.deptService.findDepts(new Dept());
        List<Dept> queryDeptList = new ArrayList<>();
        List<YbPerson> queryPersonList = new ArrayList<>();
        List<YbPerson> createList = new ArrayList<>();
        List<YbPerson> updateList = new ArrayList<>();
        boolean isUpdate = false;
        for (User user : userList) {
            queryPersonList = personList.stream().filter(s -> s.getPersonCode().equals(user.getUsername())).collect(Collectors.toList());
            if (queryPersonList.size() == 0) {
                YbPerson ybPerson = new YbPerson();
                ybPerson.setPersonCode(user.getUsername());
                ybPerson.setPersonName(user.getXmname());
                //0=男,1=女,2=保密
                ybPerson.setSex(user.getSsex());
                queryDeptList = deptList.stream().filter(s -> s.getDeptId().equals(user.getDeptId())).collect(Collectors.toList());
                if (queryDeptList.size() > 0) {
                    ybPerson.setDeptName(queryDeptList.get(0).getDeptName());
                }
                ybPerson.setEmail(user.getEmail());
                ybPerson.setTel(user.getMobile());
                ybPerson.setSpellCode("");
                ybPerson.setCreateUserId(logUser.getUserId());
                ybPerson.setCreateTime(new Date());
                ybPerson.setIsDeletemark(1);
                createList.add(ybPerson);
            } else {
                YbPerson person = queryPersonList.get(0);
                if (person.getTel() !=null && !person.getTel().equals(user.getMobile())) {
                    isUpdate = true;
                }
                if (person.getEmail() !=null && !person.getEmail().equals(user.getEmail())) {
                    isUpdate = true;
                }
                if (isUpdate) {
                    YbPerson updatePerson = new YbPerson();
                    updatePerson.setId(person.getId());
                    updatePerson.setTel(user.getMobile());
                    updatePerson.setEmail(user.getEmail());
                    updateList.add(updatePerson);
                }
                isUpdate = false;
            }
        }
        if (createList.size() > 0) {
            isTrue = this.saveBatch(createList);
        }
        if (updateList.size() > 0) {
            isTrue = this.updateBatchById(updateList);
        }
        return isTrue;
    }

    @Override
    public YbPerson findByName(String personCode) {
        LambdaQueryWrapper<YbPerson> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(YbPerson::getPersonCode, personCode);
        return baseMapper.selectOne(wrapper);
    }
    @Override
    public List<YbPerson> findPersonList(ArrayList<String> personCodeList) {
        LambdaQueryWrapper<YbPerson> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(YbPerson::getIsDeletemark, 1);
        queryWrapper.in(YbPerson::getPersonCode,personCodeList);
        List<YbPerson> list = this.list(queryWrapper);
        return list;
    }

    @Override
    public List<YbPerson> findPersonResultList(String applyDateStr){
        return this.baseMapper.findPersonResultList(applyDateStr);
    }

    /**
     * type 0 查询集合 type 1 like
     */
    @Override
    public List<YbPerson> findPersonList(YbPerson ybPerson, int type) {
        LambdaQueryWrapper<YbPerson> queryWrapper = new LambdaQueryWrapper<>();
        List<YbPerson> list = new ArrayList<>();
        if (type == 1) {
            String sql = " IS_DELETEMARK = 1 ";
            if (ybPerson.getComments() != null) {
                sql += " and (personName like '%" + ybPerson.getComments() + "%' or personCode like '%" + ybPerson.getComments() + "%')";
            } else {
                sql += " and 1=2";
            }
            queryWrapper.apply(sql);

            list = this.list(queryWrapper);
            int count = 15;
            if (list.size() >= count) {
                list = list.subList(0, count);
            }
        } else {
            queryWrapper.eq(YbPerson::getIsDeletemark, 1);
            if (ybPerson.getPersonCode() != null) {
                queryWrapper.eq(YbPerson::getPersonCode, ybPerson.getPersonCode());
            }
            if (ybPerson.getPersonName() != null) {
                queryWrapper.eq(YbPerson::getPersonName, ybPerson.getPersonName());
            }
            if (ybPerson.getDeptName() != null) {
                queryWrapper.eq(YbPerson::getDeptName, ybPerson.getDeptName());
            }
            if (ybPerson.getTel() != null) {
                queryWrapper.eq(YbPerson::getTel, ybPerson.getTel());
            }
            list = this.list(queryWrapper);
        }
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