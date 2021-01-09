package controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
import pojo.FileInformation;
import utils.Context;

import java.io.File;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.ResourceBundle;



/**
 * @author Dzy
 * @version 1.0
 * @date 2020/7/4 16:02
 * @description 选择界面控制器
 */
public class SelectPathScreenController implements Initializable {

    @FXML
    private TableView<FileInformation> tableView;

    @FXML
    private TableColumn fileNameColumn;

    @FXML
    private TableColumn fileTimeColumn;

    @FXML
    private TableColumn fileSizeColumn;

    @FXML
    private Button determine;

    private ObservableList<FileInformation> observableList = FXCollections.observableArrayList();

    public static StringBuilder path = new StringBuilder();
    public static String savePath = null;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        fileNameColumn.setCellValueFactory(new PropertyValueFactory("fileNameString"));
        fileTimeColumn.setCellValueFactory(new PropertyValueFactory("fileTimeString"));
        fileSizeColumn.setCellValueFactory(new PropertyValueFactory("fileSizeString"));
        loadFile();
    }

    /**
     * @author Dzy
     * @date 2020/7/4 16:23
     * @description 杩斿洖涓婁竴绾х洰褰�
     */
    @FXML
    void goBackUp() {
        int index = path.lastIndexOf("\\", path.length() - 2);
        path.delete(index + 1, path.length());
        loadFile();
    }

    /**
     * @author Dzy
     * @date 2020/7/4 16:23
     * @description 鐐瑰嚮姝ゆ寜閽紝鍒欏紑濮嬩笅杞芥鏂囦欢
     */
    @FXML
    void complete() {
        FileInformation fileInformation = tableView.getSelectionModel().getSelectedItem();
        if(fileInformation != null) {
            savePath = path + fileInformation.getFileName();
            //涓嬭浇鏂囦欢
            Context.client.send("downloadFile|" + LoginScreenController.user.getUserId() + "|" + MainScreenController.downloadPath);
            Stage stage = (Stage) determine.getScene().getWindow();
            stage.close();
            PromptWindowsController.showPromptWindows("寮�濮嬩笅杞�");
        }
    }

    /**
     * @author Dzy
     * @date 2020/7/4 16:24
     * @description 榧犳爣鐩戝惉浜嬩欢
     * @param event 榧犳爣鐩戝惉瀵硅薄
     */
    @FXML
    void onMouseClick(MouseEvent event) {
        if(event.getClickCount() == 2) {
            FileInformation fileInformation = tableView.getSelectionModel().getSelectedItem();
            if(fileInformation != null) {
                path.append(fileInformation.getFileName()).append("\\");
                loadFile();
            }
        }
    }

    /**
     * @author Dzy
     * @date 2020/7/4 16:24
     * @description 鍔犺浇鏂囦欢淇℃伅
     */
    private void loadFile() {
        if(path.length() == 0) {
            observableList.clear();
            observableList.add(new FileInformation("C:", null, null));
            observableList.add(new FileInformation("D:", null, null));
            observableList.add(new FileInformation("E:", null, null));
            observableList.add(new FileInformation("F:", null, null));
            tableView.setItems(observableList);
        } else {
            File file = new File(path.toString());
            if(file.isDirectory()) {
                File[] fileList = file.listFiles();
                observableList.clear();
                for(File f : fileList) {
                    if(f.isDirectory()) {
                        String fileName = f.getName();
                        long lastModified = f.lastModified();
                        Date date = new Date(lastModified);
                        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                        String fileTime = sdf.format(date);
                        observableList.add(new FileInformation(fileName, fileTime, null));
                    }
                }
                tableView.setItems(observableList);
            } else {
                String msg = "璇ユ枃浠朵笉鏄枃浠跺す锛�";
                PromptWindowsController.showPromptWindows(msg);
                goBackUp();
            }
        }
    }
}
