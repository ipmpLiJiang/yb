package cc.mrbird.febs.cn.webxml.sms;

/**
 * @author lijiang
 * @createDate 2020/11/3
 */
public class TestSend {
    public static void main(String[] args) {
        try {
            SmsService smsService = new SmsService();
            SmsServicePortType ssp = smsService.getSmsServiceHttpPort();
            String in0 = "hrp_hr";
            String in1 = "hrp_hr";
            String in2 = "MAC";
            String in3 = "FC";
            String in4 = "13971658339";
            String in5 = "test lijiang OK1111";
            //String sms = ssp.service(in0, in1, in2, in3, in4, in5);
            //System.out.print(sms);

        } catch (Exception e) {
            System.out.print(e.getMessage());
        }
    }
}
