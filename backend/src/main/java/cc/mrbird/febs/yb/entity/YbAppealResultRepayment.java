package cc.mrbird.febs.yb.entity;

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
 * @since 2020-10-09
 */

@Excel("yb_appeal_result_repayment")
@Data
@Accessors(chain = true)
public class YbAppealResultRepayment implements Serializable, Comparable<YbAppealResultRepayment> {

    private static final long serialVersionUID = 1L;

    /**
     * 还款落实Id
     */
    @ExcelField(value = "还款落实Id")
    private String id;

    /**
     * 复议年月
     */
    @TableField("applyDate")
    @ExcelField(value = "复议年月")
    private Date applyDate;
    private transient String applyDateFrom;
    private transient String applyDateTo;

    /**
     * 复议年月Str
     */
    @TableField("applyDateStr")
    @ExcelField(value = "复议年月Str")
    private String applyDateStr;

//    /**
//     * 复议上传Id
//     */
//    @TableField("resultId")
//    @ExcelField(value = "复议上传Id")
//    private String resultId;

    /**
     * 剔除明细Id
     */
    @TableField("resetDataId")
    @ExcelField(value = "剔除明细Id")
    private String resetDataId;

    /**
     * 关联Id
     */
    @TableField("relatelDataId")
    @ExcelField(value ="关联Id")
    private String relatelDataId;

    /**
     * 扣除落实Id
     */
    @TableField("deductImplementId")
    @ExcelField(value = "扣除落实Id")
    private String deductImplementId;

    /**
     * 还款方案
     */
    @TableField("repaymentProgramme")
    @ExcelField(value = "还款方案")
    private String repaymentProgramme;

    /**
     * 数据类型
     */
    @TableField("dataType")
    //@ExcelField(value ="数据类型")
    private Integer dataType;

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
     * 修改时间
     */
    @TableField("MODIFY_TIME")
    @ExcelField(value = "修改时间")
    private Date modifyTime;
    private transient String modifyTimeFrom;
    private transient String modifyTimeTo;

    /**
     * 创建时间
     */
    @TableField("CREATE_TIME")
    @ExcelField(value = "创建时间")
    private Date createTime;
    private transient String createTimeFrom;
    private transient String createTimeTo;

    /**
     * 创建人
     */
    @TableField("CREATE_USER_ID")
    @ExcelField(value = "创建人")
    private Long createUserId;

    /**
     * 修改人
     */
    @TableField("MODIFY_USER_ID")
    @ExcelField(value = "修改人")
    private Long modifyUserId;


    public static final String ID = "id";

//    public static final String RESULTID = "resultId";

    public static final String RESETDATAID = "resetDataId";

    public static final String RELATELDATAID = "relatelDataId";

    public static final String DEDUCTIMPLEMENTID = "deductImplementId";

    public static final String REPAYMENTPROGRAMME = "repaymentProgramme";

    public static final String APPLYDATE = "applyDate";

    public static final String APPLYDATESTR = "applyDateStr";

    public static final String DATATYPE ="dataType" ;

    public static final String COMMENTS = "COMMENTS";

    public static final String STATE = "STATE";

    public static final String IS_DELETEMARK = "IS_DELETEMARK";

    public static final String MODIFY_TIME = "MODIFY_TIME";

    public static final String CREATE_TIME = "CREATE_TIME";

    public static final String CREATE_USER_ID = "CREATE_USER_ID";

    public static final String MODIFY_USER_ID = "MODIFY_USER_ID";

    @Override
    public int compareTo(YbAppealResultRepayment o) {
        if (this.getId() != null && o.getId() != null) {
            return this.getId().compareTo(o.getId());
        } else if (this.getId() != null) {
            return 1;
        } else {
            return 0;
        }
    }
}