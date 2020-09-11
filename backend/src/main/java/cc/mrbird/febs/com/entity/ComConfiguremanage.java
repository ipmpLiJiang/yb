package cc.mrbird.febs.com.entity;

import java.math.BigDecimal;
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
 * @since 2020-08-05
 */

@Excel("com_configuremanage")
@Data
@Accessors(chain = true)
public class ComConfiguremanage implements Serializable{

private static final long serialVersionUID=1L;

    /**
     * 配置管理
     */
                    @TableId(value = "id" , type = IdType.AUTO)
                    @ExcelField(value ="配置管理")
    private Integer id;

    /**
     * 数字
     */
    @TableField("intField")
            @ExcelField(value ="数字")
    private Integer intField;

    /**
     * 小数
     */
    @TableField("numericField")
            @ExcelField(value ="小数")
    private BigDecimal numericField;

    /**
     * 字符
     */
    @TableField("stringField")
            @ExcelField(value ="字符")
    private String stringField;

    /**
     * 通用字段
     */
    @TableField("currencyField")
            @ExcelField(value ="通用字段")
    private String currencyField;

    /**
     * 类型
     */
    @TableField("configureType")
            @ExcelField(value ="类型")
    private Integer configureType;

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

    public static final String INTFIELD ="intField" ;

    public static final String NUMERICFIELD ="numericField" ;

    public static final String STRINGFIELD ="stringField" ;

    public static final String CURRENCYFIELD ="currencyField" ;

    public static final String CONFIGURETYPE ="configureType" ;

    public static final String COMMENTS ="COMMENTS" ;

    public static final String STATE ="STATE" ;

    public static final String IS_DELETEMARK ="IS_DELETEMARK" ;

    public static final String MODIFY_TIME ="MODIFY_TIME" ;

    public static final String CREATE_TIME ="CREATE_TIME" ;

    public static final String CREATE_USER_ID ="CREATE_USER_ID" ;

    public static final String MODIFY_USER_ID ="MODIFY_USER_ID" ;

        }