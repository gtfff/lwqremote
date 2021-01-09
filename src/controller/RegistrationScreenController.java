package controller;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.stage.Stage;
import pojo.User;
import utils.Email;
import utils.ServiceFactory;


import java.net.URL;
import java.sql.Date;
import java.util.ResourceBundle;
import java.util.regex.Matcher;
import java.util.regex.Pattern;



/**
 * @author Dzy
 * @version 1.0
 * @date 2020/7/4 16:02
 * @description 注册界面控制器
 */
public class RegistrationScreenController implements Initializable {

    @FXML
    public Button datermine;

    @FXML
    private TextField name;

    @FXML
    private PasswordField password;

    @FXML
    private PasswordField confirmPassword;

    @FXML
    private RadioButton male;

    @FXML
    private RadioButton female;

    @FXML
    private DatePicker birthday;

    @FXML
    private TextField email;

    @FXML
    private TextField code;

    @FXML
    private TextField telephone;

    public static String msg = "";
    public static final String telepnumberRegular = "^1[3|4|5|6|7|8][0-9]{9}$";
    Pattern patternTelephone = Pattern.compile(telepnumberRegular);

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        //显示周次
        birthday.setShowWeekNumbers(true);
    }

    /**
     * @author Dzy
     * @date 2020/7/4 16:21
     * @description 选中性别为女性
     */
    @FXML
    private void turnOnFemale() {
        male.setSelected(false);
        female.setSelected(true);
    }

    /**
     * @author Dzy
     * @date 2020/7/4 16:21
     * @description 选中性别为男性
     */
    @FXML
    private void turnOnMale() {
        male.setSelected(true);
        female.setSelected(false);
    }

    /**
     * @author Dzy
     * @date 2020/7/4 16:20
     * @description 立即注册
     */
    @FXML
    void registerNow() {
    	System.out.println("点击注册");
        Matcher matcherTelephone = patternTelephone.matcher(telephone.getText());
//        boolean rsTelephone = matcherTelephone.matches();
        boolean rsTelephone = true;
        String userName = name.getText();
        String userPassword = password.getText();
        String confirmUserPassword = confirmPassword.getText();
        String userPhone = telephone.getText();
        String userSex = male.isSelected() ? "男" : "女";
        String userBirthday = birthday.getValue().toString();
        String userEmail = email.getText();
        String userCode = code.getText();
        if (!userPassword.equals(confirmUserPassword) || !userCode.equals(Email.codeNow) || !rsTelephone || userSex.equals("") || userBirthday.equals("") || userPhone.equals("") || userName.equals("") || userPassword.equals("") || confirmUserPassword.equals("") || userEmail.equals("") || userCode.equals("") || userPhone.equals("")) {
            if (userPhone.equals("") || userName.equals("") || userPassword.equals("") || confirmUserPassword.equals("") || userEmail.equals("") || userCode.equals("") || userSex.equals("") || userBirthday.equals("")) {
            	System.out.println(1);
            	msg = "您的信息还有部分没填入\n         请继续输入!";
            } else if (!userPassword.equals(confirmUserPassword)) {
            	System.out.println(2);
                msg = "您两次输入的密码不一致\n         请重新输入!";
            } else if (!userCode.equals(Email.codeNow)) {
            	System.out.println(3);
                msg = "您输入的验证码错误\n       请重新输入!";
            } else if (!rsTelephone) {
            	System.out.println(4);
                msg = "您输入的电话号码不正确\n         请重新输入!";
            }
        } else {
        	System.out.println(5);
            String userId;
            do {
                userId = (int)(Math.random() * 899999999 + 100000000) + "";
            } while (ServiceFactory.getUserServiceInstance().findUserById(userId));
            boolean result = ServiceFactory.getUserServiceInstance().insertUser(new User(userId, userPassword, userName, userSex, Date.valueOf(userBirthday), userPhone, userEmail));
            if(result) {
                msg = "注册成功,您的账号：" + userId;
                //关闭当前窗口
                try {
                    Stage stage = (Stage) datermine.getScene().getWindow();
                    stage.close();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            } else {
                msg = "注册失败";
            }

            PromptWindowsController.showPromptWindows(msg);
        }
    }

    /**
     * @author Dzy
     * @date 2020/7/4 16:22
     * @description 发送验证码
     */
    @FXML
    void sendVerificationCode() {
        Email.sendCode(email.getText());
    }
}
