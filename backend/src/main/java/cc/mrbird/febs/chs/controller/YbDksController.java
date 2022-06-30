package cc.mrbird.febs.chs.controller;

import cc.mrbird.febs.common.annotation.Log;
import cc.mrbird.febs.common.controller.BaseController;
import cc.mrbird.febs.common.domain.FebsResponse;
import cc.mrbird.febs.common.domain.router.VueRouter;
import cc.mrbird.febs.common.exception.FebsException;
import cc.mrbird.febs.common.domain.QueryRequest;

import cc.mrbird.febs.chs.service.IYbDksService;
import cc.mrbird.febs.chs.entity.YbDks;

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
 * @since 2022-06-24
 */
@Slf4j
@Validated
@RestController
@RequestMapping("ybDks")

public class YbDksController extends BaseController {

    private String message;
    @Autowired
    public IYbDksService iYbDksService;


    /**
     * 分页查询数据
     *
     * @param request 分页信息
     * @param ybDks   查询条件
     * @return
     */
    @GetMapping
    @RequiresPermissions("ybDks:view")
    public Map<String, Object> List(QueryRequest request, YbDks ybDks) {
        return getDataTable(this.iYbDksService.findYbDkss(request, ybDks));
    }

    /**
     * 添加
     *
     * @param ybDks
     * @return
     */
    @Log("新增/按钮")
    @PostMapping
    @RequiresPermissions("ybDks:add")
    public void addYbDks(@Valid YbDks ybDks) throws FebsException {
        try {
            User currentUser = FebsUtil.getCurrentUser();
            ybDks.setCreateUserId(currentUser.getUserId());
            this.iYbDksService.createYbDks(ybDks);
        } catch (Exception e) {
            message = "新增/按钮失败";
            log.error(message, e);
            throw new FebsException(message);
        }
    }

    /**
     * 修改
     *
     * @param ybDks
     * @return
     */
    @Log("修改")
    @PutMapping
    @RequiresPermissions("ybDks:update")
    public void updateYbDks(@Valid YbDks ybDks) throws FebsException {
        try {
            User currentUser = FebsUtil.getCurrentUser();
            ybDks.setModifyUserId(currentUser.getUserId());
            this.iYbDksService.updateYbDks(ybDks);
        } catch (Exception e) {
            message = "修改失败";
            log.error(message, e);
            throw new FebsException(message);
        }
    }


    @Log("删除")
    @DeleteMapping("/{ids}")
    @RequiresPermissions("ybDks:delete")
    public void deleteYbDkss(@NotBlank(message = "{required}") @PathVariable String ids) throws FebsException {
        try {
            String[] arr_ids = ids.split(StringPool.COMMA);
            this.iYbDksService.deleteYbDkss(arr_ids);
        } catch (Exception e) {
            message = "删除失败";
            log.error(message, e);
            throw new FebsException(message);
        }
    }

    @PostMapping("excel")
    @RequiresPermissions("ybDks:export")
    public void export(QueryRequest request, YbDks ybDks, HttpServletResponse response) throws FebsException {
        try {
            List<YbDks> ybDkss = this.iYbDksService.findYbDkss(request, ybDks).getRecords();
            ExcelKit.$Export(YbDks.class, response).downXlsx(ybDkss, false);
        } catch (Exception e) {
            message = "导出Excel失败";
            log.error(message, e);
            throw new FebsException(message);
        }
    }

    @GetMapping("/{id}")
    public YbDks detail(@NotBlank(message = "{required}") @PathVariable String id) {
        YbDks ybDks = this.iYbDksService.getById(id);
        return ybDks;
    }

    @GetMapping("findChsDksList")
    public FebsResponse findChsDeptLists(YbDks ybDks) {
        List<YbDks> list = new ArrayList<>();
        try {
            list = this.iYbDksService.findDksList(ybDks, 1);

        } catch (Exception e) {
            log.error("获取大科室失败", e);
        }

        return new FebsResponse().data(list);
    }

    @GetMapping("findDksChsConfireList")
    public FebsResponse findDksChsConfireLists(String comments, Integer areaType) {
        List<YbDks> list = new ArrayList<>();
        try{
            User currentUser = FebsUtil.getCurrentUser();
            list = this.iYbDksService.findDksChsConfireList(currentUser.getUsername(),comments,areaType);

        } catch (Exception e) {
            log.error("获取科室失败", e);
        }

        return new FebsResponse().data(list);
    }
}