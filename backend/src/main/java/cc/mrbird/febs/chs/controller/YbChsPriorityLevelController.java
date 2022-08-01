package cc.mrbird.febs.chs.controller;

import cc.mrbird.febs.com.controller.DataTypeHelpers;
import cc.mrbird.febs.common.annotation.Log;
import cc.mrbird.febs.common.controller.BaseController;
import cc.mrbird.febs.common.domain.router.VueRouter;
import cc.mrbird.febs.common.exception.FebsException;
import cc.mrbird.febs.common.domain.QueryRequest;

import cc.mrbird.febs.chs.service.IYbChsPriorityLevelService;
import cc.mrbird.febs.chs.entity.YbChsPriorityLevel;

import cc.mrbird.febs.common.utils.FebsUtil;
import cc.mrbird.febs.system.domain.User;
import com.baomidou.mybatisplus.core.toolkit.StringPool;
import com.wuwenze.poi.ExcelKit;
import freemarker.template.utility.StringUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
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
 * @since 2022-07-13
 */
@Slf4j
@Validated
@RestController
@RequestMapping("ybChsPriorityLevel")

public class YbChsPriorityLevelController extends BaseController {

    private String message;
    @Autowired
    public IYbChsPriorityLevelService iYbChsPriorityLevelService;


    /**
     * 分页查询数据
     *
     * @param request            分页信息
     * @param ybChsPriorityLevel 查询条件
     * @return
     */
    @GetMapping
    @RequiresPermissions("ybChsPriorityLevel:view")
    public Map<String, Object> List(QueryRequest request, YbChsPriorityLevel ybChsPriorityLevel) {
        return getDataTable(this.iYbChsPriorityLevelService.findYbChsPriorityLevels(request, ybChsPriorityLevel));
    }

    /**
     * 添加
     *
     * @param ybChsPriorityLevel
     * @return
     */
    @Log("新增/按钮")
    @PostMapping
    @RequiresPermissions("ybChsPriorityLevel:add")
    public void addYbChsPriorityLevel(@Valid YbChsPriorityLevel ybChsPriorityLevel) throws FebsException {
        try {
            User currentUser = FebsUtil.getCurrentUser();
            ybChsPriorityLevel.setCreateUserId(currentUser.getUserId());
            ybChsPriorityLevel.setCreateUserId(currentUser.getUserId());
            ybChsPriorityLevel.setOperatorId(currentUser.getUserId());
            ybChsPriorityLevel.setOperatorName(currentUser.getUsername() + "-" + currentUser.getXmname());
            if(ybChsPriorityLevel.getPersonType() != YbChsPriorityLevel.PERSON_TYPE_4){
                ybChsPriorityLevel.setDoctorCodeTo("");
                ybChsPriorityLevel.setDoctorNameTo("");
            }
            if(ybChsPriorityLevel.getDeptType() != YbChsPriorityLevel.DEPT_TYPE_4){
                ybChsPriorityLevel.setDksIdTo("");
                ybChsPriorityLevel.setDksNameTo("");
            }
            if(StringUtils.isNotBlank(ybChsPriorityLevel.getDoctorNameTo())) {
//                String strDoctorNameTo = DataTypeHelpers.stringReplaceSetString(ybChsPriorityLevel.getDoctorNameTo(), ybChsPriorityLevel.getDoctorCodeTo() + "-");
                ybChsPriorityLevel.setDoctorNameTo(ybChsPriorityLevel.getDoctorNameTo());
            }

            if(StringUtils.isNotBlank(ybChsPriorityLevel.getDksNameTo())) {
//                String strDksNameTo = DataTypeHelpers.stringReplaceSetString(ybChsPriorityLevel.getDksNameTo(), ybChsPriorityLevel.getDksIdTo() + "-");
                ybChsPriorityLevel.setDksNameTo(ybChsPriorityLevel.getDksNameTo());
            }
            if(StringUtils.isNotBlank(ybChsPriorityLevel.getDksName())) {
//                String strDksName = DataTypeHelpers.stringReplaceSetString(ybChsPriorityLevel.getDksName(), ybChsPriorityLevel.getDksId() + "-");
                ybChsPriorityLevel.setDksName(ybChsPriorityLevel.getDksName());
            }
            this.iYbChsPriorityLevelService.createYbChsPriorityLevel(ybChsPriorityLevel);
        } catch (Exception e) {
            message = "新增/按钮失败";
            log.error(message, e);
            throw new FebsException(message);
        }
    }

