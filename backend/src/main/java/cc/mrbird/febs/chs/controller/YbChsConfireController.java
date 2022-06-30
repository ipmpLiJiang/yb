package cc.mrbird.febs.chs.controller;

import cc.mrbird.febs.chs.entity.YbChsConfireData;
import cc.mrbird.febs.chs.entity.YbChsConfireDataJson;
import cc.mrbird.febs.chs.entity.YbChsConfireJson;
import cc.mrbird.febs.chs.service.IYbChsConfireDataService;
import cc.mrbird.febs.com.controller.DataTypeHelpers;
import cc.mrbird.febs.com.service.IComConfiguremanageService;
import cc.mrbird.febs.common.annotation.Log;
import cc.mrbird.febs.common.controller.BaseController;
import cc.mrbird.febs.common.domain.FebsResponse;
import cc.mrbird.febs.common.domain.router.VueRouter;
import cc.mrbird.febs.common.exception.FebsException;
import cc.mrbird.febs.common.domain.QueryRequest;

import cc.mrbird.febs.chs.service.IYbChsConfireService;
import cc.mrbird.febs.chs.entity.YbChsConfire;

import cc.mrbird.febs.common.utils.FebsUtil;
import cc.mrbird.febs.system.domain.User;
import com.alibaba.fastjson.TypeReference;
import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.toolkit.StringPool;
import com.wuwenze.poi.ExcelKit;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.ModelMap;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import java.util.*;

/**
 * @author viki
 * @since 2022-06-30
 */
@Slf4j
@Validated
@RestController
@RequestMapping("ybChsConfire")

public class YbChsConfireController extends BaseController {

    private String message;
    @Autowired
    public IYbChsConfireService iYbChsConfireService;

    @Autowired
    IComConfiguremanageService iComConfiguremanageService;

    @Autowired
    IYbChsConfireDataService iYbChsConfireDataService;

    @GetMapping
    @RequiresPermissions("ybChsConfire:view")
    public Map<String, Object> List(QueryRequest request, String doctorContent, Integer adminType, Integer areaType, String deptContent, String operatorName) {
        return getDataTable(this.iYbChsConfireService.findChsConfireView(request, doctorContent, adminType, areaType, deptContent, operatorName, null));
    }

    @GetMapping("findChsConfireView")
    @RequiresPermissions("ybChsConfire:userView")
    public Map<String, Object> findUserList(QueryRequest request, String doctorContent, Integer adminType, Integer areaType, String deptContent, String operatorName) {
        User currentUser = FebsUtil.getCurrentUser();
        return getDataTable(this.iYbChsConfireService.findChsConfireUserView(request, doctorContent, adminType, areaType, deptContent, currentUser, operatorName, null));
    }

