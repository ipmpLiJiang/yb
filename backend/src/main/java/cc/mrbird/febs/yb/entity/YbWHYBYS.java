package cc.mrbird.febs.yb.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;

@Data
@Accessors(chain = true)
//武汉市医保应收明细表
public class YbWHYBYS implements Serializable {
    private  String id;
    @TableField("zyq")
    private String zyq;//院区
    @TableField("iperi")
    private String iperi;//周期
    @TableField("ybfl")
    private String ybfl;//医保类型
    @TableField("jsxq")
    private String jsxq;//辖区
    @TableField("yshj")
    private String yshj;//应收合计
    @TableField("hkhj")
    private String hkhj;//回款合计
    @TableField("t1T21")
    private String t1T21;//基本医疗-统1+统2
    @TableField("grzh2")
    private String grzh2;//基本医疗-个人账户、门诊统筹
    @TableField("hke12")
    private String hke12;//基本医疗-回款额
    @TableField("hkrq12")
    private String hkrq12;//基本医疗-回款时间
    @TableField("ysk3")
    private String ysk3;//公务员补贴-应收款
    @TableField("hke3")
    private String hke3;//公务员补贴-回款额
    @TableField("hkrq3")
    private String hkrq3;//公务员补贴-回款时间
    @TableField("ysk4")
    private String ysk4;//大额费用、大病保险-应收款
    @TableField("hke4")
    private String hke4;//大额费用、大病保险-回款额
    @TableField("hkrq4")
    private String hkrq4;//大额费用、大病保险-回款时间
    @TableField("ysk5")
    private String ysk5;//门诊个人账户-应收款
    @TableField("hke5")
    private String hke5;//门诊个人账户-回款额
    @TableField("hkrq5")
    private String hkrq5;//门诊个人账户-回款时间


}
