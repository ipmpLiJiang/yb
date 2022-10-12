package cc.mrbird.febs.chs.entity;

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
 * @since 2022-10-12
 */

@Excel("yb_chs_project_set")
@Data
@Accessors(chain = true)
public class YbChsProjectSet implements Serializable , Comparable<YbChsProjectSet>{

private static final long serialVersionUID=1L;

    /**
     * id
     */
                    @TableId(value = "id" , type = IdType.AUTO)
                    @ExcelField(value ="id")
    private Integer id;

    /**
     * 住院门诊
     */
    @TableField("zymzType")
            @ExcelField(value ="住院门诊")
    private Integer zymzType;

    /**
     * 规则名称
     */
    @TableField("ruleName")
            @ExcelField(value ="规则名称")
    private String ruleName;

    /**
     * 项目名称
     */
    @TableField("projectName")
            @ExcelField(value ="项目名称")
    private String projectName;

    /**
     * 确定项目名称
     */
    @TableField("qdName")
            @ExcelField(value ="确定项目名称")
    private String qdName;

    /**
     * 院区
     */
    @TableField("areaType")
            @ExcelField(value ="院区")
    private Integer areaType;

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
     * 通用字段
     */
    @TableField("currencyField")
            @ExcelField(value ="通用字段")
    private String currencyField;

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

    public static final String ZYMZTYPE ="zymzType" ;

    public static final String RULENAME ="ruleName" ;

    public static final String PROJECTNAME ="projectName" ;

    public static final String QDNAME ="qdName" ;

    public static final String AREATYPE ="areaType" ;

    public static final String OPERATORID ="operatorId" ;

    public static final String OPERATORNAME ="operatorName" ;

    public static final String CURRENCYFIELD ="currencyField" ;

    public static final String COMMENTS ="COMMENTS" ;

    public static final String STATE ="STATE" ;

    public static final String IS_DELETEMARK ="IS_DELETEMARK" ;

    public static final String MODIFY_TIME ="MODIFY_TIME" ;

    public static final String CREATE_TIME ="CREATE_TIME" ;

    public static final String CREATE_USER_ID ="CREATE_USER_ID" ;

    public static final String MODIFY_USER_ID ="MODIFY_USER_ID" ;

@Override
public int compareTo(YbChsProjectSet o) {
        if (this.getId() != null && o.getId() != null) {
        return this.getId().compareTo(o.getId());
        } else if (this.getId() != null) {
        return 1;
        } else {
        return 0;
        }
        }
}