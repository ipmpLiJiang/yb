package cc.mrbird.febs.job.task;

import cc.mrbird.febs.yb.entity.YbDefaultValue;
import cc.mrbird.febs.yb.service.IYbReconsiderApplyDataService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * @author lijiang
 * @createDate 2020/11/13
 */
@Slf4j
@Component
public class ReconsiderApplyTask {
    public  ReconsiderApplyTask(){

    }
    @Autowired
    IYbReconsiderApplyDataService iYbReconsiderApplyService;

    public void applyTask(String params) {
        if(params == null || params.equals("no")){
            params = "";
        }
        iYbReconsiderApplyService.findReconsiderApplyDataTask(params, YbDefaultValue.AREATYPE_0);
    }

    public void applyTaskWest(String params) {
        if(params == null || params.equals("no")){
            params = "";
        }
        iYbReconsiderApplyService.findReconsiderApplyDataTask(params, YbDefaultValue.AREATYPE_1);
    }

    public void applyTaskOther(String params) {
        if(params == null || params.equals("no")){
            params = "";
        }
        iYbReconsiderApplyService.findReconsiderApplyDataTask(params, YbDefaultValue.AREATYPE_2);
    }

}
