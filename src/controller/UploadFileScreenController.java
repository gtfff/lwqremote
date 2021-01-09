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
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.ResourceBundle;


/**
 * @author Dzy
 * @version 1.0
 * @date 2020/7/4 16:02
 * @description 上传文件界面控制器
 */
public class UploadFileScreenController implements Initializable {

    @FXML
    private Button uploadButton;

    @FXML
    private TableView<FileInformation> tableView;

    @FXML
    private TableColumn fileNameColumn;

    @FXML
    private TableColumn fileTimeColumn;

    @FXML
    private TableColumn fileSizeColumn;

    private ObservableList<FileInformation> observableList = FXCollections.observableArrayList();

    public static StringBuilder path = new StringBuilder();

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        fileNameColumn.setCellValueFactory(new PropertyValueFactory("fileNameString"));
        fileTimeColumn.setCellValueFactory(new PropertyValueFactory("fileTimeString"));
        fileSizeColumn.setCellValueFactory(new PropertyValueFactory("fileSizeString"));
        loadFile();
    }


    /**
     * @author Dzy
     * @date  16:24
     * @description 上传文件
     */
    @FXML
    void upload() {
        FileInformation fileInformation = tableView.getSelectionModel().getSelectedItem();
        if(fileInformation != null) {
            Context.client.sendFile(LoginScreenController.user.getUserId(), fileInformation.getFileName(), path.toString(), MainScreenController.uploadPath);
            MainScreenController.uploadPath = "null";
            Stage stage = (Stage) uploadButton.getScene().getWindow();
            stage.close();
        }
    }

    /**
     * @author Dzy
     * @date 2020/7/4 16:25
     * @description 返回上一级目录
     */
    @FXML
    void goBackUp() {
        int index = path.lastIndexOf("\\", path.length() - 2);
        path.delete(index + 1, path.length());
        loadFile();
    }

    /**
     * @author Dzy
     * @date 2020/7/4 16:25
     * @description 鼠标监听事件
     * @param event 鼠标监听对象
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
     * @date 2020/7/4 16:25
     * @description 加载文件信息
     */
    private void loadFile() {
        System.out.println(path);
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
                    String fileName = f.getName();
                    long lastModified = f.lastModified();
                    Date date = new Date(lastModified);
                    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                    String fileTime = sdf.format(date);
                    if(f.isDirectory()) {
                        observableList.add(new FileInformation(fileName, fileTime, null));
                    } else {
                        String fileSize = f.length() + "";
                        observableList.add(new FileInformation(fileName, fileTime, fileSize));
                    }
                }
                tableView.setItems(observableList);
            } else {
                String msg = "该文件不是文件夹！";
                PromptWindowsController.showPromptWindows(msg);
                goBackUp();
            }
        }
    }


    /**
     * @author Dzy
     * @date 2020/7/4 16:25
     * @description 文件大小格式转换
     * @param fileS 文件长度
     */
    public String FormetFileSize(long fileS) {
        DecimalFormat df = new DecimalFormat("#.00");
        String fileSizeString = "";
        if (fileS < 1024) {
            fileSizeString = df.format((double) fileS) + "B";
        } else if (fileS < 1048576) {
            fileSizeString = df.format((double) fileS / 1024) + "K";
        } else if (fileS < 1073741824) {
            fileSizeString = df.format((double) fileS / 1048576) + "M";
        } else {
            fileSizeString = df.format((double) fileS / 1073741824) +"G";
        }
        return fileSizeString;
    }

}
