package service;

import pojo.User;


/**
 * @author Dzy
 * @version 1.0
 * @date 2020/7/4 16:02
 * @description UserDao业务层
 */
public interface UserService<T> {
    /**
     * @author Dzy
     * @date 2020/7/4 16:31
     * @description 注册用户
     * @param user 用户信息
     * @return boolean
     */
    boolean insertUser(T user);

    /**
     * @author Dzy
     * @date 2020/7/4 16:31
     * @description 判断该账号是否存在
     * @param userId 用户Id
     * @return boolean
     */
    boolean findUserById(String userId);

    /**
     * @author Dzy
     * @date 2020/7/4 16:32
     * @description 修改用户密码
     * @param userId 用户id
     * @param userPassword 用户密码
     * @return boolean
     */
    boolean updateUserPassword(String userId, String userPassword);

    /**
     * @author Dzy
     * @date 2020/7/4 16:32
     * @description 判断账号密码是否正确
     * @param userId 用户Id
     * @param userPassword 用户密码
     * @return boolean
     */
    boolean findUserPasswordById(String userId, String userPassword);

    /**
     * @author Dzy
     * @date 2020/7/4 16:32
     * @description 查找用户信息
     * @param userId 用户Id
     * @return User
     */
    T findUserInformation(String userId);
}
