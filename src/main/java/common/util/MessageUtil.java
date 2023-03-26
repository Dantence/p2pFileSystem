package common.util;/**
 * @author Dantence
 * @description:
 * @date 2023/3/23
 */
import common.message.Message;

import java.io.*;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;

/**
 * @projectName: p2pFileSystem
 * @package: common.util
 * @className: MessageUtil
 * @author: Dantence
 * @description: TODO
 * @date: 2023/3/23 14:24
 * @version: 1.0
 */
public class MessageUtil {
    public static void sendBroadcastMessage(DatagramSocket socket, Message message, InetAddress address, int port) throws IOException {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        ObjectOutputStream oos = new ObjectOutputStream(baos);
        oos.writeObject(message);
        byte[] sendData = baos.toByteArray();
        DatagramPacket sendPacket = new DatagramPacket(sendData, sendData.length, address, port);
        socket.send(sendPacket);
        baos.close();
        oos.close();
    }

    public static Message resolvePacket(DatagramPacket packet) throws IOException, ClassNotFoundException {
        ByteArrayInputStream bais = new ByteArrayInputStream(packet.getData());
        ObjectInputStream ois = new ObjectInputStream(bais);
        return (Message) ois.readObject();
    }
}
