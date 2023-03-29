package cache;/**
 * @author Dantence
 * @description:
 * @date 2023/3/23
 */

import thread.BroadcastReceiverThread;
import thread.Server;

import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.Socket;
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

    public static BroadcastReceiverThread broadcastReceiverThread;

    public static Server serverThread;

    public static Map<String, Socket> serverThreadMap = new HashMap<>();

    public static boolean checkThread(String ip) {
        return serverThreadMap.containsKey(ip);
    }


    public static void add(String ip, List<String> resources) {
        ipTable.add(ip);
        ipRecourceMap.put(ip, resources);
    }

    public static void clear(String ip) {
        ipTable.remove(ip);
        ipRecourceMap.remove(ip);
    }

}
