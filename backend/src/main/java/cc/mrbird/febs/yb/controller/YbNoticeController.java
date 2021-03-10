package cc.mrbird.febs.yb.controller;

import cc.mrbird.febs.com.controller.DataTypeHelpers;
import cc.mrbird.febs.common.annotation.Log;
import cc.mrbird.febs.common.controller.BaseController;
import cc.mrbird.febs.common.domain.FebsResponse;
import cc.mrbird.febs.common.domain.router.VueRouter;
import cc.mrbird.febs.common.exception.FebsException;
import cc.mrbird.febs.common.domain.QueryRequest;

import cc.mrbird.febs.yb.domain.ResponseResultData;
import cc.mrbird.febs.yb.entity.*;
import cc.mrbird.febs.yb.service.IYbNoticeService;

import cc.mrbird.febs.common.utils.FebsUtil;
import cc.mrbird.febs.system.domain.User;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import com.baomidou.mybatisplus.core.toolkit.StringPool;
import com.mysql.cj.protocol.x.Notice;
import com.wuwenze.poi.ExcelKit;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import java.util.*;

/**
 * @author viki
 * @since 2021-03-08
 */
@Slf4j
@Validated
@RestController
@RequestMapping("ybNotice")

public class YbNoticeController extends BaseController {

    private String message;
    @Autowired
    public IYbNoticeService iYbNoticeService;


    /**
     * 分页查询数据
     *
     * @param request  分页信息
     * @param ybNotice 查询条件
     * @return
     */
    @GetMapping
    @RequiresPermissions("ybNotice:view")
    public Map<String, Object> List(QueryRequest request, YbNotice ybNotice) {
        return getDataTable(this.iYbNoticeService.findYbNotices(request, ybNotice));
    }

    /**
     * 添加
     *
     * @param ybNotice
     * @return
     */
    @Log("新增/按钮")
    @PostMapping
    @RequiresPermissions("ybNotice:add")
    public void addYbNotice(@Valid YbNotice ybNotice) throws FebsException {
        try {
            User currentUser = FebsUtil.getCurrentUser();
            ybNotice.setCreateUserId(currentUser.getUserId());
            this.iYbNoticeService.createYbNotice(ybNotice);
        } catch (Exception e) {
            message = "新增/按钮失败";
            log.error(message, e);
            throw new FebsException(message);
        }
    }

    /**
     * 修改
     *
     * @param ybNotice
     * @return
     */
    @Log("修改")
    @PutMapping
    @RequiresPermissions("ybNotice:update")
    public void updateYbNotice(@Valid YbNotice ybNotice) throws FebsException {
        try {
            User currentUser = FebsUtil.getCurrentUser();
            ybNotice.setModifyUserId(currentUser.getUserId());
            this.iYbNoticeService.updateYbNotice(ybNotice);
        } catch (Exception e) {
            message = "修改失败";
            log.error(message, e);
            throw new FebsException(message);
        }
    }


    @Log("新增/按钮")
    @PostMapping("addNotice")
    @RequiresPermissions("ybNotice:add")
    public FebsResponse addNotice(String dataJson) {
        int success = 0;
        String id = "";
        try {
            User currentUser = FebsUtil.getCurrentUser();
            YbNoticeJson noticeJson = JSON.parseObject(dataJson, new TypeReference<YbNoticeJson>() {
            });
            YbNotice create = new YbNotice();
            List<YbNoticeData> createDataList = new ArrayList<>();
            id = UUID.randomUUID().toString();
            create.setId(id);
            create.setOperatorId(currentUser.getUserId());
            create.setOperatorName(currentUser.getUsername() + "-" +currentUser.getXmname());
            create.setNtTitle(noticeJson.getNtTitle());
            create.setNtExplain(noticeJson.getNtExplain());
            create.setNtDetail(noticeJson.getNtDetail());
            create.setSendType(noticeJson.getSendType());
            create.setIsDeletemark(1);
            create.setCreateTime(new Date());
            create.setCreateUserId(currentUser.getUserId());
            for (YbNoticeDataJson item : noticeJson.getChild()) {
                YbNoticeData createData = new YbNoticeData();
                createData.setId(UUID.randomUUID().toString());
                createData.setPid(id);
                createData.setPersonCode(item.getPersonCode());
                createData.setPersonName(item.getPersonName());
                createData.setCmId(item.getCmId());
                createData.setCmName(item.getCmName());
                createData.setNdType(item.getNdType());
                createDataList.add(createData);
            }
            this.iYbNoticeService.createNotice(create, createDataList);
            success = 1;
        } catch (Exception e) {
            message = "新增/按钮失败";
            log.error(message, e);
        }

        ResponseResultData rrd = new ResponseResultData();
        rrd.setSuccess(success);
        rrd.setMessage(message);
        rrd.setData(id);
        return new FebsResponse().data(rrd);


    }

