package common.util;/**
 * @author Dantence
 * @description:
 * @date 2023/3/24
 */

import java.io.FileInputStream;
import java.io.InputStream;
import java.util.Properties;

/**
 * @projectName: p2pFileSystem
 * @package: common.util
 * @className: PropertyParser
 * @author: Dantence
 * @description: TODO
 * @date: 2023/3/24 0:34
 * @version: 1.0
 */
public class PropertyParser {

    private static final Properties properties = new Properties();

    static {
        FileInputStream inputStream = null;
        try {
            inputStream = new FileInputStream("settings/setting.properties");
            properties.load(inputStream);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (inputStream != null) {
                try {
                    inputStream.close();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public static int getPort() {
        return Integer.parseInt(properties.getProperty("port"));
    }

    public static String getShareRoot() {
        return properties.getProperty("share_root");
    }

    public static String getDownloadRoot() {
        return properties.getProperty("download_root");
    }
}
