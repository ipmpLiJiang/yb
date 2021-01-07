package cc.mrbird.febs.common.utils;

import java.io.IOException;
import java.io.InputStream;
import java.io.Serializable;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import cc.mrbird.febs.cn.webxml.sms.TestSend;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.dbcp2.BasicDataSourceFactory;

import javax.sql.DataSource;

/**
 * @author lijiang
 * @createDate 2020/11/20
 */
@Slf4j
public class OracleDB<T extends Serializable> {
    // 根据配置文件里的名称创建连接池
    private static DataSource source = null;
    public static Properties pros = null;
    static {
        pros = new Properties();
        InputStream is = OracleDB.class.getClassLoader().getResourceAsStream("db_oracle.properties");
        try {
            pros.load(is);
            source = BasicDataSourceFactory.createDataSource(pros);
        } catch (Exception e) {
            e.printStackTrace();
            log.error("配置文件读取异常",e);
        }finally{
            try {
                is.close();
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
    }

    public List<T> excuteSqlRS(T t, String sql) throws Exception {
        List<T> resultList = new ArrayList<>();
        if (sql == null || sql.trim() == "") {
            return resultList;
        }

        Connection con = null;// 创建一个数据库连接
        PreparedStatement pre = null;// 创建预编译语句对象，一般都是用这个而不用Statement
        ResultSet result = null;// 创建一个结果集对象
        boolean isErr = false;
        try {
            con = source.getConnection();
            //String sql = "SELECT * FROM Table where id = ?";// 预编译语句，“？”代表参数
            pre = con.prepareStatement(sql);// 实例化预编译语句
            //pre.setString(1, "2199966");// 设置参数，前面的1表示参数的索引，而不是表中列名的索引
            result = pre.executeQuery();// 执行查询，注意括号中不需要再加参数
            if (result != null) {
                resultList = (List<T>) ResultSetToBean.putResult(result, t.getClass());
            }
        } catch (Exception e) {
            isErr = true;
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
        if(isErr) {
            throw new Exception("his接口执行异常");
        }
        return resultList;
    }

}