    @Log("新增/按钮")
    @PostMapping("addChsConfire")
    @RequiresPermissions("ybChsConfire:add")
    public FebsResponse addChsConfire(String dataJson) {
        ModelMap map = new ModelMap();
        int success = 0;
        String id = "";
        try {
            User currentUser = FebsUtil.getCurrentUser();
            YbChsConfireJson chsConfireJson = JSON.parseObject(dataJson, new TypeReference<YbChsConfireJson>() {
            });
            YbChsConfire chsConfire = new YbChsConfire();
            chsConfire.setDoctorCode(chsConfireJson.getDoctorCode());
            chsConfire.setAreaType(chsConfireJson.getAreaType());
            chsConfire = this.iYbChsConfireService.findChsConfire(chsConfire);
            if (chsConfire == null) {
                id = UUID.randomUUID().toString();
                List<YbChsConfireData> createDataList = new ArrayList<>();
                YbChsConfire create = new YbChsConfire();
                create.setId(id);
                create.setDoctorCode(chsConfireJson.getDoctorCode());
                String strDoctorName = DataTypeHelpers.stringReplaceSetString(chsConfireJson.getDoctorName(), chsConfireJson.getDoctorCode() + "-");
                create.setDoctorName(strDoctorName);
                create.setAdminType(chsConfireJson.getAdminType());
                create.setAreaType(chsConfireJson.getAreaType());
                create.setIsDeletemark(1);
                create.setCreateTime(new Date());
                create.setCreateUserId(currentUser.getUserId());
                create.setOperatorId(currentUser.getUserId());
                create.setOperatorName(currentUser.getUsername() + "-" + currentUser.getXmname());
                for (YbChsConfireDataJson item : chsConfireJson.getChild()) {
                    YbChsConfireData createData = new YbChsConfireData();
                    createData.setId(UUID.randomUUID().toString());
                    createData.setPid(id);
                    createData.setDksId(item.getDksId());
                    String strDksName = DataTypeHelpers.stringReplaceSetString(item.getDksName(), item.getDksId() + "-");
                    createData.setDksName(strDksName);
                    createDataList.add(createData);
                }
                this.iYbChsConfireService.createChsConfire(create, createDataList);
                success = 1;
            } else {
                message = iComConfiguremanageService.getConfigAreaName(chsConfireJson.getAreaType());
                message = message + " 当前职工 " + chsConfireJson.getDoctorName() + " 已经维护过数据了";
            }
            //dataJson = JSONUtil.toJsonStr(chsConfireJson);
        } catch (Exception e) {
            message = "新增/按钮失败";
            log.error(message, e);
        }

        map.put("message",message);
        map.put("success",success);
        map.put("data",id);
        return  new FebsResponse().data(map);
    }

    @Log("修改")
    @PutMapping("updateChsConfire")
    @RequiresPermissions("ybChsConfire:update")
    public FebsResponse updateChsConfire(String dataJson) {
        ModelMap map = new ModelMap();
        int success = 0;
        try {
            User currentUser = FebsUtil.getCurrentUser();
            YbChsConfireJson chsConfireJson = JSON.parseObject(dataJson, new TypeReference<YbChsConfireJson>() {
            });
            YbChsConfire chsConfire = new YbChsConfire();
            chsConfire.setDoctorCode(chsConfireJson.getDoctorCode());
            chsConfire.setAreaType(chsConfireJson.getAreaType());
            chsConfire = this.iYbChsConfireService.findChsConfire(chsConfire);

            if (chsConfire == null || (chsConfireJson.getId().equals(chsConfire.getId()) && chsConfireJson.getDoctorCode().equals(chsConfire.getDoctorCode()))) {
                List<YbChsConfireData> updateDataList = new ArrayList<>();
                List<YbChsConfireData> createDataList = new ArrayList<>();
                YbChsConfire update = new YbChsConfire();
                update.setId(chsConfireJson.getId());
                update.setDoctorCode(chsConfire.getDoctorCode());
                update.setAdminType(chsConfireJson.getAdminType());
                update.setAreaType(chsConfire.getAreaType());
                update.setModifyTime(new Date());
                update.setModifyUserId(currentUser.getUserId());
                for (YbChsConfireDataJson item : chsConfireJson.getChild()) {
                    YbChsConfireData updateData = new YbChsConfireData();
                    updateData.setId(item.getId());
                    updateData.setPid(chsConfireJson.getId());
                    updateData.setDksId(item.getDksId());
                    String strDksName = DataTypeHelpers.stringReplaceSetString(item.getDksName(), item.getDksId() + "-");
                    updateData.setDksName(strDksName);
//                    String strDksName = item.getDksName();
//                    updateData.setDksName(strDksName);
                    if (updateData.getId() == null) {
                        updateData.setId(UUID.randomUUID().toString());
                        createDataList.add(updateData);
                    } else {
                        updateDataList.add(updateData);
                    }
                }
                if (chsConfireJson.getChild().size() == 0) {
                    this.iYbChsConfireService.updateChsConfire(update, createDataList, updateDataList);
                    success = 1;
                } else {
                    YbChsConfireData quertAcd = new YbChsConfireData();
                    quertAcd.setPid(chsConfireJson.getId());
                    quertAcd.setDksId(createDataList.get(0).getDksId());
//                    quertAcd.setDksName(createDataList.get(0).getDksName());
                    List<YbChsConfireData> queryAcdList = iYbChsConfireDataService.findChsConfireDataList(quertAcd);
                    if (queryAcdList.size() == 0) {
                        this.iYbChsConfireService.updateChsConfire(update, createDataList, updateDataList);
                        success = 1;
                    } else {
                        message = iComConfiguremanageService.getConfigAreaName(chsConfireJson.getAreaType());
                        message = message + " " + createDataList.get(0).getDksId() + "-" + createDataList.get(0).getDksName() + " 科室已存在!";
//                        message = message + " " + createDataList.get(0).getDksName() + " 已存在!";
                    }
                }
            } else {
                message = iComConfiguremanageService.getConfigAreaName(chsConfireJson.getAreaType());
                message = message + " 当前职工 " + chsConfireJson.getDoctorName() + " 已经维护过数据了";
            }
        } catch (Exception e) {
            message = "修改失败";
            log.error(message, e);
        }

        map.put("message",message);
        map.put("success",success);
        map.put("data","");
        return  new FebsResponse().data(map);
    }


