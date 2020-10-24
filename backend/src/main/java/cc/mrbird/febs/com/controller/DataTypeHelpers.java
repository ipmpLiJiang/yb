package cc.mrbird.febs.com.controller;

import lombok.val;
import org.apache.commons.lang3.StringUtils;

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
        if(val == null || val.equals("")){
            isTrue= true;
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


    public static String stringSeparate(String str, String val, String sep) {
        if (isNullOrEmpty(str)) {
            str = val;
        } else {
            boolean bl = false;
            String[] strArr = str.split(sep);
            for (String k : strArr){
                if(val.equals(k)){
                    bl = true;
                    break;
                }
            }
            if(!bl) {
                str += sep + val;
            }
        }
        return str;
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

    public static int stringDateFormatMaxDay(String strDate,String format, boolean isFormat){
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

    public static int dateFormatMaxDay(Date date){
        Calendar a = Calendar.getInstance();
        a.setTime(date);
        a.set(Calendar.DATE, 1);//把日期设置为当月第一天
        a.roll(Calendar.DATE, -1);//日期回滚一天，也就是最后一天
        int maxDay = a.get(Calendar.DATE);

        return maxDay;
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
        calendar.add(calendar.DATE, day + 1);
        Date addDate = calendar.getTime();

        return addDate;
    }
}
