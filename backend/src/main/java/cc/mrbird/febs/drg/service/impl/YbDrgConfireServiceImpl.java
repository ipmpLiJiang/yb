package cc.mrbird.febs.drg.service.impl;

import cc.mrbird.febs.com.entity.ComType;
import cc.mrbird.febs.com.service.IComTypeService;
import cc.mrbird.febs.common.domain.QueryRequest;
import cc.mrbird.febs.common.utils.SortUtil;
import cc.mrbird.febs.system.domain.User;
import cc.mrbird.febs.system.service.UserService;
import cc.mrbird.febs.drg.dao.YbDrgConfireMapper;
import cc.mrbird.febs.drg.entity.YbDrgConfire;
import cc.mrbird.febs.drg.entity.YbDrgConfireData;
import cc.mrbird.febs.drg.service.IYbDrgConfireDataService;
import cc.mrbird.febs.drg.service.IYbDrgConfireService;
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

import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.UUID;

/**
 * <p>
 * 服务实现类
 * </p>
 *
 * @author viki
 * @since 2021-01-11
 */
@Slf4j
@Service("IYbDrgConfireService")
@Transactional(propagation = Propagation.SUPPORTS, readOnly = true, rollbackFor = Exception.class)
public class YbDrgConfireServiceImpl extends ServiceImpl<YbDrgConfireMapper, YbDrgConfire> implements IYbDrgConfireService {

    @Autowired
    public IYbDrgConfireDataService iYbDrgConfireDataService;
    @Autowired
    UserService userService;
    @Autowired
    IComTypeService iComTypeService;

    @Override
    public IPage<YbDrgConfire> findYbDrgConfires(QueryRequest request, YbDrgConfire ybDrgConfire) {
        try {
            LambdaQueryWrapper<YbDrgConfire> queryWrapper = new LambdaQueryWrapper<>();
            queryWrapper.eq(YbDrgConfire::getIsDeletemark, 1);//1是未删 0是已删

            if (StringUtils.isNotBlank(ybDrgConfire.getCurrencyField())) {
                queryWrapper.like(YbDrgConfire::getCurrencyField, ybDrgConfire.getCurrencyField());
            }

            Page<YbDrgConfire> page = new Page<>();
            SortUtil.handlePageSort(request, page, false);//true 是属性  false是数据库字段可两个
            return this.page(page, queryWrapper);
        } catch (Exception e) {
            log.error("获取字典信息失败", e);
            return null;
        }
    }