    /**
     * 修改
     *
     * @param ybChsPriorityLevel
     * @return
     */
    @Log("修改")
    @PutMapping
    @RequiresPermissions("ybChsPriorityLevel:update")
    public void updateYbChsPriorityLevel(@Valid YbChsPriorityLevel ybChsPriorityLevel) throws FebsException {
        try {
            User currentUser = FebsUtil.getCurrentUser();
            ybChsPriorityLevel.setModifyUserId(currentUser.getUserId());
            if(ybChsPriorityLevel.getIsRule() != 1){
                ybChsPriorityLevel.setRuleName("");
            }
            if(ybChsPriorityLevel.getIsProject() != 1){
                ybChsPriorityLevel.setProjectName("");
            }
            if(ybChsPriorityLevel.getPersonType() != YbChsPriorityLevel.PERSON_TYPE_4){
                ybChsPriorityLevel.setDoctorCodeTo("");
                ybChsPriorityLevel.setDoctorNameTo("");
            }
            if(ybChsPriorityLevel.getDeptType() != YbChsPriorityLevel.DEPT_TYPE_4){
                ybChsPriorityLevel.setDksIdTo("");
                ybChsPriorityLevel.setDksNameTo("");
            }
            if(StringUtils.isNotBlank(ybChsPriorityLevel.getDoctorNameTo())) {
//                String strDoctorNameTo = DataTypeHelpers.stringReplaceSetString(ybChsPriorityLevel.getDoctorNameTo(), ybChsPriorityLevel.getDoctorCodeTo() + "-");
                ybChsPriorityLevel.setDoctorNameTo(ybChsPriorityLevel.getDoctorNameTo());
            }

            if(StringUtils.isNotBlank(ybChsPriorityLevel.getDksNameTo())) {
//                String strDksNameTo = DataTypeHelpers.stringReplaceSetString(ybChsPriorityLevel.getDksNameTo(), ybChsPriorityLevel.getDksIdTo() + "-");
                ybChsPriorityLevel.setDksNameTo(ybChsPriorityLevel.getDksNameTo());
            }
            if(StringUtils.isNotBlank(ybChsPriorityLevel.getDksName())) {
//                String strDksName = DataTypeHelpers.stringReplaceSetString(ybChsPriorityLevel.getDksName(), ybChsPriorityLevel.getDksId() + "-");
                ybChsPriorityLevel.setDksName(ybChsPriorityLevel.getDksName());
            }
            this.iYbChsPriorityLevelService.updateYbChsPriorityLevel(ybChsPriorityLevel);
        } catch (Exception e) {
            message = "修改失败";
            log.error(message, e);
            throw new FebsException(message);
        }
    }


    @Log("删除")
    @DeleteMapping("/{ids}")
    @RequiresPermissions("ybChsPriorityLevel:delete")
    public void deleteYbChsPriorityLevels(@NotBlank(message = "{required}") @PathVariable String ids) throws FebsException {
        try {
            String[] arr_ids = ids.split(StringPool.COMMA);
            this.iYbChsPriorityLevelService.deleteYbChsPriorityLevels(arr_ids);
        } catch (Exception e) {
            message = "删除失败";
            log.error(message, e);
            throw new FebsException(message);
        }
    }

    @PostMapping("excel")
    @RequiresPermissions("ybChsPriorityLevel:export")
    public void export(QueryRequest request, YbChsPriorityLevel ybChsPriorityLevel, HttpServletResponse response) throws FebsException {
        try {
            List<YbChsPriorityLevel> ybChsPriorityLevels = this.iYbChsPriorityLevelService.findYbChsPriorityLevels(request, ybChsPriorityLevel).getRecords();
            ExcelKit.$Export(YbChsPriorityLevel.class, response).downXlsx(ybChsPriorityLevels, false);
        } catch (Exception e) {
            message = "导出Excel失败";
            log.error(message, e);
            throw new FebsException(message);
        }
    }

    @GetMapping("/{id}")
    public YbChsPriorityLevel detail(@NotBlank(message = "{required}") @PathVariable String id) {
        YbChsPriorityLevel ybChsPriorityLevel = this.iYbChsPriorityLevelService.getById(id);
        return ybChsPriorityLevel;
    }
}