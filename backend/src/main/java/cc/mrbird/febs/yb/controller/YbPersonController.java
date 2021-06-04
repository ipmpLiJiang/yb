package cc.mrbird.febs.yb.controller;

import cc.mrbird.febs.export.excel.ExportExcelUtils;
import cc.mrbird.febs.common.annotation.Log;
import cc.mrbird.febs.common.controller.BaseController;
import cc.mrbird.febs.common.domain.FebsResponse;
import cc.mrbird.febs.common.exception.FebsException;
import cc.mrbird.febs.common.domain.QueryRequest;

import cc.mrbird.febs.common.properties.FebsProperties;
import cc.mrbird.febs.yb.domain.ResponseResultData;
import cc.mrbird.febs.yb.service.IYbPersonService;
import cc.mrbird.febs.yb.entity.YbPerson;

import cc.mrbird.febs.common.utils.FebsUtil;
import cc.mrbird.febs.system.domain.User;
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
import java.util.List;
import java.util.Map;

/**
 * @author viki
 * @since 2020-07-21
 */
@Slf4j
@Validated
@RestController
@RequestMapping("ybPerson")

public class YbPersonController extends BaseController {

    private String message;
    @Autowired
    public IYbPersonService iYbPersonService;

    @Autowired
    FebsProperties febsProperties;


    /**
     * 分页查询数据
     *
     * @param request  分页信息
     * @param ybPerson 查询条件
     * @return
     */
    @GetMapping
    @RequiresPermissions("ybPerson:view")
    public Map<String, Object> List(QueryRequest request, YbPerson ybPerson) {
        return getDataTable(this.iYbPersonService.findYbPersonList(request, ybPerson));
    }

    @GetMapping("check/{personCode}")
    public boolean checkUserName(@NotBlank(message = "{required}") @PathVariable String personCode) {
        return this.iYbPersonService.findByName(personCode) == null;
    }

    @GetMapping("findPersonList")
    public FebsResponse findPersonLists(YbPerson ybPerson) {
        List<YbPerson> list = new ArrayList<>();
        try {
            list = this.iYbPersonService.findPersonList(ybPerson,1);

        } catch (Exception e) {
            log.error("获取医生失败", e);
        }

        return new FebsResponse().data(list);
    }

    @PostMapping("importUser")
    @RequiresPermissions("ybPerson:import")
    public FebsResponse importUsers(Integer type) {
        int success = 0;
        try {
            String msg = this.iYbPersonService.importUserRoles(type);
            if (msg.equals("roleError")) {
                message = "角色未创建.";
            } else if (msg.equals("deptError")) {
                message = "部门未创建.";
            } else {
                message = "用户同步成功.";
                success = 1;
            }

        } catch (Exception e) {
            message = "用户同步失败.";
            log.error(message, e);
        }
        ResponseResultData rrd = new ResponseResultData();
        rrd.setSuccess(success);
        rrd.setMessage(message);
        return new FebsResponse().data(rrd);

    }


    @PostMapping("importPerson")
    @RequiresPermissions("ybPerson:import")
    public FebsResponse importUserToPersons() {
        int success = 0;
        try {
            User currentUser = FebsUtil.getCurrentUser();
            boolean isTrue = this.iYbPersonService.importPerson(currentUser);
            if (isTrue) {
                success = 1;
                message = "人员同步成功.";
            } else {
                message = "人员同步失败或没有可同步的数据.";
            }
        } catch (Exception e) {
            message = "人员同步失败.";
            log.error(message, e);
        }
        ResponseResultData rrd = new ResponseResultData();
        rrd.setSuccess(success);
        rrd.setMessage(message);
        return new FebsResponse().data(rrd);

    }

    /**
     * 添加
     *
     * @param ybPerson
     * @return
     */
    @Log("新增/按钮")
    @PostMapping
    @RequiresPermissions("ybPerson:add")
    public void addYbPerson(@Valid YbPerson ybPerson) throws FebsException {
        try {
            User currentUser = FebsUtil.getCurrentUser();
            ybPerson.setCreateUserId(currentUser.getUserId());
            this.iYbPersonService.createYbPerson(ybPerson);
        } catch (Exception e) {
            message = "新增/按钮失败";
            log.error(message, e);
            throw new FebsException(message);
        }
    }

    /**
     * 修改
     *
     * @param ybPerson
     * @return
     */
    @Log("修改")
    @PutMapping
    @RequiresPermissions("ybPerson:update")
    public void updateYbPerson(@Valid YbPerson ybPerson) throws FebsException {
        try {
            User currentUser = FebsUtil.getCurrentUser();
            ybPerson.setModifyUserId(currentUser.getUserId());
            this.iYbPersonService.updateYbPerson(ybPerson);
        } catch (Exception e) {
            message = "修改失败";
            log.error(message, e);
            throw new FebsException(message);
        }
    }


    @Log("删除")
    @DeleteMapping("/{ids}")
    @RequiresPermissions("ybPerson:delete")
    public void deleteYbPersons(@NotBlank(message = "{required}") @PathVariable String ids) throws FebsException {
        try {
            String[] arr_ids = ids.split(StringPool.COMMA);
            this.iYbPersonService.deleteYbPersons(arr_ids);
        } catch (Exception e) {
            message = "删除失败";
            log.error(message, e);
            throw new FebsException(message);
        }
    }

    @PostMapping("excel")
    @RequiresPermissions("ybPerson:export")
    public void export(QueryRequest request, YbPerson ybPerson, HttpServletResponse response) throws FebsException {
        try {
            List<YbPerson> ybPersons = this.iYbPersonService.findYbPersons(request, ybPerson).getRecords();
            ExcelKit.$Export(YbPerson.class, response).downXlsx(ybPersons, false);
        } catch (Exception e) {
            message = "导出Excel失败";
            log.error(message, e);
            throw new FebsException(message);
        }
    }

    @PostMapping("excel1")
    //@RequiresPermissions("ybPerson:export")
    public void export1(QueryRequest request, YbPerson ybPerson, String dataJson, HttpServletResponse response) throws FebsException {
        try {
            List<YbPerson> ybPersons = this.iYbPersonService.findYbPersons(request, ybPerson).getRecords();
//            ExportExcelUtils.exportCustomExcel(response, ybPersons,dataJson,"人员信息");
            String tempUrl = febsProperties.getUploadPath() + "person.xlsx";
            ExportExcelUtils.exportTemplateExcel(response, ybPersons, dataJson, tempUrl, 2);
        } catch (Exception e) {
            message = "导出Excel失败";
            log.error(message, e);
            throw new FebsException(message);
        }
    }

    @GetMapping("/{id}")
    public YbPerson detail(@NotBlank(message = "{required}") @PathVariable String id) {
        YbPerson ybPerson = this.iYbPersonService.getById(id);
        return ybPerson;
    }
}