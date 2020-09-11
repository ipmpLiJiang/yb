package cc.mrbird.febs.com.controller;

import cc.mrbird.febs.common.exception.FebsException;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFDateUtil;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * @author lijiang
 * @createDate 2020/8/18
 */
public class ImportExcelUtils {
    /**
     * 根据Sheet页导入Excel信息
     *
     * @param file   文件
     * @param sheetIndex Sheet页下标
     * @param startRow   开始列 ：默认第一列
     * @param startLine  开始行 ：默认第一行
     * @throws Exception
     */
    public static final List<Object[]> importExcelBySheetIndex(File file, int sheetIndex
            , int startRow, int startLine) throws Exception {

        List<Object[]> resultList = null;
                //创建WorkBook对象
        Workbook wb = null;

        String fileName = file.getName();// 读取上传文件(excel)的名字，含后缀后
        if (fileName.endsWith("xls")) {
            wb = new HSSFWorkbook(new FileInputStream(file));
        } else if (fileName.endsWith("xlsx")) {
            wb = new XSSFWorkbook(new FileInputStream(file));
        }

        // 获取Sheet
        Sheet sheet = ImportExcelUtils.getSheet(wb, sheetIndex);

        // 判断Sheet是否为空
        if (sheet != null) {

            // 遍历Sheet
            List<Object[]> list = ImportExcelUtils.listFromSheet(sheet);
            if (list != null && list.size() > 0) {
                resultList = new ArrayList<Object[]>();
                if (startLine <= list.size()) {
                    for (int i = startLine; i < list.size(); i++) {
                        int nullCount = 0;
                        Object[] rows = list.get(i);
                        if (rows != null && rows.length > 0) {
                            List<Object> resultObjects = new ArrayList<Object>();
                            for (int n = startRow; n < rows.length; n++) {
                                if (IsNullUtils.isEmpty(rows[n])) {
                                    nullCount++;
                                }
                                resultObjects.add(rows[n]);
                            }

                            //判断空的单元格个数
                            if (nullCount >= rows.length) {
                                break;
                            } else {
                                resultList.add(resultObjects.toArray());
                            }
                        }
                    }
                }
            }
        }
        wb.close();
        return resultList;
    }

    public static Map<Integer, String> getSheelNames(File file) throws Exception {
        Map<Integer, String> result = new HashMap<>();
        Workbook wb = null;
        Iterator<Sheet> sheets = null;
        String fileName = file.getName();// 读取上传文件(excel)的名字，含后缀后

        if (fileName.endsWith("xls")) {
            wb = new HSSFWorkbook(new FileInputStream(file));
            sheets = wb.iterator();
        } else if (fileName.endsWith("xlsx")) {
            wb = new XSSFWorkbook(new FileInputStream(file));
            sheets = wb.iterator();
        }
        if (sheets == null) {
            throw new Exception("excel中不含有sheet工作表");
        }
        int i = 0;
        while (sheets.hasNext()) {
            Sheet sheet = sheets.next();
            result.put(i,sheet.getSheetName());
            i++;
        }
        wb.close();
        return result;
    }

    /**
     * 获取Sheet页面(按页标)
     *
     * @param wb
     * @param index
     * @return
     */
    public static final Sheet getSheet(Workbook wb, int index) {
        return wb.getSheetAt(index);
    }

    /**
     * 获取Sheet页内容
     *
     * @param sheet
     * @return
     */
    public static final List<Object[]> listFromSheet(Sheet sheet) {

        List<Object[]> list = new ArrayList<Object[]>();
        for (int r = sheet.getFirstRowNum(); r <= sheet.getLastRowNum(); r++) {
            Row row = sheet.getRow(r);
            if (row == null || row.getPhysicalNumberOfCells() == 0) continue;
            Object[] cells = new Object[row.getLastCellNum()];
            for (int c = row.getFirstCellNum(); c <= row.getLastCellNum(); c++) {
                Cell cell = row.getCell(c);
                if (cell == null) continue;
                //判断是否为日期类型
//                if (DateUtil.isCellDateFormatted(cell)) {
//                    //用于转化为日期格式
//                    Date d = cell.getDateCellValue();
//                    DateFormat formater = new SimpleDateFormat("yyyy-MM-dd");
//                    cells[c] = formater.format(d);
//                } else {
                    cells[c] = getValueFromCell(cell);
//                }


            }
            list.add(cells);
        }
        return list;
    }

    /**
     * 获取单元格内信息
     *
     * @param cell
     * @return
     */
    public static final Object getValueFromCell(Cell cell) {
        if (cell == null) {
            System.out.println("Cell is null !!!");
            return null;
        }
        Object result = null;
        if (cell instanceof HSSFCell) {
            if (cell != null) {
                // 单元格类型：Numeric:0,String:1,Formula:2,Blank:3,Boolean:4,Error:5
                int cellType = ((HSSFCell) cell).getCellType();
                switch (cellType) {
                    case HSSFCell.CELL_TYPE_STRING:
                        result = ((HSSFCell) cell).getStringCellValue();
                        break;
                    case HSSFCell.CELL_TYPE_NUMERIC:
                        DecimalFormat df = new DecimalFormat("###.####");
                        result = df.format(((HSSFCell) cell).getNumericCellValue());
                        break;
                    case HSSFCell.CELL_TYPE_FORMULA:
                        result = ((HSSFCell) cell).getNumericCellValue();
                        break;
                    case HSSFCell.CELL_TYPE_BOOLEAN:
                        result = ((HSSFCell) cell).getBooleanCellValue();
                        break;
                    case HSSFCell.CELL_TYPE_BLANK:
                        result = null;
                        break;
                    case HSSFCell.CELL_TYPE_ERROR:
                        result = null;
                        break;
                    default:
                        System.out.println("枚举了所有类型");
                        break;
                }
            }
        } else if (cell instanceof XSSFCell) {
            if (cell != null) {
                // 单元格类型：Numeric:0,String:1,Formula:2,Blank:3,Boolean:4,Error:5
                int cellType = ((XSSFCell) cell).getCellType();
                switch (cellType) {
                    case XSSFCell.CELL_TYPE_STRING:
                        result = ((XSSFCell) cell).getRichStringCellValue().getString();
                        break;
                    case XSSFCell.CELL_TYPE_NUMERIC:
                        DecimalFormat df = new DecimalFormat("###.####");
                        result = df.format(((XSSFCell) cell).getNumericCellValue());
                        break;
                    case XSSFCell.CELL_TYPE_FORMULA:
                        result = ((XSSFCell) cell).getNumericCellValue();
                        break;
                    case XSSFCell.CELL_TYPE_BOOLEAN:
                        result = ((XSSFCell) cell).getBooleanCellValue();
                        break;
                    case XSSFCell.CELL_TYPE_BLANK:
                        result = null;
                        break;
                    case XSSFCell.CELL_TYPE_ERROR:
                        result = null;
                        break;
                    default:
                        System.out.println("枚举了所有类型");
                        break;
                }
            }
        }
        return result;
    }
}
