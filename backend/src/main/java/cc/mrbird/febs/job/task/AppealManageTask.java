package cc.mrbird.febs.job.task;

import cc.mrbird.febs.yb.service.IYbReconsiderApplyService;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.jdbc.Null;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class AppealManageTask {
    public  AppealManageTask(){

    }
    @Autowired
    private IYbReconsiderApplyService iYbReconsiderApplyService;

    public void oneDate(String params) {
        if(params == null || params.equals("no")){
            params = "";
        }
        iYbReconsiderApplyService.updateApplyEndDateOne(params);
    }

    public void twoDate(String params) {
        if(params == null || params.equals("no")){
            params = "";
        }
        iYbReconsiderApplyService.updateApplyEndDateTwo(params);
    }

    public void enableDate(String params) {
        if(params == null || params.equals("no")){
            params = "";
        }
        iYbReconsiderApplyService.updateEnableOverdue(params);
    }
}
