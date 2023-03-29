package thread;/**
 * @author Dantence
 * @description:
 * @date 2023/3/27
 */

import cache.Cache;
import common.util.PropertyParser;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * @projectName: p2pFileSystem
 * @package: thread
 * @className: Server
 * @author: Dantence
 * @description: TODO
 * @date: 2023/3/27 20:43
 * @version: 1.0
 */
public class Server implements Runnable{
    private boolean loop = true;
    public ServerSocket serverSocket = null;

    public void stop() throws IOException {
        loop = false;
        serverSocket.close();
    }

    @Override
    public void run() {
        int port = PropertyParser.getPort();
        try {
            serverSocket = new ServerSocket(port);
            while (loop) {
                Socket socket = serverSocket.accept();
                //System.out.println("Accepted connection from " + socket.getInetAddress().getHostAddress() + ":" + socket.getPort());
                new Thread(new ServerThread(socket)).start();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
