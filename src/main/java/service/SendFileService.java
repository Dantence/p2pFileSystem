package service;/**
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

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.net.UnknownHostException;

/**
 * @projectName: p2pFileSystem
 * @package: service
 * @className: SendFileService
 * @author: Dantence
 * @description: 文件发送服务
 * @date: 2023/3/23 22:08
 * @version: 1.0
 */
public class SendFileService {
    public void sendFile(String src, String sender, String receiver) {

        Message message = new Message();
        message.setMessageType(MessageType.FILE_SEND);
        message.setSrc(src);
        message.setSender(sender);
        message.setReceiver(receiver);
        byte[] fileBytes = CommonUtil.getFileBytes(PropertyParser.getShareRoot() + "/" + src);
        message.setFileBytes(fileBytes);
        Socket socket = null;
        try {
            socket = SocketPool.getSocket(receiver, PropertyParser.getPort());
            ObjectOutputStream oos = new ObjectOutputStream(socket.getOutputStream());
            oos.writeObject(message);
            //oos.flush();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if(socket != null) {
                SocketPool.offer(socket);
            }
        }

    }
}
