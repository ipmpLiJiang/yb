package cc.mrbird.febs.yb.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;

@Data
@Accessors(chain = true)
//重症登记表
public class YbZZDJB implements Serializable {
    private  String id;
    @TableField("zzbh")
    private String zzbh;//	重症编号
    @TableField("xm")
    private String xm;//姓名
    @TableField("ybxz")
    private String ybxz;//医保性质
    @TableField("ybkh")
    private String ybkh;//医保卡号
    @TableField("bzbh")
    private String bzbh;//病种编号
    @TableField("bzmc")
    private String bzmc;//病种名称
    @TableField("lxfs")
    private String lxfs;//联系方式
    @TableField("zrsj")
    private String zrsj;//转入时间
    @TableField("xq")
    private String xq;//辖区
    @TableField("ybjbr")
    private String ybjbr;//医保办经办人
    @TableField("zcsj")
    private String zcsj;//转出时间
    @TableField("bz")
    private String bz;//备注
    @TableField("zcjbr")
    private String zcjbr;//转出经办人

}
