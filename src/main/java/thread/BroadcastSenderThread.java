package thread;/**
 * @author Dantence
 * @description:
 * @date 2023/3/23
 */

import cache.Cache;
import common.dto.BroadcastLoginDTO;
import common.message.Message;
import common.message.MessageType;
import common.util.FileScanner;
import common.util.MessageUtil;
import common.util.PropertyParser;

import java.io.ByteArrayInputStream;
import java.io.ObjectInputStream;
import java.net.*;
import java.util.List;

/**
 * @projectName: p2pFileSystem
 * @package: service
 * @className: BroadcastSenderThread
 * @author: Dantence
 * @description: 节点上线后向局域网内其他节点广播自身ip和资源信息
 * @date: 2023/3/23 15:09
 * @version: 1.0
 */
public class BroadcastSenderThread implements Runnable {

    private String messageType;

    public BroadcastSenderThread(String messageType) {
        this.messageType = messageType;
    }

    public DatagramSocket sendMessage() throws Exception {
        DatagramSocket socket = new DatagramSocket();
        socket.setBroadcast(true);
        Message message = new Message();
        BroadcastLoginDTO broadcastLoginDTO = new BroadcastLoginDTO();
        broadcastLoginDTO.setIp(Cache.localHost);
        broadcastLoginDTO.setResources(FileScanner.getAllFiles(PropertyParser.getRoot()));
        message.setBroadcastLoginDTO(broadcastLoginDTO);
        message.setMessageType(messageType);

        MessageUtil.sendBroadcastMessage(socket, message, InetAddress.getByName("255.255.255.255"), PropertyParser.getPort());
        return socket;
    }

    @Override
    public void run() {
        try {
            DatagramSocket socket = sendMessage();
            switch (messageType) {
                case MessageType.BROADCAST_LOGIN_REQ: {
                    byte[] receiveData = new byte[1024];
                    DatagramPacket receivePacket = new DatagramPacket(receiveData, receiveData.length);
                    // 设置超时时间
                    socket.setSoTimeout(3000);

                    while (true) {
                        try {
                            socket.receive(receivePacket);
                            ByteArrayInputStream bais = new ByteArrayInputStream(receivePacket.getData());
                            ObjectInputStream ois = new ObjectInputStream(bais);
                            Message response = (Message) ois.readObject();
                            if(response.getMessageType().equals(MessageType.BROADCAST_LOGIN_RESP)) {
                                String ip = response.getBroadcastLoginDTO().getIp();
                                List<String> resources = response.getBroadcastLoginDTO().getResources();
                                Cache.add(ip, resources);
                                System.out.println("Received response from: " + response.getBroadcastLoginDTO().getIp());

                            }
                            receivePacket.setLength(receiveData.length);
                        } catch (SocketTimeoutException e) {
                            // 超时，已经接收到所有应答，跳出循环
                            break;
                        }
                    }
                    socket.close();
                    Cache.add(Cache.localHost, FileScanner.getAllFiles(PropertyParser.getRoot()));
                    new Thread(new ClientThread()).start();
                    break;
                }


            }

        } catch (Exception e) {
            e.printStackTrace();
        }

    }
}
