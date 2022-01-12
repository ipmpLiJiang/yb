package cc.mrbird.febs.com.service.impl;

import cc.mrbird.febs.common.domain.QueryRequest;
import cc.mrbird.febs.common.utils.SortUtil;
import cc.mrbird.febs.com.entity.ComType;
import cc.mrbird.febs.com.dao.ComTypeMapper;
import cc.mrbird.febs.com.service.IComTypeService;
import cc.mrbird.febs.system.domain.User;
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

import java.util.*;
import java.time.LocalDate;
import java.util.stream.Collectors;

/**
 * <p>
 * 服务实现类
 * </p>
 *
 * @author viki
 * @since 2021-03-05
 */
@Slf4j
@Service("IComTypeService")
@Transactional(propagation = Propagation.SUPPORTS, readOnly = true, rollbackFor = Exception.class)
public class ComTypeServiceImpl extends ServiceImpl<ComTypeMapper, ComType> implements IComTypeService {


    @Override
    public IPage<ComType> findComTypes(QueryRequest request, ComType comType) {
        try {
            LambdaQueryWrapper<ComType> queryWrapper = new LambdaQueryWrapper<>();
            if(comType.getIsDeletemark() != null) {
                queryWrapper.eq(ComType::getIsDeletemark, comType.getIsDeletemark());
            }
            if (comType.getCtType() != null) {
                queryWrapper.eq(ComType::getCtType, comType.getCtType());
            } else {
                queryWrapper.eq(ComType::getId, 0);
            }

            if (StringUtils.isNotBlank(comType.getCurrencyField())) {
                queryWrapper.like(ComType::getCurrencyField, comType.getCurrencyField());
            }

            Page<ComType> page = new Page<>();
            SortUtil.handlePageSort(request, page, false);//true 是属性  false是数据库字段可两个
            return this.page(page, queryWrapper);
        } catch (Exception e) {
            log.error("获取字典信息失败", e);
            return null;
        }
    }

    @Override
    public IPage<ComType> findComTypeList(QueryRequest request, ComType comType) {
        try {
            Page<ComType> page = new Page<>();
            SortUtil.handlePageSort(request, page, false);//true 是属性  false是数据库字段可两个
            return this.baseMapper.findComType(page, comType);
        } catch (Exception e) {
            log.error("获取失败", e);
            return null;
        }
    }

    @Override
    @Transactional
    public void createComType(ComType comType) {
        comType.setCreateTime(new Date());
        comType.setIsDeletemark(1);
        this.save(comType);
    }

    @Override
    @Transactional
    public void updateComType(ComType comType) {
        comType.setModifyTime(new Date());
        this.baseMapper.updateComType(comType);
    }

    @Override
    @Transactional
    public void editComType(ComType comType, User currentUser) {
        Date thisDate = new Date();
        if (comType.getId() == null) {
            comType.setIsDeletemark(1);
            comType.setCreateTime(thisDate);
            comType.setCreateUserId(currentUser.getUserId());
            this.save(comType);
        } else {
            comType.setModifyTime(thisDate);
            comType.setModifyUserId(currentUser.getUserId());
            this.baseMapper.updateComType(comType);
        }
    }

    @Override
    @Transactional
    public void editDrgComType(ComType comType, List<ComType> list, User currentUser) {
        Date thisDate = new Date();
        List<ComType> updateList = new ArrayList<>();
        List<ComType> yxList = list.stream().filter(s -> s.getIsDeletemark() == 1).collect(Collectors.toList());
        List<ComType> wxList = list.stream().filter(s -> s.getIsDeletemark() != 1).collect(Collectors.toList());
        if (yxList.size() > 0) {
            yxList = yxList.stream().sorted(Comparator.comparing(ComType::getOrderNum)).collect(Collectors.toList());
        }
        if (wxList.size() > 0) {
            wxList = wxList.stream().sorted(Comparator.comparing(ComType::getOrderNum)).collect(Collectors.toList());
        }

        if (comType.getId() == null) {
            boolean isUpdate = false;
            comType.setIsDeletemark(1);
            comType.setCreateTime(thisDate);
            comType.setCreateUserId(currentUser.getUserId());
            if (yxList.size() == 0) {
                comType.setOrderNum(1);
            } else {
                if (comType.getOrderNum() > yxList.size()) {
                    comType.setOrderNum(yxList.size() + 1);
                } else {
                    isUpdate = true;
                }
            }
            this.setCreateUpdateComType(comType, updateList, yxList, isUpdate);
            this.save(comType);
        } else {
            comType.setModifyTime(thisDate);
            comType.setModifyUserId(currentUser.getUserId());

            this.setUpdateComType(comType, updateList, yxList, wxList);
            this.baseMapper.updateComType(comType);
        }
        if (updateList.size() > 0) {
            this.updateBatchById(updateList);
        }

    }

