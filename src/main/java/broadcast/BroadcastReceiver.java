//package broadcast;
//
//import common.dto.BroadcastLoginDTO;
//import common.message.Message;
//import common.util.FileScanner;
//import common.util.MessageUtil;
//
//import java.io.ByteArrayInputStream;
//import java.io.ByteArrayOutputStream;
//import java.io.ObjectInputStream;
//import java.io.ObjectOutputStream;
//import java.net.DatagramPacket;
//import java.net.DatagramSocket;
//import java.net.InetAddress;
//import java.util.List;
//
//public class BroadcastReceiver {
//    public static void main(String[] args) throws Exception {
//        // 设置广播地址和端口号
//        int port = 11223;
//        InetAddress broadcastAddress = InetAddress.getByName("255.255.255.255");
//
//        // 创建UDP socket
//        DatagramSocket socket = new DatagramSocket(port);
//
//        // 接收广播数据
//        byte[] buffer = new byte[1024];
//        DatagramPacket packet = new DatagramPacket(buffer, buffer.length);
//        socket.receive(packet);
//
//        // 将字节数组转换为对象
//        Message message = MessageUtil.resolvePacket(packet);
//
//        // 解析广播数据
//        System.out.println("Received broadcast: ");
//        System.out.println("ip: " + message.getBroadcastLoginDTO().getIp());
//        System.out.println("resources: " + message.getBroadcastLoginDTO().getResources());
//
//        // 发送响应数据
//        Message response = new Message();
//        BroadcastLoginDTO broadcastLoginDTO = new BroadcastLoginDTO();
//        broadcastLoginDTO.setIp("hello, I am " + InetAddress.getLocalHost().getHostAddress());
//        response.setBroadcastLoginDTO(broadcastLoginDTO);
//
//        //MessageUtil.sendMessage(socket, response, packet.getAddress(), packet.getPort());
//
//        // 关闭socket
//        socket.close();
//    }
//}