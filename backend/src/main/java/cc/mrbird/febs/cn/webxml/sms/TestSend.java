package cc.mrbird.febs.cn.webxml.sms;


import java.util.List;

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

    public static void main(String[] args) {
        String aa = "";
        String bb = "";
        if(aa == bb){
            System.out.println("OK");
        } else {
            System.out.println("NO");
        }
//        testXc c = new testXc();
//        new Thread(c).start();
//        new Thread(c).start();
//        new Thread(c).start();
//        String code = "990050-202105-0-00000007-002.jp333e";
//        int d = code.lastIndexOf(".");
//
//        int c = code.length() - d;
//        int strNum = code.lastIndexOf("-");
//        String comFileName = code.substring(strNum + 1, code.length() - c);
//        int num = Integer.parseInt(comFileName) + 1;
//        System.out.println(num);
//        List<OutComArea> outAreaList = new ArrayList<>();
//        List<OutComArea> queryList = new ArrayList<>();
//
//        for (int i = 1;i<=11;i++ ) {
//            OutComArea area = new OutComArea();
//            area.setAreaType(i);
//            area.setAreaName("1"+ i);
//            outAreaList.add(area);
//        }
//        //页数
//        int pageSize = 20;
//        //总页数
//        int totalPage = 0;
//        int totalRow = outAreaList.size();
//        //第一次进入时会进行判断，设置默认值，当查询总数小于页数时，页数等于 查询总数， 总页数等于1
//        if (totalRow <= pageSize) {
//            pageSize = totalRow;
//            totalPage = 1;
//        } else {
//            totalPage = (totalRow + pageSize - 1) / pageSize;
//        }
//        long current = 0;
//        for(int i= 1; i <= totalPage;i++) {
//            current = i == 1 ? 0 : (i - 1) * pageSize;
//            queryList = outAreaList.stream().sorted(Comparator.comparing(OutComArea::getAreaType)).skip(current).limit(pageSize).collect(Collectors.toList());
//            System.out.println(queryList);
//        }
//
//        List<OutComArea> query = outAreaList.stream().filter(s-> s.getAreaName().contains("122")).limit(3).collect(Collectors.toList());
//        System.out.println(query.size());
//        System.out.println(query);

//        String dataJson = "{'id':'1','doctorCode':'zhangsan','doctorName':'张三','adminType':1,'comments':'2222','state':0,'isDeletemark':0," +
//                "'child':[{'id':'11','pid':'1','deptCode':'111','deptName':'内科'},{'id':'22','pid':'1','deptCode':'222','deptName':'外科'}]}";

//        JSONObject josn = JSONUtil.parseObj(dataJson);
        //id,pid,deptCode,deptName
//        YbAppealConfireJson yac = getAppealConfireJson(dataJson);
//
//        dataJson = JSONUtil.toJsonStr(yac);
//
//        System.out.println(dataJson);
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

//            double d = (double)9000000 / 3600000;//;new BigDecimal((float)9000000 / 3600000).setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();
//
//            if (d > 2) {
//                System.out.println(d);
//            } else {
//                System.out.println("333");
//            }
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
