package cc.mrbird.febs.drg.service.impl;

import cc.mrbird.febs.com.entity.ComConfiguremanage;
import cc.mrbird.febs.common.domain.QueryRequest;
import cc.mrbird.febs.common.utils.SortUtil;
import cc.mrbird.febs.drg.entity.*;
import cc.mrbird.febs.drg.dao.YbDrgApplyDataMapper;
import cc.mrbird.febs.drg.service.IYbDrgApplyDataService;
import cc.mrbird.febs.drg.service.IYbDrgApplyService;
import cc.mrbird.febs.drg.service.IYbDrgJkService;
import cc.mrbird.febs.yb.entity.YbDefaultValue;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import cc.mrbird.febs.common.utils.MySqlDB;

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
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;
import java.util.stream.Collectors;

/**
 * <p>
 * 服务实现类
 * </p>
 *
 * @author viki
 * @since 2021-11-23
 */
@Slf4j
@Service("IYbDrgApplyDataService")
@Transactional(propagation = Propagation.SUPPORTS, readOnly = true, rollbackFor = Exception.class)
public class YbDrgApplyDataServiceImpl extends ServiceImpl<YbDrgApplyDataMapper, YbDrgApplyData> implements IYbDrgApplyDataService {

    @Autowired
    private IYbDrgApplyService iYbDrgApplyService;

    @Autowired
    private IYbDrgJkService iYbDrgJkService;

    @Override
    public IPage<YbDrgApplyData> findYbDrgApplyDatas(QueryRequest request, YbDrgApplyData ybDrgApplyData) {
        try {
            LambdaQueryWrapper<YbDrgApplyData> queryWrapper = new LambdaQueryWrapper<>();
//            queryWrapper.eq(YbDrgApplyData::getIsDeletemark, 1);//1是未删 0是已删


            Page<YbDrgApplyData> page = new Page<>();
            SortUtil.handlePageSort(request, page, false);//true 是属性  false是数据库字段可两个
            return this.page(page, queryWrapper);
        } catch (Exception e) {
            log.error("获取字典信息失败", e);
            return null;
        }
    }

    @Override
    public IPage<YbDrgApplyData> findYbDrgApplyDataList(QueryRequest request, YbDrgApplyData ybDrgApplyData) {
        try {
            Page<YbDrgApplyData> page = new Page<>();
            SortUtil.handlePageSort(request, page, false);//true 是属性  false是数据库字段可两个
            return this.baseMapper.findYbDrgApplyData(page, ybDrgApplyData);
        } catch (Exception e) {
            log.error("获取失败", e);
            return null;
        }
    }

    @Override
    @Transactional
    public void createYbDrgApplyData(YbDrgApplyData ybDrgApplyData) {
        ybDrgApplyData.setId(UUID.randomUUID().toString());
//        ybDrgApplyData.setCreateTime(new Date());
//        ybDrgApplyData.setIsDeletemark(1);
        this.save(ybDrgApplyData);
    }

    @Override
    @Transactional
    public void updateYbDrgApplyData(YbDrgApplyData ybDrgApplyData) {
//        ybDrgApplyData.setModifyTime(new Date());
        this.baseMapper.updateYbDrgApplyData(ybDrgApplyData);
    }

    @Override
    @Transactional
    public void deleteYbDrgApplyDatas(String[] Ids) {
        List<String> list = Arrays.asList(Ids);
        this.baseMapper.deleteBatchIds(list);
    }

    @Override
    @Transactional
    public int deleteDrgApplyDataByPid(YbDrgApplyData ybDrgApplyData) {
        int count = 0;
        LambdaQueryWrapper<YbDrgApply> wrapperApply = new LambdaQueryWrapper<>();
        wrapperApply.eq(YbDrgApply::getId, ybDrgApplyData.getPid());
        List<YbDrgApply> applyList = iYbDrgApplyService.list(wrapperApply);
        if (applyList.size() > 0) {
            if (applyList.get(0).getState() == YbDefaultValue.DRGAPPLYSTATE_2) {
                LambdaQueryWrapper<YbDrgApplyData> wrapper = new LambdaQueryWrapper<>();
                wrapper.eq(YbDrgApplyData::getPid, applyList.get(0).getId());
                this.baseMapper.delete(wrapper);

                YbDrgApply updateApply = new YbDrgApply();
                updateApply.setId(applyList.get(0).getId());
                updateApply.setState(YbDefaultValue.DRGAPPLYSTATE_1);
                iYbDrgApplyService.updateYbDrgApply(updateApply);
                count = 1;
            }
        }
        return count;
    }


