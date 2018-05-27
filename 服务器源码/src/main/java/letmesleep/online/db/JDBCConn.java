package letmesleep.online.db;

import java.sql.*;

/**
 * Created by Letmesleep on 2018/4/11.
 */
public class JDBCConn {
    public static final String DBDRIVER = "org.gjt.mm.mysql.Driver" ;
    // 定义MySQL数据库的连接地址
    public static final String DBURL = "jdbc:mysql://localhost:3306/   your database and charset" ;
    // MySQL数据库的连接用户名
    public static final String DBUSER = "your mysql username" ;
    // MySQL数据库的连接密码
    public static final String DBPASS = "your password" ;

    public static Connection conn = null ;

    public static void getConn(){
        if(conn!=null)
            return;
        try {
            Class.forName(DBDRIVER) ;	// 加载驱动程序
            conn = DriverManager.getConnection(DBURL,DBUSER,DBPASS) ;
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (SQLException e) {
            e.printStackTrace();
        }

    }

    public static void execSql(String sql){
        getConn();
        Statement stmt = null ;			// 数据库操作

        try {
            stmt = conn.createStatement() ;	// 实例化Statement对象
            stmt.execute(sql) ;		// 执行数据库更新操作
            stmt.close() ;					// 关闭操作
        } catch (SQLException e) {
            e.printStackTrace();
        }

    }

    public static boolean login(String username,String password){
        getConn();
        boolean statue = false;
        Statement stmt = null ;			// 数据库操作
        String sql = "SELECT id,name,password FROM user where name='"+username+"' and password='"+password+"'";
        ResultSet rs = null ;
        try {
            stmt = conn.createStatement() ;	// 实例化Statement对象
            rs = stmt.executeQuery(sql) ;		// 执行数据库更新操作

            while(rs.next()){	// 依次取出数据
                statue = true;
            }
            rs.close() ;
            stmt.close() ;

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
        return statue;
    }

    public static void main(String[] args) {
        getConn();
    }
}
