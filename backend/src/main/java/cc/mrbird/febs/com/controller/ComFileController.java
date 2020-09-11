package cc.mrbird.febs.com.controller;

import cc.mrbird.febs.com.entity.ComFile;
import cc.mrbird.febs.com.entity.InComFile;
import cc.mrbird.febs.com.entity.InUploadFile;
import cc.mrbird.febs.com.entity.OutComFile;
import cc.mrbird.febs.com.service.IComFileService;
import cc.mrbird.febs.common.annotation.Log;
import cc.mrbird.febs.common.controller.BaseController;
import cc.mrbird.febs.common.domain.FebsResponse;
import cc.mrbird.febs.common.domain.router.VueRouter;
import cc.mrbird.febs.common.exception.FebsException;
import cc.mrbird.febs.common.domain.QueryRequest;

import cc.mrbird.febs.common.properties.FebsProperties;

import cc.mrbird.febs.common.utils.FebsUtil;
import cc.mrbird.febs.system.domain.User;
import cc.mrbird.febs.yb.entity.YbAppealResultView;
import cc.mrbird.febs.yb.service.IYbAppealResultService;
import cc.mrbird.febs.yb.service.IYbAppealResultViewService;
import cn.hutool.core.io.FileUtil;
import cn.hutool.core.io.IoUtil;
import cn.hutool.core.util.ZipUtil;
import cn.hutool.poi.excel.ExcelUtil;
import cn.hutool.poi.excel.ExcelWriter;
import com.baomidou.mybatisplus.core.toolkit.StringPool;
import com.wuwenze.poi.ExcelKit;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import java.io.*;
import java.net.URLEncoder;
import java.util.*;
import java.util.zip.ZipFile;

/**
 * @author viki
 * @since 2019-11-14
 */
@Slf4j
@Validated
@RestController
@RequestMapping("comFile")

public class ComFileController extends BaseController {

    private String message;
    @Autowired
    public IComFileService iComFileService;
    @Autowired
    private FebsProperties febsProperties;

    @Autowired
    public IYbAppealResultViewService iYbAppealResultViewService;

    /**
     * 分页查询数据
     *
     * @param comFile 查询条件
     * @return
     */
    @GetMapping
    @RequiresPermissions("comFile:view")
    public Map<String, Object> List(QueryRequest request, ComFile comFile) {
        return getDataTable(this.iComFileService.findComFiles(request, comFile));
    }

    /**
     * 跳转添加页面
     *
     * @return
     */
    @Log("新增/按钮")
    @PostMapping
    @RequiresPermissions("comFile:add")
    public void addComFile(@Valid ComFile comFile) throws FebsException {
        try {
            User currentUser = FebsUtil.getCurrentUser();
            //comFile.setCreateUserId(currentUser.getUserId());
            this.iComFileService.createComFile(comFile);
        } catch (Exception e) {
            message = "新增/按钮失败";
            log.error(message, e);
            throw new FebsException(message);
        }
    }

    /**
     * 跳转修改页面
     *
     * @return
     */
    @Log("修改")
    @PutMapping
    @RequiresPermissions("comFile:update")
    public void updateComFile(@Valid ComFile comFile) throws FebsException {
        try {
            User currentUser = FebsUtil.getCurrentUser();
            //comFile.setModifyUserId(currentUser.getUserId().toString());
            this.iComFileService.updateComFile(comFile);
        } catch (Exception e) {
            message = "修改成功";
            log.error(message, e);
            throw new FebsException(message);
        }
    }


    @Log("删除")
    @DeleteMapping("/{ids}")
    @RequiresPermissions("comFile:delete")
    public void deleteComFiles(@NotBlank(message = "{required}") @PathVariable String ids) throws FebsException {
        try {
            String[] arr_ids = ids.split(StringPool.COMMA);
            this.iComFileService.deleteComFiles(arr_ids);
        } catch (Exception e) {
            message = "删除成功";
            log.error(message, e);
            throw new FebsException(message);
        }
    }

    @PostMapping("excel")
    @RequiresPermissions("comFile:export")
    public void export(QueryRequest request, ComFile comFile, HttpServletResponse response) throws FebsException {
        try {
            List<ComFile> comFiles = this.iComFileService.findComFiles(request, comFile).getRecords();
            ExcelKit.$Export(ComFile.class, response).downXlsx(comFiles, false);
        } catch (Exception e) {
            message = "导出Excel失败";
            log.error(message, e);
            throw new FebsException(message);
        }
    }

