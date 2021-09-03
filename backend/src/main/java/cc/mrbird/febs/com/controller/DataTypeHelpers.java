package cc.mrbird.febs.com.controller;

import cn.hutool.core.date.DateUtil;
import lombok.val;
import org.apache.commons.lang3.StringUtils;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.math.BigDecimal;
import java.text.ParsePosition;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author lijiang
 * @createDate 2020/8/19
 */
public class DataTypeHelpers {
    public static boolean isNumeric(String strNum) {
        if (strNum == null || "".equals(strNum)) {
            return false;
        }
        try {
            double d = Double.parseDouble(strNum);
        } catch (NumberFormatException nfe) {
            return false;
        }
        return true;
    }

    /***
     * 判断 String 是否int
     *
     * @param input
     * @return
     */
    public static boolean isInteger(String input) {
        Matcher mer = Pattern.compile("^[0-9]+$").matcher(input);
        return mer.find();
    }

    public static boolean isNullOrEmpty(String val) {
        boolean isTrue = false;
        if (val == null || val.equals("")) {
            isTrue = true;
        }
        return isTrue;
    }

    /***
     * 判断 String 是否是 int
     *
     * @param input
     * @return
     */
    public static boolean isIntegerPlus(String input) {
        Matcher mer = Pattern.compile("^[+-]?[0-9]+$").matcher(input);
        return mer.find();
    }

    public static String stringReplaceSetString(String str, String strReplace) {
        if (str != null && strReplace != null) {
            if (str.contains(strReplace)) {
                str = str.replace(strReplace, "");
            }
        }
        return str;
    }

    public static String stringSeparate(String str, String val, String sep) {
        if (isNullOrEmpty(str)) {
            str = val;
        } else {
            boolean bl = false;
            String[] strArr = str.split(sep);
            for (String k : strArr) {
                if (val.equals(k)) {
                    bl = true;
                    break;
                }
            }
            if (!bl) {
                str += sep + val;
            }
        }
        return str;
    }

    public static String stringDate6Chang7(String strDate, String sep) {
        if (strDate.length() == 6) {
            if (sep.equals("")) {
                sep = "-";
            }
            String str1 = strDate.substring(0, 4);
            String str2 = strDate.substring(4, strDate.length());
            return str1 + sep + str2;
        } else {
            return "";
        }
    }

    public static String stringDate7Chang6(String strDate) {
        if (strDate.length() == 7) {
            strDate = strDate.replace("-", "").replace("/", "");
            return strDate;
        } else {
            return "";
        }
    }

    public static String stringArraySeparate(String[] arrStr, String sep) {
        String str = "";
        for (String val : arrStr) {
            if (isNullOrEmpty(str)) {
                str = val;
            } else {
                str += sep + val;
            }
        }
        return str;
    }

    public static String stringListSeparate(List<String> listStr, String sep) {
        String str = "";
        for (String val : listStr) {
            if (isNullOrEmpty(str)) {
                str = val;
            } else {
                str += sep + val;
            }
        }
        return str;
    }

    public static Date stringDateFormat(String strDate, String format, boolean isFormat) {
        if (isNullOrEmpty(format)) {
            format = "yyyyMMdd";
            isFormat = false;
        }
        if (!isFormat) {
            strDate = strDate.replace("-", "").replace("/", "");
        }
        SimpleDateFormat sdf = new SimpleDateFormat(format);
        Date date = sdf.parse(strDate, new ParsePosition(0));

        return date;
    }

    public static boolean isValidDate(String strDate, String format, boolean isFormat) {
        if (isNullOrEmpty(format)) {
            format = "yyyyMMdd";
            isFormat = false;
        }
        if (!isFormat) {
            strDate = strDate.replace("-", "").replace("/", "");
        }
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(format);
        try {
            // 设置lenient为false. 否则SimpleDateFormat会比较宽松地验证日期，比如2018-02-29会被接受，并转换成2018-03-01

            simpleDateFormat.setLenient(false);
            Date date = simpleDateFormat.parse(strDate);

            //判断传入的yyyy年-MM月-dd日 字符串是否为数字
            String[] sArray = strDate.split("-");
            for (String s : sArray) {
                boolean isNum = s.matches("[0-9]+");
                //+表示1个或多个（如"3"或"225"），*表示0个或多个（[0-9]*）（如""或"1"或"22"），?表示0个或1个([0-9]?)(如""或"7")
                if (!isNum) {
                    return false;
                }
            }
        } catch (Exception e) {
            // e.printStackTrace();
            // 如果throw java.text.ParseException或者NullPointerException，就说明格式不对
            return false;
        }

        return true;
    }


