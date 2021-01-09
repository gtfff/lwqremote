package controller;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import pojo.User;
import utils.Email;
import utils.ServiceFactory;
import view.ChangePasswordScreen;

import java.io.IOException;



/**
 * @author Dzy
 * @version 1.0
 * @date 2020/7/4 16:02
 * @description 找回密码界面控制器
 */
public class RetrievePasswordScreenController {

    @FXML
    private TextField account;

    @FXML
    private TextField email;

    @FXML
    private TextField code;

    @FXML
    private Button datermine;

    public static User user = null;

    /**
     * @author Dzy
     * @date 2020/7/4 16:23
     * @description 判断是否注册成功
     */
    @FXML
    void SuccessfullyModified() {
        String userId = account.getText();
        String userEmail = email.getText();
        String userCode = code.getText();
        if (userId.equals("")  || userEmail.equals("") || userCode.equals("")) {
            String msg = "你的信息并未输入完整\n        请重新输入!";
            PromptWindowsController.showPromptWindows(msg);
        } else {
            if (!Email.codeNow.equals(userCode)  || !user.getUserEmail().equals(userEmail)) {
                String msg = "你输入的信息有误\n     请重新输入!";
                PromptWindowsController.showPromptWindows(msg);
            } else {
                //打开更改密码界面，关闭当前窗口
                try {
                    Stage stage = (Stage) datermine.getScene().getWindow();
                    stage.close();
                    new ChangePasswordScreen().start(new Stage());
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    /**
     * @author Dzy
     * @date 2020/7/4 16:23
     * @description 发送验证码
     */
    @FXML
    void sendVerificationCode() {
        String userId = account.getText();
        String userEmail = email.getText();

        if (userId.equals("")  || userEmail.equals("")) {
            String msg = "你的信息并未输入完整\n        请重新输入!";
            PromptWindowsController.showPromptWindows(msg);
        } else {
            user = (User) ServiceFactory.getUserServiceInstance().findUserInformation(userId);
            if(user.getUserEmail().equals(userEmail)) {
                Email.sendCode(userEmail);
            } else {
                String msg = "该用户所对应的邮箱信息不匹配！\n            请重新输入!";
                PromptWindowsController.showPromptWindows(msg);
            }
        }

    }
}
