package cc.mrbird.febs.common.utils;

import java.io.Serializable;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;
import lombok.extern.slf4j.Slf4j;

/**
 * @author lijiang
 * @createDate 2020/11/20
 */
@Slf4j
public class OracleDB<T extends Serializable> {

    public List<T> excuteSqlRS(T t, String sql) {
        List<T> resultList = new ArrayList<>();
        Connection con = null;// 创建一个数据库连接
        PreparedStatement pre = null;// 创建预编译语句对象，一般都是用这个而不用Statement
        ResultSet result = null;// 创建一个结果集对象
        try {
            Class.forName("oracle.jdbc.driver.OracleDriver");// 加载Oracle驱动程序
            String url = "jdbc:oracle:thin:@(DESCRIPTION=(ADDRESS_LIST=(ADDRESS=(PROTOCOL=TCP)(HOST=192.168.60.220)(PORT=1521)))"
                    + "(CONNECT_DATA=(SERVICE_NAME=HIS)))";
            String user = "xhhis_yb";
            String password = "WHXH2020";

            con = DriverManager.getConnection(url, user, password);// 获取连接
            //String sql = "SELECT * FROM Table where id = ?";// 预编译语句，“？”代表参数
            //sql = "SELECT *  FROM his.V_SAP_INPFEES WHERE transno = '579988318-JSXH676208373' and ROWNUM < 100";
            pre = con.prepareStatement(sql);// 实例化预编译语句
            //pre.setString(1, "2199966");// 设置参数，前面的1表示参数的索引，而不是表中列名的索引
            result = pre.executeQuery();// 执行查询，注意括号中不需要再加参数
            if (result != null) {
                resultList = (List<T>) ResultSetToBean.putResult(result, t.getClass());
            }
        } catch (Exception e) {
            e.printStackTrace();
            log.error("His连接、读取数据异常",e);
        } finally {
            try {
                // 逐一将上面的几个对象关闭，因为不关闭的话会影响性能、并且占用资源
                // 注意关闭的顺序，最后使用的最先关闭
                if (result != null)
                    result.close();
                if (pre != null)
                    pre.close();
                if (con != null)
                    con.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return resultList;
    }

}
