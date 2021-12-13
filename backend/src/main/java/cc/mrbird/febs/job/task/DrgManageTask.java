package cc.mrbird.febs.job.task;

import cc.mrbird.febs.drg.service.IYbDrgApplyService;
import cc.mrbird.febs.drg.service.IYbDrgManageService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class DrgManageTask {
    public DrgManageTask(){

    }
    @Autowired
    private IYbDrgManageService iYbDrgManageService;

    public void endDate(String params) {
        if(params == null || params.equals("no")){
            params = "";
        } else {
            String[] arr = params.split("\\|");
            if(arr.length == 2) {
                params = arr[0];
                int areaType = Integer.parseInt(arr[1]);
                iYbDrgManageService.updateDrgApplyEndDate(params,areaType);
            }
        }

    }

    public void enableDate(String params) {
        if(params == null || params.equals("no")){
            params = "";
        } else {
            String[] arr = params.split("\\|");
            if(arr.length == 2) {
                params = arr[0];
                int areaType = Integer.parseInt(arr[1]);

                iYbDrgManageService.updateDrgEnableOverdue(params,areaType);
            }
        }
    }
}
