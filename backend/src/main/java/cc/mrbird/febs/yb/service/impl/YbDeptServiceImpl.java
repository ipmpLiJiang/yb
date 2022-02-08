package cc.mrbird.febs.yb.service.impl;

import cc.mrbird.febs.common.domain.QueryRequest;
import cc.mrbird.febs.common.utils.SortUtil;
import cc.mrbird.febs.yb.entity.YbAppealConfireData;
import cc.mrbird.febs.yb.entity.YbDept;
import cc.mrbird.febs.yb.dao.YbDeptMapper;
import cc.mrbird.febs.yb.entity.YbDeptHis;
import cc.mrbird.febs.yb.entity.YbPerson;
import cc.mrbird.febs.yb.service.IYbAppealConfireDataService;
import cc.mrbird.febs.yb.service.IYbDeptService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.annotations.Param;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

/**
 * <p>
 * 服务实现类
 * </p>
 *
 * @author viki
 * @since 2020-07-21
 */
@Slf4j
@Service("IYbDeptService")
@Transactional(propagation = Propagation.SUPPORTS, readOnly = true, rollbackFor = Exception.class)
public class YbDeptServiceImpl extends ServiceImpl<YbDeptMapper, YbDept> implements IYbDeptService {

    @Autowired
    IYbAppealConfireDataService iYbAppealConfireDataService;

    @Override
    public IPage<YbDept> findYbDepts(QueryRequest request, YbDept ybDept) {
        try {
            LambdaQueryWrapper<YbDept> queryWrapper = new LambdaQueryWrapper<>();
            queryWrapper.eq(YbDept::getIsDeletemark, 1);//1是未删 0是已删


            Page<YbDept> page = new Page<>();
            SortUtil.handlePageSort(request, page, false);//true 是属性  false是数据库字段可两个
            return this.page(page, queryWrapper);
        } catch (Exception e) {
            log.error("获取字典信息失败", e);
            return null;
        }
    }

    @Override
    public IPage<YbDept> findYbDeptList(QueryRequest request, YbDept ybDept) {
        try {
            Page<YbDept> page = new Page<>();
            SortUtil.handlePageSort(request, page, false);//true 是属性  false是数据库字段可两个
            return this.baseMapper.findYbDept(page, ybDept);
        } catch (Exception e) {
            log.error("获取失败", e);
            return null;
        }
    }

    /**
     * type 0 查询集合 type 1 like
     */
    @Override
    public List<YbDept> findDeptList(YbDept ybDept, int type) {
        LambdaQueryWrapper<YbDept> queryWrapper = new LambdaQueryWrapper<>();
        List<YbDept> list = new ArrayList<>();
        if (type == 1) {
            String sql = " IS_DELETEMARK = 1 ";
            if (ybDept.getComments() != null) {
                sql += " and (deptId like '%" + ybDept.getComments() + "%' or deptName like '%" + ybDept.getComments() + "%' or spellCode like '%" + ybDept.getComments() + "%')";
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
            queryWrapper.eq(YbDept::getIsDeletemark, 1);
            if (ybDept.getDeptName() != null) {
                queryWrapper.eq(YbDept::getDeptName, ybDept.getDeptName());
            }
            if (ybDept.getDeptId() != null) {
                queryWrapper.eq(YbDept::getDeptId, ybDept.getDeptId());
            }
            if (ybDept.getSpellCode() != null) {
                queryWrapper.eq(YbDept::getSpellCode, ybDept.getSpellCode());
            }
            list = this.list(queryWrapper);
        }
        return list;
    }

    @Override
    public List<YbDept> findDeptAppealConfireList(String doctorCode,String comments, Integer areaType) {
        List<YbDept> list = new ArrayList<>();
        List<YbAppealConfireData> acdList = this.iYbAppealConfireDataService.findAppealConfireDataByInDoctorCodeList(doctorCode,areaType);
        if(acdList.size() > 0) {
            list = this.baseMapper.findDeptAppealConfireList(acdList.get(0).getPid(), comments, areaType);
            int count = 15;
            if (list.size() >= count) {
                list = list.subList(0, count);
            }
        }
        return list;
    }
    @Override
    public List<YbDept> findDeptAppealConfireByUserList(String doctorCode, Integer areaType) {
        return this.baseMapper.findDeptAppealConfireByUserList(doctorCode,areaType);
    }

    @Override
    public List<String> findDeptCodeList(String value){
        YbDept dept = new YbDept();
        dept.setDeptName(value);
        List<YbDept> deltList = this.findDeptList(dept,0);
        List<String> strList = new ArrayList<>();
        for (YbDept item : deltList){
            strList.add(item.getDeptId());
        }
        return strList;
    }

    @Override
    @Transactional
    public void deleteBatchDepts() {
        LambdaQueryWrapper<YbDept> wrapper = new LambdaQueryWrapper<>();
        this.baseMapper.delete(wrapper);
    }

    @Override
    @Transactional
    public boolean createBatchDepts(List<YbDeptHis> list) {
        List<YbDept> createList = new ArrayList<>();
        List<YbDept> findList = this.findDeptList(new YbDept(), 0);
        for (YbDeptHis item : list) {
            if (findList.stream().filter(s -> s.getDeptId().equals(item.getDeptId())).count() == 0) {
                YbDept dept = new YbDept();
                dept.setDeptId(item.getDeptId());
                dept.setDeptName(item.getDeptName());
                dept.setSpellCode(item.getSpellCode());
                dept.setCreateTime(new Date());
                dept.setIsDeletemark(1);
                createList.add(dept);
            }
        }
        if (createList.size() > 0) {
            return this.saveBatch(createList);
        } else {
            return false;
        }
    }

    @Override
    @Transactional
    public void createYbDept(YbDept ybDept) {
        ybDept.setCreateTime(new Date());
        ybDept.setIsDeletemark(1);
        this.save(ybDept);
    }

    @Override
    @Transactional
    public void updateYbDept(YbDept ybDept) {
        ybDept.setModifyTime(new Date());
        this.baseMapper.updateYbDept(ybDept);
    }

    @Override
    @Transactional
    public void deleteYbDepts(String[] Ids) {
        List<String> list = Arrays.asList(Ids);
        this.baseMapper.deleteBatchIds(list);
    }


}