package controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
import pojo.FileInformation;
import utils.Context;
import view.SelectPathScreen;
import view.UploadFileScreen;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.ResourceBundle;



/**
 * @author Dzy
 * @version 1.0
 * @date 2020/7/4 16:02
 * @description 主界面控制器
 */
public class MainScreenController implements Initializable {

    @FXML
    private TableView<FileInformation> tableView;

    @FXML
    private TableColumn fileNameColumn;

    @FXML
    private TableColumn fileTimeColumn;

    @FXML
    private TableColumn fileSizeColumn;


    private ObservableList<FileInformation> observableList = FXCollections.observableArrayList();
    public static String url = null;
    public static StringBuilder path = new StringBuilder();
    public static String uploadPath = "null";
    public static String downloadPath = "null";

    public MainScreenController() {
        Context.contorllersMap.put(this.getClass().getSimpleName(), this);
    }


    @Override
    public void initialize(URL location, ResourceBundle resources) {
        fileNameColumn.setCellValueFactory(new PropertyValueFactory("fileNameString"));
        fileTimeColumn.setCellValueFactory(new PropertyValueFactory("fileTimeString"));
        fileSizeColumn.setCellValueFactory(new PropertyValueFactory("fileSizeString"));
        //表格设置为可编辑
        tableView.setEditable(true);
        //给需要编辑的列设置属性
        fileNameColumn.setCellFactory(TextFieldTableCell.forTableColumn());
        Context.client.send("getPath|" + LoginScreenController.user.getUserId());
        while(url == null) {
        }
        //刷新主界面
        System.out.println("url:"+url);
        refreshInterface();
    }

    /**
     * @author Dzy
     * @date 2020/7/4 16:18
     * @description 打开选取路径窗口
     */
    @FXML
    void download() {
        FileInformation fileInformation = tableView.getSelectionModel().getSelectedItem();
        if(fileInformation != null) {
            File file = new File(url + path + fileInformation.getFileName());
            if(file.isFile()) {
                //获取当前选择的文件路径
                try {
                    downloadPath = path + fileInformation.getFileName();
                    new SelectPathScreen().start(new Stage());
                } catch (IOException e) {
                    e.printStackTrace();
                }
            } else {
                String msg = "该文件不是文件！";
                PromptWindowsController.showPromptWindows(msg);
            }
        }
    }

    /**
     * @author Dzy
     * @date 2020/7/4 16:18
     * @description 打开上传文件窗口
     */
    @FXML
    void upload() {
        FileInformation fileInformation = tableView.getSelectionModel().getSelectedItem();
        if(fileInformation != null) {
            File file = new File(url + path + fileInformation.getFileName());
            if(file.isDirectory()) {
                try {
                    uploadPath = path + fileInformation.getFileName();
                    new UploadFileScreen().start(new Stage());
                } catch (Exception e) {
                    e.printStackTrace();
                }
            } else {
                String msg = "该文件不是文件夹！";
                PromptWindowsController.showPromptWindows(msg);
            }
        } else if(fileInformation == null && path.length() == 0) {
            try {
                new UploadFileScreen().start(new Stage());
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

    }

    /**
     * @author Dzy
     * @date 2020/7/4 16:18
     * @description 双击监听事件
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
     * @date 2020/7/4 16:18
     * @description 返回上一级目录
     */
    @FXML
    void goBackUp() {
        if(path.length() == 0) {
           return;
        }
        int index = path.lastIndexOf("\\", path.length() - 2);
        path.delete(index + 1, path.length());
        loadFile();
    }


    /**
     * @author Dzy
     * @date 2020/7/4 16:18
     * @description 创建文件夹
     */
    @FXML
    void createDirectory() {
        File file = new File(url + path + "新建文件夹");
        file.mkdir();
        refreshInterface();
    }

    /**
     * @author Dzy
     * @date 2020/7/4 16:19
     * @description 删除文件
     */
    @FXML
    void deleteFile() {
        FileInformation fileInformation = tableView.getSelectionModel().getSelectedItem();
        if(fileInformation != null) {
            File file = new File(url + path + fileInformation.getFileName());
            if(file.isFile()) {
                file.delete();
                refreshInterface();
                PromptWindowsController.showPromptWindows("删除成功");
            } else {
                String msg = "该文件不是文件！";
                PromptWindowsController.showPromptWindows(msg);
            }
        }
    }

    /**
     * @author Dzy
     * @date 2020/7/4 16:19
     * @description 监听编辑完的信息，利用io就行修改操作
     * @param value 当前编辑对象
     */
    @FXML
    void editCommit(TableColumn.CellEditEvent value) {
        File file = new File(url + path + value.getOldValue());
        File newFile = new File(url + path + value.getNewValue());
        file.renameTo(newFile);
        refreshInterface();
    }

    /**
     * @author Dzy
     * @date 2020/7/4 16:19
     * @description 加载文件信息
     */
    private void loadFile() {
        File file = new File(url + path.toString());
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

    /**
     * @author Dzy
     * @date 2020/7/4 16:19
     * @description 刷新主页面
     */
    public void refreshInterface() {
        observableList.clear();
        File[] files = new File(url).listFiles();
        System.out.println("files:"+files);
        for(File file : files) {
            String fileName = file.getName();
            long lastModified = file.lastModified();
            Date date = new Date(lastModified);
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            String fileTime = sdf.format(date);
            if(file.isDirectory()) {
                observableList.add(new FileInformation(fileName, fileTime, null));
            } else {
                String fileSize = file.length() + "";
                observableList.add(new FileInformation(fileName, fileTime, fileSize));
            }
        }
        tableView.setItems(observableList);
    }
}
