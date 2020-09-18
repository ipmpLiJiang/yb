package cc.mrbird.febs.com.controller;

import lombok.val;
import org.apache.commons.lang3.StringUtils;

import java.math.BigDecimal;
import java.text.ParsePosition;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;
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
        }
        if (!isFormat) {
            strDate = strDate.replace("-", "").replace("/", "");
        }
        SimpleDateFormat sdf = new SimpleDateFormat(format);
        Date date = sdf.parse(strDate, new ParsePosition(0));

        return date;
    }

    public static String importTernaryOperate(Object[] obj, int nThis) {
        if (obj.length >= nThis + 1) {
            return obj[nThis] != null ? obj[nThis].toString().trim() : "";
        } else {
            return "";
        }
    }

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
