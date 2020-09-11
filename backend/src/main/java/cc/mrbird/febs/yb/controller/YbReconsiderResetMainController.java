package cc.mrbird.febs.yb.controller;

import cc.mrbird.febs.common.annotation.Log;
import cc.mrbird.febs.common.controller.BaseController;
import cc.mrbird.febs.common.domain.router.VueRouter;
import cc.mrbird.febs.common.exception.FebsException;
import cc.mrbird.febs.common.domain.QueryRequest;

import cc.mrbird.febs.yb.service.IYbReconsiderResetMainService;
import cc.mrbird.febs.yb.entity.YbReconsiderResetMain;

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
 * @since 2020-08-17
 */
@Slf4j
@Validated
@RestController
@RequestMapping("ybReconsiderResetMain")

public class YbReconsiderResetMainController extends BaseController{

private String message;
@Autowired
public IYbReconsiderResetMainService iYbReconsiderResetMainService;


/**
 * 分页查询数据
 *
 * @param  request 分页信息
 * @param ybReconsiderResetMain 查询条件
 * @return
 */
@GetMapping
@RequiresPermissions("ybReconsiderResetMain:view")
public Map<String, Object> List(QueryRequest request, YbReconsiderResetMain ybReconsiderResetMain){
        return getDataTable(this.iYbReconsiderResetMainService.findYbReconsiderResetMains(request, ybReconsiderResetMain));
        }

/**
 * 添加
 * @param  ybReconsiderResetMain
 * @return
 */
@Log("新增/按钮")
@PostMapping
@RequiresPermissions("ybReconsiderResetMain:add")
public void addYbReconsiderResetMain(@Valid YbReconsiderResetMain ybReconsiderResetMain)throws FebsException{
        try{
        User currentUser= FebsUtil.getCurrentUser();
        ybReconsiderResetMain.setCreateUserId(currentUser.getUserId());
        this.iYbReconsiderResetMainService.createYbReconsiderResetMain(ybReconsiderResetMain);
        }catch(Exception e){
        message="新增/按钮失败" ;
        log.error(message,e);
        throw new FebsException(message);
        }
        }

/**
 * 修改
 * @param ybReconsiderResetMain
 * @return
 */
@Log("修改")
@PutMapping
@RequiresPermissions("ybReconsiderResetMain:update")
public void updateYbReconsiderResetMain(@Valid YbReconsiderResetMain ybReconsiderResetMain)throws FebsException{
        try{
        User currentUser= FebsUtil.getCurrentUser();
      ybReconsiderResetMain.setModifyUserId(currentUser.getUserId());
        this.iYbReconsiderResetMainService.updateYbReconsiderResetMain(ybReconsiderResetMain);
        }catch(Exception e){
        message="修改失败" ;
        log.error(message,e);
        throw new FebsException(message);
        }
        }


@Log("删除")
@DeleteMapping("/{ids}")
@RequiresPermissions("ybReconsiderResetMain:delete")
public void deleteYbReconsiderResetMains(@NotBlank(message = "{required}") @PathVariable String ids)throws FebsException{
        try{
        String[]arr_ids=ids.split(StringPool.COMMA);
        this.iYbReconsiderResetMainService.deleteYbReconsiderResetMains(arr_ids);
        }catch(Exception e){
        message="删除失败" ;
        log.error(message,e);
        throw new FebsException(message);
        }
        }
@PostMapping("excel")
@RequiresPermissions("ybReconsiderResetMain:export")
public void export(QueryRequest request, YbReconsiderResetMain ybReconsiderResetMain, HttpServletResponse response) throws FebsException {
        try {
        List<YbReconsiderResetMain> ybReconsiderResetMains = this.iYbReconsiderResetMainService.findYbReconsiderResetMains(request, ybReconsiderResetMain).getRecords();
        ExcelKit.$Export(YbReconsiderResetMain.class, response).downXlsx(ybReconsiderResetMains, false);
        } catch (Exception e) {
        message = "导出Excel失败";
        log.error(message, e);
        throw new FebsException(message);
        }
        }

@GetMapping("/{id}")
public YbReconsiderResetMain detail(@NotBlank(message = "{required}") @PathVariable String id) {
    YbReconsiderResetMain ybReconsiderResetMain=this.iYbReconsiderResetMainService.getById(id);
        return ybReconsiderResetMain;
        }
        }