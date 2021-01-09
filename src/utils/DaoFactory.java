package utils;


import dao.UserDao;
import dao.impl.UserDaoImpl;

import java.sql.Connection;

/**
 * @author Dzy
 * @version 1.0
 * @date 2020/7/4 16:02
 * @description Dao层工厂类
 */
public class DaoFactory {
    public static UserDao getUserDaoInstance(Connection conn) {
        return new UserDaoImpl(conn);
    }
}
