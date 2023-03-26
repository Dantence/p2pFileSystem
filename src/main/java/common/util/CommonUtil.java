package common.util;/**
 * @author Dantence
 * @description:
 * @date 2023/3/26
 */

import cache.Cache;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.InetAddress;
import java.net.UnknownHostException;

/**
 * @projectName: p2pFileSystem
 * @package: common.util
 * @className: CommonUtil
 * @author: Dantence
 * @description: TODO
 * @date: 2023/3/26 0:41
 * @version: 1.0
 */
public class CommonUtil {
    public static boolean checkSelf(String address) throws UnknownHostException {
        if ("127.0.0.1".equals(address)) {
            return true;
        }
        return Cache.localHost.equals(address);
    }

    public static byte[] getFileBytes(String src) {
        File file = new File(src);
        if (!file.exists()) {
            return null;
        }
        FileInputStream fileInputStream = null;
        byte[] fileBytes = new byte[(int) file.length()];
        try {
            fileInputStream = new FileInputStream(src);
            fileInputStream.read(fileBytes);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (fileInputStream != null) {
                try {
                    fileInputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return fileBytes;
    }
}
