package cc.mrbird.febs.chs.service.impl;

import cc.mrbird.febs.chs.entity.YbChsConfireData;
import cc.mrbird.febs.chs.service.IYbChsConfireDataService;
import cc.mrbird.febs.com.entity.ComType;
import cc.mrbird.febs.com.service.IComTypeService;
import cc.mrbird.febs.common.domain.QueryRequest;
import cc.mrbird.febs.common.utils.SortUtil;
import cc.mrbird.febs.chs.entity.YbChsConfire;
import cc.mrbird.febs.chs.dao.YbChsConfireMapper;
import cc.mrbird.febs.chs.service.IYbChsConfireService;
import cc.mrbird.febs.system.domain.User;
import cc.mrbird.febs.system.service.UserService;
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

import java.util.Date;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;
import java.time.LocalDate;

/**
 * <p>
 * 服务实现类
 * </p>
 *
 * @author viki
 * @since 2022-06-30
 */
@Slf4j
@Service("IYbChsConfireService")
@Transactional(propagation = Propagation.SUPPORTS, readOnly = true, rollbackFor = Exception.class)
public class YbChsConfireServiceImpl extends ServiceImpl<YbChsConfireMapper, YbChsConfire> implements IYbChsConfireService {

    @Autowired
    IYbChsConfireDataService iYbChsConfireDataService;

    @Autowired
    IComTypeService iComTypeService;

    @Autowired
    UserService userService;

    @Override
    public IPage<YbChsConfire> findYbChsConfires(QueryRequest request, YbChsConfire ybChsConfire) {
        try {
            LambdaQueryWrapper<YbChsConfire> queryWrapper = new LambdaQueryWrapper<>();
            queryWrapper.eq(YbChsConfire::getIsDeletemark, 1);//1是未删 0是已删

            if (StringUtils.isNotBlank(ybChsConfire.getCurrencyField())) {
                queryWrapper.like(YbChsConfire::getCurrencyField, ybChsConfire.getCurrencyField());
            }

            Page<YbChsConfire> page = new Page<>();
            SortUtil.handlePageSort(request, page, false);//true 是属性  false是数据库字段可两个
            return this.page(page, queryWrapper);
        } catch (Exception e) {
            log.error("获取字典信息失败", e);
            return null;
        }
    }

