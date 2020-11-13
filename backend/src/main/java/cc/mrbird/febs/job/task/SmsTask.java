package cc.mrbird.febs.job.task;

import cc.mrbird.febs.cn.webxml.sms.SmsService;
import cc.mrbird.febs.cn.webxml.sms.SmsServicePortType;
import cc.mrbird.febs.com.service.IComSmsService;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Date;

/**
 * @author lijiang
 * @createDate 2020/11/12
 */
@Slf4j
@Component
public class SmsTask {
    @Autowired
    IComSmsService iComSmsService;
    public SmsTask() {
    }

    public void smsTestTask() {
//        try {
//            Date date = new Date();
//            SmsService smsService = new SmsService();
//            SmsServicePortType ssp = smsService.getSmsServiceHttpPort();
//            String in0 = "hrp_hr";
//            String in1 = "hrp_hr";
//            String in2 = "MAC";
//            String in3 = "FC";
//            String in4 = "13971658339";
//            String in5 = "test lijiang" + date.toString();
//            String sms = ssp.service(in0, in1, in2, in3, in4, in5);
//            System.out.print(sms);
//
//        } catch (Exception e) {
//            System.out.print(e.getMessage());
//        }
        Date date = new Date();
        System.out.print("sms" + date.toString());

        iComSmsService.sendSms();
    }
}
