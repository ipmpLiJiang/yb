package cc.mrbird.febs.com.service.impl;

import cc.mrbird.febs.com.entity.InUploadFile;
import cc.mrbird.febs.common.domain.QueryRequest;
import cc.mrbird.febs.common.utils.SortUtil;
import cc.mrbird.febs.com.entity.ComFile;
import cc.mrbird.febs.com.dao.ComFileMapper;
import cc.mrbird.febs.com.service.IComFileService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.time.LocalDate;

/**
 * <p>
 * 附件 服务实现类
 * </p>
 *
 * @author viki
 * @since 2020-08-06
 */
@Slf4j
@Service("IComFileService")
@Transactional(propagation = Propagation.SUPPORTS, readOnly = true, rollbackFor = Exception.class)
public class ComFileServiceImpl extends ServiceImpl<ComFileMapper, ComFile> implements IComFileService {


    @Override
    public IPage<ComFile> findComFiles(QueryRequest request, ComFile comFile) {
        try {
            LambdaQueryWrapper<ComFile> queryWrapper = new LambdaQueryWrapper<>();
            queryWrapper.eq(ComFile::getIsDeletemark, 1);//1是未删 0是已删

            Page<ComFile> page = new Page<>();
            SortUtil.handlePageSort(request, page, false);//true 是属性  false是数据库字段可两个
            return this.page(page, queryWrapper);
        } catch (Exception e) {
            log.error("获取字典信息失败", e);
            return null;
        }
    }

    @Override
    public List<ComFile> findListComFile(String Id,String refType) {
        List<ComFile> list = new ArrayList<>();
        try {
            LambdaQueryWrapper<ComFile> queryWrapper = new LambdaQueryWrapper<>();
            String sql = " IS_DELETEMARK = 1 and REF_TAB_ID = '"+Id+"'";
            if(refType != null && !refType.equals("")){
                sql += " AND REF_TYPE = '" + refType + "'";
            }
            queryWrapper.apply(sql);

            list = this.baseMapper.selectList(queryWrapper);
            if (list.size() > 0)
                Collections.sort(list);

            return list;
        } catch (Exception e) {
            log.error("获取字典信息失败", e);
            return list;
        }
    }

    @Override
    public ComFile findComFileById(String Id){
        LambdaQueryWrapper<ComFile> queryWrapper = new LambdaQueryWrapper<>();
        String sql = " IS_DELETEMARK = 1 and Id = '"+Id+"'";
        queryWrapper.apply(sql);
        List<ComFile> list = this.list(queryWrapper);

        ComFile comFile = new ComFile();
        if(list.size()>0){
            comFile = list.get(0);
        }
        return comFile;
    }

    @Override
    public List<ComFile> findAppealResultComFiles(InUploadFile iup) {
        List<ComFile> list = this.baseMapper.findAppealResultComFile(iup);
        return list;
    }

    @Override
    public List<ComFile> findDrgResultComFiles(String applyDateStr,Integer areaType){
        return this.baseMapper.findDrgResultComFile(applyDateStr,areaType);
    }

    @Override
    public List<ComFile> findAppealResultSumComFiles(InUploadFile iup) {
        List<ComFile> list = this.baseMapper.findAppealResultSumComFile(iup);
        return list;
    }

    @Override
    public IPage<ComFile> findComFileList(QueryRequest request, ComFile comFile) {
        try {
            Page<ComFile> page = new Page<>();
            SortUtil.handlePageSort(request, page, false);//true 是属性  false是数据库字段可两个
            return this.baseMapper.findComFile(page, comFile);
        } catch (Exception e) {
            log.error("获取附件失败", e);
            return null;
        }
    }

    public boolean loadLastComFiles(List<ComFile> listComFile){
        boolean bl = this.saveBatch(listComFile);
        return  bl;
    }

    @Override
    @Transactional
    public void createComFile(ComFile comFile) {
        if (comFile.getId() == null || comFile.getId().equals("")) {
            comFile.setId(UUID.randomUUID().toString());
        }
        comFile.setCreateTime(new Date());
        comFile.setIsDeletemark(1);
        this.save(comFile);
    }

    @Override
    @Transactional
    public void updateComFile(ComFile comFile) {
        comFile.setModifyTime(new Date());
        this.baseMapper.updateComFile(comFile);
    }

    @Override
    @Transactional
    public void deleteComFiles(String[] Ids) {
        List<String> list = Arrays.asList(Ids);
        this.baseMapper.deleteBatchIds(list);
    }

    @Override
    @Transactional
    public int deleteComFile(String Id) {
        return this.baseMapper.deleteById(Id);
    }

    @Override
    @Transactional
    public int batchRefIdDelete(String refTabId){
        return this.baseMapper.batchRefIdDelete(refTabId);
    }
}