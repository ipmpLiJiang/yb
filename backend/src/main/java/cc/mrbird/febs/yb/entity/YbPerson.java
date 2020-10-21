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
 * @since 2020-07-21
 */

@Excel("yb_person")
@Data
@Accessors(chain = true)
public class YbPerson implements Serializable{

private static final long serialVersionUID=1L;

    /**
     * 人员Id
     */
                    @TableId(value = "id" , type = IdType.AUTO)
                    @ExcelField(value ="医生Id")
    private Integer id;

    /**
     * 人员编码
     */
    @TableField("personCode")
            @ExcelField(value ="医生编码")
    private String personCode;

    /**
     * 人员名称
     */
    @TableField("personName")
            @ExcelField(value ="医生名称")
    private String personName;

    /**
     * 科室编码
     */
    @TableField("deptCode")
    @ExcelField(value ="科室编码")
    private String deptCode;

    /**
     * 部门名称
     */
    @TableField("deptName")
    @ExcelField(value ="科室名称")
    private String deptName;

    /**
     * 性别
     */
    @TableField("sex")
    @ExcelField(value ="性别")
    private String sex;

    /**
     * 邮箱
     */
    @TableField("email")
    @ExcelField(value ="邮箱")
    private String email;


    /**
     * 联系电话
     */
    @TableField("tel")
    @ExcelField(value ="联系电话")
    private String tel;


    /**
     * 拼音码
     */
    @TableField("spellCode")
    @ExcelField(value ="拼音码")
    private String spellCode;

    /**
     * 五笔码
     */
    @TableField("strokeCode")
    @ExcelField(value ="五笔码")
    private String strokeCode;

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

    public static final String PERSONCODE ="personCode" ;

    public static final String PERSONNAME ="personName" ;

    public static final String DEPTCODE ="deptCode" ;

    public static final String DEPTNAME ="deptName" ;

    public static final String SEX ="sex" ;

    public static final String EMAIL ="email" ;

    public static final String TEL ="tel" ;

    public static final String SPELLCODE ="spellCode" ;

    public static final String STROKECODE ="strokeCode" ;

    public static final String COMMENTS ="COMMENTS" ;

    public static final String STATE ="STATE" ;

    public static final String IS_DELETEMARK ="IS_DELETEMARK" ;

    public static final String MODIFY_TIME ="MODIFY_TIME" ;

    public static final String CREATE_TIME ="CREATE_TIME" ;

    public static final String CREATE_USER_ID ="CREATE_USER_ID" ;

    public static final String MODIFY_USER_ID ="MODIFY_USER_ID" ;

        }