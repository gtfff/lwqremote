package utils;

import java.io.FileReader;
import java.sql.*;
import java.util.Properties;

/**
 * @author Dzy
 * @version 1.0
 * @date 2020/7/4 16:02
 * @description 鏁版嵁搴撶被
 */
public class MysqlDataBaseConnection implements DataBaseConnection {
    public static String DRIVER = "com.mysql.cj.jdbc.Driver";
    public static String URL = "jdbc:mysql://localhost:3306/remotefiletransfer?serverTimezone=GMT%2B8";
    public static String USER = "root";
    public static String PASSWORD = "root";

    private Connection conn = null;

    public MysqlDataBaseConnection() {
        try {
            Properties pro=new Properties();
            pro.load(new FileReader("C:\\Users\\dell\\eclipse-workspace\\RemoteFile\\src\\JDBC.properties"));
            DRIVER=pro.getProperty("driver");
            URL=pro.getProperty("url");
            USER=pro.getProperty("user");
            PASSWORD=pro.getProperty("password");
            Class.forName(DRIVER);
            this.conn = DriverManager.getConnection(URL, USER, PASSWORD);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    /**
     * @author Dzy
     * @date 2020/7/4 16:39
     * @description 鑾峰彇鏁版嵁搴撹繛鎺�
     * @return Connection
     */
    @Override
    public Connection getConnection() {
        return this.conn;
    }

    /**
     * @author Dzy
     * @date 2020/7/4 16:39
     * @description 鍏抽棴鏁版嵁搴撹繛鎺�
     */
    @Override
    public void close() {
        if(this.conn != null) {
            try {
                this.conn.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * @author Dzy
     * @date 2020/7/4 16:40
     * @description 鏁版嵁搴撶殑澧炪�佸垹銆佹敼绛夋搷浣�
     * @param conn Connection杩炴帴瀵硅薄
     * @param sql sql鎵ц璇彞
     * @param arr 闇�瑕佹墽琛岀殑鍊�
     * @return int
     */
    public static int execOther(Connection conn, String sql, Object[] arr) {
        System.out.println("SQL:>" + sql);
        try {
            PreparedStatement ps = conn.prepareStatement(sql);
            for (int i = 0; i < arr.length; i++) {
                ps.setObject(i + 1, arr[i]);
            }
            int result = ps.executeUpdate();
            return result;
        } catch (SQLException e) {
            e.printStackTrace();
            return -1;
        }
    }

    /**
     * @author Dzy
     * @date 2020/7/4 16:40
     * @description 鏁版嵁搴撴煡璇㈡搷浣�
     * @param conn Connection杩炴帴瀵硅薄
     * @param sql sql鎵ц璇彞
     * @param arr 闇�瑕佹墽琛岀殑鍊�
     * @return ResultSet
     */
    public static ResultSet execQuery(Connection conn, String sql, Object[] arr) {
        System.out.println("SQL:>" + sql);
        try {
            PreparedStatement ps = conn.prepareStatement(sql);
            for (int i = 0; i < arr.length; i++) {
                ps.setObject(i + 1, arr[i]);
            }
            ResultSet rs = ps.executeQuery();
            return rs;
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }
}
