package controller;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import pojo.User;
import utils.Client;
import utils.Context;
import utils.ServiceFactory;
import view.MainScreen;
import view.RegistrationScreen;
import view.RetrievePasswordScreen;

import java.io.IOException;



/**
 * @author Dzy
 * @version 1.0
 * @date 2020/7/4 16:02
 * @description 登录界面控制器
 */
public class LoginScreenController {

    @FXML
    private TextField account;

    @FXML
    private Button login;

    @FXML
    private PasswordField password;

    public static User user = null;

    public LoginScreenController() {
        Context.contorllersMap.put(this.getClass().getSimpleName(), this);
    }


    /**
     * @author Dzy
     * @date 2020/7/4 16:14
     * @description 判断是否登录成功
     */
    @FXML
    private void LoginSuccess() {
        String userId = account.getText();
        String userPassword = password.getText();

        if (userId.equals("") || userPassword.equals("")) {
            String msg = "用户名或者密码为空白!\n        请输入完整!";
            PromptWindowsController.showPromptWindows(msg);
        } else {
            boolean flag = ServiceFactory.getUserServiceInstance().findUserPasswordById(userId, userPassword);
            if(flag) {
                user = new User();
                user.setUserId(userId);
                user.setUserPassword(userPassword);
                //关闭当前窗口,打开主界面
                connectServer(userId);
                try {
                    new MainScreen().start(new Stage());
                    Stage stage = (Stage) login.getScene().getWindow();
                    stage.close();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            } else {
                String msg = "用户名或者密码错误!\n       请重新输入!";
                PromptWindowsController.showPromptWindows(msg);
            }
        }
    }

    /**
     * @author Dzy
     * @date 2020/7/4 16:11
     * @description 用户连接服务器
     * @param userId 用户Id
     */
    public static void connectServer(String userId) {
    	 System.out.println("userId====="+userId);
        Context.client = new Client();
        System.out.println("Context.client====="+Context.client);
        System.out.println("userId:"+userId);
        Context.client.send(userId);
    }

    /**
     * @author Dzy
     * @date 2020/7/4 16:14
     * @description 打开注册界面
     */
    @FXML
    private void RegistrationScreen() {
        try {
            new RegistrationScreen().start(new Stage());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * @author Dzy
     * @date 2020/7/4 16:14
     * @description 打开找回密码界面
     */
    @FXML
    private void RetrievePasswordScreen() {
        try {
            new RetrievePasswordScreen().start(new Stage());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


}
