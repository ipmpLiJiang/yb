package cc.mrbird.febs.chs.service.impl;

import cc.mrbird.febs.chs.entity.YbChsConfireData;
import cc.mrbird.febs.chs.service.IYbChsConfireDataService;
import cc.mrbird.febs.com.controller.DataTypeHelpers;
import cc.mrbird.febs.common.domain.QueryRequest;
import cc.mrbird.febs.common.utils.SortUtil;
import cc.mrbird.febs.chs.entity.YbDks;
import cc.mrbird.febs.chs.dao.YbDksMapper;
import cc.mrbird.febs.chs.service.IYbDksService;
import cc.mrbird.febs.yb.entity.YbDeptHis;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import freemarker.template.utility.StringUtil;
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
 * @since 2022-06-24
 */
@Slf4j
@Service("IYbDksService")
@Transactional(propagation = Propagation.SUPPORTS, readOnly = true, rollbackFor = Exception.class)
public class YbDksServiceImpl extends ServiceImpl<YbDksMapper, YbDks> implements IYbDksService {

    @Autowired
    IYbChsConfireDataService iYbChsConfireDataService;

    @Override
    public IPage<YbDks> findYbDkss(QueryRequest request, YbDks ybDks) {
        try {
            LambdaQueryWrapper<YbDks> queryWrapper = new LambdaQueryWrapper<>();
//            queryWrapper.eq(YbDks::getIsDeletemark, 1);//1是未删 0是已删
            String sql = "IS_DELETEMARK = 1 ";
            if(StringUtils.isNotBlank(ybDks.getDksName())) {
                sql += " and (dksName like '%" + ybDks.getDksName() + "%' or spellCode like '%" + ybDks.getDksName() + "%' )";
            }
            queryWrapper.apply(sql);
            Page<YbDks> page = new Page<>();
            SortUtil.handlePageSort(request, page, false);//true 是属性  false是数据库字段可两个
            return this.page(page, queryWrapper);
        } catch (Exception e) {
            log.error("获取字典信息失败", e);
            return null;
        }
    }

    @Override
    public IPage<YbDks> findYbDksList(QueryRequest request, YbDks ybDks) {
        try {
            Page<YbDks> page = new Page<>();
            SortUtil.handlePageSort(request, page, false);//true 是属性  false是数据库字段可两个
            return this.baseMapper.findYbDks(page, ybDks);
        } catch (Exception e) {
            log.error("获取失败", e);
            return null;
        }
    }

    @Override
    @Transactional
    public void createYbDks(YbDks ybDks) {
        ybDks.setCreateTime(new Date());
        ybDks.setIsDeletemark(1);
        this.save(ybDks);
    }

    @Override
    @Transactional
    public void updateYbDks(YbDks ybDks) {
        ybDks.setModifyTime(new Date());
        this.baseMapper.updateYbDks(ybDks);
    }

    @Override
    @Transactional
    public void deleteYbDkss(String[] Ids) {
        List<String> list = Arrays.asList(Ids);
        this.baseMapper.deleteBatchIds(list);
    }

    @Override
    public List<YbDks> findDksList(YbDks ybDks, int type) {
        LambdaQueryWrapper<YbDks> queryWrapper = new LambdaQueryWrapper<>();
        List<YbDks> list = new ArrayList<>();
        if (type == 1) {
            String sql = " IS_DELETEMARK = 1 ";
            if (ybDks.getComments() != null) {
                sql += " and (dksId like '%" + ybDks.getComments() + "%' or dksName like '%" + ybDks.getComments() + "%' or spellCode like '%" + ybDks.getComments() + "%')";
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
            queryWrapper.eq(YbDks::getIsDeletemark, 1);
            if (ybDks.getDksName() != null) {
                queryWrapper.eq(YbDks::getDksName, ybDks.getDksName());
            }
            if (ybDks.getDksId() != null) {
                queryWrapper.eq(YbDks::getDksId, ybDks.getDksId());
            }
            if (ybDks.getSpellCode() != null) {
                queryWrapper.eq(YbDks::getSpellCode, ybDks.getSpellCode());
            }
            list = this.list(queryWrapper);
        }
        return list;
    }


    @Override
    @Transactional
    public boolean createBatchDkss(List<YbDeptHis> list) {
        List<YbDks> createList = new ArrayList<>();
        List<YbDks> findList = this.findDksList(new YbDks(), 0);
        List<String> codeList = new ArrayList<>();
        for (YbDeptHis item : list) {
            if (findList.stream().filter(s -> s.getDksId().equals(item.getParentCode())).count() == 0) {
                if (codeList.stream().filter(s -> s.equals(item.getParentCode())).count() == 0) {
                    codeList.add(item.getParentCode());
                    YbDks dept = new YbDks();
                    dept.setDksId(item.getParentCode());
                    dept.setDksName(item.getBm_mc());
                    dept.setCreateTime(new Date());
                    dept.setIsDeletemark(1);
                    String py = DataTypeHelpers.chineseZpySzm(dept.getDksName());
                    if(StringUtils.isNotBlank(py)) {
                        dept.setSpellCode(py);
                    }
                    createList.add(dept);
                }
            }
        }
        if (createList.size() > 0) {
            return this.saveBatch(createList);
        } else {
            return false;
        }
    }

    @Override
    public List<YbDks> findDksChsConfireList(String doctorCode,String comments, Integer areaType) {
        List<YbDks> list = new ArrayList<>();
        List<YbChsConfireData> acdList = this.iYbChsConfireDataService.findChsConfireDataByInDoctorCodeList(doctorCode,areaType);
        if(acdList.size() > 0) {
            list = this.baseMapper.findDksChsConfireList(acdList.get(0).getPid(), comments, areaType);
            int count = 15;
            if (list.size() >= count) {
                list = list.subList(0, count);
            }
        }
        return list;
    }
}