    @PostMapping("excel1")
    public void export1(QueryRequest request, ComFile comFile, HttpServletResponse response) throws FebsException {
        try {
            List<ComFile> comFiles = this.iComFileService.findListComFile(comFile.getId());
            ExcelWriter writer = ExcelUtil.getWriter(true);
            writer.addHeaderAlias("id", "ID");
            writer.addHeaderAlias("clientName", "客户端名称");
            writer.addHeaderAlias("serverName", "服务端名称");

            writer.write(comFiles, false);
            //response为HttpServletResponse对象
            response.setContentType("application/vnd.ms-excel;charset=utf-8");
            //test.xls是弹出下载对话框的文件名，不能为中文，中文请自行编码
            response.setHeader("Content-Disposition", "attachment;filename=test.xls");
            ServletOutputStream out = response.getOutputStream();

            writer.flush(out, true);
            // 关闭writer，释放内存
            writer.close();
            //此处记得关闭输出Servlet流
            IoUtil.close(out);
        } catch (Exception e) {
            message = "导出Excel失败";
            log.error(message, e);
            throw new FebsException(message);
        }
    }

    @GetMapping("/{id}")
    public ComFile detail(@NotBlank(message = "{required}") @PathVariable String id) {
        ComFile comFile = this.iComFileService.getById(id);
        return comFile;
    }

    @PostMapping("fileImgZip")
//    @RequiresPermissions("comFile:imgExport")
    public void fileImgZip(QueryRequest request, InUploadFile inUploadFile, HttpServletResponse response) throws FebsException {
        int sourceType = inUploadFile.getSourceType();
        String strSourceType = sourceType == 0 ? "正常" : "剔除";
        String deptName = inUploadFile.getDeptName();
        String path = febsProperties.getUploadPath();
            String address = path + inUploadFile.getApplyDateStr() + "/" + deptName;
        String fileName = "";
        if (inUploadFile.getFileName() != null && inUploadFile.getFileName() != "") {
            fileName = inUploadFile.getFileName() + ".zip";
        } else {
            fileName = UUID.randomUUID().toString() + ".zip";
        }

        String filePath = address + "-" + inUploadFile.getTypeno() + ".zip";
        try {
            List<ComFile> list = this.iComFileService.findAppealResultComFiles(inUploadFile);
            if (list.size() > 0) {
                File[] fileUtils = new File[list.size()];
                for (int i = 0; i < list.size(); i++) {
                    ComFile comFile = list.get(i);
                    String strId = comFile.getRefTabId();
                    File file = new File(address + strId + strSourceType + "/" + comFile.getServerName());
                    fileUtils[i] = file;
                }
                ZipUtil.zip(FileUtil.file(filePath), false, fileUtils);
                //ZipUtil.zip(address, filePath);
                this.downFile(response, filePath, fileName);
            }
        } catch (Exception e) {
            message = "导出失败";
            log.error(message, e);
            throw new FebsException(message);
        }
    }