    private void setCreateUpdateComType(ComType comType, List<ComType> updateList, List<ComType> yxList,  boolean isUpdate) {
        int orderNum = comType.getOrderNum();
        if (isUpdate) {
            isUpdate = false;
            for (ComType item : yxList) {
                if (!isUpdate && item.getOrderNum() == comType.getOrderNum()) {
                    isUpdate = true;
                }
                if (isUpdate) {
                    ComType udpate = new ComType();
                    orderNum += 1;
                    udpate.setOrderNum(orderNum);
                    udpate.setId(item.getId());
                    updateList.add(udpate);
                }
            }
        }
//        排序都按 1 开始，新增不会新增无效数据 注释(原来是连续排序，改为按1开始)
//        orderNum = 0;
//        for (ComType item : wxList) {
//            ComType udpate = new ComType();
//            orderNum += 1;
//            udpate.setOrderNum(orderNum);
//            udpate.setId(item.getId());
//            updateList.add(udpate);
//        }
    }

    private void setUpdateComType(ComType comType, List<ComType> updateList, List<ComType> yxList, List<ComType> wxList) {
        int lodOrderNum = 0;
        int lodIsDeletemark = 0;
        int orderNum = 0;

        List<ComType> query = yxList.stream().filter(s -> s.getId().equals(comType.getId())).collect(Collectors.toList());
        int maxOrderNum = 0;
        int minOrderNum = 0;
        if (query.size() > 0) {
            lodIsDeletemark = query.get(0).getIsDeletemark();
            lodOrderNum = query.get(0).getOrderNum();
            if (comType.getIsDeletemark() == 1 && lodIsDeletemark == 1) {
                if (comType.getOrderNum() > yxList.size()) {
                    comType.setOrderNum(yxList.size());
                }
                orderNum = comType.getOrderNum();
                this.setXiangTongUpdate(orderNum, lodOrderNum, yxList, updateList);
            } else {
                int dqOrderNum = lodOrderNum + 1;
                for (ComType item : yxList) {
                    if (item.getOrderNum() == dqOrderNum) {
                        ComType udpate = new ComType();
                        udpate.setOrderNum(item.getOrderNum() - 1);
                        udpate.setId(item.getId());
                        updateList.add(udpate);
                        dqOrderNum = item.getOrderNum() + 1;
                    }
                }
                if (wxList.size() > 0) {
                    maxOrderNum = wxList.stream().max(Comparator.comparing(ComType::getOrderNum)).get().getOrderNum();
                    dqOrderNum = comType.getOrderNum();
                    if (dqOrderNum > maxOrderNum) {
                        comType.setOrderNum(maxOrderNum + 1);
                    } else {
                        for (ComType item : wxList) {
                            if (item.getOrderNum() == dqOrderNum) {
                                ComType udpate = new ComType();
                                udpate.setOrderNum(item.getOrderNum() + 1);
                                udpate.setId(item.getId());
                                updateList.add(udpate);
                                dqOrderNum = udpate.getOrderNum();
                            }
                        }
                    }
                } else {
                    comType.setOrderNum(1);
                }
            }
        } else {
            query = wxList.stream().filter(s -> s.getId().equals(comType.getId())).collect(Collectors.toList());
            lodIsDeletemark = query.get(0).getIsDeletemark();
            lodOrderNum = query.get(0).getOrderNum();
            if (comType.getIsDeletemark() == 2 && lodIsDeletemark == 2) {
                maxOrderNum = wxList.stream().max(Comparator.comparing(ComType::getOrderNum)).get().getOrderNum();
                minOrderNum = wxList.stream().min(Comparator.comparing(ComType::getOrderNum)).get().getOrderNum();
                if (comType.getOrderNum() > maxOrderNum) {
                    comType.setOrderNum(maxOrderNum);
                }
                if (comType.getOrderNum() < minOrderNum) {
                    comType.setOrderNum(minOrderNum);
                }
                orderNum = comType.getOrderNum();
                this.setXiangTongUpdate(orderNum, lodOrderNum, wxList, updateList);
            } else {
                int dqOrderNum = lodOrderNum + 1;
                for (ComType item : wxList) {
                    if (item.getOrderNum() == dqOrderNum) {
                        ComType udpate = new ComType();
                        udpate.setOrderNum(item.getOrderNum() - 1);
                        udpate.setId(item.getId());
                        updateList.add(udpate);
                        dqOrderNum = item.getOrderNum() + 1;
                    }
                }
                if (yxList.size() > 0) {
                    maxOrderNum = yxList.stream().max(Comparator.comparing(ComType::getOrderNum)).get().getOrderNum();
                    dqOrderNum = comType.getOrderNum();
                    if (dqOrderNum > maxOrderNum) {
                        comType.setOrderNum(maxOrderNum + 1);
                    } else {
                        for (ComType item : yxList) {
                            if (item.getOrderNum() == dqOrderNum) {
                                ComType udpate = new ComType();
                                udpate.setOrderNum(item.getOrderNum() + 1);
                                udpate.setId(item.getId());
                                updateList.add(udpate);
                                dqOrderNum = udpate.getOrderNum();
                            }
                        }
                    }
                } else {
                    comType.setOrderNum(1);
                }
            }
        }
    }

