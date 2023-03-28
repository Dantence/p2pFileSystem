package cache;/**
 * @author Dantence
 * @description:
 * @date 2023/3/23
 */

import common.util.FileScanner;
import common.util.PropertyParser;
import thread.BroadcastReceiverThread;
import thread.Server;
import thread.ServerThread;

import java.io.IOException;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.*;
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
        localHost = getLocalHostExactAddress();
    }

    public static String getLocalHostExactAddress() {
        try {
            Enumeration<NetworkInterface> interfaces = NetworkInterface.getNetworkInterfaces();
            while (interfaces.hasMoreElements()) {
                NetworkInterface current = interfaces.nextElement();
                if (!current.isUp() || current.isLoopback() || current.isVirtual()) {
                    continue;
                }
                Enumeration<InetAddress> addresses = current.getInetAddresses();
                while (addresses.hasMoreElements()) {
                    InetAddress currentAddr = addresses.nextElement();
                    if (currentAddr.isLoopbackAddress()) {
                        continue;
                    }
                    if (currentAddr.getHostAddress().contains(":")) {
                        // IPv6 address
                        continue;
                    }
                    if (currentAddr.isSiteLocalAddress()) {
                        return currentAddr.getHostAddress();
                    }

                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
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

    public static Server serverThread;

    public static Map<String, Socket> serverThreadMap = new HashMap<>();

    public static boolean checkThread(String ip) {
        return serverThreadMap.containsKey(ip);
    }

    public static void stopAllThread() {
        broadcastReceiverThread.stop();
        for(String ip : serverThreadMap.keySet()) {
            try {
                serverThreadMap.get(ip).close();
            } catch (Exception e) {
                e.printStackTrace();
            }

        }

        try {
            serverThread.stop();
        } catch (IOException e) {
            e.printStackTrace();
        }
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
