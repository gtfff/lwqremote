package dao;


import pojo.FileInformation;

import java.sql.SQLException;
import java.util.List;
import java.util.TreeSet;

/**
 * @author Dzy
 * @version 1.0
 * @date 2020/7/4 16:02
 * @description 用户Dao接口
 */
public interface UserDao<T> {
    /**
     * @author Dzy
     * @date 2020/7/4 16:26
     * @description 注册用户
     * @param user 用户的信息
     * @return boolean
     */
    boolean insertUser(T user);

    /**
     * @author Dzy
     * @date 2020/7/4 16:26
     * @description 判断该账号是否存在
     * @param userId 用户的id
     * @return boolean
     * @throws SQLException IO异常
     */
    boolean findUserById(String userId) throws SQLException;

    /**
     * @author Dzy
     * @date 2020/7/4 16:26
     * @description 修改用户密码
     * @param userId 用户的Id
     * @param userPassword 用户的密码
     * @return boolean
     */
    boolean updateUserPassword(String userId, String userPassword);

    /**
     * @author Dzy
     * @date 2020/7/4 16:26
     * @description 判断账号密码是否正确
     * @param userId 用户的Id
     * @param userPassword 用户的密码
     * @return boolean
     * @throws SQLException IO异常
     */
    boolean findUserPasswordById(String userId, String userPassword) throws SQLException;


    /**
     * @author Dzy
     * @date 2020/7/4 16:27
     * @description 查找用户信息
     * @param userId 用户的Id
     * @return User
     * @throws SQLException IO异常
     */
    T findUserInformation(String userId) throws SQLException;


    /**
     * @Description 文件排序
     * @param list 文件内容
     * @param num 判断用使用谁来排序
     * @return java.util.TreeSet<pojo.FileInformation>
     * @Author Dzy
     * @Date 2020/7/6 10:37
     */
    TreeSet<FileInformation> FileSort(List<FileInformation> list, int num);
}
