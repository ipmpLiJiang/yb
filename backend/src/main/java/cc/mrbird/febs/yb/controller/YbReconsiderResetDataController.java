package cc.mrbird.febs.yb.controller;

import cc.mrbird.febs.common.annotation.Log;
import cc.mrbird.febs.common.controller.BaseController;
import cc.mrbird.febs.common.domain.FebsResponse;
import cc.mrbird.febs.common.domain.router.VueRouter;
import cc.mrbird.febs.common.exception.FebsException;
import cc.mrbird.febs.common.domain.QueryRequest;

import cc.mrbird.febs.common.properties.FebsProperties;
import cc.mrbird.febs.export.excel.ExportExcelUtils;
import cc.mrbird.febs.yb.domain.ResponseResultData;
import cc.mrbird.febs.yb.entity.*;
import cc.mrbird.febs.yb.service.IYbReconsiderResetDataService;

import cc.mrbird.febs.common.utils.FebsUtil;
import cc.mrbird.febs.system.domain.User;
import cn.hutool.core.io.IoUtil;
import cn.hutool.poi.excel.ExcelUtil;
import cn.hutool.poi.excel.ExcelWriter;
import cn.hutool.poi.excel.StyleSet;
import com.baomidou.mybatisplus.core.toolkit.StringPool;
import com.wuwenze.poi.ExcelKit;
import com.wuwenze.poi.factory.ExcelMappingFactory;
import com.wuwenze.poi.pojo.ExcelMapping;
import com.wuwenze.poi.pojo.ExcelProperty;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import java.io.*;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @author viki
 * @since 2020-08-17
 */
@Slf4j
@Validated
@RestController
@RequestMapping("ybReconsiderResetData")

public class YbReconsiderResetDataController extends BaseController {

    private String message;
    @Autowired
    public IYbReconsiderResetDataService iYbReconsiderResetDataService;

    @Autowired
    private FebsProperties febsProperties;


    /**
     * 分页查询数据
     *
     * @param request               分页信息
     * @param ybReconsiderResetData 查询条件
     * @return
     */
    @GetMapping
    @RequiresPermissions("ybReconsiderResetData:view")
    public Map<String, Object> List(QueryRequest request, YbReconsiderResetData ybReconsiderResetData) {
        return getDataTable(this.iYbReconsiderResetDataService.findYbReconsiderResetDatas(request, ybReconsiderResetData));
    }

    /**
     * 添加
     *
     * @param ybReconsiderResetData
     * @return
     */
    @Log("新增/按钮")
    @PostMapping
    @RequiresPermissions("ybReconsiderResetData:add")
    public void addYbReconsiderResetData(@Valid YbReconsiderResetData ybReconsiderResetData) throws FebsException {
        try {
            User currentUser = FebsUtil.getCurrentUser();
            ybReconsiderResetData.setCreateUserId(currentUser.getUserId());
            this.iYbReconsiderResetDataService.createYbReconsiderResetData(ybReconsiderResetData);
        } catch (Exception e) {
            message = "新增/按钮失败";
            log.error(message, e);
            throw new FebsException(message);
        }
    }

    /**
     * 修改
     *
     * @param ybReconsiderResetData
     * @return
     */
    @Log("修改")
    @PutMapping
    @RequiresPermissions("ybReconsiderResetData:update")
    public void updateYbReconsiderResetData(@Valid YbReconsiderResetData ybReconsiderResetData) throws FebsException {
        try {
            User currentUser = FebsUtil.getCurrentUser();
            ybReconsiderResetData.setModifyUserId(currentUser.getUserId());
            this.iYbReconsiderResetDataService.updateYbReconsiderResetData(ybReconsiderResetData);
        } catch (Exception e) {
            message = "修改失败";
            log.error(message, e);
            throw new FebsException(message);
        }
    }


    @Log("删除")
    @DeleteMapping("/{ids}")
    @RequiresPermissions("ybReconsiderResetData:delete")
    public void deleteYbReconsiderResetDatas(@NotBlank(message = "{required}") @PathVariable String ids) throws FebsException {
        try {
            String[] arr_ids = ids.split(StringPool.COMMA);
            this.iYbReconsiderResetDataService.deleteYbReconsiderResetDatas(arr_ids);
        } catch (Exception e) {
            message = "删除失败";
            log.error(message, e);
            throw new FebsException(message);
        }
    }

    @PostMapping("excel")
    @RequiresPermissions("ybReconsiderResetData:export")
    public void export(QueryRequest request, YbReconsiderResetData ybReconsiderResetData, HttpServletResponse response) throws FebsException {
        try {
            List<YbReconsiderResetData> ybReconsiderResetDatas = this.iYbReconsiderResetDataService.findYbReconsiderResetDatas(request, ybReconsiderResetData).getRecords();
            ExcelKit.$Export(YbReconsiderResetData.class, response).downXlsx(ybReconsiderResetDatas, false);
        } catch (Exception e) {
            message = "导出Excel失败";
            log.error(message, e);
            throw new FebsException(message);
        }
    }

