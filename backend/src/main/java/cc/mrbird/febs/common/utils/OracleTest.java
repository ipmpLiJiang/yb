package cc.mrbird.febs.common.utils;

/**
 * @author lijiang
 * @createDate 2020/11/19
 */

import java.io.Serializable;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * * 引入jar包ojdbc5.jar
 */
public class OracleTest<T extends Serializable> {


    public static void main(String args[]) {
        /*
        OracleTest<YbReconsiderInpatientfeesHis> oracleTest = new OracleTest<YbReconsiderInpatientfeesHis>();
        //String sql = "SELECT * FROM his.V_SAP_INPFEES where INPATIENTID = '2199966' and rownum < 2";
        //String sql = "select DISTINCT TransNo,BillNo,ItemCode,ItemName from his.V_SAP_INPFEES where settlementdate >= TO_DATE('2020-10-01', 'yyyy-mm-dd') and settlementdate <= TO_DATE('2020-10-31', 'yyyy-mm-dd') and  ROWNUM <10";
        String sql = "select * from his.V_SAP_INPFEES where settlementdate >= TO_DATE('2020-10-01', 'yyyy-mm-dd') and settlementdate <= TO_DATE('2020-10-31', 'yyyy-mm-dd') and  ROWNUM <100";
        List<YbReconsiderInpatientfeesHis> list = oracleTest.excuteSqlRS(new YbReconsiderInpatientfeesHis(),sql);
        String whereSql = "";
        String whereSql1 = "";
        List<String> strList = new ArrayList<>();
        List<String> strList1 = new ArrayList<>();
        for(YbReconsiderInpatientfeesHis item : list){
            //String str = item.getTransNo()+"-"+item.getItemCode();
            String str = item.getTransNo();
            if(strList.stream().filter(s -> s.equals(str)).count()==0) {
                strList.add(str);
                if (whereSql.equals("")) {
                    whereSql = "'" + item.getTransNo() + "'";
                    //whereSql = "(transno = '" + item.getTransNo() + "' and itemcode = '" + item.getItemCode() + "')";
                } else {
                    whereSql += ",'" + item.getTransNo() + "'";
                    //whereSql += "or (transno = '" + item.getTransNo() + "' and itemcode = '" + item.getItemCode() + "')";
                }
            }
        }
        for(YbReconsiderInpatientfeesHis item : list){
            //String str = item.getTransNo()+"-"+item.getItemCode();
            String str = item.getItemCode();
            if(strList1.stream().filter(s -> s.equals(str)).count()==0) {
                strList1.add(str);
                if (whereSql1.equals("")) {
                    whereSql1 = "'" + item.getItemCode() + "'";
                    //whereSql = "(transno = '" + item.getTransNo() + "' and itemcode = '" + item.getItemCode() + "')";
                } else {
                    whereSql1 += ",'" + item.getItemCode() + "'";
                    //whereSql += "or (transno = '" + item.getTransNo() + "' and itemcode = '" + item.getItemCode() + "')";
                }
            }
        }
        System.out.println(whereSql);
        System.out.println(whereSql1);
        */
    }

    public List<T> excuteSqlRS(T t,String sql) {
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
            pre = con.prepareStatement(sql);// 实例化预编译语句
            //pre.setString(1, "2199966");// 设置参数，前面的1表示参数的索引，而不是表中列名的索引
            result = pre.executeQuery();// 执行查询，注意括号中不需要再加参数

            resultList = (List<T>) ResultSetToBean.putResult(result, t.getClass());
        } catch (Exception e) {
            e.printStackTrace();
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