    @Override
    public IPage<YbChsConfire> findChsConfireView(QueryRequest request, String doctorContent, Integer adminType, Integer areaType, String deptContent, String operatorName, String type) {
        try {
            Page<YbChsConfire> page = new Page<>();
            int count = this.baseMapper.findChsConfireCount(doctorContent, adminType, areaType, deptContent, operatorName);
            if (count > 0) {
                page.setSearchCount(false);
                SortUtil.handlePageSort(request, page, false);//true 是属性  false是数据库字段可两个
                IPage<YbChsConfire> pg = this.baseMapper.findChsConfireView(page, doctorContent, adminType, areaType, deptContent, operatorName, type);
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
    public IPage<YbChsConfire> findChsConfireUserView(QueryRequest request, String doctorContent, Integer adminType, Integer areaType, String deptContent, User currentUser, String operatorName, String type) {
        try {
            Page<YbChsConfire> page = new Page<>();
            String doctorCode = currentUser.getUsername();
            int count = this.baseMapper.findChsConfireUserCount(doctorContent, adminType, areaType, deptContent, currentUser.getUserId(), doctorCode, operatorName);
            if (count > 0) {
                page.setSearchCount(false);
                SortUtil.handlePageSort(request, page, false);//true 是属性  false是数据库字段可两个
                IPage<YbChsConfire> pg = this.baseMapper.findChsConfireUserView(page, doctorContent, adminType, areaType, deptContent, currentUser.getUserId(), doctorCode, operatorName, type);
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
    public IPage<YbChsConfire> findYbChsConfireList(QueryRequest request, YbChsConfire ybChsConfire) {
        try {
            Page<YbChsConfire> page = new Page<>();
            SortUtil.handlePageSort(request, page, false);//true 是属性  false是数据库字段可两个
            return this.baseMapper.findYbChsConfire(page, ybChsConfire);
        } catch (Exception e) {
            log.error("获取失败", e);
            return null;
        }
    }

    @Override
    @Transactional
    public void createYbChsConfire(YbChsConfire ybChsConfire) {
        ybChsConfire.setId(UUID.randomUUID().toString());
        ybChsConfire.setCreateTime(new Date());
        ybChsConfire.setIsDeletemark(1);
        this.save(ybChsConfire);
    }

    @Override
    @Transactional
    public void updateYbChsConfire(YbChsConfire ybChsConfire) {
        ybChsConfire.setModifyTime(new Date());
        this.baseMapper.updateYbChsConfire(ybChsConfire);
    }

    @Override
    @Transactional
    public void createChsConfire(YbChsConfire ybChsConfire, List<YbChsConfireData> createDataList) throws Exception {
        this.save(ybChsConfire);
        if (createDataList.size() > 0) {
            iYbChsConfireDataService.saveBatch(createDataList);
        }
        if (ybChsConfire.getDoctorCode() != null) {
            String[] users = new String[1];
            users[0] = ybChsConfire.getDoctorCode();

            LambdaQueryWrapper<ComType> wrapper = new LambdaQueryWrapper<>();
            wrapper.eq(ComType::getId,ybChsConfire.getAdminType());
            wrapper.eq(ComType::getCtType,1);
            wrapper.like(ComType::getCtName,"联络员");
            List<ComType> typeList= iComTypeService.getBaseMapper().selectList(wrapper);
            boolean isPersonLly = typeList.size() > 0 ? true : false;
            if(!isPersonLly){
                isPersonLly=  this.isLly(ybChsConfire.getDoctorCode(),null);
            }
            userService.saveConfUser(users, isPersonLly,true,8);
        }
    }

    @Override
    @Transactional
    public void updateChsConfire(YbChsConfire ybChsConfire, List<YbChsConfireData> createDataList, List<YbChsConfireData> updateDataList) throws Exception {
        this.updateById(ybChsConfire);
        iYbChsConfireDataService.saveBatch(createDataList);
        if (ybChsConfire.getDoctorCode() != null) {
            String[] users = new String[1];
            users[0] = ybChsConfire.getDoctorCode();

            LambdaQueryWrapper<ComType> wrapper = new LambdaQueryWrapper<>();
            wrapper.eq(ComType::getId,ybChsConfire.getAdminType());
            wrapper.eq(ComType::getCtType,1);
            wrapper.like(ComType::getCtName,"联络员");
            List<ComType> typeList= iComTypeService.getBaseMapper().selectList(wrapper);
            boolean  isPersonLly = typeList.size() > 0 ? true : false;

            if(!isPersonLly){
                isPersonLly=  this.isLly(ybChsConfire.getDoctorCode(),ybChsConfire.getId());
            }
            userService.saveConfUser(users,isPersonLly, true,8);
        }
    }

    private boolean isLly(String docCode,String id){
        YbChsConfire query = new YbChsConfire();
        query.setDoctorCode(docCode);
        if(id != null){
            query.setId(id);
        }
        // 查询未添加条件 areaType 医生有可能在其他院区是联络员
        query.setComments("联络员");
        List<YbChsConfire> llYList = this.baseMapper.findChsConfireLlyList(query);
        return  llYList.size() > 0 ? true : false;
    }

    @Override
    @Transactional
    public void deleteYbChsConfires(String[] Ids) throws Exception {
//        List<String> list = Arrays.asList(Ids);
//        this.baseMapper.deleteBatchIds(list);
        String[] idsNew = new String[1];
        idsNew[0] = Ids[0];
        LambdaQueryWrapper<YbChsConfire> wrapper = new LambdaQueryWrapper<>();
        wrapper.in(YbChsConfire::getId, idsNew);
        List<YbChsConfire> appealConfireList = this.baseMapper.selectList(wrapper);
        if (appealConfireList.size() > 0) {
            List<String> list = Arrays.asList(idsNew);
            this.iYbChsConfireDataService.deleteChsConfireDataPids(idsNew);
            this.baseMapper.deleteBatchIds(list);
            YbChsConfire appealConfire = appealConfireList.get(0);
            if (appealConfire.getDoctorCode() != null) {
                wrapper = new LambdaQueryWrapper<>();
                wrapper.eq(YbChsConfire::getDoctorCode, appealConfire.getDoctorCode());
                wrapper.ne(YbChsConfire::getAreaType, appealConfireList.get(0).getAreaType());
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
        }

    }

    @Override
    public YbChsConfire findChsConfire(YbChsConfire chsConfire) {
        LambdaQueryWrapper<YbChsConfire> wrapper = new LambdaQueryWrapper<>();
        if (chsConfire.getId() != null) {
            wrapper.eq(YbChsConfire::getId, chsConfire.getId());
        }
        if (chsConfire.getAreaType() != null) {
            wrapper.eq(YbChsConfire::getAreaType, chsConfire.getAreaType());
        }
        if (chsConfire.getDoctorCode() != null) {
            wrapper.eq(YbChsConfire::getDoctorCode, chsConfire.getDoctorCode());
        }
        wrapper.eq(YbChsConfire::getIsDeletemark, 1);
        List<YbChsConfire> list = this.baseMapper.selectList(wrapper);
        if (list.size() > 0) {
            return list.get(0);
        } else {
            return null;
        }
    }

}