    private void setXiangTongUpdate(int orderNum, int lodOrderNum, List<ComType> list, List<ComType> updateList) {
        for (ComType item : list) {
            if (orderNum < lodOrderNum) {
                if (item.getOrderNum() >= orderNum && item.getOrderNum() < lodOrderNum) {
                    ComType udpate = new ComType();
                    udpate.setOrderNum(item.getOrderNum() + 1);
                    udpate.setId(item.getId());
                    updateList.add(udpate);
                }
            }
            if (orderNum > lodOrderNum) {
                if (item.getOrderNum() <= orderNum && item.getOrderNum() > lodOrderNum) {
                    ComType udpate = new ComType();
                    udpate.setOrderNum(item.getOrderNum() - 1);
                    udpate.setId(item.getId());
                    updateList.add(udpate);
                }
            }
        }
    }

    @Override
    public List<ComType> findComTypeList(ComType comType) {
        LambdaQueryWrapper<ComType> queryWrapper = new LambdaQueryWrapper<>();
        if (comType.getIsDeletemark() != null) {
            queryWrapper.eq(ComType::getIsDeletemark, comType.getIsDeletemark());//1是未删 0是已删
        }
        if (comType.getCtType() != null) {
            queryWrapper.eq(ComType::getCtType, comType.getCtType());
        } else {
            queryWrapper.eq(ComType::getId, 0);
        }
        if (comType.getCtName() != null) {
            queryWrapper.eq(ComType::getCtName, comType.getCtName());
        }
        if (comType.getCurrencyField() != null) {
            queryWrapper.like(ComType::getCtName, comType.getCurrencyField());
        }
        return this.list(queryWrapper);
    }

    @Override
    public List<ComType> findComTypeLikeList(ComType comType) {
        List<ComType> list = new ArrayList<>();
        LambdaQueryWrapper<ComType> queryWrapper = new LambdaQueryWrapper<>();
        if (comType.getIsDeletemark() != null) {
            queryWrapper.eq(ComType::getIsDeletemark, comType.getIsDeletemark());//1是未删 0是已删
        }
        if (comType.getCtType() != null) {
            queryWrapper.eq(ComType::getCtType, comType.getCtType());
        }
        if (comType.getCtName() != null) {
            queryWrapper.eq(ComType::getCtName, comType.getCtName());
        }
        if (comType.getComments() != null) {
            queryWrapper.like(ComType::getCtName, comType.getComments());
        }


        list = this.list(queryWrapper);
        int count = 15;
        if (list.size() >= count) {
            list = list.subList(0, count);
        }
        return list;
    }

    @Override
    @Transactional
    public void deleteComTypes(String[] Ids) {
        List<String> list = Arrays.asList(Ids);
        this.baseMapper.deleteBatchIds(list);
    }


}