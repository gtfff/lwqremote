package utils;


import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;


/**
 * @author Dzy
 * @version 1.0
 * @class Server
 * @date 2020/7/4 16:02
 * @description 服务器
 */
public class Server {
    private static Map<String, Channel> administrationMap = new ConcurrentHashMap<>();

    public static void main(String[] args) {
        System.out.println("-----Server------");
        ServerSocket server = null;
        try {
            server = new ServerSocket(8888);
            while (true) {
                //接收一个登录的客户端
                Socket client = server.accept();
                Channel channel = new Channel(client);
                System.out.println("一个客户端建立了连接");
                new Thread(channel).start();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    static class Channel implements Runnable {
        private DataInputStream dis;
        private DataOutputStream dos;
        private Socket client;
        private boolean isRunning;
        private String userId;

        public Channel(Socket client) {
            try {
                this.client = client;
                this.dis = new DataInputStream(client.getInputStream());
                this.dos = new DataOutputStream(client.getOutputStream());
                this.isRunning = true;
                this.userId = receive();
                administrationMap.put(this.userId, this);
                //创建该用户存储文件的文件夹
                File file = new File("disk/" + this.userId);
                if(!file.exists()) {
                    file.mkdir();
                }
            } catch (IOException e) {
                release();
            }
        }

        /**
         * @author Dzy
         * @date 2020/7/4 16:41
         * @description 发送消息给客户端
         * @param msg 服务器发送过来的消息
         */
        private void send(String msg) {
            try {
                this.dos.writeUTF(msg);
                this.dos.flush();
            } catch (IOException e) {
                release();
            }
        }

        /**
         * @author Dzy
         * @date 2020/7/4 16:41
         * @description 接收客户端发送过来的消息
         * @return String
         */
        private String receive() {
            String msg = null;
            try {
                msg = this.dis.readUTF();
            } catch (IOException e) {
                release();
            }
            return msg;
        }

        @Override
        public void run() {
            while(this.isRunning) {
                String msg = receive();
                System.out.println(msg);
                if (msg != null) {
                    String[] arr = msg.split("\\|");
                    if(arr[0].equals("close")) {
                        String userId = arr[1];
                        //如果已登录，就从map中移除
                        if(administrationMap.containsKey(userId)) {
                            administrationMap.remove(userId);
                        }
                    } else if(arr[0].equals("getPath")) {
                        String userId = arr[1];
                        File file = new File("disk");
                        send("getPath|" + file.getAbsolutePath() + "\\" + userId + "\\");
                    } else if(arr[0].equals("uploadFile")) {
                        String userId = arr[1];
                        String fileName = arr[2];
                        long fileLength = Long.parseLong(arr[3]);
                        String uploadPath = arr[4];
                        BufferedOutputStream bos = null;
                        String path = null;
                        if(uploadPath.equals("null")) {
                            path = "disk\\"+ userId + "\\" + fileName;
                        } else {
                            path = "disk\\"+ userId + "\\" + uploadPath + "\\" + fileName;
                        }
                        try {
                            bos = new BufferedOutputStream(new FileOutputStream(path));
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
                            administrationMap.get(userId).send("uploadSuccessful");
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
                    } else if(arr[0].equals("downloadFile")) {
                        String userId = arr[1];
                        String downloadPath = arr[2];
                        //实现文件传输
                        File file = new File("disk\\"+ userId + "\\" + downloadPath);
                        administrationMap.get(userId).send("downloadFile|" + file.getName() + "|" + file.length());
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
                }
            }
        }

        //释放资源
        private void release() {
            System.out.println("一个客户端断开了连接");
            this.isRunning = false;
            try {
                if (this.client == null) {
                    this.client.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }

            try {
                if (this.dis == null) {
                    this.dis.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }

            try {
                if (this.dos == null) {
                    this.dos.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }


}