    @GetMapping("/{id}")
    public YbReconsiderResetData detail(@NotBlank(message = "{required}") @PathVariable String id) {
        YbReconsiderResetData ybReconsiderResetData = this.iYbReconsiderResetDataService.getById(id);
        return ybReconsiderResetData;
    }

    @Log("修改")
    @PutMapping("updateResetData")
    @RequiresPermissions("ybReconsiderResetData:updateResetData")
    public FebsResponse updateAppealResulResetData(String applyDateStr,Integer areaType, Integer dataType) {
        int success = 0;
        try {
            User currentUser = FebsUtil.getCurrentUser();
            Long uid = currentUser.getUserId();
            String uname = currentUser.getUsername();
            message = this.iYbReconsiderResetDataService.updateResetDatas(applyDateStr,areaType, uid, uname, dataType);
            if ("".equals(message)) {
                message = "剔除数据成功.";
                success = 1;
            }
            if (message.equals("update0")) {
                message = "未找到可更新的数据";
            }
            if (message.equals("result0")) {
                message = "未找到有效的申诉上传数据";
            }
        } catch (Exception e) {
            message = "剔除数据失败.";
            log.error(message, e);
        }

        ResponseResultData responseResultData = new ResponseResultData();
        responseResultData.setSuccess(success);
        responseResultData.setMessage(message);
        return new FebsResponse().data(responseResultData);
    }

    @Log("修改")
    @PutMapping("updateHandleResetData")
    @RequiresPermissions("ybReconsiderResetData:updateResetData")
    public FebsResponse updateHandResetData(String resultIds, String resetIds) {
        int success = 0;
        try {
            User currentUser = FebsUtil.getCurrentUser();
            Long uid = currentUser.getUserId();
            String uname = currentUser.getUsername();
            message = this.iYbReconsiderResetDataService.updateHandleResetDatas(resultIds, resetIds, uid, uname);
            if ("ok".equals(message)) {
                success = 1;
                message = "剔除数据成功.";
            }
        } catch (Exception e) {
            message = "剔除数据失败.";
            log.error(message, e);
        }

        ResponseResultData responseResultData = new ResponseResultData();
        responseResultData.setSuccess(success);
        responseResultData.setMessage(message);
        return new FebsResponse().data(responseResultData);
    }

    @Log("修改")
    @PutMapping("updateHandleResetCanceltData")
    @RequiresPermissions("ybReconsiderResetData:updateResetData")
    public FebsResponse updateHandResetCanceltData(String resetId,String applyDateStr) {
        int success = 0;
        try {
            message = this.iYbReconsiderResetDataService.updateHandleResetCancelData(resetId,applyDateStr);
            if ("ok".equals(message)) {
                success = 1;
                message = "取消剔除数据成功.";
            }
        } catch (Exception e) {
            message = "取消剔除数据失败.";
            log.error(message, e);
        }

        ResponseResultData responseResultData = new ResponseResultData();
        responseResultData.setSuccess(success);
        responseResultData.setMessage(message);
        return new FebsResponse().data(responseResultData);
    }

    @PostMapping("downFile")
    public void downFile(HttpServletResponse response) throws FebsException {
        try {
            String sheetName1 = "明细扣款";
            String sheetName2 = "主单扣款";
            ExportExcelUtils.exportTemplateFileT(response, YbReconsiderResetDataExport.class, sheetName1, YbReconsiderResetMainExport.class, sheetName2);
        } catch (Exception e) {
            message = "导出Excel模板失败";
            log.error(message, e);
            throw new FebsException(message);
        }
    }

//    @PostMapping("downFile")
//    public void downFile(HttpServletResponse response) {
//        try {
//            String path = febsProperties.getUploadPath();
//            String fileName = "剔除数据模板.xlsx";
//            String filePath = path + fileName;
//            File file = new File(filePath);
//            if (file.exists()) {
//                InputStream ins = new FileInputStream(filePath);
//                BufferedInputStream bins = new BufferedInputStream(ins);// 放到缓冲流里面
//                OutputStream outs = response.getOutputStream();// 获取文件输出IO流
//                BufferedOutputStream bouts = new BufferedOutputStream(outs);
//                response.setHeader("Content-Disposition",
//                        "attachment;filename=" + URLEncoder.encode(fileName, "utf-8"));
//
//                int bytesRead = 0;
//                byte[] buffer = new byte[512];
//                //开始向网络传输文件流
//                while ((bytesRead = bins.read(buffer, 0, 512)) != -1) {
//                    bouts.write(buffer, 0, bytesRead);
//                }
//                bouts.flush();// 这里一定要调用flush()方法
//                ins.close();
//                bins.close();
//                outs.close();
//                bouts.close();
//            } else {
////                response.sendRedirect("../error.jsp");
//            }
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//    }

}