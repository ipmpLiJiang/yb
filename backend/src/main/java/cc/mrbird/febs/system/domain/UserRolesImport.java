package cc.mrbird.febs.system.domain;

import com.wuwenze.poi.annotation.Excel;
import com.wuwenze.poi.annotation.ExcelField;
import lombok.Data;

@Excel("用户信息")
@Data
public class UserRolesImport {

    //账号
    @ExcelField(value ="用户名")
    private String userName;

    //姓名
    @ExcelField(value ="姓名")
    private String xmname;

    //密码
    @ExcelField(value ="密码")
    private String password;

    //性别
    @ExcelField(value ="性别")
    private  String sex;

    //邮箱
    @ExcelField(value ="邮箱")
    private String email;

    //电话、手机
    @ExcelField(value ="联系电话")
    private String tel;

    //部门
    @ExcelField(value ="部门")
    private String deptName;

    //角色
    @ExcelField(value ="角色")
    private String roleName;
}