    /**
     * 添加
     *
     * @param ybChsConfire
     * @return
     */
    @Log("新增/按钮")
    @PostMapping
    @RequiresPermissions("ybChsConfire:add")
    public void addYbChsConfire(@Valid YbChsConfire ybChsConfire) throws FebsException {
        try {
            User currentUser = FebsUtil.getCurrentUser();
            ybChsConfire.setCreateUserId(currentUser.getUserId());
            this.iYbChsConfireService.createYbChsConfire(ybChsConfire);
        } catch (Exception e) {
            message = "新增/按钮失败";
            log.error(message, e);
            throw new FebsException(message);
        }
    }

    /**
     * 修改
     *
     * @param ybChsConfire
     * @return
     */
    @Log("修改")
    @PutMapping
    @RequiresPermissions("ybChsConfire:update")
    public void updateYbChsConfire(@Valid YbChsConfire ybChsConfire) throws FebsException {
        try {
            User currentUser = FebsUtil.getCurrentUser();
            ybChsConfire.setModifyUserId(currentUser.getUserId());
            this.iYbChsConfireService.updateYbChsConfire(ybChsConfire);
        } catch (Exception e) {
            message = "修改失败";
            log.error(message, e);
            throw new FebsException(message);
        }
    }


    @Log("删除")
    @DeleteMapping("/{ids}")
    @RequiresPermissions("ybChsConfire:delete")
    public void deleteYbChsConfires(@NotBlank(message = "{required}") @PathVariable String ids) throws FebsException {
        try {
            String[] arr_ids = ids.split(StringPool.COMMA);
            this.iYbChsConfireService.deleteYbChsConfires(arr_ids);
        } catch (Exception e) {
            message = "删除失败";
            log.error(message, e);
            throw new FebsException(message);
        }
    }

    @PostMapping("excel")
    @RequiresPermissions("ybChsConfire:export")
    public void export(QueryRequest request, YbChsConfire ybChsConfire, HttpServletResponse response) throws FebsException {
        try {
            List<YbChsConfire> ybChsConfires = this.iYbChsConfireService.findYbChsConfires(request, ybChsConfire).getRecords();
            ExcelKit.$Export(YbChsConfire.class, response).downXlsx(ybChsConfires, false);
        } catch (Exception e) {
            message = "导出Excel失败";
            log.error(message, e);
            throw new FebsException(message);
        }
    }

    @GetMapping("/{id}")
    public YbChsConfire detail(@NotBlank(message = "{required}") @PathVariable String id) {
        YbChsConfire ybChsConfire = this.iYbChsConfireService.getById(id);
        return ybChsConfire;
    }
}