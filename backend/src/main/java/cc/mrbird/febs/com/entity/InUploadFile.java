package cc.mrbird.febs.com.entity;

import lombok.Data;

import java.io.Serializable;

/**
 * @author lijiang
 * @createDate 2020/8/7
 */
@Data
public class InUploadFile implements Serializable {
    private String id;//外键Id
    private String refTab;//外键表
    private String proposalCode;//意见书编码 上传图片名称
    private String deptName;
    private String applyDateStr;
    private String serName;
    private Integer typeno;
    private String suffix;
    private String fileName;
    private  Integer state;
    private  Integer dataType;
    private  Integer sourceType;
}
