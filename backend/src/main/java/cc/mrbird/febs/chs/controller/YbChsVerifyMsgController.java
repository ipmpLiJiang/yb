package cc.mrbird.febs.chs.controller;

import cc.mrbird.febs.common.annotation.Log;
import cc.mrbird.febs.common.controller.BaseController;
import cc.mrbird.febs.common.domain.FebsResponse;
import cc.mrbird.febs.common.domain.router.VueRouter;
import cc.mrbird.febs.common.exception.FebsException;
import cc.mrbird.febs.common.domain.QueryRequest;

import cc.mrbird.febs.chs.service.IYbChsVerifyMsgService;
import cc.mrbird.febs.chs.entity.YbChsVerifyMsg;

import cc.mrbird.febs.common.utils.FebsUtil;
import cc.mrbird.febs.system.domain.User;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.StringPool;
import com.wuwenze.poi.ExcelKit;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.ModelMap;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import java.util.*;

/**
 * @author viki
 * @since 2022-07-18
 */
@Slf4j
@Validated
@RestController
@RequestMapping("ybChsVerifyMsg")

public class YbChsVerifyMsgController extends BaseController {

    private String message;
    @Autowired
    public IYbChsVerifyMsgService iYbChsVerifyMsgService;


    /**
     * 分页查询数据
     *
     * @param request        分页信息
     * @param ybChsVerifyMsg 查询条件
     * @return
     */
    @GetMapping
    @RequiresPermissions("ybChsVerifyMsg:view")
    public Map<String, Object> List(QueryRequest request, YbChsVerifyMsg ybChsVerifyMsg) {
        return getDataTable(this.iYbChsVerifyMsgService.findYbChsVerifyMsgs(request, ybChsVerifyMsg));
    }

    @GetMapping("getList")
    public FebsResponse getLists(QueryRequest request, YbChsVerifyMsg ybChsVerifyMsg) {
        LambdaQueryWrapper<YbChsVerifyMsg> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(YbChsVerifyMsg::getApplyDateStr,ybChsVerifyMsg.getApplyDateStr());
        wrapper.eq(YbChsVerifyMsg::getAreaType,ybChsVerifyMsg.getAreaType());
        List<YbChsVerifyMsg> backList = iYbChsVerifyMsgService.list(wrapper);
        if(backList.size() > 0) {
            for (YbChsVerifyMsg item : backList) {
                item.setIds(UUID.randomUUID().toString());
            }
            backList.sort(Comparator.comparing(YbChsVerifyMsg::getZymzName).thenComparing(YbChsVerifyMsg::getRuleName));
        }
        return  new FebsResponse().data(backList);
    }

    /**
     * 添加
     *
     * @param ybChsVerifyMsg
     * @return
     */
    @Log("新增/按钮")
    @PostMapping
    @RequiresPermissions("ybChsVerifyMsg:add")
    public void addYbChsVerifyMsg(@Valid YbChsVerifyMsg ybChsVerifyMsg) throws FebsException {
        try {
            User currentUser = FebsUtil.getCurrentUser();
//        ybChsVerifyMsg.setCreateUserId(currentUser.getUserId());
            this.iYbChsVerifyMsgService.createYbChsVerifyMsg(ybChsVerifyMsg);
        } catch (Exception e) {
            message = "新增/按钮失败";
            log.error(message, e);
            throw new FebsException(message);
        }
    }

    /**
     * 修改
     *
     * @param ybChsVerifyMsg
     * @return
     */
    @Log("修改")
    @PutMapping
    @RequiresPermissions("ybChsVerifyMsg:update")
    public void updateYbChsVerifyMsg(@Valid YbChsVerifyMsg ybChsVerifyMsg) throws FebsException {
        try {
            User currentUser = FebsUtil.getCurrentUser();
//            ybChsVerifyMsg.setModifyUserId(currentUser.getUserId());
            this.iYbChsVerifyMsgService.updateYbChsVerifyMsg(ybChsVerifyMsg);
        } catch (Exception e) {
            message = "修改失败";
            log.error(message, e);
            throw new FebsException(message);
        }
    }


    @Log("删除")
    @DeleteMapping("/{ids}")
    @RequiresPermissions("ybChsVerifyMsg:delete")
    public void deleteYbChsVerifyMsgs(@NotBlank(message = "{required}") @PathVariable String ids) throws FebsException {
        try {
            String[] arr_ids = ids.split(StringPool.COMMA);
            this.iYbChsVerifyMsgService.deleteYbChsVerifyMsgs(arr_ids);
        } catch (Exception e) {
            message = "删除失败";
            log.error(message, e);
            throw new FebsException(message);
        }
    }

    @PostMapping("excel")
    @RequiresPermissions("ybChsVerifyMsg:export")
    public void export(QueryRequest request, YbChsVerifyMsg ybChsVerifyMsg, HttpServletResponse response) throws FebsException {
        try {
            List<YbChsVerifyMsg> ybChsVerifyMsgs = this.iYbChsVerifyMsgService.findYbChsVerifyMsgs(request, ybChsVerifyMsg).getRecords();
            ExcelKit.$Export(YbChsVerifyMsg.class, response).downXlsx(ybChsVerifyMsgs, false);
        } catch (Exception e) {
            message = "导出Excel失败";
            log.error(message, e);
            throw new FebsException(message);
        }
    }

    @GetMapping("/{id}")
    public YbChsVerifyMsg detail(@NotBlank(message = "{required}") @PathVariable String id) {
        YbChsVerifyMsg ybChsVerifyMsg = this.iYbChsVerifyMsgService.getById(id);
        return ybChsVerifyMsg;
    }
}