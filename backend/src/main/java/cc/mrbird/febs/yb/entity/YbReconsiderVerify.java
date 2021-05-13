package cc.mrbird.febs.yb.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;

import java.time.LocalDateTime;

import com.baomidou.mybatisplus.annotation.TableField;

import java.io.Serializable;


import lombok.Data;
import lombok.experimental.Accessors;

import java.util.Date;

import com.wuwenze.poi.annotation.Excel;
import com.wuwenze.poi.annotation.ExcelField;

/**
 * <p>
 *
 * </p>
 *
 * @author viki
 * @since 2020-07-30
 */

@Excel("yb_reconsider_verify")
@Data
@Accessors(chain = true)
public class YbReconsiderVerify implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 核对申请
     */
    @TableId(value = "id")
    @ExcelField(value = "核对申请")
    private String id;

    /**
     * 复议申请明细
     */
    @TableField("applyDataId")
    @ExcelField(value = "复议申请明细")
    private String applyDataId;

    /**
     * 参考复议医生编码
     */
    @TableField("verifyDoctorCode")
    @ExcelField(value = "参考复议医生编码")
    private String verifyDoctorCode;

    /**
     * 参考复议医生
     */
    @TableField("verifyDoctorName")
    @ExcelField(value = "参考复议医生")
    private String verifyDoctorName;

    /**
     * 参考复议科室编码
     */
    @TableField("verifyDeptCode")
    @ExcelField(value = "参考复议科室编码")
    private String verifyDeptCode;

    /**
     * 参考复议科室
     */
    @TableField("verifyDeptName")
    @ExcelField(value = "参考复议科室")
    private String verifyDeptName;


    /**
     * 数据类型
     */
    @TableField("dataType")
    //@ExcelField(value ="数据类型")
    private Integer dataType;

    /**
     * 发送人代码
     */
    @TableField("sendPersonId")
    @ExcelField(value ="发送人代码")
    private Long sendPersonId;

    /**
     * 发送人
     */
    @TableField("sendPersonName")
    @ExcelField(value ="发送人")
    private String sendPersonName;

    /**
     * 发送日期
     */
    @TableField("sendDate")
    @ExcelField(value ="发送日期")
    private Date sendDate;
    private transient String sendDateFrom;
    private transient String sendDateTo;

    /**
     * 复议年月Str
     */
    @TableField("applyDateStr")
    @ExcelField(value = "复议年月Str")
    private String applyDateStr;

    /**
     * 序号
     */
    @TableField("orderNumber")
    @ExcelField(value = "序号")
    private String orderNumber;

    /**
     * 排序
     */
    @TableField("orderNum")
    @ExcelField(value = "排序")
    private Integer orderNum;

    /**
     * 版本类型
     */
    @TableField("typeno")
    //@ExcelField(value ="版本类型")
    private Integer typeno;

    /**
     * 开方医生编码
     */
    @TableField("orderDoctorCode")
    @ExcelField(value ="开方医生编码")
    private String orderDoctorCode;

    /**
     * 开方医生名称
     */
    @TableField("orderDoctorName")
    @ExcelField(value ="开方医生名称")
    private String orderDoctorName;

    /**
     * 开方科室编码
     */
    @TableField("orderDeptCode")
    @ExcelField(value ="开方科室编码")
    private String orderDeptCode;

    /**
     * 开方科室名称
     */
    @TableField("orderDeptName")
    @ExcelField(value ="开方科室名称")
    private String orderDeptName;

    /**
     * 备注
     */
    @TableField("COMMENTS")
    @ExcelField(value = "备注")
    private String comments;

    /**
     * 状态
     */
    @TableField("STATE")
    @ExcelField(value = "状态")
    private Integer state;

    /**
     * 是否删除
     */
    @TableField("IS_DELETEMARK")
    @ExcelField(value = "是否删除")
    private Integer isDeletemark;


    /**
     * 院区
     */
    @TableField("areaType")
    @ExcelField(value = "院区")
    private Integer areaType;


    public static final String ID = "id";

    public static final String APPLYDATAID = "applyDataId";

    public static final String VERIFYDOCTORCODE = "verifyDoctorCode";

    public static final String VERIFYDOCTORNAME = "verifyDoctorName";

    public static final String VERIFYDEPTCODE = "verifyDeptCode";

    public static final String VERIFYDEPTNAME = "verifyDeptName";

    public static final String DATATYPE = "dataType";

    public static final String SENDPERSONID ="sendPersonId" ;

    public static final String SENDPERSONNAME ="sendPersonName" ;

    public static final String SENDDATE ="sendDate" ;

    public static final String APPLYDATESTR = "applyDateStr";

    public static final String ORDERNUMBER = "orderNumber";

    public static final String ORDERNUM = "orderNum";

    public static final String TYPENO = "typeno";

    public static final String ORDERDOCTORCODE = "orderDoctorCode";

    public static final String ORDERDOCTORNAME = "orderDoctorName";

    public static final String ORDERDEPTCODE = "orderDeptCode";

    public static final String ORDERDEPTNAME = "orderDeptName";

    public static final String COMMENTS = "COMMENTS";

    public static final String STATE = "STATE";

    public static final String IS_DELETEMARK = "IS_DELETEMARK";


    public static final String AREATYPE = "areaType";

}