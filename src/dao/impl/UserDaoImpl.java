package dao.impl;

import dao.UserDao;
import pojo.FileInformation;
import pojo.User;
import utils.MysqlDataBaseConnection;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Comparator;
import java.util.List;
import java.util.TreeSet;


/**
 * @author Dzy
 * @version 1.0
 * @date 2020/7/4 16:02
 * @description UserDao实现类
 */
public class UserDaoImpl implements UserDao<User> {

    private Connection conn = null;

    public UserDaoImpl(Connection conn) {
        this.conn = conn;
    }

    public UserDaoImpl() {
    }

    /**
     * @author Dzy
     * @date 2020/7/4 16:30
     * @description 注册用户
     * @param user 用户信息
     * @return boolean
     */
    @Override
    public boolean insertUser(User user) {
        String sql = "INSERT INTO user(userid, userpassword, username, usersex, userbirthday, userphone, useremail) VALUES(?, ?, ?, ?, ?, ?, ?)";
        int result = MysqlDataBaseConnection.execOther(conn, sql,
                new Object[]{user.getUserId(), user.getUserPassword(), user.getUserName(), user.getUserSex(), user.getUserBirthday(), user.getUserPhone(), user.getUserEmail()});
        return result > 0;
    }

    /**
     * @author Dzy
     * @date 2020/7/4 16:30
     * @description 判断该账号是否存在
     * @param userId 用户Id
     * @return boolean
     * @throws SQLException IO异常
     */
    @Override
    public boolean findUserById(String userId) throws SQLException {
        String sql = "SELECT * FROM user WHERE userid = ?";
        ResultSet rs = MysqlDataBaseConnection.execQuery(conn, sql,
                new Object[]{userId});
        return rs.next();
    }

    /**
     * @author Dzy
     * @date 2020/7/4 16:30
     * @description 修改用户密码
     * @param userId 用户Id
     * @param userPassword 用户密码
     * @return boolean
     */
    @Override
    public boolean updateUserPassword(String userId, String userPassword) {
        String sql = "UPDATE user SET userpassword=? WHERE userid=?";
        int result = MysqlDataBaseConnection.execOther(conn, sql,
                new Object[]{userPassword, userId});
        return result > 0;
    }

    /**
     * @author Dzy
     * @date 2020/7/4 16:31
     * @description 判断账号密码是否正确
     * @param userId 用户Id
     * @param userPassword 用户密码
     * @return boolean
     * @throws SQLException IO异常
     */
    @Override
    public boolean findUserPasswordById(String userId, String userPassword) throws SQLException {
        String sql = "SELECT userpassword FROM user WHERE userId = ?";
        ResultSet rs = MysqlDataBaseConnection.execQuery(conn,sql,
                new Object[]{userId});
        String password = null;
        while(rs.next()) {
            password = rs.getString("userpassword");
        }
        return userPassword.equals(password);
    }

    /**
     * @author Dzy
     * @date 2020/7/4 16:31
     * @description 查找用户信息
     * @param userId 用户Id
     * @return User
     * @throws SQLException IO异常
     */
    @Override
    public User findUserInformation(String userId) throws SQLException {
        String sql = "SELECT * FROM user WHERE userid = ?";
        ResultSet rs = MysqlDataBaseConnection.execQuery(conn, sql,
                new Object[]{userId});
        User user = null;
        while(rs.next()) {
            user = new User(userId, rs.getString("userpassword"), rs.getString("username"),
                    rs.getString("usersex"), rs.getDate("userbirthday"),
                    rs.getString("userphone"), rs.getString("userEmail"));
        }
        return user;
    }

    /**
     * @Description 文件排序
     * @param list 文件内容
     * @param num 判断用使用谁来排序
     * @return java.util.TreeSet<pojo.FileInformation>
     * @Author Dzy
     * @Date 2020/7/6 10:37
     */
    @Override
    public TreeSet<FileInformation> FileSort(List<FileInformation> list, int num) {
        TreeSet<FileInformation> treeSet = new TreeSet<>(new Comparator<FileInformation>() {
            @Override
            public int compare(FileInformation o1, FileInformation o2) {
                if(num == 1) {
                    return o1.getFileName().compareTo(o2.getFileName());
                } else if(num == 2) {
                    return o1.getFileTime().compareTo(o2.getFileTime());
                } else {
                    return o1.getFileSize().compareTo(o2.getFileSize());
                }
            }
        });
        treeSet.addAll(list);
        return treeSet;
    }
}
