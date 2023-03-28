package main;

import cache.Cache;
import common.message.MessageType;
import thread.BroadcastSenderThread;
import thread.BroadcastReceiverThread;
import thread.Server;
import thread.ServerThread;
import window.FileUploader;

import java.io.File;

public class Main {
    public static void main(String[] args) throws Exception {
        // 启动广播包接收线程
        Cache.broadcastReceiverThread = new BroadcastReceiverThread();
        Thread thread1 = new Thread(Cache.broadcastReceiverThread);
        thread1.setDaemon(true);
        thread1.start();

        // 启动服务线程
        Cache.serverThread = new Server();
        Thread thread = new Thread(Cache.serverThread);
        thread.setDaemon(true);
        thread.start();

        // 节点上线发送广播包
        new Thread(new BroadcastSenderThread(MessageType.BROADCAST_LOGIN_REQ)).start();

        // 启动图形界面
        new FileUploader();
    }
}
