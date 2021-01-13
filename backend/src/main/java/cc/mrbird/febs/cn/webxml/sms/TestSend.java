package cc.mrbird.febs.cn.webxml.sms;

import cc.mrbird.febs.common.utils.OracleDB;
import cc.mrbird.febs.yb.entity.*;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.NumberUtil;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import lombok.ToString;
import net.bytebuddy.implementation.bytecode.Throw;

import java.io.Console;
import java.io.IOException;
import java.io.InputStream;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @author lijiang
 * @createDate 2020/11/3
 */
public class TestSend {
    private static String aa() throws Exception {
        String str = "ddd";
        boolean istr = false;
        try {
            Integer s = Integer.parseInt(str);
            str = s.toString();
        } catch (Exception e) {
            istr = true;
        } finally {
            System.out.println("finally");
        }

        if (istr) {
            throw new Exception("未添加资产,请重新添加");
        }

        return str;
    }

    private static YbAppealConfireJson getAppealConfireJson(String dataJson){
        YbAppealConfireJson appealConfireJson = JSON.parseObject(dataJson, new TypeReference<YbAppealConfireJson>() {
        });

        return  appealConfireJson;
    }

    public static void main(String[] args) throws IOException {

        String dataJson = "{'id':'1','doctorCode':'zhangsan','doctorName':'张三','adminType':1,'comments':'2222','state':0,'isDeletemark':0," +
                "'child':[{'id':'11','pid':'1','deptCode':'111','deptName':'内科'},{'id':'22','pid':'1','deptCode':'222','deptName':'外科'}]}";

//        JSONObject josn = JSONUtil.parseObj(dataJson);
        //id,pid,deptCode,deptName
        YbAppealConfireJson yac = getAppealConfireJson(dataJson);

        dataJson = JSONUtil.toJsonStr(yac);

        System.out.println(dataJson);
        //id,doctorCode,doctorName,adminType,comments,state,isDeletemark,child

//        String dateStr = "2010-03-11";
//        Date date = DateUtil.parse(dateStr);
//
//        if (date != null) {
//            System.out.println(date);
//        } else {
//            System.out.println("日期错误");
//        }
//        try {
//            String ss = aa();
//            System.out.println(ss);
//        }catch (Exception e){
//            System.out.println("1111111111111111");
//        }
//        Calendar cal = Calendar.getInstance();//使用默认时区和语言环境获得一个日历。
//        cal.setTime(new Date());
//        cal.add(Calendar.DATE, -1);//取当前日期的前一天.
//
//        Date d1 = null;
//        Date d2 = null;
        try {
//            d1 = new Date();
//            d2 = cal.getTime();

            double d = (double)9000000 / 3600000;//;new BigDecimal((float)9000000 / 3600000).setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();

            if (d > 2) {
                System.out.println(d);
            } else {
                System.out.println("333");
            }
            //毫秒ms
//            long diff = d1.getTime() - d2.getTime();

//            long diffSeconds = diff / 1000 % 60;
//            long diffMinutes = diff / (60 * 1000) % 60;
//            long diffHours = diff / (60 * 60 * 1000);
//            long diffDays = diff / (24 * 60 * 60 * 1000);


//            System.out.println(d1);
//            System.out.println(d2);
//
//            System.out.println(diff);

//            System.out.println(diffDays + " 天, ");
//            System.out.println(diffHours + " 小时, ");
//            System.out.println(diffMinutes + " 分钟, ");
//            System.out.println(diffSeconds + " 秒.");
        } catch (Exception e) {

        }
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
