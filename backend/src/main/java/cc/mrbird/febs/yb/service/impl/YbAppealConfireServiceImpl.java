package cc.mrbird.febs.yb.service.impl;

import cc.mrbird.febs.com.entity.ComConfiguremanage;
import cc.mrbird.febs.com.entity.ComType;
import cc.mrbird.febs.com.service.IComConfiguremanageService;
import cc.mrbird.febs.com.service.IComTypeService;
import cc.mrbird.febs.common.domain.QueryRequest;
import cc.mrbird.febs.common.utils.SortUtil;
import cc.mrbird.febs.system.domain.User;
import cc.mrbird.febs.system.domain.UserRole;
import cc.mrbird.febs.system.service.UserRoleService;
import cc.mrbird.febs.system.service.UserService;
import cc.mrbird.febs.yb.entity.YbAppealConfire;
import cc.mrbird.febs.yb.dao.YbAppealConfireMapper;
import cc.mrbird.febs.yb.entity.YbAppealConfireData;
import cc.mrbird.febs.yb.entity.YbAppealSumdept;
import cc.mrbird.febs.yb.service.IYbAppealConfireDataService;
import cc.mrbird.febs.yb.service.IYbAppealConfireService;
import cc.mrbird.febs.yb.service.IYbReconsiderApplyDataService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.time.LocalDate;

/**
 * <p>
 * 服务实现类
 * </p>
 *
 * @author viki
 * @since 2021-01-11
 */
@Slf4j
@Service("IYbAppealConfireService")
@Transactional(propagation = Propagation.SUPPORTS, readOnly = true, rollbackFor = Exception.class)
public class YbAppealConfireServiceImpl extends ServiceImpl<YbAppealConfireMapper, YbAppealConfire> implements IYbAppealConfireService {

    @Autowired
    public IYbAppealConfireDataService iYbAppealConfireDataService;
    @Autowired
    UserService userService;
    @Autowired
    IComTypeService iComTypeService;

    @Override
    public IPage<YbAppealConfire> findYbAppealConfires(QueryRequest request, YbAppealConfire ybAppealConfire) {
        try {
            LambdaQueryWrapper<YbAppealConfire> queryWrapper = new LambdaQueryWrapper<>();
            queryWrapper.eq(YbAppealConfire::getIsDeletemark, 1);//1是未删 0是已删

            if (StringUtils.isNotBlank(ybAppealConfire.getCurrencyField())) {
                queryWrapper.like(YbAppealConfire::getCurrencyField, ybAppealConfire.getCurrencyField());
            }

            Page<YbAppealConfire> page = new Page<>();
            SortUtil.handlePageSort(request, page, false);//true 是属性  false是数据库字段可两个
            return this.page(page, queryWrapper);
        } catch (Exception e) {
            log.error("获取字典信息失败", e);
            return null;
        }
    }


    @Override
    public IPage<YbAppealConfire> findAppealConfireView(QueryRequest request, String doctorContent, Integer adminType, Integer areaType, String deptContent,String operatorName,String type) {
        try {
            Page<YbAppealConfire> page = new Page<>();
            int count = this.baseMapper.findAppealConfireCount(doctorContent, adminType, areaType, deptContent,operatorName);
            if (count > 0) {
                page.setSearchCount(false);
                SortUtil.handlePageSort(request, page, false);//true 是属性  false是数据库字段可两个
                IPage<YbAppealConfire> pg = this.baseMapper.findAppealConfireView(page, doctorContent, adminType, areaType, deptContent,operatorName,type);
                pg.setTotal(count);
                return pg;
            }
            return page;
        } catch (Exception e) {
            log.error("获取失败", e);
            return null;
        }
    }