    public static String getUpNianYue() {
        Date date = new Date();//获取当前时间
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.MONTH, -1);
        calendar.set(Calendar.DAY_OF_MONTH, 1);
        Date getDate = calendar.getTime();
        SimpleDateFormat f = new SimpleDateFormat("yyyy-MM");
        String ny = f.format(getDate);
        return ny;
    }


    public static int stringDateFormatMaxDay(String strDate, String format, boolean isFormat) {
        if (isNullOrEmpty(format)) {
            format = "yyyyMMdd";
            isFormat = false;
        }
        if (!isFormat) {
            strDate = strDate.replace("-", "").replace("/", "");
        }
        SimpleDateFormat sdf = new SimpleDateFormat(format);
        Date date = sdf.parse(strDate, new ParsePosition(0));

        Calendar a = Calendar.getInstance();
        a.setTime(date);
        a.set(Calendar.DATE, 1);//把日期设置为当月第一天
        a.roll(Calendar.DATE, -1);//日期回滚一天，也就是最后一天
        int maxDay = a.get(Calendar.DATE);

        return maxDay;
    }

    public static String stringDateFormatAddMonth(int addMonth, String strDate, String format, boolean isFormat) {
        if (isNullOrEmpty(format)) {
            format = "yyyyMMdd";
            isFormat = false;
        }
        if (!isFormat) {
            strDate = strDate.replace("-", "").replace("/", "");
        }
        SimpleDateFormat sdf = new SimpleDateFormat(format);
        Date date = sdf.parse(strDate, new ParsePosition(0));

        Calendar a = Calendar.getInstance();
        a.setTime(date);
        a.add(Calendar.MONTH, addMonth);
        Date dt1 = a.getTime();
        SimpleDateFormat sdf1 = new SimpleDateFormat("yyyy-MM-dd");
        String reStr = sdf1.format(dt1);
        return reStr;
    }


    public static int dateFormatMaxDay(Date date) {
        Calendar a = Calendar.getInstance();
        a.setTime(date);
        a.set(Calendar.DATE, 1);//把日期设置为当月第一天
        a.roll(Calendar.DATE, -1);//日期回滚一天，也就是最后一天
        int maxDay = a.get(Calendar.DATE);

        return maxDay;
    }

    public static Date stringToDate(String strDate) {
        Date date = null;
        try {
            date = DateUtil.parse(strDate);
        } catch (Exception e) {
        }
        return date;
    }

    public static List<String> stringApplyDateStrToList(String applyDateStr, String applyDateStrTo) {
        List<String> list = new ArrayList<>();
        if (applyDateStr.equals(applyDateStrTo)) {
            list.add(applyDateStr);
        } else {
            int nian = Integer.parseInt(applyDateStr.substring(0, 4));
            int yue = Integer.parseInt(applyDateStr.substring(5, applyDateStr.length()));
            int nianTo = Integer.parseInt(applyDateStrTo.substring(0, 4));
            int yueTo = Integer.parseInt(applyDateStrTo.substring(5, applyDateStrTo.length()));
            int result = -1;

            if (nianTo >= nian) {
                if (nianTo == nian) {
                    if (yueTo > yue) {
                        result = yueTo - yue;
                    }
                } else {
                    int nianResult = (nianTo - nian) * 12;
                    result = nianResult + yueTo - yue;
                }
                try {
                    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM");
                    Date date = sdf.parse(applyDateStr);
                    Calendar cl = Calendar.getInstance();
                    cl.setTime(date);
                    for (int i = 1; i <= result; i++) {
                        cl.add(Calendar.MONTH, 1);
                        date = cl.getTime();
                        list.add(sdf.format(date));
                    }

                    if (result > 0) {
                        list.add(applyDateStr);
                    }
                } catch (Exception e) {
                }
            }
        }
        return list;
    }

    //导入Excel专用
    public static String importTernaryOperate(Object[] obj, int nThis) {
        if (obj.length >= nThis + 1) {
            return obj[nThis] != null ? obj[nThis].toString().trim() : "";
        } else {
            return "";
        }
    }

    //导入Excel专用
    public static String stringTernaryOperate(Object obj) {
        return obj != null ? obj.toString().trim() : "";
    }

    public static Date addDateMethod(Date date, int day) {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
        String today = simpleDateFormat.format(date);
        ParsePosition pos = new ParsePosition(0);
        Calendar calendar = new GregorianCalendar();
        Date newDate = simpleDateFormat.parse(today, pos);
        calendar.setTime(newDate);
        calendar.add(calendar.DATE, day);
        Date addDate = calendar.getTime();

        return addDate;
    }

    public static File multipartFileToFile(@RequestParam MultipartFile file) throws Exception {

        File toFile = null;
        if (file.equals("") || file.getSize() <= 0) {
            file = null;
        } else {
            InputStream ins = null;
            ins = file.getInputStream();
            toFile = new File(file.getOriginalFilename());
            inputStreamToFile(ins, toFile);
            ins.close();
        }
        return toFile;
    }

    public static void inputStreamToFile(InputStream ins, File file) {
        try {
            OutputStream os = new FileOutputStream(file);
            int bytesRead = 0;
            byte[] buffer = new byte[8192];
            while ((bytesRead = ins.read(buffer, 0, 8192)) != -1) {
                os.write(buffer, 0, bytesRead);
            }
            os.close();
            ins.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static boolean deleteFile(String sPath) {
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
}
