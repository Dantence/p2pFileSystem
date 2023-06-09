package service;/**
 * @author Dantence
 * @description:
 * @date 2023/3/26
 */

import common.message.Message;
import common.message.MessageType;
import common.util.PropertyParser;
import common.util.SocketPool;

import java.io.ObjectOutputStream;
import java.net.Socket;

/**
 * @projectName: p2pFileSystem
 * @package: service
 * @className: DownloadFileService
 * @author: Dantence
 * @description: 文件下载服务
 * @date: 2023/3/26 1:01
 * @version: 1.0
 */
public class DownloadFileService {
    public void downloadFile(String sender, String receiver, String fileName, String dest) {
        Message message = new Message();
        message.setSender(sender);
        message.setReceiver(receiver);
        message.setSrc(fileName);
        message.setDest(dest);
        message.setMessageType(MessageType.FILE_DOWNLOAD_REQ);
        Socket socket = null;
        try {
            socket = SocketPool.getSocket(receiver, PropertyParser.getPort());
            ObjectOutputStream oos = new ObjectOutputStream(socket.getOutputStream());
            oos.writeObject(message);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if(socket != null) {
                SocketPool.offer(socket);
            }
        }

    }
}
