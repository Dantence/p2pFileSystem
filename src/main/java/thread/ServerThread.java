package thread;/**
 * @author Dantence
 * @description:
 * @date 2023/3/23
 */

import cache.Cache;
import common.message.Message;
import common.message.MessageType;
import common.util.CommonUtil;
import common.util.PropertyParser;
import common.util.SocketPool;

import java.io.*;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.List;

/**
 * @projectName: p2pFileSystem
 * @package: thread
 * @className: ServerThread
 * @author: Dantence
 * @description: TODO
 * @date: 2023/3/23 22:50
 * @version: 1.0
 */
public class ServerThread implements Runnable {

    public Socket socket;

    private boolean loop = true;

    public void stop() throws IOException {
        loop = false;
        socket.close();
    }

    public ServerThread(Socket socket) {
        this.socket = socket;
    }

    @Override
    public void run() {
        try {
            while (loop) {
                ObjectInputStream ois = new ObjectInputStream(socket.getInputStream());
                Message message = (Message) ois.readObject();
                String sender = message.getSender();
                System.out.println("收到 ip " + sender + "的信息");
                Cache.serverThreadMap.put(sender, socket);
                if (!CommonUtil.checkSelf(sender)) {
                    switch (message.getMessageType()) {
                        case MessageType.BROADCAST_LOGIN_RESP:
                            String ip = message.getBroadcastLoginDTO().getIp();
                            List<String> resources = message.getBroadcastLoginDTO().getResources();
                            Cache.add(ip, resources);
                            break;
                        case MessageType.FILE_SEND: {
                            System.out.println("收到来自ip " + sender + "的文件: " + message.getSrc());
                            FileOutputStream fileOutputStream = new FileOutputStream(PropertyParser.getDownloadRoot() + "/" + message.getSrc());
                            fileOutputStream.write(message.getFileBytes());
                            fileOutputStream.close();
                            System.out.println("\n 已保存文件至 " + PropertyParser.getDownloadRoot() + "/" + message.getSrc());
                            break;
                        }
                        case MessageType.FILE_DOWNLOAD_REQ:
                            Message response = new Message();
                            response.setSender(Cache.localHost);
                            response.setReceiver(sender);
                            response.setSrc(message.getSrc());
                            response.setDest(message.getDest());
                            response.setMessageType(MessageType.FILE_DOWNLOAD_RESP);

                            String src = PropertyParser.getShareRoot() + "/" + message.getSrc();
                            System.out.println("文件 " + src + "被 " + sender + " 下载");
                            byte[] fileBytes = CommonUtil.getFileBytes(src);
                            if (fileBytes == null) {
                                // TODO
                            } else {
                                response.setFileBytes(fileBytes);
                            }
                            Socket tcpSocket = SocketPool.getSocket(sender, PropertyParser.getPort());
                            ObjectOutputStream oos = new ObjectOutputStream(tcpSocket.getOutputStream());
                            oos.writeObject(response);
                            break;
                        case MessageType.FILE_DOWNLOAD_RESP: {
                            String dest = message.getDest() + "/" + message.getSrc();
                            FileOutputStream fileOutputStream = new FileOutputStream(dest);
                            fileOutputStream.write(message.getFileBytes());
                            fileOutputStream.close();
                            System.out.println("\n 已保存文件至 " + dest);
                            break;
                        } case MessageType.BROADCAST_LOGOUT_RESP: {
                            System.out.println("\nlogout resp");
                            response = new Message();
                            response.setSender(Cache.localHost);
                            response.setReceiver(sender);
                            response.setMessageType(MessageType.CLOSE_REQ);
                            tcpSocket = SocketPool.getSocket(sender, PropertyParser.getPort());
                            oos = new ObjectOutputStream(tcpSocket.getOutputStream());
                            oos.writeObject(response);
                            break;
                        } case MessageType.CLOSE_REQ: {
                            System.out.println("\nclose req");
                            response = new Message();
                            response.setSender(Cache.localHost);
                            response.setReceiver(sender);
                            response.setMessageType(MessageType.CLOSE_RESP);
                            tcpSocket = SocketPool.getSocket(sender, PropertyParser.getPort());
                            oos = new ObjectOutputStream(tcpSocket.getOutputStream());
                            oos.writeObject(response);
                            socket.shutdownOutput();
                            socket.shutdownInput();
                            loop = false;
                            break;
                        } case MessageType.CLOSE_RESP: {
                            System.out.println("\nclose resp");
                            socket.shutdownOutput();
                            socket.shutdownInput();
                            loop = false;
                            break;
                        }
                    }
                }
            }
        } catch (Exception e) {

        } finally {
            try {
                socket.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
