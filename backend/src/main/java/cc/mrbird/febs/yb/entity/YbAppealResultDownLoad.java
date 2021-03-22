package cc.mrbird.febs.yb.entity;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class YbAppealResultDownLoad {

    private  String key;

    /**
     * 部门
     */
    private String deptId;
    /**
     * 部门
     */
    private String deptName;
    /**
     * 文件名称
     */
    private String fileName;

}