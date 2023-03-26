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

    private boolean loop = true;
    private ServerSocket serverSocket = null;

    public void stop() {
        loop = false;
    }

    @Override
    public void run() {
        int port = PropertyParser.getPort();
        try {
            serverSocket = new ServerSocket(port);
            while (loop) {
                Socket socket = serverSocket.accept();
                ObjectInputStream ois = new ObjectInputStream(socket.getInputStream());
                Message message = (Message) ois.readObject();
                String receiver = message.getReceiver();
                if (!CommonUtil.checkSelf(receiver)) {
                    if (message.getMessageType().equals(MessageType.BROADCAST_LOGIN_RESP)) {
                        String ip = message.getBroadcastLoginDTO().getIp();
                        List<String> resources = message.getBroadcastLoginDTO().getResources();
                        Cache.add(ip, resources);
                    } else if (message.getMessageType().equals(MessageType.FILE_SEND)) {
                        System.out.println("收到来自ip " + message.getSender() + "的文件: " + message.getSrc());
                        FileOutputStream fileOutputStream = new FileOutputStream(message.getDest());
                        fileOutputStream.write(message.getFileBytes());
                        fileOutputStream.close();
                        System.out.println("\n 已保存文件至 " + message.getDest());
                    }
                }
                if (message.getMessageType().equals(MessageType.FILE_DOWNLOAD_REQ)) {
                    Message response = new Message();
                    response.setSender(Cache.localHost);
                    response.setReceiver(message.getSender());
                    response.setSrc(message.getSrc());
                    response.setDest(message.getDest());
                    response.setMessageType(MessageType.FILE_DOWNLOAD_RESP);

                    String src = PropertyParser.getRoot() + "/" + message.getSrc();
                    System.out.println("src: " + src);
                    byte[] fileBytes = CommonUtil.getFileBytes(src);
                    if (fileBytes == null) {
                        // TODO
                    } else {
                        response.setFileBytes(fileBytes);
                    }
                    Socket tcpSocket = SocketPool.getSocket(message.getSender(), PropertyParser.getPort());
                    ObjectOutputStream oos = new ObjectOutputStream(tcpSocket.getOutputStream());
                    oos.writeObject(response);
                }
                if(message.getMessageType().equals(MessageType.FILE_DOWNLOAD_RESP)) {
                    String dest = message.getDest() + "/" + message.getSrc();
                    FileOutputStream fileOutputStream = new FileOutputStream(dest);
                    fileOutputStream.write(message.getFileBytes());
                    fileOutputStream.close();
                    System.out.println("\n 已保存文件至 " + dest);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                serverSocket.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
