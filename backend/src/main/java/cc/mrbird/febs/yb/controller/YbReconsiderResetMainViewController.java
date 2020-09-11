package cc.mrbird.febs.yb.controller;

import cc.mrbird.febs.common.annotation.Log;
import cc.mrbird.febs.common.controller.BaseController;
import cc.mrbird.febs.common.domain.router.VueRouter;
import cc.mrbird.febs.common.exception.FebsException;
import cc.mrbird.febs.common.domain.QueryRequest;

import cc.mrbird.febs.yb.service.IYbReconsiderResetMainViewService;
import cc.mrbird.febs.yb.entity.YbReconsiderResetMainView;

import cc.mrbird.febs.common.utils.FebsUtil;
import cc.mrbird.febs.system.domain.User;
import com.baomidou.mybatisplus.core.toolkit.StringPool;
import com.wuwenze.poi.ExcelKit;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 *
 * @author viki
 * @since 2020-08-18
 */
@Slf4j
@Validated
@RestController
@RequestMapping("ybReconsiderResetMainView")

public class YbReconsiderResetMainViewController extends BaseController{

private String message;
@Autowired
public IYbReconsiderResetMainViewService iYbReconsiderResetMainViewService;


/**
 * 分页查询数据
 *
 * @param  request 分页信息
 * @param ybReconsiderResetMainView 查询条件
 * @return
 */
@GetMapping
@RequiresPermissions("ybReconsiderResetMainView:view")
public Map<String, Object> List(QueryRequest request, YbReconsiderResetMainView ybReconsiderResetMainView){
        return getDataTable(this.iYbReconsiderResetMainViewService.findYbReconsiderResetMainViews(request, ybReconsiderResetMainView));
        }

/**
 * 添加
 * @param  ybReconsiderResetMainView
 * @return
 */
@Log("新增/按钮")
@PostMapping
@RequiresPermissions("ybReconsiderResetMainView:add")
public void addYbReconsiderResetMainView(@Valid YbReconsiderResetMainView ybReconsiderResetMainView)throws FebsException{
        try{
        User currentUser= FebsUtil.getCurrentUser();
        //ybReconsiderResetMainView.setCreateUserId(currentUser.getUserId());
        this.iYbReconsiderResetMainViewService.createYbReconsiderResetMainView(ybReconsiderResetMainView);
        }catch(Exception e){
        message="新增/按钮失败" ;
        log.error(message,e);
        throw new FebsException(message);
        }
        }

/**
 * 修改
 * @param ybReconsiderResetMainView
 * @return
 */
@Log("修改")
@PutMapping
@RequiresPermissions("ybReconsiderResetMainView:update")
public void updateYbReconsiderResetMainView(@Valid YbReconsiderResetMainView ybReconsiderResetMainView)throws FebsException{
        try{
        User currentUser= FebsUtil.getCurrentUser();
//      ybReconsiderResetMainView.setModifyUserId(currentUser.getUserId());
        this.iYbReconsiderResetMainViewService.updateYbReconsiderResetMainView(ybReconsiderResetMainView);
        }catch(Exception e){
        message="修改失败" ;
        log.error(message,e);
        throw new FebsException(message);
        }
        }


@Log("删除")
@DeleteMapping("/{ids}")
@RequiresPermissions("ybReconsiderResetMainView:delete")
public void deleteYbReconsiderResetMainViews(@NotBlank(message = "{required}") @PathVariable String ids)throws FebsException{
        try{
        String[]arr_ids=ids.split(StringPool.COMMA);
        this.iYbReconsiderResetMainViewService.deleteYbReconsiderResetMainViews(arr_ids);
        }catch(Exception e){
        message="删除失败" ;
        log.error(message,e);
        throw new FebsException(message);
        }
        }
@PostMapping("excel")
@RequiresPermissions("ybReconsiderResetMainView:export")
public void export(QueryRequest request, YbReconsiderResetMainView ybReconsiderResetMainView, HttpServletResponse response) throws FebsException {
        try {
        List<YbReconsiderResetMainView> ybReconsiderResetMainViews = this.iYbReconsiderResetMainViewService.findYbReconsiderResetMainViews(request, ybReconsiderResetMainView).getRecords();
        ExcelKit.$Export(YbReconsiderResetMainView.class, response).downXlsx(ybReconsiderResetMainViews, false);
        } catch (Exception e) {
        message = "导出Excel失败";
        log.error(message, e);
        throw new FebsException(message);
        }
        }

@GetMapping("/{id}")
public YbReconsiderResetMainView detail(@NotBlank(message = "{required}") @PathVariable String id) {
    YbReconsiderResetMainView ybReconsiderResetMainView=this.iYbReconsiderResetMainViewService.getById(id);
        return ybReconsiderResetMainView;
        }
        }