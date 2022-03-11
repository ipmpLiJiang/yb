package cc.mrbird.febs.com.controller;

import cc.mrbird.febs.common.annotation.Log;
import cc.mrbird.febs.common.controller.BaseController;
import cc.mrbird.febs.common.domain.FebsResponse;
import cc.mrbird.febs.common.domain.router.VueRouter;
import cc.mrbird.febs.common.exception.FebsException;
import cc.mrbird.febs.common.domain.QueryRequest;

import cc.mrbird.febs.com.service.IComTypeService;
import cc.mrbird.febs.com.entity.ComType;

import cc.mrbird.febs.common.utils.FebsUtil;
import cc.mrbird.febs.system.domain.User;
import cc.mrbird.febs.yb.domain.ResponseResult;
import cc.mrbird.febs.yb.domain.ResponseResultData;
import com.baomidou.mybatisplus.core.toolkit.StringPool;
import com.wuwenze.poi.ExcelKit;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @author viki
 * @since 2021-03-05
 */
@Slf4j
@Validated
@RestController
@RequestMapping("comType")

public class ComTypeController extends BaseController {

    private String message;
    @Autowired
    public IComTypeService iComTypeService;


    /**
     * 分页查询数据
     *
     * @param request 分页信息
     * @param comType 查询条件
     * @return
     */
    @GetMapping
    @RequiresPermissions("comType:view")
    public Map<String, Object> List(QueryRequest request, ComType comType) {
        return getDataTable(this.iComTypeService.findComTypes(request, comType));
    }

    @GetMapping("findList")
    public FebsResponse fList(ComType comType) {
        List<ComType> list = new ArrayList<>();
        try {
            list = this.iComTypeService.findComTypeList(comType);
            if (list.size() > 0) {
                for (ComType ct : list) {
                    if (ct.getOrderNum() == null)
                        ct.setOrderNum(0);
                }
                list = list.stream().sorted(Comparator.comparing(ComType::getOrderNum)).collect(Collectors.toList());
            }
        } catch (Exception e) {
            message = "数据获取失败";
            log.error(message, e);
        }
        return new FebsResponse().data(list);
    }

    /**
     * 添加
     *
     * @param comType
     * @return
     */
    @Log("新增/按钮")
    @PostMapping
    @RequiresPermissions("comType:add")
    public void addComType(@Valid ComType comType) throws FebsException {
        try {
            User currentUser = FebsUtil.getCurrentUser();
            comType.setCreateUserId(currentUser.getUserId());
            this.iComTypeService.createComType(comType);
        } catch (Exception e) {
            message = "新增/按钮失败";
            log.error(message, e);
            throw new FebsException(message);
        }
    }

    /**
     * 修改
     *
     * @param comType
     * @return
     */
    @Log("修改")
    @PutMapping
    @RequiresPermissions("comType:update")
    public FebsResponse updateComType(@Valid ComType comType) {
        int success = 0;
        try {
            List<ComType> list = this.iComTypeService.findComTypeList(comType);
            if (list.size() == 0) {
                User currentUser = FebsUtil.getCurrentUser();
                this.iComTypeService.editComType(comType, currentUser);
                success = 1;
            } else {
                success = 0;
                message = comType.getCtName() + ", 已存在，操作失败.";
            }
        } catch (Exception e) {
            message = "操作失败.";
            log.error(message, e);
        }
        ResponseResult rr = new ResponseResult();
        rr.setSuccess(success);
        rr.setMessage(message);
        return new FebsResponse().data(rr);
    }

    @Log("修改")
    @PutMapping("updateDrgType")
    @RequiresPermissions("comType:update")
    public FebsResponse updateDrgComType(@Valid ComType comType) {
        int success = 0;
        try {
            ComType query = new ComType();
            query.setCtType(comType.getCtType());
            List<ComType> listAll = this.iComTypeService.findComTypeList(query);
            if(comType.getCtName() != null) {
                comType.setCtName(comType.getCtName().trim());
            }
            List<ComType> list = listAll.stream().filter(s -> s.getCtName().equals(comType.getCtName())).collect(Collectors.toList());
            if (list.size() == 0 || comType.getId() != null) {
                User currentUser = FebsUtil.getCurrentUser();
                this.iComTypeService.editDrgComType(comType, listAll, currentUser);
                success = 1;
            } else {
                success = 0;
                message = comType.getCtName() + ", 已存在，操作失败.";
            }
        } catch (Exception e) {
            message = "操作失败.";
            log.error(message, e);
        }
        ResponseResult rr = new ResponseResult();
        rr.setSuccess(success);
        rr.setMessage(message);
        return new FebsResponse().data(rr);
    }

    @Log("删除")
    @DeleteMapping("/{ids}")
    @RequiresPermissions("comType:delete")
    public void deleteComTypes(@NotBlank(message = "{required}") @PathVariable String ids) throws FebsException {
        try {
            String[] arr_ids = ids.split(StringPool.COMMA);
            this.iComTypeService.deleteComTypes(arr_ids);
        } catch (Exception e) {
            message = "删除失败";
            log.error(message, e);
            throw new FebsException(message);
        }
    }

    @PostMapping("excel")
    @RequiresPermissions("comType:export")
    public void export(QueryRequest request, ComType comType, HttpServletResponse response) throws FebsException {
        try {
            List<ComType> comTypes = this.iComTypeService.findComTypes(request, comType).getRecords();
            ExcelKit.$Export(ComType.class, response).downXlsx(comTypes, false);
        } catch (Exception e) {
            message = "导出Excel失败";
            log.error(message, e);
            throw new FebsException(message);
        }
    }

    @GetMapping("/{id}")
    public ComType detail(@NotBlank(message = "{required}") @PathVariable String id) {
        ComType comType = this.iComTypeService.getById(id);
        return comType;
    }

    @GetMapping("getComTypeList")
    public List<ComType> findComTypes(@Valid ComType comType) {
        return this.iComTypeService.findComTypeList(comType);
    }

    @GetMapping("getComTypeByNameList")
    public FebsResponse findComTypeByLists(@Valid ComType comType) {
        List<ComType> list = new ArrayList<>();
        try{
            list = this.iComTypeService.findComTypeLikeList(comType);

        } catch (Exception e) {
            log.error("获取科室失败", e);
        }

        return new FebsResponse().data(list);
    }
}