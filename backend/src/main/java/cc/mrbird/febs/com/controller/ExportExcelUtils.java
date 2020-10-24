package cc.mrbird.febs.com.controller;

import cc.mrbird.febs.common.exception.FebsException;
import cc.mrbird.febs.yb.entity.*;
import cn.hutool.core.io.IoUtil;
import cn.hutool.poi.excel.ExcelUtil;
import cn.hutool.poi.excel.ExcelWriter;
import cn.hutool.poi.excel.StyleSet;
import com.wuwenze.poi.factory.ExcelMappingFactory;
import com.wuwenze.poi.pojo.ExcelMapping;
import com.wuwenze.poi.pojo.ExcelProperty;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.formula.functions.T;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @author lijiang
 * @createDate 2020/8/18
 */
public class ExportExcelUtils {

    public static void exportExcel(HttpServletResponse response, Class<?> clazz, List<?> listData, String filePath, String folderName, String sheetName) throws IOException {
        String guid = UUID.randomUUID().toString();
        filePath += folderName + "/" + guid + ".xlsx";

        ExcelMapping excelMappingData = ExcelMappingFactory.get(clazz);

        int sheetColumnCount = excelMappingData.getPropertyList().size();

        ExcelWriter writer = ExcelUtil.getWriter(filePath, sheetName);

        //合并单元格后的标题行，使用默认标题样式
        //writer.merge(row1.size() - 1, "测试标题1");
        //writer.passCurrentRow();

        List<String> rowHead = new ArrayList<>();
        Map<String, String> headerAliasData = new LinkedHashMap<>();
        for (ExcelProperty item : excelMappingData.getPropertyList()) {
            rowHead.add(item.getColumn());
            headerAliasData.put(item.getName(), item.getColumn());
        }

        int rowCount = listData.size();
        if (rowCount == 0) {
            writer.writeHeadRow(rowHead);
        } else {
            writer.setHeaderAlias(headerAliasData);
            writer.write(listData, true);
        }

        //设置所有列为自动宽度，不考虑合并单元格
        writer.autoSizeColumnAll();

        //标题Row高度
        writer.setRowHeight(0, 25);

        //内容Row高度
        for (int i = 1; i <= rowCount; i++) {
            writer.setRowHeight(i, 20);
        }

        StyleSet style = writer.getStyleSet();
        CellStyle cellStyle = style.getHeadCellStyle();
        Font f1 = writer.createFont();
        f1.setBold(true);
        f1.setFontName("宋体");
        short fontHeight = 280;
        f1.setFontHeight(fontHeight);
        cellStyle.setFont(f1);

        List<org.apache.poi.ss.usermodel.Sheet> sheetList = writer.getSheets();
        for (org.apache.poi.ss.usermodel.Sheet sheet : sheetList) {
            for (int i = 0; i <= sheetColumnCount; i++) {
                sheet.autoSizeColumn(i);
            }
        }
        //response为HttpServletResponse对象
        response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet;charset=utf-8");
        //response.setContentType("application/vnd.ms-excel;charset=utf-8");
        //test.xls是弹出下载对话框的文件名，不能为中文，中文请自行编码
        response.setHeader("Content-Disposition", "attachment;filename=test.xls");
        ServletOutputStream out = response.getOutputStream();

        writer.flush(out, true);
        // 关闭writer，释放内存
        writer.close();
        //此处记得关闭输出Servlet流
        IoUtil.close(out);
    }
}
