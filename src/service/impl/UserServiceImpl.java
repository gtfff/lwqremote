package service.impl;

import service.UserService;
import pojo.User;
import utils.DaoFactory;
import utils.DataBaseConnection;
import utils.DataBaseConnectionFactory;

/**
 * @author Dzy
 * @version 1.0
 * @date 2020/7/4 16:02
 * @description UserDao业务层实现类
 */
public class UserServiceImpl implements UserService<User> {
    private DataBaseConnection dataBaseConnection;

    public UserServiceImpl() {
        dataBaseConnection = DataBaseConnectionFactory.getDataBaseConnection();
    }


    /**
     * @author Dzy
     * @date 2020/7/4 16:32
     * @description 注册用户
     * @param user 用户信息
     * @return boolean
     */
    @Override
    public boolean insertUser(User user) {
        try {
            return DaoFactory.getUserDaoInstance(dataBaseConnection.getConnection()).insertUser(user);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            this.dataBaseConnection.close();
        }
        return false;
    }

    /**
     * @author Dzy
     * @date 2020/7/4 16:32
     * @description 判断该账号是否存在
     * @param userId 用户Id
     * @return boolean
     */
    @Override
    public boolean findUserById(String userId) {
        try {
            return DaoFactory.getUserDaoInstance(dataBaseConnection.getConnection()).findUserById(userId);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            this.dataBaseConnection.close();
        }
        return false;
    }

    /**
     * @author Dzy
     * @date 2020/7/4 16:32
     * @description 修改用户密码
     * @param userId 用户Id
     * @param userPassword 用户密码
     * @return boolean
     */
    @Override
    public boolean updateUserPassword(String userId, String userPassword) {
        try {
            return DaoFactory.getUserDaoInstance(dataBaseConnection.getConnection()).updateUserPassword(userId, userPassword);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            this.dataBaseConnection.close();
        }
        return false;
    }

    /**
     * @author Dzy
     * @date 2020/7/4 16:32
     * @description 判断账号密码是否正确
     * @param userId 用户Id
     * @param userPassword 用户密码
     * @return boolean
     */
    @Override
    public boolean findUserPasswordById(String userId, String userPassword) {
        try {
            return DaoFactory.getUserDaoInstance(dataBaseConnection.getConnection()).findUserPasswordById(userId, userPassword);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            this.dataBaseConnection.close();
        }
        return false;
    }

    /**
     * @author Dzy
     * @date 2020/7/4 16:32
     * @description 查找用户信息
     * @param userId 用户Id
     * @return User
     */
    @Override
    public User findUserInformation(String userId) {
        try {
            return (User) DaoFactory.getUserDaoInstance(dataBaseConnection.getConnection()).findUserInformation(userId);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            this.dataBaseConnection.close();
        }
        return null;
    }
}
