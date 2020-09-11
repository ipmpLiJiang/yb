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
 * @since 2020-07-15
 */

@Excel("yb_reconsider_rule")
@Data
@Accessors(chain = true)
public class YbReconsiderRule implements Serializable{

private static final long serialVersionUID=1L;

    /**
     * 规则编号
     */
                    @TableId(value = "id")
                    @ExcelField(value ="规则编号")
    private String id;

    /**
     * 序号
     */
            @ExcelField(value ="序号")
    private Integer rno;

    /**
     * 规则描述
     */
            @ExcelField(value ="规则描述")
    private String rdescribe;

    /**
     * 规则解释
     */
            @ExcelField(value ="规则解释")
    private String rxplain;

    /**
     * 复议重点
     */
            @ExcelField(value ="复议重点")
    private String rkeypoints;

    /**
     * 复议资料
     */
            @ExcelField(value ="复议资料")
    private String rmaterials;

    /**
     * 操作员代码
     */
            @ExcelField(value ="操作员代码")
    private Long operatorid;

    /**
     * 操作员名称
     */
            @ExcelField(value ="操作员名称")
    private String operatorname;

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

    public static final String RNO ="rno" ;

    public static final String RDESCRIBE ="rdescribe" ;

    public static final String RXPLAIN ="rxplain" ;

    public static final String RKEYPOINTS ="rkeypoints" ;

    public static final String RMATERIALS ="rmaterials" ;

    public static final String OPERATORID ="operatorid" ;

    public static final String OPERATORNAME ="operatorname" ;

    public static final String COMMENTS ="COMMENTS" ;

    public static final String STATE ="STATE" ;

    public static final String IS_DELETEMARK ="IS_DELETEMARK" ;

    public static final String MODIFY_TIME ="MODIFY_TIME" ;

    public static final String CREATE_TIME ="CREATE_TIME" ;

    public static final String CREATE_USER_ID ="CREATE_USER_ID" ;

    public static final String MODIFY_USER_ID ="MODIFY_USER_ID" ;

        }