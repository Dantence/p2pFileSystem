package common.util;

import java.net.*;
import java.util.*;
/**
 * @projectName: p2pFileSystem
 * @package: common.util
 * @className: PropertyParser
 * @author: Dantence
 * @description: socket连接池
 * @date: 2023/3/24 14:37
 * @version: 1.0
 */
public class SocketPool {
    private static final int MAX_POOL_SIZE = 10; // 连接池最大容量
    private static final long TIMEOUT = 10000; // 连接池超时时间（毫秒）
    private static final ArrayList<Socket> pool = new ArrayList<Socket>();

    public static synchronized Socket getSocket(String host, int port) throws Exception {
        // 查找可用的socket
        for (int i = 0; i < pool.size(); i++) {
            Socket socket = pool.get(i);
            if (socket.getInetAddress().getHostName().equals(host) && socket.getPort() == port && !socket.isClosed()) {
                pool.remove(i);
                return socket;
            }
        }

        // 创建新的socket
        Socket socket = new Socket();
        socket.connect(new InetSocketAddress(host, port), (int) TIMEOUT);

        // 如果连接池已满，则关闭最早的连接
        if (pool.size() >= MAX_POOL_SIZE) {
            Socket oldestSocket = pool.remove(0);
            oldestSocket.close();
        }

        pool.add(socket);
        return socket;
    }

    public static synchronized void closeAll() {
        for (Socket socket : pool) {
            try {
                socket.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        pool.clear();
    }
}