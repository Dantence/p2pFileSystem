package thread;/**
 * @author Dantence
 * @description:
 * @date 2023/3/23
 */

import cache.Cache;
import common.message.MessageType;
import common.util.CommonUtil;
import common.util.SocketPool;
import service.DownloadFileService;
import service.SendFileService;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Scanner;

/**
 * @projectName: p2pFileSystem
 * @package: service
 * @className: ClientThread
 * @author: Dantence
 * @description: TODO
 * @date: 2023/3/23 21:42
 * @version: 1.0
 */
public class ClientThread implements Runnable {
    private boolean loop = true;
    private static final Scanner scanner = new Scanner(System.in);

    private SendFileService sendFileService = new SendFileService();

    private DownloadFileService downloadFileService = new DownloadFileService();

    private void mainMenu() throws UnknownHostException {
        while (loop) {
            System.out.println("================欢迎登录p2p局域网文件传输系统================");
            System.out.println("\t\t 1 显示在线ip列表");
            System.out.println("\t\t 2 发送文件");
            System.out.println("\t\t 3 群发文件");
            System.out.println("\t\t 4 查看局域网内共享的文件");
            System.out.println("\t\t 5 下载局域网内共享的文件");
            System.out.println("\t\t 9 退出系统");
            System.out.print("请输入你的选择:");
            String op = scanner.next();
            switch (op) {
                case "1":
                    for (String ip : Cache.ipTable) {
                        System.out.println(ip);
                    }

                    System.out.println();
                    break;
                case "2":
                    System.out.println("请输入要发送的ip地址: ");
                    String ip = scanner.next();
                    if (CommonUtil.checkSelf(ip)) {
                        System.out.println("不能发送给自己");
                        continue;
                    }
                    System.out.println("请输入要发送的文件名: ");
                    String file = scanner.next();
                    sendFileService.sendFile(file, Cache.localHost, ip);
                    break;
                case "3":
                    System.out.println("请输入要发送的文件名: ");
                    file = scanner.next();
                    for (String address : Cache.ipTable) {
                        if(!CommonUtil.checkSelf(address)) {
                            sendFileService.sendFile(file, Cache.localHost, address);
                        }
                    }
                    break;
                case "4":
                    Cache.printResources();
                    break;
                case "5":
                    System.out.println("请输入要下载的文件名: ");
                    file = scanner.next();
                    System.out.println("请输入要下载到的地址: ");
                    String dest = scanner.next();
                    String receiver = Cache.getAvailableIp(file);
                    if (receiver == null) {
                        System.out.println("该文件在局域网中不存在");
                        continue;
                    }
                    downloadFileService.downloadFile(Cache.localHost, receiver, file, dest);
                    break;
                case "9":
                    //new Thread(new BroadcastSenderThread(MessageType.BROADCAST_LOGOUT_REQ)).start();
                    //Cache.stopAllThread();
                    loop = false;
                    //SocketPool.closeAll();
                    //System.exit(0);
                    break;

            }

        }
    }

    @Override
    public void run() {
        try {
            mainMenu();
        } catch (UnknownHostException e) {
            e.printStackTrace();
        }
    }
}
