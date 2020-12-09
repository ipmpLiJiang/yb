package cc.mrbird.febs.job.task;

import cc.mrbird.febs.cn.webxml.sms.SmsService;
import cc.mrbird.febs.cn.webxml.sms.SmsServicePortType;
import cc.mrbird.febs.com.service.IComSmsService;
import cc.mrbird.febs.yb.service.IYbReconsiderApplyDataService;
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
    public SmsTask() {
    }

    @Autowired
    IComSmsService iComSmsService;

    public void sendSmsTask(){
        iComSmsService.sendSms();
    }

}
