package cc.mrbird.febs.export.pdf;

import cc.mrbird.febs.export.domain.ExportAfferentCustomExcel;
import cc.mrbird.febs.yb.entity.YbDept;
import cn.hutool.poi.excel.ExcelUtil;
import cn.hutool.poi.excel.ExcelWriter;
import cn.hutool.poi.excel.StyleSet;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Font;

import java.lang.reflect.Field;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * @author lijiang
 * @createDate 2020/11/5
 */
public class Test {
    public static void main(String[] args) {
        List<YbDept> list = new ArrayList<>();
        for (int i = 1; i < 5; i++) {
            YbDept dept = new YbDept();
            dept.setId(i);
            dept.setDeptId("dept" + i);
            dept.setDeptName("科室" + i);
            dept.setIsDeletemark(1);
            if (i < 3) {
                dept.setCreateTime(new Date());
            }
            dept.setComments("d" + i);
            list.add(dept);
        }

        String dataJson = "[{'title':'科室编码','dataIndex':'deptCode'}," +
                "{'title':'科室名称','dataIndex':'deptName'}," +
                "{'title':'备注','dataIndex':'comments'}]";

        try {
            AA(list, dataJson,"d:/writeMapTest.xlsx");
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

        private static void AA (List<?> list, String dataJson,String fileUrl) throws NoSuchFieldException, IllegalAccessException {
            List<ExportAfferentCustomExcel> exportList = JSON.parseObject(dataJson, new TypeReference<List<ExportAfferentCustomExcel>>() {
            });
            Object fieldValue = null;
            SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
            ArrayList<Map<String, Object>> rows = new ArrayList<>();
            boolean isTrue = false;
            for (Object item : list) {
                Class objClass = item.getClass();
                Map<String, Object> row = new LinkedHashMap<>();
                for (ExportAfferentCustomExcel export : exportList) {
                    isTrue = true;
                    Field field = objClass.getDeclaredField(export.getDataIndex());
                    field.setAccessible(true);
                    fieldValue = field.get(item);
                    // 如果类型是Boolean 是封装类
                    if (field.getGenericType().toString().equals("class java.lang.Boolean")) {
                        fieldValue = (Boolean) field.get(item) == true ? "是" : "否";
                    }

                    // 如果类型是boolean 基本数据类型不一样 这里有点说名如果定义名是 isXXX的 那就全都是isXXX的
                    // 反射找不到getter的具体名
                    if (field.getGenericType().toString().equals("boolean")) {
                        fieldValue = (boolean) field.get(item) == true ? "是" : "否";
                    }
                    // 如果类型是Date
                    if (field.getGenericType().toString().equals("class java.util.Date")) {
                        fieldValue = (Date) field.get(item);
                        if (fieldValue != null) {
                            fieldValue = formatter.format(fieldValue);
                        }
                    }
                    if (fieldValue == null) fieldValue = "";
                    row.put(export.getTitle(), fieldValue.toString());
                }
                if (isTrue) rows.add(row);
                isTrue = false;
            }

            // 通过工具类创建writer
            ExcelWriter writer = ExcelUtil.getWriter(fileUrl);
            // 合并单元格后的标题行，使用默认标题样式
//                writer.merge(rows.size() - 1, "一班成绩单");

            int rowCount = 0;
            int sheetColumnCount = exportList.size();
            // 一次性写出内容，使用默认样式，强制输出标题
            if (list.size() == 0) {
                List<String> rowHead = new ArrayList<>();
                for (ExportAfferentCustomExcel export : exportList) {
                    rowHead.add(export.getTitle());
                }
                writer.writeHeadRow(rowHead);
            } else {
                rowCount = rows.size();
                writer.write(rows, true);
            }
            //设置所有列为自动宽度，不考虑合并单元格
            writer.autoSizeColumnAll();
            //标题Row高度
            writer.setRowHeight(0, 25);

            //内容Row高度 有效 慢
//            for (int i = 1; i <= rowCount; i++) {
//                writer.setRowHeight(i, 20);
//            }

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
            // 关闭writer，释放内存
            writer.close();
        }

    }
