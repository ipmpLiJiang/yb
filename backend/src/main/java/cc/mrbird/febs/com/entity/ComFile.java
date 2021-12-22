package cc.mrbird.febs.com.entity;

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
 * 附件
 * </p>
 *
 * @author viki
 * @since 2020-08-06
 */

@Excel("com_file")
@Data
@Accessors(chain = true)
public class ComFile implements Comparable<ComFile> {

private static final long serialVersionUID=1L;

    /**
     * 主键
     */
                    @TableId("ID")
                    @ExcelField(value ="主键")
    private String id;

    /**
     * 文件名称
     */
    @TableField("CLIENT_NAME")
            @ExcelField(value ="文件名称")
    private String clientName;

    /**
     * 服务器地址
     */
    @TableField("SERVER_NAME")
            @ExcelField(value ="服务器地址")
    private String serverName;

    /**
     * 是否删除
     */
    @TableField("IS_DELETEMARK")
            @ExcelField(value ="是否删除")
    private Integer isDeletemark;

    /**
     * 创建时间
     */
    @TableField("CREATE_TIME")
            @ExcelField(value ="创建时间")
    private Date createTime;
    private transient String createTimeFrom;
    private transient String createTimeTo;

    /**
     * 修改时间
     */
    @TableField("MODIFY_TIME")
            @ExcelField(value ="修改时间")
    private Date modifyTime;
    private transient String modifyTimeFrom;
    private transient String modifyTimeTo;

    /**
     * 创建人
     */
    @TableField("CREATE_USER_ID")
            @ExcelField(value ="创建人")
    private Integer createUserId;

    /**
     * 修改人
     */
    @TableField("MODIFY_USER_ID")
            @ExcelField(value ="修改人")
    private Integer modifyUserId;

    /**
     * 记录ID
     */
    @TableField("REF_TAB_ID")
            @ExcelField(value ="记录ID")
    private String refTabId;

    /**
     * 表名
     */
    @TableField("REF_TAB_TABLE")
            @ExcelField(value ="表名")
    private String refTabTable;

    /**
     * 类型
     */
    @TableField("REF_TYPE")
    @ExcelField(value ="类型")
    private String refType;


    private transient Integer orderNum;

    private transient String refTypeName;

    private transient String orderNumber;

    public static final String ID ="ID" ;

    public static final String CLIENT_NAME ="CLIENT_NAME" ;

    public static final String SERVER_NAME ="SERVER_NAME" ;

    public static final String IS_DELETEMARK ="IS_DELETEMARK" ;

    public static final String CREATE_TIME ="CREATE_TIME" ;

    public static final String MODIFY_TIME ="MODIFY_TIME" ;

    public static final String CREATE_USER_ID ="CREATE_USER_ID" ;

    public static final String MODIFY_USER_ID ="MODIFY_USER_ID" ;

    public static final String REF_TAB_ID ="REF_TAB_ID" ;

    public static final String REF_TAB_TABLE ="REF_TAB_TABLE" ;

    public static final String REF_TYPE ="REF_TYPE" ;

    @Override
    public int compareTo(ComFile o) {
        if(this.getCreateTime()!=null && o.getCreateTime()!=null){
            return this.getCreateTime().compareTo(o.getCreateTime());
        }else if(this.getCreateTime()!=null) {
            return 1;
        } else {
            return 0;
        }
    }
}