package broadcast;

import cache.Cache;
import common.message.MessageType;
import thread.BroadcastSenderThread;
import thread.BroadcastReceiverThread;
import thread.ServerThread;

import java.util.HashMap;
import java.util.List;

public class BroadcastClient {
    public static HashMap<String, List<String>> onlineClientList;
    public static void main(String[] args) throws Exception {
        // 启动广播包接收线程
        Cache.broadcastReceiverThread = new BroadcastReceiverThread();
        new Thread(Cache.broadcastReceiverThread).start();

        // 启动服务线程
        Cache.serverThread = new ServerThread();
        new Thread(Cache.serverThread).start();

        // 节点上线发送广播包
        new Thread(new BroadcastSenderThread(MessageType.BROADCAST_LOGIN_REQ)).start();


//        while(true) {
//            System.out.println("waiting...");
//            Thread.sleep(100);
//        }
//        DatagramSocket socket = new DatagramSocket();
//        socket.setBroadcast(true);
//
//        BroadcastLoginMessage message = new BroadcastLoginMessage();
//        BroadcastLoginDTO broadcastLoginDTO = new BroadcastLoginDTO();
//        broadcastLoginDTO.setIp(InetAddress.getLocalHost().getHostAddress());
//        broadcastLoginDTO.setResources(FileScanner.getAllFiles("D:\\test"));
//        message.setBroadcastLoginDTO(broadcastLoginDTO);
//
//        MessageUtil.sendMessage(socket, message, InetAddress.getByName("255.255.255.255"), 11223);
//
//
//        byte[] receiveData = new byte[1024];
//        DatagramPacket receivePacket = new DatagramPacket(receiveData, receiveData.length);
//        // 设置超时时间为5秒
//        socket.setSoTimeout(5000);
//
//        while (true) {
//            try {
//                socket.receive(receivePacket);
//                ByteArrayInputStream bais = new ByteArrayInputStream(receivePacket.getData());
//                ObjectInputStream ois = new ObjectInputStream(bais);
//                BroadcastLoginMessage response = (BroadcastLoginMessage) ois.readObject();
//
//                //onlineClientList.put(response, null);
//                System.out.println("Received response from: " + response.getBroadcastLoginDTO().getIp());
//                receivePacket.setLength(receiveData.length);
//            } catch (SocketTimeoutException e) {
//                // 超时，已经接收到所有应答，跳出循环
//                break;
//            }
//        }
//        socket.close();
    }


}
