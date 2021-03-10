package cc.mrbird.febs.yb.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
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
 * @since 2021-03-08
 */

@Excel("yb_appeal_sumdept_data")
@Data
@Accessors(chain = true)
public class YbAppealSumdeptData implements Serializable , Comparable<YbAppealSumdeptData>{

private static final long serialVersionUID=1L;

    /**
     * 申诉汇总科室明细
     */
                    @TableId(value = "id" )
                    @ExcelField(value ="申诉汇总科室明细")
    private String id;

    /**
     * pid
     */
            @ExcelField(value ="pid")
    private String pid;

    /**
     * 科室编码
     */
    @TableField("deptId")
            @ExcelField(value ="科室编码")
    private String deptId;

    /**
     * 科室名称
     */
    @TableField("deptName")
            @ExcelField(value ="科室名称")
    private String deptName;



    public static final String ID ="id" ;

    public static final String PID ="pid" ;

    public static final String DEPTID ="deptId" ;

    public static final String DEPTNAME ="deptName" ;

@Override
public int compareTo(YbAppealSumdeptData o) {
        if (this.getId() != null && o.getId() != null) {
        return this.getId().compareTo(o.getId());
        } else if (this.getId() != null) {
        return 1;
        } else {
        return 0;
        }
        }
}