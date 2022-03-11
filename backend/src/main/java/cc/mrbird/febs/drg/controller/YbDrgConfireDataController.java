package cc.mrbird.febs.drg.controller;

import cc.mrbird.febs.common.annotation.Log;
import cc.mrbird.febs.common.controller.BaseController;
import cc.mrbird.febs.common.domain.FebsResponse;
import cc.mrbird.febs.common.domain.QueryRequest;
import cc.mrbird.febs.common.exception.FebsException;
import cc.mrbird.febs.common.utils.FebsUtil;
import cc.mrbird.febs.system.domain.User;
import cc.mrbird.febs.drg.entity.YbDrgConfireData;
import cc.mrbird.febs.drg.service.IYbDrgConfireDataService;
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
 * @since 2021-01-11
 */
@Slf4j
@Validated
@RestController
@RequestMapping("ybDrgConfireData")

public class YbDrgConfireDataController extends BaseController {

    private String message;
    @Autowired
    public IYbDrgConfireDataService iYbDrgConfireDataService;


    /**
     * 分页查询数据
     *
     * @param request             分页信息
     * @param ybDrgConfireData 查询条件
     * @return
     */
    @GetMapping
    @RequiresPermissions("ybDrgConfireData:view")
    public Map<String, Object> List(QueryRequest request, YbDrgConfireData ybDrgConfireData) {
        return getDataTable(this.iYbDrgConfireDataService.findYbDrgConfireDatas(request, ybDrgConfireData));
    }

    /**
     * 添加
     *
     * @param ybDrgConfireData
     * @return
     */
    @Log("新增/按钮")
    @PostMapping
    @RequiresPermissions("ybDrgConfireData:add")
    public void addYbDrgConfireData(@Valid YbDrgConfireData ybDrgConfireData) throws FebsException {
        try {
//            User currentUser = FebsUtil.getCurrentUser();
//            ybDrgConfireData.setCreateUserId(currentUser.getUserId());
            this.iYbDrgConfireDataService.createYbDrgConfireData(ybDrgConfireData);
        } catch (Exception e) {
            message = "新增/按钮失败";
            log.error(message, e);
            throw new FebsException(message);
        }
    }

    /**
     * 修改
     *
     * @param ybDrgConfireData
     * @return
     */
    @Log("修改")
    @PutMapping
    @RequiresPermissions("ybDrgConfireData:update")
    public void updateYbDrgConfireData(@Valid YbDrgConfireData ybDrgConfireData) throws FebsException {
        try {
//            User currentUser = FebsUtil.getCurrentUser();
//            ybDrgConfireData.setModifyUserId(currentUser.getUserId());
            this.iYbDrgConfireDataService.updateYbDrgConfireData(ybDrgConfireData);
        } catch (Exception e) {
            message = "修改失败";
            log.error(message, e);
            throw new FebsException(message);
        }
    }


    @Log("删除")
    @DeleteMapping("/{ids}")
    @RequiresPermissions("ybDrgConfireData:delete")
    public void deleteYbDrgConfireDatas(@NotBlank(message = "{required}") @PathVariable String ids) throws FebsException {
        try {
            String[] arr_ids = ids.split(StringPool.COMMA);
            this.iYbDrgConfireDataService.deleteYbDrgConfireDatas(arr_ids);
        } catch (Exception e) {
            message = "删除失败";
            log.error(message, e);
            throw new FebsException(message);
        }
    }

    @PostMapping("excel")
    @RequiresPermissions("ybDrgConfireData:export")
    public void export(QueryRequest request, YbDrgConfireData ybDrgConfireData, HttpServletResponse response) throws FebsException {
        try {
            List<YbDrgConfireData> ybDrgConfireDatas = this.iYbDrgConfireDataService.findYbDrgConfireDatas(request, ybDrgConfireData).getRecords();
            ExcelKit.$Export(YbDrgConfireData.class, response).downXlsx(ybDrgConfireDatas, false);
        } catch (Exception e) {
            message = "导出Excel失败";
            log.error(message, e);
            throw new FebsException(message);
        }
    }

    @GetMapping("/{id}")
    public YbDrgConfireData detail(@NotBlank(message = "{required}") @PathVariable String id) {
        YbDrgConfireData ybDrgConfireData = this.iYbDrgConfireDataService.getById(id);
        return ybDrgConfireData;
    }

    @GetMapping("findDrgConfireDataList")
    public FebsResponse findDrgConfireDataLists(Integer areaType) {
        List<YbDrgConfireData> list = new ArrayList<>();
        try {
            User currentUser = FebsUtil.getCurrentUser();
            list = this.iYbDrgConfireDataService.findDrgConfireDataByInDoctorCodeList(currentUser.getUsername(),areaType);

        } catch (Exception e) {
            log.error("获取医生失败", e);
        }

        return new FebsResponse().data(list);
    }
}