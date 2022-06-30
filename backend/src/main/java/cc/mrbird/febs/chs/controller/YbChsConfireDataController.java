package cc.mrbird.febs.chs.controller;

import cc.mrbird.febs.common.annotation.Log;
import cc.mrbird.febs.common.controller.BaseController;
import cc.mrbird.febs.common.domain.router.VueRouter;
import cc.mrbird.febs.common.exception.FebsException;
import cc.mrbird.febs.common.domain.QueryRequest;

import cc.mrbird.febs.chs.service.IYbChsConfireDataService;
import cc.mrbird.febs.chs.entity.YbChsConfireData;

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
 * @since 2022-06-30
 */
@Slf4j
@Validated
@RestController
@RequestMapping("ybChsConfireData")

public class YbChsConfireDataController extends BaseController {

    private String message;
    @Autowired
    public IYbChsConfireDataService iYbChsConfireDataService;


    /**
     * 分页查询数据
     *
     * @param request          分页信息
     * @param ybChsConfireData 查询条件
     * @return
     */
    @GetMapping
    @RequiresPermissions("ybChsConfireData:view")
    public Map<String, Object> List(QueryRequest request, YbChsConfireData ybChsConfireData) {
        return getDataTable(this.iYbChsConfireDataService.findYbChsConfireDatas(request, ybChsConfireData));
    }

    /**
     * 添加
     *
     * @param ybChsConfireData
     * @return
     */
    @Log("新增/按钮")
    @PostMapping
    @RequiresPermissions("ybChsConfireData:add")
    public void addYbChsConfireData(@Valid YbChsConfireData ybChsConfireData) throws FebsException {
        try {
            User currentUser = FebsUtil.getCurrentUser();
//        ybChsConfireData.setCreateUserId(currentUser.getUserId());
            this.iYbChsConfireDataService.createYbChsConfireData(ybChsConfireData);
        } catch (Exception e) {
            message = "新增/按钮失败";
            log.error(message, e);
            throw new FebsException(message);
        }
    }

    /**
     * 修改
     *
     * @param ybChsConfireData
     * @return
     */
    @Log("修改")
    @PutMapping
    @RequiresPermissions("ybChsConfireData:update")
    public void updateYbChsConfireData(@Valid YbChsConfireData ybChsConfireData) throws FebsException {
        try {
            User currentUser = FebsUtil.getCurrentUser();
//      ybChsConfireData.setModifyUserId(currentUser.getUserId());
            this.iYbChsConfireDataService.updateYbChsConfireData(ybChsConfireData);
        } catch (Exception e) {
            message = "修改失败";
            log.error(message, e);
            throw new FebsException(message);
        }
    }


    @Log("删除")
    @DeleteMapping("/{ids}")
    @RequiresPermissions("ybChsConfireData:delete")
    public void deleteYbChsConfireDatas(@NotBlank(message = "{required}") @PathVariable String ids) throws FebsException {
        try {
            String[] arr_ids = ids.split(StringPool.COMMA);
            this.iYbChsConfireDataService.deleteYbChsConfireDatas(arr_ids);
        } catch (Exception e) {
            message = "删除失败";
            log.error(message, e);
            throw new FebsException(message);
        }
    }

    @PostMapping("excel")
    @RequiresPermissions("ybChsConfireData:export")
    public void export(QueryRequest request, YbChsConfireData ybChsConfireData, HttpServletResponse response) throws FebsException {
        try {
            List<YbChsConfireData> ybChsConfireDatas = this.iYbChsConfireDataService.findYbChsConfireDatas(request, ybChsConfireData).getRecords();
            ExcelKit.$Export(YbChsConfireData.class, response).downXlsx(ybChsConfireDatas, false);
        } catch (Exception e) {
            message = "导出Excel失败";
            log.error(message, e);
            throw new FebsException(message);
        }
    }

    @GetMapping("/{id}")
    public YbChsConfireData detail(@NotBlank(message = "{required}") @PathVariable String id) {
        YbChsConfireData ybChsConfireData = this.iYbChsConfireDataService.getById(id);
        return ybChsConfireData;
    }
}