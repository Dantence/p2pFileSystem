package thread;/**
 * @author Dantence
 * @description:
 * @date 2023/3/23
 */

import cache.Cache;
import common.dto.BroadcastLoginDTO;
import common.message.Message;
import common.message.MessageType;
import common.util.*;

import java.io.ObjectOutputStream;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.Socket;
import java.util.List;

/**
 * @projectName: p2pFileSystem
 * @package: service
 * @className: BroadcastReceiverThread
 * @author: Dantence
 * @description: 循环接收其他节点的信息
 * @date: 2023/3/23 20:43
 * @version: 1.0
 */
public class BroadcastReceiverThread implements Runnable {
    private boolean loop = true;

    public void stop() {
        loop = false;
        socket.close();
    }

    public DatagramSocket socket = null;

    @Override
    public void run() {
        int port = PropertyParser.getPort();

        try {
            socket = new DatagramSocket(port);
            byte[] buffer = new byte[1024];
            while (loop) {

                // 接收广播数据
                DatagramPacket packet = new DatagramPacket(buffer, buffer.length);
                socket.receive(packet);

                // 将字节数组转换为对象
                Message message = MessageUtil.resolvePacket(packet);
                // 解析广播数据
                String ip = message.getBroadcastLoginDTO().getIp();
                List<String> resources = message.getBroadcastLoginDTO().getResources();

                if (!CommonUtil.checkSelf(ip)) {
                    if (message.getMessageType().equals(MessageType.BROADCAST_LOGIN_REQ)) {
                        // 在缓存中增加节点
                        Cache.add(ip, resources);

                        System.out.println("\nip " + ip + "上线");

                        // 发送响应数据
                        Message response = new Message();
                        BroadcastLoginDTO broadcastLoginDTO = new BroadcastLoginDTO();
                        broadcastLoginDTO.setIp(Cache.localHost);
                        broadcastLoginDTO.setResources(FileScanner.getAllFiles(PropertyParser.getShareRoot()));
                        response.setBroadcastLoginDTO(broadcastLoginDTO);
                        response.setMessageType(MessageType.BROADCAST_LOGIN_RESP);

                        Socket tcpSocket = SocketPool.getSocket(ip, port);

                        ObjectOutputStream oos = new ObjectOutputStream(tcpSocket.getOutputStream());
                        oos.writeObject(response);
                        //MessageUtil.sendMessage(socket, response, packet.getAddress(), packet.getPort());
                    } else if (message.getMessageType().equals(MessageType.BROADCAST_LOGOUT_REQ)) {
                        System.out.println("\nip " + ip + "下线");
                        // 从缓存中清除下线节点
                        Cache.clear(ip);
                        if(Cache.checkThread(ip)) {
                            Message response = new Message();
                            response.setSender(Cache.localHost);
                            response.setReceiver(ip);
                            response.setMessageType(MessageType.BROADCAST_LOGOUT_RESP);
                            Socket tcpSocket = SocketPool.getSocket(ip, port);
                            ObjectOutputStream oos = new ObjectOutputStream(tcpSocket.getOutputStream());
                            oos.writeObject(response);
                        }
                    }
                }

            }
        } catch (Exception e) {

        } finally {
            socket.close();
        }

    }
}
