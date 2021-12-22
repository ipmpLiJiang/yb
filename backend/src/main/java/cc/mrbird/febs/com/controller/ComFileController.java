package cc.mrbird.febs.com.controller;

import cc.mrbird.febs.com.entity.*;
import cc.mrbird.febs.com.service.IComFileService;
import cc.mrbird.febs.com.service.IComTypeService;
import cc.mrbird.febs.common.annotation.Log;
import cc.mrbird.febs.common.controller.BaseController;
import cc.mrbird.febs.common.domain.FebsResponse;
import cc.mrbird.febs.common.domain.router.VueRouter;
import cc.mrbird.febs.common.exception.FebsException;
import cc.mrbird.febs.common.domain.QueryRequest;

import cc.mrbird.febs.common.properties.FebsProperties;

import cc.mrbird.febs.common.utils.FebsUtil;
import cc.mrbird.febs.drg.entity.YbDrgResult;
import cc.mrbird.febs.drg.service.IYbDrgApplyService;
import cc.mrbird.febs.drg.service.IYbDrgResultService;
import cc.mrbird.febs.system.domain.User;
import cc.mrbird.febs.yb.domain.ResponseResult;
import cc.mrbird.febs.yb.entity.YbAppealResultView;
import cc.mrbird.febs.yb.entity.YbDefaultValue;
import cc.mrbird.febs.yb.entity.YbReconsiderApply;
import cc.mrbird.febs.yb.service.IYbAppealResultService;
import cc.mrbird.febs.yb.service.IYbAppealResultViewService;
import cc.mrbird.febs.yb.service.IYbReconsiderApplyService;
import cn.hutool.core.io.FileUtil;
import cn.hutool.core.io.IoUtil;
import cn.hutool.core.net.multipart.UploadFile;
import cn.hutool.core.util.ZipUtil;
import cn.hutool.poi.excel.ExcelUtil;
import cn.hutool.poi.excel.ExcelWriter;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.StringPool;
import com.wuwenze.poi.ExcelKit;
import lombok.extern.slf4j.Slf4j;
import net.bytebuddy.implementation.bytecode.Throw;
import org.apache.commons.io.FileUtils;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import java.io.*;
import java.net.URLEncoder;
import java.util.*;
import java.util.stream.Collectors;
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

    @Autowired
    public IYbReconsiderApplyService iYbReconsiderApplyService;

    @Autowired
    IYbDrgApplyService iYbDrgApplyService;

    @Autowired
    IComTypeService iComTypeService;

    @Autowired
    IYbDrgResultService iYbDrgResultService;

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
            List<ComFile> comFiles = this.iComFileService.findListComFile(comFile.getId(), null);
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
        String strSourceType = sourceType == 0 ? "In" : "Out";
        if (inUploadFile.getAreaType() != 0) {
            strSourceType += inUploadFile.getAreaType();
        }
        if (sourceType == 1) {
            inUploadFile.setTypeno(3);
        }
        String deptName = inUploadFile.getDeptName();

        String path = febsProperties.getUploadPath();
        //String address = path + inUploadFile.getApplyDateStr() + "/" + deptName;
        String address = path + inUploadFile.getApplyDateStr() + "/";
        String fileName = "";
        if (inUploadFile.getFileName() != null && !inUploadFile.getFileName().equals("")) {
            fileName = inUploadFile.getFileName() + ".zip";
        } else {
            fileName = UUID.randomUUID().toString() + ".zip";
        }
        Random r = new Random();
        int nxt = r.nextInt(10000);
        String filePath = address + deptName + "-" + inUploadFile.getTypeno() + "-" + inUploadFile.getAreaType() + "-" + nxt + ".zip";
        try {
            List<ComFile> list = this.iComFileService.findAppealResultComFiles(inUploadFile);
            if (list.size() > 0) {
                File[] fileUtils = new File[list.size()];
                for (int i = 0; i < list.size(); i++) {
                    ComFile comFile = list.get(i);
                    String t = comFile.getRefTabTable();
                    File file = new File(address + t + "/" + strSourceType + "/" + comFile.getServerName());
                    fileUtils[i] = file;
                }
                ZipUtil.zip(FileUtil.file(filePath), false, fileUtils);
                //ZipUtil.zip(address, filePath);
                this.downFile(response, filePath, fileName, true);
            }
        } catch (Exception e) {
            message = "导出失败";
            log.error(message, e);
            throw new FebsException(message);
        }
    }

    @PostMapping("fileSumImgZip")
