package cc.mrbird.febs.cn.webxml.sms;

import cc.mrbird.febs.common.utils.OracleDB;
import cc.mrbird.febs.yb.entity.YbDeptHis;
import cn.hutool.core.date.DateUtil;
import lombok.ToString;

import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @author lijiang
 * @createDate 2020/11/3
 */
public class TestSend {
    public static void main(String[] args) throws IOException {
//        String dateStr = "2010-03-11";
//        Date date = DateUtil.parse(dateStr);
//
//        if (date != null) {
//            System.out.println(date);
//        } else {
//            System.out.println("日期错误");
//        }

        List<String> strList = new ArrayList<>();
        strList.add("aaa");
        strList.add("bbb");
        strList.add("aaabbb");
        strList.add("abab");

        strList = strList.stream().filter(s -> s.contains("ab")).collect(Collectors.toList());

        System.out.println(strList);
//        try {
//            SmsService smsService = new SmsService();
//            SmsServicePortType ssp = smsService.getSmsServiceHttpPort();
//            String in0 = "hrp_hr";
//            String in1 = "hrp_hr";
//            String in2 = "MAC";
//            String in3 = "FC";
//            String in4 = "13971658339";
//            String in5 = "test lijiang OK1111";
//            //String sms = ssp.service(in0, in1, in2, in3, in4, in5);
//            //System.out.print(sms);
//
//        } catch (Exception e) {
//            System.out.print(e.getMessage());
//        }
//        Calendar now = Calendar.getInstance();
//        now.add(now.DATE, 1);
////        now.add(now.MINUTE, 2);
//        String fen = "" + now.get(Calendar.MINUTE);
//        String shi = "" + now.get(Calendar.HOUR_OF_DAY);
//        String ri = "" + now.get(Calendar.DAY_OF_MONTH);
//        String yue = "" + (now.get(Calendar.MONTH) + 1);
//        String nian = "" + now.get(Calendar.YEAR);
//
//        String miao = "" + now.get(Calendar.SECOND);
//        String haomiao = "" + now.getTimeInMillis();
//
////        String cron = "0" + " " + fen + " " + shi + " " + ri + " " + yue + " ? " + nian + "-" + nian;
//        String cron = "0 0 0 " + ri + " " + yue + " ? " + nian + "-" + nian;
//        String remark = nian + "年" + yue + "月" + ri + "日" + shi + "时" + fen + "分" + miao + "秒" + haomiao + "毫秒";
//        System.out.println(cron);
//        System.out.println(remark);
//        //0 2 16 12 11 ? 2020-2020

    }
}
