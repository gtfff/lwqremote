package utils;

import service.UserService;
import service.impl.UserServiceImpl;

/**
 * @author Dzy
 * @version 1.0
 * @date 2020/7/4 16:02
 * @description Dao业务层工厂类
 */
public class ServiceFactory {
    public static UserService getUserServiceInstance() {
        return new UserServiceImpl();
    }
}