    @Log("修改")
    @PutMapping("updateNotice")
    @RequiresPermissions("ybNotice:update")
    public FebsResponse updateNotice(String dataJson) {
        int success = 0;
        try {
            User currentUser = FebsUtil.getCurrentUser();
            YbNoticeJson noticeJson = JSON.parseObject(dataJson, new TypeReference<YbNoticeJson>() {
            });
            List<YbNoticeData> updateDataList = new ArrayList<>();
            List<YbNoticeData> createDataList = new ArrayList<>();
            YbNotice update = new YbNotice();
            update.setId(noticeJson.getId());
            update.setNtTitle(noticeJson.getNtTitle());
            update.setNtExplain(noticeJson.getNtExplain());
            update.setNtDetail(noticeJson.getNtDetail());
            update.setSendType(noticeJson.getSendType());
            update.setModifyTime(new Date());
            update.setModifyUserId(currentUser.getUserId());
            for (YbNoticeDataJson item : noticeJson.getChild()) {
                YbNoticeData updateData = new YbNoticeData();
                updateData.setId(item.getId());
                updateData.setPid(noticeJson.getId());
                updateData.setPersonCode(item.getPersonCode());
                updateData.setPersonName(item.getPersonName());
                updateData.setCmId(item.getCmId());
                updateData.setCmName(item.getCmName());
                updateData.setNdType(item.getNdType());
                if (updateData.getId() == null) {
                    updateData.setId(UUID.randomUUID().toString());
                    createDataList.add(updateData);
                } else {
                    updateDataList.add(updateData);
                }
            }
            this.iYbNoticeService.updateNotice(update, createDataList, updateDataList);
            success = 1;
        } catch (Exception e) {
            message = "修改失败";
            log.error(message, e);
        }

        ResponseResultData rrd = new ResponseResultData();
        rrd.setSuccess(success);
        rrd.setMessage(message);
        rrd.setData("");
        return new FebsResponse().data(rrd);
    }


    @Log("删除")
    @DeleteMapping("/{ids}")
    @RequiresPermissions("ybNotice:delete")
    public void deleteYbNotices(@NotBlank(message = "{required}") @PathVariable String ids) throws FebsException {
        try {
            String[] arr_ids = ids.split(StringPool.COMMA);
            this.iYbNoticeService.deleteYbNotices(arr_ids);
        } catch (Exception e) {
            message = "删除失败";
            log.error(message, e);
            throw new FebsException(message);
        }
    }

    @PostMapping("excel")
    @RequiresPermissions("ybNotice:export")
    public void export(QueryRequest request, YbNotice ybNotice, HttpServletResponse response) throws FebsException {
        try {
            List<YbNotice> ybNotices = this.iYbNoticeService.findYbNotices(request, ybNotice).getRecords();
            ExcelKit.$Export(YbNotice.class, response).downXlsx(ybNotices, false);
        } catch (Exception e) {
            message = "导出Excel失败";
            log.error(message, e);
            throw new FebsException(message);
        }
    }

    @GetMapping("/{id}")
    public YbNotice detail(@NotBlank(message = "{required}") @PathVariable String id) {
        YbNotice ybNotice = this.iYbNoticeService.getById(id);
        return ybNotice;
    }
}