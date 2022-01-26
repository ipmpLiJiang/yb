package cc.mrbird.febs.yb.controller;

import cc.mrbird.febs.com.controller.DataTypeHelpers;
import cc.mrbird.febs.com.entity.ComFile;
import cc.mrbird.febs.com.service.IComFileService;
import cc.mrbird.febs.common.annotation.Log;
import cc.mrbird.febs.common.controller.BaseController;
import cc.mrbird.febs.common.domain.FebsResponse;
import cc.mrbird.febs.common.domain.router.VueRouter;
import cc.mrbird.febs.common.exception.FebsException;
import cc.mrbird.febs.common.domain.QueryRequest;

import cc.mrbird.febs.common.properties.FebsProperties;
import cc.mrbird.febs.yb.domain.ResponseResultData;
import cc.mrbird.febs.yb.service.IYbAppealResultService;
import cc.mrbird.febs.yb.entity.YbAppealResult;

import cc.mrbird.febs.common.utils.FebsUtil;
import cc.mrbird.febs.system.domain.User;
import com.baomidou.mybatisplus.core.toolkit.StringPool;
import com.google.common.io.Files;
import com.wuwenze.poi.ExcelKit;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import java.io.File;
import java.util.*;

/**
 * @author viki
 * @since 2020-07-30
 */
@Slf4j
@Validated
@RestController
@RequestMapping("ybAppealResult")

public class YbAppealResultController extends BaseController {

    private String message;
    @Autowired
    public IYbAppealResultService iYbAppealResultService;

    @Autowired
    IComFileService iComFileService;

    @Autowired
    FebsProperties febsProperties;


    /**
     * 分页查询数据
     *
     * @param request        分页信息
     * @param ybAppealResult 查询条件
     * @return
     */
    @GetMapping
    @RequiresPermissions("ybAppealResult:view")
    public Map<String, Object> List(QueryRequest request, YbAppealResult ybAppealResult) {
        return getDataTable(this.iYbAppealResultService.findYbAppealResults(request, ybAppealResult));
    }

    /**
     * 添加
     *
     * @param ybAppealResult
     * @return
     */
    @Log("新增/按钮")
    @PostMapping
    @RequiresPermissions("ybAppealResult:add")
    public void addYbAppealResult(@Valid YbAppealResult ybAppealResult) throws FebsException {
        try {
            User currentUser = FebsUtil.getCurrentUser();
//            ybAppealResult.setCreateUserId(currentUser.getUserId());
            this.iYbAppealResultService.createYbAppealResult(ybAppealResult);
        } catch (Exception e) {
            message = "新增/按钮失败";
            log.error(message, e);
            throw new FebsException(message);
        }
    }

    /**
     * 修改
     *
     * @param ybAppealResult
     * @return
     */
    @Log("修改")
    @PutMapping
    @RequiresPermissions("ybAppealResult:update")
    public void updateYbAppealResult(@Valid YbAppealResult ybAppealResult) throws FebsException {
        try {
            User currentUser = FebsUtil.getCurrentUser();
//            ybAppealResult.setModifyUserId(currentUser.getUserId());
            this.iYbAppealResultService.updateYbAppealResult(ybAppealResult);
        } catch (Exception e) {
            message = "修改失败";
            log.error(message, e);
            throw new FebsException(message);
        }
    }


    @Log("删除")
    @DeleteMapping("/{ids}")
    @RequiresPermissions("ybAppealResult:delete")
    public void deleteYbAppealResults(@NotBlank(message = "{required}") @PathVariable String ids) throws FebsException {
        try {
            String[] arr_ids = ids.split(StringPool.COMMA);
            this.iYbAppealResultService.deleteYbAppealResults(arr_ids);
        } catch (Exception e) {
            message = "删除失败";
            log.error(message, e);
            throw new FebsException(message);
        }
    }

    @PostMapping("excel")
    @RequiresPermissions("ybAppealResult:export")
    public void export(QueryRequest request, YbAppealResult ybAppealResult, HttpServletResponse response) throws FebsException {
        try {
            List<YbAppealResult> ybAppealResults = this.iYbAppealResultService.findYbAppealResults(request, ybAppealResult).getRecords();
            ExcelKit.$Export(YbAppealResult.class, response).downXlsx(ybAppealResults, false);
        } catch (Exception e) {
            message = "导出Excel失败";
            log.error(message, e);
            throw new FebsException(message);
        }
    }

    @GetMapping("/{id}")
    public YbAppealResult detail(@NotBlank(message = "{required}") @PathVariable String id) {
        YbAppealResult ybAppealResult = this.iYbAppealResultService.getById(id);
        return ybAppealResult;
    }

