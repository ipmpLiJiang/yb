package cc.mrbird.febs.yb.controller;

import cc.mrbird.febs.common.annotation.Log;
import cc.mrbird.febs.common.controller.BaseController;
import cc.mrbird.febs.common.domain.FebsResponse;
import cc.mrbird.febs.common.domain.QueryRequest;
import cc.mrbird.febs.common.exception.FebsException;
import cc.mrbird.febs.common.properties.FebsProperties;
import cc.mrbird.febs.common.utils.FebsUtil;
import cc.mrbird.febs.common.utils.RfcNOC;
import cc.mrbird.febs.export.excel.ExportExcelUtils;
import cc.mrbird.febs.system.domain.User;
import cc.mrbird.febs.yb.entity.YbDept;
import cc.mrbird.febs.yb.entity.YbTYDJB;
import cc.mrbird.febs.yb.entity.YbWHYBYS;
import cc.mrbird.febs.yb.entity.YbZZDJB;
import cc.mrbird.febs.yb.service.IYbDeptService;
import com.baomidou.mybatisplus.core.toolkit.StringPool;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.ModelMap;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @author viki
 * @since 2020-07-21
 */
@Slf4j
@Validated
@RestController
@RequestMapping("ybSap")
public class YbSapController extends BaseController {

    @Autowired
    FebsProperties febsProperties;
    private String message;
    @GetMapping("whYbysList")
    @RequiresPermissions("ybSap:view")
    //武汉市医保应收明细表
    public Object List(QueryRequest request, Integer yq,String iperi) {
        int success = 0;
        ModelMap map = new ModelMap();
        List<YbWHYBYS> list = new ArrayList<>();
        try {
            RfcNOC s = new RfcNOC();
            list = s.getWhYbysList(yq, iperi);
            success = 1;
        }catch (Exception e) {
            log.error(message, e);
            message = "获取数据失败.";
        }
        map.put("success",success);
        map.put("message",message);
        map.put("data",list);
        return map;
    }

    @PostMapping("excelWhYbys")
    @RequiresPermissions("ybSap:export")
    //武汉市医保应收明细表
    public void export(QueryRequest request, Integer yq,String iperi, String dataJson, HttpServletResponse response) throws FebsException {
        try {
            RfcNOC s = new RfcNOC();
            List<YbWHYBYS> list = s.getWhYbysList(yq, iperi);
            ExportExcelUtils.exportCustomExcel(response, list, dataJson, "武汉市医保应收明细表");
        } catch (Exception e) {
            message = "导出Excel失败";
            log.error(message, e);
            throw new FebsException(message);
        }
    }

    @GetMapping("zzDjBList")
    @RequiresPermissions("ybSap:view")
    //重症登记表
    public Object List1(QueryRequest request, String zzbh,String xm,String xz,String kh,String bzbh) {
        int success = 0;
        ModelMap map = new ModelMap();
        List<YbZZDJB> list = new ArrayList<>();
        try {
            RfcNOC s = new RfcNOC();
            list = s.getZzDjBList(zzbh,xm,xz,kh,bzbh);
//            this.addYbZZDJB(list);
            success = 1;
        }catch (Exception e) {
            log.error(message, e);
            message = "获取数据失败.";
        }
        map.put("success",success);
        map.put("message",message);
        map.put("data",list);
        return map;
    }

    private void addYbZZDJB(List<YbZZDJB> list){
        for (int i=0;i<5;i++){
            YbZZDJB ybZZDJB = new YbZZDJB();
            ybZZDJB.setZcjbr("转出经办人"+i);
            ybZZDJB.setZcsj("转出时间"+i);
            ybZZDJB.setYbjbr("医保办经办人"+i);
            ybZZDJB.setZrsj("转入时间"+i);
            ybZZDJB.setLxfs("联系方式"+i);
            ybZZDJB.setBzbh("病种编号"+i);
            ybZZDJB.setBz("备注"+i);
            ybZZDJB.setZzbh("重症编号"+i);
            ybZZDJB.setXm("姓名"+i);
            ybZZDJB.setXq("辖区"+i);
            ybZZDJB.setYbxz("医保性质"+i);
            ybZZDJB.setYbkh("医保卡号"+i);
            ybZZDJB.setBzmc("病种名称"+i);
            list.add(ybZZDJB);
        }
    }

    @PostMapping("excelZzDjB")
    @RequiresPermissions("ybSap:export")
    //重症登记表
    public void export1(QueryRequest request, String zzbh,String xm,String xz,String kh,String bzbh, String dataJson, HttpServletResponse response) throws FebsException {
        try {
            RfcNOC s = new RfcNOC();
            List<YbZZDJB> list = s.getZzDjBList(zzbh,xm,xz,kh,bzbh);
//            this.addYbZZDJB(list);
            ExportExcelUtils.exportCustomExcel(response, list, dataJson, "重症登记表");
        } catch (Exception e) {
            message = "导出Excel失败";
            log.error(message, e);
            throw new FebsException(message);
        }
    }

    private void addYbTYDJB(List<YbTYDJB> list) {
        for (int i=0;i<5;i++) {
            YbTYDJB ybTYDJB = new YbTYDJB();
            ybTYDJB.setBz("备注"+i);
            ybTYDJB.setBasj("备案时间"+i);
            ybTYDJB.setKs("科室"+i);
            ybTYDJB.setLxdh("联系电话"+i);
            ybTYDJB.setPgrq("评估日期"+i);
            ybTYDJB.setSfzh("身份证号"+i);
            ybTYDJB.setSpm("商品名"+i);
            ybTYDJB.setTymc("特药名称"+i);
            ybTYDJB.setXb("性别"+i);
            ybTYDJB.setXm("姓名"+i);
            ybTYDJB.setYbkh("医保卡号"+i);
            ybTYDJB.setYfyl("用法用量"+i);
            ybTYDJB.setYyyj("用药依据"+i);
            ybTYDJB.setZd("诊断"+i);
            ybTYDJB.setZrys("责任医生"+i);
            ybTYDJB.setQzsj("确诊时间"+i);
            list.add(ybTYDJB);
        }
    }

    @GetMapping("tyDjBList")
    @RequiresPermissions("ybSap:view")
    //特药登记表
    public Object List2(QueryRequest request,String kh,String sfzh,String xm) {
        int success = 0;
        ModelMap map = new ModelMap();
        List<YbTYDJB> list = new ArrayList<>();
        try {
            RfcNOC s = new RfcNOC();
            list = list = s.getTyDjBList(kh,sfzh,xm);
//            this.addYbTYDJB(list);
            success = 1;
        }catch (Exception e) {
            log.error(message, e);
            message = "获取数据失败.";
        }
        map.put("success",success);
        map.put("message",message);
        map.put("data",list);
        return map;
    }

    @PostMapping("excelTyDjB")
    @RequiresPermissions("ybSap:export")
    //特药登记表
    public void export2(QueryRequest request,String kh,String sfzh,String xm, String dataJson, HttpServletResponse response) throws FebsException {
        try {
            RfcNOC s = new RfcNOC();
            List<YbTYDJB> list = s.getTyDjBList(kh,sfzh,xm);
//            this.addYbTYDJB(list);
            ExportExcelUtils.exportCustomExcel(response, list, dataJson, "特药登记表");
        } catch (Exception e) {
            message = "导出Excel失败";
            log.error(message, e);
            throw new FebsException(message);
        }
    }

}