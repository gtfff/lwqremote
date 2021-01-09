package view;

import javafx.application.Application;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;

import java.io.IOException;

public class LoginScreen extends Application {
 

    @Override
    public void start(Stage primaryStage) throws IOException {
        //
        Parent loginScreen = FXMLLoader.load(getClass().getResource("fxml/LoginScreen.fxml"));
        //
        primaryStage.setTitle("登录");
        primaryStage.setScene(new Scene(loginScreen, 550, 370));
        primaryStage.show();

        //
        primaryStage.setOnCloseRequest(new EventHandler<WindowEvent>() {
            @Override
            public void handle(WindowEvent event) {
                System.exit(0);
            }
        });
    }


    public static void main(String[] args) {
        launch(args);
    }
}
