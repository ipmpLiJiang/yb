package cc.mrbird.febs.export.pdf;

import cc.mrbird.febs.export.domain.ExportAfferentCustomExcel;
import cc.mrbird.febs.yb.entity.YbDept;
import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.collection.ListUtil;
import cn.hutool.core.convert.Convert;
import cn.hutool.core.date.DateUnit;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.img.Img;
import cn.hutool.core.img.ImgUtil;
import cn.hutool.core.io.FileUtil;
import cn.hutool.core.io.unit.DataUnit;
import cn.hutool.core.lang.tree.TreeNode;
import cn.hutool.core.util.ArrayUtil;
import cn.hutool.core.util.RandomUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.poi.excel.ExcelUtil;
import cn.hutool.poi.excel.ExcelWriter;
import cn.hutool.poi.excel.StyleSet;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import net.sourceforge.pinyin4j.PinyinHelper;
import net.sourceforge.pinyin4j.format.HanyuPinyinCaseType;
import net.sourceforge.pinyin4j.format.HanyuPinyinOutputFormat;
import net.sourceforge.pinyin4j.format.HanyuPinyinToneType;
import net.sourceforge.pinyin4j.format.HanyuPinyinVCharType;
import net.sourceforge.pinyin4j.format.exception.BadHanyuPinyinOutputFormatCombination;
import org.apache.commons.lang3.StringUtils;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Font;
import org.aspectj.apache.bcel.generic.Type;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.File;
import java.lang.reflect.Field;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author lijiang
 * @createDate 2020/11/5
 */
public class Test {
    public static void main(String[] args) throws BadHanyuPinyinOutputFormatCombination {
        HanyuPinyinOutputFormat format = new HanyuPinyinOutputFormat();
        //拼音小写
        format.setCaseType(HanyuPinyinCaseType.LOWERCASE);
        //不带声调
        format.setToneType(HanyuPinyinToneType.WITHOUT_TONE);
//        format.setVCharType(HanyuPinyinVCharType.WITH_V);
        //要转换的中文，格式，转换之后的拼音的分隔符，遇到不能转换的是否保留   wo,shi,zhong,guo,ren,，hello
//        System.out.println(PinyinHelper.toHanYuPinyinString("我是中国人，hello", format, ",", true));
        String str = "我是中国人";
        char[] array = str.toCharArray();
        if(array[0] >128) {
            String[] arr = PinyinHelper.toHanyuPinyinStringArray(array[0], format);
            System.out.println(arr);
        } else {
            System.out.println("no");
        }
        //        List<YbDept> list = new ArrayList<>();
//        Random r= new Random();
//        for (int i = 1; i < 10; i++) {
//            YbDept dept = new YbDept();
//            dept.setId(r.nextInt(20));
//            dept.setDeptId("dept" + i);
//            dept.setDeptName("科室" + i);
//            dept.setIsDeletemark(1);
//            if (i < 3) {
//                dept.setCreateTime(new Date());
//            }
//            dept.setComments("d" + i);
//            list.add(dept);
//        }
//
//
//        String dataJson = "[{'title':'科室编码','dataIndex':'deptCode'}," +
//                "{'title':'科室名称','dataIndex':'deptName'}," +
//                "{'title':'备注','dataIndex':'comments'}]";

        try {
//            AA(list, dataJson,"d:/writeMapTest.xlsx");

//            String[] keys = {"a", "b", "c"};
//            Integer[] values = {1,2,3};
//            Map<Integer,String> map = ArrayUtil.zip(values,keys);
//            for (int s : values) {
//                System.out.println(map.get(s));
//            }

//            ImgUtil.scale(
//                FileUtil.file("C:/Users/dajiang/Desktop/img/1.jpg"),
//                FileUtil.file("C:/Users/dajiang/Desktop/img/1_result.jpg"),
//                0.5f
//            );

//            File imgFile = FileUtil.file("C:/Users/dajiang/Desktop/img/1.jpg");

//            BufferedImage a = ImgUtil.read(imgFile);
//            int width = a.getWidth();
//            int height = a.getHeight();
//            ImgUtil.cut(
//                    imgFile,
//                FileUtil.file("C:/Users/dajiang/Desktop/img/1_result1.jpg"),
//                new Rectangle(50, 50, 448, 448)//裁剪的矩形区域
//            );

//            Img.from(FileUtil.file("C:/Users/dajiang/Desktop/img/234.jpg"))
//                    .setQuality(0.2)//压缩比率
//                    .write(FileUtil.file("C:/Users/dajiang/Desktop/img/234_target.jpg"));

        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    private static void AA(List<?> list, String dataJson, String fileUrl) throws NoSuchFieldException, IllegalAccessException {
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
