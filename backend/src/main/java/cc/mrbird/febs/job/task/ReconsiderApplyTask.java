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
    IYbReconsiderApplyDataService iYbReconsiderApplyDataService;

    public void applyTask(String params) {
        if(params == null || params.equals("no")){
            params = "";
        } else {
            String[] arr = params.split("\\|");
            if(arr.length == 2) {
                params = arr[0];
                int areaType = Integer.parseInt(arr[1]);

//                "","no","deptNo","getMessage","hisSqlNo","dataNo"
//                hisDataNo,hisMainNo,
                String msg = iYbReconsiderApplyDataService.findReconsiderApplyDataTask(params, areaType,null);
                if(msg.equals("no")){
                    msg = iYbReconsiderApplyDataService.findReconsiderApplyDataNotTask(params, areaType,null);
                }
            }
        }
    }


    public void deptTask(String params) {
        if(params == null || params.equals("no")){
            params = "";
        } else {
            iYbReconsiderApplyDataService.getHisDept();
        }
    }
}
