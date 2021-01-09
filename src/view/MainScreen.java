package view;

import controller.LoginScreenController;
import controller.MainScreenController;
import javafx.application.Application;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import utils.Context;

public class MainScreen extends Application {
    @Override
    public void start(Stage primaryStage) throws Exception {
        //
        Parent mainScreen = FXMLLoader.load(getClass().getResource("fxml/MainScreen.fxml"));
        //
        primaryStage.setTitle("文件管理器");
        primaryStage.setScene(new Scene(mainScreen, 1021, 798));
        primaryStage.show();

        //
        primaryStage.setOnCloseRequest(new EventHandler<WindowEvent>() {
            @Override
            public void handle(WindowEvent event) {
                Context.client.close(LoginScreenController.user.getUserId());
                System.exit(0);
            }
        });
    }
}
