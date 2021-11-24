package cc.mrbird.febs.drg.entity;

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
 * @since 2021-11-23
 */

@Excel("yb_drg_apply_task")
@Data
@Accessors(chain = true)
public class YbDrgApplyTask implements Serializable , Comparable<YbDrgApplyTask>{

private static final long serialVersionUID=1L;

    /**
     * drg申请任务记录
     */
                                @ExcelField(value ="drg申请任务记录")
    private String id;

    /**
     * drg年月Str
     */
    @TableField("applyDateStr")
            @ExcelField(value ="drg年月Str")
    private String applyDateStr;

    /**
     * 开始数
     */
    @TableField("startNum")
            @ExcelField(value ="开始数")
    private Integer startNum;

    /**
     * 结束数
     */
    @TableField("endNum")
            @ExcelField(value ="结束数")
    private Integer endNum;

    /**
     * 总数
     */
    @TableField("totalRow")
            @ExcelField(value ="总数")
    private Integer totalRow;

    /**
     * 当前页
     */
    @TableField("currentPage")
            @ExcelField(value ="当前页")
    private Integer currentPage;

    /**
     * 页数
     */
    @TableField("pageSize")
            @ExcelField(value ="页数")
    private Integer pageSize;

    /**
     * 总页数
     */
    @TableField("totalPage")
            @ExcelField(value ="总页数")
    private Integer totalPage;

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
     * 院区
     */
    @TableField("areaType")
            @ExcelField(value ="院区")
    private Integer areaType;

    /**
     * His更新数量
     */
    @TableField("hisCount")
            @ExcelField(value ="His更新数量")
    private Integer hisCount;



    public static final String ID ="id" ;

    public static final String APPLYDATESTR ="applyDateStr" ;

    public static final String STARTNUM ="startNum" ;

    public static final String ENDNUM ="endNum" ;

    public static final String TOTALROW ="totalRow" ;

    public static final String CURRENTPAGE ="currentPage" ;

    public static final String PAGESIZE ="pageSize" ;

    public static final String TOTALPAGE ="totalPage" ;

    public static final String COMMENTS ="COMMENTS" ;

    public static final String STATE ="STATE" ;

    public static final String AREATYPE ="areaType" ;

    public static final String HISCOUNT ="hisCount" ;

@Override
public int compareTo(YbDrgApplyTask o) {
        if (this.getId() != null && o.getId() != null) {
        return this.getId().compareTo(o.getId());
        } else if (this.getId() != null) {
        return 1;
        } else {
        return 0;
        }
        }
}