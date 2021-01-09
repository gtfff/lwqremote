package utils;

import controller.MainScreenController;
import controller.PromptWindowsController;
import controller.SelectPathScreenController;
import javafx.application.Platform;

import java.io.*;
import java.net.Socket;
import java.net.UnknownHostException;


/**
 * @author Dzy
 * @version 1.0
 * @date 2020/7/4 16:02
 * @description 客户端
 */
public class Client {
    private Socket client;
    private DataOutputStream dos;

    public Client() {
        System.out.println("-----Client------");
        //建立连接：使用Socket创建客户端 + 服务的地址和端口
        try { 
        	  System.out.println("this.client===="+this.client);
        	  Socket s =   new Socket("localhost", 8888);
        	  System.out.println("s===="+s);
            this.client = s;
            System.out.println("this.client===="+this.client);
          //  System.out.println(client.getOutputStream());
            this.dos = new DataOutputStream(client.getOutputStream());           
          //  System.out.println("this.dos===="+this.dos);
            new Thread(new Receive(client)).start();
        }catch(UnknownHostException e) {
        	System.out.println("UnknownHostException-----------------");
            e.printStackTrace();
        } 
        catch (IOException e) {
        	System.out.println("拒绝");
            e.printStackTrace();
        }
    }

    /**
     * @author Dzy
     * @date 2020/7/4 16:36
     * @description 向服务器发送消息
     * @param msg 客户端传递过来的信息
     */
    public void send(String msg) {
        try {
        	System.out.println("msg:"+msg);
        	System.out.println(this.dos);
            this.dos.writeUTF(msg);
            this.dos.flush();
        } catch (IOException e) {
        	System.out.println("IOException---------------");
            e.printStackTrace();
        }
    }

    /**
     * @author Dzy
     * @date 2020/7/4 16:37
     * @description 向服务器上传文件
     * @param userId 用户Id
     * @param fileName 文件名称
     * @param path 文件路径
     * @param uploadPath 上传路径
     */
    public void sendFile(String userId, String fileName, String path, String uploadPath) {
        //弹出提示窗口
        PromptWindowsController.showPromptWindows("开始上传");
        File file = new File(path + fileName);
        send("uploadFile|" + userId + "|" + fileName + "|" + file.length() + "|" + uploadPath);
        BufferedInputStream bis = null;
        try {
            bis = new BufferedInputStream(new FileInputStream(file));
            byte[] bytes = new byte[1024];
            int len = -1;
            while((len = bis.read(bytes)) != -1) {
                dos.write(bytes, 0, len);
                dos.flush();
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if(bis != null) {
                try {
                    bis.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    /**
     * @author Dzy
     * @date 2020/7/4 16:37
     * @description 提醒服务器该用户已退出登录
     * @param userId 用户Id
     */
    public void close(String userId) {
        send("close|" + userId);
    }


    static class Receive implements Runnable {
        private DataInputStream dis;

        public Receive(Socket client) {
            try {
                this.dis = new DataInputStream(client.getInputStream());
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        /**
         * @author Dzy
         * @date 2020/7/4 16:37
         * @description 接收服务器转发过来的数据
         */
        public String receive() {
            String msg = null;
            try {
                msg = this.dis.readUTF();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return msg;
        }


        @Override
        public void run() {
            while(true) {
                String msg = receive();
                if(msg != null) {
                    String[] arr = msg.split("\\|");
                    if(arr[0].equals("getPath")) {
                        //如何将路径传输到主界面里面去
                        MainScreenController.url = arr[1];
                    } else if(arr[0].equals("uploadSuccessful")) {
                        //提示上传成功
                        Platform.runLater(new Runnable() {
                            @Override
                            public void run() {
                                PromptWindowsController.showPromptWindows("上传成功");
                                //刷新主界面
                                MainScreenController mainScreenController = (MainScreenController) Context.contorllersMap.get(MainScreenController.class.getSimpleName());
                                mainScreenController.refreshInterface();
                            }
                        });

                    } else if(arr[0].equals("downloadFile")) {
                        String fileName = arr[1];
                        long fileLength = Long.parseLong(arr[2]);
                        BufferedOutputStream bos = null;
                        try {
                            bos = new BufferedOutputStream(new FileOutputStream(SelectPathScreenController.savePath + "\\" + fileName));
                            byte[] bytes = new byte[1024];
                            int len = -1;
                            long count = 0;
                            while((len = dis.read(bytes)) != -1) {
                                bos.write(bytes, 0, len);
                                bos.flush();
                                count += len;
                                //文件接受完成
                                if(count == fileLength) {
                                    break;
                                }
                            }
                        } catch (IOException e) {
                            e.printStackTrace();
                        } finally {
                            if(bos != null) {
                                try {
                                    bos.close();
                                } catch (IOException e) {
                                    e.printStackTrace();
                                }
                            }
                        }
                        //提示下载完成
                        Platform.runLater(new Runnable() {
                            @Override
                            public void run() {
                                PromptWindowsController.showPromptWindows("下载完成");
                            }
                        });
                    }
                }
            }
        }
    }
}
