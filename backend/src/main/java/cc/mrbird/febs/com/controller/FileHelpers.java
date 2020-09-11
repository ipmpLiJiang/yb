package cc.mrbird.febs.com.controller;

import cc.mrbird.febs.common.domain.FebsResponse;
import cc.mrbird.febs.common.exception.FebsException;
import cc.mrbird.febs.common.properties.FebsProperties;
import org.apache.poi.ss.usermodel.Workbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

/**
 * @author lijiang
 * @createDate 2020/8/18
 */
public  class FileHelpers {

    public static File fileUpLoad(@RequestParam MultipartFile file,String filePath, String fileName,String folderName) throws Exception {
        String fileName2 = file.getOriginalFilename();  // 文件名
        String suffixName = fileName2.substring(fileName2.lastIndexOf("."));  // 后缀名

        String guId = UUID.randomUUID().toString();
        if(fileName=="" || fileName==null){
            fileName = guId;
        }
        String newfileName = fileName + suffixName; // 新文件名
        File dest = new File(filePath + folderName + "/" + newfileName);
        if (!dest.getParentFile().exists()) {
            dest.getParentFile().mkdirs();
        }
        try {
            file.transferTo(dest);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return dest;
    }

    /**
     * 文件上传方法
     */

    public static boolean fileUpLoad(MultipartFile[] files, HttpServletRequest request, String path)
            throws IOException {

        if (files != null && files.length > 0) {
            for (int i = 0; i < files.length; i++) {
                MultipartFile file = files[i];
                // 保存文件
                return saveFile(request, file, path);
            }
        }
        return false;
    }

    /**
     * 保存上传文件
     *
     * @param request
     * @param file
     * @return
     */

    public static boolean saveFile(HttpServletRequest request, MultipartFile file, String path) {

        if (!file.isEmpty()) {
            try {
                File saveDir = new File(path);
                if (!saveDir.getParentFile().exists())
                    saveDir.getParentFile().mkdirs();
                // 转存文件
                file.transferTo(saveDir);
                return true;
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return false;
    }
}
