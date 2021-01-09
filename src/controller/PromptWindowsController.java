package controller;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.stage.Stage;
import view.PromptWindows;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;


/**
 * @author Dzy
 * @version 1.0
 * @date 2020/7/4 16:02
 * @description 提示界面控制器
 */
public class PromptWindowsController implements Initializable {

    @FXML
    private Button datermine;

    @FXML
    private Label label;

    private static String strOutput;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        label.setText(strOutput);
    }

    /**
     * @author Dzy
     * @date 2020/7/5 9:27
     * @description 设置需要输出的文本信息
     * @param str 文本信息
     */
    public void ReceiveOutputInformation(String str) {
        strOutput = str;
    }

    /**
     * @author Dzy
     * @date 2020/7/4 16:20
     * @description 关闭当前窗口
     */
    @FXML
    public void WindowClose() {
        try {
            Stage stage = (Stage) datermine.getScene().getWindow();
            stage.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * @author Dzy
     * @date 2020/7/4 16:20
     * @description 其他界面通过调用这个方法来打开提示界面
     * @param msg 需要输出的文本信息
     */
    public static void showPromptWindows(String msg) {
        try {
            new PromptWindowsController().ReceiveOutputInformation(msg);
            new PromptWindows().start(new Stage());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
