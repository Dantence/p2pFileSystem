package main;

import cache.Cache;
import cache.CacheUpdate;
import common.message.MessageType;
import service.BroadcastSenderService;
import thread.*;
import window.ClientWindow;

public class Main {
    public static void main(String[] args) throws Exception {
        // 启动广播包接收线程
        Cache.broadcastReceiverThread = new BroadcastReceiverThread();
        Thread broadcastReceiverThread = new Thread(Cache.broadcastReceiverThread);
        broadcastReceiverThread.setDaemon(true);
        broadcastReceiverThread.start();

        // 启动服务端线程
        Cache.serverThread = new Server();
        Thread serverThread = new Thread(Cache.serverThread);
        serverThread.setDaemon(true);
        serverThread.start();

        // 启动客户端线程
        // new Thread(new ClientThread()).start();

        // 节点上线发送广播包
        BroadcastSenderService.sendMessage(MessageType.BROADCAST_LOGIN_REQ);

        // 启动图形界面
        new ClientWindow();

        // 异步缓存更新及心跳监测线程
        CacheUpdate cacheUpdate = new CacheUpdate();
        cacheUpdate.start();
    }
}