//    @RequiresPermissions("comFile:imgExport")
    public void fileSumImgZip(QueryRequest request, InUploadFile inUploadFile, HttpServletResponse response) throws FebsException {
        int sourceType = inUploadFile.getSourceType();
        String strSourceType = sourceType == 0 ? "In" : "Out";
        if (inUploadFile.getAreaType() != 0) {
            strSourceType += inUploadFile.getAreaType();
        }
        if (sourceType == 1) {
            inUploadFile.setTypeno(3);
        }
        String deptName = inUploadFile.getDeptName();
        String path = febsProperties.getUploadPath();
        //String address = path + inUploadFile.getApplyDateStr() + "/" + deptName;
        String address = path + inUploadFile.getApplyDateStr() + "/";
        String fileName = "";
        if (inUploadFile.getFileName() != null && !inUploadFile.getFileName().equals("")) {
            fileName = inUploadFile.getFileName() + ".zip";
        } else {
            fileName = UUID.randomUUID().toString() + ".zip";
        }
        Random r = new Random();
        int nxt = r.nextInt(10000);
        String filePath = address + deptName + "-" + inUploadFile.getTypeno() + "-" + inUploadFile.getAreaType() + "-" + nxt + ".zip";
        try {
            List<ComFile> list = this.iComFileService.findAppealResultSumComFiles(inUploadFile);
            if (list.size() > 0) {
                File[] fileUtils = new File[list.size()];
                for (int i = 0; i < list.size(); i++) {
                    ComFile comFile = list.get(i);
                    String t = comFile.getRefTabTable();
                    File file = new File(address + t + "/" + strSourceType + "/" + comFile.getServerName());
                    fileUtils[i] = file;
                }
                ZipUtil.zip(FileUtil.file(filePath), false, fileUtils);
                //ZipUtil.zip(address, filePath);
                this.downFile(response, filePath, fileName, true);
            }
        } catch (Exception e) {
            message = "导出失败";
            log.error(message, e);
            throw new FebsException(message);
        }
    }

    private void downFile(HttpServletResponse response, String filePath, String fileName, boolean isDel) {
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
            if (isDel) {
                this.deleteFile(filePath);
            }
        }
    }

    @PostMapping("downloadFile")
    public void findFiles(QueryRequest request, String id, String folderName, HttpServletResponse response) throws FebsException {
        try {
            ComFile comFile = this.iComFileService.findComFileById(id);
            if (comFile != null) {
                String filePath = febsProperties.getUploadPath(); // 上传后的路径
                String fileUrl = filePath + folderName + "/" + comFile.getServerName();
                this.downFile(response, fileUrl, comFile.getClientName(), false);
            } else {
                throw new FebsException("文件不存在下载失败.");
            }
        } catch (Exception e) {
            message = "下载失败.";
            log.error(message, e);
            throw new FebsException(message);
        }
    }

    @Log("删除")
    @PostMapping("delFile")
    public FebsResponse deleteComFile(InUploadFile inUploadFile) {
        int success = 0;
        try {
            ComFile comFile = this.iComFileService.findComFileById(inUploadFile.getId());
            if (comFile != null) {
                String filePath = febsProperties.getUploadPath(); // 上传后的路径
                String fileUrl = filePath + inUploadFile.getFileName() + "/" + comFile.getServerName();
                boolean blFile = deleteFile(fileUrl);
                if (blFile) {
                    int count = this.iComFileService.deleteComFile(inUploadFile.getId());
                    if (count > 0) {
                        success = 1;
                        message = "删除文件成功.";
                    } else {
                        message = "删除文件失败.";
                    }
                }
            } else {
                message = "不存在文件数据.";
            }
        } catch (Exception e) {
            message = "删除文件异常.";
        }
        ResponseResult rr = new ResponseResult();
        rr.setMessage(message);
        rr.setSuccess(success);
        return new FebsResponse().data(rr);
    }

    @GetMapping("findFileList")
    public FebsResponse findFileList(String refTabId) {
        List<OutComFile> outList = new ArrayList<>();
        List<ComFile> list = this.iComFileService.findListComFile(refTabId, null);
        for (ComFile item : list) {
            OutComFile outComFile = new OutComFile();
            outComFile.setUid(item.getId());
            outComFile.setName(item.getClientName());
            outList.add(outComFile);
        }
        return new FebsResponse().data(outList);
    }

    @PostMapping("listImgComFile")
    public FebsResponse findImgListComFiles(InUploadFile inUploadFile) {
        List<OutComFile> outList = new ArrayList<>();
        try {
            List<ComFile> list = this.iComFileService.findListComFile(inUploadFile.getId(), null);
            String strId = inUploadFile.getId();
            int sourceType = inUploadFile.getSourceType();
            String strSourceType = sourceType == 0 ? "In" : "Out";
            if (inUploadFile.getAreaType() != 0) {
                strSourceType += inUploadFile.getAreaType();
            }
            //String deptName = inUploadFile.getDeptName() + strId + strSourceType;
            if (list.size() > 0) {
                for (ComFile item : list) {
                    String fileUrl = "uploadFile/" + inUploadFile.getApplyDateStr() + "/" + item.getRefTabTable() + "/" + strSourceType + "/" + item.getServerName();
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

    @PostMapping("uploadFile")
    public FebsResponse uploadPubFile(@RequestParam("file") MultipartFile file, InUploadFile inUploadFile) throws FebsException {
        if (file.isEmpty()) {
            throw new FebsException("空文件");
        }
        Date thisDate = new Date();
        OutComFile outComFile = new OutComFile();
//        User currentUser = FebsUtil.getCurrentUser();
        String upFileName = file.getOriginalFilename();  // 文件名
        String suffixName = upFileName.substring(upFileName.lastIndexOf("."));  // 后缀名
        String filePath = febsProperties.getUploadPath(); // 上传后的路径
        String SerFileName = UUID.randomUUID().toString() + suffixName; // 新文件名
        File dest = new File(filePath + inUploadFile.getFileName() + "/" + SerFileName);
        if (!dest.getParentFile().exists()) {
            dest.getParentFile().mkdirs();
        }
        String Id = UUID.randomUUID().toString();
        try {
            file.transferTo(dest);
            ComFile cf = new ComFile();
            cf.setId(Id);
            cf.setCreateTime(thisDate);
            cf.setClientName(upFileName);//客户端的名称
            cf.setServerName(SerFileName);
            cf.setRefTabId(inUploadFile.getId());
            cf.setRefTabTable(inUploadFile.getRefTab());
            iComFileService.createComFile(cf);
        } catch (IOException e) {
            throw new FebsException(e.getMessage());
        }
//        String fileUrl = febsProperties.getBaseUrl() + "/uploadFile/" + inUploadFile.getFileName() + "/" + SerFileName;

        outComFile.setSuccess(1);
        outComFile.setUid(Id);
        outComFile.setName(upFileName);

        return new FebsResponse().data(outComFile);
    }

    @PostMapping("uploadImg")
    public FebsResponse UploadImg(@RequestParam("file") MultipartFile file, InUploadFile inUploadFile) throws FebsException {
        if (file.isEmpty()) {
            throw new FebsException("空文件");
        }
        boolean isUpdate = false;
        String mms = "";
        Date thisDate = new Date();
        OutComFile outComFile = new OutComFile();
        if (inUploadFile.getIsCheck() == 1) {
            isUpdate = iYbReconsiderApplyService.findReconsiderApplyCheckEndDate(inUploadFile.getApplyDateStr(), inUploadFile.getAreaType(), inUploadFile.getTypeno());
            if (inUploadFile.getTypeno() == YbDefaultValue.TYPENO_1) {
                mms = "第一版";
            } else {
                mms = "第二版";
            }
        } else {
            isUpdate = true;
        }
        if (isUpdate || inUploadFile.getSourceType() == YbDefaultValue.SOURCETYPE_1) {
            int num = 1;
            List<ComFile> list = this.iComFileService.findListComFile(inUploadFile.getId(), null);
            if (list.size() > 0) {
                ComFile comFile = list.get(list.size() - 1);
                String code = comFile.getServerName();
                int dian = code.lastIndexOf(".");
                int lastDian = code.length() - dian;
                int strNum = code.lastIndexOf("-");
                String comFileName = code.substring(strNum + 1, code.length() - lastDian);
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
            User currentUser = FebsUtil.getCurrentUser();
            int sourceType = inUploadFile.getSourceType();
            String strSourceType = sourceType == 0 ? "In" : "Out";
            if (inUploadFile.getAreaType() != 0) {
                strSourceType += inUploadFile.getAreaType();
            }
            // String deptName = inUploadFile.getDeptName() + strId + strSourceType;
            String deptName = currentUser.getUsername() + "/" + strSourceType;
            String fileName2 = file.getOriginalFilename();  // 文件名
            String suffixName = fileName2.substring(fileName2.lastIndexOf("."));  // 后缀名
            String filePath = febsProperties.getUploadPath(); // 上传后的路径
            String fileName = newFileName + suffixName; // 新文件名
            File dest = new File(filePath + inUploadFile.getApplyDateStr() + "/" + deptName + "/" + fileName);
            String Id = UUID.randomUUID().toString();
            if (!dest.getParentFile().exists()) {
                dest.getParentFile().mkdirs();
            }
            try {
                file.transferTo(dest);
                ComFile cf = new ComFile();
                cf.setId(Id);
                cf.setCreateTime(thisDate);
                cf.setClientName(fileName2);//客户端的名称
                cf.setServerName(fileName);
                cf.setRefTabId(strId);
                cf.setRefTabTable(currentUser.getUsername());
                iComFileService.createComFile(cf);

            } catch (IOException e) {
                throw new FebsException(e.getMessage());
            }
            String fileUrl = febsProperties.getBaseUrl() + "/uploadFile/" + inUploadFile.getApplyDateStr() + "/" + deptName + "/" + fileName;

            outComFile.setSuccess(1);
            outComFile.setUid(Id);
            outComFile.setName(fileName2);
            outComFile.setStatus("done");
            outComFile.setUrl(fileUrl);
            outComFile.setThumbUrl(fileUrl);
            outComFile.setSerName(fileName);
        } else {
            outComFile.setSuccess(0);
            outComFile.setMessage("当前已过 " + mms + " 截止日期，无法上传图片.");
        }

        return new FebsResponse().put("data", outComFile);
    }

    @Log("删除")
    @PostMapping("deleteImg")
    public FebsResponse deleteImgComFile(InUploadFile inUploadFile) {
        int success = 0;
        try {
            boolean isUpdate = false;
            String mms = "";
            if (inUploadFile.getIsCheck() == 1) {
                isUpdate = iYbReconsiderApplyService.findReconsiderApplyCheckEndDate(inUploadFile.getApplyDateStr(), inUploadFile.getAreaType(), inUploadFile.getTypeno());
                if (inUploadFile.getTypeno() == YbDefaultValue.TYPENO_1) {
                    mms = "第一版";
                } else {
                    mms = "第二版";
                }
            } else {
                isUpdate = true;
            }
            if (isUpdate || inUploadFile.getSourceType() == YbDefaultValue.SOURCETYPE_1) {
                String strId = inUploadFile.getId();
                ComFile comFile = this.iComFileService.findComFileById(strId);
                if (comFile != null) {
//                    String strRefId = comFile.getRefTabId();
                    int sourceType = inUploadFile.getSourceType();
                    String strSourceType = sourceType == 0 ? "In" : "Out";
                    if (inUploadFile.getAreaType() != 0) {
                        strSourceType += inUploadFile.getAreaType();
                    }
                    User currentUser = FebsUtil.getCurrentUser();
                    //String deptName = inUploadFile.getDeptName() + strRefId + strSourceType;
                    String deptName = currentUser.getUsername() + "/" + strSourceType;
                    String filePath = febsProperties.getUploadPath(); // 上传后的路径
                    String fileUrl = filePath + inUploadFile.getApplyDateStr() + "/" + deptName + "/" + inUploadFile.getSerName();
                    boolean blFile = deleteFile(fileUrl);
                    if (blFile) {
                        int count = this.iComFileService.deleteComFile(inUploadFile.getId());
                        if (count > 0) {
                            success = 1;
                        }
                    }
                }
            } else {
                message = "当前已过 " + mms + " 截止日期，无法删除图片.";
            }
        } catch (Exception e) {
            message = "删除失败.";
            log.error(message, e);
        }
        ResponseResult rr = new ResponseResult();
        rr.setMessage(message);
        rr.setSuccess(success);
        return new FebsResponse().data(rr);
    }

    @PostMapping("listDrgImgComFile")
    public FebsResponse findDrgImgListComFiles(InDrgUploadFile inUploadFile) {
        List<OutComFile> outList = new ArrayList<>();
        try {
            List<ComFile> list = this.iComFileService.findListComFile(inUploadFile.getId(), inUploadFile.getRefType());
            String fn = "";
            if (list.size() > 0) {
                ComType query = new ComType();
                query.setCtType(2);
                List<ComType> typeList = iComTypeService.findComTypeList(query);
                List<ComType> queryList = new ArrayList<>();
                for (ComFile item : list) {
                    queryList = typeList.stream().filter(s -> s.getId().equals(Integer.parseInt(item.getRefType()))).collect(Collectors.toList());

                    fn = item.getClientName().substring(0, item.getClientName().lastIndexOf("."));
                    if (queryList.size() > 0) {
                        item.setClientName(queryList.get(0).getCtName() + " " + fn);
                        item.setOrderNum(queryList.get(0).getOrderNum());
                    } else {
                        item.setOrderNum(typeList.size() + 1);
                    }
                }

                if (inUploadFile.getIsOn() == null) {
                    list = list.stream().sorted(Comparator.comparing(ComFile::getCreateTime).reversed()).collect(Collectors.toList());
                } else {
                    list = list.stream().sorted(Comparator.comparing(ComFile::getOrderNum)).collect(Collectors.toList());
                }

                for (ComFile item : list) {
                    String fileUrl = "uploadFile/" + inUploadFile.getApplyDateStr() + "/DRG" + inUploadFile.getAreaType() +
                            "/" + inUploadFile.getOrderNumber() + "/" + item.getRefType() + "/" + item.getServerName();
                    OutComFile outComFile = new OutComFile();
                    outComFile.setUid(item.getId());
                    outComFile.setName(item.getClientName());
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


    @PostMapping("uploadDrgImg")
    public FebsResponse UploadDrgImg(@RequestParam("file") MultipartFile file, InDrgUploadFile inUploadFile) throws FebsException {
        if (file.isEmpty()) {
            throw new FebsException("空文件");
        }
        boolean isUpdate = false;
        Date thisDate = new Date();
        OutComFile outComFile = new OutComFile();
        if (inUploadFile.getIsCheck() == 1) {
            isUpdate = iYbDrgApplyService.findDrgApplyCheckEndDate(inUploadFile.getApplyDateStr(), inUploadFile.getAreaType());
        } else {
            isUpdate = true;
        }
        if (isUpdate) {
            String strId = inUploadFile.getId();
            String refTab = inUploadFile.getRefTab();
            String refType = inUploadFile.getRefType();
            String fileName2 = file.getOriginalFilename();  // 文件名
            String suffixName = fileName2.substring(fileName2.lastIndexOf("."));  // 后缀名
            String filePath = febsProperties.getUploadPath(); // 上传后的路径
            String fileName = UUID.randomUUID().toString() + suffixName; // 新文件名
            String furl = filePath + inUploadFile.getApplyDateStr() + "/DRG" + inUploadFile.getAreaType() +
                    "/" + inUploadFile.getOrderNumber() + "/" + inUploadFile.getRefType() + "/" + fileName;
            File dest = new File(furl);
            String Id = UUID.randomUUID().toString();
            if (!dest.getParentFile().exists()) {
                dest.getParentFile().mkdirs();
            }
            try {
                file.transferTo(dest);
                ComFile cf = new ComFile();
                cf.setId(Id);
                cf.setCreateTime(thisDate);
                cf.setClientName(fileName2);//客户端的名称
                cf.setServerName(fileName);
                cf.setRefTabId(strId);
                cf.setRefTabTable(refTab);
                cf.setRefType(refType);
                iComFileService.createComFile(cf);

            } catch (IOException e) {
                throw new FebsException(e.getMessage());
            }
            String fileUrl = febsProperties.getBaseUrl() + "/uploadFile/" + inUploadFile.getApplyDateStr() + "/DRG" + inUploadFile.getAreaType() +
                    "/" + inUploadFile.getOrderNumber() + "/" + inUploadFile.getRefType() + "/" + fileName;

            outComFile.setSuccess(1);
            outComFile.setUid(Id);
            String fn = fileName2.substring(0, fileName2.lastIndexOf("."));
            outComFile.setName(inUploadFile.getRefTypeName() + " " + fn);
            outComFile.setStatus("done");
            outComFile.setUrl(fileUrl);
            outComFile.setThumbUrl(fileUrl);
            outComFile.setSerName(fileName);
        } else {
            outComFile.setSuccess(0);
            outComFile.setMessage("当前已过截止日期，无法上传图片.");
        }

        return new FebsResponse().put("data", outComFile);
    }

    @Log("删除")
    @PostMapping("deleteDrgImg")
    public FebsResponse deleteDrgImgComFile(InDrgUploadFile inUploadFile) {
        int success = 0;
        try {
            boolean isUpdate = false;
            if (inUploadFile.getIsCheck() == 1) {
                isUpdate = iYbDrgApplyService.findDrgApplyCheckEndDate(inUploadFile.getApplyDateStr(), inUploadFile.getAreaType());
            } else {
                isUpdate = true;
            }
            if (isUpdate) {
                String strId = inUploadFile.getId();
                ComFile comFile = this.iComFileService.findComFileById(strId);
                if (comFile != null) {
                    String filePath = febsProperties.getUploadPath(); // 上传后的路径
                    String fileUrl = filePath + inUploadFile.getApplyDateStr() + "/DRG" + inUploadFile.getAreaType() + "/" +
                            "/" + inUploadFile.getOrderNumber() + "/" + comFile.getRefType() + "/" + comFile.getServerName();
                    boolean blFile = deleteFile(fileUrl);
                    if (blFile) {
                        int count = this.iComFileService.deleteComFile(inUploadFile.getId());
                        if (count > 0) {
                            success = 1;
                        }
                    }
                }
            } else {
                message = "当前已过截止日期，无法删除图片.";
            }
        } catch (Exception e) {
            message = "删除失败.";
            log.error(message, e);
        }
        ResponseResult rr = new ResponseResult();
        rr.setMessage(message);
        rr.setSuccess(success);
        return new FebsResponse().data(rr);
    }

    @PostMapping("fileDrgImgZip")
    public void fileDrgImgZip(QueryRequest request, InDrgUploadFile inUploadFile, HttpServletResponse response) throws FebsException {
        String path = febsProperties.getUploadPath();
        String address = path + inUploadFile.getApplyDateStr() + "/DRG" + inUploadFile.getAreaType() + "/";
        String fileName = "";
        if (inUploadFile.getFileName() != null && !inUploadFile.getFileName().equals("")) {
            fileName = inUploadFile.getFileName();
        } else {
            fileName = UUID.randomUUID().toString();
        }
        Random r = new Random();
        int nxt = r.nextInt(10000);
        String filePath = address + fileName + "-" + nxt + ".zip";

        fileName = fileName + ".zip";
        try {
            List<ComFile> list = this.iComFileService.findListComFile(inUploadFile.getId(), inUploadFile.getRefType());
            if (list.size() > 0) {
                File[] fileUtils = new File[list.size()];
                for (int i = 0; i < list.size(); i++) {
                    ComFile comFile = list.get(i);
                    File file = new File(address + inUploadFile.getOrderNumber() + "/" + comFile.getRefType() + "/" + comFile.getServerName());
                    fileUtils[i] = file;
                }
                ZipUtil.zip(FileUtil.file(filePath), false, fileUtils);
                //ZipUtil.zip(address, filePath);
                this.downFile(response, filePath, fileName, true);
            }
        } catch (Exception e) {
            message = "导出失败";
            log.error(message, e);
            throw new FebsException(message);
        }
    }

    @PostMapping("fileDrgSumImgZip")
    public void fileDrgSumImgZip(QueryRequest request, InDrgUploadFile inUploadFile, HttpServletResponse response) throws FebsException {
        List<String> orderNumberList = new ArrayList<>();
        Integer startOrderNumber = inUploadFile.getStartOrderNumber();
        Integer endOrderNumber = inUploadFile.getEndOrderNumber();
        if(startOrderNumber == endOrderNumber) {
            orderNumberList.add(startOrderNumber.toString());
        } else {
            while (startOrderNumber <= endOrderNumber) {
                orderNumberList.add(startOrderNumber.toString());
                startOrderNumber++;
            }
        }
        String path = febsProperties.getUploadPath();
        String address = path + inUploadFile.getApplyDateStr() + "/DRG" + inUploadFile.getAreaType() + "/";
        String fileName = orderNumberList.get(0) + "-" + inUploadFile.getEndOrderNumber().toString();
        Random r = new Random();
        int nxt = r.nextInt(10000);
        String filePath = address + fileName + "-" + nxt + ".zip";

        fileName = inUploadFile.getApplyDateStr() + "-" + inUploadFile.getAreaType() + "-" + fileName + "-" + nxt + ".zip";
        try {
            LambdaQueryWrapper<YbDrgResult> wrapperResult = new LambdaQueryWrapper<>();
            wrapperResult.eq(YbDrgResult::getApplyDateStr, inUploadFile.getApplyDateStr());
            wrapperResult.eq(YbDrgResult::getAreaType, inUploadFile.getAreaType());
            List<YbDrgResult> resultAllList = iYbDrgResultService.list(wrapperResult);
            List<YbDrgResult> resultList = this.getInResult(resultAllList,orderNumberList);
            if (resultList.size() > 0) {
                List<ComFile> fileList = iComFileService.findDrgResultComFiles(inUploadFile.getApplyDateStr(), inUploadFile.getAreaType());
                if(fileList.size() > 0) {
                    List<ComFile> list = this.getInFile(resultList,fileList);
                    File[] fileUtils = new File[list.size()];
                    for (int i = 0; i < list.size(); i++) {
                        ComFile comFile = list.get(i);
                        File file = new File(address + comFile.getOrderNumber() + "/" + comFile.getRefType() + "/" + comFile.getServerName());
                        fileUtils[i] = file;
                    }
                    ZipUtil.zip(FileUtil.file(filePath), false, fileUtils);
                    //ZipUtil.zip(address, filePath);
                    this.downFile(response, filePath, fileName, true);
                }
            }
        } catch (Exception e) {
            message = "导出失败";
            log.error(message, e);
            throw new FebsException(message);
        }
    }

    private List<YbDrgResult> getInResult(List<YbDrgResult> resultList,List<String> orderNumberList){
        List<YbDrgResult> list = new ArrayList<>();
        List<YbDrgResult> query = new ArrayList<>();
        for (String orderNumber : orderNumberList) {
            query = resultList.stream().filter(s->s.getOrderNumber().equals(orderNumber)).collect(Collectors.toList());
            if(query.size() > 0) {
                list.add(query.get(0));
            }
        }
        return list;
    }

    private List<ComFile> getInFile(List<YbDrgResult> resultList,List<ComFile> fileList){
        List<ComFile> list = new ArrayList<>();
        List<ComFile> query = new ArrayList<>();
        for (YbDrgResult result : resultList) {
            query = fileList.stream().filter(s->s.getRefTabId().equals(result.getId())).collect(Collectors.toList());
            for (ComFile comFile : query) {
                comFile.setOrderNumber(result.getOrderNumber());
                list.add(comFile);
            }
        }
        return list;
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
        if (file.exists()) {
            if (file.isFile()) {
                file.delete();
                flag = true;
            }
        } else {
            flag = true;
        }
        return flag;
    }

    @PostMapping("weUploadFile")
    public WangEditor weUploadFileImg(@RequestParam("file") MultipartFile multipartFile, HttpServletRequest request) {
        try {
            // 获取项目路径
            String realPath = request.getSession().getServletContext()
                    .getRealPath("");
            InputStream inputStream = multipartFile.getInputStream();
            String path = febsProperties.getUploadPath(); // 上传后的路径

            // 根目录下新建文件夹upload，存放上传图片
            String uploadPath = path + "upload";
            // 获取文件名称
            String upFileName = multipartFile.getOriginalFilename();  // 文件名
            String suffixName = upFileName.substring(upFileName.lastIndexOf("."));  // 后缀名
            String filename = UUID.randomUUID().toString() + suffixName; // 新文件名
            // 将文件上传的服务器根目录下的upload文件夹
            File file = new File(uploadPath, filename);
            FileUtils.copyInputStreamToFile(inputStream, file);
            // 返回图片访问路径
            String url = request.getScheme() + "://" + request.getServerName()
                    + ":" + request.getServerPort() + "/uploadFile/upload/" + filename;
            String[] strs = {url};
            WangEditor we = new WangEditor(strs);
            return we;
        } catch (IOException e) {
            log.error("上传文件失败", e);
        }
        return null;

    }

    @PostMapping("weUploadFiles")
    public WangEditor uploadFile(@RequestParam("files") MultipartFile[] files, HttpServletRequest request) {
        try {
            List list = new ArrayList();

            for (MultipartFile multipartFile : files) {
                // 获取项目路径
                String realPath = request.getSession().getServletContext()
                        .getRealPath("");
                InputStream inputStream = multipartFile.getInputStream();

                //String contextPath = request.getServletContext().getContextPath();
                //logger.info(contextPath);
                // 服务器根目录的路径
                String path = realPath;
                // 根目录下新建文件夹upload，存放上传图片
                String uploadPath = path + "upload";
                // 获取文件名称
                String filename = Calendar.getInstance().getTimeInMillis() + "image";
                // 将文件上传的服务器根目录下的upload文件夹
                File file = new File(uploadPath, filename);
                FileUtils.copyInputStreamToFile(inputStream, file);
                // 返回图片访问路径
                String url = request.getScheme() + "://" + request.getServerName()
                        + ":" + request.getServerPort() + "/upload/" + filename;
                list.add(url);

            }
            //ArrayList<String> list=new ArrayList<String>();

            String[] strings = new String[list.size()];

            list.toArray(strings);
            WangEditor we = new WangEditor(strings);
            return we;
        } catch (IOException e) {
            log.error("上传文件失败", e);
        }
        return null;
    }

}