    @Override
    public IPage<YbAppealConfire> findAppealConfireUserView(QueryRequest request, String doctorContent, Integer adminType, Integer areaType, String deptContent, User currentUser,String operatorName,String type) {
        try {
            Page<YbAppealConfire> page = new Page<>();
//            YbAppealConfire query = new YbAppealConfire();
//            query.setDoctorCode(currentUser.getUsername());
//            query.setAreaType(areaType);
//            query.setComments("联络员");
//            List<YbAppealConfire> llYList = this.baseMapper.findAppealConfireLlyList(query);
//            String doctorCode = null;
//            if(llYList.size() > 0){
//                doctorCode = currentUser.getUsername();
//            }
            String doctorCode = currentUser.getUsername();
            int count = this.baseMapper.findAppealConfireUserCount(doctorContent, adminType, areaType, deptContent, currentUser.getUserId(),doctorCode,operatorName);
            if (count > 0) {
                page.setSearchCount(false);
                SortUtil.handlePageSort(request, page, false);//true 是属性  false是数据库字段可两个
                IPage<YbAppealConfire> pg = this.baseMapper.findAppealConfireUserView(page, doctorContent, adminType, areaType, deptContent, currentUser.getUserId(),doctorCode,operatorName,type);
                pg.setTotal(count);
                return pg;
            }
            return page;
        } catch (Exception e) {
            log.error("获取失败", e);
            return null;
        }
    }

    @Override
    public IPage<YbAppealConfire> findYbAppealConfireList(QueryRequest request, YbAppealConfire ybAppealConfire) {
        try {
            Page<YbAppealConfire> page = new Page<>();
            SortUtil.handlePageSort(request, page, false);//true 是属性  false是数据库字段可两个
            return this.baseMapper.findYbAppealConfire(page, ybAppealConfire);
        } catch (Exception e) {
            log.error("获取失败", e);
            return null;
        }
    }


    @Override
    @Transactional
    public void createYbAppealConfire(YbAppealConfire ybAppealConfire) {
        ybAppealConfire.setId(UUID.randomUUID().toString());
        ybAppealConfire.setCreateTime(new Date());
        ybAppealConfire.setIsDeletemark(1);
        this.save(ybAppealConfire);
    }

    @Override
    @Transactional
    public void updateYbAppealConfire(YbAppealConfire ybAppealConfire) {
        ybAppealConfire.setModifyTime(new Date());
        this.baseMapper.updateYbAppealConfire(ybAppealConfire);
    }

    @Override
    @Transactional
    public void createAppealConfire(YbAppealConfire ybAppealConfire, List<YbAppealConfireData> createDataList) throws Exception {
        this.save(ybAppealConfire);
        if (createDataList.size() > 0) {
            iYbAppealConfireDataService.saveBatch(createDataList);
        }
        if (ybAppealConfire.getDoctorCode() != null) {
            String[] users = new String[1];
            users[0] = ybAppealConfire.getDoctorCode();

            LambdaQueryWrapper<ComType> wrapper = new LambdaQueryWrapper<>();
            wrapper.eq(ComType::getId,ybAppealConfire.getAdminType());
            wrapper.eq(ComType::getCtType,1);
            wrapper.like(ComType::getCtName,"联络员");
            List<ComType> typeList= iComTypeService.getBaseMapper().selectList(wrapper);
            boolean isPersonLly = typeList.size() > 0 ? true : false;
            if(!isPersonLly){
                isPersonLly=  this.isLly(ybAppealConfire.getDoctorCode(),null);
            }
            userService.saveConfUser(users, isPersonLly,true,8);
        }
    }

    @Override
    @Transactional
    public void updateAppealConfire(YbAppealConfire ybAppealConfire, List<YbAppealConfireData> createDataList, List<YbAppealConfireData> updateDataList) throws Exception {
        this.updateById(ybAppealConfire);
        iYbAppealConfireDataService.saveBatch(createDataList);
        if (ybAppealConfire.getDoctorCode() != null) {
            String[] users = new String[1];
            users[0] = ybAppealConfire.getDoctorCode();

            LambdaQueryWrapper<ComType> wrapper = new LambdaQueryWrapper<>();
            wrapper.eq(ComType::getId,ybAppealConfire.getAdminType());
            wrapper.eq(ComType::getCtType,1);
            wrapper.like(ComType::getCtName,"联络员");
            List<ComType> typeList= iComTypeService.getBaseMapper().selectList(wrapper);
            boolean  isPersonLly = typeList.size() > 0 ? true : false;

            if(!isPersonLly){
                isPersonLly=  this.isLly(ybAppealConfire.getDoctorCode(),ybAppealConfire.getId());
            }
            userService.saveConfUser(users,isPersonLly, true,8);
        }
    }

