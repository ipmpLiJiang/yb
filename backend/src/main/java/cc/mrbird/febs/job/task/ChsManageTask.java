package cc.mrbird.febs.job.task;

import cc.mrbird.febs.chs.service.IYbChsManageService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class ChsManageTask {
    public ChsManageTask(){

    }
    @Autowired
    private IYbChsManageService iYbChsManageService;

    public void endDate(String params) {
        if(params == null || params.equals("no")){
            params = "";
        } else {
            String[] arr = params.split("\\|");
            if(arr.length == 2) {
                params = arr[0];
                int areaType = Integer.parseInt(arr[1]);
                iYbChsManageService.updateChsApplyEndDate(params,areaType);
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

                iYbChsManageService.updateChsEnableOverdue(params,areaType);
            }
        }
    }
}