    @Override
    @Transactional
    public void importDrgApply(YbDrgApply ybDrgApply, List<YbDrgApplyData> listData) {
//        List<YbDrgApplyData> createDataList = new ArrayList<>();
//        for (YbDrgApplyData item : listData) {
//            YbDrgApplyData rrData = new YbDrgApplyData();
//            rrData.setId(item.getId());
//            rrData.setPid(item.getPid());
//            rrData.setOrderNumber(item.getOrderNumber());//序号
//            rrData.setOrderNum(item.getOrderNum());//排序
//            rrData.setKs(item.getKs())
//        }
        if (listData.size() > 0) {
            this.saveBatch(listData);
        }

        this.iYbDrgApplyService.updateYbDrgApply(ybDrgApply);
    }

    @Override
    public List<YbDrgApplyData> getApplyDatas(YbDrgApply ybDrgApply, YbDrgVerifyView ybDrgVerifyView) {
        String k = ybDrgVerifyView.getKs();
        String j = ybDrgVerifyView.getJzjlh();
        String p = ybDrgVerifyView.getBah();
        String o = ybDrgVerifyView.getOrderNumber();

        LambdaQueryWrapper<YbDrgApplyData> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(YbDrgApplyData::getPid, ybDrgApply.getId());
        if (k != null && !k.equals("")) {
            wrapper.eq(YbDrgApplyData::getKs, k);
        }
        if (j != null && !j.equals("")) {
            wrapper.eq(YbDrgApplyData::getJzjlh, j);
        }
        if (p != null && !p.equals("")) {
            wrapper.eq(YbDrgApplyData::getBah, p);
        }
        if (o != null && !o.equals("")) {
            wrapper.eq(YbDrgApplyData::getOrderNumber, o);
        }

        return this.list(wrapper);
    }


    @Override
    public List<YbDrgApplyData> findDrgApplyDataByNotVerifys(String pid, String applyDateStr, Integer areaType) {
        return this.baseMapper.findDrgApplyDataByNotVerify(pid, applyDateStr, areaType);
    }

    @Override
    public List<YbDrgApplyData> getApplyDataByKeyFieldList(String pid, String keyField, String value) {
        LambdaQueryWrapper<YbDrgApplyData> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(YbDrgApplyData::getPid, pid);
        if (value != null && !value.equals("") && !keyField.equals("readyDoctorCode") && !keyField.equals("readyDoctorName")) {
            if (keyField.equals("orderNumber")) {
                wrapper.eq(YbDrgApplyData::getOrderNumber, value);
            }
            if (keyField.equals("ks")) {
                wrapper.eq(YbDrgApplyData::getKs, value);
            }
            if (keyField.equals("jzjlh")) {
                wrapper.eq(YbDrgApplyData::getJzjlh, value);
            }
            if (keyField.equals("bah")) {
                wrapper.eq(YbDrgApplyData::getBah, value);
            }
        }
        return this.list(wrapper);
    }
    private Lock lockDrgJk = new ReentrantLock();

