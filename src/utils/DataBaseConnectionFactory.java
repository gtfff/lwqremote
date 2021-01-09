package utils;

/**
 * @author Dzy
 * @version 1.0
 * @date 2020/7/4 16:02
 * @description JDBC工厂类
 */
public class DataBaseConnectionFactory {
    public static DataBaseConnection getDataBaseConnection() {
        return new MysqlDataBaseConnection();
    }
}