    public void downFile(HttpServletResponse response, String filePath, String fileName) {
        try {
            File file = new File(filePath);
            if (file.exists()) {
                InputStream ins = new FileInputStream(filePath);
                BufferedInputStream bins = new BufferedInputStream(ins);// 放到缓冲流里面
                OutputStream outs = response.getOutputStream();// 获取文件输出IO流
                BufferedOutputStream bouts = new BufferedOutputStream(outs);
                response.setHeader("Content-Disposition",
                        "attachment;filename=" + URLEncoder.encode(fileName, "utf-8"));

                int bytesRead = 0;
                byte[] buffer = new byte[512];
                //开始向网络传输文件流
                while ((bytesRead = bins.read(buffer, 0, 512)) != -1) {
                    bouts.write(buffer, 0, bytesRead);
                }
                bouts.flush();// 这里一定要调用flush()方法
                ins.close();
                bins.close();
                outs.close();
                bouts.close();
            } else {
//                response.sendRedirect("../error.jsp");
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            this.deleteFile(filePath);
        }
    }


    @PostMapping("listImgComFile")
    public FebsResponse findImgListComFiles(InUploadFile inUploadFile) {
        List<OutComFile> outList = new ArrayList<OutComFile>();
        try {
            List<ComFile> list = this.iComFileService.findListComFile(inUploadFile.getId());
            String strId = inUploadFile.getId();
            int sourceType = inUploadFile.getSourceType();
            String strSourceType = sourceType == 0 ? "正常" : "剔除";
            String deptName = inUploadFile.getDeptName() + strId + strSourceType;
            if (list.size() > 0) {
                for (ComFile item : list) {
                    String fileUrl = febsProperties.getBaseUrl() + "/uploadFile/" + inUploadFile.getApplyDateStr() + "/" + deptName + "/" + item.getServerName();
                    OutComFile outComFile = new OutComFile();
                    outComFile.setUid(item.getId());
                    outComFile.setName(item.getServerName());
                    outComFile.setStatus("done");
                    outComFile.setUrl(fileUrl);
                    outComFile.setSerName(item.getServerName());
                    outComFile.setThumbUrl(fileUrl);
                    outList.add(outComFile);
                }
            }
        } catch (Exception e) {
            log.error(message, e);
        }
        return new FebsResponse().data(outList);
    }

    @PostMapping("uploadImg")
    public FebsResponse UploadImg(@RequestParam("file") MultipartFile file, InUploadFile inUploadFile) throws FebsException {
        if (file.isEmpty()) {
            throw new FebsException("空文件");
        }
        int num = 1;
        List<ComFile> list = this.iComFileService.findListComFile(inUploadFile.getId());
        if (list.size() > 0) {
            ComFile comFile = list.get(list.size() - 1);
            String code = comFile.getServerName();
            int strNum = code.lastIndexOf("-");
            String comFileName = code.substring(strNum + 1, code.length() - 4);
            num = Integer.parseInt(comFileName) + 1;
        }
        String newFileName = inUploadFile.getProposalCode();
        if (num < 10) {
            newFileName += "-00" + String.valueOf(num);
        } else if (num < 100) {
            newFileName += "-0" + String.valueOf(num);
        } else {
            newFileName += "-" + String.valueOf(num);
        }
        String strId = inUploadFile.getId();
        int sourceType = inUploadFile.getSourceType();
        String strSourceType = sourceType == 0 ? "正常" : "剔除";
        String deptName = inUploadFile.getDeptName() + strId + strSourceType;
        String fileName2 = file.getOriginalFilename();  // 文件名
        String suffixName = fileName2.substring(fileName2.lastIndexOf("."));  // 后缀名
        String filePath = febsProperties.getUploadPath(); // 上传后的路径
        String fileName = newFileName + suffixName; // 新文件名
        File dest = new File(filePath + inUploadFile.getApplyDateStr() + "/" + deptName + "/" + fileName);
        if (!dest.getParentFile().exists()) {
            dest.getParentFile().mkdirs();
        }
        try {
            file.transferTo(dest);
        } catch (IOException e) {
            throw new FebsException(e.getMessage());
        }

        String Id = UUID.randomUUID().toString();
        ComFile cf = new ComFile();
        cf.setId(Id);
        cf.setCreateTime(new Date());
        cf.setClientName(fileName2);//客户端的名称
        cf.setServerName(fileName);
        cf.setRefTabId(strId);
        cf.setRefTabTable(inUploadFile.getRefTab());
        iComFileService.createComFile(cf);
        String fileUrl = febsProperties.getBaseUrl() + "/uploadFile/" + inUploadFile.getApplyDateStr() + "/" + deptName + "/" + fileName;

        OutComFile outComFile = new OutComFile();
        outComFile.setUid(Id);
        outComFile.setName(fileName2);
        outComFile.setStatus("done");
        outComFile.setUrl(fileUrl);
        outComFile.setThumbUrl(fileUrl);
        outComFile.setSerName(fileName);
        return new FebsResponse().put("data", outComFile);
    }

    @Log("删除")
    @PostMapping("deleteImg")
    public FebsResponse deleteImgComFile(InUploadFile inUploadFile) {
        boolean bl = false;
        try {
            String strId = inUploadFile.getId();
            ComFile comFile = this.iComFileService.findComFileById(strId);
            if(comFile!=null) {
                String strRefId = comFile.getRefTabId();
                int sourceType = inUploadFile.getSourceType();
                String strSourceType = sourceType == 0 ? "正常" : "剔除";
                String deptName = inUploadFile.getDeptName() + strRefId + strSourceType;
                String filePath = febsProperties.getUploadPath(); // 上传后的路径
                String fileUrl = filePath + inUploadFile.getApplyDateStr() + "/" + deptName + "/" + inUploadFile.getSerName();
                boolean blFile = deleteFile(fileUrl);
                if (blFile) {
                    int count = this.iComFileService.deleteComFile(inUploadFile.getId());
                    if (count > 0) {
                        bl = true;
                    }
                }
            }
        } catch (Exception e) {
            log.error(message, e);
        }
        return new FebsResponse().put("success", bl);
    }

    /**
     * 删除单个文件
     *
     * @param sPath 被删除文件的文件名
     * @return 单个文件删除成功返回true，否则返回false
     */
    public boolean deleteFile(String sPath) {
        boolean flag = false;
        File file = new File(sPath);
        // 路径为文件且不为空则进行删除
        if (file.isFile() && file.exists()) {
            file.delete();
            flag = true;
        }
        return flag;
    }
}