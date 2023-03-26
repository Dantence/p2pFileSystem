package cache;/**
 * @author Dantence
 * @description:
 * @date 2023/3/23
 */

import common.util.FileScanner;
import common.util.PropertyParser;
import thread.BroadcastReceiverThread;
import thread.ServerThread;

import java.io.IOException;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @projectName: p2pFileSystem
 * @package: common.cache
 * @className: Cache
 * @author: Dantence
 * @description: TODO
 * @date: 2023/3/23 20:34
 * @version: 1.0
 */
public class Cache {
    public static ConcurrentHashMap<String, List<String>> ipRecourceMap = new ConcurrentHashMap<>();
    public static HashSet<String> ipTable = new HashSet<>();

    public static HashMap<String, Socket> socketPool = new HashMap<>();

    public static String localHost;

    static {
        try {
            localHost = InetAddress.getLocalHost().getHostAddress();
        } catch (UnknownHostException e) {
            e.printStackTrace();
        }
    }

    public static Socket getSocketByIp(String ip) throws IOException {
        if (socketPool.containsKey(ip)) {
            return socketPool.get(ip);
        } else {
            Socket socket = new Socket(ip, 11223);
            socketPool.put(ip, socket);
            return socket;
        }
    }

    public static BroadcastReceiverThread broadcastReceiverThread;

    public static ServerThread serverThread;

    public static void stopAllThread() {
        broadcastReceiverThread.stop();
        serverThread.stop();
    }

    public static void add(String ip, List<String> resources) {
        ipTable.add(ip);
        ipRecourceMap.put(ip, resources);
    }

    public static void clear(String ip) {
        ipTable.remove(ip);
        ipRecourceMap.remove(ip);
    }

    public static void printResources() {
        HashSet<String> fileSet = new HashSet<>();
        for (String ip : ipRecourceMap.keySet()) {
            fileSet.addAll(ipRecourceMap.get(ip));
        }
        for (String file : fileSet) {
            System.out.println(file);
        }
    }

    public static String getAvailableIp(String file) {
        for (String ip : ipRecourceMap.keySet()) {
            if (ipRecourceMap.get(ip).contains(file)) {
                return ip;
            }
        }
        return null;
    }
}