    private boolean isLly(String docCode,String id){
        YbAppealConfire query = new YbAppealConfire();
        query.setDoctorCode(docCode);
        if(id != null){
            query.setId(id);
        }
        // 查询未添加条件 areaType 医生有可能在其他院区是联络员
        query.setComments("联络员");
        List<YbAppealConfire> llYList = this.baseMapper.findAppealConfireLlyList(query);
        return  llYList.size() > 0 ? true : false;
    }

    @Override
    @Transactional
    public void deleteYbAppealConfires(String[] Ids) throws Exception {
        String[] idsNew = new String[1];
        idsNew[0] = Ids[0];
        LambdaQueryWrapper<YbAppealConfire> wrapper = new LambdaQueryWrapper<>();
        wrapper.in(YbAppealConfire::getId, idsNew);
        List<YbAppealConfire> appealConfireList = this.baseMapper.selectList(wrapper);
        if (appealConfireList.size() > 0) {
            List<String> list = Arrays.asList(idsNew);
            this.iYbAppealConfireDataService.deleteAppealConfireDataPids(idsNew);
            this.baseMapper.deleteBatchIds(list);
            YbAppealConfire appealConfire = appealConfireList.get(0);
            if (appealConfire.getDoctorCode() != null) {
                wrapper = new LambdaQueryWrapper<>();
                wrapper.eq(YbAppealConfire::getDoctorCode, appealConfire.getDoctorCode());
                wrapper.ne(YbAppealConfire::getAreaType, appealConfireList.get(0).getAreaType());
                appealConfireList = this.baseMapper.selectList(wrapper);
                if(appealConfireList.size() > 0) {
                    boolean  isPersonLly=  this.isLly(appealConfire.getDoctorCode(),Ids[0]);
                    String[] users = new String[1];
                    users[0] = appealConfire.getDoctorCode();
                    userService.saveConfUser(users,isPersonLly, true,8);
                }else{
                    String[] users = new String[1];
                    users[0] = appealConfire.getDoctorCode();
                    userService.saveConfUser(users,true, false,8);
                }
            }
//            List<String> userList = new ArrayList<>();
//            for (YbAppealConfire ybAppealConfire : appealConfireList) {
//                if (ybAppealConfire.getDoctorCode() != null) {
//                    userList.add(ybAppealConfire.getDoctorCode());
//                }
//            }
//            if (userList.size() > 0) {
//                wrapper = new LambdaQueryWrapper<>();
//                wrapper.in(YbAppealConfire::getDoctorCode, userList);
//                wrapper.ne(YbAppealConfire::getAreaType, appealConfireList.get(0).getAreaType());
//                appealConfireList = this.baseMapper.selectList(wrapper);
//                if (appealConfireList.size() > 0) {
//                    for (YbAppealConfire item : appealConfireList) {
//                        userList.remove(item.getDoctorCode());
//                    }
//                }
//                if(userList.size()>0) {
//                    String[] users = new String[userList.size()];
//                    int i = 0;
//                    for (String item : userList) {
//                        users[i] = item;
//                        i++;
//                    }
//                    userService.saveConfUser(users,true, false);
//                }
//            }
        }

    }

    @Override
    public YbAppealConfire findAppealConfire(YbAppealConfire appealConfire) {
        LambdaQueryWrapper<YbAppealConfire> wrapper = new LambdaQueryWrapper<>();
        if (appealConfire.getId() != null) {
            wrapper.eq(YbAppealConfire::getId, appealConfire.getId());
        }
        if (appealConfire.getAreaType() != null) {
            wrapper.eq(YbAppealConfire::getAreaType, appealConfire.getAreaType());
        }
        if (appealConfire.getDoctorCode() != null) {
            wrapper.eq(YbAppealConfire::getDoctorCode, appealConfire.getDoctorCode());
        }
        wrapper.eq(YbAppealConfire::getIsDeletemark, 1);
        List<YbAppealConfire> list = this.baseMapper.selectList(wrapper);
        if (list.size() > 0) {
            return list.get(0);
        } else {
            return null;
        }
    }

    @Override
    public List<YbAppealConfire> findAppealConfireATList(List<Integer> atIds, Integer areaType) {
        LambdaQueryWrapper<YbAppealConfire> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(YbAppealConfire::getIsDeletemark, 1);
        wrapper.eq(YbAppealConfire::getAreaType, areaType);
        wrapper.in(YbAppealConfire::getAdminType, atIds);
        return this.list(wrapper);
    }

}