    @Log("查询创建")
    @PostMapping("findCreateAppealResult")
    @RequiresPermissions("ybAppealResult:findCreate")
    public FebsResponse findCreateAppealResults(YbAppealResult ybAppealResult) {
        int success = 1;
        try {
            User currentUser = FebsUtil.getCurrentUser();
            Long uid = currentUser.getUserId();
            String uname = currentUser.getUsername();
            ybAppealResult = this.iYbAppealResultService.findCreateAppealResult(ybAppealResult, uid, uname);
        } catch (Exception e) {
            message = "创建失败";
            success = 0;
            log.error(message, e);
        }
        Map<String, Object> result = new HashMap<>();
        result.put("success", success);
        result.put("message", message);
        result.put("data", ybAppealResult);
        return new FebsResponse().put("data", result);
    }

    @Log("查询创建")
    @PostMapping("findLoadLastAppealResul")
    @RequiresPermissions("ybAppealResult:findCreate")
    public FebsResponse findLoadLastAppealResuls(YbAppealResult ybAppealResult) {
        int success = 0;
        try {

            String loadId = ybAppealResult.getId();//获得传过来的 sourceType==1 的 manageId
            //获取当前是否上传过附件 sourceType==1
            List<ComFile> comFileList = this.iComFileService.findListComFile(loadId,null);
            Date thisDate = new Date();
            if (comFileList.size() == 0) {
                String applyDateStr = ybAppealResult.getApplyDateStr(); //获得传过来的 applyDateStr
                //获得之前申诉记录
                ybAppealResult = this.iYbAppealResultService.findLoadLastAppealResulData(ybAppealResult);
                if (ybAppealResult != null) {
                    User currentUser = FebsUtil.getCurrentUser();
                    comFileList = this.iComFileService.findListComFile(ybAppealResult.getId(),null);
                    if (comFileList.size() > 0) {
                        List<ComFile> loadLastList = new ArrayList<ComFile>();

                        String filePath = febsProperties.getUploadPath(); // 上传后的路径
                        String oldDept = filePath + applyDateStr + "/" + currentUser.getUsername() + "/In";
                        String newDept = filePath + applyDateStr + "/" + currentUser.getUsername() + "/Out";
                        if(ybAppealResult.getAreaType() != 0){
                            oldDept += ybAppealResult.getAreaType();
                            newDept += ybAppealResult.getAreaType();
                        }

                        File f = new File(newDept);
                        if (!f.exists()) {
                            f.mkdirs(); //创建目录
                        }
                        for (ComFile comfile : comFileList) {
                            ComFile file = new ComFile();
                            file.setId(UUID.randomUUID().toString());
                            file.setRefTabId(loadId);
                            file.setServerName(comfile.getServerName());
                            file.setClientName(comfile.getClientName());
                            file.setRefTabTable(comfile.getRefTabTable());
                            file.setIsDeletemark(1);
                            file.setCreateTime(thisDate);

                            thisDate = this.addSecond(thisDate,1);
                            String oldFileUrl = oldDept + "/" + comfile.getServerName();
                            String newFileUrl = newDept + "/" + comfile.getServerName();
                            File oldFile = new File(oldFileUrl);
                            if (oldFile.exists()) {
                                File newFile = new File(newFileUrl);
                                Files.copy(oldFile, newFile);
                                loadLastList.add(file);
                            }
                        }

                        if (loadLastList.size() > 0) {
                            boolean bl = this.iComFileService.loadLastComFiles(loadLastList);
                            if (bl) {
                                success = 1;
                                message = "获取数据成功.";
                            }
                        }
                    }else{
                        message = "上次未上传复议图片.";
                    }
                }else{
                    message = "未找到上次复议图片和申诉理由.";
                }
            }else{
                message = "当前已上传图片，请先删除后再重新获取上次复议图片和申诉理由.";
            }
        } catch (Exception e) {
            message = "获取数据失败.";
            success = 0;
            log.error(message, e);
        }
        ResponseResultData rrd = new ResponseResultData();
        rrd.setMessage(message);
        rrd.setSuccess(success);
        if(ybAppealResult!=null && ybAppealResult.getOperateReason()!=null) {
            rrd.setData(ybAppealResult.getOperateReason());
        }else{
            rrd.setData("");
        }
        return new FebsResponse().data(rrd);
    }

    private Date addSecond(Date date,int t){
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        cal.add(Calendar.SECOND, t);// 24小时制
        return cal.getTime();
    }

}