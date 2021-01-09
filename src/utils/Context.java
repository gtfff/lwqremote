package utils;


import dao.impl.UserDaoImpl;
import org.junit.Test;
import pojo.FileInformation;
import service.impl.UserServiceImpl;

import java.io.File;
import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.*;


/**
 * @author Dzy
 * @version 1.0
 * @date 2020/7/4 16:02
 * @description 集合类
 */
public class Context {
    public static Map<String, Object> contorllersMap = new HashMap<>();
    public static Client client;

    //添加10000条数据
    @Test
    public void test() throws IOException {
        for(int i = 0; i < 10000; i++) {
            File file = new File("disk/887195376/" + i + ".txt");
            file.createNewFile();
        }
    }

    //测试数据库是否连通
    @Test
    public void test1() throws SQLException {
        Connection conn = DataBaseConnectionFactory.getDataBaseConnection().getConnection();
        System.out.println(conn);
        conn.close();
    }

    //测试排序是否成功
    @Test
    public void test2() {
        FileInformation f1 = new FileInformation("1", "333", "333");
        FileInformation f2 = new FileInformation("2", "332", "444");
        List<FileInformation> list = new ArrayList<>();
        list.add(f1);
        list.add(f2);
        TreeSet<FileInformation> treeSet = new UserDaoImpl().FileSort(list, 1);
        for(FileInformation f : treeSet) {
            System.out.println(f);
        }
        System.out.println("----------------------------");
        treeSet = new UserDaoImpl().FileSort(list, 2);
        for(FileInformation f : treeSet) {
            System.out.println(f);
        }
    }

}
