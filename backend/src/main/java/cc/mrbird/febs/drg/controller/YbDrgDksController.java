package cc.mrbird.febs.drg.controller;

import cc.mrbird.febs.common.annotation.Log;
import cc.mrbird.febs.common.controller.BaseController;
import cc.mrbird.febs.common.domain.FebsResponse;
import cc.mrbird.febs.common.domain.router.VueRouter;
import cc.mrbird.febs.common.exception.FebsException;
import cc.mrbird.febs.common.domain.QueryRequest;

import cc.mrbird.febs.drg.service.IYbDrgDksService;
import cc.mrbird.febs.drg.entity.YbDrgDks;

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
 * @since 2022-03-16
 */
@Slf4j
@Validated
@RestController
@RequestMapping("ybDrgDks")

public class YbDrgDksController extends BaseController {

    private String message;
    @Autowired
    public IYbDrgDksService iYbDrgDksService;


    /**
     * 分页查询数据
     *
     * @param request  分页信息
     * @param ybDrgDks 查询条件
     * @return
     */
    @GetMapping
    @RequiresPermissions("ybDrgDks:view")
    public Map<String, Object> List(QueryRequest request, YbDrgDks ybDrgDks) {
        return getDataTable(this.iYbDrgDksService.findYbDrgDkss(request, ybDrgDks));
    }

    /**
     * 添加
     *
     * @param ybDrgDks
     * @return
     */
    @Log("新增/按钮")
    @PostMapping
    @RequiresPermissions("ybDrgDks:add")
    public void addYbDrgDks(@Valid YbDrgDks ybDrgDks) throws FebsException {
        try {
            User currentUser = FebsUtil.getCurrentUser();
//        ybDrgDks.setCreateUserId(currentUser.getUserId());
            this.iYbDrgDksService.createYbDrgDks(ybDrgDks);
        } catch (Exception e) {
            message = "新增/按钮失败";
            log.error(message, e);
            throw new FebsException(message);
        }
    }

    /**
     * 修改
     *
     * @param ybDrgDks
     * @return
     */
    @Log("修改")
    @PutMapping
    @RequiresPermissions("ybDrgDks:update")
    public void updateYbDrgDks(@Valid YbDrgDks ybDrgDks) throws FebsException {
        try {
            User currentUser = FebsUtil.getCurrentUser();
//      ybDrgDks.setModifyUserId(currentUser.getUserId());
            this.iYbDrgDksService.updateYbDrgDks(ybDrgDks);
        } catch (Exception e) {
            message = "修改失败";
            log.error(message, e);
            throw new FebsException(message);
        }
    }

    @GetMapping("findDksList")
    public FebsResponse findDeptLists(YbDrgDks ybDrgDks) {
        List<YbDrgDks> list = new ArrayList<>();
        try {
            list = this.iYbDrgDksService.findDksList(ybDrgDks, 1);

        } catch (Exception e) {
            log.error("获取大专业失败", e);
        }

        return new FebsResponse().data(list);
    }


    @Log("删除")
    @DeleteMapping("/{ids}")
    @RequiresPermissions("ybDrgDks:delete")
    public void deleteYbDrgDkss(@NotBlank(message = "{required}") @PathVariable String ids) throws FebsException {
        try {
            String[] arr_ids = ids.split(StringPool.COMMA);
            this.iYbDrgDksService.deleteYbDrgDkss(arr_ids);
        } catch (Exception e) {
            message = "删除失败";
            log.error(message, e);
            throw new FebsException(message);
        }
    }

    @PostMapping("excel")
    @RequiresPermissions("ybDrgDks:export")
    public void export(QueryRequest request, YbDrgDks ybDrgDks, HttpServletResponse response) throws FebsException {
        try {
            List<YbDrgDks> ybDrgDkss = this.iYbDrgDksService.findYbDrgDkss(request, ybDrgDks).getRecords();
            ExcelKit.$Export(YbDrgDks.class, response).downXlsx(ybDrgDkss, false);
        } catch (Exception e) {
            message = "导出Excel失败";
            log.error(message, e);
            throw new FebsException(message);
        }
    }

    @GetMapping("/{id}")
    public YbDrgDks detail(@NotBlank(message = "{required}") @PathVariable String id) {
        YbDrgDks ybDrgDks = this.iYbDrgDksService.getById(id);
        return ybDrgDks;
    }
}