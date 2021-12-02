package cc.mrbird.febs.job.task;

import cc.mrbird.febs.cn.webxml.sms.SmsService;
import cc.mrbird.febs.cn.webxml.sms.SmsServicePortType;
import cc.mrbird.febs.com.entity.ComSms;
import cc.mrbird.febs.com.service.IComSmsService;
import cc.mrbird.febs.yb.entity.YbDefaultValue;
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

    public void sendSmsTask(String params) {
        if(params != null) {
//            int areaType = Integer.parseInt(params);
//            iComSmsService.sendSms(ComSms.SENDTYPE_1, areaType);
//            iComSmsService.sendSms(ComSms.SENDTYPE_5, areaType);
//        iComSmsService.sendSms(ComSms.SENDTYPE_6,areaType);
//            iComSmsService.sendSms(ComSms.SENDTYPE_7, areaType);
        }
    }

    public void sendSmsWarnTask(String params) {
        if(params != null && params!=""){
            String[] arr = params.split("\\|");
            if(arr.length == 2) {
                params = arr[0];
                int areaType = Integer.parseInt(arr[1]);
                //写方法
                iComSmsService.sendAppealManageWarnSms(params,areaType);
            }
        }
    }

}
