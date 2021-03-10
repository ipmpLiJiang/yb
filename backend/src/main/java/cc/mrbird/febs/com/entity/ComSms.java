package cc.mrbird.febs.com.entity;

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
 * @since 2020-11-13
 */

@Excel("com_sms")
@Data
@Accessors(chain = true)
public class ComSms implements Serializable , Comparable<ComSms>{

private static final long serialVersionUID=1L;

    /**
     * 1 核对发送
     */
    public static final int SENDTYPE_1 = 1 ;
    /**
     * 2 人工复议发送
     */

    public static final int SENDTYPE_2 = 2 ;
    /**
     * 3 医保办变更
     */
    public static final int SENDTYPE_3 = 3 ;

    /**
     * 4 管理员变更
     */
    public static final int SENDTYPE_4 = 4 ;

    /**
     * 5 完成剔除
     */
    public static final int SENDTYPE_5 = 5 ;

    /**
     * 6 人工复议
     */
    public static final int SENDTYPE_6 = 6 ;

    /**
     * 0 未发送
     */
    public static final int STATE_0 = 0 ;
    /**
     * 1 已发送
     */

    public static final int STATE_1 = 1 ;

    /**
     * 信息ID
     */
                                @ExcelField(value ="信息ID")
    private String id;

    /**
     * 发送账户
     */
            @ExcelField(value ="发送账户")
    private String sendcode;

    /**
     * 发送人
     */
            @ExcelField(value ="发送人")
    private String sendname;

    /**
     * 电话号码
     */
            @ExcelField(value ="手机号码")
    private String mobile;

    /**
     * 发送类型
     */
    @TableField("sendType")
            @ExcelField(value ="发送类型")
    private Integer sendType;

    /**
     * 操作员代码
     */
    @TableField("operatorId")
            @ExcelField(value ="操作员代码")
    private Long operatorId;

    /**
     * 操作员名称
     */
    @TableField("operatorName")
            @ExcelField(value ="操作员名称")
    private String operatorName;

    /**
     * 发送内容
     */
            @ExcelField(value ="发送内容")
    private String sendcontent;

    /**
     * 备注
     */
    @TableField("COMMENTS")
            @ExcelField(value ="备注")
    private String comments;

    /**
     * 状态
     */
    @TableField("STATE")
            @ExcelField(value ="状态")
    private Integer state;

    /**
     * 是否删除
     */
    @TableField("IS_DELETEMARK")
            @ExcelField(value ="是否删除")
    private Integer isDeletemark;

    /**
     * 修改时间
     */
    @TableField("MODIFY_TIME")
            @ExcelField(value ="修改时间")
    private Date modifyTime;
    private transient String modifyTimeFrom;
    private transient String modifyTimeTo;

    /**
     * 创建时间
     */
    @TableField("CREATE_TIME")
            @ExcelField(value ="创建时间")
    private Date createTime;
    private transient String createTimeFrom;
    private transient String createTimeTo;

    /**
     * 创建人
     */
    @TableField("CREATE_USER_ID")
            @ExcelField(value ="创建人")
    private Long createUserId;

    /**
     * 修改人
     */
    @TableField("MODIFY_USER_ID")
            @ExcelField(value ="修改人")
    private Long modifyUserId;



    public static final String ID ="id" ;

    public static final String SENDCODE ="sendcode" ;

    public static final String SENDNAME ="sendname" ;

    public static final String MOBILE ="mobile" ;

    public static final String SENDTYPE ="sendType" ;

    public static final String OPERATORID ="operatorId" ;

    public static final String OPERATORNAME ="operatorName" ;

    public static final String SENDCONTENT ="sendcontent" ;

    public static final String COMMENTS ="COMMENTS" ;

    public static final String STATE ="STATE" ;

    public static final String IS_DELETEMARK ="IS_DELETEMARK" ;

    public static final String MODIFY_TIME ="MODIFY_TIME" ;

    public static final String CREATE_TIME ="CREATE_TIME" ;

    public static final String CREATE_USER_ID ="CREATE_USER_ID" ;

    public static final String MODIFY_USER_ID ="MODIFY_USER_ID" ;

@Override
public int compareTo(ComSms o) {
        if (this.getId() != null && o.getId() != null) {
        return this.getId().compareTo(o.getId());
        } else if (this.getId() != null) {
        return 1;
        } else {
        return 0;
        }
        }
}