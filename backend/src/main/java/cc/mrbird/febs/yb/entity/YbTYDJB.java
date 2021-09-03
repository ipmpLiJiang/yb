package cc.mrbird.febs.yb.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;

@Data
@Accessors(chain = true)
//特药登记表
public class YbTYDJB implements Serializable {
    private  String id;
    @TableField("xm")
    private String xm;//姓名
    @TableField("bz")
    private String bz;//备注
    @TableField("basj")
    private String basj;//备案时间
    @TableField("xb")
    private String xb;//性别
    @TableField("ybkh")
    private String ybkh;//医保卡号
    @TableField("qzsj")
    private String  qzsj;//确诊时间
    @TableField("sfzh")
    private String sfzh;//身份证号
    @TableField("lxdh")
    private String lxdh;//联系电话
    @TableField("zd")
    private String zd;//诊断
    @TableField("tymc")
    private String tymc;//特药名称
    @TableField("spm")
    private String spm;//商品名
    @TableField("yyyj")
    private String yyyj;//用药依据
    @TableField("yfyl")
    private String yfyl;//用法用量
    @TableField("ks")
    private String ks;//科室
    @TableField("zrys")
    private String zrys;//责任医生
    @TableField("pgrq")
    private String pgrq;//评估日期
}
