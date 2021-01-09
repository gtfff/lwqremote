package utils;

import java.sql.Connection;

/**
 * @author Dzy
 * @version 1.0
 * @date 2020/7/4 16:02
 * @description JDBC接口
 */
public interface DataBaseConnection {
    Connection getConnection();
    void close();
}