    @Override
    @Transactional
    public String getDrgJk(String applyDateStr, Integer areaType) {
        String msg = "";
        if (lockDrgJk.tryLock()) {
            try {
                YbDrgApply drgApply = this.iYbDrgApplyService.findDrgApplyByApplyDateStrs(applyDateStr, areaType);
                if (drgApply != null) {
                    List<YbDrgJk> list = iYbDrgJkService.findDrgJkApplyDataByPid(drgApply.getId());
                    if (list.size() == 0) {
                        LambdaQueryWrapper<YbDrgApplyData> wrapper = new LambdaQueryWrapper<>();
                        wrapper.eq(YbDrgApplyData::getPid, drgApply.getId());
                        List<YbDrgApplyData> dataList = this.list(wrapper);
                        if (dataList.size() > 0) {
                            MySqlDB<YbDrgJkData> mysqlDB = new MySqlDB<>();
                            String mysql = this.getSql(dataList);
                            List<YbDrgJkData> jkList = mysqlDB.excuteSqlRS(new YbDrgJkData(), mysql);
                            List<YbDrgJk> createList = new ArrayList<>();
                            List<YbDrgJkData> query = new ArrayList<>();
                            if (jkList.size() > 0) {
                                for (YbDrgApplyData item : dataList) {
                                    query = jkList.stream().filter(s -> s.getJzjlh().equals(item.getJzjlh()) &&
                                            s.getBah().equals(item.getBah())).collect(Collectors.toList());
                                    if (query.size() > 0) {
                                        YbDrgJkData jkData = query.get(0);
                                        YbDrgJk jk = new YbDrgJk();
                                        jk.setId(UUID.randomUUID().toString());
                                        jk.setApplyDataId(item.getId());
                                        jk.setApplyDateStr(applyDateStr);
                                        jk.setAreaType(areaType);
                                        jk.setOrderNum(item.getOrderNum());
                                        jk.setOrderNumber(item.getOrderNumber());
                                        jk.setRyDate(jkData.getRyDate());
                                        jk.setCyDate(jkData.getCyDate());
                                        jk.setTczf(jkData.getTczf());
                                        jk.setFzCode(jkData.getFzCode());
                                        jk.setFzName(jkData.getFzName());
                                        jk.setZyzdCode(jkData.getZyzdCode());
                                        jk.setZyzdName(jkData.getZyzdName());
                                        jk.setZssCode(jkData.getZssCode());
                                        jk.setZssName(jkData.getZssName());
                                        jk.setQtzdCode(jkData.getQtzdCode());
                                        jk.setQtzdName(jkData.getQtzdName());
                                        jk.setQtssCode(jkData.getQtssCode());
                                        jk.setQtssName(jkData.getQtssName());
                                        jk.setDeptName(jkData.getDeptName());
                                        jk.setAreaId(jkData.getAreaId());
                                        jk.setAreaName(jkData.getAreaName());
                                        jk.setQz(jkData.getQz());
                                        jk.setKzrDocId(jkData.getKzrDocId());
                                        jk.setKzrDocName(jkData.getKzrDocName());
                                        jk.setZrysDocId(jkData.getZrysDocId());
                                        jk.setZrysDocName(jkData.getZrysDocName());
                                        jk.setZzysDocId(jkData.getZzysDocId());
                                        jk.setZzysDocName(jkData.getZzysDocName());
                                        jk.setZyysDocId(jkData.getZyysDocId());
                                        jk.setZyysDocName(jkData.getZyysDocName());
                                        jk.setYlzDeptName(jkData.getYlzDeptName());
                                        jk.setYlzDocId(jkData.getYlzDocId());
                                        jk.setYlzDocName(jkData.getYlzDocName());

                                        createList.add(jk);
                                    }
                                }
                                if(createList.size() > 0) {
                                    iYbDrgJkService.saveBatch(createList);
                                    YbDrgApply updateApply = new YbDrgApply();
                                    updateApply.setId(drgApply.getId());
                                    updateApply.setState(YbDefaultValue.DRGAPPLYSTATE_3);
                                    iYbDrgApplyService.updateYbDrgApply(updateApply);
                                    msg = "ok";
                                }
                            }
                        } else {
                            msg = applyDateStr+" DRG复议申请未上传数据.";
                        }
                    } else {
                        msg = applyDateStr+" DRG接口数据已获取完.";
                    }
                } else {
                    msg = "无法查找"+applyDateStr+" DRG复议申请.";
                }
            } catch (Exception e) {
                msg = e.getMessage();
                log.error(msg);
            } finally {
                lockDrgJk.unlock();
            }
        } else {
            msg = "已有其他用户正在获取His数据,请稍等...";
        }
        System.out.println("DrgJk end:" + msg);
        return msg;
    }

    private String getSql(List<YbDrgApplyData> dataList) {
        String sql = "select * from V_DrgInfo where drgNo in (";
        String inSql = "";
        for (YbDrgApplyData data : dataList) {
            if (inSql.equals("")) {
                inSql = "'" + data.getJzjlh() + "-" + data.getBah() + "'";
            } else {
                inSql = inSql + ",'" + data.getJzjlh() + "-" + data.getBah() + "'";
            }
        }
        return sql + inSql + ")";
    }

    @Override
    public void findDrgJk(String applyDateStr, Integer areaType) {
        String msg = "";
        try {
            MySqlDB<YbDrgJkData> mysqlDB = new MySqlDB<>();
            List<YbDrgJkData> list = mysqlDB.excuteSqlRS(new YbDrgJkData(),

                    "select * from V_DrgInfo where drgNo in ('642433590-3134934','641977454-3133962')");
            System.out.println(list);
        } catch (Exception e) {
            msg = e.getMessage();
            log.error(msg);
        }
    }

}