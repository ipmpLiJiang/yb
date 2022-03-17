package cc.mrbird.febs.yb.controller;

import cc.mrbird.febs.common.annotation.Log;
import cc.mrbird.febs.common.controller.BaseController;
import cc.mrbird.febs.common.domain.router.VueRouter;
import cc.mrbird.febs.common.exception.FebsException;
import cc.mrbird.febs.common.domain.QueryRequest;

import cc.mrbird.febs.yb.service.IYbReconsiderPriorityLevelService;
import cc.mrbird.febs.yb.entity.YbReconsiderPriorityLevel;

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
 * @since 2020-10-13
 */
@Slf4j
@Validated
@RestController
@RequestMapping("ybReconsiderPriorityLevel")

public class YbReconsiderPriorityLevelController extends BaseController {

    private String message;
    @Autowired
    public IYbReconsiderPriorityLevelService iYbReconsiderPriorityLevelService;


    /**
     * 分页查询数据
     *
     * @param request                   分页信息
     * @param ybReconsiderPriorityLevel 查询条件
     * @return
     */
    @GetMapping
    @RequiresPermissions("ybReconsiderPriorityLevel:view")
    public Map<String, Object> List(QueryRequest request, YbReconsiderPriorityLevel ybReconsiderPriorityLevel) {
        return getDataTable(this.iYbReconsiderPriorityLevelService.findYbReconsiderPriorityLevels(request, ybReconsiderPriorityLevel));
    }

    /**
     * 添加
     *
     * @param ybReconsiderPriorityLevel
     * @return
     */
    @Log("新增/按钮")
    @PostMapping
    @RequiresPermissions("ybReconsiderPriorityLevel:add")
    public void addYbReconsiderPriorityLevel(@Valid YbReconsiderPriorityLevel ybReconsiderPriorityLevel) throws FebsException {
        try {
            User currentUser = FebsUtil.getCurrentUser();
            ybReconsiderPriorityLevel.setCreateUserId(currentUser.getUserId());
            ybReconsiderPriorityLevel.setOperatorId(currentUser.getUserId());
            ybReconsiderPriorityLevel.setOperatorName(currentUser.getUsername() + "-" + currentUser.getXmname());
            if (ybReconsiderPriorityLevel.getState() == YbReconsiderPriorityLevel.PL_STATE_1 ||
                    ybReconsiderPriorityLevel.getState() == YbReconsiderPriorityLevel.PL_STATE_2) {
                if(ybReconsiderPriorityLevel.getPersonType() != YbReconsiderPriorityLevel.PERSON_TYPE_4){
                    ybReconsiderPriorityLevel.setDoctorCode("");
                    ybReconsiderPriorityLevel.setDoctorName("");
                }
                if(ybReconsiderPriorityLevel.getDeptType() != YbReconsiderPriorityLevel.DEPT_TYPE_4){
                    ybReconsiderPriorityLevel.setDeptCode("");
                    ybReconsiderPriorityLevel.setDeptName("");
                }
            }
            if (ybReconsiderPriorityLevel.getState() == YbReconsiderPriorityLevel.PL_STATE_3) {
                if(ybReconsiderPriorityLevel.getPersonType() != YbReconsiderPriorityLevel.PERSON_TYPE_4){
                    ybReconsiderPriorityLevel.setDoctorCode("");
                    ybReconsiderPriorityLevel.setDoctorName("");
                }
            }
            if (ybReconsiderPriorityLevel.getState() == YbReconsiderPriorityLevel.PL_STATE_4) {
                if(ybReconsiderPriorityLevel.getPersonType() != YbReconsiderPriorityLevel.PERSON_TYPE_2){
                    ybReconsiderPriorityLevel.setDksId("");
                    ybReconsiderPriorityLevel.setDksName("");
                }
            }
            this.iYbReconsiderPriorityLevelService.createYbReconsiderPriorityLevel(ybReconsiderPriorityLevel);
        } catch (Exception e) {
            message = "新增/按钮失败";
            log.error(message, e);
            throw new FebsException(message);
        }
    }

