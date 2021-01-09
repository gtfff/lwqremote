package view;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.File;
import java.io.IOException;

public class SelectPathScreen extends Application {

    @Override
    public void start(Stage primaryStage) throws IOException {
        Parent selectPathScreen = FXMLLoader.load(getClass().getResource("fxml/SelectPathScreen.fxml"));
        primaryStage.setTitle("选择路径");
        primaryStage.setScene(new Scene(selectPathScreen, 772, 601));
        primaryStage.show();
    }
}