    @Override
    public IPage<YbDrgConfire> findDrgConfireView(QueryRequest request, String doctorContent, Integer adminType, Integer areaType, String deptContent,String operatorName,String type) {
        try {
            Page<YbDrgConfire> page = new Page<>();
            int count = this.baseMapper.findDrgConfireCount(doctorContent, adminType, areaType, deptContent,operatorName);
            if (count > 0) {
                page.setSearchCount(false);
                SortUtil.handlePageSort(request, page, false);//true 是属性  false是数据库字段可两个
                IPage<YbDrgConfire> pg = this.baseMapper.findDrgConfireView(page, doctorContent, adminType, areaType, deptContent,operatorName,type);
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
    public IPage<YbDrgConfire> findDrgConfireUserView(QueryRequest request, String doctorContent, Integer adminType, Integer areaType, String deptContent, User currentUser,String operatorName,String type) {
        try {
            Page<YbDrgConfire> page = new Page<>();
//            YbDrgConfire query = new YbDrgConfire();
//            query.setDoctorCode(currentUser.getUsername());
//            query.setAreaType(areaType);
//            query.setComments("联络员");
//            List<YbDrgConfire> llYList = this.baseMapper.findDrgConfireLlyList(query);
//            String doctorCode = null;
//            if(llYList.size() > 0){
//                doctorCode = currentUser.getUsername();
//            }
            String doctorCode = currentUser.getUsername();
            int count = this.baseMapper.findDrgConfireUserCount(doctorContent, adminType, areaType, deptContent, currentUser.getUserId(),doctorCode,operatorName);
            if (count > 0) {
                page.setSearchCount(false);
                SortUtil.handlePageSort(request, page, false);//true 是属性  false是数据库字段可两个
                IPage<YbDrgConfire> pg = this.baseMapper.findDrgConfireUserView(page, doctorContent, adminType, areaType, deptContent, currentUser.getUserId(),doctorCode,operatorName,type);
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
    public IPage<YbDrgConfire> findYbDrgConfireList(QueryRequest request, YbDrgConfire ybDrgConfire) {
        try {
            Page<YbDrgConfire> page = new Page<>();
            SortUtil.handlePageSort(request, page, false);//true 是属性  false是数据库字段可两个
            return this.baseMapper.findYbDrgConfire(page, ybDrgConfire);
        } catch (Exception e) {
            log.error("获取失败", e);
            return null;
        }
    }


    @Override
    @Transactional
    public void createYbDrgConfire(YbDrgConfire ybDrgConfire) {
        ybDrgConfire.setId(UUID.randomUUID().toString());
        ybDrgConfire.setCreateTime(new Date());
        ybDrgConfire.setIsDeletemark(1);
        this.save(ybDrgConfire);
    }

    @Override
    @Transactional
    public void updateYbDrgConfire(YbDrgConfire ybDrgConfire) {
        ybDrgConfire.setModifyTime(new Date());
        this.baseMapper.updateYbDrgConfire(ybDrgConfire);
    }

    @Override
    @Transactional
    public void createDrgConfire(YbDrgConfire ybDrgConfire, List<YbDrgConfireData> createDataList) throws Exception {
        this.save(ybDrgConfire);
        if (createDataList.size() > 0) {
            iYbDrgConfireDataService.saveBatch(createDataList);
        }
        if (ybDrgConfire.getDoctorCode() != null) {
            String[] users = new String[1];
            users[0] = ybDrgConfire.getDoctorCode();

            LambdaQueryWrapper<ComType> wrapper = new LambdaQueryWrapper<>();
            wrapper.eq(ComType::getId,ybDrgConfire.getAdminType());
            wrapper.eq(ComType::getCtType,1);
            wrapper.like(ComType::getCtName,"联络员");
            List<ComType> typeList= iComTypeService.getBaseMapper().selectList(wrapper);
            boolean isPersonLly = typeList.size() > 0 ? true : false;
            if(!isPersonLly){
                isPersonLly=  this.isLly(ybDrgConfire.getDoctorCode(),null);
            }
            userService.saveConfUser(users, isPersonLly,true,9);
        }
    }

    @Override
    @Transactional
    public void updateDrgConfire(YbDrgConfire ybDrgConfire, List<YbDrgConfireData> createDataList, List<YbDrgConfireData> updateDataList) throws Exception {
        this.updateById(ybDrgConfire);
        iYbDrgConfireDataService.saveBatch(createDataList);
        if (ybDrgConfire.getDoctorCode() != null) {
            String[] users = new String[1];
            users[0] = ybDrgConfire.getDoctorCode();

            LambdaQueryWrapper<ComType> wrapper = new LambdaQueryWrapper<>();
            wrapper.eq(ComType::getId,ybDrgConfire.getAdminType());
            wrapper.eq(ComType::getCtType,1);
            wrapper.like(ComType::getCtName,"联络员");
            List<ComType> typeList= iComTypeService.getBaseMapper().selectList(wrapper);
            boolean  isPersonLly = typeList.size() > 0 ? true : false;

            if(!isPersonLly){
                isPersonLly=  this.isLly(ybDrgConfire.getDoctorCode(),ybDrgConfire.getId());
            }
            userService.saveConfUser(users,isPersonLly, true,9);
        }
    }

    private boolean isLly(String docCode,String id){
        YbDrgConfire query = new YbDrgConfire();
        query.setDoctorCode(docCode);
        if(id != null){
            query.setId(id);
        }
        // 查询未添加条件 areaType 医生有可能在其他院区是联络员
        query.setComments("联络员");
        List<YbDrgConfire> llYList = this.baseMapper.findDrgConfireLlyList(query);
        return  llYList.size() > 0 ? true : false;
    }

    @Override
    @Transactional
    public void deleteYbDrgConfires(String[] Ids) throws Exception {
        String[] idsNew = new String[1];
        idsNew[0] = Ids[0];
        LambdaQueryWrapper<YbDrgConfire> wrapper = new LambdaQueryWrapper<>();
        wrapper.in(YbDrgConfire::getId, idsNew);
        List<YbDrgConfire> drgConfireList = this.baseMapper.selectList(wrapper);
        if (drgConfireList.size() > 0) {
            List<String> list = Arrays.asList(idsNew);
            this.iYbDrgConfireDataService.deleteDrgConfireDataPids(idsNew);
            this.baseMapper.deleteBatchIds(list);
            YbDrgConfire drgConfire = drgConfireList.get(0);
            if (drgConfire.getDoctorCode() != null) {
                wrapper = new LambdaQueryWrapper<>();
                wrapper.eq(YbDrgConfire::getDoctorCode, drgConfire.getDoctorCode());
                wrapper.ne(YbDrgConfire::getAreaType, drgConfireList.get(0).getAreaType());
                drgConfireList = this.baseMapper.selectList(wrapper);
                if(drgConfireList.size() > 0) {
                    boolean  isPersonLly=  this.isLly(drgConfire.getDoctorCode(),Ids[0]);
                    String[] users = new String[1];
                    users[0] = drgConfire.getDoctorCode();
                    userService.saveConfUser(users,isPersonLly, true,9);
                }else{
                    String[] users = new String[1];
                    users[0] = drgConfire.getDoctorCode();
                    userService.saveConfUser(users,true, false,9);
                }
            }
        }

    }

    @Override
    public YbDrgConfire findDrgConfire(YbDrgConfire DrgConfire) {
        LambdaQueryWrapper<YbDrgConfire> wrapper = new LambdaQueryWrapper<>();
        if (DrgConfire.getId() != null) {
            wrapper.eq(YbDrgConfire::getId, DrgConfire.getId());
        }
        if (DrgConfire.getAreaType() != null) {
            wrapper.eq(YbDrgConfire::getAreaType, DrgConfire.getAreaType());
        }
        if (DrgConfire.getDoctorCode() != null) {
            wrapper.eq(YbDrgConfire::getDoctorCode, DrgConfire.getDoctorCode());
        }
        wrapper.eq(YbDrgConfire::getIsDeletemark, 1);
        List<YbDrgConfire> list = this.baseMapper.selectList(wrapper);
        if (list.size() > 0) {
            return list.get(0);
        } else {
            return null;
        }
    }

    @Override
    public List<YbDrgConfire> findDrgConfireATList(List<Integer> atIds, Integer areaType) {
        LambdaQueryWrapper<YbDrgConfire> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(YbDrgConfire::getIsDeletemark, 1);
        wrapper.eq(YbDrgConfire::getAreaType, areaType);
        wrapper.in(YbDrgConfire::getAdminType, atIds);
        return this.list(wrapper);
    }

}