    /**
     * 修改
     *
     * @param ybReconsiderPriorityLevel
     * @return
     */
    @Log("修改")
    @PutMapping
    @RequiresPermissions("ybReconsiderPriorityLevel:update")
    public void updateYbReconsiderPriorityLevel(@Valid YbReconsiderPriorityLevel ybReconsiderPriorityLevel) throws FebsException {
        try {
            User currentUser = FebsUtil.getCurrentUser();
            ybReconsiderPriorityLevel.setModifyUserId(currentUser.getUserId());
            if (ybReconsiderPriorityLevel.getState() == YbReconsiderPriorityLevel.PL_STATE_1 ||
                    ybReconsiderPriorityLevel.getState() == YbReconsiderPriorityLevel.PL_STATE_2) {
                if(ybReconsiderPriorityLevel.getPersonType() != YbReconsiderPriorityLevel.PERSON_TYPE_4){
                    ybReconsiderPriorityLevel.setDoctorCode("");
                    ybReconsiderPriorityLevel.setDoctorName("");
                }
                if(ybReconsiderPriorityLevel.getDeptType() != YbReconsiderPriorityLevel.DEPT_TYPE_4){
                    ybReconsiderPriorityLevel.setDeptCode("");
                    ybReconsiderPriorityLevel.setDeptName("");
                }
            }
            if (ybReconsiderPriorityLevel.getState() == YbReconsiderPriorityLevel.PL_STATE_3) {
                if(ybReconsiderPriorityLevel.getPersonType() != YbReconsiderPriorityLevel.PERSON_TYPE_4){
                    ybReconsiderPriorityLevel.setDoctorCode("");
                    ybReconsiderPriorityLevel.setDoctorName("");
                }
            }
            if (ybReconsiderPriorityLevel.getState() == YbReconsiderPriorityLevel.PL_STATE_4) {
                if(ybReconsiderPriorityLevel.getPersonType() != YbReconsiderPriorityLevel.PERSON_TYPE_2){
                    ybReconsiderPriorityLevel.setDksId("");
                    ybReconsiderPriorityLevel.setDksName("");
                }
            }
            this.iYbReconsiderPriorityLevelService.updateYbReconsiderPriorityLevel(ybReconsiderPriorityLevel);
        } catch (Exception e) {
            message = "修改失败";
            log.error(message, e);
            throw new FebsException(message);
        }
    }


    @Log("删除")
    @DeleteMapping("/{ids}")
    @RequiresPermissions("ybReconsiderPriorityLevel:delete")
    public void deleteYbReconsiderPriorityLevels(@NotBlank(message = "{required}") @PathVariable String ids) throws FebsException {
        try {
            String[] arr_ids = ids.split(StringPool.COMMA);
            this.iYbReconsiderPriorityLevelService.deleteYbReconsiderPriorityLevels(arr_ids);
        } catch (Exception e) {
            message = "删除失败";
            log.error(message, e);
            throw new FebsException(message);
        }
    }

    @PostMapping("excel")
    @RequiresPermissions("ybReconsiderPriorityLevel:export")
    public void export(QueryRequest request, YbReconsiderPriorityLevel ybReconsiderPriorityLevel, HttpServletResponse response) throws FebsException {
        try {
            List<YbReconsiderPriorityLevel> ybReconsiderPriorityLevels = this.iYbReconsiderPriorityLevelService.findYbReconsiderPriorityLevels(request, ybReconsiderPriorityLevel).getRecords();
            ExcelKit.$Export(YbReconsiderPriorityLevel.class, response).downXlsx(ybReconsiderPriorityLevels, false);
        } catch (Exception e) {
            message = "导出Excel失败";
            log.error(message, e);
            throw new FebsException(message);
        }
    }

    @GetMapping("/{id}")
    public YbReconsiderPriorityLevel detail(@NotBlank(message = "{required}") @PathVariable String id) {
        YbReconsiderPriorityLevel ybReconsiderPriorityLevel = this.iYbReconsiderPriorityLevelService.getById(id);
        return ybReconsiderPriorityLevel;
    }
}