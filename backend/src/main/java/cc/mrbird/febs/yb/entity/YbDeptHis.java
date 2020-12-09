package cc.mrbird.febs.yb.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.wuwenze.poi.annotation.ExcelField;
import lombok.Data;

import java.io.Serializable;


@Data
public class YbDeptHis implements Serializable{


    /**
     * 部门编码
     */
    @TableField("deptId")
            @ExcelField(value ="科室编码")
    private String deptId;

    /**
     * 部门名称
     */
    @TableField("deptName")
            @ExcelField(value ="科室名称")
    private String deptName;

    /**
     * 拼写代码
     */
    @TableField("spellCode")
    @ExcelField(value ="拼写代码")
    private String spellCode;


        }