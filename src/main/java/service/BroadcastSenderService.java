package service;/**
 * @author Dantence
 * @description:
 * @date 2023/3/29
 */

import cache.Cache;
import common.dto.BroadcastLoginDTO;
import common.message.Message;
import common.util.FileScanner;
import common.util.MessageUtil;
import common.util.PropertyParser;

import java.net.DatagramSocket;
import java.net.InetAddress;

/**
 * @projectName: p2pFileSystem
 * @package: service
 * @className: BroadcastSenderService
 * @author: Dantence
 * @description: 广播服务
 * @date: 2023/3/29 15:15
 * @version: 1.0
 */
public class BroadcastSenderService {
    public static void sendMessage(String messageType) {
        DatagramSocket socket = null;
        try {
            socket = new DatagramSocket();
            socket.setBroadcast(true);
            Message message = new Message();
            BroadcastLoginDTO broadcastLoginDTO = new BroadcastLoginDTO();
            broadcastLoginDTO.setIp(Cache.localHost);
            broadcastLoginDTO.setResources(FileScanner.getAllFiles(PropertyParser.getShareRoot()));
            message.setBroadcastLoginDTO(broadcastLoginDTO);
            message.setMessageType(messageType);
            MessageUtil.sendBroadcastMessage(socket, message, InetAddress.getByName("255.255.255.255"), PropertyParser.getPort());
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if(socket != null) {
                socket.close();
            }
        }
    }
}
