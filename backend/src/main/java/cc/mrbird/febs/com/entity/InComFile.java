package cc.mrbird.febs.com.entity;

import lombok.Data;

/**
 * @author lijiang
 * @createDate 2020/8/7
 */
@Data
public class InComFile {
    private String pid;//外键Id
    private String refTab;//外键表
    private String proposalCode;//意见书编码 上传图片名称
    private String deptName;//部门名称 按照部门上传图片
    private String area;// 按照区域上传图片
    private String applyDateStr;//年月
    private Integer type;//审核一或审核二
}
